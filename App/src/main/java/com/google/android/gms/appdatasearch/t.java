package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class t
  implements Parcelable.Creator<ResultId>
{
  static void a(ResultId paramResultId, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramResultId.iX, false);
    b.c(paramParcel, 1000, paramResultId.is);
    b.a(paramParcel, 2, paramResultId.iY, false);
    b.a(paramParcel, 3, paramResultId.jv, false);
    b.C(paramParcel, i);
  }
  
  public ResultId[] E(int paramInt)
  {
    return new ResultId[paramInt];
  }
  
  public ResultId z(Parcel paramParcel)
  {
    String str1 = null;
    int i = a.av(paramParcel);
    int j = 0;
    String str2 = null;
    String str3 = null;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1: 
        str3 = a.l(paramParcel, k);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
        break;
      case 2: 
        str2 = a.l(paramParcel, k);
        break;
      case 3: 
        str1 = a.l(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new ResultId(j, str3, str2, str1);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.t
 * JD-Core Version:    0.7.0.1
 */