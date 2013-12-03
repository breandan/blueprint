package com.google.android.voicesearch.handsfree;

import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.ThreadChanger;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.voicesearch.fragments.HandsFreeRecognizerController;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.common.base.Preconditions;
import com.google.majel.proto.ActionV2Protos.PhoneAction;
import com.google.majel.proto.MajelProtos.MajelResponse;

import java.util.List;

class SpeakNowController {
    private final ActionProcessor mActionProcessor;
    private final AsyncContactRetriever mAsyncContactRetriever;
    private final LocalTtsManager mLocalTtsManager;
    private MainController mMainController;
    private final HandsFreeRecognizerController mRecognizerController;
    private Ui mUi;
    private final ScheduledSingleThreadedExecutor mUiExecutor;
    private final ViewDisplayer mViewDisplayer;

    public SpeakNowController(HandsFreeRecognizerController paramHandsFreeRecognizerController, LocalTtsManager paramLocalTtsManager, ViewDisplayer paramViewDisplayer, AsyncContactRetriever paramAsyncContactRetriever, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor) {
        this.mRecognizerController = ((HandsFreeRecognizerController) Preconditions.checkNotNull(paramHandsFreeRecognizerController));
        this.mViewDisplayer = ((ViewDisplayer) Preconditions.checkNotNull(paramViewDisplayer));
        this.mAsyncContactRetriever = ((AsyncContactRetriever) Preconditions.checkNotNull(paramAsyncContactRetriever));
        this.mActionProcessor = new ActionProcessor(new ActionListener(null));
        this.mLocalTtsManager = paramLocalTtsManager;
        this.mUiExecutor = paramScheduledSingleThreadedExecutor;
    }

    private void handleNoMatch() {
        this.mMainController.showError(2131363633, 2131363633);
    }

    public void setMainController(MainController paramMainController) {
        this.mMainController = paramMainController;
    }

    public void start() {

        if (this.mMainController != null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mUi = this.mViewDisplayer.showSpeakNow();
            this.mUi.setController(this);
            this.mUi.setLanguage(this.mMainController.getSpokenLanguageName());
            this.mUi.showStart();
            this.mRecognizerController.attachUi(new RecognitionUiAdapter(this.mUi));
            Runnable local1 = new Runnable() {
                public void run() {
                    SpeakNowController.this.mRecognizerController.startHandsFreeContactRecognition(new SpeakNowController.RecognizerListener(SpeakNowController.this, null));
                }
            };
            Runnable localRunnable = (Runnable) ThreadChanger.createNonBlockingThreadChangeProxy(this.mUiExecutor, local1);
            this.mLocalTtsManager.enqueue(2131363230, localRunnable);
            return;
        }
    }

    private final class ActionListener
            implements ActionProcessor.ActionListener {
        private ActionListener() {
        }

        public void onPhoneAction(ActionV2Protos.PhoneAction paramPhoneAction) {
            SpeakNowController.this.mAsyncContactRetriever.start(paramPhoneAction, new SpeakNowController.AsyncPersonRetrieverListener(SpeakNowController.this, null));
        }
    }

    private final class AsyncPersonRetrieverListener
            implements AsyncContactRetriever.Listener {
        private AsyncPersonRetrieverListener() {
        }

        public void onMatch(List<Person> paramList) {
            SpeakNowController.this.mMainController.callPersons(paramList);
        }

        public void onNoMatch() {
            SpeakNowController.this.handleNoMatch();
        }
    }

    private final class RecognizerListener
            extends RecognitionEventListenerAdapter {
        private boolean mActionProcessed;

        private RecognizerListener() {
        }

        public void onDone() {
            if (!this.mActionProcessed) {
                this.mActionProcessed = true;
                SpeakNowController.this.handleNoMatch();
            }
        }

        public void onEndOfSpeech() {
            SpeakNowController.this.mUi.showNotListening();
        }

        public void onError(RecognizeException paramRecognizeException) {
            if (!this.mActionProcessed) {
                this.mActionProcessed = true;
                SpeakNowController.this.handleNoMatch();
            }
        }

        public void onMajelResult(MajelProtos.MajelResponse paramMajelResponse) {
            if (!this.mActionProcessed) {
                this.mActionProcessed = true;
                if (!SpeakNowController.this.mActionProcessor.process(paramMajelResponse)) {
                    SpeakNowController.this.handleNoMatch();
                }
            }
        }

        public void onNoSpeechDetected() {
            if (!this.mActionProcessed) {
                this.mActionProcessed = true;
                SpeakNowController.this.handleNoMatch();
            }
        }

        public void onReadyForSpeech() {
            SpeakNowController.this.mUi.showListening();
        }

        public void onRecognitionCancelled() {
            if (!this.mActionProcessed) {
                SpeakNowController.this.mMainController.exit();
            }
        }
    }

    public static abstract interface Ui
            extends HandsFreeRecognitionUi {
        public abstract void setController(SpeakNowController paramSpeakNowController);

        public abstract void showStart();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.SpeakNowController

 * JD-Core Version:    0.7.0.1

 */