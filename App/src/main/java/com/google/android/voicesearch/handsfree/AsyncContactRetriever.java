package com.google.android.voicesearch.handsfree;

import android.os.AsyncTask;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.ContactLookup.Mode;
import com.google.android.speech.contacts.Person;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.collect.Lists;
import com.google.majel.proto.ActionV2Protos.ActionContact;
import com.google.majel.proto.ActionV2Protos.PhoneAction;
import java.util.List;
import java.util.concurrent.Executor;

class AsyncContactRetriever
{
  private final Executor mBackgroundExecutor;
  private final ContactLookup mContactLookup;
  
  public AsyncContactRetriever(ContactLookup paramContactLookup, Executor paramExecutor)
  {
    this.mContactLookup = paramContactLookup;
    this.mBackgroundExecutor = paramExecutor;
  }
  
  public void start(ActionV2Protos.PhoneAction paramPhoneAction, Listener paramListener)
  {
    
    if (PhoneActionUtils.isPhoneNumberAction(paramPhoneAction))
    {
      paramListener.onMatch(Lists.newArrayList(new Person[] { Person.fromContact(Contact.newPhoneNumberOnlyContact(PhoneActionUtils.getSpokenNumber(paramPhoneAction))) }));
      return;
    }
    new RetrievePersonTask(paramPhoneAction.getContactList(), paramListener).executeOnExecutor(this.mBackgroundExecutor, new Void[0]);
  }
  
  static abstract interface Listener
  {
    public abstract void onMatch(List<Person> paramList);
    
    public abstract void onNoMatch();
  }
  
  private class RetrievePersonTask
    extends AsyncTask<Void, Void, List<Person>>
  {
    private final List<ActionV2Protos.ActionContact> mActionContacts;
    private final AsyncContactRetriever.Listener mListener;
    
    public RetrievePersonTask(AsyncContactRetriever.Listener paramListener)
    {
      this.mActionContacts = paramListener;
      Object localObject;
      this.mListener = localObject;
    }
    
    protected List<Person> doInBackground(Void... paramVarArgs)
    {
      return AsyncContactRetriever.this.mContactLookup.findAllByDisplayName(ContactLookup.Mode.PHONE_NUMBER, this.mActionContacts, null);
    }
    
    protected void onPostExecute(List<Person> paramList)
    {
      if (paramList.isEmpty())
      {
        this.mListener.onNoMatch();
        return;
      }
      this.mListener.onMatch(paramList);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.AsyncContactRetriever
 * JD-Core Version:    0.7.0.1
 */