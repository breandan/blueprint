package com.google.android.sidekick.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.google.android.search.core.debug.DebugFeatures;
import java.net.URISyntaxException;

public class RemindersListWebView
  extends Activity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!DebugFeatures.getInstance().teamDebugEnabled())
    {
      Toast.makeText(this, "Not enabled for a non-debug build", 0).show();
      finish();
      return;
    }
    WebView localWebView = new WebView(this);
    localWebView.getSettings().setBuiltInZoomControls(true);
    localWebView.getSettings().setSupportZoom(true);
    localWebView.setWebViewClient(new WebViewClient()
    {
      public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2)
      {
        Toast.makeText(jdField_this, "Oh no! " + paramAnonymousString1, 0).show();
      }
      
      public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
      {
        Uri localUri = Uri.parse(paramAnonymousString);
        Intent localIntent = null;
        if ("intent".equals(localUri.getScheme())) {}
        for (;;)
        {
          try
          {
            localIntent = Intent.parseUri(paramAnonymousString, 1);
            localIntent.setComponent(null);
            localIntent.addCategory("android.intent.category.BROWSABLE");
            if (localIntent == null) {
              break;
            }
            RemindersListWebView.this.startActivity(localIntent);
            return true;
          }
          catch (URISyntaxException localURISyntaxException)
          {
            localURISyntaxException.printStackTrace();
            continue;
          }
          localIntent = new Intent("android.intent.action.VIEW", localUri);
        }
        return false;
      }
    });
    setContentView(localWebView);
    localWebView.loadData("<html><body><ul><li><a href=\"intent:#Intent;action=com.google.android.googlequicksearchbox.MY_REMINDERS;package=com.google.android.googlequicksearchbox;end\">Intent with action</a></li></ul></body></html>", "text/html", null);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.RemindersListWebView
 * JD-Core Version:    0.7.0.1
 */