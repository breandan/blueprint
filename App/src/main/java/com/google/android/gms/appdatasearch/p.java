package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class p
  implements Parcelable.Creator<RegisteredPackageInfo>
{
  static void a(RegisteredPackageInfo paramRegisteredPackageInfo, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramRegisteredPackageInfo.packageName, false);
    b.c(paramParcel, 1000, paramRegisteredPackageInfo.is);
    b.a(paramParcel, 2, paramRegisteredPackageInfo.usedDiskBytes);
    b.a(paramParcel, 3, paramRegisteredPackageInfo.blocked);
    b.a(paramParcel, 4, paramRegisteredPackageInfo.reclaimableDiskBytes);
    b.C(paramParcel, i);
  }
  
  public RegisteredPackageInfo[] B(int paramInt)
  {
    return new RegisteredPackageInfo[paramInt];
  }
  
  public RegisteredPackageInfo w(Parcel paramParcel)
  {
    long l1 = 0L;
    boolean bool = false;
    int i = a.av(paramParcel);
    String str = null;
    long l2 = l1;
    int j = 0;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1: 
        str = a.l(paramParcel, k);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
        break;
      case 2: 
        l2 = a.g(paramParcel, k);
        break;
      case 3: 
        bool = a.c(paramParcel, k);
        break;
      case 4: 
        l1 = a.g(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new RegisteredPackageInfo(j, str, l2, bool, l1);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.p
 * JD-Core Version:    0.7.0.1
 */