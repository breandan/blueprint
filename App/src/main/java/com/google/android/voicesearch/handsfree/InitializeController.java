package com.google.android.voicesearch.handsfree;

import android.content.res.Resources;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.StateMachine;
import com.google.android.shared.util.ThreadChanger;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.embedded.OfflineActionsManager;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.common.base.Preconditions;

import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Nullable;

class InitializeController {
    private final AudioRouterHandsfree mAudioRouter;
    private final Greco3DataManager mGreco3DataManager;
    private StateMachine<Init> mInitMachine = StateMachine.newBuilder("InitializeController", Init.NOTHING).addTransition(Init.NOTHING, Init.AUDIO_ROUTE).addTransition(Init.NOTHING, Init.ERROR).addTransition(Init.AUDIO_ROUTE, Init.GRECO3_DATA).addTransition(Init.AUDIO_ROUTE, Init.ERROR).addTransition(Init.GRECO3_DATA, Init.GRAMMAR_COMPILED).addTransition(Init.GRECO3_DATA, Init.ERROR).setDebug(false).setSingleThreadOnly(true).setStrictMode(true).build();
    private final LocalTtsManager mLocalTtsManager;
    @Nullable
    private MainController mMainController;
    private final ScheduledSingleThreadedExecutor mMainExecutor;
    private final OfflineActionsManager mOfflineActionsManager;
    private boolean mPlayingTts;
    private final Settings mSettings;
    private final AudioTrackSoundManager mSoundManager;
    @Nullable
    private Ui mUi;
    private final ViewDisplayer mViewDisplayer;

    public InitializeController(AudioRouterHandsfree paramAudioRouterHandsfree, Resources paramResources, LocalTtsManager paramLocalTtsManager, ViewDisplayer paramViewDisplayer, Greco3DataManager paramGreco3DataManager, OfflineActionsManager paramOfflineActionsManager, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, ScheduledExecutorService paramScheduledExecutorService, AudioTrackSoundManager paramAudioTrackSoundManager, Settings paramSettings) {
        this.mLocalTtsManager = ((LocalTtsManager) Preconditions.checkNotNull(paramLocalTtsManager));
        this.mViewDisplayer = ((ViewDisplayer) Preconditions.checkNotNull(paramViewDisplayer));
        this.mAudioRouter = paramAudioRouterHandsfree;
        this.mMainExecutor = paramScheduledSingleThreadedExecutor;
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mOfflineActionsManager = paramOfflineActionsManager;
        this.mSettings = paramSettings;
        this.mSoundManager = paramAudioTrackSoundManager;
    }

    private void compileGrammar() {
        this.mInitMachine.checkIn(Init.GRECO3_DATA);
        String str = this.mSettings.getSpokenLocaleBcp47();
        if (!this.mGreco3DataManager.hasResourcesForCompilation(str)) {
            str = "en-US";
        }
        GrammarCompilationCallback localGrammarCompilationCallback = new GrammarCompilationCallback(null);
        SimpleCallback localSimpleCallback = (SimpleCallback) ThreadChanger.createNonBlockingThreadChangeProxy(this.mMainExecutor, localGrammarCompilationCallback);
        OfflineActionsManager localOfflineActionsManager = this.mOfflineActionsManager;
        Greco3Grammar[] arrayOfGreco3Grammar = new Greco3Grammar[2];
        arrayOfGreco3Grammar[0] = Greco3Grammar.CONTACT_DIALING;
        arrayOfGreco3Grammar[1] = Greco3Grammar.HANDS_FREE_COMMANDS;
        localOfflineActionsManager.startOfflineDataCheck(localSimpleCallback, str, arrayOfGreco3Grammar);
    }

    private void handleAudioRouteEstablished() {
        ExtraPreconditions.checkMainThread();
        this.mInitMachine.moveTo(Init.AUDIO_ROUTE);
        if (this.mGreco3DataManager.isInitialized()) {
            this.mInitMachine.moveTo(Init.GRECO3_DATA);
            compileGrammar();
        }
        this.mMainExecutor.executeDelayed(new Runnable() {
            public void run() {
                InitializeController.this.showInitialization();
            }
        }, 1000L);
    }

    private void handleGrammarCompilationError() {

        if (!this.mPlayingTts) {
            this.mSoundManager.playErrorSound();
            this.mMainController.exit();
        }
    }

    private void handleGrammarCompilationSuccess() {
        ExtraPreconditions.checkMainThread();
        this.mInitMachine.moveTo(Init.GRAMMAR_COMPILED);
        if (!this.mPlayingTts) {
            this.mMainController.startSpeakNow();
        }
    }

    private void handleTtsInitializationCompletion() {
        ExtraPreconditions.checkMainThread();
        this.mPlayingTts = false;
        if (this.mInitMachine.isIn(Init.ERROR)) {
            this.mSoundManager.playErrorSound();
            this.mMainController.exit();
        }
        while (!this.mInitMachine.isIn(Init.GRAMMAR_COMPILED)) {
            return;
        }
        this.mMainController.startSpeakNow();
    }

    private void showInitialization() {

        if (this.mInitMachine.isIn(Init.ERROR)) {
        }
        while (this.mInitMachine.isIn(Init.GRAMMAR_COMPILED)) {
            return;
        }
        this.mUi.setMessage(2131363577);
        this.mPlayingTts = true;
        Runnable localRunnable = (Runnable) ThreadChanger.createNonBlockingThreadChangeProxy(this.mMainExecutor, new Runnable() {
            public void run() {
                InitializeController.this.handleTtsInitializationCompletion();
            }
        });
        this.mLocalTtsManager.enqueue(2131363634, localRunnable);
    }

    public void setMainController(MainController paramMainController) {
        this.mMainController = paramMainController;
    }

    public void start() {
        this.mInitMachine.checkIn(Init.NOTHING);
        this.mUi = this.mViewDisplayer.showInitialize();
        this.mAudioRouter.establishRoute(new AudioRouterListener(null));
    }

    private class AudioRouterListener
            implements AudioRouterHandsfree.Listener {
        private AudioRouterListener() {
        }

        public void onAudioRouteEstablished() {
            InitializeController.this.handleAudioRouteEstablished();
        }

        public void onAudioRouteFailed() {
            InitializeController.this.mMainController.exit();
        }
    }

    private class GrammarCompilationCallback
            implements SimpleCallback<Integer> {
        private GrammarCompilationCallback() {
        }

        public void onResult(Integer paramInteger) {
            if (paramInteger.intValue() == 1) {
                InitializeController.this.handleGrammarCompilationSuccess();
                return;
            }
            InitializeController.this.handleGrammarCompilationError();
        }
    }

    static enum Init {
        static {
            AUDIO_ROUTE = new Init("AUDIO_ROUTE", 1);
            GRECO3_DATA = new Init("GRECO3_DATA", 2);
            GRAMMAR_COMPILED = new Init("GRAMMAR_COMPILED", 3);
            ERROR = new Init("ERROR", 4);
            Init[] arrayOfInit = new Init[5];
            arrayOfInit[0] = NOTHING;
            arrayOfInit[1] = AUDIO_ROUTE;
            arrayOfInit[2] = GRECO3_DATA;
            arrayOfInit[3] = GRAMMAR_COMPILED;
            arrayOfInit[4] = ERROR;
            $VALUES = arrayOfInit;
        }

        private Init() {
        }
    }

    public static abstract interface Ui {
        public abstract void setMessage(int paramInt);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.InitializeController

 * JD-Core Version:    0.7.0.1

 */