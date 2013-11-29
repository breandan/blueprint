package com.android.datetimepicker.date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.datetimepicker.R.dimen;
import com.android.datetimepicker.R.layout;
import java.util.ArrayList;
import java.util.List;

public class YearPickerView
  extends ListView
  implements AdapterView.OnItemClickListener, DatePickerDialog.OnDateChangedListener
{
  private YearAdapter mAdapter;
  private int mChildSize;
  private final DatePickerController mController;
  private TextViewWithCircularIndicator mSelectedView;
  private int mViewSize;
  
  public YearPickerView(Context paramContext, DatePickerController paramDatePickerController)
  {
    super(paramContext);
    this.mController = paramDatePickerController;
    this.mController.registerOnDateChangedListener(this);
    setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    Resources localResources = paramContext.getResources();
    this.mViewSize = localResources.getDimensionPixelOffset(R.dimen.date_picker_view_animator_height);
    this.mChildSize = localResources.getDimensionPixelOffset(R.dimen.year_label_height);
    setVerticalFadingEdgeEnabled(true);
    setFadingEdgeLength(this.mChildSize / 3);
    init(paramContext);
    setOnItemClickListener(this);
    setSelector(new StateListDrawable());
    setDividerHeight(0);
    onDateChanged();
  }
  
  private static int getYearFromTextView(TextView paramTextView)
  {
    return Integer.valueOf(paramTextView.getText().toString()).intValue();
  }
  
  private void init(Context paramContext)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = this.mController.getMinYear(); i <= this.mController.getMaxYear(); i++)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(i);
      localArrayList.add(String.format("%d", arrayOfObject));
    }
    this.mAdapter = new YearAdapter(paramContext, R.layout.year_label_text_view, localArrayList);
    setAdapter(this.mAdapter);
  }
  
  public int getFirstPositionOffset()
  {
    View localView = getChildAt(0);
    if (localView == null) {
      return 0;
    }
    return localView.getTop();
  }
  
  public void onDateChanged()
  {
    this.mAdapter.notifyDataSetChanged();
    postSetSelectionCentered(this.mController.getSelectedDay().year - this.mController.getMinYear());
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    if (paramAccessibilityEvent.getEventType() == 4096)
    {
      paramAccessibilityEvent.setFromIndex(0);
      paramAccessibilityEvent.setToIndex(0);
    }
  }
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    this.mController.tryVibrate();
    TextViewWithCircularIndicator localTextViewWithCircularIndicator = (TextViewWithCircularIndicator)paramView;
    if (localTextViewWithCircularIndicator != null)
    {
      if (localTextViewWithCircularIndicator != this.mSelectedView)
      {
        if (this.mSelectedView != null)
        {
          this.mSelectedView.drawIndicator(false);
          this.mSelectedView.requestLayout();
        }
        localTextViewWithCircularIndicator.drawIndicator(true);
        localTextViewWithCircularIndicator.requestLayout();
        this.mSelectedView = localTextViewWithCircularIndicator;
      }
      this.mController.onYearSelected(getYearFromTextView(localTextViewWithCircularIndicator));
      this.mAdapter.notifyDataSetChanged();
    }
  }
  
  public void postSetSelectionCentered(int paramInt)
  {
    postSetSelectionFromTop(paramInt, this.mViewSize / 2 - this.mChildSize / 2);
  }
  
  public void postSetSelectionFromTop(final int paramInt1, final int paramInt2)
  {
    post(new Runnable()
    {
      public void run()
      {
        YearPickerView.this.setSelectionFromTop(paramInt1, paramInt2);
        YearPickerView.this.requestLayout();
      }
    });
  }
  
  private class YearAdapter
    extends ArrayAdapter<String>
  {
    public YearAdapter(int paramInt, List<String> paramList)
    {
      super(paramList, localList);
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      TextViewWithCircularIndicator localTextViewWithCircularIndicator = (TextViewWithCircularIndicator)super.getView(paramInt, paramView, paramViewGroup);
      localTextViewWithCircularIndicator.requestLayout();
      int i = YearPickerView.getYearFromTextView(localTextViewWithCircularIndicator);
      if (YearPickerView.this.mController.getSelectedDay().year == i) {}
      for (boolean bool = true;; bool = false)
      {
        localTextViewWithCircularIndicator.drawIndicator(bool);
        if (bool) {
          YearPickerView.access$202(YearPickerView.this, localTextViewWithCircularIndicator);
        }
        return localTextViewWithCircularIndicator;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.date.YearPickerView
 * JD-Core Version:    0.7.0.1
 */