package com.google.speech.recognizer;

import com.google.speech.recognizer.api.RecognizerProtos;

public abstract interface RecognizerCallback {
    public abstract void handleAudioLevelEvent(RecognizerProtos.AudioLevelEvent paramAudioLevelEvent);

    public abstract void handleEndpointerEvent(RecognizerProtos.EndpointerEvent paramEndpointerEvent);

    public abstract void handleRecognitionEvent(RecognizerProtos.RecognitionEvent paramRecognitionEvent);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.recognizer.RecognizerCallback

 * JD-Core Version:    0.7.0.1

 */