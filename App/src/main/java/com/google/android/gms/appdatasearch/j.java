package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class j
  implements Parcelable.Creator<PhraseAffinityCorpusSpec>
{
  static void a(PhraseAffinityCorpusSpec paramPhraseAffinityCorpusSpec, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramPhraseAffinityCorpusSpec.corpus, paramInt, false);
    b.c(paramParcel, 1000, paramPhraseAffinityCorpusSpec.is);
    b.a(paramParcel, 2, paramPhraseAffinityCorpusSpec.iW, false);
    b.C(paramParcel, i);
  }
  
  public PhraseAffinityCorpusSpec q(Parcel paramParcel)
  {
    Object localObject1 = null;
    int i = a.av(paramParcel);
    int j = 0;
    Object localObject2 = null;
    if (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      Object localObject3;
      Object localObject4;
      int m;
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        localObject3 = localObject1;
        localObject4 = localObject2;
        m = j;
      }
      for (;;)
      {
        j = m;
        localObject2 = localObject4;
        localObject1 = localObject3;
        break;
        CorpusId localCorpusId = (CorpusId)a.a(paramParcel, k, CorpusId.CREATOR);
        m = j;
        localObject3 = localObject1;
        localObject4 = localCorpusId;
        continue;
        int n = a.f(paramParcel, k);
        Object localObject5 = localObject1;
        localObject4 = localObject2;
        m = n;
        localObject3 = localObject5;
        continue;
        localObject3 = a.n(paramParcel, k);
        localObject4 = localObject2;
        m = j;
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new PhraseAffinityCorpusSpec(j, localObject2, localObject1);
  }
  
  public PhraseAffinityCorpusSpec[] v(int paramInt)
  {
    return new PhraseAffinityCorpusSpec[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.j
 * JD-Core Version:    0.7.0.1
 */