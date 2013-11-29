package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.HelpAction;

public class HelpController
  extends AbstractCardController<HelpAction, Ui>
{
  private final boolean mIs24HourFormat;
  private SimpleHelpController mSimpleHelpController;
  
  public HelpController(CardController paramCardController, boolean paramBoolean)
  {
    super(paramCardController);
    this.mIs24HourFormat = paramBoolean;
  }
  
  public void initUi()
  {
    this.mSimpleHelpController.attachUi((SimpleHelpController.Ui)getUi());
  }
  
  public void refreshExample()
  {
    this.mSimpleHelpController.showNextExample();
  }
  
  public void start()
  {
    this.mSimpleHelpController = new SimpleHelpController((HelpAction)getVoiceAction(), this.mIs24HourFormat);
    showCard();
  }
  
  public static abstract interface Ui
    extends BaseCardUi, SimpleHelpController.Ui
  {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.HelpController
 * JD-Core Version:    0.7.0.1
 */