package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class PhraseAffinityResponse
  implements SafeParcelable
{
  public static final k CREATOR = new k();
  final int is;
  final CorpusId[] ja;
  final int[] jb;
  final String mErrorMessage;
  
  PhraseAffinityResponse(int paramInt, String paramString, CorpusId[] paramArrayOfCorpusId, int[] paramArrayOfInt)
  {
    this.is = paramInt;
    this.mErrorMessage = paramString;
    this.ja = paramArrayOfCorpusId;
    this.jb = paramArrayOfInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAffinityScore(int paramInt1, int paramInt2)
  {
    return this.jb[(paramInt1 + paramInt2 * this.ja.length)];
  }
  
  public String getErrorMessage()
  {
    return this.mErrorMessage;
  }
  
  public boolean hasError()
  {
    return this.mErrorMessage != null;
  }
  
  public boolean isPhraseFound(int paramInt)
  {
    int i = paramInt * this.ja.length;
    int j = i + this.ja.length;
    while (i < j)
    {
      if (this.jb[i] != 0) {
        return true;
      }
      i++;
    }
    return false;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    k.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.PhraseAffinityResponse
 * JD-Core Version:    0.7.0.1
 */