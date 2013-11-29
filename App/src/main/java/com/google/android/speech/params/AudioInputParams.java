package com.google.android.speech.params;

import android.net.Uri;

public class AudioInputParams
{
  private final int mEncoding;
  private final boolean mNoiseSuppressionEnabled;
  private final boolean mPlayBeepEnabled;
  private final Uri mRecordedAudioUri;
  private final boolean mReportSoundLevels;
  private final boolean mRequestAudioFocus;
  private final int mSamplingRateHz;
  private final long mStreamRewindTimeUsec;
  private final boolean mUsePreemptibleAudioSource;
  
  private AudioInputParams(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2, long paramLong, Uri paramUri, boolean paramBoolean4, boolean paramBoolean5)
  {
    this.mNoiseSuppressionEnabled = paramBoolean1;
    this.mPlayBeepEnabled = paramBoolean2;
    this.mReportSoundLevels = paramBoolean3;
    this.mEncoding = paramInt1;
    this.mSamplingRateHz = paramInt2;
    this.mStreamRewindTimeUsec = paramLong;
    this.mRecordedAudioUri = paramUri;
    this.mUsePreemptibleAudioSource = paramBoolean4;
    this.mRequestAudioFocus = paramBoolean5;
  }
  
  public int getEncoding()
  {
    return this.mEncoding;
  }
  
  public Uri getRecordedAudioUri()
  {
    return this.mRecordedAudioUri;
  }
  
  public int getSamplingRate()
  {
    return this.mSamplingRateHz;
  }
  
  public long getStreamRewindTime()
  {
    return this.mStreamRewindTimeUsec;
  }
  
  public boolean hasStreamRewindTime()
  {
    return this.mStreamRewindTimeUsec >= 0L;
  }
  
  public boolean isNoiseSuppressionEnabled()
  {
    return this.mNoiseSuppressionEnabled;
  }
  
  public boolean isPlayBeepEnabled()
  {
    return this.mPlayBeepEnabled;
  }
  
  public boolean shouldReportSoundLevels()
  {
    return this.mReportSoundLevels;
  }
  
  public boolean shouldRequestAudioFocus()
  {
    return this.mRequestAudioFocus;
  }
  
  public boolean usePreemptibleAudioSource()
  {
    return this.mUsePreemptibleAudioSource;
  }
  
  public static class Builder
  {
    private int mEncoding = 3;
    private boolean mNoiseSuppressionEnabled = true;
    private boolean mPlayBeepEnabled = true;
    private Uri mRecordedAudioUri;
    private boolean mReportSoundLevels = true;
    private boolean mRequestAudioFocus = true;
    private int mSamplingRateHz = 8000;
    private long mStreamRewindTimeUsec = -1L;
    private boolean mUsePreemptibleAudioSource = false;
    
    public AudioInputParams build()
    {
      return new AudioInputParams(this.mNoiseSuppressionEnabled, this.mPlayBeepEnabled, this.mReportSoundLevels, this.mEncoding, this.mSamplingRateHz, this.mStreamRewindTimeUsec, this.mRecordedAudioUri, this.mUsePreemptibleAudioSource, this.mRequestAudioFocus, null);
    }
    
    public Builder setEncoding(int paramInt)
    {
      this.mEncoding = paramInt;
      return this;
    }
    
    public Builder setNoiseSuppressionEnabled(boolean paramBoolean)
    {
      this.mNoiseSuppressionEnabled = paramBoolean;
      return this;
    }
    
    public Builder setPlayBeepEnabled(boolean paramBoolean)
    {
      this.mPlayBeepEnabled = paramBoolean;
      return this;
    }
    
    public Builder setRecordedAudioUri(Uri paramUri)
    {
      this.mRecordedAudioUri = paramUri;
      return this;
    }
    
    public Builder setReportSoundLevels(boolean paramBoolean)
    {
      this.mReportSoundLevels = paramBoolean;
      return this;
    }
    
    public Builder setRequestAudioFocus(boolean paramBoolean)
    {
      this.mRequestAudioFocus = paramBoolean;
      return this;
    }
    
    public Builder setSamplingRate(int paramInt)
    {
      this.mSamplingRateHz = paramInt;
      return this;
    }
    
    public Builder setStreamRewindTimeUsec(long paramLong)
    {
      this.mStreamRewindTimeUsec = paramLong;
      return this;
    }
    
    public Builder setUsePreemptibleAudioSource(boolean paramBoolean)
    {
      this.mUsePreemptibleAudioSource = paramBoolean;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.params.AudioInputParams
 * JD-Core Version:    0.7.0.1
 */