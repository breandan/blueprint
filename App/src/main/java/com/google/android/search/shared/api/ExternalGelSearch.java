package com.google.android.search.shared.api;

import com.google.android.shared.util.SpeechLevelSource;

public abstract interface ExternalGelSearch
{
  public abstract void commit(Query paramQuery, Callback paramCallback);
  
  public static abstract interface Callback
  {
    public abstract void onStatusChanged(int paramInt, SpeechLevelSource paramSpeechLevelSource);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.api.ExternalGelSearch
 * JD-Core Version:    0.7.0.1
 */