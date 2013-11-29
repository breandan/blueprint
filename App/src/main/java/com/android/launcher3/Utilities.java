package com.android.launcher3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;

final class Utilities
{
  private static final Paint sBlurPaint;
  private static final Canvas sCanvas;
  static int sColorIndex = 0;
  static int[] sColors;
  private static final Paint sDisabledPaint;
  private static final Paint sGlowColorFocusedPaint;
  private static final Paint sGlowColorPressedPaint;
  private static int sIconHeight;
  public static int sIconTextureHeight;
  public static int sIconTextureWidth;
  private static int sIconWidth = -1;
  private static final Rect sOldBounds;
  
  static
  {
    sIconHeight = -1;
    sIconTextureWidth = -1;
    sIconTextureHeight = -1;
    sBlurPaint = new Paint();
    sGlowColorPressedPaint = new Paint();
    sGlowColorFocusedPaint = new Paint();
    sDisabledPaint = new Paint();
    sOldBounds = new Rect();
    sCanvas = new Canvas();
    sCanvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
    sColors = new int[] { -65536, -16711936, -16776961 };
  }
  
  static Bitmap createIconBitmap(Bitmap paramBitmap, Context paramContext)
  {
    int i = sIconTextureWidth;
    int j = sIconTextureHeight;
    int k = paramBitmap.getWidth();
    int m = paramBitmap.getHeight();
    if ((k > i) && (m > j)) {
      paramBitmap = Bitmap.createBitmap(paramBitmap, (k - i) / 2, (m - j) / 2, i, j);
    }
    while ((k == i) && (m == j)) {
      return paramBitmap;
    }
    return createIconBitmap(new BitmapDrawable(paramContext.getResources(), paramBitmap), paramContext);
  }
  
  static Bitmap createIconBitmap(Drawable paramDrawable, Context paramContext)
  {
    for (;;)
    {
      int i;
      int j;
      int k;
      int m;
      float f;
      synchronized (sCanvas)
      {
        if (sIconWidth == -1) {
          initStatics(paramContext);
        }
        i = sIconWidth;
        j = sIconHeight;
        if ((paramDrawable instanceof PaintDrawable))
        {
          PaintDrawable localPaintDrawable = (PaintDrawable)paramDrawable;
          localPaintDrawable.setIntrinsicWidth(i);
          localPaintDrawable.setIntrinsicHeight(j);
          k = paramDrawable.getIntrinsicWidth();
          m = paramDrawable.getIntrinsicHeight();
          if ((k > 0) && (m > 0))
          {
            f = k / m;
            if (k > m) {
              j = (int)(i / f);
            }
          }
          else
          {
            int n = sIconTextureWidth;
            int i1 = sIconTextureHeight;
            Bitmap localBitmap = Bitmap.createBitmap(n, i1, Bitmap.Config.ARGB_8888);
            Canvas localCanvas2 = sCanvas;
            localCanvas2.setBitmap(localBitmap);
            int i2 = (n - i) / 2;
            int i3 = (i1 - j) / 2;
            sOldBounds.set(paramDrawable.getBounds());
            paramDrawable.setBounds(i2, i3, i2 + i, i3 + j);
            paramDrawable.draw(localCanvas2);
            paramDrawable.setBounds(sOldBounds);
            localCanvas2.setBitmap(null);
            return localBitmap;
          }
        }
        else
        {
          if (!(paramDrawable instanceof BitmapDrawable)) {
            continue;
          }
          BitmapDrawable localBitmapDrawable = (BitmapDrawable)paramDrawable;
          if (localBitmapDrawable.getBitmap().getDensity() != 0) {
            continue;
          }
          localBitmapDrawable.setTargetDensity(paramContext.getResources().getDisplayMetrics());
        }
      }
      if (m > k) {
        i = (int)(f * j);
      }
    }
  }
  
  static Drawable createIconDrawable(Bitmap paramBitmap)
  {
    FastBitmapDrawable localFastBitmapDrawable = new FastBitmapDrawable(paramBitmap);
    localFastBitmapDrawable.setFilterBitmap(true);
    resizeIconDrawable(localFastBitmapDrawable);
    return localFastBitmapDrawable;
  }
  
  public static float getDescendantCoordRelativeToParent(View paramView1, View paramView2, int[] paramArrayOfInt, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = paramArrayOfInt[0];
    arrayOfFloat[1] = paramArrayOfInt[1];
    for (View localView1 = paramView1; (localView1 != paramView2) && (localView1 != null); localView1 = (View)localView1.getParent()) {
      localArrayList.add(localView1);
    }
    localArrayList.add(paramView2);
    float f = 1.0F;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
    {
      View localView2 = (View)localArrayList.get(j);
      if ((localView2 != paramView1) || (paramBoolean))
      {
        arrayOfFloat[0] -= localView2.getScrollX();
        arrayOfFloat[1] -= localView2.getScrollY();
      }
      localView2.getMatrix().mapPoints(arrayOfFloat);
      arrayOfFloat[0] += localView2.getLeft();
      arrayOfFloat[1] += localView2.getTop();
      f *= localView2.getScaleX();
    }
    paramArrayOfInt[0] = Math.round(arrayOfFloat[0]);
    paramArrayOfInt[1] = Math.round(arrayOfFloat[1]);
    return f;
  }
  
  private static void initStatics(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    float f = localResources.getDisplayMetrics().density;
    int i = (int)localResources.getDimension(2131689535);
    sIconHeight = i;
    sIconWidth = i;
    int j = sIconWidth;
    sIconTextureHeight = j;
    sIconTextureWidth = j;
    sBlurPaint.setMaskFilter(new BlurMaskFilter(5.0F * f, BlurMaskFilter.Blur.NORMAL));
    sGlowColorPressedPaint.setColor(-15616);
    sGlowColorFocusedPaint.setColor(-29184);
    ColorMatrix localColorMatrix = new ColorMatrix();
    localColorMatrix.setSaturation(0.2F);
    sDisabledPaint.setColorFilter(new ColorMatrixColorFilter(localColorMatrix));
    sDisabledPaint.setAlpha(136);
  }
  
  public static float mapCoordInSelfToDescendent(View paramView1, View paramView2, int[] paramArrayOfInt)
  {
    ArrayList localArrayList = new ArrayList();
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = paramArrayOfInt[0];
    arrayOfFloat[1] = paramArrayOfInt[1];
    for (View localView1 = paramView1; localView1 != paramView2; localView1 = (View)localView1.getParent()) {
      localArrayList.add(localView1);
    }
    localArrayList.add(paramView2);
    float f = 1.0F;
    Matrix localMatrix = new Matrix();
    int i = -1 + localArrayList.size();
    if (i >= 0)
    {
      View localView2 = (View)localArrayList.get(i);
      if (i > 0) {}
      for (View localView3 = (View)localArrayList.get(i - 1);; localView3 = null)
      {
        arrayOfFloat[0] += localView2.getScrollX();
        arrayOfFloat[1] += localView2.getScrollY();
        if (localView3 != null)
        {
          arrayOfFloat[0] -= localView3.getLeft();
          arrayOfFloat[1] -= localView3.getTop();
          localView3.getMatrix().invert(localMatrix);
          localMatrix.mapPoints(arrayOfFloat);
          f *= localView3.getScaleX();
        }
        i--;
        break;
      }
    }
    paramArrayOfInt[0] = Math.round(arrayOfFloat[0]);
    paramArrayOfInt[1] = Math.round(arrayOfFloat[1]);
    return f;
  }
  
  static Bitmap resampleIconBitmap(Bitmap paramBitmap, Context paramContext)
  {
    synchronized (sCanvas)
    {
      if (sIconWidth == -1) {
        initStatics(paramContext);
      }
      if ((paramBitmap.getWidth() == sIconWidth) && (paramBitmap.getHeight() == sIconHeight)) {
        return paramBitmap;
      }
      Bitmap localBitmap = createIconBitmap(new BitmapDrawable(paramContext.getResources(), paramBitmap), paramContext);
      return localBitmap;
    }
  }
  
  static void resizeIconDrawable(Drawable paramDrawable)
  {
    paramDrawable.setBounds(0, 0, sIconTextureWidth, sIconTextureHeight);
  }
  
  public static void scaleRect(Rect paramRect, float paramFloat)
  {
    if (paramFloat != 1.0F)
    {
      paramRect.left = ((int)(0.5F + paramFloat * paramRect.left));
      paramRect.top = ((int)(0.5F + paramFloat * paramRect.top));
      paramRect.right = ((int)(0.5F + paramFloat * paramRect.right));
      paramRect.bottom = ((int)(0.5F + paramFloat * paramRect.bottom));
    }
  }
  
  public static void scaleRectAboutCenter(Rect paramRect, float paramFloat)
  {
    int i = paramRect.centerX();
    int j = paramRect.centerY();
    paramRect.offset(-i, -j);
    scaleRect(paramRect, paramFloat);
    paramRect.offset(i, j);
  }
  
  public static void setIconSize(int paramInt)
  {
    sIconHeight = paramInt;
    sIconWidth = paramInt;
    sIconTextureHeight = paramInt;
    sIconTextureWidth = paramInt;
  }
  
  public static void startActivityForResultSafely(Activity paramActivity, Intent paramIntent, int paramInt)
  {
    try
    {
      paramActivity.startActivityForResult(paramIntent, paramInt);
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Toast.makeText(paramActivity, 2131361872, 0).show();
      return;
    }
    catch (SecurityException localSecurityException)
    {
      Toast.makeText(paramActivity, 2131361872, 0).show();
      Log.e("Launcher.Utilities", "Launcher does not have the permission to launch " + paramIntent + ". Make sure to create a MAIN intent-filter for the corresponding activity " + "or use the exported attribute for this activity.", localSecurityException);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.Utilities
 * JD-Core Version:    0.7.0.1
 */