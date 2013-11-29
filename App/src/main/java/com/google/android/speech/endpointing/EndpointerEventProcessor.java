package com.google.android.speech.endpointing;

import com.google.speech.recognizer.api.RecognizerProtos.EndpointerEvent;

public abstract interface EndpointerEventProcessor
{
  public abstract void process(RecognizerProtos.EndpointerEvent paramEndpointerEvent);
  
  public abstract void updateProgress(int paramInt, long paramLong);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.endpointing.EndpointerEventProcessor
 * JD-Core Version:    0.7.0.1
 */