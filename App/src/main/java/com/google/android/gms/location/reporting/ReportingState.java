package com.google.android.gms.location.reporting;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dr;

public class ReportingState
  implements SafeParcelable
{
  public static final d CREATOR = new d();
  private final int is;
  private final int tA;
  private final int tv;
  private final int tw;
  private final boolean tx;
  private final boolean ty;
  private final boolean tz;
  
  public ReportingState(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt4)
  {
    this.is = paramInt1;
    this.tv = paramInt2;
    this.tw = paramInt3;
    this.tx = paramBoolean1;
    this.ty = paramBoolean2;
    this.tz = paramBoolean3;
    this.tA = paramInt4;
  }
  
  public ReportingState(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt3)
  {
    this(1, paramInt1, paramInt2, paramBoolean1, paramBoolean2, paramBoolean3, paramInt3);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ReportingState)) {}
    ReportingState localReportingState;
    do
    {
      return false;
      localReportingState = (ReportingState)paramObject;
    } while ((this.tv != localReportingState.tv) || (this.tw != localReportingState.tw) || (this.tx != localReportingState.tx) || (this.ty != localReportingState.ty) || (this.tz != localReportingState.tz) || (this.tA != localReportingState.tA));
    return true;
  }
  
  public int getExpectedOptInResult()
  {
    return OptInResult.sanitize(this.tA);
  }
  
  public int getHistoryEnabled()
  {
    return Setting.sanitize(this.tw);
  }
  
  public int getReportingEnabled()
  {
    return Setting.sanitize(this.tv);
  }
  
  int getVersionCode()
  {
    return this.is;
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[6];
    arrayOfObject[0] = Integer.valueOf(this.tv);
    arrayOfObject[1] = Integer.valueOf(this.tw);
    arrayOfObject[2] = Boolean.valueOf(this.tx);
    arrayOfObject[3] = Boolean.valueOf(this.ty);
    arrayOfObject[4] = Boolean.valueOf(this.tz);
    arrayOfObject[5] = Integer.valueOf(this.tA);
    return dr.hashCode(arrayOfObject);
  }
  
  public boolean isActive()
  {
    return this.ty;
  }
  
  public boolean isAllowed()
  {
    return this.tx;
  }
  
  public boolean isDeferringToMaps()
  {
    return this.tz;
  }
  
  public String toString()
  {
    return "ReportingState{mReportingEnabled=" + this.tv + ", mHistoryEnabled=" + this.tw + ", mAllowed=" + this.tx + ", mActive=" + this.ty + ", mDefer=" + this.tz + ", mExpectedOptInResult=" + this.tA + ", mVersionCode=" + this.is + '}';
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    d.a(this, paramParcel, paramInt);
  }
  
  public static final class Setting
  {
    public static boolean isOn(int paramInt)
    {
      return paramInt > 0;
    }
    
    public static int sanitize(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        if (!isOn(paramInt)) {
          break;
        }
      }
      for (int i = 99;; i = -3)
      {
        paramInt = i;
        return paramInt;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.reporting.ReportingState
 * JD-Core Version:    0.7.0.1
 */