package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class r
  implements SafeParcelable
{
  public static final s CREATOR = new s();
  final int is;
  final String js;
  final ResultId[] jt;
  final int ju;
  
  r(int paramInt1, String paramString, ResultId[] paramArrayOfResultId, int paramInt2)
  {
    this.is = paramInt1;
    this.js = paramString;
    this.jt = paramArrayOfResultId;
    this.ju = paramInt2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    s.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.r
 * JD-Core Version:    0.7.0.1
 */