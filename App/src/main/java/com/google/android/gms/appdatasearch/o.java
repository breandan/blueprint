package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class o
  implements Parcelable.Creator<RegisterSectionInfo>
{
  static void a(RegisterSectionInfo paramRegisterSectionInfo, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramRegisterSectionInfo.name, false);
    b.c(paramParcel, 1000, paramRegisterSectionInfo.is);
    b.a(paramParcel, 2, paramRegisterSectionInfo.format, false);
    b.a(paramParcel, 3, paramRegisterSectionInfo.noIndex);
    b.c(paramParcel, 4, paramRegisterSectionInfo.weight);
    b.a(paramParcel, 5, paramRegisterSectionInfo.indexPrefixes);
    b.a(paramParcel, 6, paramRegisterSectionInfo.subsectionSeparator, false);
    b.a(paramParcel, 7, paramRegisterSectionInfo.features, paramInt, false);
    b.C(paramParcel, i);
  }
  
  public RegisterSectionInfo[] A(int paramInt)
  {
    return new RegisterSectionInfo[paramInt];
  }
  
  public RegisterSectionInfo v(Parcel paramParcel)
  {
    boolean bool1 = false;
    SectionFeature[] arrayOfSectionFeature = null;
    int i = a.av(paramParcel);
    int j = 1;
    String str1 = null;
    boolean bool2 = false;
    String str2 = null;
    String str3 = null;
    int k = 0;
    while (paramParcel.dataPosition() < i)
    {
      int m = a.au(paramParcel);
      switch (a.aL(m))
      {
      default: 
        a.b(paramParcel, m);
        break;
      case 1: 
        str3 = a.l(paramParcel, m);
        break;
      case 1000: 
        k = a.f(paramParcel, m);
        break;
      case 2: 
        str2 = a.l(paramParcel, m);
        break;
      case 3: 
        bool2 = a.c(paramParcel, m);
        break;
      case 4: 
        j = a.f(paramParcel, m);
        break;
      case 5: 
        bool1 = a.c(paramParcel, m);
        break;
      case 6: 
        str1 = a.l(paramParcel, m);
        break;
      case 7: 
        arrayOfSectionFeature = (SectionFeature[])a.b(paramParcel, m, SectionFeature.CREATOR);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new RegisterSectionInfo(k, str3, str2, bool2, j, bool1, str1, arrayOfSectionFeature);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.o
 * JD-Core Version:    0.7.0.1
 */