package com.google.android.velvet.ui;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewWrapper
{
  private final WebView mDelegate;
  
  public WebViewWrapper(WebView paramWebView)
  {
    this.mDelegate = paramWebView;
  }
  
  public void addJavascriptInterface(Object paramObject, String paramString)
  {
    this.mDelegate.addJavascriptInterface(paramObject, paramString);
  }
  
  public boolean canGoBack()
  {
    return this.mDelegate.canGoBack();
  }
  
  public void destroy()
  {
    this.mDelegate.destroy();
  }
  
  public WebView getWebView()
  {
    return this.mDelegate;
  }
  
  public void goBack()
  {
    this.mDelegate.goBack();
  }
  
  public void loadUrl(String paramString)
  {
    this.mDelegate.loadUrl(paramString);
  }
  
  public void setWebChromeClient(WebChromeClient paramWebChromeClient)
  {
    this.mDelegate.setWebChromeClient(paramWebChromeClient);
  }
  
  public void setWebViewClient(WebViewClient paramWebViewClient)
  {
    this.mDelegate.setWebViewClient(paramWebViewClient);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.WebViewWrapper
 * JD-Core Version:    0.7.0.1
 */