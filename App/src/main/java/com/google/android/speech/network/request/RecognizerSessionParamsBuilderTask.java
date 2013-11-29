package com.google.android.speech.network.request;

import com.google.android.speech.SpeechSettings;
import com.google.speech.recognizer.api.RecognizerSessionParamsProto.RecognizerSessionParams;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;

public class RecognizerSessionParamsBuilderTask
  extends BaseRequestBuilderTask<RecognizerSessionParamsProto.RecognizerSessionParams>
{
  private final boolean mEnableAlternates;
  private final boolean mEnablePartials;
  private final boolean mProfanityFilterEnabled;
  private final int mSampleRate;
  private final SpeechSettings mSpeechSettings;
  
  public RecognizerSessionParamsBuilderTask(SpeechSettings paramSpeechSettings, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    super("RecognizerSessionParamsBuilderTask");
    this.mSpeechSettings = paramSpeechSettings;
    this.mSampleRate = paramInt;
    this.mEnablePartials = paramBoolean1;
    this.mEnableAlternates = paramBoolean2;
    this.mProfanityFilterEnabled = paramBoolean3;
  }
  
  protected RecognizerSessionParamsProto.RecognizerSessionParams build()
  {
    RecognizerSessionParamsProto.RecognizerSessionParams localRecognizerSessionParams = new RecognizerSessionParamsProto.RecognizerSessionParams();
    localRecognizerSessionParams.setType(0);
    localRecognizerSessionParams.setMaskOffensiveWords(this.mProfanityFilterEnabled);
    localRecognizerSessionParams.setSampleRate(this.mSampleRate);
    if (this.mEnablePartials) {
      localRecognizerSessionParams.setEnablePartialResults(true);
    }
    if (this.mEnableAlternates)
    {
      GstaticConfiguration.Configuration localConfiguration = this.mSpeechSettings.getConfiguration();
      localRecognizerSessionParams.setEnableAlternates(true);
      localRecognizerSessionParams.setAlternateParams(S3RecognizerInfoBuilderTask.getAlternateParams(localConfiguration));
    }
    return localRecognizerSessionParams;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.request.RecognizerSessionParamsBuilderTask
 * JD-Core Version:    0.7.0.1
 */