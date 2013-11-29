package com.google.android.voicesearch.ime;

import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.inputmethodservice.InputMethodService;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import com.google.android.search.core.AsyncServices;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.speech.Recognizer;
import com.google.android.speech.alternates.Hypothesis;
import com.google.android.speech.alternates.HypothesisToSuggestionSpansConverter;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.RecognitionEventListenerAdapter;
import com.google.android.speech.params.SessionParams;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.audio.AudioTrackSoundManager;
import com.google.android.voicesearch.ime.formatter.LatinTextFormatter;
import com.google.android.voicesearch.ime.view.VoiceInputViewHandler;
import com.google.android.voicesearch.ime.view.VoiceInputViewHandler.Callback;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.logger.EventLoggerService;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.ErrorUtils;
import com.google.common.base.Preconditions;
import com.google.speech.common.Alternates.RecognitionClientAlternates;
import com.google.speech.recognizer.api.RecognizerProtos.Hypothesis;
import com.google.speech.recognizer.api.RecognizerProtos.PartialPart;
import com.google.speech.recognizer.api.RecognizerProtos.PartialResult;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionResult;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Dialect;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Dictation;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class VoiceInputMethodManager
{
  private boolean mBackToPrevImeOnDone = false;
  private final Runnable mBackToPreviousImeRunnable = new Runnable()
  {
    public void run()
    {
      VoiceInputMethodManager.this.backToPreviousIme();
    }
  };
  private final TemporaryData<Void> mContinueRecording = new TemporaryData(null);
  private final TemporaryData<String> mDictationBcp47Locale = new TemporaryData(null);
  private final DictationResultHandlerImpl mDictationResultHandler;
  private final TemporaryData<Void> mForcePauseOnStart = new TemporaryData(null);
  private final ImeLoggerHelper mImeLoggerHelper;
  private boolean mInputViewActive;
  private final Runnable mReleaseResourcesRunnable = new Runnable()
  {
    public void run()
    {
      VoiceInputMethodManager.this.maybeReleaseResources();
    }
  };
  private final ScreenStateMonitor mScreenStateMonitor;
  private final Settings mSettings;
  private final AudioTrackSoundManager mSoundManager;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  private final VoiceImeInputMethodService mVoiceImeInputMethodService;
  private final VoiceImeSubtypeUpdater mVoiceImeSubtypeUpdater;
  private final VoiceInputViewHandler mVoiceInputViewHandler;
  private final VoiceLanguageSelector mVoiceLanguageSelector;
  private final VoiceRecognitionHandler mVoiceRecognitionHandler;
  
  public VoiceInputMethodManager(VoiceLanguageSelector paramVoiceLanguageSelector, ScreenStateMonitor paramScreenStateMonitor, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, ImeLoggerHelper paramImeLoggerHelper, Settings paramSettings, AudioTrackSoundManager paramAudioTrackSoundManager, VoiceImeSubtypeUpdater paramVoiceImeSubtypeUpdater, VoiceInputViewHandler paramVoiceInputViewHandler, VoiceImeInputMethodService paramVoiceImeInputMethodService, VoiceRecognitionHandler paramVoiceRecognitionHandler, DictationResultHandlerImpl paramDictationResultHandlerImpl)
  {
    Log.i("VoiceInputMethodManager", "#()");
    this.mVoiceLanguageSelector = ((VoiceLanguageSelector)Preconditions.checkNotNull(paramVoiceLanguageSelector));
    this.mUiThreadExecutor = ((ScheduledSingleThreadedExecutor)Preconditions.checkNotNull(paramScheduledSingleThreadedExecutor));
    this.mImeLoggerHelper = ((ImeLoggerHelper)Preconditions.checkNotNull(paramImeLoggerHelper));
    this.mScreenStateMonitor = ((ScreenStateMonitor)Preconditions.checkNotNull(paramScreenStateMonitor));
    this.mSettings = ((Settings)Preconditions.checkNotNull(paramSettings));
    this.mSoundManager = ((AudioTrackSoundManager)Preconditions.checkNotNull(paramAudioTrackSoundManager));
    this.mVoiceImeSubtypeUpdater = ((VoiceImeSubtypeUpdater)Preconditions.checkNotNull(paramVoiceImeSubtypeUpdater));
    this.mVoiceInputViewHandler = ((VoiceInputViewHandler)Preconditions.checkNotNull(paramVoiceInputViewHandler));
    this.mVoiceImeInputMethodService = ((VoiceImeInputMethodService)Preconditions.checkNotNull(paramVoiceImeInputMethodService));
    this.mVoiceRecognitionHandler = paramVoiceRecognitionHandler;
    this.mDictationResultHandler = paramDictationResultHandlerImpl;
  }
  
  private void backToPreviousIme()
  {
    maybeReleaseResources();
    this.mVoiceImeInputMethodService.switchToLastInputMethod();
  }
  
  public static VoiceInputMethodManager create(InputMethodService paramInputMethodService)
  {
    AsyncServices localAsyncServices = VelvetServices.get().getAsyncServices();
    ImeLoggerHelper localImeLoggerHelper = new ImeLoggerHelper();
    final VoiceSearchServices localVoiceSearchServices = VelvetServices.get().getVoiceSearchServices();
    Settings localSettings = localVoiceSearchServices.getSettings();
    AudioTrackSoundManager localAudioTrackSoundManager = localVoiceSearchServices.getSoundManager();
    HypothesisToSuggestionSpansConverter localHypothesisToSuggestionSpansConverter = localVoiceSearchServices.getHypothesisToSuggestionSpansConverter();
    VoiceImeSubtypeUpdater localVoiceImeSubtypeUpdater = localVoiceSearchServices.getVoiceImeSubtypeUpdater();
    VoiceInputViewHandler localVoiceInputViewHandler = new VoiceInputViewHandler(paramInputMethodService, localVoiceSearchServices.getSpeechLevelSource());
    final PowerManager localPowerManager = (PowerManager)paramInputMethodService.getSystemService("power");
    ScreenStateMonitor localScreenStateMonitor = new ScreenStateMonitor(paramInputMethodService);
    VoiceLanguageSelector localVoiceLanguageSelector = new VoiceLanguageSelector(paramInputMethodService, localSettings);
    VoiceImeInputMethodService local3 = new VoiceImeInputMethodService()
    {
      public InputConnection getCurrentInputConnection()
      {
        return this.val$inputMethodService.getCurrentInputConnection();
      }
      
      public EditorInfo getCurrentInputEditorInfo()
      {
        return this.val$inputMethodService.getCurrentInputEditorInfo();
      }
      
      public Recognizer getRecognizer()
      {
        return localVoiceSearchServices.getRecognizer();
      }
      
      public Resources getResources()
      {
        return this.val$inputMethodService.getResources();
      }
      
      public boolean isScreenOn()
      {
        return localPowerManager.isScreenOn();
      }
      
      public void scheduleSendEvents()
      {
        EventLoggerService.scheduleSendEvents(this.val$inputMethodService);
      }
      
      public void switchToLastInputMethod()
      {
        ((InputMethodManager)this.val$inputMethodService.getSystemService("input_method")).switchToLastInputMethod(this.val$inputMethodService.getWindow().getWindow().getAttributes().token);
      }
    };
    VoiceRecognitionHandler localVoiceRecognitionHandler = new VoiceRecognitionHandler(local3, localAsyncServices.getUiThreadExecutor());
    DictationResultHandlerImpl localDictationResultHandlerImpl = new DictationResultHandlerImpl(local3, localHypothesisToSuggestionSpansConverter, localSettings, new LatinTextFormatter(), localAsyncServices.getUiThreadExecutor());
    return new VoiceInputMethodManager(localVoiceLanguageSelector, localScreenStateMonitor, localAsyncServices.getUiThreadExecutor(), localImeLoggerHelper, localSettings, localAudioTrackSoundManager, localVoiceImeSubtypeUpdater, localVoiceInputViewHandler, local3, localVoiceRecognitionHandler, localDictationResultHandlerImpl);
  }
  
  private void forceReleaseResources()
  {
    this.mContinueRecording.forceExpire();
    maybeReleaseResources();
  }
  
  private void handleDone()
  {
    this.mImeLoggerHelper.onDone();
    this.mVoiceInputViewHandler.hideWaitingForResults();
    if (this.mBackToPrevImeOnDone) {
      backToPreviousIme();
    }
  }
  
  private void handleError(RecognizeException paramRecognizeException)
  {
    this.mDictationResultHandler.handleError();
    this.mVoiceInputViewHandler.displayError(ErrorUtils.getErrorMessage(paramRecognizeException));
    this.mImeLoggerHelper.onError();
  }
  
  private void handlePartialRecognitionResult(String paramString)
  {
    this.mImeLoggerHelper.setWaitingForResult(true);
    if (paramString != null) {
      this.mDictationResultHandler.handlePartialRecognitionResult(paramString);
    }
  }
  
  private void handlePause()
  {
    this.mDictationResultHandler.handleStop();
    this.mVoiceInputViewHandler.displayPause(this.mVoiceRecognitionHandler.isWaitingForResults());
  }
  
  private void handleRecognitionResult(Hypothesis paramHypothesis, String paramString)
  {
    this.mImeLoggerHelper.setWaitingForResult(false);
    this.mDictationResultHandler.handleRecognitionResult(paramHypothesis, paramString);
  }
  
  private void interruptDictation()
  {
    if (this.mVoiceRecognitionHandler != null)
    {
      if (this.mImeLoggerHelper != null) {
        this.mImeLoggerHelper.onInterrupt();
      }
      stopRecording();
      backToPreviousIme();
    }
  }
  
  private boolean isDictationSupportedByField()
  {
    EditorInfo localEditorInfo = this.mVoiceImeInputMethodService.getCurrentInputEditorInfo();
    int i = 0xFFF & localEditorInfo.inputType;
    int j;
    int k;
    if (i == 129)
    {
      j = 1;
      if (i != 225) {
        break label77;
      }
      k = 1;
      label38:
      if (i != 18) {
        break label83;
      }
    }
    label77:
    label83:
    for (int m = 1;; m = 0)
    {
      if ((j == 0) && (k == 0) && (m == 0)) {
        break label89;
      }
      Log.i("VoiceInputMethodManager", "Voice IME is not supported for password input type");
      return false;
      j = 0;
      break;
      k = 0;
      break label38;
    }
    label89:
    if ((localEditorInfo != null) && (localEditorInfo.privateImeOptions != null)) {
      for (String str : localEditorInfo.privateImeOptions.split(",")) {
        if (("noMicrophoneKey".equals(str)) || ("nm".equals(str)))
        {
          Log.i("VoiceInputMethodManager", "Voice IME has been disabled for this field");
          return false;
        }
      }
    }
    return true;
  }
  
  private void maybeReleaseResources()
  {
    if (this.mContinueRecording.isExpired())
    {
      Log.i("VoiceInputMethodManager", "#releaseResources");
      if (this.mInputViewActive)
      {
        if (this.mImeLoggerHelper == null) {
          break label99;
        }
        this.mImeLoggerHelper.onFinishInput();
      }
      for (;;)
      {
        this.mInputViewActive = false;
        this.mScreenStateMonitor.unregister();
        this.mDictationBcp47Locale.extend();
        this.mVoiceImeInputMethodService.scheduleSendEvents();
        stopDictation();
        this.mUiThreadExecutor.cancelExecute(this.mReleaseResourcesRunnable);
        this.mUiThreadExecutor.cancelExecute(this.mBackToPreviousImeRunnable);
        return;
        label99:
        Log.w("VoiceInputMethodManager", "onFinishInput - mImeLoggerHelper is null");
      }
    }
    Log.i("VoiceInputMethodManager", "#releaseResources - schedule");
    scheduleReleaseResources();
  }
  
  private void maybeUpdateImeSubtypes()
  {
    this.mVoiceImeSubtypeUpdater.maybeScheduleUpdate(this.mSettings.getConfiguration());
  }
  
  private void scheduleReleaseResources()
  {
    this.mUiThreadExecutor.cancelExecute(this.mReleaseResourcesRunnable);
    this.mUiThreadExecutor.executeDelayed(this.mReleaseResourcesRunnable, 500L);
  }
  
  private void startDictation()
  {
    if (!this.mInputViewActive) {
      return;
    }
    SessionParams localSessionParams = this.mVoiceRecognitionHandler.createSessionParams((String)this.mDictationBcp47Locale.getData(), this.mSettings.isProfanityFilterEnabled());
    this.mDictationResultHandler.init(localSessionParams.getRequestId());
    this.mVoiceInputViewHandler.setLanguages((String)this.mDictationBcp47Locale.getData(), this.mVoiceLanguageSelector.getEnabledDialects((String)this.mDictationBcp47Locale.getData()));
    this.mVoiceInputViewHandler.displayAudioNotInitialized();
    this.mVoiceRecognitionHandler.cancelRecognition();
    this.mVoiceRecognitionHandler.startRecognizer(localSessionParams, new DictationListener());
  }
  
  private void stopDictation()
  {
    if (this.mVoiceRecognitionHandler != null) {
      this.mVoiceRecognitionHandler.cancelRecognition();
    }
    for (;;)
    {
      if (this.mDictationResultHandler != null)
      {
        this.mDictationResultHandler.handleStop();
        this.mDictationResultHandler.reset();
      }
      return;
      Log.w("VoiceInputMethodManager", "onFinishInput - mVoiceRecognitionDelegate is null");
    }
  }
  
  private void stopRecording()
  {
    Log.i("VoiceInputMethodManager", "#stopRecording");
    this.mVoiceRecognitionHandler.stopListening();
    this.mDictationResultHandler.handleStop();
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    PrintWriterPrinter localPrintWriterPrinter = new PrintWriterPrinter(paramPrintWriter);
    localPrintWriterPrinter.println("VoiceIME state :");
    localPrintWriterPrinter.println("  mDictationResultHandler = " + this.mDictationResultHandler);
    localPrintWriterPrinter.println("  mImeLoggerHelper=" + this.mImeLoggerHelper);
    localPrintWriterPrinter.println("  mVoiceInputViewHelper=" + this.mVoiceInputViewHandler);
    localPrintWriterPrinter.println("  mInputViewActive=" + this.mInputViewActive);
    localPrintWriterPrinter.println("  mVoiceLanguageSelector=" + this.mVoiceLanguageSelector);
  }
  
  public void handleConfigurationChanged(Configuration paramConfiguration)
  {
    Log.i("VoiceInputMethodManager", "#handleConfigurationChanged " + this + " " + paramConfiguration.orientation);
    if (this.mVoiceInputViewHandler.isPaused()) {
      this.mForcePauseOnStart.extend();
    }
    if ((this.mVoiceInputViewHandler.isRecording()) || (this.mVoiceInputViewHandler.isListening()))
    {
      EventLogger.recordClientEvent(75);
      this.mContinueRecording.extend();
    }
    maybeReleaseResources();
  }
  
  public View handleCreateInputView()
  {
    Log.i("VoiceInputMethodManager", "#handleCreateInputView");
    this.mVoiceInputViewHandler.getView(new VoiceInputViewHandler.Callback()
    {
      public void close()
      {
        if (VoiceInputMethodManager.this.mVoiceRecognitionHandler.isWaitingForResults())
        {
          VoiceInputMethodManager.access$602(VoiceInputMethodManager.this, true);
          VoiceInputMethodManager.this.mVoiceInputViewHandler.displayWorking();
          return;
        }
        VoiceInputMethodManager.this.backToPreviousIme();
      }
      
      public void forceClose()
      {
        VoiceInputMethodManager.this.backToPreviousIme();
      }
      
      public void onDisplayDialectSelectionPopup()
      {
        VoiceInputMethodManager.this.stopRecording();
        VoiceInputMethodManager.this.stopDictation();
        VoiceInputMethodManager.this.mVoiceInputViewHandler.displayAudioNotInitialized();
        VoiceInputMethodManager.this.maybeUpdateImeSubtypes();
      }
      
      public void onUpdateDialect(GstaticConfiguration.Dialect paramAnonymousDialect)
      {
        EventLogger.recordClientEvent(67);
        VoiceInputMethodManager.this.mDictationBcp47Locale.setData(paramAnonymousDialect.getBcp47Locale());
        VoiceInputMethodManager.this.startDictation();
        VoiceInputMethodManager.this.mContinueRecording.extend();
      }
      
      public void startRecognition()
      {
        VoiceInputMethodManager.this.mImeLoggerHelper.onRestartRecognition();
        VoiceInputMethodManager.this.startDictation();
      }
      
      public void stopRecognition()
      {
        VoiceInputMethodManager.this.mVoiceRecognitionHandler.stopListening();
        VoiceInputMethodManager.this.mImeLoggerHelper.onPauseRecognition();
        VoiceInputMethodManager.this.handlePause();
      }
    });
  }
  
  public void handleDestroy()
  {
    Log.i("VoiceInputMethodManager", "#handleDestroy");
    if (this.mVoiceRecognitionHandler != null) {
      this.mVoiceRecognitionHandler.cancelRecognition();
    }
    forceReleaseResources();
  }
  
  public void handleFinishInput()
  {
    Log.i("VoiceInputMethodManager", "#handleFinishInput " + this);
    maybeReleaseResources();
  }
  
  public void handleFinishInputView(boolean paramBoolean)
  {
    Log.i("VoiceInputMethodManager", "#handleFinishInputView " + paramBoolean + " " + this);
  }
  
  public void handleHideWindow()
  {
    Log.i("VoiceInputMethodManager", "#handleHideWindow");
    backToPreviousIme();
    if (this.mImeLoggerHelper != null) {
      this.mImeLoggerHelper.onHideWindow();
    }
  }
  
  public void handleShowWindow(boolean paramBoolean)
  {
    Log.i("VoiceInputMethodManager", "#handleShowWindow[" + paramBoolean + "]");
    if (this.mImeLoggerHelper != null) {
      this.mImeLoggerHelper.onShowWindow();
    }
  }
  
  public void handleStartInputView(EditorInfo paramEditorInfo, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("#handleStartInputView [active=").append(this.mInputViewActive).append(",keepRecording=");
    if (!this.mContinueRecording.isExpired()) {}
    for (boolean bool = true;; bool = false)
    {
      Log.i("VoiceInputMethodManager", bool + " ] " + this);
      if (this.mInputViewActive) {
        break label259;
      }
      if ((!isDictationSupportedByField()) || (!this.mVoiceImeInputMethodService.isScreenOn()))
      {
        Log.i("VoiceInputMethodManager", "Voice IME cannot be started");
        this.mUiThreadExecutor.execute(this.mBackToPreviousImeRunnable);
      }
      this.mScreenStateMonitor.register(new ScreenStateMonitor.Listener()
      {
        public void onScreenOff()
        {
          VoiceInputMethodManager.this.backToPreviousIme();
        }
      });
      this.mImeLoggerHelper.onStartInputView(this.mVoiceImeInputMethodService.getCurrentInputEditorInfo());
      this.mInputViewActive = true;
      this.mBackToPrevImeOnDone = false;
      if ((this.mDictationBcp47Locale.isExpired()) || (this.mDictationBcp47Locale.getData() == null)) {
        this.mDictationBcp47Locale.setData(this.mVoiceLanguageSelector.getDictationBcp47Locale());
      }
      if (this.mForcePauseOnStart.isExpired()) {
        break;
      }
      this.mForcePauseOnStart.extend();
      this.mVoiceInputViewHandler.setLanguages((String)this.mDictationBcp47Locale.getData(), this.mVoiceLanguageSelector.getEnabledDialects((String)this.mDictationBcp47Locale.getData()));
      this.mVoiceInputViewHandler.displayPause(false);
      return;
    }
    startDictation();
    return;
    label259:
    if (!this.mContinueRecording.isExpired())
    {
      this.mContinueRecording.extend();
      this.mUiThreadExecutor.cancelExecute(this.mReleaseResourcesRunnable);
      this.mVoiceInputViewHandler.setLanguages((String)this.mDictationBcp47Locale.getData(), this.mVoiceLanguageSelector.getEnabledDialects((String)this.mDictationBcp47Locale.getData()));
      this.mVoiceInputViewHandler.restoreState();
      return;
    }
    Log.e("VoiceInputMethodManager", "#handleStartInputView: unhandled");
  }
  
  public void handleUpdateSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    if (paramInt3 != paramInt4) {
      interruptDictation();
    }
  }
  
  public void handleViewClicked(boolean paramBoolean)
  {
    Log.i("VoiceInputMethodManager", "#handleViewClicked[" + paramBoolean + "]");
    if (!paramBoolean) {
      interruptDictation();
    }
  }
  
  public boolean isMaybeForceFullScreen()
  {
    EditorInfo localEditorInfo = this.mVoiceImeInputMethodService.getCurrentInputEditorInfo();
    if (localEditorInfo != null) {
      if ((0x10000000 & localEditorInfo.imeOptions) == 0) {}
    }
    Resources localResources;
    do
    {
      do
      {
        return false;
      } while ((0x2000000 & localEditorInfo.imeOptions) != 0);
      localResources = this.mVoiceImeInputMethodService.getResources();
    } while (localResources.getDisplayMetrics().heightPixels > localResources.getDimension(2131689631));
    return true;
  }
  
  public class DictationListener
    extends RecognitionEventListenerAdapter
  {
    private boolean mInvalid = false;
    
    public DictationListener() {}
    
    private String getPartial(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
    {
      if (!paramRecognitionEvent.hasPartialResult()) {}
      StringBuilder localStringBuilder;
      label107:
      do
      {
        RecognizerProtos.PartialResult localPartialResult;
        int i;
        do
        {
          return null;
          localPartialResult = paramRecognitionEvent.getPartialResult();
          i = localPartialResult.getPartCount();
        } while (i == 0);
        localStringBuilder = new StringBuilder();
        float f = VoiceInputMethodManager.this.mSettings.getConfiguration().getDictation().getPartialResultMinConfidence();
        int j = 0;
        if (j < i)
        {
          RecognizerProtos.PartialPart localPartialPart = localPartialResult.getPart(j);
          if (!localPartialPart.hasText()) {}
          for (;;)
          {
            j++;
            break;
            if (localPartialPart.getStability() < f) {
              break label107;
            }
            localStringBuilder.append(localPartialPart.getText());
          }
        }
      } while (localStringBuilder.length() == 0);
      return localStringBuilder.toString();
    }
    
    private void processPartialRecognitionResult(String paramString)
    {
      VoiceInputMethodManager.this.handlePartialRecognitionResult(paramString);
    }
    
    public void invalidate()
    {
      this.mInvalid = true;
    }
    
    public boolean isValid()
    {
      return !this.mInvalid;
    }
    
    public void onBeginningOfSpeech(long paramLong)
    {
      if (this.mInvalid) {
        return;
      }
      VoiceInputMethodManager.this.mVoiceInputViewHandler.displayRecording();
    }
    
    public void onDone()
    {
      if (this.mInvalid) {
        return;
      }
      invalidate();
      VoiceInputMethodManager.this.handleDone();
    }
    
    public void onEndOfSpeech()
    {
      if (this.mInvalid) {
        return;
      }
      VoiceInputMethodManager.this.mSoundManager.playDictationDoneSound();
      VoiceInputMethodManager.this.mVoiceRecognitionHandler.stopListening();
      VoiceInputMethodManager.this.handlePause();
    }
    
    public void onError(RecognizeException paramRecognizeException)
    {
      if (this.mInvalid) {
        return;
      }
      invalidate();
      Log.w("VoiceInputMethodManager", "onError: " + paramRecognizeException.getClass().getName() + " " + paramRecognizeException.getMessage());
      VoiceInputMethodManager.this.mSoundManager.playErrorSound();
      VoiceInputMethodManager.this.handleError(paramRecognizeException);
    }
    
    public void onNoSpeechDetected()
    {
      if (this.mInvalid) {
        return;
      }
      invalidate();
      VoiceInputMethodManager.this.mSoundManager.playNoInputSound();
      VoiceInputMethodManager.this.mVoiceRecognitionHandler.cancelRecognition();
      VoiceInputMethodManager.this.handlePause();
    }
    
    public void onReadyForSpeech()
    {
      VoiceInputMethodManager.this.mVoiceInputViewHandler.displayListening();
    }
    
    public void onRecognitionCancelled()
    {
      if (this.mInvalid) {
        return;
      }
      invalidate();
      VoiceInputMethodManager.this.mSoundManager.playNoInputSound();
      VoiceInputMethodManager.this.mVoiceInputViewHandler.displayPause(VoiceInputMethodManager.this.mVoiceRecognitionHandler.isWaitingForResults());
    }
    
    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
    {
      if (this.mInvalid) {}
      label144:
      for (;;)
      {
        return;
        String str = getPartial(paramRecognitionEvent);
        RecognizerProtos.Hypothesis localHypothesis;
        Hypothesis localHypothesis1;
        if (paramRecognitionEvent.hasResult())
        {
          RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getResult();
          if (localRecognitionResult.getHypothesisCount() == 0)
          {
            Log.w("VoiceInputMethodManager", "No hypothesis in recognition result.");
            return;
          }
          localHypothesis = localRecognitionResult.getHypothesis(0);
          if ((VoiceInputMethodManager.this.mVoiceRecognitionHandler.getSessionParams().isAlternatesEnabled()) && (localHypothesis.hasAlternates()))
          {
            localHypothesis1 = Hypothesis.fromAlternateSpanProtos(localHypothesis.getText(), localHypothesis.getAlternates().getSpanList());
            VoiceInputMethodManager.this.handleRecognitionResult(localHypothesis1, str);
          }
        }
        for (;;)
        {
          if (paramRecognitionEvent.getEventType() != 1) {
            break label144;
          }
          invalidate();
          VoiceInputMethodManager.this.handleDone();
          return;
          localHypothesis1 = Hypothesis.fromText(localHypothesis.getText());
          break;
          if (str != null) {
            processPartialRecognitionResult(str);
          }
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ime.VoiceInputMethodManager
 * JD-Core Version:    0.7.0.1
 */