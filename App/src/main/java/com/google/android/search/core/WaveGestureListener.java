package com.google.android.search.core;

import android.content.Context;

public abstract interface WaveGestureListener
{
  public abstract void startListening(Context paramContext, Observer paramObserver);
  
  public abstract void stopListening(Context paramContext, Observer paramObserver);
  
  public static abstract interface Observer
  {
    public abstract void onWaveGesture();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.WaveGestureListener
 * JD-Core Version:    0.7.0.1
 */