package com.google.android.velvet.tg;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.RelativeLayout;

@TargetApi(17)
public class SetupWizardOuterFrame
  extends RelativeLayout
{
  public SetupWizardOuterFrame(Context paramContext)
  {
    super(paramContext);
  }
  
  public SetupWizardOuterFrame(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public SetupWizardOuterFrame(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    Resources localResources = getContext().getResources();
    float f1 = localResources.getFraction(2131689676, 1, 1);
    float f2 = localResources.getFraction(2131689677, 1, 1);
    int k = localResources.getDimensionPixelSize(2131689678);
    setPaddingRelative((int)(f2 * i), 0, (int)(f2 * i), k);
    View localView = findViewById(2131296991);
    if (localView != null) {
      localView.setMinimumHeight((int)(f1 * j));
    }
    super.onMeasure(paramInt1, paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.tg.SetupWizardOuterFrame
 * JD-Core Version:    0.7.0.1
 */