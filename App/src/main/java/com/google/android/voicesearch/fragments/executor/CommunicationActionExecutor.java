package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.provider.ContactsContract.Contacts;
import com.google.android.shared.util.IntentStarter;
import com.google.android.velvet.actions.VoiceActionResultCallback;
import com.google.android.voicesearch.fragments.action.CommunicationAction;

public abstract class CommunicationActionExecutor<T extends CommunicationAction>
  extends IntentActionExecutor<T>
{
  protected CommunicationActionExecutor(IntentStarter paramIntentStarter)
  {
    super(paramIntentStarter);
  }
  
  public void pickContact()
  {
    Intent localIntent = new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI);
    this.mIntentStarter.startActivityForResult(localIntent, VoiceActionResultCallback.createPickContactCallback());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.CommunicationActionExecutor
 * JD-Core Version:    0.7.0.1
 */