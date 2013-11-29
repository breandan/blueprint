package com.google.android.voicesearch.fragments;

import android.text.TextUtils;
import com.google.android.search.core.Feature;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.velvet.actions.Disambiguation.ProgressListener;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.EmailAction;
import com.google.common.base.Preconditions;

public class EmailController
  extends CommunicationActionController<EmailAction, Ui>
  implements Disambiguation.ProgressListener<Person>
{
  public EmailController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  private void initClassicUi(Disambiguation<Person> paramDisambiguation)
  {
    EmailAction localEmailAction = (EmailAction)getVoiceAction();
    int i;
    int j;
    label33:
    int k;
    if (!TextUtils.isEmpty(localEmailAction.getBody()))
    {
      i = 1;
      if (TextUtils.isEmpty(localEmailAction.getSubject())) {
        break label139;
      }
      j = 1;
      if ((localEmailAction.canExecute()) || ((i | j) != 0) || ((paramDisambiguation != null) && (paramDisambiguation.isCompleted()))) {
        break label145;
      }
      k = 1;
      label61:
      if ((paramDisambiguation != null) && (k == 0)) {
        break label151;
      }
      ((Ui)getUi()).hideContactField();
    }
    for (;;)
    {
      ((Ui)getUi()).setSubject(localEmailAction.getSubject());
      ((Ui)getUi()).setBody(localEmailAction.getBody());
      if (!localEmailAction.canExecute()) {
        break label211;
      }
      ((Ui)getUi()).showSendEmail();
      return;
      i = 0;
      break;
      label139:
      j = 0;
      break label33;
      label145:
      k = 0;
      break label61;
      label151:
      if (paramDisambiguation.isCompleted())
      {
        ((Ui)getUi()).setToContact((Person)paramDisambiguation.get());
      }
      else if ((i != 0) || (j != 0))
      {
        Preconditions.checkState(paramDisambiguation.hasNoResults());
        ((Ui)getUi()).showContactNotFound();
      }
    }
    label211:
    if (k == 0)
    {
      ((Ui)getUi()).showEditEmail();
      return;
    }
    if (getCardController().isFollowOnEnabledForRequest())
    {
      if ((paramDisambiguation != null) && (paramDisambiguation.hasNoResults())) {}
      for (boolean bool = true;; bool = false)
      {
        ((Ui)getUi()).showEmptyViewWithPickContact(bool);
        return;
      }
    }
    ((Ui)getUi()).showEmptyViewWithEditEmail();
  }
  
  public void initUi()
  {
    PersonDisambiguation localPersonDisambiguation = ((EmailAction)getVoiceAction()).getRecipient();
    if ((localPersonDisambiguation == null) || ((localPersonDisambiguation.hasNoResults()) && (!localPersonDisambiguation.hasAlternativeCandidates())) || (Disambiguation.isCompleted(localPersonDisambiguation)))
    {
      initClassicUi(localPersonDisambiguation);
      ((Ui)getUi()).showActionContent(true);
      return;
    }
    if (localPersonDisambiguation.hasAlternativeCandidates())
    {
      ((Ui)getUi()).showContactDetailsNotFound(localPersonDisambiguation.getCandidates());
      uiReady();
      return;
    }
    super.initUi();
    ((Ui)getUi()).showActionContent(false);
    uiReady();
  }
  
  protected void onDisambiguationCompleted(Disambiguation<Person> paramDisambiguation)
  {
    if ((paramDisambiguation.isSelectedByUser()) && (!((EmailAction)getVoiceAction()).canExecute()) && (!Feature.FOLLOW_ON.isEnabled()))
    {
      bailOut();
      return;
    }
    showCard();
  }
  
  protected void onExecuteError()
  {
    getCardController().showToast(2131363491);
  }
  
  public void setBody(String paramString)
  {
    EmailAction localEmailAction = (EmailAction)getVoiceAction();
    boolean bool1 = localEmailAction.canExecute();
    localEmailAction.setBody(paramString);
    boolean bool2 = localEmailAction.canExecute();
    if ((bool1) && (!bool2))
    {
      ((Ui)getUi()).showEditEmail();
      return;
    }
    ((Ui)getUi()).showSendEmail();
  }
  
  public void setSubject(String paramString)
  {
    ((EmailAction)getVoiceAction()).setSubject(paramString);
  }
  
  public static abstract interface Ui
    extends CommunicationActionCard
  {
    public abstract void hideContactField();
    
    public abstract void setBody(String paramString);
    
    public abstract void setSubject(String paramString);
    
    public abstract void setToContact(Person paramPerson);
    
    public abstract void showActionContent(boolean paramBoolean);
    
    public abstract void showEditEmail();
    
    public abstract void showEmptyViewWithEditEmail();
    
    public abstract void showEmptyViewWithPickContact(boolean paramBoolean);
    
    public abstract void showSendEmail();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.EmailController
 * JD-Core Version:    0.7.0.1
 */