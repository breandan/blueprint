package com.google.android.voicesearch.speechservice;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.e100.MessageBuffer;
import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.search.core.Feature;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.core.ears.EarsResultParser;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.shared.api.Query;
import com.google.android.sidekick.shared.util.SecondScreenUtil;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.speech.embedded.TaggerResult;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.exception.ResponseRecognizeException;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.VelvetApplication;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.actions.CardDecision;
import com.google.android.velvet.actions.CardDecisionFactory;
import com.google.android.velvet.actions.DisambiguationUtil;
import com.google.android.velvet.util.IntentUtils;
import com.google.android.voicesearch.fragments.action.CancelAction;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.android.voicesearch.fragments.action.LocalResultsAction;
import com.google.android.voicesearch.fragments.action.OpenUrlAction;
import com.google.android.voicesearch.fragments.action.PhoneCallAction;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.fragments.action.PuntAction;
import com.google.android.voicesearch.fragments.action.SmsAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.util.ErrorUtils;
import com.google.android.voicesearch.util.ExampleContactHelper;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.android.voicesearch.util.PumpkinActionFactory;
import com.google.audio.ears.proto.EarsService.EarsResult;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.OpenURLAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction.AppItem;
import com.google.majel.proto.CommonStructuredResponse.StructuredResponse;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResults;
import com.google.majel.proto.PeanutProtos.Peanut;
import com.google.majel.proto.PeanutProtos.Url;

import java.util.List;
import java.util.concurrent.Future;

public class ActionProcessor {
    private final ActionState mActionState;
    private final ActionV2Processor mActionV2Processor;
    private final CardDecisionFactory mCardDecisionFactory;
    private final ContactLookup mContactLookup;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final DeviceCapabilityManager mDeviceCapabilityManager;
    private final Supplier<DiscourseContext> mDiscourseContextSupplier;
    private final boolean mIsActionDiscoveryEnabled;
    private final boolean mIsFollowOnEnabled;
    private final Supplier<Future<Uri>> mLastAudioUriSupplier;
    private final LoginHelper mLoginHelper;

    public ActionProcessor(ActionState paramActionState, Context paramContext, Supplier<Future<Uri>> paramSupplier, ContactLookup paramContactLookup, ContentResolver paramContentResolver, DeviceCapabilityManager paramDeviceCapabilityManager, boolean paramBoolean1, boolean paramBoolean2, Supplier<DiscourseContext> paramSupplier1, LoginHelper paramLoginHelper, CardDecisionFactory paramCardDecisionFactory, MessageBuffer paramMessageBuffer, ExampleContactHelper paramExampleContactHelper) {
        this.mActionState = paramActionState;
        this.mLastAudioUriSupplier = ((Supplier) Preconditions.checkNotNull(paramSupplier));
        this.mContactLookup = ((ContactLookup) Preconditions.checkNotNull(paramContactLookup));
        this.mContentResolver = ((ContentResolver) Preconditions.checkNotNull(paramContentResolver));
        this.mContext = paramContext;
        this.mDeviceCapabilityManager = ((DeviceCapabilityManager) Preconditions.checkNotNull(paramDeviceCapabilityManager));
        this.mIsActionDiscoveryEnabled = paramBoolean1;
        this.mIsFollowOnEnabled = paramBoolean2;
        this.mDiscourseContextSupplier = ((Supplier) Preconditions.checkNotNull(paramSupplier1));
        this.mLoginHelper = ((LoginHelper) Preconditions.checkNotNull(paramLoginHelper));
        this.mCardDecisionFactory = ((CardDecisionFactory) Preconditions.checkNotNull(paramCardDecisionFactory));
        this.mActionV2Processor = new ActionV2Processor(this.mContext, this.mLastAudioUriSupplier, this.mContactLookup, this.mContentResolver, this.mDeviceCapabilityManager, this.mIsActionDiscoveryEnabled, this.mDiscourseContextSupplier, this.mLoginHelper, paramMessageBuffer, paramExampleContactHelper, VelvetApplication.getVersionCode());
    }

    private VoiceAction maybeProcessPumpkinFollowOn(TaggerResult paramTaggerResult, String paramString) {
        if (!this.mIsFollowOnEnabled) {
        }
        do {
            DiscourseContext localDiscourseContext;
            VoiceAction localVoiceAction;
            do {
                do {
                    return null;
                    if ("Undo".equals(paramString)) {
                        return ((DiscourseContext) this.mDiscourseContextSupplier.get()).getUndoAction();
                    }
                    if ("Redo".equals(paramString)) {
                        return ((DiscourseContext) this.mDiscourseContextSupplier.get()).getRedoAction();
                    }
                    if (!"Cancel".equals(paramString)) {
                        break;
                    }
                } while (!Feature.DISCOURSE_CONTEXT.isEnabled());
                localDiscourseContext = (DiscourseContext) this.mDiscourseContextSupplier.get();
                localVoiceAction = localDiscourseContext.getCurrentVoiceAction();
            } while (localVoiceAction == null);
            CancelAction localCancelAction = CancelAction.fromVoiceAction(localVoiceAction);
            localDiscourseContext.clearCurrentActionCancel();
            return localCancelAction;
        } while (!"Selection".equals(paramString));
        String str1 = paramTaggerResult.getArgument("Name");
        String str2 = paramTaggerResult.getArgument("Type");
        String str3 = paramTaggerResult.getArgument("Num");
        return DisambiguationUtil.maybeDisambiguate((DiscourseContext) this.mDiscourseContextSupplier.get(), this.mContext.getResources(), str1, str2, str3);
    }

    private boolean processActionData(Query paramQuery, ActionData paramActionData)
            throws ResponseRecognizeException {
        PeanutProtos.Peanut localPeanut = paramActionData.getPeanut();
        if (localPeanut == null) {
            Log.e("ActionProcessor", "Unknown ActionServerResult type");
            return false;
        }
        if ((localPeanut.hasStructuredResponse()) && (processStructuredResponse(paramQuery, paramActionData, localPeanut))) {
            return true;
        }
        if (localPeanut.getActionV2Count() > 0) {
            return processActionV2(paramActionData, localPeanut, localPeanut.getActionV2(0), paramQuery);
        }
        int i = localPeanut.getPrimaryType();
        if (3 == i) {
            return processUrlResponse(paramActionData, localPeanut.getUrlResponse(0), paramQuery);
        }
        VelvetStrictMode.logW("ActionProcessor", "Unhandled peanut with primary type " + i);
        return false;
    }

    private boolean processActionV2(ActionData paramActionData, PeanutProtos.Peanut paramPeanut, ActionV2Protos.ActionV2 paramActionV2, Query paramQuery)
            throws ResponseRecognizeException {
        if (paramActionV2.hasOpenURLActionExtension()) {
            return processOpenURLAction(paramActionData, paramPeanut, paramActionV2.getOpenURLActionExtension(), paramQuery);
        }
        if (paramActionV2.hasSoundSearchActionExtension()) {
            return processSoundSearchAction(paramActionData, paramQuery);
        }
        if (paramActionV2.hasSoundSearchTvActionExtension()) {
            return processSoundSearchTvAction(paramActionData, paramQuery);
        }
        if (this.mActionV2Processor.processActionV2(paramActionV2, paramQuery.isVoiceSearch(), new ActionV2ProcessorCallback(paramActionData, paramQuery))) {
            return true;
        }
        throw new ResponseRecognizeException("ActionV2 receieved that we can't handle");
    }

    private boolean processEarsResult(ActionData paramActionData, EarsService.EarsResult paramEarsResult, boolean paramBoolean, String paramString, Query paramQuery) {
        if (paramEarsResult != null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkArgument(bool);
            setVoiceAction(paramActionData, new PlayMediaAction(EarsResultParser.convertEarsToPlayMediaAction(paramEarsResult, paramBoolean, paramString)), paramQuery);
            return true;
        }
    }

    private boolean processException(ActionData paramActionData, RecognizeException paramRecognizeException, Query paramQuery) {
        setVoiceAction(paramActionData, new PuntAction(ErrorUtils.getErrorMessage(paramRecognizeException)), paramQuery);
        return true;
    }

    private boolean processLocalResults(ActionData paramActionData, EcoutezStructuredResponse.EcoutezLocalResults paramEcoutezLocalResults, Query paramQuery) {
        if (paramEcoutezLocalResults.getLocalResultCount() > 0) {
            setVoiceAction(paramActionData, new LocalResultsAction(paramEcoutezLocalResults, this.mDeviceCapabilityManager.isTelephoneCapable()), paramQuery);
            return true;
        }
        return false;
    }

    private boolean processOpenApplicationAction(ActionData paramActionData, TaggerResult paramTaggerResult, Query paramQuery) {
        setVoiceAction(paramActionData, new PlayMediaAction(new ActionV2Protos.PlayMediaAction().setAppItem(new ActionV2Protos.PlayMediaAction.AppItem().setName(paramTaggerResult.getArgument("AppName")))), paramQuery);
        return true;
    }

    private boolean processOpenURLAction(ActionData paramActionData, PeanutProtos.Peanut paramPeanut, ActionV2Protos.OpenURLAction paramOpenURLAction, Query paramQuery) {
        if (paramPeanut.getUrlResponseCount() > 0) {
        }
        for (PeanutProtos.Url localUrl = paramPeanut.getUrlResponse(0); ; localUrl = new PeanutProtos.Url().setLink(paramOpenURLAction.getUrl()).setDisplayLink(paramOpenURLAction.getDisplayUrl()).setTitle(paramOpenURLAction.getName())) {
            setVoiceAction(paramActionData, new OpenUrlAction(localUrl.getDisplayLink(), localUrl.getLink(), localUrl.getRenderedLink(), localUrl.getTitle()), paramQuery);
            return true;
        }
    }

    private boolean processPumpkinTaggerResult(final ActionData paramActionData, final Query paramQuery)
            throws ResponseRecognizeException {
        final TaggerResult localTaggerResult = paramActionData.getPumpkinTaggerResult();
        String str = localTaggerResult.getActionName();
        if (("CallContact".equals(str)) || ("CallNumber".equals(str))) {
            PumpkinActionFactory.checkPhoneAction(localTaggerResult);
            if (!this.mDeviceCapabilityManager.isTelephoneCapable()) {
                return processUnsupportedAction(paramActionData, paramQuery);
            }
            new AsyncTask() {
                protected PersonDisambiguation doInBackground(Void... paramAnonymousVarArgs) {
                    return PumpkinActionFactory.createPersonDisambiguation(localTaggerResult, ActionProcessor.this.mContactLookup, null, ActionProcessor.this.mDiscourseContextSupplier);
                }

                protected void onPostExecute(PersonDisambiguation paramAnonymousPersonDisambiguation) {
                    ActionProcessor.this.setVoiceAction(paramActionData, new PhoneCallAction(paramAnonymousPersonDisambiguation), paramQuery);
                }
            }.execute(new Void[0]);
            return true;
        }
        if ("SendTextToContact".equals(str)) {
            PumpkinActionFactory.checkSMSAction(localTaggerResult);
            if (!this.mDeviceCapabilityManager.isTelephoneCapable()) {
                return processUnsupportedAction(paramActionData, paramQuery);
            }
            new AsyncTask() {
                protected PersonDisambiguation doInBackground(Void... paramAnonymousVarArgs) {
                    return PumpkinActionFactory.createPersonDisambiguation(localTaggerResult, ActionProcessor.this.mContactLookup, PhoneActionUtils.MESSAGE_DEFAULT_CONTACT_TYPE, ActionProcessor.this.mDiscourseContextSupplier);
                }

                protected void onPostExecute(PersonDisambiguation paramAnonymousPersonDisambiguation) {
                    ActionProcessor.this.setVoiceAction(paramActionData, new SmsAction(paramAnonymousPersonDisambiguation, this.val$message), paramQuery);
                }
            }.execute(new Void[0]);
            return true;
        }
        if (("SelectRecipient".equals(str)) && (this.mIsFollowOnEnabled)) {
            new AsyncTask() {
                protected PersonDisambiguation doInBackground(Void... paramAnonymousVarArgs) {
                    return PumpkinActionFactory.createPersonDisambiguation(localTaggerResult, ActionProcessor.this.mContactLookup, null, ActionProcessor.this.mDiscourseContextSupplier);
                }

                protected void onPostExecute(PersonDisambiguation paramAnonymousPersonDisambiguation) {
                    if (Feature.DISCOURSE_CONTEXT.isEnabled()) {
                        CommunicationAction localCommunicationAction = DisambiguationUtil.getUpdatedCommunicationAction(((DiscourseContext) ActionProcessor.this.mDiscourseContextSupplier.get()).getCurrentCommunicationAction(), paramAnonymousPersonDisambiguation);
                        if (localCommunicationAction != null) {
                            ActionProcessor.this.setVoiceAction(paramActionData, localCommunicationAction, paramQuery);
                            EventLogger.recordClientEventWithSource(134, 33554432, Integer.valueOf(localCommunicationAction.getActionTypeLog()));
                            return;
                        }
                    }
                    ActionProcessor.this.setVoiceActionsEmpty(paramActionData, paramQuery);
                }
            }.execute(new Void[0]);
            return true;
        }
        if ("OpenApp".equals(str)) {
            return processOpenApplicationAction(paramActionData, localTaggerResult, paramQuery);
        }
        VoiceAction localVoiceAction = maybeProcessPumpkinFollowOn(localTaggerResult, str);
        if (localVoiceAction != null) {
            setVoiceAction(paramActionData, localVoiceAction, paramQuery);
            return true;
        }
        return false;
    }

    private boolean processSoundSearchAction(ActionData paramActionData, Query paramQuery) {
        this.mActionState.setModifiedCommit(paramActionData, paramQuery.musicSearchFromAction());
        setVoiceActionsEmpty(paramActionData, paramQuery);
        return true;
    }

    private boolean processSoundSearchTvAction(ActionData paramActionData, Query paramQuery) {
        this.mActionState.setModifiedCommit(paramActionData, paramQuery.tvSearchFromAction());
        setVoiceActionsEmpty(paramActionData, paramQuery);
        return true;
    }

    private boolean processStructuredResponse(Query paramQuery, ActionData paramActionData, PeanutProtos.Peanut paramPeanut) {
        CommonStructuredResponse.StructuredResponse localStructuredResponse = paramPeanut.getStructuredResponse();
        boolean bool1 = localStructuredResponse.hasEcoutezLocalResultsExtension();
        boolean bool2 = false;
        if (bool1) {
            EcoutezStructuredResponse.EcoutezLocalResults localEcoutezLocalResults = localStructuredResponse.getEcoutezLocalResultsExtension();
            int i = localEcoutezLocalResults.getActionType();
            bool2 = false;
            if (i != 5) {
                boolean bool3 = processLocalResults(paramActionData, localEcoutezLocalResults, paramQuery);
                bool2 = false;
                if (bool3) {
                    bool2 = true;
                }
            }
        }
        return bool2;
    }

    private boolean processUnsupportedAction(ActionData paramActionData, Query paramQuery) {
        setVoiceAction(paramActionData, new PuntAction(2131363293), paramQuery);
        return true;
    }

    private boolean processUrlResponse(ActionData paramActionData, PeanutProtos.Url paramUrl, Query paramQuery) {
        if ((!paramUrl.hasHtml()) || (paramUrl.hasLink())) {
            setVoiceAction(paramActionData, new OpenUrlAction(paramUrl.getDisplayLink(), paramUrl.getLink(), paramUrl.getRenderedLink(), paramUrl.getTitle()), paramQuery);
            return true;
        }
        VelvetStrictMode.logW("ActionProcessor", "Actions should never contain HTML. The HTML Answer Card is now depricated. See Bug: 10221526");
        return false;
    }

    private void setVoiceAction(ActionData paramActionData, VoiceAction paramVoiceAction, Query paramQuery) {
        setVoiceActions(paramActionData, Lists.newArrayList(new VoiceAction[]{paramVoiceAction}), paramQuery);
    }

    private void setVoiceActions(ActionData paramActionData, List<VoiceAction> paramList, Query paramQuery) {
        if (paramList.isEmpty()) {
        }
        for (CardDecision localCardDecision = CardDecision.SUPPRESS_NETWORK_DECISION; ; localCardDecision = this.mCardDecisionFactory.makeDecision((VoiceAction) paramList.get(0), paramActionData, paramQuery)) {
            this.mActionState.setVoiceActions(paramActionData, paramList, localCardDecision);
            return;
        }
    }

    private void setVoiceActionsEmpty(ActionData paramActionData, Query paramQuery) {
        setVoiceActions(paramActionData, Lists.newArrayList(), paramQuery);
    }

    public boolean process(Query paramQuery, ActionData paramActionData, boolean paramBoolean) {
        if (paramActionData.getEarsResponse() != null) {
            if (paramQuery.isTvSearch()) {
                EarsService.EarsResultsResponse localEarsResultsResponse = paramActionData.getEarsResponse();
                if (localEarsResultsResponse != null) {
                    EarsService.EarsResult localEarsResult2 = EarsResultParser.getFirstEarsResultWithTv(localEarsResultsResponse.getResultList());
                    if ((localEarsResult2 != null) && (localEarsResult2.getTvResult().hasContentId())) {
                        this.mActionState.setModifiedCommit(paramActionData, paramQuery.externalActivitySentinel(IntentUtils.createBundleForRelaunchableExternalActivity(SecondScreenUtil.createTvIntent(this.mContext, localEarsResult2.getTvResult().getContentId()))));
                        this.mActionState.setVoiceActionsEmpty(paramActionData);
                        return true;
                    }
                }
            } else {
                String str = EarsResultParser.getQueryForSearch(paramActionData.getEarsResponse().getResultList());
                if (str != null) {
                    this.mActionState.setModifiedCommit(paramActionData, paramQuery.withSecondarySearchQueryString(str));
                }
                EarsService.EarsResult localEarsResult1 = EarsResultParser.getFirstEarsResultWithMusic(paramActionData.getEarsResponse().getResultList());
                if (localEarsResult1 != null) {
                    return processEarsResult(paramActionData, localEarsResult1, paramBoolean, paramActionData.getEarsResponse().getDetectedCountryCode(), paramQuery);
                }
            }
        } else {
            if (paramActionData.getRecognizeException() != null) {
                return processException(paramActionData, paramActionData.getRecognizeException(), paramQuery);
            }
            if (paramActionData.getPumpkinTaggerResult() == null) {
                break label232;
            }
            try {
                boolean bool2 = processPumpkinTaggerResult(paramActionData, paramQuery);
                return bool2;
            } catch (ResponseRecognizeException localResponseRecognizeException2) {
                Log.e("ActionProcessor", "Error in processing PumpkinTagger result: " + localResponseRecognizeException2.getMessage());
                BugLogger.record(6708060);
            }
        }
        for (; ; ) {
            return false;
            label232:
            if ((paramActionData.hasPeanut()) || (paramActionData.hasHtml())) {
                try {
                    boolean bool1 = processActionData(paramQuery, paramActionData);
                    return bool1;
                } catch (ResponseRecognizeException localResponseRecognizeException1) {
                    Log.e("ActionProcessor", "Error in processing peanut: " + localResponseRecognizeException1.getMessage());
                    BugLogger.record(6708060);
                }
            }
        }
    }

    private class ActionV2ProcessorCallback
            implements SimpleCallback<List<VoiceAction>> {
        private final ActionData mData;
        private final Query mQuery;

        ActionV2ProcessorCallback(ActionData paramActionData, Query paramQuery) {
            this.mData = paramActionData;
            this.mQuery = paramQuery;
        }

        public void onResult(List<VoiceAction> paramList) {
            ActionProcessor.this.setVoiceActions(this.mData, paramList, this.mQuery);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.speechservice.ActionProcessor

 * JD-Core Version:    0.7.0.1

 */