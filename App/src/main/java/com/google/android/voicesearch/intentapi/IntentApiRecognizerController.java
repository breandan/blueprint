package com.google.android.voicesearch.intentapi;

import android.util.Log;

import com.google.android.speech.Recognizer;
import com.google.android.speech.audio.AudioStore;
import com.google.android.speech.audio.SingleRecordingAudioStore;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.embedded.Greco3RecognitionEngine;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.CancellableRecognitionEventListener;
import com.google.android.speech.listeners.CompositeRecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.params.SessionParams;
import com.google.android.speech.test.TestPlatformLog;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import com.google.speech.recognizer.api.RecognizerProtos;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public class IntentApiRecognizerController {
    private final AudioStore mAudioStore;
    private final VoiceSearchServices mVoiceSearchServices;
    private CancellableRecognitionEventListener mEventListener;
    @Nullable
    private Executor mMainThreadExecutor;
    private boolean mProfanityFilterEnabled;
    private boolean mRecognitionInProgress = false;
    @Nullable
    private Recognizer mRecognizer;
    private String mSpokenBcp47Locale;
    private Ui mUi;

    public IntentApiRecognizerController(VoiceSearchServices paramVoiceSearchServices) {
        this.mVoiceSearchServices = paramVoiceSearchServices;
        this.mAudioStore = new SingleRecordingAudioStore();
    }

    private void cancelInternal(boolean paramBoolean) {
        if (this.mRecognitionInProgress) {
            TestPlatformLog.logError("no_match");
            EventLogger.recordClientEvent(18);
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

    private SessionParams.Builder getSessionParamsBuilder() {
        return new SessionParams.Builder().setSpokenBcp47Locale(this.mSpokenBcp47Locale).setProfanityFilterEnabled(this.mProfanityFilterEnabled).setGreco3Mode(Greco3Mode.DICTATION).setMode(0);
    }

    private void maybeInit() {
        if (this.mRecognizer == null) {
            this.mRecognizer = this.mVoiceSearchServices.getRecognizer();
            this.mMainThreadExecutor = this.mVoiceSearchServices.getMainThreadExecutor();
        }
    }

    private void prepareRecognition(SessionParams paramSessionParams, RecognitionEventListener paramRecognitionEventListener) {
        cancelInternal(false);
        this.mRecognitionInProgress = true;
        CompositeRecognitionEventListener localCompositeRecognitionEventListener = null;
        if (paramRecognitionEventListener != null) {
            localCompositeRecognitionEventListener = new CompositeRecognitionEventListener();
            localCompositeRecognitionEventListener.add(new InternalRecognitionEventListener());
            localCompositeRecognitionEventListener.add(paramRecognitionEventListener);
        }

        if (localCompositeRecognitionEventListener == null) {
            this.mEventListener = new CancellableRecognitionEventListener(new InternalRecognitionEventListener());
        } else {
            this.mEventListener = new CancellableRecognitionEventListener(localCompositeRecognitionEventListener);
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
    public AudioStore.AudioRecording getLastAudio() {
        return this.mAudioStore.getLastAudio();
    }

    public void resendAudio(@Nullable RecognitionEventListener paramRecognitionEventListener, String paramString) {
        Preconditions.checkNotNull(this.mSpokenBcp47Locale);
        Preconditions.checkNotNull(this.mUi);
        AudioStore.AudioRecording localAudioRecording = getLastAudio();
        if (localAudioRecording == null) {
            start(paramRecognitionEventListener, paramString);
            return;
        }
        maybeInit();
        SessionParams localSessionParams = getSessionParamsBuilder().setTriggerApplication(paramString).setResendingAudio(true).build();
        prepareRecognition(localSessionParams, paramRecognitionEventListener);
        this.mUi.showRecognizing();
        this.mRecognizer.startRecordedAudioRecognition(localSessionParams, localAudioRecording.getAudio(), this.mEventListener, this.mMainThreadExecutor);
    }

    public void setBcp47Locale(String paramString) {
        this.mSpokenBcp47Locale = Preconditions.checkNotNull(paramString);
    }

    void setLastAudioForTest(String paramString, byte[] paramArrayOfByte, int paramInt) {
        this.mAudioStore.put(paramString, new AudioStore.AudioRecording(paramInt, paramArrayOfByte));
    }

    public void setProfanityFilterEnabled(boolean paramBoolean) {
        this.mProfanityFilterEnabled = paramBoolean;
    }

    public void start(@Nullable RecognitionEventListener paramRecognitionEventListener, String paramString) {
        Preconditions.checkNotNull(this.mSpokenBcp47Locale);
        Preconditions.checkNotNull(this.mUi);
        maybeInit();
        SessionParams localSessionParams = getSessionParamsBuilder().setTriggerApplication(paramString).setResendingAudio(false).build();
        prepareRecognition(localSessionParams, paramRecognitionEventListener);
        this.mRecognizer.startListening(localSessionParams, this.mEventListener, this.mMainThreadExecutor, this.mAudioStore);
        this.mUi.showInitializing();
    }

    public void stopListening() {
        if (this.mRecognitionInProgress) {
            EventLogger.recordClientEvent(17);
            this.mRecognizer.stopListening(this.mEventListener);
        }
    }

    public static abstract interface Ui {
        public abstract void showInitializing();

        public abstract void showRecognizing();
    }

    private class InternalRecognitionEventListener
            extends RecognitionEventListenerAdapter {
        private InternalRecognitionEventListener() {
        }

        public void onDone() {
            TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
            IntentApiRecognizerController.this.cancelInternal(false);
        }

        public void onError(RecognizeException paramRecognizeException) {
            if ((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException)) {
                Log.i("IntentApiRecognizerController", "No recognizers available.");
            }
            TestPlatformLog.logError(paramRecognizeException.toString());
            IntentApiRecognizerController.this.cancelInternal(false);
            Log.e("IntentApiRecognizerController", "onError", paramRecognizeException);
        }

        public void onNoSpeechDetected() {
            IntentApiRecognizerController.this.cancelInternal(true);
            TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
        }

        public void onReadyForSpeech() {
            TestPlatformLog.log("SPEAK_NOW");
        }

        public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
            TestPlatformLog.logResults(paramRecognitionEvent);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.intentapi.IntentApiRecognizerController

 * JD-Core Version:    0.7.0.1

 */