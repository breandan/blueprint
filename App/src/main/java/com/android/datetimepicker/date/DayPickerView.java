package com.android.datetimepicker.date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import com.android.datetimepicker.Utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public abstract class DayPickerView
  extends ListView
  implements AbsListView.OnScrollListener, DatePickerDialog.OnDateChangedListener
{
  public static int LIST_TOP_OFFSET = -1;
  private static SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
  protected MonthAdapter mAdapter;
  protected Context mContext;
  private DatePickerController mController;
  protected int mCurrentMonthDisplayed;
  protected int mCurrentScrollState = 0;
  protected int mDaysPerWeek = 7;
  protected float mFriction = 1.0F;
  protected Handler mHandler;
  protected int mNumWeeks = 6;
  private boolean mPerformingScroll;
  protected long mPreviousScrollPosition;
  protected int mPreviousScrollState = 0;
  protected ScrollStateRunnable mScrollStateChangedRunnable = new ScrollStateRunnable();
  protected MonthAdapter.CalendarDay mSelectedDay = new MonthAdapter.CalendarDay();
  protected boolean mShowWeekNumber = false;
  protected MonthAdapter.CalendarDay mTempDay = new MonthAdapter.CalendarDay();
  
  public DayPickerView(Context paramContext, DatePickerController paramDatePickerController)
  {
    super(paramContext);
    init(paramContext);
    setController(paramDatePickerController);
  }
  
  private MonthAdapter.CalendarDay findAccessibilityFocus()
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if ((localView instanceof MonthView))
      {
        MonthAdapter.CalendarDay localCalendarDay = ((MonthView)localView).getAccessibilityFocus();
        if (localCalendarDay != null)
        {
          if (Build.VERSION.SDK_INT == 17) {
            ((MonthView)localView).clearAccessibilityFocus();
          }
          return localCalendarDay;
        }
      }
    }
    return null;
  }
  
  private static String getMonthAndYearString(MonthAdapter.CalendarDay paramCalendarDay)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.set(paramCalendarDay.year, paramCalendarDay.month, paramCalendarDay.day);
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(localCalendar.getDisplayName(2, 2, Locale.getDefault()));
    localStringBuffer.append(" ");
    localStringBuffer.append(YEAR_FORMAT.format(localCalendar.getTime()));
    return localStringBuffer.toString();
  }
  
  private boolean restoreAccessibilityFocus(MonthAdapter.CalendarDay paramCalendarDay)
  {
    if (paramCalendarDay == null) {}
    for (;;)
    {
      return false;
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        if (((localView instanceof MonthView)) && (((MonthView)localView).restoreAccessibilityFocus(paramCalendarDay))) {
          return true;
        }
      }
    }
  }
  
  public abstract MonthAdapter createMonthAdapter(Context paramContext, DatePickerController paramDatePickerController);
  
  public int getMostVisiblePosition()
  {
    int i = getFirstVisiblePosition();
    int j = getHeight();
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    for (;;)
    {
      View localView;
      if (i1 < j)
      {
        localView = getChildAt(n);
        if (localView != null) {}
      }
      else
      {
        return i + m;
      }
      i1 = localView.getBottom();
      int i2 = Math.min(i1, j) - Math.max(0, localView.getTop());
      if (i2 > k)
      {
        m = n;
        k = i2;
      }
      n++;
    }
  }
  
  public boolean goTo(MonthAdapter.CalendarDay paramCalendarDay, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramBoolean2) {
      this.mSelectedDay.set(paramCalendarDay);
    }
    this.mTempDay.set(paramCalendarDay);
    int i = 12 * (paramCalendarDay.year - this.mController.getMinYear()) + paramCalendarDay.month;
    int j = 0;
    int k = j + 1;
    View localView = getChildAt(j);
    if (localView == null) {
      label66:
      if (localView == null) {
        break label238;
      }
    }
    label238:
    for (int n = getPositionForView(localView);; n = 0)
    {
      if (paramBoolean2) {
        this.mAdapter.setSelectedDay(this.mSelectedDay);
      }
      if (Log.isLoggable("MonthFragment", 3)) {
        Log.d("MonthFragment", "GoTo position " + i);
      }
      if ((i == n) && (!paramBoolean3)) {
        break label252;
      }
      setMonthDisplayed(this.mTempDay);
      this.mPreviousScrollState = 2;
      if (!paramBoolean1) {
        break label244;
      }
      smoothScrollToPositionFromTop(i, LIST_TOP_OFFSET, 250);
      return true;
      int m = localView.getTop();
      if (Log.isLoggable("MonthFragment", 3)) {
        Log.d("MonthFragment", "child at " + (k - 1) + " has top " + m);
      }
      if (m >= 0) {
        break label66;
      }
      j = k;
      break;
    }
    label244:
    postSetSelection(i);
    for (;;)
    {
      return false;
      label252:
      if (paramBoolean2) {
        setMonthDisplayed(this.mSelectedDay);
      }
    }
  }
  
  public void init(Context paramContext)
  {
    this.mHandler = new Handler();
    setLayoutParams(new AbsListView.LayoutParams(-1, -1));
    setDrawSelectorOnTop(false);
    this.mContext = paramContext;
    setUpListView();
  }
  
  protected void layoutChildren()
  {
    MonthAdapter.CalendarDay localCalendarDay = findAccessibilityFocus();
    super.layoutChildren();
    if (this.mPerformingScroll)
    {
      this.mPerformingScroll = false;
      return;
    }
    restoreAccessibilityFocus(localCalendarDay);
  }
  
  public void onChange()
  {
    refreshAdapter();
  }
  
  public void onDateChanged()
  {
    goTo(this.mController.getSelectedDay(), false, true, true);
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setItemCount(-1);
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.addAction(4096);
    paramAccessibilityNodeInfo.addAction(8192);
  }
  
  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    MonthView localMonthView = (MonthView)paramAbsListView.getChildAt(0);
    if (localMonthView == null) {
      return;
    }
    this.mPreviousScrollPosition = (paramAbsListView.getFirstVisiblePosition() * localMonthView.getHeight() - localMonthView.getBottom());
    this.mPreviousScrollState = this.mCurrentScrollState;
  }
  
  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    this.mScrollStateChangedRunnable.doScrollStateChange(paramAbsListView, paramInt);
  }
  
  @SuppressLint({"NewApi"})
  public boolean performAccessibilityAction(int paramInt, Bundle paramBundle)
  {
    if ((paramInt != 4096) && (paramInt != 8192)) {
      return super.performAccessibilityAction(paramInt, paramBundle);
    }
    int i = getFirstVisiblePosition();
    int j = i % 12;
    MonthAdapter.CalendarDay localCalendarDay = new MonthAdapter.CalendarDay(i / 12 + this.mController.getMinYear(), j, 1);
    if (paramInt == 4096)
    {
      localCalendarDay.month = (1 + localCalendarDay.month);
      if (localCalendarDay.month == 12)
      {
        localCalendarDay.month = 0;
        localCalendarDay.year = (1 + localCalendarDay.year);
      }
    }
    for (;;)
    {
      Utils.tryAccessibilityAnnounce(this, getMonthAndYearString(localCalendarDay));
      goTo(localCalendarDay, true, false, true);
      this.mPerformingScroll = true;
      return true;
      if (paramInt == 8192)
      {
        View localView = getChildAt(0);
        if ((localView != null) && (localView.getTop() >= -1))
        {
          localCalendarDay.month = (-1 + localCalendarDay.month);
          if (localCalendarDay.month == -1)
          {
            localCalendarDay.month = 11;
            localCalendarDay.year = (-1 + localCalendarDay.year);
          }
        }
      }
    }
  }
  
  public void postSetSelection(final int paramInt)
  {
    clearFocus();
    post(new Runnable()
    {
      public void run()
      {
        DayPickerView.this.setSelection(paramInt);
      }
    });
    onScrollStateChanged(this, 0);
  }
  
  protected void refreshAdapter()
  {
    if (this.mAdapter == null) {
      this.mAdapter = createMonthAdapter(getContext(), this.mController);
    }
    for (;;)
    {
      setAdapter(this.mAdapter);
      return;
      this.mAdapter.setSelectedDay(this.mSelectedDay);
    }
  }
  
  public void setController(DatePickerController paramDatePickerController)
  {
    this.mController = paramDatePickerController;
    this.mController.registerOnDateChangedListener(this);
    refreshAdapter();
    onDateChanged();
  }
  
  protected void setMonthDisplayed(MonthAdapter.CalendarDay paramCalendarDay)
  {
    this.mCurrentMonthDisplayed = paramCalendarDay.month;
    invalidateViews();
  }
  
  protected void setUpListView()
  {
    setCacheColorHint(0);
    setDivider(null);
    setItemsCanFocus(true);
    setFastScrollEnabled(false);
    setVerticalScrollBarEnabled(false);
    setOnScrollListener(this);
    setFadingEdgeLength(0);
    setFriction(ViewConfiguration.getScrollFriction() * this.mFriction);
  }
  
  protected class ScrollStateRunnable
    implements Runnable
  {
    private int mNewState;
    
    protected ScrollStateRunnable() {}
    
    public void doScrollStateChange(AbsListView paramAbsListView, int paramInt)
    {
      DayPickerView.this.mHandler.removeCallbacks(this);
      this.mNewState = paramInt;
      DayPickerView.this.mHandler.postDelayed(this, 40L);
    }
    
    public void run()
    {
      int i = 1;
      DayPickerView.this.mCurrentScrollState = this.mNewState;
      if (Log.isLoggable("MonthFragment", 3)) {
        Log.d("MonthFragment", "new scroll state: " + this.mNewState + " old state: " + DayPickerView.this.mPreviousScrollState);
      }
      if ((this.mNewState == 0) && (DayPickerView.this.mPreviousScrollState != 0) && (DayPickerView.this.mPreviousScrollState != i))
      {
        DayPickerView.this.mPreviousScrollState = this.mNewState;
        int j = 0;
        DayPickerView localDayPickerView;
        for (View localView = DayPickerView.this.getChildAt(0); (localView != null) && (localView.getBottom() <= 0); localView = localDayPickerView.getChildAt(j))
        {
          localDayPickerView = DayPickerView.this;
          j++;
        }
        if (localView == null) {
          return;
        }
        int k = DayPickerView.this.getFirstVisiblePosition();
        int m = DayPickerView.this.getLastVisiblePosition();
        if ((k != 0) && (m != -1 + DayPickerView.this.getCount())) {}
        int i1;
        for (;;)
        {
          int n = localView.getTop();
          i1 = localView.getBottom();
          int i2 = DayPickerView.this.getHeight() / 2;
          if ((i == 0) || (n >= DayPickerView.LIST_TOP_OFFSET)) {
            break;
          }
          if (i1 <= i2) {
            break label247;
          }
          DayPickerView.this.smoothScrollBy(n, 250);
          return;
          i = 0;
        }
        label247:
        DayPickerView.this.smoothScrollBy(i1, 250);
        return;
      }
      DayPickerView.this.mPreviousScrollState = this.mNewState;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.DayPickerView
 * JD-Core Version:    0.7.0.1
 */