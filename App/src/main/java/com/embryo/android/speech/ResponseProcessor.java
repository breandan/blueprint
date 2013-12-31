package com.embryo.android.speech;

import com.embryo.android.speech.message.S3ResponseProcessor;
import com.embryo.speech.s3.S3;

public class ResponseProcessor
        implements com.embryo.android.speech.callback.RecognitionEngineCallback {
    private final AudioCallback mAudioCallback;
    private final com.embryo.android.speech.endpointing.EndpointerEventProcessor mEndpointerEventProcessor;
    private final com.embryo.android.speech.listeners.RecognitionEventListener mEventListener;
    private boolean mInvalid = false;
    private final com.embryo.android.speech.logger.SpeechLibLogger mLogger;
    private final S3ResponseProcessor mS3ResponseProcessor;
    private final com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck mThreadCheck = com.embryo.android.shared.util.ExtraPreconditions.createSameThreadCheck();

    public ResponseProcessor(AudioCallback paramAudioCallback, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, boolean paramBoolean, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.EndpointerParams paramEndpointerParams, S3ResponseProcessor paramS3ResponseProcessor, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger) {
        this.mAudioCallback = paramAudioCallback;
        this.mEventListener = new InternalRecognitionEventListener(paramRecognitionEventListener, paramSpeechLibLogger, paramAudioCallback, paramBoolean);
        this.mEndpointerEventProcessor = new com.embryo.android.speech.endpointing.DefaultEndpointerEventProcessor(this.mEventListener, paramEndpointerParams);
        this.mS3ResponseProcessor = paramS3ResponseProcessor;
        this.mLogger = paramSpeechLibLogger;
    }

    private void handleS3Response(S3.S3Response s3Response) {
        if(s3Response.getStatus() == 0x1) {
            mLogger.logS3ConnectionDone();
            mAudioCallback.shutdownAudio();
        } else if(s3Response.getStatus() == 0x2) {
            mLogger.logS3ConnectionError();
            mAudioCallback.shutdownAudio();
        } else if((s3Response.getStatus() == 0) && (s3Response.hasRecognizerEventExtension()) && (s3Response.getRecognizerEventExtension().hasRecognitionEvent()) && (s3Response.getRecognizerEventExtension().getRecognitionEvent().getEventType() == 0x1)) {
            mLogger.logS3RecognitionCompleted();
            mAudioCallback.stopAudio();
        }
        mS3ResponseProcessor.process(s3Response, mEventListener);
    }

    public void invalidate() {
        this.mThreadCheck.check();
        this.mInvalid = true;
    }

    public void onError(com.embryo.android.speech.exception.RecognizeException paramRecognizeException) {
        this.mThreadCheck.check();
        if (this.mInvalid) {
            return;
        }
        this.mAudioCallback.shutdownAudio();
        this.mEventListener.onError(paramRecognizeException);
    }

    public void onProgressUpdate(int paramInt, long paramLong) {
        this.mThreadCheck.check();
        if (this.mInvalid) {
            return;
        }
        this.mEndpointerEventProcessor.updateProgress(paramInt, paramLong);
    }

    public void onRecognitionCancelled() {
        this.mThreadCheck.check();
        if (this.mInvalid) {
            return;
        }
        this.mEventListener.onRecognitionCancelled();
    }

    public void onResult(RecognitionResponse paramRecognitionResponse) {
        this.mThreadCheck.check();
        if (this.mInvalid) {
            return;
        }
        int i = paramRecognitionResponse.getType();
        if (i == 3) {
            //handleMusicDetectorResponse((Boolean) paramRecognitionResponse.get(3));
            return;
        }
        if (i == 2) {
            com.embryo.speech.recognizer.api.RecognizerProtos.EndpointerEvent localEndpointerEvent = paramRecognitionResponse.get(2);
            this.mEndpointerEventProcessor.process(localEndpointerEvent);
            return;
        }
        if (i == 1) {
            handleS3Response((S3.S3Response) paramRecognitionResponse.get(1));
            return;
        }
        throw new IllegalArgumentException("Unknown response type: ");
    }

    public static abstract interface AudioCallback {
        public abstract void recordingStarted(long paramLong);

        public abstract void shutdownAudio();

        public abstract void stopAudio();
    }

    static final class InternalRecognitionEventListener
            implements com.embryo.android.speech.listeners.RecognitionEventListener {
        private final ResponseProcessor.AudioCallback mAudioCallback;
        private final com.embryo.android.speech.listeners.RecognitionEventListener mDelegate;
        private final com.embryo.android.speech.logger.SpeechLibLogger mLogger;
        private final boolean mStopOnEndOfSpeech;

        InternalRecognitionEventListener(com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger, ResponseProcessor.AudioCallback paramAudioCallback, boolean paramBoolean) {
            this.mDelegate = paramRecognitionEventListener;
            this.mLogger = paramSpeechLibLogger;
            this.mAudioCallback = paramAudioCallback;
            this.mStopOnEndOfSpeech = paramBoolean;
        }

        public void onBeginningOfSpeech(long paramLong) {
            this.mAudioCallback.recordingStarted(paramLong);
            this.mDelegate.onBeginningOfSpeech(paramLong);
        }

        public void onDone() {
            this.mDelegate.onDone();
        }

        public void onEndOfSpeech() {
            if (this.mStopOnEndOfSpeech) {
                this.mAudioCallback.stopAudio();
            }
            this.mLogger.logEndOfSpeech();
            this.mDelegate.onEndOfSpeech();
        }

        public void onError(com.embryo.android.speech.exception.RecognizeException paramRecognizeException) {
            this.mDelegate.onError(paramRecognizeException);
        }

        public void onMediaDataResult(byte[] paramArrayOfByte) {
            this.mLogger.logS3TtsReceived();
            this.mDelegate.onMediaDataResult(paramArrayOfByte);
        }

        public void onNoSpeechDetected() {
            this.mLogger.logNoSpeechDetected();
            this.mDelegate.onNoSpeechDetected();
        }

        public void onReadyForSpeech() {
            this.mDelegate.onReadyForSpeech();
        }

        public void onRecognitionCancelled() {
            this.mDelegate.onRecognitionCancelled();
        }

        public void onRecognitionResult(com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
            this.mDelegate.onRecognitionResult(paramRecognitionEvent);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ResponseProcessor

 * JD-Core Version:    0.7.0.1

 */