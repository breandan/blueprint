package com.google.android.search.shared.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.QuickContactBadge;

public class ContactBadge
  extends QuickContactBadge
{
  private View.OnClickListener mExtraOnClickListener;
  
  public ContactBadge(Context paramContext)
  {
    super(paramContext);
  }
  
  public ContactBadge(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public ContactBadge(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void onClick(View paramView)
  {
    super.onClick(paramView);
    if (this.mExtraOnClickListener != null) {
      this.mExtraOnClickListener.onClick(paramView);
    }
  }
  
  public void setExtraOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mExtraOnClickListener = paramOnClickListener;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.ContactBadge
 * JD-Core Version:    0.7.0.1
 */