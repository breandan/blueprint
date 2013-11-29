package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PageIndicatorMarker
  extends FrameLayout
{
  private ImageView mActiveMarker;
  private ImageView mInactiveMarker;
  private boolean mIsActive = false;
  
  public PageIndicatorMarker(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PageIndicatorMarker(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PageIndicatorMarker(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  void activate(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mActiveMarker.animate().cancel();
      this.mActiveMarker.setAlpha(1.0F);
      this.mActiveMarker.setScaleX(1.0F);
      this.mActiveMarker.setScaleY(1.0F);
      this.mInactiveMarker.animate().cancel();
      this.mInactiveMarker.setAlpha(0.0F);
    }
    for (;;)
    {
      this.mIsActive = true;
      return;
      this.mActiveMarker.animate().alpha(1.0F).scaleX(1.0F).scaleY(1.0F).setDuration(175L).start();
      this.mInactiveMarker.animate().alpha(0.0F).setDuration(175L).start();
    }
  }
  
  void inactivate(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mInactiveMarker.animate().cancel();
      this.mInactiveMarker.setAlpha(1.0F);
      this.mActiveMarker.animate().cancel();
      this.mActiveMarker.setAlpha(0.0F);
      this.mActiveMarker.setScaleX(0.5F);
      this.mActiveMarker.setScaleY(0.5F);
    }
    for (;;)
    {
      this.mIsActive = false;
      return;
      this.mInactiveMarker.animate().alpha(1.0F).setDuration(175L).start();
      this.mActiveMarker.animate().alpha(0.0F).scaleX(0.5F).scaleY(0.5F).setDuration(175L).start();
    }
  }
  
  protected void onFinishInflate()
  {
    this.mActiveMarker = ((ImageView)findViewById(2131296506));
    this.mInactiveMarker = ((ImageView)findViewById(2131296505));
  }
  
  void setMarkerDrawables(int paramInt1, int paramInt2)
  {
    Resources localResources = getResources();
    this.mActiveMarker.setImageDrawable(localResources.getDrawable(paramInt1));
    this.mInactiveMarker.setImageDrawable(localResources.getDrawable(paramInt2));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.PageIndicatorMarker
 * JD-Core Version:    0.7.0.1
 */