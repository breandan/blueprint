package com.google.android.voicesearch.handsfree;

import android.content.res.Resources;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.common.base.Preconditions;

class ErrorController
{
  private final LocalTtsManager mLocalTtsManager;
  private MainController mMainController;
  private final Resources mResources;
  private Ui mUi;
  private final ViewDisplayer mViewDisplayer;
  
  public ErrorController(Resources paramResources, LocalTtsManager paramLocalTtsManager, ViewDisplayer paramViewDisplayer)
  {
    this.mResources = ((Resources)Preconditions.checkNotNull(paramResources));
    this.mLocalTtsManager = ((LocalTtsManager)Preconditions.checkNotNull(paramLocalTtsManager));
    this.mViewDisplayer = ((ViewDisplayer)Preconditions.checkNotNull(paramViewDisplayer));
  }
  
  public void setMainController(MainController paramMainController)
  {
    this.mMainController = paramMainController;
  }
  
  public void start(int paramInt1, int paramInt2)
  {
    
    if (this.mMainController != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mUi = this.mViewDisplayer.showNoMatch();
      this.mUi.setMessage(paramInt1);
      this.mLocalTtsManager.enqueue(this.mResources.getString(paramInt2), new Runnable()
      {
        public void run()
        {
          ErrorController.this.mMainController.scheduleExit();
        }
      });
      return;
    }
  }
  
  public static abstract interface Ui
  {
    public abstract void setMessage(int paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.ErrorController
 * JD-Core Version:    0.7.0.1
 */