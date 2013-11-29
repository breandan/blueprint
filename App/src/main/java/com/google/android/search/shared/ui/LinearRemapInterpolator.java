package com.google.android.search.shared.ui;

import android.animation.TimeInterpolator;
import com.google.common.base.Preconditions;

public class LinearRemapInterpolator
  implements TimeInterpolator
{
  private final TimeInterpolator mInterpolator;
  private final float mSpan;
  private final float mStart;
  
  public LinearRemapInterpolator(TimeInterpolator paramTimeInterpolator, float paramFloat1, float paramFloat2)
  {
    if ((0.0F <= paramFloat1) && (paramFloat1 < paramFloat2) && (paramFloat2 <= 1.0F)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      this.mInterpolator = paramTimeInterpolator;
      this.mStart = paramFloat1;
      this.mSpan = (paramFloat2 - paramFloat1);
      return;
    }
  }
  
  public float getInterpolation(float paramFloat)
  {
    float f = Math.max(Math.min((paramFloat - this.mStart) / this.mSpan, 1.0F), 0.0F);
    return this.mInterpolator.getInterpolation(f);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.LinearRemapInterpolator
 * JD-Core Version:    0.7.0.1
 */