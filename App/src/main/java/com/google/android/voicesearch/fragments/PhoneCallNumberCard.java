package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.voicesearch.ui.ActionEditorView;

import java.util.List;

public class PhoneCallNumberCard
        extends AbstractCardView<PhoneCallController>
        implements PhoneCallController.Ui {
    private TextView mPhoneNumberView;

    public PhoneCallNumberCard(Context paramContext) {
        super(paramContext);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        ActionEditorView localActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968617);
        this.mPhoneNumberView = ((TextView) localActionEditorView.findViewById(2131296421));
        setConfirmIcon(2130837636);
        setConfirmText(2131363607);
        TextView[] arrayOfTextView = new TextView[1];
        arrayOfTextView[0] = this.mPhoneNumberView;
        clearTextViews(arrayOfTextView);
        return localActionEditorView;
    }

    public void setPeople(List<Person> paramList) {
        throw new UnsupportedOperationException();
    }

    public void setToContact(Contact paramContact) {
        this.mPhoneNumberView.setText(paramContact.getFormattedValue());
        ((PhoneCallController) getController()).uiReady();
    }

    public void showContactDetailsNotFound(List<Person> paramList) {
        throw new UnsupportedOperationException();
    }

    public void showContactNotFound() {
        throw new UnsupportedOperationException();
    }

    public void showEmptyRecipientCard() {
        throw new UnsupportedOperationException();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.PhoneCallNumberCard

 * JD-Core Version:    0.7.0.1

 */