package com.embryo.android.voicesearch.intentapi;

import android.util.Log;

import com.embryo.android.speech.embedded.Greco3RecognitionEngine;
import com.google.common.base.Preconditions;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public class IntentApiRecognizerController {
    private final com.embryo.android.speech.audio.AudioStore mAudioStore;
    private final com.embryo.android.voicesearch.VoiceSearchServices mVoiceSearchServices;
    private com.embryo.android.speech.listeners.CancellableRecognitionEventListener mEventListener;
    @Nullable
    private Executor mMainThreadExecutor;
    private boolean mProfanityFilterEnabled;
    private boolean mRecognitionInProgress = false;
    @Nullable
    private com.embryo.android.speech.Recognizer mRecognizer;
    private String mSpokenBcp47Locale;
    private Ui mUi;

    public IntentApiRecognizerController(com.embryo.android.voicesearch.VoiceSearchServices paramVoiceSearchServices) {
        this.mVoiceSearchServices = paramVoiceSearchServices;
        this.mAudioStore = new com.embryo.android.speech.audio.SingleRecordingAudioStore();
    }

    private void cancelInternal(boolean paramBoolean) {
        if (this.mRecognitionInProgress) {
            com.embryo.android.speech.test.TestPlatformLog.logError("no_match");
            com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(18);
            if (paramBoolean) {
                this.mEventListener.onRecognitionCancelled();
            }
            this.mRecognizer.cancel(this.mEventListener);
            this.mRecognitionInProgress = false;
        }
        if (this.mEventListener != null) {
            this.mEventListener.invalidate();
            this.mEventListener = null;
        }
    }

    private com.embryo.android.speech.params.SessionParams.Builder getSessionParamsBuilder() {
        return new com.embryo.android.speech.params.SessionParams.Builder().setSpokenBcp47Locale(this.mSpokenBcp47Locale).setProfanityFilterEnabled(this.mProfanityFilterEnabled).setGreco3Mode(com.embryo.android.speech.embedded.Greco3Mode.DICTATION).setMode(0);
    }

    private void maybeInit() {
        if (this.mRecognizer == null) {
            this.mRecognizer = this.mVoiceSearchServices.getRecognizer();
            this.mMainThreadExecutor = this.mVoiceSearchServices.getMainThreadExecutor();
        }
    }

    private void prepareRecognition(com.embryo.android.speech.params.SessionParams paramSessionParams, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener) {
        cancelInternal(false);
        this.mRecognitionInProgress = true;
        com.embryo.android.speech.listeners.CompositeRecognitionEventListener localCompositeRecognitionEventListener = null;
        if (paramRecognitionEventListener != null) {
            localCompositeRecognitionEventListener = new com.embryo.android.speech.listeners.CompositeRecognitionEventListener();
            localCompositeRecognitionEventListener.add(new InternalRecognitionEventListener());
            localCompositeRecognitionEventListener.add(paramRecognitionEventListener);
        }

        if (localCompositeRecognitionEventListener == null) {
            this.mEventListener = new com.embryo.android.speech.listeners.CancellableRecognitionEventListener(new InternalRecognitionEventListener());
        } else {
            this.mEventListener = new com.embryo.android.speech.listeners.CancellableRecognitionEventListener(localCompositeRecognitionEventListener);
        }
    }

    public void cancel() {
        cancelInternal(true);
    }

    public void attachUi(IntentApiRecognizerController.Ui ui) {
        Preconditions.checkState((mUi == null));
        mUi = Preconditions.checkNotNull(ui);
    }

    public void detachUi(IntentApiRecognizerController.Ui ui) {
        Preconditions.checkState((ui == mUi));
        mUi = null;
    }

    @Nullable
    public com.embryo.android.speech.audio.AudioStore.AudioRecording getLastAudio() {
        return this.mAudioStore.getLastAudio();
    }

    public void resendAudio(@Nullable com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, String paramString) {
        Preconditions.checkNotNull(this.mSpokenBcp47Locale);
        Preconditions.checkNotNull(this.mUi);
        com.embryo.android.speech.audio.AudioStore.AudioRecording localAudioRecording = getLastAudio();
        if (localAudioRecording == null) {
            start(paramRecognitionEventListener, paramString);
            return;
        }
        maybeInit();
        com.embryo.android.speech.params.SessionParams localSessionParams = getSessionParamsBuilder().setTriggerApplication(paramString).setResendingAudio(true).build();
        prepareRecognition(localSessionParams, paramRecognitionEventListener);
        this.mUi.showRecognizing();
        this.mRecognizer.startRecordedAudioRecognition(localSessionParams, localAudioRecording.getAudio(), this.mEventListener, this.mMainThreadExecutor);
    }

    public void setBcp47Locale(String paramString) {
        this.mSpokenBcp47Locale = Preconditions.checkNotNull(paramString);
    }

    void setLastAudioForTest(String paramString, byte[] paramArrayOfByte, int paramInt) {
        this.mAudioStore.put(paramString, new com.embryo.android.speech.audio.AudioStore.AudioRecording(paramInt, paramArrayOfByte));
    }

    public void setProfanityFilterEnabled(boolean paramBoolean) {
        this.mProfanityFilterEnabled = paramBoolean;
    }

    public void start(@Nullable com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, String paramString) {
        Preconditions.checkNotNull(this.mSpokenBcp47Locale);
        Preconditions.checkNotNull(this.mUi);
        maybeInit();
        com.embryo.android.speech.params.SessionParams localSessionParams = getSessionParamsBuilder().setTriggerApplication(paramString).setResendingAudio(false).build();
        prepareRecognition(localSessionParams, paramRecognitionEventListener);
        this.mRecognizer.startListening(localSessionParams, this.mEventListener, this.mMainThreadExecutor, this.mAudioStore);
        this.mUi.showInitializing();
    }

    public void stopListening() {
        if (this.mRecognitionInProgress) {
            com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(17);
            this.mRecognizer.stopListening(this.mEventListener);
        }
    }

    public static abstract interface Ui {
        public abstract void showInitializing();

        public abstract void showRecognizing();
    }

    private class InternalRecognitionEventListener
            extends com.embryo.android.speech.listeners.RecognitionEventListenerAdapter {
        private InternalRecognitionEventListener() {
        }

        public void onDone() {
            com.embryo.android.speech.test.TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
            IntentApiRecognizerController.this.cancelInternal(false);
        }

        public void onError(com.embryo.android.speech.exception.RecognizeException paramRecognizeException) {
            if ((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException)) {
                Log.i("IntentApiRecognizerController", "No recognizers available.");
            }
            com.embryo.android.speech.test.TestPlatformLog.logError(paramRecognizeException.toString());
            IntentApiRecognizerController.this.cancelInternal(false);
            Log.e("IntentApiRecognizerController", "onError", paramRecognizeException);
        }

        public void onNoSpeechDetected() {
            IntentApiRecognizerController.this.cancelInternal(true);
            com.embryo.android.speech.test.TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
        }

        public void onReadyForSpeech() {
            com.embryo.android.speech.test.TestPlatformLog.log("SPEAK_NOW");
        }

        public void onRecognitionResult(com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
            com.embryo.android.speech.test.TestPlatformLog.logResults(paramRecognitionEvent);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     IntentApiRecognizerController

 * JD-Core Version:    0.7.0.1

 */