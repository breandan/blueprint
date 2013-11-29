package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class f
  implements Parcelable.Creator<GlobalSearchCorpusConfig>
{
  static void a(GlobalSearchCorpusConfig paramGlobalSearchCorpusConfig, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramGlobalSearchCorpusConfig.globalSearchSectionMappings, false);
    b.c(paramParcel, 1000, paramGlobalSearchCorpusConfig.is);
    b.C(paramParcel, i);
  }
  
  public GlobalSearchCorpusConfig m(Parcel paramParcel)
  {
    int i = a.av(paramParcel);
    int j = 0;
    int[] arrayOfInt = null;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1: 
        arrayOfInt = a.q(paramParcel, k);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new GlobalSearchCorpusConfig(j, arrayOfInt);
  }
  
  public GlobalSearchCorpusConfig[] r(int paramInt)
  {
    return new GlobalSearchCorpusConfig[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.f
 * JD-Core Version:    0.7.0.1
 */