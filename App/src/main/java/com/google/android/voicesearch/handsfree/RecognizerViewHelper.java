package com.google.android.voicesearch.handsfree;

import android.view.View;
import android.widget.TextView;

public class RecognizerViewHelper
{
  private final TextView mLanguage;
  private final View mView;
  
  public RecognizerViewHelper(View paramView)
  {
    this.mView = paramView.findViewById(2131296679);
    this.mLanguage = ((TextView)paramView.findViewById(2131296680));
  }
  
  public void setLanguage(String paramString)
  {
    this.mLanguage.setText(paramString);
  }
  
  public void showListening()
  {
    this.mView.setVisibility(0);
    this.mView.setBackgroundResource(2131230895);
  }
  
  public void showNotListening()
  {
    this.mView.setVisibility(0);
    this.mView.setBackgroundResource(2131230896);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.RecognizerViewHelper
 * JD-Core Version:    0.7.0.1
 */