package com.embryo.android.speech.listeners;

import com.embryo.android.speech.exception.RecognizeException;
import com.embryo.speech.recognizer.api.RecognizerProtos;

public class RecognitionEventListenerAdapter
        implements RecognitionEventListener {
    public void onBeginningOfSpeech(long paramLong) {
    }

    public void onDone() {
    }

    public void onEndOfSpeech() {
    }

    public void onError(RecognizeException paramRecognizeException) {
    }

    public void onMediaDataResult(byte[] paramArrayOfByte) {
    }

    public void onMusicDetected() {
    }

    public void onNoSpeechDetected() {
    }

    public void onReadyForSpeech() {
    }

    public void onRecognitionCancelled() {
    }

    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEventListenerAdapter

 * JD-Core Version:    0.7.0.1

 */