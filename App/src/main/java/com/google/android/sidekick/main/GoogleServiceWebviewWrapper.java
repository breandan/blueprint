package com.google.android.sidekick.main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class GoogleServiceWebviewWrapper
  extends Activity
{
  private static final Set<String> SCHEMES_TO_OPEN_IN_AN_APP = ImmutableSet.of("mailto", "market", "tel");
  private static final String TAG = Tag.getTag(GoogleServiceWebviewWrapper.class);
  Executor mBgExecutor;
  LoginHelper mLoginHelper;
  private String mTitle;
  Executor mUiExecutor;
  private Uri mUri;
  private String[] mUrlPrefixesStayInWebView;
  WebView mWebView;
  
  private void sendViewIntent(Uri paramUri)
  {
    startActivity(new Intent("android.intent.action.VIEW", paramUri));
  }
  
  private void updateActionBarIcon(String paramString)
  {
    if ("mail".equals(paramString))
    {
      ActionBar localActionBar = getActionBar();
      if (localActionBar != null) {
        localActionBar.setIcon(2130837747);
      }
    }
  }
  
  protected void handleIntent()
  {
    Intent localIntent = getIntent();
    if ((localIntent == null) || (localIntent.getData() == null))
    {
      finish();
      Log.w(TAG, "Uri required");
      return;
    }
    if (localIntent.hasExtra("webview_title")) {}
    for (String str1 = localIntent.getStringExtra("webview_title");; str1 = "")
    {
      this.mTitle = str1;
      this.mUrlPrefixesStayInWebView = localIntent.getStringArrayExtra("webview_url_prefixes");
      ActionBar localActionBar = getActionBar();
      if (localActionBar != null)
      {
        localActionBar.setDisplayOptions(4, 4);
        localActionBar.setTitle(Html.fromHtml(this.mTitle));
      }
      this.mWebView = new WebView(this, null, 0);
      this.mWebView.getSettings().setSaveFormData(false);
      boolean bool1 = localIntent.getBooleanExtra("enable_javascript", false);
      this.mWebView.getSettings().setJavaScriptEnabled(bool1);
      this.mWebView.setWebViewClient(new WebViewClientStub());
      this.mWebView.setWebChromeClient(new WebChromeClient()
      {
        public void onProgressChanged(WebView paramAnonymousWebView, int paramAnonymousInt)
        {
          GoogleServiceWebviewWrapper.this.setProgress(paramAnonymousInt * 100);
        }
      });
      setContentView(this.mWebView);
      Uri localUri = localIntent.getData();
      boolean bool2 = localIntent.hasExtra("webview_service");
      String str2 = null;
      if (bool2) {
        str2 = localIntent.getStringExtra("webview_service");
      }
      updateActionBarIcon(str2);
      new LoadServiceUrlTask(this.mUiExecutor, this.mBgExecutor, localUri, str2).execute(new Void[0]);
      return;
    }
  }
  
  protected void loadDependencies()
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    this.mUiExecutor = localVelvetServices.getAsyncServices().getUiThreadExecutor();
    this.mBgExecutor = localVelvetServices.getAsyncServices().getScheduledBackgroundExecutorService();
    this.mLoginHelper = localVelvetServices.getCoreServices().getLoginHelper();
  }
  
  void loadUrl(String paramString)
  {
    this.mWebView.loadUrl(paramString);
  }
  
  public void onBackPressed()
  {
    if (this.mWebView.canGoBack())
    {
      this.mWebView.goBack();
      return;
    }
    super.onBackPressed();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(2);
    setProgressBarVisibility(true);
    loadDependencies();
    handleIntent();
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == 16908332)
    {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  private class LoadServiceUrlTask
    extends ExecutorAsyncTask<Void, String>
  {
    private final String mService;
    private final Uri mTargetUri;
    
    public LoadServiceUrlTask(Executor paramExecutor1, Executor paramExecutor2, Uri paramUri, String paramString)
    {
      super(paramExecutor2);
      this.mTargetUri = paramUri;
      this.mService = paramString;
    }
    
    @Nullable
    protected String doInBackground(Void... paramVarArgs)
    {
      GoogleServiceWebviewWrapper.access$002(GoogleServiceWebviewWrapper.this, GoogleServiceWebviewWrapper.this.mLoginHelper.blockingGetGaiaWebLoginLink(this.mTargetUri, this.mService));
      if (GoogleServiceWebviewWrapper.this.mUri != null) {
        return GoogleServiceWebviewWrapper.this.mUri.toString();
      }
      return null;
    }
    
    protected void onPostExecute(@Nullable String paramString)
    {
      if (paramString != null)
      {
        GoogleServiceWebviewWrapper.this.loadUrl(paramString);
        return;
      }
      Log.e(GoogleServiceWebviewWrapper.TAG, "Failed to get login link for " + this.mService + ":" + this.mTargetUri);
      Toast.makeText(GoogleServiceWebviewWrapper.this.getApplicationContext(), 2131362633, 0).show();
      GoogleServiceWebviewWrapper.this.finish();
    }
  }
  
  class WebViewClientStub
    extends WebViewClient
  {
    WebViewClientStub() {}
    
    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
      Log.w(GoogleServiceWebviewWrapper.TAG, "Received error while loading page(" + paramInt + "): " + paramString1);
      GoogleServiceWebviewWrapper.this.sendViewIntent(GoogleServiceWebviewWrapper.this.mUri);
      GoogleServiceWebviewWrapper.this.finish();
    }
    
    public void onReceivedHttpAuthRequest(WebView paramWebView, HttpAuthHandler paramHttpAuthHandler, String paramString1, String paramString2)
    {
      Log.w(GoogleServiceWebviewWrapper.TAG, "Auth error while loading page");
      GoogleServiceWebviewWrapper.this.sendViewIntent(GoogleServiceWebviewWrapper.this.mUri);
      GoogleServiceWebviewWrapper.this.finish();
    }
    
    public void onReceivedLoginRequest(WebView paramWebView, String paramString1, String paramString2, String paramString3)
    {
      Log.w(GoogleServiceWebviewWrapper.TAG, "Login Request while loading page");
      GoogleServiceWebviewWrapper.this.sendViewIntent(GoogleServiceWebviewWrapper.this.mUri);
      GoogleServiceWebviewWrapper.this.finish();
    }
    
    public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError)
    {
      Log.w(GoogleServiceWebviewWrapper.TAG, "Ssl Error while loading page");
      GoogleServiceWebviewWrapper.this.sendViewIntent(GoogleServiceWebviewWrapper.this.mUri);
      GoogleServiceWebviewWrapper.this.finish();
    }
    
    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      Uri localUri = Uri.parse(paramString);
      if (localUri == null) {}
      String str3;
      do
      {
        String str1;
        do
        {
          return false;
          if (GoogleServiceWebviewWrapper.SCHEMES_TO_OPEN_IN_AN_APP.contains(localUri.getScheme()))
          {
            GoogleServiceWebviewWrapper.this.sendViewIntent(localUri);
            return true;
          }
          str1 = localUri.getHost();
        } while (str1 == null);
        String str2 = localUri.getPath();
        if (str2 == null) {
          str2 = "";
        }
        str3 = str1 + str2;
      } while (str3.startsWith("accounts.google."));
      if (GoogleServiceWebviewWrapper.this.mUrlPrefixesStayInWebView != null)
      {
        String[] arrayOfString = GoogleServiceWebviewWrapper.this.mUrlPrefixesStayInWebView;
        int i = arrayOfString.length;
        for (int j = 0;; j++)
        {
          if (j >= i) {
            break label161;
          }
          String str4 = arrayOfString[j];
          if ((str4.equals("*")) || (str3.startsWith(str4))) {
            break;
          }
        }
      }
      label161:
      GoogleServiceWebviewWrapper.this.sendViewIntent(localUri);
      return true;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.GoogleServiceWebviewWrapper
 * JD-Core Version:    0.7.0.1
 */