package com.embryo.android.voicesearch.serviceapi;

import android.speech.RecognitionService;
import android.util.Log;

import com.embryo.android.speech.Recognizer;
import com.google.common.base.Preconditions;

import java.util.concurrent.Executor;

public class GoogleRecognitionServiceImpl {
    private final LevelsGenerator mLevelsGenerator;
    private com.embryo.android.voicesearch.serviceapi.ListenerAdapter mListener;
    private final Executor mMainThreadExecutor;
    private final Recognizer mRecognizer;
    private final com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck mThreadCheck;
    private com.embryo.android.voicesearch.serviceapi.ListenerAdapter.OnDoneListener onDoneListener = new com.embryo.android.voicesearch.serviceapi.ListenerAdapter.OnDoneListener() {
        public void onDone() {
            GoogleRecognitionServiceImpl.this.internalCancel();
        }
    };

    public GoogleRecognitionServiceImpl(Recognizer paramRecognizer, LevelsGenerator paramLevelsGenerator, Executor paramExecutor) {
        this.mRecognizer = paramRecognizer;
        this.mLevelsGenerator = paramLevelsGenerator;
        this.mThreadCheck = com.embryo.android.shared.util.ExtraPreconditions.createSameThreadCheck();
        this.mMainThreadExecutor = paramExecutor;
    }

    private void internalCancel() {
        this.mThreadCheck.check();
        this.mRecognizer.cancel(this.mListener);
        this.mListener = null;
        this.mLevelsGenerator.stop();
    }

    public void cancel() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(57);
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
        com.embryo.android.speech.params.SessionParams sessionParams = new com.embryo.android.speech.params.SessionParams.Builder().setMode(0x1).setStopOnEndOfSpeech((!data.isDictationRequested())).setSpokenBcp47Locale(data.getSpokenBcp47Locale()).setProfanityFilterEnabled(data.isProfanityFilterEnabled()).setGreco3Mode(com.embryo.android.speech.embedded.Greco3Mode.DICTATION).setTriggerApplication(data.getTriggerApplication()).build();
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(0x37, data.getTriggerApplication());
        mListener = new com.embryo.android.voicesearch.serviceapi.ListenerAdapter(callback, onDoneListener, data.isDictationRequested(), data.isPartialResultsRequested());
        mRecognizer.startListening(sessionParams, mListener, mMainThreadExecutor, null);
        mLevelsGenerator.stop();
        mLevelsGenerator.start(mListener);
    }

    public void stopListening() {
        this.mThreadCheck.check();
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(58);
        this.mRecognizer.stopListening(this.mListener);
        this.mLevelsGenerator.stop();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     GoogleRecognitionServiceImpl

 * JD-Core Version:    0.7.0.1

 */