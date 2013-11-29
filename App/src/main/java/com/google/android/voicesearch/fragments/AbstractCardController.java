package com.google.android.voicesearch.fragments;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.velvet.presenter.MainContentPresenter.Transaction;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.fragments.executor.ActionExecutor;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public abstract class AbstractCardController<T extends VoiceAction, U extends BaseCardUi>
{
  private final String TAG = "AbstractCardController." + getClass().getSimpleName();
  private ActionExecutor<T> mActionExecutor;
  private final CardController mCardController;
  private final Runnable mExecuteRunnable;
  private boolean mExecuting;
  private U mUi;
  private T mVoiceAction;
  
  public AbstractCardController(CardController paramCardController)
  {
    this.mCardController = paramCardController;
    this.mExecuteRunnable = new Runnable()
    {
      public void run()
      {
        AbstractCardController.this.executeAction(true);
      }
    };
  }
  
  private void checkProberIntents()
  {
    new AsyncTask()
    {
      protected Boolean doInBackground(Void... paramAnonymousVarArgs)
      {
        return Boolean.valueOf(AbstractCardController.this.mActionExecutor.canExecute(AbstractCardController.this.getVoiceAction()));
      }
      
      protected void onPostExecute(Boolean paramAnonymousBoolean)
      {
        if (!paramAnonymousBoolean.booleanValue())
        {
          EventLogger.recordClientEvent(110, Integer.valueOf(AbstractCardController.this.getActionTypeLog()));
          if (AbstractCardController.this.isAttached()) {
            AbstractCardController.this.getUi().showDisabled();
          }
          AbstractCardController.this.cancelCountDown();
        }
        if (paramAnonymousBoolean.booleanValue()) {
          AbstractCardController.this.mCardController.updateActionTts(AbstractCardController.this.mVoiceAction);
        }
      }
    }.executeOnExecutor(this.mCardController.getBackgroundExecutor(), new Void[0]);
  }
  
  protected void actionComplete()
  {
    this.mCardController.onCardActionComplete();
  }
  
  public void attach(U paramU)
  {
    Log.i(this.TAG, "#attach");
    setUi(paramU);
    this.mCardController.logAttach();
    initUi();
    setFollowOnPrompt();
    checkProberIntents();
  }
  
  public final void bailOut()
  {
    this.mCardController.logOpenExternalApp();
    cancelCountDown();
    new AsyncTask()
    {
      protected Boolean doInBackground(Void... paramAnonymousVarArgs)
      {
        return Boolean.valueOf(AbstractCardController.this.mActionExecutor.openExternalApp(AbstractCardController.this.mVoiceAction));
      }
      
      protected void onPostExecute(Boolean paramAnonymousBoolean)
      {
        if (!paramAnonymousBoolean.booleanValue()) {
          AbstractCardController.this.onOpenExternalAppError();
        }
      }
    }.executeOnExecutor(this.mCardController.getBackgroundExecutor(), new Void[0]);
  }
  
  public void cancel() {}
  
  public boolean cancelCountDown()
  {
    boolean bool = this.mCardController.cancelCountDown();
    if ((bool) && (isAttached())) {
      getCountDownUi().cancelCountDownAnimation();
    }
    return bool;
  }
  
  public void cancelCountDownByUser()
  {
    if (cancelCountDown()) {
      this.mCardController.logCancelCountDownByUser();
    }
  }
  
  protected void clearCard()
  {
    this.mCardController.removeVoiceAction(this.mVoiceAction);
  }
  
  public void detach()
  {
    cancelCountDown();
    setUi(null);
  }
  
  public void dismissed()
  {
    this.mCardController.onDismissed(this.mVoiceAction);
    clearCard();
    onUserInteraction();
  }
  
  public final void executeAction(boolean paramBoolean)
  {
    if (this.mExecuting) {
      return;
    }
    if (this.mVoiceAction.canExecute())
    {
      this.mCardController.logExecute(paramBoolean);
      cancelCountDown();
      this.mExecuting = true;
      onPreExecute();
      new AsyncTask()
      {
        protected Boolean doInBackground(Void... paramAnonymousVarArgs)
        {
          return Boolean.valueOf(AbstractCardController.this.mActionExecutor.execute(AbstractCardController.this.mVoiceAction));
        }
        
        protected void onPostExecute(Boolean paramAnonymousBoolean)
        {
          if (paramAnonymousBoolean.booleanValue())
          {
            AbstractCardController.this.actionComplete();
            AbstractCardController.this.onPostExecute();
          }
          for (;;)
          {
            AbstractCardController.access$302(AbstractCardController.this, false);
            return;
            AbstractCardController.this.onExecuteError();
          }
        }
      }.executeOnExecutor(this.mCardController.getBackgroundExecutor(), new Void[0]);
      return;
    }
    bailOut();
  }
  
  public ActionExecutor<T> getActionExecutor()
  {
    return this.mActionExecutor;
  }
  
  protected int getActionTypeLog()
  {
    return this.mCardController.getActionTypeLog();
  }
  
  protected CardController getCardController()
  {
    return this.mCardController;
  }
  
  protected final CountDownUi getCountDownUi()
  {
    return (CountDownUi)getUi();
  }
  
  protected U getUi()
  {
    return (BaseCardUi)Preconditions.checkNotNull(this.mUi);
  }
  
  public T getVoiceAction()
  {
    return this.mVoiceAction;
  }
  
  protected abstract void initUi();
  
  public boolean isAttached()
  {
    return this.mUi != null;
  }
  
  protected void mentionEntity(@Nullable Object paramObject)
  {
    getCardController().mentionEntity(paramObject);
  }
  
  public boolean onBackPressed()
  {
    return false;
  }
  
  protected void onExecuteError() {}
  
  protected void onOpenExternalAppError() {}
  
  protected void onPostExecute()
  {
    clearCard();
  }
  
  protected void onPreExecute() {}
  
  @Deprecated
  public void onTtsPlayStateChanged()
  {
    if (isAttached()) {
      this.mUi.setFollowOnPromptState(this.mCardController.isTtsPlaying());
    }
  }
  
  protected void onUserInteraction()
  {
    this.mCardController.onUserInteraction();
  }
  
  public void postTransaction(MainContentPresenter.Transaction paramTransaction)
  {
    getCardController().post(paramTransaction);
  }
  
  public void setActionExecutor(ActionExecutor<T> paramActionExecutor)
  {
    this.mActionExecutor = paramActionExecutor;
  }
  
  public void setFollowOnPrompt()
  {
    BaseCardUi localBaseCardUi = getUi();
    localBaseCardUi.setFollowOnPrompt(this.mCardController.getDisplayPrompt(this.mVoiceAction));
    localBaseCardUi.setFollowOnPromptState(this.mCardController.isTtsPlaying());
  }
  
  protected void setUi(U paramU)
  {
    this.mUi = paramU;
  }
  
  public void setVoiceAction(T paramT)
  {
    this.mVoiceAction = paramT;
  }
  
  protected void showCard()
  {
    if ((!this.mCardController.showCard(this.mVoiceAction)) && (isAttached()))
    {
      updateUi();
      setFollowOnPrompt();
      this.mCardController.updateActionTts(this.mVoiceAction);
    }
  }
  
  public void start()
  {
    showCard();
  }
  
  public void uiReady()
  {
    if ((isAttached()) && (this.mVoiceAction.canExecute()) && (this.mCardController.takeStartCountDown(this.mVoiceAction)))
    {
      long l = this.mCardController.startCountDown(this.mVoiceAction, this.mExecuteRunnable);
      if (l > 0L) {
        getCountDownUi().startCountDownAnimation(l);
      }
    }
  }
  
  protected void updateUi()
  {
    initUi();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.AbstractCardController
 * JD-Core Version:    0.7.0.1
 */