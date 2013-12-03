package com.google.android.speech.embedded;

import android.content.Context;
import android.util.Log;

import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.shared.api.Query;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.android.voicesearch.fragments.action.PhoneCallAction;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.speechservice.PumpkinActionConstants;
import com.google.android.voicesearch.util.AppSelectionHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.speech.grammar.pumpkin.ActionFrame;
import com.google.speech.grammar.pumpkin.Tagger;
import com.google.speech.grammar.pumpkin.UserValidators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PumpkinTagger {
    private static final PumpkinTaggerResultsProto.PumpkinTaggerResults EMPTY_RESULTS = new PumpkinTaggerResultsProto.PumpkinTaggerResults();
    private ActionFrame mActionFrame;
    private final AppSelectionHelper mAppSelectionHelper;
    private PumpkinAppValidator mAppValidator;
    private final ContactLookup mContactLookup;
    private PumpkinContactsValidator mContactsValidator;
    private final Context mContext;
    private ActionFrame mCurrentFrame;
    private ActionFrame mDisambiguationFrame;
    private PumpkinDummyValidator mDummyValidator;
    private final GsaConfigFlags mGsaConfigFlags;
    private final boolean mIsFollowOnEnabled;
    private final AndroidPumpkinLoader mPumpkinLoader;
    private int mPumpkinState;
    private final CoreSearchServices mServices;
    private ActionFrame mSetRecipientFrame;
    private Tagger mTagger;
    private final Executor mUiThread;
    private UserValidators mUserValidators;
    private final Executor mWorkerThread;

    public PumpkinTagger(Context paramContext, CoreSearchServices paramCoreSearchServices, boolean paramBoolean, Executor paramExecutor1, Executor paramExecutor2, AndroidPumpkinLoader paramAndroidPumpkinLoader, ContactLookup paramContactLookup, AppSelectionHelper paramAppSelectionHelper, GsaConfigFlags paramGsaConfigFlags) {
        this.mContext = paramContext;
        this.mServices = paramCoreSearchServices;
        this.mIsFollowOnEnabled = paramBoolean;
        this.mUiThread = paramExecutor1;
        this.mWorkerThread = paramExecutor2;
        this.mPumpkinLoader = paramAndroidPumpkinLoader;
        this.mContactLookup = paramContactLookup;
        this.mAppSelectionHelper = paramAppSelectionHelper;
        this.mGsaConfigFlags = paramGsaConfigFlags;
        this.mPumpkinState = 0;
    }

    private void init() {
        int i = 1;
        for (; ; ) {
            try {
                if (this.mPumpkinState == i) {
                    Preconditions.checkState(i);
                    EventLogger.recordLatencyStart(5);
                }
            } finally {
            }
            try {
                this.mPumpkinLoader.loadPumpkinConfigBytes();
                this.mPumpkinLoader.init();
                this.mActionFrame = this.mPumpkinLoader.createActionFrame(this.mPumpkinLoader.loadActionSetConfigBytes());
                this.mTagger = this.mPumpkinLoader.getTagger();
                this.mUserValidators = this.mPumpkinLoader.getUserValidators();
                this.mAppValidator = new PumpkinAppValidator(this.mAppSelectionHelper);
                this.mContactsValidator = new PumpkinContactsValidator(this.mServices, this.mContactLookup);
                this.mDummyValidator = new PumpkinDummyValidator();
                this.mUserValidators.addValidator("CONTACT", this.mContactsValidator);
                this.mUserValidators.addValidator("APP", this.mAppValidator);
                EventLogger.recordClientEvent(97);
            } catch (IOException localIOException) {
                try {
                    this.mPumpkinState = 3;
                    Log.e("PumpkinTagger", "Couldn't load configuration assets.");
                    return;
                } finally {
                }
            }
            try {
                this.mPumpkinState = 2;
                return;
            } finally {
            }
            int j = 0;
        }
    }

    private boolean isInActionFrame() {
        return (this.mActionFrame != null) && (this.mCurrentFrame == this.mActionFrame);
    }

    private boolean isInDisambiguationFrame() {
        return (this.mDisambiguationFrame != null) && (this.mCurrentFrame == this.mDisambiguationFrame);
    }

    private boolean isPumpkinMatch(PumpkinTaggerResultsProto.PumpkinTaggerResults paramPumpkinTaggerResults) {
        return (paramPumpkinTaggerResults != null) && (paramPumpkinTaggerResults.getHypothesisCount() > 0);
    }

    public static float moreTokensHigherProbability(String paramString) {
        return 1.0F / (1.0F + (float) Math.exp(-4 * paramString.split(" ").length));
    }

    private PumpkinTaggerResultsProto.PumpkinTaggerResults tagAlternates(@Nullable ImmutableList<CharSequence> paramImmutableList, int paramInt, VoicesearchClientLogProto.EmbeddedParserDetails paramEmbeddedParserDetails, ActionFrame paramActionFrame) {
        PumpkinTaggerResultsProto.PumpkinTaggerResults localPumpkinTaggerResults = null;
        if (paramInt > 0) {
            localPumpkinTaggerResults = null;
            if (paramImmutableList != null) {
                for (int i = 0; (i < paramInt) && (i < paramImmutableList.size()) && ((localPumpkinTaggerResults == null) || (localPumpkinTaggerResults.getHypothesisCount() == 0)); i++) {
                    localPumpkinTaggerResults = tag(((CharSequence) paramImmutableList.get(i)).toString(), paramActionFrame);
                    if (isPumpkinMatch(localPumpkinTaggerResults)) {
                        paramEmbeddedParserDetails.setHypothesisIndex(i + 1);
                    }
                }
            }
        }
        return localPumpkinTaggerResults;
    }

    private PumpkinTaggerResultsProto.PumpkinTaggerResults tagAlternatesConservative(String paramString, @Nullable ImmutableList<CharSequence> paramImmutableList, int paramInt, VoicesearchClientLogProto.EmbeddedParserDetails paramEmbeddedParserDetails, ActionFrame paramActionFrame) {
        this.mUserValidators.addValidator("CONTACT", this.mDummyValidator);
        this.mUserValidators.addValidator("APP", this.mDummyValidator);
        PumpkinTaggerResultsProto.PumpkinTaggerResults localPumpkinTaggerResults1 = tag(paramString, paramActionFrame);
        boolean bool1 = isPumpkinMatch(localPumpkinTaggerResults1);
        PumpkinTaggerResultsProto.PumpkinTaggerResults localPumpkinTaggerResults2 = null;
        if (bool1) {
            if (!localPumpkinTaggerResults1.getHypothesis(0).getActionName().equals("CallContact")) {
                boolean bool2 = localPumpkinTaggerResults1.getHypothesis(0).getActionName().equals("OpenApp");
                localPumpkinTaggerResults2 = null;
                if (!bool2) {
                }
            } else {
                localPumpkinTaggerResults2 = tagAlternates(paramImmutableList, paramInt, paramEmbeddedParserDetails, paramActionFrame);
            }
        }
        this.mUserValidators.addValidator("CONTACT", this.mContactsValidator);
        this.mUserValidators.addValidator("APP", this.mAppValidator);
        return localPumpkinTaggerResults2;
    }

    List<String> getSelectableContactTypes(List<Person> paramList, ContactLookup.Mode paramMode) {
        ArrayList localArrayList = Lists.newArrayList();
        if (paramMode == ContactLookup.Mode.PERSON) {
            return localArrayList;
        }
        Iterator localIterator1 = paramList.iterator();
        while (localIterator1.hasNext()) {
            Iterator localIterator2 = ((Person) localIterator1.next()).denormalizeContacts(paramMode).iterator();
            while (localIterator2.hasNext()) {
                String str1 = ((Contact) localIterator2.next()).getLabel(this.mContext.getResources());
                if (str1 != null) {
                    String str2 = str1.toLowerCase();
                    if (!localArrayList.contains(str2.toLowerCase())) {
                        localArrayList.add(str2.toLowerCase());
                    }
                }
            }
        }
    }

    List<String> getSelectablePersonNames(@Nonnull List<Person> paramList) {
        ArrayList localArrayList = Lists.newArrayListWithCapacity(paramList.size());
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            Person localPerson = (Person) localIterator.next();
            if (localPerson.hasName()) {
                String str1 = localPerson.getName().toLowerCase();
                if (!localArrayList.contains(str1)) {
                    localArrayList.add(str1);
                }
                for (String str2 : str1.split(" ")) {
                    if (!localArrayList.contains(str2)) {
                        localArrayList.add(str2);
                    }
                }
            }
        }
        return localArrayList;
    }

    public void handleQuery(Query paramQuery, SimpleCallback<TaggerResult> paramSimpleCallback1, SimpleCallback<TaggerResult> paramSimpleCallback2, SimpleCallback<TaggerResult> paramSimpleCallback3) {
        if (this.mIsFollowOnEnabled) {
            maybeUpdateValidators((DiscourseContext) this.mServices.getDiscourseContext().get());
        }
        if (Feature.TAG_N_BEST_HYPOTHESES.isEnabled()) {
        }
        for (int i = 4; ; i = this.mGsaConfigFlags.getPumpkinAlternatesToTag()) {
            tagAsync(paramQuery.getQueryString(), paramQuery.getOtherHypotheses(), i, paramSimpleCallback1, paramSimpleCallback2, paramSimpleCallback3);
            return;
        }
    }

    boolean isActionEnabled(String paramString) {
        int i = this.mGsaConfigFlags.getPumpkinEnabledActions();
        Integer localInteger = (Integer) PumpkinActionConstants.ACTION_FLAGS.get(paramString);
        if (localInteger == null) {
            Log.w("PumpkinTagger", "isActionEnabled() - unknown, assuming false: " + paramString);
        }
        while ((i & localInteger.intValue()) == 0) {
            return false;
        }
        return true;
    }

    public void maybeInit(final SimpleCallback<Boolean> paramSimpleCallback) {
        try {
            if (this.mPumpkinState == 0) {
                this.mPumpkinState = 1;
                this.mWorkerThread.execute(new Runnable() {
                    public void run() {
                        PumpkinTagger.this.init();
                        PumpkinTagger.this.mUiThread.execute(new Runnable() {
                            public void run() {
                                SimpleCallback localSimpleCallback = PumpkinTagger .1.
                                this.val$callback;
                                if (PumpkinTagger.this.mPumpkinState == 2) {
                                }
                                for (boolean bool = true; ; bool = false) {
                                    localSimpleCallback.onResult(Boolean.valueOf(bool));
                                    return;
                                }
                            }
                        });
                    }
                });
                return;
            }
            return;
        } finally {
        }
    }

    public void maybeUpdateValidators(DiscourseContext paramDiscourseContext) {
        if (paramDiscourseContext == null) {
        }
        for (CommunicationAction localCommunicationAction = null; localCommunicationAction == null; localCommunicationAction = paramDiscourseContext.getCurrentCommunicationAction()) {
            this.mCurrentFrame = this.mActionFrame;
            return;
        }
        PersonDisambiguation localPersonDisambiguation = localCommunicationAction.getRecipient();
        if (((localPersonDisambiguation == null) || (localPersonDisambiguation.hasNoResults())) && ((localCommunicationAction instanceof PhoneCallAction))) {
            if (this.mSetRecipientFrame == null) {
            }
            try {
                this.mSetRecipientFrame = this.mPumpkinLoader.createActionFrame(this.mPumpkinLoader.loadActionSelectRecipientSetConfigBytes());
                this.mCurrentFrame = this.mSetRecipientFrame;
                return;
            } catch (IOException localIOException2) {
                for (; ; ) {
                    Log.e("PumpkinTagger", "Couldn't load set recipient assets.");
                }
            }
        }
        if ((localPersonDisambiguation == null) || (!localPersonDisambiguation.isOngoing())) {
            this.mCurrentFrame = this.mActionFrame;
            return;
        }
        List localList1 = localPersonDisambiguation.getCandidates();
        if (this.mDisambiguationFrame == null) {
        }
        try {
            this.mDisambiguationFrame = this.mPumpkinLoader.createActionFrame(this.mPumpkinLoader.loadActionDisambigSetConfigBytes());
            List localList2 = getSelectablePersonNames(localList1);
            updateSelectionTypes(getSelectableContactTypes(localList1, localCommunicationAction.getSelectMode().getContactLookupMode()));
            updateSelectionNames(localList2);
            this.mCurrentFrame = this.mDisambiguationFrame;
            return;
        } catch (IOException localIOException1) {
            for (; ; ) {
                Log.e("PumpkinTagger", "Couldn't load disambiguation assets.");
            }
        }
    }

    /* Error */
    public PumpkinTaggerResultsProto.PumpkinTaggerResults tag(String paramString, ActionFrame paramActionFrame) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 71	com/google/android/speech/embedded/PumpkinTagger:mPumpkinState	I
        //   6: iconst_3
        //   7: if_icmpeq +7 -> 14
        //   10: aload_2
        //   11: ifnonnull +17 -> 28
        //   14: new 44	com/google/speech/grammar/pumpkin/PumpkinTaggerResultsProto$PumpkinTaggerResults
        //   17: dup
        //   18: invokespecial 47	com/google/speech/grammar/pumpkin/PumpkinTaggerResultsProto$PumpkinTaggerResults:<init>	()V
        //   21: astore 8
        //   23: aload_0
        //   24: monitorexit
        //   25: aload 8
        //   27: areturn
        //   28: aload_0
        //   29: getfield 71	com/google/android/speech/embedded/PumpkinTagger:mPumpkinState	I
        //   32: iconst_2
        //   33: if_icmpne +86 -> 119
        //   36: iconst_1
        //   37: istore 4
        //   39: iload 4
        //   41: invokestatic 123	com/google/common/base/Preconditions:checkState	(Z)V
        //   44: aload_0
        //   45: monitorexit
        //   46: bipush 6
        //   48: invokestatic 129	com/google/android/voicesearch/logger/EventLogger:recordLatencyStart	(I)V
        //   51: aload_0
        //   52: getfield 155	com/google/android/speech/embedded/PumpkinTagger:mUserValidators	Lcom/google/speech/grammar/pumpkin/UserValidators;
        //   55: astore 5
        //   57: aload 5
        //   59: monitorenter
        //   60: aload_0
        //   61: getfield 149	com/google/android/speech/embedded/PumpkinTagger:mTagger	Lcom/google/speech/grammar/pumpkin/Tagger;
        //   64: aload_1
        //   65: invokevirtual 312	java/lang/String:toLowerCase	()Ljava/lang/String;
        //   68: aload_2
        //   69: aload_0
        //   70: getfield 155	com/google/android/speech/embedded/PumpkinTagger:mUserValidators	Lcom/google/speech/grammar/pumpkin/UserValidators;
        //   73: invokevirtual 486	com/google/speech/grammar/pumpkin/Tagger:tag	(Ljava/lang/String;Lcom/google/speech/grammar/pumpkin/ActionFrame;Lcom/google/speech/grammar/pumpkin/UserValidators;)Lcom/google/speech/grammar/pumpkin/PumpkinTaggerResultsProto$PumpkinTaggerResults;
        //   76: astore 7
        //   78: aload 5
        //   80: monitorexit
        //   81: aload_0
        //   82: aload 7
        //   84: invokespecial 90	com/google/android/speech/embedded/PumpkinTagger:isPumpkinMatch	(Lcom/google/speech/grammar/pumpkin/PumpkinTaggerResultsProto$PumpkinTaggerResults;)Z
        //   87: ifeq +24 -> 111
        //   90: aload_0
        //   91: aload 7
        //   93: iconst_0
        //   94: invokevirtual 249	com/google/speech/grammar/pumpkin/PumpkinTaggerResultsProto$PumpkinTaggerResults:getHypothesis	(I)Lcom/google/speech/grammar/pumpkin/PumpkinTaggerResultsProto$HypothesisResult;
        //   97: invokevirtual 254	com/google/speech/grammar/pumpkin/PumpkinTaggerResultsProto$HypothesisResult:getActionName	()Ljava/lang/String;
        //   100: invokevirtual 488	com/google/android/speech/embedded/PumpkinTagger:isActionEnabled	(Ljava/lang/String;)Z
        //   103: ifne +8 -> 111
        //   106: getstatic 49	com/google/android/speech/embedded/PumpkinTagger:EMPTY_RESULTS	Lcom/google/speech/grammar/pumpkin/PumpkinTaggerResultsProto$PumpkinTaggerResults;
        //   109: astore 7
        //   111: bipush 96
        //   113: invokestatic 187	com/google/android/voicesearch/logger/EventLogger:recordClientEvent	(I)V
        //   116: aload 7
        //   118: areturn
        //   119: iconst_0
        //   120: istore 4
        //   122: goto -83 -> 39
        //   125: astore_3
        //   126: aload_0
        //   127: monitorexit
        //   128: aload_3
        //   129: athrow
        //   130: astore 6
        //   132: aload 5
        //   134: monitorexit
        //   135: aload 6
        //   137: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	138	0	this	PumpkinTagger
        //   0	138	1	paramString	String
        //   0	138	2	paramActionFrame	ActionFrame
        //   125	4	3	localObject1	Object
        //   37	84	4	bool	boolean
        //   130	6	6	localObject2	Object
        //   76	41	7	localPumpkinTaggerResults1	PumpkinTaggerResultsProto.PumpkinTaggerResults
        //   21	5	8	localPumpkinTaggerResults2	PumpkinTaggerResultsProto.PumpkinTaggerResults
        // Exception table:
        //   from	to	target	type
        //   2	10	125	finally
        //   14	25	125	finally
        //   28	36	125	finally
        //   39	46	125	finally
        //   126	128	125	finally
        //   60	81	130	finally
        //   132	135	130	finally
    }

    public void tagAsync(final String paramString, @Nullable final ImmutableList<CharSequence> paramImmutableList, final int paramInt, final SimpleCallback<TaggerResult> paramSimpleCallback1, final SimpleCallback<TaggerResult> paramSimpleCallback2, final SimpleCallback<TaggerResult> paramSimpleCallback3) {
        this.mUiThread.execute(new Runnable() {
            public void run() {
                VoicesearchClientLogProto.EmbeddedParserDetails localEmbeddedParserDetails = new VoicesearchClientLogProto.EmbeddedParserDetails();
                PumpkinTaggerResultsProto.PumpkinTaggerResults localPumpkinTaggerResults = PumpkinTagger.this.tag(paramString, PumpkinTagger.this.mCurrentFrame);
                if (!PumpkinTagger.this.isPumpkinMatch(localPumpkinTaggerResults)) {
                    if (!PumpkinTagger.this.isInActionFrame()) {
                        localPumpkinTaggerResults = PumpkinTagger.this.tag(paramString, PumpkinTagger.this.mActionFrame);
                        if (!PumpkinTagger.this.isPumpkinMatch(localPumpkinTaggerResults)) {
                            localPumpkinTaggerResults = PumpkinTagger.this.tagAlternates(paramImmutableList, 4, localEmbeddedParserDetails, PumpkinTagger.this.mCurrentFrame);
                        }
                        if (!PumpkinTagger.this.isPumpkinMatch(localPumpkinTaggerResults)) {
                            break label195;
                        }
                    }
                }
                label195:
                for (final TaggerResult localTaggerResult = new TaggerResult(localPumpkinTaggerResults.getHypothesis(0), localEmbeddedParserDetails); ; localTaggerResult = null) {
                    PumpkinTagger.this.mUiThread.execute(new Runnable() {
                        public void run() {
                            if ((PumpkinTagger.this.isInDisambiguationFrame()) && (localTaggerResult == null)) {
                                PumpkinTagger .2.
                                this.val$disambiguationFailureCallback.onResult(null);
                                return;
                            }
                            if (localTaggerResult == null) {
                                PumpkinTagger .2. this.val$noMatchCallback.onResult(null);
                                return;
                            }
                            PumpkinTagger .2. this.val$successCallback.onResult(localTaggerResult);
                        }
                    });
                    return;
                    if (paramInt <= 0) {
                        break;
                    }
                    localPumpkinTaggerResults = PumpkinTagger.this.tagAlternatesConservative(paramString, paramImmutableList, paramInt, localEmbeddedParserDetails, PumpkinTagger.this.mActionFrame);
                    break;
                    localEmbeddedParserDetails.setHypothesisIndex(0);
                    break;
                }
            }
        });
    }

    public void updateSelectionNames(Collection<String> paramCollection) {
        if (this.mPumpkinState == 2) {
            synchronized (this.mUserValidators) {
                this.mUserValidators.addUserValidator("SELECT_NAME", (String[]) paramCollection.toArray(new String[paramCollection.size()]));
                return;
            }
        }
    }

    public void updateSelectionTypes(Collection<String> paramCollection) {
        if (this.mPumpkinState == 2) {
            synchronized (this.mUserValidators) {
                this.mUserValidators.addUserValidator("SELECT_TYPE", (String[]) paramCollection.toArray(new String[paramCollection.size()]));
                return;
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.PumpkinTagger

 * JD-Core Version:    0.7.0.1

 */