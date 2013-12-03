package com.google.android.voicesearch.handsfree;

import android.content.res.Resources;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.fragments.HandsFreeRecognizerController;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

import javax.annotation.Nullable;

class PhoneCallDisambigContactController {
    private List<Contact> mContacts = Lists.newArrayList();
    private final InterpretationProcessor mInterpretationProcessor;
    private boolean mListening;
    @Nullable
    private MainController mMainController;
    @Nullable
    private List<Person> mPersons;
    private final HandsFreeRecognizerController mRecognizerController;
    private final Resources mResources;
    private boolean mSameName;
    private final AudioTrackSoundManager mSoundManager;
    @Nullable
    private Ui mUi;
    private final ViewDisplayer mViewDisplayer;

    public PhoneCallDisambigContactController(HandsFreeRecognizerController paramHandsFreeRecognizerController, Resources paramResources, ViewDisplayer paramViewDisplayer, AudioTrackSoundManager paramAudioTrackSoundManager) {
        this.mResources = paramResources;
        this.mRecognizerController = paramHandsFreeRecognizerController;
        this.mInterpretationProcessor = new InterpretationProcessor(new InterpretationProcessorListener(null));
        this.mViewDisplayer = paramViewDisplayer;
        this.mSoundManager = paramAudioTrackSoundManager;
    }

    private String buildDifferentNamesChoiceText(List<Contact> paramList) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramList.size(); i++) {
            Contact localContact = (Contact) paramList.get(i);
            Resources localResources = this.mResources;
            Object[] arrayOfObject = new Object[3];
            arrayOfObject[0] = Integer.valueOf(i + 1);
            arrayOfObject[1] = localContact.getName();
            arrayOfObject[2] = localContact.getLabel(this.mResources);
            localStringBuilder.append(localResources.getString(2131363228, arrayOfObject));
            localStringBuilder.append(" ");
        }
        localStringBuilder.append(this.mResources.getString(2131363231));
        return localStringBuilder.toString();
    }

    private String buildOneNameChoiceText(List<Contact> paramList) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(((Contact) paramList.get(0)).getName());
        localStringBuilder.append(". ");
        for (int i = 0; i < paramList.size(); i++) {
            Contact localContact = (Contact) paramList.get(i);
            Resources localResources = this.mResources;
            Object[] arrayOfObject = new Object[2];
            arrayOfObject[0] = Integer.valueOf(i + 1);
            arrayOfObject[1] = localContact.getLabel(this.mResources);
            localStringBuilder.append(localResources.getString(2131363229, arrayOfObject));
            localStringBuilder.append(" ");
        }
        localStringBuilder.append(this.mResources.getString(2131363231));
        return localStringBuilder.toString();
    }

    private List<Contact> trim(List<Contact> paramList) {
        if (paramList.size() > 3) {
            paramList = paramList.subList(0, 3);
        }
        return paramList;
    }

    public void cancelByTouch() {
        EventLogger.recordClientEventWithSource(45, 16777216, Integer.valueOf(10));
        this.mMainController.exit();
    }

    public void cancelByVoice() {
        EventLogger.recordClientEventWithSource(45, 33554432, Integer.valueOf(10));
        this.mSoundManager.playHandsFreeShutDownSound();
        this.mMainController.exit();
    }

    public void createView() {
        this.mUi = this.mViewDisplayer.showPhoneCallDisambigContact();
        this.mUi.setContacts(this.mContacts, this.mSameName);
        this.mUi.setController(this);
        this.mUi.setLanguage(this.mMainController.getSpokenLanguageName());
        if (this.mSameName) {
            String str = ((Contact) this.mContacts.get(0)).getName();
            if (str != null) {
                this.mUi.setTitle(str);
            }
        }
        if (this.mListening) {
            this.mUi.showListening();
        }
    }

    public void handleVoiceErrror() {
        EventLogger.recordClientEventWithSource(45, 50331648, Integer.valueOf(10));
        this.mMainController.showError(2131363633, 2131363633);
    }

    public void pickContactByTouch(Contact paramContact) {
        EventLogger.recordClientEventWithSource(44, 16777216, Integer.valueOf(10));
        EventLogger.recordClientEventWithSource(13, 16777216, Integer.valueOf(10));
        this.mMainController.startActivity(PhoneActionUtils.getCallIntent(paramContact.getValue()));
        this.mMainController.exit();
    }

    public void pickContactByVoice(Contact paramContact) {
        EventLogger.recordClientEventWithSource(44, 33554432, Integer.valueOf(10));
        this.mMainController.callContact(paramContact);
    }

    public void setMainController(MainController paramMainController) {
        if (this.mMainController == null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mMainController = ((MainController) Preconditions.checkNotNull(paramMainController));
            return;
        }
    }

    public void start(List<Person> paramList) {
        ExtraPreconditions.checkMainThread();
        this.mPersons = paramList;
        this.mContacts = trim(Person.denormalizeContacts(ContactLookup.Mode.PHONE_NUMBER, this.mPersons));
        this.mListening = false;
        this.mSameName = PhoneActionUtils.containsSameName(this.mPersons);
        createView();
        if (this.mSameName) {
        }
        for (String str = buildOneNameChoiceText(this.mContacts); ; str = buildDifferentNamesChoiceText(this.mContacts)) {
            this.mRecognizerController.startCommandRecognitionNoUi(new DisambigListener(), 3, str);
            return;
        }
    }

    private class DisambigListener
            extends RecognitionEventListenerAdapter {
        private boolean mResponseDispatched = false;

        public DisambigListener() {
        }

        public void onDone() {

            if (!this.mResponseDispatched) {
                this.mResponseDispatched = true;
                PhoneCallDisambigContactController.this.handleVoiceErrror();
            }
        }

        public void onEndOfSpeech() {
            PhoneCallDisambigContactController.access$102(PhoneCallDisambigContactController.this, false);
            PhoneCallDisambigContactController.this.mUi.showNotListening();
        }

        public void onError(RecognizeException paramRecognizeException) {
            ExtraPreconditions.checkMainThread();
            PhoneCallDisambigContactController.this.handleVoiceErrror();
            this.mResponseDispatched = true;
        }

        public void onNoSpeechDetected() {
            ExtraPreconditions.checkMainThread();
            PhoneCallDisambigContactController.access$102(PhoneCallDisambigContactController.this, false);
            PhoneCallDisambigContactController.this.handleVoiceErrror();
            this.mResponseDispatched = true;
        }

        public void onReadyForSpeech() {
            PhoneCallDisambigContactController.access$102(PhoneCallDisambigContactController.this, true);
            PhoneCallDisambigContactController.this.mUi.showListening();
        }

        public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {

            if ((!this.mResponseDispatched) && (PhoneCallDisambigContactController.this.mInterpretationProcessor.handleRecognitionEvent(paramRecognitionEvent))) {
                this.mResponseDispatched = true;
            }
        }
    }

    private class InterpretationProcessorListener
            implements InterpretationProcessor.Listener {
        private InterpretationProcessorListener() {
        }

        public void onCancel() {
            PhoneCallDisambigContactController.this.cancelByVoice();
        }

        public void onConfirm() {
            PhoneCallDisambigContactController.this.handleVoiceErrror();
        }

        public void onSelect(int paramInt) {
            int i = 1;
            if (paramInt >= i) {
            }
            for (; ; ) {
                Preconditions.checkArgument(i);
                int j = paramInt - 1;
                if (j >= PhoneCallDisambigContactController.this.mContacts.size()) {
                    PhoneCallDisambigContactController.this.handleVoiceErrror();
                }
                PhoneCallDisambigContactController.this.pickContactByVoice((Contact) PhoneCallDisambigContactController.this.mContacts.get(j));
                return;
                i = 0;
            }
        }
    }

    public static abstract interface Ui
            extends HandsFreeRecognitionUi {
        public abstract void setContacts(List<Contact> paramList, boolean paramBoolean);

        public abstract void setController(PhoneCallDisambigContactController paramPhoneCallDisambigContactController);

        public abstract void setTitle(String paramString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.PhoneCallDisambigContactController

 * JD-Core Version:    0.7.0.1

 */