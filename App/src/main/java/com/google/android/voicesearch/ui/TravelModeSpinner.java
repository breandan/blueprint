package com.google.android.voicesearch.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TravelModeSpinner
  extends Spinner
{
  private int mActionType;
  
  public TravelModeSpinner(Context paramContext)
  {
    super(paramContext);
  }
  
  public TravelModeSpinner(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }
  
  public TravelModeSpinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TravelModeSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TravelModeSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private String[] getArrayLabels(int paramInt)
  {
    Context localContext = getContext();
    if (3 == paramInt) {
      return localContext.getResources().getStringArray(2131492875);
    }
    return localContext.getResources().getStringArray(2131492873);
  }
  
  private int[] getArrayValues(int paramInt)
  {
    if (3 == paramInt) {
      return getContext().getResources().getIntArray(2131492876);
    }
    return getContext().getResources().getIntArray(2131492874);
  }
  
  private void setTransportationMethod(int paramInt)
  {
    int[] arrayOfInt = getArrayValues(this.mActionType);
    for (int i = 0;; i++)
    {
      int j = arrayOfInt.length;
      int k = 0;
      if (i < j)
      {
        if (arrayOfInt[i] == paramInt) {
          k = i;
        }
      }
      else
      {
        setSelection(k);
        return;
      }
    }
  }
  
  public int getSelectedTransportationMethod()
  {
    if (!inSupportedActionType()) {
      return 0;
    }
    int i = getSelectedItemPosition();
    return getArrayValues(this.mActionType)[i];
  }
  
  public boolean inSupportedActionType()
  {
    int i = 1;
    if ((4 == this.mActionType) || (i == this.mActionType)) {
      i = 0;
    }
    return i;
  }
  
  public void setActionType(int paramInt1, int paramInt2)
  {
    this.mActionType = paramInt1;
    if (!inSupportedActionType())
    {
      setVisibility(8);
      return;
    }
    setAdapter(new ArrayAdapter(getContext(), 2130968889, getArrayLabels(paramInt1)));
    setTransportationMethod(paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ui.TravelModeSpinner
 * JD-Core Version:    0.7.0.1
 */