package com.android.ex.photo;

import android.content.Context;
import android.content.Intent;
import com.android.ex.photo.fragments.PhotoViewFragment;

public class Intents
{
  public static PhotoViewIntentBuilder newPhotoViewFragmentIntentBuilder(Context paramContext)
  {
    return new PhotoViewIntentBuilder(paramContext, PhotoViewFragment.class, null);
  }
  
  public static class PhotoViewIntentBuilder
  {
    private boolean mActionBarHiddenInitially;
    private boolean mDisplayFullScreenThumbs;
    private String mInitialPhotoUri;
    private final Intent mIntent;
    private Float mMaxInitialScale;
    private Integer mPhotoIndex;
    private String mPhotosUri;
    private String[] mProjection;
    private String mResolvedPhotoUri;
    private boolean mScaleAnimation;
    private int mStartHeight;
    private int mStartWidth;
    private int mStartX;
    private int mStartY;
    private String mThumbnailUri;
    private boolean mWatchNetwork;
    
    private PhotoViewIntentBuilder(Context paramContext, Class<?> paramClass)
    {
      this.mIntent = new Intent(paramContext, paramClass);
      initialize();
    }
    
    private void initialize()
    {
      this.mScaleAnimation = false;
      this.mActionBarHiddenInitially = false;
      this.mDisplayFullScreenThumbs = false;
    }
    
    public Intent build()
    {
      this.mIntent.setAction("android.intent.action.VIEW");
      this.mIntent.setFlags(524288);
      if (this.mPhotoIndex != null) {
        this.mIntent.putExtra("photo_index", this.mPhotoIndex.intValue());
      }
      if (this.mInitialPhotoUri != null) {
        this.mIntent.putExtra("initial_photo_uri", this.mInitialPhotoUri);
      }
      if ((this.mInitialPhotoUri != null) && (this.mPhotoIndex != null)) {
        throw new IllegalStateException("specified both photo index and photo uri");
      }
      if (this.mPhotosUri != null) {
        this.mIntent.putExtra("photos_uri", this.mPhotosUri);
      }
      if (this.mResolvedPhotoUri != null) {
        this.mIntent.putExtra("resolved_photo_uri", this.mResolvedPhotoUri);
      }
      if (this.mProjection != null) {
        this.mIntent.putExtra("projection", this.mProjection);
      }
      if (this.mThumbnailUri != null) {
        this.mIntent.putExtra("thumbnail_uri", this.mThumbnailUri);
      }
      if (this.mMaxInitialScale != null) {
        this.mIntent.putExtra("max_scale", this.mMaxInitialScale);
      }
      if (this.mWatchNetwork == true) {
        this.mIntent.putExtra("watch_network", true);
      }
      this.mIntent.putExtra("scale_up_animation", this.mScaleAnimation);
      if (this.mScaleAnimation)
      {
        this.mIntent.putExtra("start_x_extra", this.mStartX);
        this.mIntent.putExtra("start_y_extra", this.mStartY);
        this.mIntent.putExtra("start_width_extra", this.mStartWidth);
        this.mIntent.putExtra("start_height_extra", this.mStartHeight);
      }
      this.mIntent.putExtra("action_bar_hidden_initially", this.mActionBarHiddenInitially);
      this.mIntent.putExtra("display_thumbs_fullscreen", this.mDisplayFullScreenThumbs);
      return this.mIntent;
    }
    
    public PhotoViewIntentBuilder setDisplayThumbsFullScreen(boolean paramBoolean)
    {
      this.mDisplayFullScreenThumbs = paramBoolean;
      return this;
    }
    
    public PhotoViewIntentBuilder setMaxInitialScale(float paramFloat)
    {
      this.mMaxInitialScale = Float.valueOf(paramFloat);
      return this;
    }
    
    public PhotoViewIntentBuilder setResolvedPhotoUri(String paramString)
    {
      this.mResolvedPhotoUri = paramString;
      return this;
    }
    
    public PhotoViewIntentBuilder setThumbnailUri(String paramString)
    {
      this.mThumbnailUri = paramString;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.Intents
 * JD-Core Version:    0.7.0.1
 */