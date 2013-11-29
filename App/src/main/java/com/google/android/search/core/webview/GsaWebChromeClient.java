package com.google.android.search.core.webview;

import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.google.android.search.core.google.LocationSettings;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;

public class GsaWebChromeClient
  extends WebChromeClient
{
  private final WebView mClientWebView;
  private final LocationSettings mLocationSettings;
  
  public GsaWebChromeClient(@Nonnull WebView paramWebView, @Nonnull LocationSettings paramLocationSettings)
  {
    this.mClientWebView = ((WebView)Preconditions.checkNotNull(paramWebView));
    this.mLocationSettings = ((LocationSettings)Preconditions.checkNotNull(paramLocationSettings));
  }
  
  public boolean onConsoleMessage(ConsoleMessage paramConsoleMessage)
  {
    return true;
  }
  
  public void onGeolocationPermissionsShowPrompt(String paramString, GeolocationPermissions.Callback paramCallback)
  {
    paramCallback.invoke(paramString, this.mLocationSettings.canUseLocationForSearch(), false);
  }
  
  public boolean onJsAlert(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
  {
    return false;
  }
  
  public boolean onJsConfirm(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
  {
    return false;
  }
  
  public boolean onJsPrompt(WebView paramWebView, String paramString1, String paramString2, String paramString3, JsPromptResult paramJsPromptResult)
  {
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.webview.GsaWebChromeClient
 * JD-Core Version:    0.7.0.1
 */