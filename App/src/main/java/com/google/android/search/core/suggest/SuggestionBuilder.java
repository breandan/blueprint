package com.google.android.search.core.suggest;

import android.content.ComponentName;
import com.google.android.search.core.google.complete.CompleteServerConstants;
import com.google.android.search.core.summons.Source;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Util;

public class SuggestionBuilder
{
  private boolean mHasFullSizeIcon;
  private String mIcon1;
  private String mIntentAction;
  private ComponentName mIntentComponent;
  private String mIntentData;
  private String mIntentExtraData;
  private boolean mIsApplicationSuggestion;
  private boolean mIsContactSuggestion;
  private boolean mIsCorrectionSuggestion;
  private boolean mIsFromIcing;
  private boolean mIsHistory;
  private boolean mIsNavSuggestion;
  private boolean mIsNowPromo;
  private boolean mIsWebSearchSuggestion;
  private boolean mIsWordByWordSuggestion;
  private long mLastAccessTime;
  private String mLogType;
  private Source mSource;
  private String mSourceCanonicalName;
  private String mSourceIcon;
  private String mSourcePackageName;
  private String mSuggestionQuery;
  private CharSequence mText1;
  private CharSequence mText2;
  private String mText2Url;
  
  public static SuggestionBuilder builder()
  {
    return new SuggestionBuilder();
  }
  
  public Suggestion build()
  {
    boolean bool1;
    boolean bool2;
    if (this.mSource == null) {
      if ((CompleteServerConstants.LOG_TYPE_DEVICE_HISTORY.equals(this.mLogType)) || (CompleteServerConstants.LOG_TYPE_DEVICE_UNDO_REWRITE.equals(this.mLogType)) || (CompleteServerConstants.LOG_TYPE_DEVICE_OFFLINE.equals(this.mLogType)))
      {
        bool1 = true;
        if ((!bool1) && (!CompleteServerConstants.LOG_TYPE_QUERY.equals(this.mLogType)) && (!CompleteServerConstants.LOG_TYPE_SEARCH_HISTORY.equals(this.mLogType))) {
          break label219;
        }
        bool2 = true;
        label80:
        this.mIsWebSearchSuggestion = bool2;
        this.mIsNavSuggestion = CompleteServerConstants.LOG_TYPE_NAV.equals(this.mLogType);
      }
    }
    for (;;)
    {
      return new Suggestion(this.mSourceCanonicalName, this.mSourcePackageName, this.mSourceIcon, this.mText1, this.mText2, this.mText2Url, this.mIcon1, this.mLastAccessTime, this.mIntentAction, this.mIntentData, this.mIntentExtraData, this.mIntentComponent, this.mSuggestionQuery, this.mLogType, this.mIsHistory, this.mIsWordByWordSuggestion, this.mIsCorrectionSuggestion, this.mIsApplicationSuggestion, this.mIsContactSuggestion, this.mIsWebSearchSuggestion, this.mIsNavSuggestion, this.mHasFullSizeIcon, this.mIsFromIcing, this.mIsNowPromo, bool1, CompleteServerConstants.LOG_TYPE_DEVICE_UNDO_REWRITE.equals(this.mLogType));
      bool1 = false;
      break;
      label219:
      bool2 = false;
      break label80;
      this.mIsContactSuggestion = this.mSource.isContactsSource();
      this.mSourcePackageName = this.mSource.getPackageName();
      this.mSourceCanonicalName = this.mSource.getCanonicalName();
      this.mIsApplicationSuggestion = "applications".equals(this.mSource.getCanonicalName());
      this.mHasFullSizeIcon = this.mSource.hasFullSizeIcon();
      this.mSourceIcon = Util.toResourceUriString(this.mSource.getPackageName(), this.mSource.getSourceIconResource());
      String str = this.mIntentAction;
      bool1 = false;
      if (str == null)
      {
        this.mIntentAction = this.mSource.getDefaultIntentAction();
        bool1 = false;
      }
    }
  }
  
  public SuggestionBuilder fromSuggestion(Suggestion paramSuggestion)
  {
    this.mSourcePackageName = paramSuggestion.getSourcePackageName();
    this.mSourceCanonicalName = paramSuggestion.getSourceCanonicalName();
    this.mSourceIcon = paramSuggestion.getSourceIcon();
    this.mText1 = paramSuggestion.getSuggestionText1();
    this.mText2 = paramSuggestion.getSuggestionText2();
    this.mText2Url = paramSuggestion.getSuggestionText2Url();
    this.mIcon1 = paramSuggestion.getSuggestionIcon1();
    this.mIntentAction = paramSuggestion.getSuggestionIntentAction();
    this.mIntentData = paramSuggestion.getSuggestionIntentDataString();
    this.mIntentExtraData = paramSuggestion.getSuggestionIntentExtraData();
    this.mIntentComponent = paramSuggestion.getSuggestionIntentComponent();
    this.mSuggestionQuery = paramSuggestion.getSuggestionQuery();
    this.mLogType = paramSuggestion.getSuggestionLogType();
    this.mIsHistory = paramSuggestion.isHistorySuggestion();
    this.mIsWordByWordSuggestion = paramSuggestion.isWordByWordSuggestion();
    this.mIsCorrectionSuggestion = paramSuggestion.isCorrectionSuggestion();
    this.mIsFromIcing = paramSuggestion.isFromIcing();
    this.mIsApplicationSuggestion = paramSuggestion.isApplicationSuggestion();
    this.mIsWebSearchSuggestion = paramSuggestion.isWebSearchSuggestion();
    this.mIsNavSuggestion = paramSuggestion.isNavSuggestion();
    this.mIsContactSuggestion = paramSuggestion.isContactSuggestion();
    this.mHasFullSizeIcon = paramSuggestion.hasFullSizeIcon();
    this.mIsNowPromo = paramSuggestion.isNowPromo();
    return this;
  }
  
  public SuggestionBuilder icon1(String paramString)
  {
    this.mIcon1 = paramString;
    return this;
  }
  
  public SuggestionBuilder intentAction(String paramString)
  {
    this.mIntentAction = paramString;
    return this;
  }
  
  public SuggestionBuilder intentComponent(ComponentName paramComponentName)
  {
    this.mIntentComponent = paramComponentName;
    return this;
  }
  
  public SuggestionBuilder intentData(String paramString)
  {
    this.mIntentData = paramString;
    return this;
  }
  
  public SuggestionBuilder intentExtraData(String paramString)
  {
    this.mIntentExtraData = paramString;
    return this;
  }
  
  public SuggestionBuilder isApplication(boolean paramBoolean)
  {
    this.mIsApplicationSuggestion = paramBoolean;
    return this;
  }
  
  public SuggestionBuilder isCorrectionSuggestion(boolean paramBoolean)
  {
    this.mIsCorrectionSuggestion = paramBoolean;
    return this;
  }
  
  public SuggestionBuilder isFromIcing(boolean paramBoolean)
  {
    this.mIsFromIcing = paramBoolean;
    return this;
  }
  
  public SuggestionBuilder isHistory(boolean paramBoolean)
  {
    this.mIsHistory = paramBoolean;
    return this;
  }
  
  public SuggestionBuilder isNowPromo(boolean paramBoolean)
  {
    this.mIsNowPromo = paramBoolean;
    return this;
  }
  
  public SuggestionBuilder lastAccessTime(long paramLong)
  {
    this.mLastAccessTime = paramLong;
    return this;
  }
  
  public SuggestionBuilder logType(String paramString)
  {
    this.mLogType = paramString;
    return this;
  }
  
  public SuggestionBuilder packageName(String paramString)
  {
    this.mSourcePackageName = paramString;
    return this;
  }
  
  public SuggestionBuilder source(Source paramSource)
  {
    this.mSource = paramSource;
    return this;
  }
  
  public SuggestionBuilder sourceCanonicalName(String paramString)
  {
    this.mSourceCanonicalName = paramString;
    return this;
  }
  
  public SuggestionBuilder suggestionQuery(String paramString)
  {
    this.mSuggestionQuery = paramString;
    return this;
  }
  
  public SuggestionBuilder text1(CharSequence paramCharSequence)
  {
    this.mText1 = paramCharSequence;
    return this;
  }
  
  public SuggestionBuilder text2(CharSequence paramCharSequence)
  {
    this.mText2 = paramCharSequence;
    return this;
  }
  
  public SuggestionBuilder text2Url(String paramString)
  {
    this.mText2Url = paramString;
    return this;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionBuilder
 * JD-Core Version:    0.7.0.1
 */