package com.embryo.android.speech;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public abstract interface Recognizer {
    public abstract void cancel(com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener);

    public abstract void startListening(com.embryo.android.speech.params.SessionParams paramSessionParams, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, Executor paramExecutor, @Nullable com.embryo.android.speech.audio.AudioStore paramAudioStore);

    public abstract void startRecordedAudioRecognition(com.embryo.android.speech.params.SessionParams paramSessionParams, byte[] paramArrayOfByte, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, Executor paramExecutor);

    public abstract void stopListening(com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Recognizer

 * JD-Core Version:    0.7.0.1

 */