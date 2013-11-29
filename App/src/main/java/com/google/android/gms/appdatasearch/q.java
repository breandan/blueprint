package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class q
  implements Parcelable.Creator<RequestIndexingSpecification>
{
  static void a(RequestIndexingSpecification paramRequestIndexingSpecification, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.c(paramParcel, 1000, paramRequestIndexingSpecification.is);
    b.C(paramParcel, i);
  }
  
  public RequestIndexingSpecification[] C(int paramInt)
  {
    return new RequestIndexingSpecification[paramInt];
  }
  
  public RequestIndexingSpecification x(Parcel paramParcel)
  {
    int i = a.av(paramParcel);
    int j = 0;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new RequestIndexingSpecification(j);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.q
 * JD-Core Version:    0.7.0.1
 */