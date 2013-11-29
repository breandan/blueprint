package com.android.datetimepicker;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.view.View;

public class Utils
{
  public static int getDaysInMonth(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IllegalArgumentException("Invalid Month");
    case 0: 
    case 2: 
    case 4: 
    case 6: 
    case 7: 
    case 9: 
    case 11: 
      return 31;
    case 3: 
    case 5: 
    case 8: 
    case 10: 
      return 30;
    }
    if (paramInt2 % 4 == 0) {
      return 29;
    }
    return 28;
  }
  
  public static ObjectAnimator getPulseAnimator(View paramView, float paramFloat1, float paramFloat2)
  {
    Keyframe localKeyframe1 = Keyframe.ofFloat(0.0F, 1.0F);
    Keyframe localKeyframe2 = Keyframe.ofFloat(0.275F, paramFloat1);
    Keyframe localKeyframe3 = Keyframe.ofFloat(0.69F, paramFloat2);
    Keyframe localKeyframe4 = Keyframe.ofFloat(1.0F, 1.0F);
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(paramView, new PropertyValuesHolder[] { PropertyValuesHolder.ofKeyframe("scaleX", new Keyframe[] { localKeyframe1, localKeyframe2, localKeyframe3, localKeyframe4 }), PropertyValuesHolder.ofKeyframe("scaleY", new Keyframe[] { localKeyframe1, localKeyframe2, localKeyframe3, localKeyframe4 }) });
    localObjectAnimator.setDuration(544L);
    return localObjectAnimator;
  }
  
  public static boolean isJellybeanOrLater()
  {
    return Build.VERSION.SDK_INT >= 16;
  }
  
  @SuppressLint({"NewApi"})
  public static void tryAccessibilityAnnounce(View paramView, CharSequence paramCharSequence)
  {
    if ((isJellybeanOrLater()) && (paramView != null) && (paramCharSequence != null)) {
      paramView.announceForAccessibility(paramCharSequence);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.Utils
 * JD-Core Version:    0.7.0.1
 */