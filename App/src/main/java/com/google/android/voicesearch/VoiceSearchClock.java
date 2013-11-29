package com.google.android.voicesearch;

import android.os.SystemClock;

public class VoiceSearchClock
{
  protected static VoiceSearchClock mVoiceSearchClock;
  
  public static long elapsedRealtime()
  {
    if (mVoiceSearchClock == null) {
      return SystemClock.elapsedRealtime();
    }
    return mVoiceSearchClock.internalElapsedRealtime();
  }
  
  protected long internalElapsedRealtime()
  {
    return 0L;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.VoiceSearchClock
 * JD-Core Version:    0.7.0.1
 */