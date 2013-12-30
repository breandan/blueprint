package com.google.android.voicesearch.serviceapi;

import android.speech.RecognitionService;
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

    public void startListening(GoogleRecognitionParams data, RecognitionService.Callback callback) {
        Preconditions.checkNotNull(data);
        Preconditions.checkNotNull(callback);
        mThreadCheck.check();
        Log.i("GoogleRecognitionServiceImpl", "#startListening [" + data.getSpokenBcp47Locale() + "]");
        SessionParams sessionParams = new SessionParams.Builder().setMode(0x1).setStopOnEndOfSpeech((!data.isDictationRequested())).setSpokenBcp47Locale(data.getSpokenBcp47Locale()).setProfanityFilterEnabled(data.isProfanityFilterEnabled()).setGreco3Mode(Greco3Mode.DICTATION).setTriggerApplication(data.getTriggerApplication()).build();
        EventLogger.recordClientEvent(0x37, data.getTriggerApplication());
        mListener = new ListenerAdapter(callback, onDoneListener, data.isDictationRequested(), data.isPartialResultsRequested());
        mRecognizer.startListening(sessionParams, mListener, mMainThreadExecutor, null);
        mLevelsGenerator.stop();
        mLevelsGenerator.start(mListener);
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