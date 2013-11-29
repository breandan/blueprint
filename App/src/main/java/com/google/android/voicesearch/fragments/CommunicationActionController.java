package com.google.android.voicesearch.fragments;

import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.velvet.actions.Disambiguation.ProgressListener;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.android.voicesearch.fragments.executor.ActionExecutor;
import com.google.android.voicesearch.fragments.executor.CommunicationActionExecutor;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public abstract class CommunicationActionController<T extends CommunicationAction, U extends CommunicationActionCard>
  extends AbstractCardController<T, U>
  implements Disambiguation.ProgressListener<Person>
{
  public CommunicationActionController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  private void onDisambiguationProgress(Disambiguation<Person> paramDisambiguation, boolean paramBoolean)
  {
    if (paramBoolean) {
      getCardController().updateCardDecision(getVoiceAction());
    }
    if (paramDisambiguation.isOngoing())
    {
      showCard();
      return;
    }
    if (paramDisambiguation.isCompleted())
    {
      mentionEntity(paramDisambiguation.get());
      onDisambiguationCompleted(paramDisambiguation);
      return;
    }
    Preconditions.checkState(paramDisambiguation.hasNoResults());
    showCard();
  }
  
  protected void initUi()
  {
    CommunicationActionCard localCommunicationActionCard = (CommunicationActionCard)getUi();
    PersonDisambiguation localPersonDisambiguation = ((CommunicationAction)getVoiceAction()).getRecipient();
    if ((localPersonDisambiguation == null) || (localPersonDisambiguation.hasNoResults()))
    {
      localCommunicationActionCard.showContactNotFound();
      return;
    }
    localCommunicationActionCard.setPeople(localPersonDisambiguation.getCandidates());
  }
  
  public void onContactDetailSelected(Person paramPerson, Contact paramContact)
  {
    EventLogger.recordClientEvent(44, Integer.valueOf(getActionTypeLog()));
    PersonDisambiguation localPersonDisambiguation = ((CommunicationAction)getVoiceAction()).getRecipient();
    paramPerson.setSelectedItem(paramContact);
    if (localPersonDisambiguation.isOngoing()) {
      localPersonDisambiguation.select(paramPerson);
    }
    onUserInteraction();
  }
  
  protected abstract void onDisambiguationCompleted(Disambiguation<Person> paramDisambiguation);
  
  public void onDisambiguationProgress(Disambiguation<Person> paramDisambiguation)
  {
    onDisambiguationProgress(paramDisambiguation, true);
  }
  
  public void onPersonSelected(Person paramPerson)
  {
    ((CommunicationAction)getVoiceAction()).getRecipient().refineCandidates(Lists.newArrayList(new Person[] { paramPerson }));
    onUserInteraction();
  }
  
  public void pickContact()
  {
    onUserInteraction();
    ((CommunicationActionExecutor)getActionExecutor()).pickContact();
  }
  
  public void setActionExecutor(ActionExecutor<T> paramActionExecutor)
  {
    Preconditions.checkArgument(paramActionExecutor instanceof CommunicationActionExecutor);
    super.setActionExecutor(paramActionExecutor);
  }
  
  public void start()
  {
    ((CommunicationAction)getVoiceAction()).setDisambiguationProgressListener(this);
    PersonDisambiguation localPersonDisambiguation = ((CommunicationAction)getVoiceAction()).getRecipient();
    if (localPersonDisambiguation != null)
    {
      onDisambiguationProgress(localPersonDisambiguation, false);
      return;
    }
    showCard();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.CommunicationActionController
 * JD-Core Version:    0.7.0.1
 */