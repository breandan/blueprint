package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class c
  implements Parcelable.Creator<CorpusStatus>
{
  static void a(CorpusStatus paramCorpusStatus, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramCorpusStatus.it);
    b.c(paramParcel, 1000, paramCorpusStatus.is);
    b.a(paramParcel, 2, paramCorpusStatus.iu);
    b.a(paramParcel, 3, paramCorpusStatus.iv);
    b.a(paramParcel, 4, paramCorpusStatus.iw);
    b.a(paramParcel, 5, paramCorpusStatus.ix, false);
    b.a(paramParcel, 6, paramCorpusStatus.iy, false);
    b.C(paramParcel, i);
  }
  
  public CorpusStatus j(Parcel paramParcel)
  {
    String str = null;
    boolean bool = false;
    long l1 = 0L;
    int i = a.av(paramParcel);
    Bundle localBundle = null;
    long l2 = l1;
    long l3 = l1;
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
        bool = a.c(paramParcel, k);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
        break;
      case 2: 
        l3 = a.g(paramParcel, k);
        break;
      case 3: 
        l2 = a.g(paramParcel, k);
        break;
      case 4: 
        l1 = a.g(paramParcel, k);
        break;
      case 5: 
        localBundle = a.n(paramParcel, k);
        break;
      case 6: 
        str = a.l(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new CorpusStatus(j, bool, l3, l2, l1, localBundle, str);
  }
  
  public CorpusStatus[] o(int paramInt)
  {
    return new CorpusStatus[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.c
 * JD-Core Version:    0.7.0.1
 */