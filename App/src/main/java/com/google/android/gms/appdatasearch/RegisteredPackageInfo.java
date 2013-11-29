package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class RegisteredPackageInfo
  implements SafeParcelable
{
  public static final p CREATOR = new p();
  public final boolean blocked;
  final int is;
  public final String packageName;
  public final long reclaimableDiskBytes;
  public final long usedDiskBytes;
  
  RegisteredPackageInfo(int paramInt, String paramString, long paramLong1, boolean paramBoolean, long paramLong2)
  {
    this.is = paramInt;
    this.packageName = paramString;
    this.usedDiskBytes = paramLong1;
    this.blocked = paramBoolean;
    this.reclaimableDiskBytes = paramLong2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    p.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.RegisteredPackageInfo
 * JD-Core Version:    0.7.0.1
 */