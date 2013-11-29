package com.android.ex.photo;

import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;

public class PhotoViewController
{
  private final PhotoViewControllerCallbacks mCallback;
  private int mLastFlags;
  private final View.OnSystemUiVisibilityChangeListener mSystemUiVisibilityChangeListener;
  
  public PhotoViewController(PhotoViewControllerCallbacks paramPhotoViewControllerCallbacks)
  {
    this.mCallback = paramPhotoViewControllerCallbacks;
    if (Build.VERSION.SDK_INT < 11)
    {
      this.mSystemUiVisibilityChangeListener = null;
      return;
    }
    this.mSystemUiVisibilityChangeListener = new View.OnSystemUiVisibilityChangeListener()
    {
      public void onSystemUiVisibilityChange(int paramAnonymousInt)
      {
        if ((Build.VERSION.SDK_INT >= 19) && (paramAnonymousInt == 0) && (PhotoViewController.this.mLastFlags == 3846)) {
          PhotoViewController.this.mCallback.setNotFullscreenCallbackDoNotUseThisFunction();
        }
      }
    };
  }
  
  public View.OnSystemUiVisibilityChangeListener getSystemUiVisibilityChangeListener()
  {
    return this.mSystemUiVisibilityChangeListener;
  }
  
  public void setImmersiveMode(boolean paramBoolean)
  {
    int i = Build.VERSION.SDK_INT;
    int j;
    int k;
    if (i < 16)
    {
      j = 1;
      if ((!paramBoolean) || ((this.mCallback.isScaleAnimationEnabled()) && (!this.mCallback.isEnterAnimationFinished()))) {
        break label137;
      }
      if (i < 19) {
        break label96;
      }
      k = 3846;
      label51:
      if (j != 0) {
        this.mCallback.hideActionBar();
      }
    }
    label137:
    label203:
    for (;;)
    {
      if (i >= 11)
      {
        this.mLastFlags = k;
        this.mCallback.getRootView().setSystemUiVisibility(k);
      }
      return;
      j = 0;
      break;
      label96:
      if (i >= 16)
      {
        k = 1285;
        break label51;
      }
      if (i >= 14)
      {
        k = 1;
        break label51;
      }
      k = 0;
      if (i < 11) {
        break label51;
      }
      k = 1;
      break label51;
      if (i >= 19) {
        k = 1792;
      }
      for (;;)
      {
        if (j == 0) {
          break label203;
        }
        this.mCallback.showActionBar();
        break;
        if (i >= 16)
        {
          k = 1280;
        }
        else if (i >= 14)
        {
          k = 0;
        }
        else
        {
          k = 0;
          if (i >= 11) {
            k = 0;
          }
        }
      }
    }
  }
  
  public static abstract interface PhotoViewControllerCallbacks
  {
    public abstract View getRootView();
    
    public abstract void hideActionBar();
    
    public abstract boolean isEnterAnimationFinished();
    
    public abstract boolean isScaleAnimationEnabled();
    
    public abstract void setNotFullscreenCallbackDoNotUseThisFunction();
    
    public abstract void showActionBar();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.PhotoViewController
 * JD-Core Version:    0.7.0.1
 */