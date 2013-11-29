package com.google.android.voicesearch.handsfree;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

class ErrorView
  extends FrameLayout
  implements ErrorController.Ui
{
  private final TextView mTextView;
  
  public ErrorView(Context paramContext)
  {
    super(paramContext);
    ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(2130968712, this);
    this.mTextView = ((TextView)findViewById(2131296678));
  }
  
  public void setMessage(int paramInt)
  {
    this.mTextView.setText(paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.ErrorView
 * JD-Core Version:    0.7.0.1
 */