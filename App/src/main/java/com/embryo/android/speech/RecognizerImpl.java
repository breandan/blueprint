package com.embryo.android.speech;

import android.util.Log;

import com.embryo.android.shared.util.ThreadChanger;
import com.embryo.android.speech.audio.MicrophoneInputStreamFactory;
import com.embryo.android.speech.exception.NoEnginesRecognizeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nullable;


public class RecognizerImpl
        implements Recognizer {
    private final com.embryo.android.speech.audio.AudioController mAudioController;
    private final com.embryo.android.speech.audio.AudioRecorder mAudioRecorder;
    private final RecognitionEngineStore mEngineStore;
    private final com.embryo.android.shared.util.StateMachine<State> mListeningState = com.embryo.android.shared.util.StateMachine.newBuilder("RecognizerImpl", State.IDLE).addTransition(State.IDLE, State.LISTENING).addTransition(State.LISTENING, State.IDLE).addTransition(State.LISTENING, State.LISTENING).addTransition(State.LISTENING, State.STOPPED).addTransition(State.STOPPED, State.IDLE).setSingleThreadOnly(true).setStrictMode(true).build();
    private final com.embryo.android.speech.dispatcher.RecognitionDispatcher mRecognitionDispatcher;
    private final SpeechLibFactory mSpeechLibFactory;
    private final com.embryo.android.speech.logger.SpeechLibLogger mSpeechLibLogger;
    private com.embryo.android.speech.listeners.RecognitionEventListener mRecognitionListener;
    private ResponseProcessor mResponseProcessor;

    public RecognizerImpl(com.embryo.android.speech.audio.AudioController paramAudioController, com.embryo.android.speech.audio.AudioRecorder paramAudioRecorder, com.embryo.android.speech.dispatcher.RecognitionDispatcher paramRecognitionDispatcher, RecognitionEngineStore paramRecognitionEngineStore, SpeechLibFactory paramSpeechLibFactory) {
        this.mAudioController = paramAudioController;
        this.mAudioRecorder = paramAudioRecorder;
        this.mRecognitionDispatcher = paramRecognitionDispatcher;
        this.mEngineStore = paramRecognitionEngineStore;
        this.mSpeechLibFactory = paramSpeechLibFactory;
        this.mSpeechLibLogger = paramSpeechLibFactory.buildSpeechLibLogger();
    }

    public static Recognizer create(ExecutorService paramExecutorService, com.embryo.android.speech.audio.AudioController paramAudioController, SpeechLibFactory paramSpeechLibFactory) {
        return threadChange(paramExecutorService, new RecognizerImpl(paramAudioController, new com.embryo.android.speech.audio.AudioRecorder(), new com.embryo.android.speech.dispatcher.RecognitionDispatcher(paramExecutorService, paramSpeechLibFactory), paramSpeechLibFactory.buildRecognitionEngineStore(), paramSpeechLibFactory));
    }

    private static final <T> T threadChange(Executor paramExecutor, Class<T> paramClass, T paramT) {
        return ThreadChanger.createNonBlockingThreadChangeProxy(paramExecutor, paramClass, paramT);
    }

    private static final <T> T threadChange(Executor paramExecutor, T paramT) {
        return ThreadChanger.createNonBlockingThreadChangeProxy(paramExecutor, paramT);
    }

    private void internalShutdownAudio() {
        this.mListeningState.moveTo(State.IDLE);
        this.mResponseProcessor.invalidate();
        this.mResponseProcessor = null;
        this.mRecognitionListener = null;
        this.mAudioController.shutdown();
        this.mAudioRecorder.waitForRecording();
        this.mRecognitionDispatcher.cancel();
    }

    private void internalShutdownAudio(com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener) {
        if (!isListenerStillCurrent(paramRecognitionEventListener)) {
            return;
        }
        internalShutdownAudio();
    }

    private void internalStopAudio() {
        if (this.mListeningState.isIn(State.LISTENING)) {
            this.mListeningState.moveTo(State.STOPPED);
            this.mAudioController.stopListening();
            this.mAudioRecorder.waitForRecording();
        }
    }

    private void internalStopAudio(com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener) {
        if (!isListenerStillCurrent(paramRecognitionEventListener)) {
            return;
        }
        internalStopAudio();
    }

    private boolean isListenerStillCurrent(com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener) {
        if ((paramRecognitionEventListener == null) || (paramRecognitionEventListener != this.mRecognitionListener)) {
            Log.i("RecognizerImpl", "Supplied listener [" + paramRecognitionEventListener + "] is not the one that is currently " + "active [" + this.mRecognitionListener + "]");
            return false;
        }
        return true;
    }

    private void recordStartRecognitionEvent(com.embryo.android.speech.params.SessionParams params) {
        if (params.getMode() == 6) {
            this.mSpeechLibLogger.recordSpeechEvent(3, params.getRequestId());
        }
        switch (params.getMode()) {
            case 4:
                this.mSpeechLibLogger.recordSpeechEvent(11);
            case 5:
                this.mSpeechLibLogger.recordSpeechEvent(12);
            case 6:
                this.mSpeechLibLogger.recordSpeechEvent(8);
            case 7:
                this.mSpeechLibLogger.recordSpeechEvent(10);
            case 8:
                this.mSpeechLibLogger.recordSpeechEvent(15);
        }
    }

    public void cancel(com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener) {
        if ((!this.mListeningState.notIn(State.IDLE)) || (!isListenerStillCurrent(paramRecognitionEventListener))) {
            return;
        }
        this.mResponseProcessor.onRecognitionCancelled();
        internalShutdownAudio(paramRecognitionEventListener);
    }

    ResponseProcessor.AudioCallback getAudioCallback(final com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener) {
        return new ResponseProcessor.AudioCallback() {
            public void recordingStarted(long paramAnonymousLong) {
                if (RecognizerImpl.this.mAudioRecorder.isRecording()) {
                    RecognizerImpl.this.mAudioRecorder.setRecordingStartTime(paramAnonymousLong);
                }
            }

            public void shutdownAudio() {
                if (RecognizerImpl.this.mListeningState.notIn(RecognizerImpl.State.IDLE)) {
                    RecognizerImpl.this.internalShutdownAudio(paramRecognitionEventListener);
                }
            }

            public void stopAudio() {
                if (RecognizerImpl.this.mListeningState.isIn(RecognizerImpl.State.LISTENING)) {
                    RecognizerImpl.this.internalStopAudio(paramRecognitionEventListener);
                }
            }
        };
    }

    public void startListening(com.embryo.android.speech.params.SessionParams paramSessionParams, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, Executor paramExecutor, @Nullable com.embryo.android.speech.audio.AudioStore paramAudioStore) {
        com.embryo.android.speech.params.AudioInputParams localAudioInputParams = paramSessionParams.getAudioInputParams();
        boolean i = false;

        if (this.mListeningState.notIn(State.IDLE)) {
            if (!localAudioInputParams.hasStreamRewindTime()) {
                Log.w("RecognizerImpl", "Multiple recognitions in progress, the first will be cancelled.");
                internalShutdownAudio();
                this.mListeningState.checkIn(State.IDLE);
            }
            i = true;
            this.mRecognitionDispatcher.cancel();
            this.mResponseProcessor.invalidate();
            this.mResponseProcessor = null;
        }

        com.embryo.android.speech.listeners.RecognitionEventListener localRecognitionEventListener;
        com.embryo.android.speech.EngineSelector localEngineSelector;
        List localList1;

        this.mRecognitionListener = paramRecognitionEventListener;
        localRecognitionEventListener = threadChange(paramExecutor, com.embryo.android.speech.listeners.RecognitionEventListener.class, paramRecognitionEventListener);
        recordStartRecognitionEvent(paramSessionParams);
        localEngineSelector = this.mSpeechLibFactory.buildEngineSelector(paramSessionParams);
        this.mResponseProcessor = this.mSpeechLibFactory.buildResponseProcessor(getAudioCallback(this.mRecognitionListener), localRecognitionEventListener, paramSessionParams, this.mSpeechLibLogger);
        localList1 = localEngineSelector.getEngineList();
        if (localList1.isEmpty()) {
            localRecognitionEventListener.onError(new NoEnginesRecognizeException());
            return;
        }

        if (i) {
            com.embryo.android.speech.audio.AudioInputStreamFactory localAudioInputStreamFactory = this.mAudioController.createInputStreamFactory(localAudioInputParams);
            if (paramAudioStore != null) {
                this.mAudioController.rewindInputStreamFactory(localAudioInputParams.getStreamRewindTime());
            }
            try {
                int j = localAudioInputParams.getSamplingRate();
                this.mAudioRecorder.startRecording(localAudioInputStreamFactory.createInputStream(), j, MicrophoneInputStreamFactory.getMicrophoneReadSize(j), paramAudioStore, paramSessionParams.getRequestId());
                this.mListeningState.moveTo(State.LISTENING);
                this.mAudioController.startListening(localAudioInputParams, localRecognitionEventListener);
                com.embryo.android.speech.dispatcher.RecognitionDispatcher localRecognitionDispatcher = this.mRecognitionDispatcher;
                List localList2 = this.mEngineStore.getEngines(localList1);
                ResponseProcessor localResponseProcessor = this.mResponseProcessor;
                localRecognitionDispatcher.startRecognition(localList2, localAudioInputStreamFactory, paramSessionParams, localEngineSelector, localResponseProcessor);
            } catch (IOException localIOException) {
                localRecognitionEventListener.onError(new com.embryo.android.speech.exception.AudioRecognizeException("Unable to start the audio recording", localIOException));
            }
        }
    }

    public void startRecordedAudioRecognition(com.embryo.android.speech.params.SessionParams paramSessionParams, final byte[] paramArrayOfByte, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, Executor paramExecutor) {
        if (this.mListeningState.notIn(State.IDLE)) {
            Log.w("RecognizerImpl", "Multiple recognitions in progress, the first will be cancelled.");
            internalShutdownAudio();
        }
        this.mRecognitionListener = paramRecognitionEventListener;
        com.embryo.android.speech.listeners.RecognitionEventListener localRecognitionEventListener = threadChange(paramExecutor, com.embryo.android.speech.listeners.RecognitionEventListener.class, paramRecognitionEventListener);
        this.mListeningState.checkIn(State.IDLE);
        recordStartRecognitionEvent(paramSessionParams);
        com.embryo.android.speech.audio.AudioInputStreamFactory local2 = new com.embryo.android.speech.audio.AudioInputStreamFactory() {
            public InputStream createInputStream() {
                return new ByteArrayInputStream(paramArrayOfByte);
            }
        };
        this.mListeningState.moveTo(State.LISTENING);
        com.embryo.android.speech.EngineSelector localEngineSelector = this.mSpeechLibFactory.buildEngineSelector(paramSessionParams);
        this.mResponseProcessor = this.mSpeechLibFactory.buildResponseProcessor(getAudioCallback(this.mRecognitionListener), localRecognitionEventListener, paramSessionParams, this.mSpeechLibLogger);
        this.mRecognitionDispatcher.startRecognition(this.mEngineStore.getEngines(localEngineSelector.getEngineList()), local2, paramSessionParams, localEngineSelector, this.mResponseProcessor);
    }

    public void stopListening(com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener) {
        if (this.mListeningState.isIn(State.LISTENING)) {
            internalStopAudio(paramRecognitionEventListener);
        }
    }

    private static enum State {
        IDLE, LISTENING, STOPPED;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognizerImpl

 * JD-Core Version:    0.7.0.1

 */