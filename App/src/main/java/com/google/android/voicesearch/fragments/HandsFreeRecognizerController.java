package com.google.android.voicesearch.fragments;

import android.net.Uri;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.android.search.core.prefetch.S3FetchTask;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.RecognitionUi;
import com.google.android.speech.Recognizer;
import com.google.android.speech.alternates.Hypothesis;
import com.google.android.speech.alternates.HypothesisToSuggestionSpansConverter;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.embedded.Greco3RecognitionEngine;
import com.google.android.speech.embedded.OfflineActionsManager;
import com.google.android.speech.exception.NetworkRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.CancellableRecognitionEventListener;
import com.google.android.speech.listeners.CompositeRecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.params.AudioInputParams;
import com.google.android.speech.params.SessionParams;
import com.google.android.speech.test.TestPlatformLog;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.speech.utils.RecognizedText;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.majel.proto.MajelProtos;
import com.google.speech.recognizer.api.RecognizerProtos;
import com.google.speech.s3.PinholeStream;

import java.util.concurrent.Executor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HandsFreeRecognizerController {
    private static final RecognitionUi NO_OP_UI = new RecognitionUi() {
        public void setFinalRecognizedText(@Nonnull CharSequence paramAnonymousCharSequence) {
        }

        public void showRecognitionState(int paramAnonymousInt) {
        }

        public void updateRecognizedText(String paramAnonymousString1, String paramAnonymousString2) {
        }
    };
    private final VoiceSearchServices mVoiceSearchServices;
    private CancellableRecognitionEventListener mEventListener;
    private int mFlags;
    @Nullable
    private GrammarCompilationCallback mGrammarCompilationCallback;
    private int mMode;
    @Nullable
    private NetworkInformation mNetworkInformation;
    @Nullable
    private OfflineActionsManager mOfflineActionsManager;
    private boolean mRecognitionInProgress;
    @Nullable
    private Recognizer mRecognizer;
    @Nullable
    private AudioTrackSoundManager mSoundManager;
    private String mSpeakPrompt;
    private RecognitionUi mUi;
    @Nullable
    private Executor mUiThreadExecutor;

    HandsFreeRecognizerController(VoiceSearchServices paramVoiceSearchServices) {
        this.mVoiceSearchServices = paramVoiceSearchServices;
        this.mUi = NO_OP_UI;
    }

    public static HandsFreeRecognizerController createForVoiceDialer(VoiceSearchServices paramVoiceSearchServices) {
        return new HandsFreeRecognizerController(paramVoiceSearchServices);
    }

    private static void maybeLogException(RecognizeException paramRecognizeException) {
        if ((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException)) {
            Log.i("HandsFreeRecognizerController", "No recognizers available.");
            return;
        }
        Log.e("HandsFreeRecognizerController", "onError", paramRecognizeException);
    }

    private void cancelInternal(boolean paramBoolean) {
        if (this.mRecognitionInProgress) {
            TestPlatformLog.logError("no_match");
            if (paramBoolean) {
                this.mEventListener.onRecognitionCancelled();
            }
            this.mRecognizer.cancel(this.mEventListener);
            this.mRecognitionInProgress = false;
        }
        if (this.mEventListener != null) {
            this.mEventListener.invalidate();
            this.mEventListener = null;
        }
        if ((this.mOfflineActionsManager != null) && (this.mGrammarCompilationCallback != null)) {
            this.mOfflineActionsManager.detach(this.mGrammarCompilationCallback);
            this.mGrammarCompilationCallback = null;
        }
    }

    private Greco3Grammar getGrammarType(int paramInt) {
        if (paramInt == 5) {
            return Greco3Grammar.HANDS_FREE_COMMANDS;
        }
        return Greco3Grammar.CONTACT_DIALING;
    }

    private Greco3Mode getGreco3Mode(int paramInt) {
        if ((paramInt == 2) || (paramInt == 5) || (paramInt == 4)) {
            return Greco3Mode.GRAMMAR;
        }
        return Greco3Mode.ENDPOINTER_VOICESEARCH;
    }

    private SessionParams.Builder getSessionParamsBuilder(int paramInt, boolean paramBoolean, @Nullable Uri paramUri) {
        AudioInputParams.Builder localBuilder = new AudioInputParams.Builder();
        if (isFlagSet(4)) {
            localBuilder.setPlayBeepEnabled(false);
        }
        localBuilder.setRecordedAudioUri(paramUri);
        SessionParams.Builder localBuilder1 = new SessionParams.Builder();
        Settings localSettings = this.mVoiceSearchServices.getSettings();
        localBuilder1.setSpokenBcp47Locale(localSettings.getSpokenLocaleBcp47()).setGreco3Grammar(getGrammarType(paramInt)).setGreco3Mode(getGreco3Mode(paramInt)).setResendingAudio(paramBoolean).setMode(paramInt).setProfanityFilterEnabled(localSettings.isProfanityFilterEnabled()).setAudioInputParams(localBuilder.build());
        if (paramInt == 2) {
            String str = this.mVoiceSearchServices.getSearchConfig().getVoiceActionsS3ServiceOverride();
            if (!TextUtils.isEmpty(str)) {
                localBuilder1.setServiceOverride(str);
            }
            localBuilder1.setServerEndpointingEnabled(this.mVoiceSearchServices.getGsaConfigFlags().isServerEndpointingEnabled());
        }
        if (isFlagSet(2)) {
            localBuilder1.setNoSpeechDetectedEnabled(false);
        }
        return localBuilder1;
    }

    private boolean isFlagSet(int paramInt) {
        return (paramInt & this.mFlags) != 0;
    }

    private void maybeInit() {
        if (this.mRecognizer == null) {
            this.mRecognizer = this.mVoiceSearchServices.getRecognizer();
            this.mUiThreadExecutor = this.mVoiceSearchServices.getMainThreadExecutor();
            this.mSoundManager = this.mVoiceSearchServices.getSoundManager();
            this.mOfflineActionsManager = this.mVoiceSearchServices.getOfflineActionsManager();
            this.mNetworkInformation = this.mVoiceSearchServices.getNetworkInformation();
        }
    }

    private void prepareRecognition(SessionParams sessionParams, RecognitionEventListener clientListener, String requestId) {
        cancelInternal(true);
        mMode = sessionParams.getMode();
        mRecognitionInProgress = true;
        if (clientListener != null) {
            CompositeRecognitionEventListener listeners = new CompositeRecognitionEventListener();
            listeners.add(new HandsFreeRecognizerController.InternalRecognitionEventListener(requestId));
            listeners.add(clientListener);
            CompositeRecognitionEventListener listener = listeners;
        } else {
            HandsFreeRecognizerController.InternalRecognitionEventListener listener = new HandsFreeRecognizerController.InternalRecognitionEventListener(requestId);
            mEventListener = new CancellableRecognitionEventListener(listener);
        }
    }

    private void reallyStartListening(SessionParams paramSessionParams) {
        this.mRecognizer.startListening(paramSessionParams, this.mEventListener, this.mUiThreadExecutor, this.mVoiceSearchServices.getVoiceSearchAudioStore());
    }

    private void startEmbeddedRecognitionInternal(int paramInt, RecognitionEventListener paramRecognitionEventListener) {
        startListening(getSessionParamsBuilder(paramInt, false, null).build(), paramRecognitionEventListener, true);
    }

    private void startListening(final SessionParams sessionParams, RecognitionEventListener listener, boolean offline) {
        prepareRecognition(sessionParams, listener, sessionParams.getRequestId());
        if (isFlagSet(0x1)) {
            mVoiceSearchServices.getLocalTtsManager().enqueue(mSpeakPrompt, new Runnable() {
                public void run() {
                    HandsFreeRecognizerController.this.reallyStartListening(sessionParams);
                }
            });
        } else {
            reallyStartListening(sessionParams);
        }
        if (!offline) {
            mUi.showRecognitionState(0x3);
        }
    }

    private void startOfflineRecognition(RecognitionEventListener resultsListener, int recognizerMode) {
        mGrammarCompilationCallback = new HandsFreeRecognizerController.GrammarCompilationCallback(resultsListener, recognizerMode);
        mOfflineActionsManager.startOfflineDataCheck(mGrammarCompilationCallback, SessionParams.isVoiceDialerSearch(recognizerMode) ? "en-US" : mVoiceSearchServices.getSettings().getSpokenLocaleBcp47(), new Greco3Grammar[]{Greco3Grammar.CONTACT_DIALING, Greco3Grammar.HANDS_FREE_COMMANDS});
        mUi.showRecognitionState(0x1);
    }

    private void startRecognition(@Nullable RecognitionEventListener paramRecognitionEventListener, int paramInt, @Nonnull Query paramQuery) {
        maybeInit();
        this.mFlags = 0;
        this.mSpeakPrompt = null;
        if ((!this.mVoiceSearchServices.getSettings().isNetworkRecognitionOnlyForDebug()) && ((!this.mNetworkInformation.isConnected()) || (this.mVoiceSearchServices.getSettings().isEmbeddedRecognitionOnlyForDebug()))) {
            startOfflineRecognition(paramRecognitionEventListener, paramInt);
            return;
        }
        startListening(getSessionParamsBuilder(paramInt, false, paramQuery.getRecordedAudioUri()).build(), paramRecognitionEventListener, false);
    }

    public void attachUi(RecognitionUi ui) {
        Preconditions.checkState(mUi == NO_OP_UI);
        mUi = Preconditions.checkNotNull(ui);
    }

    public void cancel() {
        if (this.mRecognitionInProgress) {
            EventLogger.recordClientEvent(18);
        }
        cancelInternal(true);
    }

    public void detachUi(RecognitionUi paramRecognitionUi) {
        Preconditions.checkState((paramRecognitionUi == this.mUi) || (this.mUi == NO_OP_UI));
        this.mUi = NO_OP_UI;
    }

    public void startCommandRecognitionNoUi(@Nullable RecognitionEventListener paramRecognitionEventListener, int paramInt, String paramString) {
        maybeInit();
        this.mFlags = paramInt;
        this.mSpeakPrompt = paramString;
        detachUi(this.mUi);
        startOfflineRecognition(paramRecognitionEventListener, 5);
    }

    public void startHandsFreeContactRecognition(@Nonnull RecognitionEventListener paramRecognitionEventListener) {
        startRecognition(Preconditions.checkNotNull(paramRecognitionEventListener), 4, Query.EMPTY);
    }

    private class GrammarCompilationCallback
            implements SimpleCallback<Integer> {
        private final int mRecognizerMode;
        private final RecognitionEventListener mResultsListener;

        public GrammarCompilationCallback(RecognitionEventListener paramRecognitionEventListener, int paramInt) {
            this.mResultsListener = paramRecognitionEventListener;
            this.mRecognizerMode = paramInt;
        }

        private void reportError(RecognizeException paramRecognizeException) {
            if (this.mResultsListener != null) {
                this.mResultsListener.onError(paramRecognizeException);
            }
        }

        public void onResult(Integer result) {
            if (result.intValue() == 0x1) {
                return;
            }
            if (result.intValue() == 0x4) {
                reportError(new OfflineActionsManager.GrammarCompilationException());
                return;
            }
            if (result.intValue() == 0x3) {
                reportError(new NetworkRecognizeException("No network connection"));
            }
        }
    }

    private class InternalRecognitionEventListener
            extends RecognitionEventListenerAdapter {
        private final RecognizedText mRecognizedText = new RecognizedText();
        private S3FetchTask mProxyFetchTask;
        private String mRequestId;

        public InternalRecognitionEventListener(String paramString) {
            this.mRequestId = paramString;
        }

        private void dispatchNoMatchException() {
            TestPlatformLog.logError("no_match");
        }

        private boolean hasCompletedRecognition() {
            return this.mRecognizedText.hasCompletedRecognition();
        }

        public void onBeginningOfSpeech(long paramLong) {
            HandsFreeRecognizerController.this.mUi.showRecognitionState(5);
        }

        public void onDone() {
            if (!hasCompletedRecognition()) {
                if (HandsFreeRecognizerController.this.mMode == 2) {
                    dispatchNoMatchException();
                }
                if (!HandsFreeRecognizerController.this.isFlagSet(4)) {
                    HandsFreeRecognizerController.this.mSoundManager.playNoInputSound();
                }
            }
            if ((this.mProxyFetchTask != null) && (!this.mProxyFetchTask.isFailedOrComplete())) {
                Log.e("HandsFreeRecognizerController", "Incomplete proxy task: " + this.mProxyFetchTask);
                this.mProxyFetchTask.cancel();
            }
            TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
            HandsFreeRecognizerController.this.cancelInternal(false);
        }

        public void onEndOfSpeech() {
            HandsFreeRecognizerController.this.mUi.showRecognitionState(6);
        }

        public void onError(RecognizeException paramRecognizeException) {
            HandsFreeRecognizerController.maybeLogException(paramRecognizeException);
            TestPlatformLog.logError(paramRecognizeException.toString());
            if ((!HandsFreeRecognizerController.this.isFlagSet(4)) && (!hasCompletedRecognition())) {
                HandsFreeRecognizerController.this.mSoundManager.playErrorSound();
            }
            if (this.mProxyFetchTask != null) {
                this.mProxyFetchTask.reportError(paramRecognizeException);
            }
            if (HandsFreeRecognizerController.this.mMode == 2) {
                HandsFreeRecognizerController.this.mEventListener.invalidate();
                String str = this.mRecognizedText.getStableForErrorReporting();
                Log.e("HandsFreeRecognizerController", "Got error after recognizing [" + str + "]");
            }
            HandsFreeRecognizerController.this.cancelInternal(false);
        }

        public void onMajelResult(MajelProtos.MajelResponse paramMajelResponse) {
            if (PhoneActionUtils.isPhoneActionFromEmbeddedRecognizer(paramMajelResponse)) {
                return;
            }
            Log.w("HandsFreeRecognizerController", "Unexpected majel response in stream.");
        }

        public void onMediaDataResult(byte[] paramArrayOfByte) {
        }

        public void onMusicDetected() {
        }

        public void onNoSpeechDetected() {
            HandsFreeRecognizerController.this.mUi.showRecognitionState(2);
            HandsFreeRecognizerController.this.cancelInternal(false);
            TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
        }

        public void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput) {
            if (this.mProxyFetchTask != null) {
                this.mProxyFetchTask.offerPinholeResult(paramPinholeOutput);
            }
        }

        public void onReadyForSpeech() {
            TestPlatformLog.log("SPEAK_NOW");
            EventLogger.recordClientEvent(5);
            HandsFreeRecognizerController.this.mUi.showRecognitionState(4);
        }

        public void onRecognitionCancelled() {
            if ((!HandsFreeRecognizerController.this.isFlagSet(4)) && (!hasCompletedRecognition())) {
                HandsFreeRecognizerController.this.mSoundManager.playNoInputSound();
            }
            if (this.mProxyFetchTask != null) {
                this.mProxyFetchTask.cancel();
            }
        }

        public void onRecognitionResult(RecognizerProtos.RecognitionEvent recognitionEvent) {
            TestPlatformLog.logResults(recognitionEvent);
            if (mRecognizedText.hasCompletedRecognition()) {
                Log.e("HandsFreeRecognizerController", "Result after completed recognition.");
                return;
            }
            if (recognitionEvent.getEventType() == 0) {
                Pair<String, String> stableAndUnstable = mRecognizedText.updateInProgress(recognitionEvent);
                String stable = stableAndUnstable.first;
                String unstable = stableAndUnstable.second;
                mUi.updateRecognizedText(stable, unstable);
                return;
            }
            if (recognitionEvent.getEventType() == 0x1) {
                ImmutableList<Hypothesis> allHypotheses = mRecognizedText.updateFinal(recognitionEvent);
                if (allHypotheses.isEmpty()) {
                    Log.i("HandsFreeRecognizerController", "Empty combined result");
                    dispatchNoMatchException();
                    return;
                }
                HypothesisToSuggestionSpansConverter converter = mVoiceSearchServices.getHypothesisToSuggestionSpansConverter();
                SpannedString firstResult = converter.getSuggestionSpannedStringForQuery(mRequestId, allHypotheses.get(0x0));
                mUi.setFinalRecognizedText(firstResult);
                ImmutableList.Builder<CharSequence> otherHypothesesBuilder = ImmutableList.builder();
                for (int i = 0x1; i < allHypotheses.size(); i = i + 0x1) {
                    Hypothesis hypothesis = allHypotheses.get(i);
                    otherHypothesesBuilder.add(hypothesis.getText());
                }
                ImmutableList<CharSequence> otherHypotheses = otherHypothesesBuilder.build();
                mProxyFetchTask = null;
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.HandsFreeRecognizerController

 * JD-Core Version:    0.7.0.1

 */