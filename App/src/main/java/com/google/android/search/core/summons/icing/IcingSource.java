package com.google.android.search.core.summons.icing;

import android.content.pm.ApplicationInfo;
import com.google.android.search.core.summons.Source;

public class IcingSource
  implements Source
{
  private final ApplicationInfo mApplicationInfo;
  private final String mCanonicalSourceName;
  private final String mDefaultIntentAction;
  private final String mDefaultIntentActivity;
  private final String mDefaultIntentData;
  private final boolean mHasFullSizeIcon;
  private final int mIconResourceId;
  private final String mInternalCorpusName;
  private final int mLabelId;
  private final String mName;
  private final String mPackageName;
  private final int mSettingsDescriptionId;
  
  IcingSource(String paramString1, ApplicationInfo paramApplicationInfo, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, String paramString4, String paramString5, boolean paramBoolean, String paramString6, String paramString7)
  {
    this.mName = paramString1;
    this.mPackageName = paramString2;
    this.mApplicationInfo = paramApplicationInfo;
    this.mLabelId = paramInt1;
    this.mSettingsDescriptionId = paramInt2;
    this.mIconResourceId = paramInt3;
    this.mDefaultIntentAction = paramString3;
    this.mDefaultIntentData = paramString4;
    this.mDefaultIntentActivity = paramString5;
    this.mHasFullSizeIcon = paramBoolean;
    this.mCanonicalSourceName = paramString6;
    this.mInternalCorpusName = paramString7;
  }
  
  public boolean equals(Object paramObject)
  {
    return super.equals(paramObject);
  }
  
  public ApplicationInfo getApplicationInfo()
  {
    return this.mApplicationInfo;
  }
  
  public String getCanonicalName()
  {
    return this.mCanonicalSourceName;
  }
  
  public String getDefaultIntentAction()
  {
    return this.mDefaultIntentAction;
  }
  
  public String getDefaultIntentActivity()
  {
    return this.mDefaultIntentActivity;
  }
  
  public String getDefaultIntentData()
  {
    return this.mDefaultIntentData;
  }
  
  public String getInternalCorpusName()
  {
    return this.mInternalCorpusName;
  }
  
  public int getLabelResourceId()
  {
    return this.mLabelId;
  }
  
  public String getName()
  {
    return this.mName;
  }
  
  public String getPackageName()
  {
    return this.mPackageName;
  }
  
  public int getSettingsDescriptionResourceId()
  {
    return this.mSettingsDescriptionId;
  }
  
  public int getSourceIconResource()
  {
    return this.mIconResourceId;
  }
  
  public boolean hasFullSizeIcon()
  {
    return this.mHasFullSizeIcon;
  }
  
  public boolean isContactsSource()
  {
    return "contacts".equals(this.mCanonicalSourceName);
  }
  
  public boolean isEnabledByDefault()
  {
    return true;
  }
  
  public String toString()
  {
    return "IcingSource[name=" + this.mName + ", canonicalName=" + this.mCanonicalSourceName + "]";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.IcingSource
 * JD-Core Version:    0.7.0.1
 */