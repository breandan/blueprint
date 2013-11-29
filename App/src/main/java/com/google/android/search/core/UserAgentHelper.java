package com.google.android.search.core;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.google.android.velvet.VelvetApplication;
import com.google.common.base.Supplier;
import java.util.Locale;

public class UserAgentHelper
  implements Supplier<String>
{
  private final SearchConfig mConfig;
  private final Context mContext;
  private final SearchSettings mSettings;
  private String mUserAgent;
  private boolean mUserAgentAssumed;
  
  public UserAgentHelper(Context paramContext, SearchConfig paramSearchConfig, SearchSettings paramSearchSettings)
  {
    this.mContext = paramContext;
    this.mConfig = paramSearchConfig;
    this.mSettings = paramSearchSettings;
  }
  
  private String buildUserAgent(String paramString)
  {
    Locale localLocale = Locale.US;
    String str = this.mConfig.getUserAgentPattern();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramString;
    arrayOfObject[1] = VelvetApplication.getVersionName();
    return String.format(localLocale, str, arrayOfObject);
  }
  
  public String get()
  {
    return getUserAgent();
  }
  
  public String getUserAgent()
  {
    try
    {
      if (this.mUserAgent == null)
      {
        String str2 = this.mSettings.getCachedUserAgentBase();
        if (str2 == null) {
          str2 = WebViewUtils.getCurrentUserAgent(this.mContext);
        }
        this.mUserAgentAssumed = true;
        this.mUserAgent = buildUserAgent(str2);
      }
      String str1 = this.mUserAgent;
      return str1;
    }
    finally {}
  }
  
  public void onWebViewCreated(WebView paramWebView)
  {
    try
    {
      if ((this.mUserAgent == null) || (this.mUserAgentAssumed))
      {
        String str = paramWebView.getSettings().getUserAgentString();
        this.mSettings.setCachedUserAgentBase(str);
        this.mUserAgent = buildUserAgent(str);
        this.mUserAgentAssumed = false;
      }
      return;
    }
    finally {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.UserAgentHelper
 * JD-Core Version:    0.7.0.1
 */