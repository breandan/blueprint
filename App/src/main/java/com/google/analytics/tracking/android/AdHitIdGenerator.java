package com.google.analytics.tracking.android;

class AdHitIdGenerator
{
  private boolean mAdMobSdkInstalled;
  
  AdHitIdGenerator()
  {
    try
    {
      if (Class.forName("com.google.ads.AdRequest") != null) {}
      for (boolean bool = true;; bool = false)
      {
        this.mAdMobSdkInstalled = bool;
        return;
      }
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      this.mAdMobSdkInstalled = false;
    }
  }
  
  int getAdHitId()
  {
    if (!this.mAdMobSdkInstalled) {
      return 0;
    }
    return AdMobInfo.getInstance().generateAdHitId();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.AdHitIdGenerator
 * JD-Core Version:    0.7.0.1
 */