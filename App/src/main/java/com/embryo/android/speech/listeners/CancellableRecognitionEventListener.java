package com.embryo.android.speech.listeners;

import com.embryo.android.speech.exception.RecognizeException;
import com.google.audio.ears.proto.EarsService;
import com.google.majel.proto.MajelProtos;
import com.embryo.speech.recognizer.api.RecognizerProtos;
import com.google.speech.s3.PinholeStream;

public class CancellableRecognitionEventListener
        implements RecognitionEventListener {
    private boolean mActive = true;
    private final RecognitionEventListener mListener;

    public CancellableRecognitionEventListener(RecognitionEventListener paramRecognitionEventListener) {
        this.mListener = paramRecognitionEventListener;
    }

    public void invalidate() {
        this.mActive = false;
    }

    public void onBeginningOfSpeech(long paramLong) {
        if (this.mActive) {
            this.mListener.onBeginningOfSpeech(paramLong);
        }
    }

    public void onDone() {
        if (this.mActive) {
            this.mListener.onDone();
        }
    }

    public void onEndOfSpeech() {
        if (this.mActive) {
            this.mListener.onEndOfSpeech();
        }
    }

    public void onError(RecognizeException paramRecognizeException) {
        if (this.mActive) {
            this.mListener.onError(paramRecognizeException);
        }
    }

    public void onMediaDataResult(byte[] paramArrayOfByte) {
        if (this.mActive) {
            this.mListener.onMediaDataResult(paramArrayOfByte);
        }
    }

    public void onNoSpeechDetected() {
        if (this.mActive) {
            this.mListener.onNoSpeechDetected();
        }
    }

    public void onReadyForSpeech() {
        if (this.mActive) {
            this.mListener.onReadyForSpeech();
        }
    }

    public void onRecognitionCancelled() {
        if (this.mActive) {
            this.mListener.onRecognitionCancelled();
        }
    }

    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if (this.mActive) {
            this.mListener.onRecognitionResult(paramRecognitionEvent);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     CancellableRecognitionEventListener

 * JD-Core Version:    0.7.0.1

 */