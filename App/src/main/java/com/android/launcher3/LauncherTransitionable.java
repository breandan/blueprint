package com.android.launcher3;

import android.view.View;

abstract interface LauncherTransitionable
{
  public abstract View getContent();
  
  public abstract void onLauncherTransitionEnd(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void onLauncherTransitionPrepare(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void onLauncherTransitionStart(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void onLauncherTransitionStep(Launcher paramLauncher, float paramFloat);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.LauncherTransitionable
 * JD-Core Version:    0.7.0.1
 */