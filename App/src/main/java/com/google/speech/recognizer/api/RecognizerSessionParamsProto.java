package com.google.speech.recognizer.api;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import com.google.speech.common.Alternates.AlternateParams;
import java.io.IOException;

public final class RecognizerSessionParamsProto
{
  public static final class RecognizerSessionParams
    extends MessageMicro
  {
    private Alternates.AlternateParams alternateParams_ = null;
    private int cachedSize = -1;
    private int channelCount_ = 1;
    private boolean enableAlternates_ = false;
    private boolean enableFrameLogging_ = false;
    private boolean enablePartialNbest_ = false;
    private boolean enablePartialResults_ = true;
    private boolean enableSpeakerTraining_ = false;
    private String forceTranscript_ = "";
    private boolean hasAlternateParams;
    private boolean hasChannelCount;
    private boolean hasEnableAlternates;
    private boolean hasEnableFrameLogging;
    private boolean hasEnablePartialNbest;
    private boolean hasEnablePartialResults;
    private boolean hasEnableSpeakerTraining;
    private boolean hasForceTranscript;
    private boolean hasHotwordDecisionThreshold;
    private boolean hasMaskOffensiveWords;
    private boolean hasNumNbest;
    private boolean hasResetIntervalMs;
    private boolean hasSampleRate;
    private boolean hasSpeakerId;
    private boolean hasType;
    private float hotwordDecisionThreshold_ = 0.0F;
    private boolean maskOffensiveWords_ = true;
    private int numNbest_ = 0;
    private int resetIntervalMs_ = 0;
    private float sampleRate_ = 8000.0F;
    private String speakerId_ = "";
    private int type_ = 0;
    
    public Alternates.AlternateParams getAlternateParams()
    {
      return this.alternateParams_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getChannelCount()
    {
      return this.channelCount_;
    }
    
    public boolean getEnableAlternates()
    {
      return this.enableAlternates_;
    }
    
    public boolean getEnableFrameLogging()
    {
      return this.enableFrameLogging_;
    }
    
    public boolean getEnablePartialNbest()
    {
      return this.enablePartialNbest_;
    }
    
    public boolean getEnablePartialResults()
    {
      return this.enablePartialResults_;
    }
    
    public boolean getEnableSpeakerTraining()
    {
      return this.enableSpeakerTraining_;
    }
    
    public String getForceTranscript()
    {
      return this.forceTranscript_;
    }
    
    public float getHotwordDecisionThreshold()
    {
      return this.hotwordDecisionThreshold_;
    }
    
    public boolean getMaskOffensiveWords()
    {
      return this.maskOffensiveWords_;
    }
    
    public int getNumNbest()
    {
      return this.numNbest_;
    }
    
    public int getResetIntervalMs()
    {
      return this.resetIntervalMs_;
    }
    
    public float getSampleRate()
    {
      return this.sampleRate_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getType());
      }
      if (hasSampleRate()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getSampleRate());
      }
      if (hasMaskOffensiveWords()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getMaskOffensiveWords());
      }
      if (hasEnableAlternates()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getEnableAlternates());
      }
      if (hasAlternateParams()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getAlternateParams());
      }
      if (hasNumNbest()) {
        i += CodedOutputStreamMicro.computeInt32Size(6, getNumNbest());
      }
      if (hasEnablePartialResults()) {
        i += CodedOutputStreamMicro.computeBoolSize(7, getEnablePartialResults());
      }
      if (hasResetIntervalMs()) {
        i += CodedOutputStreamMicro.computeInt32Size(8, getResetIntervalMs());
      }
      if (hasHotwordDecisionThreshold()) {
        i += CodedOutputStreamMicro.computeFloatSize(9, getHotwordDecisionThreshold());
      }
      if (hasChannelCount()) {
        i += CodedOutputStreamMicro.computeInt32Size(10, getChannelCount());
      }
      if (hasEnableFrameLogging()) {
        i += CodedOutputStreamMicro.computeBoolSize(11, getEnableFrameLogging());
      }
      if (hasForceTranscript()) {
        i += CodedOutputStreamMicro.computeStringSize(12, getForceTranscript());
      }
      if (hasEnablePartialNbest()) {
        i += CodedOutputStreamMicro.computeBoolSize(13, getEnablePartialNbest());
      }
      if (hasEnableSpeakerTraining()) {
        i += CodedOutputStreamMicro.computeBoolSize(14, getEnableSpeakerTraining());
      }
      if (hasSpeakerId()) {
        i += CodedOutputStreamMicro.computeStringSize(15, getSpeakerId());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSpeakerId()
    {
      return this.speakerId_;
    }
    
    public int getType()
    {
      return this.type_;
    }
    
    public boolean hasAlternateParams()
    {
      return this.hasAlternateParams;
    }
    
    public boolean hasChannelCount()
    {
      return this.hasChannelCount;
    }
    
    public boolean hasEnableAlternates()
    {
      return this.hasEnableAlternates;
    }
    
    public boolean hasEnableFrameLogging()
    {
      return this.hasEnableFrameLogging;
    }
    
    public boolean hasEnablePartialNbest()
    {
      return this.hasEnablePartialNbest;
    }
    
    public boolean hasEnablePartialResults()
    {
      return this.hasEnablePartialResults;
    }
    
    public boolean hasEnableSpeakerTraining()
    {
      return this.hasEnableSpeakerTraining;
    }
    
    public boolean hasForceTranscript()
    {
      return this.hasForceTranscript;
    }
    
    public boolean hasHotwordDecisionThreshold()
    {
      return this.hasHotwordDecisionThreshold;
    }
    
    public boolean hasMaskOffensiveWords()
    {
      return this.hasMaskOffensiveWords;
    }
    
    public boolean hasNumNbest()
    {
      return this.hasNumNbest;
    }
    
    public boolean hasResetIntervalMs()
    {
      return this.hasResetIntervalMs;
    }
    
    public boolean hasSampleRate()
    {
      return this.hasSampleRate;
    }
    
    public boolean hasSpeakerId()
    {
      return this.hasSpeakerId;
    }
    
    public boolean hasType()
    {
      return this.hasType;
    }
    
    public RecognizerSessionParams mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setType(paramCodedInputStreamMicro.readInt32());
          break;
        case 21: 
          setSampleRate(paramCodedInputStreamMicro.readFloat());
          break;
        case 24: 
          setMaskOffensiveWords(paramCodedInputStreamMicro.readBool());
          break;
        case 32: 
          setEnableAlternates(paramCodedInputStreamMicro.readBool());
          break;
        case 42: 
          Alternates.AlternateParams localAlternateParams = new Alternates.AlternateParams();
          paramCodedInputStreamMicro.readMessage(localAlternateParams);
          setAlternateParams(localAlternateParams);
          break;
        case 48: 
          setNumNbest(paramCodedInputStreamMicro.readInt32());
          break;
        case 56: 
          setEnablePartialResults(paramCodedInputStreamMicro.readBool());
          break;
        case 64: 
          setResetIntervalMs(paramCodedInputStreamMicro.readInt32());
          break;
        case 77: 
          setHotwordDecisionThreshold(paramCodedInputStreamMicro.readFloat());
          break;
        case 80: 
          setChannelCount(paramCodedInputStreamMicro.readInt32());
          break;
        case 88: 
          setEnableFrameLogging(paramCodedInputStreamMicro.readBool());
          break;
        case 98: 
          setForceTranscript(paramCodedInputStreamMicro.readString());
          break;
        case 104: 
          setEnablePartialNbest(paramCodedInputStreamMicro.readBool());
          break;
        case 112: 
          setEnableSpeakerTraining(paramCodedInputStreamMicro.readBool());
          break;
        }
        setSpeakerId(paramCodedInputStreamMicro.readString());
      }
    }
    
    public RecognizerSessionParams setAlternateParams(Alternates.AlternateParams paramAlternateParams)
    {
      if (paramAlternateParams == null) {
        throw new NullPointerException();
      }
      this.hasAlternateParams = true;
      this.alternateParams_ = paramAlternateParams;
      return this;
    }
    
    public RecognizerSessionParams setChannelCount(int paramInt)
    {
      this.hasChannelCount = true;
      this.channelCount_ = paramInt;
      return this;
    }
    
    public RecognizerSessionParams setEnableAlternates(boolean paramBoolean)
    {
      this.hasEnableAlternates = true;
      this.enableAlternates_ = paramBoolean;
      return this;
    }
    
    public RecognizerSessionParams setEnableFrameLogging(boolean paramBoolean)
    {
      this.hasEnableFrameLogging = true;
      this.enableFrameLogging_ = paramBoolean;
      return this;
    }
    
    public RecognizerSessionParams setEnablePartialNbest(boolean paramBoolean)
    {
      this.hasEnablePartialNbest = true;
      this.enablePartialNbest_ = paramBoolean;
      return this;
    }
    
    public RecognizerSessionParams setEnablePartialResults(boolean paramBoolean)
    {
      this.hasEnablePartialResults = true;
      this.enablePartialResults_ = paramBoolean;
      return this;
    }
    
    public RecognizerSessionParams setEnableSpeakerTraining(boolean paramBoolean)
    {
      this.hasEnableSpeakerTraining = true;
      this.enableSpeakerTraining_ = paramBoolean;
      return this;
    }
    
    public RecognizerSessionParams setForceTranscript(String paramString)
    {
      this.hasForceTranscript = true;
      this.forceTranscript_ = paramString;
      return this;
    }
    
    public RecognizerSessionParams setHotwordDecisionThreshold(float paramFloat)
    {
      this.hasHotwordDecisionThreshold = true;
      this.hotwordDecisionThreshold_ = paramFloat;
      return this;
    }
    
    public RecognizerSessionParams setMaskOffensiveWords(boolean paramBoolean)
    {
      this.hasMaskOffensiveWords = true;
      this.maskOffensiveWords_ = paramBoolean;
      return this;
    }
    
    public RecognizerSessionParams setNumNbest(int paramInt)
    {
      this.hasNumNbest = true;
      this.numNbest_ = paramInt;
      return this;
    }
    
    public RecognizerSessionParams setResetIntervalMs(int paramInt)
    {
      this.hasResetIntervalMs = true;
      this.resetIntervalMs_ = paramInt;
      return this;
    }
    
    public RecognizerSessionParams setSampleRate(float paramFloat)
    {
      this.hasSampleRate = true;
      this.sampleRate_ = paramFloat;
      return this;
    }
    
    public RecognizerSessionParams setSpeakerId(String paramString)
    {
      this.hasSpeakerId = true;
      this.speakerId_ = paramString;
      return this;
    }
    
    public RecognizerSessionParams setType(int paramInt)
    {
      this.hasType = true;
      this.type_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasType()) {
        paramCodedOutputStreamMicro.writeInt32(1, getType());
      }
      if (hasSampleRate()) {
        paramCodedOutputStreamMicro.writeFloat(2, getSampleRate());
      }
      if (hasMaskOffensiveWords()) {
        paramCodedOutputStreamMicro.writeBool(3, getMaskOffensiveWords());
      }
      if (hasEnableAlternates()) {
        paramCodedOutputStreamMicro.writeBool(4, getEnableAlternates());
      }
      if (hasAlternateParams()) {
        paramCodedOutputStreamMicro.writeMessage(5, getAlternateParams());
      }
      if (hasNumNbest()) {
        paramCodedOutputStreamMicro.writeInt32(6, getNumNbest());
      }
      if (hasEnablePartialResults()) {
        paramCodedOutputStreamMicro.writeBool(7, getEnablePartialResults());
      }
      if (hasResetIntervalMs()) {
        paramCodedOutputStreamMicro.writeInt32(8, getResetIntervalMs());
      }
      if (hasHotwordDecisionThreshold()) {
        paramCodedOutputStreamMicro.writeFloat(9, getHotwordDecisionThreshold());
      }
      if (hasChannelCount()) {
        paramCodedOutputStreamMicro.writeInt32(10, getChannelCount());
      }
      if (hasEnableFrameLogging()) {
        paramCodedOutputStreamMicro.writeBool(11, getEnableFrameLogging());
      }
      if (hasForceTranscript()) {
        paramCodedOutputStreamMicro.writeString(12, getForceTranscript());
      }
      if (hasEnablePartialNbest()) {
        paramCodedOutputStreamMicro.writeBool(13, getEnablePartialNbest());
      }
      if (hasEnableSpeakerTraining()) {
        paramCodedOutputStreamMicro.writeBool(14, getEnableSpeakerTraining());
      }
      if (hasSpeakerId()) {
        paramCodedOutputStreamMicro.writeString(15, getSpeakerId());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.recognizer.api.RecognizerSessionParamsProto
 * JD-Core Version:    0.7.0.1
 */