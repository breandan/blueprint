package com.google.android.search.core.summons;

import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.os.RemoteException;
import android.util.Pair;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.suggest.SuggestionFilter;
import com.google.android.search.core.suggest.SuggestionFilterProvider;
import com.google.android.shared.util.Clock;
import javax.annotation.Nullable;

public class SearchableSource
  implements ContentProviderSource
{
  private final ActivityInfo mActivityInfo;
  @Nullable
  private final Uri mBaseSuggestUri;
  @Nullable
  private final String mBaseSuggestUriString;
  private final String mCanonicalName;
  private final Clock mClock;
  private final boolean mEnabledByDefault;
  private final boolean mHasFullSizeIcon;
  private final String mName;
  private final SearchableInfo mSearchable;
  private final SuggestionFilterProvider mSuggestionFilterProvider;
  
  public SearchableSource(ActivityInfo paramActivityInfo, SearchConfig paramSearchConfig, Clock paramClock, SearchableInfo paramSearchableInfo, String paramString1, String paramString2, Uri paramUri, SuggestionFilterProvider paramSuggestionFilterProvider)
  {
    this.mClock = paramClock;
    this.mName = paramString1;
    this.mCanonicalName = paramString2;
    this.mSearchable = paramSearchableInfo;
    this.mBaseSuggestUri = paramUri;
    if (this.mBaseSuggestUri == null) {}
    for (String str = null;; str = this.mBaseSuggestUri.toString())
    {
      this.mBaseSuggestUriString = str;
      this.mActivityInfo = paramActivityInfo;
      this.mSuggestionFilterProvider = paramSuggestionFilterProvider;
      this.mEnabledByDefault = paramSearchConfig.isSourceEnabledByDefault(this.mBaseSuggestUriString, getName());
      this.mHasFullSizeIcon = paramSearchConfig.isFullSizeIconSource(this.mBaseSuggestUriString);
      return;
    }
  }
  
  private SuggestionFilter getSuggestionFilterForQuery(String paramString)
  {
    return this.mSuggestionFilterProvider.getFilter(this, paramString);
  }
  
  private Pair<Cursor, ContentProviderClient> getSuggestions(Context paramContext, SearchableInfo paramSearchableInfo, String paramString, int paramInt, CancellationSignal paramCancellationSignal)
    throws RemoteException
  {
    if (this.mBaseSuggestUri == null) {
      return null;
    }
    Uri.Builder localBuilder = this.mBaseSuggestUri.buildUpon();
    String str = paramSearchableInfo.getSuggestSelection();
    if (str != null) {}
    ContentProviderClient localContentProviderClient;
    for (String[] arrayOfString = { paramString };; arrayOfString = null)
    {
      localBuilder.appendQueryParameter("limit", String.valueOf(paramInt));
      Uri localUri = localBuilder.build();
      localContentProviderClient = paramContext.getContentResolver().acquireUnstableContentProviderClient(localUri);
      try
      {
        Cursor localCursor = localContentProviderClient.query(localUri, null, str, arrayOfString, null, paramCancellationSignal);
        return Pair.create(localCursor, localContentProviderClient);
      }
      catch (OperationCanceledException localOperationCanceledException)
      {
        if (paramCancellationSignal.isCanceled()) {
          break;
        }
        throw localOperationCanceledException;
      }
      localBuilder.appendPath(paramString);
    }
    return Pair.create(null, localContentProviderClient);
  }
  
  public boolean equals(Object paramObject)
  {
    return super.equals(paramObject);
  }
  
  public ApplicationInfo getApplicationInfo()
  {
    return this.mActivityInfo.applicationInfo;
  }
  
  public String getCanonicalName()
  {
    return this.mCanonicalName;
  }
  
  public String getDefaultIntentAction()
  {
    String str = this.mSearchable.getSuggestIntentAction();
    if (str != null) {
      return str;
    }
    return "android.intent.action.SEARCH";
  }
  
  public String getDefaultIntentData()
  {
    return this.mSearchable.getSuggestIntentData();
  }
  
  public String getIconPackage()
  {
    String str = this.mSearchable.getSuggestPackage();
    if (str != null) {
      return str;
    }
    return this.mSearchable.getSearchActivity().getPackageName();
  }
  
  public ComponentName getIntentComponent()
  {
    return this.mSearchable.getSearchActivity();
  }
  
  public int getLabelResourceId()
  {
    return this.mActivityInfo.labelRes;
  }
  
  public String getName()
  {
    return this.mName;
  }
  
  public String getPackageName()
  {
    return this.mActivityInfo.packageName;
  }
  
  public int getQueryThreshold()
  {
    return this.mSearchable.getSuggestThreshold();
  }
  
  public int getSettingsDescriptionResourceId()
  {
    return this.mSearchable.getSettingsDescriptionId();
  }
  
  public int getSourceIconResource()
  {
    return this.mActivityInfo.getIconResource();
  }
  
  @Nullable
  public String getSuggestUri()
  {
    return this.mBaseSuggestUriString;
  }
  
  /* Error */
  public com.google.android.search.core.suggest.SuggestionList getSuggestions(Context paramContext, com.google.android.search.shared.api.Query paramQuery, int paramInt, CancellationSignal paramCancellationSignal)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 5
    //   3: aload_2
    //   4: invokevirtual 210	com/google/android/search/shared/api/Query:getQueryStringForSuggest	()Ljava/lang/String;
    //   7: astore 6
    //   9: new 212	com/google/android/search/core/util/Latency
    //   12: dup
    //   13: aload_0
    //   14: getfield 31	com/google/android/search/core/summons/SearchableSource:mClock	Lcom/google/android/shared/util/Clock;
    //   17: invokespecial 215	com/google/android/search/core/util/Latency:<init>	(Lcom/google/android/shared/util/Clock;)V
    //   20: astore 7
    //   22: aload_0
    //   23: aload_1
    //   24: aload_0
    //   25: getfield 37	com/google/android/search/core/summons/SearchableSource:mSearchable	Landroid/app/SearchableInfo;
    //   28: aload 6
    //   30: iload_3
    //   31: aload 4
    //   33: invokespecial 217	com/google/android/search/core/summons/SearchableSource:getSuggestions	(Landroid/content/Context;Landroid/app/SearchableInfo;Ljava/lang/String;ILandroid/os/CancellationSignal;)Landroid/util/Pair;
    //   36: astore 5
    //   38: aload 5
    //   40: getfield 221	android/util/Pair:first	Ljava/lang/Object;
    //   43: checkcast 223	android/database/Cursor
    //   46: astore 14
    //   48: aload 14
    //   50: ifnull +85 -> 135
    //   53: aload 14
    //   55: invokeinterface 226 1 0
    //   60: pop
    //   61: new 228	com/google/android/search/core/summons/CursorSuggestionBuilder
    //   64: dup
    //   65: aload_0
    //   66: aload_2
    //   67: aload 14
    //   69: aload_0
    //   70: aload 6
    //   72: invokespecial 230	com/google/android/search/core/summons/SearchableSource:getSuggestionFilterForQuery	(Ljava/lang/String;)Lcom/google/android/search/core/suggest/SuggestionFilter;
    //   75: invokespecial 233	com/google/android/search/core/summons/CursorSuggestionBuilder:<init>	(Lcom/google/android/search/core/summons/ContentProviderSource;Lcom/google/android/search/shared/api/Query;Landroid/database/Cursor;Lcom/google/android/search/core/suggest/SuggestionFilter;)V
    //   78: iload_3
    //   79: invokevirtual 236	com/google/android/search/core/summons/CursorSuggestionBuilder:build	(I)Lcom/google/android/search/core/suggest/SuggestionList;
    //   82: astore 16
    //   84: aload 16
    //   86: aload 7
    //   88: invokevirtual 239	com/google/android/search/core/util/Latency:getLatency	()I
    //   91: invokeinterface 245 2 0
    //   96: aload 5
    //   98: ifnull +34 -> 132
    //   101: aload 5
    //   103: getfield 221	android/util/Pair:first	Ljava/lang/Object;
    //   106: checkcast 247	java/io/Closeable
    //   109: invokestatic 253	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   112: aload 5
    //   114: getfield 256	android/util/Pair:second	Ljava/lang/Object;
    //   117: ifnull +15 -> 132
    //   120: aload 5
    //   122: getfield 256	android/util/Pair:second	Ljava/lang/Object;
    //   125: checkcast 123	android/content/ContentProviderClient
    //   128: invokevirtual 259	android/content/ContentProviderClient:release	()Z
    //   131: pop
    //   132: aload 16
    //   134: areturn
    //   135: goto -74 -> 61
    //   138: astore 10
    //   140: ldc_w 261
    //   143: new 263	java/lang/StringBuilder
    //   146: dup
    //   147: invokespecial 264	java/lang/StringBuilder:<init>	()V
    //   150: aload_0
    //   151: invokevirtual 265	com/google/android/search/core/summons/SearchableSource:toString	()Ljava/lang/String;
    //   154: invokevirtual 269	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   157: ldc_w 271
    //   160: invokevirtual 269	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: aload 6
    //   165: invokevirtual 269	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: ldc_w 273
    //   171: invokevirtual 269	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: invokevirtual 274	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   177: aload 10
    //   179: invokestatic 280	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   182: pop
    //   183: new 282	com/google/android/search/core/suggest/SuggestionListImpl
    //   186: dup
    //   187: aload_0
    //   188: invokevirtual 49	com/google/android/search/core/summons/SearchableSource:getName	()Ljava/lang/String;
    //   191: aload_2
    //   192: invokespecial 285	com/google/android/search/core/suggest/SuggestionListImpl:<init>	(Ljava/lang/String;Lcom/google/android/search/shared/api/Query;)V
    //   195: astore 12
    //   197: aload 12
    //   199: iconst_1
    //   200: invokevirtual 289	com/google/android/search/core/suggest/SuggestionListImpl:setRequestFailed	(Z)V
    //   203: aload 5
    //   205: ifnull +34 -> 239
    //   208: aload 5
    //   210: getfield 221	android/util/Pair:first	Ljava/lang/Object;
    //   213: checkcast 247	java/io/Closeable
    //   216: invokestatic 253	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   219: aload 5
    //   221: getfield 256	android/util/Pair:second	Ljava/lang/Object;
    //   224: ifnull +15 -> 239
    //   227: aload 5
    //   229: getfield 256	android/util/Pair:second	Ljava/lang/Object;
    //   232: checkcast 123	android/content/ContentProviderClient
    //   235: invokevirtual 259	android/content/ContentProviderClient:release	()Z
    //   238: pop
    //   239: aload 12
    //   241: areturn
    //   242: astore 8
    //   244: aload 5
    //   246: ifnull +34 -> 280
    //   249: aload 5
    //   251: getfield 221	android/util/Pair:first	Ljava/lang/Object;
    //   254: checkcast 247	java/io/Closeable
    //   257: invokestatic 253	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   260: aload 5
    //   262: getfield 256	android/util/Pair:second	Ljava/lang/Object;
    //   265: ifnull +15 -> 280
    //   268: aload 5
    //   270: getfield 256	android/util/Pair:second	Ljava/lang/Object;
    //   273: checkcast 123	android/content/ContentProviderClient
    //   276: invokevirtual 259	android/content/ContentProviderClient:release	()Z
    //   279: pop
    //   280: aload 8
    //   282: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	283	0	this	SearchableSource
    //   0	283	1	paramContext	Context
    //   0	283	2	paramQuery	com.google.android.search.shared.api.Query
    //   0	283	3	paramInt	int
    //   0	283	4	paramCancellationSignal	CancellationSignal
    //   1	268	5	localPair	Pair
    //   7	157	6	str	String
    //   20	67	7	localLatency	com.google.android.search.core.util.Latency
    //   242	39	8	localObject	Object
    //   138	40	10	localException	java.lang.Exception
    //   195	45	12	localSuggestionListImpl	com.google.android.search.core.suggest.SuggestionListImpl
    //   46	22	14	localCursor	Cursor
    //   82	51	16	localSuggestionList	com.google.android.search.core.suggest.SuggestionList
    // Exception table:
    //   from	to	target	type
    //   9	48	138	java/lang/Exception
    //   53	61	138	java/lang/Exception
    //   61	96	138	java/lang/Exception
    //   9	48	242	finally
    //   53	61	242	finally
    //   61	96	242	finally
    //   140	203	242	finally
  }
  
  public boolean hasFullSizeIcon()
  {
    return this.mHasFullSizeIcon;
  }
  
  public boolean isContactsSource()
  {
    return "com.android.contacts".equals(this.mSearchable.getSuggestAuthority());
  }
  
  public boolean isEnabledByDefault()
  {
    return this.mEnabledByDefault;
  }
  
  public boolean queryAfterZeroResults()
  {
    return this.mSearchable.queryAfterZeroResults();
  }
  
  public String toString()
  {
    return "SearchableSource[name=" + this.mName + ", canonicalName=" + this.mCanonicalName + "]";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.SearchableSource
 * JD-Core Version:    0.7.0.1
 */