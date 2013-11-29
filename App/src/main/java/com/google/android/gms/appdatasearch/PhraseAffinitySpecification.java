package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.LinkedList;
import java.util.List;

public class PhraseAffinitySpecification
  implements SafeParcelable
{
  public static final l CREATOR = new l();
  final int is;
  final PhraseAffinityCorpusSpec[] jc;
  
  PhraseAffinitySpecification(int paramInt, PhraseAffinityCorpusSpec[] paramArrayOfPhraseAffinityCorpusSpec)
  {
    this.is = paramInt;
    this.jc = paramArrayOfPhraseAffinityCorpusSpec;
  }
  
  public PhraseAffinitySpecification(PhraseAffinityCorpusSpec[] paramArrayOfPhraseAffinityCorpusSpec)
  {
    this(1, (PhraseAffinityCorpusSpec[])paramArrayOfPhraseAffinityCorpusSpec.clone());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    l.a(this, paramParcel, paramInt);
  }
  
  public static class Builder
  {
    private final List<PhraseAffinityCorpusSpec> jd = new LinkedList();
    
    public Builder addCorpusSpec(PhraseAffinityCorpusSpec.Builder paramBuilder)
    {
      this.jd.add(paramBuilder.build());
      return this;
    }
    
    public PhraseAffinitySpecification build()
    {
      int i = this.jd.size();
      if (i == 0) {
        throw new IllegalStateException("No corpus specs specified");
      }
      return new PhraseAffinitySpecification((PhraseAffinityCorpusSpec[])this.jd.toArray(new PhraseAffinityCorpusSpec[i]));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.PhraseAffinitySpecification
 * JD-Core Version:    0.7.0.1
 */