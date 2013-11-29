package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class g
  implements Parcelable.Creator<GlobalSearchQuerySpecification>
{
  static void a(GlobalSearchQuerySpecification paramGlobalSearchQuerySpecification, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramGlobalSearchQuerySpecification.iD, paramInt, false);
    b.c(paramParcel, 1000, paramGlobalSearchQuerySpecification.is);
    b.c(paramParcel, 2, paramGlobalSearchQuerySpecification.scoringVerbosityLevel);
    b.a(paramParcel, 3, paramGlobalSearchQuerySpecification.iF, paramInt, false);
    b.C(paramParcel, i);
  }
  
  public GlobalSearchQuerySpecification n(Parcel paramParcel)
  {
    Object localObject1 = null;
    int i = 0;
    int j = a.av(paramParcel);
    Object localObject2 = null;
    int k = 0;
    if (paramParcel.dataPosition() < j)
    {
      int m = a.au(paramParcel);
      Object localObject3;
      int n;
      Object localObject4;
      int i1;
      switch (a.aL(m))
      {
      default: 
        a.b(paramParcel, m);
        localObject3 = localObject1;
        n = i;
        localObject4 = localObject2;
        i1 = k;
      }
      for (;;)
      {
        k = i1;
        localObject2 = localObject4;
        i = n;
        localObject1 = localObject3;
        break;
        CorpusId[] arrayOfCorpusId = (CorpusId[])a.b(paramParcel, m, CorpusId.CREATOR);
        i1 = k;
        int i4 = i;
        localObject4 = arrayOfCorpusId;
        localObject3 = localObject1;
        n = i4;
        continue;
        int i3 = a.f(paramParcel, m);
        Object localObject6 = localObject1;
        n = i;
        localObject4 = localObject2;
        i1 = i3;
        localObject3 = localObject6;
        continue;
        int i2 = a.f(paramParcel, m);
        localObject4 = localObject2;
        i1 = k;
        Object localObject5 = localObject1;
        n = i2;
        localObject3 = localObject5;
        continue;
        localObject3 = (CorpusScoringInfo[])a.b(paramParcel, m, CorpusScoringInfo.CREATOR);
        n = i;
        localObject4 = localObject2;
        i1 = k;
      }
    }
    if (paramParcel.dataPosition() != j) {
      throw new a.a("Overread allowed size end=" + j, paramParcel);
    }
    return new GlobalSearchQuerySpecification(k, localObject2, i, localObject1);
  }
  
  public GlobalSearchQuerySpecification[] s(int paramInt)
  {
    return new GlobalSearchQuerySpecification[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.g
 * JD-Core Version:    0.7.0.1
 */