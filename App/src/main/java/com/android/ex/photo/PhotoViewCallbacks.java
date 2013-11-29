package com.android.ex.photo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import com.android.ex.photo.adapters.PhotoPagerAdapter;
import com.android.ex.photo.fragments.PhotoViewFragment;
import com.android.ex.photo.loaders.PhotoBitmapLoaderInterface.BitmapResult;

public abstract interface PhotoViewCallbacks
{
  public abstract void addCursorListener(CursorChangedListener paramCursorChangedListener);
  
  public abstract void addScreenListener(int paramInt, OnScreenListener paramOnScreenListener);
  
  public abstract PhotoPagerAdapter getAdapter();
  
  public abstract boolean isFragmentActive(Fragment paramFragment);
  
  public abstract boolean isFragmentFullScreen(Fragment paramFragment);
  
  public abstract Loader<PhotoBitmapLoaderInterface.BitmapResult> onCreateBitmapLoader(int paramInt, Bundle paramBundle, String paramString);
  
  public abstract void onCursorChanged(PhotoViewFragment paramPhotoViewFragment, Cursor paramCursor);
  
  public abstract void onFragmentPhotoLoadComplete(PhotoViewFragment paramPhotoViewFragment, boolean paramBoolean);
  
  public abstract void onFragmentVisible(PhotoViewFragment paramPhotoViewFragment);
  
  public abstract void onNewPhotoLoaded(int paramInt);
  
  public abstract void removeCursorListener(CursorChangedListener paramCursorChangedListener);
  
  public abstract void removeScreenListener(int paramInt);
  
  public abstract void toggleFullScreen();
  
  public static abstract interface CursorChangedListener
  {
    public abstract void onCursorChanged(Cursor paramCursor);
  }
  
  public static abstract interface OnScreenListener
  {
    public abstract void onFullScreenChanged(boolean paramBoolean);
    
    public abstract boolean onInterceptMoveLeft(float paramFloat1, float paramFloat2);
    
    public abstract boolean onInterceptMoveRight(float paramFloat1, float paramFloat2);
    
    public abstract void onViewActivated();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.PhotoViewCallbacks
 * JD-Core Version:    0.7.0.1
 */