package com.android.datetimepicker.time;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import com.android.datetimepicker.R.color;
import com.android.datetimepicker.R.string;

public class CircleView
  extends View
{
  private float mAmPmCircleRadiusMultiplier;
  private int mCircleColor;
  private int mCircleRadius;
  private float mCircleRadiusMultiplier;
  private int mDotColor;
  private boolean mDrawValuesReady;
  private boolean mIs24HourMode;
  private boolean mIsInitialized;
  private final Paint mPaint = new Paint();
  private int mXCenter;
  private int mYCenter;
  
  public CircleView(Context paramContext)
  {
    super(paramContext);
    Resources localResources = paramContext.getResources();
    this.mCircleColor = localResources.getColor(R.color.white);
    this.mDotColor = localResources.getColor(R.color.numbers_text_color);
    this.mPaint.setAntiAlias(true);
    this.mIsInitialized = false;
  }
  
  public void initialize(Context paramContext, boolean paramBoolean)
  {
    if (this.mIsInitialized)
    {
      Log.e("CircleView", "CircleView may only be initialized once.");
      return;
    }
    Resources localResources = paramContext.getResources();
    this.mIs24HourMode = paramBoolean;
    if (paramBoolean) {
      this.mCircleRadiusMultiplier = Float.parseFloat(localResources.getString(R.string.circle_radius_multiplier_24HourMode));
    }
    for (;;)
    {
      this.mIsInitialized = true;
      return;
      this.mCircleRadiusMultiplier = Float.parseFloat(localResources.getString(R.string.circle_radius_multiplier));
      this.mAmPmCircleRadiusMultiplier = Float.parseFloat(localResources.getString(R.string.ampm_circle_radius_multiplier));
    }
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    if ((getWidth() == 0) || (!this.mIsInitialized)) {
      return;
    }
    if (!this.mDrawValuesReady)
    {
      this.mXCenter = (getWidth() / 2);
      this.mYCenter = (getHeight() / 2);
      this.mCircleRadius = ((int)(Math.min(this.mXCenter, this.mYCenter) * this.mCircleRadiusMultiplier));
      if (!this.mIs24HourMode)
      {
        int i = (int)(this.mCircleRadius * this.mAmPmCircleRadiusMultiplier);
        this.mYCenter -= i / 2;
      }
      this.mDrawValuesReady = true;
    }
    this.mPaint.setColor(this.mCircleColor);
    paramCanvas.drawCircle(this.mXCenter, this.mYCenter, this.mCircleRadius, this.mPaint);
    this.mPaint.setColor(this.mDotColor);
    paramCanvas.drawCircle(this.mXCenter, this.mYCenter, 2.0F, this.mPaint);
  }
  
  void setTheme(Context paramContext, boolean paramBoolean)
  {
    Resources localResources = paramContext.getResources();
    if (paramBoolean)
    {
      this.mCircleColor = localResources.getColor(R.color.dark_gray);
      this.mDotColor = localResources.getColor(R.color.light_gray);
      return;
    }
    this.mCircleColor = localResources.getColor(R.color.white);
    this.mDotColor = localResources.getColor(R.color.numbers_text_color);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.time.CircleView
 * JD-Core Version:    0.7.0.1
 */