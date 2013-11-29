package com.android.datetimepicker.date;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.datetimepicker.HapticFeedbackController;
import com.android.datetimepicker.R.id;
import com.android.datetimepicker.R.layout;
import com.android.datetimepicker.R.string;
import com.android.datetimepicker.Utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

public class DatePickerDialog
  extends DialogFragment
  implements View.OnClickListener, DatePickerController
{
  private static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd", Locale.getDefault());
  private static SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
  private AccessibleDateAnimator mAnimator;
  private final Calendar mCalendar = Calendar.getInstance();
  private OnDateSetListener mCallBack;
  private int mCurrentView = -1;
  private TextView mDayOfWeekView;
  private String mDayPickerDescription;
  private DayPickerView mDayPickerView;
  private boolean mDelayAnimation = true;
  private Button mDoneButton;
  private HapticFeedbackController mHapticFeedbackController;
  private HashSet<OnDateChangedListener> mListeners = new HashSet();
  private int mMaxYear = 2100;
  private int mMinYear = 1900;
  private LinearLayout mMonthAndDayView;
  private String mSelectDay;
  private String mSelectYear;
  private TextView mSelectedDayTextView;
  private TextView mSelectedMonthTextView;
  private int mWeekStart = this.mCalendar.getFirstDayOfWeek();
  private String mYearPickerDescription;
  private YearPickerView mYearPickerView;
  private TextView mYearView;
  
  private void adjustDayInMonthIfNeeded(int paramInt1, int paramInt2)
  {
    int i = this.mCalendar.get(5);
    int j = Utils.getDaysInMonth(paramInt1, paramInt2);
    if (i > j) {
      this.mCalendar.set(5, j);
    }
  }
  
  public static DatePickerDialog newInstance(OnDateSetListener paramOnDateSetListener, int paramInt1, int paramInt2, int paramInt3)
  {
    DatePickerDialog localDatePickerDialog = new DatePickerDialog();
    localDatePickerDialog.initialize(paramOnDateSetListener, paramInt1, paramInt2, paramInt3);
    return localDatePickerDialog;
  }
  
  private void setCurrentView(int paramInt)
  {
    long l = this.mCalendar.getTimeInMillis();
    switch (paramInt)
    {
    default: 
      return;
    case 0: 
      ObjectAnimator localObjectAnimator2 = Utils.getPulseAnimator(this.mMonthAndDayView, 0.9F, 1.05F);
      if (this.mDelayAnimation)
      {
        localObjectAnimator2.setStartDelay(500L);
        this.mDelayAnimation = false;
      }
      this.mDayPickerView.onDateChanged();
      if (this.mCurrentView != paramInt)
      {
        this.mMonthAndDayView.setSelected(true);
        this.mYearView.setSelected(false);
        this.mAnimator.setDisplayedChild(0);
        this.mCurrentView = paramInt;
      }
      localObjectAnimator2.start();
      String str2 = DateUtils.formatDateTime(getActivity(), l, 16);
      this.mAnimator.setContentDescription(this.mDayPickerDescription + ": " + str2);
      Utils.tryAccessibilityAnnounce(this.mAnimator, this.mSelectDay);
      return;
    }
    ObjectAnimator localObjectAnimator1 = Utils.getPulseAnimator(this.mYearView, 0.85F, 1.1F);
    if (this.mDelayAnimation)
    {
      localObjectAnimator1.setStartDelay(500L);
      this.mDelayAnimation = false;
    }
    this.mYearPickerView.onDateChanged();
    if (this.mCurrentView != paramInt)
    {
      this.mMonthAndDayView.setSelected(false);
      this.mYearView.setSelected(true);
      this.mAnimator.setDisplayedChild(1);
      this.mCurrentView = paramInt;
    }
    localObjectAnimator1.start();
    String str1 = YEAR_FORMAT.format(Long.valueOf(l));
    this.mAnimator.setContentDescription(this.mYearPickerDescription + ": " + str1);
    Utils.tryAccessibilityAnnounce(this.mAnimator, this.mSelectYear);
  }
  
  private void updateDisplay(boolean paramBoolean)
  {
    if (this.mDayOfWeekView != null) {
      this.mDayOfWeekView.setText(this.mCalendar.getDisplayName(7, 2, Locale.getDefault()).toUpperCase(Locale.getDefault()));
    }
    this.mSelectedMonthTextView.setText(this.mCalendar.getDisplayName(2, 1, Locale.getDefault()).toUpperCase(Locale.getDefault()));
    this.mSelectedDayTextView.setText(DAY_FORMAT.format(this.mCalendar.getTime()));
    this.mYearView.setText(YEAR_FORMAT.format(this.mCalendar.getTime()));
    long l = this.mCalendar.getTimeInMillis();
    this.mAnimator.setDateMillis(l);
    String str1 = DateUtils.formatDateTime(getActivity(), l, 24);
    this.mMonthAndDayView.setContentDescription(str1);
    if (paramBoolean)
    {
      String str2 = DateUtils.formatDateTime(getActivity(), l, 20);
      Utils.tryAccessibilityAnnounce(this.mAnimator, str2);
    }
  }
  
  private void updatePickers()
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((OnDateChangedListener)localIterator.next()).onDateChanged();
    }
  }
  
  public int getFirstDayOfWeek()
  {
    return this.mWeekStart;
  }
  
  public int getMaxYear()
  {
    return this.mMaxYear;
  }
  
  public int getMinYear()
  {
    return this.mMinYear;
  }
  
  public MonthAdapter.CalendarDay getSelectedDay()
  {
    return new MonthAdapter.CalendarDay(this.mCalendar);
  }
  
  public void initialize(OnDateSetListener paramOnDateSetListener, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mCallBack = paramOnDateSetListener;
    this.mCalendar.set(1, paramInt1);
    this.mCalendar.set(2, paramInt2);
    this.mCalendar.set(5, paramInt3);
  }
  
  public void onClick(View paramView)
  {
    tryVibrate();
    if (paramView.getId() == R.id.date_picker_year) {
      setCurrentView(1);
    }
    while (paramView.getId() != R.id.date_picker_month_and_day) {
      return;
    }
    setCurrentView(0);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getActivity().getWindow().setSoftInputMode(3);
    if (paramBundle != null)
    {
      this.mCalendar.set(1, paramBundle.getInt("year"));
      this.mCalendar.set(2, paramBundle.getInt("month"));
      this.mCalendar.set(5, paramBundle.getInt("day"));
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    Log.d("DatePickerDialog", "onCreateView: ");
    getDialog().getWindow().requestFeature(1);
    View localView = paramLayoutInflater.inflate(R.layout.date_picker_dialog, null);
    this.mDayOfWeekView = ((TextView)localView.findViewById(R.id.date_picker_header));
    this.mMonthAndDayView = ((LinearLayout)localView.findViewById(R.id.date_picker_month_and_day));
    this.mMonthAndDayView.setOnClickListener(this);
    this.mSelectedMonthTextView = ((TextView)localView.findViewById(R.id.date_picker_month));
    this.mSelectedDayTextView = ((TextView)localView.findViewById(R.id.date_picker_day));
    this.mYearView = ((TextView)localView.findViewById(R.id.date_picker_year));
    this.mYearView.setOnClickListener(this);
    int i = -1;
    int j = 0;
    int k = 0;
    if (paramBundle != null)
    {
      this.mWeekStart = paramBundle.getInt("week_start");
      this.mMinYear = paramBundle.getInt("year_start");
      this.mMaxYear = paramBundle.getInt("year_end");
      j = paramBundle.getInt("current_view");
      i = paramBundle.getInt("list_position");
      k = paramBundle.getInt("list_position_offset");
    }
    Activity localActivity = getActivity();
    this.mDayPickerView = new SimpleDayPickerView(localActivity, this);
    this.mYearPickerView = new YearPickerView(localActivity, this);
    Resources localResources = getResources();
    this.mDayPickerDescription = localResources.getString(R.string.day_picker_description);
    this.mSelectDay = localResources.getString(R.string.select_day);
    this.mYearPickerDescription = localResources.getString(R.string.year_picker_description);
    this.mSelectYear = localResources.getString(R.string.select_year);
    this.mAnimator = ((AccessibleDateAnimator)localView.findViewById(R.id.animator));
    this.mAnimator.addView(this.mDayPickerView);
    this.mAnimator.addView(this.mYearPickerView);
    this.mAnimator.setDateMillis(this.mCalendar.getTimeInMillis());
    AlphaAnimation localAlphaAnimation1 = new AlphaAnimation(0.0F, 1.0F);
    localAlphaAnimation1.setDuration(300L);
    this.mAnimator.setInAnimation(localAlphaAnimation1);
    AlphaAnimation localAlphaAnimation2 = new AlphaAnimation(1.0F, 0.0F);
    localAlphaAnimation2.setDuration(300L);
    this.mAnimator.setOutAnimation(localAlphaAnimation2);
    this.mDoneButton = ((Button)localView.findViewById(R.id.done));
    this.mDoneButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        DatePickerDialog.this.tryVibrate();
        if (DatePickerDialog.this.mCallBack != null) {
          DatePickerDialog.this.mCallBack.onDateSet(DatePickerDialog.this, DatePickerDialog.this.mCalendar.get(1), DatePickerDialog.this.mCalendar.get(2), DatePickerDialog.this.mCalendar.get(5));
        }
        DatePickerDialog.this.dismiss();
      }
    });
    updateDisplay(false);
    setCurrentView(j);
    if (i != -1)
    {
      if (j != 0) {
        break label468;
      }
      this.mDayPickerView.postSetSelection(i);
    }
    for (;;)
    {
      this.mHapticFeedbackController = new HapticFeedbackController(localActivity);
      return localView;
      label468:
      if (j == 1) {
        this.mYearPickerView.postSetSelectionFromTop(i, k);
      }
    }
  }
  
  public void onDayOfMonthSelected(int paramInt1, int paramInt2, int paramInt3)
  {
    this.mCalendar.set(1, paramInt1);
    this.mCalendar.set(2, paramInt2);
    this.mCalendar.set(5, paramInt3);
    updatePickers();
    updateDisplay(true);
  }
  
  public void onPause()
  {
    super.onPause();
    this.mHapticFeedbackController.stop();
  }
  
  public void onResume()
  {
    super.onResume();
    this.mHapticFeedbackController.start();
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("year", this.mCalendar.get(1));
    paramBundle.putInt("month", this.mCalendar.get(2));
    paramBundle.putInt("day", this.mCalendar.get(5));
    paramBundle.putInt("week_start", this.mWeekStart);
    paramBundle.putInt("year_start", this.mMinYear);
    paramBundle.putInt("year_end", this.mMaxYear);
    paramBundle.putInt("current_view", this.mCurrentView);
    int i = -1;
    if (this.mCurrentView == 0) {
      i = this.mDayPickerView.getMostVisiblePosition();
    }
    for (;;)
    {
      paramBundle.putInt("list_position", i);
      return;
      if (this.mCurrentView == 1)
      {
        i = this.mYearPickerView.getFirstVisiblePosition();
        paramBundle.putInt("list_position_offset", this.mYearPickerView.getFirstPositionOffset());
      }
    }
  }
  
  public void onYearSelected(int paramInt)
  {
    adjustDayInMonthIfNeeded(this.mCalendar.get(2), paramInt);
    this.mCalendar.set(1, paramInt);
    updatePickers();
    setCurrentView(0);
    updateDisplay(true);
  }
  
  public void registerOnDateChangedListener(OnDateChangedListener paramOnDateChangedListener)
  {
    this.mListeners.add(paramOnDateChangedListener);
  }
  
  public void setFirstDayOfWeek(int paramInt)
  {
    if ((paramInt < 1) || (paramInt > 7)) {
      throw new IllegalArgumentException("Value must be between Calendar.SUNDAY and Calendar.SATURDAY");
    }
    this.mWeekStart = paramInt;
    if (this.mDayPickerView != null) {
      this.mDayPickerView.onChange();
    }
  }
  
  public void setOnDateSetListener(OnDateSetListener paramOnDateSetListener)
  {
    this.mCallBack = paramOnDateSetListener;
  }
  
  public void setYearRange(int paramInt1, int paramInt2)
  {
    if (paramInt2 <= paramInt1) {
      throw new IllegalArgumentException("Year end must be larger than year start");
    }
    this.mMinYear = paramInt1;
    this.mMaxYear = paramInt2;
    if (this.mDayPickerView != null) {
      this.mDayPickerView.onChange();
    }
  }
  
  public void tryVibrate()
  {
    this.mHapticFeedbackController.tryVibrate();
  }
  
  public static abstract interface OnDateChangedListener
  {
    public abstract void onDateChanged();
  }
  
  public static abstract interface OnDateSetListener
  {
    public abstract void onDateSet(DatePickerDialog paramDatePickerDialog, int paramInt1, int paramInt2, int paramInt3);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.DatePickerDialog
 * JD-Core Version:    0.7.0.1
 */