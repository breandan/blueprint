package com.google.android.speech.params;

import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.embedded.Greco3CallbackFactory;
import com.google.android.speech.embedded.Greco3EngineManager;
import com.google.android.speech.embedded.Greco3ModeSelector;
import com.google.android.speech.engine.RetryPolicy;
import com.google.android.speech.network.S3ConnectionFactory;
import javax.annotation.Nullable;

public class RecognitionEngineParams
{
  private final EmbeddedParams mEmbeddedParams;
  private final MusicDetectorParams mMusicDetectorParams;
  private final NetworkParams mNetworkParams;
  
  public RecognitionEngineParams(EmbeddedParams paramEmbeddedParams, NetworkParams paramNetworkParams, MusicDetectorParams paramMusicDetectorParams)
  {
    this.mEmbeddedParams = paramEmbeddedParams;
    this.mNetworkParams = paramNetworkParams;
    this.mMusicDetectorParams = paramMusicDetectorParams;
  }
  
  public EmbeddedParams getEmbeddedParams()
  {
    return this.mEmbeddedParams;
  }
  
  public MusicDetectorParams getMusicDetectorParams()
  {
    return this.mMusicDetectorParams;
  }
  
  public NetworkParams getNetworkParams()
  {
    return this.mNetworkParams;
  }
  
  public static class EmbeddedParams
  {
    private final int mBytesPerSample;
    private final Greco3CallbackFactory mCallbackFactory;
    private final Greco3EngineManager mGreco3EngineManager;
    private final Greco3ModeSelector mModeSelector;
    private final int mSamplingRate;
    private final SpeechLevelSource mSpeechLevelSource;
    private final SpeechSettings mSpeechSettings;
    
    public EmbeddedParams(Greco3CallbackFactory paramGreco3CallbackFactory, Greco3EngineManager paramGreco3EngineManager, Greco3ModeSelector paramGreco3ModeSelector, SpeechLevelSource paramSpeechLevelSource, SpeechSettings paramSpeechSettings, int paramInt1, int paramInt2)
    {
      this.mCallbackFactory = paramGreco3CallbackFactory;
      this.mGreco3EngineManager = paramGreco3EngineManager;
      this.mModeSelector = paramGreco3ModeSelector;
      this.mSpeechLevelSource = paramSpeechLevelSource;
      this.mSpeechSettings = paramSpeechSettings;
      this.mBytesPerSample = paramInt1;
      this.mSamplingRate = paramInt2;
    }
    
    public int getBytesPerSample()
    {
      return this.mBytesPerSample;
    }
    
    public Greco3CallbackFactory getCallbackFactory()
    {
      return this.mCallbackFactory;
    }
    
    public Greco3EngineManager getGreco3EngineManager()
    {
      return this.mGreco3EngineManager;
    }
    
    public Greco3ModeSelector getModeSelector()
    {
      return this.mModeSelector;
    }
    
    public int getSamplingRate()
    {
      return this.mSamplingRate;
    }
    
    public SpeechSettings getSpeechSettings()
    {
      return this.mSpeechSettings;
    }
  }
  
  public static class MusicDetectorParams
  {
    private final SpeechSettings mSettings;
    
    public MusicDetectorParams(SpeechSettings paramSpeechSettings)
    {
      this.mSettings = paramSpeechSettings;
    }
    
    public SpeechSettings getSettings()
    {
      return this.mSettings;
    }
  }
  
  public static class NetworkParams
  {
    @Nullable
    private final S3ConnectionFactory mFallbackConnectionFactory;
    private final NetworkRequestProducerParams mNetworkRequestProducerParams;
    private final S3ConnectionFactory mPrimaryConnectionFactory;
    private final RetryPolicy mRetryPolicy;
    
    public NetworkParams(S3ConnectionFactory paramS3ConnectionFactory1, @Nullable S3ConnectionFactory paramS3ConnectionFactory2, RetryPolicy paramRetryPolicy, NetworkRequestProducerParams paramNetworkRequestProducerParams)
    {
      this.mPrimaryConnectionFactory = paramS3ConnectionFactory1;
      this.mFallbackConnectionFactory = paramS3ConnectionFactory2;
      this.mRetryPolicy = paramRetryPolicy;
      this.mNetworkRequestProducerParams = paramNetworkRequestProducerParams;
    }
    
    public S3ConnectionFactory getFallbackConnectionFactory()
    {
      return this.mFallbackConnectionFactory;
    }
    
    public NetworkRequestProducerParams getNetworkRequestProducerParams()
    {
      return this.mNetworkRequestProducerParams;
    }
    
    public S3ConnectionFactory getPrimaryConnectionFactory()
    {
      return this.mPrimaryConnectionFactory;
    }
    
    public RetryPolicy getRetryPolicy()
    {
      return this.mRetryPolicy;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.params.RecognitionEngineParams
 * JD-Core Version:    0.7.0.1
 */