package com.embryo.android.speech.listeners;

import com.embryo.android.speech.exception.RecognizeException;
import com.google.audio.ears.proto.EarsService;
import com.embryo.speech.recognizer.api.RecognizerProtos;
import com.google.speech.s3.PinholeStream;

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

    public void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput) {
    }

    public void onReadyForSpeech() {
    }

    public void onRecognitionCancelled() {
    }

    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
    }

    public void onSoundSearchResult(EarsService.EarsResultsResponse paramEarsResultsResponse) {
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEventListenerAdapter

 * JD-Core Version:    0.7.0.1

 */