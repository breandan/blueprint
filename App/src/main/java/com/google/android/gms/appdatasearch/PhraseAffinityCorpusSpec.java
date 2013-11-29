package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PhraseAffinityCorpusSpec
  implements SafeParcelable
{
  public static final j CREATOR = new j();
  public final CorpusId corpus;
  final Bundle iW;
  final int is;
  
  PhraseAffinityCorpusSpec(int paramInt, CorpusId paramCorpusId, Bundle paramBundle)
  {
    this.is = paramInt;
    this.corpus = paramCorpusId;
    this.iW = paramBundle;
  }
  
  public PhraseAffinityCorpusSpec(String paramString1, String paramString2, Map<String, Integer> paramMap)
  {
    this(1, new CorpusId(paramString1, paramString2), new Bundle());
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      this.iW.putInt((String)localEntry.getKey(), ((Integer)localEntry.getValue()).intValue());
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    j.a(this, paramParcel, paramInt);
  }
  
  public static class Builder
  {
    private String iX;
    private String iY;
    private final Map<String, Integer> iZ = new HashMap();
    
    public Builder addSectionWeight(String paramString, int paramInt)
    {
      this.iZ.put(paramString, Integer.valueOf(paramInt));
      return this;
    }
    
    public PhraseAffinityCorpusSpec build()
    {
      if (this.iX == null) {
        throw new IllegalStateException("No package name specified");
      }
      if (this.iY == null) {
        throw new IllegalStateException("No corpus name specified");
      }
      if (this.iZ.size() == 0) {
        throw new IllegalStateException("No section weights specified");
      }
      return new PhraseAffinityCorpusSpec(this.iX, this.iY, this.iZ);
    }
    
    public Builder corpusName(String paramString)
    {
      this.iY = paramString;
      return this;
    }
    
    public Builder packageName(String paramString)
    {
      this.iX = paramString;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.PhraseAffinityCorpusSpec
 * JD-Core Version:    0.7.0.1
 */