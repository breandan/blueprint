package com.android.photos;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;
import com.android.gallery3d.common.BitmapUtils;
import com.android.gallery3d.common.Utils;
import com.android.gallery3d.exif.ExifInterface;
import com.android.gallery3d.glrenderer.BasicTexture;
import com.android.gallery3d.glrenderer.BitmapTexture;
import com.android.photos.views.TiledImageRenderer;
import com.android.photos.views.TiledImageRenderer.TileSource;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@TargetApi(15)
public class BitmapRegionTileSource
  implements TiledImageRenderer.TileSource
{
  private static final boolean REUSE_BITMAP;
  private Canvas mCanvas;
  SimpleBitmapRegionDecoder mDecoder;
  int mHeight;
  private BitmapFactory.Options mOptions;
  private Rect mOverlapRegion = new Rect();
  private BasicTexture mPreview;
  private final int mRotation;
  int mTileSize;
  private Rect mWantRegion = new Rect();
  int mWidth;
  
  static
  {
    if (Build.VERSION.SDK_INT >= 16) {}
    for (boolean bool = true;; bool = false)
    {
      REUSE_BITMAP = bool;
      return;
    }
  }
  
  public BitmapRegionTileSource(Context paramContext, BitmapSource paramBitmapSource)
  {
    this.mTileSize = TiledImageRenderer.suggestedTileSize(paramContext);
    this.mRotation = paramBitmapSource.getRotation();
    this.mDecoder = paramBitmapSource.getBitmapRegionDecoder();
    Bitmap localBitmap;
    if (this.mDecoder != null)
    {
      this.mWidth = this.mDecoder.getWidth();
      this.mHeight = this.mDecoder.getHeight();
      this.mOptions = new BitmapFactory.Options();
      this.mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
      this.mOptions.inPreferQualityOverSpeed = true;
      this.mOptions.inTempStorage = new byte[16384];
      int i = paramBitmapSource.getPreviewSize();
      if (i != 0)
      {
        localBitmap = decodePreview(paramBitmapSource, Math.min(i, 1024));
        if ((localBitmap.getWidth() > 2048) || (localBitmap.getHeight() > 2048)) {
          break label183;
        }
        this.mPreview = new BitmapTexture(localBitmap);
      }
    }
    return;
    label183:
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = Integer.valueOf(this.mWidth);
    arrayOfObject[1] = Integer.valueOf(this.mHeight);
    arrayOfObject[2] = Integer.valueOf(localBitmap.getWidth());
    arrayOfObject[3] = Integer.valueOf(localBitmap.getHeight());
    Log.w("BitmapRegionTileSource", String.format("Failed to create preview of apropriate size!  in: %dx%d, out: %dx%d", arrayOfObject));
  }
  
  private Bitmap decodePreview(BitmapSource paramBitmapSource, int paramInt)
  {
    Bitmap localBitmap = paramBitmapSource.getPreviewBitmap();
    if (localBitmap == null) {
      return null;
    }
    float f = paramInt / Math.max(localBitmap.getWidth(), localBitmap.getHeight());
    if (f <= 0.5D) {
      localBitmap = BitmapUtils.resizeBitmapByScale(localBitmap, f, true);
    }
    return ensureGLCompatibleBitmap(localBitmap);
  }
  
  private static Bitmap ensureGLCompatibleBitmap(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (paramBitmap.getConfig() != null)) {
      return paramBitmap;
    }
    Bitmap localBitmap = paramBitmap.copy(Bitmap.Config.ARGB_8888, false);
    paramBitmap.recycle();
    return localBitmap;
  }
  
  private Bitmap getTileWithoutReusingBitmap(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt4 << paramInt1;
    this.mWantRegion.set(paramInt2, paramInt3, paramInt2 + i, paramInt3 + i);
    this.mOverlapRegion.set(0, 0, this.mWidth, this.mHeight);
    this.mOptions.inSampleSize = (1 << paramInt1);
    Bitmap localBitmap1 = this.mDecoder.decodeRegion(this.mOverlapRegion, this.mOptions);
    if (localBitmap1 == null) {
      Log.w("BitmapRegionTileSource", "fail in decoding region");
    }
    if (this.mWantRegion.equals(this.mOverlapRegion)) {
      return localBitmap1;
    }
    Bitmap localBitmap2 = Bitmap.createBitmap(paramInt4, paramInt4, Bitmap.Config.ARGB_8888);
    if (this.mCanvas == null) {
      this.mCanvas = new Canvas();
    }
    this.mCanvas.setBitmap(localBitmap2);
    this.mCanvas.drawBitmap(localBitmap1, this.mOverlapRegion.left - this.mWantRegion.left >> paramInt1, this.mOverlapRegion.top - this.mWantRegion.top >> paramInt1, null);
    this.mCanvas.setBitmap(null);
    return localBitmap2;
  }
  
  public int getImageHeight()
  {
    return this.mHeight;
  }
  
  public int getImageWidth()
  {
    return this.mWidth;
  }
  
  public BasicTexture getPreview()
  {
    return this.mPreview;
  }
  
  public int getRotation()
  {
    return this.mRotation;
  }
  
  public Bitmap getTile(int paramInt1, int paramInt2, int paramInt3, Bitmap paramBitmap)
  {
    int i = getTileSize();
    if (!REUSE_BITMAP) {
      return getTileWithoutReusingBitmap(paramInt1, paramInt2, paramInt3, i);
    }
    int j = i << paramInt1;
    this.mWantRegion.set(paramInt2, paramInt3, paramInt2 + j, paramInt3 + j);
    if (paramBitmap == null) {
      paramBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
    }
    this.mOptions.inSampleSize = (1 << paramInt1);
    this.mOptions.inBitmap = paramBitmap;
    try
    {
      Bitmap localBitmap = this.mDecoder.decodeRegion(this.mWantRegion, this.mOptions);
      if ((this.mOptions.inBitmap != localBitmap) && (this.mOptions.inBitmap != null)) {
        this.mOptions.inBitmap = null;
      }
      if (localBitmap == null) {
        Log.w("BitmapRegionTileSource", "fail in decoding region");
      }
      return localBitmap;
    }
    finally
    {
      if ((this.mOptions.inBitmap != paramBitmap) && (this.mOptions.inBitmap != null)) {
        this.mOptions.inBitmap = null;
      }
    }
  }
  
  public int getTileSize()
  {
    return this.mTileSize;
  }
  
  public static abstract class BitmapSource
  {
    private SimpleBitmapRegionDecoder mDecoder;
    private Bitmap mPreview;
    private int mPreviewSize;
    private int mRotation;
    private State mState = State.NOT_LOADED;
    
    public BitmapSource(int paramInt)
    {
      this.mPreviewSize = paramInt;
    }
    
    public SimpleBitmapRegionDecoder getBitmapRegionDecoder()
    {
      return this.mDecoder;
    }
    
    public State getLoadingState()
    {
      return this.mState;
    }
    
    public Bitmap getPreviewBitmap()
    {
      return this.mPreview;
    }
    
    public int getPreviewSize()
    {
      return this.mPreviewSize;
    }
    
    public int getRotation()
    {
      return this.mRotation;
    }
    
    public abstract SimpleBitmapRegionDecoder loadBitmapRegionDecoder();
    
    public boolean loadInBackground()
    {
      ExifInterface localExifInterface = new ExifInterface();
      if (readExif(localExifInterface))
      {
        Integer localInteger = localExifInterface.getTagIntValue(ExifInterface.TAG_ORIENTATION);
        if (localInteger != null) {
          this.mRotation = ExifInterface.getRotationForOrientationValue(localInteger.shortValue());
        }
      }
      this.mDecoder = loadBitmapRegionDecoder();
      if (this.mDecoder == null)
      {
        this.mState = State.ERROR_LOADING;
        return false;
      }
      int i = this.mDecoder.getWidth();
      int j = this.mDecoder.getHeight();
      if (this.mPreviewSize != 0)
      {
        int k = Math.min(this.mPreviewSize, 1024);
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        localOptions.inPreferQualityOverSpeed = true;
        localOptions.inSampleSize = BitmapUtils.computeSampleSizeLarger(k / Math.max(i, j));
        localOptions.inJustDecodeBounds = false;
        this.mPreview = loadPreviewBitmap(localOptions);
      }
      this.mState = State.LOADED;
      return true;
    }
    
    public abstract Bitmap loadPreviewBitmap(BitmapFactory.Options paramOptions);
    
    public abstract boolean readExif(ExifInterface paramExifInterface);
    
    public static enum State
    {
      static
      {
        LOADED = new State("LOADED", 1);
        ERROR_LOADING = new State("ERROR_LOADING", 2);
        State[] arrayOfState = new State[3];
        arrayOfState[0] = NOT_LOADED;
        arrayOfState[1] = LOADED;
        arrayOfState[2] = ERROR_LOADING;
        $VALUES = arrayOfState;
      }
      
      private State() {}
    }
  }
  
  public static class FilePathBitmapSource
    extends BitmapRegionTileSource.BitmapSource
  {
    private String mPath;
    
    public FilePathBitmapSource(String paramString, int paramInt)
    {
      super();
      this.mPath = paramString;
    }
    
    public SimpleBitmapRegionDecoder loadBitmapRegionDecoder()
    {
      Object localObject = SimpleBitmapRegionDecoderWrapper.newInstance(this.mPath, true);
      if (localObject == null) {
        localObject = DumbBitmapRegionDecoder.newInstance(this.mPath);
      }
      return localObject;
    }
    
    public Bitmap loadPreviewBitmap(BitmapFactory.Options paramOptions)
    {
      return BitmapFactory.decodeFile(this.mPath, paramOptions);
    }
    
    public boolean readExif(ExifInterface paramExifInterface)
    {
      try
      {
        paramExifInterface.readExif(this.mPath);
        return true;
      }
      catch (IOException localIOException)
      {
        Log.w("BitmapRegionTileSource", "getting decoder failed", localIOException);
      }
      return false;
    }
  }
  
  public static class ResourceBitmapSource
    extends BitmapRegionTileSource.BitmapSource
  {
    private Resources mRes;
    private int mResId;
    
    public ResourceBitmapSource(Resources paramResources, int paramInt1, int paramInt2)
    {
      super();
      this.mRes = paramResources;
      this.mResId = paramInt1;
    }
    
    private InputStream regenerateInputStream()
    {
      return new BufferedInputStream(this.mRes.openRawResource(this.mResId));
    }
    
    public SimpleBitmapRegionDecoder loadBitmapRegionDecoder()
    {
      InputStream localInputStream1 = regenerateInputStream();
      Object localObject = SimpleBitmapRegionDecoderWrapper.newInstance(localInputStream1, false);
      Utils.closeSilently(localInputStream1);
      if (localObject == null)
      {
        InputStream localInputStream2 = regenerateInputStream();
        localObject = DumbBitmapRegionDecoder.newInstance(localInputStream2);
        Utils.closeSilently(localInputStream2);
      }
      return localObject;
    }
    
    public Bitmap loadPreviewBitmap(BitmapFactory.Options paramOptions)
    {
      return BitmapFactory.decodeResource(this.mRes, this.mResId, paramOptions);
    }
    
    public boolean readExif(ExifInterface paramExifInterface)
    {
      try
      {
        InputStream localInputStream = regenerateInputStream();
        paramExifInterface.readExif(localInputStream);
        Utils.closeSilently(localInputStream);
        return true;
      }
      catch (IOException localIOException)
      {
        Log.e("BitmapRegionTileSource", "Error reading resource", localIOException);
      }
      return false;
    }
  }
  
  public static class UriBitmapSource
    extends BitmapRegionTileSource.BitmapSource
  {
    private Context mContext;
    private Uri mUri;
    
    public UriBitmapSource(Context paramContext, Uri paramUri, int paramInt)
    {
      super();
      this.mContext = paramContext;
      this.mUri = paramUri;
    }
    
    private InputStream regenerateInputStream()
      throws FileNotFoundException
    {
      return new BufferedInputStream(this.mContext.getContentResolver().openInputStream(this.mUri));
    }
    
    public SimpleBitmapRegionDecoder loadBitmapRegionDecoder()
    {
      try
      {
        InputStream localInputStream1 = regenerateInputStream();
        Object localObject = SimpleBitmapRegionDecoderWrapper.newInstance(localInputStream1, false);
        Utils.closeSilently(localInputStream1);
        if (localObject == null)
        {
          InputStream localInputStream2 = regenerateInputStream();
          localObject = DumbBitmapRegionDecoder.newInstance(localInputStream2);
          Utils.closeSilently(localInputStream2);
        }
        return localObject;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        Log.e("BitmapRegionTileSource", "Failed to load URI " + this.mUri, localFileNotFoundException);
        return null;
      }
      catch (IOException localIOException)
      {
        Log.e("BitmapRegionTileSource", "Failure while reading URI " + this.mUri, localIOException);
      }
      return null;
    }
    
    public Bitmap loadPreviewBitmap(BitmapFactory.Options paramOptions)
    {
      try
      {
        InputStream localInputStream = regenerateInputStream();
        Bitmap localBitmap = BitmapFactory.decodeStream(localInputStream, null, paramOptions);
        Utils.closeSilently(localInputStream);
        return localBitmap;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        Log.e("BitmapRegionTileSource", "Failed to load URI " + this.mUri, localFileNotFoundException);
      }
      return null;
    }
    
    public boolean readExif(ExifInterface paramExifInterface)
    {
      InputStream localInputStream = null;
      try
      {
        localInputStream = regenerateInputStream();
        paramExifInterface.readExif(localInputStream);
        Utils.closeSilently(localInputStream);
        return true;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        Log.e("BitmapRegionTileSource", "Failed to load URI " + this.mUri, localFileNotFoundException);
        return false;
      }
      catch (IOException localIOException)
      {
        Log.e("BitmapRegionTileSource", "Failed to load URI " + this.mUri, localIOException);
        return false;
      }
      finally
      {
        Utils.closeSilently(localInputStream);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.photos.BitmapRegionTileSource
 * JD-Core Version:    0.7.0.1
 */