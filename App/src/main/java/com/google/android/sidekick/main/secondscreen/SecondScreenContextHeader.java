package com.google.android.sidekick.main.secondscreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.search.shared.ui.PathClippingView;
import javax.annotation.Nullable;

public class SecondScreenContextHeader
  extends RelativeLayout
  implements PathClippingView
{
  private Path mClipPath;
  
  public SecondScreenContextHeader(Context paramContext)
  {
    super(paramContext);
  }
  
  public SecondScreenContextHeader(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public SecondScreenContextHeader(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    if (this.mClipPath != null)
    {
      paramCanvas.save();
      paramCanvas.clipPath(this.mClipPath);
      boolean bool = super.drawChild(paramCanvas, paramView, paramLong);
      paramCanvas.restore();
      return bool;
    }
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  public void setClipPath(@Nullable Path paramPath)
  {
    this.mClipPath = paramPath;
    invalidate();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.secondscreen.SecondScreenContextHeader
 * JD-Core Version:    0.7.0.1
 */