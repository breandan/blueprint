package com.google.android.launcher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.android.launcher3.Launcher;
import com.android.launcher3.Launcher.CustomContentCallbacks;
import com.google.android.search.gel.GoogleNowPromoController;
import com.google.android.search.gel.SearchOverlay;
import com.google.android.search.gel.SearchOverlay.Listener;
import com.google.android.search.gel.SearchOverlayImpl;
import com.google.android.search.shared.api.ExternalGelSearch;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.ui.ScrollViewControl.ScrollListener;
import com.google.android.shared.util.ActivityIntentStarter;
import com.google.android.shared.util.GelStartupPrefs;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.client.NowClientCardsView.DoodleListener;
import com.google.android.sidekick.shared.client.NowOverlay;
import com.google.android.sidekick.shared.client.NowRemoteClient;
import com.google.android.velvet.tg.FirstRunActivity;
import com.google.common.base.Supplier;
import java.util.Locale;

public class GEL
  extends Launcher
{
  private GelStartupPrefs mGelStartupPrefs;
  private boolean mHasWindowFocus = false;
  private ActivityIntentStarter mIntentStarter;
  private boolean mIsInteracting = false;
  private boolean mMoveToCustomContentInitially = false;
  private boolean mNowEnabled = false;
  private NowOverlay mNowOverlay;
  private NowRemoteClient mNowRemoteClient;
  private boolean mRecentAssistGesture = false;
  private boolean mResumed = false;
  private SearchOverlay mSearchOverlay;
  private boolean mShouldAnimateAssist;
  private boolean mShouldShowNowCardsDealAnimation = false;
  private boolean mStoppedSinceLastIntent = false;
  private ScheduledSingleThreadedExecutor mUiThreadExecutor;
  
  private void ensureNowOverlayResumed()
  {
    if ((this.mNowOverlay != null) && (this.mNowOverlay.isPaused())) {
      this.mNowOverlay.onResume();
    }
  }
  
  private void handleIntent(Intent paramIntent, boolean paramBoolean)
  {
    boolean bool = true;
    Log.i("GEL", "handleIntent(" + paramIntent + ")");
    String str1 = paramIntent.getAction();
    Bundle localBundle = paramIntent.getBundleExtra("app_data");
    String str2;
    if (localBundle != null)
    {
      str2 = localBundle.getString("source");
      if (str2 == null) {
        str2 = "android-search-app";
      }
      if ((!"com.google.android.googlequicksearchbox.GOOGLE_ICON".equals(str1)) || (this.mNowEnabled) || (this.mGelStartupPrefs.getBoolean("GSAPrefs.first_run_screens_shown", false))) {
        break label135;
      }
      Intent localIntent = new Intent(getApplicationContext(), FirstRunActivity.class);
      localIntent.addFlags(268468224);
      startActivity(localIntent);
    }
    label135:
    do
    {
      return;
      str2 = null;
      break;
      if ("android.intent.action.ASSIST".equals(str1))
      {
        if (paramBoolean) {
          showWorkspace(false);
        }
        if (this.mNowEnabled)
        {
          if (this.mStoppedSinceLastIntent)
          {
            this.mShouldShowNowCardsDealAnimation = bool;
            this.mStoppedSinceLastIntent = false;
          }
          this.mRecentAssistGesture = bool;
          ensureNowOverlayResumed();
          moveToCustomContentScreen();
          this.mSearchOverlay.stopSearch(bool);
          return;
        }
        SearchOverlay localSearchOverlay2 = this.mSearchOverlay;
        if (!this.mStoppedSinceLastIntent) {}
        for (;;)
        {
          localSearchOverlay2.startTextSearch(bool, str2);
          return;
          bool = false;
        }
      }
      if (("android.search.action.GLOBAL_SEARCH".equals(str1)) || ("com.google.android.googlequicksearchbox.GOOGLE_ICON".equals(str1)) || (paramIntent.getBooleanExtra("from-first-run", false)))
      {
        if (paramBoolean) {
          showWorkspace(false);
        }
        SearchOverlay localSearchOverlay1 = this.mSearchOverlay;
        if (!this.mStoppedSinceLastIntent) {}
        for (;;)
        {
          localSearchOverlay1.startTextSearch(bool, str2);
          return;
          bool = false;
        }
      }
    } while ((!"android.speech.action.WEB_SEARCH".equals(str1)) && (!"android.intent.action.SEARCH_LONG_PRESS".equals(str1)) && (!"android.intent.action.VOICE_ASSIST".equals(str1)));
    if (paramBoolean) {
      showWorkspace(false);
    }
    this.mSearchOverlay.startVoiceSearch(str2);
  }
  
  private void initSearch()
  {
    this.mSearchOverlay.setListener(new SearchOverlay.Listener()
    {
      public void onSearchPlateShown(boolean paramAnonymousBoolean)
      {
        GEL.this.disableVoiceButtonProxy(paramAnonymousBoolean);
        if ((GEL.this.hasCustomContentToLeft()) && (GEL.this.mNowOverlay != null))
        {
          if (!paramAnonymousBoolean) {
            break label54;
          }
          GEL.this.mNowOverlay.hideViewsForSearch();
        }
        for (;;)
        {
          if (paramAnonymousBoolean) {
            GEL.this.closeFolder();
          }
          return;
          label54:
          GEL.this.mNowOverlay.showViewsForSearch();
        }
      }
    });
  }
  
  private void moveToCustomContentScreen()
  {
    boolean bool = true;
    if ((this.mNowOverlay == null) && (this.mNowEnabled))
    {
      this.mMoveToCustomContentInitially = bool;
      return;
    }
    if ((this.mShouldAnimateAssist) && (!isAllAppsVisible())) {}
    for (;;)
    {
      moveToCustomContentScreen(bool);
      return;
      bool = false;
    }
  }
  
  private void reloadNowEnabled()
  {
    this.mGelStartupPrefs.startReloadIfChanged();
    boolean bool = this.mGelStartupPrefs.getBoolean("GEL.GSAPrefs.now_enabled", false);
    if (bool != this.mNowEnabled)
    {
      this.mNowEnabled = bool;
      invalidateHasCustomContentToLeft();
      if ((!bool) && (this.mNowOverlay != null))
      {
        this.mNowOverlay.onDestroy();
        this.mNowOverlay = null;
      }
    }
  }
  
  private boolean stopSearch(boolean paramBoolean)
  {
    if (this.mSearchOverlay != null) {
      return this.mSearchOverlay.stopSearch(paramBoolean);
    }
    return false;
  }
  
  private void updateHotwordDetection()
  {
    SearchOverlay localSearchOverlay;
    if (this.mSearchOverlay != null)
    {
      localSearchOverlay = this.mSearchOverlay;
      if ((!this.mResumed) || (!this.mHasWindowFocus) || (this.mIsInteracting)) {
        break label43;
      }
    }
    label43:
    for (boolean bool = true;; bool = false)
    {
      localSearchOverlay.setHotwordDetectionEnabled(bool);
      return;
    }
  }
  
  protected void addCustomContentToLeft()
  {
    if (this.mNowOverlay != null)
    {
      Log.w("GEL", "Destroying pre-existing Now overlay");
      this.mNowOverlay.onDestroy();
      this.mNowOverlay = null;
    }
    this.mNowOverlay = NowOverlay.create(this, this.mNowRemoteClient, this.mUiThreadExecutor, this.mIntentStarter, new MyGelSearchSupplier(null));
    Launcher.CustomContentCallbacks local2 = new Launcher.CustomContentCallbacks()
    {
      public void onHide()
      {
        GEL.this.mNowOverlay.onHide();
      }
      
      public void onScrollProgressChanged(float paramAnonymousFloat)
      {
        if (GEL.this.mSearchOverlay != null) {
          GEL.this.mSearchOverlay.setProximityToNow(paramAnonymousFloat);
        }
        if (GEL.this.mNowOverlay != null) {
          GEL.this.mNowOverlay.setProximityToNow(paramAnonymousFloat);
        }
      }
      
      public void onShow()
      {
        GEL.this.mNowOverlay.onShow(GEL.this.mShouldShowNowCardsDealAnimation, GEL.this.mRecentAssistGesture);
        GEL.access$202(GEL.this, false);
        GEL.access$302(GEL.this, false);
      }
    };
    this.mNowOverlay.setAllowedSwipeDirections(false, true);
    this.mNowOverlay.onResume();
    this.mNowOverlay.setProximityToNow(0.0F);
    String str = getResources().getString(2131363132);
    addToCustomContentPage(this.mNowOverlay.getView(), local2, str);
    this.mNowOverlay.setDoodleListener(new NowClientCardsView.DoodleListener()
    {
      public void onDoodleChanged(boolean paramAnonymousBoolean)
      {
        if (GEL.this.mSearchOverlay != null) {
          GEL.this.mSearchOverlay.onDoodleChanged(paramAnonymousBoolean);
        }
      }
    });
    this.mNowOverlay.addScrollListener(new ScrollViewControl.ScrollListener()
    {
      public void onOverscroll(int paramAnonymousInt) {}
      
      public void onOverscrollFinished() {}
      
      public void onOverscrollStarted() {}
      
      public void onScrollAnimationFinished() {}
      
      public void onScrollChanged(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        GEL.this.mSearchOverlay.onNowScroll(paramAnonymousInt1);
      }
      
      public void onScrollFinished() {}
      
      public void onScrollMarginConsumed(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2) {}
    });
    if (getCurrentWorkspaceScreen() == 0)
    {
      this.mNowOverlay.onShow(true, false);
      if (this.mSearchOverlay != null) {
        this.mSearchOverlay.setProximityToNow(1.0F);
      }
      this.mShouldShowNowCardsDealAnimation = false;
    }
    if (this.mMoveToCustomContentInitially)
    {
      moveToCustomContentScreen();
      this.mMoveToCustomContentInitially = false;
    }
  }
  
  public void closeSystemDialogs()
  {
    super.closeSystemDialogs();
    stopSearch(true);
  }
  
  protected String getFirstRunClingSearchBarHint()
  {
    if (Locale.getDefault().toString().equals("en_US")) {
      return getResources().getString(2131363338);
    }
    return "";
  }
  
  protected String getFirstRunCustomContentHint()
  {
    if ((this.mGelStartupPrefs != null) && (this.mGelStartupPrefs.getBoolean("GEL.GSAPrefs.now_enabled", false))) {
      return getResources().getString(2131363337);
    }
    return "";
  }
  
  protected String getFirstRunFocusedHotseatAppBubbleDescription()
  {
    return getResources().getString(2131363340);
  }
  
  protected String getFirstRunFocusedHotseatAppBubbleTitle()
  {
    return getResources().getString(2131363339);
  }
  
  protected ComponentName getFirstRunFocusedHotseatAppComponentName()
  {
    return ComponentName.unflattenFromString(getResources().getString(2131363341));
  }
  
  protected int getFirstRunFocusedHotseatAppDrawableId()
  {
    return 2130903041;
  }
  
  protected int getFirstRunFocusedHotseatAppRank()
  {
    return getResources().getInteger(2131427468);
  }
  
  public View getQsbBar()
  {
    if (this.mSearchOverlay == null) {
      this.mSearchOverlay = new SearchOverlayImpl(getDragLayer(), this.mUiThreadExecutor, new GoogleNowPromoController(this.mNowRemoteClient, this.mGelStartupPrefs));
    }
    this.mSearchOverlay.onWindowFocusChanged(hasWindowFocus());
    return this.mSearchOverlay.getContainer();
  }
  
  protected ComponentName getWallpaperPickerComponent()
  {
    return new ComponentName(this, GelWallpaperPickerActivity.class);
  }
  
  protected boolean hasCustomContentToLeft()
  {
    return this.mNowEnabled;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (this.mIntentStarter != null) {
      this.mIntentStarter.onActivityResultDelegate(paramInt1, paramInt2, paramIntent);
    }
  }
  
  public void onBackPressed()
  {
    if (stopSearch(true)) {}
    while ((this.mNowOverlay != null) && ((!hasCustomContentToLeft()) || (this.mNowOverlay.onBackPressed()))) {
      return;
    }
    super.onBackPressed();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    GelServices localGelServices = GelServices.get(getApplicationContext());
    this.mUiThreadExecutor = localGelServices.getUiThreadExecutor();
    this.mIntentStarter = new ActivityIntentStarter(this, 100);
    this.mNowRemoteClient = localGelServices.getNowRemoteClient();
    this.mGelStartupPrefs = localGelServices.getGelStartupPrefs();
    super.onCreate(paramBundle);
    reloadNowEnabled();
    initSearch();
    handleIntent(getIntent(), false);
  }
  
  public void onDestroy()
  {
    if (this.mNowOverlay != null) {
      this.mNowOverlay.onDestroy();
    }
    if (this.mSearchOverlay != null)
    {
      this.mSearchOverlay.onDestroy();
      this.mSearchOverlay = null;
    }
    super.onDestroy();
  }
  
  protected void onInteractionBegin()
  {
    if (!this.mIsInteracting)
    {
      this.mIsInteracting = true;
      updateHotwordDetection();
    }
  }
  
  protected void onInteractionEnd()
  {
    if (this.mIsInteracting)
    {
      this.mIsInteracting = false;
      updateHotwordDetection();
    }
  }
  
  public void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    reloadNowEnabled();
    handleIntent(paramIntent, true);
  }
  
  public void onPause()
  {
    super.onPause();
    this.mResumed = false;
    this.mShouldShowNowCardsDealAnimation = false;
    if (this.mNowOverlay != null) {
      this.mNowOverlay.onPause();
    }
  }
  
  public void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    if ((paramBundle == null) || (!paramBundle.getBoolean("gel:changing_configurations", false))) {
      this.mSearchOverlay.clearSearchPlate();
    }
  }
  
  protected void onResume()
  {
    reloadNowEnabled();
    ensureNowOverlayResumed();
    super.onResume();
    this.mShouldAnimateAssist = true;
    this.mResumed = true;
    this.mHasWindowFocus = hasWindowFocus();
    this.mStoppedSinceLastIntent = false;
    if (this.mSearchOverlay != null) {
      this.mSearchOverlay.onResume();
    }
    updateHotwordDetection();
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("gel:changing_configurations", isChangingConfigurations());
  }
  
  protected void onStart()
  {
    super.onStart();
  }
  
  protected void onStop()
  {
    this.mStoppedSinceLastIntent = true;
    stopSearch(false);
    this.mShouldAnimateAssist = false;
    updateHotwordDetection();
    if ((this.mNowOverlay != null) && (getCurrentWorkspaceScreen() != 0)) {
      this.mNowOverlay.getScrollViewControl().setScrollY(0);
    }
    super.onStop();
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    this.mHasWindowFocus = paramBoolean;
    updateHotwordDetection();
    this.mSearchOverlay.onWindowFocusChanged(paramBoolean);
  }
  
  public void resetQSBScroll() {}
  
  public void startSearch(String paramString, boolean paramBoolean, Bundle paramBundle, Rect paramRect)
  {
    if (this.mSearchOverlay != null) {
      this.mSearchOverlay.startTextSearch(true, "android-launcher-search");
    }
  }
  
  protected void startSettings()
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.velvet.ui.settings.SettingsActivity");
    startActivity(localIntent);
  }
  
  public void startVoice()
  {
    if (this.mSearchOverlay != null) {
      this.mSearchOverlay.startVoiceSearch("android-search-app");
    }
  }
  
  protected void updateGlobalSearchIcon(Drawable.ConstantState paramConstantState) {}
  
  protected boolean updateGlobalSearchIcon()
  {
    return true;
  }
  
  public void updateVoiceButtonProxyVisible(boolean paramBoolean)
  {
    super.updateVoiceButtonProxyVisible(true);
  }
  
  protected void updateVoiceSearchIcon(Drawable.ConstantState paramConstantState) {}
  
  protected boolean updateVoiceSearchIcon(boolean paramBoolean)
  {
    return true;
  }
  
  private class MyGelSearchSupplier
    implements Supplier<ExternalGelSearch>
  {
    private MyGelSearchSupplier() {}
    
    public ExternalGelSearch get()
    {
      if (GEL.this.mSearchOverlay == null) {
        return null;
      }
      return GEL.this.mSearchOverlay.getGelSearch();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.launcher.GEL
 * JD-Core Version:    0.7.0.1
 */