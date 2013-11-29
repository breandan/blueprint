package com.google.android.search.core.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.LocationSettings;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.prefetch.SearchResult;
import com.google.android.search.core.prefetch.WebPage;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.velvet.Cookies;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GsaWebViewController
{
  private final WebViewControllerClient mClient;
  private final Clock mClock;
  private Query mCommittedQuery;
  private final SearchConfig mConfig;
  private final Cookies mCookies;
  private Map<String, String> mDebugLastLoadHeaders;
  private final Runnable mEndPreviousResultsSuppression = new Runnable()
  {
    public void run()
    {
      if (GsaWebViewController.this.mSuppressingPreviousResults)
      {
        GsaWebViewController.access$202(GsaWebViewController.this, false);
        GsaWebViewController.this.handleStateChange();
      }
    }
  };
  private final File mGeolocationDir;
  private GsaCommunicationJsHelper mGsaCommunicationJsHelper;
  private final Runnable mHandleInternalEvents = new Runnable()
  {
    public void run()
    {
      synchronized (GsaWebViewController.this.mInternalEvents)
      {
        GsaWebViewController.this.handleInternalEvents();
        return;
      }
    }
  };
  private boolean mHasPendingHistoryClear;
  private final List<InternalEvent> mInternalEvents;
  private String mLastLoadUrl;
  private Uri mLastPage;
  private Query mLastQuery;
  private SearchResult mLastResult;
  private int mLoadState;
  private final LocationSettings mLocationSettings;
  private int mNumInternalEvents;
  private final WebView.PictureListener mPictureListener = new WebView.PictureListener()
  {
    @Deprecated
    public void onNewPicture(WebView paramAnonymousWebView, Picture paramAnonymousPicture)
    {
      if (GsaWebViewController.this.mLoadState == 2) {
        GsaWebViewController.this.postInternalReadyToShow();
      }
    }
  };
  private long mPreviousResultsSuppressedUntil;
  private final QueryState mQueryState;
  private boolean mSuppressingPreviousResults;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  private final SearchUrlHelper mUrlHelper;
  private final Supplier<String> mUserAgent;
  private WebView mWebView;
  private final GsaWebViewClient mWebViewClient;
  private final Object mWebViewClientLock = new Object();
  private boolean mWebViewInUse;
  
  public GsaWebViewController(SearchConfig paramSearchConfig, Clock paramClock, SearchUrlHelper paramSearchUrlHelper, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, WebViewControllerClient paramWebViewControllerClient, QueryState paramQueryState, LocationSettings paramLocationSettings, Executor paramExecutor, Supplier<String> paramSupplier, Cookies paramCookies, Context paramContext)
  {
    this.mConfig = paramSearchConfig;
    this.mClock = paramClock;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
    this.mLocationSettings = paramLocationSettings;
    this.mClient = paramWebViewControllerClient;
    this.mUserAgent = paramSupplier;
    this.mCookies = paramCookies;
    this.mInternalEvents = Lists.newArrayList();
    this.mWebViewClient = new GsaWebViewClient(null);
    this.mLoadState = 0;
    this.mQueryState = paramQueryState;
    this.mCommittedQuery = Query.EMPTY;
    this.mGeolocationDir = paramContext.getDir("webview_geolocation", 0);
  }
  
  private void dumpMap(String paramString, PrintWriter paramPrintWriter, Map<String, String> paramMap)
  {
    if (paramMap == null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("null");
    }
    for (;;)
    {
      return;
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramPrintWriter.print(paramString);
        paramPrintWriter.print((String)localEntry.getKey());
        paramPrintWriter.print(": ");
        paramPrintWriter.println(SearchUrlHelper.safeLogHeader((String)localEntry.getKey(), (String)localEntry.getValue()));
      }
    }
  }
  
  private void dumpUrlAndHeaders(String paramString1, PrintWriter paramPrintWriter, String paramString2, @Nullable String paramString3, Map<String, String> paramMap)
  {
    paramPrintWriter.print(paramString1);
    paramPrintWriter.print("Requested URL: ");
    paramPrintWriter.println(SearchUrlHelper.safeLogUrl(paramString2));
    if (paramString3 != null)
    {
      paramPrintWriter.print(paramString1);
      paramPrintWriter.print("Loaded URL: ");
      paramPrintWriter.println(SearchUrlHelper.safeLogUrl(paramString3));
    }
    if (paramMap != null)
    {
      paramPrintWriter.print(paramString1);
      paramPrintWriter.print("Headers (without auth or cookies): (");
      paramPrintWriter.print(paramMap.size());
      paramPrintWriter.println(")");
      dumpMap(paramString1 + "  ", paramPrintWriter, paramMap);
      return;
    }
    paramPrintWriter.print(paramString1);
    paramPrintWriter.println("Headers: null");
  }
  
  private String getLoadedUrl()
  {
    if (this.mWebView == null) {
      return null;
    }
    return this.mWebView.getUrl();
  }
  
  private void handleInternalEvents()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    Object localObject4 = null;
    Object localObject5;
    InternalEvent localInternalEvent;
    if (this.mWebViewInUse)
    {
      int i = 0;
      for (int j = 0;; j++)
      {
        int k = this.mNumInternalEvents;
        localObject5 = null;
        if (j >= k) {
          break label102;
        }
        localInternalEvent = (InternalEvent)this.mInternalEvents.get(j);
        if (localInternalEvent.mEvent == 3) {
          i = 1;
        }
        if (localInternalEvent.mEvent != 4) {
          break;
        }
        if (localObject3 == null) {
          localObject3 = localInternalEvent;
        }
      }
      if (localInternalEvent.mEvent != 0) {
        break label233;
      }
      localObject5 = localInternalEvent;
      localObject2 = null;
      label102:
      if (i != 0) {
        this.mGsaCommunicationJsHelper.registerJsBridge();
      }
      if (localObject1 != null) {
        handleLoadStarted(((InternalEvent)localObject1).mUrl);
      }
      if (localObject2 == null) {
        break label281;
      }
      if (!this.mUrlHelper.shouldAllowBackBetween(this.mLastPage, ((InternalEvent)localObject2).mUrl)) {
        this.mHasPendingHistoryClear = true;
      }
      this.mLastPage = ((InternalEvent)localObject2).mUrl;
      handlePendingClearHistory();
      handleLoadFinished(((InternalEvent)localObject2).mUrl);
      label173:
      if (localObject3 != null)
      {
        Uri localUri = localObject3.mUrl;
        if (localUri == null) {
          break label313;
        }
        this.mClient.onLinkClicked(localUri, Uri.parse(this.mLastLoadUrl));
      }
    }
    for (;;)
    {
      if ((localObject4 != null) && (this.mLoadState == 2)) {
        setLoadState(this.mCommittedQuery, 3);
      }
      releaseInternalEvents();
      return;
      label233:
      if (localInternalEvent.mEvent == 1)
      {
        localObject1 = localInternalEvent;
        localObject2 = null;
        break;
      }
      if (localInternalEvent.mEvent == 3)
      {
        localObject2 = localInternalEvent;
        break;
      }
      if (localInternalEvent.mEvent != 5) {
        break;
      }
      localObject4 = localInternalEvent;
      break;
      label281:
      if (localObject5 == null) {
        break label173;
      }
      this.mClient.onPageError(localObject5.mQuery, localObject5.mErrorCode, localObject5.mErrorDescription);
      break label173;
      label313:
      maybeSetNewQueryFromWebView(localObject3.mQuery);
    }
  }
  
  private void handleLoadFinished(Uri paramUri)
  {
    handleLoadStartedOrFinished(paramUri, 0, 11);
  }
  
  private void handleLoadStarted(Uri paramUri)
  {
    handleLoadStartedOrFinished(paramUri, 2, 9);
  }
  
  private void handleLoadStartedOrFinished(Uri paramUri, int paramInt1, int paramInt2)
  {
    Query localQuery = this.mUrlHelper.getQueryFromUrl(this.mCommittedQuery, paramUri);
    if (localQuery != null)
    {
      maybeSetNewQueryFromWebView(localQuery);
      this.mQueryState.reportLatencyEvent(paramInt2);
      setLoadState(this.mCommittedQuery, paramInt1);
    }
  }
  
  private void handlePendingClearHistory()
  {
    if ((this.mHasPendingHistoryClear) && (canUseWebView()))
    {
      this.mWebView.clearHistory();
      this.mHasPendingHistoryClear = false;
    }
  }
  
  private void handleStateChange()
  {
    if ((canUseWebView()) && (haveStartedLoading())) {
      setPreviousResultsSuppressionDeadline();
    }
    WebViewControllerClient localWebViewControllerClient = this.mClient;
    if (!this.mSuppressingPreviousResults) {}
    for (boolean bool = true;; bool = false)
    {
      localWebViewControllerClient.onStateChanged(bool);
      return;
    }
  }
  
  private boolean haveStartedLoading()
  {
    return this.mLoadState != 0;
  }
  
  @SuppressLint({"SetJavaScriptEnabled"})
  private void initWebSettings()
  {
    WebSettings localWebSettings = this.mWebView.getSettings();
    localWebSettings.setJavaScriptEnabled(true);
    localWebSettings.setDomStorageEnabled(true);
    localWebSettings.setUserAgentString((String)this.mUserAgent.get());
    localWebSettings.setSupportZoom(false);
    localWebSettings.setGeolocationEnabled(true);
    localWebSettings.setGeolocationDatabasePath(this.mGeolocationDir.getAbsolutePath());
  }
  
  private void initWebView()
  {
    if (this.mWebView != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      initWebSettings();
      this.mWebView.setWebViewClient(this.mWebViewClient);
      this.mWebView.setWebChromeClient(new GsaWebChromeClient(this.mWebView, this.mLocationSettings));
      this.mWebView.setPictureListener(this.mPictureListener);
      return;
    }
  }
  
  private void loadFromCacheFailed(String paramString, Query paramQuery, @Nullable Exception paramException)
  {
    int i = 444;
    if (paramException == null) {
      Log.e("Velvet.GsaWebViewController", "Could not load page from cache: " + SearchUrlHelper.safeLogUrl(paramString));
    }
    for (;;)
    {
      postInternalPageError(i, paramQuery, "No response");
      return;
      if ((paramException instanceof HttpHelper.HttpException)) {
        i = ((HttpHelper.HttpException)paramException).getStatusCode();
      }
      Log.e("Velvet.GsaWebViewController", "Error loading page: " + SearchUrlHelper.safeLogUrl(paramString), paramException);
    }
  }
  
  private void loadRegularSearchResults(@Nonnull Query paramQuery, @Nonnull SearchResult paramSearchResult)
  {
    Preconditions.checkNotNull(paramQuery);
    Preconditions.checkNotNull(paramSearchResult);
    this.mWebView.clearView();
    this.mWebView.loadUrl("about:blank");
    UriRequest localUriRequest = this.mUrlHelper.getSearchRequestNoAuthOrCookies(paramQuery, paramSearchResult.getSpeechRequestId());
    String str = localUriRequest.getUri().toString();
    Map localMap = localUriRequest.getHeadersCopy();
    this.mDebugLastLoadHeaders = localMap;
    setLastResult(str, paramSearchResult, paramQuery);
    this.mWebView.loadUrl(str, localMap);
  }
  
  private void maybeSetNewQueryFromWebView(Query paramQuery)
  {
    if (!this.mUrlHelper.equivalentForSearch(paramQuery, this.mCommittedQuery)) {
      this.mClient.onNewQuery(paramQuery);
    }
  }
  
  private void maybeSuppressPreviousResults()
  {
    long l = this.mClock.uptimeMillis();
    this.mUiThreadExecutor.cancelExecute(this.mEndPreviousResultsSuppression);
    if (this.mPreviousResultsSuppressedUntil > l)
    {
      this.mSuppressingPreviousResults = true;
      this.mUiThreadExecutor.executeDelayed(this.mEndPreviousResultsSuppression, this.mPreviousResultsSuppressedUntil - l);
      return;
    }
    this.mSuppressingPreviousResults = false;
  }
  
  private void postInternalEvent(int paramInt1, Uri paramUri, Query paramQuery, int paramInt2, String paramString)
  {
    synchronized (this.mInternalEvents)
    {
      if (this.mNumInternalEvents == this.mInternalEvents.size()) {
        this.mInternalEvents.add(new InternalEvent(null));
      }
      InternalEvent localInternalEvent = (InternalEvent)this.mInternalEvents.get(this.mNumInternalEvents);
      this.mNumInternalEvents = (1 + this.mNumInternalEvents);
      InternalEvent.access$2402(localInternalEvent, paramInt1);
      InternalEvent.access$2502(localInternalEvent, paramUri);
      InternalEvent.access$2602(localInternalEvent, paramQuery);
      InternalEvent.access$2702(localInternalEvent, paramInt2);
      InternalEvent.access$2802(localInternalEvent, paramString);
      this.mUiThreadExecutor.cancelExecute(this.mHandleInternalEvents);
      this.mUiThreadExecutor.executeDelayed(this.mHandleInternalEvents, 10L);
      return;
    }
  }
  
  private void postInternalInterceptedPageLoad(Uri paramUri, Query paramQuery)
  {
    postInternalEvent(4, paramUri, paramQuery, 0, null);
  }
  
  private void postInternalPageError(int paramInt, Query paramQuery, String paramString)
  {
    postInternalEvent(0, null, paramQuery, paramInt, paramString);
  }
  
  private void postInternalPageFinished(Uri paramUri)
  {
    postInternalEvent(3, paramUri, null, 0, null);
  }
  
  private void postInternalPageStarted(Uri paramUri)
  {
    postInternalEvent(1, paramUri, null, 0, null);
  }
  
  private void postInternalReadyToShow()
  {
    postInternalEvent(5, null, null, 0, null);
  }
  
  private void releaseInternalEvents()
  {
    while (this.mNumInternalEvents > 10)
    {
      List localList = this.mInternalEvents;
      int i = -1 + this.mNumInternalEvents;
      this.mNumInternalEvents = i;
      localList.remove(i);
    }
    this.mNumInternalEvents = 0;
  }
  
  private void setLastResult(String paramString, SearchResult paramSearchResult, Query paramQuery)
  {
    if ((paramString != null) && (paramSearchResult != null) && (paramQuery != null)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      synchronized (this.mWebViewClientLock)
      {
        this.mLastLoadUrl = paramString;
        this.mLastResult = paramSearchResult;
        this.mLastQuery = paramQuery;
        return;
      }
    }
  }
  
  private void setLoadState(Query paramQuery, int paramInt)
  {
    if (paramInt != this.mLoadState)
    {
      if (paramInt != 3) {
        break label33;
      }
      this.mClient.onStartResultsPage(paramQuery);
    }
    for (;;)
    {
      this.mLoadState = paramInt;
      handleStateChange();
      return;
      label33:
      if (paramInt == 0)
      {
        setPreviousResultsSuppressionDeadline();
        this.mClient.onEndResultsPage(paramQuery);
      }
    }
  }
  
  private void setPreviousResultsSuppressionDeadline()
  {
    int i = this.mConfig.getWebViewSuppressPreviousResultsForMs();
    this.mPreviousResultsSuppressedUntil = (this.mClock.uptimeMillis() + i);
  }
  
  private boolean shouldOverrideWebViewClick(Uri paramUri)
  {
    if (!this.mWebViewInUse) {
      return true;
    }
    Query localQuery = this.mUrlHelper.getQueryFromUrl(this.mCommittedQuery, paramUri);
    if (localQuery != null)
    {
      Log.i("Velvet.GsaWebViewController", "URL change initiated from the page");
      postInternalInterceptedPageLoad(null, localQuery);
      return true;
    }
    postInternalInterceptedPageLoad(paramUri, null);
    return true;
  }
  
  public boolean canUseWebView()
  {
    return this.mWebView != null;
  }
  
  public void clearWebViewsHistory()
  {
    this.mHasPendingHistoryClear = true;
  }
  
  public void dispose()
  {
    try
    {
      releaseInternalEvents();
      this.mUiThreadExecutor.cancelExecute(this.mHandleInternalEvents);
      if (this.mWebView != null)
      {
        this.mWebView.destroy();
        this.mWebView = null;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    synchronized (this.mWebViewClientLock)
    {
      String str1 = this.mLastLoadUrl;
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("GsaWebViewController state:");
      String str2 = paramString + "  ";
      paramPrintWriter.print(str2);
      paramPrintWriter.println("Last load:");
      String str3 = str2 + "  ";
      dumpUrlAndHeaders(str3, paramPrintWriter, str1, getLoadedUrl(), this.mDebugLastLoadHeaders);
      paramPrintWriter.print(str2);
      paramPrintWriter.println("SuppressPreviousResults:");
      paramPrintWriter.print(str3);
      paramPrintWriter.print(this.mSuppressingPreviousResults);
      paramPrintWriter.print("/");
      paramPrintWriter.println(this.mPreviousResultsSuppressedUntil);
      return;
    }
  }
  
  public void handleAgsaEvents(String paramString)
  {
    String str1;
    if (this.mWebViewInUse) {
      str1 = getLoadedUrl();
    }
    for (;;)
    {
      synchronized (this.mWebViewClientLock)
      {
        String str2 = this.mLastLoadUrl;
        localQuery = null;
        if (str2 != null)
        {
          boolean bool = this.mLastLoadUrl.equals(str1);
          localQuery = null;
          if (bool) {
            localQuery = this.mLastResult.getSrpQuery();
          }
        }
        if (localQuery != null)
        {
          if (TextUtils.isEmpty(paramString)) {
            break;
          }
          this.mClient.onShowedPrefetchedSrp(localQuery, paramString);
        }
        return;
      }
      Log.w("Velvet.GsaWebViewController", "Couldn't get WebView so couldn't send gen_204");
      Query localQuery = null;
    }
    Log.w("Velvet.GsaWebViewController", "Could not get event id from prefetched SRP");
  }
  
  public boolean onBackPressed()
  {
    WebBackForwardList localWebBackForwardList;
    Query localQuery;
    int i;
    if ((this.mWebViewInUse) && (this.mWebView.canGoBack()) && (!this.mHasPendingHistoryClear))
    {
      localWebBackForwardList = this.mWebView.copyBackForwardList();
      localQuery = null;
      i = localWebBackForwardList.getCurrentIndex();
    }
    for (int j = i - 1;; j--)
    {
      Uri localUri;
      if (j >= 0)
      {
        localUri = Uri.parse(localWebBackForwardList.getItemAtIndex(j).getUrl());
        localQuery = this.mUrlHelper.getQueryFromUrl(this.mCommittedQuery, localUri);
        if (localQuery == null) {}
      }
      else
      {
        if (localQuery != null) {
          break;
        }
        return false;
      }
      Log.w("Velvet.GsaWebViewController", "Went back to non-search URL:" + SearchUrlHelper.safeLogUrl(localUri));
    }
    if (!this.mUrlHelper.equivalentForSearch(localQuery, this.mCommittedQuery))
    {
      Log.w("Velvet.GsaWebViewController", "WebView back wants to change the query");
      return false;
    }
    this.mClient.onStartResultsPage(this.mCommittedQuery);
    this.mWebView.goBackOrForward(j - i);
    this.mClient.onEndResultsPage(this.mCommittedQuery);
    return true;
  }
  
  void removePendingHistoryClear()
  {
    this.mHasPendingHistoryClear = false;
  }
  
  void setCommittedQuery(Query paramQuery)
  {
    this.mCommittedQuery = paramQuery;
  }
  
  public void setWebViewAndGsaCommunicationJsHelper(WebView paramWebView, GsaCommunicationJsHelper paramGsaCommunicationJsHelper)
  {
    if (this.mWebView == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "WebView may only be set once.");
      this.mWebView = paramWebView;
      this.mWebView.setBackgroundColor(this.mWebView.getResources().getColor(2131230799));
      this.mGsaCommunicationJsHelper = paramGsaCommunicationJsHelper;
      initWebView();
      return;
    }
  }
  
  public void showSearchResult(@Nonnull Query paramQuery, @Nonnull SearchResult paramSearchResult)
  {
    Preconditions.checkNotNull(paramSearchResult);
    Preconditions.checkState(canUseWebView());
    this.mQueryState.reportLatencyEvent(4);
    this.mCommittedQuery = paramQuery;
    synchronized (this.mInternalEvents)
    {
      releaseInternalEvents();
      clearWebViewsHistory();
      loadRegularSearchResults(this.mCommittedQuery, paramSearchResult);
      this.mLoadState = 1;
      this.mWebViewInUse = true;
      this.mQueryState.reportLatencyEvent(7);
      maybeSuppressPreviousResults();
      return;
    }
  }
  
  private class GsaWebViewClient
    extends WebViewClient
  {
    private GsaWebViewClient() {}
    
    private WebResourceResponse makeWebResourceResponse(@Nonnull String paramString, @Nonnull SearchResult paramSearchResult, @Nonnull Query paramQuery)
    {
      try
      {
        Map localMap = paramSearchResult.getWebPage().getHeaders();
        WebResourceResponse localWebResourceResponse = paramSearchResult.getWebPage().toWebResourceResponse();
        if ((localMap != null) && (localWebResourceResponse != null))
        {
          GsaWebViewController.this.mCookies.setCookiesFromHeaders(paramString, localMap);
          localWebResourceResponse.setData(new WebViewInputStream(paramQuery, localWebResourceResponse.getData(), GsaWebViewController.this.mQueryState, GsaWebViewController.this.mUiThreadExecutor));
          return localWebResourceResponse;
        }
        Log.e("Velvet.GsaWebViewController", "Missing headers or response: " + paramString);
        GsaWebViewController.this.loadFromCacheFailed(paramString, paramQuery, null);
      }
      catch (Exception localException)
      {
        for (;;)
        {
          if (paramQuery != null) {
            GsaWebViewController.this.loadFromCacheFailed(paramString, paramQuery, localException);
          } else {
            Log.e("Velvet.GsaWebViewController", "Could not send exception to QueryState because query == null", localException);
          }
        }
      }
      return null;
    }
    
    public void onPageFinished(WebView paramWebView, String paramString)
    {
      if (!paramString.equals("about:blank")) {
        GsaWebViewController.this.postInternalPageFinished(Uri.parse(paramString));
      }
    }
    
    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
      if (!paramString.equals("about:blank")) {
        GsaWebViewController.this.postInternalPageStarted(Uri.parse(paramString));
      }
    }
    
    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
      if (GsaWebViewController.this.mWebViewInUse) {
        GsaWebViewController.this.postInternalPageError(paramInt, GsaWebViewController.this.mCommittedQuery, paramString1);
      }
    }
    
    public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError)
    {
      super.onReceivedSslError(paramWebView, paramSslErrorHandler, paramSslError);
    }
    
    public WebResourceResponse shouldInterceptRequest(WebView paramWebView, String paramString)
    {
      if ((paramString != null) && (paramString.startsWith("http"))) {
        synchronized (GsaWebViewController.this.mWebViewClientLock)
        {
          String str = GsaWebViewController.this.mLastLoadUrl;
          SearchResult localSearchResult = null;
          Query localQuery = null;
          if (str != null)
          {
            boolean bool = GsaWebViewController.this.mLastLoadUrl.equals(paramString);
            localSearchResult = null;
            localQuery = null;
            if (bool)
            {
              localSearchResult = GsaWebViewController.this.mLastResult;
              localQuery = GsaWebViewController.this.mLastQuery;
            }
          }
          if (localSearchResult != null) {
            return makeWebResourceResponse(paramString, localSearchResult, localQuery);
          }
        }
      }
      if (GsaWebViewController.this.mLoadState == 2) {
        GsaWebViewController.this.postInternalReadyToShow();
      }
      return null;
    }
    
    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      if (TextUtils.equals("about:blank", paramString)) {}
      Uri localUri;
      do
      {
        return false;
        localUri = Uri.parse(paramString);
        if (GsaWebViewController.this.mConfig.isGoogleSearchLogoutRedirect(localUri.getPath()))
        {
          GsaWebViewController.this.mClient.onLogoutRedirect();
          return true;
        }
      } while (((localUri.isRelative()) || (GsaWebViewController.this.mUrlHelper.isSearchAuthority(localUri.getAuthority()))) && (GsaWebViewController.this.mConfig.isGoogleUtilityPath(localUri.getPath())));
      return GsaWebViewController.this.shouldOverrideWebViewClick(localUri);
    }
  }
  
  private class InternalEvent
  {
    private int mErrorCode;
    private String mErrorDescription;
    private int mEvent;
    private Query mQuery;
    private Uri mUrl;
    
    private InternalEvent() {}
    
    private String eventString()
    {
      switch (this.mEvent)
      {
      default: 
        return "UNKNOWN(" + this.mEvent + ")";
      case 0: 
        return "ERROR";
      case 1: 
        return "STARTED";
      case 2: 
        return "PROGRESS";
      case 3: 
        return "FINISHED";
      case 4: 
        return "INTERCEPT";
      }
      return "READY_TO_SHOW";
    }
    
    public String toString()
    {
      return eventString() + ";" + this.mQuery + ";" + this.mUrl;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.webview.GsaWebViewController
 * JD-Core Version:    0.7.0.1
 */