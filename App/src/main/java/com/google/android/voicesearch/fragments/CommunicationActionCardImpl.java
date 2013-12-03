package com.google.android.voicesearch.fragments;

import android.content.Context;

import com.google.android.speech.contacts.Person;
import com.google.android.voicesearch.contacts.ActionCardContactDisambiguationCallback;
import com.google.android.voicesearch.contacts.ContactDisambiguationView;
import com.google.android.voicesearch.contacts.ContactSelectMode;

import java.util.List;

public abstract class CommunicationActionCardImpl<T extends CommunicationActionController<?, ?>>
        extends AbstractCardView<T>
        implements CommunicationActionCard {
    private ContactDisambiguationView mContactDisambiguationView;

    public CommunicationActionCardImpl(Context paramContext) {
        super(paramContext);
    }

    protected void handleConfirmation(int paramInt) {
        if (paramInt == 100) {
            ((CommunicationActionController) getController()).pickContact();
            return;
        }
        super.handleConfirmation(paramInt);
    }

    protected void setContactDisambiguationView(ContactDisambiguationView paramContactDisambiguationView) {
        this.mContactDisambiguationView = paramContactDisambiguationView;
        this.mContactDisambiguationView.setCallback(new ActionCardContactDisambiguationCallback(this));
    }

    protected void setPeople(List<Person> paramList, ContactSelectMode paramContactSelectMode) {
        int i = 1;
        this.mContactDisambiguationView.setPeople(paramList, paramContactSelectMode);
        this.mContactDisambiguationView.setVisibility(0);
        if (paramList.size() != i) {
        }
        for (; ; ) {
            showFindPeople(i);
            return;
            int j = 0;
        }
    }

    protected void showContactDisambiguationView(boolean paramBoolean) {
        ContactDisambiguationView localContactDisambiguationView = this.mContactDisambiguationView;
        if (paramBoolean) {
        }
        for (int i = 0; ; i = 8) {
            localContactDisambiguationView.setVisibility(i);
            return;
        }
    }

    protected void showFindPeople(boolean paramBoolean) {
        if (paramBoolean) {
            setConfirmIcon(2130837673);
            setConfirmText(2131363597);
            setConfirmTag(100);
        }
        showConfirmBar(paramBoolean);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.CommunicationActionCardImpl

 * JD-Core Version:    0.7.0.1

 */