package com.embryo.android.speech.engine;

public abstract interface RecognitionEngine {
    public abstract void close();

    public abstract void startRecognition(com.embryo.android.speech.audio.AudioInputStreamFactory paramAudioInputStreamFactory, com.embryo.android.speech.callback.RecognitionEngineCallback paramRecognitionEngineCallback, com.embryo.android.speech.params.SessionParams paramSessionParams);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEngine

 * JD-Core Version:    0.7.0.1

 */