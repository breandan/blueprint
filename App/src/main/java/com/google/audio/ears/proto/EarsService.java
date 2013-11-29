package com.google.audio.ears.proto;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class EarsService
{
  public static final class EarsLookupRequest
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String clientCountryCode_ = "";
    private List<String> clientFlags_ = Collections.emptyList();
    private String clientId_ = "";
    private String clientLocale_ = "";
    private String clientName_ = "";
    private String clientVersion_ = "";
    private boolean debug_ = false;
    private List<Integer> desiredResultType_ = Collections.emptyList();
    private boolean hasClientCountryCode;
    private boolean hasClientId;
    private boolean hasClientLocale;
    private boolean hasClientName;
    private boolean hasClientVersion;
    private boolean hasDebug;
    private boolean hasMaxResults;
    private boolean hasSessionId;
    private int maxResults_ = 0;
    private String sessionId_ = "";
    
    public EarsLookupRequest addClientFlags(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.clientFlags_.isEmpty()) {
        this.clientFlags_ = new ArrayList();
      }
      this.clientFlags_.add(paramString);
      return this;
    }
    
    public EarsLookupRequest addDesiredResultType(int paramInt)
    {
      if (this.desiredResultType_.isEmpty()) {
        this.desiredResultType_ = new ArrayList();
      }
      this.desiredResultType_.add(Integer.valueOf(paramInt));
      return this;
    }
    
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
    
    public List<String> getClientFlagsList()
    {
      return this.clientFlags_;
    }
    
    public String getClientId()
    {
      return this.clientId_;
    }
    
    public String getClientLocale()
    {
      return this.clientLocale_;
    }
    
    public String getClientName()
    {
      return this.clientName_;
    }
    
    public String getClientVersion()
    {
      return this.clientVersion_;
    }
    
    public boolean getDebug()
    {
      return this.debug_;
    }
    
    public List<Integer> getDesiredResultTypeList()
    {
      return this.desiredResultType_;
    }
    
    public int getMaxResults()
    {
      return this.maxResults_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSessionId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSessionId());
      }
      int j = 0;
      Iterator localIterator1 = getDesiredResultTypeList().iterator();
      while (localIterator1.hasNext()) {
        j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator1.next()).intValue());
      }
      int k = i + j + 1 * getDesiredResultTypeList().size();
      if (hasDebug()) {
        k += CodedOutputStreamMicro.computeBoolSize(3, getDebug());
      }
      if (hasClientLocale()) {
        k += CodedOutputStreamMicro.computeStringSize(4, getClientLocale());
      }
      if (hasClientName()) {
        k += CodedOutputStreamMicro.computeStringSize(5, getClientName());
      }
      if (hasClientVersion()) {
        k += CodedOutputStreamMicro.computeStringSize(6, getClientVersion());
      }
      if (hasClientCountryCode()) {
        k += CodedOutputStreamMicro.computeStringSize(7, getClientCountryCode());
      }
      if (hasMaxResults()) {
        k += CodedOutputStreamMicro.computeInt32Size(8, getMaxResults());
      }
      if (hasClientId()) {
        k += CodedOutputStreamMicro.computeStringSize(9, getClientId());
      }
      int m = 0;
      Iterator localIterator2 = getClientFlagsList().iterator();
      while (localIterator2.hasNext()) {
        m += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator2.next());
      }
      int n = k + m + 1 * getClientFlagsList().size();
      this.cachedSize = n;
      return n;
    }
    
    public String getSessionId()
    {
      return this.sessionId_;
    }
    
    public boolean hasClientCountryCode()
    {
      return this.hasClientCountryCode;
    }
    
    public boolean hasClientId()
    {
      return this.hasClientId;
    }
    
    public boolean hasClientLocale()
    {
      return this.hasClientLocale;
    }
    
    public boolean hasClientName()
    {
      return this.hasClientName;
    }
    
    public boolean hasClientVersion()
    {
      return this.hasClientVersion;
    }
    
    public boolean hasDebug()
    {
      return this.hasDebug;
    }
    
    public boolean hasMaxResults()
    {
      return this.hasMaxResults;
    }
    
    public boolean hasSessionId()
    {
      return this.hasSessionId;
    }
    
    public EarsLookupRequest mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setSessionId(paramCodedInputStreamMicro.readString());
          break;
        case 16: 
          addDesiredResultType(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setDebug(paramCodedInputStreamMicro.readBool());
          break;
        case 34: 
          setClientLocale(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setClientName(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          setClientVersion(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          setClientCountryCode(paramCodedInputStreamMicro.readString());
          break;
        case 64: 
          setMaxResults(paramCodedInputStreamMicro.readInt32());
          break;
        case 74: 
          setClientId(paramCodedInputStreamMicro.readString());
          break;
        }
        addClientFlags(paramCodedInputStreamMicro.readString());
      }
    }
    
    public EarsLookupRequest setClientCountryCode(String paramString)
    {
      this.hasClientCountryCode = true;
      this.clientCountryCode_ = paramString;
      return this;
    }
    
    public EarsLookupRequest setClientId(String paramString)
    {
      this.hasClientId = true;
      this.clientId_ = paramString;
      return this;
    }
    
    public EarsLookupRequest setClientLocale(String paramString)
    {
      this.hasClientLocale = true;
      this.clientLocale_ = paramString;
      return this;
    }
    
    public EarsLookupRequest setClientName(String paramString)
    {
      this.hasClientName = true;
      this.clientName_ = paramString;
      return this;
    }
    
    public EarsLookupRequest setClientVersion(String paramString)
    {
      this.hasClientVersion = true;
      this.clientVersion_ = paramString;
      return this;
    }
    
    public EarsLookupRequest setDebug(boolean paramBoolean)
    {
      this.hasDebug = true;
      this.debug_ = paramBoolean;
      return this;
    }
    
    public EarsLookupRequest setMaxResults(int paramInt)
    {
      this.hasMaxResults = true;
      this.maxResults_ = paramInt;
      return this;
    }
    
    public EarsLookupRequest setSessionId(String paramString)
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
      Iterator localIterator1 = getDesiredResultTypeList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(2, ((Integer)localIterator1.next()).intValue());
      }
      if (hasDebug()) {
        paramCodedOutputStreamMicro.writeBool(3, getDebug());
      }
      if (hasClientLocale()) {
        paramCodedOutputStreamMicro.writeString(4, getClientLocale());
      }
      if (hasClientName()) {
        paramCodedOutputStreamMicro.writeString(5, getClientName());
      }
      if (hasClientVersion()) {
        paramCodedOutputStreamMicro.writeString(6, getClientVersion());
      }
      if (hasClientCountryCode()) {
        paramCodedOutputStreamMicro.writeString(7, getClientCountryCode());
      }
      if (hasMaxResults()) {
        paramCodedOutputStreamMicro.writeInt32(8, getMaxResults());
      }
      if (hasClientId()) {
        paramCodedOutputStreamMicro.writeString(9, getClientId());
      }
      Iterator localIterator2 = getClientFlagsList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeString(10, (String)localIterator2.next());
      }
    }
  }
  
  public static final class EarsResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private float confidence_ = 0.0F;
    private List<String> country_ = Collections.emptyList();
    private String debug_ = "";
    private long elapsedMs_ = 0L;
    private EarsService.FamousSpeechResult famousSpeechResult_ = null;
    private boolean hasConfidence;
    private boolean hasDebug;
    private boolean hasElapsedMs;
    private boolean hasFamousSpeechResult;
    private boolean hasLookupDelayMs;
    private boolean hasMusicResult;
    private boolean hasOriginalReferenceId;
    private boolean hasProbeRange;
    private boolean hasRefRange;
    private boolean hasReferenceId;
    private boolean hasTtsResponse;
    private boolean hasTvResult;
    private long lookupDelayMs_ = 0L;
    private EarsService.MusicResult musicResult_ = null;
    private long originalReferenceId_ = 0L;
    private MatchRange probeRange_ = null;
    private MatchRange refRange_ = null;
    private long referenceId_ = 0L;
    private String ttsResponse_ = "";
    private EarsService.TvResult tvResult_ = null;
    
    public EarsResult addCountry(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.country_.isEmpty()) {
        this.country_ = new ArrayList();
      }
      this.country_.add(paramString);
      return this;
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
    
    public List<String> getCountryList()
    {
      return this.country_;
    }
    
    public String getDebug()
    {
      return this.debug_;
    }
    
    public long getElapsedMs()
    {
      return this.elapsedMs_;
    }
    
    public EarsService.FamousSpeechResult getFamousSpeechResult()
    {
      return this.famousSpeechResult_;
    }
    
    public long getLookupDelayMs()
    {
      return this.lookupDelayMs_;
    }
    
    public EarsService.MusicResult getMusicResult()
    {
      return this.musicResult_;
    }
    
    public long getOriginalReferenceId()
    {
      return this.originalReferenceId_;
    }
    
    public MatchRange getProbeRange()
    {
      return this.probeRange_;
    }
    
    public MatchRange getRefRange()
    {
      return this.refRange_;
    }
    
    public long getReferenceId()
    {
      return this.referenceId_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasConfidence();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeFloatSize(1, getConfidence());
      }
      if (hasDebug()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getDebug());
      }
      if (hasMusicResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getMusicResult());
      }
      if (hasTvResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getTvResult());
      }
      if (hasElapsedMs()) {
        i += CodedOutputStreamMicro.computeInt64Size(5, getElapsedMs());
      }
      if (hasReferenceId()) {
        i += CodedOutputStreamMicro.computeUInt64Size(6, getReferenceId());
      }
      if (hasProbeRange()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, getProbeRange());
      }
      if (hasRefRange()) {
        i += CodedOutputStreamMicro.computeMessageSize(8, getRefRange());
      }
      if (hasFamousSpeechResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(9, getFamousSpeechResult());
      }
      int j = 0;
      Iterator localIterator = getCountryList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getCountryList().size();
      if (hasTtsResponse()) {
        k += CodedOutputStreamMicro.computeStringSize(11, getTtsResponse());
      }
      if (hasOriginalReferenceId()) {
        k += CodedOutputStreamMicro.computeUInt64Size(12, getOriginalReferenceId());
      }
      if (hasLookupDelayMs()) {
        k += CodedOutputStreamMicro.computeInt64Size(13, getLookupDelayMs());
      }
      this.cachedSize = k;
      return k;
    }
    
    public String getTtsResponse()
    {
      return this.ttsResponse_;
    }
    
    public EarsService.TvResult getTvResult()
    {
      return this.tvResult_;
    }
    
    public boolean hasConfidence()
    {
      return this.hasConfidence;
    }
    
    public boolean hasDebug()
    {
      return this.hasDebug;
    }
    
    public boolean hasElapsedMs()
    {
      return this.hasElapsedMs;
    }
    
    public boolean hasFamousSpeechResult()
    {
      return this.hasFamousSpeechResult;
    }
    
    public boolean hasLookupDelayMs()
    {
      return this.hasLookupDelayMs;
    }
    
    public boolean hasMusicResult()
    {
      return this.hasMusicResult;
    }
    
    public boolean hasOriginalReferenceId()
    {
      return this.hasOriginalReferenceId;
    }
    
    public boolean hasProbeRange()
    {
      return this.hasProbeRange;
    }
    
    public boolean hasRefRange()
    {
      return this.hasRefRange;
    }
    
    public boolean hasReferenceId()
    {
      return this.hasReferenceId;
    }
    
    public boolean hasTtsResponse()
    {
      return this.hasTtsResponse;
    }
    
    public boolean hasTvResult()
    {
      return this.hasTvResult;
    }
    
    public EarsResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 13: 
          setConfidence(paramCodedInputStreamMicro.readFloat());
          break;
        case 18: 
          setDebug(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          EarsService.MusicResult localMusicResult = new EarsService.MusicResult();
          paramCodedInputStreamMicro.readMessage(localMusicResult);
          setMusicResult(localMusicResult);
          break;
        case 34: 
          EarsService.TvResult localTvResult = new EarsService.TvResult();
          paramCodedInputStreamMicro.readMessage(localTvResult);
          setTvResult(localTvResult);
          break;
        case 40: 
          setElapsedMs(paramCodedInputStreamMicro.readInt64());
          break;
        case 48: 
          setReferenceId(paramCodedInputStreamMicro.readUInt64());
          break;
        case 58: 
          MatchRange localMatchRange2 = new MatchRange();
          paramCodedInputStreamMicro.readMessage(localMatchRange2);
          setProbeRange(localMatchRange2);
          break;
        case 66: 
          MatchRange localMatchRange1 = new MatchRange();
          paramCodedInputStreamMicro.readMessage(localMatchRange1);
          setRefRange(localMatchRange1);
          break;
        case 74: 
          EarsService.FamousSpeechResult localFamousSpeechResult = new EarsService.FamousSpeechResult();
          paramCodedInputStreamMicro.readMessage(localFamousSpeechResult);
          setFamousSpeechResult(localFamousSpeechResult);
          break;
        case 82: 
          addCountry(paramCodedInputStreamMicro.readString());
          break;
        case 90: 
          setTtsResponse(paramCodedInputStreamMicro.readString());
          break;
        case 96: 
          setOriginalReferenceId(paramCodedInputStreamMicro.readUInt64());
          break;
        }
        setLookupDelayMs(paramCodedInputStreamMicro.readInt64());
      }
    }
    
    public EarsResult setConfidence(float paramFloat)
    {
      this.hasConfidence = true;
      this.confidence_ = paramFloat;
      return this;
    }
    
    public EarsResult setDebug(String paramString)
    {
      this.hasDebug = true;
      this.debug_ = paramString;
      return this;
    }
    
    public EarsResult setElapsedMs(long paramLong)
    {
      this.hasElapsedMs = true;
      this.elapsedMs_ = paramLong;
      return this;
    }
    
    public EarsResult setFamousSpeechResult(EarsService.FamousSpeechResult paramFamousSpeechResult)
    {
      if (paramFamousSpeechResult == null) {
        throw new NullPointerException();
      }
      this.hasFamousSpeechResult = true;
      this.famousSpeechResult_ = paramFamousSpeechResult;
      return this;
    }
    
    public EarsResult setLookupDelayMs(long paramLong)
    {
      this.hasLookupDelayMs = true;
      this.lookupDelayMs_ = paramLong;
      return this;
    }
    
    public EarsResult setMusicResult(EarsService.MusicResult paramMusicResult)
    {
      if (paramMusicResult == null) {
        throw new NullPointerException();
      }
      this.hasMusicResult = true;
      this.musicResult_ = paramMusicResult;
      return this;
    }
    
    public EarsResult setOriginalReferenceId(long paramLong)
    {
      this.hasOriginalReferenceId = true;
      this.originalReferenceId_ = paramLong;
      return this;
    }
    
    public EarsResult setProbeRange(MatchRange paramMatchRange)
    {
      if (paramMatchRange == null) {
        throw new NullPointerException();
      }
      this.hasProbeRange = true;
      this.probeRange_ = paramMatchRange;
      return this;
    }
    
    public EarsResult setRefRange(MatchRange paramMatchRange)
    {
      if (paramMatchRange == null) {
        throw new NullPointerException();
      }
      this.hasRefRange = true;
      this.refRange_ = paramMatchRange;
      return this;
    }
    
    public EarsResult setReferenceId(long paramLong)
    {
      this.hasReferenceId = true;
      this.referenceId_ = paramLong;
      return this;
    }
    
    public EarsResult setTtsResponse(String paramString)
    {
      this.hasTtsResponse = true;
      this.ttsResponse_ = paramString;
      return this;
    }
    
    public EarsResult setTvResult(EarsService.TvResult paramTvResult)
    {
      if (paramTvResult == null) {
        throw new NullPointerException();
      }
      this.hasTvResult = true;
      this.tvResult_ = paramTvResult;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(1, getConfidence());
      }
      if (hasDebug()) {
        paramCodedOutputStreamMicro.writeString(2, getDebug());
      }
      if (hasMusicResult()) {
        paramCodedOutputStreamMicro.writeMessage(3, getMusicResult());
      }
      if (hasTvResult()) {
        paramCodedOutputStreamMicro.writeMessage(4, getTvResult());
      }
      if (hasElapsedMs()) {
        paramCodedOutputStreamMicro.writeInt64(5, getElapsedMs());
      }
      if (hasReferenceId()) {
        paramCodedOutputStreamMicro.writeUInt64(6, getReferenceId());
      }
      if (hasProbeRange()) {
        paramCodedOutputStreamMicro.writeMessage(7, getProbeRange());
      }
      if (hasRefRange()) {
        paramCodedOutputStreamMicro.writeMessage(8, getRefRange());
      }
      if (hasFamousSpeechResult()) {
        paramCodedOutputStreamMicro.writeMessage(9, getFamousSpeechResult());
      }
      Iterator localIterator = getCountryList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(10, (String)localIterator.next());
      }
      if (hasTtsResponse()) {
        paramCodedOutputStreamMicro.writeString(11, getTtsResponse());
      }
      if (hasOriginalReferenceId()) {
        paramCodedOutputStreamMicro.writeUInt64(12, getOriginalReferenceId());
      }
      if (hasLookupDelayMs()) {
        paramCodedOutputStreamMicro.writeInt64(13, getLookupDelayMs());
      }
    }
    
    public static final class MatchRange
      extends MessageMicro
    {
      private int cachedSize = -1;
      private long endMs_ = 0L;
      private boolean hasEndMs;
      private boolean hasStartMs;
      private long startMs_ = 0L;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public long getEndMs()
      {
        return this.endMs_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasStartMs();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getStartMs());
        }
        if (hasEndMs()) {
          i += CodedOutputStreamMicro.computeInt64Size(2, getEndMs());
        }
        this.cachedSize = i;
        return i;
      }
      
      public long getStartMs()
      {
        return this.startMs_;
      }
      
      public boolean hasEndMs()
      {
        return this.hasEndMs;
      }
      
      public boolean hasStartMs()
      {
        return this.hasStartMs;
      }
      
      public MatchRange mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setStartMs(paramCodedInputStreamMicro.readInt64());
            break;
          }
          setEndMs(paramCodedInputStreamMicro.readInt64());
        }
      }
      
      public MatchRange setEndMs(long paramLong)
      {
        this.hasEndMs = true;
        this.endMs_ = paramLong;
        return this;
      }
      
      public MatchRange setStartMs(long paramLong)
      {
        this.hasStartMs = true;
        this.startMs_ = paramLong;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasStartMs()) {
          paramCodedOutputStreamMicro.writeInt64(1, getStartMs());
        }
        if (hasEndMs()) {
          paramCodedOutputStreamMicro.writeInt64(2, getEndMs());
        }
      }
    }
  }
  
  public static final class EarsResultsResponse
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String detectedCountryCode_ = "";
    private boolean hasDetectedCountryCode;
    private boolean hasStatusCode;
    private List<EarsService.EarsResult> result_ = Collections.emptyList();
    private int statusCode_ = 0;
    
    public EarsResultsResponse addResult(EarsService.EarsResult paramEarsResult)
    {
      if (paramEarsResult == null) {
        throw new NullPointerException();
      }
      if (this.result_.isEmpty()) {
        this.result_ = new ArrayList();
      }
      this.result_.add(paramEarsResult);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDetectedCountryCode()
    {
      return this.detectedCountryCode_;
    }
    
    public List<EarsService.EarsResult> getResultList()
    {
      return this.result_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getResultList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (EarsService.EarsResult)localIterator.next());
      }
      if (hasStatusCode()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getStatusCode());
      }
      if (hasDetectedCountryCode()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getDetectedCountryCode());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getStatusCode()
    {
      return this.statusCode_;
    }
    
    public boolean hasDetectedCountryCode()
    {
      return this.hasDetectedCountryCode;
    }
    
    public boolean hasStatusCode()
    {
      return this.hasStatusCode;
    }
    
    public EarsResultsResponse mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EarsService.EarsResult localEarsResult = new EarsService.EarsResult();
          paramCodedInputStreamMicro.readMessage(localEarsResult);
          addResult(localEarsResult);
          break;
        case 16: 
          setStatusCode(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setDetectedCountryCode(paramCodedInputStreamMicro.readString());
      }
    }
    
    public EarsResultsResponse setDetectedCountryCode(String paramString)
    {
      this.hasDetectedCountryCode = true;
      this.detectedCountryCode_ = paramString;
      return this;
    }
    
    public EarsResultsResponse setStatusCode(int paramInt)
    {
      this.hasStatusCode = true;
      this.statusCode_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getResultList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (EarsService.EarsResult)localIterator.next());
      }
      if (hasStatusCode()) {
        paramCodedOutputStreamMicro.writeInt32(2, getStatusCode());
      }
      if (hasDetectedCountryCode()) {
        paramCodedOutputStreamMicro.writeString(3, getDetectedCountryCode());
      }
    }
  }
  
  public static final class EarsStreamRequest
    extends MessageMicro
  {
    private int audioContainer_ = 0;
    private int audioEncoding_ = 0;
    private int cachedSize = -1;
    private boolean hasAudioContainer;
    private boolean hasAudioEncoding;
    
    public int getAudioContainer()
    {
      return this.audioContainer_;
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
    
    public int getSerializedSize()
    {
      boolean bool = hasAudioContainer();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getAudioContainer());
      }
      if (hasAudioEncoding()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getAudioEncoding());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAudioContainer()
    {
      return this.hasAudioContainer;
    }
    
    public boolean hasAudioEncoding()
    {
      return this.hasAudioEncoding;
    }
    
    public EarsStreamRequest mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setAudioContainer(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setAudioEncoding(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public EarsStreamRequest setAudioContainer(int paramInt)
    {
      this.hasAudioContainer = true;
      this.audioContainer_ = paramInt;
      return this;
    }
    
    public EarsStreamRequest setAudioEncoding(int paramInt)
    {
      this.hasAudioEncoding = true;
      this.audioEncoding_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAudioContainer()) {
        paramCodedOutputStreamMicro.writeInt32(1, getAudioContainer());
      }
      if (hasAudioEncoding()) {
        paramCodedOutputStreamMicro.writeInt32(2, getAudioEncoding());
      }
    }
  }
  
  public static final class FamousSpeechResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasSpeakerName;
    private boolean hasSpeechTitle;
    private String speakerName_ = "";
    private String speechTitle_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSpeakerName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSpeakerName());
      }
      if (hasSpeechTitle()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getSpeechTitle());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSpeakerName()
    {
      return this.speakerName_;
    }
    
    public String getSpeechTitle()
    {
      return this.speechTitle_;
    }
    
    public boolean hasSpeakerName()
    {
      return this.hasSpeakerName;
    }
    
    public boolean hasSpeechTitle()
    {
      return this.hasSpeechTitle;
    }
    
    public FamousSpeechResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setSpeakerName(paramCodedInputStreamMicro.readString());
          break;
        }
        setSpeechTitle(paramCodedInputStreamMicro.readString());
      }
    }
    
    public FamousSpeechResult setSpeakerName(String paramString)
    {
      this.hasSpeakerName = true;
      this.speakerName_ = paramString;
      return this;
    }
    
    public FamousSpeechResult setSpeechTitle(String paramString)
    {
      this.hasSpeechTitle = true;
      this.speechTitle_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSpeakerName()) {
        paramCodedOutputStreamMicro.writeString(1, getSpeakerName());
      }
      if (hasSpeechTitle()) {
        paramCodedOutputStreamMicro.writeString(2, getSpeechTitle());
      }
    }
  }
  
  public static final class MusicResult
    extends MessageMicro
  {
    private String albumArtUrl_ = "";
    private ByteStringMicro albumArt_ = ByteStringMicro.EMPTY;
    private String album_ = "";
    private String artistId_ = "";
    private String artist_ = "";
    private int cachedSize = -1;
    private boolean hasAlbum;
    private boolean hasAlbumArt;
    private boolean hasAlbumArtUrl;
    private boolean hasArtist;
    private boolean hasArtistId;
    private boolean hasIsExplicit;
    private boolean hasIsrc;
    private boolean hasLabelCode;
    private boolean hasPopularityScore;
    private boolean hasPrerelease;
    private boolean hasSignedInAlbumArtUrl;
    private boolean hasTrack;
    private boolean isExplicit_ = false;
    private String isrc_ = "";
    private String labelCode_ = "";
    private List<EarsService.ProductOffer> offer_ = Collections.emptyList();
    private double popularityScore_ = 0.0D;
    private boolean prerelease_ = false;
    private String signedInAlbumArtUrl_ = "";
    private String track_ = "";
    private List<EarsService.YouTubeVideo> video_ = Collections.emptyList();
    
    public MusicResult addOffer(EarsService.ProductOffer paramProductOffer)
    {
      if (paramProductOffer == null) {
        throw new NullPointerException();
      }
      if (this.offer_.isEmpty()) {
        this.offer_ = new ArrayList();
      }
      this.offer_.add(paramProductOffer);
      return this;
    }
    
    public MusicResult addVideo(EarsService.YouTubeVideo paramYouTubeVideo)
    {
      if (paramYouTubeVideo == null) {
        throw new NullPointerException();
      }
      if (this.video_.isEmpty()) {
        this.video_ = new ArrayList();
      }
      this.video_.add(paramYouTubeVideo);
      return this;
    }
    
    public String getAlbum()
    {
      return this.album_;
    }
    
    public ByteStringMicro getAlbumArt()
    {
      return this.albumArt_;
    }
    
    public String getAlbumArtUrl()
    {
      return this.albumArtUrl_;
    }
    
    public String getArtist()
    {
      return this.artist_;
    }
    
    public String getArtistId()
    {
      return this.artistId_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public boolean getIsExplicit()
    {
      return this.isExplicit_;
    }
    
    public String getIsrc()
    {
      return this.isrc_;
    }
    
    public String getLabelCode()
    {
      return this.labelCode_;
    }
    
    public List<EarsService.ProductOffer> getOfferList()
    {
      return this.offer_;
    }
    
    public double getPopularityScore()
    {
      return this.popularityScore_;
    }
    
    public boolean getPrerelease()
    {
      return this.prerelease_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasArtist();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getArtist());
      }
      if (hasTrack()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getTrack());
      }
      if (hasAlbum()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getAlbum());
      }
      if (hasIsrc()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getIsrc());
      }
      Iterator localIterator1 = getVideoList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, (EarsService.YouTubeVideo)localIterator1.next());
      }
      Iterator localIterator2 = getOfferList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, (EarsService.ProductOffer)localIterator2.next());
      }
      if (hasAlbumArt()) {
        i += CodedOutputStreamMicro.computeBytesSize(7, getAlbumArt());
      }
      if (hasLabelCode()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getLabelCode());
      }
      if (hasAlbumArtUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getAlbumArtUrl());
      }
      if (hasPopularityScore()) {
        i += CodedOutputStreamMicro.computeDoubleSize(10, getPopularityScore());
      }
      if (hasSignedInAlbumArtUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(11, getSignedInAlbumArtUrl());
      }
      if (hasArtistId()) {
        i += CodedOutputStreamMicro.computeStringSize(12, getArtistId());
      }
      if (hasPrerelease()) {
        i += CodedOutputStreamMicro.computeBoolSize(13, getPrerelease());
      }
      if (hasIsExplicit()) {
        i += CodedOutputStreamMicro.computeBoolSize(14, getIsExplicit());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSignedInAlbumArtUrl()
    {
      return this.signedInAlbumArtUrl_;
    }
    
    public String getTrack()
    {
      return this.track_;
    }
    
    public List<EarsService.YouTubeVideo> getVideoList()
    {
      return this.video_;
    }
    
    public boolean hasAlbum()
    {
      return this.hasAlbum;
    }
    
    public boolean hasAlbumArt()
    {
      return this.hasAlbumArt;
    }
    
    public boolean hasAlbumArtUrl()
    {
      return this.hasAlbumArtUrl;
    }
    
    public boolean hasArtist()
    {
      return this.hasArtist;
    }
    
    public boolean hasArtistId()
    {
      return this.hasArtistId;
    }
    
    public boolean hasIsExplicit()
    {
      return this.hasIsExplicit;
    }
    
    public boolean hasIsrc()
    {
      return this.hasIsrc;
    }
    
    public boolean hasLabelCode()
    {
      return this.hasLabelCode;
    }
    
    public boolean hasPopularityScore()
    {
      return this.hasPopularityScore;
    }
    
    public boolean hasPrerelease()
    {
      return this.hasPrerelease;
    }
    
    public boolean hasSignedInAlbumArtUrl()
    {
      return this.hasSignedInAlbumArtUrl;
    }
    
    public boolean hasTrack()
    {
      return this.hasTrack;
    }
    
    public MusicResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setArtist(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setTrack(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setAlbum(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setIsrc(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          EarsService.YouTubeVideo localYouTubeVideo = new EarsService.YouTubeVideo();
          paramCodedInputStreamMicro.readMessage(localYouTubeVideo);
          addVideo(localYouTubeVideo);
          break;
        case 50: 
          EarsService.ProductOffer localProductOffer = new EarsService.ProductOffer();
          paramCodedInputStreamMicro.readMessage(localProductOffer);
          addOffer(localProductOffer);
          break;
        case 58: 
          setAlbumArt(paramCodedInputStreamMicro.readBytes());
          break;
        case 66: 
          setLabelCode(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          setAlbumArtUrl(paramCodedInputStreamMicro.readString());
          break;
        case 81: 
          setPopularityScore(paramCodedInputStreamMicro.readDouble());
          break;
        case 90: 
          setSignedInAlbumArtUrl(paramCodedInputStreamMicro.readString());
          break;
        case 98: 
          setArtistId(paramCodedInputStreamMicro.readString());
          break;
        case 104: 
          setPrerelease(paramCodedInputStreamMicro.readBool());
          break;
        }
        setIsExplicit(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public MusicResult setAlbum(String paramString)
    {
      this.hasAlbum = true;
      this.album_ = paramString;
      return this;
    }
    
    public MusicResult setAlbumArt(ByteStringMicro paramByteStringMicro)
    {
      this.hasAlbumArt = true;
      this.albumArt_ = paramByteStringMicro;
      return this;
    }
    
    public MusicResult setAlbumArtUrl(String paramString)
    {
      this.hasAlbumArtUrl = true;
      this.albumArtUrl_ = paramString;
      return this;
    }
    
    public MusicResult setArtist(String paramString)
    {
      this.hasArtist = true;
      this.artist_ = paramString;
      return this;
    }
    
    public MusicResult setArtistId(String paramString)
    {
      this.hasArtistId = true;
      this.artistId_ = paramString;
      return this;
    }
    
    public MusicResult setIsExplicit(boolean paramBoolean)
    {
      this.hasIsExplicit = true;
      this.isExplicit_ = paramBoolean;
      return this;
    }
    
    public MusicResult setIsrc(String paramString)
    {
      this.hasIsrc = true;
      this.isrc_ = paramString;
      return this;
    }
    
    public MusicResult setLabelCode(String paramString)
    {
      this.hasLabelCode = true;
      this.labelCode_ = paramString;
      return this;
    }
    
    public MusicResult setPopularityScore(double paramDouble)
    {
      this.hasPopularityScore = true;
      this.popularityScore_ = paramDouble;
      return this;
    }
    
    public MusicResult setPrerelease(boolean paramBoolean)
    {
      this.hasPrerelease = true;
      this.prerelease_ = paramBoolean;
      return this;
    }
    
    public MusicResult setSignedInAlbumArtUrl(String paramString)
    {
      this.hasSignedInAlbumArtUrl = true;
      this.signedInAlbumArtUrl_ = paramString;
      return this;
    }
    
    public MusicResult setTrack(String paramString)
    {
      this.hasTrack = true;
      this.track_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasArtist()) {
        paramCodedOutputStreamMicro.writeString(1, getArtist());
      }
      if (hasTrack()) {
        paramCodedOutputStreamMicro.writeString(2, getTrack());
      }
      if (hasAlbum()) {
        paramCodedOutputStreamMicro.writeString(3, getAlbum());
      }
      if (hasIsrc()) {
        paramCodedOutputStreamMicro.writeString(4, getIsrc());
      }
      Iterator localIterator1 = getVideoList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(5, (EarsService.YouTubeVideo)localIterator1.next());
      }
      Iterator localIterator2 = getOfferList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(6, (EarsService.ProductOffer)localIterator2.next());
      }
      if (hasAlbumArt()) {
        paramCodedOutputStreamMicro.writeBytes(7, getAlbumArt());
      }
      if (hasLabelCode()) {
        paramCodedOutputStreamMicro.writeString(8, getLabelCode());
      }
      if (hasAlbumArtUrl()) {
        paramCodedOutputStreamMicro.writeString(9, getAlbumArtUrl());
      }
      if (hasPopularityScore()) {
        paramCodedOutputStreamMicro.writeDouble(10, getPopularityScore());
      }
      if (hasSignedInAlbumArtUrl()) {
        paramCodedOutputStreamMicro.writeString(11, getSignedInAlbumArtUrl());
      }
      if (hasArtistId()) {
        paramCodedOutputStreamMicro.writeString(12, getArtistId());
      }
      if (hasPrerelease()) {
        paramCodedOutputStreamMicro.writeBool(13, getPrerelease());
      }
      if (hasIsExplicit()) {
        paramCodedOutputStreamMicro.writeBool(14, getIsExplicit());
      }
    }
  }
  
  public static final class ProductOffer
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasIdentifier;
    private boolean hasParentIdentifier;
    private boolean hasPreviewUrl;
    private boolean hasUrl;
    private boolean hasVendor;
    private String identifier_ = "";
    private String parentIdentifier_ = "";
    private List<PricingInfo> parentPricingInfo_ = Collections.emptyList();
    private String previewUrl_ = "";
    private List<PricingInfo> pricingInfo_ = Collections.emptyList();
    private String url_ = "";
    private int vendor_ = 0;
    
    public ProductOffer addParentPricingInfo(PricingInfo paramPricingInfo)
    {
      if (paramPricingInfo == null) {
        throw new NullPointerException();
      }
      if (this.parentPricingInfo_.isEmpty()) {
        this.parentPricingInfo_ = new ArrayList();
      }
      this.parentPricingInfo_.add(paramPricingInfo);
      return this;
    }
    
    public ProductOffer addPricingInfo(PricingInfo paramPricingInfo)
    {
      if (paramPricingInfo == null) {
        throw new NullPointerException();
      }
      if (this.pricingInfo_.isEmpty()) {
        this.pricingInfo_ = new ArrayList();
      }
      this.pricingInfo_.add(paramPricingInfo);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getIdentifier()
    {
      return this.identifier_;
    }
    
    public String getParentIdentifier()
    {
      return this.parentIdentifier_;
    }
    
    public List<PricingInfo> getParentPricingInfoList()
    {
      return this.parentPricingInfo_;
    }
    
    public String getPreviewUrl()
    {
      return this.previewUrl_;
    }
    
    public List<PricingInfo> getPricingInfoList()
    {
      return this.pricingInfo_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasVendor();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getVendor());
      }
      if (hasIdentifier()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getIdentifier());
      }
      if (hasParentIdentifier()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getParentIdentifier());
      }
      Iterator localIterator1 = getPricingInfoList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (PricingInfo)localIterator1.next());
      }
      if (hasUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getUrl());
      }
      Iterator localIterator2 = getParentPricingInfoList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, (PricingInfo)localIterator2.next());
      }
      if (hasPreviewUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getPreviewUrl());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public int getVendor()
    {
      return this.vendor_;
    }
    
    public boolean hasIdentifier()
    {
      return this.hasIdentifier;
    }
    
    public boolean hasParentIdentifier()
    {
      return this.hasParentIdentifier;
    }
    
    public boolean hasPreviewUrl()
    {
      return this.hasPreviewUrl;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public boolean hasVendor()
    {
      return this.hasVendor;
    }
    
    public ProductOffer mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setVendor(paramCodedInputStreamMicro.readInt32());
          break;
        case 18: 
          setIdentifier(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setParentIdentifier(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          PricingInfo localPricingInfo2 = new PricingInfo();
          paramCodedInputStreamMicro.readMessage(localPricingInfo2);
          addPricingInfo(localPricingInfo2);
          break;
        case 42: 
          setUrl(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          PricingInfo localPricingInfo1 = new PricingInfo();
          paramCodedInputStreamMicro.readMessage(localPricingInfo1);
          addParentPricingInfo(localPricingInfo1);
          break;
        }
        setPreviewUrl(paramCodedInputStreamMicro.readString());
      }
    }
    
    public ProductOffer setIdentifier(String paramString)
    {
      this.hasIdentifier = true;
      this.identifier_ = paramString;
      return this;
    }
    
    public ProductOffer setParentIdentifier(String paramString)
    {
      this.hasParentIdentifier = true;
      this.parentIdentifier_ = paramString;
      return this;
    }
    
    public ProductOffer setPreviewUrl(String paramString)
    {
      this.hasPreviewUrl = true;
      this.previewUrl_ = paramString;
      return this;
    }
    
    public ProductOffer setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public ProductOffer setVendor(int paramInt)
    {
      this.hasVendor = true;
      this.vendor_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasVendor()) {
        paramCodedOutputStreamMicro.writeInt32(1, getVendor());
      }
      if (hasIdentifier()) {
        paramCodedOutputStreamMicro.writeString(2, getIdentifier());
      }
      if (hasParentIdentifier()) {
        paramCodedOutputStreamMicro.writeString(3, getParentIdentifier());
      }
      Iterator localIterator1 = getPricingInfoList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (PricingInfo)localIterator1.next());
      }
      if (hasUrl()) {
        paramCodedOutputStreamMicro.writeString(5, getUrl());
      }
      Iterator localIterator2 = getParentPricingInfoList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(6, (PricingInfo)localIterator2.next());
      }
      if (hasPreviewUrl()) {
        paramCodedOutputStreamMicro.writeString(7, getPreviewUrl());
      }
    }
    
    public static final class PricingInfo
      extends MessageMicro
    {
      private int cachedSize = -1;
      private List<String> country_ = Collections.emptyList();
      private String currencyCode_ = "";
      private boolean hasCurrencyCode;
      private boolean hasPrice;
      private boolean hasPriceMicros;
      private long priceMicros_ = 0L;
      private String price_ = "";
      
      public PricingInfo addCountry(String paramString)
      {
        if (paramString == null) {
          throw new NullPointerException();
        }
        if (this.country_.isEmpty()) {
          this.country_ = new ArrayList();
        }
        this.country_.add(paramString);
        return this;
      }
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public List<String> getCountryList()
      {
        return this.country_;
      }
      
      public String getCurrencyCode()
      {
        return this.currencyCode_;
      }
      
      public String getPrice()
      {
        return this.price_;
      }
      
      public long getPriceMicros()
      {
        return this.priceMicros_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasPrice();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeStringSize(1, getPrice());
        }
        int j = 0;
        Iterator localIterator = getCountryList().iterator();
        while (localIterator.hasNext()) {
          j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
        }
        int k = i + j + 1 * getCountryList().size();
        if (hasPriceMicros()) {
          k += CodedOutputStreamMicro.computeInt64Size(3, getPriceMicros());
        }
        if (hasCurrencyCode()) {
          k += CodedOutputStreamMicro.computeStringSize(4, getCurrencyCode());
        }
        this.cachedSize = k;
        return k;
      }
      
      public boolean hasCurrencyCode()
      {
        return this.hasCurrencyCode;
      }
      
      public boolean hasPrice()
      {
        return this.hasPrice;
      }
      
      public boolean hasPriceMicros()
      {
        return this.hasPriceMicros;
      }
      
      public PricingInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setPrice(paramCodedInputStreamMicro.readString());
            break;
          case 18: 
            addCountry(paramCodedInputStreamMicro.readString());
            break;
          case 24: 
            setPriceMicros(paramCodedInputStreamMicro.readInt64());
            break;
          }
          setCurrencyCode(paramCodedInputStreamMicro.readString());
        }
      }
      
      public PricingInfo setCurrencyCode(String paramString)
      {
        this.hasCurrencyCode = true;
        this.currencyCode_ = paramString;
        return this;
      }
      
      public PricingInfo setPrice(String paramString)
      {
        this.hasPrice = true;
        this.price_ = paramString;
        return this;
      }
      
      public PricingInfo setPriceMicros(long paramLong)
      {
        this.hasPriceMicros = true;
        this.priceMicros_ = paramLong;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasPrice()) {
          paramCodedOutputStreamMicro.writeString(1, getPrice());
        }
        Iterator localIterator = getCountryList().iterator();
        while (localIterator.hasNext()) {
          paramCodedOutputStreamMicro.writeString(2, (String)localIterator.next());
        }
        if (hasPriceMicros()) {
          paramCodedOutputStreamMicro.writeInt64(3, getPriceMicros());
        }
        if (hasCurrencyCode()) {
          paramCodedOutputStreamMicro.writeString(4, getCurrencyCode());
        }
      }
    }
  }
  
  public static final class TvResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String contentId_ = "";
    private boolean hasContentId;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getContentId()
    {
      return this.contentId_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasContentId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getContentId());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasContentId()
    {
      return this.hasContentId;
    }
    
    public TvResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setContentId(paramCodedInputStreamMicro.readString());
      }
    }
    
    public TvResult setContentId(String paramString)
    {
      this.hasContentId = true;
      this.contentId_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasContentId()) {
        paramCodedOutputStreamMicro.writeString(1, getContentId());
      }
    }
  }
  
  public static final class YouTubeVideo
    extends MessageMicro
  {
    private List<String> allowedCountry_ = Collections.emptyList();
    private List<String> blockedCountry_ = Collections.emptyList();
    private int cachedSize = -1;
    private int duration_ = 0;
    private boolean hasDuration;
    private boolean hasId;
    private boolean hasTitle;
    private boolean hasWatchUrl;
    private String id_ = "";
    private List<VideoThumbnail> thumbnail_ = Collections.emptyList();
    private String title_ = "";
    private String watchUrl_ = "";
    
    public YouTubeVideo addAllowedCountry(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.allowedCountry_.isEmpty()) {
        this.allowedCountry_ = new ArrayList();
      }
      this.allowedCountry_.add(paramString);
      return this;
    }
    
    public YouTubeVideo addBlockedCountry(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.blockedCountry_.isEmpty()) {
        this.blockedCountry_ = new ArrayList();
      }
      this.blockedCountry_.add(paramString);
      return this;
    }
    
    public YouTubeVideo addThumbnail(VideoThumbnail paramVideoThumbnail)
    {
      if (paramVideoThumbnail == null) {
        throw new NullPointerException();
      }
      if (this.thumbnail_.isEmpty()) {
        this.thumbnail_ = new ArrayList();
      }
      this.thumbnail_.add(paramVideoThumbnail);
      return this;
    }
    
    public List<String> getAllowedCountryList()
    {
      return this.allowedCountry_;
    }
    
    public List<String> getBlockedCountryList()
    {
      return this.blockedCountry_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getDuration()
    {
      return this.duration_;
    }
    
    public String getId()
    {
      return this.id_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getId());
      }
      if (hasWatchUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getWatchUrl());
      }
      if (hasTitle()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getTitle());
      }
      if (hasDuration()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getDuration());
      }
      Iterator localIterator1 = getThumbnailList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, (VideoThumbnail)localIterator1.next());
      }
      int j = 0;
      Iterator localIterator2 = getBlockedCountryList().iterator();
      while (localIterator2.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator2.next());
      }
      int k = i + j + 1 * getBlockedCountryList().size();
      int m = 0;
      Iterator localIterator3 = getAllowedCountryList().iterator();
      while (localIterator3.hasNext()) {
        m += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator3.next());
      }
      int n = k + m + 1 * getAllowedCountryList().size();
      this.cachedSize = n;
      return n;
    }
    
    public List<VideoThumbnail> getThumbnailList()
    {
      return this.thumbnail_;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public String getWatchUrl()
    {
      return this.watchUrl_;
    }
    
    public boolean hasDuration()
    {
      return this.hasDuration;
    }
    
    public boolean hasId()
    {
      return this.hasId;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public boolean hasWatchUrl()
    {
      return this.hasWatchUrl;
    }
    
    public YouTubeVideo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setId(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setWatchUrl(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 32: 
          setDuration(paramCodedInputStreamMicro.readInt32());
          break;
        case 42: 
          VideoThumbnail localVideoThumbnail = new VideoThumbnail();
          paramCodedInputStreamMicro.readMessage(localVideoThumbnail);
          addThumbnail(localVideoThumbnail);
          break;
        case 50: 
          addBlockedCountry(paramCodedInputStreamMicro.readString());
          break;
        }
        addAllowedCountry(paramCodedInputStreamMicro.readString());
      }
    }
    
    public YouTubeVideo setDuration(int paramInt)
    {
      this.hasDuration = true;
      this.duration_ = paramInt;
      return this;
    }
    
    public YouTubeVideo setId(String paramString)
    {
      this.hasId = true;
      this.id_ = paramString;
      return this;
    }
    
    public YouTubeVideo setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public YouTubeVideo setWatchUrl(String paramString)
    {
      this.hasWatchUrl = true;
      this.watchUrl_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasId()) {
        paramCodedOutputStreamMicro.writeString(1, getId());
      }
      if (hasWatchUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getWatchUrl());
      }
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(3, getTitle());
      }
      if (hasDuration()) {
        paramCodedOutputStreamMicro.writeInt32(4, getDuration());
      }
      Iterator localIterator1 = getThumbnailList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(5, (VideoThumbnail)localIterator1.next());
      }
      Iterator localIterator2 = getBlockedCountryList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeString(6, (String)localIterator2.next());
      }
      Iterator localIterator3 = getAllowedCountryList().iterator();
      while (localIterator3.hasNext()) {
        paramCodedOutputStreamMicro.writeString(7, (String)localIterator3.next());
      }
    }
    
    public static final class VideoThumbnail
      extends MessageMicro
    {
      private int cachedSize = -1;
      private boolean hasHeight;
      private boolean hasUrl;
      private boolean hasWidth;
      private int height_ = 0;
      private String url_ = "";
      private int width_ = 0;
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public int getHeight()
      {
        return this.height_;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasUrl();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeStringSize(1, getUrl());
        }
        if (hasWidth()) {
          i += CodedOutputStreamMicro.computeInt32Size(2, getWidth());
        }
        if (hasHeight()) {
          i += CodedOutputStreamMicro.computeInt32Size(3, getHeight());
        }
        this.cachedSize = i;
        return i;
      }
      
      public String getUrl()
      {
        return this.url_;
      }
      
      public int getWidth()
      {
        return this.width_;
      }
      
      public boolean hasHeight()
      {
        return this.hasHeight;
      }
      
      public boolean hasUrl()
      {
        return this.hasUrl;
      }
      
      public boolean hasWidth()
      {
        return this.hasWidth;
      }
      
      public VideoThumbnail mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
            setUrl(paramCodedInputStreamMicro.readString());
            break;
          case 16: 
            setWidth(paramCodedInputStreamMicro.readInt32());
            break;
          }
          setHeight(paramCodedInputStreamMicro.readInt32());
        }
      }
      
      public VideoThumbnail setHeight(int paramInt)
      {
        this.hasHeight = true;
        this.height_ = paramInt;
        return this;
      }
      
      public VideoThumbnail setUrl(String paramString)
      {
        this.hasUrl = true;
        this.url_ = paramString;
        return this;
      }
      
      public VideoThumbnail setWidth(int paramInt)
      {
        this.hasWidth = true;
        this.width_ = paramInt;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasUrl()) {
          paramCodedOutputStreamMicro.writeString(1, getUrl());
        }
        if (hasWidth()) {
          paramCodedOutputStreamMicro.writeInt32(2, getWidth());
        }
        if (hasHeight()) {
          paramCodedOutputStreamMicro.writeInt32(3, getHeight());
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.audio.ears.proto.EarsService
 * JD-Core Version:    0.7.0.1
 */