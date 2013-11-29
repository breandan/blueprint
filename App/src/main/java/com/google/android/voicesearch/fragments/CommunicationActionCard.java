package com.google.android.voicesearch.fragments;

import com.google.android.speech.contacts.Person;
import java.util.List;

public abstract interface CommunicationActionCard
  extends BaseCardUi, CountDownUi
{
  public abstract void setPeople(List<Person> paramList);
  
  public abstract void showContactDetailsNotFound(List<Person> paramList);
  
  public abstract void showContactNotFound();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.CommunicationActionCard
 * JD-Core Version:    0.7.0.1
 */