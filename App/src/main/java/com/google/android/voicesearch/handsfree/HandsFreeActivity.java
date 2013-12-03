package com.google.android.voicesearch.handsfree;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;

import com.google.android.search.core.DeviceCapabilityManager;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.OfflineActionsManager;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.fragments.HandsFreeRecognizerController;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.logger.EventLoggerService;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.LocalTtsManager;

public class HandsFreeActivity
        extends Activity {
    private boolean mControllerStarted;
    private DeviceCapabilityManager mDeviceCapabilityManager;
    private Handler mHandler;
    private MainController mHandsFreeMainController;
    private boolean mShouldFinishOnStop;
    private SpokenLanguageHelper mSpokenLanguageHelper;
    private final Runnable mStartControllerRunnable = new Runnable() {
        public void run() {
            EventLogger.recordClientEvent(77);
            HandsFreeActivity.access$002(HandsFreeActivity.this, true);
            HandsFreeActivity.this.mHandsFreeMainController.start();
        }
    };

    private boolean hasDeviceCapabilities() {
        boolean bool1 = this.mSpokenLanguageHelper.hasResources();
        boolean bool2 = this.mDeviceCapabilityManager.isTelephoneCapable();
        return (bool1) && (bool2);
    }

    public void onCreate(Bundle paramBundle) {
        EventLogger.recordClientEvent(79);
        super.onCreate(paramBundle);
        VelvetServices localVelvetServices = VelvetServices.get();
        VoiceSearchServices localVoiceSearchServices = localVelvetServices.getVoiceSearchServices();
        Settings localSettings = localVoiceSearchServices.getSettings();
        boolean bool = getIntent().getAction().equals("android.speech.action.VOICE_SEARCH_HANDS_FREE");
        Greco3DataManager localGreco3DataManager = localVoiceSearchServices.getGreco3Container().getGreco3DataManager();
        localGreco3DataManager.initialize();
        getWindow().addFlags(2621440);
        setContentView(2130968706);
        this.mDeviceCapabilityManager = localVelvetServices.getCoreServices().getDeviceCapabilityManager();
        ScheduledSingleThreadedExecutor localScheduledSingleThreadedExecutor = VelvetServices.get().getAsyncServices().getUiThreadExecutor();
        this.mSpokenLanguageHelper = new SpokenLanguageHelper(localGreco3DataManager, localSettings);
        this.mHandler = new Handler(Looper.getMainLooper());
        HandsFreeRecognizerController localHandsFreeRecognizerController = HandsFreeRecognizerController.createForVoiceDialer(localVoiceSearchServices);
        LocalTtsManager localLocalTtsManager = localVoiceSearchServices.getLocalTtsManager();
        AudioTrackSoundManager localAudioTrackSoundManager = localVoiceSearchServices.getSoundManager();
        ActivityCallback localActivityCallback = new ActivityCallback(this);
        ViewDisplayer localViewDisplayer = new ViewDisplayer(this, (FrameLayout) findViewById(2131296670));
        AsyncContactRetriever localAsyncContactRetriever = new AsyncContactRetriever(ContactLookup.newInstance(this), localVoiceSearchServices.getScheduledExecutorService());
        OfflineActionsManager localOfflineActionsManager = localVoiceSearchServices.getOfflineActionsManager();
        AudioRouterHandsfree localAudioRouterHandsfree = new AudioRouterHandsfree(localScheduledSingleThreadedExecutor, localVoiceSearchServices.getScheduledExecutorService(), localVoiceSearchServices.getAudioRouter(), localVoiceSearchServices.getBluetoothController(), bool);
        InitializeController localInitializeController = new InitializeController(localAudioRouterHandsfree, getResources(), localLocalTtsManager, localViewDisplayer, localGreco3DataManager, localOfflineActionsManager, localScheduledSingleThreadedExecutor, localVoiceSearchServices.getScheduledExecutorService(), localAudioTrackSoundManager, localSettings);
        SpeakNowController localSpeakNowController = new SpeakNowController(localHandsFreeRecognizerController, localLocalTtsManager, localViewDisplayer, localAsyncContactRetriever, localScheduledSingleThreadedExecutor);
        PhoneCallDisambigContactController localPhoneCallDisambigContactController = new PhoneCallDisambigContactController(localHandsFreeRecognizerController, getResources(), localViewDisplayer, localAudioTrackSoundManager);
        PhoneCallContactController localPhoneCallContactController = new PhoneCallContactController(localHandsFreeRecognizerController, getResources(), localViewDisplayer, localAudioTrackSoundManager);
        ErrorController localErrorController = new ErrorController(getResources(), localLocalTtsManager, localViewDisplayer);
        this.mHandsFreeMainController = new MainController(localActivityCallback, localInitializeController, localSpeakNowController, localPhoneCallDisambigContactController, localPhoneCallContactController, localErrorController, this.mSpokenLanguageHelper, localHandsFreeRecognizerController, localLocalTtsManager, localAudioRouterHandsfree, localScheduledSingleThreadedExecutor);
        this.mHandsFreeMainController.init();
    }

    public void onDestroy() {
        if (this.mHandsFreeMainController != null) {
            this.mHandsFreeMainController.destroy();
        }
        super.onDestroy();
    }

    public void onPause() {
        if (this.mControllerStarted) {
            EventLogger.recordClientEvent(78);
            this.mHandsFreeMainController.pause();
            this.mShouldFinishOnStop = true;
            EventLoggerService.scheduleSendEvents(this);
        }
        for (; ; ) {
            super.onPause();
            return;
            this.mHandler.removeCallbacks(this.mStartControllerRunnable);
        }
    }

    public void onResume() {
        super.onResume();
        EventLoggerService.cancelSendEvents(this);
        if (!hasDeviceCapabilities()) {
            finish();
            return;
        }
        this.mShouldFinishOnStop = false;
        this.mHandler.postDelayed(this.mStartControllerRunnable, 200L);
    }

    public void onStop() {
        super.onStop();
        if ((!isFinishing()) && (this.mShouldFinishOnStop)) {
            finish();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.HandsFreeActivity

 * JD-Core Version:    0.7.0.1

 */