package com.google.android.gms.location.reporting;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dr;

public final class UploadRequestResult
  implements SafeParcelable
{
  public static final f CREATOR = new f();
  private final int is;
  private final int tI;
  private final long tJ;
  
  UploadRequestResult(int paramInt1, int paramInt2, long paramLong)
  {
    this.is = paramInt1;
    this.tI = paramInt2;
    this.tJ = paramLong;
  }
  
  public UploadRequestResult(int paramInt, long paramLong)
  {
    this(1, paramInt, paramLong);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof UploadRequestResult)) {}
    UploadRequestResult localUploadRequestResult;
    do
    {
      return false;
      localUploadRequestResult = (UploadRequestResult)paramObject;
    } while ((this.tJ != localUploadRequestResult.tJ) || (this.tI != localUploadRequestResult.tI));
    return true;
  }
  
  public long getRequestId()
  {
    return this.tJ;
  }
  
  public int getResultCode()
  {
    return this.tI;
  }
  
  int getVersionCode()
  {
    return this.is;
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Integer.valueOf(this.tI);
    arrayOfObject[1] = Long.valueOf(this.tJ);
    return dr.hashCode(arrayOfObject);
  }
  
  public String toString()
  {
    return "Result{mVersionCode=" + this.is + ", mResultCode=" + this.tI + ", mRequestId=" + this.tJ + '}';
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    f.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.location.reporting.UploadRequestResult
 * JD-Core Version:    0.7.0.1
 */