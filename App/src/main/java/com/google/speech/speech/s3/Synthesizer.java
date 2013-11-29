package com.google.speech.speech.s3;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import com.google.speech.synthesizer.EngineSpecific.SynthesisEngineSpecificRequest;
import java.io.IOException;

public final class Synthesizer
{
  public static final class TtsAudioInfo
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasSampleRate;
    private int sampleRate_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSampleRate()
    {
      return this.sampleRate_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSampleRate();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getSampleRate());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasSampleRate()
    {
      return this.hasSampleRate;
    }
    
    public TtsAudioInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        }
        setSampleRate(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public TtsAudioInfo setSampleRate(int paramInt)
    {
      this.hasSampleRate = true;
      this.sampleRate_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSampleRate()) {
        paramCodedOutputStreamMicro.writeInt32(1, getSampleRate());
      }
    }
  }
  
  public static final class TtsCapabilitiesRequest
    extends MessageMicro
  {
    private int cachedSize = -1;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      this.cachedSize = 0;
      return 0;
    }
    
    public TtsCapabilitiesRequest mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      int i;
      do
      {
        i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        }
      } while (parseUnknownField(paramCodedInputStreamMicro, i));
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro) {}
  }
  
  public static final class TtsCapabilitiesResponse
    extends MessageMicro
  {
    private int cachedSize = -1;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      this.cachedSize = 0;
      return 0;
    }
    
    public TtsCapabilitiesResponse mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      int i;
      do
      {
        i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        }
      } while (parseUnknownField(paramCodedInputStreamMicro, i));
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro) {}
  }
  
  public static final class TtsServiceEvent
    extends MessageMicro
  {
    private Synthesizer.TtsAudioInfo audioInfo_ = null;
    private ByteStringMicro audio_ = ByteStringMicro.EMPTY;
    private int cachedSize = -1;
    private boolean endOfData_ = false;
    private boolean hasAudio;
    private boolean hasAudioInfo;
    private boolean hasEndOfData;
    
    public ByteStringMicro getAudio()
    {
      return this.audio_;
    }
    
    public Synthesizer.TtsAudioInfo getAudioInfo()
    {
      return this.audioInfo_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public boolean getEndOfData()
    {
      return this.endOfData_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasAudio();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeBytesSize(1, getAudio());
      }
      if (hasEndOfData()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getEndOfData());
      }
      if (hasAudioInfo()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getAudioInfo());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAudio()
    {
      return this.hasAudio;
    }
    
    public boolean hasAudioInfo()
    {
      return this.hasAudioInfo;
    }
    
    public boolean hasEndOfData()
    {
      return this.hasEndOfData;
    }
    
    public TtsServiceEvent mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 10: 
          setAudio(paramCodedInputStreamMicro.readBytes());
          break;
        case 16: 
          setEndOfData(paramCodedInputStreamMicro.readBool());
          break;
        }
        Synthesizer.TtsAudioInfo localTtsAudioInfo = new Synthesizer.TtsAudioInfo();
        paramCodedInputStreamMicro.readMessage(localTtsAudioInfo);
        setAudioInfo(localTtsAudioInfo);
      }
    }
    
    public TtsServiceEvent setAudio(ByteStringMicro paramByteStringMicro)
    {
      this.hasAudio = true;
      this.audio_ = paramByteStringMicro;
      return this;
    }
    
    public TtsServiceEvent setAudioInfo(Synthesizer.TtsAudioInfo paramTtsAudioInfo)
    {
      if (paramTtsAudioInfo == null) {
        throw new NullPointerException();
      }
      this.hasAudioInfo = true;
      this.audioInfo_ = paramTtsAudioInfo;
      return this;
    }
    
    public TtsServiceEvent setEndOfData(boolean paramBoolean)
    {
      this.hasEndOfData = true;
      this.endOfData_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAudio()) {
        paramCodedOutputStreamMicro.writeBytes(1, getAudio());
      }
      if (hasEndOfData()) {
        paramCodedOutputStreamMicro.writeBool(2, getEndOfData());
      }
      if (hasAudioInfo()) {
        paramCodedOutputStreamMicro.writeMessage(3, getAudioInfo());
      }
    }
  }
  
  public static final class TtsServiceRequest
    extends MessageMicro
  {
    private int audioChunkSize_ = 1024;
    private int audioEncoding_ = 4;
    private int cachedSize = -1;
    private EngineSpecific.SynthesisEngineSpecificRequest engineSpecificRequest_ = null;
    private boolean hasAudioChunkSize;
    private boolean hasAudioEncoding;
    private boolean hasEngineSpecificRequest;
    private boolean hasInputIsLoggable;
    private boolean hasSsml;
    private boolean hasSynthesisPitch;
    private boolean hasSynthesisSpeed;
    private boolean hasSynthesisText;
    private boolean hasSynthesisVolume;
    private boolean hasVoiceEngine;
    private boolean hasVoiceLanguage;
    private boolean hasVoiceName;
    private boolean hasVoiceSampleRate;
    private boolean inputIsLoggable_ = false;
    private String ssml_ = "";
    private double synthesisPitch_ = 0.0D;
    private double synthesisSpeed_ = 0.0D;
    private String synthesisText_ = "";
    private double synthesisVolume_ = 0.0D;
    private String voiceEngine_ = "";
    private String voiceLanguage_ = "";
    private String voiceName_ = "";
    private int voiceSampleRate_ = 0;
    
    public int getAudioChunkSize()
    {
      return this.audioChunkSize_;
    }
    
    public int getAudioEncoding()
    {
      return this.audioEncoding_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EngineSpecific.SynthesisEngineSpecificRequest getEngineSpecificRequest()
    {
      return this.engineSpecificRequest_;
    }
    
    public boolean getInputIsLoggable()
    {
      return this.inputIsLoggable_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSynthesisText();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSynthesisText());
      }
      if (hasSsml()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getSsml());
      }
      if (hasVoiceLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getVoiceLanguage());
      }
      if (hasVoiceName()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getVoiceName());
      }
      if (hasVoiceSampleRate()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getVoiceSampleRate());
      }
      if (hasVoiceEngine()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getVoiceEngine());
      }
      if (hasSynthesisSpeed()) {
        i += CodedOutputStreamMicro.computeDoubleSize(7, getSynthesisSpeed());
      }
      if (hasSynthesisPitch()) {
        i += CodedOutputStreamMicro.computeDoubleSize(8, getSynthesisPitch());
      }
      if (hasSynthesisVolume()) {
        i += CodedOutputStreamMicro.computeDoubleSize(9, getSynthesisVolume());
      }
      if (hasAudioEncoding()) {
        i += CodedOutputStreamMicro.computeInt32Size(10, getAudioEncoding());
      }
      if (hasAudioChunkSize()) {
        i += CodedOutputStreamMicro.computeInt32Size(11, getAudioChunkSize());
      }
      if (hasInputIsLoggable()) {
        i += CodedOutputStreamMicro.computeBoolSize(12, getInputIsLoggable());
      }
      if (hasEngineSpecificRequest()) {
        i += CodedOutputStreamMicro.computeMessageSize(13, getEngineSpecificRequest());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSsml()
    {
      return this.ssml_;
    }
    
    public double getSynthesisPitch()
    {
      return this.synthesisPitch_;
    }
    
    public double getSynthesisSpeed()
    {
      return this.synthesisSpeed_;
    }
    
    public String getSynthesisText()
    {
      return this.synthesisText_;
    }
    
    public double getSynthesisVolume()
    {
      return this.synthesisVolume_;
    }
    
    public String getVoiceEngine()
    {
      return this.voiceEngine_;
    }
    
    public String getVoiceLanguage()
    {
      return this.voiceLanguage_;
    }
    
    public String getVoiceName()
    {
      return this.voiceName_;
    }
    
    public int getVoiceSampleRate()
    {
      return this.voiceSampleRate_;
    }
    
    public boolean hasAudioChunkSize()
    {
      return this.hasAudioChunkSize;
    }
    
    public boolean hasAudioEncoding()
    {
      return this.hasAudioEncoding;
    }
    
    public boolean hasEngineSpecificRequest()
    {
      return this.hasEngineSpecificRequest;
    }
    
    public boolean hasInputIsLoggable()
    {
      return this.hasInputIsLoggable;
    }
    
    public boolean hasSsml()
    {
      return this.hasSsml;
    }
    
    public boolean hasSynthesisPitch()
    {
      return this.hasSynthesisPitch;
    }
    
    public boolean hasSynthesisSpeed()
    {
      return this.hasSynthesisSpeed;
    }
    
    public boolean hasSynthesisText()
    {
      return this.hasSynthesisText;
    }
    
    public boolean hasSynthesisVolume()
    {
      return this.hasSynthesisVolume;
    }
    
    public boolean hasVoiceEngine()
    {
      return this.hasVoiceEngine;
    }
    
    public boolean hasVoiceLanguage()
    {
      return this.hasVoiceLanguage;
    }
    
    public boolean hasVoiceName()
    {
      return this.hasVoiceName;
    }
    
    public boolean hasVoiceSampleRate()
    {
      return this.hasVoiceSampleRate;
    }
    
    public TtsServiceRequest mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 10: 
          setSynthesisText(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setSsml(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setVoiceLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setVoiceName(paramCodedInputStreamMicro.readString());
          break;
        case 40: 
          setVoiceSampleRate(paramCodedInputStreamMicro.readInt32());
          break;
        case 50: 
          setVoiceEngine(paramCodedInputStreamMicro.readString());
          break;
        case 57: 
          setSynthesisSpeed(paramCodedInputStreamMicro.readDouble());
          break;
        case 65: 
          setSynthesisPitch(paramCodedInputStreamMicro.readDouble());
          break;
        case 73: 
          setSynthesisVolume(paramCodedInputStreamMicro.readDouble());
          break;
        case 80: 
          setAudioEncoding(paramCodedInputStreamMicro.readInt32());
          break;
        case 88: 
          setAudioChunkSize(paramCodedInputStreamMicro.readInt32());
          break;
        case 96: 
          setInputIsLoggable(paramCodedInputStreamMicro.readBool());
          break;
        }
        EngineSpecific.SynthesisEngineSpecificRequest localSynthesisEngineSpecificRequest = new EngineSpecific.SynthesisEngineSpecificRequest();
        paramCodedInputStreamMicro.readMessage(localSynthesisEngineSpecificRequest);
        setEngineSpecificRequest(localSynthesisEngineSpecificRequest);
      }
    }
    
    public TtsServiceRequest setAudioChunkSize(int paramInt)
    {
      this.hasAudioChunkSize = true;
      this.audioChunkSize_ = paramInt;
      return this;
    }
    
    public TtsServiceRequest setAudioEncoding(int paramInt)
    {
      this.hasAudioEncoding = true;
      this.audioEncoding_ = paramInt;
      return this;
    }
    
    public TtsServiceRequest setEngineSpecificRequest(EngineSpecific.SynthesisEngineSpecificRequest paramSynthesisEngineSpecificRequest)
    {
      if (paramSynthesisEngineSpecificRequest == null) {
        throw new NullPointerException();
      }
      this.hasEngineSpecificRequest = true;
      this.engineSpecificRequest_ = paramSynthesisEngineSpecificRequest;
      return this;
    }
    
    public TtsServiceRequest setInputIsLoggable(boolean paramBoolean)
    {
      this.hasInputIsLoggable = true;
      this.inputIsLoggable_ = paramBoolean;
      return this;
    }
    
    public TtsServiceRequest setSsml(String paramString)
    {
      this.hasSsml = true;
      this.ssml_ = paramString;
      return this;
    }
    
    public TtsServiceRequest setSynthesisPitch(double paramDouble)
    {
      this.hasSynthesisPitch = true;
      this.synthesisPitch_ = paramDouble;
      return this;
    }
    
    public TtsServiceRequest setSynthesisSpeed(double paramDouble)
    {
      this.hasSynthesisSpeed = true;
      this.synthesisSpeed_ = paramDouble;
      return this;
    }
    
    public TtsServiceRequest setSynthesisText(String paramString)
    {
      this.hasSynthesisText = true;
      this.synthesisText_ = paramString;
      return this;
    }
    
    public TtsServiceRequest setSynthesisVolume(double paramDouble)
    {
      this.hasSynthesisVolume = true;
      this.synthesisVolume_ = paramDouble;
      return this;
    }
    
    public TtsServiceRequest setVoiceEngine(String paramString)
    {
      this.hasVoiceEngine = true;
      this.voiceEngine_ = paramString;
      return this;
    }
    
    public TtsServiceRequest setVoiceLanguage(String paramString)
    {
      this.hasVoiceLanguage = true;
      this.voiceLanguage_ = paramString;
      return this;
    }
    
    public TtsServiceRequest setVoiceName(String paramString)
    {
      this.hasVoiceName = true;
      this.voiceName_ = paramString;
      return this;
    }
    
    public TtsServiceRequest setVoiceSampleRate(int paramInt)
    {
      this.hasVoiceSampleRate = true;
      this.voiceSampleRate_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSynthesisText()) {
        paramCodedOutputStreamMicro.writeString(1, getSynthesisText());
      }
      if (hasSsml()) {
        paramCodedOutputStreamMicro.writeString(2, getSsml());
      }
      if (hasVoiceLanguage()) {
        paramCodedOutputStreamMicro.writeString(3, getVoiceLanguage());
      }
      if (hasVoiceName()) {
        paramCodedOutputStreamMicro.writeString(4, getVoiceName());
      }
      if (hasVoiceSampleRate()) {
        paramCodedOutputStreamMicro.writeInt32(5, getVoiceSampleRate());
      }
      if (hasVoiceEngine()) {
        paramCodedOutputStreamMicro.writeString(6, getVoiceEngine());
      }
      if (hasSynthesisSpeed()) {
        paramCodedOutputStreamMicro.writeDouble(7, getSynthesisSpeed());
      }
      if (hasSynthesisPitch()) {
        paramCodedOutputStreamMicro.writeDouble(8, getSynthesisPitch());
      }
      if (hasSynthesisVolume()) {
        paramCodedOutputStreamMicro.writeDouble(9, getSynthesisVolume());
      }
      if (hasAudioEncoding()) {
        paramCodedOutputStreamMicro.writeInt32(10, getAudioEncoding());
      }
      if (hasAudioChunkSize()) {
        paramCodedOutputStreamMicro.writeInt32(11, getAudioChunkSize());
      }
      if (hasInputIsLoggable()) {
        paramCodedOutputStreamMicro.writeBool(12, getInputIsLoggable());
      }
      if (hasEngineSpecificRequest()) {
        paramCodedOutputStreamMicro.writeMessage(13, getEngineSpecificRequest());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.speech.s3.Synthesizer
 * JD-Core Version:    0.7.0.1
 */