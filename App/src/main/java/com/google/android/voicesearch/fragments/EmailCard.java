package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class EmailCard
        extends CommunicationActionCardImpl<EmailController>
        implements EmailController.Ui {
    private ActionEditorView mActionEditorView;
    private View mCardView;
    private TextView mContactNotFoundView;
    private ImageView mEmptyEmailPicture;
    private boolean mIsBodySet;
    private boolean mIsContactSet;
    private boolean mIsSubjectSet;
    private TextView mMessageField;
    private PersonSelectItem mPersonItem;
    private TextView mSubjectView;

    public EmailCard(Context paramContext) {
        super(paramContext);
    }

    private void checkUiReady() {
        if ((this.mIsContactSet) && (this.mIsSubjectSet) && (this.mIsBodySet)) {
            ((EmailController) getController()).uiReady();
        }
    }

    public void hideContactField() {
        this.mPersonItem.setVisibility(8);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        this.mActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968667);
        this.mCardView = this.mActionEditorView.findViewById(2131296540);
        this.mCardView.setVisibility(8);
        setContactDisambiguationView((ContactDisambiguationView) this.mActionEditorView.findViewById(2131296416));
        showContactDisambiguationView(false);
        this.mContactNotFoundView = ((TextView) this.mCardView.findViewById(2131296417));
        this.mMessageField = ((TextView) this.mCardView.findViewById(2131296543));
        this.mSubjectView = ((TextView) this.mCardView.findViewById(2131296542));
        this.mEmptyEmailPicture = ((ImageView) this.mCardView.findViewById(2131296544));
        this.mEmptyEmailPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ((EmailController) EmailCard.this.getController()).pickContact();
            }
        });
        TextView[] arrayOfTextView = new TextView[2];
        arrayOfTextView[0] = this.mSubjectView;
        arrayOfTextView[1] = this.mMessageField;
        clearTextViews(arrayOfTextView);
        this.mPersonItem = ((PersonSelectItem) this.mCardView.findViewById(2131296541));
        if (Feature.EDIT_MESSAGE_TEXT.isEnabled()) {
            this.mSubjectView.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable paramAnonymousEditable) {
                    ((EmailController) EmailCard.this.getController()).setSubject(paramAnonymousEditable.toString());
                }

                public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
                }

                public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
                }
            });
            this.mMessageField.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable paramAnonymousEditable) {
                    ((EmailController) EmailCard.this.getController()).setBody(paramAnonymousEditable.toString());
                }

                public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
                }

                public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
                }
            });
        }
        for (; ; ) {
            this.mActionEditorView.setContentClickable(false);
            this.mActionEditorView.showCountDownView(false);
            return this.mActionEditorView;
            this.mSubjectView.setKeyListener(null);
            this.mMessageField.setKeyListener(null);
        }
    }

    public void setBody(String paramString) {
        if (Feature.FOLLOW_ON.isEnabled()) {
            this.mMessageField.setVisibility(0);
        }
        for (; ; ) {
            this.mMessageField.setText(paramString);
            this.mIsBodySet = true;
            checkUiReady();
            return;
            boolean bool = TextUtils.isEmpty(paramString);
            TextView localTextView = this.mMessageField;
            int i = 0;
            if (bool) {
                i = 8;
            }
            localTextView.setVisibility(i);
        }
    }

    public void setPeople(List<Person> paramList) {
        super.setPeople(paramList, ContactSelectMode.EMAIL);
        this.mCardView.setVisibility(8);
    }

    public void setSubject(String paramString) {
        boolean bool = TextUtils.isEmpty(paramString);
        TextView localTextView = this.mSubjectView;
        if (bool) {
        }
        for (int i = 8; ; i = 0) {
            localTextView.setVisibility(i);
            this.mSubjectView.setText(paramString);
            this.mIsSubjectSet = true;
            checkUiReady();
            return;
        }
    }

    public void setToContact(Person paramPerson) {
        Contact localContact = paramPerson.getSelectedItem();
        this.mContactNotFoundView.setVisibility(8);
        this.mPersonItem.setVisibility(0);
        PersonSelectItem localPersonSelectItem = this.mPersonItem;
        if (localContact == null) {
        }
        for (List localList = null; ; localList = Arrays.asList(new Contact[]{localContact})) {
            localPersonSelectItem.setPerson(paramPerson, localList, new Runnable() {
                public void run() {
                    EmailCard.access$002(EmailCard.this, true);
                    EmailCard.this.checkUiReady();
                }
            });
            checkUiReady();
            return;
        }
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
        this.mEmptyEmailPicture.setVisibility(8);
        setPeople(paramList, ContactSelectMode.EMAIL);
        this.mContactNotFoundView.setVisibility(8);
        setConfirmIcon(2130837673);
        setConfirmText(2131363615);
        setConfirmTag(0);
        this.mActionEditorView.showCountDownView(true);
    }

    public void showContactNotFound() {
        hideContactField();
        this.mEmptyEmailPicture.setVisibility(8);
        this.mContactNotFoundView.setVisibility(0);
        checkUiReady();
    }

    public void showDisabled() {
        disableActionEditor(2131363672);
    }

    public void showEditEmail() {
        if (!Feature.EDIT_MESSAGE_TEXT.isEnabled()) {
            this.mActionEditorView.setContentClickable(true);
        }
        setConfirmTag(1);
        setConfirmIcon(2130837642);
        setConfirmText(2131363602);
        this.mEmptyEmailPicture.setVisibility(8);
    }

    public void showEmptyViewWithEditEmail() {
        this.mSubjectView.setVisibility(8);
        this.mMessageField.setVisibility(8);
        this.mEmptyEmailPicture.setVisibility(0);
        this.mActionEditorView.setContentClickable(false);
        setConfirmTag(1);
        setConfirmIcon(2130837642);
        setConfirmText(2131363602);
    }

    public void showEmptyViewWithPickContact(boolean paramBoolean) {
        this.mSubjectView.setVisibility(8);
        this.mMessageField.setVisibility(8);
        this.mEmptyEmailPicture.setVisibility(0);
        this.mActionEditorView.setContentClickable(false);
        if (paramBoolean) {
            this.mContactNotFoundView.setVisibility(0);
        }
        showFindPeople(true);
    }

    public void showSendEmail() {
        if (!Feature.EDIT_MESSAGE_TEXT.isEnabled()) {
            this.mActionEditorView.setContentClickable(true);
        }
        setConfirmTag(0);
        setConfirmIcon(2130837692);
        setConfirmText(2131363603);
        this.mEmptyEmailPicture.setVisibility(8);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.EmailCard

 * JD-Core Version:    0.7.0.1

 */