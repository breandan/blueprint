package com.google.android.search.core.summons;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class SourceUtil
{
  public static Drawable getIcon(Context paramContext, Source paramSource)
  {
    Drawable localDrawable = loadSourceIcon(paramContext, paramSource);
    if (localDrawable == null) {
      localDrawable = paramContext.getResources().getDrawable(2130838047);
    }
    return localDrawable;
  }
  
  public static CharSequence getLabel(Context paramContext, Source paramSource)
  {
    Object localObject = getTextFromResource(paramContext, paramSource, paramSource.getLabelResourceId());
    if ((localObject == null) && (paramSource.getApplicationInfo().labelRes != 0)) {
      localObject = getTextFromResource(paramContext, paramSource, paramSource.getApplicationInfo().labelRes);
    }
    if (localObject == null) {
      localObject = paramSource.getPackageName();
    }
    return localObject;
  }
  
  public static CharSequence getTextFromResource(Context paramContext, Source paramSource, int paramInt)
  {
    if (paramInt == 0) {
      return null;
    }
    return paramContext.getPackageManager().getText(paramSource.getPackageName(), paramInt, paramSource.getApplicationInfo());
  }
  
  private static Drawable loadSourceIcon(Context paramContext, Source paramSource)
  {
    if (paramSource.getSourceIconResource() == 0) {
      return null;
    }
    return paramContext.getPackageManager().getDrawable(paramSource.getPackageName(), paramSource.getSourceIconResource(), paramSource.getApplicationInfo());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.SourceUtil
 * JD-Core Version:    0.7.0.1
 */