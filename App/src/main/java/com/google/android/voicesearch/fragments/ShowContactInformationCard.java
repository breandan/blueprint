package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.voicesearch.contacts.ContactDisambiguationView;
import com.google.android.voicesearch.contacts.ContactListView;
import com.google.android.voicesearch.contacts.ContactListViewListenerAdapter;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.android.voicesearch.fragments.action.ShowContactInformationAction;
import com.google.android.voicesearch.ui.ActionEditorSetContactPictureTask;
import com.google.android.voicesearch.ui.ActionEditorView;
import com.google.common.collect.Lists;

import java.util.List;

public class ShowContactInformationCard
        extends CommunicationActionCardImpl<ShowContactInformationController>
        implements ShowContactInformationController.Ui {
    private ActionEditorView mActionEditorView;
    private View mCardView;
    private View mContactBadge;
    private TextView mContactNameView;
    private TextView mContactNotFoundView;
    private View mContactPictureDivider;
    private ImageView mContactPictureView;
    private final MenuItem.OnMenuItemClickListener mCopyToClipboardMenuItemListener = new MenuItem.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
            if (ShowContactInformationCard.this.mSelectedContact != null) {
                ((ShowContactInformationController) ShowContactInformationCard.this.getController()).copyToClipboard(ShowContactInformationCard.this.mSelectedContact);
            }
            return true;
        }
    };
    private final View.OnCreateContextMenuListener mOnCreateContextMenuListener = new View.OnCreateContextMenuListener() {
        public void onCreateContextMenu(ContextMenu paramAnonymousContextMenu, View paramAnonymousView, ContextMenu.ContextMenuInfo paramAnonymousContextMenuInfo) {
            if (ShowContactInformationCard.this.mSelectedContact != null) {
                paramAnonymousContextMenu.setHeaderTitle(ShowContactInformationCard.this.mSelectedContact.getFormattedValue());
                paramAnonymousContextMenu.add(0, 1, 0, ShowContactInformationCard.this.getContext().getString(2131363620)).setOnMenuItemClickListener(ShowContactInformationCard.this.mCopyToClipboardMenuItemListener);
            }
        }
    };
    private Contact mSelectedContact;
    private List<ContactMethod> mVisibleContactMethods = Lists.newArrayList();

    public ShowContactInformationCard(Context paramContext) {
        super(paramContext);
    }

    private void hideAllContactMethods() {
        for (ContactMethod localContactMethod :) {
            setHeadlineVisibility(localContactMethod, 8);
            this.mActionEditorView.findViewById(localContactMethod.contactListId).setVisibility(8);
        }
        this.mVisibleContactMethods.clear();
    }

    private void setContactListViewCallbacks(ContactMethod paramContactMethod, ContactListView.Listener paramListener) {
        ContactListView localContactListView = (ContactListView) this.mActionEditorView.findViewById(paramContactMethod.contactListId);
        localContactListView.setContactSelectedListener(paramListener);
        localContactListView.setOnCreateContextMenuListener(this.mOnCreateContextMenuListener);
    }

    private void setHeadlineVisibility(ContactMethod paramContactMethod, int paramInt) {
        this.mActionEditorView.findViewById(paramContactMethod.headlineId).setVisibility(paramInt);
        this.mActionEditorView.findViewById(paramContactMethod.dividerId).setVisibility(paramInt);
    }

    private void showContactInformation(List<Contact> paramList, ContactMethod paramContactMethod) {
        if ((paramList == null) || (paramList.isEmpty())) {
            return;
        }
        switch (this.mVisibleContactMethods.size()) {
        }
        for (; ; ) {
            setHeadlineVisibility(paramContactMethod, 0);
            this.mVisibleContactMethods.add(paramContactMethod);
            ContactListView localContactListView = (ContactListView) this.mActionEditorView.findViewById(paramContactMethod.contactListId);
            localContactListView.setVisibility(0);
            localContactListView.setContacts(paramList, 2130968831, paramContactMethod.actionIconId, paramContactMethod.actionDescId, paramContactMethod.isActionButtonClickable());
            return;
            setHeadlineVisibility((ContactMethod) this.mVisibleContactMethods.get(0), 0);
            this.mContactPictureDivider.setVisibility(8);
        }
    }

    private void showErrorMessage(int paramInt1, int paramInt2, boolean paramBoolean) {
        hideAllContactMethods();
        View localView = this.mContactBadge;
        if (paramBoolean) {
        }
        for (int i = 0; ; i = 8) {
            localView.setVisibility(i);
            this.mContactNotFoundView.setText(paramInt1);
            setConfirmText(paramInt2);
            this.mContactNotFoundView.setVisibility(0);
            ((ShowContactInformationController) getController()).uiReady();
            return;
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setContactListViewCallbacks(ContactMethod.PHONE, new ContactListViewListenerAdapter() {
            public void onActionButtonClicked(Contact paramAnonymousContact) {
                ((ShowContactInformationController) ShowContactInformationCard.this.getController()).sendTextToContact(paramAnonymousContact);
            }

            public void onContactSelected(Contact paramAnonymousContact) {
                ((ShowContactInformationController) ShowContactInformationCard.this.getController()).callContact(paramAnonymousContact);
            }

            public void onContactTouched(Contact paramAnonymousContact) {
                ShowContactInformationCard.access$002(ShowContactInformationCard.this, paramAnonymousContact);
            }
        });
        setContactListViewCallbacks(ContactMethod.EMAIL, new ContactListViewListenerAdapter() {
            public void onContactSelected(Contact paramAnonymousContact) {
                ((ShowContactInformationController) ShowContactInformationCard.this.getController()).sendEmailToContact(paramAnonymousContact);
            }

            public void onContactTouched(Contact paramAnonymousContact) {
                ShowContactInformationCard.access$002(ShowContactInformationCard.this, paramAnonymousContact);
            }
        });
        setContactListViewCallbacks(ContactMethod.ADDRESS, new ContactListViewListenerAdapter() {
            public void onContactSelected(Contact paramAnonymousContact) {
                ((ShowContactInformationController) ShowContactInformationCard.this.getController()).navigateToContact(paramAnonymousContact);
            }

            public void onContactTouched(Contact paramAnonymousContact) {
                ShowContactInformationCard.access$002(ShowContactInformationCard.this, paramAnonymousContact);
            }
        });
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        this.mActionEditorView = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968830);
        this.mCardView = this.mActionEditorView.findViewById(2131296540);
        this.mCardView.setVisibility(8);
        setContactDisambiguationView((ContactDisambiguationView) this.mActionEditorView.findViewById(2131296416));
        showContactDisambiguationView(false);
        this.mContactNameView = ((TextView) this.mCardView.findViewById(2131296464));
        this.mContactNotFoundView = ((TextView) this.mCardView.findViewById(2131296417));
        this.mContactPictureView = ((ImageView) this.mCardView.findViewById(2131296420));
        this.mContactPictureDivider = this.mCardView.findViewById(2131296422);
        this.mContactBadge = this.mCardView.findViewById(2131296473);
        this.mActionEditorView.setContentClickable(false);
        this.mActionEditorView.showCountDownView(true);
        return this.mActionEditorView;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
        paramAccessibilityNodeInfo.setClassName(ShowContactInformationCard.class.getCanonicalName());
    }

    public void setPeople(List<Person> paramList) {
        ContactSelectMode localContactSelectMode = ContactSelectMode.SMS;
        switch (((ShowContactInformationAction) ((ShowContactInformationController) getController()).getVoiceAction()).getContactMethod()) {
        }
        for (; ; ) {
            super.setPeople(paramList, localContactSelectMode);
            this.mCardView.setVisibility(8);
            return;
            localContactSelectMode = ContactSelectMode.EMAIL;
            continue;
            localContactSelectMode = ContactSelectMode.CALL_NUMBER;
        }
    }

    public void setPerson(Person paramPerson) {
        this.mContactNameView.setVisibility(0);
        this.mContactNameView.setText(paramPerson.getName());
        if (paramPerson.getId() > 0L) {
            ActionEditorSetContactPictureTask localActionEditorSetContactPictureTask = new ActionEditorSetContactPictureTask(this.mContactPictureView);
            Long[] arrayOfLong = new Long[1];
            arrayOfLong[0] = Long.valueOf(paramPerson.getId());
            localActionEditorSetContactPictureTask.execute(arrayOfLong);
        }
        hideAllContactMethods();
        this.mContactPictureDivider.setVisibility(0);
    }

    public void showActionContent(boolean paramBoolean) {
        boolean bool;
        View localView;
        int i;
        if (!paramBoolean) {
            bool = true;
            showContactDisambiguationView(bool);
            if (paramBoolean) {
                setConfirmIcon(2130837673);
                setConfirmText(2131363615);
                setConfirmTag(0);
            }
            if ((paramBoolean) && (this.mCardView.getVisibility() != 0)) {
                setVisibility(8);
                setVisibility(0);
            }
            localView = this.mCardView;
            i = 0;
            if (!paramBoolean) {
                break label83;
            }
        }
        for (; ; ) {
            localView.setVisibility(i);
            return;
            bool = false;
            break;
            label83:
            i = 8;
        }
    }

    public void showContactDetailsNotFound(List<Person> paramList) {
        showErrorMessage(2131363616, 2131363615, true);
    }

    public void showContactNotFound() {
        showErrorMessage(2131363596, 2131363597, false);
    }

    public void showEmailAddressNotFound() {
        showErrorMessage(2131363618, 2131363615, true);
    }

    public void showEmailAddresses(List<Contact> paramList) {
        showContactInformation(paramList, ContactMethod.EMAIL);
    }

    public void showPhoneNumberNotFound() {
        showErrorMessage(2131363617, 2131363615, true);
    }

    public void showPhoneNumbers(List<Contact> paramList, boolean paramBoolean) {
        if (paramBoolean) {
        }
        for (ContactMethod localContactMethod = ContactMethod.PHONE_AND_SMS; ; localContactMethod = ContactMethod.PHONE) {
            showContactInformation(paramList, localContactMethod);
            return;
        }
    }

    public void showPostalAddressNotFound() {
        showErrorMessage(2131363619, 2131363615, true);
    }

    public void showPostalAddresses(List<Contact> paramList) {
        showContactInformation(paramList, ContactMethod.ADDRESS);
    }

    private static enum ContactMethod {
        final int actionDescId;
        final int actionIconId;
        final int contactListId;
        final int dividerId;
        final int headlineId;

        static {
            EMAIL = new ContactMethod("EMAIL", 2, 2131297007, 2131297008, 2131297009, 0, 0);
            ADDRESS = new ContactMethod("ADDRESS", 3, 2131297010, 2131297011, 2131297012, 0, 0);
            ContactMethod[] arrayOfContactMethod = new ContactMethod[4];
            arrayOfContactMethod[0] = PHONE;
            arrayOfContactMethod[1] = PHONE_AND_SMS;
            arrayOfContactMethod[2] = EMAIL;
            arrayOfContactMethod[3] = ADDRESS;
            $VALUES = arrayOfContactMethod;
        }

        private ContactMethod(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
            this.headlineId = paramInt1;
            this.dividerId = paramInt2;
            this.contactListId = paramInt3;
            this.actionIconId = paramInt4;
            this.actionDescId = paramInt5;
        }

        boolean isActionButtonClickable() {
            return this.actionIconId != 0;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.ShowContactInformationCard

 * JD-Core Version:    0.7.0.1

 */