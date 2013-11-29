package com.google.android.speech.network.request;

import com.google.android.speech.SpeechSettings;
import com.google.speech.common.Alternates.AlternateParams;
import com.google.speech.common.proto.RecognitionContextProto.RecognitionContext;
import com.google.speech.speech.s3.Recognizer.S3RecognizerInfo;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Dictation;

public class S3RecognizerInfoBuilderTask
  extends BaseRequestBuilderTask<Recognizer.S3RecognizerInfo>
{
  private final int mMaxNbest;
  private final boolean mNeedsAlternates;
  private final boolean mNeedsCombinedNbest;
  private final boolean mNeedsPartialResults;
  private final boolean mProfanityFilterEnabled;
  private final RecognitionContextProto.RecognitionContext mRecognitionContext;
  private final boolean mServerEndpointingEnabled;
  private final SpeechSettings mSpeechSettings;
  
  public S3RecognizerInfoBuilderTask(RecognitionContextProto.RecognitionContext paramRecognitionContext, SpeechSettings paramSpeechSettings, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4, boolean paramBoolean5)
  {
    super("S3RecognizerInfoBuilderTask");
    this.mRecognitionContext = paramRecognitionContext;
    this.mSpeechSettings = paramSpeechSettings;
    this.mNeedsPartialResults = paramBoolean1;
    this.mNeedsCombinedNbest = paramBoolean2;
    this.mNeedsAlternates = paramBoolean3;
    this.mMaxNbest = paramInt;
    this.mServerEndpointingEnabled = paramBoolean4;
    this.mProfanityFilterEnabled = paramBoolean5;
  }
  
  static Alternates.AlternateParams getAlternateParams(GstaticConfiguration.Configuration paramConfiguration)
  {
    return new Alternates.AlternateParams().setMaxSpanLength(paramConfiguration.getDictation().getMaxSpanLength()).setMaxTotalSpanLength(paramConfiguration.getDictation().getMaxTotalSpanLength()).setUnit(1);
  }
  
  protected Recognizer.S3RecognizerInfo build()
  {
    Recognizer.S3RecognizerInfo localS3RecognizerInfo = new Recognizer.S3RecognizerInfo();
    if (this.mRecognitionContext != null) {
      localS3RecognizerInfo.setRecognitionContext(this.mRecognitionContext);
    }
    localS3RecognizerInfo.setEnablePartialResults(this.mNeedsPartialResults);
    localS3RecognizerInfo.setEnableCombinedNbest(this.mNeedsCombinedNbest);
    if (this.mNeedsCombinedNbest) {
      localS3RecognizerInfo.setMaxNbest(this.mMaxNbest);
    }
    localS3RecognizerInfo.setEnableAlternates(this.mNeedsAlternates);
    if (this.mNeedsAlternates) {
      localS3RecognizerInfo.setAlternateParams(getAlternateParams(this.mSpeechSettings.getConfiguration()));
    }
    if (this.mProfanityFilterEnabled) {}
    for (int i = 2;; i = 0)
    {
      localS3RecognizerInfo.setProfanityFilter(i);
      localS3RecognizerInfo.setEnablePersonalization(this.mSpeechSettings.isPersonalizationEnabled());
      localS3RecognizerInfo.setEnableEndpointerEvents(this.mServerEndpointingEnabled);
      return localS3RecognizerInfo;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.request.S3RecognizerInfoBuilderTask
 * JD-Core Version:    0.7.0.1
 */