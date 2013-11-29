package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ResultId
  implements SafeParcelable
{
  public static final t CREATOR = new t();
  final String iX;
  final String iY;
  final int is;
  final String jv;
  
  ResultId(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    this.is = paramInt;
    this.iX = paramString1;
    this.iY = paramString2;
    this.jv = paramString3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    t.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.ResultId
 * JD-Core Version:    0.7.0.1
 */