package com.google.android.velvet.ui.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomValueArrayAdapter<T>
  extends ArrayAdapter<T>
{
  private CharSequence mCustomValue;
  private int mCustomValuePosition;
  private final LayoutInflater mInflater;
  
  public CustomValueArrayAdapter(Context paramContext, int paramInt, T[] paramArrayOfT, T paramT)
  {
    super(paramContext, paramInt);
    addAll(paramArrayOfT);
    this.mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = super.getView(paramInt, paramView, paramViewGroup);
    if (paramInt == this.mCustomValuePosition) {
      ((TextView)localView).setText(this.mCustomValue);
    }
    return localView;
  }
  
  public void notifyDataSetChanged()
  {
    this.mCustomValuePosition = (-1 + getCount());
    super.notifyDataSetChanged();
  }
  
  public void setCustomValue(CharSequence paramCharSequence)
  {
    if (!TextUtils.equals(this.mCustomValue, paramCharSequence))
    {
      this.mCustomValue = paramCharSequence;
      notifyDataSetChanged();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.util.CustomValueArrayAdapter
 * JD-Core Version:    0.7.0.1
 */