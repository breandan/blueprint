package com.google.android.velvet.presenter;

import android.os.Bundle;
import android.view.View;
import com.google.android.search.core.SearchController;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.velvet.VelvetFactory;

public abstract class VelvetFragmentPresenter
{
  private VelvetPresenter mPresenter;
  private final String mTag;
  
  protected VelvetFragmentPresenter(String paramString)
  {
    this.mTag = paramString;
  }
  
  public void doUserRefresh(boolean paramBoolean) {}
  
  public int getDimensionPixelSize(int paramInt)
  {
    return this.mPresenter.getDimensionPixelSize(paramInt);
  }
  
  public VelvetEventBus getEventBus()
  {
    return this.mPresenter.getSearchController().getEventBus();
  }
  
  public VelvetFactory getFactory()
  {
    return this.mPresenter.getFactory();
  }
  
  public View getReminderPeekView()
  {
    return this.mPresenter.getReminderPeekView();
  }
  
  public View getRemindersFooterIcon()
  {
    return this.mPresenter.getRemindersFooterIcon();
  }
  
  public ScrollViewControl getScrollViewControl()
  {
    return this.mPresenter.getScrollViewControl();
  }
  
  public final String getTag()
  {
    return this.mTag;
  }
  
  public View getTrainingFooterIcon()
  {
    return this.mPresenter.getTrainingFooterIcon();
  }
  
  public View getTrainingPeekIcon()
  {
    return this.mPresenter.getTrainingPeekIcon();
  }
  
  public View getTrainingPeekView()
  {
    return this.mPresenter.getTrainingPeekView();
  }
  
  public VelvetPresenter getVelvetPresenter()
  {
    return this.mPresenter;
  }
  
  public boolean isAttached()
  {
    return this.mPresenter != null;
  }
  
  public final void onAttach(VelvetPresenter paramVelvetPresenter, Bundle paramBundle)
  {
    this.mPresenter = paramVelvetPresenter;
    onPostAttach(paramBundle);
  }
  
  public boolean onBackPressed()
  {
    return false;
  }
  
  public final void onDetach()
  {
    onPreDetach();
    this.mPresenter = null;
  }
  
  public void onPause() {}
  
  protected abstract void onPostAttach(Bundle paramBundle);
  
  protected abstract void onPreDetach();
  
  public void onResume() {}
  
  public void onStart() {}
  
  public void onStop() {}
  
  public void saveInstanceState(Bundle paramBundle, boolean paramBoolean) {}
  
  public void update(UiModeManager paramUiModeManager, UiMode paramUiMode) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.VelvetFragmentPresenter
 * JD-Core Version:    0.7.0.1
 */