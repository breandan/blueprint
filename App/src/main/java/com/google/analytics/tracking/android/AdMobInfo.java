package com.google.analytics.tracking.android;

import java.util.Random;

class AdMobInfo
{
  private static final AdMobInfo INSTANCE = new AdMobInfo();
  private int mAdHitId;
  private Random mRandom = new Random();
  
  static AdMobInfo getInstance()
  {
    return INSTANCE;
  }
  
  int generateAdHitId()
  {
    this.mAdHitId = (1 + this.mRandom.nextInt(2147483646));
    return this.mAdHitId;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.AdMobInfo
 * JD-Core Version:    0.7.0.1
 */