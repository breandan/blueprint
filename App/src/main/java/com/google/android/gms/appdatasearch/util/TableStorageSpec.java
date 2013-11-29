package com.google.android.gms.appdatasearch.util;

import android.text.TextUtils;
import com.google.android.gms.appdatasearch.GlobalSearchSections;
import com.google.android.gms.appdatasearch.RegisterSectionInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TableStorageSpec
{
  private final String mCondition;
  private final int[] mGlobalSearchTemplates;
  private final String mScore;
  private final Map<String, String> mSectionNameToColumnName;
  private final List<RegisterSectionInfo> mSections;
  private final String mTableName;
  private final boolean mTrimmable;
  private final String mUriCol;
  private final String mVersion;
  
  TableStorageSpec(String paramString1, String paramString2, String paramString3, String paramString4, List<RegisterSectionInfo> paramList, Map<String, String> paramMap, String paramString5, int[] paramArrayOfInt, boolean paramBoolean)
  {
    if (paramString2 == null) {
      throw new NullPointerException("A URI column must be specified for table " + paramString1);
    }
    if (paramList.size() == 0) {
      throw new IllegalArgumentException("At least one section must be specified for table " + paramString1);
    }
    if (paramList.size() > 16) {
      throw new IllegalArgumentException("Too many sections for table " + paramString1 + "; max is 16");
    }
    this.mTableName = paramString1;
    this.mUriCol = paramString2;
    if (paramString3 == null) {
      paramString3 = "0";
    }
    this.mScore = paramString3;
    if (paramString4 == null) {
      paramString4 = "1";
    }
    this.mVersion = paramString4;
    this.mSections = Collections.unmodifiableList(new ArrayList(paramList));
    this.mSectionNameToColumnName = Collections.unmodifiableMap(new HashMap(paramMap));
    this.mCondition = paramString5;
    this.mGlobalSearchTemplates = paramArrayOfInt;
    this.mTrimmable = paramBoolean;
  }
  
  public static Builder builder(String paramString)
  {
    return new Builder(paramString);
  }
  
  public static String getCorpusName(String paramString1, String paramString2)
  {
    return paramString1 + "_" + paramString2;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof TableStorageSpec)) {
      return false;
    }
    TableStorageSpec localTableStorageSpec = (TableStorageSpec)paramObject;
    return this.mTableName.equals(localTableStorageSpec.mTableName);
  }
  
  public String getCondition()
  {
    return this.mCondition;
  }
  
  public String getCorpusName()
  {
    return getCorpusName(this.mTableName, this.mUriCol);
  }
  
  public int[] getGlobalSearchSectionTemplates()
  {
    return this.mGlobalSearchTemplates;
  }
  
  public String getScoreSpec()
  {
    return this.mScore;
  }
  
  public Map<String, String> getSectionToColumnNameMap()
  {
    return this.mSectionNameToColumnName;
  }
  
  public List<RegisterSectionInfo> getSections()
  {
    return this.mSections;
  }
  
  public String getTableName()
  {
    return this.mTableName;
  }
  
  public boolean getTrimmable()
  {
    return this.mTrimmable;
  }
  
  public String getUriColumn()
  {
    return this.mUriCol;
  }
  
  public String getVersion()
  {
    return this.mVersion;
  }
  
  public int hashCode()
  {
    return this.mTableName.hashCode();
  }
  
  public String toString()
  {
    return "TableStorageSpec[" + this.mTableName + "]";
  }
  
  public static final class Builder
  {
    private String mCondition;
    private int[] mGlobalSearchSpecs;
    private String mScore;
    private final Map<String, String> mSectionToColumnNameMap;
    private final List<RegisterSectionInfo> mSections;
    private final String mTableName;
    private boolean mTrimmable;
    private String mUriCol;
    private String mVersion;
    
    public Builder(String paramString)
    {
      if (TextUtils.isEmpty(paramString)) {
        throw new IllegalArgumentException("A valid tableName must be supplied");
      }
      this.mTableName = paramString;
      this.mSections = new ArrayList();
      this.mSectionToColumnNameMap = new HashMap();
      this.mTrimmable = true;
    }
    
    private void checkSectionNotAlreadyAdded(String paramString)
    {
      if (this.mSectionToColumnNameMap.containsKey(paramString)) {
        throw new IllegalArgumentException("Table spec already contains mapping for section " + paramString);
      }
    }
    
    public Builder addGlobalSearchSectionTemplate(String paramString, int paramInt)
    {
      if (this.mGlobalSearchSpecs == null) {
        this.mGlobalSearchSpecs = new int[GlobalSearchSections.getSectionsCount()];
      }
      int i = GlobalSearchSections.getSectionId(paramString);
      if (i == -1) {
        throw new IllegalArgumentException("Invalid global search section: " + paramString);
      }
      if (this.mGlobalSearchSpecs[i] != 0) {
        throw new IllegalArgumentException("Table spec already contains mapping for global search section " + paramString);
      }
      this.mGlobalSearchSpecs[i] = paramInt;
      return this;
    }
    
    public Builder addSectionForColumn(String paramString, RegisterSectionInfo paramRegisterSectionInfo)
    {
      checkSectionNotAlreadyAdded(paramRegisterSectionInfo.name);
      this.mSections.add(paramRegisterSectionInfo);
      this.mSectionToColumnNameMap.put(paramRegisterSectionInfo.name, paramString);
      return this;
    }
    
    public TableStorageSpec build()
    {
      return new TableStorageSpec(this.mTableName, this.mUriCol, this.mScore, this.mVersion, this.mSections, this.mSectionToColumnNameMap, this.mCondition, this.mGlobalSearchSpecs, this.mTrimmable);
    }
    
    public Builder score(String paramString)
    {
      this.mScore = paramString;
      return this;
    }
    
    public Builder untrimmable()
    {
      this.mTrimmable = false;
      return this;
    }
    
    public Builder uriColumn(String paramString)
    {
      this.mUriCol = paramString;
      return this;
    }
    
    public Builder version(String paramString)
    {
      this.mVersion = paramString;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.util.TableStorageSpec
 * JD-Core Version:    0.7.0.1
 */