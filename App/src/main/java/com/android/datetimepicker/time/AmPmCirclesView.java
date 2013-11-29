package com.android.datetimepicker.time;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import com.android.datetimepicker.R.color;
import com.android.datetimepicker.R.string;
import java.text.DateFormatSymbols;

public class AmPmCirclesView
  extends View
{
  private int mAmOrPm;
  private int mAmOrPmPressed;
  private int mAmPmCircleRadius;
  private float mAmPmCircleRadiusMultiplier;
  private int mAmPmTextColor;
  private int mAmPmYCenter;
  private String mAmText;
  private int mAmXCenter;
  private float mCircleRadiusMultiplier;
  private boolean mDrawValuesReady;
  private boolean mIsInitialized = false;
  private final Paint mPaint = new Paint();
  private String mPmText;
  private int mPmXCenter;
  private int mSelectedAlpha;
  private int mSelectedColor;
  private int mUnselectedColor;
  
  public AmPmCirclesView(Context paramContext)
  {
    super(paramContext);
  }
  
  public int getIsTouchingAmOrPm(float paramFloat1, float paramFloat2)
  {
    if (!this.mDrawValuesReady) {}
    int i;
    do
    {
      return -1;
      i = (int)((paramFloat2 - this.mAmPmYCenter) * (paramFloat2 - this.mAmPmYCenter));
      if ((int)Math.sqrt((paramFloat1 - this.mAmXCenter) * (paramFloat1 - this.mAmXCenter) + i) <= this.mAmPmCircleRadius) {
        return 0;
      }
    } while ((int)Math.sqrt((paramFloat1 - this.mPmXCenter) * (paramFloat1 - this.mPmXCenter) + i) > this.mAmPmCircleRadius);
    return 1;
  }
  
  public void initialize(Context paramContext, int paramInt)
  {
    if (this.mIsInitialized)
    {
      Log.e("AmPmCirclesView", "AmPmCirclesView may only be initialized once.");
      return;
    }
    Resources localResources = paramContext.getResources();
    this.mUnselectedColor = localResources.getColor(R.color.white);
    this.mSelectedColor = localResources.getColor(R.color.blue);
    this.mAmPmTextColor = localResources.getColor(R.color.ampm_text_color);
    this.mSelectedAlpha = 51;
    Typeface localTypeface = Typeface.create(localResources.getString(R.string.sans_serif), 0);
    this.mPaint.setTypeface(localTypeface);
    this.mPaint.setAntiAlias(true);
    this.mPaint.setTextAlign(Paint.Align.CENTER);
    this.mCircleRadiusMultiplier = Float.parseFloat(localResources.getString(R.string.circle_radius_multiplier));
    this.mAmPmCircleRadiusMultiplier = Float.parseFloat(localResources.getString(R.string.ampm_circle_radius_multiplier));
    String[] arrayOfString = new DateFormatSymbols().getAmPmStrings();
    this.mAmText = arrayOfString[0];
    this.mPmText = arrayOfString[1];
    setAmOrPm(paramInt);
    this.mAmOrPmPressed = -1;
    this.mIsInitialized = true;
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    if ((getWidth() == 0) || (!this.mIsInitialized)) {
      return;
    }
    if (!this.mDrawValuesReady)
    {
      int i1 = getWidth() / 2;
      int i2 = getHeight() / 2;
      int i3 = (int)(Math.min(i1, i2) * this.mCircleRadiusMultiplier);
      this.mAmPmCircleRadius = ((int)(i3 * this.mAmPmCircleRadiusMultiplier));
      int i4 = 3 * this.mAmPmCircleRadius / 4;
      this.mPaint.setTextSize(i4);
      this.mAmPmYCenter = (i3 + (i2 - this.mAmPmCircleRadius / 2));
      this.mAmXCenter = (i1 - i3 + this.mAmPmCircleRadius);
      this.mPmXCenter = (i1 + i3 - this.mAmPmCircleRadius);
      this.mDrawValuesReady = true;
    }
    int i = this.mUnselectedColor;
    int j = 255;
    int k = this.mUnselectedColor;
    int m = 255;
    if (this.mAmOrPm == 0)
    {
      i = this.mSelectedColor;
      j = this.mSelectedAlpha;
      if (this.mAmOrPmPressed != 0) {
        break label370;
      }
      i = this.mSelectedColor;
      j = this.mSelectedAlpha;
    }
    for (;;)
    {
      this.mPaint.setColor(i);
      this.mPaint.setAlpha(j);
      paramCanvas.drawCircle(this.mAmXCenter, this.mAmPmYCenter, this.mAmPmCircleRadius, this.mPaint);
      this.mPaint.setColor(k);
      this.mPaint.setAlpha(m);
      paramCanvas.drawCircle(this.mPmXCenter, this.mAmPmYCenter, this.mAmPmCircleRadius, this.mPaint);
      this.mPaint.setColor(this.mAmPmTextColor);
      int n = this.mAmPmYCenter - (int)(this.mPaint.descent() + this.mPaint.ascent()) / 2;
      paramCanvas.drawText(this.mAmText, this.mAmXCenter, n, this.mPaint);
      paramCanvas.drawText(this.mPmText, this.mPmXCenter, n, this.mPaint);
      return;
      if (this.mAmOrPm != 1) {
        break;
      }
      k = this.mSelectedColor;
      m = this.mSelectedAlpha;
      break;
      label370:
      if (this.mAmOrPmPressed == 1)
      {
        k = this.mSelectedColor;
        m = this.mSelectedAlpha;
      }
    }
  }
  
  public void setAmOrPm(int paramInt)
  {
    this.mAmOrPm = paramInt;
  }
  
  public void setAmOrPmPressed(int paramInt)
  {
    this.mAmOrPmPressed = paramInt;
  }
  
  void setTheme(Context paramContext, boolean paramBoolean)
  {
    Resources localResources = paramContext.getResources();
    if (paramBoolean)
    {
      this.mUnselectedColor = localResources.getColor(R.color.dark_gray);
      this.mSelectedColor = localResources.getColor(R.color.red);
      this.mAmPmTextColor = localResources.getColor(R.color.white);
      this.mSelectedAlpha = 102;
      return;
    }
    this.mUnselectedColor = localResources.getColor(R.color.white);
    this.mSelectedColor = localResources.getColor(R.color.blue);
    this.mAmPmTextColor = localResources.getColor(R.color.ampm_text_color);
    this.mSelectedAlpha = 51;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.time.AmPmCirclesView
 * JD-Core Version:    0.7.0.1
 */