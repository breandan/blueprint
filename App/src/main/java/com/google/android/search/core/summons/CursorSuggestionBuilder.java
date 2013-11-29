package com.google.android.search.core.summons;

import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.suggest.SuggestionBuilder;
import com.google.android.search.core.suggest.SuggestionFilter;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.SuggestionListFactory;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CursorSuggestionBuilder
{
  private final Cursor mCursor;
  @Nullable
  private final SuggestionFilter mFilter;
  private final int mFormatCol;
  private final int mIcon1Col;
  private final ContentProviderSource mSource;
  private final int mText1Col;
  private final int mText2Col;
  private final int mText2UrlCol;
  private final Query mUserQuery;
  
  public CursorSuggestionBuilder(ContentProviderSource paramContentProviderSource, Query paramQuery, Cursor paramCursor, @Nullable SuggestionFilter paramSuggestionFilter)
  {
    this.mSource = paramContentProviderSource;
    this.mUserQuery = paramQuery;
    this.mCursor = paramCursor;
    this.mFilter = paramSuggestionFilter;
    this.mFormatCol = getColumnIndex("suggest_format");
    this.mText1Col = getColumnIndex("suggest_text_1");
    this.mText2Col = getColumnIndex("suggest_text_2");
    this.mText2UrlCol = getColumnIndex("suggest_text_2_url");
    this.mIcon1Col = getColumnIndex("suggest_icon_1");
  }
  
  private CharSequence formatString(String paramString)
  {
    if ((paramString != null) && ("html".equals(getStringOrNull(this.mFormatCol)))) {
      paramString = Html.fromHtml(paramString);
    }
    return paramString;
  }
  
  private String getIcon1()
  {
    return Util.toResourceUriString(this.mSource.getIconPackage(), getStringOrNull(this.mIcon1Col));
  }
  
  private String getIntentExtraData()
  {
    return getStringOrNull("suggest_intent_extra_data");
  }
  
  private long getLastAccessTime()
  {
    return getLongOrDefault("suggest_last_access_hint", 0L);
  }
  
  private long getLongOrDefault(int paramInt, long paramLong)
  {
    if (this.mCursor == null) {}
    while (paramInt == -1) {
      return paramLong;
    }
    try
    {
      long l = this.mCursor.getLong(paramInt);
      return l;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("QSB.CursorSuggestionBuilder", "getLong() failed, ", localRuntimeException);
    }
    return paramLong;
  }
  
  private long getLongOrDefault(String paramString, long paramLong)
  {
    return getLongOrDefault(getColumnIndex(paramString), paramLong);
  }
  
  private String getQuery()
  {
    return getStringOrNull("suggest_intent_query");
  }
  
  private String getStringOrNull(int paramInt)
  {
    if (this.mCursor == null) {}
    while (paramInt == -1) {
      return null;
    }
    try
    {
      String str = this.mCursor.getString(paramInt);
      return str;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("QSB.CursorSuggestionBuilder", "getString() failed, ", localRuntimeException);
    }
    return null;
  }
  
  private String getStringOrNull(String paramString)
  {
    return getStringOrNull(getColumnIndex(paramString));
  }
  
  private String getSuggestionLogType()
  {
    return getStringOrNull("suggest_log_type");
  }
  
  private CharSequence getText1()
  {
    return formatString(getStringOrNull(this.mText1Col));
  }
  
  private CharSequence getText2()
  {
    return formatString(getStringOrNull(this.mText2Col));
  }
  
  private String getText2Url()
  {
    return getStringOrNull(this.mText2UrlCol);
  }
  
  private boolean isQsbApp(Suggestion paramSuggestion)
  {
    return ("content://applications/applications/com.google.android.googlequicksearchbox/com.google.android.googlequicksearchbox.SearchActivity".equals(paramSuggestion.getSuggestionIntentDataString())) && ("android.intent.action.MAIN".equals(paramSuggestion.getSuggestionIntentAction()));
  }
  
  private boolean shouldKeepSuggestion(Suggestion paramSuggestion)
  {
    if ((paramSuggestion != null) && (!TextUtils.isEmpty(paramSuggestion.getSuggestionText1())) && (!isQsbApp(paramSuggestion)))
    {
      if (this.mFilter != null) {
        return this.mFilter.accept(null, paramSuggestion);
      }
      return true;
    }
    return false;
  }
  
  @Nonnull
  public SuggestionList build(int paramInt)
  {
    if (this.mCursor == null) {
      return createSuggestionList(ImmutableList.of(), false);
    }
    boolean bool1;
    int i;
    if (!this.mCursor.isClosed())
    {
      bool1 = true;
      Preconditions.checkState(bool1);
      i = Math.min(this.mCursor.getCount(), paramInt);
      if (this.mCursor.getColumnIndex("suggest_last_access_hint") == -1) {
        break label131;
      }
    }
    ArrayList localArrayList;
    label131:
    for (boolean bool2 = true;; bool2 = false)
    {
      localArrayList = new ArrayList(i);
      for (int j = 0; j < i; j++)
      {
        this.mCursor.moveToPosition(j);
        Suggestion localSuggestion = getCurrentSuggestion();
        if (shouldKeepSuggestion(localSuggestion)) {
          localArrayList.add(localSuggestion);
        }
      }
      bool1 = false;
      break;
    }
    return createSuggestionList(localArrayList, bool2);
  }
  
  @Nonnull
  SuggestionList createSuggestionList(List<Suggestion> paramList, boolean paramBoolean)
  {
    return SuggestionListFactory.createSuggestionList(this.mSource.getName(), this.mUserQuery, paramList, paramBoolean);
  }
  
  int getColumnIndex(String paramString)
  {
    if (this.mCursor == null) {
      return -1;
    }
    try
    {
      int i = this.mCursor.getColumnIndex(paramString);
      return i;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("QSB.CursorSuggestionBuilder", "getColumnIndex() failed, ", localRuntimeException);
    }
    return -1;
  }
  
  protected Suggestion getCurrentSuggestion()
  {
    return SuggestionBuilder.builder().source(this.mSource).text1(getText1()).text2(getText2()).text2Url(getText2Url()).icon1(getIcon1()).lastAccessTime(getLastAccessTime()).intentAction(getIntentAction()).intentData(getIntentDataString()).intentExtraData(getIntentExtraData()).intentComponent(this.mSource.getIntentComponent()).suggestionQuery(getQuery()).logType(getSuggestionLogType()).build();
  }
  
  String getIntentAction()
  {
    String str1 = getStringOrNull("suggest_intent_action");
    if (str1 != null) {
      return str1;
    }
    if (this.mSource == null) {}
    for (String str2 = null;; str2 = this.mSource.getDefaultIntentAction()) {
      return str2;
    }
  }
  
  String getIntentDataString()
  {
    String str1 = getStringOrNull("suggest_intent_data");
    if ((str1 == null) && (this.mSource != null)) {
      str1 = this.mSource.getDefaultIntentData();
    }
    if (str1 != null)
    {
      String str2 = getStringOrNull("suggest_intent_data_id");
      if (str2 != null) {
        str1 = str1 + "/" + Uri.encode(str2);
      }
    }
    return str1;
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + "[" + this.mUserQuery + "]";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.CursorSuggestionBuilder
 * JD-Core Version:    0.7.0.1
 */