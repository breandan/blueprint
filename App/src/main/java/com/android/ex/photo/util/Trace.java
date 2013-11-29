package com.android.ex.photo.util;

import android.os.Build.VERSION;

public abstract class Trace
{
  public static void beginSection(String paramString)
  {
    if (Build.VERSION.SDK_INT >= 18) {
      android.os.Trace.beginSection(paramString);
    }
  }
  
  public static void endSection()
  {
    if (Build.VERSION.SDK_INT >= 18) {
      android.os.Trace.endSection();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.util.Trace
 * JD-Core Version:    0.7.0.1
 */