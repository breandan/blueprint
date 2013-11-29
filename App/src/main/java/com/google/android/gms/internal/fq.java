package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Locale;

public class fq
  implements SafeParcelable
{
  public static final fr CREATOR = new fr();
  private final int is;
  private final String sN;
  private final int sO;
  private final short sQ;
  private final double sR;
  private final double sS;
  private final float sT;
  private final int sU;
  private final int sV;
  private final long tp;
  
  public fq(int paramInt1, String paramString, int paramInt2, short paramShort, double paramDouble1, double paramDouble2, float paramFloat, long paramLong, int paramInt3, int paramInt4)
  {
    ac(paramString);
    b(paramFloat);
    a(paramDouble1, paramDouble2);
    int i = bq(paramInt2);
    this.is = paramInt1;
    this.sQ = paramShort;
    this.sN = paramString;
    this.sR = paramDouble1;
    this.sS = paramDouble2;
    this.sT = paramFloat;
    this.tp = paramLong;
    this.sO = i;
    this.sU = paramInt3;
    this.sV = paramInt4;
  }
  
  private static void a(double paramDouble1, double paramDouble2)
  {
    if ((paramDouble1 > 90.0D) || (paramDouble1 < -90.0D)) {
      throw new IllegalArgumentException("invalid latitude: " + paramDouble1);
    }
    if ((paramDouble2 > 180.0D) || (paramDouble2 < -180.0D)) {
      throw new IllegalArgumentException("invalid longitude: " + paramDouble2);
    }
  }
  
  private static void ac(String paramString)
  {
    if ((paramString == null) || (paramString.length() > 100)) {
      throw new IllegalArgumentException("requestId is null or too long: " + paramString);
    }
  }
  
  private static void b(float paramFloat)
  {
    if (paramFloat <= 0.0F) {
      throw new IllegalArgumentException("invalid radius: " + paramFloat);
    }
  }
  
  private static int bq(int paramInt)
  {
    int i = paramInt & 0x7;
    if (i == 0) {
      throw new IllegalArgumentException("No supported transition specified: " + paramInt);
    }
    return i;
  }
  
  private static String br(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    }
    return "CIRCLE";
  }
  
  public short cs()
  {
    return this.sQ;
  }
  
  public float ct()
  {
    return this.sT;
  }
  
  public int cu()
  {
    return this.sO;
  }
  
  public int cv()
  {
    return this.sV;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    fq localfq;
    do
    {
      return true;
      if (paramObject == null) {
        return false;
      }
      if (!(paramObject instanceof fq)) {
        return false;
      }
      localfq = (fq)paramObject;
      if (this.sT != localfq.sT) {
        return false;
      }
      if (this.sR != localfq.sR) {
        return false;
      }
      if (this.sS != localfq.sS) {
        return false;
      }
    } while (this.sQ == localfq.sQ);
    return false;
  }
  
  public long getExpirationTime()
  {
    return this.tp;
  }
  
  public double getLatitude()
  {
    return this.sR;
  }
  
  public double getLongitude()
  {
    return this.sS;
  }
  
  public int getNotificationResponsiveness()
  {
    return this.sU;
  }
  
  public String getRequestId()
  {
    return this.sN;
  }
  
  public int getVersionCode()
  {
    return this.is;
  }
  
  public int hashCode()
  {
    long l1 = Double.doubleToLongBits(this.sR);
    int i = 31 + (int)(l1 ^ l1 >>> 32);
    long l2 = Double.doubleToLongBits(this.sS);
    return 31 * (31 * (31 * (i * 31 + (int)(l2 ^ l2 >>> 32)) + Float.floatToIntBits(this.sT)) + this.sQ) + this.sO;
  }
  
  public String toString()
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[9];
    arrayOfObject[0] = br(this.sQ);
    arrayOfObject[1] = this.sN;
    arrayOfObject[2] = Integer.valueOf(this.sO);
    arrayOfObject[3] = Double.valueOf(this.sR);
    arrayOfObject[4] = Double.valueOf(this.sS);
    arrayOfObject[5] = Float.valueOf(this.sT);
    arrayOfObject[6] = Integer.valueOf(this.sU / 1000);
    arrayOfObject[7] = Integer.valueOf(this.sV);
    arrayOfObject[8] = Long.valueOf(this.tp);
    return String.format(localLocale, "Geofence[%s id:%s transitions:%d %.6f, %.6f %.0fm, resp=%ds, dwell=%dms, @%d]", arrayOfObject);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    fr.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.fq
 * JD-Core Version:    0.7.0.1
 */