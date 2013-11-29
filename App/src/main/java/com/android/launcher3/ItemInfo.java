package com.android.launcher3;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ItemInfo
{
  int cellX = -1;
  int cellY = -1;
  long container = -1L;
  int[] dropPos = null;
  long id = -1L;
  int itemType;
  int minSpanX = 1;
  int minSpanY = 1;
  boolean requiresDbUpdate = false;
  long screenId = -1L;
  int spanX = 1;
  int spanY = 1;
  CharSequence title;
  
  ItemInfo() {}
  
  ItemInfo(ItemInfo paramItemInfo)
  {
    this.id = paramItemInfo.id;
    this.cellX = paramItemInfo.cellX;
    this.cellY = paramItemInfo.cellY;
    this.spanX = paramItemInfo.spanX;
    this.spanY = paramItemInfo.spanY;
    this.screenId = paramItemInfo.screenId;
    this.itemType = paramItemInfo.itemType;
    this.container = paramItemInfo.container;
    LauncherModel.checkItemInfo(this);
  }
  
  static byte[] flattenBitmap(Bitmap paramBitmap)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(4 * (paramBitmap.getWidth() * paramBitmap.getHeight()));
    try
    {
      paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
      localByteArrayOutputStream.flush();
      localByteArrayOutputStream.close();
      byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      Log.w("Favorite", "Could not write icon");
    }
    return null;
  }
  
  static void writeBitmap(ContentValues paramContentValues, Bitmap paramBitmap)
  {
    if (paramBitmap != null) {
      paramContentValues.put("icon", flattenBitmap(paramBitmap));
    }
  }
  
  protected Intent getIntent()
  {
    throw new RuntimeException("Unexpected Intent");
  }
  
  void onAddToDatabase(ContentValues paramContentValues)
  {
    paramContentValues.put("itemType", Integer.valueOf(this.itemType));
    paramContentValues.put("container", Long.valueOf(this.container));
    paramContentValues.put("screen", Long.valueOf(this.screenId));
    paramContentValues.put("cellX", Integer.valueOf(this.cellX));
    paramContentValues.put("cellY", Integer.valueOf(this.cellY));
    paramContentValues.put("spanX", Integer.valueOf(this.spanX));
    paramContentValues.put("spanY", Integer.valueOf(this.spanY));
  }
  
  public String toString()
  {
    return "Item(id=" + this.id + " type=" + this.itemType + " container=" + this.container + " screen=" + this.screenId + " cellX=" + this.cellX + " cellY=" + this.cellY + " spanX=" + this.spanX + " spanY=" + this.spanY + " dropPos=" + this.dropPos + ")";
  }
  
  void unbind() {}
  
  void updateValuesWithCoordinates(ContentValues paramContentValues, int paramInt1, int paramInt2)
  {
    paramContentValues.put("cellX", Integer.valueOf(paramInt1));
    paramContentValues.put("cellY", Integer.valueOf(paramInt2));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.ItemInfo
 * JD-Core Version:    0.7.0.1
 */