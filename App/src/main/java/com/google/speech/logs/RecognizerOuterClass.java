package com.google.speech.logs;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class RecognizerOuterClass
{
  public static final class Alternate
    extends MessageMicro
  {
    private int cachedSize = -1;
    private float confidence_ = 0.0F;
    private boolean hasConfidence;
    private boolean hasText;
    private String text_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getConfidence()
    {
      return this.confidence_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasText();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getText());
      }
      if (hasConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getConfidence());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getText()
    {
      return this.text_;
    }
    
    public boolean hasConfidence()
    {
      return this.hasConfidence;
    }
    
    public boolean hasText()
    {
      return this.hasText;
    }
    
    public Alternate mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setText(paramCodedInputStreamMicro.readString());
          break;
        }
        setConfidence(paramCodedInputStreamMicro.readFloat());
      }
    }
    
    public Alternate setConfidence(float paramFloat)
    {
      this.hasConfidence = true;
      this.confidence_ = paramFloat;
      return this;
    }
    
    public Alternate setText(String paramString)
    {
      this.hasText = true;
      this.text_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasText()) {
        paramCodedOutputStreamMicro.writeString(1, getText());
      }
      if (hasConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(2, getConfidence());
      }
    }
  }
  
  public static final class AlternateSpan
    extends MessageMicro
  {
    private List<RecognizerOuterClass.Alternate> alternates_ = Collections.emptyList();
    private int cachedSize = -1;
    private float confidence_ = 0.0F;
    private boolean hasConfidence;
    private boolean hasLength;
    private boolean hasStart;
    private int length_ = 0;
    private int start_ = 0;
    
    public AlternateSpan addAlternates(RecognizerOuterClass.Alternate paramAlternate)
    {
      if (paramAlternate == null) {
        throw new NullPointerException();
      }
      if (this.alternates_.isEmpty()) {
        this.alternates_ = new ArrayList();
      }
      this.alternates_.add(paramAlternate);
      return this;
    }
    
    public List<RecognizerOuterClass.Alternate> getAlternatesList()
    {
      return this.alternates_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getConfidence()
    {
      return this.confidence_;
    }
    
    public int getLength()
    {
      return this.length_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasStart();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getStart());
      }
      if (hasLength()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getLength());
      }
      Iterator localIterator = getAlternatesList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (RecognizerOuterClass.Alternate)localIterator.next());
      }
      if (hasConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(4, getConfidence());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getStart()
    {
      return this.start_;
    }
    
    public boolean hasConfidence()
    {
      return this.hasConfidence;
    }
    
    public boolean hasLength()
    {
      return this.hasLength;
    }
    
    public boolean hasStart()
    {
      return this.hasStart;
    }
    
    public AlternateSpan mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setStart(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setLength(paramCodedInputStreamMicro.readInt32());
          break;
        case 26: 
          RecognizerOuterClass.Alternate localAlternate = new RecognizerOuterClass.Alternate();
          paramCodedInputStreamMicro.readMessage(localAlternate);
          addAlternates(localAlternate);
          break;
        }
        setConfidence(paramCodedInputStreamMicro.readFloat());
      }
    }
    
    public AlternateSpan setConfidence(float paramFloat)
    {
      this.hasConfidence = true;
      this.confidence_ = paramFloat;
      return this;
    }
    
    public AlternateSpan setLength(int paramInt)
    {
      this.hasLength = true;
      this.length_ = paramInt;
      return this;
    }
    
    public AlternateSpan setStart(int paramInt)
    {
      this.hasStart = true;
      this.start_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasStart()) {
        paramCodedOutputStreamMicro.writeInt32(1, getStart());
      }
      if (hasLength()) {
        paramCodedOutputStreamMicro.writeInt32(2, getLength());
      }
      Iterator localIterator = getAlternatesList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (RecognizerOuterClass.Alternate)localIterator.next());
      }
      if (hasConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(4, getConfidence());
      }
    }
  }
  
  public static final class AudioFeatureLog
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int featureType_ = 0;
    private List<FrameDataLog> frameData_ = Collections.emptyList();
    private int frameDimension_ = 0;
    private float frameRateMs_ = 0.0F;
    private boolean hasFeatureType;
    private boolean hasFrameDimension;
    private boolean hasFrameRateMs;
    
    public AudioFeatureLog addFrameData(FrameDataLog paramFrameDataLog)
    {
      if (paramFrameDataLog == null) {
        throw new NullPointerException();
      }
      if (this.frameData_.isEmpty()) {
        this.frameData_ = new ArrayList();
      }
      this.frameData_.add(paramFrameDataLog);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getFeatureType()
    {
      return this.featureType_;
    }
    
    public List<FrameDataLog> getFrameDataList()
    {
      return this.frameData_;
    }
    
    public int getFrameDimension()
    {
      return this.frameDimension_;
    }
    
    public float getFrameRateMs()
    {
      return this.frameRateMs_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasFeatureType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getFeatureType());
      }
      if (hasFrameRateMs()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getFrameRateMs());
      }
      if (hasFrameDimension()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getFrameDimension());
      }
      Iterator localIterator = getFrameDataList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (FrameDataLog)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasFeatureType()
    {
      return this.hasFeatureType;
    }
    
    public boolean hasFrameDimension()
    {
      return this.hasFrameDimension;
    }
    
    public boolean hasFrameRateMs()
    {
      return this.hasFrameRateMs;
    }
    
    public AudioFeatureLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setFeatureType(paramCodedInputStreamMicro.readInt32());
          break;
        case 21: 
          setFrameRateMs(paramCodedInputStreamMicro.readFloat());
          break;
        case 24: 
          setFrameDimension(paramCodedInputStreamMicro.readInt32());
          break;
        }
        FrameDataLog localFrameDataLog = new FrameDataLog();
        paramCodedInputStreamMicro.readMessage(localFrameDataLog);
        addFrameData(localFrameDataLog);
      }
    }
    
    public AudioFeatureLog setFeatureType(int paramInt)
    {
      this.hasFeatureType = true;
      this.featureType_ = paramInt;
      return this;
    }
    
    public AudioFeatureLog setFrameDimension(int paramInt)
    {
      this.hasFrameDimension = true;
      this.frameDimension_ = paramInt;
      return this;
    }
    
    public AudioFeatureLog setFrameRateMs(float paramFloat)
    {
      this.hasFrameRateMs = true;
      this.frameRateMs_ = paramFloat;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasFeatureType()) {
        paramCodedOutputStreamMicro.writeInt32(1, getFeatureType());
      }
      if (hasFrameRateMs()) {
        paramCodedOutputStreamMicro.writeFloat(2, getFrameRateMs());
      }
      if (hasFrameDimension()) {
        paramCodedOutputStreamMicro.writeInt32(3, getFrameDimension());
      }
      Iterator localIterator = getFrameDataList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (FrameDataLog)localIterator.next());
      }
    }
    
    public static final class FrameDataLog
      extends MessageMicro
    {
      private int cachedSize = -1;
      private List<Float> value_ = Collections.emptyList();
      
      public FrameDataLog addValue(float paramFloat)
      {
        if (this.value_.isEmpty()) {
          this.value_ = new ArrayList();
        }
        this.value_.add(Float.valueOf(paramFloat));
        return this;
      }
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public int getSerializedSize()
      {
        int i = 0 + 4 * getValueList().size() + 1 * getValueList().size();
        this.cachedSize = i;
        return i;
      }
      
      public List<Float> getValueList()
      {
        return this.value_;
      }
      
      public FrameDataLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          addValue(paramCodedInputStreamMicro.readFloat());
        }
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        Iterator localIterator = getValueList().iterator();
        while (localIterator.hasNext()) {
          paramCodedOutputStreamMicro.writeFloat(1, ((Float)localIterator.next()).floatValue());
        }
      }
    }
  }
  
  public static final class LanguagePackLog
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasLocale;
    private boolean hasVersion;
    private String locale_ = "";
    private String version_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getLocale()
    {
      return this.locale_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLocale();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getLocale());
      }
      if (hasVersion()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getVersion());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getVersion()
    {
      return this.version_;
    }
    
    public boolean hasLocale()
    {
      return this.hasLocale;
    }
    
    public boolean hasVersion()
    {
      return this.hasVersion;
    }
    
    public LanguagePackLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setLocale(paramCodedInputStreamMicro.readString());
          break;
        }
        setVersion(paramCodedInputStreamMicro.readString());
      }
    }
    
    public LanguagePackLog setLocale(String paramString)
    {
      this.hasLocale = true;
      this.locale_ = paramString;
      return this;
    }
    
    public LanguagePackLog setVersion(String paramString)
    {
      this.hasVersion = true;
      this.version_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLocale()) {
        paramCodedOutputStreamMicro.writeString(1, getLocale());
      }
      if (hasVersion()) {
        paramCodedOutputStreamMicro.writeString(2, getVersion());
      }
    }
  }
  
  public static final class RecognizerAlternatesLog
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasMaxSpanLength;
    private boolean hasUnit;
    private int maxSpanLength_ = 0;
    private List<RecognizerOuterClass.AlternateSpan> span_ = Collections.emptyList();
    private int unit_ = 0;
    
    public RecognizerAlternatesLog addSpan(RecognizerOuterClass.AlternateSpan paramAlternateSpan)
    {
      if (paramAlternateSpan == null) {
        throw new NullPointerException();
      }
      if (this.span_.isEmpty()) {
        this.span_ = new ArrayList();
      }
      this.span_.add(paramAlternateSpan);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getMaxSpanLength()
    {
      return this.maxSpanLength_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getSpanList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (RecognizerOuterClass.AlternateSpan)localIterator.next());
      }
      if (hasMaxSpanLength()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getMaxSpanLength());
      }
      if (hasUnit()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getUnit());
      }
      this.cachedSize = i;
      return i;
    }
    
    public List<RecognizerOuterClass.AlternateSpan> getSpanList()
    {
      return this.span_;
    }
    
    public int getUnit()
    {
      return this.unit_;
    }
    
    public boolean hasMaxSpanLength()
    {
      return this.hasMaxSpanLength;
    }
    
    public boolean hasUnit()
    {
      return this.hasUnit;
    }
    
    public RecognizerAlternatesLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          RecognizerOuterClass.AlternateSpan localAlternateSpan = new RecognizerOuterClass.AlternateSpan();
          paramCodedInputStreamMicro.readMessage(localAlternateSpan);
          addSpan(localAlternateSpan);
          break;
        case 16: 
          setMaxSpanLength(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setUnit(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public RecognizerAlternatesLog setMaxSpanLength(int paramInt)
    {
      this.hasMaxSpanLength = true;
      this.maxSpanLength_ = paramInt;
      return this;
    }
    
    public RecognizerAlternatesLog setUnit(int paramInt)
    {
      this.hasUnit = true;
      this.unit_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getSpanList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (RecognizerOuterClass.AlternateSpan)localIterator.next());
      }
      if (hasMaxSpanLength()) {
        paramCodedOutputStreamMicro.writeInt32(2, getMaxSpanLength());
      }
      if (hasUnit()) {
        paramCodedOutputStreamMicro.writeInt32(3, getUnit());
      }
    }
  }
  
  public static final class RecognizerContextLog
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<String> enabledKeyboardLanguage_ = Collections.emptyList();
    private String fieldId_ = "";
    private String fieldName_ = "";
    private boolean hasFieldId;
    private boolean hasFieldName;
    private boolean hasHint;
    private boolean hasImeOptions;
    private boolean hasInputType;
    private boolean hasLabel;
    private boolean hasLanguageModel;
    private boolean hasSelectedKeyboardLanguage;
    private boolean hasSingleLine;
    private boolean hasVoiceSearchLanguage;
    private String hint_ = "";
    private int imeOptions_ = 0;
    private int inputType_ = 0;
    private String label_ = "";
    private String languageModel_ = "";
    private String selectedKeyboardLanguage_ = "";
    private boolean singleLine_ = false;
    private String voiceSearchLanguage_ = "";
    
    public RecognizerContextLog addEnabledKeyboardLanguage(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.enabledKeyboardLanguage_.isEmpty()) {
        this.enabledKeyboardLanguage_ = new ArrayList();
      }
      this.enabledKeyboardLanguage_.add(paramString);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<String> getEnabledKeyboardLanguageList()
    {
      return this.enabledKeyboardLanguage_;
    }
    
    public String getFieldId()
    {
      return this.fieldId_;
    }
    
    public String getFieldName()
    {
      return this.fieldName_;
    }
    
    public String getHint()
    {
      return this.hint_;
    }
    
    public int getImeOptions()
    {
      return this.imeOptions_;
    }
    
    public int getInputType()
    {
      return this.inputType_;
    }
    
    public String getLabel()
    {
      return this.label_;
    }
    
    public String getLanguageModel()
    {
      return this.languageModel_;
    }
    
    public String getSelectedKeyboardLanguage()
    {
      return this.selectedKeyboardLanguage_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasVoiceSearchLanguage();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getVoiceSearchLanguage());
      }
      if (hasFieldName()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getFieldName());
      }
      if (hasFieldId()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getFieldId());
      }
      if (hasSingleLine()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getSingleLine());
      }
      if (hasLabel()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getLabel());
      }
      if (hasHint()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getHint());
      }
      if (hasInputType()) {
        i += CodedOutputStreamMicro.computeInt32Size(7, getInputType());
      }
      if (hasImeOptions()) {
        i += CodedOutputStreamMicro.computeInt32Size(8, getImeOptions());
      }
      if (hasLanguageModel()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getLanguageModel());
      }
      if (hasSelectedKeyboardLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getSelectedKeyboardLanguage());
      }
      int j = 0;
      Iterator localIterator = getEnabledKeyboardLanguageList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getEnabledKeyboardLanguageList().size();
      this.cachedSize = k;
      return k;
    }
    
    public boolean getSingleLine()
    {
      return this.singleLine_;
    }
    
    public String getVoiceSearchLanguage()
    {
      return this.voiceSearchLanguage_;
    }
    
    public boolean hasFieldId()
    {
      return this.hasFieldId;
    }
    
    public boolean hasFieldName()
    {
      return this.hasFieldName;
    }
    
    public boolean hasHint()
    {
      return this.hasHint;
    }
    
    public boolean hasImeOptions()
    {
      return this.hasImeOptions;
    }
    
    public boolean hasInputType()
    {
      return this.hasInputType;
    }
    
    public boolean hasLabel()
    {
      return this.hasLabel;
    }
    
    public boolean hasLanguageModel()
    {
      return this.hasLanguageModel;
    }
    
    public boolean hasSelectedKeyboardLanguage()
    {
      return this.hasSelectedKeyboardLanguage;
    }
    
    public boolean hasSingleLine()
    {
      return this.hasSingleLine;
    }
    
    public boolean hasVoiceSearchLanguage()
    {
      return this.hasVoiceSearchLanguage;
    }
    
    public RecognizerContextLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setVoiceSearchLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setFieldName(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setFieldId(paramCodedInputStreamMicro.readString());
          break;
        case 32: 
          setSingleLine(paramCodedInputStreamMicro.readBool());
          break;
        case 42: 
          setLabel(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          setHint(paramCodedInputStreamMicro.readString());
          break;
        case 56: 
          setInputType(paramCodedInputStreamMicro.readInt32());
          break;
        case 64: 
          setImeOptions(paramCodedInputStreamMicro.readInt32());
          break;
        case 74: 
          setLanguageModel(paramCodedInputStreamMicro.readString());
          break;
        case 82: 
          setSelectedKeyboardLanguage(paramCodedInputStreamMicro.readString());
          break;
        }
        addEnabledKeyboardLanguage(paramCodedInputStreamMicro.readString());
      }
    }
    
    public RecognizerContextLog setFieldId(String paramString)
    {
      this.hasFieldId = true;
      this.fieldId_ = paramString;
      return this;
    }
    
    public RecognizerContextLog setFieldName(String paramString)
    {
      this.hasFieldName = true;
      this.fieldName_ = paramString;
      return this;
    }
    
    public RecognizerContextLog setHint(String paramString)
    {
      this.hasHint = true;
      this.hint_ = paramString;
      return this;
    }
    
    public RecognizerContextLog setImeOptions(int paramInt)
    {
      this.hasImeOptions = true;
      this.imeOptions_ = paramInt;
      return this;
    }
    
    public RecognizerContextLog setInputType(int paramInt)
    {
      this.hasInputType = true;
      this.inputType_ = paramInt;
      return this;
    }
    
    public RecognizerContextLog setLabel(String paramString)
    {
      this.hasLabel = true;
      this.label_ = paramString;
      return this;
    }
    
    public RecognizerContextLog setLanguageModel(String paramString)
    {
      this.hasLanguageModel = true;
      this.languageModel_ = paramString;
      return this;
    }
    
    public RecognizerContextLog setSelectedKeyboardLanguage(String paramString)
    {
      this.hasSelectedKeyboardLanguage = true;
      this.selectedKeyboardLanguage_ = paramString;
      return this;
    }
    
    public RecognizerContextLog setSingleLine(boolean paramBoolean)
    {
      this.hasSingleLine = true;
      this.singleLine_ = paramBoolean;
      return this;
    }
    
    public RecognizerContextLog setVoiceSearchLanguage(String paramString)
    {
      this.hasVoiceSearchLanguage = true;
      this.voiceSearchLanguage_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasVoiceSearchLanguage()) {
        paramCodedOutputStreamMicro.writeString(1, getVoiceSearchLanguage());
      }
      if (hasFieldName()) {
        paramCodedOutputStreamMicro.writeString(2, getFieldName());
      }
      if (hasFieldId()) {
        paramCodedOutputStreamMicro.writeString(3, getFieldId());
      }
      if (hasSingleLine()) {
        paramCodedOutputStreamMicro.writeBool(4, getSingleLine());
      }
      if (hasLabel()) {
        paramCodedOutputStreamMicro.writeString(5, getLabel());
      }
      if (hasHint()) {
        paramCodedOutputStreamMicro.writeString(6, getHint());
      }
      if (hasInputType()) {
        paramCodedOutputStreamMicro.writeInt32(7, getInputType());
      }
      if (hasImeOptions()) {
        paramCodedOutputStreamMicro.writeInt32(8, getImeOptions());
      }
      if (hasLanguageModel()) {
        paramCodedOutputStreamMicro.writeString(9, getLanguageModel());
      }
      if (hasSelectedKeyboardLanguage()) {
        paramCodedOutputStreamMicro.writeString(10, getSelectedKeyboardLanguage());
      }
      Iterator localIterator = getEnabledKeyboardLanguageList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(11, (String)localIterator.next());
      }
    }
  }
  
  public static final class RecognizerHypothesisLog
    extends MessageMicro
  {
    private RecognizerOuterClass.RecognizerAlternatesLog alternates_ = null;
    private float amCost_ = 0.0F;
    private int cachedSize = -1;
    private float confidence_ = 0.0F;
    private boolean hasAlternates;
    private boolean hasAmCost;
    private boolean hasConfidence;
    private boolean hasHypothesis;
    private boolean hasIsRedacted;
    private boolean hasLmCost;
    private boolean hasPrenormHypothesis;
    private boolean hasRecognizerCost;
    private String hypothesis_ = "";
    private boolean isRedacted_ = false;
    private float lmCost_ = 0.0F;
    private String prenormHypothesis_ = "";
    private float recognizerCost_ = 0.0F;
    
    public RecognizerOuterClass.RecognizerAlternatesLog getAlternates()
    {
      return this.alternates_;
    }
    
    public float getAmCost()
    {
      return this.amCost_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getConfidence()
    {
      return this.confidence_;
    }
    
    public String getHypothesis()
    {
      return this.hypothesis_;
    }
    
    public boolean getIsRedacted()
    {
      return this.isRedacted_;
    }
    
    public float getLmCost()
    {
      return this.lmCost_;
    }
    
    public String getPrenormHypothesis()
    {
      return this.prenormHypothesis_;
    }
    
    public float getRecognizerCost()
    {
      return this.recognizerCost_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasPrenormHypothesis();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getPrenormHypothesis());
      }
      if (hasHypothesis()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getHypothesis());
      }
      if (hasConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(3, getConfidence());
      }
      if (hasRecognizerCost()) {
        i += CodedOutputStreamMicro.computeFloatSize(4, getRecognizerCost());
      }
      if (hasAmCost()) {
        i += CodedOutputStreamMicro.computeFloatSize(5, getAmCost());
      }
      if (hasLmCost()) {
        i += CodedOutputStreamMicro.computeFloatSize(6, getLmCost());
      }
      if (hasIsRedacted()) {
        i += CodedOutputStreamMicro.computeBoolSize(7, getIsRedacted());
      }
      if (hasAlternates()) {
        i += CodedOutputStreamMicro.computeMessageSize(8, getAlternates());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAlternates()
    {
      return this.hasAlternates;
    }
    
    public boolean hasAmCost()
    {
      return this.hasAmCost;
    }
    
    public boolean hasConfidence()
    {
      return this.hasConfidence;
    }
    
    public boolean hasHypothesis()
    {
      return this.hasHypothesis;
    }
    
    public boolean hasIsRedacted()
    {
      return this.hasIsRedacted;
    }
    
    public boolean hasLmCost()
    {
      return this.hasLmCost;
    }
    
    public boolean hasPrenormHypothesis()
    {
      return this.hasPrenormHypothesis;
    }
    
    public boolean hasRecognizerCost()
    {
      return this.hasRecognizerCost;
    }
    
    public RecognizerHypothesisLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setPrenormHypothesis(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setHypothesis(paramCodedInputStreamMicro.readString());
          break;
        case 29: 
          setConfidence(paramCodedInputStreamMicro.readFloat());
          break;
        case 37: 
          setRecognizerCost(paramCodedInputStreamMicro.readFloat());
          break;
        case 45: 
          setAmCost(paramCodedInputStreamMicro.readFloat());
          break;
        case 53: 
          setLmCost(paramCodedInputStreamMicro.readFloat());
          break;
        case 56: 
          setIsRedacted(paramCodedInputStreamMicro.readBool());
          break;
        }
        RecognizerOuterClass.RecognizerAlternatesLog localRecognizerAlternatesLog = new RecognizerOuterClass.RecognizerAlternatesLog();
        paramCodedInputStreamMicro.readMessage(localRecognizerAlternatesLog);
        setAlternates(localRecognizerAlternatesLog);
      }
    }
    
    public RecognizerHypothesisLog setAlternates(RecognizerOuterClass.RecognizerAlternatesLog paramRecognizerAlternatesLog)
    {
      if (paramRecognizerAlternatesLog == null) {
        throw new NullPointerException();
      }
      this.hasAlternates = true;
      this.alternates_ = paramRecognizerAlternatesLog;
      return this;
    }
    
    public RecognizerHypothesisLog setAmCost(float paramFloat)
    {
      this.hasAmCost = true;
      this.amCost_ = paramFloat;
      return this;
    }
    
    public RecognizerHypothesisLog setConfidence(float paramFloat)
    {
      this.hasConfidence = true;
      this.confidence_ = paramFloat;
      return this;
    }
    
    public RecognizerHypothesisLog setHypothesis(String paramString)
    {
      this.hasHypothesis = true;
      this.hypothesis_ = paramString;
      return this;
    }
    
    public RecognizerHypothesisLog setIsRedacted(boolean paramBoolean)
    {
      this.hasIsRedacted = true;
      this.isRedacted_ = paramBoolean;
      return this;
    }
    
    public RecognizerHypothesisLog setLmCost(float paramFloat)
    {
      this.hasLmCost = true;
      this.lmCost_ = paramFloat;
      return this;
    }
    
    public RecognizerHypothesisLog setPrenormHypothesis(String paramString)
    {
      this.hasPrenormHypothesis = true;
      this.prenormHypothesis_ = paramString;
      return this;
    }
    
    public RecognizerHypothesisLog setRecognizerCost(float paramFloat)
    {
      this.hasRecognizerCost = true;
      this.recognizerCost_ = paramFloat;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasPrenormHypothesis()) {
        paramCodedOutputStreamMicro.writeString(1, getPrenormHypothesis());
      }
      if (hasHypothesis()) {
        paramCodedOutputStreamMicro.writeString(2, getHypothesis());
      }
      if (hasConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(3, getConfidence());
      }
      if (hasRecognizerCost()) {
        paramCodedOutputStreamMicro.writeFloat(4, getRecognizerCost());
      }
      if (hasAmCost()) {
        paramCodedOutputStreamMicro.writeFloat(5, getAmCost());
      }
      if (hasLmCost()) {
        paramCodedOutputStreamMicro.writeFloat(6, getLmCost());
      }
      if (hasIsRedacted()) {
        paramCodedOutputStreamMicro.writeBool(7, getIsRedacted());
      }
      if (hasAlternates()) {
        paramCodedOutputStreamMicro.writeMessage(8, getAlternates());
      }
    }
  }
  
  public static final class RecognizerLog
    extends MessageMicro
  {
    private int audioEncoding_ = 0;
    private float averageConfidence_ = 0.0F;
    private int cachedSize = -1;
    private int channelCount_ = 0;
    private String dEPRECATEDAcousticModelVersion_ = "";
    private String dEPRECATEDLanguageModelVersion_ = "";
    private String dEPRECATEDLexiconVersion_ = "";
    private String dEPRECATEDTextnormVersion_ = "";
    private int decoderGaussianSelectionCentroids_ = 0;
    private float decoderLmWeight_ = 0.0F;
    private float decoderLocalBeam_ = 0.0F;
    private int decoderMaxArcs_ = 0;
    private float decoderWordPen_ = 0.0F;
    private RecognizerOuterClass.AudioFeatureLog features_ = null;
    private boolean hasAudioEncoding;
    private boolean hasAverageConfidence;
    private boolean hasChannelCount;
    private boolean hasDEPRECATEDAcousticModelVersion;
    private boolean hasDEPRECATEDLanguageModelVersion;
    private boolean hasDEPRECATEDLexiconVersion;
    private boolean hasDEPRECATEDTextnormVersion;
    private boolean hasDecoderGaussianSelectionCentroids;
    private boolean hasDecoderLmWeight;
    private boolean hasDecoderLocalBeam;
    private boolean hasDecoderMaxArcs;
    private boolean hasDecoderWordPen;
    private boolean hasFeatures;
    private boolean hasLangPack;
    private boolean hasNoiseCancelerEnabled;
    private boolean hasPersonalizationEnabled;
    private boolean hasRecognizerContext;
    private boolean hasRecognizerLanguage;
    private boolean hasRecognizerStatus;
    private boolean hasRequestDurationMs;
    private boolean hasSampleRate;
    private boolean hasServerCluster;
    private boolean hasServerMachineName;
    private boolean hasSpokenLanguage;
    private boolean hasStartTimeMs;
    private boolean hasTopHypothesis;
    private boolean hasTotalAudioDurationMs;
    private boolean hasUtteranceId;
    private boolean hasWaveform;
    private RecognizerOuterClass.LanguagePackLog langPack_ = null;
    private boolean noiseCancelerEnabled_ = false;
    private boolean personalizationEnabled_ = false;
    private RecognizerOuterClass.RecognizerContextLog recognizerContext_ = null;
    private String recognizerLanguage_ = "";
    private List<RecognizerOuterClass.RecognizerSegmentLog> recognizerSegment_ = Collections.emptyList();
    private int recognizerStatus_ = 0;
    private int requestDurationMs_ = 0;
    private float sampleRate_ = 0.0F;
    private String serverCluster_ = "";
    private String serverMachineName_ = "";
    private String spokenLanguage_ = "";
    private long startTimeMs_ = 0L;
    private RecognizerOuterClass.RecognizerHypothesisLog topHypothesis_ = null;
    private int totalAudioDurationMs_ = 0;
    private String utteranceId_ = "";
    private ByteStringMicro waveform_ = ByteStringMicro.EMPTY;
    
    public RecognizerLog addRecognizerSegment(RecognizerOuterClass.RecognizerSegmentLog paramRecognizerSegmentLog)
    {
      if (paramRecognizerSegmentLog == null) {
        throw new NullPointerException();
      }
      if (this.recognizerSegment_.isEmpty()) {
        this.recognizerSegment_ = new ArrayList();
      }
      this.recognizerSegment_.add(paramRecognizerSegmentLog);
      return this;
    }
    
    public int getAudioEncoding()
    {
      return this.audioEncoding_;
    }
    
    public float getAverageConfidence()
    {
      return this.averageConfidence_;
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
    
    public String getDEPRECATEDAcousticModelVersion()
    {
      return this.dEPRECATEDAcousticModelVersion_;
    }
    
    public String getDEPRECATEDLanguageModelVersion()
    {
      return this.dEPRECATEDLanguageModelVersion_;
    }
    
    public String getDEPRECATEDLexiconVersion()
    {
      return this.dEPRECATEDLexiconVersion_;
    }
    
    public String getDEPRECATEDTextnormVersion()
    {
      return this.dEPRECATEDTextnormVersion_;
    }
    
    public int getDecoderGaussianSelectionCentroids()
    {
      return this.decoderGaussianSelectionCentroids_;
    }
    
    public float getDecoderLmWeight()
    {
      return this.decoderLmWeight_;
    }
    
    public float getDecoderLocalBeam()
    {
      return this.decoderLocalBeam_;
    }
    
    public int getDecoderMaxArcs()
    {
      return this.decoderMaxArcs_;
    }
    
    public float getDecoderWordPen()
    {
      return this.decoderWordPen_;
    }
    
    public RecognizerOuterClass.AudioFeatureLog getFeatures()
    {
      return this.features_;
    }
    
    public RecognizerOuterClass.LanguagePackLog getLangPack()
    {
      return this.langPack_;
    }
    
    public boolean getNoiseCancelerEnabled()
    {
      return this.noiseCancelerEnabled_;
    }
    
    public boolean getPersonalizationEnabled()
    {
      return this.personalizationEnabled_;
    }
    
    public RecognizerOuterClass.RecognizerContextLog getRecognizerContext()
    {
      return this.recognizerContext_;
    }
    
    public String getRecognizerLanguage()
    {
      return this.recognizerLanguage_;
    }
    
    public List<RecognizerOuterClass.RecognizerSegmentLog> getRecognizerSegmentList()
    {
      return this.recognizerSegment_;
    }
    
    public int getRecognizerStatus()
    {
      return this.recognizerStatus_;
    }
    
    public int getRequestDurationMs()
    {
      return this.requestDurationMs_;
    }
    
    public float getSampleRate()
    {
      return this.sampleRate_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasUtteranceId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getUtteranceId());
      }
      if (hasWaveform()) {
        i += CodedOutputStreamMicro.computeBytesSize(2, getWaveform());
      }
      if (hasAudioEncoding()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getAudioEncoding());
      }
      if (hasSampleRate()) {
        i += CodedOutputStreamMicro.computeFloatSize(4, getSampleRate());
      }
      if (hasRecognizerContext()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getRecognizerContext());
      }
      if (hasDEPRECATEDAcousticModelVersion()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getDEPRECATEDAcousticModelVersion());
      }
      if (hasDEPRECATEDLanguageModelVersion()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getDEPRECATEDLanguageModelVersion());
      }
      if (hasDEPRECATEDTextnormVersion()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getDEPRECATEDTextnormVersion());
      }
      if (hasDEPRECATEDLexiconVersion()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getDEPRECATEDLexiconVersion());
      }
      if (hasDecoderMaxArcs()) {
        i += CodedOutputStreamMicro.computeInt32Size(10, getDecoderMaxArcs());
      }
      if (hasDecoderLocalBeam()) {
        i += CodedOutputStreamMicro.computeFloatSize(11, getDecoderLocalBeam());
      }
      if (hasDecoderWordPen()) {
        i += CodedOutputStreamMicro.computeFloatSize(12, getDecoderWordPen());
      }
      if (hasDecoderLmWeight()) {
        i += CodedOutputStreamMicro.computeFloatSize(13, getDecoderLmWeight());
      }
      if (hasDecoderGaussianSelectionCentroids()) {
        i += CodedOutputStreamMicro.computeInt32Size(14, getDecoderGaussianSelectionCentroids());
      }
      if (hasNoiseCancelerEnabled()) {
        i += CodedOutputStreamMicro.computeBoolSize(15, getNoiseCancelerEnabled());
      }
      if (hasTopHypothesis()) {
        i += CodedOutputStreamMicro.computeMessageSize(16, getTopHypothesis());
      }
      if (hasTotalAudioDurationMs()) {
        i += CodedOutputStreamMicro.computeInt32Size(17, getTotalAudioDurationMs());
      }
      if (hasAverageConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(18, getAverageConfidence());
      }
      if (hasRecognizerStatus()) {
        i += CodedOutputStreamMicro.computeInt32Size(19, getRecognizerStatus());
      }
      if (hasSpokenLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(20, getSpokenLanguage());
      }
      if (hasRecognizerLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(21, getRecognizerLanguage());
      }
      Iterator localIterator = getRecognizerSegmentList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(22, (RecognizerOuterClass.RecognizerSegmentLog)localIterator.next());
      }
      if (hasServerCluster()) {
        i += CodedOutputStreamMicro.computeStringSize(23, getServerCluster());
      }
      if (hasRequestDurationMs()) {
        i += CodedOutputStreamMicro.computeInt32Size(24, getRequestDurationMs());
      }
      if (hasPersonalizationEnabled()) {
        i += CodedOutputStreamMicro.computeBoolSize(25, getPersonalizationEnabled());
      }
      if (hasStartTimeMs()) {
        i += CodedOutputStreamMicro.computeInt64Size(26, getStartTimeMs());
      }
      if (hasLangPack()) {
        i += CodedOutputStreamMicro.computeMessageSize(27, getLangPack());
      }
      if (hasServerMachineName()) {
        i += CodedOutputStreamMicro.computeStringSize(28, getServerMachineName());
      }
      if (hasChannelCount()) {
        i += CodedOutputStreamMicro.computeInt32Size(29, getChannelCount());
      }
      if (hasFeatures()) {
        i += CodedOutputStreamMicro.computeMessageSize(30, getFeatures());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getServerCluster()
    {
      return this.serverCluster_;
    }
    
    public String getServerMachineName()
    {
      return this.serverMachineName_;
    }
    
    public String getSpokenLanguage()
    {
      return this.spokenLanguage_;
    }
    
    public long getStartTimeMs()
    {
      return this.startTimeMs_;
    }
    
    public RecognizerOuterClass.RecognizerHypothesisLog getTopHypothesis()
    {
      return this.topHypothesis_;
    }
    
    public int getTotalAudioDurationMs()
    {
      return this.totalAudioDurationMs_;
    }
    
    public String getUtteranceId()
    {
      return this.utteranceId_;
    }
    
    public ByteStringMicro getWaveform()
    {
      return this.waveform_;
    }
    
    public boolean hasAudioEncoding()
    {
      return this.hasAudioEncoding;
    }
    
    public boolean hasAverageConfidence()
    {
      return this.hasAverageConfidence;
    }
    
    public boolean hasChannelCount()
    {
      return this.hasChannelCount;
    }
    
    public boolean hasDEPRECATEDAcousticModelVersion()
    {
      return this.hasDEPRECATEDAcousticModelVersion;
    }
    
    public boolean hasDEPRECATEDLanguageModelVersion()
    {
      return this.hasDEPRECATEDLanguageModelVersion;
    }
    
    public boolean hasDEPRECATEDLexiconVersion()
    {
      return this.hasDEPRECATEDLexiconVersion;
    }
    
    public boolean hasDEPRECATEDTextnormVersion()
    {
      return this.hasDEPRECATEDTextnormVersion;
    }
    
    public boolean hasDecoderGaussianSelectionCentroids()
    {
      return this.hasDecoderGaussianSelectionCentroids;
    }
    
    public boolean hasDecoderLmWeight()
    {
      return this.hasDecoderLmWeight;
    }
    
    public boolean hasDecoderLocalBeam()
    {
      return this.hasDecoderLocalBeam;
    }
    
    public boolean hasDecoderMaxArcs()
    {
      return this.hasDecoderMaxArcs;
    }
    
    public boolean hasDecoderWordPen()
    {
      return this.hasDecoderWordPen;
    }
    
    public boolean hasFeatures()
    {
      return this.hasFeatures;
    }
    
    public boolean hasLangPack()
    {
      return this.hasLangPack;
    }
    
    public boolean hasNoiseCancelerEnabled()
    {
      return this.hasNoiseCancelerEnabled;
    }
    
    public boolean hasPersonalizationEnabled()
    {
      return this.hasPersonalizationEnabled;
    }
    
    public boolean hasRecognizerContext()
    {
      return this.hasRecognizerContext;
    }
    
    public boolean hasRecognizerLanguage()
    {
      return this.hasRecognizerLanguage;
    }
    
    public boolean hasRecognizerStatus()
    {
      return this.hasRecognizerStatus;
    }
    
    public boolean hasRequestDurationMs()
    {
      return this.hasRequestDurationMs;
    }
    
    public boolean hasSampleRate()
    {
      return this.hasSampleRate;
    }
    
    public boolean hasServerCluster()
    {
      return this.hasServerCluster;
    }
    
    public boolean hasServerMachineName()
    {
      return this.hasServerMachineName;
    }
    
    public boolean hasSpokenLanguage()
    {
      return this.hasSpokenLanguage;
    }
    
    public boolean hasStartTimeMs()
    {
      return this.hasStartTimeMs;
    }
    
    public boolean hasTopHypothesis()
    {
      return this.hasTopHypothesis;
    }
    
    public boolean hasTotalAudioDurationMs()
    {
      return this.hasTotalAudioDurationMs;
    }
    
    public boolean hasUtteranceId()
    {
      return this.hasUtteranceId;
    }
    
    public boolean hasWaveform()
    {
      return this.hasWaveform;
    }
    
    public RecognizerLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setUtteranceId(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setWaveform(paramCodedInputStreamMicro.readBytes());
          break;
        case 24: 
          setAudioEncoding(paramCodedInputStreamMicro.readInt32());
          break;
        case 37: 
          setSampleRate(paramCodedInputStreamMicro.readFloat());
          break;
        case 42: 
          RecognizerOuterClass.RecognizerContextLog localRecognizerContextLog = new RecognizerOuterClass.RecognizerContextLog();
          paramCodedInputStreamMicro.readMessage(localRecognizerContextLog);
          setRecognizerContext(localRecognizerContextLog);
          break;
        case 50: 
          setDEPRECATEDAcousticModelVersion(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          setDEPRECATEDLanguageModelVersion(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setDEPRECATEDTextnormVersion(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          setDEPRECATEDLexiconVersion(paramCodedInputStreamMicro.readString());
          break;
        case 80: 
          setDecoderMaxArcs(paramCodedInputStreamMicro.readInt32());
          break;
        case 93: 
          setDecoderLocalBeam(paramCodedInputStreamMicro.readFloat());
          break;
        case 101: 
          setDecoderWordPen(paramCodedInputStreamMicro.readFloat());
          break;
        case 109: 
          setDecoderLmWeight(paramCodedInputStreamMicro.readFloat());
          break;
        case 112: 
          setDecoderGaussianSelectionCentroids(paramCodedInputStreamMicro.readInt32());
          break;
        case 120: 
          setNoiseCancelerEnabled(paramCodedInputStreamMicro.readBool());
          break;
        case 130: 
          RecognizerOuterClass.RecognizerHypothesisLog localRecognizerHypothesisLog = new RecognizerOuterClass.RecognizerHypothesisLog();
          paramCodedInputStreamMicro.readMessage(localRecognizerHypothesisLog);
          setTopHypothesis(localRecognizerHypothesisLog);
          break;
        case 136: 
          setTotalAudioDurationMs(paramCodedInputStreamMicro.readInt32());
          break;
        case 149: 
          setAverageConfidence(paramCodedInputStreamMicro.readFloat());
          break;
        case 152: 
          setRecognizerStatus(paramCodedInputStreamMicro.readInt32());
          break;
        case 162: 
          setSpokenLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 170: 
          setRecognizerLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 178: 
          RecognizerOuterClass.RecognizerSegmentLog localRecognizerSegmentLog = new RecognizerOuterClass.RecognizerSegmentLog();
          paramCodedInputStreamMicro.readMessage(localRecognizerSegmentLog);
          addRecognizerSegment(localRecognizerSegmentLog);
          break;
        case 186: 
          setServerCluster(paramCodedInputStreamMicro.readString());
          break;
        case 192: 
          setRequestDurationMs(paramCodedInputStreamMicro.readInt32());
          break;
        case 200: 
          setPersonalizationEnabled(paramCodedInputStreamMicro.readBool());
          break;
        case 208: 
          setStartTimeMs(paramCodedInputStreamMicro.readInt64());
          break;
        case 218: 
          RecognizerOuterClass.LanguagePackLog localLanguagePackLog = new RecognizerOuterClass.LanguagePackLog();
          paramCodedInputStreamMicro.readMessage(localLanguagePackLog);
          setLangPack(localLanguagePackLog);
          break;
        case 226: 
          setServerMachineName(paramCodedInputStreamMicro.readString());
          break;
        case 232: 
          setChannelCount(paramCodedInputStreamMicro.readInt32());
          break;
        }
        RecognizerOuterClass.AudioFeatureLog localAudioFeatureLog = new RecognizerOuterClass.AudioFeatureLog();
        paramCodedInputStreamMicro.readMessage(localAudioFeatureLog);
        setFeatures(localAudioFeatureLog);
      }
    }
    
    public RecognizerLog setAudioEncoding(int paramInt)
    {
      this.hasAudioEncoding = true;
      this.audioEncoding_ = paramInt;
      return this;
    }
    
    public RecognizerLog setAverageConfidence(float paramFloat)
    {
      this.hasAverageConfidence = true;
      this.averageConfidence_ = paramFloat;
      return this;
    }
    
    public RecognizerLog setChannelCount(int paramInt)
    {
      this.hasChannelCount = true;
      this.channelCount_ = paramInt;
      return this;
    }
    
    public RecognizerLog setDEPRECATEDAcousticModelVersion(String paramString)
    {
      this.hasDEPRECATEDAcousticModelVersion = true;
      this.dEPRECATEDAcousticModelVersion_ = paramString;
      return this;
    }
    
    public RecognizerLog setDEPRECATEDLanguageModelVersion(String paramString)
    {
      this.hasDEPRECATEDLanguageModelVersion = true;
      this.dEPRECATEDLanguageModelVersion_ = paramString;
      return this;
    }
    
    public RecognizerLog setDEPRECATEDLexiconVersion(String paramString)
    {
      this.hasDEPRECATEDLexiconVersion = true;
      this.dEPRECATEDLexiconVersion_ = paramString;
      return this;
    }
    
    public RecognizerLog setDEPRECATEDTextnormVersion(String paramString)
    {
      this.hasDEPRECATEDTextnormVersion = true;
      this.dEPRECATEDTextnormVersion_ = paramString;
      return this;
    }
    
    public RecognizerLog setDecoderGaussianSelectionCentroids(int paramInt)
    {
      this.hasDecoderGaussianSelectionCentroids = true;
      this.decoderGaussianSelectionCentroids_ = paramInt;
      return this;
    }
    
    public RecognizerLog setDecoderLmWeight(float paramFloat)
    {
      this.hasDecoderLmWeight = true;
      this.decoderLmWeight_ = paramFloat;
      return this;
    }
    
    public RecognizerLog setDecoderLocalBeam(float paramFloat)
    {
      this.hasDecoderLocalBeam = true;
      this.decoderLocalBeam_ = paramFloat;
      return this;
    }
    
    public RecognizerLog setDecoderMaxArcs(int paramInt)
    {
      this.hasDecoderMaxArcs = true;
      this.decoderMaxArcs_ = paramInt;
      return this;
    }
    
    public RecognizerLog setDecoderWordPen(float paramFloat)
    {
      this.hasDecoderWordPen = true;
      this.decoderWordPen_ = paramFloat;
      return this;
    }
    
    public RecognizerLog setFeatures(RecognizerOuterClass.AudioFeatureLog paramAudioFeatureLog)
    {
      if (paramAudioFeatureLog == null) {
        throw new NullPointerException();
      }
      this.hasFeatures = true;
      this.features_ = paramAudioFeatureLog;
      return this;
    }
    
    public RecognizerLog setLangPack(RecognizerOuterClass.LanguagePackLog paramLanguagePackLog)
    {
      if (paramLanguagePackLog == null) {
        throw new NullPointerException();
      }
      this.hasLangPack = true;
      this.langPack_ = paramLanguagePackLog;
      return this;
    }
    
    public RecognizerLog setNoiseCancelerEnabled(boolean paramBoolean)
    {
      this.hasNoiseCancelerEnabled = true;
      this.noiseCancelerEnabled_ = paramBoolean;
      return this;
    }
    
    public RecognizerLog setPersonalizationEnabled(boolean paramBoolean)
    {
      this.hasPersonalizationEnabled = true;
      this.personalizationEnabled_ = paramBoolean;
      return this;
    }
    
    public RecognizerLog setRecognizerContext(RecognizerOuterClass.RecognizerContextLog paramRecognizerContextLog)
    {
      if (paramRecognizerContextLog == null) {
        throw new NullPointerException();
      }
      this.hasRecognizerContext = true;
      this.recognizerContext_ = paramRecognizerContextLog;
      return this;
    }
    
    public RecognizerLog setRecognizerLanguage(String paramString)
    {
      this.hasRecognizerLanguage = true;
      this.recognizerLanguage_ = paramString;
      return this;
    }
    
    public RecognizerLog setRecognizerStatus(int paramInt)
    {
      this.hasRecognizerStatus = true;
      this.recognizerStatus_ = paramInt;
      return this;
    }
    
    public RecognizerLog setRequestDurationMs(int paramInt)
    {
      this.hasRequestDurationMs = true;
      this.requestDurationMs_ = paramInt;
      return this;
    }
    
    public RecognizerLog setSampleRate(float paramFloat)
    {
      this.hasSampleRate = true;
      this.sampleRate_ = paramFloat;
      return this;
    }
    
    public RecognizerLog setServerCluster(String paramString)
    {
      this.hasServerCluster = true;
      this.serverCluster_ = paramString;
      return this;
    }
    
    public RecognizerLog setServerMachineName(String paramString)
    {
      this.hasServerMachineName = true;
      this.serverMachineName_ = paramString;
      return this;
    }
    
    public RecognizerLog setSpokenLanguage(String paramString)
    {
      this.hasSpokenLanguage = true;
      this.spokenLanguage_ = paramString;
      return this;
    }
    
    public RecognizerLog setStartTimeMs(long paramLong)
    {
      this.hasStartTimeMs = true;
      this.startTimeMs_ = paramLong;
      return this;
    }
    
    public RecognizerLog setTopHypothesis(RecognizerOuterClass.RecognizerHypothesisLog paramRecognizerHypothesisLog)
    {
      if (paramRecognizerHypothesisLog == null) {
        throw new NullPointerException();
      }
      this.hasTopHypothesis = true;
      this.topHypothesis_ = paramRecognizerHypothesisLog;
      return this;
    }
    
    public RecognizerLog setTotalAudioDurationMs(int paramInt)
    {
      this.hasTotalAudioDurationMs = true;
      this.totalAudioDurationMs_ = paramInt;
      return this;
    }
    
    public RecognizerLog setUtteranceId(String paramString)
    {
      this.hasUtteranceId = true;
      this.utteranceId_ = paramString;
      return this;
    }
    
    public RecognizerLog setWaveform(ByteStringMicro paramByteStringMicro)
    {
      this.hasWaveform = true;
      this.waveform_ = paramByteStringMicro;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasUtteranceId()) {
        paramCodedOutputStreamMicro.writeString(1, getUtteranceId());
      }
      if (hasWaveform()) {
        paramCodedOutputStreamMicro.writeBytes(2, getWaveform());
      }
      if (hasAudioEncoding()) {
        paramCodedOutputStreamMicro.writeInt32(3, getAudioEncoding());
      }
      if (hasSampleRate()) {
        paramCodedOutputStreamMicro.writeFloat(4, getSampleRate());
      }
      if (hasRecognizerContext()) {
        paramCodedOutputStreamMicro.writeMessage(5, getRecognizerContext());
      }
      if (hasDEPRECATEDAcousticModelVersion()) {
        paramCodedOutputStreamMicro.writeString(6, getDEPRECATEDAcousticModelVersion());
      }
      if (hasDEPRECATEDLanguageModelVersion()) {
        paramCodedOutputStreamMicro.writeString(7, getDEPRECATEDLanguageModelVersion());
      }
      if (hasDEPRECATEDTextnormVersion()) {
        paramCodedOutputStreamMicro.writeString(8, getDEPRECATEDTextnormVersion());
      }
      if (hasDEPRECATEDLexiconVersion()) {
        paramCodedOutputStreamMicro.writeString(9, getDEPRECATEDLexiconVersion());
      }
      if (hasDecoderMaxArcs()) {
        paramCodedOutputStreamMicro.writeInt32(10, getDecoderMaxArcs());
      }
      if (hasDecoderLocalBeam()) {
        paramCodedOutputStreamMicro.writeFloat(11, getDecoderLocalBeam());
      }
      if (hasDecoderWordPen()) {
        paramCodedOutputStreamMicro.writeFloat(12, getDecoderWordPen());
      }
      if (hasDecoderLmWeight()) {
        paramCodedOutputStreamMicro.writeFloat(13, getDecoderLmWeight());
      }
      if (hasDecoderGaussianSelectionCentroids()) {
        paramCodedOutputStreamMicro.writeInt32(14, getDecoderGaussianSelectionCentroids());
      }
      if (hasNoiseCancelerEnabled()) {
        paramCodedOutputStreamMicro.writeBool(15, getNoiseCancelerEnabled());
      }
      if (hasTopHypothesis()) {
        paramCodedOutputStreamMicro.writeMessage(16, getTopHypothesis());
      }
      if (hasTotalAudioDurationMs()) {
        paramCodedOutputStreamMicro.writeInt32(17, getTotalAudioDurationMs());
      }
      if (hasAverageConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(18, getAverageConfidence());
      }
      if (hasRecognizerStatus()) {
        paramCodedOutputStreamMicro.writeInt32(19, getRecognizerStatus());
      }
      if (hasSpokenLanguage()) {
        paramCodedOutputStreamMicro.writeString(20, getSpokenLanguage());
      }
      if (hasRecognizerLanguage()) {
        paramCodedOutputStreamMicro.writeString(21, getRecognizerLanguage());
      }
      Iterator localIterator = getRecognizerSegmentList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(22, (RecognizerOuterClass.RecognizerSegmentLog)localIterator.next());
      }
      if (hasServerCluster()) {
        paramCodedOutputStreamMicro.writeString(23, getServerCluster());
      }
      if (hasRequestDurationMs()) {
        paramCodedOutputStreamMicro.writeInt32(24, getRequestDurationMs());
      }
      if (hasPersonalizationEnabled()) {
        paramCodedOutputStreamMicro.writeBool(25, getPersonalizationEnabled());
      }
      if (hasStartTimeMs()) {
        paramCodedOutputStreamMicro.writeInt64(26, getStartTimeMs());
      }
      if (hasLangPack()) {
        paramCodedOutputStreamMicro.writeMessage(27, getLangPack());
      }
      if (hasServerMachineName()) {
        paramCodedOutputStreamMicro.writeString(28, getServerMachineName());
      }
      if (hasChannelCount()) {
        paramCodedOutputStreamMicro.writeInt32(29, getChannelCount());
      }
      if (hasFeatures()) {
        paramCodedOutputStreamMicro.writeMessage(30, getFeatures());
      }
    }
  }
  
  public static final class RecognizerSegmentLog
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int dEPRECATEDFinalEndpointerFiredMs_ = 0;
    private long dEPRECATEDFinalRecognitionResultComputedMs_ = 0L;
    private boolean hasDEPRECATEDFinalEndpointerFiredMs;
    private boolean hasDEPRECATEDFinalRecognitionResultComputedMs;
    private boolean hasRelativeEndTimeMs;
    private boolean hasRelativeStartTimeMs;
    private List<RecognizerOuterClass.RecognizerHypothesisLog> hypothesis_ = Collections.emptyList();
    private int relativeEndTimeMs_ = 0;
    private int relativeStartTimeMs_ = 0;
    
    public RecognizerSegmentLog addHypothesis(RecognizerOuterClass.RecognizerHypothesisLog paramRecognizerHypothesisLog)
    {
      if (paramRecognizerHypothesisLog == null) {
        throw new NullPointerException();
      }
      if (this.hypothesis_.isEmpty()) {
        this.hypothesis_ = new ArrayList();
      }
      this.hypothesis_.add(paramRecognizerHypothesisLog);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getDEPRECATEDFinalEndpointerFiredMs()
    {
      return this.dEPRECATEDFinalEndpointerFiredMs_;
    }
    
    public long getDEPRECATEDFinalRecognitionResultComputedMs()
    {
      return this.dEPRECATEDFinalRecognitionResultComputedMs_;
    }
    
    public List<RecognizerOuterClass.RecognizerHypothesisLog> getHypothesisList()
    {
      return this.hypothesis_;
    }
    
    public int getRelativeEndTimeMs()
    {
      return this.relativeEndTimeMs_;
    }
    
    public int getRelativeStartTimeMs()
    {
      return this.relativeStartTimeMs_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasRelativeStartTimeMs();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getRelativeStartTimeMs());
      }
      if (hasRelativeEndTimeMs()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getRelativeEndTimeMs());
      }
      if (hasDEPRECATEDFinalEndpointerFiredMs()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getDEPRECATEDFinalEndpointerFiredMs());
      }
      if (hasDEPRECATEDFinalRecognitionResultComputedMs()) {
        i += CodedOutputStreamMicro.computeInt64Size(4, getDEPRECATEDFinalRecognitionResultComputedMs());
      }
      Iterator localIterator = getHypothesisList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, (RecognizerOuterClass.RecognizerHypothesisLog)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasDEPRECATEDFinalEndpointerFiredMs()
    {
      return this.hasDEPRECATEDFinalEndpointerFiredMs;
    }
    
    public boolean hasDEPRECATEDFinalRecognitionResultComputedMs()
    {
      return this.hasDEPRECATEDFinalRecognitionResultComputedMs;
    }
    
    public boolean hasRelativeEndTimeMs()
    {
      return this.hasRelativeEndTimeMs;
    }
    
    public boolean hasRelativeStartTimeMs()
    {
      return this.hasRelativeStartTimeMs;
    }
    
    public RecognizerSegmentLog mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setRelativeStartTimeMs(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setRelativeEndTimeMs(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setDEPRECATEDFinalEndpointerFiredMs(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setDEPRECATEDFinalRecognitionResultComputedMs(paramCodedInputStreamMicro.readInt64());
          break;
        }
        RecognizerOuterClass.RecognizerHypothesisLog localRecognizerHypothesisLog = new RecognizerOuterClass.RecognizerHypothesisLog();
        paramCodedInputStreamMicro.readMessage(localRecognizerHypothesisLog);
        addHypothesis(localRecognizerHypothesisLog);
      }
    }
    
    public RecognizerSegmentLog setDEPRECATEDFinalEndpointerFiredMs(int paramInt)
    {
      this.hasDEPRECATEDFinalEndpointerFiredMs = true;
      this.dEPRECATEDFinalEndpointerFiredMs_ = paramInt;
      return this;
    }
    
    public RecognizerSegmentLog setDEPRECATEDFinalRecognitionResultComputedMs(long paramLong)
    {
      this.hasDEPRECATEDFinalRecognitionResultComputedMs = true;
      this.dEPRECATEDFinalRecognitionResultComputedMs_ = paramLong;
      return this;
    }
    
    public RecognizerSegmentLog setRelativeEndTimeMs(int paramInt)
    {
      this.hasRelativeEndTimeMs = true;
      this.relativeEndTimeMs_ = paramInt;
      return this;
    }
    
    public RecognizerSegmentLog setRelativeStartTimeMs(int paramInt)
    {
      this.hasRelativeStartTimeMs = true;
      this.relativeStartTimeMs_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasRelativeStartTimeMs()) {
        paramCodedOutputStreamMicro.writeInt32(1, getRelativeStartTimeMs());
      }
      if (hasRelativeEndTimeMs()) {
        paramCodedOutputStreamMicro.writeInt32(2, getRelativeEndTimeMs());
      }
      if (hasDEPRECATEDFinalEndpointerFiredMs()) {
        paramCodedOutputStreamMicro.writeInt32(3, getDEPRECATEDFinalEndpointerFiredMs());
      }
      if (hasDEPRECATEDFinalRecognitionResultComputedMs()) {
        paramCodedOutputStreamMicro.writeInt64(4, getDEPRECATEDFinalRecognitionResultComputedMs());
      }
      Iterator localIterator = getHypothesisList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(5, (RecognizerOuterClass.RecognizerHypothesisLog)localIterator.next());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.logs.RecognizerOuterClass
 * JD-Core Version:    0.7.0.1
 */