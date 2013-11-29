package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ds;

public class GlobalSearchCorpusConfig
  implements SafeParcelable
{
  public static final f CREATOR = new f();
  public final int[] globalSearchSectionMappings;
  final int is;
  
  GlobalSearchCorpusConfig(int paramInt, int[] paramArrayOfInt)
  {
    this.is = paramInt;
    this.globalSearchSectionMappings = paramArrayOfInt;
  }
  
  public GlobalSearchCorpusConfig(int[] paramArrayOfInt)
  {
    this(2, paramArrayOfInt);
    if (paramArrayOfInt.length == GlobalSearchSections.getSectionsCount()) {}
    for (boolean bool = true;; bool = false)
    {
      ds.m(bool);
      return;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    f.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.GlobalSearchCorpusConfig
 * JD-Core Version:    0.7.0.1
 */