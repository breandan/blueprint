package com.google.android.gms.appdatasearch;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class n
  implements Parcelable.Creator<RegisterCorpusInfo>
{
  static void a(RegisterCorpusInfo paramRegisterCorpusInfo, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramRegisterCorpusInfo.name, false);
    b.c(paramParcel, 1000, paramRegisterCorpusInfo.is);
    b.a(paramParcel, 2, paramRegisterCorpusInfo.version, false);
    b.a(paramParcel, 3, paramRegisterCorpusInfo.contentProviderUri, paramInt, false);
    b.a(paramParcel, 4, paramRegisterCorpusInfo.sections, paramInt, false);
    b.a(paramParcel, 7, paramRegisterCorpusInfo.globalSearchConfig, paramInt, false);
    b.a(paramParcel, 8, paramRegisterCorpusInfo.trimmable);
    b.C(paramParcel, i);
  }
  
  public RegisterCorpusInfo u(Parcel paramParcel)
  {
    GlobalSearchCorpusConfig localGlobalSearchCorpusConfig = null;
    int i = a.av(paramParcel);
    int j = 0;
    String str1 = "0";
    boolean bool = true;
    RegisterSectionInfo[] arrayOfRegisterSectionInfo = null;
    Uri localUri = null;
    String str2 = null;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1: 
        str2 = a.l(paramParcel, k);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
        break;
      case 2: 
        str1 = a.l(paramParcel, k);
        break;
      case 3: 
        localUri = (Uri)a.a(paramParcel, k, Uri.CREATOR);
        break;
      case 4: 
        arrayOfRegisterSectionInfo = (RegisterSectionInfo[])a.b(paramParcel, k, RegisterSectionInfo.CREATOR);
        break;
      case 7: 
        localGlobalSearchCorpusConfig = (GlobalSearchCorpusConfig)a.a(paramParcel, k, GlobalSearchCorpusConfig.CREATOR);
        break;
      case 8: 
        bool = a.c(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new RegisterCorpusInfo(j, str2, str1, localUri, arrayOfRegisterSectionInfo, localGlobalSearchCorpusConfig, bool);
  }
  
  public RegisterCorpusInfo[] z(int paramInt)
  {
    return new RegisterCorpusInfo[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.n
 * JD-Core Version:    0.7.0.1
 */