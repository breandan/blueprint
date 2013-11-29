package com.google.android.search.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import com.google.android.googlequicksearchbox.R.styleable;
import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.UriLoader;
import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;

public class WebImageView
  extends ImageView
{
  private static final boolean DBG = false;
  private static final String TAG = "WebImageView";
  private double mAspectRatio;
  private Listener mDownloadListener;
  private boolean mFromCache;
  private int mImageScroll;
  private Uri mImageUri;
  private CancellableNowOrLater<? extends Drawable> mLoader;
  private Consumer<Drawable> mLoaderCallback;
  private boolean mUsingDefaultPlaceholderColor;
  
  public WebImageView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public WebImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public WebImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.WebImageView, paramInt, 0);
    this.mAspectRatio = localTypedArray.getFloat(0, 0.0F);
    this.mImageScroll = localTypedArray.getInt(1, 0);
    if (getBackground() == null)
    {
      setBackgroundColor(paramContext.getResources().getColor(2131230811));
      this.mUsingDefaultPlaceholderColor = true;
    }
    localTypedArray.recycle();
  }
  
  public Uri getImageUri()
  {
    return this.mImageUri;
  }
  
  public String getImageUriString()
  {
    if (this.mImageUri == null) {
      return null;
    }
    return this.mImageUri.toString();
  }
  
  public boolean isLoadedFromCache()
  {
    return this.mFromCache;
  }
  
  protected void onDetachedFromWindow()
  {
    if (this.mLoader != null)
    {
      this.mLoader.cancelGetLater(this.mLoaderCallback);
      this.mLoader = null;
    }
    super.onDetachedFromWindow();
  }
  
  protected void onImageDownloaded(Drawable paramDrawable)
  {
    ExtraPreconditions.checkMainThread();
    setImageDrawable(paramDrawable);
    resetBackground();
    this.mLoader = null;
    if (this.mDownloadListener != null) {
      this.mDownloadListener.onImageDownloaded(paramDrawable);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (AspectRatioHelper.shouldApplyAspectRatio(this.mAspectRatio, paramInt1))
    {
      AspectRatioHelper.setAspectRatio(this, this.mAspectRatio, this.mImageScroll, paramInt1, paramInt2, getSuggestedMinimumWidth(), getMaxHeight());
      return;
    }
    super.onMeasure(paramInt1, paramInt2);
  }
  
  protected void resetBackground()
  {
    if (this.mUsingDefaultPlaceholderColor) {
      setBackgroundColor(0);
    }
  }
  
  protected void setDimensions(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public void setImageUri(Uri paramUri, UriLoader<Drawable> paramUriLoader)
  {
    this.mImageUri = paramUri;
    if (this.mLoader != null)
    {
      this.mLoader.cancelGetLater(this.mLoaderCallback);
      this.mLoader = null;
    }
    Drawable localDrawable = null;
    CancellableNowOrLater localCancellableNowOrLater;
    if (paramUri != null)
    {
      localCancellableNowOrLater = paramUriLoader.load(paramUri);
      if (!localCancellableNowOrLater.haveNow()) {
        break label102;
      }
      this.mFromCache = true;
      localDrawable = (Drawable)localCancellableNowOrLater.getNow();
      if ((this.mDownloadListener != null) && (localDrawable != null)) {
        this.mDownloadListener.onImageDownloaded(localDrawable);
      }
      resetBackground();
    }
    for (;;)
    {
      setImageDrawable(localDrawable);
      return;
      label102:
      if (this.mLoaderCallback == null) {
        this.mLoaderCallback = new DrawableConsumer(this);
      }
      localCancellableNowOrLater.getLater(this.mLoaderCallback);
      this.mLoader = localCancellableNowOrLater;
      localDrawable = null;
    }
  }
  
  public void setImageUrl(String paramString, UriLoader<Drawable> paramUriLoader)
  {
    setImageUri(Uri.parse(paramString), paramUriLoader);
  }
  
  public void setOnDownloadListener(Listener paramListener)
  {
    if (this.mDownloadListener == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mDownloadListener = paramListener;
      return;
    }
  }
  
  protected static class AspectRatioHelper
  {
    protected static void setAspectRatio(WebImageView paramWebImageView, double paramDouble, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      if (View.MeasureSpec.getMode(paramInt2) != 0)
      {
        int i = View.MeasureSpec.getSize(paramInt2);
        int j = (int)(paramDouble * i);
        if (View.MeasureSpec.getMode(paramInt3) != 0) {
          paramInt5 = Math.min(paramInt5, View.MeasureSpec.getSize(paramInt3));
        }
        if (paramInt5 < j)
        {
          paramWebImageView.setScrollY(paramInt1 * (j - paramInt5) / 2);
          j = paramInt5;
        }
        paramWebImageView.setDimensions(i, Math.max(paramInt4, j));
      }
    }
    
    protected static boolean shouldApplyAspectRatio(double paramDouble, int paramInt)
    {
      if (paramDouble != 0.0D)
      {
        if (View.MeasureSpec.getMode(paramInt) == 0) {
          Log.w("WebImageView", "fixedAspectRatio set, but neither width nor height is restricted.");
        }
      }
      else {
        return false;
      }
      return true;
    }
  }
  
  protected static class DrawableConsumer
    implements Consumer<Drawable>
  {
    private final WeakReference<WebImageView> mRef;
    
    protected DrawableConsumer(WebImageView paramWebImageView)
    {
      this.mRef = new WeakReference(paramWebImageView);
    }
    
    public boolean consume(Drawable paramDrawable)
    {
      WebImageView localWebImageView = (WebImageView)this.mRef.get();
      if (localWebImageView != null)
      {
        localWebImageView.onImageDownloaded(paramDrawable);
        return true;
      }
      return false;
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onImageDownloaded(Drawable paramDrawable);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.WebImageView
 * JD-Core Version:    0.7.0.1
 */