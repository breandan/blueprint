package com.google.android.search.core.webview;

import android.util.JsonReader;
import android.util.JsonToken;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.google.android.search.core.GsaJsEventController;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.velvet.util.JavascriptInterfaceHelper;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;
import java.util.Map;

public class GsaCommunicationJsHelper
  extends JavascriptInterfaceHelper
{
  private final SearchConfig mConfig;
  private final GsaJsEventController mJsEventController;
  private Map<String, String> mResults = Maps.newHashMap();
  private final SearchSettings mSettings;
  
  public GsaCommunicationJsHelper(WebView paramWebView, SearchConfig paramSearchConfig, SearchSettings paramSearchSettings, GsaJsEventController paramGsaJsEventController)
  {
    super(paramWebView, paramSearchConfig.getVelvetGsaBridgeInterfaceName(), false);
    this.mConfig = paramSearchConfig;
    this.mSettings = paramSearchSettings;
    this.mJsEventController = paramGsaJsEventController;
  }
  
  private Map<String, String> read(String paramString)
  {
    this.mResults.clear();
    JsonReader localJsonReader = new JsonReader(new StringReader(paramString));
    try
    {
      localJsonReader.beginObject();
      label29:
      boolean bool = localJsonReader.hasNext();
      if (bool) {
        str1 = null;
      }
    }
    catch (IOException localIOException1)
    {
      String str1;
      JsonToken localJsonToken1;
      JsonToken localJsonToken2;
      Object localObject2;
      String str3;
      String str2;
      break label29;
    }
    finally
    {
      Closeables.closeQuietly(localJsonReader);
    }
  }
  
  protected void addJavaScriptInterface(WebView paramWebView, String paramString)
  {
    paramWebView.addJavascriptInterface(new JsBridge(null), paramString);
  }
  
  public void registerJsBridge()
  {
    loadJsString(this.mConfig.getRegisterGsaBridgeJavascript());
    if (this.mSettings.isDebugWeinreEnabled())
    {
      String str1 = this.mConfig.getWeinreJavascriptPattern();
      String str2 = this.mSettings.getDebugWeinreServerAddress();
      loadJsString(String.format(Locale.US, str1, new Object[] { str2 }));
    }
  }
  
  private class JsBridge
  {
    private JsBridge() {}
    
    @JavascriptInterface
    public void onJsEvents(String paramString1, String paramString2)
    {
      GsaCommunicationJsHelper.this.mJsEventController.dispatchEvents(GsaCommunicationJsHelper.this.read(paramString1));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.webview.GsaCommunicationJsHelper
 * JD-Core Version:    0.7.0.1
 */