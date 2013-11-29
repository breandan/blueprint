package com.android.ex.photo.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.ex.photo.PhotoViewCallbacks;
import com.android.ex.photo.PhotoViewCallbacks.CursorChangedListener;
import com.android.ex.photo.PhotoViewCallbacks.OnScreenListener;
import com.android.ex.photo.R.bool;
import com.android.ex.photo.R.drawable;
import com.android.ex.photo.R.id;
import com.android.ex.photo.R.layout;
import com.android.ex.photo.R.string;
import com.android.ex.photo.adapters.PhotoPagerAdapter;
import com.android.ex.photo.loaders.PhotoBitmapLoaderInterface;
import com.android.ex.photo.loaders.PhotoBitmapLoaderInterface.BitmapResult;
import com.android.ex.photo.util.ImageUtils;
import com.android.ex.photo.util.ImageUtils.ImageSize;
import com.android.ex.photo.views.PhotoView;
import com.android.ex.photo.views.ProgressBarWrapper;

public class PhotoViewFragment
  extends Fragment
  implements LoaderManager.LoaderCallbacks<PhotoBitmapLoaderInterface.BitmapResult>, View.OnClickListener, PhotoViewCallbacks.CursorChangedListener, PhotoViewCallbacks.OnScreenListener
{
  public static Integer sPhotoSize;
  protected PhotoPagerAdapter mAdapter;
  protected PhotoViewCallbacks mCallback;
  protected boolean mConnected;
  protected boolean mDisplayThumbsFullScreen;
  protected TextView mEmptyText;
  protected boolean mFullScreen;
  protected Intent mIntent;
  protected BroadcastReceiver mInternetStateReceiver;
  protected boolean mOnlyShowSpinner;
  protected View mPhotoPreviewAndProgress;
  protected ImageView mPhotoPreviewImage;
  protected ProgressBarWrapper mPhotoProgressBar;
  protected PhotoView mPhotoView;
  protected int mPosition;
  protected boolean mProgressBarNeeded = true;
  protected String mResolvedPhotoUri;
  protected ImageView mRetryButton;
  protected boolean mThumbnailShown;
  protected String mThumbnailUri;
  protected boolean mWatchNetworkState;
  
  private void bindPhoto(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      if (this.mPhotoView != null) {
        this.mPhotoView.bindDrawable(paramDrawable);
      }
      enableImageTransforms(true);
      this.mPhotoPreviewAndProgress.setVisibility(8);
      this.mProgressBarNeeded = false;
    }
  }
  
  private void displayPhoto(PhotoBitmapLoaderInterface.BitmapResult paramBitmapResult)
  {
    Drawable localDrawable = paramBitmapResult.getDrawable(getResources());
    if (paramBitmapResult.status == 1)
    {
      this.mProgressBarNeeded = false;
      this.mEmptyText.setText(R.string.failed);
      this.mEmptyText.setVisibility(0);
      this.mCallback.onFragmentPhotoLoadComplete(this, false);
      return;
    }
    bindPhoto(localDrawable);
    this.mCallback.onFragmentPhotoLoadComplete(this, true);
  }
  
  public static PhotoViewFragment newInstance(Intent paramIntent, int paramInt, boolean paramBoolean)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("arg-intent", paramIntent);
    localBundle.putInt("arg-position", paramInt);
    localBundle.putBoolean("arg-show-spinner", paramBoolean);
    PhotoViewFragment localPhotoViewFragment = new PhotoViewFragment();
    localPhotoViewFragment.setArguments(localBundle);
    return localPhotoViewFragment;
  }
  
  private void resetPhotoView()
  {
    if (this.mPhotoView != null) {
      this.mPhotoView.bindPhoto(null);
    }
  }
  
  private void setViewVisibility()
  {
    if (this.mCallback == null) {}
    for (boolean bool = false;; bool = this.mCallback.isFragmentFullScreen(this))
    {
      setFullScreen(bool);
      return;
    }
  }
  
  public void enableImageTransforms(boolean paramBoolean)
  {
    this.mPhotoView.enableImageTransforms(paramBoolean);
  }
  
  public String getPhotoUri()
  {
    return this.mResolvedPhotoUri;
  }
  
  protected void initializeView(View paramView)
  {
    this.mPhotoView = ((PhotoView)paramView.findViewById(R.id.photo_view));
    this.mPhotoView.setMaxInitialScale(this.mIntent.getFloatExtra("max_scale", 1.0F));
    this.mPhotoView.setOnClickListener(this);
    this.mPhotoView.setFullScreen(this.mFullScreen, false);
    this.mPhotoView.enableImageTransforms(false);
    this.mPhotoPreviewAndProgress = paramView.findViewById(R.id.photo_preview);
    this.mPhotoPreviewImage = ((ImageView)paramView.findViewById(R.id.photo_preview_image));
    this.mThumbnailShown = false;
    ProgressBar localProgressBar = (ProgressBar)paramView.findViewById(R.id.indeterminate_progress);
    this.mPhotoProgressBar = new ProgressBarWrapper((ProgressBar)paramView.findViewById(R.id.determinate_progress), localProgressBar, true);
    this.mEmptyText = ((TextView)paramView.findViewById(R.id.empty_text));
    this.mRetryButton = ((ImageView)paramView.findViewById(R.id.retry_button));
    setViewVisibility();
  }
  
  public boolean isPhotoBound()
  {
    return (this.mPhotoView != null) && (this.mPhotoView.isPhotoBound());
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mCallback = ((PhotoViewCallbacks)getActivity());
    if (this.mCallback == null) {
      throw new IllegalArgumentException("Activity must be a derived class of PhotoViewActivity");
    }
    this.mAdapter = this.mCallback.getAdapter();
    if (this.mAdapter == null) {
      throw new IllegalStateException("Callback reported null adapter");
    }
    setViewVisibility();
  }
  
  public void onClick(View paramView)
  {
    this.mCallback.toggleFullScreen();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DisplayMetrics localDisplayMetrics;
    ImageUtils.ImageSize localImageSize;
    if (sPhotoSize == null)
    {
      localDisplayMetrics = new DisplayMetrics();
      WindowManager localWindowManager = (WindowManager)getActivity().getSystemService("window");
      localImageSize = ImageUtils.sUseImageSize;
      localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
    }
    Bundle localBundle1;
    switch (1.$SwitchMap$com$android$ex$photo$util$ImageUtils$ImageSize[localImageSize.ordinal()])
    {
    default: 
      sPhotoSize = Integer.valueOf(Math.min(localDisplayMetrics.heightPixels, localDisplayMetrics.widthPixels));
      localBundle1 = getArguments();
      if (localBundle1 != null) {
        break;
      }
    }
    do
    {
      return;
      sPhotoSize = Integer.valueOf(800 * Math.min(localDisplayMetrics.heightPixels, localDisplayMetrics.widthPixels) / 1000);
      break;
      this.mIntent = ((Intent)localBundle1.getParcelable("arg-intent"));
      this.mDisplayThumbsFullScreen = this.mIntent.getBooleanExtra("display_thumbs_fullscreen", false);
      this.mPosition = localBundle1.getInt("arg-position");
      this.mOnlyShowSpinner = localBundle1.getBoolean("arg-show-spinner");
      this.mProgressBarNeeded = true;
      if (paramBundle != null)
      {
        Bundle localBundle2 = paramBundle.getBundle("com.android.mail.photo.fragments.PhotoViewFragment.INTENT");
        if (localBundle2 != null) {
          this.mIntent = new Intent().putExtras(localBundle2);
        }
      }
    } while (this.mIntent == null);
    this.mResolvedPhotoUri = this.mIntent.getStringExtra("resolved_photo_uri");
    this.mThumbnailUri = this.mIntent.getStringExtra("thumbnail_uri");
    this.mWatchNetworkState = this.mIntent.getBooleanExtra("watch_network", false);
  }
  
  public Loader<PhotoBitmapLoaderInterface.BitmapResult> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    if (this.mOnlyShowSpinner) {
      return null;
    }
    String str = null;
    switch (paramInt)
    {
    }
    for (;;)
    {
      return this.mCallback.onCreateBitmapLoader(paramInt, paramBundle, str);
      str = this.mThumbnailUri;
      continue;
      str = this.mResolvedPhotoUri;
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(R.layout.photo_fragment_view, paramViewGroup, false);
    initializeView(localView);
    return localView;
  }
  
  public void onCursorChanged(Cursor paramCursor)
  {
    if (this.mAdapter == null) {}
    Loader localLoader2;
    do
    {
      LoaderManager localLoaderManager;
      do
      {
        do
        {
          return;
        } while ((!paramCursor.moveToPosition(this.mPosition)) || (isPhotoBound()));
        this.mCallback.onCursorChanged(this, paramCursor);
        localLoaderManager = getLoaderManager();
        Loader localLoader1 = localLoaderManager.getLoader(3);
        if (localLoader1 != null)
        {
          PhotoBitmapLoaderInterface localPhotoBitmapLoaderInterface2 = (PhotoBitmapLoaderInterface)localLoader1;
          this.mResolvedPhotoUri = this.mAdapter.getPhotoUri(paramCursor);
          localPhotoBitmapLoaderInterface2.setPhotoUri(this.mResolvedPhotoUri);
          localPhotoBitmapLoaderInterface2.forceLoad();
        }
      } while (this.mThumbnailShown);
      localLoader2 = localLoaderManager.getLoader(2);
    } while (localLoader2 == null);
    PhotoBitmapLoaderInterface localPhotoBitmapLoaderInterface1 = (PhotoBitmapLoaderInterface)localLoader2;
    this.mThumbnailUri = this.mAdapter.getThumbnailUri(paramCursor);
    localPhotoBitmapLoaderInterface1.setPhotoUri(this.mThumbnailUri);
    localPhotoBitmapLoaderInterface1.forceLoad();
  }
  
  public void onDestroyView()
  {
    if (this.mPhotoView != null)
    {
      this.mPhotoView.clear();
      this.mPhotoView = null;
    }
    super.onDestroyView();
  }
  
  public void onDetach()
  {
    this.mCallback = null;
    super.onDetach();
  }
  
  public void onFullScreenChanged(boolean paramBoolean)
  {
    setViewVisibility();
  }
  
  public boolean onInterceptMoveLeft(float paramFloat1, float paramFloat2)
  {
    if (!this.mCallback.isFragmentActive(this)) {}
    while ((this.mPhotoView == null) || (!this.mPhotoView.interceptMoveLeft(paramFloat1, paramFloat2))) {
      return false;
    }
    return true;
  }
  
  public boolean onInterceptMoveRight(float paramFloat1, float paramFloat2)
  {
    if (!this.mCallback.isFragmentActive(this)) {}
    while ((this.mPhotoView == null) || (!this.mPhotoView.interceptMoveRight(paramFloat1, paramFloat2))) {
      return false;
    }
    return true;
  }
  
  public void onLoadFinished(Loader<PhotoBitmapLoaderInterface.BitmapResult> paramLoader, PhotoBitmapLoaderInterface.BitmapResult paramBitmapResult)
  {
    Drawable localDrawable = paramBitmapResult.getDrawable(getResources());
    if (getView() == null) {
      return;
    }
    switch (paramLoader.getId())
    {
    }
    for (;;)
    {
      if (!this.mProgressBarNeeded) {
        this.mPhotoProgressBar.setVisibility(8);
      }
      if (localDrawable != null) {
        this.mCallback.onNewPhotoLoaded(this.mPosition);
      }
      setViewVisibility();
      return;
      if (this.mDisplayThumbsFullScreen)
      {
        displayPhoto(paramBitmapResult);
      }
      else
      {
        if (isPhotoBound()) {
          break;
        }
        if (localDrawable == null) {
          this.mPhotoPreviewImage.setImageResource(R.drawable.default_image);
        }
        for (this.mThumbnailShown = false;; this.mThumbnailShown = true)
        {
          this.mPhotoPreviewImage.setVisibility(0);
          if (getResources().getBoolean(R.bool.force_thumbnail_no_scaling)) {
            this.mPhotoPreviewImage.setScaleType(ImageView.ScaleType.CENTER);
          }
          enableImageTransforms(false);
          break;
          this.mPhotoPreviewImage.setImageDrawable(localDrawable);
        }
        displayPhoto(paramBitmapResult);
      }
    }
  }
  
  public void onLoaderReset(Loader<PhotoBitmapLoaderInterface.BitmapResult> paramLoader) {}
  
  public void onPause()
  {
    if (this.mWatchNetworkState) {
      getActivity().unregisterReceiver(this.mInternetStateReceiver);
    }
    this.mCallback.removeCursorListener(this);
    this.mCallback.removeScreenListener(this.mPosition);
    resetPhotoView();
    super.onPause();
  }
  
  public void onResume()
  {
    super.onResume();
    this.mCallback.addScreenListener(this.mPosition, this);
    this.mCallback.addCursorListener(this);
    NetworkInfo localNetworkInfo;
    if (this.mWatchNetworkState)
    {
      if (this.mInternetStateReceiver == null) {
        this.mInternetStateReceiver = new InternetStateBroadcastReceiver(null);
      }
      getActivity().registerReceiver(this.mInternetStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
      localNetworkInfo = ((ConnectivityManager)getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo == null) {
        break label152;
      }
    }
    label152:
    for (this.mConnected = localNetworkInfo.isConnected();; this.mConnected = false)
    {
      if (!isPhotoBound())
      {
        this.mProgressBarNeeded = true;
        this.mPhotoPreviewAndProgress.setVisibility(0);
        getLoaderManager().initLoader(2, null, this);
        getLoaderManager().initLoader(3, null, this);
      }
      return;
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mIntent != null) {
      paramBundle.putParcelable("com.android.mail.photo.fragments.PhotoViewFragment.INTENT", this.mIntent.getExtras());
    }
  }
  
  public void onViewActivated()
  {
    if (!this.mCallback.isFragmentActive(this))
    {
      resetViews();
      return;
    }
    if (!isPhotoBound()) {
      getLoaderManager().restartLoader(2, null, this);
    }
    this.mCallback.onFragmentVisible(this);
  }
  
  public void resetViews()
  {
    if (this.mPhotoView != null) {
      this.mPhotoView.resetTransformations();
    }
  }
  
  public void setFullScreen(boolean paramBoolean)
  {
    this.mFullScreen = paramBoolean;
  }
  
  private class InternetStateBroadcastReceiver
    extends BroadcastReceiver
  {
    private InternetStateBroadcastReceiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if ((localNetworkInfo == null) || (!localNetworkInfo.isConnected())) {
        PhotoViewFragment.this.mConnected = false;
      }
      while ((PhotoViewFragment.this.mConnected) || (PhotoViewFragment.this.isPhotoBound())) {
        return;
      }
      if (!PhotoViewFragment.this.mThumbnailShown) {
        PhotoViewFragment.this.getLoaderManager().restartLoader(2, null, PhotoViewFragment.this);
      }
      PhotoViewFragment.this.getLoaderManager().restartLoader(3, null, PhotoViewFragment.this);
      PhotoViewFragment.this.mConnected = true;
      PhotoViewFragment.this.mPhotoProgressBar.setVisibility(0);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.fragments.PhotoViewFragment
 * JD-Core Version:    0.7.0.1
 */