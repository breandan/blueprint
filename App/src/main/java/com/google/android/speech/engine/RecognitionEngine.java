package com.google.android.speech.engine;

import com.google.android.speech.audio.AudioInputStreamFactory;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.params.SessionParams;

public abstract interface RecognitionEngine
{
  public abstract void close();
  
  public abstract void startRecognition(AudioInputStreamFactory paramAudioInputStreamFactory, RecognitionEngineCallback paramRecognitionEngineCallback, SessionParams paramSessionParams);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.engine.RecognitionEngine
 * JD-Core Version:    0.7.0.1
 */