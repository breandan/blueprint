package com.google.android.voicesearch.fragments.action;

import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation.ProgressListener;
import javax.annotation.Nullable;

public abstract class CommunicationActionImpl
  implements CommunicationAction
{
  @Nullable
  private Disambiguation.ProgressListener<Person> mListener;
  @Nullable
  protected PersonDisambiguation mRecipient;
  
  public PersonDisambiguation getRecipient()
  {
    return this.mRecipient;
  }
  
  public void setDisambiguationProgressListener(Disambiguation.ProgressListener<Person> paramProgressListener)
  {
    this.mListener = paramProgressListener;
    if (this.mRecipient != null) {
      this.mRecipient.setListener(this.mListener);
    }
  }
  
  public void setRecipient(PersonDisambiguation paramPersonDisambiguation)
  {
    this.mRecipient = paramPersonDisambiguation;
    if (this.mListener != null) {
      this.mRecipient.setListener(this.mListener);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.CommunicationActionImpl
 * JD-Core Version:    0.7.0.1
 */