package com.google.android.search.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DeviceCapabilityManagerImpl
  implements DeviceCapabilityManager
{
  private final Context mContext;
  private boolean mInited;
  private boolean mTelephoneCapable;
  
  public DeviceCapabilityManagerImpl(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  private static boolean isTelephoneCapable(TelephonyManager paramTelephonyManager)
  {
    try
    {
      Method localMethod2 = TelephonyManager.class.getMethod("isVoiceCapable", new Class[0]);
      localMethod1 = localMethod2;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;)
      {
        label42:
        Method localMethod1 = null;
      }
    }
    if (localMethod1 != null) {}
    try
    {
      boolean bool = ((Boolean)localMethod1.invoke(paramTelephonyManager, (Object[])null)).booleanValue();
      return bool;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      return true;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      break label42;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      break label42;
    }
  }
  
  private void maybeInit()
  {
    for (;;)
    {
      try
      {
        boolean bool = this.mInited;
        if (bool) {
          return;
        }
        TelephonyManager localTelephonyManager = (TelephonyManager)this.mContext.getSystemService("phone");
        if ((localTelephonyManager != null) && (isTelephoneCapable(localTelephonyManager)))
        {
          this.mTelephoneCapable = true;
          this.mInited = true;
        }
        else
        {
          this.mTelephoneCapable = false;
        }
      }
      finally {}
    }
  }
  
  public boolean hasRearFacingCamera()
  {
    return this.mContext.getPackageManager().hasSystemFeature("android.hardware.camera");
  }
  
  public boolean isTelephoneCapable()
  {
    maybeInit();
    return this.mTelephoneCapable;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.DeviceCapabilityManagerImpl
 * JD-Core Version:    0.7.0.1
 */