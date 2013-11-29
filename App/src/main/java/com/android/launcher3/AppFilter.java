package com.android.launcher3;

import android.content.ComponentName;
import android.text.TextUtils;
import android.util.Log;

public abstract class AppFilter
{
  private static final boolean DBG = false;
  private static final String TAG = "AppFilter";
  
  public static AppFilter loadByName(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    try
    {
      AppFilter localAppFilter = (AppFilter)Class.forName(paramString).newInstance();
      return localAppFilter;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      Log.e("AppFilter", "Bad AppFilter class", localClassNotFoundException);
      return null;
    }
    catch (InstantiationException localInstantiationException)
    {
      Log.e("AppFilter", "Bad AppFilter class", localInstantiationException);
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Log.e("AppFilter", "Bad AppFilter class", localIllegalAccessException);
      return null;
    }
    catch (ClassCastException localClassCastException)
    {
      Log.e("AppFilter", "Bad AppFilter class", localClassCastException);
    }
    return null;
  }
  
  public abstract boolean shouldShowApp(ComponentName paramComponentName);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AppFilter
 * JD-Core Version:    0.7.0.1
 */