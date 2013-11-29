package com.google.android.voicesearch.fragments;

import android.net.Uri;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.prefetch.S3FetchTask;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.RecognitionUi;
import com.google.android.speech.Recognizer;
import com.google.android.speech.alternates.Hypothesis;
import com.google.android.speech.alternates.HypothesisToSuggestionSpansConverter;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.embedded.Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException;
import com.google.android.speech.embedded.OfflineActionsManager;
import com.google.android.speech.embedded.OfflineActionsManager.GrammarCompilationException;
import com.google.android.speech.exception.NetworkRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.CancellableRecognitionEventListener;
import com.google.android.speech.listeners.CompositeRecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.params.AudioInputParams.Builder;
import com.google.android.speech.params.SessionParams;
import com.google.android.speech.params.SessionParams.Builder;
import com.google.android.speech.test.TestPlatformLog;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.speech.utils.RecognizedText;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import com.google.speech.s3.PinholeStream.PinholeOutput;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HandsFreeRecognizerController
{
  private static final RecognitionUi NO_OP_UI = new RecognitionUi()
  {
    public void setFinalRecognizedText(@Nonnull CharSequence paramAnonymousCharSequence) {}
    
    public void showRecognitionState(int paramAnonymousInt) {}
    
    public void updateRecognizedText(String paramAnonymousString1, String paramAnonymousString2) {}
  };
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
  private final VoiceSearchServices mVoiceSearchServices;
  
  HandsFreeRecognizerController(VoiceSearchServices paramVoiceSearchServices)
  {
    this.mVoiceSearchServices = paramVoiceSearchServices;
    this.mUi = NO_OP_UI;
  }
  
  private void cancelInternal(boolean paramBoolean)
  {
    if (this.mRecognitionInProgress)
    {
      TestPlatformLog.logError("no_match");
      if (paramBoolean) {
        this.mEventListener.onRecognitionCancelled();
      }
      this.mRecognizer.cancel(this.mEventListener);
      this.mRecognitionInProgress = false;
    }
    if (this.mEventListener != null)
    {
      this.mEventListener.invalidate();
      this.mEventListener = null;
    }
    if ((this.mOfflineActionsManager != null) && (this.mGrammarCompilationCallback != null))
    {
      this.mOfflineActionsManager.detach(this.mGrammarCompilationCallback);
      this.mGrammarCompilationCallback = null;
    }
  }
  
  public static HandsFreeRecognizerController createForVoiceDialer(VoiceSearchServices paramVoiceSearchServices)
  {
    return new HandsFreeRecognizerController(paramVoiceSearchServices);
  }
  
  private Greco3Grammar getGrammarType(int paramInt)
  {
    if (paramInt == 5) {
      return Greco3Grammar.HANDS_FREE_COMMANDS;
    }
    return Greco3Grammar.CONTACT_DIALING;
  }
  
  private Greco3Mode getGreco3Mode(int paramInt)
  {
    if ((paramInt == 2) || (paramInt == 5) || (paramInt == 4)) {
      return Greco3Mode.GRAMMAR;
    }
    return Greco3Mode.ENDPOINTER_VOICESEARCH;
  }
  
  private SessionParams.Builder getSessionParamsBuilder(int paramInt, boolean paramBoolean, @Nullable Uri paramUri)
  {
    AudioInputParams.Builder localBuilder = new AudioInputParams.Builder();
    if (isFlagSet(4)) {
      localBuilder.setPlayBeepEnabled(false);
    }
    localBuilder.setRecordedAudioUri(paramUri);
    SessionParams.Builder localBuilder1 = new SessionParams.Builder();
    Settings localSettings = this.mVoiceSearchServices.getSettings();
    localBuilder1.setSpokenBcp47Locale(localSettings.getSpokenLocaleBcp47()).setGreco3Grammar(getGrammarType(paramInt)).setGreco3Mode(getGreco3Mode(paramInt)).setResendingAudio(paramBoolean).setMode(paramInt).setProfanityFilterEnabled(localSettings.isProfanityFilterEnabled()).setAudioInputParams(localBuilder.build());
    if (paramInt == 2)
    {
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
  
  private boolean isFlagSet(int paramInt)
  {
    return (paramInt & this.mFlags) != 0;
  }
  
  private void maybeInit()
  {
    if (this.mRecognizer == null)
    {
      this.mRecognizer = this.mVoiceSearchServices.getRecognizer();
      this.mUiThreadExecutor = this.mVoiceSearchServices.getMainThreadExecutor();
      this.mSoundManager = this.mVoiceSearchServices.getSoundManager();
      this.mOfflineActionsManager = this.mVoiceSearchServices.getOfflineActionsManager();
      this.mNetworkInformation = this.mVoiceSearchServices.getNetworkInformation();
    }
  }
  
  private static void maybeLogException(RecognizeException paramRecognizeException)
  {
    if ((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException))
    {
      Log.i("HandsFreeRecognizerController", "No recognizers available.");
      return;
    }
    Log.e("HandsFreeRecognizerController", "onError", paramRecognizeException);
  }
  
  private void prepareRecognition(SessionParams paramSessionParams, RecognitionEventListener paramRecognitionEventListener, String paramString)
  {
    cancelInternal(true);
    this.mMode = paramSessionParams.getMode();
    this.mRecognitionInProgress = true;
    CompositeRecognitionEventListener localCompositeRecognitionEventListener;
    if (paramRecognitionEventListener != null)
    {
      localCompositeRecognitionEventListener = new CompositeRecognitionEventListener();
      localCompositeRecognitionEventListener.add(new InternalRecognitionEventListener(paramString));
      localCompositeRecognitionEventListener.add(paramRecognitionEventListener);
    }
    for (Object localObject = localCompositeRecognitionEventListener;; localObject = new InternalRecognitionEventListener(paramString))
    {
      this.mEventListener = new CancellableRecognitionEventListener((RecognitionEventListener)localObject);
      return;
    }
  }
  
  private void reallyStartListening(SessionParams paramSessionParams)
  {
    this.mRecognizer.startListening(paramSessionParams, this.mEventListener, this.mUiThreadExecutor, this.mVoiceSearchServices.getVoiceSearchAudioStore());
  }
  
  private void startEmbeddedRecognitionInternal(int paramInt, RecognitionEventListener paramRecognitionEventListener)
  {
    startListening(getSessionParamsBuilder(paramInt, false, null).build(), paramRecognitionEventListener, true);
  }
  
  private void startListening(final SessionParams paramSessionParams, RecognitionEventListener paramRecognitionEventListener, boolean paramBoolean)
  {
    prepareRecognition(paramSessionParams, paramRecognitionEventListener, paramSessionParams.getRequestId());
    if (isFlagSet(1)) {
      this.mVoiceSearchServices.getLocalTtsManager().enqueue(this.mSpeakPrompt, new Runnable()
      {
        public void run()
        {
          HandsFreeRecognizerController.this.reallyStartListening(paramSessionParams);
        }
      });
    }
    for (;;)
    {
      if (!paramBoolean) {
        this.mUi.showRecognitionState(3);
      }
      return;
      reallyStartListening(paramSessionParams);
    }
  }
  
  private void startOfflineRecognition(RecognitionEventListener paramRecognitionEventListener, int paramInt)
  {
    this.mGrammarCompilationCallback = new GrammarCompilationCallback(paramRecognitionEventListener, paramInt);
    if (SessionParams.isVoiceDialerSearch(paramInt)) {}
    for (String str = "en-US";; str = this.mVoiceSearchServices.getSettings().getSpokenLocaleBcp47())
    {
      OfflineActionsManager localOfflineActionsManager = this.mOfflineActionsManager;
      GrammarCompilationCallback localGrammarCompilationCallback = this.mGrammarCompilationCallback;
      Greco3Grammar[] arrayOfGreco3Grammar = new Greco3Grammar[2];
      arrayOfGreco3Grammar[0] = Greco3Grammar.CONTACT_DIALING;
      arrayOfGreco3Grammar[1] = Greco3Grammar.HANDS_FREE_COMMANDS;
      localOfflineActionsManager.startOfflineDataCheck(localGrammarCompilationCallback, str, arrayOfGreco3Grammar);
      this.mUi.showRecognitionState(1);
      return;
    }
  }
  
  private void startRecognition(@Nullable RecognitionEventListener paramRecognitionEventListener, int paramInt, @Nonnull Query paramQuery)
  {
    maybeInit();
    this.mFlags = 0;
    this.mSpeakPrompt = null;
    if ((!this.mVoiceSearchServices.getSettings().isNetworkRecognitionOnlyForDebug()) && ((!this.mNetworkInformation.isConnected()) || (this.mVoiceSearchServices.getSettings().isEmbeddedRecognitionOnlyForDebug())))
    {
      startOfflineRecognition(paramRecognitionEventListener, paramInt);
      return;
    }
    startListening(getSessionParamsBuilder(paramInt, false, paramQuery.getRecordedAudioUri()).build(), paramRecognitionEventListener, false);
  }
  
  public void attachUi(RecognitionUi paramRecognitionUi)
  {
    if (this.mUi == NO_OP_UI) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mUi = ((RecognitionUi)Preconditions.checkNotNull(paramRecognitionUi));
      return;
    }
  }
  
  public void cancel()
  {
    if (this.mRecognitionInProgress) {
      EventLogger.recordClientEvent(18);
    }
    cancelInternal(true);
  }
  
  public void detachUi(RecognitionUi paramRecognitionUi)
  {
    if ((paramRecognitionUi == this.mUi) || (this.mUi == NO_OP_UI)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mUi = NO_OP_UI;
      return;
    }
  }
  
  public void startCommandRecognitionNoUi(@Nullable RecognitionEventListener paramRecognitionEventListener, int paramInt, String paramString)
  {
    maybeInit();
    this.mFlags = paramInt;
    this.mSpeakPrompt = paramString;
    detachUi(this.mUi);
    startOfflineRecognition(paramRecognitionEventListener, 5);
  }
  
  public void startHandsFreeContactRecognition(@Nonnull RecognitionEventListener paramRecognitionEventListener)
  {
    startRecognition((RecognitionEventListener)Preconditions.checkNotNull(paramRecognitionEventListener), 4, Query.EMPTY);
  }
  
  private class GrammarCompilationCallback
    implements SimpleCallback<Integer>
  {
    private final int mRecognizerMode;
    private final RecognitionEventListener mResultsListener;
    
    public GrammarCompilationCallback(RecognitionEventListener paramRecognitionEventListener, int paramInt)
    {
      this.mResultsListener = paramRecognitionEventListener;
      this.mRecognizerMode = paramInt;
    }
    
    private void reportError(RecognizeException paramRecognizeException)
    {
      if (this.mResultsListener != null) {
        this.mResultsListener.onError(paramRecognizeException);
      }
    }
    
    public void onResult(Integer paramInteger)
    {
      if (paramInteger.intValue() == 1) {
        HandsFreeRecognizerController.this.startEmbeddedRecognitionInternal(this.mRecognizerMode, this.mResultsListener);
      }
      do
      {
        return;
        if (paramInteger.intValue() == 4)
        {
          reportError(new OfflineActionsManager.GrammarCompilationException());
          return;
        }
      } while (paramInteger.intValue() != 3);
      reportError(new NetworkRecognizeException("No network connection"));
    }
  }
  
  private class InternalRecognitionEventListener
    extends RecognitionEventListenerAdapter
  {
    private S3FetchTask mProxyFetchTask;
    private final RecognizedText mRecognizedText = new RecognizedText();
    private String mRequestId;
    
    public InternalRecognitionEventListener(String paramString)
    {
      this.mRequestId = paramString;
    }
    
    private void dispatchNoMatchException()
    {
      TestPlatformLog.logError("no_match");
    }
    
    private boolean hasCompletedRecognition()
    {
      return this.mRecognizedText.hasCompletedRecognition();
    }
    
    public void onBeginningOfSpeech(long paramLong)
    {
      HandsFreeRecognizerController.this.mUi.showRecognitionState(5);
    }
    
    public void onDone()
    {
      if (!hasCompletedRecognition())
      {
        if (HandsFreeRecognizerController.this.mMode == 2) {
          dispatchNoMatchException();
        }
        if (!HandsFreeRecognizerController.this.isFlagSet(4)) {
          HandsFreeRecognizerController.this.mSoundManager.playNoInputSound();
        }
      }
      if ((this.mProxyFetchTask != null) && (!this.mProxyFetchTask.isFailedOrComplete()))
      {
        Log.e("HandsFreeRecognizerController", "Incomplete proxy task: " + this.mProxyFetchTask);
        this.mProxyFetchTask.cancel();
      }
      TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
      HandsFreeRecognizerController.this.cancelInternal(false);
    }
    
    public void onEndOfSpeech()
    {
      HandsFreeRecognizerController.this.mUi.showRecognitionState(6);
    }
    
    public void onError(RecognizeException paramRecognizeException)
    {
      HandsFreeRecognizerController.maybeLogException(paramRecognizeException);
      TestPlatformLog.logError(paramRecognizeException.toString());
      if ((!HandsFreeRecognizerController.this.isFlagSet(4)) && (!hasCompletedRecognition())) {
        HandsFreeRecognizerController.this.mSoundManager.playErrorSound();
      }
      if (this.mProxyFetchTask != null) {
        this.mProxyFetchTask.reportError(paramRecognizeException);
      }
      if (HandsFreeRecognizerController.this.mMode == 2)
      {
        HandsFreeRecognizerController.this.mEventListener.invalidate();
        String str = this.mRecognizedText.getStableForErrorReporting();
        Log.e("HandsFreeRecognizerController", "Got error after recognizing [" + str + "]");
      }
      HandsFreeRecognizerController.this.cancelInternal(false);
    }
    
    public void onMajelResult(MajelProtos.MajelResponse paramMajelResponse)
    {
      if (PhoneActionUtils.isPhoneActionFromEmbeddedRecognizer(paramMajelResponse)) {
        return;
      }
      Log.w("HandsFreeRecognizerController", "Unexpected majel response in stream.");
    }
    
    public void onMediaDataResult(byte[] paramArrayOfByte) {}
    
    public void onMusicDetected() {}
    
    public void onNoSpeechDetected()
    {
      HandsFreeRecognizerController.this.mUi.showRecognitionState(2);
      HandsFreeRecognizerController.this.cancelInternal(false);
      TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
    }
    
    public void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput)
    {
      if (this.mProxyFetchTask != null) {
        this.mProxyFetchTask.offerPinholeResult(paramPinholeOutput);
      }
    }
    
    public void onReadyForSpeech()
    {
      TestPlatformLog.log("SPEAK_NOW");
      EventLogger.recordClientEvent(5);
      HandsFreeRecognizerController.this.mUi.showRecognitionState(4);
    }
    
    public void onRecognitionCancelled()
    {
      if ((!HandsFreeRecognizerController.this.isFlagSet(4)) && (!hasCompletedRecognition())) {
        HandsFreeRecognizerController.this.mSoundManager.playNoInputSound();
      }
      if (this.mProxyFetchTask != null) {
        this.mProxyFetchTask.cancel();
      }
    }
    
    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
    {
      int i = 1;
      TestPlatformLog.logResults(paramRecognitionEvent);
      if (this.mRecognizedText.hasCompletedRecognition()) {
        Log.e("HandsFreeRecognizerController", "Result after completed recognition.");
      }
      do
      {
        return;
        if (paramRecognitionEvent.getEventType() == 0)
        {
          Pair localPair = this.mRecognizedText.updateInProgress(paramRecognitionEvent);
          String str1 = (String)localPair.first;
          String str2 = (String)localPair.second;
          HandsFreeRecognizerController.this.mUi.updateRecognizedText(str1, str2);
          return;
        }
      } while (paramRecognitionEvent.getEventType() != i);
      ImmutableList localImmutableList = this.mRecognizedText.updateFinal(paramRecognitionEvent);
      if ((localImmutableList.isEmpty()) || (TextUtils.isEmpty(((Hypothesis)localImmutableList.get(0)).getText()))) {}
      for (;;)
      {
        if ((!HandsFreeRecognizerController.this.isFlagSet(4)) && (i != 0)) {
          HandsFreeRecognizerController.this.mSoundManager.playNoInputSound();
        }
        if (i == 0) {
          break;
        }
        Log.i("HandsFreeRecognizerController", "Empty combined result");
        dispatchNoMatchException();
        return;
        i = 0;
      }
      SpannedString localSpannedString = HandsFreeRecognizerController.this.mVoiceSearchServices.getHypothesisToSuggestionSpansConverter().getSuggestionSpannedStringForQuery(this.mRequestId, (Hypothesis)localImmutableList.get(0));
      HandsFreeRecognizerController.this.mUi.setFinalRecognizedText(localSpannedString);
      ImmutableList.Builder localBuilder = ImmutableList.builder();
      for (int j = 1; j < localImmutableList.size(); j++) {
        localBuilder.add(((Hypothesis)localImmutableList.get(j)).getText());
      }
      localBuilder.build();
      this.mProxyFetchTask = null;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.HandsFreeRecognizerController
 * JD-Core Version:    0.7.0.1
 */