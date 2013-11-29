package com.google.android.gms.location.reporting;

import android.accounts.Account;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dr;
import com.google.android.gms.internal.ds;
import com.google.android.gms.internal.fu;

public class UploadRequest
  implements SafeParcelable
{
  public static final e CREATOR = new e();
  private final int is;
  private final String tB;
  private final long tC;
  private final long tD;
  private final long tE;
  private final String tF;
  private final Account tr;
  
  UploadRequest(int paramInt, Account paramAccount, String paramString1, long paramLong1, long paramLong2, long paramLong3, String paramString2)
  {
    this.is = paramInt;
    this.tr = paramAccount;
    this.tB = paramString1;
    this.tC = paramLong1;
    this.tD = paramLong2;
    this.tE = paramLong3;
    this.tF = paramString2;
  }
  
  private UploadRequest(Builder paramBuilder)
  {
    this.is = 1;
    this.tr = Builder.a(paramBuilder);
    this.tB = Builder.b(paramBuilder);
    this.tC = Builder.c(paramBuilder);
    this.tD = Builder.d(paramBuilder);
    this.tE = Builder.e(paramBuilder);
    this.tF = Builder.f(paramBuilder);
  }
  
  public static Builder builder(Account paramAccount, String paramString, long paramLong)
  {
    return new Builder(paramAccount, paramString, paramLong, null);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    UploadRequest localUploadRequest;
    do
    {
      return true;
      if (!(paramObject instanceof UploadRequest)) {
        return false;
      }
      localUploadRequest = (UploadRequest)paramObject;
    } while ((this.tr.equals(localUploadRequest.tr)) && (this.tB.equals(localUploadRequest.tB)) && (dr.equal(Long.valueOf(this.tC), Long.valueOf(localUploadRequest.tC))) && (this.tD == localUploadRequest.tD) && (this.tE == localUploadRequest.tE) && (dr.equal(this.tF, localUploadRequest.tF)));
    return false;
  }
  
  public Account getAccount()
  {
    return this.tr;
  }
  
  public String getAppSpecificKey()
  {
    return this.tF;
  }
  
  public long getDurationMillis()
  {
    return this.tC;
  }
  
  public long getMovingLatencyMillis()
  {
    return this.tD;
  }
  
  public String getReason()
  {
    return this.tB;
  }
  
  public long getStationaryLatencyMillis()
  {
    return this.tE;
  }
  
  int getVersionCode()
  {
    return this.is;
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[6];
    arrayOfObject[0] = this.tr;
    arrayOfObject[1] = this.tB;
    arrayOfObject[2] = Long.valueOf(this.tC);
    arrayOfObject[3] = Long.valueOf(this.tD);
    arrayOfObject[4] = Long.valueOf(this.tE);
    arrayOfObject[5] = this.tF;
    return dr.hashCode(arrayOfObject);
  }
  
  public String toString()
  {
    return "UploadRequest{mVersionCode=" + this.is + ", mAccount=" + fu.a(this.tr) + ", mReason='" + this.tB + '\'' + ", mDurationMillis=" + this.tC + ", mMovingLatencyMillis=" + this.tD + ", mStationaryLatencyMillis=" + this.tE + ", mAppSpecificKey='" + this.tF + '\'' + '}';
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    e.a(this, paramParcel, paramInt);
  }
  
  public static class Builder
  {
    private final String tB;
    private final long tC;
    private String tF;
    private long tG = 9223372036854775807L;
    private long tH = 9223372036854775807L;
    private final Account tr;
    
    private Builder(Account paramAccount, String paramString, long paramLong)
    {
      this.tr = ((Account)ds.a(paramAccount, "account"));
      this.tB = ((String)ds.a(paramString, "reason"));
      this.tC = paramLong;
    }
    
    public Builder appSpecificKey(String paramString)
    {
      this.tF = paramString;
      return this;
    }
    
    public UploadRequest build()
    {
      return new UploadRequest(this, null);
    }
    
    public Builder movingLatencyMillis(long paramLong)
    {
      this.tG = paramLong;
      return this;
    }
    
    public Builder stationaryLatencyMillis(long paramLong)
    {
      this.tH = paramLong;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.reporting.UploadRequest
 * JD-Core Version:    0.7.0.1
 */