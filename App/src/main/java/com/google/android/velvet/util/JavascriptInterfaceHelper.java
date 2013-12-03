package com.google.android.velvet.util;

import android.util.Log;
import android.webkit.WebView;

public abstract class JavascriptInterfaceHelper {
    private final boolean mDebug;
    private final String mInterfaceName;
    private final StringBuilder mJsCallBuilder = new StringBuilder(100);
    private WebView mWebView;

    public JavascriptInterfaceHelper(WebView paramWebView, String paramString, boolean paramBoolean) {
        this.mWebView = paramWebView;
        this.mInterfaceName = paramString;
        addJavaScriptInterface(this.mWebView, paramString);
        this.mDebug = paramBoolean;
    }

    protected abstract void addJavaScriptInterface(WebView paramWebView, String paramString);

    protected void loadJsString(String paramString) {
        if (this.mDebug) {
            Log.d("Velvet.JavascriptInterfaceHelper", "LOADING: " + paramString);
        }
        if (this.mWebView != null) {
            this.mWebView.loadUrl(paramString);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.util.JavascriptInterfaceHelper

 * JD-Core Version:    0.7.0.1

 */