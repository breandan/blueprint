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
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.android.speech.Recognizer;
import com.google.android.speech.alternates.Hypothesis;
import com.google.android.speech.alternates.HypothesisToSuggestionSpansConverter;
import com.google.android.speech.audio.AudioStore;
import com.google.android.speech.audio.AudioStore.AudioRecording;
import com.google.android.speech.audio.AudioUtils;
import com.google.android.speech.audio.AudioUtils.Encoding;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.embedded.Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException;
import com.google.android.speech.embedded.OfflineActionsManager;
import com.google.android.speech.embedded.OfflineActionsManager.GrammarCompilationException;
import com.google.android.speech.exception.NetworkRecognizeException;
import com.google.android.speech.exception.NoMatchRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.CancellableRecognitionEventListener;
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
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import com.google.speech.s3.PinholeStream.PinholeOutput;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VoiceSearchController
{
  private final Clock mClock;
  @Nullable
  private CancellableRecognitionEventListener mEventListener;
  @Nullable
  private GrammarCompilationCallback mGrammarCompilationCallback;
  private final GsaConfigFlags mGsaConfigFlags;
  private S3FetchTask mProxyFetchTask;
  private boolean mRecognitionInProgress;
  private final SearchUrlHelper mSearchUrlHelper;
  private final ExtraPreconditions.ThreadCheck mThreadCheck;
  private final VoiceSearchServices mVss;
  
  public VoiceSearchController(VoiceSearchServices paramVoiceSearchServices, Clock paramClock, SearchUrlHelper paramSearchUrlHelper, GsaConfigFlags paramGsaConfigFlags)
  {
    this.mVss = paramVoiceSearchServices;
    this.mClock = paramClock;
    this.mSearchUrlHelper = paramSearchUrlHelper;
    this.mThreadCheck = ExtraPreconditions.createSameThreadCheck();
    this.mGsaConfigFlags = paramGsaConfigFlags;
  }
  
  private void cancelInternal(boolean paramBoolean1, boolean paramBoolean2)
  {
    CancellableRecognitionEventListener localCancellableRecognitionEventListener = this.mEventListener;
    if (this.mEventListener != null)
    {
      this.mEventListener.invalidate();
      this.mEventListener = null;
    }
    if ((this.mProxyFetchTask != null) && (paramBoolean2)) {
      this.mProxyFetchTask.cancel();
    }
    this.mProxyFetchTask = null;
    if (this.mGrammarCompilationCallback != null)
    {
      this.mVss.getOfflineActionsManager().detach(this.mGrammarCompilationCallback);
      this.mGrammarCompilationCallback = null;
    }
    if (this.mRecognitionInProgress)
    {
      TestPlatformLog.logError("no_match");
      if (paramBoolean1) {
        this.mVss.getSoundManager().playNoInputSound();
      }
      this.mVss.getRecognizer().cancel(localCancellableRecognitionEventListener);
      this.mRecognitionInProgress = false;
    }
  }
  
  private SessionParams getSessionParams(@Nullable AudioStore.AudioRecording paramAudioRecording, boolean paramBoolean, @Nullable Uri paramUri)
  {
    AudioInputParams.Builder localBuilder = new AudioInputParams.Builder();
    SessionParams.Builder localBuilder1;
    Settings localSettings;
    SessionParams.Builder localBuilder2;
    if (paramAudioRecording != null)
    {
      localBuilder.setSamplingRate(paramAudioRecording.getSampleRate());
      localBuilder.setEncoding(AudioUtils.getAmrEncodingForRecording(paramAudioRecording).getRecognizerEncoding());
      localBuilder1 = new SessionParams.Builder();
      localSettings = this.mVss.getSettings();
      localBuilder2 = localBuilder1.setSpokenBcp47Locale(localSettings.getSpokenLocaleBcp47()).setGreco3Grammar(Greco3Grammar.CONTACT_DIALING).setGreco3Mode(Greco3Mode.GRAMMAR);
      if (paramAudioRecording == null) {
        break label224;
      }
    }
    label224:
    for (boolean bool = true;; bool = false)
    {
      localBuilder2.setResendingAudio(bool).setMode(2).setProfanityFilterEnabled(localSettings.isProfanityFilterEnabled()).setAudioInputParams(localBuilder.build()).setServerEndpointingEnabled(this.mVss.getGsaConfigFlags().isServerEndpointingEnabled());
      String str = this.mVss.getSearchConfig().getVoiceActionsS3ServiceOverride();
      if (!TextUtils.isEmpty(str)) {
        localBuilder1.setServiceOverride(str);
      }
      return localBuilder1.build();
      if (paramUri != null)
      {
        localBuilder.setRecordedAudioUri(paramUri);
        break;
      }
      if ((!this.mVss.getGsaConfigFlags().shouldUseAmrWbEncoding()) || (this.mVss.getSettings().isBluetoothHeadsetEnabled()) || (paramBoolean)) {
        break;
      }
      localBuilder.setEncoding(9);
      localBuilder.setSamplingRate(16000);
      break;
    }
  }
  
  private static void maybeLogException(RecognizeException paramRecognizeException)
  {
    if ((paramRecognizeException instanceof Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException))
    {
      Log.i("VoiceSearchController", "No recognizers available.");
      return;
    }
    Log.e("VoiceSearchController", "onError", paramRecognizeException);
  }
  
  private void resendVoiceSearch(Query paramQuery, Listener paramListener)
  {
    AudioStore.AudioRecording localAudioRecording = this.mVss.getVoiceSearchAudioStore().getLastAudio();
    if (localAudioRecording == null)
    {
      startNewVoiceSearch(paramQuery, paramListener);
      return;
    }
    SessionParams localSessionParams = getSessionParams(localAudioRecording, paramQuery.isTriggeredFromBluetoothHandsfree(), null);
    this.mRecognitionInProgress = true;
    this.mEventListener = new CancellableRecognitionEventListener(new InternalRecognitionEventListener(localSessionParams.getRequestId(), paramListener, paramQuery));
    paramListener.onRecognizing();
    this.mVss.getRecognizer().startRecordedAudioRecognition(localSessionParams, localAudioRecording.getAudio(), this.mEventListener, this.mVss.getMainThreadExecutor());
  }
  
  private void startListening(Listener paramListener, Query paramQuery, boolean paramBoolean)
  {
    SessionParams localSessionParams = getSessionParams(null, paramQuery.isTriggeredFromBluetoothHandsfree(), paramQuery.getRecordedAudioUri());
    this.mEventListener = new CancellableRecognitionEventListener(new InternalRecognitionEventListener(localSessionParams.getRequestId(), paramListener, paramQuery));
    this.mVss.getRecognizer().startListening(localSessionParams, this.mEventListener, this.mVss.getMainThreadExecutor(), this.mVss.getVoiceSearchAudioStore());
    if (!paramBoolean) {
      paramListener.onInitializing();
    }
  }
  
  private void startNewVoiceSearch(Query paramQuery, Listener paramListener)
  {
    this.mRecognitionInProgress = true;
    if ((this.mVss.getSettings().isNetworkRecognitionOnlyForDebug()) || ((this.mVss.getNetworkInformation().isConnected()) && (!this.mVss.getSettings().isEmbeddedRecognitionOnlyForDebug())))
    {
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
  
  public void cancel(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mThreadCheck.check();
    if (this.mRecognitionInProgress)
    {
      EventLogger.recordClientEvent(18);
      cancelInternal(paramBoolean1, paramBoolean2);
    }
  }
  
  protected S3FetchTask createFetchTask()
  {
    SearchConfig localSearchConfig = this.mVss.getSearchConfig();
    return new S3FetchTask(this.mVss.getExecutorService(), localSearchConfig.getMaxGwsResponseSizeBytes(), localSearchConfig.getSuggestionPelletPath(), this.mSearchUrlHelper);
  }
  
  public void start(Query paramQuery, Listener paramListener)
  {
    this.mThreadCheck.check();
    if (this.mRecognitionInProgress)
    {
      Log.i("VoiceSearchController", "Recognition already in progress!");
      cancelInternal(false, true);
    }
    if (paramQuery.shouldResendLastRecording())
    {
      resendVoiceSearch(paramQuery, paramListener);
      return;
    }
    startNewVoiceSearch(paramQuery, paramListener);
  }
  
  public void stopListening()
  {
    this.mThreadCheck.check();
    if (this.mRecognitionInProgress)
    {
      EventLogger.recordClientEvent(17);
      this.mVss.getRecognizer().stopListening(this.mEventListener);
    }
  }
  
  private class GrammarCompilationCallback
    implements SimpleCallback<Integer>
  {
    private final VoiceSearchController.Listener mListener;
    private final Query mQuery;
    
    public GrammarCompilationCallback(VoiceSearchController.Listener paramListener, Query paramQuery)
    {
      this.mListener = paramListener;
      this.mQuery = paramQuery;
    }
    
    private void reportError(RecognizeException paramRecognizeException)
    {
      VoiceSearchController.this.cancelInternal(true, true);
      this.mListener.onError(paramRecognizeException, null);
    }
    
    public void onResult(Integer paramInteger)
    {
      VoiceSearchController.this.mThreadCheck.check();
      if (paramInteger.intValue() == 1) {
        VoiceSearchController.this.startListening(this.mListener, this.mQuery, true);
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
    private final VoiceSearchController.Listener mListener;
    private final Query mQuery;
    private final RecognizedText mRecognizedText = new RecognizedText();
    private final String mRequestId;
    
    public InternalRecognitionEventListener(String paramString, VoiceSearchController.Listener paramListener, Query paramQuery)
    {
      this.mRequestId = paramString;
      this.mListener = paramListener;
      this.mQuery = paramQuery;
    }
    
    private void dispatchNoMatchException()
    {
      TestPlatformLog.logError("no_match");
      this.mListener.onNoMatch(new NoMatchRecognizeException(), this.mRequestId);
    }
    
    private boolean hasCompletedRecognition()
    {
      return this.mRecognizedText.hasCompletedRecognition();
    }
    
    public void onBeginningOfSpeech(long paramLong)
    {
      VoiceSearchController.this.mThreadCheck.check();
      this.mListener.onSpeechDetected();
    }
    
    public void onDone()
    {
      VoiceSearchController.this.mThreadCheck.check();
      if (!hasCompletedRecognition()) {
        dispatchNoMatchException();
      }
      if ((VoiceSearchController.this.mProxyFetchTask != null) && (!VoiceSearchController.this.mProxyFetchTask.isFailedOrComplete()))
      {
        Log.e("VoiceSearchController", "Incomplete proxy task: " + VoiceSearchController.this.mProxyFetchTask);
        VoiceSearchController.this.mProxyFetchTask.cancel();
        VoiceSearchController.access$502(VoiceSearchController.this, null);
      }
      TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
      VoiceSearchController localVoiceSearchController = VoiceSearchController.this;
      if (!hasCompletedRecognition()) {}
      for (boolean bool = true;; bool = false)
      {
        localVoiceSearchController.cancelInternal(bool, false);
        this.mListener.onDone();
        return;
      }
    }
    
    public void onEndOfSpeech()
    {
      VoiceSearchController.this.mThreadCheck.check();
      this.mListener.onRecognizing();
    }
    
    public void onError(RecognizeException paramRecognizeException)
    {
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
    
    public void onMajelResult(MajelProtos.MajelResponse paramMajelResponse)
    {
      VoiceSearchController.this.mThreadCheck.check();
      if (PhoneActionUtils.isPhoneActionFromEmbeddedRecognizer(paramMajelResponse)) {
        return;
      }
      Log.w("VoiceSearchController", "Unexpected majel response in stream.");
    }
    
    public void onMediaDataResult(byte[] paramArrayOfByte)
    {
      VoiceSearchController.this.mThreadCheck.check();
      this.mListener.onTtsAvailable(paramArrayOfByte);
    }
    
    public void onMusicDetected()
    {
      VoiceSearchController.this.mThreadCheck.check();
      this.mListener.onMusicDetected();
    }
    
    public void onNoSpeechDetected()
    {
      VoiceSearchController.this.mThreadCheck.check();
      VoiceSearchController.this.cancelInternal(true, true);
      this.mListener.onNoSpeechDetected();
      TestPlatformLog.log("VOICE_SEARCH_COMPLETE");
    }
    
    public void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput)
    {
      VoiceSearchController.this.mThreadCheck.check();
      if (VoiceSearchController.this.mProxyFetchTask != null) {
        VoiceSearchController.this.mProxyFetchTask.offerPinholeResult(paramPinholeOutput);
      }
    }
    
    public void onReadyForSpeech()
    {
      VoiceSearchController.this.mThreadCheck.check();
      TestPlatformLog.log("SPEAK_NOW");
      EventLogger.recordClientEvent(5);
      this.mListener.onReadyForSpeech();
    }
    
    public void onRecognitionCancelled()
    {
      VoiceSearchController.this.mThreadCheck.check();
      VoiceSearchController localVoiceSearchController = VoiceSearchController.this;
      if (!hasCompletedRecognition()) {}
      for (boolean bool = true;; bool = false)
      {
        localVoiceSearchController.cancelInternal(bool, true);
        return;
      }
    }
    
    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
    {
      VoiceSearchController.this.mThreadCheck.check();
      TestPlatformLog.logResults(paramRecognitionEvent);
      if (this.mRecognizedText.hasCompletedRecognition()) {
        Log.e("VoiceSearchController", "Result after completed recognition.");
      }
      do
      {
        return;
        if (paramRecognitionEvent.getEventType() == 0)
        {
          Pair localPair = this.mRecognizedText.updateInProgress(paramRecognitionEvent);
          String str1 = (String)localPair.first;
          String str2 = (String)localPair.second;
          this.mListener.updateRecognizedText(str1, str2);
          return;
        }
      } while (paramRecognitionEvent.getEventType() != 1);
      ImmutableList localImmutableList1 = this.mRecognizedText.updateFinal(paramRecognitionEvent);
      if ((localImmutableList1.isEmpty()) || (TextUtils.isEmpty(((Hypothesis)localImmutableList1.get(0)).getText()))) {}
      for (int i = 1;; i = 0)
      {
        if ((i == 0) && (!this.mQuery.isFollowOn())) {
          VoiceSearchController.this.mVss.getSoundManager().playRecognitionDoneSound();
        }
        if (i == 0) {
          break;
        }
        Log.i("VoiceSearchController", "Empty combined result");
        VoiceSearchController.this.cancelInternal(true, true);
        dispatchNoMatchException();
        return;
      }
      boolean bool = VoiceSearchController.this.mGsaConfigFlags.isVoiceCorrectionEnabled();
      HypothesisToSuggestionSpansConverter localHypothesisToSuggestionSpansConverter = VoiceSearchController.this.mVss.getHypothesisToSuggestionSpansConverter();
      if (bool) {}
      ImmutableList.Builder localBuilder;
      for (SpannedString localSpannedString = SpannedString.valueOf(((Hypothesis)localImmutableList1.get(0)).getText());; localSpannedString = localHypothesisToSuggestionSpansConverter.getSuggestionSpannedStringForQuery(this.mRequestId, (Hypothesis)localImmutableList1.get(0)))
      {
        this.mListener.setFinalRecognizedText(localSpannedString);
        localBuilder = ImmutableList.builder();
        for (int j = 1; j < localImmutableList1.size(); j++) {
          localBuilder.add(((Hypothesis)localImmutableList1.get(j)).getText());
        }
      }
      ImmutableList localImmutableList2 = localBuilder.build();
      Preconditions.checkNotNull(this.mQuery);
      VoiceSearchController.access$502(VoiceSearchController.this, VoiceSearchController.this.createFetchTask());
      SearchResult localSearchResult = SearchResult.forSrp(this.mQuery.withRecognizedText(localSpannedString, localImmutableList2, bool), VoiceSearchController.this.mClock.elapsedRealtime(), this.mRequestId, VoiceSearchController.this.mProxyFetchTask, VoiceSearchController.this.mVss.getMainThreadExecutor());
      localSearchResult.startFetch();
      this.mListener.onRecognitionResult(localSpannedString, localImmutableList2, localSearchResult);
    }
  }
  
  public static abstract interface Listener
  {
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
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.VoiceSearchController
 * JD-Core Version:    0.7.0.1
 */