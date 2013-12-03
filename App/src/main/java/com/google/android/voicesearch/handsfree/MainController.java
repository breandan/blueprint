package com.google.android.voicesearch.handsfree;

import android.content.Intent;

import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.voicesearch.fragments.HandsFreeRecognizerController;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.common.base.Preconditions;

import java.util.List;

public class MainController {
    private final ActivityCallback mActivityCallback;
    private final ErrorController mErrorController;
    private final AudioRouterHandsfree mHandsFreeAudioRouter;
    private final SpokenLanguageHelper mHandsFreeSpokenLanguageHelper;
    private final InitializeController mInitializeController;
    private final LocalTtsManager mLocalTtsManager;
    private final ScheduledSingleThreadedExecutor mMainThreadExecutor;
    private final PhoneCallContactController mPhoneCallContactController;
    private final PhoneCallDisambigContactController mPhoneCallDisambigContactController;
    private final HandsFreeRecognizerController mRecognizerController;
    private final SpeakNowController mSpeakNowController;

    public MainController(ActivityCallback paramActivityCallback, InitializeController paramInitializeController, SpeakNowController paramSpeakNowController, PhoneCallDisambigContactController paramPhoneCallDisambigContactController, PhoneCallContactController paramPhoneCallContactController, ErrorController paramErrorController, SpokenLanguageHelper paramSpokenLanguageHelper, HandsFreeRecognizerController paramHandsFreeRecognizerController, LocalTtsManager paramLocalTtsManager, AudioRouterHandsfree paramAudioRouterHandsfree, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor) {
        this.mActivityCallback = paramActivityCallback;
        this.mInitializeController = paramInitializeController;
        this.mSpeakNowController = paramSpeakNowController;
        this.mPhoneCallDisambigContactController = paramPhoneCallDisambigContactController;
        this.mPhoneCallContactController = paramPhoneCallContactController;
        this.mErrorController = paramErrorController;
        this.mHandsFreeSpokenLanguageHelper = paramSpokenLanguageHelper;
        this.mRecognizerController = paramHandsFreeRecognizerController;
        this.mLocalTtsManager = paramLocalTtsManager;
        this.mHandsFreeAudioRouter = paramAudioRouterHandsfree;
        this.mMainThreadExecutor = paramScheduledSingleThreadedExecutor;
    }

    public void callContact(Contact paramContact) {
        this.mPhoneCallContactController.start(paramContact);
    }

    public void callPersons(List<Person> paramList) {
        if (paramList.size() > 0) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkArgument(bool);
            PersonDisambiguation localPersonDisambiguation = new PersonDisambiguation(ContactLookup.Mode.PHONE_NUMBER);
            localPersonDisambiguation.setCandidates(paramList, false);
            if (!localPersonDisambiguation.isCompleted()) {
                break;
            }
            callContact(((Person) localPersonDisambiguation.get()).getSelectedItem());
            return;
        }
        this.mPhoneCallDisambigContactController.start(paramList);
    }

    public void destroy() {
    }

    public void exit() {
        this.mLocalTtsManager.clearCallbacksAndStop();
        this.mActivityCallback.finish();
    }

    public String getSpokenLanguageName() {
        return this.mHandsFreeSpokenLanguageHelper.getSpokenDisplayLanguageName();
    }

    public void init() {
        this.mInitializeController.setMainController(this);
        this.mSpeakNowController.setMainController(this);
        this.mPhoneCallDisambigContactController.setMainController(this);
        this.mPhoneCallContactController.setMainController(this);
        this.mErrorController.setMainController(this);
    }

    public void pause() {
        this.mRecognizerController.cancel();
        this.mLocalTtsManager.clearCallbacksAndStop();
        this.mHandsFreeAudioRouter.stopRoute();
    }

    public void scheduleExit() {
        this.mMainThreadExecutor.executeDelayed(new Runnable() {
            public void run() {
                MainController.this.exit();
            }
        }, 1000L);
    }

    public void showError(int paramInt1, int paramInt2) {
        this.mErrorController.start(paramInt1, paramInt2);
    }

    public void start() {
        this.mInitializeController.start();
    }

    public void startActivity(Intent paramIntent) {
        this.mActivityCallback.startActivity(paramIntent);
    }

    public void startSpeakNow() {
        this.mSpeakNowController.start();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.MainController

 * JD-Core Version:    0.7.0.1

 */