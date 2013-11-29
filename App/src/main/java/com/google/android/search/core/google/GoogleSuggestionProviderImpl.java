package com.google.android.search.core.google;

import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.SuggestionListImpl;
import com.google.android.search.core.suggest.WebSuggestionsBackedCursor;
import com.google.android.search.shared.api.Query;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
import java.util.List;

public class GoogleSuggestionProviderImpl
{
  private final Context mContext;
  private WebSuggestSource mSource;
  private final UriMatcher mUriMatcher;
  private final VelvetServices mVelvetServices;
  
  public GoogleSuggestionProviderImpl(Context paramContext, VelvetServices paramVelvetServices)
  {
    this.mContext = paramContext;
    this.mVelvetServices = paramVelvetServices;
    this.mUriMatcher = buildUriMatcher(this.mContext);
  }
  
  private static UriMatcher buildUriMatcher(Context paramContext)
  {
    String str = getAuthority(paramContext);
    UriMatcher localUriMatcher = new UriMatcher(-1);
    localUriMatcher.addURI(str, "search_suggest_query", 0);
    localUriMatcher.addURI(str, "search_suggest_query/*", 0);
    localUriMatcher.addURI(str, "search_suggest_shortcut", 1);
    localUriMatcher.addURI(str, "search_suggest_shortcut/*", 1);
    return localUriMatcher;
  }
  
  private SuggestionList emptyIfNull(SuggestionList paramSuggestionList, Query paramQuery)
  {
    if (paramSuggestionList == null) {
      paramSuggestionList = new SuggestionListImpl(getSource().getSourceName(), paramQuery);
    }
    return paramSuggestionList;
  }
  
  private static String getAuthority(Context paramContext)
  {
    return paramContext.getPackageName() + ".google";
  }
  
  private Query getQuery(Uri paramUri)
  {
    if (paramUri.getPathSegments().size() > 1) {
      return Query.EMPTY.withQueryChars(paramUri.getLastPathSegment());
    }
    return Query.EMPTY;
  }
  
  private WebSuggestSource getSource()
  {
    try
    {
      if (this.mSource == null) {
        this.mSource = this.mVelvetServices.getFactory().createGoogleExternalSource();
      }
      WebSuggestSource localWebSuggestSource = this.mSource;
      return localWebSuggestSource;
    }
    finally {}
  }
  
  public String getType(Uri paramUri)
  {
    return "vnd.android.cursor.dir/vnd.android.search.suggest";
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    int i = this.mUriMatcher.match(paramUri);
    if (i == 0)
    {
      Query localQuery = getQuery(paramUri);
      return new WebSuggestionsBackedCursor(emptyIfNull(getSource().queryExternal(localQuery.getQueryStringForSuggest()), localQuery));
    }
    if (i == 1) {
      return null;
    }
    throw new IllegalArgumentException("Unknown URI " + paramUri);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.GoogleSuggestionProviderImpl
 * JD-Core Version:    0.7.0.1
 */