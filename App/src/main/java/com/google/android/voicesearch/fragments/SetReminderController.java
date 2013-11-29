package com.google.android.voicesearch.fragments;

import com.google.android.speech.callback.SimpleCallback;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.android.voicesearch.fragments.reminders.EditReminderPresenter;
import com.google.android.voicesearch.fragments.reminders.EditReminderUi;

public class SetReminderController
  extends AbstractCardController<SetReminderAction, Ui>
{
  private final EditReminderPresenter mPresenter;
  private final SimpleCallback<Boolean> mSaveOnLocationAliasConfirmedCallback = new SimpleCallback()
  {
    public void onResult(Boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean.booleanValue())
      {
        if (SetReminderController.this.isAttached()) {
          ((SetReminderController.Ui)SetReminderController.this.getUi()).showSaving();
        }
        SetReminderController.this.actionComplete();
        SetReminderController.this.clearCard();
        SetReminderController.this.mPresenter.saveReminder(SetReminderController.this.mToastCallback);
      }
    }
  };
  private final SimpleCallback<Boolean> mToastCallback = new SimpleCallback()
  {
    public void onResult(Boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean.booleanValue())
      {
        SetReminderController.this.getCardController().showToast(2131363516);
        return;
      }
      SetReminderController.this.getCardController().showToast(2131363517);
    }
  };
  
  public SetReminderController(CardController paramCardController, EditReminderPresenter paramEditReminderPresenter)
  {
    super(paramCardController);
    this.mPresenter = paramEditReminderPresenter;
  }
  
  public void initUi()
  {
    Ui localUi = (Ui)getUi();
    this.mPresenter.setUi(localUi);
    localUi.setPresenter(this.mPresenter);
    this.mPresenter.initUi();
  }
  
  protected void onPreExecute()
  {
    this.mPresenter.setAliasLocationIfRequired(this.mSaveOnLocationAliasConfirmedCallback);
  }
  
  public static abstract interface Ui
    extends BaseCardUi, CountDownUi, EditReminderUi
  {
    public abstract void setPresenter(EditReminderPresenter paramEditReminderPresenter);
    
    public abstract void showSaving();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.SetReminderController
 * JD-Core Version:    0.7.0.1
 */