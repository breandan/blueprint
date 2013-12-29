package com.google.android.voicesearch.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.intentapi.IntentApiActivity;
import com.google.android.voicesearch.intentapi.IntentApiParams;
import com.google.android.voicesearch.intentapi.IntentApiRecognizerController;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.ErrorUtils;
import com.google.common.base.Preconditions;
import com.google.speech.recognizer.api.RecognizerProtos;

import java.util.ArrayList;
import java.util.Locale;

public class IntentApiController {
    private final IntentApiActivity mActivity;
    private final IntentApiRecognizerController mRecognizerController;
    private final Settings mSettings;
    private final AudioTrackSoundManager mSoundManager;
    private final SpeechLevelSource mSpeechLevelSource;
    private boolean haveCompletedRecognition = false;
    private IntentApiParams mIntentApiParams;
    private boolean mShowingError;
    private Ui mUi;

    public IntentApiController(IntentApiActivity paramIntentApiActivity, Settings paramSettings, SpeechLevelSource paramSpeechLevelSource, AudioTrackSoundManager paramAudioTrackSoundManager, IntentApiRecognizerController paramIntentApiRecognizerController) {
        this.mActivity = paramIntentApiActivity;
        this.mSettings = paramSettings;
        this.mSpeechLevelSource = paramSpeechLevelSource;
        this.mSoundManager = paramAudioTrackSoundManager;
        this.mRecognizerController = paramIntentApiRecognizerController;
    }

    private boolean checkIncomingIntent() {
        if ((this.mIntentApiParams.isReturnAudio()) && (!hasPermissionToRecordAudio(this.mIntentApiParams.getCallingPackage()))) {
            Log.w("IntentApiController", "Must have android.permission.RECORD_AUDIO to record audio");
            return false;
        }
        return true;
    }

    private void handleCancel() {
        if (!this.haveCompletedRecognition) {
            this.mSoundManager.playNoInputSound();
        }
        this.mActivity.finish();
    }

    private void handleDone() {
        if ((!this.mShowingError) && (!this.mActivity.isFinishing())) {
            handleNoMatch();
        }
        while (this.haveCompletedRecognition) {
            return;
        }
        this.mSoundManager.playNoInputSound();
    }

    private void handleError(RecognizeException paramRecognizeException) {
        int i = ErrorUtils.getErrorMessage(paramRecognizeException);
        int j = ErrorUtils.getRecognizerIntentError(paramRecognizeException);
        boolean bool = ErrorUtils.canResendSameAudio(paramRecognizeException);
        this.mShowingError = true;
        this.mUi.showError(i, j, bool);
        if (!this.haveCompletedRecognition) {
            this.mSoundManager.playErrorSound();
        }
    }

    private void handleNoMatch() {
        this.mShowingError = true;
        this.mUi.showNoMatch(1);
        if (!this.haveCompletedRecognition) {
            this.mSoundManager.playNoInputSound();
        }
    }

    private void handleResult(RecognizerProtos.RecognitionResult paramRecognitionResult) {
        Intent localIntent = new Intent();
        int i = paramRecognitionResult.getHypothesisCount();
        if (i < 1) {
            handleNoMatch();
            return;
        }
        this.haveCompletedRecognition = true;
        this.mSoundManager.playRecognitionDoneSound();
        if ((this.mIntentApiParams.getMaxResults() > 0) && (this.mIntentApiParams.getMaxResults() < i)) {
            i = this.mIntentApiParams.getMaxResults();
        }
        ArrayList localArrayList = new ArrayList(i);
        float[] arrayOfFloat = new float[i];
        for (int j = 0; j < i; j++) {
            localArrayList.add(paramRecognitionResult.getHypothesis(j).getText());
            arrayOfFloat[j] = paramRecognitionResult.getHypothesis(j).getConfidence();
        }
        localIntent.putExtra("android.speech.extra.RESULTS", localArrayList);
        localIntent.putExtra("android.speech.extra.CONFIDENCE_SCORES", arrayOfFloat);
        localIntent.putExtra("query", (String) localArrayList.get(0));
        if (this.mIntentApiParams.isAutoScript()) {
            Log.i("IntentApiController", "Recognition results: [" + (String) localArrayList.get(0) + "]");
        }
        if (this.mIntentApiParams.getPendingIntent() == null) {
            this.mActivity.setResultInternal(-1, localIntent);
            this.mActivity.finish();
            return;
        }
        if (this.mIntentApiParams.getPendingBundleIntent() != null) {
            localIntent.putExtras(this.mIntentApiParams.getPendingBundleIntent());
        }
        try {
            this.mIntentApiParams.getPendingIntent().send(this.mActivity, -1, localIntent);
        } catch (PendingIntent.CanceledException localCanceledException) {
            Log.e("IntentApiController", "Not possible to start pending intent", localCanceledException);
        } finally {
            this.mActivity.finish();
        }
    }

    private boolean hasPermissionToRecordAudio(String paramString) {
        return this.mActivity.getPackageManager().checkPermission("android.permission.RECORD_AUDIO", paramString) == 0;
    }

    private void internalStart(boolean paramBoolean) {
        if (paramBoolean) {
            this.mUi.setPromptText(this.mActivity.getString(2131363440));
        }
        RecognitionEventListenerAdapter local1;
        for (; ; ) {
            local1 = new RecognitionEventListenerAdapter() {
                public void onBeginningOfSpeech(long paramAnonymousLong) {
                    IntentApiController.this.mUi.showRecording();
                }

                public void onDone() {
                    IntentApiController.this.handleDone();
                }

                public void onEndOfSpeech() {
                    IntentApiController.this.mUi.showRecognizing();
                }

                public void onError(RecognizeException paramAnonymousRecognizeException) {
                    IntentApiController.this.handleError(paramAnonymousRecognizeException);
                }

                public void onNoSpeechDetected() {
                    IntentApiController.this.handleCancel();
                }

                public void onReadyForSpeech() {
                    IntentApiController.this.mUi.showListening();
                }

                public void onRecognitionCancelled() {
                    IntentApiController.this.handleCancel();
                }

                public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramAnonymousRecognitionEvent) {
                    if (!paramAnonymousRecognitionEvent.hasCombinedResult()) {
                        return;
                    }
                    IntentApiController.this.handleResult(paramAnonymousRecognitionEvent.getCombinedResult());
                }
            };
            if (!paramBoolean) {
                break;
            }
            this.mRecognizerController.resendAudio(local1, this.mIntentApiParams.getCallingPackage());
            if (this.mIntentApiParams.getPrompt() != null) {
                this.mUi.setPromptText(this.mIntentApiParams.getPrompt());
            } else {
                this.mUi.setPromptText(null);
            }
        }
        this.mRecognizerController.start(local1, this.mIntentApiParams.getCallingPackage());
    }

    private void updateLocale() {
        String str = SpokenLanguageUtils.getSpokenBcp47Locale(this.mSettings, this.mIntentApiParams.getLanguage());
        this.mRecognizerController.setBcp47Locale(str);
        if (str.equals(SpokenLanguageUtils.getDefaultMainSpokenLanguageBcp47(Locale.getDefault().toString(), this.mSettings.getConfiguration()))) {
            this.mUi.setLanguage(null);
            return;
        }
        this.mUi.setLanguage(SpokenLanguageUtils.getDisplayName(this.mSettings.getConfiguration(), str));
    }

    private void updateProfanityFilter() {
        this.mRecognizerController.setProfanityFilterEnabled(this.mIntentApiParams.getProfanityFilterEnabled(this.mSettings.isProfanityFilterEnabled()));
    }

    public void attachUi(Ui paramUi) {
        this.mUi = paramUi;
        this.mRecognizerController.attachUi(this.mUi);
        this.mUi.setSpeechLevelSource(this.mSpeechLevelSource);
    }

    public void cancel() {
        this.mRecognizerController.cancel();
    }

    public void detachUi(Ui paramUi) {
        this.mRecognizerController.detachUi(paramUi);
    }

    public void finishWithReturnCode(int paramInt) {
        this.mActivity.setResultInternal(paramInt);
        this.mActivity.finish();
    }

    public void onResume(IntentApiParams paramIntentApiParams) {
        if (this.mShowingError) {
            return;
        }
        if (this.mUi != null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mIntentApiParams = ((IntentApiParams) Preconditions.checkNotNull(paramIntentApiParams));
            if (checkIncomingIntent()) {
                break;
            }
            this.mActivity.finish();
            return;
        }
        updateLocale();
        updateProfanityFilter();
        internalStart(false);
    }

    public void retryLastRecognition() {
        this.mShowingError = false;
        internalStart(true);
    }

    public void startOver() {
        this.mShowingError = false;
        internalStart(false);
    }

    public void stopListening() {
        this.mRecognizerController.stopListening();
    }

    public static abstract interface Ui
            extends IntentApiRecognizerController.Ui {
        public abstract void setLanguage(String paramString);

        public abstract void setPromptText(String paramString);

        public abstract void setSpeechLevelSource(SpeechLevelSource paramSpeechLevelSource);

        public abstract void showError(int paramInt1, int paramInt2, boolean paramBoolean);

        public abstract void showListening();

        public abstract void showNoMatch(int paramInt);

        public abstract void showRecording();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.IntentApiController

 * JD-Core Version:    0.7.0.1

 */