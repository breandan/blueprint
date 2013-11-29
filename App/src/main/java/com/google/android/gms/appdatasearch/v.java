package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class v
  implements Parcelable.Creator<Section>
{
  static void a(Section paramSection, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramSection.name, false);
    b.c(paramParcel, 1000, paramSection.is);
    b.a(paramParcel, 2, paramSection.snippeted);
    b.c(paramParcel, 3, paramSection.snippetLength);
    b.C(paramParcel, i);
  }
  
  public Section B(Parcel paramParcel)
  {
    int i = 0;
    int j = a.av(paramParcel);
    String str = null;
    boolean bool = false;
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
        str = a.l(paramParcel, m);
        break;
      case 1000: 
        k = a.f(paramParcel, m);
        break;
      case 2: 
        bool = a.c(paramParcel, m);
        break;
      case 3: 
        i = a.f(paramParcel, m);
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new Section(k, str, bool, i);
  }
  
  public Section[] H(int paramInt)
  {
    return new Section[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.v
 * JD-Core Version:    0.7.0.1
 */