package com.google.android.search.shared.ui;

import android.animation.TimeInterpolator;

public class HoldInterpolator
  implements TimeInterpolator
{
  public static final HoldInterpolator INSTANCE = new HoldInterpolator(false);
  public static final HoldInterpolator REVERSE_INSTANCE = new HoldInterpolator(true);
  private boolean mReverse;
  
  private HoldInterpolator(boolean paramBoolean)
  {
    this.mReverse = paramBoolean;
  }
  
  public float getInterpolation(float paramFloat)
  {
    if ((!this.mReverse) || (paramFloat == 1.0F)) {
      return 1.0F;
    }
    return 0.0F;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.HoldInterpolator
 * JD-Core Version:    0.7.0.1
 */