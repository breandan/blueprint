package com.android.gallery3d.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.FloatMath;

public class BitmapUtils
{
  public static int computeSampleSizeLarger(float paramFloat)
  {
    int i = (int)FloatMath.floor(1.0F / paramFloat);
    if (i <= 1) {
      return 1;
    }
    if (i <= 8) {
      return Utils.prevPowerOf2(i);
    }
    return 8 * (i / 8);
  }
  
  private static Bitmap.Config getConfig(Bitmap paramBitmap)
  {
    Bitmap.Config localConfig = paramBitmap.getConfig();
    if (localConfig == null) {
      localConfig = Bitmap.Config.ARGB_8888;
    }
    return localConfig;
  }
  
  public static Bitmap resizeBitmapByScale(Bitmap paramBitmap, float paramFloat, boolean paramBoolean)
  {
    int i = Math.round(paramFloat * paramBitmap.getWidth());
    int j = Math.round(paramFloat * paramBitmap.getHeight());
    if ((i == paramBitmap.getWidth()) && (j == paramBitmap.getHeight())) {
      return paramBitmap;
    }
    Bitmap localBitmap = Bitmap.createBitmap(i, j, getConfig(paramBitmap));
    Canvas localCanvas = new Canvas(localBitmap);
    localCanvas.scale(paramFloat, paramFloat);
    localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, new Paint(6));
    if (paramBoolean) {
      paramBitmap.recycle();
    }
    return localBitmap;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.common.BitmapUtils
 * JD-Core Version:    0.7.0.1
 */