package com.google.android.voicesearch.fragments;

import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.PhoneCallAction;
import com.google.android.voicesearch.logger.EventLogger;

public class PhoneCallController
  extends CommunicationActionController<PhoneCallAction, Ui>
{
  public PhoneCallController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  public void initUi()
  {
    PersonDisambiguation localPersonDisambiguation = ((PhoneCallAction)getVoiceAction()).getRecipient();
    if (localPersonDisambiguation == null) {
      ((Ui)getUi()).showEmptyRecipientCard();
    }
    for (;;)
    {
      uiReady();
      return;
      if ((localPersonDisambiguation.isCompleted()) && (!((Person)localPersonDisambiguation.get()).hasName())) {
        ((Ui)getUi()).setToContact(((Person)localPersonDisambiguation.get()).getSelectedItem());
      } else if (localPersonDisambiguation.hasAlternativeCandidates()) {
        ((Ui)getUi()).showContactDetailsNotFound(localPersonDisambiguation.getCandidates());
      } else {
        super.initUi();
      }
    }
  }
  
  protected void onDisambiguationCompleted(Disambiguation<Person> paramDisambiguation)
  {
    EventLogger.recordSpeechEvent(13, ((Person)paramDisambiguation.get()).getSelectedItem());
    if ((((PhoneCallAction)getVoiceAction()).canExecute()) && (paramDisambiguation.isSelectedByUser()))
    {
      executeAction(false);
      return;
    }
    showCard();
  }
  
  public static abstract interface Ui
    extends CommunicationActionCard
  {
    public abstract void setToContact(Contact paramContact);
    
    public abstract void showEmptyRecipientCard();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.PhoneCallController
 * JD-Core Version:    0.7.0.1
 */