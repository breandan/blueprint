package com.google.speech.recognizer;

import com.google.speech.recognizer.api.RecognizerProtos.AudioLevelEvent;
import com.google.speech.recognizer.api.RecognizerProtos.EndpointerEvent;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;

public abstract interface RecognizerCallback
{
  public abstract void handleAudioLevelEvent(RecognizerProtos.AudioLevelEvent paramAudioLevelEvent);
  
  public abstract void handleEndpointerEvent(RecognizerProtos.EndpointerEvent paramEndpointerEvent);
  
  public abstract void handleRecognitionEvent(RecognizerProtos.RecognitionEvent paramRecognitionEvent);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.recognizer.RecognizerCallback
 * JD-Core Version:    0.7.0.1
 */