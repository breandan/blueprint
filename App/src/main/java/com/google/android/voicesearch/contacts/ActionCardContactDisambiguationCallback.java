package com.google.android.voicesearch.contacts;

import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.voicesearch.fragments.AbstractCardView;
import com.google.android.voicesearch.fragments.CommunicationActionController;
import com.google.common.base.Preconditions;

public class ActionCardContactDisambiguationCallback
        implements ContactDisambiguationView.Callback {
    private final AbstractCardView<? extends CommunicationActionController<?, ?>> mCardView;

    public ActionCardContactDisambiguationCallback(AbstractCardView<? extends CommunicationActionController<?, ?>> paramAbstractCardView) {
        this.mCardView = ((AbstractCardView) Preconditions.checkNotNull(paramAbstractCardView));
    }

    public void onContactDetailSelected(Person paramPerson, Contact paramContact) {
        ((CommunicationActionController) this.mCardView.getController()).onContactDetailSelected(paramPerson, paramContact);
    }

    public void onPersonSelected(Person paramPerson) {
        ((CommunicationActionController) this.mCardView.getController()).onPersonSelected(paramPerson);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.contacts.ActionCardContactDisambiguationCallback

 * JD-Core Version:    0.7.0.1

 */