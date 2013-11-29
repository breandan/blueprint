package com.google.android.velvet.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.SearchController;
import com.google.android.search.core.SearchControllerCache;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.util.IntentUtils;
import com.google.common.base.Preconditions;

public class RetainedFragment
  extends Fragment
{
  private boolean mInitialized;
  private SearchController mSearchController;
  private SearchControllerCache mSearchControllerCache;
  private int mSearchControllerToken;
  private UserInteractionLogger mUserInteractionLogger;
  
  public RetainedFragment()
  {
    setRetainInstance(true);
  }
  
  public SearchController getSearchController()
  {
    return this.mSearchController;
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    if (!this.mInitialized)
    {
      VelvetServices localVelvetServices = VelvetServices.get();
      this.mSearchControllerCache = localVelvetServices.getCoreServices().getSearchControllerCache();
      this.mUserInteractionLogger = localVelvetServices.getCoreServices().getUserInteractionLogger();
      this.mSearchControllerToken = IntentUtils.getSearchControllerToken(paramActivity.getIntent());
      if (this.mSearchControllerToken != -1) {
        this.mSearchController = ((SearchController)this.mSearchControllerCache.get(this.mSearchControllerToken));
      }
      if (this.mSearchController == null)
      {
        this.mSearchControllerToken = this.mSearchControllerCache.acquireToken();
        this.mSearchController = ((SearchController)this.mSearchControllerCache.get(this.mSearchControllerToken));
      }
      this.mInitialized = true;
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mUserInteractionLogger.onSessionStart();
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    Preconditions.checkState(this.mInitialized);
    this.mUserInteractionLogger.onSessionStop();
    this.mSearchControllerCache.releaseToken(this.mSearchControllerToken);
    Log.i("RetainedFragment", "onDestroy");
  }
  
  public void onTrimMemory(int paramInt)
  {
    super.onTrimMemory(paramInt);
    this.mSearchController.onTrimMemory();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.RetainedFragment
 * JD-Core Version:    0.7.0.1
 */