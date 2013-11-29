package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ds;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GlobalSearchQuerySpecification
  implements SafeParcelable
{
  public static final g CREATOR = new g();
  final CorpusId[] iD;
  final transient Map<String, Set<String>> iE;
  final CorpusScoringInfo[] iF;
  final transient Map<CorpusId, CorpusScoringInfo> iG;
  final int is;
  public final int scoringVerbosityLevel;
  
  GlobalSearchQuerySpecification(int paramInt1, CorpusId[] paramArrayOfCorpusId, int paramInt2, CorpusScoringInfo[] paramArrayOfCorpusScoringInfo)
  {
    this.is = paramInt1;
    this.iD = paramArrayOfCorpusId;
    this.scoringVerbosityLevel = paramInt2;
    this.iF = paramArrayOfCorpusScoringInfo;
    if ((paramArrayOfCorpusId == null) || (paramArrayOfCorpusId.length == 0))
    {
      this.iE = null;
      if ((paramArrayOfCorpusScoringInfo != null) && (paramArrayOfCorpusScoringInfo.length != 0)) {
        break label165;
      }
      this.iG = null;
    }
    for (;;)
    {
      return;
      this.iE = new HashMap();
      for (int j = 0; j < paramArrayOfCorpusId.length; j++)
      {
        Object localObject = (Set)this.iE.get(paramArrayOfCorpusId[j].packageName);
        if (localObject == null)
        {
          localObject = new HashSet();
          this.iE.put(paramArrayOfCorpusId[j].packageName, localObject);
        }
        if (paramArrayOfCorpusId[j].corpusName != null) {
          ((Set)localObject).add(paramArrayOfCorpusId[j].corpusName);
        }
      }
      break;
      label165:
      this.iG = new HashMap(paramArrayOfCorpusScoringInfo.length);
      while (i < paramArrayOfCorpusScoringInfo.length)
      {
        this.iG.put(paramArrayOfCorpusScoringInfo[i].corpus, paramArrayOfCorpusScoringInfo[i]);
        i++;
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    g.a(this, paramParcel, paramInt);
  }
  
  public static class Builder
  {
    private final Map<String, Set<String>> iH = new HashMap();
    private int iI;
    private final Map<CorpusId, CorpusScoringInfo> iJ = new HashMap();
    
    public Builder addCorpus(CorpusId paramCorpusId)
    {
      String str1 = paramCorpusId.packageName;
      String str2 = paramCorpusId.corpusName;
      ds.a(str1, "Package name can't be null.");
      ds.a(str2, "CorpusNames can't be null");
      if ((!this.iH.containsKey(str1)) || (!((Set)this.iH.get(str1)).isEmpty())) {}
      for (boolean bool = true;; bool = false)
      {
        ds.a(bool, "Whole package was added before.");
        Object localObject = (Set)this.iH.get(str1);
        if (localObject == null)
        {
          localObject = new HashSet();
          this.iH.put(str1, localObject);
        }
        ((Set)localObject).add(str2);
        return this;
      }
    }
    
    public Builder addPackage(String paramString)
    {
      ds.a(paramString, "Package name can't bu null.");
      if ((!this.iH.containsKey(paramString)) || (((Set)this.iH.get(paramString)).isEmpty())) {}
      for (boolean bool = true;; bool = false)
      {
        ds.a(bool, "More specific filtering was added before.");
        this.iH.put(paramString, Collections.emptySet());
        return this;
      }
    }
    
    public GlobalSearchQuerySpecification build()
    {
      ArrayList localArrayList = new ArrayList(this.iH.size());
      Iterator localIterator1 = this.iH.entrySet().iterator();
      while (localIterator1.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator1.next();
        if (((Set)localEntry.getValue()).isEmpty())
        {
          localArrayList.add(new CorpusId((String)localEntry.getKey(), null));
        }
        else
        {
          Iterator localIterator3 = ((Set)localEntry.getValue()).iterator();
          while (localIterator3.hasNext())
          {
            String str = (String)localIterator3.next();
            localArrayList.add(new CorpusId((String)localEntry.getKey(), str));
          }
        }
      }
      CorpusScoringInfo[] arrayOfCorpusScoringInfo = new CorpusScoringInfo[this.iJ.size()];
      Iterator localIterator2 = this.iJ.values().iterator();
      int j;
      for (int i = 0; localIterator2.hasNext(); i = j)
      {
        CorpusScoringInfo localCorpusScoringInfo = (CorpusScoringInfo)localIterator2.next();
        j = i + 1;
        arrayOfCorpusScoringInfo[i] = localCorpusScoringInfo;
      }
      return new GlobalSearchQuerySpecification(1, (CorpusId[])localArrayList.toArray(new CorpusId[localArrayList.size()]), this.iI, arrayOfCorpusScoringInfo);
    }
    
    public Builder setCorpusWeight(CorpusId paramCorpusId, int paramInt)
    {
      this.iJ.put(paramCorpusId, new CorpusScoringInfo(paramCorpusId, paramInt));
      return this;
    }
    
    public Builder setPackageWeight(String paramString, int paramInt)
    {
      return setCorpusWeight(new CorpusId(paramString, null), paramInt);
    }
    
    public Builder setScoringVerbosityLevel(int paramInt)
    {
      this.iI = paramInt;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.GlobalSearchQuerySpecification
 * JD-Core Version:    0.7.0.1
 */