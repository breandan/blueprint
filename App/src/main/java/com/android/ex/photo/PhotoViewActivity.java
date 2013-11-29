package com.android.ex.photo;

import android.app.ActionBar;
import android.app.ActionBar.OnMenuVisibilityListener;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import com.android.ex.photo.adapters.PhotoPagerAdapter;
import com.android.ex.photo.fragments.PhotoViewFragment;
import com.android.ex.photo.loaders.PhotoBitmapLoader;
import com.android.ex.photo.loaders.PhotoBitmapLoaderInterface.BitmapResult;
import com.android.ex.photo.loaders.PhotoPagerLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PhotoViewActivity
  extends FragmentActivity
  implements ActionBar.OnMenuVisibilityListener, LoaderManager.LoaderCallbacks<Cursor>, ViewPager.OnPageChangeListener, PhotoViewCallbacks, PhotoViewController.PhotoViewControllerCallbacks, PhotoViewPager.OnInterceptTouchListener
{
  public static int sMemoryClass;
  protected boolean mActionBarHiddenInitially;
  protected String mActionBarSubtitle;
  protected String mActionBarTitle;
  protected PhotoPagerAdapter mAdapter;
  protected int mAlbumCount = -1;
  protected int mAnimationStartHeight;
  protected int mAnimationStartWidth;
  protected int mAnimationStartX;
  protected int mAnimationStartY;
  protected View mBackground;
  protected BitmapCallback mBitmapCallback;
  private PhotoViewController mController;
  private int mCurrentPhotoIndex;
  private String mCurrentPhotoUri;
  private final Set<PhotoViewCallbacks.CursorChangedListener> mCursorListeners = new HashSet();
  protected boolean mDisplayThumbsFullScreen;
  private boolean mEnterAnimationFinished;
  private long mEnterFullScreenDelayTime;
  private final Runnable mEnterFullScreenRunnable = new Runnable()
  {
    public void run()
    {
      PhotoViewActivity.this.setFullScreen(true, true);
    }
  };
  protected boolean mFullScreen;
  protected final Handler mHandler = new Handler();
  protected boolean mIsEmpty;
  protected boolean mIsPaused = true;
  protected float mMaxInitialScale;
  private String mPhotosUri;
  private String[] mProjection;
  private boolean mRestartLoader;
  protected View mRootView;
  protected boolean mScaleAnimationEnabled;
  private final Map<Integer, PhotoViewCallbacks.OnScreenListener> mScreenListeners = new HashMap();
  protected ImageView mTemporaryImage;
  protected PhotoViewPager mViewPager;
  
  private int calculateTranslate(int paramInt1, int paramInt2, int paramInt3, float paramFloat)
  {
    int i = Math.round((paramInt3 - paramFloat * paramInt3) / 2.0F);
    int j = Math.round((paramFloat * paramInt3 - paramInt2) / 2.0F);
    return paramInt1 - i - j;
  }
  
  private void cancelEnterFullScreenRunnable()
  {
    this.mHandler.removeCallbacks(this.mEnterFullScreenRunnable);
  }
  
  private static final String getInputOrEmpty(String paramString)
  {
    if (paramString == null) {
      paramString = "";
    }
    return paramString;
  }
  
  private void initTemporaryImage(Drawable paramDrawable)
  {
    if (this.mEnterAnimationFinished) {
      return;
    }
    this.mTemporaryImage.setImageDrawable(paramDrawable);
    if (paramDrawable != null)
    {
      if (this.mRootView.getMeasuredWidth() != 0) {
        break label64;
      }
      final View localView = this.mRootView;
      localView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
      {
        public void onGlobalLayout()
        {
          if (Build.VERSION.SDK_INT >= 16) {
            localView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
          for (;;)
          {
            PhotoViewActivity.this.runEnterAnimation();
            return;
            localView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          }
        }
      });
    }
    for (;;)
    {
      getSupportLoaderManager().initLoader(100, null, this);
      return;
      label64:
      runEnterAnimation();
    }
  }
  
  private void notifyCursorListeners(Cursor paramCursor)
  {
    try
    {
      Iterator localIterator = this.mCursorListeners.iterator();
      while (localIterator.hasNext()) {
        ((PhotoViewCallbacks.CursorChangedListener)localIterator.next()).onCursorChanged(paramCursor);
      }
    }
    finally {}
  }
  
  private void onExitAnimationComplete()
  {
    finish();
    overridePendingTransition(0, 0);
  }
  
  private void postEnterFullScreenRunnableWithDelay()
  {
    this.mHandler.postDelayed(this.mEnterFullScreenRunnable, this.mEnterFullScreenDelayTime);
  }
  
  private void runEnterAnimation()
  {
    int i = this.mRootView.getMeasuredWidth();
    int j = this.mRootView.getMeasuredHeight();
    this.mTemporaryImage.setVisibility(0);
    float f = Math.max(this.mAnimationStartWidth / i, this.mAnimationStartHeight / j);
    int k = calculateTranslate(this.mAnimationStartX, this.mAnimationStartWidth, i, f);
    int m = calculateTranslate(this.mAnimationStartY, this.mAnimationStartHeight, j, f);
    int n = Build.VERSION.SDK_INT;
    if (n >= 14)
    {
      this.mBackground.setAlpha(0.0F);
      this.mBackground.animate().alpha(1.0F).setDuration(250L).start();
      this.mBackground.setVisibility(0);
      this.mTemporaryImage.setScaleX(f);
      this.mTemporaryImage.setScaleY(f);
      this.mTemporaryImage.setTranslationX(k);
      this.mTemporaryImage.setTranslationY(m);
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          PhotoViewActivity.this.onEnterAnimationComplete();
        }
      };
      ViewPropertyAnimator localViewPropertyAnimator = this.mTemporaryImage.animate().scaleX(1.0F).scaleY(1.0F).translationX(0.0F).translationY(0.0F).setDuration(250L);
      if (n >= 16) {
        localViewPropertyAnimator.withEndAction(local2);
      }
      for (;;)
      {
        localViewPropertyAnimator.start();
        return;
        this.mHandler.postDelayed(local2, 250L);
      }
    }
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
    localAlphaAnimation.setDuration(250L);
    this.mBackground.startAnimation(localAlphaAnimation);
    this.mBackground.setVisibility(0);
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(k, m, 0.0F, 0.0F);
    localTranslateAnimation.setDuration(250L);
    ScaleAnimation localScaleAnimation = new ScaleAnimation(f, f, 0.0F, 0.0F);
    localScaleAnimation.setDuration(250L);
    AnimationSet localAnimationSet = new AnimationSet(true);
    localAnimationSet.addAnimation(localTranslateAnimation);
    localAnimationSet.addAnimation(localScaleAnimation);
    localAnimationSet.setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnonymousAnimation)
      {
        PhotoViewActivity.this.onEnterAnimationComplete();
      }
      
      public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
      
      public void onAnimationStart(Animation paramAnonymousAnimation) {}
    });
    this.mTemporaryImage.startAnimation(localAnimationSet);
  }
  
  private void runExitAnimation()
  {
    getIntent();
    int i = this.mRootView.getMeasuredWidth();
    int j = this.mRootView.getMeasuredHeight();
    float f = Math.max(this.mAnimationStartWidth / i, this.mAnimationStartHeight / j);
    int k = calculateTranslate(this.mAnimationStartX, this.mAnimationStartWidth, i, f);
    int m = calculateTranslate(this.mAnimationStartY, this.mAnimationStartHeight, j, f);
    int n = Build.VERSION.SDK_INT;
    if (n >= 14)
    {
      this.mBackground.animate().alpha(0.0F).setDuration(250L).start();
      this.mBackground.setVisibility(0);
      Runnable local4 = new Runnable()
      {
        public void run()
        {
          PhotoViewActivity.this.onExitAnimationComplete();
        }
      };
      ViewPropertyAnimator localViewPropertyAnimator;
      if (this.mTemporaryImage.getVisibility() == 0)
      {
        localViewPropertyAnimator = this.mTemporaryImage.animate().scaleX(f).scaleY(f).translationX(k).translationY(m).setDuration(250L);
        if (n < 16) {
          break label234;
        }
        localViewPropertyAnimator.withEndAction(local4);
      }
      for (;;)
      {
        localViewPropertyAnimator.start();
        return;
        localViewPropertyAnimator = this.mViewPager.animate().scaleX(f).scaleY(f).translationX(k).translationY(m).setDuration(250L);
        break;
        label234:
        this.mHandler.postDelayed(local4, 250L);
      }
    }
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(1.0F, 0.0F);
    localAlphaAnimation.setDuration(250L);
    this.mBackground.startAnimation(localAlphaAnimation);
    this.mBackground.setVisibility(0);
    ScaleAnimation localScaleAnimation = new ScaleAnimation(1.0F, 1.0F, f, f);
    localScaleAnimation.setDuration(250L);
    localScaleAnimation.setAnimationListener(new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnonymousAnimation)
      {
        PhotoViewActivity.this.onExitAnimationComplete();
      }
      
      public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
      
      public void onAnimationStart(Animation paramAnonymousAnimation) {}
    });
    if (this.mTemporaryImage.getVisibility() == 0)
    {
      this.mTemporaryImage.startAnimation(localScaleAnimation);
      return;
    }
    this.mViewPager.startAnimation(localScaleAnimation);
  }
  
  public void addCursorListener(PhotoViewCallbacks.CursorChangedListener paramCursorChangedListener)
  {
    try
    {
      this.mCursorListeners.add(paramCursorChangedListener);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void addScreenListener(int paramInt, PhotoViewCallbacks.OnScreenListener paramOnScreenListener)
  {
    this.mScreenListeners.put(Integer.valueOf(paramInt), paramOnScreenListener);
  }
  
  protected PhotoPagerAdapter createPhotoPagerAdapter(Context paramContext, FragmentManager paramFragmentManager, Cursor paramCursor, float paramFloat)
  {
    return new PhotoPagerAdapter(paramContext, paramFragmentManager, paramCursor, paramFloat, this.mDisplayThumbsFullScreen);
  }
  
  public PhotoPagerAdapter getAdapter()
  {
    return this.mAdapter;
  }
  
  public Cursor getCursor()
  {
    if (this.mAdapter == null) {
      return null;
    }
    return this.mAdapter.getCursor();
  }
  
  public Cursor getCursorAtProperPosition()
  {
    if (this.mViewPager == null) {
      return null;
    }
    int i = this.mViewPager.getCurrentItem();
    Cursor localCursor = this.mAdapter.getCursor();
    if (localCursor == null) {
      return null;
    }
    localCursor.moveToPosition(i);
    return localCursor;
  }
  
  public View getRootView()
  {
    return this.mRootView;
  }
  
  public void hideActionBar()
  {
    getActionBar().hide();
  }
  
  public boolean isEnterAnimationFinished()
  {
    return this.mEnterAnimationFinished;
  }
  
  public boolean isFragmentActive(Fragment paramFragment)
  {
    if ((this.mViewPager == null) || (this.mAdapter == null)) {}
    while (this.mViewPager.getCurrentItem() != this.mAdapter.getItemPosition(paramFragment)) {
      return false;
    }
    return true;
  }
  
  public boolean isFragmentFullScreen(Fragment paramFragment)
  {
    if ((this.mViewPager == null) || (this.mAdapter == null) || (this.mAdapter.getCount() == 0)) {
      return this.mFullScreen;
    }
    return (this.mFullScreen) || (this.mViewPager.getCurrentItem() != this.mAdapter.getItemPosition(paramFragment));
  }
  
  protected boolean isFullScreen()
  {
    return this.mFullScreen;
  }
  
  public boolean isScaleAnimationEnabled()
  {
    return this.mScaleAnimationEnabled;
  }
  
  public void onBackPressed()
  {
    if ((this.mFullScreen) && (!this.mActionBarHiddenInitially))
    {
      toggleFullScreen();
      return;
    }
    if (this.mScaleAnimationEnabled)
    {
      runExitAnimation();
      return;
    }
    super.onBackPressed();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sMemoryClass = ((ActivityManager)getApplicationContext().getSystemService("activity")).getMemoryClass();
    this.mController = new PhotoViewController(this);
    Intent localIntent = getIntent();
    if (localIntent.hasExtra("photos_uri")) {
      this.mPhotosUri = localIntent.getStringExtra("photos_uri");
    }
    if (localIntent.getBooleanExtra("scale_up_animation", false))
    {
      this.mScaleAnimationEnabled = true;
      this.mAnimationStartX = localIntent.getIntExtra("start_x_extra", 0);
      this.mAnimationStartY = localIntent.getIntExtra("start_y_extra", 0);
      this.mAnimationStartWidth = localIntent.getIntExtra("start_width_extra", 0);
      this.mAnimationStartHeight = localIntent.getIntExtra("start_height_extra", 0);
    }
    this.mActionBarHiddenInitially = localIntent.getBooleanExtra("action_bar_hidden_initially", false);
    this.mDisplayThumbsFullScreen = localIntent.getBooleanExtra("display_thumbs_fullscreen", false);
    label313:
    Resources localResources;
    if (localIntent.hasExtra("projection"))
    {
      this.mProjection = localIntent.getStringArrayExtra("projection");
      this.mMaxInitialScale = localIntent.getFloatExtra("max_scale", 1.0F);
      this.mCurrentPhotoUri = null;
      this.mCurrentPhotoIndex = -1;
      if (localIntent.hasExtra("photo_index")) {
        this.mCurrentPhotoIndex = localIntent.getIntExtra("photo_index", -1);
      }
      if (localIntent.hasExtra("initial_photo_uri")) {
        this.mCurrentPhotoUri = localIntent.getStringExtra("initial_photo_uri");
      }
      this.mIsEmpty = true;
      if (paramBundle == null) {
        break label577;
      }
      this.mCurrentPhotoUri = paramBundle.getString("com.google.android.apps.plus.PhotoViewFragment.CURRENT_URI");
      this.mCurrentPhotoIndex = paramBundle.getInt("com.google.android.apps.plus.PhotoViewFragment.CURRENT_INDEX");
      this.mFullScreen = paramBundle.getBoolean("com.google.android.apps.plus.PhotoViewFragment.FULLSCREEN", false);
      this.mActionBarTitle = paramBundle.getString("com.google.android.apps.plus.PhotoViewFragment.ACTIONBARTITLE");
      this.mActionBarSubtitle = paramBundle.getString("com.google.android.apps.plus.PhotoViewFragment.ACTIONBARTITLE");
      this.mEnterAnimationFinished = paramBundle.getBoolean("com.google.android.apps.plus.PhotoViewFragment.SCALEANIMATIONFINISHED", false);
      setContentView(R.layout.photo_activity_view);
      this.mAdapter = createPhotoPagerAdapter(this, getSupportFragmentManager(), null, this.mMaxInitialScale);
      localResources = getResources();
      this.mRootView = findViewById(R.id.photo_activity_root_view);
      if (Build.VERSION.SDK_INT >= 11) {
        this.mRootView.setOnSystemUiVisibilityChangeListener(this.mController.getSystemUiVisibilityChangeListener());
      }
      this.mBackground = findViewById(R.id.photo_activity_background);
      this.mTemporaryImage = ((ImageView)findViewById(R.id.photo_activity_temporary_image));
      this.mViewPager = ((PhotoViewPager)findViewById(R.id.photo_view_pager));
      this.mViewPager.setAdapter(this.mAdapter);
      this.mViewPager.setOnPageChangeListener(this);
      this.mViewPager.setOnInterceptTouchListener(this);
      this.mViewPager.setPageMargin(localResources.getDimensionPixelSize(R.dimen.photo_page_margin));
      this.mBitmapCallback = new BitmapCallback(null);
      if ((this.mScaleAnimationEnabled) && (!this.mEnterAnimationFinished)) {
        break label588;
      }
      getSupportLoaderManager().initLoader(100, null, this);
      this.mBackground.setVisibility(0);
    }
    for (;;)
    {
      this.mEnterFullScreenDelayTime = localResources.getInteger(R.integer.reenter_fullscreen_delay_time_in_millis);
      ActionBar localActionBar = getActionBar();
      if (localActionBar != null)
      {
        localActionBar.setDisplayHomeAsUpEnabled(true);
        localActionBar.addOnMenuVisibilityListener(this);
        localActionBar.setDisplayOptions(8, 8);
        setActionBarTitles(localActionBar);
      }
      if (this.mScaleAnimationEnabled) {
        break label636;
      }
      setLightsOutMode(this.mFullScreen);
      return;
      this.mProjection = null;
      break;
      label577:
      this.mFullScreen = this.mActionBarHiddenInitially;
      break label313;
      label588:
      this.mViewPager.setVisibility(8);
      Bundle localBundle = new Bundle();
      localBundle.putString("image_uri", this.mCurrentPhotoUri);
      getSupportLoaderManager().initLoader(2, localBundle, this.mBitmapCallback);
    }
    label636:
    setLightsOutMode(false);
  }
  
  public Loader<PhotoBitmapLoaderInterface.BitmapResult> onCreateBitmapLoader(int paramInt, Bundle paramBundle, String paramString)
  {
    switch (paramInt)
    {
    default: 
      return null;
    }
    return new PhotoBitmapLoader(this, paramString);
  }
  
  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    if (paramInt == 100) {
      return new PhotoPagerLoader(this, Uri.parse(this.mPhotosUri), this.mProjection);
    }
    return null;
  }
  
  public void onCursorChanged(PhotoViewFragment paramPhotoViewFragment, Cursor paramCursor) {}
  
  public void onEnterAnimationComplete()
  {
    this.mEnterAnimationFinished = true;
    this.mViewPager.setVisibility(0);
    setLightsOutMode(this.mFullScreen);
  }
  
  public void onFragmentPhotoLoadComplete(PhotoViewFragment paramPhotoViewFragment, boolean paramBoolean)
  {
    if ((this.mTemporaryImage.getVisibility() != 8) && (TextUtils.equals(paramPhotoViewFragment.getPhotoUri(), this.mCurrentPhotoUri)))
    {
      if (paramBoolean)
      {
        this.mTemporaryImage.setVisibility(8);
        this.mViewPager.setVisibility(0);
      }
    }
    else {
      return;
    }
    Log.w("PhotoViewActivity", "Failed to load fragment image");
    this.mTemporaryImage.setVisibility(8);
    this.mViewPager.setVisibility(0);
  }
  
  public void onFragmentVisible(PhotoViewFragment paramPhotoViewFragment) {}
  
  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (paramLoader.getId() == 100)
    {
      if ((paramCursor != null) && (paramCursor.getCount() != 0)) {
        break label32;
      }
      this.mIsEmpty = true;
    }
    for (;;)
    {
      updateActionItems();
      return;
      label32:
      this.mAlbumCount = paramCursor.getCount();
      int i;
      int j;
      Uri localUri1;
      if (this.mCurrentPhotoUri != null)
      {
        i = 0;
        j = paramCursor.getColumnIndex("uri");
        if (Build.VERSION.SDK_INT < 11) {
          break label166;
        }
        localUri1 = Uri.parse(this.mCurrentPhotoUri).buildUpon().clearQuery().build();
      }
      for (;;)
      {
        String str;
        if (paramCursor.moveToNext())
        {
          str = paramCursor.getString(j);
          if (Build.VERSION.SDK_INT < 11) {
            break label191;
          }
        }
        label166:
        label191:
        for (Uri localUri2 = Uri.parse(str).buildUpon().clearQuery().build();; localUri2 = Uri.parse(str).buildUpon().query(null).build())
        {
          if ((localUri1 == null) || (!localUri1.equals(localUri2))) {
            break label211;
          }
          this.mCurrentPhotoIndex = i;
          if (!this.mIsPaused) {
            break label217;
          }
          this.mRestartLoader = true;
          return;
          localUri1 = Uri.parse(this.mCurrentPhotoUri).buildUpon().query(null).build();
          i = 0;
          break;
        }
        label211:
        i++;
      }
      label217:
      boolean bool = this.mIsEmpty;
      this.mIsEmpty = false;
      this.mAdapter.swapCursor(paramCursor);
      if (this.mViewPager.getAdapter() == null) {
        this.mViewPager.setAdapter(this.mAdapter);
      }
      notifyCursorListeners(paramCursor);
      if (this.mCurrentPhotoIndex < 0) {
        this.mCurrentPhotoIndex = 0;
      }
      this.mViewPager.setCurrentItem(this.mCurrentPhotoIndex, false);
      if (bool) {
        setViewActivated(this.mCurrentPhotoIndex);
      }
    }
  }
  
  public void onLoaderReset(Loader<Cursor> paramLoader) {}
  
  public void onMenuVisibilityChanged(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      cancelEnterFullScreenRunnable();
      return;
    }
    postEnterFullScreenRunnableWithDelay();
  }
  
  public void onNewPhotoLoaded(int paramInt) {}
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default: 
      return super.onOptionsItemSelected(paramMenuItem);
    }
    finish();
    return true;
  }
  
  public void onPageScrollStateChanged(int paramInt) {}
  
  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}
  
  public void onPageSelected(int paramInt)
  {
    this.mCurrentPhotoIndex = paramInt;
    setViewActivated(paramInt);
  }
  
  protected void onPause()
  {
    this.mIsPaused = true;
    super.onPause();
  }
  
  protected void onResume()
  {
    super.onResume();
    setFullScreen(this.mFullScreen, false);
    this.mIsPaused = false;
    if (this.mRestartLoader)
    {
      this.mRestartLoader = false;
      getSupportLoaderManager().restartLoader(100, null, this);
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("com.google.android.apps.plus.PhotoViewFragment.CURRENT_URI", this.mCurrentPhotoUri);
    paramBundle.putInt("com.google.android.apps.plus.PhotoViewFragment.CURRENT_INDEX", this.mCurrentPhotoIndex);
    paramBundle.putBoolean("com.google.android.apps.plus.PhotoViewFragment.FULLSCREEN", this.mFullScreen);
    paramBundle.putString("com.google.android.apps.plus.PhotoViewFragment.ACTIONBARTITLE", this.mActionBarTitle);
    paramBundle.putString("com.google.android.apps.plus.PhotoViewFragment.ACTIONBARTITLE", this.mActionBarSubtitle);
    paramBundle.putBoolean("com.google.android.apps.plus.PhotoViewFragment.SCALEANIMATIONFINISHED", this.mEnterAnimationFinished);
  }
  
  public PhotoViewPager.InterceptType onTouchIntercept(float paramFloat1, float paramFloat2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    Iterator localIterator = this.mScreenListeners.values().iterator();
    while (localIterator.hasNext())
    {
      PhotoViewCallbacks.OnScreenListener localOnScreenListener = (PhotoViewCallbacks.OnScreenListener)localIterator.next();
      if (!bool1) {
        bool1 = localOnScreenListener.onInterceptMoveLeft(paramFloat1, paramFloat2);
      }
      if (!bool2) {
        bool2 = localOnScreenListener.onInterceptMoveRight(paramFloat1, paramFloat2);
      }
    }
    if (bool1)
    {
      if (bool2) {
        return PhotoViewPager.InterceptType.BOTH;
      }
      return PhotoViewPager.InterceptType.LEFT;
    }
    if (bool2) {
      return PhotoViewPager.InterceptType.RIGHT;
    }
    return PhotoViewPager.InterceptType.NONE;
  }
  
  public void removeCursorListener(PhotoViewCallbacks.CursorChangedListener paramCursorChangedListener)
  {
    try
    {
      this.mCursorListeners.remove(paramCursorChangedListener);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void removeScreenListener(int paramInt)
  {
    this.mScreenListeners.remove(Integer.valueOf(paramInt));
  }
  
  protected final void setActionBarTitles(ActionBar paramActionBar)
  {
    if (paramActionBar == null) {
      return;
    }
    paramActionBar.setTitle(getInputOrEmpty(this.mActionBarTitle));
    paramActionBar.setSubtitle(getInputOrEmpty(this.mActionBarSubtitle));
  }
  
  protected void setFullScreen(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i;
    if (paramBoolean1 != this.mFullScreen)
    {
      i = 1;
      this.mFullScreen = paramBoolean1;
      if (!this.mFullScreen) {
        break label88;
      }
      setLightsOutMode(true);
      cancelEnterFullScreenRunnable();
    }
    for (;;)
    {
      if (i == 0) {
        return;
      }
      Iterator localIterator = this.mScreenListeners.values().iterator();
      while (localIterator.hasNext()) {
        ((PhotoViewCallbacks.OnScreenListener)localIterator.next()).onFullScreenChanged(this.mFullScreen);
      }
      i = 0;
      break;
      label88:
      setLightsOutMode(false);
      if (paramBoolean2) {
        postEnterFullScreenRunnableWithDelay();
      }
    }
  }
  
  protected void setLightsOutMode(boolean paramBoolean)
  {
    this.mController.setImmersiveMode(paramBoolean);
  }
  
  public void setNotFullscreenCallbackDoNotUseThisFunction()
  {
    setFullScreen(false, true);
  }
  
  protected void setPhotoIndex(int paramInt)
  {
    this.mCurrentPhotoIndex = paramInt;
  }
  
  public void setViewActivated(int paramInt)
  {
    PhotoViewCallbacks.OnScreenListener localOnScreenListener = (PhotoViewCallbacks.OnScreenListener)this.mScreenListeners.get(Integer.valueOf(paramInt));
    if (localOnScreenListener != null) {
      localOnScreenListener.onViewActivated();
    }
    Cursor localCursor = getCursorAtProperPosition();
    this.mCurrentPhotoIndex = paramInt;
    this.mCurrentPhotoUri = localCursor.getString(localCursor.getColumnIndex("uri"));
    updateActionBar();
    cancelEnterFullScreenRunnable();
    postEnterFullScreenRunnableWithDelay();
  }
  
  public void showActionBar()
  {
    getActionBar().show();
  }
  
  public void toggleFullScreen()
  {
    if (!this.mFullScreen) {}
    for (boolean bool = true;; bool = false)
    {
      setFullScreen(bool, true);
      return;
    }
  }
  
  protected void updateActionBar()
  {
    int i = 1 + this.mViewPager.getCurrentItem();
    int j;
    if (this.mAlbumCount >= 0)
    {
      j = 1;
      Cursor localCursor = getCursorAtProperPosition();
      if (localCursor == null) {
        break label81;
      }
      this.mActionBarTitle = localCursor.getString(localCursor.getColumnIndex("_display_name"));
      label47:
      if ((!this.mIsEmpty) && (j != 0) && (i > 0)) {
        break label89;
      }
    }
    label81:
    label89:
    Resources localResources;
    int k;
    Object[] arrayOfObject;
    for (this.mActionBarSubtitle = null;; this.mActionBarSubtitle = localResources.getString(k, arrayOfObject))
    {
      setActionBarTitles(getActionBar());
      return;
      j = 0;
      break;
      this.mActionBarTitle = null;
      break label47;
      localResources = getResources();
      k = R.string.photo_view_count;
      arrayOfObject = new Object[2];
      arrayOfObject[0] = Integer.valueOf(i);
      arrayOfObject[1] = Integer.valueOf(this.mAlbumCount);
    }
  }
  
  protected void updateActionItems() {}
  
  private class BitmapCallback
    implements LoaderManager.LoaderCallbacks<PhotoBitmapLoaderInterface.BitmapResult>
  {
    private BitmapCallback() {}
    
    public Loader<PhotoBitmapLoaderInterface.BitmapResult> onCreateLoader(int paramInt, Bundle paramBundle)
    {
      String str = paramBundle.getString("image_uri");
      switch (paramInt)
      {
      default: 
        return null;
      case 2: 
        return PhotoViewActivity.this.onCreateBitmapLoader(2, paramBundle, str);
      }
      return PhotoViewActivity.this.onCreateBitmapLoader(1, paramBundle, str);
    }
    
    public void onLoadFinished(Loader<PhotoBitmapLoaderInterface.BitmapResult> paramLoader, PhotoBitmapLoaderInterface.BitmapResult paramBitmapResult)
    {
      Drawable localDrawable = paramBitmapResult.getDrawable(PhotoViewActivity.this.getResources());
      ActionBar localActionBar = PhotoViewActivity.this.getActionBar();
      switch (paramLoader.getId())
      {
      default: 
        return;
      case 2: 
        PhotoViewActivity.this.initTemporaryImage(localDrawable);
        PhotoViewActivity.this.getSupportLoaderManager().destroyLoader(2);
        return;
      }
      if (localDrawable == null)
      {
        localActionBar.setLogo(null);
        return;
      }
      localActionBar.setLogo(localDrawable);
    }
    
    public void onLoaderReset(Loader<PhotoBitmapLoaderInterface.BitmapResult> paramLoader) {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.PhotoViewActivity
 * JD-Core Version:    0.7.0.1
 */