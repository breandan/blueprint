package com.android.datetimepicker.time;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.android.datetimepicker.HapticFeedbackController;
import com.android.datetimepicker.R.color;
import java.util.List;

public class RadialPickerLayout
  extends FrameLayout
  implements View.OnTouchListener
{
  private final int TAP_TIMEOUT;
  private final int TOUCH_SLOP;
  private AccessibilityManager mAccessibilityManager;
  private AmPmCirclesView mAmPmCirclesView;
  private CircleView mCircleView;
  private int mCurrentHoursOfDay;
  private int mCurrentItemShowing;
  private int mCurrentMinutes;
  private boolean mDoingMove;
  private boolean mDoingTouch;
  private int mDownDegrees;
  private float mDownX;
  private float mDownY;
  private View mGrayBox;
  private Handler mHandler = new Handler();
  private HapticFeedbackController mHapticFeedbackController;
  private boolean mHideAmPm;
  private RadialSelectorView mHourRadialSelectorView;
  private RadialTextsView mHourRadialTextsView;
  private boolean mInputEnabled;
  private boolean mIs24HourMode;
  private int mIsTouchingAmOrPm = -1;
  private int mLastValueSelected;
  private OnValueSelectedListener mListener;
  private RadialSelectorView mMinuteRadialSelectorView;
  private RadialTextsView mMinuteRadialTextsView;
  private int[] mSnapPrefer30sMap;
  private boolean mTimeInitialized;
  private AnimatorSet mTransition;
  
  public RadialPickerLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOnTouchListener(this);
    this.TOUCH_SLOP = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    this.TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    this.mDoingMove = false;
    this.mCircleView = new CircleView(paramContext);
    addView(this.mCircleView);
    this.mAmPmCirclesView = new AmPmCirclesView(paramContext);
    addView(this.mAmPmCirclesView);
    this.mHourRadialTextsView = new RadialTextsView(paramContext);
    addView(this.mHourRadialTextsView);
    this.mMinuteRadialTextsView = new RadialTextsView(paramContext);
    addView(this.mMinuteRadialTextsView);
    this.mHourRadialSelectorView = new RadialSelectorView(paramContext);
    addView(this.mHourRadialSelectorView);
    this.mMinuteRadialSelectorView = new RadialSelectorView(paramContext);
    addView(this.mMinuteRadialSelectorView);
    preparePrefer30sMap();
    this.mLastValueSelected = -1;
    this.mInputEnabled = true;
    this.mGrayBox = new View(paramContext);
    this.mGrayBox.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    this.mGrayBox.setBackgroundColor(getResources().getColor(R.color.transparent_black));
    this.mGrayBox.setVisibility(4);
    addView(this.mGrayBox);
    this.mAccessibilityManager = ((AccessibilityManager)paramContext.getSystemService("accessibility"));
    this.mTimeInitialized = false;
  }
  
  private int getCurrentlyShowingValue()
  {
    int i = getCurrentItemShowing();
    if (i == 0) {
      return this.mCurrentHoursOfDay;
    }
    if (i == 1) {
      return this.mCurrentMinutes;
    }
    return -1;
  }
  
  private int getDegreesFromCoords(float paramFloat1, float paramFloat2, boolean paramBoolean, Boolean[] paramArrayOfBoolean)
  {
    int i = getCurrentItemShowing();
    if (i == 0) {
      return this.mHourRadialSelectorView.getDegreesFromCoords(paramFloat1, paramFloat2, paramBoolean, paramArrayOfBoolean);
    }
    if (i == 1) {
      return this.mMinuteRadialSelectorView.getDegreesFromCoords(paramFloat1, paramFloat2, paramBoolean, paramArrayOfBoolean);
    }
    return -1;
  }
  
  private boolean isHourInnerCircle(int paramInt)
  {
    return (this.mIs24HourMode) && (paramInt <= 12) && (paramInt != 0);
  }
  
  private void preparePrefer30sMap()
  {
    this.mSnapPrefer30sMap = new int[361];
    int i = 0;
    int j = 1;
    int k = 8;
    int m = 0;
    if (m < 361)
    {
      this.mSnapPrefer30sMap[m] = i;
      if (j == k)
      {
        i += 6;
        if (i == 360) {
          k = 7;
        }
      }
      label53:
      for (j = 1;; j++)
      {
        m++;
        break;
        if (i % 30 == 0)
        {
          k = 14;
          break label53;
        }
        k = 4;
        break label53;
      }
    }
  }
  
  private int reselectSelector(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    int i = -1;
    if (paramInt == i) {
      return i;
    }
    int j = getCurrentItemShowing();
    int k;
    label31:
    int m;
    label43:
    RadialSelectorView localRadialSelectorView;
    int n;
    if ((!paramBoolean2) && (j == 1))
    {
      k = 1;
      if (k == 0) {
        break label139;
      }
      m = snapPrefer30s(paramInt);
      if (j != 0) {
        break label149;
      }
      localRadialSelectorView = this.mHourRadialSelectorView;
      n = 30;
      label58:
      localRadialSelectorView.setSelection(m, paramBoolean1, paramBoolean3);
      localRadialSelectorView.invalidate();
      if (j != 0) {
        break label193;
      }
      if (!this.mIs24HourMode) {
        break label180;
      }
      if ((m != 0) || (!paramBoolean1)) {
        break label162;
      }
      m = 360;
    }
    for (;;)
    {
      i = m / n;
      if ((j != 0) || (!this.mIs24HourMode) || (paramBoolean1) || (m == 0)) {
        break;
      }
      return i + 12;
      k = 0;
      break label31;
      label139:
      m = snapOnly30s(paramInt, 0);
      break label43;
      label149:
      localRadialSelectorView = this.mMinuteRadialSelectorView;
      n = 6;
      break label58;
      label162:
      if ((m == 360) && (!paramBoolean1))
      {
        m = 0;
        continue;
        label180:
        if (m == 0)
        {
          m = 360;
          continue;
          label193:
          if ((m == 360) && (j == 1)) {
            m = 0;
          }
        }
      }
    }
  }
  
  private void setItem(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 0)
    {
      setValueForItem(0, paramInt2);
      j = 30 * (paramInt2 % 12);
      this.mHourRadialSelectorView.setSelection(j, isHourInnerCircle(paramInt2), false);
      this.mHourRadialSelectorView.invalidate();
    }
    while (paramInt1 != 1)
    {
      int j;
      return;
    }
    setValueForItem(1, paramInt2);
    int i = paramInt2 * 6;
    this.mMinuteRadialSelectorView.setSelection(i, false, false);
    this.mMinuteRadialSelectorView.invalidate();
  }
  
  private void setValueForItem(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 0) {
      this.mCurrentHoursOfDay = paramInt2;
    }
    do
    {
      do
      {
        return;
        if (paramInt1 == 1)
        {
          this.mCurrentMinutes = paramInt2;
          return;
        }
      } while (paramInt1 != 2);
      if (paramInt2 == 0)
      {
        this.mCurrentHoursOfDay %= 12;
        return;
      }
    } while (paramInt2 != 1);
    this.mCurrentHoursOfDay = (12 + this.mCurrentHoursOfDay % 12);
  }
  
  private static int snapOnly30s(int paramInt1, int paramInt2)
  {
    int i = 30 * (paramInt1 / 30);
    int j = i + 30;
    if (paramInt2 == 1) {
      return j;
    }
    if (paramInt2 == -1)
    {
      if (paramInt1 == i) {
        i -= 30;
      }
      return i;
    }
    if (paramInt1 - i < j - paramInt1) {
      return i;
    }
    return j;
  }
  
  private int snapPrefer30s(int paramInt)
  {
    if (this.mSnapPrefer30sMap == null) {
      return -1;
    }
    return this.mSnapPrefer30sMap[paramInt];
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (paramAccessibilityEvent.getEventType() == 32)
    {
      paramAccessibilityEvent.getText().clear();
      Time localTime = new Time();
      localTime.hour = getHours();
      localTime.minute = getMinutes();
      long l = localTime.normalize(true);
      int i = 1;
      if (this.mIs24HourMode) {
        i |= 0x80;
      }
      String str = DateUtils.formatDateTime(getContext(), l, i);
      paramAccessibilityEvent.getText().add(str);
      return true;
    }
    return super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
  }
  
  public int getCurrentItemShowing()
  {
    if ((this.mCurrentItemShowing != 0) && (this.mCurrentItemShowing != 1))
    {
      Log.e("RadialPickerLayout", "Current item showing was unfortunately set to " + this.mCurrentItemShowing);
      return -1;
    }
    return this.mCurrentItemShowing;
  }
  
  public int getHours()
  {
    return this.mCurrentHoursOfDay;
  }
  
  public int getIsCurrentlyAmOrPm()
  {
    if (this.mCurrentHoursOfDay < 12) {
      return 0;
    }
    if (this.mCurrentHoursOfDay < 24) {
      return 1;
    }
    return -1;
  }
  
  public int getMinutes()
  {
    return this.mCurrentMinutes;
  }
  
  public void initialize(Context paramContext, HapticFeedbackController paramHapticFeedbackController, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (this.mTimeInitialized)
    {
      Log.e("RadialPickerLayout", "Time has already been initialized.");
      return;
    }
    this.mHapticFeedbackController = paramHapticFeedbackController;
    this.mIs24HourMode = paramBoolean;
    boolean bool;
    int m;
    label89:
    Resources localResources;
    int[] arrayOfInt1;
    int[] arrayOfInt3;
    String[] arrayOfString1;
    String[] arrayOfString2;
    String[] arrayOfString3;
    int i;
    label342:
    Object[] arrayOfObject4;
    if (this.mAccessibilityManager.isTouchExplorationEnabled())
    {
      bool = true;
      this.mHideAmPm = bool;
      this.mCircleView.initialize(paramContext, this.mHideAmPm);
      this.mCircleView.invalidate();
      if (!this.mHideAmPm)
      {
        AmPmCirclesView localAmPmCirclesView = this.mAmPmCirclesView;
        if (paramInt1 >= 12) {
          break label466;
        }
        m = 0;
        localAmPmCirclesView.initialize(paramContext, m);
        this.mAmPmCirclesView.invalidate();
      }
      localResources = paramContext.getResources();
      arrayOfInt1 = new int[] { 12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
      int[] arrayOfInt2 = { 0, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
      arrayOfInt3 = new int[] { 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55 };
      arrayOfString1 = new String[12];
      arrayOfString2 = new String[12];
      arrayOfString3 = new String[12];
      i = 0;
      if (i >= 12) {
        break label503;
      }
      if (!paramBoolean) {
        break label472;
      }
      arrayOfObject4 = new Object[1];
      arrayOfObject4[0] = Integer.valueOf(arrayOfInt2[i]);
    }
    label466:
    label472:
    Object[] arrayOfObject1;
    for (String str = String.format("%02d", arrayOfObject4);; str = String.format("%d", arrayOfObject1))
    {
      arrayOfString1[i] = str;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(arrayOfInt1[i]);
      arrayOfString2[i] = String.format("%d", arrayOfObject2);
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Integer.valueOf(arrayOfInt3[i]);
      arrayOfString3[i] = String.format("%02d", arrayOfObject3);
      i++;
      break label342;
      bool = this.mIs24HourMode;
      break;
      m = 1;
      break label89;
      arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(arrayOfInt1[i]);
    }
    label503:
    RadialTextsView localRadialTextsView = this.mHourRadialTextsView;
    if (paramBoolean) {}
    for (String[] arrayOfString4 = arrayOfString2;; arrayOfString4 = null)
    {
      localRadialTextsView.initialize(localResources, arrayOfString1, arrayOfString4, this.mHideAmPm, true);
      this.mHourRadialTextsView.invalidate();
      this.mMinuteRadialTextsView.initialize(localResources, arrayOfString3, null, this.mHideAmPm, false);
      this.mMinuteRadialTextsView.invalidate();
      setValueForItem(0, paramInt1);
      setValueForItem(1, paramInt2);
      int j = 30 * (paramInt1 % 12);
      this.mHourRadialSelectorView.initialize(paramContext, this.mHideAmPm, paramBoolean, true, j, isHourInnerCircle(paramInt1));
      int k = paramInt2 * 6;
      this.mMinuteRadialSelectorView.initialize(paramContext, this.mHideAmPm, false, false, k, false);
      this.mTimeInitialized = true;
      return;
    }
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.addAction(4096);
    paramAccessibilityNodeInfo.addAction(8192);
  }
  
  public void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getSize(paramInt2);
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = Math.min(i, k);
    super.onMeasure(View.MeasureSpec.makeMeasureSpec(n, j), View.MeasureSpec.makeMeasureSpec(n, m));
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    final Boolean[] arrayOfBoolean = new Boolean[1];
    arrayOfBoolean[0] = Boolean.valueOf(false);
    switch (paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return false;
      if (!this.mInputEnabled) {
        return true;
      }
      this.mDownX = f1;
      this.mDownY = f2;
      this.mLastValueSelected = -1;
      this.mDoingMove = false;
      this.mDoingTouch = true;
      if (!this.mHideAmPm)
      {
        this.mIsTouchingAmOrPm = this.mAmPmCirclesView.getIsTouchingAmOrPm(f1, f2);
        if ((this.mIsTouchingAmOrPm != 0) && (this.mIsTouchingAmOrPm != 1)) {
          break label172;
        }
        this.mHapticFeedbackController.tryVibrate();
        this.mDownDegrees = -1;
        this.mHandler.postDelayed(new Runnable()
        {
          public void run()
          {
            RadialPickerLayout.this.mAmPmCirclesView.setAmOrPmPressed(RadialPickerLayout.this.mIsTouchingAmOrPm);
            RadialPickerLayout.this.mAmPmCirclesView.invalidate();
          }
        }, this.TAP_TIMEOUT);
      }
      for (;;)
      {
        return true;
        this.mIsTouchingAmOrPm = -1;
        break;
        label172:
        this.mDownDegrees = getDegreesFromCoords(f1, f2, this.mAccessibilityManager.isTouchExplorationEnabled(), arrayOfBoolean);
        if (this.mDownDegrees != -1)
        {
          this.mHapticFeedbackController.tryVibrate();
          this.mHandler.postDelayed(new Runnable()
          {
            public void run()
            {
              RadialPickerLayout.access$202(RadialPickerLayout.this, true);
              int i = RadialPickerLayout.this.reselectSelector(RadialPickerLayout.this.mDownDegrees, arrayOfBoolean[0].booleanValue(), false, true);
              RadialPickerLayout.access$502(RadialPickerLayout.this, i);
              RadialPickerLayout.this.mListener.onValueSelected(RadialPickerLayout.this.getCurrentItemShowing(), i, false);
            }
          }, this.TAP_TIMEOUT);
        }
      }
      if (!this.mInputEnabled)
      {
        Log.e("RadialPickerLayout", "Input was disabled, but received ACTION_MOVE.");
        return true;
      }
      float f3 = Math.abs(f2 - this.mDownY);
      float f4 = Math.abs(f1 - this.mDownX);
      if ((this.mDoingMove) || (f4 > this.TOUCH_SLOP) || (f3 > this.TOUCH_SLOP)) {
        if ((this.mIsTouchingAmOrPm == 0) || (this.mIsTouchingAmOrPm == 1))
        {
          this.mHandler.removeCallbacksAndMessages(null);
          if (this.mAmPmCirclesView.getIsTouchingAmOrPm(f1, f2) != this.mIsTouchingAmOrPm)
          {
            this.mAmPmCirclesView.setAmOrPmPressed(-1);
            this.mAmPmCirclesView.invalidate();
            this.mIsTouchingAmOrPm = -1;
          }
        }
        else if (this.mDownDegrees != -1)
        {
          this.mDoingMove = true;
          this.mHandler.removeCallbacksAndMessages(null);
          int n = getDegreesFromCoords(f1, f2, true, arrayOfBoolean);
          if (n != -1)
          {
            int i1 = reselectSelector(n, arrayOfBoolean[0].booleanValue(), false, true);
            if (i1 != this.mLastValueSelected)
            {
              this.mHapticFeedbackController.tryVibrate();
              this.mLastValueSelected = i1;
              this.mListener.onValueSelected(getCurrentItemShowing(), i1, false);
            }
          }
          return true;
          if (!this.mInputEnabled)
          {
            Log.d("RadialPickerLayout", "Input was disabled, but received ACTION_UP.");
            this.mListener.onValueSelected(3, 1, false);
            return true;
          }
          this.mHandler.removeCallbacksAndMessages(null);
          this.mDoingTouch = false;
          if ((this.mIsTouchingAmOrPm != 0) && (this.mIsTouchingAmOrPm != 1)) {
            break;
          }
          int i = this.mAmPmCirclesView.getIsTouchingAmOrPm(f1, f2);
          this.mAmPmCirclesView.setAmOrPmPressed(-1);
          this.mAmPmCirclesView.invalidate();
          if (i == this.mIsTouchingAmOrPm)
          {
            this.mAmPmCirclesView.setAmOrPm(i);
            if (getIsCurrentlyAmOrPm() != i)
            {
              this.mListener.onValueSelected(2, this.mIsTouchingAmOrPm, false);
              setValueForItem(2, i);
            }
          }
          this.mIsTouchingAmOrPm = -1;
        }
      }
    }
    boolean bool2;
    int m;
    if (this.mDownDegrees != -1)
    {
      int j = getDegreesFromCoords(f1, f2, this.mDoingMove, arrayOfBoolean);
      if (j != -1)
      {
        boolean bool1 = arrayOfBoolean[0].booleanValue();
        if (this.mDoingMove) {
          break label735;
        }
        bool2 = true;
        k = reselectSelector(j, bool1, bool2, false);
        if ((getCurrentItemShowing() == 0) && (!this.mIs24HourMode))
        {
          m = getIsCurrentlyAmOrPm();
          if ((m != 0) || (k != 12)) {
            break label741;
          }
        }
      }
    }
    for (int k = 0;; k += 12) {
      label735:
      label741:
      do
      {
        setValueForItem(getCurrentItemShowing(), k);
        this.mListener.onValueSelected(getCurrentItemShowing(), k, true);
        this.mDoingMove = false;
        return true;
        bool2 = false;
        break;
      } while ((m != 1) || (k == 12));
    }
  }
  
  @SuppressLint({"NewApi"})
  public boolean performAccessibilityAction(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityAction(paramInt, paramBundle)) {
      return true;
    }
    int i;
    int k;
    int m;
    label52:
    int n;
    int i1;
    int i2;
    if (paramInt == 4096)
    {
      i = 1;
      if (i == 0) {
        break label184;
      }
      int j = getCurrentlyShowingValue();
      k = getCurrentItemShowing();
      if (k != 0) {
        break label134;
      }
      m = 30;
      j %= 12;
      n = snapOnly30s(j * m, i) / m;
      i1 = 0;
      if (k != 0) {
        break label160;
      }
      if (!this.mIs24HourMode) {
        break label150;
      }
      i2 = 23;
      label85:
      if (n <= i2) {
        break label170;
      }
      n = i1;
    }
    for (;;)
    {
      setItem(k, n);
      this.mListener.onValueSelected(k, n, false);
      return true;
      i = 0;
      if (paramInt != 8192) {
        break;
      }
      i = -1;
      break;
      label134:
      m = 0;
      if (k != 1) {
        break label52;
      }
      m = 6;
      break label52;
      label150:
      i2 = 12;
      i1 = 1;
      break label85;
      label160:
      i2 = 55;
      i1 = 0;
      break label85;
      label170:
      if (n < i1) {
        n = i2;
      }
    }
    label184:
    return false;
  }
  
  public void setAmOrPm(int paramInt)
  {
    this.mAmPmCirclesView.setAmOrPm(paramInt);
    this.mAmPmCirclesView.invalidate();
    setValueForItem(2, paramInt);
  }
  
  public void setCurrentItemShowing(int paramInt, boolean paramBoolean)
  {
    int i = 255;
    if ((paramInt != 0) && (paramInt != 1))
    {
      Log.e("RadialPickerLayout", "TimePicker does not support view at index " + paramInt);
      return;
    }
    int j = getCurrentItemShowing();
    this.mCurrentItemShowing = paramInt;
    if ((paramBoolean) && (paramInt != j))
    {
      ObjectAnimator[] arrayOfObjectAnimator = new ObjectAnimator[4];
      if (paramInt == 1)
      {
        arrayOfObjectAnimator[0] = this.mHourRadialTextsView.getDisappearAnimator();
        arrayOfObjectAnimator[1] = this.mHourRadialSelectorView.getDisappearAnimator();
        arrayOfObjectAnimator[2] = this.mMinuteRadialTextsView.getReappearAnimator();
        arrayOfObjectAnimator[3] = this.mMinuteRadialSelectorView.getReappearAnimator();
      }
      for (;;)
      {
        if ((this.mTransition != null) && (this.mTransition.isRunning())) {
          this.mTransition.end();
        }
        this.mTransition = new AnimatorSet();
        this.mTransition.playTogether(arrayOfObjectAnimator);
        this.mTransition.start();
        return;
        if (paramInt == 0)
        {
          arrayOfObjectAnimator[0] = this.mHourRadialTextsView.getReappearAnimator();
          arrayOfObjectAnimator[1] = this.mHourRadialSelectorView.getReappearAnimator();
          arrayOfObjectAnimator[2] = this.mMinuteRadialTextsView.getDisappearAnimator();
          arrayOfObjectAnimator[3] = this.mMinuteRadialSelectorView.getDisappearAnimator();
        }
      }
    }
    int k;
    if (paramInt == 0)
    {
      k = i;
      if (paramInt != 1) {
        break label277;
      }
    }
    for (;;)
    {
      this.mHourRadialTextsView.setAlpha(k);
      this.mHourRadialSelectorView.setAlpha(k);
      this.mMinuteRadialTextsView.setAlpha(i);
      this.mMinuteRadialSelectorView.setAlpha(i);
      return;
      k = 0;
      break;
      label277:
      i = 0;
    }
  }
  
  public void setOnValueSelectedListener(OnValueSelectedListener paramOnValueSelectedListener)
  {
    this.mListener = paramOnValueSelectedListener;
  }
  
  void setTheme(Context paramContext, boolean paramBoolean)
  {
    this.mCircleView.setTheme(paramContext, paramBoolean);
    this.mAmPmCirclesView.setTheme(paramContext, paramBoolean);
    this.mHourRadialTextsView.setTheme(paramContext, paramBoolean);
    this.mMinuteRadialTextsView.setTheme(paramContext, paramBoolean);
    this.mHourRadialSelectorView.setTheme(paramContext, paramBoolean);
    this.mMinuteRadialSelectorView.setTheme(paramContext, paramBoolean);
  }
  
  public void setTime(int paramInt1, int paramInt2)
  {
    setItem(0, paramInt1);
    setItem(1, paramInt2);
  }
  
  public boolean trySettingInputEnabled(boolean paramBoolean)
  {
    if ((this.mDoingTouch) && (!paramBoolean)) {
      return false;
    }
    this.mInputEnabled = paramBoolean;
    View localView = this.mGrayBox;
    int i = 0;
    if (paramBoolean) {
      i = 4;
    }
    localView.setVisibility(i);
    return true;
  }
  
  public static abstract interface OnValueSelectedListener
  {
    public abstract void onValueSelected(int paramInt1, int paramInt2, boolean paramBoolean);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.time.RadialPickerLayout
 * JD-Core Version:    0.7.0.1
 */