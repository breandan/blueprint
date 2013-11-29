package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class s
  implements Parcelable.Creator<r>
{
  static void a(r paramr, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramr.js, false);
    b.c(paramParcel, 1000, paramr.is);
    b.a(paramParcel, 2, paramr.jt, paramInt, false);
    b.c(paramParcel, 3, paramr.ju);
    b.C(paramParcel, i);
  }
  
  public r[] D(int paramInt)
  {
    return new r[paramInt];
  }
  
  public r y(Parcel paramParcel)
  {
    Object localObject1 = null;
    int i = 0;
    int j = a.av(paramParcel);
    Object localObject2 = null;
    int k = 0;
    if (paramParcel.dataPosition() < j)
    {
      int m = a.au(paramParcel);
      int n;
      Object localObject3;
      Object localObject4;
      int i1;
      switch (a.aL(m))
      {
      default: 
        a.b(paramParcel, m);
        n = i;
        localObject3 = localObject1;
        localObject4 = localObject2;
        i1 = k;
      }
      for (;;)
      {
        k = i1;
        localObject2 = localObject4;
        localObject1 = localObject3;
        i = n;
        break;
        String str = a.l(paramParcel, m);
        i1 = k;
        Object localObject5 = localObject1;
        localObject4 = str;
        n = i;
        localObject3 = localObject5;
        continue;
        int i3 = a.f(paramParcel, m);
        int i4 = i;
        localObject3 = localObject1;
        localObject4 = localObject2;
        i1 = i3;
        n = i4;
        continue;
        ResultId[] arrayOfResultId = (ResultId[])a.b(paramParcel, m, ResultId.CREATOR);
        localObject4 = localObject2;
        i1 = k;
        int i2 = i;
        localObject3 = arrayOfResultId;
        n = i2;
        continue;
        n = a.f(paramParcel, m);
        localObject3 = localObject1;
        localObject4 = localObject2;
        i1 = k;
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new r(k, localObject2, localObject1, i);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.s
 * JD-Core Version:    0.7.0.1
 */