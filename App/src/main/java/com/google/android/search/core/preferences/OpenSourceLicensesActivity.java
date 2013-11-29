package com.google.android.search.core.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class OpenSourceLicensesActivity
  extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968580);
    getWindow().setLayout(-1, -1);
    ((WebView)findViewById(2131296294)).loadUrl("file:///android_asset/html/licenses.html");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.OpenSourceLicensesActivity
 * JD-Core Version:    0.7.0.1
 */