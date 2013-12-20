package com.google.android.voicesearch.hotword;

import android.content.Context;
import android.media.AudioManager;
import android.view.accessibility.AccessibilityManager;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.params.AudioInputParams;
import com.google.android.speech.params.SessionParams;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.google.speech.recognizer.api.RecognizerProtos;

import java.util.concurrent.Executor;

public class HotwordDetector {
    private final AccessibilityManager mAccessibilityService;
    private final AudioManager mAudioManager;
    private final Context mContext;
    private final Executor mMainThreadExecutor;
    private final Settings mSettings;
    private final ExtraPreconditions.ThreadCheck mThreadCheck;
    private final VoiceSearchServices mVss;
    private boolean mActive;
    private HotwordListener mHotwordListener;
    private RecognitionEventListener mRecognitionListener;
    private boolean mStarted;

    public HotwordDetector(VoiceSearchServices paramVoiceSearchServices, Context paramContext, Settings paramSettings, Executor paramExecutor) {
        this.mVss = paramVoiceSearchServices;
        this.mContext = paramContext;
        this.mSettings = paramSettings;
        this.mMainThreadExecutor = paramExecutor;
        this.mAccessibilityService = ((AccessibilityManager) this.mContext.getSystemService("accessibility"));
        this.mAudioManager = this.mVss.getAudioManager();
        this.mThreadCheck = ExtraPreconditions.createSameThreadCheck();
    }

    private boolean canStartHotword() {
        return (!isMusicActive()) && (!isSpokenFeedbackEnabled()) && (!this.mAudioManager.isSpeakerphoneOn()) && (this.mAudioManager.getMode() == 0);
    }

    private SessionParams createHotwordSessionParams(String paramString, boolean paramBoolean) {
        return new SessionParams.Builder().setAudioInputParams(new AudioInputParams.Builder().setPlayBeepEnabled(false).setReportSoundLevels(false).setUsePreemptibleAudioSource(true).setRequestAudioFocus(false).build()).setMode(6).setGreco3Mode(Greco3Mode.HOTWORD).setUseMusicHotworder(paramBoolean).setSpokenBcp47Locale(paramString).build();
    }

    private void internalStart(boolean useMusicHotworder) {
        Preconditions.checkState((!mStarted));
        Preconditions.checkState(!mActive);
        String locale = mSettings.getSpokenLocaleBcp47();
        if(!canStartHotword()) {
            if(mHotwordListener != null) {
                mHotwordListener.onHotwordDetectorNotStarted();
            }
            mHotwordListener = null;
            return;
        }
        mStarted = true;
        mRecognitionListener = new HotwordDetector.HotwordDetectorListener();
        mVss.getRecognizer().startListening(createHotwordSessionParams(locale, useMusicHotworder), mRecognitionListener, mMainThreadExecutor, null);
    }


    private void internalStop(boolean paramBoolean) {
        Preconditions.checkState(this.mStarted);
        HotwordListener localHotwordListener = this.mHotwordListener;
        this.mHotwordListener = null;
        this.mVss.getRecognizer().cancel(this.mRecognitionListener);
        this.mRecognitionListener = null;
        this.mActive = false;
        this.mStarted = false;
        if (localHotwordListener != null) {
            localHotwordListener.onHotwordDetectorStopped(paramBoolean);
        }
    }

    private boolean isMusicActive() {
        return this.mAudioManager.isMusicActive();
    }

    private boolean isSpokenFeedbackEnabled() {
        if (this.mAccessibilityService.isEnabled()) {
            return !this.mAccessibilityService.getEnabledAccessibilityServiceList(1).isEmpty();
        }
        return false;
    }

    public void start(HotwordListener paramHotwordListener, boolean paramBoolean) {
        this.mThreadCheck.check();
        if ((this.mHotwordListener != null) && (this.mHotwordListener != paramHotwordListener)) {
            this.mHotwordListener = paramHotwordListener;
            BugLogger.record(8543612);
        }
        if (!this.mStarted) {
            this.mHotwordListener = paramHotwordListener;
            internalStart(paramBoolean);
        }
    }

    public void stop() {
        this.mThreadCheck.check();
        if (this.mStarted) {
            internalStop(false);
        }
    }

    public static abstract interface HotwordListener {
        public abstract void onHotword(long paramLong);

        public abstract void onHotwordDetectorNotStarted();

        public abstract void onHotwordDetectorStarted();

        public abstract void onHotwordDetectorStopped(boolean paramBoolean);

        public abstract void onMusicDetected();
    }

    private final class HotwordDetectorListener
            extends RecognitionEventListenerAdapter {
        private boolean mHotwordFired;

        private HotwordDetectorListener() {
        }

        public void onError(RecognizeException paramRecognizeException) {
            HotwordDetector.this.mThreadCheck.check();
            if (HotwordDetector.this.mStarted) {
                HotwordDetector.this.internalStop(true);
            }
        }

        public void onMusicDetected() {
            HotwordDetector.this.mThreadCheck.check();
            if (HotwordDetector.this.mStarted) {
                HotwordDetector.this.mHotwordListener.onMusicDetected();
            }
        }

        public void onReadyForSpeech() {
            HotwordDetector.this.mThreadCheck.check();
            if (HotwordDetector.this.mStarted) {
                Preconditions.checkNotNull(HotwordDetector.this.mHotwordListener);
                HotwordDetector.this.mActive = true;
                HotwordDetector.this.mHotwordListener.onHotwordDetectorStarted();
            }
        }

        public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
            HotwordDetector.this.mThreadCheck.check();
            HotwordDetector.HotwordListener localHotwordListener = HotwordDetector.this.mHotwordListener;
            if ((!this.mHotwordFired) && (localHotwordListener != null) && (paramRecognitionEvent.hasPartialResult()) && (paramRecognitionEvent.getPartialResult().getHotwordFired())) {
                localHotwordListener.onHotword(paramRecognitionEvent.getPartialResult().getEndTimeUsec());
                this.mHotwordFired = true;
            }
        }
    }
}
