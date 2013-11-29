package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class w
  implements Parcelable.Creator<SectionFeature>
{
  static void a(SectionFeature paramSectionFeature, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.c(paramParcel, 1, paramSectionFeature.id);
    b.c(paramParcel, 1000, paramSectionFeature.is);
    b.a(paramParcel, 2, paramSectionFeature.jM, false);
    b.C(paramParcel, i);
  }
  
  public SectionFeature C(Parcel paramParcel)
  {
    int i = 0;
    int j = a.av(paramParcel);
    Bundle localBundle = null;
    int k = 0;
    while (paramParcel.dataPosition() < j)
    {
      int m = a.au(paramParcel);
      switch (a.aL(m))
      {
      default: 
        a.b(paramParcel, m);
        break;
      case 1: 
        i = a.f(paramParcel, m);
        break;
      case 1000: 
        k = a.f(paramParcel, m);
        break;
      case 2: 
        localBundle = a.n(paramParcel, m);
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new SectionFeature(k, i, localBundle);
  }
  
  public SectionFeature[] I(int paramInt)
  {
    return new SectionFeature[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.w
 * JD-Core Version:    0.7.0.1
 */