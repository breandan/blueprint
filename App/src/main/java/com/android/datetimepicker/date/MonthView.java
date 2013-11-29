package com.android.datetimepicker.date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import com.android.datetimepicker.R.color;
import com.android.datetimepicker.R.dimen;
import com.android.datetimepicker.R.string;
import com.android.datetimepicker.Utils;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public abstract class MonthView
  extends View
{
  protected static int DAY_SELECTED_CIRCLE_SIZE;
  protected static int DAY_SEPARATOR_WIDTH = 1;
  protected static int DEFAULT_HEIGHT = 32;
  protected static int MINI_DAY_NUMBER_TEXT_SIZE;
  protected static int MIN_HEIGHT = 10;
  protected static int MONTH_DAY_LABEL_TEXT_SIZE;
  protected static int MONTH_HEADER_SIZE;
  protected static int MONTH_LABEL_TEXT_SIZE;
  protected static float mScale = 0.0F;
  private final Calendar mCalendar;
  private final Calendar mDayLabelCalendar;
  private int mDayOfWeekStart = 0;
  private String mDayOfWeekTypeface;
  protected int mDayTextColor;
  protected int mFirstJulianDay = -1;
  protected int mFirstMonth = -1;
  private final Formatter mFormatter;
  protected boolean mHasToday = false;
  protected int mLastMonth = -1;
  private boolean mLockAccessibilityDelegate;
  protected int mMonth;
  protected Paint mMonthDayLabelPaint;
  protected Paint mMonthNumPaint;
  protected int mMonthTitleBGColor;
  protected Paint mMonthTitleBGPaint;
  protected int mMonthTitleColor;
  protected Paint mMonthTitlePaint;
  private String mMonthTitleTypeface;
  protected int mNumCells = this.mNumDays;
  protected int mNumDays = 7;
  private int mNumRows = 6;
  private OnDayClickListener mOnDayClickListener;
  protected int mPadding = 0;
  protected int mRowHeight = DEFAULT_HEIGHT;
  protected Paint mSelectedCirclePaint;
  protected int mSelectedDay = -1;
  protected int mSelectedLeft = -1;
  protected int mSelectedRight = -1;
  private final StringBuilder mStringBuilder;
  protected int mToday = -1;
  protected int mTodayNumberColor;
  private final MonthViewTouchHelper mTouchHelper;
  protected int mWeekStart = 1;
  protected int mWidth;
  protected int mYear;
  
  public MonthView(Context paramContext)
  {
    super(paramContext);
    Resources localResources = paramContext.getResources();
    this.mDayLabelCalendar = Calendar.getInstance();
    this.mCalendar = Calendar.getInstance();
    this.mDayOfWeekTypeface = localResources.getString(R.string.day_of_week_label_typeface);
    this.mMonthTitleTypeface = localResources.getString(R.string.sans_serif);
    this.mDayTextColor = localResources.getColor(R.color.date_picker_text_normal);
    this.mTodayNumberColor = localResources.getColor(R.color.blue);
    this.mMonthTitleColor = localResources.getColor(R.color.white);
    this.mMonthTitleBGColor = localResources.getColor(R.color.circle_background);
    this.mStringBuilder = new StringBuilder(50);
    this.mFormatter = new Formatter(this.mStringBuilder, Locale.getDefault());
    MINI_DAY_NUMBER_TEXT_SIZE = localResources.getDimensionPixelSize(R.dimen.day_number_size);
    MONTH_LABEL_TEXT_SIZE = localResources.getDimensionPixelSize(R.dimen.month_label_size);
    MONTH_DAY_LABEL_TEXT_SIZE = localResources.getDimensionPixelSize(R.dimen.month_day_label_text_size);
    MONTH_HEADER_SIZE = localResources.getDimensionPixelOffset(R.dimen.month_list_item_header_height);
    DAY_SELECTED_CIRCLE_SIZE = localResources.getDimensionPixelSize(R.dimen.day_number_select_circle_radius);
    this.mRowHeight = ((localResources.getDimensionPixelOffset(R.dimen.date_picker_view_animator_height) - MONTH_HEADER_SIZE) / 6);
    this.mTouchHelper = new MonthViewTouchHelper(this);
    ViewCompat.setAccessibilityDelegate(this, this.mTouchHelper);
    ViewCompat.setImportantForAccessibility(this, 1);
    this.mLockAccessibilityDelegate = true;
    initView();
  }
  
  private int calculateNumRows()
  {
    int i = findDayOffset();
    int j = (i + this.mNumCells) / this.mNumDays;
    if ((i + this.mNumCells) % this.mNumDays > 0) {}
    for (int k = 1;; k = 0) {
      return k + j;
    }
  }
  
  private void drawMonthDayLabels(Canvas paramCanvas)
  {
    int i = MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE / 2;
    int j = (this.mWidth - 2 * this.mPadding) / (2 * this.mNumDays);
    for (int k = 0; k < this.mNumDays; k++)
    {
      int m = (k + this.mWeekStart) % this.mNumDays;
      int n = j * (1 + k * 2) + this.mPadding;
      this.mDayLabelCalendar.set(7, m);
      paramCanvas.drawText(this.mDayLabelCalendar.getDisplayName(7, 1, Locale.getDefault()).toUpperCase(Locale.getDefault()), n, i, this.mMonthDayLabelPaint);
    }
  }
  
  private void drawMonthTitle(Canvas paramCanvas)
  {
    int i = (this.mWidth + 2 * this.mPadding) / 2;
    int j = (MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE) / 2 + MONTH_LABEL_TEXT_SIZE / 3;
    paramCanvas.drawText(getMonthAndYearString(), i, j, this.mMonthTitlePaint);
  }
  
  private int findDayOffset()
  {
    if (this.mDayOfWeekStart < this.mWeekStart) {}
    for (int i = this.mDayOfWeekStart + this.mNumDays;; i = this.mDayOfWeekStart) {
      return i - this.mWeekStart;
    }
  }
  
  private String getMonthAndYearString()
  {
    this.mStringBuilder.setLength(0);
    long l = this.mCalendar.getTimeInMillis();
    return DateUtils.formatDateRange(getContext(), this.mFormatter, l, l, 52, Time.getCurrentTimezone()).toString();
  }
  
  private void onDayClick(int paramInt)
  {
    if (this.mOnDayClickListener != null) {
      this.mOnDayClickListener.onDayClick(this, new MonthAdapter.CalendarDay(this.mYear, this.mMonth, paramInt));
    }
    this.mTouchHelper.sendEventForVirtualView(paramInt, 1);
  }
  
  private boolean sameDay(int paramInt, Time paramTime)
  {
    return (this.mYear == paramTime.year) && (this.mMonth == paramTime.month) && (paramInt == paramTime.monthDay);
  }
  
  public void clearAccessibilityFocus()
  {
    this.mTouchHelper.clearFocusedVirtualView();
  }
  
  public boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
  {
    if (this.mTouchHelper.dispatchHoverEvent(paramMotionEvent)) {
      return true;
    }
    return super.dispatchHoverEvent(paramMotionEvent);
  }
  
  public abstract void drawMonthDay(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9);
  
  protected void drawMonthNums(Canvas paramCanvas)
  {
    int i = (this.mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + MONTH_HEADER_SIZE;
    int j = (this.mWidth - 2 * this.mPadding) / (2 * this.mNumDays);
    int k = findDayOffset();
    for (int m = 1; m <= this.mNumCells; m++)
    {
      int n = j * (1 + k * 2) + this.mPadding;
      int i1 = (this.mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH;
      int i2 = n - j;
      int i3 = n + j;
      int i4 = i - i1;
      int i5 = i4 + this.mRowHeight;
      drawMonthDay(paramCanvas, this.mYear, this.mMonth, m, n, i, i2, i3, i4, i5);
      k++;
      if (k == this.mNumDays)
      {
        k = 0;
        i += this.mRowHeight;
      }
    }
  }
  
  public MonthAdapter.CalendarDay getAccessibilityFocus()
  {
    int i = this.mTouchHelper.getFocusedVirtualView();
    if (i >= 0) {
      return new MonthAdapter.CalendarDay(this.mYear, this.mMonth, i);
    }
    return null;
  }
  
  public int getDayFromLocation(float paramFloat1, float paramFloat2)
  {
    int i = this.mPadding;
    int j;
    if ((paramFloat1 < i) || (paramFloat1 > this.mWidth - this.mPadding)) {
      j = -1;
    }
    do
    {
      return j;
      int k = (int)(paramFloat2 - MONTH_HEADER_SIZE) / this.mRowHeight;
      j = 1 + ((int)((paramFloat1 - i) * this.mNumDays / (this.mWidth - i - this.mPadding)) - findDayOffset()) + k * this.mNumDays;
    } while ((j >= 1) && (j <= this.mNumCells));
    return -1;
  }
  
  protected void initView()
  {
    this.mMonthTitlePaint = new Paint();
    this.mMonthTitlePaint.setFakeBoldText(true);
    this.mMonthTitlePaint.setAntiAlias(true);
    this.mMonthTitlePaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
    this.mMonthTitlePaint.setTypeface(Typeface.create(this.mMonthTitleTypeface, 1));
    this.mMonthTitlePaint.setColor(this.mDayTextColor);
    this.mMonthTitlePaint.setTextAlign(Paint.Align.CENTER);
    this.mMonthTitlePaint.setStyle(Paint.Style.FILL);
    this.mMonthTitleBGPaint = new Paint();
    this.mMonthTitleBGPaint.setFakeBoldText(true);
    this.mMonthTitleBGPaint.setAntiAlias(true);
    this.mMonthTitleBGPaint.setColor(this.mMonthTitleBGColor);
    this.mMonthTitleBGPaint.setTextAlign(Paint.Align.CENTER);
    this.mMonthTitleBGPaint.setStyle(Paint.Style.FILL);
    this.mSelectedCirclePaint = new Paint();
    this.mSelectedCirclePaint.setFakeBoldText(true);
    this.mSelectedCirclePaint.setAntiAlias(true);
    this.mSelectedCirclePaint.setColor(this.mTodayNumberColor);
    this.mSelectedCirclePaint.setTextAlign(Paint.Align.CENTER);
    this.mSelectedCirclePaint.setStyle(Paint.Style.FILL);
    this.mSelectedCirclePaint.setAlpha(60);
    this.mMonthDayLabelPaint = new Paint();
    this.mMonthDayLabelPaint.setAntiAlias(true);
    this.mMonthDayLabelPaint.setTextSize(MONTH_DAY_LABEL_TEXT_SIZE);
    this.mMonthDayLabelPaint.setColor(this.mDayTextColor);
    this.mMonthDayLabelPaint.setTypeface(Typeface.create(this.mDayOfWeekTypeface, 0));
    this.mMonthDayLabelPaint.setStyle(Paint.Style.FILL);
    this.mMonthDayLabelPaint.setTextAlign(Paint.Align.CENTER);
    this.mMonthDayLabelPaint.setFakeBoldText(true);
    this.mMonthNumPaint = new Paint();
    this.mMonthNumPaint.setAntiAlias(true);
    this.mMonthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
    this.mMonthNumPaint.setStyle(Paint.Style.FILL);
    this.mMonthNumPaint.setTextAlign(Paint.Align.CENTER);
    this.mMonthNumPaint.setFakeBoldText(false);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    drawMonthTitle(paramCanvas);
    drawMonthDayLabels(paramCanvas);
    drawMonthNums(paramCanvas);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), this.mRowHeight * this.mNumRows + MONTH_HEADER_SIZE);
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mWidth = paramInt1;
    this.mTouchHelper.invalidateRoot();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return true;
      int i = getDayFromLocation(paramMotionEvent.getX(), paramMotionEvent.getY());
      if (i >= 0) {
        onDayClick(i);
      }
    }
  }
  
  public boolean restoreAccessibilityFocus(MonthAdapter.CalendarDay paramCalendarDay)
  {
    if ((paramCalendarDay.year != this.mYear) || (paramCalendarDay.month != this.mMonth) || (paramCalendarDay.day > this.mNumCells)) {
      return false;
    }
    this.mTouchHelper.setFocusedVirtualView(paramCalendarDay.day);
    return true;
  }
  
  public void reuse()
  {
    this.mNumRows = 6;
    requestLayout();
  }
  
  public void setAccessibilityDelegate(View.AccessibilityDelegate paramAccessibilityDelegate)
  {
    if (!this.mLockAccessibilityDelegate) {
      super.setAccessibilityDelegate(paramAccessibilityDelegate);
    }
  }
  
  public void setMonthParams(HashMap<String, Integer> paramHashMap)
  {
    if ((!paramHashMap.containsKey("month")) && (!paramHashMap.containsKey("year"))) {
      throw new InvalidParameterException("You must specify month and year for this view");
    }
    setTag(paramHashMap);
    if (paramHashMap.containsKey("height"))
    {
      this.mRowHeight = ((Integer)paramHashMap.get("height")).intValue();
      if (this.mRowHeight < MIN_HEIGHT) {
        this.mRowHeight = MIN_HEIGHT;
      }
    }
    if (paramHashMap.containsKey("selected_day")) {
      this.mSelectedDay = ((Integer)paramHashMap.get("selected_day")).intValue();
    }
    this.mMonth = ((Integer)paramHashMap.get("month")).intValue();
    this.mYear = ((Integer)paramHashMap.get("year")).intValue();
    Time localTime = new Time(Time.getCurrentTimezone());
    localTime.setToNow();
    this.mHasToday = false;
    this.mToday = -1;
    this.mCalendar.set(2, this.mMonth);
    this.mCalendar.set(1, this.mYear);
    this.mCalendar.set(5, 1);
    this.mDayOfWeekStart = this.mCalendar.get(7);
    if (paramHashMap.containsKey("week_start")) {}
    for (this.mWeekStart = ((Integer)paramHashMap.get("week_start")).intValue();; this.mWeekStart = this.mCalendar.getFirstDayOfWeek())
    {
      this.mNumCells = Utils.getDaysInMonth(this.mMonth, this.mYear);
      for (int i = 0; i < this.mNumCells; i++)
      {
        int j = i + 1;
        if (sameDay(j, localTime))
        {
          this.mHasToday = true;
          this.mToday = j;
        }
      }
    }
    this.mNumRows = calculateNumRows();
    this.mTouchHelper.invalidateRoot();
  }
  
  public void setOnDayClickListener(OnDayClickListener paramOnDayClickListener)
  {
    this.mOnDayClickListener = paramOnDayClickListener;
  }
  
  private class MonthViewTouchHelper
    extends ExploreByTouchHelper
  {
    private final Calendar mTempCalendar = Calendar.getInstance();
    private final Rect mTempRect = new Rect();
    
    public MonthViewTouchHelper(View paramView)
    {
      super();
    }
    
    private void getItemBounds(int paramInt, Rect paramRect)
    {
      int i = MonthView.this.mPadding;
      int j = MonthView.MONTH_HEADER_SIZE;
      int k = MonthView.this.mRowHeight;
      int m = (MonthView.this.mWidth - 2 * MonthView.this.mPadding) / MonthView.this.mNumDays;
      int n = paramInt - 1 + MonthView.this.findDayOffset();
      int i1 = n / MonthView.this.mNumDays;
      int i2 = i + m * (n % MonthView.this.mNumDays);
      int i3 = j + i1 * k;
      paramRect.set(i2, i3, i2 + m, i3 + k);
    }
    
    private CharSequence getItemDescription(int paramInt)
    {
      this.mTempCalendar.set(MonthView.this.mYear, MonthView.this.mMonth, paramInt);
      Object localObject = DateFormat.format("dd MMMM yyyy", this.mTempCalendar.getTimeInMillis());
      if (paramInt == MonthView.this.mSelectedDay) {
        localObject = MonthView.this.getContext().getString(R.string.item_is_selected, new Object[] { localObject });
      }
      return localObject;
    }
    
    public void clearFocusedVirtualView()
    {
      int i = getFocusedVirtualView();
      if (i != -2147483648) {
        getAccessibilityNodeProvider(MonthView.this).performAction(i, 128, null);
      }
    }
    
    protected int getVirtualViewAt(float paramFloat1, float paramFloat2)
    {
      int i = MonthView.this.getDayFromLocation(paramFloat1, paramFloat2);
      if (i >= 0) {
        return i;
      }
      return -2147483648;
    }
    
    protected void getVisibleVirtualViews(List<Integer> paramList)
    {
      for (int i = 1; i <= MonthView.this.mNumCells; i++) {
        paramList.add(Integer.valueOf(i));
      }
    }
    
    protected boolean onPerformActionForVirtualView(int paramInt1, int paramInt2, Bundle paramBundle)
    {
      switch (paramInt2)
      {
      default: 
        return false;
      }
      MonthView.this.onDayClick(paramInt1);
      return true;
    }
    
    protected void onPopulateEventForVirtualView(int paramInt, AccessibilityEvent paramAccessibilityEvent)
    {
      paramAccessibilityEvent.setContentDescription(getItemDescription(paramInt));
    }
    
    protected void onPopulateNodeForVirtualView(int paramInt, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      getItemBounds(paramInt, this.mTempRect);
      paramAccessibilityNodeInfoCompat.setContentDescription(getItemDescription(paramInt));
      paramAccessibilityNodeInfoCompat.setBoundsInParent(this.mTempRect);
      paramAccessibilityNodeInfoCompat.addAction(16);
      if (paramInt == MonthView.this.mSelectedDay) {
        paramAccessibilityNodeInfoCompat.setSelected(true);
      }
    }
    
    public void setFocusedVirtualView(int paramInt)
    {
      getAccessibilityNodeProvider(MonthView.this).performAction(paramInt, 64, null);
    }
  }
  
  public static abstract interface OnDayClickListener
  {
    public abstract void onDayClick(MonthView paramMonthView, MonthAdapter.CalendarDay paramCalendarDay);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.MonthView
 * JD-Core Version:    0.7.0.1
 */