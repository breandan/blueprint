package com.android.ex.photo.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.android.ex.photo.Intents;
import com.android.ex.photo.Intents.PhotoViewIntentBuilder;
import com.android.ex.photo.fragments.PhotoViewFragment;

public class PhotoPagerAdapter
  extends BaseCursorPagerAdapter
{
  protected int mContentUriIndex;
  protected boolean mDisplayThumbsFullScreen;
  protected int mLoadingIndex;
  protected final float mMaxScale;
  protected int mThumbnailUriIndex;
  
  public PhotoPagerAdapter(Context paramContext, FragmentManager paramFragmentManager, Cursor paramCursor, float paramFloat, boolean paramBoolean)
  {
    super(paramContext, paramFragmentManager, paramCursor);
    this.mMaxScale = paramFloat;
    this.mDisplayThumbsFullScreen = paramBoolean;
  }
  
  public Fragment getItem(Context paramContext, Cursor paramCursor, int paramInt)
  {
    String str1 = paramCursor.getString(this.mContentUriIndex);
    String str2 = paramCursor.getString(this.mThumbnailUriIndex);
    if (this.mLoadingIndex != -1) {}
    for (boolean bool1 = Boolean.valueOf(paramCursor.getString(this.mLoadingIndex)).booleanValue();; bool1 = false)
    {
      boolean bool2 = false;
      if (str1 == null)
      {
        bool2 = false;
        if (bool1) {
          bool2 = true;
        }
      }
      Intents.PhotoViewIntentBuilder localPhotoViewIntentBuilder = Intents.newPhotoViewFragmentIntentBuilder(this.mContext);
      localPhotoViewIntentBuilder.setResolvedPhotoUri(str1).setThumbnailUri(str2).setDisplayThumbsFullScreen(this.mDisplayThumbsFullScreen).setMaxInitialScale(this.mMaxScale);
      return PhotoViewFragment.newInstance(localPhotoViewIntentBuilder.build(), paramInt, bool2);
    }
  }
  
  public String getPhotoUri(Cursor paramCursor)
  {
    return paramCursor.getString(this.mContentUriIndex);
  }
  
  public String getThumbnailUri(Cursor paramCursor)
  {
    return paramCursor.getString(this.mThumbnailUriIndex);
  }
  
  public Cursor swapCursor(Cursor paramCursor)
  {
    if (paramCursor != null)
    {
      this.mContentUriIndex = paramCursor.getColumnIndex("contentUri");
      this.mThumbnailUriIndex = paramCursor.getColumnIndex("thumbnailUri");
    }
    for (this.mLoadingIndex = paramCursor.getColumnIndex("loadingIndicator");; this.mLoadingIndex = -1)
    {
      return super.swapCursor(paramCursor);
      this.mContentUriIndex = -1;
      this.mThumbnailUriIndex = -1;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.adapters.PhotoPagerAdapter
 * JD-Core Version:    0.7.0.1
 */