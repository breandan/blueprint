package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class CorpusScoringInfo
  implements SafeParcelable
{
  public static final b CREATOR = new b();
  public final CorpusId corpus;
  final int is;
  public final int weight;
  
  CorpusScoringInfo(int paramInt1, CorpusId paramCorpusId, int paramInt2)
  {
    this.is = paramInt1;
    this.corpus = paramCorpusId;
    this.weight = paramInt2;
  }
  
  public CorpusScoringInfo(CorpusId paramCorpusId, int paramInt)
  {
    this(1, paramCorpusId, paramInt);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    b.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.CorpusScoringInfo
 * JD-Core Version:    0.7.0.1
 */