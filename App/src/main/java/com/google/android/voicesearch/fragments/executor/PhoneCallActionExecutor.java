package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.voicesearch.fragments.action.PhoneCallAction;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;

public class PhoneCallActionExecutor
  extends CommunicationActionExecutor<PhoneCallAction>
{
  public PhoneCallActionExecutor(IntentStarter paramIntentStarter)
  {
    super(paramIntentStarter);
  }
  
  @Nullable
  private Person getRecipient(PhoneCallAction paramPhoneCallAction)
  {
    PersonDisambiguation localPersonDisambiguation = paramPhoneCallAction.getRecipient();
    if ((localPersonDisambiguation != null) && (localPersonDisambiguation.isCompleted())) {
      return (Person)localPersonDisambiguation.get();
    }
    return null;
  }
  
  protected Intent[] getExecuteIntents(PhoneCallAction paramPhoneCallAction)
  {
    Contact localContact = ((Person)paramPhoneCallAction.getRecipient().get()).getSelectedItem();
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = PhoneActionUtils.getCallIntent(localContact.getValue());
    return arrayOfIntent;
  }
  
  protected Intent[] getOpenExternalAppIntents(PhoneCallAction paramPhoneCallAction)
  {
    Person localPerson = getRecipient(paramPhoneCallAction);
    if (localPerson != null)
    {
      Intent[] arrayOfIntent2 = new Intent[1];
      arrayOfIntent2[0] = PhoneActionUtils.getShowPersonIntent(localPerson);
      return arrayOfIntent2;
    }
    List localList = paramPhoneCallAction.getRecipient().getCandidates();
    if (localList.size() == 1) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      Intent[] arrayOfIntent1 = new Intent[1];
      arrayOfIntent1[0] = PhoneActionUtils.getEditPersonIntent((Person)localList.get(0));
      return arrayOfIntent1;
    }
  }
  
  protected Intent[] getProberIntents(PhoneCallAction paramPhoneCallAction)
  {
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.PhoneCallActionExecutor
 * JD-Core Version:    0.7.0.1
 */