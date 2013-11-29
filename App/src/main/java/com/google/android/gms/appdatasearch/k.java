package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class k
  implements Parcelable.Creator<PhraseAffinityResponse>
{
  static void a(PhraseAffinityResponse paramPhraseAffinityResponse, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramPhraseAffinityResponse.mErrorMessage, false);
    b.c(paramParcel, 1000, paramPhraseAffinityResponse.is);
    b.a(paramParcel, 2, paramPhraseAffinityResponse.ja, paramInt, false);
    b.a(paramParcel, 3, paramPhraseAffinityResponse.jb, false);
    b.C(paramParcel, i);
  }
  
  public PhraseAffinityResponse r(Parcel paramParcel)
  {
    Object localObject1 = null;
    int i = a.av(paramParcel);
    int j = 0;
    Object localObject2 = null;
    Object localObject3 = null;
    if (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      Object localObject4;
      Object localObject5;
      Object localObject6;
      int m;
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        localObject4 = localObject1;
        localObject5 = localObject2;
        localObject6 = localObject3;
        m = j;
      }
      for (;;)
      {
        j = m;
        localObject3 = localObject6;
        localObject2 = localObject5;
        localObject1 = localObject4;
        break;
        String str = a.l(paramParcel, k);
        m = j;
        Object localObject9 = localObject2;
        localObject6 = str;
        localObject4 = localObject1;
        localObject5 = localObject9;
        continue;
        int n = a.f(paramParcel, k);
        Object localObject8 = localObject1;
        localObject5 = localObject2;
        localObject6 = localObject3;
        m = n;
        localObject4 = localObject8;
        continue;
        CorpusId[] arrayOfCorpusId = (CorpusId[])a.b(paramParcel, k, CorpusId.CREATOR);
        localObject6 = localObject3;
        m = j;
        Object localObject7 = localObject1;
        localObject5 = arrayOfCorpusId;
        localObject4 = localObject7;
        continue;
        localObject4 = a.q(paramParcel, k);
        localObject5 = localObject2;
        localObject6 = localObject3;
        m = j;
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new PhraseAffinityResponse(j, localObject3, localObject2, localObject1);
  }
  
  public PhraseAffinityResponse[] w(int paramInt)
  {
    return new PhraseAffinityResponse[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.k
 * JD-Core Version:    0.7.0.1
 */