package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class e
  implements Parcelable.Creator<GlobalSearchApplicationInfo>
{
  static void a(GlobalSearchApplicationInfo paramGlobalSearchApplicationInfo, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramGlobalSearchApplicationInfo.packageName, false);
    b.c(paramParcel, 1000, paramGlobalSearchApplicationInfo.is);
    b.c(paramParcel, 2, paramGlobalSearchApplicationInfo.labelId);
    b.c(paramParcel, 3, paramGlobalSearchApplicationInfo.settingsDescriptionId);
    b.c(paramParcel, 4, paramGlobalSearchApplicationInfo.iconId);
    b.a(paramParcel, 5, paramGlobalSearchApplicationInfo.defaultIntentAction, false);
    b.a(paramParcel, 6, paramGlobalSearchApplicationInfo.defaultIntentData, false);
    b.a(paramParcel, 7, paramGlobalSearchApplicationInfo.defaultIntentActivity, false);
    b.C(paramParcel, i);
  }
  
  public GlobalSearchApplicationInfo l(Parcel paramParcel)
  {
    String str1 = null;
    int i = 0;
    int j = a.av(paramParcel);
    String str2 = null;
    String str3 = null;
    int k = 0;
    int m = 0;
    String str4 = null;
    int n = 0;
    while (paramParcel.dataPosition() < j)
    {
      int i1 = a.au(paramParcel);
      switch (a.aL(i1))
      {
      default: 
        a.b(paramParcel, i1);
        break;
      case 1: 
        str4 = a.l(paramParcel, i1);
        break;
      case 1000: 
        n = a.f(paramParcel, i1);
        break;
      case 2: 
        m = a.f(paramParcel, i1);
        break;
      case 3: 
        k = a.f(paramParcel, i1);
        break;
      case 4: 
        i = a.f(paramParcel, i1);
        break;
      case 5: 
        str3 = a.l(paramParcel, i1);
        break;
      case 6: 
        str2 = a.l(paramParcel, i1);
        break;
      case 7: 
        str1 = a.l(paramParcel, i1);
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new GlobalSearchApplicationInfo(n, str4, m, k, i, str3, str2, str1);
  }
  
  public GlobalSearchApplicationInfo[] q(int paramInt)
  {
    return new GlobalSearchApplicationInfo[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.e
 * JD-Core Version:    0.7.0.1
 */