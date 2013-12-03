package com.google.android.voicesearch.serviceapi;

import android.util.Log;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.Recognizer;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.params.SessionParams;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;

import java.util.concurrent.Executor;

public class GoogleRecognitionServiceImpl {
    private final LevelsGenerator mLevelsGenerator;
    private ListenerAdapter mListener;
    private final Executor mMainThreadExecutor;
    private final Recognizer mRecognizer;
    private final ExtraPreconditions.ThreadCheck mThreadCheck;
    private ListenerAdapter.OnDoneListener onDoneListener = new ListenerAdapter.OnDoneListener() {
        public void onDone() {
            GoogleRecognitionServiceImpl.this.internalCancel();
        }
    };

    public GoogleRecognitionServiceImpl(Recognizer paramRecognizer, LevelsGenerator paramLevelsGenerator, Executor paramExecutor) {
        this.mRecognizer = paramRecognizer;
        this.mLevelsGenerator = paramLevelsGenerator;
        this.mThreadCheck = ExtraPreconditions.createSameThreadCheck();
        this.mMainThreadExecutor = paramExecutor;
    }

    private void internalCancel() {
        this.mThreadCheck.check();
        this.mRecognizer.cancel(this.mListener);
        this.mListener = null;
        this.mLevelsGenerator.stop();
    }

    public void cancel() {
        EventLogger.recordClientEvent(57);
        internalCancel();
    }

    public void destroy() {
        internalCancel();
    }

    public void startListening(GoogleRecognitionParams paramGoogleRecognitionParams, RecognitionService.Callback paramCallback) {
        int i = 1;
        Preconditions.checkNotNull(paramGoogleRecognitionParams);
        Preconditions.checkNotNull(paramCallback);
        this.mThreadCheck.check();
        Log.i("GoogleRecognitionServiceImpl", "#startListening [" + paramGoogleRecognitionParams.getSpokenBcp47Locale() + "]");
        SessionParams.Builder localBuilder = new SessionParams.Builder().setMode(i);
        if (!paramGoogleRecognitionParams.isDictationRequested()) {
        }
        for (; ; ) {
            SessionParams localSessionParams = localBuilder.setStopOnEndOfSpeech(i).setSpokenBcp47Locale(paramGoogleRecognitionParams.getSpokenBcp47Locale()).setProfanityFilterEnabled(paramGoogleRecognitionParams.isProfanityFilterEnabled()).setGreco3Mode(Greco3Mode.DICTATION).setTriggerApplication(paramGoogleRecognitionParams.getTriggerApplication()).build();
            EventLogger.recordClientEvent(55, paramGoogleRecognitionParams.getTriggerApplication());
            this.mListener = new ListenerAdapter(paramCallback, this.onDoneListener, paramGoogleRecognitionParams.isDictationRequested(), paramGoogleRecognitionParams.isPartialResultsRequested());
            this.mRecognizer.startListening(localSessionParams, this.mListener, this.mMainThreadExecutor, null);
            this.mLevelsGenerator.stop();
            this.mLevelsGenerator.start(this.mListener);
            return;
            int j = 0;
        }
    }

    public void stopListening() {
        this.mThreadCheck.check();
        EventLogger.recordClientEvent(58);
        this.mRecognizer.stopListening(this.mListener);
        this.mLevelsGenerator.stop();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.serviceapi.GoogleRecognitionServiceImpl

 * JD-Core Version:    0.7.0.1

 */