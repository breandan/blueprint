package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class h
  implements Parcelable.Creator<PIMEUpdate>
{
  static void a(PIMEUpdate paramPIMEUpdate, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramPIMEUpdate.iN, false);
    b.c(paramParcel, 1000, paramPIMEUpdate.is);
    b.a(paramParcel, 2, paramPIMEUpdate.iO, false);
    b.c(paramParcel, 3, paramPIMEUpdate.sourceClass);
    b.a(paramParcel, 4, paramPIMEUpdate.sourcePackageName, false);
    b.a(paramParcel, 5, paramPIMEUpdate.sourceCorpusHandle, false);
    b.a(paramParcel, 6, paramPIMEUpdate.inputByUser);
    b.a(paramParcel, 8, paramPIMEUpdate.iP, false);
    b.a(paramParcel, 9, paramPIMEUpdate.score);
    b.C(paramParcel, i);
  }
  
  public PIMEUpdate o(Parcel paramParcel)
  {
    boolean bool = false;
    Bundle localBundle = null;
    int i = a.av(paramParcel);
    long l = 0L;
    String str1 = null;
    String str2 = null;
    int j = 0;
    byte[] arrayOfByte1 = null;
    byte[] arrayOfByte2 = null;
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
        arrayOfByte2 = a.o(paramParcel, m);
        break;
      case 1000: 
        k = a.f(paramParcel, m);
        break;
      case 2: 
        arrayOfByte1 = a.o(paramParcel, m);
        break;
      case 3: 
        j = a.f(paramParcel, m);
        break;
      case 4: 
        str2 = a.l(paramParcel, m);
        break;
      case 5: 
        str1 = a.l(paramParcel, m);
        break;
      case 6: 
        bool = a.c(paramParcel, m);
        break;
      case 8: 
        localBundle = a.n(paramParcel, m);
        break;
      case 9: 
        l = a.g(paramParcel, m);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new PIMEUpdate(k, arrayOfByte2, arrayOfByte1, j, str2, str1, bool, localBundle, l);
  }
  
  public PIMEUpdate[] t(int paramInt)
  {
    return new PIMEUpdate[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.h
 * JD-Core Version:    0.7.0.1
 */