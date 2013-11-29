package com.google.android.velvet.tg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.StaticMapCache;
import com.google.android.sidekick.shared.cards.AbstractPlaceEntryAdapter;
import com.google.android.sidekick.shared.cards.FlightStatusEntryAdapter;
import com.google.android.sidekick.shared.cards.WeatherEntryAdapter;
import com.google.android.sidekick.shared.ui.FlightCard;
import com.google.android.velvet.VelvetServices;

public class SampleCardsView
  extends FrameLayout
{
  private final boolean DBG = false;
  private final String TAG = "SampleCardsView";
  private boolean mRtl;
  
  public SampleCardsView(Context paramContext)
  {
    super(paramContext);
  }
  
  public SampleCardsView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public void init(LayoutInflater paramLayoutInflater, Clock paramClock)
  {
    this.mRtl = LayoutUtils.isDefaultLocaleRtl();
    View localView1 = findViewById(2131296604);
    WeatherEntryAdapter.populateSampleCard(localView1);
    localView1.setBackgroundResource(2130838039);
    FlightCard localFlightCard = (FlightCard)findViewById(2131296605);
    FlightStatusEntryAdapter.populateSampleCard(localFlightCard, paramLayoutInflater, paramClock, false);
    localFlightCard.setBackgroundResource(2130838039);
    View localView2 = findViewById(2131296606);
    AbstractPlaceEntryAdapter.populateSampleCard(localView2);
    localView2.setBackgroundResource(2130838039);
    final ImageView localImageView = (ImageView)localView2.findViewById(2131296333);
    new AsyncTask()
    {
      protected Bitmap doInBackground(Void... paramAnonymousVarArgs)
      {
        return this.val$staticMapCache.getSampleMap();
      }
      
      protected void onPostExecute(Bitmap paramAnonymousBitmap)
      {
        if (paramAnonymousBitmap != null) {
          localImageView.setImageBitmap(paramAnonymousBitmap);
        }
      }
    }.execute(new Void[0]);
  }
  
  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = View.MeasureSpec.makeMeasureSpec(0, 0);
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    paramView.measure(getChildMeasureSpec(paramInt1, paramInt2 + (getPaddingLeft() + getPaddingRight() + localMarginLayoutParams.leftMargin + localMarginLayoutParams.rightMargin), localMarginLayoutParams.width), i);
  }
  
  public boolean onInterceptHoverEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    int i = getChildCount();
    float f1 = getContext().getResources().getDimension(2131689729);
    float f2 = getContext().getResources().getDimension(2131689730);
    float f3 = f1 + f2 * (i - 1);
    float f4 = 0.0F;
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      float f9 = f2 * j + localView.getMeasuredHeight();
      if (f9 > f4) {
        f4 = f9;
      }
    }
    float f5 = Math.min(paramInt1 / f3, paramInt2 / f4);
    if (f5 > 1.0F) {
      f5 = 1.0F;
    }
    float f6 = (paramInt1 - f3 * f5) / 2.0F;
    float f7 = (paramInt2 - f4 * f5) / 2.0F;
    float f8 = f2 * f5;
    int k = getChildCount();
    int m = 0;
    if (m < k)
    {
      ViewGroup localViewGroup = (ViewGroup)getChildAt(m);
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localViewGroup.getLayoutParams();
      localLayoutParams.width = ((int)f1);
      localLayoutParams.gravity = 3;
      localViewGroup.setScaleX(f5);
      localViewGroup.setScaleY(f5);
      if (this.mRtl) {}
      for (int n = -1 + (k - m);; n = m)
      {
        localViewGroup.setTranslationX(f6 + f8 * n);
        localViewGroup.setTranslationY(f7 + f8 * m);
        m++;
        break;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.tg.SampleCardsView
 * JD-Core Version:    0.7.0.1
 */