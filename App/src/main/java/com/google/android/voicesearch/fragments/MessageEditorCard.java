package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.search.core.Feature;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.voicesearch.contacts.ContactDisambiguationView;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.android.voicesearch.contacts.PersonSelectItem;
import com.google.android.voicesearch.ui.ActionEditorView;

import java.util.Arrays;
import java.util.List;

public class MessageEditorCard
        extends CommunicationActionCardImpl<MessageEditorController>
        implements MessageEditorController.Ui {
    private ActionEditorView mActionEditorView;
    private View mCardView;
    private TextView mContactNotFoundView;
    private ImageView mEmptySmsPicture;
    private boolean mIsContactSet;
    private boolean mIsMessageBodySet;
    private TextView mMessageField;
    private TextView mNumberOnlyView;
    private PersonSelectItem mPersonItem;

    public MessageEditorCard(Context paramContext) {
        super(paramContext);
    }

    private void checkUiReady() {
        if ((this.mIsContactSet) && (this.mIsMessageBodySet)) {
            ((MessageEditorController) getController()).uiReady();
        }
    }

    private void setNormalPerson(Person paramPerson) {
        Contact localContact = paramPerson.getSelectedItem();
        this.mContactNotFoundView.setVisibility(8);
        this.mPersonItem.setVisibility(0);
        PersonSelectItem localPersonSelectItem = this.mPersonItem;
        if (localContact == null) {
        }
        for (List localList = null; ; localList = Arrays.asList(new Contact[]{localContact})) {
            localPersonSelectItem.setPerson(paramPerson, localList, new Runnable() {
                public void run() {
                    MessageEditorCard.access$002(MessageEditorCard.this, true);
                    MessageEditorCard.this.checkUiReady();
                }
            });
            checkUiReady();
            return;
        }
    }

    private void setNumberOnlyContact(Contact paramContact) {
        this.mNumberOnlyView.setText(paramContact.getValue());
        this.mIsContactSet = true;
        checkUiReady();
    }

    private void showFieldAndHideOthers(View paramView) {
        TextView localTextView1 = this.mNumberOnlyView;
        int i;
        int j;
        label37:
        int k;
        label61:
        ImageView localImageView1;
        int m;
        if (paramView == this.mNumberOnlyView) {
            i = 0;
            localTextView1.setVisibility(i);
            PersonSelectItem localPersonSelectItem = this.mPersonItem;
            if (paramView != this.mPersonItem) {
                break label103;
            }
            j = 0;
            localPersonSelectItem.setVisibility(j);
            TextView localTextView2 = this.mContactNotFoundView;
            if (paramView != this.mContactNotFoundView) {
                break label110;
            }
            k = 0;
            localTextView2.setVisibility(k);
            localImageView1 = this.mEmptySmsPicture;
            ImageView localImageView2 = this.mEmptySmsPicture;
            m = 0;
            if (paramView != localImageView2) {
                break label117;
            }
        }
        for (; ; ) {
            localImageView1.setVisibility(m);
            return;
            i = 8;
            break;
            label103:
            j = 8;
            break label37;
            label110:
            k = 8;
            break label61;
            label117:
            m = 8;
        }
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        this.mActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968748);
        this.mCardView = this.mActionEditorView.findViewById(2131296540);
        this.mCardView.setVisibility(8);
        setContactDisambiguationView((ContactDisambiguationView) this.mActionEditorView.findViewById(2131296416));
        showContactDisambiguationView(false);
        this.mContactNotFoundView = ((TextView) this.mCardView.findViewById(2131296417));
        this.mNumberOnlyView = ((TextView) this.mCardView.findViewById(2131296794));
        this.mMessageField = ((TextView) this.mCardView.findViewById(2131296543));
        this.mPersonItem = ((PersonSelectItem) this.mCardView.findViewById(2131296793));
        TextView[] arrayOfTextView = new TextView[2];
        arrayOfTextView[0] = this.mNumberOnlyView;
        arrayOfTextView[1] = this.mMessageField;
        clearTextViews(arrayOfTextView);
        this.mEmptySmsPicture = ((ImageView) this.mCardView.findViewById(2131296795));
        this.mEmptySmsPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ((MessageEditorController) MessageEditorCard.this.getController()).pickContact();
            }
        });
        this.mActionEditorView.setConfirmIcon(2130837894);
        this.mActionEditorView.setContentClickable(false);
        this.mActionEditorView.showCountDownView(false);
        return this.mActionEditorView;
    }

    public void setMessageBody(CharSequence paramCharSequence) {
        if (Feature.FOLLOW_ON.isEnabled()) {
            this.mMessageField.setVisibility(0);
        }
        for (; ; ) {
            this.mMessageField.setText(paramCharSequence);
            this.mIsMessageBodySet = true;
            checkUiReady();
            return;
            boolean bool = TextUtils.isEmpty(paramCharSequence);
            TextView localTextView = this.mMessageField;
            int i = 0;
            if (bool) {
                i = 8;
            }
            localTextView.setVisibility(i);
        }
    }

    public void setPeople(List<Person> paramList) {
        super.setPeople(paramList, ContactSelectMode.SMS);
        this.mCardView.setVisibility(8);
    }

    public void setToPerson(Person paramPerson) {
        Contact localContact = paramPerson.getSelectedItem();
        this.mEmptySmsPicture.setVisibility(8);
        if (localContact.hasName()) {
            setNormalPerson(paramPerson);
            return;
        }
        setNumberOnlyContact(localContact);
    }

    public void showActionContent(boolean paramBoolean) {
        boolean bool;
        View localView;
        if (!paramBoolean) {
            bool = true;
            showContactDisambiguationView(bool);
            if ((paramBoolean) && (this.mCardView.getVisibility() != 0)) {
                setVisibility(8);
                setVisibility(0);
            }
            localView = this.mCardView;
            if (!paramBoolean) {
                break label72;
            }
        }
        label72:
        for (int i = 0; ; i = 8) {
            localView.setVisibility(i);
            if (!paramBoolean) {
                break label79;
            }
            this.mActionEditorView.showCountDownView(true);
            return;
            bool = false;
            break;
        }
        label79:
        this.mActionEditorView.setContentClickable(false);
    }

    public void showContactDetailsNotFound(List<Person> paramList) {
        this.mMessageField.setVisibility(8);
        setPeople(paramList, ContactSelectMode.SMS);
        this.mContactNotFoundView.setVisibility(8);
        setConfirmIcon(2130837673);
        setConfirmText(2131363615);
        setConfirmTag(0);
        this.mActionEditorView.showCountDownView(true);
    }

    public void showContactField() {
        showFieldAndHideOthers(this.mPersonItem);
    }

    public void showContactNotFound() {
        showFieldAndHideOthers(this.mContactNotFoundView);
        this.mContactNotFoundView.setVisibility(0);
    }

    public void showEmptyViewWithEditMessage() {
        this.mActionEditorView.setContentClickable(false);
        showFieldAndHideOthers(this.mEmptySmsPicture);
        this.mMessageField.setVisibility(8);
        showNewMessage();
    }

    public void showEmptyViewWithPickContact(boolean paramBoolean) {
        this.mActionEditorView.setContentClickable(false);
        showFieldAndHideOthers(this.mEmptySmsPicture);
        this.mMessageField.setVisibility(8);
        if (paramBoolean) {
            this.mContactNotFoundView.setVisibility(0);
        }
        showFindPeople(true);
    }

    public void showNewMessage() {
        this.mActionEditorView.setContentClickable(true);
        setConfirmTag(1);
        setConfirmIcon(2130837642);
        setConfirmText(2131363600);
    }

    public void showNumberOnlyField() {
        showFieldAndHideOthers(this.mNumberOnlyView);
    }

    public void showSendMessage() {
        this.mActionEditorView.setContentClickable(true);
        setConfirmTag(0);
        setConfirmIcon(2130837692);
        setConfirmText(2131363601);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.MessageEditorCard

 * JD-Core Version:    0.7.0.1

 */