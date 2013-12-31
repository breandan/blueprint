package com.embryo.android.speech.internal;

import com.embryo.android.speech.RecognitionResponseWrapper;

public class Greco3CallbackImpl
        implements com.embryo.android.speech.embedded.Greco3Callback {
    private final com.embryo.android.speech.callback.RecognitionEngineCallback mCallback;
    private final RecognizerEventProcessor mRecognitionEventProcessor;

    public Greco3CallbackImpl(com.embryo.android.speech.embedded.Greco3Mode paramGreco3Mode, com.embryo.android.speech.callback.RecognitionEngineCallback paramRecognitionEngineCallback) {
        this.mRecognitionEventProcessor = new RecognizerEventProcessor(paramGreco3Mode, new RecognitionResponseWrapper(paramRecognitionEngineCallback, 1));
        this.mCallback = paramRecognitionEngineCallback;
    }

    public void handleAudioLevelEvent(com.embryo.speech.recognizer.api.RecognizerProtos.AudioLevelEvent paramAudioLevelEvent) {
    }

    public void handleEndpointerEvent(com.embryo.speech.recognizer.api.RecognizerProtos.EndpointerEvent paramEndpointerEvent) {
        this.mCallback.onResult(new com.embryo.android.speech.RecognitionResponse(1, paramEndpointerEvent));
    }

    public void handleError(com.embryo.android.speech.exception.RecognizeException paramRecognizeException) {
        paramRecognizeException.setEngine(1);
        this.mCallback.onError(paramRecognizeException);
    }

    public void handleProgressUpdate(long paramLong) {
        this.mCallback.onProgressUpdate(1, paramLong);
    }

    public void handleRecognitionEvent(com.embryo.speech.recognizer.api.RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mRecognitionEventProcessor.process(paramRecognitionEvent);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3CallbackImpl

 * JD-Core Version:    0.7.0.1

 */