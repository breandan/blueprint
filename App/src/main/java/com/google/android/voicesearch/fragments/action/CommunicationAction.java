package com.google.android.voicesearch.fragments.action;

import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation.ProgressListener;
import com.google.android.voicesearch.contacts.ContactSelectMode;

public abstract interface CommunicationAction
        extends VoiceAction {
    public abstract CommunicationAction forNewRecipient(PersonDisambiguation paramPersonDisambiguation);

    public abstract int getActionTypeLog();

    public abstract PersonDisambiguation getRecipient();

    public abstract ContactSelectMode getSelectMode();

    public abstract void setDisambiguationProgressListener(Disambiguation.ProgressListener<Person> paramProgressListener);

    public abstract void setRecipient(PersonDisambiguation paramPersonDisambiguation);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.CommunicationAction

 * JD-Core Version:    0.7.0.1

 */