package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.voicesearch.fragments.action.ShowContactInformationAction;
import com.google.android.voicesearch.util.EmailSender;
import com.google.android.voicesearch.util.EmailSender.Email;
import com.google.android.voicesearch.util.MapUtil;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public class ShowContactInformationActionExecutor
  extends CommunicationActionExecutor<ShowContactInformationAction>
{
  private final EmailSender mEmailSender;
  
  public ShowContactInformationActionExecutor(IntentStarter paramIntentStarter, EmailSender paramEmailSender)
  {
    super(paramIntentStarter);
    this.mEmailSender = ((EmailSender)Preconditions.checkNotNull(paramEmailSender));
  }
  
  public void callContact(Contact paramContact)
  {
    IntentStarter localIntentStarter = this.mIntentStarter;
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = PhoneActionUtils.getCallIntent(paramContact.getValue());
    localIntentStarter.startActivity(arrayOfIntent);
  }
  
  protected Intent[] getExecuteIntents(ShowContactInformationAction paramShowContactInformationAction)
  {
    return null;
  }
  
  protected Intent[] getOpenExternalAppIntents(ShowContactInformationAction paramShowContactInformationAction)
  {
    PersonDisambiguation localPersonDisambiguation = paramShowContactInformationAction.getRecipient();
    Intent localIntent;
    if ((localPersonDisambiguation == null) || (!localPersonDisambiguation.isCompleted())) {
      localIntent = PhoneActionUtils.getShowPersonIntent(null);
    }
    for (;;)
    {
      return new Intent[] { localIntent };
      if (paramShowContactInformationAction.isContactDetailsFound()) {
        localIntent = PhoneActionUtils.getEditPersonIntent((Person)localPersonDisambiguation.get());
      } else {
        localIntent = PhoneActionUtils.getEditPersonIntent((Person)localPersonDisambiguation.get());
      }
    }
  }
  
  @Nullable
  protected Intent[] getProberIntents(ShowContactInformationAction paramShowContactInformationAction)
  {
    return null;
  }
  
  public void navigateToContact(Contact paramContact)
  {
    this.mIntentStarter.startActivity(MapUtil.getMapsSearchIntents(paramContact.getValue()));
  }
  
  public void sendEmailToContact(Contact paramContact)
  {
    EmailSender.Email localEmail = new EmailSender.Email();
    String[] arrayOfString = new String[1];
    arrayOfString[0] = paramContact.toRfc822Token();
    localEmail.to = arrayOfString;
    this.mEmailSender.sendEmail(localEmail, false, this.mIntentStarter, null);
  }
  
  public void sendTextToContact(Contact paramContact)
  {
    IntentStarter localIntentStarter = this.mIntentStarter;
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = PhoneActionUtils.getSendSmsIntent(paramContact.getValue(), null);
    localIntentStarter.startActivity(arrayOfIntent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.ShowContactInformationActionExecutor
 * JD-Core Version:    0.7.0.1
 */