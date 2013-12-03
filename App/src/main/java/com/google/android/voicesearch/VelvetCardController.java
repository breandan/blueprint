package com.google.android.voicesearch;

import android.content.Context;
import android.net.Uri;

import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.shared.api.Query;
import com.google.android.speech.audio.AudioProvider;
import com.google.android.speech.audio.AudioStore;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.ContactRetriever;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.actions.CardDecisionFactory;
import com.google.android.voicesearch.speechservice.ActionProcessor;
import com.google.android.voicesearch.util.ExampleContactHelper;
import com.google.common.base.Supplier;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class VelvetCardController {
    private static final String TAG = VelvetCardController.class.getSimpleName();
    private ActionProcessor mActionProcessor;
    private final CardDecisionFactory mCardDecisionFactory;
    private final SearchConfig mConfig;
    private ContactLookup mContactLookup;
    private final Context mContext;
    private final CoreSearchServices mCoreSearchServices;
    private final Supplier<DiscourseContext> mDiscourseContextSupplier;
    private final VelvetEventBus mEventBus;
    private ExampleContactHelper mExampleContactHelper;
    private final Supplier<Future<Uri>> mLastAudioUriSupplier;
    private final VoiceSearchServices mVoiceSearchServices;

    public VelvetCardController(CoreSearchServices paramCoreSearchServices, Context paramContext, VoiceSearchServices paramVoiceSearchServices, VelvetEventBus paramVelvetEventBus, SearchConfig paramSearchConfig, Supplier<DiscourseContext> paramSupplier, CardDecisionFactory paramCardDecisionFactory) {
        this.mCoreSearchServices = paramCoreSearchServices;
        this.mContext = paramContext;
        this.mVoiceSearchServices = paramVoiceSearchServices;
        this.mEventBus = paramVelvetEventBus;
        this.mConfig = paramSearchConfig;
        this.mDiscourseContextSupplier = paramSupplier;
        this.mCardDecisionFactory = paramCardDecisionFactory;
        this.mLastAudioUriSupplier = new Supplier() {
            public Future<Uri> get() {
                VelvetCardController.this.mVoiceSearchServices.getExecutorService().submit(new Callable() {
                    public Uri call()
                            throws Exception {
                        AudioStore.AudioRecording localAudioRecording = VelvetCardController.this.mVoiceSearchServices.getVoiceSearchAudioStore().getLastAudio();
                        return AudioProvider.insert(VelvetCardController.this.mContext, localAudioRecording);
                    }
                });
            }
        };
    }

    private ContactLookup getContactLookup() {
        if (this.mContactLookup == null) {
            this.mContactLookup = ContactLookup.newInstance(this.mContext);
        }
        return this.mContactLookup;
    }

    private ExampleContactHelper getExampleContactHelper() {
        if (this.mExampleContactHelper == null) {
            this.mExampleContactHelper = new ExampleContactHelper(new ContactRetriever(this.mContext.getContentResolver()), true);
        }
        return this.mExampleContactHelper;
    }

    private void maybeInitAnswerProcessors() {
        if (this.mActionProcessor != null) {
            return;
        }
        this.mActionProcessor = new ActionProcessor(this.mEventBus.getActionState(), this.mContext, this.mLastAudioUriSupplier, getContactLookup(), this.mContext.getContentResolver(), this.mCoreSearchServices.getDeviceCapabilityManager(), this.mConfig.isActionDiscoveryEnabled(), this.mVoiceSearchServices.isFollowOnEnabled(this.mCoreSearchServices.getGsaConfigFlags(), this.mVoiceSearchServices.getSettings().getSpokenLocaleBcp47()), this.mDiscourseContextSupplier, this.mCoreSearchServices.getLoginHelper(), this.mCardDecisionFactory, this.mCoreSearchServices.getMessageBuffer(), getExampleContactHelper());
    }

    public void handleAction(Query paramQuery, ActionData paramActionData) {
        ActionState localActionState = this.mEventBus.getActionState();
        maybeInitAnswerProcessors();
        if (!this.mActionProcessor.process(paramQuery, paramActionData, this.mCoreSearchServices.getLoginHelper().hasAccount())) {
            localActionState.setVoiceActionsEmpty(paramActionData);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.VelvetCardController

 * JD-Core Version:    0.7.0.1

 */