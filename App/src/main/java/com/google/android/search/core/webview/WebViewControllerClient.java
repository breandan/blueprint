package com.google.android.search.core.webview;

import android.net.Uri;
import com.google.android.search.shared.api.Query;
import javax.annotation.Nullable;

public abstract interface WebViewControllerClient
{
  public abstract void onEndResultsPage(Query paramQuery);
  
  public abstract void onLinkClicked(Uri paramUri1, @Nullable Uri paramUri2);
  
  public abstract void onLogoutRedirect();
  
  public abstract void onNewQuery(Query paramQuery);
  
  public abstract void onPageError(Query paramQuery, int paramInt, String paramString);
  
  public abstract void onShowedPrefetchedSrp(Query paramQuery, String paramString);
  
  public abstract void onStartResultsPage(Query paramQuery);
  
  public abstract void onStateChanged(boolean paramBoolean);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.webview.WebViewControllerClient
 * JD-Core Version:    0.7.0.1
 */