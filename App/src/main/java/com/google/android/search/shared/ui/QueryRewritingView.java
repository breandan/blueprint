package com.google.android.search.shared.ui;

import android.text.Layout;
import android.text.TextPaint;

public abstract interface QueryRewritingView
{
  public abstract void removeAllViews();
  
  public abstract void start(String paramString, Layout paramLayout, TextPaint paramTextPaint, int paramInt, Callback paramCallback);
  
  public abstract void switchText(String paramString, Layout paramLayout, int paramInt);
  
  public static abstract interface Callback
  {
    public abstract void onAnimationComplete();
    
    public abstract void onSetupComplete();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.QueryRewritingView
 * JD-Core Version:    0.7.0.1
 */