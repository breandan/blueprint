package android.support.v4.view;

import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.view.View;

class ViewCompatHC
{
  public static float getAlpha(View paramView)
  {
    return paramView.getAlpha();
  }
  
  static long getFrameTime()
  {
    return ValueAnimator.getFrameDelay();
  }
  
  public static void setLayerType(View paramView, int paramInt, Paint paramPaint)
  {
    paramView.setLayerType(paramInt, paramPaint);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewCompatHC
 * JD-Core Version:    0.7.0.1
 */