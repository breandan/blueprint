package com.google.android.speech.helper;

import javax.annotation.Nullable;

public abstract interface SpeechLocationHelper
{
  @Nullable
  public abstract String getXGeoLocation();
  
  public abstract boolean shouldSendLocation();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.helper.SpeechLocationHelper
 * JD-Core Version:    0.7.0.1
 */