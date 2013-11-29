package com.google.android.search.core;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.util.Log;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.webview.GsaWebViewController;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SimpleIntentStarter;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.gallery.ImageMetadataController;
import com.google.android.velvet.gallery.VelvetPhotoViewActivity;
import java.util.Map;

public class GsaJsEventController
  extends JsEventController
{
  private final CoreSearchServices mCoreSearchServices;
  private final SimpleIntentStarter mIntentStarter;
  private final String mPackageName;
  private final Resources mResources;
  private final SearchController mSearchController;
  private final SearchControllerCache mSearchControllerCache;
  private final EntryProvider mSidekickEntryProvider;
  private final GsaWebViewController mWebViewController;
  
  public GsaJsEventController(CoreSearchServices paramCoreSearchServices, SearchController paramSearchController, GsaWebViewController paramGsaWebViewController, Resources paramResources, EntryProvider paramEntryProvider, SimpleIntentStarter paramSimpleIntentStarter, String paramString, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, SearchControllerCache paramSearchControllerCache)
  {
    super(paramScheduledSingleThreadedExecutor);
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mSearchController = paramSearchController;
    this.mSearchControllerCache = paramSearchControllerCache;
    this.mWebViewController = paramGsaWebViewController;
    this.mResources = paramResources;
    this.mSidekickEntryProvider = paramEntryProvider;
    this.mIntentStarter = paramSimpleIntentStarter;
    this.mPackageName = paramString;
  }
  
  private void handleChromePrerender(String paramString)
  {
    this.mSearchController.getChromePrerenderer().prerenderJsonUrls(paramString);
  }
  
  private void handleEventId(String paramString)
  {
    this.mWebViewController.handleAgsaEvents(paramString);
  }
  
  private void handleForceRestart()
  {
    Log.e("GsaJsEventController", "Received 'force_restart' in SERP. Downloading a new config and restarting the app.");
    this.mCoreSearchServices.getBackgroundTasks().forceRun("send_gsa_home_request_then_crash", 0L);
  }
  
  private void handleImageMetadata(String paramString)
  {
    Log.v("GsaJsEventController", "Received image metadata event from WebView");
    if (!this.mSearchController.isWebViewActive())
    {
      Log.v("GsaJsEventController", "Rejected image metadata becase WebView not active or niv disabled");
      return;
    }
    this.mCoreSearchServices.getImageMetadataController().setJson(paramString, this.mSearchController.getEventBus().getQueryState().getCommittedQuery());
  }
  
  private void handleImageSelected(String paramString)
  {
    Log.v("GsaJsEventController", "Received image selected event from WebView");
    if (!this.mSearchController.isWebViewActive())
    {
      Log.v("GsaJsEventController", "Rejected image selected event becase WebView not active or niv disabled");
      return;
    }
    this.mCoreSearchServices.getImageMetadataController().setQuery(this.mSearchController.getEventBus().getQueryState().getCommittedQuery(), paramString);
    int i = this.mSearchControllerCache.acquireToken();
    SimpleIntentStarter localSimpleIntentStarter = this.mIntentStarter;
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = VelvetPhotoViewActivity.createPhotoViewIntent(this.mPackageName, paramString, i);
    localSimpleIntentStarter.startActivity(arrayOfIntent);
  }
  
  private void handleTempUnits(String paramString)
  {
    boolean bool = Boolean.parseBoolean(paramString);
    String str = this.mResources.getString(2131362044);
    SharedPreferences.Editor localEditor = this.mCoreSearchServices.getPredictiveCardsPreferences().getWorkingPreferences().edit();
    if (bool) {}
    for (int i = 0;; i = 1)
    {
      localEditor.putInt(str, i).apply();
      this.mSidekickEntryProvider.invalidate();
      return;
    }
  }
  
  protected void handleEvents(Map<String, String> paramMap)
  {
    if (paramMap.containsKey("agsase")) {
      handleEventId((String)paramMap.get("agsase"));
    }
    if (paramMap.containsKey("gsais")) {
      handleImageSelected((String)paramMap.get("gsais"));
    }
    if (paramMap.containsKey("gsaim")) {
      handleImageMetadata((String)paramMap.get("gsaim"));
    }
    if (paramMap.containsKey("wobtm")) {
      handleTempUnits((String)paramMap.get("wobtm"));
    }
    if ((paramMap.containsKey("agsafr")) && (((String)paramMap.get("agsafr")).equals("1"))) {
      handleForceRestart();
    }
    if (paramMap.containsKey("pre")) {
      handleChromePrerender((String)paramMap.get("pre"));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GsaJsEventController
 * JD-Core Version:    0.7.0.1
 */