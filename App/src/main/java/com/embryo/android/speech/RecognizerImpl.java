package com.embryo.android.speech;

import android.util.Log;

import com.embryo.android.shared.util.StateMachine;
import com.embryo.android.shared.util.ThreadChanger;
import com.embryo.android.speech.audio.AudioController;
import com.embryo.android.speech.audio.AudioInputStreamFactory;
import com.embryo.android.speech.audio.AudioRecorder;
import com.embryo.android.speech.audio.AudioStore;
import com.embryo.android.speech.audio.MicrophoneInputStreamFactory;
import com.embryo.android.speech.dispatcher.RecognitionDispatcher;
import com.embryo.android.speech.exception.AudioRecognizeException;
import com.embryo.android.speech.exception.NoEnginesRecognizeException;
import com.embryo.android.speech.listeners.RecognitionEventListener;
import com.embryo.android.speech.logger.SpeechLibLogger;
import com.embryo.android.speech.params.AudioInputParams;
import com.embryo.android.speech.params.SessionParams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nullable;


public class RecognizerImpl implements Recognizer {
    private final AudioController mAudioController;
    private final AudioRecorder mAudioRecorder;
    private final RecognitionEngineStore mEngineStore;
    private final StateMachine<State> mListeningState = StateMachine.newBuilder("RecognizerImpl", State.IDLE).addTransition(State.IDLE, State.LISTENING).addTransition(State.LISTENING, State.IDLE).addTransition(State.LISTENING, State.LISTENING).addTransition(State.LISTENING, State.STOPPED).addTransition(State.STOPPED, State.IDLE).setSingleThreadOnly(true).setStrictMode(true).build();
    private final RecognitionDispatcher mRecognitionDispatcher;
    private final SpeechLibFactory mSpeechLibFactory;
    private final SpeechLibLogger mSpeechLibLogger;
    private com.embryo.android.speech.listeners.RecognitionEventListener mRecognitionListener;
    private ResponseProcessor mResponseProcessor;

    public RecognizerImpl(com.embryo.android.speech.audio.AudioController paramAudioController, com.embryo.android.speech.audio.AudioRecorder paramAudioRecorder, RecognitionDispatcher paramRecognitionDispatcher, RecognitionEngineStore paramRecognitionEngineStore, SpeechLibFactory paramSpeechLibFactory) {
        this.mAudioController = paramAudioController;
        this.mAudioRecorder = paramAudioRecorder;
        this.mRecognitionDispatcher = paramRecognitionDispatcher;
        this.mEngineStore = paramRecognitionEngineStore;
        this.mSpeechLibFactory = paramSpeechLibFactory;
        this.mSpeechLibLogger = paramSpeechLibFactory.buildSpeechLibLogger();
    }

    public static Recognizer create(ExecutorService paramExecutorService, AudioController paramAudioController, SpeechLibFactory paramSpeechLibFactory) {
        return threadChange(paramExecutorService, new RecognizerImpl(paramAudioController, new AudioRecorder(), new RecognitionDispatcher(paramExecutorService, paramSpeechLibFactory), paramSpeechLibFactory.buildRecognitionEngineStore(), paramSpeechLibFactory));
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

    private void internalShutdownAudio(RecognitionEventListener paramRecognitionEventListener) {
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

    private void internalStopAudio(RecognitionEventListener paramRecognitionEventListener) {
        if (!isListenerStillCurrent(paramRecognitionEventListener)) {
            return;
        }
        internalStopAudio();
    }

    private boolean isListenerStillCurrent(RecognitionEventListener paramRecognitionEventListener) {
        if ((paramRecognitionEventListener == null) || (paramRecognitionEventListener != this.mRecognitionListener)) {
            Log.i("RecognizerImpl", "Supplied listener [" + paramRecognitionEventListener + "] is not the one that is currently " + "active [" + this.mRecognitionListener + "]");
            return false;
        }
        return true;
    }

    private void recordStartRecognitionEvent(SessionParams params) {
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

    public void cancel(RecognitionEventListener paramRecognitionEventListener) {
        if ((!this.mListeningState.notIn(State.IDLE)) || (!isListenerStillCurrent(paramRecognitionEventListener))) {
            return;
        }
        this.mResponseProcessor.onRecognitionCancelled();
        internalShutdownAudio(paramRecognitionEventListener);
    }

    ResponseProcessor.AudioCallback getAudioCallback(final RecognitionEventListener paramRecognitionEventListener) {
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

    public void startListening(SessionParams paramSessionParams, RecognitionEventListener paramRecognitionEventListener, Executor paramExecutor, @Nullable AudioStore paramAudioStore) {
        AudioInputParams localAudioInputParams = paramSessionParams.getAudioInputParams();
        boolean transition = false;

        if (this.mListeningState.notIn(State.IDLE)) {
            if (!localAudioInputParams.hasStreamRewindTime()) {
                Log.w("RecognizerImpl", "Multiple recognitions in progress, the first will be cancelled.");
                internalShutdownAudio();
                this.mListeningState.checkIn(State.IDLE);
            }
            transition = true;
            this.mRecognitionDispatcher.cancel();
            this.mResponseProcessor.invalidate();
            this.mResponseProcessor = null;
        }

        this.mRecognitionListener = paramRecognitionEventListener;
        RecognitionEventListener eventListener = threadChange(paramExecutor, RecognitionEventListener.class, paramRecognitionEventListener);
        recordStartRecognitionEvent(paramSessionParams);
        EngineSelector engineSelector = this.mSpeechLibFactory.buildEngineSelector(paramSessionParams);
        this.mResponseProcessor = this.mSpeechLibFactory.buildResponseProcessor(getAudioCallback(this.mRecognitionListener), eventListener, paramSessionParams, this.mSpeechLibLogger);
        List<Integer> engines = engineSelector.getEngineList();
        if (engines.isEmpty()) {
            eventListener.onError(new NoEnginesRecognizeException());
            return;
        }

        AudioInputStreamFactory localAudioInputStreamFactory;

        if (!transition) {
            localAudioInputStreamFactory = this.mAudioController.createInputStreamFactory(localAudioInputParams);
        } else {
            localAudioInputStreamFactory = this.mAudioController.rewindInputStreamFactory(localAudioInputParams.getStreamRewindTime());
        }

        try {
            if (paramAudioStore != null) {
                this.mAudioRecorder.startRecording(localAudioInputStreamFactory.createInputStream(), localAudioInputParams.getSamplingRate(), MicrophoneInputStreamFactory.getMicrophoneReadSize(localAudioInputParams.getSamplingRate()), paramAudioStore, paramSessionParams.getRequestId());
            } else {
                this.mListeningState.moveTo(State.LISTENING);
                this.mAudioController.startListening(localAudioInputParams, eventListener);
                mRecognitionDispatcher.startRecognition(mEngineStore.getEngines(engines), localAudioInputStreamFactory, paramSessionParams, engineSelector, mResponseProcessor);
            }
        } catch (IOException localIOException) {
            eventListener.onError(new AudioRecognizeException("Unable to start the audio recording", localIOException));
        }
    }

    public void startRecordedAudioRecognition(SessionParams paramSessionParams, final byte[] paramArrayOfByte, RecognitionEventListener paramRecognitionEventListener, Executor paramExecutor) {
        if (this.mListeningState.notIn(State.IDLE)) {
            Log.w("RecognizerImpl", "Multiple recognitions in progress, the first will be cancelled.");
            internalShutdownAudio();
        }
        this.mRecognitionListener = paramRecognitionEventListener;
        RecognitionEventListener localRecognitionEventListener = threadChange(paramExecutor, RecognitionEventListener.class, paramRecognitionEventListener);
        this.mListeningState.checkIn(State.IDLE);
        recordStartRecognitionEvent(paramSessionParams);
        AudioInputStreamFactory local2 = new AudioInputStreamFactory() {
            public InputStream createInputStream() {
                return new ByteArrayInputStream(paramArrayOfByte);
            }
        };
        this.mListeningState.moveTo(State.LISTENING);
        EngineSelector localEngineSelector = this.mSpeechLibFactory.buildEngineSelector(paramSessionParams);
        this.mResponseProcessor = this.mSpeechLibFactory.buildResponseProcessor(getAudioCallback(this.mRecognitionListener), localRecognitionEventListener, paramSessionParams, this.mSpeechLibLogger);
        this.mRecognitionDispatcher.startRecognition(this.mEngineStore.getEngines(localEngineSelector.getEngineList()), local2, paramSessionParams, localEngineSelector, this.mResponseProcessor);
    }

    public void stopListening(RecognitionEventListener paramRecognitionEventListener) {
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