package com.android.launcher3;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executor;

public class WidgetPreviewLoader
{
  private static HashSet<String> sInvalidPackages = new HashSet();
  private int mAppIconSize;
  private CanvasCache mCachedAppWidgetPreviewCanvas = new CanvasCache();
  private RectCache mCachedAppWidgetPreviewDestRect = new RectCache();
  private PaintCache mCachedAppWidgetPreviewPaint = new PaintCache();
  private RectCache mCachedAppWidgetPreviewSrcRect = new RectCache();
  private BitmapFactoryOptionsCache mCachedBitmapFactoryOptions = new BitmapFactoryOptionsCache();
  private String mCachedSelectQuery;
  private BitmapCache mCachedShortcutPreviewBitmap = new BitmapCache();
  private CanvasCache mCachedShortcutPreviewCanvas = new CanvasCache();
  private PaintCache mCachedShortcutPreviewPaint = new PaintCache();
  private Context mContext;
  private CacheDb mDb;
  private IconCache mIconCache;
  private HashMap<String, WeakReference<Bitmap>> mLoadedPreviews;
  private PackageManager mPackageManager;
  private int mPreviewBitmapHeight;
  private int mPreviewBitmapWidth;
  private String mSize;
  private ArrayList<SoftReference<Bitmap>> mUnusedBitmaps;
  private PagedViewCellLayout mWidgetSpacingLayout;
  private final float sWidgetPreviewIconPaddingPercentage = 0.25F;
  
  public WidgetPreviewLoader(Context paramContext)
  {
    LauncherAppState localLauncherAppState = LauncherAppState.getInstance();
    DeviceProfile localDeviceProfile = localLauncherAppState.getDynamicGrid().getDeviceProfile();
    this.mContext = paramContext;
    this.mPackageManager = this.mContext.getPackageManager();
    this.mAppIconSize = localDeviceProfile.iconSizePx;
    this.mIconCache = localLauncherAppState.getIconCache();
    this.mDb = localLauncherAppState.getWidgetPreviewCacheDb();
    this.mLoadedPreviews = new HashMap();
    this.mUnusedBitmaps = new ArrayList();
    SharedPreferences localSharedPreferences = paramContext.getSharedPreferences(LauncherAppState.getSharedPreferencesKey(), 0);
    String str1 = localSharedPreferences.getString("android.incremental.version", null);
    String str2 = Build.VERSION.INCREMENTAL;
    if (!str2.equals(str1))
    {
      clearDb();
      SharedPreferences.Editor localEditor = localSharedPreferences.edit();
      localEditor.putString("android.incremental.version", str2);
      localEditor.commit();
    }
  }
  
  private void clearDb()
  {
    this.mDb.getWritableDatabase().delete("shortcut_and_widget_previews", null, null);
  }
  
  private Bitmap generateShortcutPreview(ResolveInfo paramResolveInfo, int paramInt1, int paramInt2, Bitmap paramBitmap)
  {
    Bitmap localBitmap = (Bitmap)this.mCachedShortcutPreviewBitmap.get();
    Canvas localCanvas = (Canvas)this.mCachedShortcutPreviewCanvas.get();
    if ((localBitmap == null) || (localBitmap.getWidth() != paramInt1) || (localBitmap.getHeight() != paramInt2))
    {
      localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
      this.mCachedShortcutPreviewBitmap.set(localBitmap);
    }
    Drawable localDrawable;
    for (;;)
    {
      localDrawable = this.mIconCache.getFullResIcon(paramResolveInfo);
      int i = this.mContext.getResources().getDimensionPixelOffset(2131689552);
      int j = this.mContext.getResources().getDimensionPixelOffset(2131689550);
      int k = this.mContext.getResources().getDimensionPixelOffset(2131689551);
      int m = paramInt1 - j - k;
      renderDrawableToBitmap(localDrawable, localBitmap, j, i, m, m);
      if ((paramBitmap == null) || ((paramBitmap.getWidth() == paramInt1) && (paramBitmap.getHeight() == paramInt2))) {
        break;
      }
      throw new RuntimeException("Improperly sized bitmap passed as argument");
      localCanvas.setBitmap(localBitmap);
      localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
      localCanvas.setBitmap(null);
    }
    if (paramBitmap == null) {
      paramBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
    }
    localCanvas.setBitmap(paramBitmap);
    Paint localPaint = (Paint)this.mCachedShortcutPreviewPaint.get();
    if (localPaint == null)
    {
      localPaint = new Paint();
      ColorMatrix localColorMatrix = new ColorMatrix();
      localColorMatrix.setSaturation(0.0F);
      localPaint.setColorFilter(new ColorMatrixColorFilter(localColorMatrix));
      localPaint.setAlpha(15);
      this.mCachedShortcutPreviewPaint.set(localPaint);
    }
    localCanvas.drawBitmap(localBitmap, 0.0F, 0.0F, localPaint);
    localCanvas.setBitmap(null);
    int n = this.mAppIconSize;
    int i1 = this.mAppIconSize;
    renderDrawableToBitmap(localDrawable, paramBitmap, 0, 0, n, i1);
    return paramBitmap;
  }
  
  private static String getObjectName(Object paramObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramObject instanceof AppWidgetProviderInfo))
    {
      localStringBuilder.append("Widget:");
      localStringBuilder.append(((AppWidgetProviderInfo)paramObject).provider.flattenToString());
      String str2 = localStringBuilder.toString();
      localStringBuilder.setLength(0);
      return str2;
    }
    localStringBuilder.append("Shortcut:");
    ResolveInfo localResolveInfo = (ResolveInfo)paramObject;
    localStringBuilder.append(new ComponentName(localResolveInfo.activityInfo.packageName, localResolveInfo.activityInfo.name).flattenToString());
    String str1 = localStringBuilder.toString();
    localStringBuilder.setLength(0);
    return str1;
  }
  
  private String getObjectPackage(Object paramObject)
  {
    if ((paramObject instanceof AppWidgetProviderInfo)) {
      return ((AppWidgetProviderInfo)paramObject).provider.getPackageName();
    }
    return ((ResolveInfo)paramObject).activityInfo.packageName;
  }
  
  private Bitmap readFromDb(String paramString, Bitmap paramBitmap)
  {
    if (this.mCachedSelectQuery == null) {
      this.mCachedSelectQuery = "name = ? AND size = ?";
    }
    SQLiteDatabase localSQLiteDatabase = this.mDb.getReadableDatabase();
    String[] arrayOfString1 = { "preview_bitmap" };
    String str = this.mCachedSelectQuery;
    String[] arrayOfString2 = new String[2];
    arrayOfString2[0] = paramString;
    arrayOfString2[1] = this.mSize;
    Cursor localCursor = localSQLiteDatabase.query("shortcut_and_widget_previews", arrayOfString1, str, arrayOfString2, null, null, null, null);
    if (localCursor.getCount() > 0)
    {
      localCursor.moveToFirst();
      byte[] arrayOfByte = localCursor.getBlob(0);
      localCursor.close();
      BitmapFactory.Options localOptions = (BitmapFactory.Options)this.mCachedBitmapFactoryOptions.get();
      localOptions.inBitmap = paramBitmap;
      localOptions.inSampleSize = 1;
      try
      {
        Bitmap localBitmap = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length, localOptions);
        return localBitmap;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        removeItemFromDb(this.mDb, paramString);
        return null;
      }
    }
    localCursor.close();
    return null;
  }
  
  public static void removeItemFromDb(CacheDb paramCacheDb, final String paramString)
  {
    AsyncTask local3 = new AsyncTask()
    {
      public Void doInBackground(Void... paramAnonymousVarArgs)
      {
        SQLiteDatabase localSQLiteDatabase = this.val$cacheDb.getWritableDatabase();
        String[] arrayOfString = new String[1];
        arrayOfString[0] = paramString;
        localSQLiteDatabase.delete("shortcut_and_widget_previews", "name = ? ", arrayOfString);
        return null;
      }
    };
    Executor localExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
    Void[] arrayOfVoid = new Void[1];
    arrayOfVoid[0] = ((Void)null);
    local3.executeOnExecutor(localExecutor, arrayOfVoid);
  }
  
  public static void removePackageFromDb(CacheDb paramCacheDb, final String paramString)
  {
    synchronized (sInvalidPackages)
    {
      sInvalidPackages.add(paramString);
      AsyncTask local2 = new AsyncTask()
      {
        public Void doInBackground(Void... paramAnonymousVarArgs)
        {
          SQLiteDatabase localSQLiteDatabase = this.val$cacheDb.getWritableDatabase();
          String[] arrayOfString = new String[2];
          arrayOfString[0] = ("Widget:" + paramString + "/%");
          arrayOfString[1] = ("Shortcut:" + paramString + "/%");
          localSQLiteDatabase.delete("shortcut_and_widget_previews", "name LIKE ? OR name LIKE ?", arrayOfString);
          synchronized (WidgetPreviewLoader.sInvalidPackages)
          {
            WidgetPreviewLoader.sInvalidPackages.remove(paramString);
            return null;
          }
        }
      };
      Executor localExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
      Void[] arrayOfVoid = new Void[1];
      arrayOfVoid[0] = ((Void)null);
      local2.executeOnExecutor(localExecutor, arrayOfVoid);
      return;
    }
  }
  
  public static void renderDrawableToBitmap(Drawable paramDrawable, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    renderDrawableToBitmap(paramDrawable, paramBitmap, paramInt1, paramInt2, paramInt3, paramInt4, 1.0F);
  }
  
  private static void renderDrawableToBitmap(Drawable paramDrawable, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat)
  {
    if (paramBitmap != null)
    {
      Canvas localCanvas = new Canvas(paramBitmap);
      localCanvas.scale(paramFloat, paramFloat);
      Rect localRect = paramDrawable.copyBounds();
      paramDrawable.setBounds(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
      paramDrawable.draw(localCanvas);
      paramDrawable.setBounds(localRect);
      localCanvas.setBitmap(null);
    }
  }
  
  private void writeToDb(Object paramObject, Bitmap paramBitmap)
  {
    String str = getObjectName(paramObject);
    SQLiteDatabase localSQLiteDatabase = this.mDb.getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("name", str);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
    localContentValues.put("preview_bitmap", localByteArrayOutputStream.toByteArray());
    localContentValues.put("size", this.mSize);
    localSQLiteDatabase.insert("shortcut_and_widget_previews", null, localContentValues);
  }
  
  public Bitmap generatePreview(Object paramObject, Bitmap paramBitmap)
  {
    if ((paramBitmap != null) && ((paramBitmap.getWidth() != this.mPreviewBitmapWidth) || (paramBitmap.getHeight() != this.mPreviewBitmapHeight))) {
      throw new RuntimeException("Improperly sized bitmap passed as argument");
    }
    if ((paramObject instanceof AppWidgetProviderInfo)) {
      return generateWidgetPreview((AppWidgetProviderInfo)paramObject, paramBitmap);
    }
    return generateShortcutPreview((ResolveInfo)paramObject, this.mPreviewBitmapWidth, this.mPreviewBitmapHeight, paramBitmap);
  }
  
  public Bitmap generateWidgetPreview(AppWidgetProviderInfo paramAppWidgetProviderInfo, Bitmap paramBitmap)
  {
    int[] arrayOfInt = Launcher.getSpanForWidget(this.mContext, paramAppWidgetProviderInfo);
    int i = maxWidthForWidgetPreview(arrayOfInt[0]);
    int j = maxHeightForWidgetPreview(arrayOfInt[1]);
    return generateWidgetPreview(paramAppWidgetProviderInfo.provider, paramAppWidgetProviderInfo.previewImage, paramAppWidgetProviderInfo.icon, arrayOfInt[0], arrayOfInt[1], i, j, paramBitmap, null);
  }
  
  public Bitmap generateWidgetPreview(ComponentName paramComponentName, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Bitmap paramBitmap, int[] paramArrayOfInt)
  {
    String str = paramComponentName.getPackageName();
    if (paramInt5 < 0) {
      paramInt5 = 2147483647;
    }
    if (paramInt6 < 0) {}
    Drawable localDrawable1 = null;
    if (paramInt1 != 0)
    {
      localDrawable1 = this.mPackageManager.getDrawable(str, paramInt1, null);
      if (localDrawable1 == null) {
        Log.w("WidgetPreviewLoader", "Can't load widget preview drawable 0x" + Integer.toHexString(paramInt1) + " for provider: " + paramComponentName);
      }
    }
    Bitmap localBitmap = null;
    int i;
    int m;
    int n;
    if (localDrawable1 != null)
    {
      i = 1;
      if (i == 0) {
        break label228;
      }
      m = localDrawable1.getIntrinsicWidth();
      n = localDrawable1.getIntrinsicHeight();
    }
    int i2;
    for (;;)
    {
      float f3 = 1.0F;
      if (paramArrayOfInt != null) {
        paramArrayOfInt[0] = m;
      }
      if (m > paramInt5) {
        f3 = paramInt5 / m;
      }
      if (f3 != 1.0F)
      {
        m = (int)(f3 * m);
        n = (int)(f3 * n);
      }
      if (paramBitmap == null) {
        paramBitmap = Bitmap.createBitmap(m, n, Bitmap.Config.ARGB_8888);
      }
      i2 = (paramBitmap.getWidth() - m) / 2;
      if (i == 0) {
        break label493;
      }
      renderDrawableToBitmap(localDrawable1, paramBitmap, i2, 0, m, n);
      return paramBitmap;
      i = 0;
      break;
      label228:
      if (paramInt3 < 1) {
        paramInt3 = 1;
      }
      if (paramInt4 < 1) {
        paramInt4 = 1;
      }
      BitmapDrawable localBitmapDrawable = (BitmapDrawable)this.mContext.getResources().getDrawable(2130838130);
      int j = localBitmapDrawable.getIntrinsicWidth();
      int k = localBitmapDrawable.getIntrinsicHeight();
      m = j * paramInt3;
      n = k * paramInt4;
      localBitmap = Bitmap.createBitmap(m, n, Bitmap.Config.ARGB_8888);
      Canvas localCanvas1 = (Canvas)this.mCachedAppWidgetPreviewCanvas.get();
      localCanvas1.setBitmap(localBitmap);
      localBitmapDrawable.setBounds(0, 0, m, n);
      localBitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
      localBitmapDrawable.draw(localCanvas1);
      localCanvas1.setBitmap(null);
      int i1 = (int)(0.25F * this.mAppIconSize);
      float f1 = Math.min(Math.min(m, n) / (this.mAppIconSize + i1 * 2), 1.0F);
      float f2 = j;
      try
      {
        int i3 = (int)((f2 - f1 * this.mAppIconSize) / 2.0F);
        int i4 = (int)((k - f1 * this.mAppIconSize) / 2.0F);
        Drawable localDrawable2 = null;
        if (paramInt2 > 0) {
          localDrawable2 = this.mIconCache.getFullResIcon(str, paramInt2);
        }
        if (localDrawable2 != null) {
          renderDrawableToBitmap(localDrawable2, localBitmap, i3, i4, (int)(f1 * this.mAppIconSize), (int)(f1 * this.mAppIconSize));
        }
      }
      catch (Resources.NotFoundException localNotFoundException) {}
    }
    label493:
    Canvas localCanvas2 = (Canvas)this.mCachedAppWidgetPreviewCanvas.get();
    Rect localRect1 = (Rect)this.mCachedAppWidgetPreviewSrcRect.get();
    Rect localRect2 = (Rect)this.mCachedAppWidgetPreviewDestRect.get();
    localCanvas2.setBitmap(paramBitmap);
    localRect1.set(0, 0, localBitmap.getWidth(), localBitmap.getHeight());
    localRect2.set(i2, 0, i2 + m, n);
    Paint localPaint = (Paint)this.mCachedAppWidgetPreviewPaint.get();
    if (localPaint == null)
    {
      localPaint = new Paint();
      localPaint.setFilterBitmap(true);
      this.mCachedAppWidgetPreviewPaint.set(localPaint);
    }
    localCanvas2.drawBitmap(localBitmap, localRect1, localRect2, localPaint);
    localCanvas2.setBitmap(null);
    return paramBitmap;
  }
  
  public Bitmap getPreview(final Object paramObject)
  {
    String str1 = getObjectName(paramObject);
    String str2 = getObjectPackage(paramObject);
    for (;;)
    {
      synchronized (sInvalidPackages)
      {
        if (sInvalidPackages.contains(str2)) {
          break label470;
        }
        i = 1;
        if (i == 0) {
          return null;
        }
      }
      if (i != 0) {}
      Bitmap localBitmap1;
      for (;;)
      {
        ArrayList localArrayList;
        synchronized (this.mLoadedPreviews)
        {
          if ((this.mLoadedPreviews.containsKey(str1)) && (((WeakReference)this.mLoadedPreviews.get(str1)).get() != null))
          {
            Bitmap localBitmap4 = (Bitmap)((WeakReference)this.mLoadedPreviews.get(str1)).get();
            return localBitmap4;
          }
          localBitmap1 = null;
          localArrayList = this.mUnusedBitmaps;
          if (localBitmap1 == null) {}
        }
        try
        {
          if (((!localBitmap1.isMutable()) || (localBitmap1.getWidth() != this.mPreviewBitmapWidth) || (localBitmap1.getHeight() != this.mPreviewBitmapHeight)) && (this.mUnusedBitmaps.size() > 0))
          {
            localBitmap1 = (Bitmap)((SoftReference)this.mUnusedBitmaps.remove(0)).get();
            continue;
            localObject5 = finally;
            throw localObject5;
          }
          else
          {
            if (localBitmap1 != null)
            {
              Canvas localCanvas = (Canvas)this.mCachedAppWidgetPreviewCanvas.get();
              localCanvas.setBitmap(localBitmap1);
              localCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
              localCanvas.setBitmap(null);
            }
            if (localBitmap1 == null) {
              localBitmap1 = Bitmap.createBitmap(this.mPreviewBitmapWidth, this.mPreviewBitmapHeight, Bitmap.Config.ARGB_8888);
            }
            Bitmap localBitmap2 = null;
            if (i != 0) {
              localBitmap2 = readFromDb(str1, localBitmap1);
            }
            if (localBitmap2 != null) {
              synchronized (this.mLoadedPreviews)
              {
                this.mLoadedPreviews.put(str1, new WeakReference(localBitmap2));
                return localBitmap2;
              }
            }
            localBitmap3 = generatePreview(paramObject, localBitmap1);
          }
        }
        finally {}
      }
      final Bitmap localBitmap3;
      if (localBitmap3 != localBitmap1) {
        throw new RuntimeException("generatePreview is not recycling the bitmap " + paramObject);
      }
      synchronized (this.mLoadedPreviews)
      {
        this.mLoadedPreviews.put(str1, new WeakReference(localBitmap3));
        AsyncTask local1 = new AsyncTask()
        {
          public Void doInBackground(Void... paramAnonymousVarArgs)
          {
            WidgetPreviewLoader.this.writeToDb(paramObject, localBitmap3);
            return null;
          }
        };
        Executor localExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
        Void[] arrayOfVoid = new Void[1];
        arrayOfVoid[0] = ((Void)null);
        local1.executeOnExecutor(localExecutor, arrayOfVoid);
        return localBitmap3;
      }
      label470:
      int i = 0;
    }
  }
  
  public int maxHeightForWidgetPreview(int paramInt)
  {
    return Math.min(this.mPreviewBitmapHeight, this.mWidgetSpacingLayout.estimateCellHeight(paramInt));
  }
  
  public int maxWidthForWidgetPreview(int paramInt)
  {
    return Math.min(this.mPreviewBitmapWidth, this.mWidgetSpacingLayout.estimateCellWidth(paramInt));
  }
  
  public void recycleBitmap(Object paramObject, Bitmap paramBitmap)
  {
    String str = getObjectName(paramObject);
    synchronized (this.mLoadedPreviews)
    {
      Bitmap localBitmap;
      if (this.mLoadedPreviews.containsKey(str))
      {
        localBitmap = (Bitmap)((WeakReference)this.mLoadedPreviews.get(str)).get();
        if (localBitmap != paramBitmap) {
          break label115;
        }
        this.mLoadedPreviews.remove(str);
        if (!paramBitmap.isMutable()) {}
      }
      synchronized (this.mUnusedBitmaps)
      {
        this.mUnusedBitmaps.add(new SoftReference(localBitmap));
        return;
      }
    }
    label115:
    throw new RuntimeException("Bitmap passed in doesn't match up");
  }
  
  public void setPreviewSize(int paramInt1, int paramInt2, PagedViewCellLayout paramPagedViewCellLayout)
  {
    this.mPreviewBitmapWidth = paramInt1;
    this.mPreviewBitmapHeight = paramInt2;
    this.mSize = (paramInt1 + "x" + paramInt2);
    this.mWidgetSpacingLayout = paramPagedViewCellLayout;
  }
  
  static class CacheDb
    extends SQLiteOpenHelper
  {
    Context mContext;
    
    public CacheDb(Context paramContext)
    {
      super(new File(paramContext.getCacheDir(), "widgetpreviews.db").getPath(), null, 2);
      this.mContext = paramContext;
    }
    
    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS shortcut_and_widget_previews (name TEXT NOT NULL, size TEXT NOT NULL, preview_bitmap BLOB NOT NULL, PRIMARY KEY (name, size) );");
    }
    
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      if (paramInt1 != paramInt2) {
        paramSQLiteDatabase.execSQL("DELETE FROM shortcut_and_widget_previews");
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.WidgetPreviewLoader
 * JD-Core Version:    0.7.0.1
 */