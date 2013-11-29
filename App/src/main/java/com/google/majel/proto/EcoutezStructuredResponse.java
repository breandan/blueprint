package com.google.majel.proto;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class EcoutezStructuredResponse
{
  public static final class AssociationData
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasMatchList;
    private boolean hasName;
    private EcoutezStructuredResponse.MatchList matchList_ = null;
    private String name_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EcoutezStructuredResponse.MatchList getMatchList()
    {
      return this.matchList_;
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
      if (hasMatchList()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getMatchList());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasMatchList()
    {
      return this.hasMatchList;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public AssociationData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        }
        EcoutezStructuredResponse.MatchList localMatchList = new EcoutezStructuredResponse.MatchList();
        paramCodedInputStreamMicro.readMessage(localMatchList);
        setMatchList(localMatchList);
      }
    }
    
    public AssociationData setMatchList(EcoutezStructuredResponse.MatchList paramMatchList)
    {
      if (paramMatchList == null) {
        throw new NullPointerException();
      }
      this.hasMatchList = true;
      this.matchList_ = paramMatchList;
      return this;
    }
    
    public AssociationData setName(String paramString)
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
      if (hasMatchList()) {
        paramCodedOutputStreamMicro.writeMessage(2, getMatchList());
      }
    }
  }
  
  public static final class BaseballMatch
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int firstTeamErrors_ = 0;
    private int firstTeamHits_ = 0;
    private int firstTeamRuns_ = 0;
    private boolean hasFirstTeamErrors;
    private boolean hasFirstTeamHits;
    private boolean hasFirstTeamRuns;
    private boolean hasSecondTeamErrors;
    private boolean hasSecondTeamHits;
    private boolean hasSecondTeamRuns;
    private int secondTeamErrors_ = 0;
    private int secondTeamHits_ = 0;
    private int secondTeamRuns_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getFirstTeamErrors()
    {
      return this.firstTeamErrors_;
    }
    
    public int getFirstTeamHits()
    {
      return this.firstTeamHits_;
    }
    
    public int getFirstTeamRuns()
    {
      return this.firstTeamRuns_;
    }
    
    public int getSecondTeamErrors()
    {
      return this.secondTeamErrors_;
    }
    
    public int getSecondTeamHits()
    {
      return this.secondTeamHits_;
    }
    
    public int getSecondTeamRuns()
    {
      return this.secondTeamRuns_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasFirstTeamRuns();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getFirstTeamRuns());
      }
      if (hasFirstTeamHits()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getFirstTeamHits());
      }
      if (hasFirstTeamErrors()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getFirstTeamErrors());
      }
      if (hasSecondTeamHits()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getSecondTeamHits());
      }
      if (hasSecondTeamRuns()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getSecondTeamRuns());
      }
      if (hasSecondTeamErrors()) {
        i += CodedOutputStreamMicro.computeInt32Size(6, getSecondTeamErrors());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasFirstTeamErrors()
    {
      return this.hasFirstTeamErrors;
    }
    
    public boolean hasFirstTeamHits()
    {
      return this.hasFirstTeamHits;
    }
    
    public boolean hasFirstTeamRuns()
    {
      return this.hasFirstTeamRuns;
    }
    
    public boolean hasSecondTeamErrors()
    {
      return this.hasSecondTeamErrors;
    }
    
    public boolean hasSecondTeamHits()
    {
      return this.hasSecondTeamHits;
    }
    
    public boolean hasSecondTeamRuns()
    {
      return this.hasSecondTeamRuns;
    }
    
    public BaseballMatch mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setFirstTeamRuns(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setFirstTeamHits(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setFirstTeamErrors(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setSecondTeamHits(paramCodedInputStreamMicro.readInt32());
          break;
        case 40: 
          setSecondTeamRuns(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setSecondTeamErrors(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public BaseballMatch setFirstTeamErrors(int paramInt)
    {
      this.hasFirstTeamErrors = true;
      this.firstTeamErrors_ = paramInt;
      return this;
    }
    
    public BaseballMatch setFirstTeamHits(int paramInt)
    {
      this.hasFirstTeamHits = true;
      this.firstTeamHits_ = paramInt;
      return this;
    }
    
    public BaseballMatch setFirstTeamRuns(int paramInt)
    {
      this.hasFirstTeamRuns = true;
      this.firstTeamRuns_ = paramInt;
      return this;
    }
    
    public BaseballMatch setSecondTeamErrors(int paramInt)
    {
      this.hasSecondTeamErrors = true;
      this.secondTeamErrors_ = paramInt;
      return this;
    }
    
    public BaseballMatch setSecondTeamHits(int paramInt)
    {
      this.hasSecondTeamHits = true;
      this.secondTeamHits_ = paramInt;
      return this;
    }
    
    public BaseballMatch setSecondTeamRuns(int paramInt)
    {
      this.hasSecondTeamRuns = true;
      this.secondTeamRuns_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasFirstTeamRuns()) {
        paramCodedOutputStreamMicro.writeInt32(1, getFirstTeamRuns());
      }
      if (hasFirstTeamHits()) {
        paramCodedOutputStreamMicro.writeInt32(2, getFirstTeamHits());
      }
      if (hasFirstTeamErrors()) {
        paramCodedOutputStreamMicro.writeInt32(3, getFirstTeamErrors());
      }
      if (hasSecondTeamHits()) {
        paramCodedOutputStreamMicro.writeInt32(4, getSecondTeamHits());
      }
      if (hasSecondTeamRuns()) {
        paramCodedOutputStreamMicro.writeInt32(5, getSecondTeamRuns());
      }
      if (hasSecondTeamErrors()) {
        paramCodedOutputStreamMicro.writeInt32(6, getSecondTeamErrors());
      }
    }
  }
  
  public static final class BaseballPeriod
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasInningStatus;
    private boolean hasNumOfBalls;
    private boolean hasNumOfOuts;
    private boolean hasNumOfStrikes;
    private int inningStatus_ = 0;
    private int numOfBalls_ = 0;
    private int numOfOuts_ = 0;
    private int numOfStrikes_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getInningStatus()
    {
      return this.inningStatus_;
    }
    
    public int getNumOfBalls()
    {
      return this.numOfBalls_;
    }
    
    public int getNumOfOuts()
    {
      return this.numOfOuts_;
    }
    
    public int getNumOfStrikes()
    {
      return this.numOfStrikes_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasInningStatus();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getInningStatus());
      }
      if (hasNumOfOuts()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getNumOfOuts());
      }
      if (hasNumOfStrikes()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getNumOfStrikes());
      }
      if (hasNumOfBalls()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getNumOfBalls());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasInningStatus()
    {
      return this.hasInningStatus;
    }
    
    public boolean hasNumOfBalls()
    {
      return this.hasNumOfBalls;
    }
    
    public boolean hasNumOfOuts()
    {
      return this.hasNumOfOuts;
    }
    
    public boolean hasNumOfStrikes()
    {
      return this.hasNumOfStrikes;
    }
    
    public BaseballPeriod mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setInningStatus(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setNumOfOuts(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setNumOfStrikes(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setNumOfBalls(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public BaseballPeriod setInningStatus(int paramInt)
    {
      this.hasInningStatus = true;
      this.inningStatus_ = paramInt;
      return this;
    }
    
    public BaseballPeriod setNumOfBalls(int paramInt)
    {
      this.hasNumOfBalls = true;
      this.numOfBalls_ = paramInt;
      return this;
    }
    
    public BaseballPeriod setNumOfOuts(int paramInt)
    {
      this.hasNumOfOuts = true;
      this.numOfOuts_ = paramInt;
      return this;
    }
    
    public BaseballPeriod setNumOfStrikes(int paramInt)
    {
      this.hasNumOfStrikes = true;
      this.numOfStrikes_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasInningStatus()) {
        paramCodedOutputStreamMicro.writeInt32(1, getInningStatus());
      }
      if (hasNumOfOuts()) {
        paramCodedOutputStreamMicro.writeInt32(2, getNumOfOuts());
      }
      if (hasNumOfStrikes()) {
        paramCodedOutputStreamMicro.writeInt32(3, getNumOfStrikes());
      }
      if (hasNumOfBalls()) {
        paramCodedOutputStreamMicro.writeInt32(4, getNumOfBalls());
      }
    }
  }
  
  public static final class Chain
    extends MessageMicro
  {
    private int cachedSize = -1;
    private EcoutezStructuredResponse.ChainId chainId_ = null;
    private String displayName_ = "";
    private boolean hasChainId;
    private boolean hasDisplayName;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EcoutezStructuredResponse.ChainId getChainId()
    {
      return this.chainId_;
    }
    
    public String getDisplayName()
    {
      return this.displayName_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasChainId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getChainId());
      }
      if (hasDisplayName()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getDisplayName());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasChainId()
    {
      return this.hasChainId;
    }
    
    public boolean hasDisplayName()
    {
      return this.hasDisplayName;
    }
    
    public Chain mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EcoutezStructuredResponse.ChainId localChainId = new EcoutezStructuredResponse.ChainId();
          paramCodedInputStreamMicro.readMessage(localChainId);
          setChainId(localChainId);
          break;
        }
        setDisplayName(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Chain setChainId(EcoutezStructuredResponse.ChainId paramChainId)
    {
      if (paramChainId == null) {
        throw new NullPointerException();
      }
      this.hasChainId = true;
      this.chainId_ = paramChainId;
      return this;
    }
    
    public Chain setDisplayName(String paramString)
    {
      this.hasDisplayName = true;
      this.displayName_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasChainId()) {
        paramCodedOutputStreamMicro.writeMessage(1, getChainId());
      }
      if (hasDisplayName()) {
        paramCodedOutputStreamMicro.writeString(2, getDisplayName());
      }
    }
  }
  
  public static final class ChainId
    extends MessageMicro
  {
    private int cachedSize = -1;
    private EcoutezStructuredResponse.FeatureIdProto featureId_ = null;
    private boolean hasFeatureId;
    private boolean hasProminentEntityId;
    private boolean hasSitechunk;
    private String prominentEntityId_ = "";
    private String sitechunk_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EcoutezStructuredResponse.FeatureIdProto getFeatureId()
    {
      return this.featureId_;
    }
    
    public String getProminentEntityId()
    {
      return this.prominentEntityId_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasProminentEntityId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getProminentEntityId());
      }
      if (hasSitechunk()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getSitechunk());
      }
      if (hasFeatureId()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getFeatureId());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSitechunk()
    {
      return this.sitechunk_;
    }
    
    public boolean hasFeatureId()
    {
      return this.hasFeatureId;
    }
    
    public boolean hasProminentEntityId()
    {
      return this.hasProminentEntityId;
    }
    
    public boolean hasSitechunk()
    {
      return this.hasSitechunk;
    }
    
    public ChainId mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setProminentEntityId(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setSitechunk(paramCodedInputStreamMicro.readString());
          break;
        }
        EcoutezStructuredResponse.FeatureIdProto localFeatureIdProto = new EcoutezStructuredResponse.FeatureIdProto();
        paramCodedInputStreamMicro.readMessage(localFeatureIdProto);
        setFeatureId(localFeatureIdProto);
      }
    }
    
    public ChainId setFeatureId(EcoutezStructuredResponse.FeatureIdProto paramFeatureIdProto)
    {
      if (paramFeatureIdProto == null) {
        throw new NullPointerException();
      }
      this.hasFeatureId = true;
      this.featureId_ = paramFeatureIdProto;
      return this;
    }
    
    public ChainId setProminentEntityId(String paramString)
    {
      this.hasProminentEntityId = true;
      this.prominentEntityId_ = paramString;
      return this;
    }
    
    public ChainId setSitechunk(String paramString)
    {
      this.hasSitechunk = true;
      this.sitechunk_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasProminentEntityId()) {
        paramCodedOutputStreamMicro.writeString(1, getProminentEntityId());
      }
      if (hasSitechunk()) {
        paramCodedOutputStreamMicro.writeString(2, getSitechunk());
      }
      if (hasFeatureId()) {
        paramCodedOutputStreamMicro.writeMessage(3, getFeatureId());
      }
    }
  }
  
  public static final class DailyForecast
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int chanceOfPrecipitation_ = 0;
    private EcoutezStructuredResponse.WeatherCondition condition_ = null;
    private boolean hasChanceOfPrecipitation;
    private boolean hasCondition;
    private boolean hasHighTemp;
    private boolean hasLowTemp;
    private int highTemp_ = 0;
    private int lowTemp_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getChanceOfPrecipitation()
    {
      return this.chanceOfPrecipitation_;
    }
    
    public EcoutezStructuredResponse.WeatherCondition getCondition()
    {
      return this.condition_;
    }
    
    public int getHighTemp()
    {
      return this.highTemp_;
    }
    
    public int getLowTemp()
    {
      return this.lowTemp_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasCondition();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getCondition());
      }
      if (hasHighTemp()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getHighTemp());
      }
      if (hasLowTemp()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getLowTemp());
      }
      if (hasChanceOfPrecipitation()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getChanceOfPrecipitation());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasChanceOfPrecipitation()
    {
      return this.hasChanceOfPrecipitation;
    }
    
    public boolean hasCondition()
    {
      return this.hasCondition;
    }
    
    public boolean hasHighTemp()
    {
      return this.hasHighTemp;
    }
    
    public boolean hasLowTemp()
    {
      return this.hasLowTemp;
    }
    
    public DailyForecast mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EcoutezStructuredResponse.WeatherCondition localWeatherCondition = new EcoutezStructuredResponse.WeatherCondition();
          paramCodedInputStreamMicro.readMessage(localWeatherCondition);
          setCondition(localWeatherCondition);
          break;
        case 16: 
          setHighTemp(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setLowTemp(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setChanceOfPrecipitation(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public DailyForecast setChanceOfPrecipitation(int paramInt)
    {
      this.hasChanceOfPrecipitation = true;
      this.chanceOfPrecipitation_ = paramInt;
      return this;
    }
    
    public DailyForecast setCondition(EcoutezStructuredResponse.WeatherCondition paramWeatherCondition)
    {
      if (paramWeatherCondition == null) {
        throw new NullPointerException();
      }
      this.hasCondition = true;
      this.condition_ = paramWeatherCondition;
      return this;
    }
    
    public DailyForecast setHighTemp(int paramInt)
    {
      this.hasHighTemp = true;
      this.highTemp_ = paramInt;
      return this;
    }
    
    public DailyForecast setLowTemp(int paramInt)
    {
      this.hasLowTemp = true;
      this.lowTemp_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasCondition()) {
        paramCodedOutputStreamMicro.writeMessage(1, getCondition());
      }
      if (hasHighTemp()) {
        paramCodedOutputStreamMicro.writeInt32(2, getHighTemp());
      }
      if (hasLowTemp()) {
        paramCodedOutputStreamMicro.writeInt32(3, getLowTemp());
      }
      if (hasChanceOfPrecipitation()) {
        paramCodedOutputStreamMicro.writeInt32(4, getChanceOfPrecipitation());
      }
    }
  }
  
  public static final class DictionaryLink
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasText;
    private boolean hasUrl;
    private String text_ = "";
    private String url_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasText();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getText());
      }
      if (hasUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getUrl());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getText()
    {
      return this.text_;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public boolean hasText()
    {
      return this.hasText;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public DictionaryLink mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setUrl(paramCodedInputStreamMicro.readString());
      }
    }
    
    public DictionaryLink setText(String paramString)
    {
      this.hasText = true;
      this.text_ = paramString;
      return this;
    }
    
    public DictionaryLink setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasText()) {
        paramCodedOutputStreamMicro.writeString(1, getText());
      }
      if (hasUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getUrl());
      }
    }
  }
  
  public static final class DictionaryResult
    extends MessageMicro
  {
    private EcoutezStructuredResponse.DictionaryLink attributionLink_ = null;
    private int cachedSize = -1;
    private String dictionaryWord_ = "";
    private List<EcoutezStructuredResponse.DictionaryLink> externalDictionaryLink_ = Collections.emptyList();
    private EcoutezStructuredResponse.DictionaryLink googleDictionaryLink_ = null;
    private boolean hasAttributionLink;
    private boolean hasDictionaryWord;
    private boolean hasGoogleDictionaryLink;
    private boolean hasNormalForm;
    private boolean hasPartOfSpeech;
    private boolean hasPronunciation;
    private boolean hasSound;
    private boolean hasSynonymsHeader;
    private boolean hasVariationType;
    private String normalForm_ = "";
    private List<EcoutezStructuredResponse.PosMeaning> partOfSpeechMeaning_ = Collections.emptyList();
    private String partOfSpeech_ = "";
    private String pronunciation_ = "";
    private String sound_ = "";
    private List<EcoutezStructuredResponse.Synonym> synonym_ = Collections.emptyList();
    private String synonymsHeader_ = "";
    private String variationType_ = "";
    
    public DictionaryResult addExternalDictionaryLink(EcoutezStructuredResponse.DictionaryLink paramDictionaryLink)
    {
      if (paramDictionaryLink == null) {
        throw new NullPointerException();
      }
      if (this.externalDictionaryLink_.isEmpty()) {
        this.externalDictionaryLink_ = new ArrayList();
      }
      this.externalDictionaryLink_.add(paramDictionaryLink);
      return this;
    }
    
    public DictionaryResult addPartOfSpeechMeaning(EcoutezStructuredResponse.PosMeaning paramPosMeaning)
    {
      if (paramPosMeaning == null) {
        throw new NullPointerException();
      }
      if (this.partOfSpeechMeaning_.isEmpty()) {
        this.partOfSpeechMeaning_ = new ArrayList();
      }
      this.partOfSpeechMeaning_.add(paramPosMeaning);
      return this;
    }
    
    public DictionaryResult addSynonym(EcoutezStructuredResponse.Synonym paramSynonym)
    {
      if (paramSynonym == null) {
        throw new NullPointerException();
      }
      if (this.synonym_.isEmpty()) {
        this.synonym_ = new ArrayList();
      }
      this.synonym_.add(paramSynonym);
      return this;
    }
    
    public EcoutezStructuredResponse.DictionaryLink getAttributionLink()
    {
      return this.attributionLink_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDictionaryWord()
    {
      return this.dictionaryWord_;
    }
    
    public List<EcoutezStructuredResponse.DictionaryLink> getExternalDictionaryLinkList()
    {
      return this.externalDictionaryLink_;
    }
    
    public EcoutezStructuredResponse.DictionaryLink getGoogleDictionaryLink()
    {
      return this.googleDictionaryLink_;
    }
    
    public String getNormalForm()
    {
      return this.normalForm_;
    }
    
    public String getPartOfSpeech()
    {
      return this.partOfSpeech_;
    }
    
    public List<EcoutezStructuredResponse.PosMeaning> getPartOfSpeechMeaningList()
    {
      return this.partOfSpeechMeaning_;
    }
    
    public String getPronunciation()
    {
      return this.pronunciation_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasDictionaryWord();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getDictionaryWord());
      }
      if (hasVariationType()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getVariationType());
      }
      if (hasNormalForm()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getNormalForm());
      }
      if (hasPartOfSpeech()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getPartOfSpeech());
      }
      if (hasPronunciation()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getPronunciation());
      }
      if (hasSound()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getSound());
      }
      Iterator localIterator1 = getPartOfSpeechMeaningList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, (EcoutezStructuredResponse.PosMeaning)localIterator1.next());
      }
      if (hasSynonymsHeader()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getSynonymsHeader());
      }
      Iterator localIterator2 = getSynonymList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(9, (EcoutezStructuredResponse.Synonym)localIterator2.next());
      }
      if (hasGoogleDictionaryLink()) {
        i += CodedOutputStreamMicro.computeMessageSize(10, getGoogleDictionaryLink());
      }
      if (hasAttributionLink()) {
        i += CodedOutputStreamMicro.computeMessageSize(11, getAttributionLink());
      }
      Iterator localIterator3 = getExternalDictionaryLinkList().iterator();
      while (localIterator3.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(12, (EcoutezStructuredResponse.DictionaryLink)localIterator3.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSound()
    {
      return this.sound_;
    }
    
    public List<EcoutezStructuredResponse.Synonym> getSynonymList()
    {
      return this.synonym_;
    }
    
    public String getSynonymsHeader()
    {
      return this.synonymsHeader_;
    }
    
    public String getVariationType()
    {
      return this.variationType_;
    }
    
    public boolean hasAttributionLink()
    {
      return this.hasAttributionLink;
    }
    
    public boolean hasDictionaryWord()
    {
      return this.hasDictionaryWord;
    }
    
    public boolean hasGoogleDictionaryLink()
    {
      return this.hasGoogleDictionaryLink;
    }
    
    public boolean hasNormalForm()
    {
      return this.hasNormalForm;
    }
    
    public boolean hasPartOfSpeech()
    {
      return this.hasPartOfSpeech;
    }
    
    public boolean hasPronunciation()
    {
      return this.hasPronunciation;
    }
    
    public boolean hasSound()
    {
      return this.hasSound;
    }
    
    public boolean hasSynonymsHeader()
    {
      return this.hasSynonymsHeader;
    }
    
    public boolean hasVariationType()
    {
      return this.hasVariationType;
    }
    
    public DictionaryResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setDictionaryWord(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setVariationType(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setNormalForm(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setPartOfSpeech(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setPronunciation(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          setSound(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          EcoutezStructuredResponse.PosMeaning localPosMeaning = new EcoutezStructuredResponse.PosMeaning();
          paramCodedInputStreamMicro.readMessage(localPosMeaning);
          addPartOfSpeechMeaning(localPosMeaning);
          break;
        case 66: 
          setSynonymsHeader(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          EcoutezStructuredResponse.Synonym localSynonym = new EcoutezStructuredResponse.Synonym();
          paramCodedInputStreamMicro.readMessage(localSynonym);
          addSynonym(localSynonym);
          break;
        case 82: 
          EcoutezStructuredResponse.DictionaryLink localDictionaryLink3 = new EcoutezStructuredResponse.DictionaryLink();
          paramCodedInputStreamMicro.readMessage(localDictionaryLink3);
          setGoogleDictionaryLink(localDictionaryLink3);
          break;
        case 90: 
          EcoutezStructuredResponse.DictionaryLink localDictionaryLink2 = new EcoutezStructuredResponse.DictionaryLink();
          paramCodedInputStreamMicro.readMessage(localDictionaryLink2);
          setAttributionLink(localDictionaryLink2);
          break;
        }
        EcoutezStructuredResponse.DictionaryLink localDictionaryLink1 = new EcoutezStructuredResponse.DictionaryLink();
        paramCodedInputStreamMicro.readMessage(localDictionaryLink1);
        addExternalDictionaryLink(localDictionaryLink1);
      }
    }
    
    public DictionaryResult setAttributionLink(EcoutezStructuredResponse.DictionaryLink paramDictionaryLink)
    {
      if (paramDictionaryLink == null) {
        throw new NullPointerException();
      }
      this.hasAttributionLink = true;
      this.attributionLink_ = paramDictionaryLink;
      return this;
    }
    
    public DictionaryResult setDictionaryWord(String paramString)
    {
      this.hasDictionaryWord = true;
      this.dictionaryWord_ = paramString;
      return this;
    }
    
    public DictionaryResult setGoogleDictionaryLink(EcoutezStructuredResponse.DictionaryLink paramDictionaryLink)
    {
      if (paramDictionaryLink == null) {
        throw new NullPointerException();
      }
      this.hasGoogleDictionaryLink = true;
      this.googleDictionaryLink_ = paramDictionaryLink;
      return this;
    }
    
    public DictionaryResult setNormalForm(String paramString)
    {
      this.hasNormalForm = true;
      this.normalForm_ = paramString;
      return this;
    }
    
    public DictionaryResult setPartOfSpeech(String paramString)
    {
      this.hasPartOfSpeech = true;
      this.partOfSpeech_ = paramString;
      return this;
    }
    
    public DictionaryResult setPronunciation(String paramString)
    {
      this.hasPronunciation = true;
      this.pronunciation_ = paramString;
      return this;
    }
    
    public DictionaryResult setSound(String paramString)
    {
      this.hasSound = true;
      this.sound_ = paramString;
      return this;
    }
    
    public DictionaryResult setSynonymsHeader(String paramString)
    {
      this.hasSynonymsHeader = true;
      this.synonymsHeader_ = paramString;
      return this;
    }
    
    public DictionaryResult setVariationType(String paramString)
    {
      this.hasVariationType = true;
      this.variationType_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasDictionaryWord()) {
        paramCodedOutputStreamMicro.writeString(1, getDictionaryWord());
      }
      if (hasVariationType()) {
        paramCodedOutputStreamMicro.writeString(2, getVariationType());
      }
      if (hasNormalForm()) {
        paramCodedOutputStreamMicro.writeString(3, getNormalForm());
      }
      if (hasPartOfSpeech()) {
        paramCodedOutputStreamMicro.writeString(4, getPartOfSpeech());
      }
      if (hasPronunciation()) {
        paramCodedOutputStreamMicro.writeString(5, getPronunciation());
      }
      if (hasSound()) {
        paramCodedOutputStreamMicro.writeString(6, getSound());
      }
      Iterator localIterator1 = getPartOfSpeechMeaningList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(7, (EcoutezStructuredResponse.PosMeaning)localIterator1.next());
      }
      if (hasSynonymsHeader()) {
        paramCodedOutputStreamMicro.writeString(8, getSynonymsHeader());
      }
      Iterator localIterator2 = getSynonymList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(9, (EcoutezStructuredResponse.Synonym)localIterator2.next());
      }
      if (hasGoogleDictionaryLink()) {
        paramCodedOutputStreamMicro.writeMessage(10, getGoogleDictionaryLink());
      }
      if (hasAttributionLink()) {
        paramCodedOutputStreamMicro.writeMessage(11, getAttributionLink());
      }
      Iterator localIterator3 = getExternalDictionaryLinkList().iterator();
      while (localIterator3.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(12, (EcoutezStructuredResponse.DictionaryLink)localIterator3.next());
      }
    }
  }
  
  public static final class EcnResult
    extends MessageMicro
  {
    private String anchor_ = "";
    private int cachedSize = -1;
    private boolean hasAnchor;
    private boolean hasLastChangeTime;
    private boolean hasLastPrice;
    private boolean hasPriceChange;
    private boolean hasPricePercentChange;
    private String lastChangeTime_ = "";
    private float lastPrice_ = 0.0F;
    private float priceChange_ = 0.0F;
    private float pricePercentChange_ = 0.0F;
    
    public String getAnchor()
    {
      return this.anchor_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getLastChangeTime()
    {
      return this.lastChangeTime_;
    }
    
    public float getLastPrice()
    {
      return this.lastPrice_;
    }
    
    public float getPriceChange()
    {
      return this.priceChange_;
    }
    
    public float getPricePercentChange()
    {
      return this.pricePercentChange_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasAnchor();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getAnchor());
      }
      if (hasLastPrice()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getLastPrice());
      }
      if (hasPriceChange()) {
        i += CodedOutputStreamMicro.computeFloatSize(3, getPriceChange());
      }
      if (hasPricePercentChange()) {
        i += CodedOutputStreamMicro.computeFloatSize(4, getPricePercentChange());
      }
      if (hasLastChangeTime()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getLastChangeTime());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAnchor()
    {
      return this.hasAnchor;
    }
    
    public boolean hasLastChangeTime()
    {
      return this.hasLastChangeTime;
    }
    
    public boolean hasLastPrice()
    {
      return this.hasLastPrice;
    }
    
    public boolean hasPriceChange()
    {
      return this.hasPriceChange;
    }
    
    public boolean hasPricePercentChange()
    {
      return this.hasPricePercentChange;
    }
    
    public EcnResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setAnchor(paramCodedInputStreamMicro.readString());
          break;
        case 21: 
          setLastPrice(paramCodedInputStreamMicro.readFloat());
          break;
        case 29: 
          setPriceChange(paramCodedInputStreamMicro.readFloat());
          break;
        case 37: 
          setPricePercentChange(paramCodedInputStreamMicro.readFloat());
          break;
        }
        setLastChangeTime(paramCodedInputStreamMicro.readString());
      }
    }
    
    public EcnResult setAnchor(String paramString)
    {
      this.hasAnchor = true;
      this.anchor_ = paramString;
      return this;
    }
    
    public EcnResult setLastChangeTime(String paramString)
    {
      this.hasLastChangeTime = true;
      this.lastChangeTime_ = paramString;
      return this;
    }
    
    public EcnResult setLastPrice(float paramFloat)
    {
      this.hasLastPrice = true;
      this.lastPrice_ = paramFloat;
      return this;
    }
    
    public EcnResult setPriceChange(float paramFloat)
    {
      this.hasPriceChange = true;
      this.priceChange_ = paramFloat;
      return this;
    }
    
    public EcnResult setPricePercentChange(float paramFloat)
    {
      this.hasPricePercentChange = true;
      this.pricePercentChange_ = paramFloat;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAnchor()) {
        paramCodedOutputStreamMicro.writeString(1, getAnchor());
      }
      if (hasLastPrice()) {
        paramCodedOutputStreamMicro.writeFloat(2, getLastPrice());
      }
      if (hasPriceChange()) {
        paramCodedOutputStreamMicro.writeFloat(3, getPriceChange());
      }
      if (hasPricePercentChange()) {
        paramCodedOutputStreamMicro.writeFloat(4, getPricePercentChange());
      }
      if (hasLastChangeTime()) {
        paramCodedOutputStreamMicro.writeString(5, getLastChangeTime());
      }
    }
  }
  
  public static final class EcoutezLocalResult
    extends MessageMicro
  {
    private String actionBikingUrl_ = "";
    private String actionDrivingUrl_ = "";
    private String actionTransitUrl_ = "";
    private String actionWalkingUrl_ = "";
    private String address1_ = "";
    private String address2_ = "";
    private String addressForMapImageUrl_ = "";
    private String address_ = "";
    private AliasProto.Alias alias_ = null;
    private String authority_ = "";
    private String businessDomain_ = "";
    private String businessUrl_ = "";
    private int cachedSize = -1;
    private EcoutezStructuredResponse.Chain chain_ = null;
    private String clusterId_ = "";
    private String deprecatedHours_ = "";
    private EcoutezStructuredResponse.FeatureIdProto featureId_ = null;
    private boolean hasActionBikingUrl;
    private boolean hasActionDrivingUrl;
    private boolean hasActionTransitUrl;
    private boolean hasActionWalkingUrl;
    private boolean hasAddress;
    private boolean hasAddress1;
    private boolean hasAddress2;
    private boolean hasAddressForMapImageUrl;
    private boolean hasAlias;
    private boolean hasAuthority;
    private boolean hasBusinessDomain;
    private boolean hasBusinessUrl;
    private boolean hasChain;
    private boolean hasClusterId;
    private boolean hasDeprecatedHours;
    private boolean hasFeatureId;
    private boolean hasHours;
    private boolean hasIsChain;
    private boolean hasLatDegrees;
    private boolean hasLatSpanDegrees;
    private boolean hasLngDegrees;
    private boolean hasLngSpanDegrees;
    private boolean hasMapsUrl;
    private boolean hasNearLocation;
    private boolean hasNumHalfStars;
    private boolean hasNumReviews;
    private boolean hasPhoneNumber;
    private boolean hasPlacePageUrl;
    private boolean hasQuery;
    private boolean hasReviewSnippet;
    private boolean hasReviewsText;
    private boolean hasTitle;
    private boolean hasTransitStationName;
    private boolean hasTransitStationText;
    private boolean hasTransitStationType;
    private EcoutezStructuredResponse.Hours hours_ = null;
    private boolean isChain_ = false;
    private double latDegrees_ = 0.0D;
    private double latSpanDegrees_ = 0.0D;
    private double lngDegrees_ = 0.0D;
    private double lngSpanDegrees_ = 0.0D;
    private String mapsUrl_ = "";
    private String nearLocation_ = "";
    private int numHalfStars_ = 0;
    private int numReviews_ = 0;
    private String phoneNumber_ = "";
    private String placePageUrl_ = "";
    private String query_ = "";
    private List<EcoutezStructuredResponse.ReviewSite> reviewSite_ = Collections.emptyList();
    private String reviewSnippet_ = "";
    private String reviewsText_ = "";
    private String title_ = "";
    private String transitStationName_ = "";
    private String transitStationText_ = "";
    private String transitStationType_ = "";
    
    public EcoutezLocalResult addReviewSite(EcoutezStructuredResponse.ReviewSite paramReviewSite)
    {
      if (paramReviewSite == null) {
        throw new NullPointerException();
      }
      if (this.reviewSite_.isEmpty()) {
        this.reviewSite_ = new ArrayList();
      }
      this.reviewSite_.add(paramReviewSite);
      return this;
    }
    
    public String getActionBikingUrl()
    {
      return this.actionBikingUrl_;
    }
    
    public String getActionDrivingUrl()
    {
      return this.actionDrivingUrl_;
    }
    
    public String getActionTransitUrl()
    {
      return this.actionTransitUrl_;
    }
    
    public String getActionWalkingUrl()
    {
      return this.actionWalkingUrl_;
    }
    
    public String getAddress()
    {
      return this.address_;
    }
    
    public String getAddress1()
    {
      return this.address1_;
    }
    
    public String getAddress2()
    {
      return this.address2_;
    }
    
    public String getAddressForMapImageUrl()
    {
      return this.addressForMapImageUrl_;
    }
    
    public AliasProto.Alias getAlias()
    {
      return this.alias_;
    }
    
    public String getAuthority()
    {
      return this.authority_;
    }
    
    public String getBusinessDomain()
    {
      return this.businessDomain_;
    }
    
    public String getBusinessUrl()
    {
      return this.businessUrl_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EcoutezStructuredResponse.Chain getChain()
    {
      return this.chain_;
    }
    
    public String getClusterId()
    {
      return this.clusterId_;
    }
    
    public String getDeprecatedHours()
    {
      return this.deprecatedHours_;
    }
    
    public EcoutezStructuredResponse.FeatureIdProto getFeatureId()
    {
      return this.featureId_;
    }
    
    public EcoutezStructuredResponse.Hours getHours()
    {
      return this.hours_;
    }
    
    public boolean getIsChain()
    {
      return this.isChain_;
    }
    
    public double getLatDegrees()
    {
      return this.latDegrees_;
    }
    
    public double getLatSpanDegrees()
    {
      return this.latSpanDegrees_;
    }
    
    public double getLngDegrees()
    {
      return this.lngDegrees_;
    }
    
    public double getLngSpanDegrees()
    {
      return this.lngSpanDegrees_;
    }
    
    public String getMapsUrl()
    {
      return this.mapsUrl_;
    }
    
    public String getNearLocation()
    {
      return this.nearLocation_;
    }
    
    public int getNumHalfStars()
    {
      return this.numHalfStars_;
    }
    
    public int getNumReviews()
    {
      return this.numReviews_;
    }
    
    public String getPhoneNumber()
    {
      return this.phoneNumber_;
    }
    
    public String getPlacePageUrl()
    {
      return this.placePageUrl_;
    }
    
    public String getQuery()
    {
      return this.query_;
    }
    
    public List<EcoutezStructuredResponse.ReviewSite> getReviewSiteList()
    {
      return this.reviewSite_;
    }
    
    public String getReviewSnippet()
    {
      return this.reviewSnippet_;
    }
    
    public String getReviewsText()
    {
      return this.reviewsText_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasTitle();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getTitle());
      }
      if (hasBusinessUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getBusinessUrl());
      }
      if (hasBusinessDomain()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getBusinessDomain());
      }
      if (hasPlacePageUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getPlacePageUrl());
      }
      if (hasLatDegrees()) {
        i += CodedOutputStreamMicro.computeDoubleSize(5, getLatDegrees());
      }
      if (hasLngDegrees()) {
        i += CodedOutputStreamMicro.computeDoubleSize(6, getLngDegrees());
      }
      if (hasNearLocation()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getNearLocation());
      }
      if (hasQuery()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getQuery());
      }
      if (hasAddress()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getAddress());
      }
      if (hasAddress1()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getAddress1());
      }
      if (hasAddress2()) {
        i += CodedOutputStreamMicro.computeStringSize(11, getAddress2());
      }
      if (hasPhoneNumber()) {
        i += CodedOutputStreamMicro.computeStringSize(12, getPhoneNumber());
      }
      if (hasDeprecatedHours()) {
        i += CodedOutputStreamMicro.computeStringSize(13, getDeprecatedHours());
      }
      if (hasAuthority()) {
        i += CodedOutputStreamMicro.computeStringSize(14, getAuthority());
      }
      if (hasTransitStationText()) {
        i += CodedOutputStreamMicro.computeStringSize(15, getTransitStationText());
      }
      if (hasTransitStationType()) {
        i += CodedOutputStreamMicro.computeStringSize(16, getTransitStationType());
      }
      if (hasTransitStationName()) {
        i += CodedOutputStreamMicro.computeStringSize(17, getTransitStationName());
      }
      if (hasReviewSnippet()) {
        i += CodedOutputStreamMicro.computeStringSize(18, getReviewSnippet());
      }
      if (hasNumHalfStars()) {
        i += CodedOutputStreamMicro.computeInt32Size(19, getNumHalfStars());
      }
      if (hasNumReviews()) {
        i += CodedOutputStreamMicro.computeInt32Size(20, getNumReviews());
      }
      if (hasReviewsText()) {
        i += CodedOutputStreamMicro.computeStringSize(21, getReviewsText());
      }
      Iterator localIterator = getReviewSiteList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(22, (EcoutezStructuredResponse.ReviewSite)localIterator.next());
      }
      if (hasClusterId()) {
        i += CodedOutputStreamMicro.computeStringSize(23, getClusterId());
      }
      if (hasMapsUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(24, getMapsUrl());
      }
      if (hasActionDrivingUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(25, getActionDrivingUrl());
      }
      if (hasActionWalkingUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(26, getActionWalkingUrl());
      }
      if (hasActionTransitUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(27, getActionTransitUrl());
      }
      if (hasActionBikingUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(28, getActionBikingUrl());
      }
      if (hasLatSpanDegrees()) {
        i += CodedOutputStreamMicro.computeDoubleSize(29, getLatSpanDegrees());
      }
      if (hasLngSpanDegrees()) {
        i += CodedOutputStreamMicro.computeDoubleSize(30, getLngSpanDegrees());
      }
      if (hasHours()) {
        i += CodedOutputStreamMicro.computeMessageSize(31, getHours());
      }
      if (hasAddressForMapImageUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(32, getAddressForMapImageUrl());
      }
      if (hasAlias()) {
        i += CodedOutputStreamMicro.computeMessageSize(33, getAlias());
      }
      if (hasFeatureId()) {
        i += CodedOutputStreamMicro.computeMessageSize(34, getFeatureId());
      }
      if (hasIsChain()) {
        i += CodedOutputStreamMicro.computeBoolSize(35, getIsChain());
      }
      if (hasChain()) {
        i += CodedOutputStreamMicro.computeMessageSize(36, getChain());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public String getTransitStationName()
    {
      return this.transitStationName_;
    }
    
    public String getTransitStationText()
    {
      return this.transitStationText_;
    }
    
    public String getTransitStationType()
    {
      return this.transitStationType_;
    }
    
    public boolean hasActionBikingUrl()
    {
      return this.hasActionBikingUrl;
    }
    
    public boolean hasActionDrivingUrl()
    {
      return this.hasActionDrivingUrl;
    }
    
    public boolean hasActionTransitUrl()
    {
      return this.hasActionTransitUrl;
    }
    
    public boolean hasActionWalkingUrl()
    {
      return this.hasActionWalkingUrl;
    }
    
    public boolean hasAddress()
    {
      return this.hasAddress;
    }
    
    public boolean hasAddress1()
    {
      return this.hasAddress1;
    }
    
    public boolean hasAddress2()
    {
      return this.hasAddress2;
    }
    
    public boolean hasAddressForMapImageUrl()
    {
      return this.hasAddressForMapImageUrl;
    }
    
    public boolean hasAlias()
    {
      return this.hasAlias;
    }
    
    public boolean hasAuthority()
    {
      return this.hasAuthority;
    }
    
    public boolean hasBusinessDomain()
    {
      return this.hasBusinessDomain;
    }
    
    public boolean hasBusinessUrl()
    {
      return this.hasBusinessUrl;
    }
    
    public boolean hasChain()
    {
      return this.hasChain;
    }
    
    public boolean hasClusterId()
    {
      return this.hasClusterId;
    }
    
    public boolean hasDeprecatedHours()
    {
      return this.hasDeprecatedHours;
    }
    
    public boolean hasFeatureId()
    {
      return this.hasFeatureId;
    }
    
    public boolean hasHours()
    {
      return this.hasHours;
    }
    
    public boolean hasIsChain()
    {
      return this.hasIsChain;
    }
    
    public boolean hasLatDegrees()
    {
      return this.hasLatDegrees;
    }
    
    public boolean hasLatSpanDegrees()
    {
      return this.hasLatSpanDegrees;
    }
    
    public boolean hasLngDegrees()
    {
      return this.hasLngDegrees;
    }
    
    public boolean hasLngSpanDegrees()
    {
      return this.hasLngSpanDegrees;
    }
    
    public boolean hasMapsUrl()
    {
      return this.hasMapsUrl;
    }
    
    public boolean hasNearLocation()
    {
      return this.hasNearLocation;
    }
    
    public boolean hasNumHalfStars()
    {
      return this.hasNumHalfStars;
    }
    
    public boolean hasNumReviews()
    {
      return this.hasNumReviews;
    }
    
    public boolean hasPhoneNumber()
    {
      return this.hasPhoneNumber;
    }
    
    public boolean hasPlacePageUrl()
    {
      return this.hasPlacePageUrl;
    }
    
    public boolean hasQuery()
    {
      return this.hasQuery;
    }
    
    public boolean hasReviewSnippet()
    {
      return this.hasReviewSnippet;
    }
    
    public boolean hasReviewsText()
    {
      return this.hasReviewsText;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public boolean hasTransitStationName()
    {
      return this.hasTransitStationName;
    }
    
    public boolean hasTransitStationText()
    {
      return this.hasTransitStationText;
    }
    
    public boolean hasTransitStationType()
    {
      return this.hasTransitStationType;
    }
    
    public EcoutezLocalResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setBusinessUrl(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setBusinessDomain(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setPlacePageUrl(paramCodedInputStreamMicro.readString());
          break;
        case 41: 
          setLatDegrees(paramCodedInputStreamMicro.readDouble());
          break;
        case 49: 
          setLngDegrees(paramCodedInputStreamMicro.readDouble());
          break;
        case 58: 
          setNearLocation(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setQuery(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          setAddress(paramCodedInputStreamMicro.readString());
          break;
        case 82: 
          setAddress1(paramCodedInputStreamMicro.readString());
          break;
        case 90: 
          setAddress2(paramCodedInputStreamMicro.readString());
          break;
        case 98: 
          setPhoneNumber(paramCodedInputStreamMicro.readString());
          break;
        case 106: 
          setDeprecatedHours(paramCodedInputStreamMicro.readString());
          break;
        case 114: 
          setAuthority(paramCodedInputStreamMicro.readString());
          break;
        case 122: 
          setTransitStationText(paramCodedInputStreamMicro.readString());
          break;
        case 130: 
          setTransitStationType(paramCodedInputStreamMicro.readString());
          break;
        case 138: 
          setTransitStationName(paramCodedInputStreamMicro.readString());
          break;
        case 146: 
          setReviewSnippet(paramCodedInputStreamMicro.readString());
          break;
        case 152: 
          setNumHalfStars(paramCodedInputStreamMicro.readInt32());
          break;
        case 160: 
          setNumReviews(paramCodedInputStreamMicro.readInt32());
          break;
        case 170: 
          setReviewsText(paramCodedInputStreamMicro.readString());
          break;
        case 178: 
          EcoutezStructuredResponse.ReviewSite localReviewSite = new EcoutezStructuredResponse.ReviewSite();
          paramCodedInputStreamMicro.readMessage(localReviewSite);
          addReviewSite(localReviewSite);
          break;
        case 186: 
          setClusterId(paramCodedInputStreamMicro.readString());
          break;
        case 194: 
          setMapsUrl(paramCodedInputStreamMicro.readString());
          break;
        case 202: 
          setActionDrivingUrl(paramCodedInputStreamMicro.readString());
          break;
        case 210: 
          setActionWalkingUrl(paramCodedInputStreamMicro.readString());
          break;
        case 218: 
          setActionTransitUrl(paramCodedInputStreamMicro.readString());
          break;
        case 226: 
          setActionBikingUrl(paramCodedInputStreamMicro.readString());
          break;
        case 233: 
          setLatSpanDegrees(paramCodedInputStreamMicro.readDouble());
          break;
        case 241: 
          setLngSpanDegrees(paramCodedInputStreamMicro.readDouble());
          break;
        case 250: 
          EcoutezStructuredResponse.Hours localHours = new EcoutezStructuredResponse.Hours();
          paramCodedInputStreamMicro.readMessage(localHours);
          setHours(localHours);
          break;
        case 258: 
          setAddressForMapImageUrl(paramCodedInputStreamMicro.readString());
          break;
        case 266: 
          AliasProto.Alias localAlias = new AliasProto.Alias();
          paramCodedInputStreamMicro.readMessage(localAlias);
          setAlias(localAlias);
          break;
        case 274: 
          EcoutezStructuredResponse.FeatureIdProto localFeatureIdProto = new EcoutezStructuredResponse.FeatureIdProto();
          paramCodedInputStreamMicro.readMessage(localFeatureIdProto);
          setFeatureId(localFeatureIdProto);
          break;
        case 280: 
          setIsChain(paramCodedInputStreamMicro.readBool());
          break;
        }
        EcoutezStructuredResponse.Chain localChain = new EcoutezStructuredResponse.Chain();
        paramCodedInputStreamMicro.readMessage(localChain);
        setChain(localChain);
      }
    }
    
    public EcoutezLocalResult setActionBikingUrl(String paramString)
    {
      this.hasActionBikingUrl = true;
      this.actionBikingUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setActionDrivingUrl(String paramString)
    {
      this.hasActionDrivingUrl = true;
      this.actionDrivingUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setActionTransitUrl(String paramString)
    {
      this.hasActionTransitUrl = true;
      this.actionTransitUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setActionWalkingUrl(String paramString)
    {
      this.hasActionWalkingUrl = true;
      this.actionWalkingUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setAddress(String paramString)
    {
      this.hasAddress = true;
      this.address_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setAddress1(String paramString)
    {
      this.hasAddress1 = true;
      this.address1_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setAddress2(String paramString)
    {
      this.hasAddress2 = true;
      this.address2_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setAddressForMapImageUrl(String paramString)
    {
      this.hasAddressForMapImageUrl = true;
      this.addressForMapImageUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setAlias(AliasProto.Alias paramAlias)
    {
      if (paramAlias == null) {
        throw new NullPointerException();
      }
      this.hasAlias = true;
      this.alias_ = paramAlias;
      return this;
    }
    
    public EcoutezLocalResult setAuthority(String paramString)
    {
      this.hasAuthority = true;
      this.authority_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setBusinessDomain(String paramString)
    {
      this.hasBusinessDomain = true;
      this.businessDomain_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setBusinessUrl(String paramString)
    {
      this.hasBusinessUrl = true;
      this.businessUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setChain(EcoutezStructuredResponse.Chain paramChain)
    {
      if (paramChain == null) {
        throw new NullPointerException();
      }
      this.hasChain = true;
      this.chain_ = paramChain;
      return this;
    }
    
    public EcoutezLocalResult setClusterId(String paramString)
    {
      this.hasClusterId = true;
      this.clusterId_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setDeprecatedHours(String paramString)
    {
      this.hasDeprecatedHours = true;
      this.deprecatedHours_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setFeatureId(EcoutezStructuredResponse.FeatureIdProto paramFeatureIdProto)
    {
      if (paramFeatureIdProto == null) {
        throw new NullPointerException();
      }
      this.hasFeatureId = true;
      this.featureId_ = paramFeatureIdProto;
      return this;
    }
    
    public EcoutezLocalResult setHours(EcoutezStructuredResponse.Hours paramHours)
    {
      if (paramHours == null) {
        throw new NullPointerException();
      }
      this.hasHours = true;
      this.hours_ = paramHours;
      return this;
    }
    
    public EcoutezLocalResult setIsChain(boolean paramBoolean)
    {
      this.hasIsChain = true;
      this.isChain_ = paramBoolean;
      return this;
    }
    
    public EcoutezLocalResult setLatDegrees(double paramDouble)
    {
      this.hasLatDegrees = true;
      this.latDegrees_ = paramDouble;
      return this;
    }
    
    public EcoutezLocalResult setLatSpanDegrees(double paramDouble)
    {
      this.hasLatSpanDegrees = true;
      this.latSpanDegrees_ = paramDouble;
      return this;
    }
    
    public EcoutezLocalResult setLngDegrees(double paramDouble)
    {
      this.hasLngDegrees = true;
      this.lngDegrees_ = paramDouble;
      return this;
    }
    
    public EcoutezLocalResult setLngSpanDegrees(double paramDouble)
    {
      this.hasLngSpanDegrees = true;
      this.lngSpanDegrees_ = paramDouble;
      return this;
    }
    
    public EcoutezLocalResult setMapsUrl(String paramString)
    {
      this.hasMapsUrl = true;
      this.mapsUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setNearLocation(String paramString)
    {
      this.hasNearLocation = true;
      this.nearLocation_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setNumHalfStars(int paramInt)
    {
      this.hasNumHalfStars = true;
      this.numHalfStars_ = paramInt;
      return this;
    }
    
    public EcoutezLocalResult setNumReviews(int paramInt)
    {
      this.hasNumReviews = true;
      this.numReviews_ = paramInt;
      return this;
    }
    
    public EcoutezLocalResult setPhoneNumber(String paramString)
    {
      this.hasPhoneNumber = true;
      this.phoneNumber_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setPlacePageUrl(String paramString)
    {
      this.hasPlacePageUrl = true;
      this.placePageUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setQuery(String paramString)
    {
      this.hasQuery = true;
      this.query_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setReviewSnippet(String paramString)
    {
      this.hasReviewSnippet = true;
      this.reviewSnippet_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setReviewsText(String paramString)
    {
      this.hasReviewsText = true;
      this.reviewsText_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setTransitStationName(String paramString)
    {
      this.hasTransitStationName = true;
      this.transitStationName_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setTransitStationText(String paramString)
    {
      this.hasTransitStationText = true;
      this.transitStationText_ = paramString;
      return this;
    }
    
    public EcoutezLocalResult setTransitStationType(String paramString)
    {
      this.hasTransitStationType = true;
      this.transitStationType_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(1, getTitle());
      }
      if (hasBusinessUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getBusinessUrl());
      }
      if (hasBusinessDomain()) {
        paramCodedOutputStreamMicro.writeString(3, getBusinessDomain());
      }
      if (hasPlacePageUrl()) {
        paramCodedOutputStreamMicro.writeString(4, getPlacePageUrl());
      }
      if (hasLatDegrees()) {
        paramCodedOutputStreamMicro.writeDouble(5, getLatDegrees());
      }
      if (hasLngDegrees()) {
        paramCodedOutputStreamMicro.writeDouble(6, getLngDegrees());
      }
      if (hasNearLocation()) {
        paramCodedOutputStreamMicro.writeString(7, getNearLocation());
      }
      if (hasQuery()) {
        paramCodedOutputStreamMicro.writeString(8, getQuery());
      }
      if (hasAddress()) {
        paramCodedOutputStreamMicro.writeString(9, getAddress());
      }
      if (hasAddress1()) {
        paramCodedOutputStreamMicro.writeString(10, getAddress1());
      }
      if (hasAddress2()) {
        paramCodedOutputStreamMicro.writeString(11, getAddress2());
      }
      if (hasPhoneNumber()) {
        paramCodedOutputStreamMicro.writeString(12, getPhoneNumber());
      }
      if (hasDeprecatedHours()) {
        paramCodedOutputStreamMicro.writeString(13, getDeprecatedHours());
      }
      if (hasAuthority()) {
        paramCodedOutputStreamMicro.writeString(14, getAuthority());
      }
      if (hasTransitStationText()) {
        paramCodedOutputStreamMicro.writeString(15, getTransitStationText());
      }
      if (hasTransitStationType()) {
        paramCodedOutputStreamMicro.writeString(16, getTransitStationType());
      }
      if (hasTransitStationName()) {
        paramCodedOutputStreamMicro.writeString(17, getTransitStationName());
      }
      if (hasReviewSnippet()) {
        paramCodedOutputStreamMicro.writeString(18, getReviewSnippet());
      }
      if (hasNumHalfStars()) {
        paramCodedOutputStreamMicro.writeInt32(19, getNumHalfStars());
      }
      if (hasNumReviews()) {
        paramCodedOutputStreamMicro.writeInt32(20, getNumReviews());
      }
      if (hasReviewsText()) {
        paramCodedOutputStreamMicro.writeString(21, getReviewsText());
      }
      Iterator localIterator = getReviewSiteList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(22, (EcoutezStructuredResponse.ReviewSite)localIterator.next());
      }
      if (hasClusterId()) {
        paramCodedOutputStreamMicro.writeString(23, getClusterId());
      }
      if (hasMapsUrl()) {
        paramCodedOutputStreamMicro.writeString(24, getMapsUrl());
      }
      if (hasActionDrivingUrl()) {
        paramCodedOutputStreamMicro.writeString(25, getActionDrivingUrl());
      }
      if (hasActionWalkingUrl()) {
        paramCodedOutputStreamMicro.writeString(26, getActionWalkingUrl());
      }
      if (hasActionTransitUrl()) {
        paramCodedOutputStreamMicro.writeString(27, getActionTransitUrl());
      }
      if (hasActionBikingUrl()) {
        paramCodedOutputStreamMicro.writeString(28, getActionBikingUrl());
      }
      if (hasLatSpanDegrees()) {
        paramCodedOutputStreamMicro.writeDouble(29, getLatSpanDegrees());
      }
      if (hasLngSpanDegrees()) {
        paramCodedOutputStreamMicro.writeDouble(30, getLngSpanDegrees());
      }
      if (hasHours()) {
        paramCodedOutputStreamMicro.writeMessage(31, getHours());
      }
      if (hasAddressForMapImageUrl()) {
        paramCodedOutputStreamMicro.writeString(32, getAddressForMapImageUrl());
      }
      if (hasAlias()) {
        paramCodedOutputStreamMicro.writeMessage(33, getAlias());
      }
      if (hasFeatureId()) {
        paramCodedOutputStreamMicro.writeMessage(34, getFeatureId());
      }
      if (hasIsChain()) {
        paramCodedOutputStreamMicro.writeBool(35, getIsChain());
      }
      if (hasChain()) {
        paramCodedOutputStreamMicro.writeMessage(36, getChain());
      }
    }
  }
  
  public static final class EcoutezLocalResults
    extends MessageMicro
  {
    private int actionType_ = 0;
    private int cachedSize = -1;
    private boolean hasActionType;
    private boolean hasMapsUrl;
    private boolean hasPreviewImage;
    private boolean hasPreviewImageUrl;
    private boolean hasTransportationMethod;
    private List<EcoutezStructuredResponse.EcoutezLocalResult> localResult_ = Collections.emptyList();
    private String mapsUrl_ = "";
    private List<EcoutezStructuredResponse.EcoutezLocalResult> origin_ = Collections.emptyList();
    private String previewImageUrl_ = "";
    private ByteStringMicro previewImage_ = ByteStringMicro.EMPTY;
    private int transportationMethod_ = 0;
    
    public EcoutezLocalResults addLocalResult(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
    {
      if (paramEcoutezLocalResult == null) {
        throw new NullPointerException();
      }
      if (this.localResult_.isEmpty()) {
        this.localResult_ = new ArrayList();
      }
      this.localResult_.add(paramEcoutezLocalResult);
      return this;
    }
    
    public EcoutezLocalResults addOrigin(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
    {
      if (paramEcoutezLocalResult == null) {
        throw new NullPointerException();
      }
      if (this.origin_.isEmpty()) {
        this.origin_ = new ArrayList();
      }
      this.origin_.add(paramEcoutezLocalResult);
      return this;
    }
    
    public int getActionType()
    {
      return this.actionType_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EcoutezStructuredResponse.EcoutezLocalResult getLocalResult(int paramInt)
    {
      return (EcoutezStructuredResponse.EcoutezLocalResult)this.localResult_.get(paramInt);
    }
    
    public int getLocalResultCount()
    {
      return this.localResult_.size();
    }
    
    public List<EcoutezStructuredResponse.EcoutezLocalResult> getLocalResultList()
    {
      return this.localResult_;
    }
    
    public String getMapsUrl()
    {
      return this.mapsUrl_;
    }
    
    public EcoutezStructuredResponse.EcoutezLocalResult getOrigin(int paramInt)
    {
      return (EcoutezStructuredResponse.EcoutezLocalResult)this.origin_.get(paramInt);
    }
    
    public int getOriginCount()
    {
      return this.origin_.size();
    }
    
    public List<EcoutezStructuredResponse.EcoutezLocalResult> getOriginList()
    {
      return this.origin_;
    }
    
    public ByteStringMicro getPreviewImage()
    {
      return this.previewImage_;
    }
    
    public String getPreviewImageUrl()
    {
      return this.previewImageUrl_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getLocalResultList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (EcoutezStructuredResponse.EcoutezLocalResult)localIterator1.next());
      }
      if (hasPreviewImageUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getPreviewImageUrl());
      }
      if (hasActionType()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getActionType());
      }
      Iterator localIterator2 = getOriginList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (EcoutezStructuredResponse.EcoutezLocalResult)localIterator2.next());
      }
      if (hasTransportationMethod()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getTransportationMethod());
      }
      if (hasMapsUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getMapsUrl());
      }
      if (hasPreviewImage()) {
        i += CodedOutputStreamMicro.computeBytesSize(7, getPreviewImage());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getTransportationMethod()
    {
      return this.transportationMethod_;
    }
    
    public boolean hasActionType()
    {
      return this.hasActionType;
    }
    
    public boolean hasMapsUrl()
    {
      return this.hasMapsUrl;
    }
    
    public boolean hasPreviewImage()
    {
      return this.hasPreviewImage;
    }
    
    public boolean hasPreviewImageUrl()
    {
      return this.hasPreviewImageUrl;
    }
    
    public boolean hasTransportationMethod()
    {
      return this.hasTransportationMethod;
    }
    
    public EcoutezLocalResults mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult2 = new EcoutezStructuredResponse.EcoutezLocalResult();
          paramCodedInputStreamMicro.readMessage(localEcoutezLocalResult2);
          addLocalResult(localEcoutezLocalResult2);
          break;
        case 18: 
          setPreviewImageUrl(paramCodedInputStreamMicro.readString());
          break;
        case 24: 
          setActionType(paramCodedInputStreamMicro.readInt32());
          break;
        case 34: 
          EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult1 = new EcoutezStructuredResponse.EcoutezLocalResult();
          paramCodedInputStreamMicro.readMessage(localEcoutezLocalResult1);
          addOrigin(localEcoutezLocalResult1);
          break;
        case 40: 
          setTransportationMethod(paramCodedInputStreamMicro.readInt32());
          break;
        case 50: 
          setMapsUrl(paramCodedInputStreamMicro.readString());
          break;
        }
        setPreviewImage(paramCodedInputStreamMicro.readBytes());
      }
    }
    
    public EcoutezLocalResults setActionType(int paramInt)
    {
      this.hasActionType = true;
      this.actionType_ = paramInt;
      return this;
    }
    
    public EcoutezLocalResults setMapsUrl(String paramString)
    {
      this.hasMapsUrl = true;
      this.mapsUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResults setPreviewImage(ByteStringMicro paramByteStringMicro)
    {
      this.hasPreviewImage = true;
      this.previewImage_ = paramByteStringMicro;
      return this;
    }
    
    public EcoutezLocalResults setPreviewImageUrl(String paramString)
    {
      this.hasPreviewImageUrl = true;
      this.previewImageUrl_ = paramString;
      return this;
    }
    
    public EcoutezLocalResults setTransportationMethod(int paramInt)
    {
      this.hasTransportationMethod = true;
      this.transportationMethod_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getLocalResultList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (EcoutezStructuredResponse.EcoutezLocalResult)localIterator1.next());
      }
      if (hasPreviewImageUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getPreviewImageUrl());
      }
      if (hasActionType()) {
        paramCodedOutputStreamMicro.writeInt32(3, getActionType());
      }
      Iterator localIterator2 = getOriginList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (EcoutezStructuredResponse.EcoutezLocalResult)localIterator2.next());
      }
      if (hasTransportationMethod()) {
        paramCodedOutputStreamMicro.writeInt32(5, getTransportationMethod());
      }
      if (hasMapsUrl()) {
        paramCodedOutputStreamMicro.writeString(6, getMapsUrl());
      }
      if (hasPreviewImage()) {
        paramCodedOutputStreamMicro.writeBytes(7, getPreviewImage());
      }
    }
  }
  
  public static final class Fact
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasLabel;
    private boolean hasValue;
    private String label_ = "";
    private String value_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getLabel()
    {
      return this.label_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLabel();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getLabel());
      }
      if (hasValue()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getValue());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getValue()
    {
      return this.value_;
    }
    
    public boolean hasLabel()
    {
      return this.hasLabel;
    }
    
    public boolean hasValue()
    {
      return this.hasValue;
    }
    
    public Fact mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setLabel(paramCodedInputStreamMicro.readString());
          break;
        }
        setValue(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Fact setLabel(String paramString)
    {
      this.hasLabel = true;
      this.label_ = paramString;
      return this;
    }
    
    public Fact setValue(String paramString)
    {
      this.hasValue = true;
      this.value_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLabel()) {
        paramCodedOutputStreamMicro.writeString(1, getLabel());
      }
      if (hasValue()) {
        paramCodedOutputStreamMicro.writeString(2, getValue());
      }
    }
  }
  
  public static final class FeatureIdProto
    extends MessageMicro
  {
    private int cachedSize = -1;
    private long cellId_ = 0L;
    private long fprint_ = 0L;
    private boolean hasCellId;
    private boolean hasFprint;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public long getCellId()
    {
      return this.cellId_;
    }
    
    public long getFprint()
    {
      return this.fprint_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasCellId();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeFixed64Size(1, getCellId());
      }
      if (hasFprint()) {
        i += CodedOutputStreamMicro.computeFixed64Size(2, getFprint());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasCellId()
    {
      return this.hasCellId;
    }
    
    public boolean hasFprint()
    {
      return this.hasFprint;
    }
    
    public FeatureIdProto mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 9: 
          setCellId(paramCodedInputStreamMicro.readFixed64());
          break;
        }
        setFprint(paramCodedInputStreamMicro.readFixed64());
      }
    }
    
    public FeatureIdProto setCellId(long paramLong)
    {
      this.hasCellId = true;
      this.cellId_ = paramLong;
      return this;
    }
    
    public FeatureIdProto setFprint(long paramLong)
    {
      this.hasFprint = true;
      this.fprint_ = paramLong;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasCellId()) {
        paramCodedOutputStreamMicro.writeFixed64(1, getCellId());
      }
      if (hasFprint()) {
        paramCodedOutputStreamMicro.writeFixed64(2, getFprint());
      }
    }
  }
  
  public static final class FinanceResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String chartLink_ = "";
    private String chartUrl_ = "";
    private String company_ = "";
    private EcoutezStructuredResponse.EcnResult ecnResult_ = null;
    private String exchangeCode_ = "";
    private String exchange_ = "";
    private boolean hasChartLink;
    private boolean hasChartUrl;
    private boolean hasCompany;
    private boolean hasEcnResult;
    private boolean hasExchange;
    private boolean hasExchangeCode;
    private boolean hasStockResult;
    private boolean hasSymbol;
    private boolean hasSymbolUrl;
    private EcoutezStructuredResponse.StockResult stockResult_ = null;
    private String symbolUrl_ = "";
    private String symbol_ = "";
    private List<TopLink> topLink_ = Collections.emptyList();
    
    public FinanceResult addTopLink(TopLink paramTopLink)
    {
      if (paramTopLink == null) {
        throw new NullPointerException();
      }
      if (this.topLink_.isEmpty()) {
        this.topLink_ = new ArrayList();
      }
      this.topLink_.add(paramTopLink);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getChartLink()
    {
      return this.chartLink_;
    }
    
    public String getChartUrl()
    {
      return this.chartUrl_;
    }
    
    public String getCompany()
    {
      return this.company_;
    }
    
    public EcoutezStructuredResponse.EcnResult getEcnResult()
    {
      return this.ecnResult_;
    }
    
    public String getExchange()
    {
      return this.exchange_;
    }
    
    public String getExchangeCode()
    {
      return this.exchangeCode_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSymbol();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSymbol());
      }
      if (hasSymbolUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getSymbolUrl());
      }
      if (hasCompany()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getCompany());
      }
      if (hasExchange()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getExchange());
      }
      if (hasExchangeCode()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getExchangeCode());
      }
      Iterator localIterator = getTopLinkList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, (TopLink)localIterator.next());
      }
      if (hasChartUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getChartUrl());
      }
      if (hasChartLink()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getChartLink());
      }
      if (hasStockResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(9, getStockResult());
      }
      if (hasEcnResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(10, getEcnResult());
      }
      this.cachedSize = i;
      return i;
    }
    
    public EcoutezStructuredResponse.StockResult getStockResult()
    {
      return this.stockResult_;
    }
    
    public String getSymbol()
    {
      return this.symbol_;
    }
    
    public String getSymbolUrl()
    {
      return this.symbolUrl_;
    }
    
    public List<TopLink> getTopLinkList()
    {
      return this.topLink_;
    }
    
    public boolean hasChartLink()
    {
      return this.hasChartLink;
    }
    
    public boolean hasChartUrl()
    {
      return this.hasChartUrl;
    }
    
    public boolean hasCompany()
    {
      return this.hasCompany;
    }
    
    public boolean hasEcnResult()
    {
      return this.hasEcnResult;
    }
    
    public boolean hasExchange()
    {
      return this.hasExchange;
    }
    
    public boolean hasExchangeCode()
    {
      return this.hasExchangeCode;
    }
    
    public boolean hasStockResult()
    {
      return this.hasStockResult;
    }
    
    public boolean hasSymbol()
    {
      return this.hasSymbol;
    }
    
    public boolean hasSymbolUrl()
    {
      return this.hasSymbolUrl;
    }
    
    public FinanceResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setSymbol(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setSymbolUrl(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setCompany(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setExchange(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setExchangeCode(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          TopLink localTopLink = new TopLink();
          paramCodedInputStreamMicro.readMessage(localTopLink);
          addTopLink(localTopLink);
          break;
        case 58: 
          setChartUrl(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setChartLink(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          EcoutezStructuredResponse.StockResult localStockResult = new EcoutezStructuredResponse.StockResult();
          paramCodedInputStreamMicro.readMessage(localStockResult);
          setStockResult(localStockResult);
          break;
        }
        EcoutezStructuredResponse.EcnResult localEcnResult = new EcoutezStructuredResponse.EcnResult();
        paramCodedInputStreamMicro.readMessage(localEcnResult);
        setEcnResult(localEcnResult);
      }
    }
    
    public FinanceResult setChartLink(String paramString)
    {
      this.hasChartLink = true;
      this.chartLink_ = paramString;
      return this;
    }
    
    public FinanceResult setChartUrl(String paramString)
    {
      this.hasChartUrl = true;
      this.chartUrl_ = paramString;
      return this;
    }
    
    public FinanceResult setCompany(String paramString)
    {
      this.hasCompany = true;
      this.company_ = paramString;
      return this;
    }
    
    public FinanceResult setEcnResult(EcoutezStructuredResponse.EcnResult paramEcnResult)
    {
      if (paramEcnResult == null) {
        throw new NullPointerException();
      }
      this.hasEcnResult = true;
      this.ecnResult_ = paramEcnResult;
      return this;
    }
    
    public FinanceResult setExchange(String paramString)
    {
      this.hasExchange = true;
      this.exchange_ = paramString;
      return this;
    }
    
    public FinanceResult setExchangeCode(String paramString)
    {
      this.hasExchangeCode = true;
      this.exchangeCode_ = paramString;
      return this;
    }
    
    public FinanceResult setStockResult(EcoutezStructuredResponse.StockResult paramStockResult)
    {
      if (paramStockResult == null) {
        throw new NullPointerException();
      }
      this.hasStockResult = true;
      this.stockResult_ = paramStockResult;
      return this;
    }
    
    public FinanceResult setSymbol(String paramString)
    {
      this.hasSymbol = true;
      this.symbol_ = paramString;
      return this;
    }
    
    public FinanceResult setSymbolUrl(String paramString)
    {
      this.hasSymbolUrl = true;
      this.symbolUrl_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSymbol()) {
        paramCodedOutputStreamMicro.writeString(1, getSymbol());
      }
      if (hasSymbolUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getSymbolUrl());
      }
      if (hasCompany()) {
        paramCodedOutputStreamMicro.writeString(3, getCompany());
      }
      if (hasExchange()) {
        paramCodedOutputStreamMicro.writeString(4, getExchange());
      }
      if (hasExchangeCode()) {
        paramCodedOutputStreamMicro.writeString(5, getExchangeCode());
      }
      Iterator localIterator = getTopLinkList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(6, (TopLink)localIterator.next());
      }
      if (hasChartUrl()) {
        paramCodedOutputStreamMicro.writeString(7, getChartUrl());
      }
      if (hasChartLink()) {
        paramCodedOutputStreamMicro.writeString(8, getChartLink());
      }
      if (hasStockResult()) {
        paramCodedOutputStreamMicro.writeMessage(9, getStockResult());
      }
      if (hasEcnResult()) {
        paramCodedOutputStreamMicro.writeMessage(10, getEcnResult());
      }
    }
    
    public static final class TopLink
      extends MessageMicro
    {
      private int cachedSize = -1;
      private boolean hasTitle;
      private boolean hasUrl;
      private String title_ = "";
      private String url_ = "";
      
      public int getCachedSize()
      {
        if (this.cachedSize < 0) {
          getSerializedSize();
        }
        return this.cachedSize;
      }
      
      public int getSerializedSize()
      {
        boolean bool = hasUrl();
        int i = 0;
        if (bool) {
          i = 0 + CodedOutputStreamMicro.computeStringSize(1, getUrl());
        }
        if (hasTitle()) {
          i += CodedOutputStreamMicro.computeStringSize(2, getTitle());
        }
        this.cachedSize = i;
        return i;
      }
      
      public String getTitle()
      {
        return this.title_;
      }
      
      public String getUrl()
      {
        return this.url_;
      }
      
      public boolean hasTitle()
      {
        return this.hasTitle;
      }
      
      public boolean hasUrl()
      {
        return this.hasUrl;
      }
      
      public TopLink mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          }
          setTitle(paramCodedInputStreamMicro.readString());
        }
      }
      
      public TopLink setTitle(String paramString)
      {
        this.hasTitle = true;
        this.title_ = paramString;
        return this;
      }
      
      public TopLink setUrl(String paramString)
      {
        this.hasUrl = true;
        this.url_ = paramString;
        return this;
      }
      
      public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
        throws IOException
      {
        if (hasUrl()) {
          paramCodedOutputStreamMicro.writeString(1, getUrl());
        }
        if (hasTitle()) {
          paramCodedOutputStreamMicro.writeString(2, getTitle());
        }
      }
    }
  }
  
  public static final class FlightData
    extends MessageMicro
  {
    private String airlineCode_ = "";
    private String airlineName_ = "";
    private String arrivalAirportCode_ = "";
    private String arrivalAirportName_ = "";
    private String arrivalGate_ = "";
    private String arrivalTerminal_ = "";
    private String arrivalTimeActual_ = "";
    private String arrivalTimeScheduled_ = "";
    private String arrivalTimeZone_ = "";
    private int cachedSize = -1;
    private String departureAirportCode_ = "";
    private String departureAirportName_ = "";
    private String departureGate_ = "";
    private String departureTerminal_ = "";
    private String departureTimeActual_ = "";
    private String departureTimeScheduled_ = "";
    private String departureTimeZone_ = "";
    private String flightStatsId_ = "";
    private boolean hasAirlineCode;
    private boolean hasAirlineName;
    private boolean hasArrivalAirportCode;
    private boolean hasArrivalAirportName;
    private boolean hasArrivalGate;
    private boolean hasArrivalTerminal;
    private boolean hasArrivalTimeActual;
    private boolean hasArrivalTimeScheduled;
    private boolean hasArrivalTimeZone;
    private boolean hasDepartureAirportCode;
    private boolean hasDepartureAirportName;
    private boolean hasDepartureGate;
    private boolean hasDepartureTerminal;
    private boolean hasDepartureTimeActual;
    private boolean hasDepartureTimeScheduled;
    private boolean hasDepartureTimeZone;
    private boolean hasFlightStatsId;
    private boolean hasNumber;
    private boolean hasStatusCode;
    private String number_ = "";
    private int statusCode_ = 0;
    
    public String getAirlineCode()
    {
      return this.airlineCode_;
    }
    
    public String getAirlineName()
    {
      return this.airlineName_;
    }
    
    public String getArrivalAirportCode()
    {
      return this.arrivalAirportCode_;
    }
    
    public String getArrivalAirportName()
    {
      return this.arrivalAirportName_;
    }
    
    public String getArrivalGate()
    {
      return this.arrivalGate_;
    }
    
    public String getArrivalTerminal()
    {
      return this.arrivalTerminal_;
    }
    
    public String getArrivalTimeActual()
    {
      return this.arrivalTimeActual_;
    }
    
    public String getArrivalTimeScheduled()
    {
      return this.arrivalTimeScheduled_;
    }
    
    public String getArrivalTimeZone()
    {
      return this.arrivalTimeZone_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDepartureAirportCode()
    {
      return this.departureAirportCode_;
    }
    
    public String getDepartureAirportName()
    {
      return this.departureAirportName_;
    }
    
    public String getDepartureGate()
    {
      return this.departureGate_;
    }
    
    public String getDepartureTerminal()
    {
      return this.departureTerminal_;
    }
    
    public String getDepartureTimeActual()
    {
      return this.departureTimeActual_;
    }
    
    public String getDepartureTimeScheduled()
    {
      return this.departureTimeScheduled_;
    }
    
    public String getDepartureTimeZone()
    {
      return this.departureTimeZone_;
    }
    
    public String getFlightStatsId()
    {
      return this.flightStatsId_;
    }
    
    public String getNumber()
    {
      return this.number_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasAirlineCode();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getAirlineCode());
      }
      if (hasAirlineName()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getAirlineName());
      }
      if (hasNumber()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getNumber());
      }
      if (hasFlightStatsId()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getFlightStatsId());
      }
      if (hasDepartureAirportCode()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getDepartureAirportCode());
      }
      if (hasDepartureAirportName()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getDepartureAirportName());
      }
      if (hasDepartureTerminal()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getDepartureTerminal());
      }
      if (hasDepartureGate()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getDepartureGate());
      }
      if (hasDepartureTimeScheduled()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getDepartureTimeScheduled());
      }
      if (hasDepartureTimeActual()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getDepartureTimeActual());
      }
      if (hasDepartureTimeZone()) {
        i += CodedOutputStreamMicro.computeStringSize(11, getDepartureTimeZone());
      }
      if (hasArrivalAirportCode()) {
        i += CodedOutputStreamMicro.computeStringSize(12, getArrivalAirportCode());
      }
      if (hasArrivalAirportName()) {
        i += CodedOutputStreamMicro.computeStringSize(13, getArrivalAirportName());
      }
      if (hasArrivalTerminal()) {
        i += CodedOutputStreamMicro.computeStringSize(14, getArrivalTerminal());
      }
      if (hasArrivalGate()) {
        i += CodedOutputStreamMicro.computeStringSize(15, getArrivalGate());
      }
      if (hasArrivalTimeScheduled()) {
        i += CodedOutputStreamMicro.computeStringSize(16, getArrivalTimeScheduled());
      }
      if (hasArrivalTimeActual()) {
        i += CodedOutputStreamMicro.computeStringSize(17, getArrivalTimeActual());
      }
      if (hasArrivalTimeZone()) {
        i += CodedOutputStreamMicro.computeStringSize(18, getArrivalTimeZone());
      }
      if (hasStatusCode()) {
        i += CodedOutputStreamMicro.computeInt32Size(19, getStatusCode());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getStatusCode()
    {
      return this.statusCode_;
    }
    
    public boolean hasAirlineCode()
    {
      return this.hasAirlineCode;
    }
    
    public boolean hasAirlineName()
    {
      return this.hasAirlineName;
    }
    
    public boolean hasArrivalAirportCode()
    {
      return this.hasArrivalAirportCode;
    }
    
    public boolean hasArrivalAirportName()
    {
      return this.hasArrivalAirportName;
    }
    
    public boolean hasArrivalGate()
    {
      return this.hasArrivalGate;
    }
    
    public boolean hasArrivalTerminal()
    {
      return this.hasArrivalTerminal;
    }
    
    public boolean hasArrivalTimeActual()
    {
      return this.hasArrivalTimeActual;
    }
    
    public boolean hasArrivalTimeScheduled()
    {
      return this.hasArrivalTimeScheduled;
    }
    
    public boolean hasArrivalTimeZone()
    {
      return this.hasArrivalTimeZone;
    }
    
    public boolean hasDepartureAirportCode()
    {
      return this.hasDepartureAirportCode;
    }
    
    public boolean hasDepartureAirportName()
    {
      return this.hasDepartureAirportName;
    }
    
    public boolean hasDepartureGate()
    {
      return this.hasDepartureGate;
    }
    
    public boolean hasDepartureTerminal()
    {
      return this.hasDepartureTerminal;
    }
    
    public boolean hasDepartureTimeActual()
    {
      return this.hasDepartureTimeActual;
    }
    
    public boolean hasDepartureTimeScheduled()
    {
      return this.hasDepartureTimeScheduled;
    }
    
    public boolean hasDepartureTimeZone()
    {
      return this.hasDepartureTimeZone;
    }
    
    public boolean hasFlightStatsId()
    {
      return this.hasFlightStatsId;
    }
    
    public boolean hasNumber()
    {
      return this.hasNumber;
    }
    
    public boolean hasStatusCode()
    {
      return this.hasStatusCode;
    }
    
    public FlightData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setAirlineCode(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setAirlineName(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setNumber(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setFlightStatsId(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setDepartureAirportCode(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          setDepartureAirportName(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          setDepartureTerminal(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setDepartureGate(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          setDepartureTimeScheduled(paramCodedInputStreamMicro.readString());
          break;
        case 82: 
          setDepartureTimeActual(paramCodedInputStreamMicro.readString());
          break;
        case 90: 
          setDepartureTimeZone(paramCodedInputStreamMicro.readString());
          break;
        case 98: 
          setArrivalAirportCode(paramCodedInputStreamMicro.readString());
          break;
        case 106: 
          setArrivalAirportName(paramCodedInputStreamMicro.readString());
          break;
        case 114: 
          setArrivalTerminal(paramCodedInputStreamMicro.readString());
          break;
        case 122: 
          setArrivalGate(paramCodedInputStreamMicro.readString());
          break;
        case 130: 
          setArrivalTimeScheduled(paramCodedInputStreamMicro.readString());
          break;
        case 138: 
          setArrivalTimeActual(paramCodedInputStreamMicro.readString());
          break;
        case 146: 
          setArrivalTimeZone(paramCodedInputStreamMicro.readString());
          break;
        }
        setStatusCode(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public FlightData setAirlineCode(String paramString)
    {
      this.hasAirlineCode = true;
      this.airlineCode_ = paramString;
      return this;
    }
    
    public FlightData setAirlineName(String paramString)
    {
      this.hasAirlineName = true;
      this.airlineName_ = paramString;
      return this;
    }
    
    public FlightData setArrivalAirportCode(String paramString)
    {
      this.hasArrivalAirportCode = true;
      this.arrivalAirportCode_ = paramString;
      return this;
    }
    
    public FlightData setArrivalAirportName(String paramString)
    {
      this.hasArrivalAirportName = true;
      this.arrivalAirportName_ = paramString;
      return this;
    }
    
    public FlightData setArrivalGate(String paramString)
    {
      this.hasArrivalGate = true;
      this.arrivalGate_ = paramString;
      return this;
    }
    
    public FlightData setArrivalTerminal(String paramString)
    {
      this.hasArrivalTerminal = true;
      this.arrivalTerminal_ = paramString;
      return this;
    }
    
    public FlightData setArrivalTimeActual(String paramString)
    {
      this.hasArrivalTimeActual = true;
      this.arrivalTimeActual_ = paramString;
      return this;
    }
    
    public FlightData setArrivalTimeScheduled(String paramString)
    {
      this.hasArrivalTimeScheduled = true;
      this.arrivalTimeScheduled_ = paramString;
      return this;
    }
    
    public FlightData setArrivalTimeZone(String paramString)
    {
      this.hasArrivalTimeZone = true;
      this.arrivalTimeZone_ = paramString;
      return this;
    }
    
    public FlightData setDepartureAirportCode(String paramString)
    {
      this.hasDepartureAirportCode = true;
      this.departureAirportCode_ = paramString;
      return this;
    }
    
    public FlightData setDepartureAirportName(String paramString)
    {
      this.hasDepartureAirportName = true;
      this.departureAirportName_ = paramString;
      return this;
    }
    
    public FlightData setDepartureGate(String paramString)
    {
      this.hasDepartureGate = true;
      this.departureGate_ = paramString;
      return this;
    }
    
    public FlightData setDepartureTerminal(String paramString)
    {
      this.hasDepartureTerminal = true;
      this.departureTerminal_ = paramString;
      return this;
    }
    
    public FlightData setDepartureTimeActual(String paramString)
    {
      this.hasDepartureTimeActual = true;
      this.departureTimeActual_ = paramString;
      return this;
    }
    
    public FlightData setDepartureTimeScheduled(String paramString)
    {
      this.hasDepartureTimeScheduled = true;
      this.departureTimeScheduled_ = paramString;
      return this;
    }
    
    public FlightData setDepartureTimeZone(String paramString)
    {
      this.hasDepartureTimeZone = true;
      this.departureTimeZone_ = paramString;
      return this;
    }
    
    public FlightData setFlightStatsId(String paramString)
    {
      this.hasFlightStatsId = true;
      this.flightStatsId_ = paramString;
      return this;
    }
    
    public FlightData setNumber(String paramString)
    {
      this.hasNumber = true;
      this.number_ = paramString;
      return this;
    }
    
    public FlightData setStatusCode(int paramInt)
    {
      this.hasStatusCode = true;
      this.statusCode_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAirlineCode()) {
        paramCodedOutputStreamMicro.writeString(1, getAirlineCode());
      }
      if (hasAirlineName()) {
        paramCodedOutputStreamMicro.writeString(2, getAirlineName());
      }
      if (hasNumber()) {
        paramCodedOutputStreamMicro.writeString(3, getNumber());
      }
      if (hasFlightStatsId()) {
        paramCodedOutputStreamMicro.writeString(4, getFlightStatsId());
      }
      if (hasDepartureAirportCode()) {
        paramCodedOutputStreamMicro.writeString(5, getDepartureAirportCode());
      }
      if (hasDepartureAirportName()) {
        paramCodedOutputStreamMicro.writeString(6, getDepartureAirportName());
      }
      if (hasDepartureTerminal()) {
        paramCodedOutputStreamMicro.writeString(7, getDepartureTerminal());
      }
      if (hasDepartureGate()) {
        paramCodedOutputStreamMicro.writeString(8, getDepartureGate());
      }
      if (hasDepartureTimeScheduled()) {
        paramCodedOutputStreamMicro.writeString(9, getDepartureTimeScheduled());
      }
      if (hasDepartureTimeActual()) {
        paramCodedOutputStreamMicro.writeString(10, getDepartureTimeActual());
      }
      if (hasDepartureTimeZone()) {
        paramCodedOutputStreamMicro.writeString(11, getDepartureTimeZone());
      }
      if (hasArrivalAirportCode()) {
        paramCodedOutputStreamMicro.writeString(12, getArrivalAirportCode());
      }
      if (hasArrivalAirportName()) {
        paramCodedOutputStreamMicro.writeString(13, getArrivalAirportName());
      }
      if (hasArrivalTerminal()) {
        paramCodedOutputStreamMicro.writeString(14, getArrivalTerminal());
      }
      if (hasArrivalGate()) {
        paramCodedOutputStreamMicro.writeString(15, getArrivalGate());
      }
      if (hasArrivalTimeScheduled()) {
        paramCodedOutputStreamMicro.writeString(16, getArrivalTimeScheduled());
      }
      if (hasArrivalTimeActual()) {
        paramCodedOutputStreamMicro.writeString(17, getArrivalTimeActual());
      }
      if (hasArrivalTimeZone()) {
        paramCodedOutputStreamMicro.writeString(18, getArrivalTimeZone());
      }
      if (hasStatusCode()) {
        paramCodedOutputStreamMicro.writeInt32(19, getStatusCode());
      }
    }
  }
  
  public static final class FlightResult
    extends MessageMicro
  {
    private String airlineCode_ = "";
    private String airlineName_ = "";
    private int cachedSize = -1;
    private String dateForUrl_ = "";
    private List<EcoutezStructuredResponse.FlightData> flight_ = Collections.emptyList();
    private boolean hasAirlineCode;
    private boolean hasAirlineName;
    private boolean hasDateForUrl;
    private boolean hasNumber;
    private boolean hasUpdateTime;
    private String number_ = "";
    private String updateTime_ = "";
    
    public FlightResult addFlight(EcoutezStructuredResponse.FlightData paramFlightData)
    {
      if (paramFlightData == null) {
        throw new NullPointerException();
      }
      if (this.flight_.isEmpty()) {
        this.flight_ = new ArrayList();
      }
      this.flight_.add(paramFlightData);
      return this;
    }
    
    public String getAirlineCode()
    {
      return this.airlineCode_;
    }
    
    public String getAirlineName()
    {
      return this.airlineName_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDateForUrl()
    {
      return this.dateForUrl_;
    }
    
    public List<EcoutezStructuredResponse.FlightData> getFlightList()
    {
      return this.flight_;
    }
    
    public String getNumber()
    {
      return this.number_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getFlightList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (EcoutezStructuredResponse.FlightData)localIterator.next());
      }
      if (hasAirlineCode()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getAirlineCode());
      }
      if (hasAirlineName()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getAirlineName());
      }
      if (hasNumber()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getNumber());
      }
      if (hasDateForUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getDateForUrl());
      }
      if (hasUpdateTime()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getUpdateTime());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getUpdateTime()
    {
      return this.updateTime_;
    }
    
    public boolean hasAirlineCode()
    {
      return this.hasAirlineCode;
    }
    
    public boolean hasAirlineName()
    {
      return this.hasAirlineName;
    }
    
    public boolean hasDateForUrl()
    {
      return this.hasDateForUrl;
    }
    
    public boolean hasNumber()
    {
      return this.hasNumber;
    }
    
    public boolean hasUpdateTime()
    {
      return this.hasUpdateTime;
    }
    
    public FlightResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EcoutezStructuredResponse.FlightData localFlightData = new EcoutezStructuredResponse.FlightData();
          paramCodedInputStreamMicro.readMessage(localFlightData);
          addFlight(localFlightData);
          break;
        case 18: 
          setAirlineCode(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setAirlineName(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setNumber(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setDateForUrl(paramCodedInputStreamMicro.readString());
          break;
        }
        setUpdateTime(paramCodedInputStreamMicro.readString());
      }
    }
    
    public FlightResult setAirlineCode(String paramString)
    {
      this.hasAirlineCode = true;
      this.airlineCode_ = paramString;
      return this;
    }
    
    public FlightResult setAirlineName(String paramString)
    {
      this.hasAirlineName = true;
      this.airlineName_ = paramString;
      return this;
    }
    
    public FlightResult setDateForUrl(String paramString)
    {
      this.hasDateForUrl = true;
      this.dateForUrl_ = paramString;
      return this;
    }
    
    public FlightResult setNumber(String paramString)
    {
      this.hasNumber = true;
      this.number_ = paramString;
      return this;
    }
    
    public FlightResult setUpdateTime(String paramString)
    {
      this.hasUpdateTime = true;
      this.updateTime_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getFlightList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (EcoutezStructuredResponse.FlightData)localIterator.next());
      }
      if (hasAirlineCode()) {
        paramCodedOutputStreamMicro.writeString(2, getAirlineCode());
      }
      if (hasAirlineName()) {
        paramCodedOutputStreamMicro.writeString(3, getAirlineName());
      }
      if (hasNumber()) {
        paramCodedOutputStreamMicro.writeString(4, getNumber());
      }
      if (hasDateForUrl()) {
        paramCodedOutputStreamMicro.writeString(5, getDateForUrl());
      }
      if (hasUpdateTime()) {
        paramCodedOutputStreamMicro.writeString(6, getUpdateTime());
      }
    }
  }
  
  public static final class HourlyForecast
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<EcoutezStructuredResponse.WeatherState> forecast_ = Collections.emptyList();
    private boolean hasStartHour;
    private boolean hasUtcDate;
    private int startHour_ = 0;
    private String utcDate_ = "";
    
    public HourlyForecast addForecast(EcoutezStructuredResponse.WeatherState paramWeatherState)
    {
      if (paramWeatherState == null) {
        throw new NullPointerException();
      }
      if (this.forecast_.isEmpty()) {
        this.forecast_ = new ArrayList();
      }
      this.forecast_.add(paramWeatherState);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<EcoutezStructuredResponse.WeatherState> getForecastList()
    {
      return this.forecast_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasUtcDate();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getUtcDate());
      }
      if (hasStartHour()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getStartHour());
      }
      Iterator localIterator = getForecastList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (EcoutezStructuredResponse.WeatherState)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getStartHour()
    {
      return this.startHour_;
    }
    
    public String getUtcDate()
    {
      return this.utcDate_;
    }
    
    public boolean hasStartHour()
    {
      return this.hasStartHour;
    }
    
    public boolean hasUtcDate()
    {
      return this.hasUtcDate;
    }
    
    public HourlyForecast mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setUtcDate(paramCodedInputStreamMicro.readString());
          break;
        case 16: 
          setStartHour(paramCodedInputStreamMicro.readInt32());
          break;
        }
        EcoutezStructuredResponse.WeatherState localWeatherState = new EcoutezStructuredResponse.WeatherState();
        paramCodedInputStreamMicro.readMessage(localWeatherState);
        addForecast(localWeatherState);
      }
    }
    
    public HourlyForecast setStartHour(int paramInt)
    {
      this.hasStartHour = true;
      this.startHour_ = paramInt;
      return this;
    }
    
    public HourlyForecast setUtcDate(String paramString)
    {
      this.hasUtcDate = true;
      this.utcDate_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasUtcDate()) {
        paramCodedOutputStreamMicro.writeString(1, getUtcDate());
      }
      if (hasStartHour()) {
        paramCodedOutputStreamMicro.writeInt32(2, getStartHour());
      }
      Iterator localIterator = getForecastList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (EcoutezStructuredResponse.WeatherState)localIterator.next());
      }
    }
  }
  
  public static final class Hours
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String day_ = "";
    private boolean hasDay;
    private List<String> interval_ = Collections.emptyList();
    
    public Hours addInterval(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.interval_.isEmpty()) {
        this.interval_ = new ArrayList();
      }
      this.interval_.add(paramString);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDay()
    {
      return this.day_;
    }
    
    public List<String> getIntervalList()
    {
      return this.interval_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasDay();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getDay());
      }
      int j = 0;
      Iterator localIterator = getIntervalList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getIntervalList().size();
      this.cachedSize = k;
      return k;
    }
    
    public boolean hasDay()
    {
      return this.hasDay;
    }
    
    public Hours mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setDay(paramCodedInputStreamMicro.readString());
          break;
        }
        addInterval(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Hours setDay(String paramString)
    {
      this.hasDay = true;
      this.day_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasDay()) {
        paramCodedOutputStreamMicro.writeString(1, getDay());
      }
      Iterator localIterator = getIntervalList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(2, (String)localIterator.next());
      }
    }
  }
  
  public static final class KnowledgeResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private AttributionProtos.Attribution descriptionAttribution_ = null;
    private String description_ = "";
    private List<EcoutezStructuredResponse.Fact> fact_ = Collections.emptyList();
    private boolean hasDescription;
    private boolean hasDescriptionAttribution;
    private boolean hasTitle;
    private String title_ = "";
    
    public KnowledgeResult addFact(EcoutezStructuredResponse.Fact paramFact)
    {
      if (paramFact == null) {
        throw new NullPointerException();
      }
      if (this.fact_.isEmpty()) {
        this.fact_ = new ArrayList();
      }
      this.fact_.add(paramFact);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDescription()
    {
      return this.description_;
    }
    
    public AttributionProtos.Attribution getDescriptionAttribution()
    {
      return this.descriptionAttribution_;
    }
    
    public List<EcoutezStructuredResponse.Fact> getFactList()
    {
      return this.fact_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasTitle();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getTitle());
      }
      if (hasDescription()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getDescription());
      }
      if (hasDescriptionAttribution()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getDescriptionAttribution());
      }
      Iterator localIterator = getFactList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (EcoutezStructuredResponse.Fact)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public boolean hasDescription()
    {
      return this.hasDescription;
    }
    
    public boolean hasDescriptionAttribution()
    {
      return this.hasDescriptionAttribution;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public KnowledgeResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setDescription(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          AttributionProtos.Attribution localAttribution = new AttributionProtos.Attribution();
          paramCodedInputStreamMicro.readMessage(localAttribution);
          setDescriptionAttribution(localAttribution);
          break;
        }
        EcoutezStructuredResponse.Fact localFact = new EcoutezStructuredResponse.Fact();
        paramCodedInputStreamMicro.readMessage(localFact);
        addFact(localFact);
      }
    }
    
    public KnowledgeResult setDescription(String paramString)
    {
      this.hasDescription = true;
      this.description_ = paramString;
      return this;
    }
    
    public KnowledgeResult setDescriptionAttribution(AttributionProtos.Attribution paramAttribution)
    {
      if (paramAttribution == null) {
        throw new NullPointerException();
      }
      this.hasDescriptionAttribution = true;
      this.descriptionAttribution_ = paramAttribution;
      return this;
    }
    
    public KnowledgeResult setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(1, getTitle());
      }
      if (hasDescription()) {
        paramCodedOutputStreamMicro.writeString(2, getDescription());
      }
      if (hasDescriptionAttribution()) {
        paramCodedOutputStreamMicro.writeMessage(3, getDescriptionAttribution());
      }
      Iterator localIterator = getFactList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (EcoutezStructuredResponse.Fact)localIterator.next());
      }
    }
  }
  
  public static final class Logo
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
    
    public Logo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
    
    public Logo setHeight(int paramInt)
    {
      this.hasHeight = true;
      this.height_ = paramInt;
      return this;
    }
    
    public Logo setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public Logo setWidth(int paramInt)
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
  
  public static final class Match
    extends MessageMicro
  {
    private EcoutezStructuredResponse.BaseballMatch baseball_ = null;
    private String boxUrl_ = "";
    private int cachedSize = -1;
    private int currentPeriodNum_ = 0;
    private String dEPRECATEDFirstTeamLogoUrl_ = "";
    private String dEPRECATEDSecondTeamLogoUrl_ = "";
    private boolean firstIsWinner_ = false;
    private String firstScore_ = "";
    private EcoutezStructuredResponse.Logo firstTeamLogo_ = null;
    private String firstTeamShortName_ = "";
    private String firstTeam_ = "";
    private String forumUrl_ = "";
    private boolean hasBaseball;
    private boolean hasBoxUrl;
    private boolean hasCurrentPeriodNum;
    private boolean hasDEPRECATEDFirstTeamLogoUrl;
    private boolean hasDEPRECATEDSecondTeamLogoUrl;
    private boolean hasFirstIsWinner;
    private boolean hasFirstScore;
    private boolean hasFirstTeam;
    private boolean hasFirstTeamLogo;
    private boolean hasFirstTeamShortName;
    private boolean hasForumUrl;
    private boolean hasIsHidden;
    private boolean hasIsHiddenSecondary;
    private boolean hasLiveStreamUrl;
    private boolean hasLiveUpdateUrl;
    private boolean hasPreviewUrl;
    private boolean hasRecapUrl;
    private boolean hasSecondIsWinner;
    private boolean hasSecondScore;
    private boolean hasSecondTeam;
    private boolean hasSecondTeamLogo;
    private boolean hasSecondTeamShortName;
    private boolean hasStartTime;
    private boolean hasStartTimestamp;
    private boolean hasStatus;
    private boolean hasTicketsUrl;
    private boolean isHiddenSecondary_ = false;
    private boolean isHidden_ = false;
    private String liveStreamUrl_ = "";
    private String liveUpdateUrl_ = "";
    private List<EcoutezStructuredResponse.Period> period_ = Collections.emptyList();
    private String previewUrl_ = "";
    private String recapUrl_ = "";
    private boolean secondIsWinner_ = false;
    private String secondScore_ = "";
    private EcoutezStructuredResponse.Logo secondTeamLogo_ = null;
    private String secondTeamShortName_ = "";
    private String secondTeam_ = "";
    private String startTime_ = "";
    private long startTimestamp_ = 0L;
    private int status_ = 0;
    private String ticketsUrl_ = "";
    
    public Match addPeriod(EcoutezStructuredResponse.Period paramPeriod)
    {
      if (paramPeriod == null) {
        throw new NullPointerException();
      }
      if (this.period_.isEmpty()) {
        this.period_ = new ArrayList();
      }
      this.period_.add(paramPeriod);
      return this;
    }
    
    public EcoutezStructuredResponse.BaseballMatch getBaseball()
    {
      return this.baseball_;
    }
    
    public String getBoxUrl()
    {
      return this.boxUrl_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getCurrentPeriodNum()
    {
      return this.currentPeriodNum_;
    }
    
    public String getDEPRECATEDFirstTeamLogoUrl()
    {
      return this.dEPRECATEDFirstTeamLogoUrl_;
    }
    
    public String getDEPRECATEDSecondTeamLogoUrl()
    {
      return this.dEPRECATEDSecondTeamLogoUrl_;
    }
    
    public boolean getFirstIsWinner()
    {
      return this.firstIsWinner_;
    }
    
    public String getFirstScore()
    {
      return this.firstScore_;
    }
    
    public String getFirstTeam()
    {
      return this.firstTeam_;
    }
    
    public EcoutezStructuredResponse.Logo getFirstTeamLogo()
    {
      return this.firstTeamLogo_;
    }
    
    public String getFirstTeamShortName()
    {
      return this.firstTeamShortName_;
    }
    
    public String getForumUrl()
    {
      return this.forumUrl_;
    }
    
    public boolean getIsHidden()
    {
      return this.isHidden_;
    }
    
    public boolean getIsHiddenSecondary()
    {
      return this.isHiddenSecondary_;
    }
    
    public String getLiveStreamUrl()
    {
      return this.liveStreamUrl_;
    }
    
    public String getLiveUpdateUrl()
    {
      return this.liveUpdateUrl_;
    }
    
    public List<EcoutezStructuredResponse.Period> getPeriodList()
    {
      return this.period_;
    }
    
    public String getPreviewUrl()
    {
      return this.previewUrl_;
    }
    
    public String getRecapUrl()
    {
      return this.recapUrl_;
    }
    
    public boolean getSecondIsWinner()
    {
      return this.secondIsWinner_;
    }
    
    public String getSecondScore()
    {
      return this.secondScore_;
    }
    
    public String getSecondTeam()
    {
      return this.secondTeam_;
    }
    
    public EcoutezStructuredResponse.Logo getSecondTeamLogo()
    {
      return this.secondTeamLogo_;
    }
    
    public String getSecondTeamShortName()
    {
      return this.secondTeamShortName_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasStatus();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getStatus());
      }
      if (hasIsHidden()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getIsHidden());
      }
      if (hasIsHiddenSecondary()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getIsHiddenSecondary());
      }
      if (hasStartTimestamp()) {
        i += CodedOutputStreamMicro.computeInt64Size(4, getStartTimestamp());
      }
      if (hasStartTime()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getStartTime());
      }
      if (hasCurrentPeriodNum()) {
        i += CodedOutputStreamMicro.computeInt32Size(6, getCurrentPeriodNum());
      }
      if (hasFirstTeam()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getFirstTeam());
      }
      if (hasFirstScore()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getFirstScore());
      }
      if (hasFirstIsWinner()) {
        i += CodedOutputStreamMicro.computeBoolSize(9, getFirstIsWinner());
      }
      if (hasSecondTeam()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getSecondTeam());
      }
      if (hasSecondScore()) {
        i += CodedOutputStreamMicro.computeStringSize(11, getSecondScore());
      }
      if (hasSecondIsWinner()) {
        i += CodedOutputStreamMicro.computeBoolSize(12, getSecondIsWinner());
      }
      if (hasRecapUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(13, getRecapUrl());
      }
      if (hasBoxUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(14, getBoxUrl());
      }
      if (hasPreviewUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(15, getPreviewUrl());
      }
      if (hasTicketsUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(16, getTicketsUrl());
      }
      Iterator localIterator = getPeriodList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(17, (EcoutezStructuredResponse.Period)localIterator.next());
      }
      if (hasBaseball()) {
        i += CodedOutputStreamMicro.computeMessageSize(18, getBaseball());
      }
      if (hasDEPRECATEDFirstTeamLogoUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(19, getDEPRECATEDFirstTeamLogoUrl());
      }
      if (hasDEPRECATEDSecondTeamLogoUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(20, getDEPRECATEDSecondTeamLogoUrl());
      }
      if (hasFirstTeamLogo()) {
        i += CodedOutputStreamMicro.computeMessageSize(21, getFirstTeamLogo());
      }
      if (hasSecondTeamLogo()) {
        i += CodedOutputStreamMicro.computeMessageSize(22, getSecondTeamLogo());
      }
      if (hasSecondTeamShortName()) {
        i += CodedOutputStreamMicro.computeStringSize(23, getSecondTeamShortName());
      }
      if (hasFirstTeamShortName()) {
        i += CodedOutputStreamMicro.computeStringSize(24, getFirstTeamShortName());
      }
      if (hasLiveUpdateUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(25, getLiveUpdateUrl());
      }
      if (hasLiveStreamUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(26, getLiveStreamUrl());
      }
      if (hasForumUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(27, getForumUrl());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getStartTime()
    {
      return this.startTime_;
    }
    
    public long getStartTimestamp()
    {
      return this.startTimestamp_;
    }
    
    public int getStatus()
    {
      return this.status_;
    }
    
    public String getTicketsUrl()
    {
      return this.ticketsUrl_;
    }
    
    public boolean hasBaseball()
    {
      return this.hasBaseball;
    }
    
    public boolean hasBoxUrl()
    {
      return this.hasBoxUrl;
    }
    
    public boolean hasCurrentPeriodNum()
    {
      return this.hasCurrentPeriodNum;
    }
    
    public boolean hasDEPRECATEDFirstTeamLogoUrl()
    {
      return this.hasDEPRECATEDFirstTeamLogoUrl;
    }
    
    public boolean hasDEPRECATEDSecondTeamLogoUrl()
    {
      return this.hasDEPRECATEDSecondTeamLogoUrl;
    }
    
    public boolean hasFirstIsWinner()
    {
      return this.hasFirstIsWinner;
    }
    
    public boolean hasFirstScore()
    {
      return this.hasFirstScore;
    }
    
    public boolean hasFirstTeam()
    {
      return this.hasFirstTeam;
    }
    
    public boolean hasFirstTeamLogo()
    {
      return this.hasFirstTeamLogo;
    }
    
    public boolean hasFirstTeamShortName()
    {
      return this.hasFirstTeamShortName;
    }
    
    public boolean hasForumUrl()
    {
      return this.hasForumUrl;
    }
    
    public boolean hasIsHidden()
    {
      return this.hasIsHidden;
    }
    
    public boolean hasIsHiddenSecondary()
    {
      return this.hasIsHiddenSecondary;
    }
    
    public boolean hasLiveStreamUrl()
    {
      return this.hasLiveStreamUrl;
    }
    
    public boolean hasLiveUpdateUrl()
    {
      return this.hasLiveUpdateUrl;
    }
    
    public boolean hasPreviewUrl()
    {
      return this.hasPreviewUrl;
    }
    
    public boolean hasRecapUrl()
    {
      return this.hasRecapUrl;
    }
    
    public boolean hasSecondIsWinner()
    {
      return this.hasSecondIsWinner;
    }
    
    public boolean hasSecondScore()
    {
      return this.hasSecondScore;
    }
    
    public boolean hasSecondTeam()
    {
      return this.hasSecondTeam;
    }
    
    public boolean hasSecondTeamLogo()
    {
      return this.hasSecondTeamLogo;
    }
    
    public boolean hasSecondTeamShortName()
    {
      return this.hasSecondTeamShortName;
    }
    
    public boolean hasStartTime()
    {
      return this.hasStartTime;
    }
    
    public boolean hasStartTimestamp()
    {
      return this.hasStartTimestamp;
    }
    
    public boolean hasStatus()
    {
      return this.hasStatus;
    }
    
    public boolean hasTicketsUrl()
    {
      return this.hasTicketsUrl;
    }
    
    public Match mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setIsHidden(paramCodedInputStreamMicro.readBool());
          break;
        case 24: 
          setIsHiddenSecondary(paramCodedInputStreamMicro.readBool());
          break;
        case 32: 
          setStartTimestamp(paramCodedInputStreamMicro.readInt64());
          break;
        case 42: 
          setStartTime(paramCodedInputStreamMicro.readString());
          break;
        case 48: 
          setCurrentPeriodNum(paramCodedInputStreamMicro.readInt32());
          break;
        case 58: 
          setFirstTeam(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setFirstScore(paramCodedInputStreamMicro.readString());
          break;
        case 72: 
          setFirstIsWinner(paramCodedInputStreamMicro.readBool());
          break;
        case 82: 
          setSecondTeam(paramCodedInputStreamMicro.readString());
          break;
        case 90: 
          setSecondScore(paramCodedInputStreamMicro.readString());
          break;
        case 96: 
          setSecondIsWinner(paramCodedInputStreamMicro.readBool());
          break;
        case 106: 
          setRecapUrl(paramCodedInputStreamMicro.readString());
          break;
        case 114: 
          setBoxUrl(paramCodedInputStreamMicro.readString());
          break;
        case 122: 
          setPreviewUrl(paramCodedInputStreamMicro.readString());
          break;
        case 130: 
          setTicketsUrl(paramCodedInputStreamMicro.readString());
          break;
        case 138: 
          EcoutezStructuredResponse.Period localPeriod = new EcoutezStructuredResponse.Period();
          paramCodedInputStreamMicro.readMessage(localPeriod);
          addPeriod(localPeriod);
          break;
        case 146: 
          EcoutezStructuredResponse.BaseballMatch localBaseballMatch = new EcoutezStructuredResponse.BaseballMatch();
          paramCodedInputStreamMicro.readMessage(localBaseballMatch);
          setBaseball(localBaseballMatch);
          break;
        case 154: 
          setDEPRECATEDFirstTeamLogoUrl(paramCodedInputStreamMicro.readString());
          break;
        case 162: 
          setDEPRECATEDSecondTeamLogoUrl(paramCodedInputStreamMicro.readString());
          break;
        case 170: 
          EcoutezStructuredResponse.Logo localLogo2 = new EcoutezStructuredResponse.Logo();
          paramCodedInputStreamMicro.readMessage(localLogo2);
          setFirstTeamLogo(localLogo2);
          break;
        case 178: 
          EcoutezStructuredResponse.Logo localLogo1 = new EcoutezStructuredResponse.Logo();
          paramCodedInputStreamMicro.readMessage(localLogo1);
          setSecondTeamLogo(localLogo1);
          break;
        case 186: 
          setSecondTeamShortName(paramCodedInputStreamMicro.readString());
          break;
        case 194: 
          setFirstTeamShortName(paramCodedInputStreamMicro.readString());
          break;
        case 202: 
          setLiveUpdateUrl(paramCodedInputStreamMicro.readString());
          break;
        case 210: 
          setLiveStreamUrl(paramCodedInputStreamMicro.readString());
          break;
        }
        setForumUrl(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Match setBaseball(EcoutezStructuredResponse.BaseballMatch paramBaseballMatch)
    {
      if (paramBaseballMatch == null) {
        throw new NullPointerException();
      }
      this.hasBaseball = true;
      this.baseball_ = paramBaseballMatch;
      return this;
    }
    
    public Match setBoxUrl(String paramString)
    {
      this.hasBoxUrl = true;
      this.boxUrl_ = paramString;
      return this;
    }
    
    public Match setCurrentPeriodNum(int paramInt)
    {
      this.hasCurrentPeriodNum = true;
      this.currentPeriodNum_ = paramInt;
      return this;
    }
    
    public Match setDEPRECATEDFirstTeamLogoUrl(String paramString)
    {
      this.hasDEPRECATEDFirstTeamLogoUrl = true;
      this.dEPRECATEDFirstTeamLogoUrl_ = paramString;
      return this;
    }
    
    public Match setDEPRECATEDSecondTeamLogoUrl(String paramString)
    {
      this.hasDEPRECATEDSecondTeamLogoUrl = true;
      this.dEPRECATEDSecondTeamLogoUrl_ = paramString;
      return this;
    }
    
    public Match setFirstIsWinner(boolean paramBoolean)
    {
      this.hasFirstIsWinner = true;
      this.firstIsWinner_ = paramBoolean;
      return this;
    }
    
    public Match setFirstScore(String paramString)
    {
      this.hasFirstScore = true;
      this.firstScore_ = paramString;
      return this;
    }
    
    public Match setFirstTeam(String paramString)
    {
      this.hasFirstTeam = true;
      this.firstTeam_ = paramString;
      return this;
    }
    
    public Match setFirstTeamLogo(EcoutezStructuredResponse.Logo paramLogo)
    {
      if (paramLogo == null) {
        throw new NullPointerException();
      }
      this.hasFirstTeamLogo = true;
      this.firstTeamLogo_ = paramLogo;
      return this;
    }
    
    public Match setFirstTeamShortName(String paramString)
    {
      this.hasFirstTeamShortName = true;
      this.firstTeamShortName_ = paramString;
      return this;
    }
    
    public Match setForumUrl(String paramString)
    {
      this.hasForumUrl = true;
      this.forumUrl_ = paramString;
      return this;
    }
    
    public Match setIsHidden(boolean paramBoolean)
    {
      this.hasIsHidden = true;
      this.isHidden_ = paramBoolean;
      return this;
    }
    
    public Match setIsHiddenSecondary(boolean paramBoolean)
    {
      this.hasIsHiddenSecondary = true;
      this.isHiddenSecondary_ = paramBoolean;
      return this;
    }
    
    public Match setLiveStreamUrl(String paramString)
    {
      this.hasLiveStreamUrl = true;
      this.liveStreamUrl_ = paramString;
      return this;
    }
    
    public Match setLiveUpdateUrl(String paramString)
    {
      this.hasLiveUpdateUrl = true;
      this.liveUpdateUrl_ = paramString;
      return this;
    }
    
    public Match setPreviewUrl(String paramString)
    {
      this.hasPreviewUrl = true;
      this.previewUrl_ = paramString;
      return this;
    }
    
    public Match setRecapUrl(String paramString)
    {
      this.hasRecapUrl = true;
      this.recapUrl_ = paramString;
      return this;
    }
    
    public Match setSecondIsWinner(boolean paramBoolean)
    {
      this.hasSecondIsWinner = true;
      this.secondIsWinner_ = paramBoolean;
      return this;
    }
    
    public Match setSecondScore(String paramString)
    {
      this.hasSecondScore = true;
      this.secondScore_ = paramString;
      return this;
    }
    
    public Match setSecondTeam(String paramString)
    {
      this.hasSecondTeam = true;
      this.secondTeam_ = paramString;
      return this;
    }
    
    public Match setSecondTeamLogo(EcoutezStructuredResponse.Logo paramLogo)
    {
      if (paramLogo == null) {
        throw new NullPointerException();
      }
      this.hasSecondTeamLogo = true;
      this.secondTeamLogo_ = paramLogo;
      return this;
    }
    
    public Match setSecondTeamShortName(String paramString)
    {
      this.hasSecondTeamShortName = true;
      this.secondTeamShortName_ = paramString;
      return this;
    }
    
    public Match setStartTime(String paramString)
    {
      this.hasStartTime = true;
      this.startTime_ = paramString;
      return this;
    }
    
    public Match setStartTimestamp(long paramLong)
    {
      this.hasStartTimestamp = true;
      this.startTimestamp_ = paramLong;
      return this;
    }
    
    public Match setStatus(int paramInt)
    {
      this.hasStatus = true;
      this.status_ = paramInt;
      return this;
    }
    
    public Match setTicketsUrl(String paramString)
    {
      this.hasTicketsUrl = true;
      this.ticketsUrl_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasStatus()) {
        paramCodedOutputStreamMicro.writeInt32(1, getStatus());
      }
      if (hasIsHidden()) {
        paramCodedOutputStreamMicro.writeBool(2, getIsHidden());
      }
      if (hasIsHiddenSecondary()) {
        paramCodedOutputStreamMicro.writeBool(3, getIsHiddenSecondary());
      }
      if (hasStartTimestamp()) {
        paramCodedOutputStreamMicro.writeInt64(4, getStartTimestamp());
      }
      if (hasStartTime()) {
        paramCodedOutputStreamMicro.writeString(5, getStartTime());
      }
      if (hasCurrentPeriodNum()) {
        paramCodedOutputStreamMicro.writeInt32(6, getCurrentPeriodNum());
      }
      if (hasFirstTeam()) {
        paramCodedOutputStreamMicro.writeString(7, getFirstTeam());
      }
      if (hasFirstScore()) {
        paramCodedOutputStreamMicro.writeString(8, getFirstScore());
      }
      if (hasFirstIsWinner()) {
        paramCodedOutputStreamMicro.writeBool(9, getFirstIsWinner());
      }
      if (hasSecondTeam()) {
        paramCodedOutputStreamMicro.writeString(10, getSecondTeam());
      }
      if (hasSecondScore()) {
        paramCodedOutputStreamMicro.writeString(11, getSecondScore());
      }
      if (hasSecondIsWinner()) {
        paramCodedOutputStreamMicro.writeBool(12, getSecondIsWinner());
      }
      if (hasRecapUrl()) {
        paramCodedOutputStreamMicro.writeString(13, getRecapUrl());
      }
      if (hasBoxUrl()) {
        paramCodedOutputStreamMicro.writeString(14, getBoxUrl());
      }
      if (hasPreviewUrl()) {
        paramCodedOutputStreamMicro.writeString(15, getPreviewUrl());
      }
      if (hasTicketsUrl()) {
        paramCodedOutputStreamMicro.writeString(16, getTicketsUrl());
      }
      Iterator localIterator = getPeriodList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(17, (EcoutezStructuredResponse.Period)localIterator.next());
      }
      if (hasBaseball()) {
        paramCodedOutputStreamMicro.writeMessage(18, getBaseball());
      }
      if (hasDEPRECATEDFirstTeamLogoUrl()) {
        paramCodedOutputStreamMicro.writeString(19, getDEPRECATEDFirstTeamLogoUrl());
      }
      if (hasDEPRECATEDSecondTeamLogoUrl()) {
        paramCodedOutputStreamMicro.writeString(20, getDEPRECATEDSecondTeamLogoUrl());
      }
      if (hasFirstTeamLogo()) {
        paramCodedOutputStreamMicro.writeMessage(21, getFirstTeamLogo());
      }
      if (hasSecondTeamLogo()) {
        paramCodedOutputStreamMicro.writeMessage(22, getSecondTeamLogo());
      }
      if (hasSecondTeamShortName()) {
        paramCodedOutputStreamMicro.writeString(23, getSecondTeamShortName());
      }
      if (hasFirstTeamShortName()) {
        paramCodedOutputStreamMicro.writeString(24, getFirstTeamShortName());
      }
      if (hasLiveUpdateUrl()) {
        paramCodedOutputStreamMicro.writeString(25, getLiveUpdateUrl());
      }
      if (hasLiveStreamUrl()) {
        paramCodedOutputStreamMicro.writeString(26, getLiveStreamUrl());
      }
      if (hasForumUrl()) {
        paramCodedOutputStreamMicro.writeString(27, getForumUrl());
      }
    }
  }
  
  public static final class MatchList
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasHasHiddenMatches;
    private boolean hasHasHiddenMatchesSecondary;
    private boolean hasHiddenMatchesSecondary_ = false;
    private boolean hasHiddenMatches_ = false;
    private List<EcoutezStructuredResponse.Match> match_ = Collections.emptyList();
    
    public MatchList addMatch(EcoutezStructuredResponse.Match paramMatch)
    {
      if (paramMatch == null) {
        throw new NullPointerException();
      }
      if (this.match_.isEmpty()) {
        this.match_ = new ArrayList();
      }
      this.match_.add(paramMatch);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public boolean getHasHiddenMatches()
    {
      return this.hasHiddenMatches_;
    }
    
    public boolean getHasHiddenMatchesSecondary()
    {
      return this.hasHiddenMatchesSecondary_;
    }
    
    public List<EcoutezStructuredResponse.Match> getMatchList()
    {
      return this.match_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasHasHiddenMatches();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeBoolSize(1, getHasHiddenMatches());
      }
      if (hasHasHiddenMatchesSecondary()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getHasHiddenMatchesSecondary());
      }
      Iterator localIterator = getMatchList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (EcoutezStructuredResponse.Match)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasHasHiddenMatches()
    {
      return this.hasHasHiddenMatches;
    }
    
    public boolean hasHasHiddenMatchesSecondary()
    {
      return this.hasHasHiddenMatchesSecondary;
    }
    
    public MatchList mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setHasHiddenMatches(paramCodedInputStreamMicro.readBool());
          break;
        case 16: 
          setHasHiddenMatchesSecondary(paramCodedInputStreamMicro.readBool());
          break;
        }
        EcoutezStructuredResponse.Match localMatch = new EcoutezStructuredResponse.Match();
        paramCodedInputStreamMicro.readMessage(localMatch);
        addMatch(localMatch);
      }
    }
    
    public MatchList setHasHiddenMatches(boolean paramBoolean)
    {
      this.hasHasHiddenMatches = true;
      this.hasHiddenMatches_ = paramBoolean;
      return this;
    }
    
    public MatchList setHasHiddenMatchesSecondary(boolean paramBoolean)
    {
      this.hasHasHiddenMatchesSecondary = true;
      this.hasHiddenMatchesSecondary_ = paramBoolean;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasHasHiddenMatches()) {
        paramCodedOutputStreamMicro.writeBool(1, getHasHiddenMatches());
      }
      if (hasHasHiddenMatchesSecondary()) {
        paramCodedOutputStreamMicro.writeBool(2, getHasHiddenMatchesSecondary());
      }
      Iterator localIterator = getMatchList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (EcoutezStructuredResponse.Match)localIterator.next());
      }
    }
  }
  
  public static final class Meaning
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<String> example_ = Collections.emptyList();
    private boolean hasText;
    private boolean hasUrl;
    private String text_ = "";
    private String url_ = "";
    
    public Meaning addExample(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.example_.isEmpty()) {
        this.example_ = new ArrayList();
      }
      this.example_.add(paramString);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<String> getExampleList()
    {
      return this.example_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasText();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getText());
      }
      if (hasUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getUrl());
      }
      int j = 0;
      Iterator localIterator = getExampleList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getExampleList().size();
      this.cachedSize = k;
      return k;
    }
    
    public String getText()
    {
      return this.text_;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public boolean hasText()
    {
      return this.hasText;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public Meaning mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 18: 
          setUrl(paramCodedInputStreamMicro.readString());
          break;
        }
        addExample(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Meaning setText(String paramString)
    {
      this.hasText = true;
      this.text_ = paramString;
      return this;
    }
    
    public Meaning setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasText()) {
        paramCodedOutputStreamMicro.writeString(1, getText());
      }
      if (hasUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getUrl());
      }
      Iterator localIterator = getExampleList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(3, (String)localIterator.next());
      }
    }
  }
  
  public static final class Period
    extends MessageMicro
  {
    private EcoutezStructuredResponse.BaseballPeriod baseball_ = null;
    private int cachedSize = -1;
    private String firstTeamScore_ = "";
    private boolean hasBaseball;
    private boolean hasFirstTeamScore;
    private boolean hasMaxNumber;
    private boolean hasMinutes;
    private boolean hasNumber;
    private boolean hasSecondTeamScore;
    private boolean hasSeconds;
    private boolean hasStatus;
    private boolean hasType;
    private int maxNumber_ = 0;
    private String minutes_ = "";
    private int number_ = 0;
    private String secondTeamScore_ = "";
    private String seconds_ = "";
    private int status_ = 0;
    private int type_ = 0;
    
    public EcoutezStructuredResponse.BaseballPeriod getBaseball()
    {
      return this.baseball_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getFirstTeamScore()
    {
      return this.firstTeamScore_;
    }
    
    public int getMaxNumber()
    {
      return this.maxNumber_;
    }
    
    public String getMinutes()
    {
      return this.minutes_;
    }
    
    public int getNumber()
    {
      return this.number_;
    }
    
    public String getSecondTeamScore()
    {
      return this.secondTeamScore_;
    }
    
    public String getSeconds()
    {
      return this.seconds_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getType());
      }
      if (hasStatus()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getStatus());
      }
      if (hasNumber()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getNumber());
      }
      if (hasMaxNumber()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getMaxNumber());
      }
      if (hasFirstTeamScore()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getFirstTeamScore());
      }
      if (hasSecondTeamScore()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getSecondTeamScore());
      }
      if (hasMinutes()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getMinutes());
      }
      if (hasSeconds()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getSeconds());
      }
      if (hasBaseball()) {
        i += CodedOutputStreamMicro.computeMessageSize(9, getBaseball());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getStatus()
    {
      return this.status_;
    }
    
    public int getType()
    {
      return this.type_;
    }
    
    public boolean hasBaseball()
    {
      return this.hasBaseball;
    }
    
    public boolean hasFirstTeamScore()
    {
      return this.hasFirstTeamScore;
    }
    
    public boolean hasMaxNumber()
    {
      return this.hasMaxNumber;
    }
    
    public boolean hasMinutes()
    {
      return this.hasMinutes;
    }
    
    public boolean hasNumber()
    {
      return this.hasNumber;
    }
    
    public boolean hasSecondTeamScore()
    {
      return this.hasSecondTeamScore;
    }
    
    public boolean hasSeconds()
    {
      return this.hasSeconds;
    }
    
    public boolean hasStatus()
    {
      return this.hasStatus;
    }
    
    public boolean hasType()
    {
      return this.hasType;
    }
    
    public Period mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 16: 
          setStatus(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setNumber(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setMaxNumber(paramCodedInputStreamMicro.readInt32());
          break;
        case 42: 
          setFirstTeamScore(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          setSecondTeamScore(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          setMinutes(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setSeconds(paramCodedInputStreamMicro.readString());
          break;
        }
        EcoutezStructuredResponse.BaseballPeriod localBaseballPeriod = new EcoutezStructuredResponse.BaseballPeriod();
        paramCodedInputStreamMicro.readMessage(localBaseballPeriod);
        setBaseball(localBaseballPeriod);
      }
    }
    
    public Period setBaseball(EcoutezStructuredResponse.BaseballPeriod paramBaseballPeriod)
    {
      if (paramBaseballPeriod == null) {
        throw new NullPointerException();
      }
      this.hasBaseball = true;
      this.baseball_ = paramBaseballPeriod;
      return this;
    }
    
    public Period setFirstTeamScore(String paramString)
    {
      this.hasFirstTeamScore = true;
      this.firstTeamScore_ = paramString;
      return this;
    }
    
    public Period setMaxNumber(int paramInt)
    {
      this.hasMaxNumber = true;
      this.maxNumber_ = paramInt;
      return this;
    }
    
    public Period setMinutes(String paramString)
    {
      this.hasMinutes = true;
      this.minutes_ = paramString;
      return this;
    }
    
    public Period setNumber(int paramInt)
    {
      this.hasNumber = true;
      this.number_ = paramInt;
      return this;
    }
    
    public Period setSecondTeamScore(String paramString)
    {
      this.hasSecondTeamScore = true;
      this.secondTeamScore_ = paramString;
      return this;
    }
    
    public Period setSeconds(String paramString)
    {
      this.hasSeconds = true;
      this.seconds_ = paramString;
      return this;
    }
    
    public Period setStatus(int paramInt)
    {
      this.hasStatus = true;
      this.status_ = paramInt;
      return this;
    }
    
    public Period setType(int paramInt)
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
      if (hasStatus()) {
        paramCodedOutputStreamMicro.writeInt32(2, getStatus());
      }
      if (hasNumber()) {
        paramCodedOutputStreamMicro.writeInt32(3, getNumber());
      }
      if (hasMaxNumber()) {
        paramCodedOutputStreamMicro.writeInt32(4, getMaxNumber());
      }
      if (hasFirstTeamScore()) {
        paramCodedOutputStreamMicro.writeString(5, getFirstTeamScore());
      }
      if (hasSecondTeamScore()) {
        paramCodedOutputStreamMicro.writeString(6, getSecondTeamScore());
      }
      if (hasMinutes()) {
        paramCodedOutputStreamMicro.writeString(7, getMinutes());
      }
      if (hasSeconds()) {
        paramCodedOutputStreamMicro.writeString(8, getSeconds());
      }
      if (hasBaseball()) {
        paramCodedOutputStreamMicro.writeMessage(9, getBaseball());
      }
    }
  }
  
  public static final class PlayoffsStandings
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int firstTeamWins_ = 0;
    private String firstTeam_ = "";
    private boolean hasFirstTeam;
    private boolean hasFirstTeamWins;
    private boolean hasSecondTeam;
    private boolean hasSecondTeamWins;
    private int secondTeamWins_ = 0;
    private String secondTeam_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getFirstTeam()
    {
      return this.firstTeam_;
    }
    
    public int getFirstTeamWins()
    {
      return this.firstTeamWins_;
    }
    
    public String getSecondTeam()
    {
      return this.secondTeam_;
    }
    
    public int getSecondTeamWins()
    {
      return this.secondTeamWins_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasFirstTeam();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getFirstTeam());
      }
      if (hasFirstTeamWins()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getFirstTeamWins());
      }
      if (hasSecondTeam()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getSecondTeam());
      }
      if (hasSecondTeamWins()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getSecondTeamWins());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasFirstTeam()
    {
      return this.hasFirstTeam;
    }
    
    public boolean hasFirstTeamWins()
    {
      return this.hasFirstTeamWins;
    }
    
    public boolean hasSecondTeam()
    {
      return this.hasSecondTeam;
    }
    
    public boolean hasSecondTeamWins()
    {
      return this.hasSecondTeamWins;
    }
    
    public PlayoffsStandings mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setFirstTeam(paramCodedInputStreamMicro.readString());
          break;
        case 16: 
          setFirstTeamWins(paramCodedInputStreamMicro.readInt32());
          break;
        case 26: 
          setSecondTeam(paramCodedInputStreamMicro.readString());
          break;
        }
        setSecondTeamWins(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public PlayoffsStandings setFirstTeam(String paramString)
    {
      this.hasFirstTeam = true;
      this.firstTeam_ = paramString;
      return this;
    }
    
    public PlayoffsStandings setFirstTeamWins(int paramInt)
    {
      this.hasFirstTeamWins = true;
      this.firstTeamWins_ = paramInt;
      return this;
    }
    
    public PlayoffsStandings setSecondTeam(String paramString)
    {
      this.hasSecondTeam = true;
      this.secondTeam_ = paramString;
      return this;
    }
    
    public PlayoffsStandings setSecondTeamWins(int paramInt)
    {
      this.hasSecondTeamWins = true;
      this.secondTeamWins_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasFirstTeam()) {
        paramCodedOutputStreamMicro.writeString(1, getFirstTeam());
      }
      if (hasFirstTeamWins()) {
        paramCodedOutputStreamMicro.writeInt32(2, getFirstTeamWins());
      }
      if (hasSecondTeam()) {
        paramCodedOutputStreamMicro.writeString(3, getSecondTeam());
      }
      if (hasSecondTeamWins()) {
        paramCodedOutputStreamMicro.writeInt32(4, getSecondTeamWins());
      }
    }
  }
  
  public static final class PosMeaning
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasPartOfSpeech;
    private List<EcoutezStructuredResponse.Meaning> meaning_ = Collections.emptyList();
    private String partOfSpeech_ = "";
    
    public PosMeaning addMeaning(EcoutezStructuredResponse.Meaning paramMeaning)
    {
      if (paramMeaning == null) {
        throw new NullPointerException();
      }
      if (this.meaning_.isEmpty()) {
        this.meaning_ = new ArrayList();
      }
      this.meaning_.add(paramMeaning);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<EcoutezStructuredResponse.Meaning> getMeaningList()
    {
      return this.meaning_;
    }
    
    public String getPartOfSpeech()
    {
      return this.partOfSpeech_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasPartOfSpeech();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getPartOfSpeech());
      }
      Iterator localIterator = getMeaningList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, (EcoutezStructuredResponse.Meaning)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasPartOfSpeech()
    {
      return this.hasPartOfSpeech;
    }
    
    public PosMeaning mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setPartOfSpeech(paramCodedInputStreamMicro.readString());
          break;
        }
        EcoutezStructuredResponse.Meaning localMeaning = new EcoutezStructuredResponse.Meaning();
        paramCodedInputStreamMicro.readMessage(localMeaning);
        addMeaning(localMeaning);
      }
    }
    
    public PosMeaning setPartOfSpeech(String paramString)
    {
      this.hasPartOfSpeech = true;
      this.partOfSpeech_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasPartOfSpeech()) {
        paramCodedOutputStreamMicro.writeString(1, getPartOfSpeech());
      }
      Iterator localIterator = getMeaningList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(2, (EcoutezStructuredResponse.Meaning)localIterator.next());
      }
    }
  }
  
  public static final class PublicDataResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String disclaimerText_ = "";
    private String disclaimerUrl_ = "";
    private boolean hasDisclaimerText;
    private boolean hasDisclaimerUrl;
    private boolean hasSourceLabel;
    private boolean hasSourceName;
    private boolean hasSymbolAfter;
    private boolean hasSymbolBefore;
    private boolean hasTime;
    private boolean hasUnit;
    private boolean hasValue;
    private String sourceLabel_ = "";
    private String sourceName_ = "";
    private String symbolAfter_ = "";
    private String symbolBefore_ = "";
    private String time_ = "";
    private String unit_ = "";
    private String value_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDisclaimerText()
    {
      return this.disclaimerText_;
    }
    
    public String getDisclaimerUrl()
    {
      return this.disclaimerUrl_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSymbolBefore();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSymbolBefore());
      }
      if (hasValue()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getValue());
      }
      if (hasSymbolAfter()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getSymbolAfter());
      }
      if (hasUnit()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getUnit());
      }
      if (hasTime()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getTime());
      }
      if (hasSourceLabel()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getSourceLabel());
      }
      if (hasSourceName()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getSourceName());
      }
      if (hasDisclaimerUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getDisclaimerUrl());
      }
      if (hasDisclaimerText()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getDisclaimerText());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSourceLabel()
    {
      return this.sourceLabel_;
    }
    
    public String getSourceName()
    {
      return this.sourceName_;
    }
    
    public String getSymbolAfter()
    {
      return this.symbolAfter_;
    }
    
    public String getSymbolBefore()
    {
      return this.symbolBefore_;
    }
    
    public String getTime()
    {
      return this.time_;
    }
    
    public String getUnit()
    {
      return this.unit_;
    }
    
    public String getValue()
    {
      return this.value_;
    }
    
    public boolean hasDisclaimerText()
    {
      return this.hasDisclaimerText;
    }
    
    public boolean hasDisclaimerUrl()
    {
      return this.hasDisclaimerUrl;
    }
    
    public boolean hasSourceLabel()
    {
      return this.hasSourceLabel;
    }
    
    public boolean hasSourceName()
    {
      return this.hasSourceName;
    }
    
    public boolean hasSymbolAfter()
    {
      return this.hasSymbolAfter;
    }
    
    public boolean hasSymbolBefore()
    {
      return this.hasSymbolBefore;
    }
    
    public boolean hasTime()
    {
      return this.hasTime;
    }
    
    public boolean hasUnit()
    {
      return this.hasUnit;
    }
    
    public boolean hasValue()
    {
      return this.hasValue;
    }
    
    public PublicDataResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setSymbolBefore(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setValue(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setSymbolAfter(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setUnit(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setTime(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          setSourceLabel(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          setSourceName(paramCodedInputStreamMicro.readString());
          break;
        case 66: 
          setDisclaimerUrl(paramCodedInputStreamMicro.readString());
          break;
        }
        setDisclaimerText(paramCodedInputStreamMicro.readString());
      }
    }
    
    public PublicDataResult setDisclaimerText(String paramString)
    {
      this.hasDisclaimerText = true;
      this.disclaimerText_ = paramString;
      return this;
    }
    
    public PublicDataResult setDisclaimerUrl(String paramString)
    {
      this.hasDisclaimerUrl = true;
      this.disclaimerUrl_ = paramString;
      return this;
    }
    
    public PublicDataResult setSourceLabel(String paramString)
    {
      this.hasSourceLabel = true;
      this.sourceLabel_ = paramString;
      return this;
    }
    
    public PublicDataResult setSourceName(String paramString)
    {
      this.hasSourceName = true;
      this.sourceName_ = paramString;
      return this;
    }
    
    public PublicDataResult setSymbolAfter(String paramString)
    {
      this.hasSymbolAfter = true;
      this.symbolAfter_ = paramString;
      return this;
    }
    
    public PublicDataResult setSymbolBefore(String paramString)
    {
      this.hasSymbolBefore = true;
      this.symbolBefore_ = paramString;
      return this;
    }
    
    public PublicDataResult setTime(String paramString)
    {
      this.hasTime = true;
      this.time_ = paramString;
      return this;
    }
    
    public PublicDataResult setUnit(String paramString)
    {
      this.hasUnit = true;
      this.unit_ = paramString;
      return this;
    }
    
    public PublicDataResult setValue(String paramString)
    {
      this.hasValue = true;
      this.value_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSymbolBefore()) {
        paramCodedOutputStreamMicro.writeString(1, getSymbolBefore());
      }
      if (hasValue()) {
        paramCodedOutputStreamMicro.writeString(2, getValue());
      }
      if (hasSymbolAfter()) {
        paramCodedOutputStreamMicro.writeString(3, getSymbolAfter());
      }
      if (hasUnit()) {
        paramCodedOutputStreamMicro.writeString(4, getUnit());
      }
      if (hasTime()) {
        paramCodedOutputStreamMicro.writeString(5, getTime());
      }
      if (hasSourceLabel()) {
        paramCodedOutputStreamMicro.writeString(6, getSourceLabel());
      }
      if (hasSourceName()) {
        paramCodedOutputStreamMicro.writeString(7, getSourceName());
      }
      if (hasDisclaimerUrl()) {
        paramCodedOutputStreamMicro.writeString(8, getDisclaimerUrl());
      }
      if (hasDisclaimerText()) {
        paramCodedOutputStreamMicro.writeString(9, getDisclaimerText());
      }
    }
  }
  
  public static final class RegularSeasonStandings
    extends MessageMicro
  {
    private String associationName_ = "";
    private String associationStanding_ = "";
    private int cachedSize = -1;
    private boolean hasAssociationName;
    private boolean hasAssociationStanding;
    private boolean hasPoints;
    private boolean hasWinPercentage;
    private int points_ = 0;
    private List<Integer> record_ = Collections.emptyList();
    private String winPercentage_ = "";
    
    public RegularSeasonStandings addRecord(int paramInt)
    {
      if (this.record_.isEmpty()) {
        this.record_ = new ArrayList();
      }
      this.record_.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public String getAssociationName()
    {
      return this.associationName_;
    }
    
    public String getAssociationStanding()
    {
      return this.associationStanding_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getPoints()
    {
      return this.points_;
    }
    
    public List<Integer> getRecordList()
    {
      return this.record_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasAssociationName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getAssociationName());
      }
      if (hasAssociationStanding()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getAssociationStanding());
      }
      if (hasPoints()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getPoints());
      }
      int j = 0;
      Iterator localIterator = getRecordList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator.next()).intValue());
      }
      int k = i + j + 1 * getRecordList().size();
      if (hasWinPercentage()) {
        k += CodedOutputStreamMicro.computeStringSize(5, getWinPercentage());
      }
      this.cachedSize = k;
      return k;
    }
    
    public String getWinPercentage()
    {
      return this.winPercentage_;
    }
    
    public boolean hasAssociationName()
    {
      return this.hasAssociationName;
    }
    
    public boolean hasAssociationStanding()
    {
      return this.hasAssociationStanding;
    }
    
    public boolean hasPoints()
    {
      return this.hasPoints;
    }
    
    public boolean hasWinPercentage()
    {
      return this.hasWinPercentage;
    }
    
    public RegularSeasonStandings mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setAssociationName(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setAssociationStanding(paramCodedInputStreamMicro.readString());
          break;
        case 24: 
          setPoints(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          addRecord(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setWinPercentage(paramCodedInputStreamMicro.readString());
      }
    }
    
    public RegularSeasonStandings setAssociationName(String paramString)
    {
      this.hasAssociationName = true;
      this.associationName_ = paramString;
      return this;
    }
    
    public RegularSeasonStandings setAssociationStanding(String paramString)
    {
      this.hasAssociationStanding = true;
      this.associationStanding_ = paramString;
      return this;
    }
    
    public RegularSeasonStandings setPoints(int paramInt)
    {
      this.hasPoints = true;
      this.points_ = paramInt;
      return this;
    }
    
    public RegularSeasonStandings setWinPercentage(String paramString)
    {
      this.hasWinPercentage = true;
      this.winPercentage_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAssociationName()) {
        paramCodedOutputStreamMicro.writeString(1, getAssociationName());
      }
      if (hasAssociationStanding()) {
        paramCodedOutputStreamMicro.writeString(2, getAssociationStanding());
      }
      if (hasPoints()) {
        paramCodedOutputStreamMicro.writeInt32(3, getPoints());
      }
      Iterator localIterator = getRecordList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(4, ((Integer)localIterator.next()).intValue());
      }
      if (hasWinPercentage()) {
        paramCodedOutputStreamMicro.writeString(5, getWinPercentage());
      }
    }
  }
  
  public static final class RelatedSearchResults
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<String> relatedTerm_ = Collections.emptyList();
    
    public RelatedSearchResults addRelatedTerm(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.relatedTerm_.isEmpty()) {
        this.relatedTerm_ = new ArrayList();
      }
      this.relatedTerm_.add(paramString);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<String> getRelatedTermList()
    {
      return this.relatedTerm_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getRelatedTermList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int j = 0 + i + 1 * getRelatedTermList().size();
      this.cachedSize = j;
      return j;
    }
    
    public RelatedSearchResults mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        addRelatedTerm(paramCodedInputStreamMicro.readString());
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getRelatedTermList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(1, (String)localIterator.next());
      }
    }
  }
  
  public static final class ReviewSite
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasName;
    private boolean hasReviewCount;
    private boolean hasUrl;
    private String name_ = "";
    private int reviewCount_ = 0;
    private String url_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getName()
    {
      return this.name_;
    }
    
    public int getReviewCount()
    {
      return this.reviewCount_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getName());
      }
      if (hasUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getUrl());
      }
      if (hasReviewCount()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getReviewCount());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public boolean hasReviewCount()
    {
      return this.hasReviewCount;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public ReviewSite mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setUrl(paramCodedInputStreamMicro.readString());
          break;
        }
        setReviewCount(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public ReviewSite setName(String paramString)
    {
      this.hasName = true;
      this.name_ = paramString;
      return this;
    }
    
    public ReviewSite setReviewCount(int paramInt)
    {
      this.hasReviewCount = true;
      this.reviewCount_ = paramInt;
      return this;
    }
    
    public ReviewSite setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasName()) {
        paramCodedOutputStreamMicro.writeString(1, getName());
      }
      if (hasUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getUrl());
      }
      if (hasReviewCount()) {
        paramCodedOutputStreamMicro.writeInt32(3, getReviewCount());
      }
    }
  }
  
  public static final class SnippetResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String domain_ = "";
    private boolean hasDomain;
    private boolean hasSnippet;
    private boolean hasTitle;
    private boolean hasUrl;
    private String snippet_ = "";
    private String title_ = "";
    private String url_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDomain()
    {
      return this.domain_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasUrl();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getUrl());
      }
      if (hasDomain()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getDomain());
      }
      if (hasTitle()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getTitle());
      }
      if (hasSnippet()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getSnippet());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSnippet()
    {
      return this.snippet_;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public boolean hasDomain()
    {
      return this.hasDomain;
    }
    
    public boolean hasSnippet()
    {
      return this.hasSnippet;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public SnippetResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        case 18: 
          setDomain(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        }
        setSnippet(paramCodedInputStreamMicro.readString());
      }
    }
    
    public SnippetResult setDomain(String paramString)
    {
      this.hasDomain = true;
      this.domain_ = paramString;
      return this;
    }
    
    public SnippetResult setSnippet(String paramString)
    {
      this.hasSnippet = true;
      this.snippet_ = paramString;
      return this;
    }
    
    public SnippetResult setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public SnippetResult setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasUrl()) {
        paramCodedOutputStreamMicro.writeString(1, getUrl());
      }
      if (hasDomain()) {
        paramCodedOutputStreamMicro.writeString(2, getDomain());
      }
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(3, getTitle());
      }
      if (hasSnippet()) {
        paramCodedOutputStreamMicro.writeString(4, getSnippet());
      }
    }
  }
  
  public static final class SnippetResults
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<EcoutezStructuredResponse.SnippetResult> result_ = Collections.emptyList();
    
    public SnippetResults addResult(EcoutezStructuredResponse.SnippetResult paramSnippetResult)
    {
      if (paramSnippetResult == null) {
        throw new NullPointerException();
      }
      if (this.result_.isEmpty()) {
        this.result_ = new ArrayList();
      }
      this.result_.add(paramSnippetResult);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<EcoutezStructuredResponse.SnippetResult> getResultList()
    {
      return this.result_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getResultList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (EcoutezStructuredResponse.SnippetResult)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public SnippetResults mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        EcoutezStructuredResponse.SnippetResult localSnippetResult = new EcoutezStructuredResponse.SnippetResult();
        paramCodedInputStreamMicro.readMessage(localSnippetResult);
        addResult(localSnippetResult);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getResultList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (EcoutezStructuredResponse.SnippetResult)localIterator.next());
      }
    }
  }
  
  public static final class SportsResult
    extends MessageMicro
  {
    private EcoutezStructuredResponse.AssociationData associationData_ = null;
    private int cachedSize = -1;
    private boolean hasAssociationData;
    private boolean hasSportType;
    private boolean hasTeamData;
    private boolean hasTeamVsTeamData;
    private boolean hasTitle;
    private int sportType_ = 0;
    private EcoutezStructuredResponse.TeamData teamData_ = null;
    private EcoutezStructuredResponse.TeamVsTeamData teamVsTeamData_ = null;
    private String title_ = "";
    
    public EcoutezStructuredResponse.AssociationData getAssociationData()
    {
      return this.associationData_;
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
      boolean bool = hasSportType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getSportType());
      }
      if (hasTitle()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getTitle());
      }
      if (hasTeamData()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getTeamData());
      }
      if (hasTeamVsTeamData()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getTeamVsTeamData());
      }
      if (hasAssociationData()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getAssociationData());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getSportType()
    {
      return this.sportType_;
    }
    
    public EcoutezStructuredResponse.TeamData getTeamData()
    {
      return this.teamData_;
    }
    
    public EcoutezStructuredResponse.TeamVsTeamData getTeamVsTeamData()
    {
      return this.teamVsTeamData_;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public boolean hasAssociationData()
    {
      return this.hasAssociationData;
    }
    
    public boolean hasSportType()
    {
      return this.hasSportType;
    }
    
    public boolean hasTeamData()
    {
      return this.hasTeamData;
    }
    
    public boolean hasTeamVsTeamData()
    {
      return this.hasTeamVsTeamData;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public SportsResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setSportType(paramCodedInputStreamMicro.readInt32());
          break;
        case 18: 
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          EcoutezStructuredResponse.TeamData localTeamData = new EcoutezStructuredResponse.TeamData();
          paramCodedInputStreamMicro.readMessage(localTeamData);
          setTeamData(localTeamData);
          break;
        case 34: 
          EcoutezStructuredResponse.TeamVsTeamData localTeamVsTeamData = new EcoutezStructuredResponse.TeamVsTeamData();
          paramCodedInputStreamMicro.readMessage(localTeamVsTeamData);
          setTeamVsTeamData(localTeamVsTeamData);
          break;
        }
        EcoutezStructuredResponse.AssociationData localAssociationData = new EcoutezStructuredResponse.AssociationData();
        paramCodedInputStreamMicro.readMessage(localAssociationData);
        setAssociationData(localAssociationData);
      }
    }
    
    public SportsResult setAssociationData(EcoutezStructuredResponse.AssociationData paramAssociationData)
    {
      if (paramAssociationData == null) {
        throw new NullPointerException();
      }
      this.hasAssociationData = true;
      this.associationData_ = paramAssociationData;
      return this;
    }
    
    public SportsResult setSportType(int paramInt)
    {
      this.hasSportType = true;
      this.sportType_ = paramInt;
      return this;
    }
    
    public SportsResult setTeamData(EcoutezStructuredResponse.TeamData paramTeamData)
    {
      if (paramTeamData == null) {
        throw new NullPointerException();
      }
      this.hasTeamData = true;
      this.teamData_ = paramTeamData;
      return this;
    }
    
    public SportsResult setTeamVsTeamData(EcoutezStructuredResponse.TeamVsTeamData paramTeamVsTeamData)
    {
      if (paramTeamVsTeamData == null) {
        throw new NullPointerException();
      }
      this.hasTeamVsTeamData = true;
      this.teamVsTeamData_ = paramTeamVsTeamData;
      return this;
    }
    
    public SportsResult setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSportType()) {
        paramCodedOutputStreamMicro.writeInt32(1, getSportType());
      }
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(2, getTitle());
      }
      if (hasTeamData()) {
        paramCodedOutputStreamMicro.writeMessage(3, getTeamData());
      }
      if (hasTeamVsTeamData()) {
        paramCodedOutputStreamMicro.writeMessage(4, getTeamVsTeamData());
      }
      if (hasAssociationData()) {
        paramCodedOutputStreamMicro.writeMessage(5, getAssociationData());
      }
    }
  }
  
  public static final class StockResult
    extends MessageMicro
  {
    private String avgVolumeText_ = "";
    private String avgVolume_ = "";
    private int cachedSize = -1;
    private String delay_ = "";
    private String disclaimerUrl_ = "";
    private String disclaimer_ = "";
    private boolean hasAvgVolume;
    private boolean hasAvgVolumeText;
    private boolean hasDelay;
    private boolean hasDisclaimer;
    private boolean hasDisclaimerUrl;
    private boolean hasHighPrice;
    private boolean hasHighText;
    private boolean hasLastChangeTime;
    private boolean hasLastPrice;
    private boolean hasLowPrice;
    private boolean hasLowText;
    private boolean hasMarketCap;
    private boolean hasMarketCapText;
    private boolean hasOpenPrice;
    private boolean hasOpenText;
    private boolean hasPriceChange;
    private boolean hasPricePercentChange;
    private boolean hasVolume;
    private boolean hasVolumeText;
    private float highPrice_ = 0.0F;
    private String highText_ = "";
    private String lastChangeTime_ = "";
    private float lastPrice_ = 0.0F;
    private float lowPrice_ = 0.0F;
    private String lowText_ = "";
    private String marketCapText_ = "";
    private String marketCap_ = "";
    private float openPrice_ = 0.0F;
    private String openText_ = "";
    private float priceChange_ = 0.0F;
    private float pricePercentChange_ = 0.0F;
    private String volumeText_ = "";
    private String volume_ = "";
    
    public String getAvgVolume()
    {
      return this.avgVolume_;
    }
    
    public String getAvgVolumeText()
    {
      return this.avgVolumeText_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDelay()
    {
      return this.delay_;
    }
    
    public String getDisclaimer()
    {
      return this.disclaimer_;
    }
    
    public String getDisclaimerUrl()
    {
      return this.disclaimerUrl_;
    }
    
    public float getHighPrice()
    {
      return this.highPrice_;
    }
    
    public String getHighText()
    {
      return this.highText_;
    }
    
    public String getLastChangeTime()
    {
      return this.lastChangeTime_;
    }
    
    public float getLastPrice()
    {
      return this.lastPrice_;
    }
    
    public float getLowPrice()
    {
      return this.lowPrice_;
    }
    
    public String getLowText()
    {
      return this.lowText_;
    }
    
    public String getMarketCap()
    {
      return this.marketCap_;
    }
    
    public String getMarketCapText()
    {
      return this.marketCapText_;
    }
    
    public float getOpenPrice()
    {
      return this.openPrice_;
    }
    
    public String getOpenText()
    {
      return this.openText_;
    }
    
    public float getPriceChange()
    {
      return this.priceChange_;
    }
    
    public float getPricePercentChange()
    {
      return this.pricePercentChange_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLastPrice();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeFloatSize(1, getLastPrice());
      }
      if (hasPriceChange()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getPriceChange());
      }
      if (hasPricePercentChange()) {
        i += CodedOutputStreamMicro.computeFloatSize(3, getPricePercentChange());
      }
      if (hasLastChangeTime()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getLastChangeTime());
      }
      if (hasOpenPrice()) {
        i += CodedOutputStreamMicro.computeFloatSize(5, getOpenPrice());
      }
      if (hasOpenText()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getOpenText());
      }
      if (hasHighPrice()) {
        i += CodedOutputStreamMicro.computeFloatSize(7, getHighPrice());
      }
      if (hasHighText()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getHighText());
      }
      if (hasLowPrice()) {
        i += CodedOutputStreamMicro.computeFloatSize(9, getLowPrice());
      }
      if (hasLowText()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getLowText());
      }
      if (hasVolume()) {
        i += CodedOutputStreamMicro.computeStringSize(11, getVolume());
      }
      if (hasVolumeText()) {
        i += CodedOutputStreamMicro.computeStringSize(12, getVolumeText());
      }
      if (hasAvgVolume()) {
        i += CodedOutputStreamMicro.computeStringSize(13, getAvgVolume());
      }
      if (hasAvgVolumeText()) {
        i += CodedOutputStreamMicro.computeStringSize(14, getAvgVolumeText());
      }
      if (hasMarketCap()) {
        i += CodedOutputStreamMicro.computeStringSize(15, getMarketCap());
      }
      if (hasMarketCapText()) {
        i += CodedOutputStreamMicro.computeStringSize(16, getMarketCapText());
      }
      if (hasDelay()) {
        i += CodedOutputStreamMicro.computeStringSize(17, getDelay());
      }
      if (hasDisclaimer()) {
        i += CodedOutputStreamMicro.computeStringSize(18, getDisclaimer());
      }
      if (hasDisclaimerUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(19, getDisclaimerUrl());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getVolume()
    {
      return this.volume_;
    }
    
    public String getVolumeText()
    {
      return this.volumeText_;
    }
    
    public boolean hasAvgVolume()
    {
      return this.hasAvgVolume;
    }
    
    public boolean hasAvgVolumeText()
    {
      return this.hasAvgVolumeText;
    }
    
    public boolean hasDelay()
    {
      return this.hasDelay;
    }
    
    public boolean hasDisclaimer()
    {
      return this.hasDisclaimer;
    }
    
    public boolean hasDisclaimerUrl()
    {
      return this.hasDisclaimerUrl;
    }
    
    public boolean hasHighPrice()
    {
      return this.hasHighPrice;
    }
    
    public boolean hasHighText()
    {
      return this.hasHighText;
    }
    
    public boolean hasLastChangeTime()
    {
      return this.hasLastChangeTime;
    }
    
    public boolean hasLastPrice()
    {
      return this.hasLastPrice;
    }
    
    public boolean hasLowPrice()
    {
      return this.hasLowPrice;
    }
    
    public boolean hasLowText()
    {
      return this.hasLowText;
    }
    
    public boolean hasMarketCap()
    {
      return this.hasMarketCap;
    }
    
    public boolean hasMarketCapText()
    {
      return this.hasMarketCapText;
    }
    
    public boolean hasOpenPrice()
    {
      return this.hasOpenPrice;
    }
    
    public boolean hasOpenText()
    {
      return this.hasOpenText;
    }
    
    public boolean hasPriceChange()
    {
      return this.hasPriceChange;
    }
    
    public boolean hasPricePercentChange()
    {
      return this.hasPricePercentChange;
    }
    
    public boolean hasVolume()
    {
      return this.hasVolume;
    }
    
    public boolean hasVolumeText()
    {
      return this.hasVolumeText;
    }
    
    public StockResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setLastPrice(paramCodedInputStreamMicro.readFloat());
          break;
        case 21: 
          setPriceChange(paramCodedInputStreamMicro.readFloat());
          break;
        case 29: 
          setPricePercentChange(paramCodedInputStreamMicro.readFloat());
          break;
        case 34: 
          setLastChangeTime(paramCodedInputStreamMicro.readString());
          break;
        case 45: 
          setOpenPrice(paramCodedInputStreamMicro.readFloat());
          break;
        case 50: 
          setOpenText(paramCodedInputStreamMicro.readString());
          break;
        case 61: 
          setHighPrice(paramCodedInputStreamMicro.readFloat());
          break;
        case 66: 
          setHighText(paramCodedInputStreamMicro.readString());
          break;
        case 77: 
          setLowPrice(paramCodedInputStreamMicro.readFloat());
          break;
        case 82: 
          setLowText(paramCodedInputStreamMicro.readString());
          break;
        case 90: 
          setVolume(paramCodedInputStreamMicro.readString());
          break;
        case 98: 
          setVolumeText(paramCodedInputStreamMicro.readString());
          break;
        case 106: 
          setAvgVolume(paramCodedInputStreamMicro.readString());
          break;
        case 114: 
          setAvgVolumeText(paramCodedInputStreamMicro.readString());
          break;
        case 122: 
          setMarketCap(paramCodedInputStreamMicro.readString());
          break;
        case 130: 
          setMarketCapText(paramCodedInputStreamMicro.readString());
          break;
        case 138: 
          setDelay(paramCodedInputStreamMicro.readString());
          break;
        case 146: 
          setDisclaimer(paramCodedInputStreamMicro.readString());
          break;
        }
        setDisclaimerUrl(paramCodedInputStreamMicro.readString());
      }
    }
    
    public StockResult setAvgVolume(String paramString)
    {
      this.hasAvgVolume = true;
      this.avgVolume_ = paramString;
      return this;
    }
    
    public StockResult setAvgVolumeText(String paramString)
    {
      this.hasAvgVolumeText = true;
      this.avgVolumeText_ = paramString;
      return this;
    }
    
    public StockResult setDelay(String paramString)
    {
      this.hasDelay = true;
      this.delay_ = paramString;
      return this;
    }
    
    public StockResult setDisclaimer(String paramString)
    {
      this.hasDisclaimer = true;
      this.disclaimer_ = paramString;
      return this;
    }
    
    public StockResult setDisclaimerUrl(String paramString)
    {
      this.hasDisclaimerUrl = true;
      this.disclaimerUrl_ = paramString;
      return this;
    }
    
    public StockResult setHighPrice(float paramFloat)
    {
      this.hasHighPrice = true;
      this.highPrice_ = paramFloat;
      return this;
    }
    
    public StockResult setHighText(String paramString)
    {
      this.hasHighText = true;
      this.highText_ = paramString;
      return this;
    }
    
    public StockResult setLastChangeTime(String paramString)
    {
      this.hasLastChangeTime = true;
      this.lastChangeTime_ = paramString;
      return this;
    }
    
    public StockResult setLastPrice(float paramFloat)
    {
      this.hasLastPrice = true;
      this.lastPrice_ = paramFloat;
      return this;
    }
    
    public StockResult setLowPrice(float paramFloat)
    {
      this.hasLowPrice = true;
      this.lowPrice_ = paramFloat;
      return this;
    }
    
    public StockResult setLowText(String paramString)
    {
      this.hasLowText = true;
      this.lowText_ = paramString;
      return this;
    }
    
    public StockResult setMarketCap(String paramString)
    {
      this.hasMarketCap = true;
      this.marketCap_ = paramString;
      return this;
    }
    
    public StockResult setMarketCapText(String paramString)
    {
      this.hasMarketCapText = true;
      this.marketCapText_ = paramString;
      return this;
    }
    
    public StockResult setOpenPrice(float paramFloat)
    {
      this.hasOpenPrice = true;
      this.openPrice_ = paramFloat;
      return this;
    }
    
    public StockResult setOpenText(String paramString)
    {
      this.hasOpenText = true;
      this.openText_ = paramString;
      return this;
    }
    
    public StockResult setPriceChange(float paramFloat)
    {
      this.hasPriceChange = true;
      this.priceChange_ = paramFloat;
      return this;
    }
    
    public StockResult setPricePercentChange(float paramFloat)
    {
      this.hasPricePercentChange = true;
      this.pricePercentChange_ = paramFloat;
      return this;
    }
    
    public StockResult setVolume(String paramString)
    {
      this.hasVolume = true;
      this.volume_ = paramString;
      return this;
    }
    
    public StockResult setVolumeText(String paramString)
    {
      this.hasVolumeText = true;
      this.volumeText_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLastPrice()) {
        paramCodedOutputStreamMicro.writeFloat(1, getLastPrice());
      }
      if (hasPriceChange()) {
        paramCodedOutputStreamMicro.writeFloat(2, getPriceChange());
      }
      if (hasPricePercentChange()) {
        paramCodedOutputStreamMicro.writeFloat(3, getPricePercentChange());
      }
      if (hasLastChangeTime()) {
        paramCodedOutputStreamMicro.writeString(4, getLastChangeTime());
      }
      if (hasOpenPrice()) {
        paramCodedOutputStreamMicro.writeFloat(5, getOpenPrice());
      }
      if (hasOpenText()) {
        paramCodedOutputStreamMicro.writeString(6, getOpenText());
      }
      if (hasHighPrice()) {
        paramCodedOutputStreamMicro.writeFloat(7, getHighPrice());
      }
      if (hasHighText()) {
        paramCodedOutputStreamMicro.writeString(8, getHighText());
      }
      if (hasLowPrice()) {
        paramCodedOutputStreamMicro.writeFloat(9, getLowPrice());
      }
      if (hasLowText()) {
        paramCodedOutputStreamMicro.writeString(10, getLowText());
      }
      if (hasVolume()) {
        paramCodedOutputStreamMicro.writeString(11, getVolume());
      }
      if (hasVolumeText()) {
        paramCodedOutputStreamMicro.writeString(12, getVolumeText());
      }
      if (hasAvgVolume()) {
        paramCodedOutputStreamMicro.writeString(13, getAvgVolume());
      }
      if (hasAvgVolumeText()) {
        paramCodedOutputStreamMicro.writeString(14, getAvgVolumeText());
      }
      if (hasMarketCap()) {
        paramCodedOutputStreamMicro.writeString(15, getMarketCap());
      }
      if (hasMarketCapText()) {
        paramCodedOutputStreamMicro.writeString(16, getMarketCapText());
      }
      if (hasDelay()) {
        paramCodedOutputStreamMicro.writeString(17, getDelay());
      }
      if (hasDisclaimer()) {
        paramCodedOutputStreamMicro.writeString(18, getDisclaimer());
      }
      if (hasDisclaimerUrl()) {
        paramCodedOutputStreamMicro.writeString(19, getDisclaimerUrl());
      }
    }
  }
  
  public static final class Synonym
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasPartOfSpeech;
    private String partOfSpeech_ = "";
    private List<String> synonym_ = Collections.emptyList();
    
    public Synonym addSynonym(String paramString)
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (this.synonym_.isEmpty()) {
        this.synonym_ = new ArrayList();
      }
      this.synonym_.add(paramString);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getPartOfSpeech()
    {
      return this.partOfSpeech_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasPartOfSpeech();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getPartOfSpeech());
      }
      int j = 0;
      Iterator localIterator = getSynonymList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeStringSizeNoTag((String)localIterator.next());
      }
      int k = i + j + 1 * getSynonymList().size();
      this.cachedSize = k;
      return k;
    }
    
    public List<String> getSynonymList()
    {
      return this.synonym_;
    }
    
    public boolean hasPartOfSpeech()
    {
      return this.hasPartOfSpeech;
    }
    
    public Synonym mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setPartOfSpeech(paramCodedInputStreamMicro.readString());
          break;
        }
        addSynonym(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Synonym setPartOfSpeech(String paramString)
    {
      this.hasPartOfSpeech = true;
      this.partOfSpeech_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasPartOfSpeech()) {
        paramCodedOutputStreamMicro.writeString(1, getPartOfSpeech());
      }
      Iterator localIterator = getSynonymList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeString(2, (String)localIterator.next());
      }
    }
  }
  
  public static final class TeamData
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String dEPRECATEDLogoUrl_ = "";
    private boolean hasDEPRECATEDLogoUrl;
    private boolean hasInProgressMatch;
    private boolean hasLastMatch;
    private boolean hasLogo;
    private boolean hasMatchList;
    private boolean hasName;
    private boolean hasNextMatch;
    private boolean hasPlayoffsStandings;
    private boolean hasRegularSeasonStandings;
    private boolean hasShortName;
    private EcoutezStructuredResponse.Match inProgressMatch_ = null;
    private EcoutezStructuredResponse.Match lastMatch_ = null;
    private EcoutezStructuredResponse.Logo logo_ = null;
    private EcoutezStructuredResponse.MatchList matchList_ = null;
    private String name_ = "";
    private EcoutezStructuredResponse.Match nextMatch_ = null;
    private EcoutezStructuredResponse.PlayoffsStandings playoffsStandings_ = null;
    private EcoutezStructuredResponse.RegularSeasonStandings regularSeasonStandings_ = null;
    private String shortName_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDEPRECATEDLogoUrl()
    {
      return this.dEPRECATEDLogoUrl_;
    }
    
    public EcoutezStructuredResponse.Match getInProgressMatch()
    {
      return this.inProgressMatch_;
    }
    
    public EcoutezStructuredResponse.Match getLastMatch()
    {
      return this.lastMatch_;
    }
    
    public EcoutezStructuredResponse.Logo getLogo()
    {
      return this.logo_;
    }
    
    public EcoutezStructuredResponse.MatchList getMatchList()
    {
      return this.matchList_;
    }
    
    public String getName()
    {
      return this.name_;
    }
    
    public EcoutezStructuredResponse.Match getNextMatch()
    {
      return this.nextMatch_;
    }
    
    public EcoutezStructuredResponse.PlayoffsStandings getPlayoffsStandings()
    {
      return this.playoffsStandings_;
    }
    
    public EcoutezStructuredResponse.RegularSeasonStandings getRegularSeasonStandings()
    {
      return this.regularSeasonStandings_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getName());
      }
      if (hasRegularSeasonStandings()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getRegularSeasonStandings());
      }
      if (hasPlayoffsStandings()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getPlayoffsStandings());
      }
      if (hasMatchList()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getMatchList());
      }
      if (hasLastMatch()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getLastMatch());
      }
      if (hasNextMatch()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getNextMatch());
      }
      if (hasInProgressMatch()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, getInProgressMatch());
      }
      if (hasDEPRECATEDLogoUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getDEPRECATEDLogoUrl());
      }
      if (hasLogo()) {
        i += CodedOutputStreamMicro.computeMessageSize(9, getLogo());
      }
      if (hasShortName()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getShortName());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getShortName()
    {
      return this.shortName_;
    }
    
    public boolean hasDEPRECATEDLogoUrl()
    {
      return this.hasDEPRECATEDLogoUrl;
    }
    
    public boolean hasInProgressMatch()
    {
      return this.hasInProgressMatch;
    }
    
    public boolean hasLastMatch()
    {
      return this.hasLastMatch;
    }
    
    public boolean hasLogo()
    {
      return this.hasLogo;
    }
    
    public boolean hasMatchList()
    {
      return this.hasMatchList;
    }
    
    public boolean hasName()
    {
      return this.hasName;
    }
    
    public boolean hasNextMatch()
    {
      return this.hasNextMatch;
    }
    
    public boolean hasPlayoffsStandings()
    {
      return this.hasPlayoffsStandings;
    }
    
    public boolean hasRegularSeasonStandings()
    {
      return this.hasRegularSeasonStandings;
    }
    
    public boolean hasShortName()
    {
      return this.hasShortName;
    }
    
    public TeamData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EcoutezStructuredResponse.RegularSeasonStandings localRegularSeasonStandings = new EcoutezStructuredResponse.RegularSeasonStandings();
          paramCodedInputStreamMicro.readMessage(localRegularSeasonStandings);
          setRegularSeasonStandings(localRegularSeasonStandings);
          break;
        case 26: 
          EcoutezStructuredResponse.PlayoffsStandings localPlayoffsStandings = new EcoutezStructuredResponse.PlayoffsStandings();
          paramCodedInputStreamMicro.readMessage(localPlayoffsStandings);
          setPlayoffsStandings(localPlayoffsStandings);
          break;
        case 34: 
          EcoutezStructuredResponse.MatchList localMatchList = new EcoutezStructuredResponse.MatchList();
          paramCodedInputStreamMicro.readMessage(localMatchList);
          setMatchList(localMatchList);
          break;
        case 42: 
          EcoutezStructuredResponse.Match localMatch3 = new EcoutezStructuredResponse.Match();
          paramCodedInputStreamMicro.readMessage(localMatch3);
          setLastMatch(localMatch3);
          break;
        case 50: 
          EcoutezStructuredResponse.Match localMatch2 = new EcoutezStructuredResponse.Match();
          paramCodedInputStreamMicro.readMessage(localMatch2);
          setNextMatch(localMatch2);
          break;
        case 58: 
          EcoutezStructuredResponse.Match localMatch1 = new EcoutezStructuredResponse.Match();
          paramCodedInputStreamMicro.readMessage(localMatch1);
          setInProgressMatch(localMatch1);
          break;
        case 66: 
          setDEPRECATEDLogoUrl(paramCodedInputStreamMicro.readString());
          break;
        case 74: 
          EcoutezStructuredResponse.Logo localLogo = new EcoutezStructuredResponse.Logo();
          paramCodedInputStreamMicro.readMessage(localLogo);
          setLogo(localLogo);
          break;
        }
        setShortName(paramCodedInputStreamMicro.readString());
      }
    }
    
    public TeamData setDEPRECATEDLogoUrl(String paramString)
    {
      this.hasDEPRECATEDLogoUrl = true;
      this.dEPRECATEDLogoUrl_ = paramString;
      return this;
    }
    
    public TeamData setInProgressMatch(EcoutezStructuredResponse.Match paramMatch)
    {
      if (paramMatch == null) {
        throw new NullPointerException();
      }
      this.hasInProgressMatch = true;
      this.inProgressMatch_ = paramMatch;
      return this;
    }
    
    public TeamData setLastMatch(EcoutezStructuredResponse.Match paramMatch)
    {
      if (paramMatch == null) {
        throw new NullPointerException();
      }
      this.hasLastMatch = true;
      this.lastMatch_ = paramMatch;
      return this;
    }
    
    public TeamData setLogo(EcoutezStructuredResponse.Logo paramLogo)
    {
      if (paramLogo == null) {
        throw new NullPointerException();
      }
      this.hasLogo = true;
      this.logo_ = paramLogo;
      return this;
    }
    
    public TeamData setMatchList(EcoutezStructuredResponse.MatchList paramMatchList)
    {
      if (paramMatchList == null) {
        throw new NullPointerException();
      }
      this.hasMatchList = true;
      this.matchList_ = paramMatchList;
      return this;
    }
    
    public TeamData setName(String paramString)
    {
      this.hasName = true;
      this.name_ = paramString;
      return this;
    }
    
    public TeamData setNextMatch(EcoutezStructuredResponse.Match paramMatch)
    {
      if (paramMatch == null) {
        throw new NullPointerException();
      }
      this.hasNextMatch = true;
      this.nextMatch_ = paramMatch;
      return this;
    }
    
    public TeamData setPlayoffsStandings(EcoutezStructuredResponse.PlayoffsStandings paramPlayoffsStandings)
    {
      if (paramPlayoffsStandings == null) {
        throw new NullPointerException();
      }
      this.hasPlayoffsStandings = true;
      this.playoffsStandings_ = paramPlayoffsStandings;
      return this;
    }
    
    public TeamData setRegularSeasonStandings(EcoutezStructuredResponse.RegularSeasonStandings paramRegularSeasonStandings)
    {
      if (paramRegularSeasonStandings == null) {
        throw new NullPointerException();
      }
      this.hasRegularSeasonStandings = true;
      this.regularSeasonStandings_ = paramRegularSeasonStandings;
      return this;
    }
    
    public TeamData setShortName(String paramString)
    {
      this.hasShortName = true;
      this.shortName_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasName()) {
        paramCodedOutputStreamMicro.writeString(1, getName());
      }
      if (hasRegularSeasonStandings()) {
        paramCodedOutputStreamMicro.writeMessage(2, getRegularSeasonStandings());
      }
      if (hasPlayoffsStandings()) {
        paramCodedOutputStreamMicro.writeMessage(3, getPlayoffsStandings());
      }
      if (hasMatchList()) {
        paramCodedOutputStreamMicro.writeMessage(4, getMatchList());
      }
      if (hasLastMatch()) {
        paramCodedOutputStreamMicro.writeMessage(5, getLastMatch());
      }
      if (hasNextMatch()) {
        paramCodedOutputStreamMicro.writeMessage(6, getNextMatch());
      }
      if (hasInProgressMatch()) {
        paramCodedOutputStreamMicro.writeMessage(7, getInProgressMatch());
      }
      if (hasDEPRECATEDLogoUrl()) {
        paramCodedOutputStreamMicro.writeString(8, getDEPRECATEDLogoUrl());
      }
      if (hasLogo()) {
        paramCodedOutputStreamMicro.writeMessage(9, getLogo());
      }
      if (hasShortName()) {
        paramCodedOutputStreamMicro.writeString(10, getShortName());
      }
    }
  }
  
  public static final class TeamVsTeamData
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String dEPRECATEDFirstTeamLogoUrl_ = "";
    private String dEPRECATEDSecondTeamLogoUrl_ = "";
    private EcoutezStructuredResponse.Logo firstTeamLogo_ = null;
    private String firstTeamShortName_ = "";
    private String firstTeam_ = "";
    private boolean hasDEPRECATEDFirstTeamLogoUrl;
    private boolean hasDEPRECATEDSecondTeamLogoUrl;
    private boolean hasFirstTeam;
    private boolean hasFirstTeamLogo;
    private boolean hasFirstTeamShortName;
    private boolean hasMatchList;
    private boolean hasSecondTeam;
    private boolean hasSecondTeamLogo;
    private boolean hasSecondTeamShortName;
    private EcoutezStructuredResponse.MatchList matchList_ = null;
    private EcoutezStructuredResponse.Logo secondTeamLogo_ = null;
    private String secondTeamShortName_ = "";
    private String secondTeam_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getDEPRECATEDFirstTeamLogoUrl()
    {
      return this.dEPRECATEDFirstTeamLogoUrl_;
    }
    
    public String getDEPRECATEDSecondTeamLogoUrl()
    {
      return this.dEPRECATEDSecondTeamLogoUrl_;
    }
    
    public String getFirstTeam()
    {
      return this.firstTeam_;
    }
    
    public EcoutezStructuredResponse.Logo getFirstTeamLogo()
    {
      return this.firstTeamLogo_;
    }
    
    public String getFirstTeamShortName()
    {
      return this.firstTeamShortName_;
    }
    
    public EcoutezStructuredResponse.MatchList getMatchList()
    {
      return this.matchList_;
    }
    
    public String getSecondTeam()
    {
      return this.secondTeam_;
    }
    
    public EcoutezStructuredResponse.Logo getSecondTeamLogo()
    {
      return this.secondTeamLogo_;
    }
    
    public String getSecondTeamShortName()
    {
      return this.secondTeamShortName_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasFirstTeam();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getFirstTeam());
      }
      if (hasSecondTeam()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getSecondTeam());
      }
      if (hasMatchList()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getMatchList());
      }
      if (hasDEPRECATEDFirstTeamLogoUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getDEPRECATEDFirstTeamLogoUrl());
      }
      if (hasDEPRECATEDSecondTeamLogoUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getDEPRECATEDSecondTeamLogoUrl());
      }
      if (hasFirstTeamLogo()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getFirstTeamLogo());
      }
      if (hasSecondTeamLogo()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, getSecondTeamLogo());
      }
      if (hasFirstTeamShortName()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getFirstTeamShortName());
      }
      if (hasSecondTeamShortName()) {
        i += CodedOutputStreamMicro.computeStringSize(9, getSecondTeamShortName());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasDEPRECATEDFirstTeamLogoUrl()
    {
      return this.hasDEPRECATEDFirstTeamLogoUrl;
    }
    
    public boolean hasDEPRECATEDSecondTeamLogoUrl()
    {
      return this.hasDEPRECATEDSecondTeamLogoUrl;
    }
    
    public boolean hasFirstTeam()
    {
      return this.hasFirstTeam;
    }
    
    public boolean hasFirstTeamLogo()
    {
      return this.hasFirstTeamLogo;
    }
    
    public boolean hasFirstTeamShortName()
    {
      return this.hasFirstTeamShortName;
    }
    
    public boolean hasMatchList()
    {
      return this.hasMatchList;
    }
    
    public boolean hasSecondTeam()
    {
      return this.hasSecondTeam;
    }
    
    public boolean hasSecondTeamLogo()
    {
      return this.hasSecondTeamLogo;
    }
    
    public boolean hasSecondTeamShortName()
    {
      return this.hasSecondTeamShortName;
    }
    
    public TeamVsTeamData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setFirstTeam(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setSecondTeam(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          EcoutezStructuredResponse.MatchList localMatchList = new EcoutezStructuredResponse.MatchList();
          paramCodedInputStreamMicro.readMessage(localMatchList);
          setMatchList(localMatchList);
          break;
        case 34: 
          setDEPRECATEDFirstTeamLogoUrl(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setDEPRECATEDSecondTeamLogoUrl(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          EcoutezStructuredResponse.Logo localLogo2 = new EcoutezStructuredResponse.Logo();
          paramCodedInputStreamMicro.readMessage(localLogo2);
          setFirstTeamLogo(localLogo2);
          break;
        case 58: 
          EcoutezStructuredResponse.Logo localLogo1 = new EcoutezStructuredResponse.Logo();
          paramCodedInputStreamMicro.readMessage(localLogo1);
          setSecondTeamLogo(localLogo1);
          break;
        case 66: 
          setFirstTeamShortName(paramCodedInputStreamMicro.readString());
          break;
        }
        setSecondTeamShortName(paramCodedInputStreamMicro.readString());
      }
    }
    
    public TeamVsTeamData setDEPRECATEDFirstTeamLogoUrl(String paramString)
    {
      this.hasDEPRECATEDFirstTeamLogoUrl = true;
      this.dEPRECATEDFirstTeamLogoUrl_ = paramString;
      return this;
    }
    
    public TeamVsTeamData setDEPRECATEDSecondTeamLogoUrl(String paramString)
    {
      this.hasDEPRECATEDSecondTeamLogoUrl = true;
      this.dEPRECATEDSecondTeamLogoUrl_ = paramString;
      return this;
    }
    
    public TeamVsTeamData setFirstTeam(String paramString)
    {
      this.hasFirstTeam = true;
      this.firstTeam_ = paramString;
      return this;
    }
    
    public TeamVsTeamData setFirstTeamLogo(EcoutezStructuredResponse.Logo paramLogo)
    {
      if (paramLogo == null) {
        throw new NullPointerException();
      }
      this.hasFirstTeamLogo = true;
      this.firstTeamLogo_ = paramLogo;
      return this;
    }
    
    public TeamVsTeamData setFirstTeamShortName(String paramString)
    {
      this.hasFirstTeamShortName = true;
      this.firstTeamShortName_ = paramString;
      return this;
    }
    
    public TeamVsTeamData setMatchList(EcoutezStructuredResponse.MatchList paramMatchList)
    {
      if (paramMatchList == null) {
        throw new NullPointerException();
      }
      this.hasMatchList = true;
      this.matchList_ = paramMatchList;
      return this;
    }
    
    public TeamVsTeamData setSecondTeam(String paramString)
    {
      this.hasSecondTeam = true;
      this.secondTeam_ = paramString;
      return this;
    }
    
    public TeamVsTeamData setSecondTeamLogo(EcoutezStructuredResponse.Logo paramLogo)
    {
      if (paramLogo == null) {
        throw new NullPointerException();
      }
      this.hasSecondTeamLogo = true;
      this.secondTeamLogo_ = paramLogo;
      return this;
    }
    
    public TeamVsTeamData setSecondTeamShortName(String paramString)
    {
      this.hasSecondTeamShortName = true;
      this.secondTeamShortName_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasFirstTeam()) {
        paramCodedOutputStreamMicro.writeString(1, getFirstTeam());
      }
      if (hasSecondTeam()) {
        paramCodedOutputStreamMicro.writeString(2, getSecondTeam());
      }
      if (hasMatchList()) {
        paramCodedOutputStreamMicro.writeMessage(3, getMatchList());
      }
      if (hasDEPRECATEDFirstTeamLogoUrl()) {
        paramCodedOutputStreamMicro.writeString(4, getDEPRECATEDFirstTeamLogoUrl());
      }
      if (hasDEPRECATEDSecondTeamLogoUrl()) {
        paramCodedOutputStreamMicro.writeString(5, getDEPRECATEDSecondTeamLogoUrl());
      }
      if (hasFirstTeamLogo()) {
        paramCodedOutputStreamMicro.writeMessage(6, getFirstTeamLogo());
      }
      if (hasSecondTeamLogo()) {
        paramCodedOutputStreamMicro.writeMessage(7, getSecondTeamLogo());
      }
      if (hasFirstTeamShortName()) {
        paramCodedOutputStreamMicro.writeString(8, getFirstTeamShortName());
      }
      if (hasSecondTeamShortName()) {
        paramCodedOutputStreamMicro.writeString(9, getSecondTeamShortName());
      }
    }
  }
  
  public static final class WeatherCondition
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasImageHeight;
    private boolean hasImageUrl;
    private boolean hasImageWidth;
    private boolean hasText;
    private boolean hasType;
    private int imageHeight_ = 0;
    private String imageUrl_ = "";
    private int imageWidth_ = 0;
    private String text_ = "";
    private int type_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getImageHeight()
    {
      return this.imageHeight_;
    }
    
    public String getImageUrl()
    {
      return this.imageUrl_;
    }
    
    public int getImageWidth()
    {
      return this.imageWidth_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getType());
      }
      if (hasText()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getText());
      }
      if (hasImageUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getImageUrl());
      }
      if (hasImageWidth()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getImageWidth());
      }
      if (hasImageHeight()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getImageHeight());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getText()
    {
      return this.text_;
    }
    
    public int getType()
    {
      return this.type_;
    }
    
    public boolean hasImageHeight()
    {
      return this.hasImageHeight;
    }
    
    public boolean hasImageUrl()
    {
      return this.hasImageUrl;
    }
    
    public boolean hasImageWidth()
    {
      return this.hasImageWidth;
    }
    
    public boolean hasText()
    {
      return this.hasText;
    }
    
    public boolean hasType()
    {
      return this.hasType;
    }
    
    public WeatherCondition mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setText(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setImageUrl(paramCodedInputStreamMicro.readString());
          break;
        case 32: 
          setImageWidth(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setImageHeight(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public WeatherCondition setImageHeight(int paramInt)
    {
      this.hasImageHeight = true;
      this.imageHeight_ = paramInt;
      return this;
    }
    
    public WeatherCondition setImageUrl(String paramString)
    {
      this.hasImageUrl = true;
      this.imageUrl_ = paramString;
      return this;
    }
    
    public WeatherCondition setImageWidth(int paramInt)
    {
      this.hasImageWidth = true;
      this.imageWidth_ = paramInt;
      return this;
    }
    
    public WeatherCondition setText(String paramString)
    {
      this.hasText = true;
      this.text_ = paramString;
      return this;
    }
    
    public WeatherCondition setType(int paramInt)
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
      if (hasText()) {
        paramCodedOutputStreamMicro.writeString(2, getText());
      }
      if (hasImageUrl()) {
        paramCodedOutputStreamMicro.writeString(3, getImageUrl());
      }
      if (hasImageWidth()) {
        paramCodedOutputStreamMicro.writeInt32(4, getImageWidth());
      }
      if (hasImageHeight()) {
        paramCodedOutputStreamMicro.writeInt32(5, getImageHeight());
      }
    }
  }
  
  public static final class WeatherLocation
    extends MessageMicro
  {
    private int cachedSize = -1;
    private String city_ = "";
    private String formattedAddress_ = "";
    private boolean hasCity;
    private boolean hasFormattedAddress;
    private boolean hasTimeZone;
    private String timeZone_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getCity()
    {
      return this.city_;
    }
    
    public String getFormattedAddress()
    {
      return this.formattedAddress_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasFormattedAddress();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getFormattedAddress());
      }
      if (hasCity()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getCity());
      }
      if (hasTimeZone()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getTimeZone());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getTimeZone()
    {
      return this.timeZone_;
    }
    
    public boolean hasCity()
    {
      return this.hasCity;
    }
    
    public boolean hasFormattedAddress()
    {
      return this.hasFormattedAddress;
    }
    
    public boolean hasTimeZone()
    {
      return this.hasTimeZone;
    }
    
    public WeatherLocation mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setFormattedAddress(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setCity(paramCodedInputStreamMicro.readString());
          break;
        }
        setTimeZone(paramCodedInputStreamMicro.readString());
      }
    }
    
    public WeatherLocation setCity(String paramString)
    {
      this.hasCity = true;
      this.city_ = paramString;
      return this;
    }
    
    public WeatherLocation setFormattedAddress(String paramString)
    {
      this.hasFormattedAddress = true;
      this.formattedAddress_ = paramString;
      return this;
    }
    
    public WeatherLocation setTimeZone(String paramString)
    {
      this.hasTimeZone = true;
      this.timeZone_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasFormattedAddress()) {
        paramCodedOutputStreamMicro.writeString(1, getFormattedAddress());
      }
      if (hasCity()) {
        paramCodedOutputStreamMicro.writeString(2, getCity());
      }
      if (hasTimeZone()) {
        paramCodedOutputStreamMicro.writeString(3, getTimeZone());
      }
    }
  }
  
  public static final class WeatherResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private EcoutezStructuredResponse.WeatherState current_ = null;
    private List<EcoutezStructuredResponse.DailyForecast> dailyForecast_ = Collections.emptyList();
    private String forecastStartDate_ = "";
    private boolean hasCurrent;
    private boolean hasForecastStartDate;
    private boolean hasHourlyForecast;
    private boolean hasInMetricUnits;
    private boolean hasLocation;
    private EcoutezStructuredResponse.HourlyForecast hourlyForecast_ = null;
    private boolean inMetricUnits_ = false;
    private EcoutezStructuredResponse.WeatherLocation location_ = null;
    
    public WeatherResult addDailyForecast(EcoutezStructuredResponse.DailyForecast paramDailyForecast)
    {
      if (paramDailyForecast == null) {
        throw new NullPointerException();
      }
      if (this.dailyForecast_.isEmpty()) {
        this.dailyForecast_ = new ArrayList();
      }
      this.dailyForecast_.add(paramDailyForecast);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public EcoutezStructuredResponse.WeatherState getCurrent()
    {
      return this.current_;
    }
    
    public List<EcoutezStructuredResponse.DailyForecast> getDailyForecastList()
    {
      return this.dailyForecast_;
    }
    
    public String getForecastStartDate()
    {
      return this.forecastStartDate_;
    }
    
    public EcoutezStructuredResponse.HourlyForecast getHourlyForecast()
    {
      return this.hourlyForecast_;
    }
    
    public boolean getInMetricUnits()
    {
      return this.inMetricUnits_;
    }
    
    public EcoutezStructuredResponse.WeatherLocation getLocation()
    {
      return this.location_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLocation();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getLocation());
      }
      if (hasCurrent()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getCurrent());
      }
      if (hasForecastStartDate()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getForecastStartDate());
      }
      Iterator localIterator = getDailyForecastList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (EcoutezStructuredResponse.DailyForecast)localIterator.next());
      }
      if (hasHourlyForecast()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getHourlyForecast());
      }
      if (hasInMetricUnits()) {
        i += CodedOutputStreamMicro.computeBoolSize(6, getInMetricUnits());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasCurrent()
    {
      return this.hasCurrent;
    }
    
    public boolean hasForecastStartDate()
    {
      return this.hasForecastStartDate;
    }
    
    public boolean hasHourlyForecast()
    {
      return this.hasHourlyForecast;
    }
    
    public boolean hasInMetricUnits()
    {
      return this.hasInMetricUnits;
    }
    
    public boolean hasLocation()
    {
      return this.hasLocation;
    }
    
    public WeatherResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EcoutezStructuredResponse.WeatherLocation localWeatherLocation = new EcoutezStructuredResponse.WeatherLocation();
          paramCodedInputStreamMicro.readMessage(localWeatherLocation);
          setLocation(localWeatherLocation);
          break;
        case 18: 
          EcoutezStructuredResponse.WeatherState localWeatherState = new EcoutezStructuredResponse.WeatherState();
          paramCodedInputStreamMicro.readMessage(localWeatherState);
          setCurrent(localWeatherState);
          break;
        case 26: 
          setForecastStartDate(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          EcoutezStructuredResponse.DailyForecast localDailyForecast = new EcoutezStructuredResponse.DailyForecast();
          paramCodedInputStreamMicro.readMessage(localDailyForecast);
          addDailyForecast(localDailyForecast);
          break;
        case 42: 
          EcoutezStructuredResponse.HourlyForecast localHourlyForecast = new EcoutezStructuredResponse.HourlyForecast();
          paramCodedInputStreamMicro.readMessage(localHourlyForecast);
          setHourlyForecast(localHourlyForecast);
          break;
        }
        setInMetricUnits(paramCodedInputStreamMicro.readBool());
      }
    }
    
    public WeatherResult setCurrent(EcoutezStructuredResponse.WeatherState paramWeatherState)
    {
      if (paramWeatherState == null) {
        throw new NullPointerException();
      }
      this.hasCurrent = true;
      this.current_ = paramWeatherState;
      return this;
    }
    
    public WeatherResult setForecastStartDate(String paramString)
    {
      this.hasForecastStartDate = true;
      this.forecastStartDate_ = paramString;
      return this;
    }
    
    public WeatherResult setHourlyForecast(EcoutezStructuredResponse.HourlyForecast paramHourlyForecast)
    {
      if (paramHourlyForecast == null) {
        throw new NullPointerException();
      }
      this.hasHourlyForecast = true;
      this.hourlyForecast_ = paramHourlyForecast;
      return this;
    }
    
    public WeatherResult setInMetricUnits(boolean paramBoolean)
    {
      this.hasInMetricUnits = true;
      this.inMetricUnits_ = paramBoolean;
      return this;
    }
    
    public WeatherResult setLocation(EcoutezStructuredResponse.WeatherLocation paramWeatherLocation)
    {
      if (paramWeatherLocation == null) {
        throw new NullPointerException();
      }
      this.hasLocation = true;
      this.location_ = paramWeatherLocation;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLocation()) {
        paramCodedOutputStreamMicro.writeMessage(1, getLocation());
      }
      if (hasCurrent()) {
        paramCodedOutputStreamMicro.writeMessage(2, getCurrent());
      }
      if (hasForecastStartDate()) {
        paramCodedOutputStreamMicro.writeString(3, getForecastStartDate());
      }
      Iterator localIterator = getDailyForecastList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (EcoutezStructuredResponse.DailyForecast)localIterator.next());
      }
      if (hasHourlyForecast()) {
        paramCodedOutputStreamMicro.writeMessage(5, getHourlyForecast());
      }
      if (hasInMetricUnits()) {
        paramCodedOutputStreamMicro.writeBool(6, getInMetricUnits());
      }
    }
  }
  
  public static final class WeatherState
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int chanceOfPrecipitation_ = 0;
    private EcoutezStructuredResponse.WeatherCondition condition_ = null;
    private boolean hasChanceOfPrecipitation;
    private boolean hasCondition;
    private boolean hasHumidity;
    private boolean hasTemp;
    private boolean hasWindDirection;
    private boolean hasWindSpeed;
    private int humidity_ = 0;
    private int temp_ = 0;
    private int windDirection_ = 0;
    private int windSpeed_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getChanceOfPrecipitation()
    {
      return this.chanceOfPrecipitation_;
    }
    
    public EcoutezStructuredResponse.WeatherCondition getCondition()
    {
      return this.condition_;
    }
    
    public int getHumidity()
    {
      return this.humidity_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasCondition();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getCondition());
      }
      if (hasTemp()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getTemp());
      }
      if (hasWindDirection()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getWindDirection());
      }
      if (hasWindSpeed()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getWindSpeed());
      }
      if (hasHumidity()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getHumidity());
      }
      if (hasChanceOfPrecipitation()) {
        i += CodedOutputStreamMicro.computeInt32Size(6, getChanceOfPrecipitation());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getTemp()
    {
      return this.temp_;
    }
    
    public int getWindDirection()
    {
      return this.windDirection_;
    }
    
    public int getWindSpeed()
    {
      return this.windSpeed_;
    }
    
    public boolean hasChanceOfPrecipitation()
    {
      return this.hasChanceOfPrecipitation;
    }
    
    public boolean hasCondition()
    {
      return this.hasCondition;
    }
    
    public boolean hasHumidity()
    {
      return this.hasHumidity;
    }
    
    public boolean hasTemp()
    {
      return this.hasTemp;
    }
    
    public boolean hasWindDirection()
    {
      return this.hasWindDirection;
    }
    
    public boolean hasWindSpeed()
    {
      return this.hasWindSpeed;
    }
    
    public WeatherState mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          EcoutezStructuredResponse.WeatherCondition localWeatherCondition = new EcoutezStructuredResponse.WeatherCondition();
          paramCodedInputStreamMicro.readMessage(localWeatherCondition);
          setCondition(localWeatherCondition);
          break;
        case 16: 
          setTemp(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setWindDirection(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setWindSpeed(paramCodedInputStreamMicro.readInt32());
          break;
        case 40: 
          setHumidity(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setChanceOfPrecipitation(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public WeatherState setChanceOfPrecipitation(int paramInt)
    {
      this.hasChanceOfPrecipitation = true;
      this.chanceOfPrecipitation_ = paramInt;
      return this;
    }
    
    public WeatherState setCondition(EcoutezStructuredResponse.WeatherCondition paramWeatherCondition)
    {
      if (paramWeatherCondition == null) {
        throw new NullPointerException();
      }
      this.hasCondition = true;
      this.condition_ = paramWeatherCondition;
      return this;
    }
    
    public WeatherState setHumidity(int paramInt)
    {
      this.hasHumidity = true;
      this.humidity_ = paramInt;
      return this;
    }
    
    public WeatherState setTemp(int paramInt)
    {
      this.hasTemp = true;
      this.temp_ = paramInt;
      return this;
    }
    
    public WeatherState setWindDirection(int paramInt)
    {
      this.hasWindDirection = true;
      this.windDirection_ = paramInt;
      return this;
    }
    
    public WeatherState setWindSpeed(int paramInt)
    {
      this.hasWindSpeed = true;
      this.windSpeed_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasCondition()) {
        paramCodedOutputStreamMicro.writeMessage(1, getCondition());
      }
      if (hasTemp()) {
        paramCodedOutputStreamMicro.writeInt32(2, getTemp());
      }
      if (hasWindDirection()) {
        paramCodedOutputStreamMicro.writeInt32(3, getWindDirection());
      }
      if (hasWindSpeed()) {
        paramCodedOutputStreamMicro.writeInt32(4, getWindSpeed());
      }
      if (hasHumidity()) {
        paramCodedOutputStreamMicro.writeInt32(5, getHumidity());
      }
      if (hasChanceOfPrecipitation()) {
        paramCodedOutputStreamMicro.writeInt32(6, getChanceOfPrecipitation());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.EcoutezStructuredResponse
 * JD-Core Version:    0.7.0.1
 */