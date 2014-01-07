package com.embryo.android.speech.dispatcher;

import android.util.Log;
import android.util.Pair;

import com.embryo.android.shared.util.ThreadChanger;
import com.embryo.android.speech.EngineSelector;
import com.embryo.android.speech.engine.RecognitionEngine;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class RecognitionDispatcher {
    private final ExecutorService mExecutor;
    private final com.embryo.android.speech.SpeechLibFactory mSpeechLibFactory;
    private final com.embryo.android.shared.util.StateMachine<State> mState = com.embryo.android.shared.util.StateMachine.newBuilder("RecognitionDispatcher", State.IDLE).addTransition(State.IDLE, State.RUNNING).addTransition(State.RUNNING, State.IDLE).setSingleThreadOnly(true).setStrictMode(true).build();
    private Collection<Pair<Integer, RecognitionEngine>> mRecognitionEngines;
    private ResultsMerger mResultsMerger;

    public RecognitionDispatcher(ExecutorService paramExecutorService, com.embryo.android.speech.SpeechLibFactory paramSpeechLibFactory) {
        this.mExecutor = paramExecutorService;
        this.mSpeechLibFactory = paramSpeechLibFactory;
    }

    private static final <T> T threadChange(Executor paramExecutor, T paramT) {
        return ThreadChanger.createNonBlockingThreadChangeProxy(paramExecutor, paramT);
    }

    private void stop() {
        this.mState.checkIn(State.RUNNING);
        this.mState.moveTo(State.IDLE);
        this.mResultsMerger.invalidate();
        this.mResultsMerger = null;
        Iterator localIterator = this.mRecognitionEngines.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEngine) ((Pair) localIterator.next()).second).close();
        }
        this.mRecognitionEngines = null;
    }

    public void cancel() {
        if (this.mState.isIn(State.RUNNING)) {
            stop();
        }
    }

    public void startRecognition(Collection<Pair<Integer, RecognitionEngine>> paramCollection, com.embryo.android.speech.audio.AudioInputStreamFactory paramAudioInputStreamFactory, com.embryo.android.speech.params.SessionParams paramSessionParams, EngineSelector paramEngineSelector, com.embryo.android.speech.callback.RecognitionEngineCallback paramRecognitionEngineCallback) {
        Log.w("RecognitionDispatcher", "Recognition initialized!");
        if (this.mState.isIn(State.RUNNING)) {
            Log.w("RecognitionDispatcher", "Multiple recognitions in progress, the first will be cancelled.");
            stop();
        }
        this.mState.moveTo(State.RUNNING);
        this.mRecognitionEngines = paramCollection;
        this.mResultsMerger = this.mSpeechLibFactory.buildResultsMerger(paramSessionParams, this, paramEngineSelector, paramRecognitionEngineCallback, this.mExecutor);
        com.embryo.android.speech.callback.RecognitionEngineCallback localRecognitionEngineCallback = threadChange(this.mExecutor, this.mResultsMerger);
        Iterator localIterator = this.mRecognitionEngines.iterator();
        while (localIterator.hasNext()) {
            ((RecognitionEngine) ((Pair) localIterator.next()).second).startRecognition(paramAudioInputStreamFactory, localRecognitionEngineCallback, paramSessionParams);
        }
    }

    public void stopEngine(int paramInt) {
        if (this.mState.isIn(State.RUNNING)) {
            int i = 0;
            Iterator localIterator = this.mRecognitionEngines.iterator();
            while (localIterator.hasNext()) {
                Pair localPair = (Pair) localIterator.next();
                if (paramInt == ((Integer) localPair.first).intValue()) {
                    ((RecognitionEngine) localPair.second).close();
                    localIterator.remove();
                    i = 1;
                }
            }
            if (i == 0) {
                Log.w("RecognitionDispatcher", "Could not stop engine " + paramInt);
            }
            if (this.mRecognitionEngines.isEmpty()) {
                stop();
            }
        }
    }

    private static enum State {
        IDLE, RUNNING;
    }

    public static abstract interface ResultsMerger
            extends com.embryo.android.speech.callback.RecognitionEngineCallback {
        public abstract void invalidate();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionDispatcher

 * JD-Core Version:    0.7.0.1

 */