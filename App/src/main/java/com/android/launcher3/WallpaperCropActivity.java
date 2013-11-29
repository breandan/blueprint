package com.android.launcher3;

import android.app.ActionBar;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;
import com.android.gallery3d.common.Utils;
import com.android.photos.BitmapRegionTileSource;
import com.android.photos.BitmapRegionTileSource.BitmapSource;
import com.android.photos.BitmapRegionTileSource.BitmapSource.State;
import com.android.photos.BitmapRegionTileSource.UriBitmapSource;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class WallpaperCropActivity
  extends Activity
{
  protected CropView mCropView;
  
  protected static Bitmap.CompressFormat convertExtensionToCompressFormat(String paramString)
  {
    if (paramString.equals("png")) {
      return Bitmap.CompressFormat.PNG;
    }
    return Bitmap.CompressFormat.JPEG;
  }
  
  protected static Point getDefaultWallpaperSize(Resources paramResources, WindowManager paramWindowManager)
  {
    Point localPoint1 = new Point();
    Point localPoint2 = new Point();
    paramWindowManager.getDefaultDisplay().getCurrentSizeRange(localPoint1, localPoint2);
    int i = Math.max(localPoint2.x, localPoint2.y);
    int j = Math.max(localPoint1.x, localPoint1.y);
    if (Build.VERSION.SDK_INT >= 17)
    {
      Point localPoint3 = new Point();
      paramWindowManager.getDefaultDisplay().getRealSize(localPoint3);
      i = Math.max(localPoint3.x, localPoint3.y);
      j = Math.min(localPoint3.x, localPoint3.y);
    }
    int k;
    if (isScreenLarge(paramResources)) {
      k = (int)(i * wallpaperTravelToScreenWidthRatio(i, j));
    }
    for (int m = i;; m = i)
    {
      return new Point(k, m);
      k = Math.max((int)(2.0F * j), i);
    }
  }
  
  protected static String getFileExtension(String paramString)
  {
    if (paramString == null) {}
    for (String str1 = "jpg";; str1 = paramString)
    {
      String str2 = str1.toLowerCase();
      if ((!str2.equals("png")) && (!str2.equals("gif"))) {
        break;
      }
      return "png";
    }
    return "jpg";
  }
  
  protected static RectF getMaxCropRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    RectF localRectF = new RectF();
    if (paramInt1 / paramInt2 > paramInt3 / paramInt4)
    {
      localRectF.top = 0.0F;
      localRectF.bottom = paramInt2;
      localRectF.left = ((paramInt1 - paramInt3 / paramInt4 * paramInt2) / 2.0F);
      localRectF.right = (paramInt1 - localRectF.left);
      if (paramBoolean)
      {
        localRectF.right -= localRectF.left;
        localRectF.left = 0.0F;
      }
      return localRectF;
    }
    localRectF.left = 0.0F;
    localRectF.right = paramInt1;
    localRectF.top = ((paramInt2 - paramInt4 / paramInt3 * paramInt1) / 2.0F);
    localRectF.bottom = (paramInt2 - localRectF.top);
    return localRectF;
  }
  
  public static int getRotationFromExif(Context paramContext, Uri paramUri)
  {
    return getRotationFromExifHelper(null, null, 0, paramContext, paramUri);
  }
  
  public static int getRotationFromExif(Resources paramResources, int paramInt)
  {
    return getRotationFromExifHelper(null, paramResources, paramInt, null, null);
  }
  
  public static int getRotationFromExif(String paramString)
  {
    return getRotationFromExifHelper(paramString, null, 0, null, null);
  }
  
  /* Error */
  private static int getRotationFromExifHelper(String paramString, Resources paramResources, int paramInt, Context paramContext, Uri paramUri)
  {
    // Byte code:
    //   0: new 122	com/android/gallery3d/exif/ExifInterface
    //   3: dup
    //   4: invokespecial 123	com/android/gallery3d/exif/ExifInterface:<init>	()V
    //   7: astore 5
    //   9: aconst_null
    //   10: astore 6
    //   12: aconst_null
    //   13: astore 7
    //   15: aload_0
    //   16: ifnull +47 -> 63
    //   19: aload 5
    //   21: aload_0
    //   22: invokevirtual 127	com/android/gallery3d/exif/ExifInterface:readExif	(Ljava/lang/String;)V
    //   25: aload 5
    //   27: getstatic 130	com/android/gallery3d/exif/ExifInterface:TAG_ORIENTATION	I
    //   30: invokevirtual 134	com/android/gallery3d/exif/ExifInterface:getTagIntValue	(I)Ljava/lang/Integer;
    //   33: astore 12
    //   35: aload 12
    //   37: ifnull +99 -> 136
    //   40: aload 12
    //   42: invokevirtual 140	java/lang/Integer:shortValue	()S
    //   45: invokestatic 144	com/android/gallery3d/exif/ExifInterface:getRotationForOrientationValue	(S)I
    //   48: istore 13
    //   50: aload 7
    //   52: invokestatic 150	com/android/gallery3d/common/Utils:closeSilently	(Ljava/io/Closeable;)V
    //   55: aload 6
    //   57: invokestatic 150	com/android/gallery3d/common/Utils:closeSilently	(Ljava/io/Closeable;)V
    //   60: iload 13
    //   62: ireturn
    //   63: aload 4
    //   65: ifnull +39 -> 104
    //   68: aload_3
    //   69: invokevirtual 156	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   72: aload 4
    //   74: invokevirtual 162	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
    //   77: astore 6
    //   79: new 164	java/io/BufferedInputStream
    //   82: dup
    //   83: aload 6
    //   85: invokespecial 167	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   88: astore 11
    //   90: aload 5
    //   92: aload 11
    //   94: invokevirtual 169	com/android/gallery3d/exif/ExifInterface:readExif	(Ljava/io/InputStream;)V
    //   97: aload 11
    //   99: astore 7
    //   101: goto -76 -> 25
    //   104: aload_1
    //   105: iload_2
    //   106: invokevirtual 175	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   109: astore 6
    //   111: new 164	java/io/BufferedInputStream
    //   114: dup
    //   115: aload 6
    //   117: invokespecial 167	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   120: astore 11
    //   122: aload 5
    //   124: aload 11
    //   126: invokevirtual 169	com/android/gallery3d/exif/ExifInterface:readExif	(Ljava/io/InputStream;)V
    //   129: aload 11
    //   131: astore 7
    //   133: goto -108 -> 25
    //   136: aload 7
    //   138: invokestatic 150	com/android/gallery3d/common/Utils:closeSilently	(Ljava/io/Closeable;)V
    //   141: aload 6
    //   143: invokestatic 150	com/android/gallery3d/common/Utils:closeSilently	(Ljava/io/Closeable;)V
    //   146: iconst_0
    //   147: ireturn
    //   148: astore 9
    //   150: ldc 177
    //   152: ldc 179
    //   154: aload 9
    //   156: invokestatic 185	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   159: pop
    //   160: aload 7
    //   162: invokestatic 150	com/android/gallery3d/common/Utils:closeSilently	(Ljava/io/Closeable;)V
    //   165: aload 6
    //   167: invokestatic 150	com/android/gallery3d/common/Utils:closeSilently	(Ljava/io/Closeable;)V
    //   170: goto -24 -> 146
    //   173: astore 8
    //   175: aload 7
    //   177: invokestatic 150	com/android/gallery3d/common/Utils:closeSilently	(Ljava/io/Closeable;)V
    //   180: aload 6
    //   182: invokestatic 150	com/android/gallery3d/common/Utils:closeSilently	(Ljava/io/Closeable;)V
    //   185: aload 8
    //   187: athrow
    //   188: astore 8
    //   190: aload 11
    //   192: astore 7
    //   194: goto -19 -> 175
    //   197: astore 9
    //   199: aload 11
    //   201: astore 7
    //   203: goto -53 -> 150
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	206	0	paramString	String
    //   0	206	1	paramResources	Resources
    //   0	206	2	paramInt	int
    //   0	206	3	paramContext	Context
    //   0	206	4	paramUri	Uri
    //   7	116	5	localExifInterface	com.android.gallery3d.exif.ExifInterface
    //   10	171	6	localObject1	Object
    //   13	189	7	localObject2	Object
    //   173	13	8	localObject3	Object
    //   188	1	8	localObject4	Object
    //   148	7	9	localIOException1	IOException
    //   197	1	9	localIOException2	IOException
    //   88	112	11	localBufferedInputStream	BufferedInputStream
    //   33	8	12	localInteger	java.lang.Integer
    //   48	13	13	i	int
    // Exception table:
    //   from	to	target	type
    //   19	25	148	java/io/IOException
    //   25	35	148	java/io/IOException
    //   40	50	148	java/io/IOException
    //   68	90	148	java/io/IOException
    //   104	122	148	java/io/IOException
    //   19	25	173	finally
    //   25	35	173	finally
    //   40	50	173	finally
    //   68	90	173	finally
    //   104	122	173	finally
    //   150	160	173	finally
    //   90	97	188	finally
    //   122	129	188	finally
    //   90	97	197	java/io/IOException
    //   122	129	197	java/io/IOException
  }
  
  public static String getSharedPreferencesKey()
  {
    return WallpaperCropActivity.class.getName();
  }
  
  private static boolean isScreenLarge(Resources paramResources)
  {
    return paramResources.getConfiguration().smallestScreenWidthDp >= 720;
  }
  
  public static void suggestWallpaperDimension(Resources paramResources, final SharedPreferences paramSharedPreferences, WindowManager paramWindowManager, final WallpaperManager paramWallpaperManager)
  {
    new Thread("suggestWallpaperDimension")
    {
      public void run()
      {
        int i = paramSharedPreferences.getInt("wallpaper.width", this.val$defaultWallpaperSize.x);
        int j = paramSharedPreferences.getInt("wallpaper.height", this.val$defaultWallpaperSize.y);
        paramWallpaperManager.suggestDesiredDimensions(i, j);
      }
    }.start();
  }
  
  private static float wallpaperTravelToScreenWidthRatio(int paramInt1, int paramInt2)
  {
    return 1.007692F + 0.3076923F * (paramInt1 / paramInt2);
  }
  
  protected void cropImageAndSetWallpaper(Resources paramResources, int paramInt, final boolean paramBoolean)
  {
    int i = getRotationFromExif(paramResources, paramInt);
    Point localPoint1 = this.mCropView.getSourceDimensions();
    Point localPoint2 = getDefaultWallpaperSize(getResources(), getWindowManager());
    RectF localRectF = getMaxCropRect(localPoint1.x, localPoint1.y, localPoint2.x, localPoint2.y, false);
    Runnable local6 = new Runnable()
    {
      public void run()
      {
        WallpaperCropActivity.this.updateWallpaperDimensions(0, 0);
        if (paramBoolean)
        {
          WallpaperCropActivity.this.setResult(-1);
          WallpaperCropActivity.this.finish();
        }
      }
    };
    new BitmapCropTask(this, paramResources, paramInt, localRectF, i, localPoint2.x, localPoint2.y, true, false, local6).execute(new Void[0]);
  }
  
  protected void cropImageAndSetWallpaper(Uri paramUri, OnBitmapCroppedHandler paramOnBitmapCroppedHandler, final boolean paramBoolean)
  {
    int i;
    int j;
    label56:
    Point localPoint2;
    RectF localRectF;
    int k;
    float f1;
    float[] arrayOfFloat;
    float f2;
    label200:
    float f4;
    if (this.mCropView.getLayoutDirection() == 0)
    {
      i = 1;
      Display localDisplay = getWindowManager().getDefaultDisplay();
      Point localPoint1 = new Point();
      localDisplay.getSize(localPoint1);
      if (localPoint1.x >= localPoint1.y) {
        break label356;
      }
      j = 1;
      localPoint2 = getDefaultWallpaperSize(getResources(), getWindowManager());
      localRectF = this.mCropView.getCrop();
      k = this.mCropView.getImageRotation();
      f1 = this.mCropView.getWidth() / localRectF.width();
      Point localPoint3 = this.mCropView.getSourceDimensions();
      Matrix localMatrix = new Matrix();
      localMatrix.setRotate(k);
      arrayOfFloat = new float[2];
      arrayOfFloat[0] = localPoint3.x;
      arrayOfFloat[1] = localPoint3.y;
      localMatrix.mapPoints(arrayOfFloat);
      arrayOfFloat[0] = Math.abs(arrayOfFloat[0]);
      arrayOfFloat[1] = Math.abs(arrayOfFloat[1]);
      if (i == 0) {
        break label362;
      }
      f2 = arrayOfFloat[0] - localRectF.right;
      float f3 = localPoint2.x / f1 - localRectF.width();
      f4 = Math.min(f2, f3);
      if (i == 0) {
        break label372;
      }
      localRectF.right = (f4 + localRectF.right);
      label244:
      if (j == 0) {
        break label388;
      }
    }
    label356:
    label362:
    label372:
    float f6;
    for (localRectF.bottom = (localRectF.top + localPoint2.y / f1);; localRectF.bottom = (f6 + localRectF.bottom))
    {
      final int m = Math.round(f1 * localRectF.width());
      final int n = Math.round(f1 * localRectF.height());
      BitmapCropTask localBitmapCropTask = new BitmapCropTask(this, paramUri, localRectF, k, m, n, true, false, new Runnable()
      {
        public void run()
        {
          WallpaperCropActivity.this.updateWallpaperDimensions(m, n);
          if (paramBoolean)
          {
            WallpaperCropActivity.this.setResult(-1);
            WallpaperCropActivity.this.finish();
          }
        }
      });
      if (paramOnBitmapCroppedHandler != null) {
        localBitmapCropTask.setOnBitmapCropped(paramOnBitmapCroppedHandler);
      }
      localBitmapCropTask.execute(new Void[0]);
      return;
      i = 0;
      break;
      j = 0;
      break label56;
      f2 = localRectF.left;
      break label200;
      localRectF.left -= f4;
      break label244;
      label388:
      float f5 = localPoint2.y / f1 - localRectF.height();
      f6 = Math.min(Math.min(arrayOfFloat[1] - localRectF.bottom, localRectF.top), f5 / 2.0F);
      localRectF.top -= f6;
    }
  }
  
  public boolean enableRotation()
  {
    return getResources().getBoolean(2131755011);
  }
  
  protected void init()
  {
    setContentView(2130968917);
    this.mCropView = ((CropView)findViewById(2131297220));
    final Uri localUri = getIntent().getData();
    if (localUri == null)
    {
      Log.e("Launcher3.CropActivity", "No URI passed in intent, exiting WallpaperCropActivity");
      finish();
      return;
    }
    ActionBar localActionBar = getActionBar();
    localActionBar.setCustomView(2130968579);
    localActionBar.getCustomView().setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        WallpaperCropActivity.this.cropImageAndSetWallpaper(localUri, null, true);
      }
    });
    final BitmapRegionTileSource.UriBitmapSource localUriBitmapSource = new BitmapRegionTileSource.UriBitmapSource(this, localUri, 1024);
    setCropViewTileSource(localUriBitmapSource, true, false, new Runnable()
    {
      public void run()
      {
        if (localUriBitmapSource.getLoadingState() != BitmapRegionTileSource.BitmapSource.State.LOADED)
        {
          Toast.makeText(WallpaperCropActivity.this, WallpaperCropActivity.this.getString(2131361865), 1).show();
          WallpaperCropActivity.this.finish();
        }
      }
    });
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    init();
    if (!enableRotation()) {
      setRequestedOrientation(1);
    }
  }
  
  public void setCropViewTileSource(final BitmapRegionTileSource.BitmapSource paramBitmapSource, final boolean paramBoolean1, final boolean paramBoolean2, final Runnable paramRunnable)
  {
    final View localView = findViewById(2131297221);
    final AsyncTask local3 = new AsyncTask()
    {
      protected Void doInBackground(Void... paramAnonymousVarArgs)
      {
        if (!isCancelled()) {
          paramBitmapSource.loadInBackground();
        }
        return null;
      }
      
      protected void onPostExecute(Void paramAnonymousVoid)
      {
        if (!isCancelled())
        {
          localView.setVisibility(4);
          if (paramBitmapSource.getLoadingState() == BitmapRegionTileSource.BitmapSource.State.LOADED)
          {
            WallpaperCropActivity.this.mCropView.setTileSource(new BitmapRegionTileSource(jdField_this, paramBitmapSource), null);
            WallpaperCropActivity.this.mCropView.setTouchEnabled(paramBoolean1);
            if (paramBoolean2) {
              WallpaperCropActivity.this.mCropView.moveToLeft();
            }
          }
        }
        if (paramRunnable != null) {
          paramRunnable.run();
        }
      }
    };
    localView.postDelayed(new Runnable()
    {
      public void run()
      {
        if (local3.getStatus() != AsyncTask.Status.FINISHED) {
          localView.setVisibility(0);
        }
      }
    }, 1000L);
    local3.execute(new Void[0]);
  }
  
  protected void setWallpaper(String paramString, final boolean paramBoolean)
  {
    BitmapCropTask localBitmapCropTask = new BitmapCropTask(this, paramString, null, getRotationFromExif(paramString), 0, 0, true, false, null);
    localBitmapCropTask.setOnEndRunnable(new Runnable()
    {
      public void run()
      {
        WallpaperCropActivity.this.updateWallpaperDimensions(this.val$bounds.x, this.val$bounds.y);
        if (paramBoolean)
        {
          WallpaperCropActivity.this.setResult(-1);
          WallpaperCropActivity.this.finish();
        }
      }
    });
    localBitmapCropTask.setNoCrop(true);
    localBitmapCropTask.execute(new Void[0]);
  }
  
  protected void updateWallpaperDimensions(int paramInt1, int paramInt2)
  {
    SharedPreferences localSharedPreferences = getSharedPreferences(getSharedPreferencesKey(), 4);
    SharedPreferences.Editor localEditor = localSharedPreferences.edit();
    if ((paramInt1 != 0) && (paramInt2 != 0))
    {
      localEditor.putInt("wallpaper.width", paramInt1);
      localEditor.putInt("wallpaper.height", paramInt2);
    }
    for (;;)
    {
      localEditor.commit();
      suggestWallpaperDimension(getResources(), localSharedPreferences, getWindowManager(), WallpaperManager.getInstance(this));
      return;
      localEditor.remove("wallpaper.width");
      localEditor.remove("wallpaper.height");
    }
  }
  
  protected static class BitmapCropTask
    extends AsyncTask<Void, Void, Boolean>
  {
    Context mContext;
    RectF mCropBounds = null;
    Bitmap mCroppedBitmap;
    String mInFilePath;
    byte[] mInImageBytes;
    int mInResId = 0;
    Uri mInUri = null;
    boolean mNoCrop;
    WallpaperCropActivity.OnBitmapCroppedHandler mOnBitmapCroppedHandler;
    Runnable mOnEndRunnable;
    int mOutHeight;
    int mOutWidth;
    String mOutputFormat = "jpg";
    Resources mResources;
    int mRotation;
    boolean mSaveCroppedBitmap;
    boolean mSetWallpaper;
    
    public BitmapCropTask(Context paramContext, Resources paramResources, int paramInt1, RectF paramRectF, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, Runnable paramRunnable)
    {
      this.mContext = paramContext;
      this.mInResId = paramInt1;
      this.mResources = paramResources;
      init(paramRectF, paramInt2, paramInt3, paramInt4, paramBoolean1, paramBoolean2, paramRunnable);
    }
    
    public BitmapCropTask(Context paramContext, Uri paramUri, RectF paramRectF, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, Runnable paramRunnable)
    {
      this.mContext = paramContext;
      this.mInUri = paramUri;
      init(paramRectF, paramInt1, paramInt2, paramInt3, paramBoolean1, paramBoolean2, paramRunnable);
    }
    
    public BitmapCropTask(Context paramContext, String paramString, RectF paramRectF, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, Runnable paramRunnable)
    {
      this.mContext = paramContext;
      this.mInFilePath = paramString;
      init(paramRectF, paramInt1, paramInt2, paramInt3, paramBoolean1, paramBoolean2, paramRunnable);
    }
    
    public BitmapCropTask(byte[] paramArrayOfByte, RectF paramRectF, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, Runnable paramRunnable)
    {
      this.mInImageBytes = paramArrayOfByte;
      init(paramRectF, paramInt1, paramInt2, paramInt3, paramBoolean1, paramBoolean2, paramRunnable);
    }
    
    private void init(RectF paramRectF, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, Runnable paramRunnable)
    {
      this.mCropBounds = paramRectF;
      this.mRotation = paramInt1;
      this.mOutWidth = paramInt2;
      this.mOutHeight = paramInt3;
      this.mSetWallpaper = paramBoolean1;
      this.mSaveCroppedBitmap = paramBoolean2;
      this.mOnEndRunnable = paramRunnable;
    }
    
    private InputStream regenerateInputStream()
    {
      if ((this.mInUri == null) && (this.mInResId == 0) && (this.mInFilePath == null) && (this.mInImageBytes == null)) {
        Log.w("Launcher3.CropActivity", "cannot read original file, no input URI, resource ID, or image byte array given");
      }
      for (;;)
      {
        return null;
        try
        {
          if (this.mInUri == null) {
            break;
          }
          BufferedInputStream localBufferedInputStream1 = new BufferedInputStream(this.mContext.getContentResolver().openInputStream(this.mInUri));
          return localBufferedInputStream1;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          Log.w("Launcher3.CropActivity", "cannot read file: " + this.mInUri.toString(), localFileNotFoundException);
        }
      }
      if (this.mInFilePath != null) {
        return this.mContext.openFileInput(this.mInFilePath);
      }
      if (this.mInImageBytes != null) {
        return new BufferedInputStream(new ByteArrayInputStream(this.mInImageBytes));
      }
      BufferedInputStream localBufferedInputStream2 = new BufferedInputStream(this.mResources.openRawResource(this.mInResId));
      return localBufferedInputStream2;
    }
    
    public boolean cropBitmap()
    {
      boolean bool1 = this.mSetWallpaper;
      WallpaperManager localWallpaperManager = null;
      if (bool1) {
        localWallpaperManager = WallpaperManager.getInstance(this.mContext.getApplicationContext());
      }
      if ((this.mSetWallpaper) && (this.mNoCrop))
      {
        try
        {
          InputStream localInputStream3 = regenerateInputStream();
          i2 = 0;
          if (localInputStream3 != null)
          {
            localWallpaperManager.setStream(localInputStream3);
            Utils.closeSilently(localInputStream3);
          }
        }
        catch (IOException localIOException3)
        {
          for (;;)
          {
            Log.w("Launcher3.CropActivity", "cannot write stream to wallpaper", localIOException3);
            int i2 = 1;
          }
        }
        return i2 == 0;
      }
      Rect localRect = new Rect();
      Matrix localMatrix1 = new Matrix();
      Matrix localMatrix2 = new Matrix();
      if (this.mRotation > 0)
      {
        localMatrix1.setRotate(this.mRotation);
        localMatrix2.setRotate(-this.mRotation);
        this.mCropBounds.roundOut(localRect);
        RectF localRectF7 = new RectF(localRect);
        this.mCropBounds = localRectF7;
        Point localPoint = getImageBounds();
        if (localPoint == null)
        {
          Log.w("Launcher3.CropActivity", "cannot get bounds for image");
          return false;
        }
        float[] arrayOfFloat2 = new float[2];
        arrayOfFloat2[0] = localPoint.x;
        arrayOfFloat2[1] = localPoint.y;
        localMatrix1.mapPoints(arrayOfFloat2);
        arrayOfFloat2[0] = Math.abs(arrayOfFloat2[0]);
        arrayOfFloat2[1] = Math.abs(arrayOfFloat2[1]);
        this.mCropBounds.offset(-arrayOfFloat2[0] / 2.0F, -arrayOfFloat2[1] / 2.0F);
        localMatrix2.mapRect(this.mCropBounds);
        this.mCropBounds.offset(localPoint.x / 2, localPoint.y / 2);
      }
      this.mCropBounds.roundOut(localRect);
      if ((localRect.width() <= 0) || (localRect.height() <= 0))
      {
        Log.w("Launcher3.CropActivity", "crop has bad values for full size image");
        return false;
      }
      int i = Math.max(1, Math.min(localRect.width() / this.mOutWidth, localRect.height() / this.mOutHeight));
      BitmapRegionDecoder localBitmapRegionDecoder = null;
      InputStream localInputStream1 = null;
      try
      {
        localInputStream1 = regenerateInputStream();
        if (localInputStream1 == null)
        {
          Log.w("Launcher3.CropActivity", "cannot get input stream for uri=" + this.mInUri.toString());
          return false;
        }
        localBitmapRegionDecoder = BitmapRegionDecoder.newInstance(localInputStream1, false);
        Utils.closeSilently(localInputStream1);
      }
      catch (IOException localIOException1)
      {
        for (;;)
        {
          BitmapFactory.Options localOptions1;
          InputStream localInputStream2;
          Bitmap localBitmap2;
          BitmapFactory.Options localOptions2;
          RectF localRectF3;
          RectF localRectF4;
          RectF localRectF5;
          RectF localRectF6;
          int k;
          int m;
          int n;
          int i1;
          Log.w("Launcher3.CropActivity", "cannot open region decoder for file: " + this.mInUri.toString(), localIOException1);
          Utils.closeSilently(localInputStream1);
        }
      }
      finally
      {
        Utils.closeSilently(localInputStream1);
      }
      Object localObject2 = null;
      if (localBitmapRegionDecoder != null)
      {
        localOptions1 = new BitmapFactory.Options();
        if (i > 1) {
          localOptions1.inSampleSize = i;
        }
        localObject2 = localBitmapRegionDecoder.decodeRegion(localRect, localOptions1);
        localBitmapRegionDecoder.recycle();
      }
      if (localObject2 == null)
      {
        localInputStream2 = regenerateInputStream();
        localBitmap2 = null;
        if (localInputStream2 != null)
        {
          localOptions2 = new BitmapFactory.Options();
          if (i > 1) {
            localOptions2.inSampleSize = i;
          }
          localBitmap2 = BitmapFactory.decodeStream(localInputStream2, null, localOptions2);
          Utils.closeSilently(localInputStream2);
        }
        if (localBitmap2 != null)
        {
          localRectF3 = this.mCropBounds;
          localRectF3.left /= i;
          localRectF4 = this.mCropBounds;
          localRectF4.top /= i;
          localRectF5 = this.mCropBounds;
          localRectF5.bottom /= i;
          localRectF6 = this.mCropBounds;
          localRectF6.right /= i;
          this.mCropBounds.roundOut(localRect);
          k = localRect.left;
          m = localRect.top;
          n = localRect.width();
          i1 = localRect.height();
          localObject2 = Bitmap.createBitmap(localBitmap2, k, m, n, i1);
        }
      }
      if (localObject2 == null)
      {
        Log.w("Launcher3.CropActivity", "cannot decode file: " + this.mInUri.toString());
        return false;
      }
      float[] arrayOfFloat1;
      RectF localRectF1;
      RectF localRectF2;
      Matrix localMatrix3;
      if (((this.mOutWidth > 0) && (this.mOutHeight > 0)) || (this.mRotation > 0))
      {
        arrayOfFloat1 = new float[2];
        arrayOfFloat1[0] = ((Bitmap)localObject2).getWidth();
        arrayOfFloat1[1] = ((Bitmap)localObject2).getHeight();
        localMatrix1.mapPoints(arrayOfFloat1);
        arrayOfFloat1[0] = Math.abs(arrayOfFloat1[0]);
        arrayOfFloat1[1] = Math.abs(arrayOfFloat1[1]);
        if ((this.mOutWidth <= 0) || (this.mOutHeight <= 0))
        {
          this.mOutWidth = Math.round(arrayOfFloat1[0]);
          this.mOutHeight = Math.round(arrayOfFloat1[1]);
        }
        localRectF1 = new RectF(0.0F, 0.0F, arrayOfFloat1[0], arrayOfFloat1[1]);
        localRectF2 = new RectF(0.0F, 0.0F, this.mOutWidth, this.mOutHeight);
        localMatrix3 = new Matrix();
        if (this.mRotation != 0) {
          break label1127;
        }
        localMatrix3.setRectToRect(localRectF1, localRectF2, Matrix.ScaleToFit.FILL);
        Bitmap localBitmap1 = Bitmap.createBitmap((int)localRectF2.width(), (int)localRectF2.height(), Bitmap.Config.ARGB_8888);
        if (localBitmap1 != null)
        {
          Canvas localCanvas = new Canvas(localBitmap1);
          Paint localPaint = new Paint();
          localPaint.setFilterBitmap(true);
          localCanvas.drawBitmap((Bitmap)localObject2, localMatrix3, localPaint);
          localObject2 = localBitmap1;
        }
      }
      if (this.mSaveCroppedBitmap) {
        this.mCroppedBitmap = ((Bitmap)localObject2);
      }
      Bitmap.CompressFormat localCompressFormat = WallpaperCropActivity.convertExtensionToCompressFormat(WallpaperCropActivity.getFileExtension(this.mOutputFormat));
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(2048);
      int j;
      if (((Bitmap)localObject2).compress(localCompressFormat, 90, localByteArrayOutputStream))
      {
        boolean bool2 = this.mSetWallpaper;
        j = 0;
        if (bool2)
        {
          j = 0;
          if (localWallpaperManager == null) {}
        }
      }
      for (;;)
      {
        try
        {
          byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
          ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
          localWallpaperManager.setStream(localByteArrayInputStream);
          WallpaperCropActivity.OnBitmapCroppedHandler localOnBitmapCroppedHandler = this.mOnBitmapCroppedHandler;
          j = 0;
          if (localOnBitmapCroppedHandler != null) {
            this.mOnBitmapCroppedHandler.onBitmapCropped(arrayOfByte);
          }
        }
        catch (IOException localIOException2)
        {
          label1127:
          Matrix localMatrix4;
          Matrix localMatrix5;
          Matrix localMatrix6;
          Matrix localMatrix7;
          Matrix localMatrix8;
          Matrix localMatrix9;
          Log.w("Launcher3.CropActivity", "cannot write stream to wallpaper", localIOException2);
          j = 1;
          continue;
        }
        if (j != 0) {
          break label1310;
        }
        return true;
        localMatrix4 = new Matrix();
        localMatrix4.setTranslate(-((Bitmap)localObject2).getWidth() / 2.0F, -((Bitmap)localObject2).getHeight() / 2.0F);
        localMatrix5 = new Matrix();
        localMatrix5.setRotate(this.mRotation);
        localMatrix6 = new Matrix();
        localMatrix6.setTranslate(arrayOfFloat1[0] / 2.0F, arrayOfFloat1[1] / 2.0F);
        localMatrix7 = new Matrix();
        localMatrix7.setRectToRect(localRectF1, localRectF2, Matrix.ScaleToFit.FILL);
        localMatrix8 = new Matrix();
        localMatrix8.setConcat(localMatrix5, localMatrix4);
        localMatrix9 = new Matrix();
        localMatrix9.setConcat(localMatrix7, localMatrix6);
        localMatrix3.setConcat(localMatrix9, localMatrix8);
        break;
        Log.w("Launcher3.CropActivity", "cannot compress bitmap");
        j = 1;
      }
      label1310:
      return false;
    }
    
    protected Boolean doInBackground(Void... paramVarArgs)
    {
      return Boolean.valueOf(cropBitmap());
    }
    
    public Bitmap getCroppedBitmap()
    {
      return this.mCroppedBitmap;
    }
    
    public Point getImageBounds()
    {
      InputStream localInputStream = regenerateInputStream();
      Point localPoint = null;
      if (localInputStream != null)
      {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(localInputStream, null, localOptions);
        Utils.closeSilently(localInputStream);
        int i = localOptions.outWidth;
        localPoint = null;
        if (i != 0)
        {
          int j = localOptions.outHeight;
          localPoint = null;
          if (j != 0) {
            localPoint = new Point(localOptions.outWidth, localOptions.outHeight);
          }
        }
      }
      return localPoint;
    }
    
    protected void onPostExecute(Boolean paramBoolean)
    {
      if (this.mOnEndRunnable != null) {
        this.mOnEndRunnable.run();
      }
    }
    
    public void setCropBounds(RectF paramRectF)
    {
      this.mCropBounds = paramRectF;
    }
    
    public void setNoCrop(boolean paramBoolean)
    {
      this.mNoCrop = paramBoolean;
    }
    
    public void setOnBitmapCropped(WallpaperCropActivity.OnBitmapCroppedHandler paramOnBitmapCroppedHandler)
    {
      this.mOnBitmapCroppedHandler = paramOnBitmapCroppedHandler;
    }
    
    public void setOnEndRunnable(Runnable paramRunnable)
    {
      this.mOnEndRunnable = paramRunnable;
    }
  }
  
  public static abstract interface OnBitmapCroppedHandler
  {
    public abstract void onBitmapCropped(byte[] paramArrayOfByte);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.WallpaperCropActivity
 * JD-Core Version:    0.7.0.1
 */