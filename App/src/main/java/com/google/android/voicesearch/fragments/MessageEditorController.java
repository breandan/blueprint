package com.google.android.voicesearch.fragments;

import com.google.android.search.core.Feature;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.velvet.actions.Disambiguation.ProgressListener;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.SmsAction;

public class MessageEditorController
  extends CommunicationActionController<SmsAction, Ui>
  implements Disambiguation.ProgressListener<Person>
{
  public MessageEditorController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  private void initClassicUi(Disambiguation<Person> paramDisambiguation)
  {
    SmsAction localSmsAction = (SmsAction)getVoiceAction();
    int i;
    int j;
    label28:
    int k;
    label38:
    Ui localUi;
    if (paramDisambiguation != null)
    {
      i = 1;
      if ((i == 0) || (!Disambiguation.isCompleted(paramDisambiguation))) {
        break label121;
      }
      j = 1;
      if (localSmsAction.hasBody()) {
        break label127;
      }
      k = 1;
      localUi = (Ui)getUi();
      localUi.setMessageBody(localSmsAction.getBody());
      if (j == 0) {
        break label151;
      }
      Person localPerson = (Person)paramDisambiguation.get();
      Contact localContact = localPerson.getSelectedItem();
      localUi.setToPerson(localPerson);
      if (!localContact.hasName()) {
        break label133;
      }
      localUi.showContactField();
    }
    for (;;)
    {
      if (k == 0) {
        break label143;
      }
      localUi.showNewMessage();
      return;
      i = 0;
      break;
      label121:
      j = 0;
      break label28;
      label127:
      k = 0;
      break label38;
      label133:
      localUi.showNumberOnlyField();
    }
    label143:
    localUi.showSendMessage();
    return;
    label151:
    if (k == 0)
    {
      localUi.showContactNotFound();
      return;
    }
    if (getCardController().isFollowOnEnabledForRequest())
    {
      if ((paramDisambiguation != null) && (paramDisambiguation.hasNoResults())) {}
      for (boolean bool = true;; bool = false)
      {
        localUi.showEmptyViewWithPickContact(bool);
        return;
      }
    }
    localUi.showEmptyViewWithEditMessage();
  }
  
  public void initUi()
  {
    PersonDisambiguation localPersonDisambiguation = ((SmsAction)getVoiceAction()).getRecipient();
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
    if ((paramDisambiguation.isSelectedByUser()) && (!((SmsAction)getVoiceAction()).hasBody()) && (!Feature.FOLLOW_ON.isEnabled()))
    {
      bailOut();
      return;
    }
    showCard();
  }
  
  public static abstract interface Ui
    extends CommunicationActionCard
  {
    public abstract void setMessageBody(CharSequence paramCharSequence);
    
    public abstract void setToPerson(Person paramPerson);
    
    public abstract void showActionContent(boolean paramBoolean);
    
    public abstract void showContactField();
    
    public abstract void showEmptyViewWithEditMessage();
    
    public abstract void showEmptyViewWithPickContact(boolean paramBoolean);
    
    public abstract void showNewMessage();
    
    public abstract void showNumberOnlyField();
    
    public abstract void showSendMessage();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.MessageEditorController
 * JD-Core Version:    0.7.0.1
 */