package com.google.speech.grammar.pumpkin;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class PumpkinConfigProto
{
  public static final class DecoderConfig
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasNumOfActiveStates;
    private boolean hasOnTheFlyComposition;
    private boolean hasRetainOnlyBestHypotheses;
    private int numOfActiveStates_ = 50;
    private boolean onTheFlyComposition_ = false;
    private boolean retainOnlyBestHypotheses_ = true;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getNumOfActiveStates()
    {
      return this.numOfActiveStates_;
    }
    
    public boolean getOnTheFlyComposition()
    {
      return this.onTheFlyComposition_;
    }
    
    public boolean getRetainOnlyBestHypotheses()
    {
      return this.retainOnlyBestHypotheses_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasNumOfActiveStates();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getNumOfActiveStates());
      }
      if (hasOnTheFlyComposition()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getOnTheFlyComposition());
      }
      if (hasRetainOnlyBestHypotheses()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getRetainOnlyBestHypotheses());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasNumOfActiveStates()
    {
      return this.hasNumOfActiveStates;
    }
    
    public boolean hasOnTheFlyComposition()
    {
      return this.hasOnTheFlyComposition;
    }
    
    public boolean hasRetainOnlyBestHypotheses()
    {
      return this.hasRetainOnlyBestHypotheses;
    }
    
    public DecoderConfig mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setNumOfActiveStates(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setOnTheFlyComposition(paramCodedInputStreamMicro.readBool());
          break;
        }
        setRetainOnlyBestHypotheses(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public DecoderConfig setNumOfActiveStates(int paramInt)
    {
      this.hasNumOfActiveStates = true;
      this.numOfActiveStates_ = paramInt;
      return this;
    }
    
    public DecoderConfig setOnTheFlyComposition(boolean paramBoolean)
    {
      this.hasOnTheFlyComposition = true;
      this.onTheFlyComposition_ = paramBoolean;
      return this;
    }
    
    public DecoderConfig setRetainOnlyBestHypotheses(boolean paramBoolean)
    {
      this.hasRetainOnlyBestHypotheses = true;
      this.retainOnlyBestHypotheses_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasNumOfActiveStates()) {
        paramCodedOutputStreamMicro.writeInt32(1, getNumOfActiveStates());
      }
      if (hasOnTheFlyComposition()) {
        paramCodedOutputStreamMicro.writeBool(2, getOnTheFlyComposition());
      }
      if (hasRetainOnlyBestHypotheses()) {
        paramCodedOutputStreamMicro.writeBool(3, getRetainOnlyBestHypotheses());
      }
    }
  }
  
  public static final class PumpkinConfig
    extends MessageMicro
  {
    private int cachedSize;
    private boolean canonicalizeToFullContacts_;
    private PumpkinConfigProto.DecoderConfig decoderConfig_;
    private boolean hasCanonicalizeToFullContacts;
    private boolean hasDecoderConfig;
    private boolean hasInsErrScore;
    private boolean hasLanguage;
    private boolean hasValidatorManagerConfig;
    private float insErrScore_;
    private String language_;
    private PumpkinConfigProto.ValidatorManagerConfig validatorManagerConfig_;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public boolean getCanonicalizeToFullContacts()
    {
      return this.canonicalizeToFullContacts_;
    }
    
    public PumpkinConfigProto.DecoderConfig getDecoderConfig()
    {
      return this.decoderConfig_;
    }
    
    public float getInsErrScore()
    {
      return this.insErrScore_;
    }
    
    public String getLanguage()
    {
      return this.language_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLanguage();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getLanguage());
      }
      if (hasValidatorManagerConfig()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getValidatorManagerConfig());
      }
      if (hasDecoderConfig()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getDecoderConfig());
      }
      if (hasInsErrScore()) {
        i += CodedOutputStreamMicro.computeFloatSize(4, getInsErrScore());
      }
      if (hasCanonicalizeToFullContacts()) {
        i += CodedOutputStreamMicro.computeBoolSize(5, getCanonicalizeToFullContacts());
      }
      this.cachedSize = i;
      return i;
    }
    
    public PumpkinConfigProto.ValidatorManagerConfig getValidatorManagerConfig()
    {
      return this.validatorManagerConfig_;
    }
    
    public boolean hasCanonicalizeToFullContacts()
    {
      return this.hasCanonicalizeToFullContacts;
    }
    
    public boolean hasDecoderConfig()
    {
      return this.hasDecoderConfig;
    }
    
    public boolean hasInsErrScore()
    {
      return this.hasInsErrScore;
    }
    
    public boolean hasLanguage()
    {
      return this.hasLanguage;
    }
    
    public boolean hasValidatorManagerConfig()
    {
      return this.hasValidatorManagerConfig;
    }
    
    public PumpkinConfig mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          PumpkinConfigProto.ValidatorManagerConfig localValidatorManagerConfig = new PumpkinConfigProto.ValidatorManagerConfig();
          paramCodedInputStreamMicro.readMessage(localValidatorManagerConfig);
          setValidatorManagerConfig(localValidatorManagerConfig);
          break;
        case 26: 
          PumpkinConfigProto.DecoderConfig localDecoderConfig = new PumpkinConfigProto.DecoderConfig();
          paramCodedInputStreamMicro.readMessage(localDecoderConfig);
          setDecoderConfig(localDecoderConfig);
          break;
        case 37: 
          setInsErrScore(paramCodedInputStreamMicro.readFloat());
          break;
        }
        setCanonicalizeToFullContacts(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public PumpkinConfig setCanonicalizeToFullContacts(boolean paramBoolean)
    {
      this.hasCanonicalizeToFullContacts = true;
      this.canonicalizeToFullContacts_ = paramBoolean;
      return this;
    }
    
    public PumpkinConfig setDecoderConfig(PumpkinConfigProto.DecoderConfig paramDecoderConfig)
    {
      if (paramDecoderConfig == null) {
        throw new NullPointerException();
      }
      this.hasDecoderConfig = true;
      this.decoderConfig_ = paramDecoderConfig;
      return this;
    }
    
    public PumpkinConfig setInsErrScore(float paramFloat)
    {
      this.hasInsErrScore = true;
      this.insErrScore_ = paramFloat;
      return this;
    }
    
    public PumpkinConfig setLanguage(String paramString)
    {
      this.hasLanguage = true;
      this.language_ = paramString;
      return this;
    }
    
    public PumpkinConfig setValidatorManagerConfig(PumpkinConfigProto.ValidatorManagerConfig paramValidatorManagerConfig)
    {
      if (paramValidatorManagerConfig == null) {
        throw new NullPointerException();
      }
      this.hasValidatorManagerConfig = true;
      this.validatorManagerConfig_ = paramValidatorManagerConfig;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLanguage()) {
        paramCodedOutputStreamMicro.writeString(1, getLanguage());
      }
      if (hasValidatorManagerConfig()) {
        paramCodedOutputStreamMicro.writeMessage(2, getValidatorManagerConfig());
      }
      if (hasDecoderConfig()) {
        paramCodedOutputStreamMicro.writeMessage(3, getDecoderConfig());
      }
      if (hasInsErrScore()) {
        paramCodedOutputStreamMicro.writeFloat(4, getInsErrScore());
      }
      if (hasCanonicalizeToFullContacts()) {
        paramCodedOutputStreamMicro.writeBool(5, getCanonicalizeToFullContacts());
      }
    }
  }
  
  public static final class PumpkinResource
    extends MessageMicro
  {
    private int cachedSize = -1;
    private ByteStringMicro data_ = ByteStringMicro.EMPTY;
    private String filename_ = "";
    private boolean hasData;
    private boolean hasFilename;
    private boolean hasName;
    private String name_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public ByteStringMicro getData()
    {
      return this.data_;
    }
    
    public String getFilename()
    {
      return this.filename_;
    }
    
    public String getName()
    {
      return this.name_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getName());
      }
      if (hasFilename()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getFilename());
      }
      if (hasData()) {
        i += CodedOutputStreamMicro.computeBytesSize(3, getData());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasData()
    {
      return this.hasData;
    }
    
    public boolean hasFilename()
    {
      return this.hasFilename;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public PumpkinResource mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setName(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setFilename(paramCodedInputStreamMicro.readString());
          break;
        }
        setData(paramCodedInputStreamMicro.readBytes());
      }
    }
    
    public PumpkinResource setData(ByteStringMicro paramByteStringMicro)
    {
      this.hasData = true;
      this.data_ = paramByteStringMicro;
      return this;
    }
    
    public PumpkinResource setFilename(String paramString)
    {
      this.hasFilename = true;
      this.filename_ = paramString;
      return this;
    }
    
    public PumpkinResource setName(String paramString)
    {
      this.hasName = true;
      this.name_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasName()) {
        paramCodedOutputStreamMicro.writeString(1, getName());
      }
      if (hasFilename()) {
        paramCodedOutputStreamMicro.writeString(2, getFilename());
      }
      if (hasData()) {
        paramCodedOutputStreamMicro.writeBytes(3, getData());
      }
    }
  }
  
  public static final class ValidatorManagerConfig
    extends MessageMicro
  {
    private String atInEmails_ = "";
    private int cachedSize = -1;
    private String contactKeyWords_ = "";
    private String dotInEmails_ = "";
    private String dotInUrls_ = "";
    private List<PumpkinConfigProto.PumpkinResource> grammar_ = Collections.emptyList();
    private boolean hasAtInEmails;
    private boolean hasContactKeyWords;
    private boolean hasDotInEmails;
    private boolean hasDotInUrls;
    private boolean hasResourcePath;
    private String resourcePath_ = "";
    
    public ValidatorManagerConfig addGrammar(PumpkinConfigProto.PumpkinResource paramPumpkinResource)
    {
      if (paramPumpkinResource == null) {
        throw new NullPointerException();
      }
      if (this.grammar_.isEmpty()) {
        this.grammar_ = new ArrayList();
      }
      this.grammar_.add(paramPumpkinResource);
      return this;
    }
    
    public String getAtInEmails()
    {
      return this.atInEmails_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getContactKeyWords()
    {
      return this.contactKeyWords_;
    }
    
    public String getDotInEmails()
    {
      return this.dotInEmails_;
    }
    
    public String getDotInUrls()
    {
      return this.dotInUrls_;
    }
    
    public List<PumpkinConfigProto.PumpkinResource> getGrammarList()
    {
      return this.grammar_;
    }
    
    public String getResourcePath()
    {
      return this.resourcePath_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasResourcePath();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getResourcePath());
      }
      if (hasContactKeyWords()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getContactKeyWords());
      }
      if (hasDotInEmails()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getDotInEmails());
      }
      if (hasAtInEmails()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getAtInEmails());
      }
      if (hasDotInUrls()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getDotInUrls());
      }
      Iterator localIterator = getGrammarList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, (PumpkinConfigProto.PumpkinResource)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAtInEmails()
    {
      return this.hasAtInEmails;
    }
    
    public boolean hasContactKeyWords()
    {
      return this.hasContactKeyWords;
    }
    
    public boolean hasDotInEmails()
    {
      return this.hasDotInEmails;
    }
    
    public boolean hasDotInUrls()
    {
      return this.hasDotInUrls;
    }
    
    public boolean hasResourcePath()
    {
      return this.hasResourcePath;
    }
    
    public ValidatorManagerConfig mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setResourcePath(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setContactKeyWords(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setDotInEmails(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setAtInEmails(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setDotInUrls(paramCodedInputStreamMicro.readString());
          break;
        }
        PumpkinConfigProto.PumpkinResource localPumpkinResource = new PumpkinConfigProto.PumpkinResource();
        paramCodedInputStreamMicro.readMessage(localPumpkinResource);
        addGrammar(localPumpkinResource);
      }
    }
    
    public ValidatorManagerConfig setAtInEmails(String paramString)
    {
      this.hasAtInEmails = true;
      this.atInEmails_ = paramString;
      return this;
    }
    
    public ValidatorManagerConfig setContactKeyWords(String paramString)
    {
      this.hasContactKeyWords = true;
      this.contactKeyWords_ = paramString;
      return this;
    }
    
    public ValidatorManagerConfig setDotInEmails(String paramString)
    {
      this.hasDotInEmails = true;
      this.dotInEmails_ = paramString;
      return this;
    }
    
    public ValidatorManagerConfig setDotInUrls(String paramString)
    {
      this.hasDotInUrls = true;
      this.dotInUrls_ = paramString;
      return this;
    }
    
    public ValidatorManagerConfig setResourcePath(String paramString)
    {
      this.hasResourcePath = true;
      this.resourcePath_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasResourcePath()) {
        paramCodedOutputStreamMicro.writeString(1, getResourcePath());
      }
      if (hasContactKeyWords()) {
        paramCodedOutputStreamMicro.writeString(2, getContactKeyWords());
      }
      if (hasDotInEmails()) {
        paramCodedOutputStreamMicro.writeString(3, getDotInEmails());
      }
      if (hasAtInEmails()) {
        paramCodedOutputStreamMicro.writeString(4, getAtInEmails());
      }
      if (hasDotInUrls()) {
        paramCodedOutputStreamMicro.writeString(5, getDotInUrls());
      }
      Iterator localIterator = getGrammarList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(6, (PumpkinConfigProto.PumpkinResource)localIterator.next());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.grammar.pumpkin.PumpkinConfigProto
 * JD-Core Version:    0.7.0.1
 */