package com.google.android.voicesearch.fragments;

import android.net.Uri;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.prefetch.S3FetchTask;
import com.google.android.search.core.prefetch.SearchResult;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.alternates.Hypothesis;
import com.google.android.speech.alternates.HypothesisToSuggestionSpansConverter;
import com.google.android.speech.audio.AudioStore;
import com.google.android.speech.audio.AudioUtils;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.embedded.Greco3RecognitionEngine;
import com.google.android.speech.embedded.OfflineActionsManager;
import com.google.android.speech.exception.NetworkRecognizeException;
import com.google.android.speech.exception.NoMatchRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.CancellableRecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.params.AudioInputParams;
import com.google.android.speech.params.SessionParams;
import com.google.android.speech.test.TestPlatformLog;
import com.google.android.speech.utils.RecognizedText;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.majel.proto.MajelProtos;
import com.google.speech.recognizer.api.RecognizerProtos;
import com.google.speech.s3.PinholeStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VoiceSearchController {
    private final Clock mClock;
    private final GsaConfigFlags mGsaConfigFlags;
    private final SearchUrlHelper mSearchUrlHelper;
    private final ExtraPreconditions.ThreadCheck mThreadCheck;
    private final VoiceSearchServices mVss;
    @Nullable
    private CancellableRecognitionEventListener mEventListener;
    @Nullable
    private GrammarCompilationCallback mGrammarCompilationCallback;
    private S3FetchTask mProxyFetchTask;
    private boolean mRecognitionInProgress;

    public VoiceSearchController(VoiceSearchServices paramVoiceSearchServices, Clock paramClock, SearchUrlHelper paramSearchUrlHelper, GsaConfigFlags paramGsaConfigFlags) {
        this.mVss = paramVoiceSearchServices;
        this.mClock = paramClock;
        this.mSearchUrlHelper = paramSearchUrlHelper;
        this.mThreadCheck = ExtraPreconditions.createSameThreadCheck();
        this.mGsaConfigFlags = paramGsaConfigFlags;
    }

    private static void maybeLogException(RecognizeException paramRecognizeException) {
        if ((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException)) {
            Log.i("VoiceSearchController", "No recognizers available.");
            return;
        }
        Log.e("VoiceSearchController", "onError", paramRecognizeException);
    }

    private void cancelInternal(boolean paramBoolean1, boolean paramBoolean2) {
        CancellableRecognitionEventListener localCancellableRecognitionEventListener = this.mEventListener;
        if (this.mEventListener != null) {
            this.mEventListener.invalidate();
            this.mEventListener = null;
        }
        if ((this.mProxyFetchTask != null) && (paramBoolean2)) {
            this.mProxyFetchTask.cancel();
        }
        this.mProxyFetchTask = null;
        if (this.mGrammarCompilationCallback != null) {
            this.mVss.getOfflineActionsManager().detach(this.mGrammarCompilationCallback);
            this.mGrammarCompilationCallback = null;
        }
        if (this.mRecognitionInProgress) {
            TestPlatformLog.logError("no_match");
            if (paramBoolean1) {
                this.mVss.getSoundManager().playNoInputSound();
            }
            this.mVss.getRecognizer().cancel(localCancellableRecognitionEventListener);
            this.mRecognitionInProgress = false;
        }
    }

    private SessionParams getSessionParams(AudioStore.AudioRecording resendAudio, boolean isTriggeredFromBluetooth, Uri recordedAudioUri) {
        AudioInputParams.Builder audioBuilder = new AudioInputParams.Builder();
        if (resendAudio != null) {
            audioBuilder.setSamplingRate(resendAudio.getSampleRate());
            audioBuilder.setEncoding(AudioUtils.getAmrEncodingForRecording(resendAudio).getRecognizerEncoding());
        } else if (recordedAudioUri != null) {
            audioBuilder.setRecordedAudioUri(recordedAudioUri);
        } else if ((mVss.getGsaConfigFlags().shouldUseAmrWbEncoding()) && (!mVss.getSettings().isBluetoothHeadsetEnabled()) && (!isTriggeredFromBluetooth)) {
            audioBuilder.setEncoding(0x9);
            audioBuilder.setSamplingRate(0x3e80);
        }
        SessionParams.Builder builder = new SessionParams.Builder();
        Settings speechSettings = mVss.getSettings();
        builder.setSpokenBcp47Locale(speechSettings.getSpokenLocaleBcp47()).setGreco3Grammar(Greco3Grammar.CONTACT_DIALING).setGreco3Mode(Greco3Mode.GRAMMAR).setResendingAudio((resendAudio != null)).setMode(0x2).setProfanityFilterEnabled(speechSettings.isProfanityFilterEnabled()).setAudioInputParams(audioBuilder.build()).setServerEndpointingEnabled(mVss.getGsaConfigFlags().isServerEndpointingEnabled());
        String serviceOverride = mVss.getSearchConfig().getVoiceActionsS3ServiceOverride();
        if (!TextUtils.isEmpty(serviceOverride)) {
            builder.setServiceOverride(serviceOverride);
        }
        return builder.build();
    }

    private void resendVoiceSearch(Query paramQuery, Listener paramListener) {
        AudioStore.AudioRecording localAudioRecording = this.mVss.getVoiceSearchAudioStore().getLastAudio();
        if (localAudioRecording == null) {
            startNewVoiceSearch(paramQuery, paramListener);
            return;
        }
        SessionParams localSessionParams = getSessionParams(localAudioRecording, paramQuery.isTriggeredFromBluetoothHandsfree(), null);
        this.mRecognitionInProgress = true;
        this.mEventListener = new CancellableRecognitionEventListener(new InternalRecognitionEventListener(localSessionParams.getRequestId(), paramListener, paramQuery));
        paramListener.onRecognizing();
        this.mVss.getRecognizer().startRecordedAudioRecognition(localSessionParams, localAudioRecording.getAudio(), this.mEventListener, this.mVss.getMainThreadExecutor());
    }

    private void startListening(Listener paramListener, Query paramQuery, boolean paramBoolean) {
        SessionParams localSessionParams = getSessionParams(null, paramQuery.isTriggeredFromBluetoothHandsfree(), paramQuery.getRecordedAudioUri());
        this.mEventListener = new CancellableRecognitionEventListener(new InternalRecognitionEventListener(localSessionParams.getRequestId(), paramListener, paramQuery));
        this.mVss.getRecognizer().startListening(localSessionParams, this.mEventListener, this.mVss.getMainThreadExecutor(), this.mVss.getVoiceSearchAudioStore());
        if (!paramBoolean) {
            paramListener.onInitializing();
        }
    }

    private void startNewVoiceSearch(Query paramQuery, Listener paramListener) {
        this.mRecognitionInProgress = true;
        if ((this.mVss.getSettings().isNetworkRecognitionOnlyForDebug()) || ((this.mVss.getNetworkInformation().isConnected()) && (!this.mVss.getSettings().isEmbeddedRecognitionOnlyForDebug()))) {
            startListening(paramListener, paramQuery, false);
            return;
        }
        this.mGrammarCompilationCallback = new GrammarCompilationCallback(paramListener, paramQuery);
        OfflineActionsManager localOfflineActionsManager = this.mVss.getOfflineActionsManager();
        GrammarCompilationCallback localGrammarCompilationCallback = this.mGrammarCompilationCallback;
        String str = this.mVss.getSettings().getSpokenLocaleBcp47();
        Greco3Grammar[] arrayOfGreco3Grammar = new Greco3Grammar[2];
        arrayOfGreco3Grammar[0] = Greco3Grammar.CONTACT_DIALING;
        arrayOfGreco3Grammar[1] = Greco3Grammar.HANDS_FREE_COMMANDS;
        localOfflineActionsManager.startOfflineDataCheck(localGrammarCompilationCallback, str, arrayOfGreco3Grammar);
        paramListener.onInitializing();
    }

    public void cancel(boolean paramBoolean1, boolean paramBoolean2) {
        this.mThreadCheck.check();
        if (this.mRecognitionInProgress) {
            EventLogger.recordClientEvent(18);
            cancelInternal(paramBoolean1, paramBoolean2);
        }
    }

    protected S3FetchTask createFetchTask() {
        SearchConfig localSearchConfig = this.mVss.getSearchConfig();
        return new S3FetchTask(this.mVss.getExecutorService(), localSearchConfig.getMaxGwsResponseSizeBytes(), localSearchConfig.getSuggestionPelletPath(), this.mSearchUrlHelper);
    }

    public void start(Query paramQuery, Listener paramListener) {
        this.mThreadCheck.check();
        if (this.mRecognitionInProgress) {
            Log.i("VoiceSearchController", "Recognition already in progress!");
            cancelInternal(false, true);
        }
        if (paramQuery.shouldResendLastRecording()) {
            resendVoiceSearch(paramQuery, paramListener);
            return;
        }
        startNewVoiceSearch(paramQuery, paramListener);
    }

    public void stopListening() {
        this.mThreadCheck.check();
        if (this.mRecognitionInProgress) {
            EventLogger.recordClientEvent(17);
            this.mVss.getRecognizer().stopListening(this.mEventListener);
        }
    }

    public static abstract interface Listener {
        public abstract void onDone();

        public abstract void onError(RecognizeException paramRecognizeException, @Nullable String paramString);

        public abstract void onInitializing();

        public abstract void onMusicDetected();

        public abstract void onNoMatch(NoMatchRecognizeException paramNoMatchRecognizeException, String paramString);

        public abstract void onNoSpeechDetected();

        public abstract void onReadyForSpeech();

        public abstract void onRecognitionResult(CharSequence paramCharSequence, ImmutableList<CharSequence> paramImmutableList, @Nullable SearchResult paramSearchResult);

        public abstract void onRecognizing();

        public abstract void onSpeechDetected();

        public abstract void onTtsAvailable(byte[] paramArrayOfByte);

        public abstract void setFinalRecognizedText(@Nonnull CharSequence paramCharSequence);

        public abstract void updateRecognizedText(String paramString1, String paramString2);
    }

    private class GrammarCompilationCallback
            implements SimpleCallback<Integer> {
        private final VoiceSearchController.Listener mListener;
        private final Query mQuery;

        public GrammarCompilationCallback(VoiceSearchController.Listener paramListener, Query paramQuery) {
            this.mListener = paramListener;
            this.mQuery = paramQuery;
        }

        private void reportError(RecognizeException paramRecognizeException) {
            VoiceSearchController.this.cancelInternal(true, true);
            this.mListener.onError(paramRecognizeException, null);
        }

        public void onResult(Integer result) {
            mThreadCheck.check();
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
        private final VoiceSearchController.Listener mListener;
        private final Query mQuery;
        private final RecognizedText mRecognizedText = new RecognizedText();
        private final String mRequestId;

        public InternalRecognitionEventListener(String paramString, VoiceSearchController.Listener paramListener, Query paramQuery) {
            this.mRequestId = paramString;
            this.mListener = paramListener;
            this.mQuery = paramQuery;
        }

        private void dispatchNoMatchException() {
            TestPlatformLog.logError("no_match");
            this.mListener.onNoMatch(new NoMatchRecognizeException(), this.mRequestId);
        }

        private boolean hasCompletedRecognition() {
            return this.mRecognizedText.hasCompletedRecognition();
        }

        public void onBeginningOfSpeech(long paramLong) {
            VoiceSearchController.this.mThreadCheck.check();
            this.mListener.onSpeechDetected();
        }

        public void onDone() {
            mThreadCheck.check();
            if (!hasCompletedRecognition()) {
                dispatchNoMatchException();
            }
            if ((mProxyFetchTask != null) && (!mProxyFetchTask.isFailedOrComplete())) {
                Log.e("VoiceSearchController", "Incomplete proxy task: " + mProxyFetchTask);
                mProxyFetchTask.cancel();
                mProxyFetchTask = null;
            }
            TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
            mListener.onDone();
        }

        public void onEndOfSpeech() {
            VoiceSearchController.this.mThreadCheck.check();
            this.mListener.onRecognizing();
        }

        public void onError(RecognizeException paramRecognizeException) {
            VoiceSearchController.this.mThreadCheck.check();
            VoiceSearchController.maybeLogException(paramRecognizeException);
            TestPlatformLog.logError(paramRecognizeException.toString());
            if (!hasCompletedRecognition()) {
                VoiceSearchController.this.mVss.getSoundManager().playErrorSound();
            }
            if (VoiceSearchController.this.mProxyFetchTask != null) {
                VoiceSearchController.this.mProxyFetchTask.reportError(paramRecognizeException);
            }
            String str = this.mRecognizedText.getStableForErrorReporting();
            if (!TextUtils.isEmpty(str)) {
                Log.e("VoiceSearchController", "Got error after recognizing [" + str + "]");
            }
            VoiceSearchController.this.cancelInternal(false, false);
            this.mListener.onError(paramRecognizeException, this.mRequestId);
        }

        public void onMajelResult(MajelProtos.MajelResponse paramMajelResponse) {
            VoiceSearchController.this.mThreadCheck.check();
            if (PhoneActionUtils.isPhoneActionFromEmbeddedRecognizer(paramMajelResponse)) {
                return;
            }
            Log.w("VoiceSearchController", "Unexpected majel response in stream.");
        }

        public void onMediaDataResult(byte[] paramArrayOfByte) {
            VoiceSearchController.this.mThreadCheck.check();
            this.mListener.onTtsAvailable(paramArrayOfByte);
        }

        public void onMusicDetected() {
            VoiceSearchController.this.mThreadCheck.check();
            this.mListener.onMusicDetected();
        }

        public void onNoSpeechDetected() {
            VoiceSearchController.this.mThreadCheck.check();
            VoiceSearchController.this.cancelInternal(true, true);
            this.mListener.onNoSpeechDetected();
            TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
        }

        public void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput) {
            VoiceSearchController.this.mThreadCheck.check();
            if (VoiceSearchController.this.mProxyFetchTask != null) {
                VoiceSearchController.this.mProxyFetchTask.offerPinholeResult(paramPinholeOutput);
            }
        }

        public void onReadyForSpeech() {
            VoiceSearchController.this.mThreadCheck.check();
            TestPlatformLog.log("SPEAK_NOW");
            EventLogger.recordClientEvent(5);
            this.mListener.onReadyForSpeech();
        }

        public void onRecognitionCancelled() {
            VoiceSearchController.this.mThreadCheck.check();
            VoiceSearchController localVoiceSearchController = VoiceSearchController.this;
            if (!hasCompletedRecognition()) {
            }
            for (boolean bool = true; ; bool = false) {
                localVoiceSearchController.cancelInternal(bool, true);
                return;
            }
        }

        public void onRecognitionResult(RecognizerProtos.RecognitionEvent recognitionEvent) {
            mThreadCheck.check();
            TestPlatformLog.logResults(recognitionEvent);
            if (mRecognizedText.hasCompletedRecognition()) {
                Log.e("VoiceSearchController", "Result after completed recognition.");
                return;
            }
            if (recognitionEvent.getEventType() == 0) {
                Pair<String, String> stableAndUnstable = mRecognizedText.updateInProgress(recognitionEvent);
                String stable = (String) stableAndUnstable.first;
                String unstable = (String) stableAndUnstable.second;
                mListener.updateRecognizedText(stable, unstable);
                return;
            }
            if (recognitionEvent.getEventType() == 0x1) {
                ImmutableList<Hypothesis> allHypotheses = mRecognizedText.updateFinal(recognitionEvent);
                !TextUtils.isEmpty((Hypothesis) allHypotheses.get(0x0).getText()) ?;
                boolean isEmpty = allHypotheses.isEmpty() ? true : false;
                if ((!isEmpty) && (!mQuery.isFollowOn())) {
                    mVss.getSoundManager().playRecognitionDoneSound();
                    if (isEmpty) {
                        Log.i("VoiceSearchController", "Empty combined result");
                        dispatchNoMatchException();
                        return;
                    }
                    boolean isVoiceCorrectionEnabled = mGsaConfigFlags.isVoiceCorrectionEnabled();
                    HypothesisToSuggestionSpansConverter converter = mVss.getHypothesisToSuggestionSpansConverter();
                    SpannedString firstResult = isVoiceCorrectionEnabled ? SpannedString.valueOf((Hypothesis) allHypotheses.get(0x0).getText()) : converter.getSuggestionSpannedStringForQuery(mRequestId, (Hypothesis) allHypotheses.get(0x0));
                    mListener.setFinalRecognizedText(firstResult);
                    ImmutableList.Builder<CharSequence> otherHypothesesBuilder = ImmutableList.builder();
                    for (int i = 0x1; i < allHypotheses.size(); i = i + 0x1) {
                        Hypothesis hypothesis = (Hypothesis) allHypotheses.get(i);
                        otherHypothesesBuilder.add(hypothesis.getText());
                    }
                    ImmutableList<CharSequence> otherHypotheses = otherHypothesesBuilder.build();
                    Preconditions.checkNotNull(mQuery);
                    mProxyFetchTask = createFetchTask();
                    Query q = mQuery.withRecognizedText(firstResult, otherHypotheses, isVoiceCorrectionEnabled);
                    SearchResult proxiedResult = SearchResult.forSrp(q, mClock.elapsedRealtime(), mRequestId, mProxyFetchTask, mVss.getMainThreadExecutor());
                    proxiedResult.startFetch();
                    mListener.onRecognitionResult(firstResult, otherHypotheses, proxiedResult);
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.VoiceSearchController

 * JD-Core Version:    0.7.0.1

 */