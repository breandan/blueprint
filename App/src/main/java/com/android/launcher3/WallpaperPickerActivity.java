package com.android.launcher3;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.util.Pair;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.android.photos.BitmapRegionTileSource;
import com.android.photos.BitmapRegionTileSource.BitmapSource.State;
import com.android.photos.BitmapRegionTileSource.ResourceBitmapSource;
import com.android.photos.BitmapRegionTileSource.UriBitmapSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class WallpaperPickerActivity
  extends WallpaperCropActivity
{
  private ActionMode mActionMode;
  private ActionMode.Callback mActionModeCallback;
  private boolean mIgnoreNextTap;
  private WallpaperInfo mLiveWallpaperInfoOnPickerLaunch;
  private View.OnLongClickListener mLongClickListener;
  private SavedWallpaperImages mSavedImages;
  private View mSelectedTile;
  ArrayList<Uri> mTempWallpaperTiles = new ArrayList();
  private View.OnClickListener mThumbnailOnClickListener;
  private View mWallpaperStrip;
  private LinearLayout mWallpapersView;
  
  private void addLongPressHandler(View paramView)
  {
    paramView.setOnLongClickListener(this.mLongClickListener);
  }
  
  private void addTemporaryWallpaperTile(final Uri paramUri)
  {
    this.mTempWallpaperTiles.add(paramUri);
    FrameLayout localFrameLayout = (FrameLayout)getLayoutInflater().inflate(2130968920, this.mWallpapersView, false);
    localFrameLayout.setVisibility(8);
    setWallpaperItemPaddingToZero(localFrameLayout);
    this.mWallpapersView.addView(localFrameLayout, 0);
    final ImageView localImageView = (ImageView)localFrameLayout.findViewById(2131297228);
    new AsyncTask()
    {
      protected Bitmap doInBackground(Void... paramAnonymousVarArgs)
      {
        int i = WallpaperCropActivity.getRotationFromExif(jdField_this, paramUri);
        return WallpaperPickerActivity.createThumbnail(this.val$defaultSize, jdField_this, paramUri, null, null, 0, i, false);
      }
      
      protected void onPostExecute(Bitmap paramAnonymousBitmap)
      {
        if (paramAnonymousBitmap != null)
        {
          localImageView.setImageBitmap(paramAnonymousBitmap);
          localImageView.getDrawable().setDither(true);
          return;
        }
        Log.e("Launcher.WallpaperPickerActivity", "Error loading thumbnail for uri=" + paramUri);
      }
    }.execute(new Void[0]);
    UriWallpaperInfo localUriWallpaperInfo = new UriWallpaperInfo(paramUri);
    localFrameLayout.setTag(localUriWallpaperInfo);
    localUriWallpaperInfo.setView(localFrameLayout);
    addLongPressHandler(localFrameLayout);
    updateTileIndices();
    localFrameLayout.setOnClickListener(this.mThumbnailOnClickListener);
    this.mThumbnailOnClickListener.onClick(localFrameLayout);
  }
  
  private ArrayList<ResourceWallpaperInfo> addWallpapers(Resources paramResources, String paramString, int paramInt)
  {
    ArrayList localArrayList = new ArrayList(24);
    String[] arrayOfString = paramResources.getStringArray(paramInt);
    int i = arrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String str = arrayOfString[j];
      int k = paramResources.getIdentifier(str, "drawable", paramString);
      if (k != 0)
      {
        int m = paramResources.getIdentifier(str + "_small", "drawable", paramString);
        if (m != 0) {
          localArrayList.add(new ResourceWallpaperInfo(paramResources, k, paramResources.getDrawable(m)));
        }
      }
      for (;;)
      {
        j++;
        break;
        Log.e("Launcher.WallpaperPickerActivity", "Couldn't find wallpaper " + str);
      }
    }
    return localArrayList;
  }
  
  public static View createImageTileView(LayoutInflater paramLayoutInflater, int paramInt, View paramView, ViewGroup paramViewGroup, Drawable paramDrawable)
  {
    if (paramView == null) {}
    for (View localView = paramLayoutInflater.inflate(2130968920, paramViewGroup, false);; localView = paramView)
    {
      setWallpaperItemPaddingToZero((FrameLayout)localView);
      ImageView localImageView = (ImageView)localView.findViewById(2131297228);
      if (paramDrawable != null)
      {
        localImageView.setImageDrawable(paramDrawable);
        paramDrawable.setDither(true);
      }
      return localView;
    }
  }
  
  private static Bitmap createThumbnail(Point paramPoint, Context paramContext, Uri paramUri, byte[] paramArrayOfByte, Resources paramResources, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = paramPoint.x;
    int j = paramPoint.y;
    WallpaperCropActivity.BitmapCropTask localBitmapCropTask;
    if (paramUri != null) {
      localBitmapCropTask = new WallpaperCropActivity.BitmapCropTask(paramContext, paramUri, null, paramInt2, i, j, false, true, null);
    }
    Point localPoint;
    for (;;)
    {
      localPoint = localBitmapCropTask.getImageBounds();
      if ((localPoint != null) && (localPoint.x != 0) && (localPoint.y != 0)) {
        break;
      }
      return null;
      if (paramArrayOfByte != null) {
        localBitmapCropTask = new WallpaperCropActivity.BitmapCropTask(paramArrayOfByte, null, paramInt2, i, j, false, true, null);
      } else {
        localBitmapCropTask = new WallpaperCropActivity.BitmapCropTask(paramContext, paramResources, paramInt1, null, paramInt2, i, j, false, true, null);
      }
    }
    Matrix localMatrix = new Matrix();
    localMatrix.setRotate(paramInt2);
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = localPoint.x;
    arrayOfFloat[1] = localPoint.y;
    localMatrix.mapPoints(arrayOfFloat);
    arrayOfFloat[0] = Math.abs(arrayOfFloat[0]);
    arrayOfFloat[1] = Math.abs(arrayOfFloat[1]);
    localBitmapCropTask.setCropBounds(WallpaperCropActivity.getMaxCropRect((int)arrayOfFloat[0], (int)arrayOfFloat[1], i, j, paramBoolean));
    if (localBitmapCropTask.cropBitmap()) {
      return localBitmapCropTask.getCroppedBitmap();
    }
    return null;
  }
  
  private ArrayList<ResourceWallpaperInfo> findBundledWallpapers()
  {
    Object localObject = new ArrayList(24);
    Pair localPair = getWallpaperArrayResourceId();
    if (localPair != null) {}
    try
    {
      ArrayList localArrayList = addWallpapers(getPackageManager().getResourcesForApplication((ApplicationInfo)localPair.first), ((ApplicationInfo)localPair.first).packageName, ((Integer)localPair.second).intValue());
      localObject = localArrayList;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      label62:
      ResourceWallpaperInfo localResourceWallpaperInfo;
      break label62;
    }
    if (Build.VERSION.SDK_INT < 19)
    {
      localResourceWallpaperInfo = getPreKKDefaultWallpaperInfo();
      if (localResourceWallpaperInfo != null) {
        ((ArrayList)localObject).add(0, localResourceWallpaperInfo);
      }
    }
    return localObject;
  }
  
  private static Point getDefaultThumbnailSize(Resources paramResources)
  {
    return new Point(paramResources.getDimensionPixelSize(2131689512), paramResources.getDimensionPixelSize(2131689513));
  }
  
  private DefaultWallpaperInfo getDefaultWallpaper()
  {
    File localFile = new File(getFilesDir(), "default_thumb2.jpg");
    Bitmap localBitmap;
    boolean bool;
    if (localFile.exists())
    {
      localBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
      bool = true;
    }
    while (bool)
    {
      return new DefaultWallpaperInfo(new BitmapDrawable(localBitmap));
      new File(getFilesDir(), "default_thumb.jpg").delete();
      Point localPoint = getDefaultThumbnailSize(getResources());
      Drawable localDrawable = WallpaperManager.getInstance(this).getBuiltInDrawable(localPoint.x, localPoint.y, true, 0.5F, 0.5F);
      localBitmap = null;
      if (localDrawable != null)
      {
        localBitmap = Bitmap.createBitmap(localPoint.x, localPoint.y, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap);
        localDrawable.setBounds(0, 0, localPoint.x, localPoint.y);
        localDrawable.draw(localCanvas);
        localCanvas.setBitmap(null);
      }
      bool = false;
      if (localBitmap != null) {
        bool = writeImageToFileAsJpeg(localFile, localBitmap);
      }
    }
    return null;
  }
  
  private ResourceWallpaperInfo getPreKKDefaultWallpaperInfo()
  {
    Resources localResources1 = Resources.getSystem();
    int i = localResources1.getIdentifier("default_wallpaper", "drawable", "android");
    File localFile = new File(getFilesDir(), "default_thumb2.jpg");
    Bitmap localBitmap;
    boolean bool;
    if (localFile.exists())
    {
      localBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
      bool = true;
    }
    for (;;)
    {
      ResourceWallpaperInfo localResourceWallpaperInfo = null;
      if (bool) {
        localResourceWallpaperInfo = new ResourceWallpaperInfo(localResources1, i, new BitmapDrawable(localBitmap));
      }
      return localResourceWallpaperInfo;
      Resources localResources2 = getResources();
      localBitmap = createThumbnail(getDefaultThumbnailSize(localResources2), this, null, null, localResources1, i, WallpaperCropActivity.getRotationFromExif(localResources2, i), false);
      bool = false;
      if (localBitmap != null) {
        bool = writeImageToFileAsJpeg(localFile, localBitmap);
      }
    }
  }
  
  private void initializeScrollForRtl()
  {
    final HorizontalScrollView localHorizontalScrollView = (HorizontalScrollView)findViewById(2131297223);
    if (localHorizontalScrollView.getLayoutDirection() == 1) {
      localHorizontalScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
      {
        public void onGlobalLayout()
        {
          LinearLayout localLinearLayout = (LinearLayout)WallpaperPickerActivity.this.findViewById(2131297224);
          localHorizontalScrollView.scrollTo(localLinearLayout.getWidth(), 0);
          localHorizontalScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      });
    }
  }
  
  private void populateWallpapersFromAdapter(ViewGroup paramViewGroup, BaseAdapter paramBaseAdapter, boolean paramBoolean)
  {
    for (int i = 0; i < paramBaseAdapter.getCount(); i++)
    {
      FrameLayout localFrameLayout = (FrameLayout)paramBaseAdapter.getView(i, null, paramViewGroup);
      paramViewGroup.addView(localFrameLayout, i);
      WallpaperTileInfo localWallpaperTileInfo = (WallpaperTileInfo)paramBaseAdapter.getItem(i);
      localFrameLayout.setTag(localWallpaperTileInfo);
      localWallpaperTileInfo.setView(localFrameLayout);
      if (paramBoolean) {
        addLongPressHandler(localFrameLayout);
      }
      localFrameLayout.setOnClickListener(this.mThumbnailOnClickListener);
    }
  }
  
  private void selectTile(View paramView)
  {
    if (this.mSelectedTile != null)
    {
      this.mSelectedTile.setSelected(false);
      this.mSelectedTile = null;
    }
    this.mSelectedTile = paramView;
    paramView.setSelected(true);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramView.getContentDescription();
    paramView.announceForAccessibility(getString(2131361867, arrayOfObject));
  }
  
  static void setWallpaperItemPaddingToZero(FrameLayout paramFrameLayout)
  {
    paramFrameLayout.setPadding(0, 0, 0, 0);
    paramFrameLayout.setForeground(new ZeroPaddingDrawable(paramFrameLayout.getForeground()));
  }
  
  private void updateTileIndices()
  {
    LinearLayout localLinearLayout1 = (LinearLayout)findViewById(2131297224);
    int i = localLinearLayout1.getChildCount();
    Resources localResources = getResources();
    int j = 0;
    for (int k = 0; k < 2; k++)
    {
      int m = 0;
      for (int n = 0; n < i; n++)
      {
        View localView = localLinearLayout1.getChildAt(n);
        LinearLayout localLinearLayout2;
        int i2;
        int i1;
        int i3;
        label81:
        WallpaperTileInfo localWallpaperTileInfo;
        if ((localView.getTag() instanceof WallpaperTileInfo))
        {
          localLinearLayout2 = localLinearLayout1;
          i2 = n;
          i1 = n + 1;
          i3 = i2;
          if (i3 >= i1) {
            continue;
          }
          localWallpaperTileInfo = (WallpaperTileInfo)localLinearLayout2.getChildAt(i3).getTag();
          if (localWallpaperTileInfo.isNamelessWallpaper())
          {
            if (k != 0) {
              break label145;
            }
            j++;
          }
        }
        for (;;)
        {
          i3++;
          break label81;
          localLinearLayout2 = (LinearLayout)localView;
          i1 = localLinearLayout2.getChildCount();
          i2 = 0;
          break;
          label145:
          Object[] arrayOfObject = new Object[2];
          m++;
          arrayOfObject[0] = Integer.valueOf(m);
          arrayOfObject[1] = Integer.valueOf(j);
          localWallpaperTileInfo.onIndexUpdated(localResources.getString(2131361866, arrayOfObject));
        }
      }
    }
  }
  
  private boolean writeImageToFileAsJpeg(File paramFile, Bitmap paramBitmap)
  {
    try
    {
      paramFile.createNewFile();
      FileOutputStream localFileOutputStream = openFileOutput(paramFile.getName(), 0);
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 95, localFileOutputStream);
      localFileOutputStream.close();
      return true;
    }
    catch (IOException localIOException)
    {
      Log.e("Launcher.WallpaperPickerActivity", "Error while writing bitmap to file " + localIOException);
      paramFile.delete();
    }
    return false;
  }
  
  public boolean enableRotation()
  {
    return (super.enableRotation()) || (Launcher.sForceEnableRotation);
  }
  
  public CropView getCropView()
  {
    return this.mCropView;
  }
  
  public SavedWallpaperImages getSavedImages()
  {
    return this.mSavedImages;
  }
  
  protected Bitmap getThumbnailOfLastPhoto()
  {
    Cursor localCursor = MediaStore.Images.Media.query(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { "_id", "datetaken" }, null, null, "datetaken DESC LIMIT 1");
    boolean bool = localCursor.moveToNext();
    Bitmap localBitmap = null;
    if (bool)
    {
      int i = localCursor.getInt(0);
      localBitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), i, 1, null);
    }
    localCursor.close();
    return localBitmap;
  }
  
  public Pair<ApplicationInfo, Integer> getWallpaperArrayResourceId()
  {
    String str = getResources().getResourcePackageName(2131492872);
    try
    {
      Pair localPair = new Pair(getPackageManager().getApplicationInfo(str, 0), Integer.valueOf(2131492872));
      return localPair;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    return null;
  }
  
  protected void init()
  {
    setContentView(2130968918);
    this.mCropView = ((CropView)findViewById(2131297220));
    this.mWallpaperStrip = findViewById(2131297222);
    CropView localCropView1 = this.mCropView;
    CropView.TouchCallback local1 = new CropView.TouchCallback()
    {
      LauncherViewPropertyAnimator mAnim;
      
      public void onTap()
      {
        boolean bool = WallpaperPickerActivity.this.mIgnoreNextTap;
        WallpaperPickerActivity.access$502(WallpaperPickerActivity.this, false);
        if (!bool)
        {
          if (this.mAnim != null) {
            this.mAnim.cancel();
          }
          WallpaperPickerActivity.this.mWallpaperStrip.setVisibility(0);
          this.mAnim = new LauncherViewPropertyAnimator(WallpaperPickerActivity.this.mWallpaperStrip);
          this.mAnim.alpha(1.0F).setDuration(150L).setInterpolator(new DecelerateInterpolator(0.75F));
          this.mAnim.start();
        }
      }
      
      public void onTouchDown()
      {
        if (this.mAnim != null) {
          this.mAnim.cancel();
        }
        if (WallpaperPickerActivity.this.mWallpaperStrip.getAlpha() == 1.0F) {
          WallpaperPickerActivity.access$502(WallpaperPickerActivity.this, true);
        }
        this.mAnim = new LauncherViewPropertyAnimator(WallpaperPickerActivity.this.mWallpaperStrip);
        this.mAnim.alpha(0.0F).setDuration(150L).addListener(new Animator.AnimatorListener()
        {
          public void onAnimationCancel(Animator paramAnonymous2Animator) {}
          
          public void onAnimationEnd(Animator paramAnonymous2Animator)
          {
            WallpaperPickerActivity.this.mWallpaperStrip.setVisibility(4);
          }
          
          public void onAnimationRepeat(Animator paramAnonymous2Animator) {}
          
          public void onAnimationStart(Animator paramAnonymous2Animator) {}
        });
        this.mAnim.setInterpolator(new AccelerateInterpolator(0.75F));
        this.mAnim.start();
      }
      
      public void onTouchUp()
      {
        WallpaperPickerActivity.access$502(WallpaperPickerActivity.this, false);
      }
    };
    localCropView1.setTouchCallback(local1);
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (WallpaperPickerActivity.this.mActionMode != null)
        {
          if (paramAnonymousView.isLongClickable()) {
            WallpaperPickerActivity.this.mLongClickListener.onLongClick(paramAnonymousView);
          }
          return;
        }
        WallpaperPickerActivity.WallpaperTileInfo localWallpaperTileInfo = (WallpaperPickerActivity.WallpaperTileInfo)paramAnonymousView.getTag();
        if ((localWallpaperTileInfo.isSelectable()) && (paramAnonymousView.getVisibility() == 0)) {
          WallpaperPickerActivity.this.selectTile(paramAnonymousView);
        }
        localWallpaperTileInfo.onClick(WallpaperPickerActivity.this);
      }
    };
    this.mThumbnailOnClickListener = local2;
    View.OnLongClickListener local3 = new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramAnonymousView)
      {
        ((CheckableFrameLayout)paramAnonymousView).toggle();
        if (WallpaperPickerActivity.this.mActionMode != null) {
          WallpaperPickerActivity.this.mActionMode.invalidate();
        }
        for (;;)
        {
          return true;
          WallpaperPickerActivity.access$602(WallpaperPickerActivity.this, WallpaperPickerActivity.this.startActionMode(WallpaperPickerActivity.this.mActionModeCallback));
          int i = WallpaperPickerActivity.this.mWallpapersView.getChildCount();
          for (int j = 0; j < i; j++) {
            WallpaperPickerActivity.this.mWallpapersView.getChildAt(j).setSelected(false);
          }
        }
      }
    };
    this.mLongClickListener = local3;
    ArrayList localArrayList = findBundledWallpapers();
    this.mWallpapersView = ((LinearLayout)findViewById(2131297225));
    BuiltInWallpapersAdapter localBuiltInWallpapersAdapter = new BuiltInWallpapersAdapter(this, localArrayList);
    populateWallpapersFromAdapter(this.mWallpapersView, localBuiltInWallpapersAdapter, false);
    SavedWallpaperImages localSavedWallpaperImages = new SavedWallpaperImages(this);
    this.mSavedImages = localSavedWallpaperImages;
    this.mSavedImages.loadThumbnailsAndImageIdList();
    populateWallpapersFromAdapter(this.mWallpapersView, this.mSavedImages, true);
    final LinearLayout localLinearLayout1 = (LinearLayout)findViewById(2131297226);
    final LiveWallpaperListAdapter localLiveWallpaperListAdapter = new LiveWallpaperListAdapter(this);
    DataSetObserver local4 = new DataSetObserver()
    {
      public void onChanged()
      {
        localLinearLayout1.removeAllViews();
        WallpaperPickerActivity.this.populateWallpapersFromAdapter(localLinearLayout1, localLiveWallpaperListAdapter, false);
        WallpaperPickerActivity.this.initializeScrollForRtl();
        WallpaperPickerActivity.this.updateTileIndices();
      }
    };
    localLiveWallpaperListAdapter.registerDataSetObserver(local4);
    LinearLayout localLinearLayout2 = (LinearLayout)findViewById(2131297227);
    ThirdPartyWallpaperPickerListAdapter localThirdPartyWallpaperPickerListAdapter = new ThirdPartyWallpaperPickerListAdapter(this);
    populateWallpapersFromAdapter(localLinearLayout2, localThirdPartyWallpaperPickerListAdapter, false);
    LinearLayout localLinearLayout3 = (LinearLayout)findViewById(2131297224);
    FrameLayout localFrameLayout1 = (FrameLayout)getLayoutInflater().inflate(2130968919, localLinearLayout3, false);
    setWallpaperItemPaddingToZero(localFrameLayout1);
    localLinearLayout3.addView(localFrameLayout1, 0);
    if (getThumbnailOfLastPhoto() != null)
    {
      ImageView localImageView = (ImageView)localFrameLayout1.findViewById(2131297228);
      localImageView.setImageBitmap(getThumbnailOfLastPhoto());
      localImageView.setColorFilter(getResources().getColor(2131230766), PorterDuff.Mode.SRC_ATOP);
    }
    PickImageInfo localPickImageInfo = new PickImageInfo();
    localFrameLayout1.setTag(localPickImageInfo);
    localPickImageInfo.setView(localFrameLayout1);
    localFrameLayout1.setOnClickListener(this.mThumbnailOnClickListener);
    if (Build.VERSION.SDK_INT >= 19)
    {
      DefaultWallpaperInfo localDefaultWallpaperInfo = getDefaultWallpaper();
      FrameLayout localFrameLayout2 = (FrameLayout)createImageTileView(getLayoutInflater(), 0, null, this.mWallpapersView, localDefaultWallpaperInfo.mThumb);
      setWallpaperItemPaddingToZero(localFrameLayout2);
      localFrameLayout2.setTag(localDefaultWallpaperInfo);
      this.mWallpapersView.addView(localFrameLayout2, 0);
      localFrameLayout2.setOnClickListener(this.mThumbnailOnClickListener);
      localDefaultWallpaperInfo.setView(localFrameLayout2);
    }
    CropView localCropView2 = this.mCropView;
    View.OnLayoutChangeListener local5 = new View.OnLayoutChangeListener()
    {
      public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
      {
        if ((paramAnonymousInt3 - paramAnonymousInt1 > 0) && (paramAnonymousInt4 - paramAnonymousInt2 > 0))
        {
          if (WallpaperPickerActivity.this.mWallpapersView.getChildCount() > 0) {
            WallpaperPickerActivity.this.mThumbnailOnClickListener.onClick(WallpaperPickerActivity.this.mWallpapersView.getChildAt(0));
          }
          paramAnonymousView.removeOnLayoutChangeListener(this);
        }
      }
    };
    localCropView2.addOnLayoutChangeListener(local5);
    updateTileIndices();
    initializeScrollForRtl();
    LayoutTransition localLayoutTransition = new LayoutTransition();
    localLayoutTransition.setDuration(200L);
    localLayoutTransition.setStartDelay(1, 0L);
    localLayoutTransition.setAnimator(3, null);
    this.mWallpapersView.setLayoutTransition(localLayoutTransition);
    ActionBar localActionBar = getActionBar();
    localActionBar.setCustomView(2130968579);
    View localView = localActionBar.getCustomView();
    View.OnClickListener local6 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (WallpaperPickerActivity.this.mSelectedTile != null) {
          ((WallpaperPickerActivity.WallpaperTileInfo)WallpaperPickerActivity.this.mSelectedTile.getTag()).onSave(WallpaperPickerActivity.this);
        }
      }
    };
    localView.setOnClickListener(local6);
    ActionMode.Callback local7 = new ActionMode.Callback()
    {
      private int numCheckedItems()
      {
        int i = WallpaperPickerActivity.this.mWallpapersView.getChildCount();
        int j = 0;
        for (int k = 0; k < i; k++) {
          if (((CheckableFrameLayout)WallpaperPickerActivity.this.mWallpapersView.getChildAt(k)).isChecked()) {
            j++;
          }
        }
        return j;
      }
      
      public boolean onActionItemClicked(ActionMode paramAnonymousActionMode, MenuItem paramAnonymousMenuItem)
      {
        if (paramAnonymousMenuItem.getItemId() == 2131297281)
        {
          int i = WallpaperPickerActivity.this.mWallpapersView.getChildCount();
          ArrayList localArrayList = new ArrayList();
          for (int j = 0; j < i; j++)
          {
            CheckableFrameLayout localCheckableFrameLayout = (CheckableFrameLayout)WallpaperPickerActivity.this.mWallpapersView.getChildAt(j);
            if (localCheckableFrameLayout.isChecked())
            {
              ((WallpaperPickerActivity.WallpaperTileInfo)localCheckableFrameLayout.getTag()).onDelete(WallpaperPickerActivity.this);
              localArrayList.add(localCheckableFrameLayout);
            }
          }
          Iterator localIterator = localArrayList.iterator();
          while (localIterator.hasNext())
          {
            View localView = (View)localIterator.next();
            WallpaperPickerActivity.this.mWallpapersView.removeView(localView);
          }
          WallpaperPickerActivity.this.updateTileIndices();
          paramAnonymousActionMode.finish();
          return true;
        }
        return false;
      }
      
      public boolean onCreateActionMode(ActionMode paramAnonymousActionMode, Menu paramAnonymousMenu)
      {
        paramAnonymousActionMode.getMenuInflater().inflate(2131886081, paramAnonymousMenu);
        return true;
      }
      
      public void onDestroyActionMode(ActionMode paramAnonymousActionMode)
      {
        int i = WallpaperPickerActivity.this.mWallpapersView.getChildCount();
        for (int j = 0; j < i; j++) {
          ((CheckableFrameLayout)WallpaperPickerActivity.this.mWallpapersView.getChildAt(j)).setChecked(false);
        }
        WallpaperPickerActivity.this.mSelectedTile.setSelected(true);
        WallpaperPickerActivity.access$602(WallpaperPickerActivity.this, null);
      }
      
      public boolean onPrepareActionMode(ActionMode paramAnonymousActionMode, Menu paramAnonymousMenu)
      {
        int i = numCheckedItems();
        if (i == 0)
        {
          paramAnonymousActionMode.finish();
          return true;
        }
        Resources localResources = WallpaperPickerActivity.this.getResources();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(i);
        paramAnonymousActionMode.setTitle(localResources.getQuantityString(2131558408, i, arrayOfObject));
        return true;
      }
    };
    this.mActionModeCallback = local7;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 5) && (paramInt2 == -1)) {
      if ((paramIntent != null) && (paramIntent.getData() != null)) {
        addTemporaryWallpaperTile(paramIntent.getData());
      }
    }
    WallpaperInfo localWallpaperInfo1;
    WallpaperInfo localWallpaperInfo2;
    do
    {
      do
      {
        return;
        if (paramInt1 == 6)
        {
          setResult(-1);
          finish();
          return;
        }
      } while (paramInt1 != 7);
      WallpaperManager localWallpaperManager = WallpaperManager.getInstance(this);
      localWallpaperInfo1 = this.mLiveWallpaperInfoOnPickerLaunch;
      localWallpaperInfo2 = localWallpaperManager.getWallpaperInfo();
    } while ((localWallpaperInfo2 == null) || ((localWallpaperInfo1 != null) && (localWallpaperInfo1.getComponent().equals(localWallpaperInfo2.getComponent()))));
    setResult(-1);
    finish();
  }
  
  public void onLiveWallpaperPickerLaunch()
  {
    this.mLiveWallpaperInfoOnPickerLaunch = WallpaperManager.getInstance(this).getWallpaperInfo();
  }
  
  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    Iterator localIterator = paramBundle.getParcelableArrayList("TEMP_WALLPAPER_TILES").iterator();
    while (localIterator.hasNext()) {
      addTemporaryWallpaperTile((Uri)localIterator.next());
    }
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelableArrayList("TEMP_WALLPAPER_TILES", this.mTempWallpaperTiles);
  }
  
  protected void onStop()
  {
    super.onStop();
    this.mWallpaperStrip = findViewById(2131297222);
    if (this.mWallpaperStrip.getAlpha() < 1.0F)
    {
      this.mWallpaperStrip.setAlpha(1.0F);
      this.mWallpaperStrip.setVisibility(0);
    }
  }
  
  public void setWallpaperStripYOffset(float paramFloat)
  {
    this.mWallpaperStrip.setPadding(0, 0, 0, (int)paramFloat);
  }
  
  private static class BuiltInWallpapersAdapter
    extends BaseAdapter
    implements ListAdapter
  {
    private LayoutInflater mLayoutInflater;
    private ArrayList<WallpaperPickerActivity.ResourceWallpaperInfo> mWallpapers;
    
    BuiltInWallpapersAdapter(Activity paramActivity, ArrayList<WallpaperPickerActivity.ResourceWallpaperInfo> paramArrayList)
    {
      this.mLayoutInflater = paramActivity.getLayoutInflater();
      this.mWallpapers = paramArrayList;
    }
    
    public int getCount()
    {
      return this.mWallpapers.size();
    }
    
    public WallpaperPickerActivity.ResourceWallpaperInfo getItem(int paramInt)
    {
      return (WallpaperPickerActivity.ResourceWallpaperInfo)this.mWallpapers.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Drawable localDrawable = WallpaperPickerActivity.ResourceWallpaperInfo.access$1500((WallpaperPickerActivity.ResourceWallpaperInfo)this.mWallpapers.get(paramInt));
      if (localDrawable == null) {
        Log.e("Launcher.WallpaperPickerActivity", "Error decoding thumbnail for wallpaper #" + paramInt);
      }
      return WallpaperPickerActivity.createImageTileView(this.mLayoutInflater, paramInt, paramView, paramViewGroup, localDrawable);
    }
  }
  
  public static class DefaultWallpaperInfo
    extends WallpaperPickerActivity.WallpaperTileInfo
  {
    public Drawable mThumb;
    
    public DefaultWallpaperInfo(Drawable paramDrawable)
    {
      this.mThumb = paramDrawable;
    }
    
    public boolean isNamelessWallpaper()
    {
      return true;
    }
    
    public boolean isSelectable()
    {
      return true;
    }
    
    public void onClick(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      CropView localCropView = paramWallpaperPickerActivity.getCropView();
      localCropView.setTileSource(new DrawableTileSource(paramWallpaperPickerActivity, WallpaperManager.getInstance(paramWallpaperPickerActivity).getBuiltInDrawable(localCropView.getWidth(), localCropView.getHeight(), false, 0.5F, 0.5F), 1024), null);
      localCropView.setScale(1.0F);
      localCropView.setTouchEnabled(false);
    }
    
    public void onSave(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      try
      {
        WallpaperManager.getInstance(paramWallpaperPickerActivity).clear();
        paramWallpaperPickerActivity.finish();
        return;
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          Log.w("Setting wallpaper to default threw exception", localIOException);
        }
      }
    }
  }
  
  public static class PickImageInfo
    extends WallpaperPickerActivity.WallpaperTileInfo
  {
    public void onClick(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
      localIntent.setType("image/*");
      Utilities.startActivityForResultSafely(paramWallpaperPickerActivity, localIntent, 5);
    }
  }
  
  public static class ResourceWallpaperInfo
    extends WallpaperPickerActivity.WallpaperTileInfo
  {
    private int mResId;
    private Resources mResources;
    private Drawable mThumb;
    
    public ResourceWallpaperInfo(Resources paramResources, int paramInt, Drawable paramDrawable)
    {
      this.mResources = paramResources;
      this.mResId = paramInt;
      this.mThumb = paramDrawable;
    }
    
    public boolean isNamelessWallpaper()
    {
      return true;
    }
    
    public boolean isSelectable()
    {
      return true;
    }
    
    public void onClick(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      BitmapRegionTileSource.ResourceBitmapSource localResourceBitmapSource = new BitmapRegionTileSource.ResourceBitmapSource(this.mResources, this.mResId, 1024);
      localResourceBitmapSource.loadInBackground();
      BitmapRegionTileSource localBitmapRegionTileSource = new BitmapRegionTileSource(paramWallpaperPickerActivity, localResourceBitmapSource);
      CropView localCropView = paramWallpaperPickerActivity.getCropView();
      localCropView.setTileSource(localBitmapRegionTileSource, null);
      Point localPoint = WallpaperCropActivity.getDefaultWallpaperSize(paramWallpaperPickerActivity.getResources(), paramWallpaperPickerActivity.getWindowManager());
      RectF localRectF = WallpaperCropActivity.getMaxCropRect(localBitmapRegionTileSource.getImageWidth(), localBitmapRegionTileSource.getImageHeight(), localPoint.x, localPoint.y, false);
      localCropView.setScale(localPoint.x / localRectF.width());
      localCropView.setTouchEnabled(false);
    }
    
    public void onSave(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      paramWallpaperPickerActivity.cropImageAndSetWallpaper(this.mResources, this.mResId, true);
    }
  }
  
  public static class UriWallpaperInfo
    extends WallpaperPickerActivity.WallpaperTileInfo
  {
    private BitmapRegionTileSource.UriBitmapSource mBitmapSource;
    private boolean mFirstClick = true;
    private Uri mUri;
    
    public UriWallpaperInfo(Uri paramUri)
    {
      this.mUri = paramUri;
    }
    
    public boolean isNamelessWallpaper()
    {
      return true;
    }
    
    public boolean isSelectable()
    {
      return true;
    }
    
    public void onClick(final WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      if (!this.mFirstClick) {}
      for (Object localObject = null;; localObject = new Runnable()
          {
            public void run()
            {
              if ((WallpaperPickerActivity.UriWallpaperInfo.this.mBitmapSource != null) && (WallpaperPickerActivity.UriWallpaperInfo.this.mBitmapSource.getLoadingState() == BitmapRegionTileSource.BitmapSource.State.LOADED))
              {
                WallpaperPickerActivity.UriWallpaperInfo.this.mView.setVisibility(0);
                paramWallpaperPickerActivity.selectTile(WallpaperPickerActivity.UriWallpaperInfo.this.mView);
              }
              ViewGroup localViewGroup;
              do
              {
                return;
                localViewGroup = (ViewGroup)WallpaperPickerActivity.UriWallpaperInfo.this.mView.getParent();
              } while (localViewGroup == null);
              localViewGroup.removeView(WallpaperPickerActivity.UriWallpaperInfo.this.mView);
              Toast.makeText(paramWallpaperPickerActivity, paramWallpaperPickerActivity.getString(2131361864), 0).show();
            }
          })
      {
        this.mBitmapSource = new BitmapRegionTileSource.UriBitmapSource(paramWallpaperPickerActivity, this.mUri, 1024);
        paramWallpaperPickerActivity.setCropViewTileSource(this.mBitmapSource, true, false, (Runnable)localObject);
        return;
        this.mFirstClick = false;
      }
    }
    
    public void onSave(final WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      WallpaperCropActivity.OnBitmapCroppedHandler local2 = new WallpaperCropActivity.OnBitmapCroppedHandler()
      {
        public void onBitmapCropped(byte[] paramAnonymousArrayOfByte)
        {
          Bitmap localBitmap = WallpaperPickerActivity.createThumbnail(WallpaperPickerActivity.access$200(paramWallpaperPickerActivity.getResources()), null, null, paramAnonymousArrayOfByte, null, 0, 0, true);
          paramWallpaperPickerActivity.getSavedImages().writeImage(localBitmap, paramAnonymousArrayOfByte);
        }
      };
      paramWallpaperPickerActivity.cropImageAndSetWallpaper(this.mUri, local2, true);
    }
  }
  
  public static abstract class WallpaperTileInfo
  {
    protected View mView;
    
    public boolean isNamelessWallpaper()
    {
      return false;
    }
    
    public boolean isSelectable()
    {
      return false;
    }
    
    public void onClick(WallpaperPickerActivity paramWallpaperPickerActivity) {}
    
    public void onDelete(WallpaperPickerActivity paramWallpaperPickerActivity) {}
    
    public void onIndexUpdated(CharSequence paramCharSequence)
    {
      if (isNamelessWallpaper()) {
        this.mView.setContentDescription(paramCharSequence);
      }
    }
    
    public void onSave(WallpaperPickerActivity paramWallpaperPickerActivity) {}
    
    public void setView(View paramView)
    {
      this.mView = paramView;
    }
  }
  
  static class ZeroPaddingDrawable
    extends LevelListDrawable
  {
    public ZeroPaddingDrawable(Drawable paramDrawable)
    {
      addLevel(0, 0, paramDrawable);
      setLevel(0);
    }
    
    public boolean getPadding(Rect paramRect)
    {
      paramRect.set(0, 0, 0, 0);
      return true;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.WallpaperPickerActivity
 * JD-Core Version:    0.7.0.1
 */