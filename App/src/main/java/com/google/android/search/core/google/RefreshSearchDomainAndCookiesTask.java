package com.google.android.search.core.google;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.util.ForceableLock;
import com.google.android.search.core.util.ForceableLock.Owner;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.UriRequest;
import com.google.android.shared.util.Clock;
import com.google.android.velvet.Cookies;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetStrictMode;
import com.google.common.base.Strings;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class RefreshSearchDomainAndCookiesTask
  implements ForceableLock.Owner, Callable<Void>
{
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final Cookies mCookies;
  private final ForceableLock mCookiesLock;
  private String mCountryCode;
  private String mDomain;
  private final VelvetFactory mFactory;
  private final String mGoogleAccountToUse;
  UriRequest mGsaSearchParametersRequest;
  private final HttpHelper mHttpHelper;
  private boolean mInterrupted;
  private final boolean mIsForcedRun;
  private final Object mLock;
  private String mLoggedInUser;
  private final LoginHelper mLoginHelper;
  private final boolean mNeedToLogout;
  private long mRefreshWebViewCookiesAt;
  private String mSearchLanguage;
  private final SearchSettings mSettings;
  private final String mTokenType;
  private final Executor mUiExecutor;
  private final SearchUrlHelper mUrlHelper;
  private boolean mUseSsl;
  private WebView mWebView;
  private boolean mWebViewDestroyDone;
  private boolean mWebViewError;
  private boolean mWebViewInitDone;
  private String mWebViewLoggedInAccount;
  private String mWebViewLoggedInDomain;
  Uri mWebViewLoginDomain;
  private boolean mWebViewPageFinished;
  private long mWebViewTimeoutAt;
  
  public RefreshSearchDomainAndCookiesTask(Clock paramClock, SearchConfig paramSearchConfig, SearchSettings paramSearchSettings, LoginHelper paramLoginHelper, SearchUrlHelper paramSearchUrlHelper, HttpHelper paramHttpHelper, Cookies paramCookies, ForceableLock paramForceableLock, Executor paramExecutor, VelvetFactory paramVelvetFactory, boolean paramBoolean)
  {
    this.mConfig = paramSearchConfig;
    this.mClock = paramClock;
    this.mSettings = paramSearchSettings;
    this.mHttpHelper = paramHttpHelper;
    this.mLoginHelper = paramLoginHelper;
    this.mUrlHelper = paramSearchUrlHelper;
    this.mCookies = paramCookies;
    this.mCookiesLock = paramForceableLock;
    this.mUiExecutor = paramExecutor;
    this.mFactory = paramVelvetFactory;
    this.mGoogleAccountToUse = this.mSettings.getGoogleAccountToUse();
    this.mWebViewLoggedInAccount = this.mSettings.getWebViewLoggedInAccount();
    this.mWebViewLoggedInDomain = this.mSettings.getWebViewLoggedInDomain();
    this.mTokenType = this.mConfig.getTextSearchTokenType();
    this.mRefreshWebViewCookiesAt = this.mSettings.getRefreshWebViewCookiesAt();
    this.mLock = new Object();
    boolean bool1;
    if ((this.mRefreshWebViewCookiesAt == 0L) || (!this.mTokenType.equals(this.mSettings.getTextSearchTokenTypeRefreshed())) || ((!TextUtils.isEmpty(this.mWebViewLoggedInAccount)) && (!TextUtils.equals(this.mWebViewLoggedInAccount, this.mGoogleAccountToUse))))
    {
      bool1 = true;
      this.mNeedToLogout = bool1;
      if ((!paramBoolean) && (this.mRefreshWebViewCookiesAt != 0L)) {
        break label226;
      }
    }
    label226:
    for (boolean bool2 = true;; bool2 = false)
    {
      this.mIsForcedRun = bool2;
      return;
      bool1 = false;
      break;
    }
  }
  
  private void checkInterrupted()
    throws InterruptedException
  {
    synchronized (this.mLock)
    {
      if (Thread.interrupted()) {
        this.mInterrupted = true;
      }
      if (this.mInterrupted) {
        throw new InterruptedException();
      }
    }
  }
  
  private void clearWebViewLoginState()
  {
    this.mSettings.setWebViewLoggedInAccount("");
    this.mSettings.setWebViewLoggedInDomain("");
    this.mSettings.setRefreshWebViewCookiesAt(0L);
    this.mWebViewLoggedInAccount = "";
    this.mWebViewLoggedInDomain = "";
    this.mRefreshWebViewCookiesAt = 0L;
  }
  
  private void createWebViewAndStartLoad(Uri paramUri)
  {
    synchronized (this.mLock)
    {
      if (!this.mInterrupted)
      {
        this.mWebView = this.mFactory.createOffscreenWebView();
        this.mWebView.setWebViewClient(new WatchRedirectsWebViewClient(null));
        this.mWebViewTimeoutAt = (this.mClock.uptimeMillis() + this.mConfig.getWebViewLoginLoadTimeoutMs());
        this.mWebView.loadUrl(paramUri.toString());
      }
      this.mWebViewInitDone = true;
      this.mLock.notifyAll();
      return;
    }
  }
  
  private void logoutCurrentUser()
  {
    this.mCookies.removeAllCookies();
    this.mCookies.sync();
    clearWebViewLoginState();
  }
  
  private void maybeDestroyWebview()
  {
    if (this.mWebView != null)
    {
      this.mUiExecutor.execute(new Runnable()
      {
        public void run()
        {
          synchronized (RefreshSearchDomainAndCookiesTask.this.mLock)
          {
            RefreshSearchDomainAndCookiesTask.this.mWebView.destroy();
            RefreshSearchDomainAndCookiesTask.access$402(RefreshSearchDomainAndCookiesTask.this, true);
            RefreshSearchDomainAndCookiesTask.this.mLock.notifyAll();
            return;
          }
        }
      });
      synchronized (this.mLock)
      {
        for (;;)
        {
          boolean bool = this.mWebViewDestroyDone;
          if (!bool) {
            try
            {
              this.mLock.wait();
            }
            catch (InterruptedException localInterruptedException)
            {
              this.mInterrupted = true;
            }
          }
        }
      }
    }
  }
  
  private boolean maybeStartWebViewLogin()
  {
    String str1 = this.mSettings.getWebViewLoggedInAccount();
    String str2 = this.mSettings.getWebViewLoggedInDomain();
    if ((TextUtils.equals(str1, this.mSettings.getGoogleAccountToUse())) && (TextUtils.equals(str2, this.mUrlHelper.getSearchDomain())) && (this.mRefreshWebViewCookiesAt >= this.mClock.currentTimeMillis())) {
      return false;
    }
    this.mWebViewLoginDomain = this.mUrlHelper.getLoginDomainUrl();
    final Uri localUri = this.mLoginHelper.blockingGetGaiaWebLoginLink(this.mWebViewLoginDomain, this.mConfig.getPersonalizedSearchService());
    if (localUri == null)
    {
      this.mSettings.setWebViewLoggedInDomain("");
      return false;
    }
    this.mUiExecutor.execute(new Runnable()
    {
      public void run()
      {
        RefreshSearchDomainAndCookiesTask.this.createWebViewAndStartLoad(localUri);
      }
    });
    synchronized (this.mLock)
    {
      for (;;)
      {
        boolean bool = this.mWebViewInitDone;
        if (!bool) {
          try
          {
            this.mLock.wait();
          }
          catch (InterruptedException localInterruptedException)
          {
            this.mInterrupted = true;
          }
        }
      }
    }
    return true;
  }
  
  private void updateSettingsFromWebViewLoginResult()
  {
    if (this.mWebViewError) {}
    while (!this.mWebViewPageFinished) {
      return;
    }
    this.mCookies.sync();
    this.mSettings.setWebViewLoggedInAccount(this.mGoogleAccountToUse);
    this.mSettings.setWebViewLoggedInDomain(this.mWebViewLoginDomain.getAuthority());
    this.mSettings.setRefreshWebViewCookiesAt(this.mClock.currentTimeMillis() + this.mConfig.getRefreshSearchParametersCookieRefreshPeriodMs());
  }
  
  private void updateWebViewLoginState(boolean paramBoolean1, boolean paramBoolean2)
  {
    for (;;)
    {
      long l;
      synchronized (this.mLock)
      {
        this.mWebViewPageFinished = paramBoolean1;
        this.mWebViewError = paramBoolean2;
        l = this.mClock.uptimeMillis();
        if (paramBoolean2)
        {
          this.mWebViewTimeoutAt = l;
          this.mLock.notifyAll();
          return;
        }
        if (paramBoolean1) {
          this.mWebViewTimeoutAt = (l + this.mConfig.getWebViewLoginRedirectTimeoutMs());
        }
      }
      this.mWebViewTimeoutAt = (l + this.mConfig.getWebViewLoginLoadTimeoutMs());
    }
  }
  
  private void waitForWebViewLoginToComplete()
    throws InterruptedException
  {
    while (this.mClock.uptimeMillis() < this.mWebViewTimeoutAt) {
      synchronized (this.mLock)
      {
        long l = this.mClock.uptimeMillis();
        Math.max(0L, this.mWebViewTimeoutAt - l);
        this.mLock.wait(this.mWebViewTimeoutAt - this.mClock.uptimeMillis());
      }
    }
  }
  
  public Void call()
  {
    try
    {
      if ((this.mIsForcedRun) || (this.mNeedToLogout))
      {
        this.mCookiesLock.forceObtain(this);
        if (this.mNeedToLogout) {
          logoutCurrentUser();
        }
      }
      boolean bool;
      do
      {
        Log.i("Search.RefreshSearchDomainAndCookiesTask", "refreshing search domain");
        checkInterrupted();
        String str = fetchSearchParameters();
        checkInterrupted();
        if ((parseSearchParametersJson(str)) && (validateSearchParams()))
        {
          checkInterrupted();
          saveToSettings();
          if ((TextUtils.isEmpty(this.mTokenType)) && (maybeStartWebViewLogin()))
          {
            Log.i("Search.RefreshSearchDomainAndCookiesTask", "refreshing cookies");
            waitForWebViewLoginToComplete();
            checkInterrupted();
            updateSettingsFromWebViewLoginResult();
          }
        }
        return null;
        bool = this.mCookiesLock.tryObtain(this);
      } while (bool);
      return null;
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.w("Search.RefreshSearchDomainAndCookiesTask", "refresh interrupted");
      return null;
    }
    finally
    {
      this.mSettings.setTextSearchTokenTypeRefreshed(this.mTokenType);
      maybeDestroyWebview();
      this.mCookiesLock.release(this);
    }
  }
  
  String fetchSearchParameters()
  {
    try
    {
      this.mGsaSearchParametersRequest = this.mUrlHelper.getGsaSearchParametersRequest();
      HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(this.mGsaSearchParametersRequest.getUri().toString(), this.mGsaSearchParametersRequest.getHeaders());
      localGetRequest.setUseCaches(false);
      String str = this.mHttpHelper.get(localGetRequest, 9);
      return str;
    }
    catch (IOException localIOException)
    {
      Log.w("Search.RefreshSearchDomainAndCookiesTask", "Search parameters fetch failed: " + localIOException);
    }
    return null;
  }
  
  public void forceReleaseLock()
  {
    synchronized (this.mLock)
    {
      this.mInterrupted = true;
      this.mLock.notifyAll();
      return;
    }
  }
  
  boolean parseSearchParametersJson(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      Log.w("Search.RefreshSearchDomainAndCookiesTask", "Search parameters fetch failed");
      return false;
    }
    JsonReader localJsonReader;
    for (;;)
    {
      String str;
      try
      {
        localJsonReader = new JsonReader(new StringReader(paramString));
        localJsonReader.beginObject();
        if (localJsonReader.peek() == JsonToken.END_OBJECT) {
          break;
        }
        str = localJsonReader.nextName();
        if (str.equals("domain"))
        {
          this.mDomain = localJsonReader.nextString();
          continue;
        }
        if (!str.equals("countryCode")) {
          break label129;
        }
      }
      catch (IOException localIOException)
      {
        Log.w("Search.RefreshSearchDomainAndCookiesTask", "Search parameters parsing failed: " + localIOException);
        return false;
      }
      this.mCountryCode = localJsonReader.nextString();
      continue;
      label129:
      if (str.equals("userLang")) {
        this.mSearchLanguage = localJsonReader.nextString();
      } else if (str.equals("loggedInUser")) {
        this.mLoggedInUser = localJsonReader.nextString();
      } else if (str.equals("useSsl")) {
        this.mUseSsl = localJsonReader.nextBoolean();
      } else {
        localJsonReader.skipValue();
      }
    }
    localJsonReader.endObject();
    localJsonReader.close();
    return true;
  }
  
  void saveToSettings()
  {
    String str1;
    String str2;
    if (this.mUseSsl)
    {
      str1 = "https";
      this.mSettings.setSearchDomain(str1, this.mDomain, this.mCountryCode, Strings.nullToEmpty(this.mSearchLanguage));
      str2 = this.mGsaSearchParametersRequest.getUri().getAuthority();
      if (!str2.equals(this.mUrlHelper.getSearchDomain())) {
        break label95;
      }
      this.mSettings.setWebViewLoggedInAccount(Strings.nullToEmpty(this.mLoggedInUser));
      this.mSettings.setWebViewLoggedInDomain(str2);
    }
    label95:
    while (!str2.equals(this.mUrlHelper.getDefaultSearchDomain()))
    {
      return;
      str1 = "http";
      break;
    }
    this.mSettings.setWebViewLoggedInAccount(Strings.nullToEmpty(this.mLoggedInUser));
  }
  
  public String toString()
  {
    return super.toString();
  }
  
  boolean validateSearchParams()
  {
    if (TextUtils.isEmpty(this.mDomain))
    {
      VelvetStrictMode.logW("Search.RefreshSearchDomainAndCookiesTask", "Search parameters didn't specify domain");
      return false;
    }
    if (TextUtils.isEmpty(this.mCountryCode))
    {
      VelvetStrictMode.logW("Search.RefreshSearchDomainAndCookiesTask", "Search parameters didn't specify country code");
      return false;
    }
    return true;
  }
  
  private class WatchRedirectsWebViewClient
    extends WebViewClient
  {
    private WatchRedirectsWebViewClient() {}
    
    public void onPageFinished(WebView paramWebView, String paramString)
    {
      RefreshSearchDomainAndCookiesTask.this.updateWebViewLoginState(true, false);
    }
    
    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
      RefreshSearchDomainAndCookiesTask.this.updateWebViewLoginState(false, false);
    }
    
    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
      Log.w("Search.RefreshSearchDomainAndCookiesTask", "Failed to log in: " + paramInt + " " + paramString1 + " from URL " + SearchUrlHelper.safeLogUrl(paramString2));
      RefreshSearchDomainAndCookiesTask.this.updateWebViewLoginState(false, true);
    }
    
    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      return false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.RefreshSearchDomainAndCookiesTask
 * JD-Core Version:    0.7.0.1
 */