package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dr;

public class CorpusId
  implements SafeParcelable
{
  public static final a CREATOR = new a();
  public final String corpusName;
  final int is;
  public final String packageName;
  
  CorpusId(int paramInt, String paramString1, String paramString2)
  {
    this.is = paramInt;
    this.packageName = paramString1;
    this.corpusName = paramString2;
  }
  
  public CorpusId(String paramString1, String paramString2)
  {
    this(1, paramString1, paramString2);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof CorpusId;
    boolean bool2 = false;
    if (bool1)
    {
      CorpusId localCorpusId = (CorpusId)paramObject;
      boolean bool3 = dr.equal(this.packageName, localCorpusId.packageName);
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = dr.equal(this.corpusName, localCorpusId.corpusName);
        bool2 = false;
        if (bool4) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = this.packageName;
    arrayOfObject[1] = this.corpusName;
    return dr.hashCode(arrayOfObject);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    a.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.CorpusId
 * JD-Core Version:    0.7.0.1
 */