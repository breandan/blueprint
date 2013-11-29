package com.google.android.voicesearch.handsfree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ViewDisplayer
{
  private final Context mContext;
  private final ViewGroup mViewGroup;
  
  public ViewDisplayer(Context paramContext, ViewGroup paramViewGroup)
  {
    this.mContext = paramContext;
    this.mViewGroup = paramViewGroup;
  }
  
  private void addView(View paramView)
  {
    this.mViewGroup.removeAllViews();
    this.mViewGroup.addView(paramView);
  }
  
  public InitializeController.Ui showInitialize()
  {
    InitializeView localInitializeView = new InitializeView(this.mContext);
    addView(localInitializeView);
    return localInitializeView;
  }
  
  public ErrorController.Ui showNoMatch()
  {
    ErrorView localErrorView = new ErrorView(this.mContext);
    addView(localErrorView);
    return localErrorView;
  }
  
  public PhoneCallContactController.Ui showPhoneCallContact()
  {
    PhoneCallContactView localPhoneCallContactView = new PhoneCallContactView(this.mContext);
    addView(localPhoneCallContactView);
    return localPhoneCallContactView;
  }
  
  public PhoneCallDisambigContactController.Ui showPhoneCallDisambigContact()
  {
    PhoneCallDisambigContactView localPhoneCallDisambigContactView = new PhoneCallDisambigContactView(this.mContext);
    addView(localPhoneCallDisambigContactView);
    return localPhoneCallDisambigContactView;
  }
  
  public PhoneCallContactController.Ui showPhoneCallNumber()
  {
    PhoneCallNumberView localPhoneCallNumberView = new PhoneCallNumberView(this.mContext);
    addView(localPhoneCallNumberView);
    return localPhoneCallNumberView;
  }
  
  public SpeakNowController.Ui showSpeakNow()
  {
    SpeakNowView localSpeakNowView = new SpeakNowView(this.mContext);
    addView(localSpeakNowView);
    return localSpeakNowView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.ViewDisplayer
 * JD-Core Version:    0.7.0.1
 */