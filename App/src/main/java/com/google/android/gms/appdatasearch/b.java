package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;

public class b
  implements Parcelable.Creator<CorpusScoringInfo>
{
  static void a(CorpusScoringInfo paramCorpusScoringInfo, Parcel paramParcel, int paramInt)
  {
    int i = com.google.android.gms.common.internal.safeparcel.b.aw(paramParcel);
    com.google.android.gms.common.internal.safeparcel.b.a(paramParcel, 1, paramCorpusScoringInfo.corpus, paramInt, false);
    com.google.android.gms.common.internal.safeparcel.b.c(paramParcel, 1000, paramCorpusScoringInfo.is);
    com.google.android.gms.common.internal.safeparcel.b.c(paramParcel, 2, paramCorpusScoringInfo.weight);
    com.google.android.gms.common.internal.safeparcel.b.C(paramParcel, i);
  }
  
  public CorpusScoringInfo i(Parcel paramParcel)
  {
    int i = 0;
    int j = a.av(paramParcel);
    Object localObject1 = null;
    int k = 0;
    if (paramParcel.dataPosition() < j)
    {
      int m = a.au(paramParcel);
      int n;
      Object localObject2;
      int i1;
      switch (a.aL(m))
      {
      default: 
        a.b(paramParcel, m);
        n = i;
        localObject2 = localObject1;
        i1 = k;
      }
      for (;;)
      {
        k = i1;
        localObject1 = localObject2;
        i = n;
        break;
        CorpusId localCorpusId = (CorpusId)a.a(paramParcel, m, CorpusId.CREATOR);
        i1 = k;
        n = i;
        localObject2 = localCorpusId;
        continue;
        int i2 = a.f(paramParcel, m);
        int i3 = i;
        localObject2 = localObject1;
        i1 = i2;
        n = i3;
        continue;
        n = a.f(paramParcel, m);
        localObject2 = localObject1;
        i1 = k;
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new CorpusScoringInfo(k, localObject1, i);
  }
  
  public CorpusScoringInfo[] n(int paramInt)
  {
    return new CorpusScoringInfo[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.b
 * JD-Core Version:    0.7.0.1
 */