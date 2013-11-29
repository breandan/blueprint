package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import com.google.android.shared.util.LayoutUtils;

public class UndoDismissToast
  extends LinearLayout
{
  private ClipBoundsDrawable mButtonDrawable;
  private View mDivider;
  private int mMaxWidth;
  private boolean mRtl;
  private View mUndoButton;
  
  public UndoDismissToast(Context paramContext)
  {
    super(paramContext);
  }
  
  public UndoDismissToast(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public UndoDismissToast(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mRtl = LayoutUtils.isDefaultLocaleRtl();
    this.mUndoButton = findViewById(2131297188);
    this.mDivider = findViewById(2131296675);
    Drawable localDrawable = this.mUndoButton.getBackground();
    localDrawable.setColorFilter(1724697804, PorterDuff.Mode.SRC_IN);
    this.mUndoButton.setBackground(null);
    this.mButtonDrawable = new ClipBoundsDrawable(localDrawable);
    this.mUndoButton.setBackground(this.mButtonDrawable);
    this.mMaxWidth = getContext().getResources().getDimensionPixelOffset(2131689886);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    ClipBoundsDrawable localClipBoundsDrawable = this.mButtonDrawable;
    int i;
    if (this.mRtl)
    {
      i = 0;
      if (!this.mRtl) {
        break label73;
      }
    }
    label73:
    for (int j = this.mDivider.getRight();; j = this.mUndoButton.getWidth())
    {
      localClipBoundsDrawable.setClipBounds(i, 0, j, this.mUndoButton.getHeight());
      return;
      i = this.mDivider.getLeft();
      break;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (View.MeasureSpec.getMode(paramInt1) == -2147483648)
    {
      int i = View.MeasureSpec.getSize(paramInt1);
      if (i > this.mMaxWidth) {
        i = this.mMaxWidth;
      }
      paramInt1 = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    }
    super.onMeasure(paramInt1, paramInt2);
  }
  
  private static class ClipBoundsDrawable
    extends ClipDrawable
  {
    private final Rect mClipRect = new Rect();
    private final Drawable mDrawable;
    
    public ClipBoundsDrawable(Drawable paramDrawable)
    {
      super(8388611, 1);
      this.mDrawable = paramDrawable;
    }
    
    public void draw(Canvas paramCanvas)
    {
      paramCanvas.save();
      paramCanvas.clipRect(this.mClipRect);
      this.mDrawable.draw(paramCanvas);
      paramCanvas.restore();
    }
    
    public void setClipBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this.mClipRect.left = paramInt1;
      this.mClipRect.top = paramInt2;
      this.mClipRect.right = paramInt3;
      this.mClipRect.bottom = paramInt4;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.UndoDismissToast
 * JD-Core Version:    0.7.0.1
 */