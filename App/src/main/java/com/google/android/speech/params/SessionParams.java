package com.google.android.speech.params;

import android.location.Location;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.speech.common.proto.RecognitionContextProto.RecognitionContext;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Dictation;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.EndpointerParams;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.IntentApi;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.ServiceApi;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.VoiceSearch;
import javax.annotation.Nullable;

public class SessionParams
{
  private final boolean mAlternatesEnabled;
  private final String mApplicationId;
  private final AudioInputParams mAudioInputParams;
  private final Greco3Grammar mGreco3Grammar;
  private final Greco3Mode mGreco3Mode;
  @Nullable
  private final Location mLocationOverride;
  private final int mMaxNbest;
  private final int mMode;
  private final boolean mNoSpeechDetectedEnabled;
  private final boolean mPartialResultsEnabled;
  private final boolean mProfanityFilterEnabled;
  @Nullable
  private final RecognitionContextProto.RecognitionContext mRecognitionContext;
  private final Supplier<String> mRequestIdSupplier;
  private final boolean mResendingAudio;
  private final boolean mServerEndpointingEnabled;
  private final String mService;
  private final boolean mSoundSearchTtsEnabled;
  private final String mSpokenBcp47Locale;
  private final boolean mStopOnEndOfSpeech;
  private final boolean mSuggestionsEnabled;
  @Nullable
  private final String mTriggerApplication;
  private final boolean mUseMusicHotworder;
  
  private SessionParams(int paramInt1, AudioInputParams paramAudioInputParams, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString1, String paramString2, Greco3Grammar paramGreco3Grammar, Greco3Mode paramGreco3Mode, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, int paramInt2, Location paramLocation, RecognitionContextProto.RecognitionContext paramRecognitionContext, String paramString3, Supplier<String> paramSupplier, String paramString4, boolean paramBoolean9, boolean paramBoolean10)
  {
    this.mMode = paramInt1;
    this.mAudioInputParams = paramAudioInputParams;
    this.mAlternatesEnabled = paramBoolean1;
    this.mResendingAudio = paramBoolean2;
    this.mStopOnEndOfSpeech = paramBoolean3;
    this.mSpokenBcp47Locale = paramString1;
    this.mTriggerApplication = paramString2;
    this.mGreco3Grammar = paramGreco3Grammar;
    this.mGreco3Mode = paramGreco3Mode;
    this.mUseMusicHotworder = paramBoolean4;
    this.mNoSpeechDetectedEnabled = paramBoolean5;
    this.mPartialResultsEnabled = paramBoolean6;
    this.mProfanityFilterEnabled = paramBoolean7;
    this.mSuggestionsEnabled = paramBoolean8;
    this.mMaxNbest = paramInt2;
    this.mLocationOverride = paramLocation;
    this.mRecognitionContext = paramRecognitionContext;
    this.mApplicationId = paramString3;
    this.mRequestIdSupplier = paramSupplier;
    this.mService = paramString4;
    this.mSoundSearchTtsEnabled = paramBoolean9;
    this.mServerEndpointingEnabled = paramBoolean10;
  }
  
  public static boolean isSoundSearch(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 8)) {}
    for (boolean bool1 = true;; bool1 = false)
    {
      Preconditions.checkArgument(bool1, "Unsupported mode");
      boolean bool2;
      if (paramInt != 7)
      {
        bool2 = false;
        if (paramInt != 8) {}
      }
      else
      {
        bool2 = true;
      }
      return bool2;
    }
  }
  
  public static boolean isVoiceDialerSearch(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 8)) {}
    for (boolean bool1 = true;; bool1 = false)
    {
      Preconditions.checkArgument(bool1, "Unsupported mode");
      boolean bool2;
      if (paramInt != 4)
      {
        bool2 = false;
        if (paramInt != 5) {}
      }
      else
      {
        bool2 = true;
      }
      return bool2;
    }
  }
  
  public String getApplicationId()
  {
    return this.mApplicationId;
  }
  
  public AudioInputParams getAudioInputParams()
  {
    return this.mAudioInputParams;
  }
  
  public int getEmbeddedFallbackTimeout(SpeechSettings paramSpeechSettings)
  {
    if (this.mGreco3Mode == Greco3Mode.DICTATION) {
      return paramSpeechSettings.getConfiguration().getDictation().getEmbeddedRecognizerFallbackTimeout();
    }
    if (this.mGreco3Mode == Greco3Mode.GRAMMAR) {
      return paramSpeechSettings.getConfiguration().getVoiceSearch().getEmbeddedRecognizerFallbackTimeout();
    }
    return -1;
  }
  
  @Nullable
  public GstaticConfiguration.EndpointerParams getEndpointerParams(SpeechSettings paramSpeechSettings)
  {
    GstaticConfiguration.EndpointerParams localEndpointerParams;
    switch (this.mMode)
    {
    default: 
      localEndpointerParams = paramSpeechSettings.getConfiguration().getVoiceSearch().getEndpointerParams();
    }
    for (;;)
    {
      if (!this.mNoSpeechDetectedEnabled) {
        localEndpointerParams.setNoSpeechDetectedTimeoutMsec(20000);
      }
      return localEndpointerParams;
      localEndpointerParams = paramSpeechSettings.getConfiguration().getDictation().getEndpointerParams();
      continue;
      localEndpointerParams = paramSpeechSettings.getConfiguration().getIntentApi().getEndpointerParams();
      continue;
      if (this.mStopOnEndOfSpeech) {}
      for (localEndpointerParams = paramSpeechSettings.getConfiguration().getServiceApi().getEndpointerParams();; localEndpointerParams = paramSpeechSettings.getConfiguration().getDictation().getEndpointerParams()) {
        break;
      }
      localEndpointerParams = paramSpeechSettings.getConfiguration().getVoiceSearch().getEndpointerParams();
    }
  }
  
  public Greco3Grammar getGreco3Grammar()
  {
    return this.mGreco3Grammar;
  }
  
  public Greco3Mode getGreco3Mode()
  {
    return this.mGreco3Mode;
  }
  
  public Location getLocationOverride()
  {
    return this.mLocationOverride;
  }
  
  public int getMaxNbest()
  {
    return this.mMaxNbest;
  }
  
  public int getMode()
  {
    return this.mMode;
  }
  
  public RecognitionContextProto.RecognitionContext getRecognitionContext()
  {
    return this.mRecognitionContext;
  }
  
  public String getRequestId()
  {
    return (String)this.mRequestIdSupplier.get();
  }
  
  public String getService()
  {
    return this.mService;
  }
  
  public String getSpokenBcp47Locale()
  {
    return this.mSpokenBcp47Locale;
  }
  
  public String getTriggerApplication()
  {
    return this.mTriggerApplication;
  }
  
  public boolean isAlternatesEnabled()
  {
    return this.mAlternatesEnabled;
  }
  
  public boolean isCombinedNbestEnabled()
  {
    return (this.mMode == 2) || (this.mMode == 1) || (this.mMode == 0);
  }
  
  public boolean isPartialResultsEnabled()
  {
    return this.mPartialResultsEnabled;
  }
  
  public boolean isProfanityFilterEnabled()
  {
    return this.mProfanityFilterEnabled;
  }
  
  public boolean isResendingAudio()
  {
    return this.mResendingAudio;
  }
  
  public boolean isServerEndpointingEnabled()
  {
    return this.mServerEndpointingEnabled;
  }
  
  public boolean isSoundSearchTtsEnabled()
  {
    return this.mSoundSearchTtsEnabled;
  }
  
  public boolean isSuggestionsEnabled()
  {
    return this.mSuggestionsEnabled;
  }
  
  public boolean shouldUseMusicHotworder()
  {
    return this.mUseMusicHotworder;
  }
  
  public boolean stopOnEndOfSpeech()
  {
    return this.mStopOnEndOfSpeech;
  }
  
  public static class Builder
  {
    private boolean mAlternatesEnabled = true;
    private String mApplicationId;
    private AudioInputParams mAudioInputParams;
    private Greco3Grammar mGreco3Grammar = Greco3Grammar.CONTACT_DIALING;
    private Greco3Mode mGreco3Mode = Greco3Mode.ENDPOINTER_VOICESEARCH;
    private Location mLocationOverride;
    private int mMaxNbest = 5;
    private int mMode = 2;
    private boolean mNoSpeechDetectedEnabled = true;
    private boolean mPartialResultsEnabled = true;
    private boolean mProfanityFilterEnabled = true;
    private RecognitionContextProto.RecognitionContext mRecognitionContext;
    private boolean mResendingAudio = false;
    private boolean mServerEndpointingEnabled;
    private String mService;
    private boolean mSoundSearchTtsEnabled = false;
    private String mSpokenBcp47Locale = "en-US";
    private boolean mStopOnEndOfSpeech = true;
    private boolean mSuggestionsEnabled = true;
    private String mTriggerApplication;
    private boolean mUseMusicHotworder = true;
    
    private Supplier<String> createNewRequestId()
    {
      Suppliers.memoize(new Supplier()
      {
        public String get()
        {
          return RequestIdGenerator.INSTANCE.newRequestId();
        }
      });
    }
    
    private static String getDefaultApplicationId(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        throw new IllegalStateException("Unknown mode " + paramInt);
      case 0: 
        return "intent-api";
      case 1: 
        return "service-api";
      case 2: 
      case 6: 
      case 7: 
        return "voice-search";
      case 3: 
        return "voice-ime";
      case 4: 
      case 5: 
        return "hands-free";
      }
      return "now-tv";
    }
    
    private static String getDefaultService(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        throw new IllegalStateException("Unknown mode " + paramInt);
      case 0: 
      case 1: 
        return "recognizer";
      case 4: 
        return "voicesearch";
      case 2: 
        return "voicesearch-web";
      case 3: 
      case 5: 
      case 6: 
        return "recognizer";
      case 7: 
        return "sound-search";
      }
      return "sound-search-tv";
    }
    
    public SessionParams build()
    {
      if (this.mAudioInputParams == null) {
        this.mAudioInputParams = new AudioInputParams.Builder().build();
      }
      if (this.mApplicationId == null) {
        this.mApplicationId = getDefaultApplicationId(this.mMode);
      }
      if (this.mService == null) {
        this.mService = getDefaultService(this.mMode);
      }
      return new SessionParams(this.mMode, this.mAudioInputParams, this.mAlternatesEnabled, this.mResendingAudio, this.mStopOnEndOfSpeech, this.mSpokenBcp47Locale, this.mTriggerApplication, this.mGreco3Grammar, this.mGreco3Mode, this.mUseMusicHotworder, this.mNoSpeechDetectedEnabled, this.mPartialResultsEnabled, this.mProfanityFilterEnabled, this.mSuggestionsEnabled, this.mMaxNbest, this.mLocationOverride, this.mRecognitionContext, this.mApplicationId, createNewRequestId(), this.mService, this.mSoundSearchTtsEnabled, this.mServerEndpointingEnabled, null);
    }
    
    public Builder setApplicationIdOverride(String paramString)
    {
      this.mApplicationId = paramString;
      return this;
    }
    
    public Builder setAudioInputParams(AudioInputParams paramAudioInputParams)
    {
      this.mAudioInputParams = paramAudioInputParams;
      return this;
    }
    
    public Builder setGreco3Grammar(Greco3Grammar paramGreco3Grammar)
    {
      this.mGreco3Grammar = paramGreco3Grammar;
      return this;
    }
    
    public Builder setGreco3Mode(Greco3Mode paramGreco3Mode)
    {
      this.mGreco3Mode = paramGreco3Mode;
      return this;
    }
    
    public Builder setMode(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 8)) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkArgument(bool, "Unsupported mode");
        this.mMode = paramInt;
        return this;
      }
    }
    
    public Builder setNoSpeechDetectedEnabled(boolean paramBoolean)
    {
      this.mNoSpeechDetectedEnabled = paramBoolean;
      return this;
    }
    
    public Builder setProfanityFilterEnabled(boolean paramBoolean)
    {
      this.mProfanityFilterEnabled = paramBoolean;
      return this;
    }
    
    public Builder setRecognitionContext(RecognitionContextProto.RecognitionContext paramRecognitionContext)
    {
      this.mRecognitionContext = paramRecognitionContext;
      return this;
    }
    
    public Builder setResendingAudio(boolean paramBoolean)
    {
      this.mResendingAudio = paramBoolean;
      return this;
    }
    
    public Builder setServerEndpointingEnabled(boolean paramBoolean)
    {
      this.mServerEndpointingEnabled = paramBoolean;
      return this;
    }
    
    public Builder setServiceOverride(String paramString)
    {
      this.mService = paramString;
      return this;
    }
    
    public Builder setSoundSearchTtsEnabled(boolean paramBoolean)
    {
      this.mSoundSearchTtsEnabled = paramBoolean;
      return this;
    }
    
    public Builder setSpokenBcp47Locale(String paramString)
    {
      this.mSpokenBcp47Locale = paramString;
      return this;
    }
    
    public Builder setStopOnEndOfSpeech(boolean paramBoolean)
    {
      this.mStopOnEndOfSpeech = paramBoolean;
      return this;
    }
    
    public Builder setSuggestionsEnabled(boolean paramBoolean)
    {
      this.mSuggestionsEnabled = paramBoolean;
      return this;
    }
    
    public Builder setTriggerApplication(String paramString)
    {
      this.mTriggerApplication = paramString;
      return this;
    }
    
    public Builder setUseMusicHotworder(boolean paramBoolean)
    {
      this.mUseMusicHotworder = paramBoolean;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.params.SessionParams
 * JD-Core Version:    0.7.0.1
 */