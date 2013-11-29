package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.CancelAction;

public class CancelController
  extends AbstractCardController<CancelAction, Ui>
{
  public CancelController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  public void cancelAction()
  {
    getCardController().cancelAction();
    onUserInteraction();
  }
  
  protected void initUi()
  {
    CancelAction localCancelAction = (CancelAction)getVoiceAction();
    ((Ui)getUi()).showMessage(localCancelAction.getMessageId());
  }
  
  public static abstract interface Ui
    extends BaseCardUi
  {
    public abstract void showMessage(int paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.CancelController
 * JD-Core Version:    0.7.0.1
 */