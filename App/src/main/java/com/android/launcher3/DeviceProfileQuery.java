package com.android.launcher3;

import android.graphics.PointF;

class DeviceProfileQuery
{
  PointF dimens;
  float heightDps;
  float value;
  float widthDps;
  
  DeviceProfileQuery(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    this.widthDps = paramFloat1;
    this.heightDps = paramFloat2;
    this.value = paramFloat3;
    this.dimens = new PointF(paramFloat1, paramFloat2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.DeviceProfileQuery
 * JD-Core Version:    0.7.0.1
 */