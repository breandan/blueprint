package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.a.a;
import com.google.android.gms.common.internal.safeparcel.b;

public class l
  implements Parcelable.Creator<PhraseAffinitySpecification>
{
  static void a(PhraseAffinitySpecification paramPhraseAffinitySpecification, Parcel paramParcel, int paramInt)
  {
    int i = b.aw(paramParcel);
    b.a(paramParcel, 1, paramPhraseAffinitySpecification.jc, paramInt, false);
    b.c(paramParcel, 1000, paramPhraseAffinitySpecification.is);
    b.C(paramParcel, i);
  }
  
  public PhraseAffinitySpecification s(Parcel paramParcel)
  {
    int i = a.av(paramParcel);
    int j = 0;
    PhraseAffinityCorpusSpec[] arrayOfPhraseAffinityCorpusSpec = null;
    while (paramParcel.dataPosition() < i)
    {
      int k = a.au(paramParcel);
      switch (a.aL(k))
      {
      default: 
        a.b(paramParcel, k);
        break;
      case 1: 
        arrayOfPhraseAffinityCorpusSpec = (PhraseAffinityCorpusSpec[])a.b(paramParcel, k, PhraseAffinityCorpusSpec.CREATOR);
        break;
      case 1000: 
        j = a.f(paramParcel, k);
      }
    }
    if (paramParcel.dataPosition() != i) {
      throw new a.a("Overread allowed size end=" + i, paramParcel);
    }
    return new PhraseAffinitySpecification(j, arrayOfPhraseAffinityCorpusSpec);
  }
  
  public PhraseAffinitySpecification[] x(int paramInt)
  {
    return new PhraseAffinitySpecification[paramInt];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.l
 * JD-Core Version:    0.7.0.1
 */