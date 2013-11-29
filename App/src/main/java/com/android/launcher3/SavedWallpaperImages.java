package com.android.launcher3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import com.android.photos.BitmapRegionTileSource.FilePathBitmapSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SavedWallpaperImages
  extends BaseAdapter
  implements ListAdapter
{
  private static String TAG = "Launcher3.SavedWallpaperImages";
  Context mContext;
  private ImageDb mDb;
  ArrayList<SavedWallpaperTile> mImages;
  LayoutInflater mLayoutInflater;
  
  public SavedWallpaperImages(Activity paramActivity)
  {
    this.mDb = new ImageDb(paramActivity);
    this.mContext = paramActivity;
    this.mLayoutInflater = paramActivity.getLayoutInflater();
  }
  
  private Pair<String, String> getImageFilenames(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb.getReadableDatabase();
    String[] arrayOfString1 = { "image_thumbnail", "image" };
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = Integer.toString(paramInt);
    Cursor localCursor = localSQLiteDatabase.query("saved_wallpaper_images", arrayOfString1, "id = ?", arrayOfString2, null, null, null, null);
    int i = localCursor.getCount();
    Pair localPair = null;
    if (i > 0)
    {
      localCursor.moveToFirst();
      String str1 = localCursor.getString(0);
      String str2 = localCursor.getString(1);
      localCursor.close();
      localPair = new Pair(str1, str2);
    }
    return localPair;
  }
  
  public void deleteImage(int paramInt)
  {
    Pair localPair = getImageFilenames(paramInt);
    new File(this.mContext.getFilesDir(), (String)localPair.first).delete();
    new File(this.mContext.getFilesDir(), (String)localPair.second).delete();
    SQLiteDatabase localSQLiteDatabase = this.mDb.getWritableDatabase();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Integer.toString(paramInt);
    localSQLiteDatabase.delete("saved_wallpaper_images", "id = ?", arrayOfString);
  }
  
  public int getCount()
  {
    return this.mImages.size();
  }
  
  public String getImageFilename(int paramInt)
  {
    Pair localPair = getImageFilenames(paramInt);
    if (localPair != null) {
      return (String)localPair.second;
    }
    return null;
  }
  
  public SavedWallpaperTile getItem(int paramInt)
  {
    return (SavedWallpaperTile)this.mImages.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Drawable localDrawable = ((SavedWallpaperTile)this.mImages.get(paramInt)).mThumb;
    if (localDrawable == null) {
      Log.e(TAG, "Error decoding thumbnail for wallpaper #" + paramInt);
    }
    return WallpaperPickerActivity.createImageTileView(this.mLayoutInflater, paramInt, paramView, paramViewGroup, localDrawable);
  }
  
  public void loadThumbnailsAndImageIdList()
  {
    this.mImages = new ArrayList();
    Cursor localCursor = this.mDb.getReadableDatabase().query("saved_wallpaper_images", new String[] { "id", "image_thumbnail" }, null, null, null, null, "id DESC", null);
    while (localCursor.moveToNext())
    {
      String str = localCursor.getString(1);
      Bitmap localBitmap = BitmapFactory.decodeFile(new File(this.mContext.getFilesDir(), str).getAbsolutePath());
      if (localBitmap != null) {
        this.mImages.add(new SavedWallpaperTile(localCursor.getInt(0), new BitmapDrawable(localBitmap)));
      }
    }
    localCursor.close();
  }
  
  public void writeImage(Bitmap paramBitmap, byte[] paramArrayOfByte)
  {
    try
    {
      File localFile1 = File.createTempFile("wallpaper", "", this.mContext.getFilesDir());
      FileOutputStream localFileOutputStream1 = this.mContext.openFileOutput(localFile1.getName(), 0);
      localFileOutputStream1.write(paramArrayOfByte);
      localFileOutputStream1.close();
      File localFile2 = File.createTempFile("wallpaperthumb", "", this.mContext.getFilesDir());
      FileOutputStream localFileOutputStream2 = this.mContext.openFileOutput(localFile2.getName(), 0);
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 95, localFileOutputStream2);
      localFileOutputStream2.close();
      SQLiteDatabase localSQLiteDatabase = this.mDb.getWritableDatabase();
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("image_thumbnail", localFile2.getName());
      localContentValues.put("image", localFile1.getName());
      localSQLiteDatabase.insert("saved_wallpaper_images", null, localContentValues);
      return;
    }
    catch (IOException localIOException)
    {
      Log.e(TAG, "Failed writing images to storage " + localIOException);
    }
  }
  
  static class ImageDb
    extends SQLiteOpenHelper
  {
    Context mContext;
    
    public ImageDb(Context paramContext)
    {
      super(new File(paramContext.getCacheDir(), "saved_wallpaper_images.db").getPath(), null, 1);
      this.mContext = paramContext;
    }
    
    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS saved_wallpaper_images (id INTEGER NOT NULL, image_thumbnail TEXT NOT NULL, image TEXT NOT NULL, PRIMARY KEY (id ASC) );");
    }
    
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      if (paramInt1 != paramInt2) {
        paramSQLiteDatabase.execSQL("DELETE FROM saved_wallpaper_images");
      }
    }
  }
  
  public static class SavedWallpaperTile
    extends WallpaperPickerActivity.WallpaperTileInfo
  {
    private int mDbId;
    private Drawable mThumb;
    
    public SavedWallpaperTile(int paramInt, Drawable paramDrawable)
    {
      this.mDbId = paramInt;
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
      String str = paramWallpaperPickerActivity.getSavedImages().getImageFilename(this.mDbId);
      paramWallpaperPickerActivity.setCropViewTileSource(new BitmapRegionTileSource.FilePathBitmapSource(new File(paramWallpaperPickerActivity.getFilesDir(), str).getAbsolutePath(), 1024), false, true, null);
    }
    
    public void onDelete(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      paramWallpaperPickerActivity.getSavedImages().deleteImage(this.mDbId);
    }
    
    public void onSave(WallpaperPickerActivity paramWallpaperPickerActivity)
    {
      paramWallpaperPickerActivity.setWallpaper(paramWallpaperPickerActivity.getSavedImages().getImageFilename(this.mDbId), true);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.SavedWallpaperImages
 * JD-Core Version:    0.7.0.1
 */