package com.google.android.gms.appdatasearch;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dr;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CorpusStatus
  implements SafeParcelable
{
  public static final c CREATOR = new c();
  final int is;
  final boolean it;
  final long iu;
  final long iv;
  final long iw;
  final Bundle ix;
  final String iy;
  
  CorpusStatus()
  {
    this(2, false, 0L, 0L, 0L, null, null);
  }
  
  CorpusStatus(int paramInt, boolean paramBoolean, long paramLong1, long paramLong2, long paramLong3, Bundle paramBundle, String paramString)
  {
    this.is = paramInt;
    this.it = paramBoolean;
    this.iu = paramLong1;
    this.iv = paramLong2;
    this.iw = paramLong3;
    if (paramBundle == null) {
      paramBundle = new Bundle();
    }
    this.ix = paramBundle;
    this.iy = paramString;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof CorpusStatus;
    boolean bool2 = false;
    if (bool1)
    {
      CorpusStatus localCorpusStatus = (CorpusStatus)paramObject;
      boolean bool3 = dr.equal(Boolean.valueOf(this.it), Boolean.valueOf(localCorpusStatus.it));
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = dr.equal(Long.valueOf(this.iu), Long.valueOf(localCorpusStatus.iu));
        bool2 = false;
        if (bool4)
        {
          boolean bool5 = dr.equal(Long.valueOf(this.iv), Long.valueOf(localCorpusStatus.iv));
          bool2 = false;
          if (bool5)
          {
            boolean bool6 = dr.equal(Long.valueOf(this.iw), Long.valueOf(localCorpusStatus.iw));
            bool2 = false;
            if (bool6)
            {
              boolean bool7 = dr.equal(getCounters(), localCorpusStatus.getCounters());
              bool2 = false;
              if (bool7) {
                bool2 = true;
              }
            }
          }
        }
      }
    }
    return bool2;
  }
  
  public boolean found()
  {
    return this.it;
  }
  
  public Map<String, Integer> getCounters()
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator = this.ix.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      int i = this.ix.getInt(str, -1);
      if (i != -1) {
        localHashMap.put(str, Integer.valueOf(i));
      }
    }
    return localHashMap;
  }
  
  public long getLastCommittedSeqno()
  {
    return this.iv;
  }
  
  public long getLastIndexedSeqno()
  {
    return this.iu;
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[5];
    arrayOfObject[0] = Boolean.valueOf(this.it);
    arrayOfObject[1] = Long.valueOf(this.iu);
    arrayOfObject[2] = Long.valueOf(this.iv);
    arrayOfObject[3] = Long.valueOf(this.iw);
    arrayOfObject[4] = getCounters();
    return dr.hashCode(arrayOfObject);
  }
  
  public String toString()
  {
    return "CorpusStatus{found=" + this.it + ", lastIndexedSeqno=" + this.iu + ", lastCommittedSeqno=" + this.iv + ", committedNumDocuments=" + this.iw + ", counters=" + this.ix + "}";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    c.a(this, paramParcel, paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.CorpusStatus
 * JD-Core Version:    0.7.0.1
 */