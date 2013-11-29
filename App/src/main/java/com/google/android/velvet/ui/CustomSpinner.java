package com.google.android.velvet.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class CustomSpinner
  extends Spinner
{
  private int mLastSelection = 0;
  OnItemSeletedAlwaysListener mListener;
  
  public CustomSpinner(Context paramContext)
  {
    super(paramContext);
  }
  
  public CustomSpinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public CustomSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void revertToPreviousSelection()
  {
    if (this.mLastSelection != getSelectedItemPosition()) {
      setSelectionNoCallback(this.mLastSelection);
    }
  }
  
  public void setOnItemSelectedAlwaysListener(OnItemSeletedAlwaysListener paramOnItemSeletedAlwaysListener)
  {
    this.mListener = paramOnItemSeletedAlwaysListener;
  }
  
  public void setSelection(int paramInt)
  {
    this.mLastSelection = getSelectedItemPosition();
    super.setSelection(paramInt);
    if (this.mListener != null) {
      this.mListener.onItemSelected(paramInt);
    }
  }
  
  public void setSelectionNoCallback(int paramInt)
  {
    super.setSelection(paramInt);
  }
  
  public static abstract interface OnItemSeletedAlwaysListener
  {
    public abstract void onItemSelected(int paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.CustomSpinner
 * JD-Core Version:    0.7.0.1
 */