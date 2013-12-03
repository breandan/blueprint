package com.google.android.speech.internal;

import com.google.android.speech.RecognitionResponse;
import com.google.android.speech.RecognitionResponseWrapper;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.embedded.Greco3Callback;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.exception.RecognizeException;

public class Greco3CallbackImpl
        implements Greco3Callback {
    private final RecognitionEngineCallback mCallback;
    private final RecognizerEventProcessor mRecognitionEventProcessor;

    public Greco3CallbackImpl(Greco3Mode paramGreco3Mode, RecognitionEngineCallback paramRecognitionEngineCallback) {
        this.mRecognitionEventProcessor = new RecognizerEventProcessor(paramGreco3Mode, new RecognitionResponseWrapper(paramRecognitionEngineCallback, 1));
        this.mCallback = paramRecognitionEngineCallback;
    }

    public void handleAudioLevelEvent(RecognizerProtos.AudioLevelEvent paramAudioLevelEvent) {
    }

    public void handleEndpointerEvent(RecognizerProtos.EndpointerEvent paramEndpointerEvent) {
        this.mCallback.onResult(new RecognitionResponse(1, paramEndpointerEvent));
    }

    public void handleError(RecognizeException paramRecognizeException) {
        paramRecognizeException.setEngine(1);
        this.mCallback.onError(paramRecognizeException);
    }

    public void handleProgressUpdate(long paramLong) {
        this.mCallback.onProgressUpdate(1, paramLong);
    }

    public void handleRecognitionEvent(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mRecognitionEventProcessor.process(paramRecognitionEvent);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.internal.Greco3CallbackImpl

 * JD-Core Version:    0.7.0.1

 */