package com.google.android.speech;

import com.google.android.speech.audio.AudioStore;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.params.SessionParams;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public abstract interface Recognizer {
    public abstract void cancel(RecognitionEventListener paramRecognitionEventListener);

    public abstract void startListening(SessionParams paramSessionParams, RecognitionEventListener paramRecognitionEventListener, Executor paramExecutor, @Nullable AudioStore paramAudioStore);

    public abstract void startRecordedAudioRecognition(SessionParams paramSessionParams, byte[] paramArrayOfByte, RecognitionEventListener paramRecognitionEventListener, Executor paramExecutor);

    public abstract void stopListening(RecognitionEventListener paramRecognitionEventListener);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.Recognizer

 * JD-Core Version:    0.7.0.1

 */