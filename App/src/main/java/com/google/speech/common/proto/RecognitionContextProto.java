package com.google.speech.common.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class RecognitionContextProto
{
  public static final class RecognitionContext
    extends MessageMicro
  {
    private String applicationName_ = "";
    private int cachedSize = -1;
    private String canonicalLanguage_ = "";
    private String clientApplicationId_ = "";
    private String clientId_ = "";
    private List<String> enabledKeyboardLanguage_ = Collections.emptyList();
    private String fieldId_ = "";
    private String fieldName_ = "";
    private String hardware_ = "";
    private boolean hasApplicationName;
    private boolean hasCanonicalLanguage;
    private boolean hasClientApplicationId;
    private boolean hasClientId;
    private boolean hasFieldId;
    private boolean hasFieldName;
    private boolean hasGrxmlGrammar_ = false;
    private boolean hasHardware;
    private boolean hasHasGrxmlGrammar;
    private boolean hasHint;
    private boolean hasImeOptions;
    private boolean hasInputType;
    private boolean hasLabel;
    private boolean hasLanguage;
    private boolean hasLanguageModel;
    private boolean hasRawLanguage;
    private boolean hasReferer;
    private boolean hasSelectedKeyboardLanguage;
    private boolean hasSingleLine;
    private boolean hasUserAgent;
    private boolean hasVoiceSearchLanguage;
    private String hint_ = "";
    private int imeOptions_ = 0;
    private int inputType_ = 0;
    private String label_ = "";
    private String languageModel_ = "";
    private String language_ = "";
    private String rawLanguage_ = "";
    private String referer_ = "";
    private String selectedKeyboardLanguage_ = "";
    private boolean singleLine_ = false;
    private String userAgent_ = "";
    private String voiceSearchLanguage_ = "";
    
    public RecognitionContext addEnabledKeyboardLanguage(String paramString)
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
    
    public String getApplicationName()
    {
      return this.applicationName_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getCanonicalLanguage()
    {
      return this.canonicalLanguage_;
    }
    
    public String getClientApplicationId()
    {
      return this.clientApplicationId_;
    }
    
    public String getClientId()
    {
      return this.clientId_;
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
    
    public String getHardware()
    {
      return this.hardware_;
    }
    
    public boolean getHasGrxmlGrammar()
    {
      return this.hasGrxmlGrammar_;
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
    
    public String getLanguage()
    {
      return this.language_;
    }
    
    public String getLanguageModel()
    {
      return this.languageModel_;
    }
    
    public String getRawLanguage()
    {
      return this.rawLanguage_;
    }
    
    public String getReferer()
    {
      return this.referer_;
    }
    
    public String getSelectedKeyboardLanguage()
    {
      return this.selectedKeyboardLanguage_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasApplicationName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getApplicationName());
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
      if (hasLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getLanguage());
      }
      if (hasClientId()) {
        i += CodedOutputStreamMicro.computeStringSize(11, getClientId());
      }
      if (hasHasGrxmlGrammar()) {
        i += CodedOutputStreamMicro.computeBoolSize(12, getHasGrxmlGrammar());
      }
      if (hasSelectedKeyboardLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(13, getSelectedKeyboardLanguage());
      }
      int j = 0;
      Iterator localIterator = getEnabledKeyboardLanguageList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getEnabledKeyboardLanguageList().size();
      if (hasVoiceSearchLanguage()) {
        k += CodedOutputStreamMicro.computeStringSize(15, getVoiceSearchLanguage());
      }
      if (hasReferer()) {
        k += CodedOutputStreamMicro.computeStringSize(16, getReferer());
      }
      if (hasUserAgent()) {
        k += CodedOutputStreamMicro.computeStringSize(17, getUserAgent());
      }
      if (hasHardware()) {
        k += CodedOutputStreamMicro.computeStringSize(18, getHardware());
      }
      if (hasClientApplicationId()) {
        k += CodedOutputStreamMicro.computeStringSize(19, getClientApplicationId());
      }
      if (hasRawLanguage()) {
        k += CodedOutputStreamMicro.computeStringSize(20, getRawLanguage());
      }
      if (hasCanonicalLanguage()) {
        k += CodedOutputStreamMicro.computeStringSize(21, getCanonicalLanguage());
      }
      this.cachedSize = k;
      return k;
    }
    
    public boolean getSingleLine()
    {
      return this.singleLine_;
    }
    
    public String getUserAgent()
    {
      return this.userAgent_;
    }
    
    public String getVoiceSearchLanguage()
    {
      return this.voiceSearchLanguage_;
    }
    
    public boolean hasApplicationName()
    {
      return this.hasApplicationName;
    }
    
    public boolean hasCanonicalLanguage()
    {
      return this.hasCanonicalLanguage;
    }
    
    public boolean hasClientApplicationId()
    {
      return this.hasClientApplicationId;
    }
    
    public boolean hasClientId()
    {
      return this.hasClientId;
    }
    
    public boolean hasFieldId()
    {
      return this.hasFieldId;
    }
    
    public boolean hasFieldName()
    {
      return this.hasFieldName;
    }
    
    public boolean hasHardware()
    {
      return this.hasHardware;
    }
    
    public boolean hasHasGrxmlGrammar()
    {
      return this.hasHasGrxmlGrammar;
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
    
    public boolean hasLanguage()
    {
      return this.hasLanguage;
    }
    
    public boolean hasLanguageModel()
    {
      return this.hasLanguageModel;
    }
    
    public boolean hasRawLanguage()
    {
      return this.hasRawLanguage;
    }
    
    public boolean hasReferer()
    {
      return this.hasReferer;
    }
    
    public boolean hasSelectedKeyboardLanguage()
    {
      return this.hasSelectedKeyboardLanguage;
    }
    
    public boolean hasSingleLine()
    {
      return this.hasSingleLine;
    }
    
    public boolean hasUserAgent()
    {
      return this.hasUserAgent;
    }
    
    public boolean hasVoiceSearchLanguage()
    {
      return this.hasVoiceSearchLanguage;
    }
    
    public RecognitionContext mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setApplicationName(paramCodedInputStreamMicro.readString());
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
          setLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 90: 
          setClientId(paramCodedInputStreamMicro.readString());
          break;
        case 96: 
          setHasGrxmlGrammar(paramCodedInputStreamMicro.readBool());
          break;
        case 106: 
          setSelectedKeyboardLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 114: 
          addEnabledKeyboardLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 122: 
          setVoiceSearchLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 130: 
          setReferer(paramCodedInputStreamMicro.readString());
          break;
        case 138: 
          setUserAgent(paramCodedInputStreamMicro.readString());
          break;
        case 146: 
          setHardware(paramCodedInputStreamMicro.readString());
          break;
        case 154: 
          setClientApplicationId(paramCodedInputStreamMicro.readString());
          break;
        case 162: 
          setRawLanguage(paramCodedInputStreamMicro.readString());
          break;
        }
        setCanonicalLanguage(paramCodedInputStreamMicro.readString());
      }
    }
    
    public RecognitionContext setApplicationName(String paramString)
    {
      this.hasApplicationName = true;
      this.applicationName_ = paramString;
      return this;
    }
    
    public RecognitionContext setCanonicalLanguage(String paramString)
    {
      this.hasCanonicalLanguage = true;
      this.canonicalLanguage_ = paramString;
      return this;
    }
    
    public RecognitionContext setClientApplicationId(String paramString)
    {
      this.hasClientApplicationId = true;
      this.clientApplicationId_ = paramString;
      return this;
    }
    
    public RecognitionContext setClientId(String paramString)
    {
      this.hasClientId = true;
      this.clientId_ = paramString;
      return this;
    }
    
    public RecognitionContext setFieldId(String paramString)
    {
      this.hasFieldId = true;
      this.fieldId_ = paramString;
      return this;
    }
    
    public RecognitionContext setFieldName(String paramString)
    {
      this.hasFieldName = true;
      this.fieldName_ = paramString;
      return this;
    }
    
    public RecognitionContext setHardware(String paramString)
    {
      this.hasHardware = true;
      this.hardware_ = paramString;
      return this;
    }
    
    public RecognitionContext setHasGrxmlGrammar(boolean paramBoolean)
    {
      this.hasHasGrxmlGrammar = true;
      this.hasGrxmlGrammar_ = paramBoolean;
      return this;
    }
    
    public RecognitionContext setHint(String paramString)
    {
      this.hasHint = true;
      this.hint_ = paramString;
      return this;
    }
    
    public RecognitionContext setImeOptions(int paramInt)
    {
      this.hasImeOptions = true;
      this.imeOptions_ = paramInt;
      return this;
    }
    
    public RecognitionContext setInputType(int paramInt)
    {
      this.hasInputType = true;
      this.inputType_ = paramInt;
      return this;
    }
    
    public RecognitionContext setLabel(String paramString)
    {
      this.hasLabel = true;
      this.label_ = paramString;
      return this;
    }
    
    public RecognitionContext setLanguage(String paramString)
    {
      this.hasLanguage = true;
      this.language_ = paramString;
      return this;
    }
    
    public RecognitionContext setLanguageModel(String paramString)
    {
      this.hasLanguageModel = true;
      this.languageModel_ = paramString;
      return this;
    }
    
    public RecognitionContext setRawLanguage(String paramString)
    {
      this.hasRawLanguage = true;
      this.rawLanguage_ = paramString;
      return this;
    }
    
    public RecognitionContext setReferer(String paramString)
    {
      this.hasReferer = true;
      this.referer_ = paramString;
      return this;
    }
    
    public RecognitionContext setSelectedKeyboardLanguage(String paramString)
    {
      this.hasSelectedKeyboardLanguage = true;
      this.selectedKeyboardLanguage_ = paramString;
      return this;
    }
    
    public RecognitionContext setSingleLine(boolean paramBoolean)
    {
      this.hasSingleLine = true;
      this.singleLine_ = paramBoolean;
      return this;
    }
    
    public RecognitionContext setUserAgent(String paramString)
    {
      this.hasUserAgent = true;
      this.userAgent_ = paramString;
      return this;
    }
    
    public RecognitionContext setVoiceSearchLanguage(String paramString)
    {
      this.hasVoiceSearchLanguage = true;
      this.voiceSearchLanguage_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasApplicationName()) {
        paramCodedOutputStreamMicro.writeString(1, getApplicationName());
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
      if (hasLanguage()) {
        paramCodedOutputStreamMicro.writeString(10, getLanguage());
      }
      if (hasClientId()) {
        paramCodedOutputStreamMicro.writeString(11, getClientId());
      }
      if (hasHasGrxmlGrammar()) {
        paramCodedOutputStreamMicro.writeBool(12, getHasGrxmlGrammar());
      }
      if (hasSelectedKeyboardLanguage()) {
        paramCodedOutputStreamMicro.writeString(13, getSelectedKeyboardLanguage());
      }
      Iterator localIterator = getEnabledKeyboardLanguageList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(14, (String)localIterator.next());
      }
      if (hasVoiceSearchLanguage()) {
        paramCodedOutputStreamMicro.writeString(15, getVoiceSearchLanguage());
      }
      if (hasReferer()) {
        paramCodedOutputStreamMicro.writeString(16, getReferer());
      }
      if (hasUserAgent()) {
        paramCodedOutputStreamMicro.writeString(17, getUserAgent());
      }
      if (hasHardware()) {
        paramCodedOutputStreamMicro.writeString(18, getHardware());
      }
      if (hasClientApplicationId()) {
        paramCodedOutputStreamMicro.writeString(19, getClientApplicationId());
      }
      if (hasRawLanguage()) {
        paramCodedOutputStreamMicro.writeString(20, getRawLanguage());
      }
      if (hasCanonicalLanguage()) {
        paramCodedOutputStreamMicro.writeString(21, getCanonicalLanguage());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.common.proto.RecognitionContextProto
 * JD-Core Version:    0.7.0.1
 */