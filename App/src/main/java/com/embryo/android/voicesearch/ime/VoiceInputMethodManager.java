package com.embryo.android.voicesearch.ime;

import android.util.Log;

import com.embryo.android.shared.util.ScheduledSingleThreadedExecutor;
import com.embryo.android.speech.alternates.Hypothesis;
import com.embryo.android.speech.exception.RecognizeException;
import com.embryo.android.voicesearch.audio.AudioTrackSoundManager;
import com.embryo.android.voicesearch.ime.view.VoiceInputViewHandler;
import com.embryo.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.embryo.speech.recognizer.api.RecognizerProtos;

public class VoiceInputMethodManager {
    private final Runnable mBackToPreviousImeRunnable = new Runnable() {
        public void run() {
            VoiceInputMethodManager.this.backToPreviousIme();
        }
    };
    private final TemporaryData<Void> mContinueRecording = new TemporaryData(null);
    private final TemporaryData<String> mDictationBcp47Locale = new TemporaryData(null);
    private final DictationResultHandlerImpl mDictationResultHandler;
    private final com.embryo.android.voicesearch.ime.ImeLoggerHelper mImeLoggerHelper;
    private final Runnable mReleaseResourcesRunnable = new Runnable() {
        public void run() {
            VoiceInputMethodManager.this.maybeReleaseResources();
        }
    };
    private final ScreenStateMonitor mScreenStateMonitor;
    private final Settings mSettings;
    private final AudioTrackSoundManager mSoundManager;
    private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
    private final com.embryo.android.voicesearch.ime.VoiceImeInputMethodService mVoiceImeInputMethodService;
    private final VoiceImeSubtypeUpdater mVoiceImeSubtypeUpdater;
    private final VoiceInputViewHandler mVoiceInputViewHandler;
    private final com.embryo.android.voicesearch.ime.VoiceLanguageSelector mVoiceLanguageSelector;
    private final VoiceRecognitionHandler mVoiceRecognitionHandler;
    private boolean mBackToPrevImeOnDone = false;
    private boolean mInputViewActive;

    public VoiceInputMethodManager(com.embryo.android.voicesearch.ime.VoiceLanguageSelector paramVoiceLanguageSelector, ScreenStateMonitor paramScreenStateMonitor, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, com.embryo.android.voicesearch.ime.ImeLoggerHelper paramImeLoggerHelper, Settings paramSettings, AudioTrackSoundManager paramAudioTrackSoundManager, VoiceImeSubtypeUpdater paramVoiceImeSubtypeUpdater, VoiceInputViewHandler paramVoiceInputViewHandler, com.embryo.android.voicesearch.ime.VoiceImeInputMethodService paramVoiceImeInputMethodService, VoiceRecognitionHandler paramVoiceRecognitionHandler, DictationResultHandlerImpl paramDictationResultHandlerImpl) {
        Log.i("VoiceInputMethodManager", "#()");
        this.mVoiceLanguageSelector = Preconditions.checkNotNull(paramVoiceLanguageSelector);
        this.mUiThreadExecutor = Preconditions.checkNotNull(paramScheduledSingleThreadedExecutor);
        this.mImeLoggerHelper = Preconditions.checkNotNull(paramImeLoggerHelper);
        this.mScreenStateMonitor = Preconditions.checkNotNull(paramScreenStateMonitor);
        this.mSettings = Preconditions.checkNotNull(paramSettings);
        this.mSoundManager = Preconditions.checkNotNull(paramAudioTrackSoundManager);
        this.mVoiceImeSubtypeUpdater = Preconditions.checkNotNull(paramVoiceImeSubtypeUpdater);
        this.mVoiceInputViewHandler = Preconditions.checkNotNull(paramVoiceInputViewHandler);
        this.mVoiceImeInputMethodService = Preconditions.checkNotNull(paramVoiceImeInputMethodService);
        this.mVoiceRecognitionHandler = paramVoiceRecognitionHandler;
        this.mDictationResultHandler = paramDictationResultHandlerImpl;
    }

    private void backToPreviousIme() {
        maybeReleaseResources();
        this.mVoiceImeInputMethodService.switchToLastInputMethod();
    }

    private void handleDone() {
        this.mImeLoggerHelper.onDone();
        this.mVoiceInputViewHandler.hideWaitingForResults();
        if (this.mBackToPrevImeOnDone) {
            backToPreviousIme();
        }
    }

    private void handleError(RecognizeException paramRecognizeException) {
        this.mDictationResultHandler.handleError();
        this.mVoiceInputViewHandler.displayError(com.embryo.android.voicesearch.util.ErrorUtils.getErrorMessage(paramRecognizeException));
        this.mImeLoggerHelper.onError();
    }

    private void handlePartialRecognitionResult(String paramString) {
        this.mImeLoggerHelper.setWaitingForResult(true);
        if (paramString != null) {
            this.mDictationResultHandler.handlePartialRecognitionResult(paramString);
        }
    }

    private void handlePause() {
        this.mDictationResultHandler.handleStop();
        this.mVoiceInputViewHandler.displayPause(this.mVoiceRecognitionHandler.isWaitingForResults());
    }

    private void handleRecognitionResult(Hypothesis paramHypothesis, String paramString) {
        this.mImeLoggerHelper.setWaitingForResult(false);
        this.mDictationResultHandler.handleRecognitionResult(paramHypothesis, paramString);
    }

    private void maybeReleaseResources() {
        if (mContinueRecording.isExpired()) {
            Log.i("VoiceInputMethodManager", "#releaseResources");
            if (mInputViewActive) {
                if (mImeLoggerHelper != null) {
                    mImeLoggerHelper.onFinishInput();
                } else {
                    Log.w("VoiceInputMethodManager", "onFinishInput - mImeLoggerHelper is null");
                }
            }
            mInputViewActive = false;
            mScreenStateMonitor.unregister();
            mDictationBcp47Locale.extend();
            mVoiceImeInputMethodService.scheduleSendEvents();
            stopDictation();
            mUiThreadExecutor.cancelExecute(mReleaseResourcesRunnable);
            mUiThreadExecutor.cancelExecute(mBackToPreviousImeRunnable);
            return;
        }
        Log.i("VoiceInputMethodManager", "#releaseResources - schedule");
        scheduleReleaseResources();
    }

    private void scheduleReleaseResources() {
        this.mUiThreadExecutor.cancelExecute(this.mReleaseResourcesRunnable);
        this.mUiThreadExecutor.executeDelayed(this.mReleaseResourcesRunnable, 500L);
    }

    private void stopDictation() {
        if (mVoiceRecognitionHandler != null) {
            mVoiceRecognitionHandler.cancelRecognition();
        } else {
            Log.w("VoiceInputMethodManager", "onFinishInput - mVoiceRecognitionDelegate is null");
        }
        if (mDictationResultHandler != null) {
            mDictationResultHandler.handleStop();
            mDictationResultHandler.reset();
        }
    }

    public class DictationListener
            extends com.embryo.android.speech.listeners.RecognitionEventListenerAdapter {
        private boolean mInvalid = false;

        private String getPartial(RecognizerProtos.RecognitionEvent recognitionEvent) {
            if (!recognitionEvent.hasPartialResult()) {
                return null;
            }
            RecognizerProtos.PartialResult partialResult = recognitionEvent.getPartialResult();
            if (0 == partialResult.getPartCount()) {
                return null;
            }
            StringBuilder builder = new StringBuilder();
            float partialResultMinConfidence = VoiceInputMethodManager.this.mSettings.getConfiguration().getDictation().getPartialResultMinConfidence();
            for (RecognizerProtos.PartialPart localPartialPart : partialResult.getPartList()) {
                if (!localPartialPart.hasText()) {
                    if (localPartialPart.getStability() >= partialResultMinConfidence) {
                        builder.append(localPartialPart.getText());
                    } else if (builder.length() == 0) {
                        break;
                    }
                }
            }
            return builder.toString();
        }

        private void processPartialRecognitionResult(String paramString) {
            VoiceInputMethodManager.this.handlePartialRecognitionResult(paramString);
        }

        public void invalidate() {
            this.mInvalid = true;
        }

        public boolean isValid() {
            return !this.mInvalid;
        }

        public void onBeginningOfSpeech(long paramLong) {
            if (this.mInvalid) {
                return;
            }
            VoiceInputMethodManager.this.mVoiceInputViewHandler.displayRecording();
        }

        public void onDone() {
            if (this.mInvalid) {
                return;
            }
            invalidate();
            VoiceInputMethodManager.this.handleDone();
        }

        public void onEndOfSpeech() {
            if (this.mInvalid) {
                return;
            }
            VoiceInputMethodManager.this.mSoundManager.playDictationDoneSound();
            VoiceInputMethodManager.this.mVoiceRecognitionHandler.stopListening();
            VoiceInputMethodManager.this.handlePause();
        }

        public void onError(RecognizeException paramRecognizeException) {
            if (this.mInvalid) {
                return;
            }
            invalidate();
            Log.w("VoiceInputMethodManager", "onError: " + paramRecognizeException.getClass().getName() + " " + paramRecognizeException.getMessage());
            VoiceInputMethodManager.this.mSoundManager.playErrorSound();
            VoiceInputMethodManager.this.handleError(paramRecognizeException);
        }

        public void onNoSpeechDetected() {
            if (this.mInvalid) {
                return;
            }
            invalidate();
            VoiceInputMethodManager.this.mSoundManager.playNoInputSound();
            VoiceInputMethodManager.this.mVoiceRecognitionHandler.cancelRecognition();
            VoiceInputMethodManager.this.handlePause();
        }

        public void onReadyForSpeech() {
            VoiceInputMethodManager.this.mVoiceInputViewHandler.displayListening();
        }

        public void onRecognitionCancelled() {
            if (this.mInvalid) {
                return;
            }
            invalidate();
            VoiceInputMethodManager.this.mSoundManager.playNoInputSound();
            VoiceInputMethodManager.this.mVoiceInputViewHandler.displayPause(VoiceInputMethodManager.this.mVoiceRecognitionHandler.isWaitingForResults());
        }

        public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
            if (this.mInvalid) {
                return;
            }
            String str = getPartial(paramRecognitionEvent);
            RecognizerProtos.Hypothesis proto;
            Hypothesis hypothesis;
            if (paramRecognitionEvent.hasResult()) {
                RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getResult();
                if (localRecognitionResult.getHypothesisCount() == 0) {
                    Log.w("VoiceInputMethodManager", "No hypothesis in recognition result.");
                    return;
                }
                proto = localRecognitionResult.getHypothesis(0);
                if ((VoiceInputMethodManager.this.mVoiceRecognitionHandler.getSessionParams().isAlternatesEnabled()) && (proto.hasAlternates())) {
                    hypothesis = Hypothesis.fromAlternateSpanProtos(proto.getText(), proto.getAlternates().getSpanList());
                    VoiceInputMethodManager.this.handleRecognitionResult(hypothesis, str);
                } else {
                    hypothesis = Hypothesis.fromText(proto.getText());
                    if (str != null) {
                        processPartialRecognitionResult(str);
                    }
                }
            } else {
                if (paramRecognitionEvent.getEventType() != 1) {
                    return;
                }
                invalidate();
                VoiceInputMethodManager.this.handleDone();
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoiceInputMethodManager

 * JD-Core Version:    0.7.0.1

 */