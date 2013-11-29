package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class fr
  implements Parcelable.Creator<fq>
{
  static void a(fq paramfq, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramfq.getRequestId(), false);
    b.c(paramParcel, 1000, paramfq.getVersionCode());
    b.a(paramParcel, 2, paramfq.getExpirationTime());
    b.a(paramParcel, 3, paramfq.cs());
    b.a(paramParcel, 4, paramfq.getLatitude());
    b.a(paramParcel, 5, paramfq.getLongitude());
    b.a(paramParcel, 6, paramfq.ct());
    b.c(paramParcel, 7, paramfq.cu());
    b.c(paramParcel, 8, paramfq.getNotificationResponsiveness());
    b.c(paramParcel, 9, paramfq.cv());
    b.C(paramParcel, i);
  }
  
  public fq aO(Parcel paramParcel)
  {
    int i = a.av(paramParcel);
    int j = 0;
    String str = null;
    int k = 0;
    short s = 0;
    double d1 = 0.0D;
    double d2 = 0.0D;
    float f = 0.0F;
    long l = 0L;
    int m = 0;
    int n = -1;
    while (paramParcel.dataPosition() < i)
    {
      int i1 = a.au(paramParcel);
      switch (a.aL(i1))
      {
      default: 
        a.b(paramParcel, i1);
        break;
      case 1: 
        str = a.l(paramParcel, i1);
        break;
      case 1000: 
        j = a.f(paramParcel, i1);
        break;
      case 2: 
        l = a.g(paramParcel, i1);
        break;
      case 3: 
        s = a.e(paramParcel, i1);
        break;
      case 4: 
        d1 = a.j(paramParcel, i1);
        break;
      case 5: 
        d2 = a.j(paramParcel, i1);
        break;
      case 6: 
        f = a.i(paramParcel, i1);
        break;
      case 7: 
        k = a.f(paramParcel, i1);
        break;
      case 8: 
        m = a.f(paramParcel, i1);
        break;
      case 9: 
        n = a.f(paramParcel, i1);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new fq(j, str, k, s, d1, d2, f, l, m, n);
  }
  
  public fq[] bs(int paramInt)
  {
    return new fq[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.fr
 * JD-Core Version:    0.7.0.1
 */