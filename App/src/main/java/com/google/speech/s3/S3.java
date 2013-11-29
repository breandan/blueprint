package com.google.speech.s3;

import com.google.bionics.goggles.api2.GogglesProtos.GogglesClientLog;
import com.google.bionics.goggles.api2.GogglesProtos.GogglesStreamRequest;
import com.google.bionics.goggles.api2.GogglesProtos.GogglesStreamResponse;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import com.google.speech.speech.s3.Majel.MajelClientInfo;
import com.google.speech.speech.s3.Majel.MajelServiceEvent;
import com.google.speech.speech.s3.Majel.MajelServiceRequest;
import com.google.speech.speech.s3.Recognizer.RecognizerEvent;
import com.google.speech.speech.s3.Recognizer.RecognizerVocabularyContext;
import com.google.speech.speech.s3.Recognizer.S3RecognizerInfo;
import com.google.speech.speech.s3.SoundSearch.SoundSearchInfo;
import com.google.speech.speech.s3.SoundSearch.SoundSearchServiceEvent;
import com.google.speech.speech.s3.Synthesizer.TtsCapabilitiesRequest;
import com.google.speech.speech.s3.Synthesizer.TtsCapabilitiesResponse;
import com.google.speech.speech.s3.Synthesizer.TtsServiceEvent;
import com.google.speech.speech.s3.Synthesizer.TtsServiceRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import speech.s3.goggles.GogglesS3.GogglesS3SessionOptions;

public final class S3
{
  public static final class AuthToken
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String email_ = "";
    private boolean hasEmail;
    private boolean hasName;
    private boolean hasToken;
    private boolean hasUserId;
    private boolean hasUserIdType;
    private String name_ = "";
    private String token_ = "";
    private int userIdType_ = 0;
    private long userId_ = 0L;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getEmail()
    {
      return this.email_;
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
      if (hasToken()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getToken());
      }
      if (hasUserId()) {
        i += CodedOutputStreamMicro.computeInt64Size(3, getUserId());
      }
      if (hasUserIdType()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getUserIdType());
      }
      if (hasEmail()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getEmail());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getToken()
    {
      return this.token_;
    }
    
    public long getUserId()
    {
      return this.userId_;
    }
    
    public int getUserIdType()
    {
      return this.userIdType_;
    }
    
    public boolean hasEmail()
    {
      return this.hasEmail;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public boolean hasToken()
    {
      return this.hasToken;
    }
    
    public boolean hasUserId()
    {
      return this.hasUserId;
    }
    
    public boolean hasUserIdType()
    {
      return this.hasUserIdType;
    }
    
    public AuthToken mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setToken(paramCodedInputStreamMicro.readString());
          break;
        case 24: 
          setUserId(paramCodedInputStreamMicro.readInt64());
          break;
        case 32: 
          setUserIdType(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setEmail(paramCodedInputStreamMicro.readString());
      }
    }
    
    public AuthToken setEmail(String paramString)
    {
      this.hasEmail = true;
      this.email_ = paramString;
      return this;
    }
    
    public AuthToken setName(String paramString)
    {
      this.hasName = true;
      this.name_ = paramString;
      return this;
    }
    
    public AuthToken setToken(String paramString)
    {
      this.hasToken = true;
      this.token_ = paramString;
      return this;
    }
    
    public AuthToken setUserId(long paramLong)
    {
      this.hasUserId = true;
      this.userId_ = paramLong;
      return this;
    }
    
    public AuthToken setUserIdType(int paramInt)
    {
      this.hasUserIdType = true;
      this.userIdType_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasName()) {
        paramCodedOutputStreamMicro.writeString(1, getName());
      }
      if (hasToken()) {
        paramCodedOutputStreamMicro.writeString(2, getToken());
      }
      if (hasUserId()) {
        paramCodedOutputStreamMicro.writeInt64(3, getUserId());
      }
      if (hasUserIdType()) {
        paramCodedOutputStreamMicro.writeInt32(4, getUserIdType());
      }
      if (hasEmail()) {
        paramCodedOutputStreamMicro.writeString(5, getEmail());
      }
    }
  }
  
  public static final class Locale
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int format_ = 0;
    private boolean hasFormat;
    private boolean hasLocale;
    private String locale_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getFormat()
    {
      return this.format_;
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
      if (hasFormat()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getFormat());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasFormat()
    {
      return this.hasFormat;
    }
    
    public boolean hasLocale()
    {
      return this.hasLocale;
    }
    
    public Locale mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setFormat(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public Locale setFormat(int paramInt)
    {
      this.hasFormat = true;
      this.format_ = paramInt;
      return this;
    }
    
    public Locale setLocale(String paramString)
    {
      this.hasLocale = true;
      this.locale_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLocale()) {
        paramCodedOutputStreamMicro.writeString(1, getLocale());
      }
      if (hasFormat()) {
        paramCodedOutputStreamMicro.writeInt32(2, getFormat());
      }
    }
  }
  
  public static final class S3ClientInfo
    extends MessageMicro
  {
    private String applicationId_ = "";
    private String applicationVersion_ = "";
    private int cachedSize = -1;
    private String clientId_ = "";
    private int deviceDisplayDensityDpi_ = 0;
    private int deviceDisplayHeightPixels_ = 0;
    private int deviceDisplayWidthPixels_ = 0;
    private String deviceModel_ = "";
    private List<String> experimentId_ = Collections.emptyList();
    private boolean hasApplicationId;
    private boolean hasApplicationVersion;
    private boolean hasClientId;
    private boolean hasDeviceDisplayDensityDpi;
    private boolean hasDeviceDisplayHeightPixels;
    private boolean hasDeviceDisplayWidthPixels;
    private boolean hasDeviceModel;
    private boolean hasPlatformId;
    private boolean hasPlatformVersion;
    private boolean hasTriggerApplicationId;
    private boolean hasUserAgent;
    private List<String> noiseSuppressorId_ = Collections.emptyList();
    private String platformId_ = "";
    private String platformVersion_ = "";
    private String triggerApplicationId_ = "";
    private String userAgent_ = "";
    
    public S3ClientInfo addExperimentId(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.experimentId_.isEmpty()) {
        this.experimentId_ = new ArrayList();
      }
      this.experimentId_.add(paramString);
      return this;
    }
    
    public S3ClientInfo addNoiseSuppressorId(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.noiseSuppressorId_.isEmpty()) {
        this.noiseSuppressorId_ = new ArrayList();
      }
      this.noiseSuppressorId_.add(paramString);
      return this;
    }
    
    public String getApplicationId()
    {
      return this.applicationId_;
    }
    
    public String getApplicationVersion()
    {
      return this.applicationVersion_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getClientId()
    {
      return this.clientId_;
    }
    
    public int getDeviceDisplayDensityDpi()
    {
      return this.deviceDisplayDensityDpi_;
    }
    
    public int getDeviceDisplayHeightPixels()
    {
      return this.deviceDisplayHeightPixels_;
    }
    
    public int getDeviceDisplayWidthPixels()
    {
      return this.deviceDisplayWidthPixels_;
    }
    
    public String getDeviceModel()
    {
      return this.deviceModel_;
    }
    
    public List<String> getExperimentIdList()
    {
      return this.experimentId_;
    }
    
    public List<String> getNoiseSuppressorIdList()
    {
      return this.noiseSuppressorId_;
    }
    
    public String getPlatformId()
    {
      return this.platformId_;
    }
    
    public String getPlatformVersion()
    {
      return this.platformVersion_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasClientId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getClientId());
      }
      if (hasApplicationId()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getApplicationId());
      }
      if (hasUserAgent()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getUserAgent());
      }
      int j = 0;
      Iterator localIterator1 = getExperimentIdList().iterator();
      while (localIterator1.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator1.next());
      }
      int k = i + j + 1 * getExperimentIdList().size();
      if (hasPlatformId()) {
        k += CodedOutputStreamMicro.computeStringSize(8, getPlatformId());
      }
      if (hasPlatformVersion()) {
        k += CodedOutputStreamMicro.computeStringSize(9, getPlatformVersion());
      }
      if (hasApplicationVersion()) {
        k += CodedOutputStreamMicro.computeStringSize(10, getApplicationVersion());
      }
      if (hasDeviceModel()) {
        k += CodedOutputStreamMicro.computeStringSize(11, getDeviceModel());
      }
      if (hasDeviceDisplayWidthPixels()) {
        k += CodedOutputStreamMicro.computeInt32Size(12, getDeviceDisplayWidthPixels());
      }
      if (hasDeviceDisplayHeightPixels()) {
        k += CodedOutputStreamMicro.computeInt32Size(13, getDeviceDisplayHeightPixels());
      }
      if (hasDeviceDisplayDensityDpi()) {
        k += CodedOutputStreamMicro.computeInt32Size(14, getDeviceDisplayDensityDpi());
      }
      int m = 0;
      Iterator localIterator2 = getNoiseSuppressorIdList().iterator();
      while (localIterator2.hasNext()) {
        m += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator2.next());
      }
      int n = k + m + 1 * getNoiseSuppressorIdList().size();
      if (hasTriggerApplicationId()) {
        n += CodedOutputStreamMicro.computeStringSize(16, getTriggerApplicationId());
      }
      this.cachedSize = n;
      return n;
    }
    
    public String getTriggerApplicationId()
    {
      return this.triggerApplicationId_;
    }
    
    public String getUserAgent()
    {
      return this.userAgent_;
    }
    
    public boolean hasApplicationId()
    {
      return this.hasApplicationId;
    }
    
    public boolean hasApplicationVersion()
    {
      return this.hasApplicationVersion;
    }
    
    public boolean hasClientId()
    {
      return this.hasClientId;
    }
    
    public boolean hasDeviceDisplayDensityDpi()
    {
      return this.hasDeviceDisplayDensityDpi;
    }
    
    public boolean hasDeviceDisplayHeightPixels()
    {
      return this.hasDeviceDisplayHeightPixels;
    }
    
    public boolean hasDeviceDisplayWidthPixels()
    {
      return this.hasDeviceDisplayWidthPixels;
    }
    
    public boolean hasDeviceModel()
    {
      return this.hasDeviceModel;
    }
    
    public boolean hasPlatformId()
    {
      return this.hasPlatformId;
    }
    
    public boolean hasPlatformVersion()
    {
      return this.hasPlatformVersion;
    }
    
    public boolean hasTriggerApplicationId()
    {
      return this.hasTriggerApplicationId;
    }
    
    public boolean hasUserAgent()
    {
      return this.hasUserAgent;
    }
    
    public S3ClientInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setClientId(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setApplicationId(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setUserAgent(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          addExperimentId(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setPlatformId(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          setPlatformVersion(paramCodedInputStreamMicro.readString());
          break;
        case 82: 
          setApplicationVersion(paramCodedInputStreamMicro.readString());
          break;
        case 90: 
          setDeviceModel(paramCodedInputStreamMicro.readString());
          break;
        case 96: 
          setDeviceDisplayWidthPixels(paramCodedInputStreamMicro.readInt32());
          break;
        case 104: 
          setDeviceDisplayHeightPixels(paramCodedInputStreamMicro.readInt32());
          break;
        case 112: 
          setDeviceDisplayDensityDpi(paramCodedInputStreamMicro.readInt32());
          break;
        case 122: 
          addNoiseSuppressorId(paramCodedInputStreamMicro.readString());
          break;
        }
        setTriggerApplicationId(paramCodedInputStreamMicro.readString());
      }
    }
    
    public S3ClientInfo setApplicationId(String paramString)
    {
      this.hasApplicationId = true;
      this.applicationId_ = paramString;
      return this;
    }
    
    public S3ClientInfo setApplicationVersion(String paramString)
    {
      this.hasApplicationVersion = true;
      this.applicationVersion_ = paramString;
      return this;
    }
    
    public S3ClientInfo setClientId(String paramString)
    {
      this.hasClientId = true;
      this.clientId_ = paramString;
      return this;
    }
    
    public S3ClientInfo setDeviceDisplayDensityDpi(int paramInt)
    {
      this.hasDeviceDisplayDensityDpi = true;
      this.deviceDisplayDensityDpi_ = paramInt;
      return this;
    }
    
    public S3ClientInfo setDeviceDisplayHeightPixels(int paramInt)
    {
      this.hasDeviceDisplayHeightPixels = true;
      this.deviceDisplayHeightPixels_ = paramInt;
      return this;
    }
    
    public S3ClientInfo setDeviceDisplayWidthPixels(int paramInt)
    {
      this.hasDeviceDisplayWidthPixels = true;
      this.deviceDisplayWidthPixels_ = paramInt;
      return this;
    }
    
    public S3ClientInfo setDeviceModel(String paramString)
    {
      this.hasDeviceModel = true;
      this.deviceModel_ = paramString;
      return this;
    }
    
    public S3ClientInfo setPlatformId(String paramString)
    {
      this.hasPlatformId = true;
      this.platformId_ = paramString;
      return this;
    }
    
    public S3ClientInfo setPlatformVersion(String paramString)
    {
      this.hasPlatformVersion = true;
      this.platformVersion_ = paramString;
      return this;
    }
    
    public S3ClientInfo setTriggerApplicationId(String paramString)
    {
      this.hasTriggerApplicationId = true;
      this.triggerApplicationId_ = paramString;
      return this;
    }
    
    public S3ClientInfo setUserAgent(String paramString)
    {
      this.hasUserAgent = true;
      this.userAgent_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasClientId()) {
        paramCodedOutputStreamMicro.writeString(1, getClientId());
      }
      if (hasApplicationId()) {
        paramCodedOutputStreamMicro.writeString(2, getApplicationId());
      }
      if (hasUserAgent()) {
        paramCodedOutputStreamMicro.writeString(4, getUserAgent());
      }
      Iterator localIterator1 = getExperimentIdList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeString(5, (String)localIterator1.next());
      }
      if (hasPlatformId()) {
        paramCodedOutputStreamMicro.writeString(8, getPlatformId());
      }
      if (hasPlatformVersion()) {
        paramCodedOutputStreamMicro.writeString(9, getPlatformVersion());
      }
      if (hasApplicationVersion()) {
        paramCodedOutputStreamMicro.writeString(10, getApplicationVersion());
      }
      if (hasDeviceModel()) {
        paramCodedOutputStreamMicro.writeString(11, getDeviceModel());
      }
      if (hasDeviceDisplayWidthPixels()) {
        paramCodedOutputStreamMicro.writeInt32(12, getDeviceDisplayWidthPixels());
      }
      if (hasDeviceDisplayHeightPixels()) {
        paramCodedOutputStreamMicro.writeInt32(13, getDeviceDisplayHeightPixels());
      }
      if (hasDeviceDisplayDensityDpi()) {
        paramCodedOutputStreamMicro.writeInt32(14, getDeviceDisplayDensityDpi());
      }
      Iterator localIterator2 = getNoiseSuppressorIdList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeString(15, (String)localIterator2.next());
      }
      if (hasTriggerApplicationId()) {
        paramCodedOutputStreamMicro.writeString(16, getTriggerApplicationId());
      }
    }
  }
  
  public static final class S3ConnectionInfo
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String clientCountryCode_ = "ZZ";
    private String clientIp_ = "";
    private int clientPort_ = 0;
    private boolean debugIp_ = false;
    private String gfeFrontlineInfo_ = "";
    private boolean hasClientCountryCode;
    private boolean hasClientIp;
    private boolean hasClientPort;
    private boolean hasDebugIp;
    private boolean hasGfeFrontlineInfo;
    private boolean hasHost;
    private boolean hasImmediateClientIp;
    private boolean hasImmediateClientPort;
    private boolean hasLegacyClientTargetVip;
    private boolean hasType;
    private String host_ = "";
    private String immediateClientIp_ = "";
    private int immediateClientPort_ = 0;
    private String legacyClientTargetVip_ = "";
    private int type_ = 1;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getClientCountryCode()
    {
      return this.clientCountryCode_;
    }
    
    public String getClientIp()
    {
      return this.clientIp_;
    }
    
    public int getClientPort()
    {
      return this.clientPort_;
    }
    
    public boolean getDebugIp()
    {
      return this.debugIp_;
    }
    
    public String getGfeFrontlineInfo()
    {
      return this.gfeFrontlineInfo_;
    }
    
    public String getHost()
    {
      return this.host_;
    }
    
    public String getImmediateClientIp()
    {
      return this.immediateClientIp_;
    }
    
    public int getImmediateClientPort()
    {
      return this.immediateClientPort_;
    }
    
    public String getLegacyClientTargetVip()
    {
      return this.legacyClientTargetVip_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getType());
      }
      if (hasClientIp()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getClientIp());
      }
      if (hasClientPort()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getClientPort());
      }
      if (hasDebugIp()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getDebugIp());
      }
      if (hasGfeFrontlineInfo()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getGfeFrontlineInfo());
      }
      if (hasHost()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getHost());
      }
      if (hasImmediateClientIp()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getImmediateClientIp());
      }
      if (hasImmediateClientPort()) {
        i += CodedOutputStreamMicro.computeInt32Size(8, getImmediateClientPort());
      }
      if (hasLegacyClientTargetVip()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getLegacyClientTargetVip());
      }
      if (hasClientCountryCode()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getClientCountryCode());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getType()
    {
      return this.type_;
    }
    
    public boolean hasClientCountryCode()
    {
      return this.hasClientCountryCode;
    }
    
    public boolean hasClientIp()
    {
      return this.hasClientIp;
    }
    
    public boolean hasClientPort()
    {
      return this.hasClientPort;
    }
    
    public boolean hasDebugIp()
    {
      return this.hasDebugIp;
    }
    
    public boolean hasGfeFrontlineInfo()
    {
      return this.hasGfeFrontlineInfo;
    }
    
    public boolean hasHost()
    {
      return this.hasHost;
    }
    
    public boolean hasImmediateClientIp()
    {
      return this.hasImmediateClientIp;
    }
    
    public boolean hasImmediateClientPort()
    {
      return this.hasImmediateClientPort;
    }
    
    public boolean hasLegacyClientTargetVip()
    {
      return this.hasLegacyClientTargetVip;
    }
    
    public boolean hasType()
    {
      return this.hasType;
    }
    
    public S3ConnectionInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 18: 
          setClientIp(paramCodedInputStreamMicro.readString());
          break;
        case 24: 
          setClientPort(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setDebugIp(paramCodedInputStreamMicro.readBool());
          break;
        case 42: 
          setGfeFrontlineInfo(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          setHost(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          setImmediateClientIp(paramCodedInputStreamMicro.readString());
          break;
        case 64: 
          setImmediateClientPort(paramCodedInputStreamMicro.readInt32());
          break;
        case 74: 
          setLegacyClientTargetVip(paramCodedInputStreamMicro.readString());
          break;
        }
        setClientCountryCode(paramCodedInputStreamMicro.readString());
      }
    }
    
    public S3ConnectionInfo setClientCountryCode(String paramString)
    {
      this.hasClientCountryCode = true;
      this.clientCountryCode_ = paramString;
      return this;
    }
    
    public S3ConnectionInfo setClientIp(String paramString)
    {
      this.hasClientIp = true;
      this.clientIp_ = paramString;
      return this;
    }
    
    public S3ConnectionInfo setClientPort(int paramInt)
    {
      this.hasClientPort = true;
      this.clientPort_ = paramInt;
      return this;
    }
    
    public S3ConnectionInfo setDebugIp(boolean paramBoolean)
    {
      this.hasDebugIp = true;
      this.debugIp_ = paramBoolean;
      return this;
    }
    
    public S3ConnectionInfo setGfeFrontlineInfo(String paramString)
    {
      this.hasGfeFrontlineInfo = true;
      this.gfeFrontlineInfo_ = paramString;
      return this;
    }
    
    public S3ConnectionInfo setHost(String paramString)
    {
      this.hasHost = true;
      this.host_ = paramString;
      return this;
    }
    
    public S3ConnectionInfo setImmediateClientIp(String paramString)
    {
      this.hasImmediateClientIp = true;
      this.immediateClientIp_ = paramString;
      return this;
    }
    
    public S3ConnectionInfo setImmediateClientPort(int paramInt)
    {
      this.hasImmediateClientPort = true;
      this.immediateClientPort_ = paramInt;
      return this;
    }
    
    public S3ConnectionInfo setLegacyClientTargetVip(String paramString)
    {
      this.hasLegacyClientTargetVip = true;
      this.legacyClientTargetVip_ = paramString;
      return this;
    }
    
    public S3ConnectionInfo setType(int paramInt)
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
      if (hasClientIp()) {
        paramCodedOutputStreamMicro.writeString(2, getClientIp());
      }
      if (hasClientPort()) {
        paramCodedOutputStreamMicro.writeInt32(3, getClientPort());
      }
      if (hasDebugIp()) {
        paramCodedOutputStreamMicro.writeBool(4, getDebugIp());
      }
      if (hasGfeFrontlineInfo()) {
        paramCodedOutputStreamMicro.writeString(5, getGfeFrontlineInfo());
      }
      if (hasHost()) {
        paramCodedOutputStreamMicro.writeString(6, getHost());
      }
      if (hasImmediateClientIp()) {
        paramCodedOutputStreamMicro.writeString(7, getImmediateClientIp());
      }
      if (hasImmediateClientPort()) {
        paramCodedOutputStreamMicro.writeInt32(8, getImmediateClientPort());
      }
      if (hasLegacyClientTargetVip()) {
        paramCodedOutputStreamMicro.writeString(9, getLegacyClientTargetVip());
      }
      if (hasClientCountryCode()) {
        paramCodedOutputStreamMicro.writeString(10, getClientCountryCode());
      }
    }
  }
  
  public static final class S3DebugInfo
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
    
    public S3DebugInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
  
  public static final class S3ExperimentInfo
    extends MessageMicro
  {
    private List<Integer> applicableExperiments_ = Collections.emptyList();
    private int cachedSize = -1;
    private List<Integer> enabledExperiments_ = Collections.emptyList();
    
    public S3ExperimentInfo addApplicableExperiments(int paramInt)
    {
      if (this.applicableExperiments_.isEmpty()) {
        this.applicableExperiments_ = new ArrayList();
      }
      this.applicableExperiments_.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public S3ExperimentInfo addEnabledExperiments(int paramInt)
    {
      if (this.enabledExperiments_.isEmpty()) {
        this.enabledExperiments_ = new ArrayList();
      }
      this.enabledExperiments_.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public List<Integer> getApplicableExperimentsList()
    {
      return this.applicableExperiments_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<Integer> getEnabledExperimentsList()
    {
      return this.enabledExperiments_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getApplicableExperimentsList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator1.next()).intValue());
      }
      int j = 0 + i + 1 * getApplicableExperimentsList().size();
      int k = 0;
      Iterator localIterator2 = getEnabledExperimentsList().iterator();
      while (localIterator2.hasNext()) {
        k += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator2.next()).intValue());
      }
      int m = j + k + 1 * getEnabledExperimentsList().size();
      this.cachedSize = m;
      return m;
    }
    
    public S3ExperimentInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          addApplicableExperiments(paramCodedInputStreamMicro.readInt32());
          break;
        }
        addEnabledExperiments(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getApplicableExperimentsList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(1, ((Integer)localIterator1.next()).intValue());
      }
      Iterator localIterator2 = getEnabledExperimentsList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(2, ((Integer)localIterator2.next()).intValue());
      }
    }
  }
  
  public static final class S3Request
    extends MessageMicro
  {
    private int cachedSize = -1;
    private ClientLogRequestProto.ClientLogRequest clientLogRequestExtension_ = null;
    private boolean debuggingEnabled_ = false;
    private boolean endOfData_ = false;
    private GogglesProtos.GogglesClientLog gogglesClientLogExtension_ = null;
    private GogglesS3.GogglesS3SessionOptions gogglesS3SessionOptionsExtension_ = null;
    private GogglesProtos.GogglesStreamRequest gogglesStreamRequestExtension_ = null;
    private boolean hasClientLogRequestExtension;
    private boolean hasDebuggingEnabled;
    private boolean hasEndOfData;
    private boolean hasGogglesClientLogExtension;
    private boolean hasGogglesS3SessionOptionsExtension;
    private boolean hasGogglesStreamRequestExtension;
    private boolean hasLoggingEnabled;
    private boolean hasMajelClientInfoExtension;
    private boolean hasMajelServiceRequestExtension;
    private boolean hasMobileUserInfoExtension;
    private boolean hasPinholeParamsExtension;
    private boolean hasPinholeTtsBridgeParamsExtension;
    private boolean hasRecognizerVocabularyContextExtension;
    private boolean hasS3AudioDataExtension;
    private boolean hasS3AudioInfoExtension;
    private boolean hasS3ClientInfoExtension;
    private boolean hasS3ConnectionInfoExtension;
    private boolean hasS3DebugInfoExtension;
    private boolean hasS3ExperimentInfoExtension;
    private boolean hasS3RecognizerInfoExtension;
    private boolean hasS3SessionInfoExtension;
    private boolean hasS3UserInfoExtension;
    private boolean hasService;
    private boolean hasSoundSearchInfoExtension;
    private boolean hasTtsCapabilitiesRequestExtension;
    private boolean hasTtsServiceRequestExtension;
    private boolean loggingEnabled_ = true;
    private Majel.MajelClientInfo majelClientInfoExtension_ = null;
    private Majel.MajelServiceRequest majelServiceRequestExtension_ = null;
    private MobileUser.MobileUserInfo mobileUserInfoExtension_ = null;
    private PinholeStream.PinholeParams pinholeParamsExtension_ = null;
    private PinholeStream.PinholeTtsBridgeParams pinholeTtsBridgeParamsExtension_ = null;
    private Recognizer.RecognizerVocabularyContext recognizerVocabularyContextExtension_ = null;
    private Audio.S3AudioData s3AudioDataExtension_ = null;
    private Audio.S3AudioInfo s3AudioInfoExtension_ = null;
    private S3.S3ClientInfo s3ClientInfoExtension_ = null;
    private S3.S3ConnectionInfo s3ConnectionInfoExtension_ = null;
    private S3.S3DebugInfo s3DebugInfoExtension_ = null;
    private S3.S3ExperimentInfo s3ExperimentInfoExtension_ = null;
    private Recognizer.S3RecognizerInfo s3RecognizerInfoExtension_ = null;
    private S3.S3SessionInfo s3SessionInfoExtension_ = null;
    private S3.S3UserInfo s3UserInfoExtension_ = null;
    private String service_ = "";
    private SoundSearch.SoundSearchInfo soundSearchInfoExtension_ = null;
    private Synthesizer.TtsCapabilitiesRequest ttsCapabilitiesRequestExtension_ = null;
    private Synthesizer.TtsServiceRequest ttsServiceRequestExtension_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public ClientLogRequestProto.ClientLogRequest getClientLogRequestExtension()
    {
      return this.clientLogRequestExtension_;
    }
    
    public boolean getDebuggingEnabled()
    {
      return this.debuggingEnabled_;
    }
    
    public boolean getEndOfData()
    {
      return this.endOfData_;
    }
    
    public GogglesProtos.GogglesClientLog getGogglesClientLogExtension()
    {
      return this.gogglesClientLogExtension_;
    }
    
    public GogglesS3.GogglesS3SessionOptions getGogglesS3SessionOptionsExtension()
    {
      return this.gogglesS3SessionOptionsExtension_;
    }
    
    public GogglesProtos.GogglesStreamRequest getGogglesStreamRequestExtension()
    {
      return this.gogglesStreamRequestExtension_;
    }
    
    public boolean getLoggingEnabled()
    {
      return this.loggingEnabled_;
    }
    
    public Majel.MajelClientInfo getMajelClientInfoExtension()
    {
      return this.majelClientInfoExtension_;
    }
    
    public Majel.MajelServiceRequest getMajelServiceRequestExtension()
    {
      return this.majelServiceRequestExtension_;
    }
    
    public MobileUser.MobileUserInfo getMobileUserInfoExtension()
    {
      return this.mobileUserInfoExtension_;
    }
    
    public PinholeStream.PinholeParams getPinholeParamsExtension()
    {
      return this.pinholeParamsExtension_;
    }
    
    public PinholeStream.PinholeTtsBridgeParams getPinholeTtsBridgeParamsExtension()
    {
      return this.pinholeTtsBridgeParamsExtension_;
    }
    
    public Recognizer.RecognizerVocabularyContext getRecognizerVocabularyContextExtension()
    {
      return this.recognizerVocabularyContextExtension_;
    }
    
    public Audio.S3AudioData getS3AudioDataExtension()
    {
      return this.s3AudioDataExtension_;
    }
    
    public Audio.S3AudioInfo getS3AudioInfoExtension()
    {
      return this.s3AudioInfoExtension_;
    }
    
    public S3.S3ClientInfo getS3ClientInfoExtension()
    {
      return this.s3ClientInfoExtension_;
    }
    
    public S3.S3ConnectionInfo getS3ConnectionInfoExtension()
    {
      return this.s3ConnectionInfoExtension_;
    }
    
    public S3.S3DebugInfo getS3DebugInfoExtension()
    {
      return this.s3DebugInfoExtension_;
    }
    
    public S3.S3ExperimentInfo getS3ExperimentInfoExtension()
    {
      return this.s3ExperimentInfoExtension_;
    }
    
    public Recognizer.S3RecognizerInfo getS3RecognizerInfoExtension()
    {
      return this.s3RecognizerInfoExtension_;
    }
    
    public S3.S3SessionInfo getS3SessionInfoExtension()
    {
      return this.s3SessionInfoExtension_;
    }
    
    public S3.S3UserInfo getS3UserInfoExtension()
    {
      return this.s3UserInfoExtension_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasService();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getService());
      }
      if (hasLoggingEnabled()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getLoggingEnabled());
      }
      if (hasEndOfData()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getEndOfData());
      }
      if (hasDebuggingEnabled()) {
        i += CodedOutputStreamMicro.computeBoolSize(4, getDebuggingEnabled());
      }
      if (hasS3UserInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(293000, getS3UserInfoExtension());
      }
      if (hasS3AudioInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(293100, getS3AudioInfoExtension());
      }
      if (hasS3AudioDataExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(293101, getS3AudioDataExtension());
      }
      if (hasS3ClientInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(294000, getS3ClientInfoExtension());
      }
      if (hasS3RecognizerInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(294500, getS3RecognizerInfoExtension());
      }
      if (hasMobileUserInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(27301014, getMobileUserInfoExtension());
      }
      if (hasS3SessionInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(27423252, getS3SessionInfoExtension());
      }
      if (hasClientLogRequestExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(27800551, getClientLogRequestExtension());
      }
      if (hasTtsServiceRequestExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(27801516, getTtsServiceRequestExtension());
      }
      if (hasMajelServiceRequestExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(27834892, getMajelServiceRequestExtension());
      }
      if (hasMajelClientInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(29734512, getMajelClientInfoExtension());
      }
      if (hasS3ExperimentInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(33357560, getS3ExperimentInfoExtension());
      }
      if (hasPinholeParamsExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(34352150, getPinholeParamsExtension());
      }
      if (hasS3ConnectionInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(34552679, getS3ConnectionInfoExtension());
      }
      if (hasSoundSearchInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(35351945, getSoundSearchInfoExtension());
      }
      if (hasGogglesClientLogExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(35379363, getGogglesClientLogExtension());
      }
      if (hasGogglesS3SessionOptionsExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(35380122, getGogglesS3SessionOptionsExtension());
      }
      if (hasGogglesStreamRequestExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(35570342, getGogglesStreamRequestExtension());
      }
      if (hasPinholeTtsBridgeParamsExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(40941187, getPinholeTtsBridgeParamsExtension());
      }
      if (hasTtsCapabilitiesRequestExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(45981484, getTtsCapabilitiesRequestExtension());
      }
      if (hasS3DebugInfoExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(47096292, getS3DebugInfoExtension());
      }
      if (hasRecognizerVocabularyContextExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(54660322, getRecognizerVocabularyContextExtension());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getService()
    {
      return this.service_;
    }
    
    public SoundSearch.SoundSearchInfo getSoundSearchInfoExtension()
    {
      return this.soundSearchInfoExtension_;
    }
    
    public Synthesizer.TtsCapabilitiesRequest getTtsCapabilitiesRequestExtension()
    {
      return this.ttsCapabilitiesRequestExtension_;
    }
    
    public Synthesizer.TtsServiceRequest getTtsServiceRequestExtension()
    {
      return this.ttsServiceRequestExtension_;
    }
    
    public boolean hasClientLogRequestExtension()
    {
      return this.hasClientLogRequestExtension;
    }
    
    public boolean hasDebuggingEnabled()
    {
      return this.hasDebuggingEnabled;
    }
    
    public boolean hasEndOfData()
    {
      return this.hasEndOfData;
    }
    
    public boolean hasGogglesClientLogExtension()
    {
      return this.hasGogglesClientLogExtension;
    }
    
    public boolean hasGogglesS3SessionOptionsExtension()
    {
      return this.hasGogglesS3SessionOptionsExtension;
    }
    
    public boolean hasGogglesStreamRequestExtension()
    {
      return this.hasGogglesStreamRequestExtension;
    }
    
    public boolean hasLoggingEnabled()
    {
      return this.hasLoggingEnabled;
    }
    
    public boolean hasMajelClientInfoExtension()
    {
      return this.hasMajelClientInfoExtension;
    }
    
    public boolean hasMajelServiceRequestExtension()
    {
      return this.hasMajelServiceRequestExtension;
    }
    
    public boolean hasMobileUserInfoExtension()
    {
      return this.hasMobileUserInfoExtension;
    }
    
    public boolean hasPinholeParamsExtension()
    {
      return this.hasPinholeParamsExtension;
    }
    
    public boolean hasPinholeTtsBridgeParamsExtension()
    {
      return this.hasPinholeTtsBridgeParamsExtension;
    }
    
    public boolean hasRecognizerVocabularyContextExtension()
    {
      return this.hasRecognizerVocabularyContextExtension;
    }
    
    public boolean hasS3AudioDataExtension()
    {
      return this.hasS3AudioDataExtension;
    }
    
    public boolean hasS3AudioInfoExtension()
    {
      return this.hasS3AudioInfoExtension;
    }
    
    public boolean hasS3ClientInfoExtension()
    {
      return this.hasS3ClientInfoExtension;
    }
    
    public boolean hasS3ConnectionInfoExtension()
    {
      return this.hasS3ConnectionInfoExtension;
    }
    
    public boolean hasS3DebugInfoExtension()
    {
      return this.hasS3DebugInfoExtension;
    }
    
    public boolean hasS3ExperimentInfoExtension()
    {
      return this.hasS3ExperimentInfoExtension;
    }
    
    public boolean hasS3RecognizerInfoExtension()
    {
      return this.hasS3RecognizerInfoExtension;
    }
    
    public boolean hasS3SessionInfoExtension()
    {
      return this.hasS3SessionInfoExtension;
    }
    
    public boolean hasS3UserInfoExtension()
    {
      return this.hasS3UserInfoExtension;
    }
    
    public boolean hasService()
    {
      return this.hasService;
    }
    
    public boolean hasSoundSearchInfoExtension()
    {
      return this.hasSoundSearchInfoExtension;
    }
    
    public boolean hasTtsCapabilitiesRequestExtension()
    {
      return this.hasTtsCapabilitiesRequestExtension;
    }
    
    public boolean hasTtsServiceRequestExtension()
    {
      return this.hasTtsServiceRequestExtension;
    }
    
    public S3Request mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setService(paramCodedInputStreamMicro.readString());
          break;
        case 16: 
          setLoggingEnabled(paramCodedInputStreamMicro.readBool());
          break;
        case 24: 
          setEndOfData(paramCodedInputStreamMicro.readBool());
          break;
        case 32: 
          setDebuggingEnabled(paramCodedInputStreamMicro.readBool());
          break;
        case 2344002: 
          S3.S3UserInfo localS3UserInfo = new S3.S3UserInfo();
          paramCodedInputStreamMicro.readMessage(localS3UserInfo);
          setS3UserInfoExtension(localS3UserInfo);
          break;
        case 2344802: 
          Audio.S3AudioInfo localS3AudioInfo = new Audio.S3AudioInfo();
          paramCodedInputStreamMicro.readMessage(localS3AudioInfo);
          setS3AudioInfoExtension(localS3AudioInfo);
          break;
        case 2344810: 
          Audio.S3AudioData localS3AudioData = new Audio.S3AudioData();
          paramCodedInputStreamMicro.readMessage(localS3AudioData);
          setS3AudioDataExtension(localS3AudioData);
          break;
        case 2352002: 
          S3.S3ClientInfo localS3ClientInfo = new S3.S3ClientInfo();
          paramCodedInputStreamMicro.readMessage(localS3ClientInfo);
          setS3ClientInfoExtension(localS3ClientInfo);
          break;
        case 2356002: 
          Recognizer.S3RecognizerInfo localS3RecognizerInfo = new Recognizer.S3RecognizerInfo();
          paramCodedInputStreamMicro.readMessage(localS3RecognizerInfo);
          setS3RecognizerInfoExtension(localS3RecognizerInfo);
          break;
        case 218408114: 
          MobileUser.MobileUserInfo localMobileUserInfo = new MobileUser.MobileUserInfo();
          paramCodedInputStreamMicro.readMessage(localMobileUserInfo);
          setMobileUserInfoExtension(localMobileUserInfo);
          break;
        case 219386018: 
          S3.S3SessionInfo localS3SessionInfo = new S3.S3SessionInfo();
          paramCodedInputStreamMicro.readMessage(localS3SessionInfo);
          setS3SessionInfoExtension(localS3SessionInfo);
          break;
        case 222404410: 
          ClientLogRequestProto.ClientLogRequest localClientLogRequest = new ClientLogRequestProto.ClientLogRequest();
          paramCodedInputStreamMicro.readMessage(localClientLogRequest);
          setClientLogRequestExtension(localClientLogRequest);
          break;
        case 222412130: 
          Synthesizer.TtsServiceRequest localTtsServiceRequest = new Synthesizer.TtsServiceRequest();
          paramCodedInputStreamMicro.readMessage(localTtsServiceRequest);
          setTtsServiceRequestExtension(localTtsServiceRequest);
          break;
        case 222679138: 
          Majel.MajelServiceRequest localMajelServiceRequest = new Majel.MajelServiceRequest();
          paramCodedInputStreamMicro.readMessage(localMajelServiceRequest);
          setMajelServiceRequestExtension(localMajelServiceRequest);
          break;
        case 237876098: 
          Majel.MajelClientInfo localMajelClientInfo = new Majel.MajelClientInfo();
          paramCodedInputStreamMicro.readMessage(localMajelClientInfo);
          setMajelClientInfoExtension(localMajelClientInfo);
          break;
        case 266860482: 
          S3.S3ExperimentInfo localS3ExperimentInfo = new S3.S3ExperimentInfo();
          paramCodedInputStreamMicro.readMessage(localS3ExperimentInfo);
          setS3ExperimentInfoExtension(localS3ExperimentInfo);
          break;
        case 274817202: 
          PinholeStream.PinholeParams localPinholeParams = new PinholeStream.PinholeParams();
          paramCodedInputStreamMicro.readMessage(localPinholeParams);
          setPinholeParamsExtension(localPinholeParams);
          break;
        case 276421434: 
          S3.S3ConnectionInfo localS3ConnectionInfo = new S3.S3ConnectionInfo();
          paramCodedInputStreamMicro.readMessage(localS3ConnectionInfo);
          setS3ConnectionInfoExtension(localS3ConnectionInfo);
          break;
        case 282815562: 
          SoundSearch.SoundSearchInfo localSoundSearchInfo = new SoundSearch.SoundSearchInfo();
          paramCodedInputStreamMicro.readMessage(localSoundSearchInfo);
          setSoundSearchInfoExtension(localSoundSearchInfo);
          break;
        case 283034906: 
          GogglesProtos.GogglesClientLog localGogglesClientLog = new GogglesProtos.GogglesClientLog();
          paramCodedInputStreamMicro.readMessage(localGogglesClientLog);
          setGogglesClientLogExtension(localGogglesClientLog);
          break;
        case 283040978: 
          GogglesS3.GogglesS3SessionOptions localGogglesS3SessionOptions = new GogglesS3.GogglesS3SessionOptions();
          paramCodedInputStreamMicro.readMessage(localGogglesS3SessionOptions);
          setGogglesS3SessionOptionsExtension(localGogglesS3SessionOptions);
          break;
        case 284562738: 
          GogglesProtos.GogglesStreamRequest localGogglesStreamRequest = new GogglesProtos.GogglesStreamRequest();
          paramCodedInputStreamMicro.readMessage(localGogglesStreamRequest);
          setGogglesStreamRequestExtension(localGogglesStreamRequest);
          break;
        case 327529498: 
          PinholeStream.PinholeTtsBridgeParams localPinholeTtsBridgeParams = new PinholeStream.PinholeTtsBridgeParams();
          paramCodedInputStreamMicro.readMessage(localPinholeTtsBridgeParams);
          setPinholeTtsBridgeParamsExtension(localPinholeTtsBridgeParams);
          break;
        case 367851874: 
          Synthesizer.TtsCapabilitiesRequest localTtsCapabilitiesRequest = new Synthesizer.TtsCapabilitiesRequest();
          paramCodedInputStreamMicro.readMessage(localTtsCapabilitiesRequest);
          setTtsCapabilitiesRequestExtension(localTtsCapabilitiesRequest);
          break;
        case 376770338: 
          S3.S3DebugInfo localS3DebugInfo = new S3.S3DebugInfo();
          paramCodedInputStreamMicro.readMessage(localS3DebugInfo);
          setS3DebugInfoExtension(localS3DebugInfo);
          break;
        }
        Recognizer.RecognizerVocabularyContext localRecognizerVocabularyContext = new Recognizer.RecognizerVocabularyContext();
        paramCodedInputStreamMicro.readMessage(localRecognizerVocabularyContext);
        setRecognizerVocabularyContextExtension(localRecognizerVocabularyContext);
      }
    }
    
    public S3Request setClientLogRequestExtension(ClientLogRequestProto.ClientLogRequest paramClientLogRequest)
    {
      if (paramClientLogRequest == null) {
        throw new NullPointerException();
      }
      this.hasClientLogRequestExtension = true;
      this.clientLogRequestExtension_ = paramClientLogRequest;
      return this;
    }
    
    public S3Request setDebuggingEnabled(boolean paramBoolean)
    {
      this.hasDebuggingEnabled = true;
      this.debuggingEnabled_ = paramBoolean;
      return this;
    }
    
    public S3Request setEndOfData(boolean paramBoolean)
    {
      this.hasEndOfData = true;
      this.endOfData_ = paramBoolean;
      return this;
    }
    
    public S3Request setGogglesClientLogExtension(GogglesProtos.GogglesClientLog paramGogglesClientLog)
    {
      if (paramGogglesClientLog == null) {
        throw new NullPointerException();
      }
      this.hasGogglesClientLogExtension = true;
      this.gogglesClientLogExtension_ = paramGogglesClientLog;
      return this;
    }
    
    public S3Request setGogglesS3SessionOptionsExtension(GogglesS3.GogglesS3SessionOptions paramGogglesS3SessionOptions)
    {
      if (paramGogglesS3SessionOptions == null) {
        throw new NullPointerException();
      }
      this.hasGogglesS3SessionOptionsExtension = true;
      this.gogglesS3SessionOptionsExtension_ = paramGogglesS3SessionOptions;
      return this;
    }
    
    public S3Request setGogglesStreamRequestExtension(GogglesProtos.GogglesStreamRequest paramGogglesStreamRequest)
    {
      if (paramGogglesStreamRequest == null) {
        throw new NullPointerException();
      }
      this.hasGogglesStreamRequestExtension = true;
      this.gogglesStreamRequestExtension_ = paramGogglesStreamRequest;
      return this;
    }
    
    public S3Request setLoggingEnabled(boolean paramBoolean)
    {
      this.hasLoggingEnabled = true;
      this.loggingEnabled_ = paramBoolean;
      return this;
    }
    
    public S3Request setMajelClientInfoExtension(Majel.MajelClientInfo paramMajelClientInfo)
    {
      if (paramMajelClientInfo == null) {
        throw new NullPointerException();
      }
      this.hasMajelClientInfoExtension = true;
      this.majelClientInfoExtension_ = paramMajelClientInfo;
      return this;
    }
    
    public S3Request setMajelServiceRequestExtension(Majel.MajelServiceRequest paramMajelServiceRequest)
    {
      if (paramMajelServiceRequest == null) {
        throw new NullPointerException();
      }
      this.hasMajelServiceRequestExtension = true;
      this.majelServiceRequestExtension_ = paramMajelServiceRequest;
      return this;
    }
    
    public S3Request setMobileUserInfoExtension(MobileUser.MobileUserInfo paramMobileUserInfo)
    {
      if (paramMobileUserInfo == null) {
        throw new NullPointerException();
      }
      this.hasMobileUserInfoExtension = true;
      this.mobileUserInfoExtension_ = paramMobileUserInfo;
      return this;
    }
    
    public S3Request setPinholeParamsExtension(PinholeStream.PinholeParams paramPinholeParams)
    {
      if (paramPinholeParams == null) {
        throw new NullPointerException();
      }
      this.hasPinholeParamsExtension = true;
      this.pinholeParamsExtension_ = paramPinholeParams;
      return this;
    }
    
    public S3Request setPinholeTtsBridgeParamsExtension(PinholeStream.PinholeTtsBridgeParams paramPinholeTtsBridgeParams)
    {
      if (paramPinholeTtsBridgeParams == null) {
        throw new NullPointerException();
      }
      this.hasPinholeTtsBridgeParamsExtension = true;
      this.pinholeTtsBridgeParamsExtension_ = paramPinholeTtsBridgeParams;
      return this;
    }
    
    public S3Request setRecognizerVocabularyContextExtension(Recognizer.RecognizerVocabularyContext paramRecognizerVocabularyContext)
    {
      if (paramRecognizerVocabularyContext == null) {
        throw new NullPointerException();
      }
      this.hasRecognizerVocabularyContextExtension = true;
      this.recognizerVocabularyContextExtension_ = paramRecognizerVocabularyContext;
      return this;
    }
    
    public S3Request setS3AudioDataExtension(Audio.S3AudioData paramS3AudioData)
    {
      if (paramS3AudioData == null) {
        throw new NullPointerException();
      }
      this.hasS3AudioDataExtension = true;
      this.s3AudioDataExtension_ = paramS3AudioData;
      return this;
    }
    
    public S3Request setS3AudioInfoExtension(Audio.S3AudioInfo paramS3AudioInfo)
    {
      if (paramS3AudioInfo == null) {
        throw new NullPointerException();
      }
      this.hasS3AudioInfoExtension = true;
      this.s3AudioInfoExtension_ = paramS3AudioInfo;
      return this;
    }
    
    public S3Request setS3ClientInfoExtension(S3.S3ClientInfo paramS3ClientInfo)
    {
      if (paramS3ClientInfo == null) {
        throw new NullPointerException();
      }
      this.hasS3ClientInfoExtension = true;
      this.s3ClientInfoExtension_ = paramS3ClientInfo;
      return this;
    }
    
    public S3Request setS3ConnectionInfoExtension(S3.S3ConnectionInfo paramS3ConnectionInfo)
    {
      if (paramS3ConnectionInfo == null) {
        throw new NullPointerException();
      }
      this.hasS3ConnectionInfoExtension = true;
      this.s3ConnectionInfoExtension_ = paramS3ConnectionInfo;
      return this;
    }
    
    public S3Request setS3DebugInfoExtension(S3.S3DebugInfo paramS3DebugInfo)
    {
      if (paramS3DebugInfo == null) {
        throw new NullPointerException();
      }
      this.hasS3DebugInfoExtension = true;
      this.s3DebugInfoExtension_ = paramS3DebugInfo;
      return this;
    }
    
    public S3Request setS3ExperimentInfoExtension(S3.S3ExperimentInfo paramS3ExperimentInfo)
    {
      if (paramS3ExperimentInfo == null) {
        throw new NullPointerException();
      }
      this.hasS3ExperimentInfoExtension = true;
      this.s3ExperimentInfoExtension_ = paramS3ExperimentInfo;
      return this;
    }
    
    public S3Request setS3RecognizerInfoExtension(Recognizer.S3RecognizerInfo paramS3RecognizerInfo)
    {
      if (paramS3RecognizerInfo == null) {
        throw new NullPointerException();
      }
      this.hasS3RecognizerInfoExtension = true;
      this.s3RecognizerInfoExtension_ = paramS3RecognizerInfo;
      return this;
    }
    
    public S3Request setS3SessionInfoExtension(S3.S3SessionInfo paramS3SessionInfo)
    {
      if (paramS3SessionInfo == null) {
        throw new NullPointerException();
      }
      this.hasS3SessionInfoExtension = true;
      this.s3SessionInfoExtension_ = paramS3SessionInfo;
      return this;
    }
    
    public S3Request setS3UserInfoExtension(S3.S3UserInfo paramS3UserInfo)
    {
      if (paramS3UserInfo == null) {
        throw new NullPointerException();
      }
      this.hasS3UserInfoExtension = true;
      this.s3UserInfoExtension_ = paramS3UserInfo;
      return this;
    }
    
    public S3Request setService(String paramString)
    {
      this.hasService = true;
      this.service_ = paramString;
      return this;
    }
    
    public S3Request setSoundSearchInfoExtension(SoundSearch.SoundSearchInfo paramSoundSearchInfo)
    {
      if (paramSoundSearchInfo == null) {
        throw new NullPointerException();
      }
      this.hasSoundSearchInfoExtension = true;
      this.soundSearchInfoExtension_ = paramSoundSearchInfo;
      return this;
    }
    
    public S3Request setTtsCapabilitiesRequestExtension(Synthesizer.TtsCapabilitiesRequest paramTtsCapabilitiesRequest)
    {
      if (paramTtsCapabilitiesRequest == null) {
        throw new NullPointerException();
      }
      this.hasTtsCapabilitiesRequestExtension = true;
      this.ttsCapabilitiesRequestExtension_ = paramTtsCapabilitiesRequest;
      return this;
    }
    
    public S3Request setTtsServiceRequestExtension(Synthesizer.TtsServiceRequest paramTtsServiceRequest)
    {
      if (paramTtsServiceRequest == null) {
        throw new NullPointerException();
      }
      this.hasTtsServiceRequestExtension = true;
      this.ttsServiceRequestExtension_ = paramTtsServiceRequest;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasService()) {
        paramCodedOutputStreamMicro.writeString(1, getService());
      }
      if (hasLoggingEnabled()) {
        paramCodedOutputStreamMicro.writeBool(2, getLoggingEnabled());
      }
      if (hasEndOfData()) {
        paramCodedOutputStreamMicro.writeBool(3, getEndOfData());
      }
      if (hasDebuggingEnabled()) {
        paramCodedOutputStreamMicro.writeBool(4, getDebuggingEnabled());
      }
      if (hasS3UserInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(293000, getS3UserInfoExtension());
      }
      if (hasS3AudioInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(293100, getS3AudioInfoExtension());
      }
      if (hasS3AudioDataExtension()) {
        paramCodedOutputStreamMicro.writeMessage(293101, getS3AudioDataExtension());
      }
      if (hasS3ClientInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(294000, getS3ClientInfoExtension());
      }
      if (hasS3RecognizerInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(294500, getS3RecognizerInfoExtension());
      }
      if (hasMobileUserInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(27301014, getMobileUserInfoExtension());
      }
      if (hasS3SessionInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(27423252, getS3SessionInfoExtension());
      }
      if (hasClientLogRequestExtension()) {
        paramCodedOutputStreamMicro.writeMessage(27800551, getClientLogRequestExtension());
      }
      if (hasTtsServiceRequestExtension()) {
        paramCodedOutputStreamMicro.writeMessage(27801516, getTtsServiceRequestExtension());
      }
      if (hasMajelServiceRequestExtension()) {
        paramCodedOutputStreamMicro.writeMessage(27834892, getMajelServiceRequestExtension());
      }
      if (hasMajelClientInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(29734512, getMajelClientInfoExtension());
      }
      if (hasS3ExperimentInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(33357560, getS3ExperimentInfoExtension());
      }
      if (hasPinholeParamsExtension()) {
        paramCodedOutputStreamMicro.writeMessage(34352150, getPinholeParamsExtension());
      }
      if (hasS3ConnectionInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(34552679, getS3ConnectionInfoExtension());
      }
      if (hasSoundSearchInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(35351945, getSoundSearchInfoExtension());
      }
      if (hasGogglesClientLogExtension()) {
        paramCodedOutputStreamMicro.writeMessage(35379363, getGogglesClientLogExtension());
      }
      if (hasGogglesS3SessionOptionsExtension()) {
        paramCodedOutputStreamMicro.writeMessage(35380122, getGogglesS3SessionOptionsExtension());
      }
      if (hasGogglesStreamRequestExtension()) {
        paramCodedOutputStreamMicro.writeMessage(35570342, getGogglesStreamRequestExtension());
      }
      if (hasPinholeTtsBridgeParamsExtension()) {
        paramCodedOutputStreamMicro.writeMessage(40941187, getPinholeTtsBridgeParamsExtension());
      }
      if (hasTtsCapabilitiesRequestExtension()) {
        paramCodedOutputStreamMicro.writeMessage(45981484, getTtsCapabilitiesRequestExtension());
      }
      if (hasS3DebugInfoExtension()) {
        paramCodedOutputStreamMicro.writeMessage(47096292, getS3DebugInfoExtension());
      }
      if (hasRecognizerVocabularyContextExtension()) {
        paramCodedOutputStreamMicro.writeMessage(54660322, getRecognizerVocabularyContextExtension());
      }
    }
  }
  
  public static final class S3Response
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<String> debugLine_ = Collections.emptyList();
    private int errorCode_ = 0;
    private String errorDescription_ = "";
    private GogglesProtos.GogglesStreamResponse gogglesStreamResponseExtension_ = null;
    private boolean hasErrorCode;
    private boolean hasErrorDescription;
    private boolean hasGogglesStreamResponseExtension;
    private boolean hasMajelServiceEventExtension;
    private boolean hasPinholeOutputExtension;
    private boolean hasRecognizerEventExtension;
    private boolean hasSoundSearchServiceEventExtension;
    private boolean hasStatus;
    private boolean hasTtsCapabilitiesResponseExtension;
    private boolean hasTtsServiceEventExtension;
    private Majel.MajelServiceEvent majelServiceEventExtension_ = null;
    private PinholeStream.PinholeOutput pinholeOutputExtension_ = null;
    private Recognizer.RecognizerEvent recognizerEventExtension_ = null;
    private SoundSearch.SoundSearchServiceEvent soundSearchServiceEventExtension_ = null;
    private int status_ = 0;
    private Synthesizer.TtsCapabilitiesResponse ttsCapabilitiesResponseExtension_ = null;
    private Synthesizer.TtsServiceEvent ttsServiceEventExtension_ = null;
    
    public S3Response addDebugLine(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.debugLine_.isEmpty()) {
        this.debugLine_ = new ArrayList();
      }
      this.debugLine_.add(paramString);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<String> getDebugLineList()
    {
      return this.debugLine_;
    }
    
    public int getErrorCode()
    {
      return this.errorCode_;
    }
    
    public String getErrorDescription()
    {
      return this.errorDescription_;
    }
    
    public GogglesProtos.GogglesStreamResponse getGogglesStreamResponseExtension()
    {
      return this.gogglesStreamResponseExtension_;
    }
    
    public Majel.MajelServiceEvent getMajelServiceEventExtension()
    {
      return this.majelServiceEventExtension_;
    }
    
    public PinholeStream.PinholeOutput getPinholeOutputExtension()
    {
      return this.pinholeOutputExtension_;
    }
    
    public Recognizer.RecognizerEvent getRecognizerEventExtension()
    {
      return this.recognizerEventExtension_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasStatus();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getStatus());
      }
      if (hasErrorCode()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getErrorCode());
      }
      if (hasErrorDescription()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getErrorDescription());
      }
      int j = 0;
      Iterator localIterator = getDebugLineList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getDebugLineList().size();
      if (hasRecognizerEventExtension()) {
        k += CodedOutputStreamMicro.computeMessageSize(1253625, getRecognizerEventExtension());
      }
      if (hasMajelServiceEventExtension()) {
        k += CodedOutputStreamMicro.computeMessageSize(26599812, getMajelServiceEventExtension());
      }
      if (hasTtsServiceEventExtension()) {
        k += CodedOutputStreamMicro.computeMessageSize(28599812, getTtsServiceEventExtension());
      }
      if (hasSoundSearchServiceEventExtension()) {
        k += CodedOutputStreamMicro.computeMessageSize(35351945, getSoundSearchServiceEventExtension());
      }
      if (hasGogglesStreamResponseExtension()) {
        k += CodedOutputStreamMicro.computeMessageSize(35419983, getGogglesStreamResponseExtension());
      }
      if (hasPinholeOutputExtension()) {
        k += CodedOutputStreamMicro.computeMessageSize(39442181, getPinholeOutputExtension());
      }
      if (hasTtsCapabilitiesResponseExtension()) {
        k += CodedOutputStreamMicro.computeMessageSize(45982169, getTtsCapabilitiesResponseExtension());
      }
      this.cachedSize = k;
      return k;
    }
    
    public SoundSearch.SoundSearchServiceEvent getSoundSearchServiceEventExtension()
    {
      return this.soundSearchServiceEventExtension_;
    }
    
    public int getStatus()
    {
      return this.status_;
    }
    
    public Synthesizer.TtsCapabilitiesResponse getTtsCapabilitiesResponseExtension()
    {
      return this.ttsCapabilitiesResponseExtension_;
    }
    
    public Synthesizer.TtsServiceEvent getTtsServiceEventExtension()
    {
      return this.ttsServiceEventExtension_;
    }
    
    public boolean hasErrorCode()
    {
      return this.hasErrorCode;
    }
    
    public boolean hasErrorDescription()
    {
      return this.hasErrorDescription;
    }
    
    public boolean hasGogglesStreamResponseExtension()
    {
      return this.hasGogglesStreamResponseExtension;
    }
    
    public boolean hasMajelServiceEventExtension()
    {
      return this.hasMajelServiceEventExtension;
    }
    
    public boolean hasPinholeOutputExtension()
    {
      return this.hasPinholeOutputExtension;
    }
    
    public boolean hasRecognizerEventExtension()
    {
      return this.hasRecognizerEventExtension;
    }
    
    public boolean hasSoundSearchServiceEventExtension()
    {
      return this.hasSoundSearchServiceEventExtension;
    }
    
    public boolean hasStatus()
    {
      return this.hasStatus;
    }
    
    public boolean hasTtsCapabilitiesResponseExtension()
    {
      return this.hasTtsCapabilitiesResponseExtension;
    }
    
    public boolean hasTtsServiceEventExtension()
    {
      return this.hasTtsServiceEventExtension;
    }
    
    public S3Response mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setStatus(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setErrorCode(paramCodedInputStreamMicro.readInt32());
          break;
        case 26: 
          setErrorDescription(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          addDebugLine(paramCodedInputStreamMicro.readString());
          break;
        case 10029002: 
          Recognizer.RecognizerEvent localRecognizerEvent = new Recognizer.RecognizerEvent();
          paramCodedInputStreamMicro.readMessage(localRecognizerEvent);
          setRecognizerEventExtension(localRecognizerEvent);
          break;
        case 212798498: 
          Majel.MajelServiceEvent localMajelServiceEvent = new Majel.MajelServiceEvent();
          paramCodedInputStreamMicro.readMessage(localMajelServiceEvent);
          setMajelServiceEventExtension(localMajelServiceEvent);
          break;
        case 228798498: 
          Synthesizer.TtsServiceEvent localTtsServiceEvent = new Synthesizer.TtsServiceEvent();
          paramCodedInputStreamMicro.readMessage(localTtsServiceEvent);
          setTtsServiceEventExtension(localTtsServiceEvent);
          break;
        case 282815562: 
          SoundSearch.SoundSearchServiceEvent localSoundSearchServiceEvent = new SoundSearch.SoundSearchServiceEvent();
          paramCodedInputStreamMicro.readMessage(localSoundSearchServiceEvent);
          setSoundSearchServiceEventExtension(localSoundSearchServiceEvent);
          break;
        case 283359866: 
          GogglesProtos.GogglesStreamResponse localGogglesStreamResponse = new GogglesProtos.GogglesStreamResponse();
          paramCodedInputStreamMicro.readMessage(localGogglesStreamResponse);
          setGogglesStreamResponseExtension(localGogglesStreamResponse);
          break;
        case 315537450: 
          PinholeStream.PinholeOutput localPinholeOutput = new PinholeStream.PinholeOutput();
          paramCodedInputStreamMicro.readMessage(localPinholeOutput);
          setPinholeOutputExtension(localPinholeOutput);
          break;
        }
        Synthesizer.TtsCapabilitiesResponse localTtsCapabilitiesResponse = new Synthesizer.TtsCapabilitiesResponse();
        paramCodedInputStreamMicro.readMessage(localTtsCapabilitiesResponse);
        setTtsCapabilitiesResponseExtension(localTtsCapabilitiesResponse);
      }
    }
    
    public S3Response setErrorCode(int paramInt)
    {
      this.hasErrorCode = true;
      this.errorCode_ = paramInt;
      return this;
    }
    
    public S3Response setErrorDescription(String paramString)
    {
      this.hasErrorDescription = true;
      this.errorDescription_ = paramString;
      return this;
    }
    
    public S3Response setGogglesStreamResponseExtension(GogglesProtos.GogglesStreamResponse paramGogglesStreamResponse)
    {
      if (paramGogglesStreamResponse == null) {
        throw new NullPointerException();
      }
      this.hasGogglesStreamResponseExtension = true;
      this.gogglesStreamResponseExtension_ = paramGogglesStreamResponse;
      return this;
    }
    
    public S3Response setMajelServiceEventExtension(Majel.MajelServiceEvent paramMajelServiceEvent)
    {
      if (paramMajelServiceEvent == null) {
        throw new NullPointerException();
      }
      this.hasMajelServiceEventExtension = true;
      this.majelServiceEventExtension_ = paramMajelServiceEvent;
      return this;
    }
    
    public S3Response setPinholeOutputExtension(PinholeStream.PinholeOutput paramPinholeOutput)
    {
      if (paramPinholeOutput == null) {
        throw new NullPointerException();
      }
      this.hasPinholeOutputExtension = true;
      this.pinholeOutputExtension_ = paramPinholeOutput;
      return this;
    }
    
    public S3Response setRecognizerEventExtension(Recognizer.RecognizerEvent paramRecognizerEvent)
    {
      if (paramRecognizerEvent == null) {
        throw new NullPointerException();
      }
      this.hasRecognizerEventExtension = true;
      this.recognizerEventExtension_ = paramRecognizerEvent;
      return this;
    }
    
    public S3Response setSoundSearchServiceEventExtension(SoundSearch.SoundSearchServiceEvent paramSoundSearchServiceEvent)
    {
      if (paramSoundSearchServiceEvent == null) {
        throw new NullPointerException();
      }
      this.hasSoundSearchServiceEventExtension = true;
      this.soundSearchServiceEventExtension_ = paramSoundSearchServiceEvent;
      return this;
    }
    
    public S3Response setStatus(int paramInt)
    {
      this.hasStatus = true;
      this.status_ = paramInt;
      return this;
    }
    
    public S3Response setTtsCapabilitiesResponseExtension(Synthesizer.TtsCapabilitiesResponse paramTtsCapabilitiesResponse)
    {
      if (paramTtsCapabilitiesResponse == null) {
        throw new NullPointerException();
      }
      this.hasTtsCapabilitiesResponseExtension = true;
      this.ttsCapabilitiesResponseExtension_ = paramTtsCapabilitiesResponse;
      return this;
    }
    
    public S3Response setTtsServiceEventExtension(Synthesizer.TtsServiceEvent paramTtsServiceEvent)
    {
      if (paramTtsServiceEvent == null) {
        throw new NullPointerException();
      }
      this.hasTtsServiceEventExtension = true;
      this.ttsServiceEventExtension_ = paramTtsServiceEvent;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasStatus()) {
        paramCodedOutputStreamMicro.writeInt32(1, getStatus());
      }
      if (hasErrorCode()) {
        paramCodedOutputStreamMicro.writeInt32(2, getErrorCode());
      }
      if (hasErrorDescription()) {
        paramCodedOutputStreamMicro.writeString(3, getErrorDescription());
      }
      Iterator localIterator = getDebugLineList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(4, (String)localIterator.next());
      }
      if (hasRecognizerEventExtension()) {
        paramCodedOutputStreamMicro.writeMessage(1253625, getRecognizerEventExtension());
      }
      if (hasMajelServiceEventExtension()) {
        paramCodedOutputStreamMicro.writeMessage(26599812, getMajelServiceEventExtension());
      }
      if (hasTtsServiceEventExtension()) {
        paramCodedOutputStreamMicro.writeMessage(28599812, getTtsServiceEventExtension());
      }
      if (hasSoundSearchServiceEventExtension()) {
        paramCodedOutputStreamMicro.writeMessage(35351945, getSoundSearchServiceEventExtension());
      }
      if (hasGogglesStreamResponseExtension()) {
        paramCodedOutputStreamMicro.writeMessage(35419983, getGogglesStreamResponseExtension());
      }
      if (hasPinholeOutputExtension()) {
        paramCodedOutputStreamMicro.writeMessage(39442181, getPinholeOutputExtension());
      }
      if (hasTtsCapabilitiesResponseExtension()) {
        paramCodedOutputStreamMicro.writeMessage(45982169, getTtsCapabilitiesResponseExtension());
      }
    }
  }
  
  public static final class S3SessionInfo
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasSessionId;
    private String sessionId_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSessionId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSessionId());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSessionId()
    {
      return this.sessionId_;
    }
    
    public boolean hasSessionId()
    {
      return this.hasSessionId;
    }
    
    public S3SessionInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setSessionId(paramCodedInputStreamMicro.readString());
      }
    }
    
    public S3SessionInfo setSessionId(String paramString)
    {
      this.hasSessionId = true;
      this.sessionId_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSessionId()) {
        paramCodedOutputStreamMicro.writeString(1, getSessionId());
      }
    }
  }
  
  public static final class S3UserInfo
    extends MessageMicro
  {
    private String adaptationId_ = "";
    private List<S3.Locale> additionalLanguages_ = Collections.emptyList();
    private List<S3.AuthToken> authToken_ = Collections.emptyList();
    private int cachedSize = -1;
    private boolean hasAdaptationId;
    private boolean hasInstallId;
    private boolean hasLatLong;
    private boolean hasSpokenLanguage;
    private boolean hasUsePreciseGeolocation;
    private boolean hasUserLocale;
    private boolean hasXGeoLocation;
    private String installId_ = "";
    private String latLong_ = "";
    private S3.Locale spokenLanguage_ = null;
    private boolean usePreciseGeolocation_ = true;
    private S3.Locale userLocale_ = null;
    private String xGeoLocation_ = "";
    
    public S3UserInfo addAdditionalLanguages(S3.Locale paramLocale)
    {
      if (paramLocale == null) {
        throw new NullPointerException();
      }
      if (this.additionalLanguages_.isEmpty()) {
        this.additionalLanguages_ = new ArrayList();
      }
      this.additionalLanguages_.add(paramLocale);
      return this;
    }
    
    public S3UserInfo addAuthToken(S3.AuthToken paramAuthToken)
    {
      if (paramAuthToken == null) {
        throw new NullPointerException();
      }
      if (this.authToken_.isEmpty()) {
        this.authToken_ = new ArrayList();
      }
      this.authToken_.add(paramAuthToken);
      return this;
    }
    
    public S3UserInfo clearAuthToken()
    {
      this.authToken_ = Collections.emptyList();
      return this;
    }
    
    public String getAdaptationId()
    {
      return this.adaptationId_;
    }
    
    public List<S3.Locale> getAdditionalLanguagesList()
    {
      return this.additionalLanguages_;
    }
    
    public List<S3.AuthToken> getAuthTokenList()
    {
      return this.authToken_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getInstallId()
    {
      return this.installId_;
    }
    
    public String getLatLong()
    {
      return this.latLong_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSpokenLanguage();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(2, getSpokenLanguage());
      }
      if (hasUserLocale()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getUserLocale());
      }
      Iterator localIterator1 = getAdditionalLanguagesList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (S3.Locale)localIterator1.next());
      }
      if (hasInstallId()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getInstallId());
      }
      if (hasLatLong()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getLatLong());
      }
      if (hasXGeoLocation()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getXGeoLocation());
      }
      Iterator localIterator2 = getAuthTokenList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(9, (S3.AuthToken)localIterator2.next());
      }
      if (hasAdaptationId()) {
        i += CodedOutputStreamMicro.computeStringSize(12, getAdaptationId());
      }
      if (hasUsePreciseGeolocation()) {
        i += CodedOutputStreamMicro.computeBoolSize(13, getUsePreciseGeolocation());
      }
      this.cachedSize = i;
      return i;
    }
    
    public S3.Locale getSpokenLanguage()
    {
      return this.spokenLanguage_;
    }
    
    public boolean getUsePreciseGeolocation()
    {
      return this.usePreciseGeolocation_;
    }
    
    public S3.Locale getUserLocale()
    {
      return this.userLocale_;
    }
    
    public String getXGeoLocation()
    {
      return this.xGeoLocation_;
    }
    
    public boolean hasAdaptationId()
    {
      return this.hasAdaptationId;
    }
    
    public boolean hasInstallId()
    {
      return this.hasInstallId;
    }
    
    public boolean hasLatLong()
    {
      return this.hasLatLong;
    }
    
    public boolean hasSpokenLanguage()
    {
      return this.hasSpokenLanguage;
    }
    
    public boolean hasUsePreciseGeolocation()
    {
      return this.hasUsePreciseGeolocation;
    }
    
    public boolean hasUserLocale()
    {
      return this.hasUserLocale;
    }
    
    public boolean hasXGeoLocation()
    {
      return this.hasXGeoLocation;
    }
    
    public S3UserInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 18: 
          S3.Locale localLocale3 = new S3.Locale();
          paramCodedInputStreamMicro.readMessage(localLocale3);
          setSpokenLanguage(localLocale3);
          break;
        case 26: 
          S3.Locale localLocale2 = new S3.Locale();
          paramCodedInputStreamMicro.readMessage(localLocale2);
          setUserLocale(localLocale2);
          break;
        case 34: 
          S3.Locale localLocale1 = new S3.Locale();
          paramCodedInputStreamMicro.readMessage(localLocale1);
          addAdditionalLanguages(localLocale1);
          break;
        case 42: 
          setInstallId(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          setLatLong(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setXGeoLocation(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          S3.AuthToken localAuthToken = new S3.AuthToken();
          paramCodedInputStreamMicro.readMessage(localAuthToken);
          addAuthToken(localAuthToken);
          break;
        case 98: 
          setAdaptationId(paramCodedInputStreamMicro.readString());
          break;
        }
        setUsePreciseGeolocation(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public S3UserInfo setAdaptationId(String paramString)
    {
      this.hasAdaptationId = true;
      this.adaptationId_ = paramString;
      return this;
    }
    
    public S3UserInfo setInstallId(String paramString)
    {
      this.hasInstallId = true;
      this.installId_ = paramString;
      return this;
    }
    
    public S3UserInfo setLatLong(String paramString)
    {
      this.hasLatLong = true;
      this.latLong_ = paramString;
      return this;
    }
    
    public S3UserInfo setSpokenLanguage(S3.Locale paramLocale)
    {
      if (paramLocale == null) {
        throw new NullPointerException();
      }
      this.hasSpokenLanguage = true;
      this.spokenLanguage_ = paramLocale;
      return this;
    }
    
    public S3UserInfo setUsePreciseGeolocation(boolean paramBoolean)
    {
      this.hasUsePreciseGeolocation = true;
      this.usePreciseGeolocation_ = paramBoolean;
      return this;
    }
    
    public S3UserInfo setUserLocale(S3.Locale paramLocale)
    {
      if (paramLocale == null) {
        throw new NullPointerException();
      }
      this.hasUserLocale = true;
      this.userLocale_ = paramLocale;
      return this;
    }
    
    public S3UserInfo setXGeoLocation(String paramString)
    {
      this.hasXGeoLocation = true;
      this.xGeoLocation_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSpokenLanguage()) {
        paramCodedOutputStreamMicro.writeMessage(2, getSpokenLanguage());
      }
      if (hasUserLocale()) {
        paramCodedOutputStreamMicro.writeMessage(3, getUserLocale());
      }
      Iterator localIterator1 = getAdditionalLanguagesList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (S3.Locale)localIterator1.next());
      }
      if (hasInstallId()) {
        paramCodedOutputStreamMicro.writeString(5, getInstallId());
      }
      if (hasLatLong()) {
        paramCodedOutputStreamMicro.writeString(6, getLatLong());
      }
      if (hasXGeoLocation()) {
        paramCodedOutputStreamMicro.writeString(8, getXGeoLocation());
      }
      Iterator localIterator2 = getAuthTokenList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(9, (S3.AuthToken)localIterator2.next());
      }
      if (hasAdaptationId()) {
        paramCodedOutputStreamMicro.writeString(12, getAdaptationId());
      }
      if (hasUsePreciseGeolocation()) {
        paramCodedOutputStreamMicro.writeBool(13, getUsePreciseGeolocation());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.s3.S3
 * JD-Core Version:    0.7.0.1
 */