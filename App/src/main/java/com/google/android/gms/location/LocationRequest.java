package com.google.android.gms.location;

import android.os.Parcel;
import android.os.SystemClock;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dr;

public final class LocationRequest
  implements SafeParcelable
{
  public static final LocationRequestCreator CREATOR = new LocationRequestCreator();
  private final int is;
  int mPriority;
  long sP;
  long sW;
  long sX;
  boolean sY;
  int sZ;
  float ta;
  
  public LocationRequest()
  {
    this.is = 1;
    this.mPriority = 102;
    this.sW = 3600000L;
    this.sX = 600000L;
    this.sY = false;
    this.sP = 9223372036854775807L;
    this.sZ = 2147483647;
    this.ta = 0.0F;
  }
  
  LocationRequest(int paramInt1, int paramInt2, long paramLong1, long paramLong2, boolean paramBoolean, long paramLong3, int paramInt3, float paramFloat)
  {
    this.is = paramInt1;
    this.mPriority = paramInt2;
    this.sW = paramLong1;
    this.sX = paramLong2;
    this.sY = paramBoolean;
    this.sP = paramLong3;
    this.sZ = paramInt3;
    this.ta = paramFloat;
  }
  
  private static void bn(int paramInt)
  {
    switch (paramInt)
    {
    case 101: 
    case 103: 
    default: 
      throw new IllegalArgumentException("invalid quality: " + paramInt);
    }
  }
  
  public static String bo(int paramInt)
  {
    switch (paramInt)
    {
    case 101: 
    case 103: 
    default: 
      return "???";
    case 100: 
      return "PRIORITY_HIGH_ACCURACY";
    case 102: 
      return "PRIORITY_BALANCED_POWER_ACCURACY";
    case 104: 
      return "PRIORITY_LOW_POWER";
    }
    return "PRIORITY_NO_POWER";
  }
  
  public static LocationRequest create()
  {
    return new LocationRequest();
  }
  
  private static void h(long paramLong)
  {
    if (paramLong < 0L) {
      throw new IllegalArgumentException("invalid interval: " + paramLong);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    LocationRequest localLocationRequest;
    do
    {
      return true;
      if (!(paramObject instanceof LocationRequest)) {
        return false;
      }
      localLocationRequest = (LocationRequest)paramObject;
    } while ((this.mPriority == localLocationRequest.mPriority) && (this.sW == localLocationRequest.sW) && (this.sX == localLocationRequest.sX) && (this.sY == localLocationRequest.sY) && (this.sP == localLocationRequest.sP) && (this.sZ == localLocationRequest.sZ) && (this.ta == localLocationRequest.ta));
    return false;
  }
  
  int getVersionCode()
  {
    return this.is;
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[7];
    arrayOfObject[0] = Integer.valueOf(this.mPriority);
    arrayOfObject[1] = Long.valueOf(this.sW);
    arrayOfObject[2] = Long.valueOf(this.sX);
    arrayOfObject[3] = Boolean.valueOf(this.sY);
    arrayOfObject[4] = Long.valueOf(this.sP);
    arrayOfObject[5] = Integer.valueOf(this.sZ);
    arrayOfObject[6] = Float.valueOf(this.ta);
    return dr.hashCode(arrayOfObject);
  }
  
  public LocationRequest setFastestInterval(long paramLong)
  {
    h(paramLong);
    this.sY = true;
    this.sX = paramLong;
    return this;
  }
  
  public LocationRequest setInterval(long paramLong)
  {
    h(paramLong);
    this.sW = paramLong;
    if (!this.sY) {
      this.sX = ((this.sW / 6.0D));
    }
    return this;
  }
  
  public LocationRequest setPriority(int paramInt)
  {
    bn(paramInt);
    this.mPriority = paramInt;
    return this;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Request[").append(bo(this.mPriority));
    if (this.mPriority != 105)
    {
      localStringBuilder.append(" requested=");
      localStringBuilder.append(this.sW + "ms");
    }
    localStringBuilder.append(" fastest=");
    localStringBuilder.append(this.sX + "ms");
    if (this.sP != 9223372036854775807L)
    {
      long l = this.sP - SystemClock.elapsedRealtime();
      localStringBuilder.append(" expireIn=");
      localStringBuilder.append(l + "ms");
    }
    if (this.sZ != 2147483647) {
      localStringBuilder.append(" num=").append(this.sZ);
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    LocationRequestCreator.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.LocationRequest
 * JD-Core Version:    0.7.0.1
 */