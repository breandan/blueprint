package com.google.android.search.core.prefetch;

import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.google.complete.SuggestionFetcher.SuggestionResponse;
import com.google.android.search.core.util.ExecutorObservable;
import com.google.android.search.core.util.VoiceCorrectionUtils;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.VoiceCorrectionSpan;
import com.google.android.shared.util.Util;
import com.google.android.shared.util.Whitespace;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.common.base.Preconditions;
import com.google.quality.genie.proto.QueryAlternativesProto.QueryAlternatives;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SearchResult
  extends ExecutorObservable
  implements SearchResultFetcher.FetchTaskConsumer
{
  private ActionData mActionData;
  private final Object mDataLock = new Object();
  @Nonnull
  private final SearchResultFetcher.FetchTask mFetchTask;
  private final long mFetchTimeMillis;
  @Nullable
  private final String mSpeechRequestId;
  private SrpMetadata mSrpMetadata;
  @Nonnull
  private Query mSrpQuery;
  private final SearchResultState mState = new SearchResultState();
  @Nullable
  private SuggestionFetcher.SuggestionResponse mSuggestions;
  @Nonnull
  private final Query mSuggestionsQuery;
  private WebPage mWebPage;
  
  private SearchResult(@Nullable Query paramQuery1, @Nullable Query paramQuery2, long paramLong, @Nullable String paramString, @Nonnull SearchResultFetcher.FetchTask paramFetchTask, @Nonnull Executor paramExecutor)
  {
    super((Executor)Preconditions.checkNotNull(paramExecutor));
    int i;
    int j;
    if (paramQuery1 != null)
    {
      i = 1;
      if (paramQuery2 == null) {
        break label104;
      }
      j = 1;
      label48:
      Preconditions.checkArgument(j ^ i);
      if (paramQuery1 == null) {
        break label110;
      }
      label60:
      this.mSrpQuery = paramQuery1;
      if (paramQuery2 == null) {
        break label117;
      }
    }
    for (;;)
    {
      this.mSuggestionsQuery = paramQuery2;
      this.mFetchTimeMillis = paramLong;
      this.mSpeechRequestId = paramString;
      this.mFetchTask = ((SearchResultFetcher.FetchTask)Preconditions.checkNotNull(paramFetchTask));
      return;
      i = 0;
      break;
      label104:
      j = 0;
      break label48;
      label110:
      paramQuery1 = Query.EMPTY;
      break label60;
      label117:
      paramQuery2 = Query.EMPTY;
    }
  }
  
  public static SearchResult forSrp(@Nonnull Query paramQuery, long paramLong, @Nullable String paramString, @Nonnull SearchResultFetcher.FetchTask paramFetchTask, @Nonnull Executor paramExecutor)
  {
    return new SearchResult(paramQuery, null, paramLong, paramString, paramFetchTask, paramExecutor);
  }
  
  public static SearchResult forSuggestionsAndSrp(@Nonnull Query paramQuery, long paramLong, @Nonnull SearchResultFetcher.FetchTask paramFetchTask, @Nonnull Executor paramExecutor)
  {
    return new SearchResult(null, paramQuery, paramLong, null, paramFetchTask, paramExecutor);
  }
  
  private void updateObservers()
  {
    setChanged();
    notifyObservers();
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("WebPage:");
    String str = paramString + "  ";
    for (;;)
    {
      synchronized (this.mDataLock)
      {
        Query localQuery = this.mSrpQuery;
        WebPage localWebPage = this.mWebPage;
        ActionData localActionData = this.mActionData;
        DumpUtils.println(paramPrintWriter, new Object[] { str, "mQuery: ", localQuery });
        Object[] arrayOfObject1 = new Object[3];
        arrayOfObject1[0] = str;
        arrayOfObject1[1] = "mFetchTimeMillis: ";
        arrayOfObject1[2] = Long.valueOf(this.mFetchTimeMillis);
        DumpUtils.println(paramPrintWriter, arrayOfObject1);
        Object[] arrayOfObject2 = new Object[3];
        arrayOfObject2[0] = str;
        arrayOfObject2[1] = "mSpeechRequestId: ";
        arrayOfObject2[2] = this.mSpeechRequestId;
        DumpUtils.println(paramPrintWriter, arrayOfObject2);
        DumpUtils.println(paramPrintWriter, new Object[] { str, "mActionData: ", localActionData });
        DumpUtils.println(paramPrintWriter, new Object[] { str, "mWebPage: ", localWebPage });
        if (localWebPage != null)
        {
          Object[] arrayOfObject4 = new Object[3];
          arrayOfObject4[0] = str;
          arrayOfObject4[1] = "srp.mHeaders: ";
          arrayOfObject4[2] = localWebPage.mHeaders;
          DumpUtils.println(paramPrintWriter, arrayOfObject4);
          Object[] arrayOfObject5 = new Object[3];
          arrayOfObject5[0] = str;
          arrayOfObject5[1] = "srp.mContentType: ";
          arrayOfObject5[2] = localWebPage.mContentType;
          DumpUtils.println(paramPrintWriter, arrayOfObject5);
          Object[] arrayOfObject6 = new Object[3];
          arrayOfObject6[0] = str;
          arrayOfObject6[1] = "srp.mContentStream: ";
          arrayOfObject6[2] = localWebPage.mContentStream;
          DumpUtils.println(paramPrintWriter, arrayOfObject6);
          Object[] arrayOfObject3 = new Object[3];
          arrayOfObject3[0] = str;
          arrayOfObject3[1] = "mState: ";
          arrayOfObject3[2] = this.mState;
          DumpUtils.println(paramPrintWriter, arrayOfObject3);
          return;
        }
      }
      DumpUtils.println(paramPrintWriter, new Object[] { str, "SearchResultPage not set" });
    }
  }
  
  /* Error */
  public void dumpContent(PrintWriter paramPrintWriter)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 49	com/google/android/search/core/prefetch/SearchResult:mDataLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 116	com/google/android/search/core/prefetch/SearchResult:mWebPage	Lcom/google/android/search/core/prefetch/WebPage;
    //   11: astore 4
    //   13: aload_2
    //   14: monitorexit
    //   15: aload 4
    //   17: ifnonnull +15 -> 32
    //   20: aload_1
    //   21: ldc 171
    //   23: invokevirtual 101	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   26: return
    //   27: astore_3
    //   28: aload_2
    //   29: monitorexit
    //   30: aload_3
    //   31: athrow
    //   32: aload 4
    //   34: invokevirtual 175	com/google/android/search/core/prefetch/WebPage:toWebResourceResponse	()Landroid/webkit/WebResourceResponse;
    //   37: astore 5
    //   39: aload 5
    //   41: ifnonnull +10 -> 51
    //   44: aload_1
    //   45: ldc 177
    //   47: invokevirtual 101	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   50: return
    //   51: aconst_null
    //   52: astore 6
    //   54: new 179	java/io/InputStreamReader
    //   57: dup
    //   58: aload 5
    //   60: invokevirtual 185	android/webkit/WebResourceResponse:getData	()Ljava/io/InputStream;
    //   63: aload 5
    //   65: invokevirtual 188	android/webkit/WebResourceResponse:getEncoding	()Ljava/lang/String;
    //   68: invokespecial 191	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   71: astore 7
    //   73: aload 7
    //   75: aload_1
    //   76: invokestatic 197	com/google/common/io/CharStreams:copy	(Ljava/lang/Readable;Ljava/lang/Appendable;)J
    //   79: pop2
    //   80: aload 7
    //   82: invokestatic 203	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   85: return
    //   86: astore 8
    //   88: aload_1
    //   89: ldc 177
    //   91: invokevirtual 101	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   94: ldc 205
    //   96: ldc 207
    //   98: aload 8
    //   100: invokestatic 213	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   103: pop
    //   104: aload 6
    //   106: invokestatic 203	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   109: return
    //   110: astore 11
    //   112: aload_1
    //   113: ldc 177
    //   115: invokevirtual 101	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   118: ldc 205
    //   120: ldc 215
    //   122: aload 11
    //   124: invokestatic 213	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   127: pop
    //   128: aload 6
    //   130: invokestatic 203	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   133: return
    //   134: astore 9
    //   136: aload 6
    //   138: invokestatic 203	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   141: aload 9
    //   143: athrow
    //   144: astore 9
    //   146: aload 7
    //   148: astore 6
    //   150: goto -14 -> 136
    //   153: astore 11
    //   155: aload 7
    //   157: astore 6
    //   159: goto -47 -> 112
    //   162: astore 8
    //   164: aload 7
    //   166: astore 6
    //   168: goto -80 -> 88
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	171	0	this	SearchResult
    //   0	171	1	paramPrintWriter	PrintWriter
    //   4	25	2	localObject1	Object
    //   27	4	3	localObject2	Object
    //   11	22	4	localWebPage	WebPage
    //   37	27	5	localWebResourceResponse	android.webkit.WebResourceResponse
    //   52	115	6	localObject3	Object
    //   71	94	7	localInputStreamReader	java.io.InputStreamReader
    //   86	13	8	localUnsupportedEncodingException1	java.io.UnsupportedEncodingException
    //   162	1	8	localUnsupportedEncodingException2	java.io.UnsupportedEncodingException
    //   134	8	9	localObject4	Object
    //   144	1	9	localObject5	Object
    //   110	13	11	localIOException1	IOException
    //   153	1	11	localIOException2	IOException
    // Exception table:
    //   from	to	target	type
    //   7	15	27	finally
    //   28	30	27	finally
    //   54	73	86	java/io/UnsupportedEncodingException
    //   54	73	110	java/io/IOException
    //   54	73	134	finally
    //   88	104	134	finally
    //   112	128	134	finally
    //   73	80	144	finally
    //   73	80	153	java/io/IOException
    //   73	80	162	java/io/UnsupportedEncodingException
  }
  
  public ActionData getActionData()
  {
    synchronized (this.mDataLock)
    {
      ActionData localActionData = this.mActionData;
      return localActionData;
    }
  }
  
  public IOException getError()
  {
    return this.mState.getError();
  }
  
  public long getFetchTimeMillis()
  {
    return this.mFetchTimeMillis;
  }
  
  public String getSpeechRequestId()
  {
    return this.mSpeechRequestId;
  }
  
  public SrpMetadata getSrpMetadata()
  {
    synchronized (this.mDataLock)
    {
      SrpMetadata localSrpMetadata = this.mSrpMetadata;
      return localSrpMetadata;
    }
  }
  
  @Nonnull
  public Query getSrpQuery()
  {
    synchronized (this.mDataLock)
    {
      Query localQuery = this.mSrpQuery;
      return localQuery;
    }
  }
  
  public SuggestionFetcher.SuggestionResponse getSuggestions()
  {
    synchronized (this.mDataLock)
    {
      SuggestionFetcher.SuggestionResponse localSuggestionResponse = this.mSuggestions;
      return localSuggestionResponse;
    }
  }
  
  public WebPage getWebPage()
  {
    synchronized (this.mDataLock)
    {
      if ((this.mWebPage != null) && (this.mSrpQuery != null))
      {
        WebPage localWebPage = this.mWebPage;
        return localWebPage;
      }
      return null;
    }
  }
  
  public boolean hasSuggestions()
  {
    for (;;)
    {
      synchronized (this.mDataLock)
      {
        if (this.mSuggestions != null)
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public boolean isCancelled()
  {
    return this.mState.isCancelled();
  }
  
  public boolean isComplete()
  {
    return this.mState.isComplete();
  }
  
  public boolean isFailed()
  {
    return this.mState.isFailed();
  }
  
  public boolean isFailedOrCancelled()
  {
    return this.mState.isCancelledOrFailed();
  }
  
  public boolean isLoading()
  {
    return this.mState.isLoading();
  }
  
  public void setActionData(ActionData paramActionData)
  {
    if (!isFailedOrCancelled()) {}
    synchronized (this.mDataLock)
    {
      if (this.mActionData != null) {
        VelvetStrictMode.logW("Velvet.SearchResult", "Received multiple ActionData objects");
      }
      this.mActionData = paramActionData;
      setChanged();
      notifyObservers();
      return;
    }
  }
  
  public void setCancelled()
  {
    if (this.mState.setCancelled())
    {
      updateObservers();
      this.mFetchTask.cancel();
      setWebPage(null);
    }
  }
  
  public void setComplete()
  {
    if (this.mState.setComplete()) {
      updateObservers();
    }
  }
  
  public void setFailed(IOException paramIOException)
  {
    if (this.mState.setFailed(paramIOException))
    {
      updateObservers();
      setWebPage(null);
    }
  }
  
  public void setSrpMetadata(SrpMetadata paramSrpMetadata)
  {
    if (!isFailedOrCancelled()) {}
    synchronized (this.mDataLock)
    {
      if (this.mSrpMetadata != null) {
        VelvetStrictMode.logW("Velvet.SearchResult", "Received multiple SrpMetadata objects");
      }
      this.mSrpMetadata = paramSrpMetadata;
      if ((this.mSrpMetadata.mRewrittenQueryString != null) && (!this.mSrpMetadata.mRewrittenQueryString.equals(this.mSrpQuery.getQueryStringForSearch()))) {
        this.mSrpQuery = this.mSrpQuery.withRewrittenQuery(this.mSrpMetadata.mRewrittenQueryString);
      }
      if ((this.mSrpQuery.needVoiceCorrection()) && (this.mSrpMetadata.mVoiceCorrection != null) && (!this.mSrpQuery.hasSpans(VoiceCorrectionSpan.class)))
      {
        Spanned localSpanned = VoiceCorrectionUtils.getSpannedString(this.mSrpQuery, this.mSrpMetadata.mVoiceCorrection);
        if (localSpanned != null) {
          this.mSrpQuery = this.mSrpQuery.withNewQueryChars(localSpanned);
        }
      }
      setChanged();
      notifyObservers();
      return;
    }
  }
  
  public void setSrpQuery(String paramString)
  {
    if (!isFailedOrCancelled()) {}
    synchronized (this.mDataLock)
    {
      if (this.mSrpQuery == Query.EMPTY) {
        this.mSrpQuery = this.mSuggestionsQuery.fromPrefetch(paramString);
      }
      String str;
      do
      {
        setChanged();
        notifyObservers();
        return;
        str = Whitespace.trimAndCollapseFrom(paramString, ' ');
      } while (this.mSrpQuery.getQueryStringForSearch().equals(str));
      VelvetStrictMode.logW("Velvet.SearchResult", "Received query string \"" + str + "\" does not match existing one \"" + this.mSrpQuery.getQueryStringForSearch() + "\"");
    }
  }
  
  public void setSuggestions(SuggestionFetcher.SuggestionResponse paramSuggestionResponse)
  {
    if (!isFailedOrCancelled()) {}
    synchronized (this.mDataLock)
    {
      String str;
      if (this.mSuggestions != null)
      {
        BugLogger.record(11450669);
        str = "Old: " + Util.boundedDebugString(this.mSuggestions.mJson, 500) + ", New: " + Util.boundedDebugString(paramSuggestionResponse.mJson, 500);
        if (this.mSuggestions.mEventId.equals(paramSuggestionResponse.mEventId)) {
          VelvetStrictMode.logW("Velvet.SearchResult", "Received multiple SuggestionResponse objects in the same response. " + str);
        }
      }
      else
      {
        this.mSuggestions = paramSuggestionResponse;
        setChanged();
        notifyObservers();
        return;
      }
      VelvetStrictMode.logW("Velvet.SearchResult", "Received multiple SuggestionResponse objects from different responses. " + str);
    }
  }
  
  public void setWebPage(WebPage paramWebPage)
  {
    if (!isFailedOrCancelled()) {}
    synchronized (this.mDataLock)
    {
      if (this.mWebPage != null) {
        VelvetStrictMode.logW("Velvet.SearchResult", "Received multiple WebPage objects");
      }
      this.mWebPage = paramWebPage;
      setChanged();
      notifyObservers();
      return;
    }
  }
  
  public void startFetch()
  {
    this.mFetchTask.startFetch(this);
  }
  
  public String toString()
  {
    return "SearchResult[" + this.mState + ", mSuggestionsQuery=" + this.mSuggestionsQuery + ", mSrpQuery=" + this.mSrpQuery + ", " + this.mActionData + "]";
  }
  
  private static class SearchResultState
  {
    private IOException mError = null;
    private int mState = 0;
    private Object mStateLock = new Object();
    
    private static String stateToString(int paramInt, IOException paramIOException)
    {
      switch (paramInt)
      {
      default: 
        return "UNKNOWN";
      case 0: 
        return "loading";
      case 2: 
        return "failed with error: " + paramIOException;
      case 1: 
        return "cancelled";
      }
      return "complete";
    }
    
    private boolean tryUpdateState(int paramInt, IOException paramIOException)
    {
      for (;;)
      {
        synchronized (this.mStateLock)
        {
          boolean bool1 = isLoading();
          bool2 = false;
          if (bool1)
          {
            this.mState = paramInt;
            if (paramInt == 2) {
              this.mError = paramIOException;
            }
          }
          else
          {
            if ((!bool2) && (this.mState != paramInt)) {
              Log.i("Velvet.SearchResult", "Cannot set state to " + stateToString(paramInt, paramIOException) + " because it is already " + stateToString(this.mState, this.mError));
            }
            return bool2;
          }
        }
        boolean bool2 = true;
      }
    }
    
    public IOException getError()
    {
      synchronized (this.mStateLock)
      {
        IOException localIOException = this.mError;
        return localIOException;
      }
    }
    
    public boolean isCancelled()
    {
      int j;
      for (int i = 1;; j = 0) {
        synchronized (this.mStateLock)
        {
          if (this.mState == i) {
            return i;
          }
        }
      }
    }
    
    public boolean isCancelledOrFailed()
    {
      int j;
      for (int i = 1;; j = 0) {
        synchronized (this.mStateLock)
        {
          if ((this.mState == i) || (this.mState == 2)) {
            return i;
          }
        }
      }
    }
    
    public boolean isComplete()
    {
      for (;;)
      {
        synchronized (this.mStateLock)
        {
          if (this.mState == 3)
          {
            bool = true;
            return bool;
          }
        }
        boolean bool = false;
      }
    }
    
    public boolean isFailed()
    {
      for (;;)
      {
        synchronized (this.mStateLock)
        {
          if (this.mState == 2)
          {
            bool = true;
            return bool;
          }
        }
        boolean bool = false;
      }
    }
    
    public boolean isLoading()
    {
      for (;;)
      {
        synchronized (this.mStateLock)
        {
          if (this.mState == 0)
          {
            bool = true;
            return bool;
          }
        }
        boolean bool = false;
      }
    }
    
    public boolean setCancelled()
    {
      return tryUpdateState(1, null);
    }
    
    public boolean setComplete()
    {
      return tryUpdateState(3, null);
    }
    
    public boolean setFailed(IOException paramIOException)
    {
      return tryUpdateState(2, paramIOException);
    }
    
    public String toString()
    {
      synchronized (this.mStateLock)
      {
        String str = stateToString(this.mState, this.mError);
        return str;
      }
    }
  }
  
  public static class SrpMetadata
  {
    public final String mEventId;
    public final String mRewrittenQueryString;
    public final QueryAlternativesProto.QueryAlternatives mVoiceCorrection;
    
    public SrpMetadata(String paramString1, String paramString2, QueryAlternativesProto.QueryAlternatives paramQueryAlternatives)
    {
      this.mEventId = paramString1;
      this.mRewrittenQueryString = paramString2;
      this.mVoiceCorrection = paramQueryAlternatives;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof SrpMetadata)) {
        return false;
      }
      if ((TextUtils.equals(this.mEventId, ((SrpMetadata)paramObject).mEventId)) && (TextUtils.equals(this.mRewrittenQueryString, ((SrpMetadata)paramObject).mRewrittenQueryString)) && (this.mVoiceCorrection == ((SrpMetadata)paramObject).mVoiceCorrection)) {}
      for (boolean bool = true;; bool = false) {
        return bool;
      }
    }
    
    public String toString()
    {
      return "SrpMetadata{" + this.mEventId + ", " + this.mRewrittenQueryString + "}";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.SearchResult
 * JD-Core Version:    0.7.0.1
 */