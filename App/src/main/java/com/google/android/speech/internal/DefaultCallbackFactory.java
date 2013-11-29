package com.google.android.speech.internal;

import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.embedded.Greco3Callback;
import com.google.android.speech.embedded.Greco3CallbackFactory;
import com.google.android.speech.embedded.Greco3Mode;

public class DefaultCallbackFactory
  implements Greco3CallbackFactory
{
  public Greco3Callback create(RecognitionEngineCallback paramRecognitionEngineCallback, Greco3Mode paramGreco3Mode)
  {
    return new Greco3CallbackImpl(paramGreco3Mode, paramRecognitionEngineCallback);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.internal.DefaultCallbackFactory
 * JD-Core Version:    0.7.0.1
 */