package com.google.android.launcher;

import android.content.ComponentName;
import com.android.launcher3.AppFilter;
import com.google.android.search.shared.ondevice.GelVelAppFilter;

public class GelAppFilter
  extends AppFilter
{
  public boolean shouldShowApp(ComponentName paramComponentName)
  {
    return GelVelAppFilter.shouldShowApp(paramComponentName.getPackageName(), paramComponentName.getClassName(), false);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.launcher.GelAppFilter
 * JD-Core Version:    0.7.0.1
 */