package com.google.android.search.shared.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.google.common.base.Preconditions;

public class ClippedWebImageView
  extends WebImageView
{
  private Rect mClipRect;
  
  public ClippedWebImageView(Context paramContext)
  {
    super(paramContext);
  }
  
  public ClippedWebImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public ClippedWebImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private Bitmap clip(Bitmap paramBitmap)
  {
    Preconditions.checkNotNull(paramBitmap);
    Preconditions.checkNotNull(this.mClipRect);
    return Bitmap.createBitmap(paramBitmap, this.mClipRect.left, this.mClipRect.top, this.mClipRect.width(), this.mClipRect.height());
  }
  
  public void setClipRect(Rect paramRect)
  {
    this.mClipRect = paramRect;
  }
  
  public void setImageDrawable(Drawable paramDrawable)
  {
    Bitmap localBitmap;
    if ((this.mClipRect != null) && (paramDrawable != null))
    {
      if (!(paramDrawable instanceof BitmapDrawable)) {
        break label49;
      }
      localBitmap = ((BitmapDrawable)paramDrawable).getBitmap();
    }
    for (;;)
    {
      paramDrawable = new BitmapDrawable(getResources(), clip(localBitmap));
      super.setImageDrawable(paramDrawable);
      return;
      label49:
      localBitmap = Bitmap.createBitmap(paramDrawable.getIntrinsicWidth(), paramDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      paramDrawable.setBounds(0, 0, localCanvas.getWidth(), localCanvas.getHeight());
      paramDrawable.draw(localCanvas);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.ClippedWebImageView
 * JD-Core Version:    0.7.0.1
 */