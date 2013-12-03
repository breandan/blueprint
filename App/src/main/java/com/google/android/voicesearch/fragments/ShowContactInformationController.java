package com.google.android.voicesearch.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;

import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.velvet.actions.Disambiguation.ProgressListener;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.ShowContactInformationAction;
import com.google.android.voicesearch.fragments.executor.ShowContactInformationActionExecutor;
import com.google.android.voicesearch.logger.EventLogger;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nullable;

public class ShowContactInformationController
        extends CommunicationActionController<ShowContactInformationAction, Ui>
        implements Disambiguation.ProgressListener<Person> {
    private final ExecutorService mBackgroundExecutor;
    private final String mClipboardLabel;
    private final ClipboardManager mClipboardManager;
    private final ContactLookup mContactLookup;
    private final DeviceCapabilityManager mDeviceCapabilityManager;

    public ShowContactInformationController(CardController paramCardController, ContactLookup paramContactLookup, ExecutorService paramExecutorService, String paramString, ClipboardManager paramClipboardManager, DeviceCapabilityManager paramDeviceCapabilityManager) {
        super(paramCardController);
        this.mContactLookup = paramContactLookup;
        this.mBackgroundExecutor = paramExecutorService;
        this.mClipboardLabel = paramString;
        this.mClipboardManager = paramClipboardManager;
        this.mDeviceCapabilityManager = paramDeviceCapabilityManager;
    }

    private void initClassicUi() {
        ShowContactInformationAction localShowContactInformationAction = (ShowContactInformationAction) getVoiceAction();
        PersonDisambiguation localPersonDisambiguation = localShowContactInformationAction.getRecipient();
        int i = localShowContactInformationAction.getContactMethod();
        Ui localUi = (Ui) getUi();
        if (localPersonDisambiguation.hasNoResults()) {
            localUi.showContactNotFound();
        }
        for (; ; ) {
            uiReady();
            return;
            if (localShowContactInformationAction.isContactDetailsFound()) {
                localUi.setPerson((Person) localPersonDisambiguation.get());
                localUi.showPhoneNumbers(localShowContactInformationAction.getPhoneNumbers(), this.mDeviceCapabilityManager.isTelephoneCapable());
                localUi.showEmailAddresses(localShowContactInformationAction.getEmailAddresses());
                localUi.showPostalAddresses(localShowContactInformationAction.getPostalAddresses());
            } else {
                localUi.setPerson((Person) localPersonDisambiguation.get());
                if (i == 0) {
                    localUi.showContactDetailsNotFound(null);
                } else if (i == 1) {
                    localUi.showPhoneNumberNotFound();
                } else if (i == 2) {
                    localUi.showEmailAddressNotFound();
                } else if (i == 3) {
                    localUi.showPostalAddressNotFound();
                }
            }
        }
    }

    private void lookUpContactData() {
        new AsyncTask() {
            protected Void doInBackground(Void... paramAnonymousVarArgs) {
                boolean bool = true;
                long l = ((Person) this.val$voiceAction.getRecipient().get()).getId();
                int i = this.val$voiceAction.getContactMethod();
                String str = this.val$voiceAction.getContactDetailType();
                switch (i) {
                    default:
                        throw new IllegalStateException();
                    case 1:
                        this.val$voiceAction.setPhoneNumbers(ShowContactInformationController.this.mContactLookup.fetchPhoneNumbers(l, str));
                        if (this.val$voiceAction.getPhoneNumbers().isEmpty()) {
                            break;
                        }
                }
                do {
                    do {
                        do {
                            for (; ; ) {
                                this.val$voiceAction.setContactDetailsFound(bool);
                                return null;
                                bool = false;
                            }
                            this.val$voiceAction.setEmailAddresses(ShowContactInformationController.this.mContactLookup.fetchEmailAddresses(l, str));
                        } while (!this.val$voiceAction.getEmailAddresses().isEmpty());
                        for (; ; ) {
                            bool = false;
                        }
                        this.val$voiceAction.setPostalAddresses(ShowContactInformationController.this.mContactLookup.fetchPostalAddresses(l, str));
                    } while (!this.val$voiceAction.getPostalAddresses().isEmpty());
                    for (; ; ) {
                        bool = false;
                    }
                    this.val$voiceAction.setPhoneNumbers(ShowContactInformationController.this.mContactLookup.fetchPhoneNumbers(l, str));
                    this.val$voiceAction.setEmailAddresses(ShowContactInformationController.this.mContactLookup.fetchEmailAddresses(l, str));
                    this.val$voiceAction.setPostalAddresses(ShowContactInformationController.this.mContactLookup.fetchPostalAddresses(l, str));
                }
                while ((this.val$voiceAction.getPhoneNumbers().isEmpty()) && (this.val$voiceAction.getEmailAddresses().isEmpty()) && (!this.val$voiceAction.getPostalAddresses().isEmpty()));
                for (; ; ) {
                    bool = false;
                }
            }

            protected void onPostExecute(Void paramAnonymousVoid) {
                ShowContactInformationController.this.showCard();
            }
        }.executeOnExecutor(this.mBackgroundExecutor, new Void[0]);
    }

    public void callContact(Contact paramContact) {
        EventLogger.recordClientEvent(116, Integer.valueOf(getActionTypeLog()));
        cancelCountDown();
        ((ShowContactInformationActionExecutor) getActionExecutor()).callContact(paramContact);
    }

    public void copyToClipboard(Contact paramContact) {
        this.mClipboardManager.setPrimaryClip(ClipData.newPlainText(this.mClipboardLabel, paramContact.getValue()));
    }

    public void initUi() {
        PersonDisambiguation localPersonDisambiguation = ((ShowContactInformationAction) getVoiceAction()).getRecipient();
        if ((localPersonDisambiguation == null) || (localPersonDisambiguation.hasNoResults()) || (Disambiguation.isCompleted(localPersonDisambiguation))) {
            initClassicUi();
            ((Ui) getUi()).showActionContent(true);
            return;
        }
        super.initUi();
        ((Ui) getUi()).showActionContent(false);
        uiReady();
    }

    public void navigateToContact(Contact paramContact) {
        EventLogger.recordClientEvent(119, Integer.valueOf(getActionTypeLog()));
        cancelCountDown();
        ((ShowContactInformationActionExecutor) getActionExecutor()).navigateToContact(paramContact);
    }

    protected void onDisambiguationCompleted(Disambiguation<Person> paramDisambiguation) {
        lookUpContactData();
    }

    public void sendEmailToContact(Contact paramContact) {
        EventLogger.recordClientEvent(118, Integer.valueOf(getActionTypeLog()));
        cancelCountDown();
        ((ShowContactInformationActionExecutor) getActionExecutor()).sendEmailToContact(paramContact);
    }

    public void sendTextToContact(Contact paramContact) {
        EventLogger.recordClientEvent(117, Integer.valueOf(getActionTypeLog()));
        cancelCountDown();
        ((ShowContactInformationActionExecutor) getActionExecutor()).sendTextToContact(paramContact);
    }

    public static abstract interface Ui
            extends CommunicationActionCard {
        public abstract void setPerson(Person paramPerson);

        public abstract void showActionContent(boolean paramBoolean);

        public abstract void showEmailAddressNotFound();

        public abstract void showEmailAddresses(@Nullable List<Contact> paramList);

        public abstract void showPhoneNumberNotFound();

        public abstract void showPhoneNumbers(@Nullable List<Contact> paramList, boolean paramBoolean);

        public abstract void showPostalAddressNotFound();

        public abstract void showPostalAddresses(@Nullable List<Contact> paramList);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.ShowContactInformationController

 * JD-Core Version:    0.7.0.1

 */