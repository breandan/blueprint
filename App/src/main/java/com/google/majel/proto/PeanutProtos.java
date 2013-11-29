package com.google.majel.proto;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class PeanutProtos
{
  public static final class ClientSideAction
    extends MessageMicro
  {
    private int cachedSize = -1;
    private ActionProtos.Email email_ = null;
    private boolean hasEmail;
    private boolean hasIdentifyAudio;
    private boolean hasNavigate;
    private boolean hasPhone;
    private boolean hasSms;
    private boolean hasTvControl;
    private ActionProtos.IdentifyAudio identifyAudio_ = null;
    private ActionProtos.Navigate navigate_ = null;
    private ActionProtos.Phone phone_ = null;
    private ActionProtos.Sms sms_ = null;
    private ActionProtos.TvControl tvControl_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public ActionProtos.Email getEmail()
    {
      return this.email_;
    }
    
    public ActionProtos.IdentifyAudio getIdentifyAudio()
    {
      return this.identifyAudio_;
    }
    
    public ActionProtos.Navigate getNavigate()
    {
      return this.navigate_;
    }
    
    public ActionProtos.Phone getPhone()
    {
      return this.phone_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasPhone();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getPhone());
      }
      if (hasSms()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getSms());
      }
      if (hasNavigate()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getNavigate());
      }
      if (hasIdentifyAudio()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getIdentifyAudio());
      }
      if (hasEmail()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getEmail());
      }
      if (hasTvControl()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getTvControl());
      }
      this.cachedSize = i;
      return i;
    }
    
    public ActionProtos.Sms getSms()
    {
      return this.sms_;
    }
    
    public ActionProtos.TvControl getTvControl()
    {
      return this.tvControl_;
    }
    
    public boolean hasEmail()
    {
      return this.hasEmail;
    }
    
    public boolean hasIdentifyAudio()
    {
      return this.hasIdentifyAudio;
    }
    
    public boolean hasNavigate()
    {
      return this.hasNavigate;
    }
    
    public boolean hasPhone()
    {
      return this.hasPhone;
    }
    
    public boolean hasSms()
    {
      return this.hasSms;
    }
    
    public boolean hasTvControl()
    {
      return this.hasTvControl;
    }
    
    public ClientSideAction mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          ActionProtos.Phone localPhone = new ActionProtos.Phone();
          paramCodedInputStreamMicro.readMessage(localPhone);
          setPhone(localPhone);
          break;
        case 18: 
          ActionProtos.Sms localSms = new ActionProtos.Sms();
          paramCodedInputStreamMicro.readMessage(localSms);
          setSms(localSms);
          break;
        case 26: 
          ActionProtos.Navigate localNavigate = new ActionProtos.Navigate();
          paramCodedInputStreamMicro.readMessage(localNavigate);
          setNavigate(localNavigate);
          break;
        case 34: 
          ActionProtos.IdentifyAudio localIdentifyAudio = new ActionProtos.IdentifyAudio();
          paramCodedInputStreamMicro.readMessage(localIdentifyAudio);
          setIdentifyAudio(localIdentifyAudio);
          break;
        case 42: 
          ActionProtos.Email localEmail = new ActionProtos.Email();
          paramCodedInputStreamMicro.readMessage(localEmail);
          setEmail(localEmail);
          break;
        }
        ActionProtos.TvControl localTvControl = new ActionProtos.TvControl();
        paramCodedInputStreamMicro.readMessage(localTvControl);
        setTvControl(localTvControl);
      }
    }
    
    public ClientSideAction setEmail(ActionProtos.Email paramEmail)
    {
      if (paramEmail == null) {
        throw new NullPointerException();
      }
      this.hasEmail = true;
      this.email_ = paramEmail;
      return this;
    }
    
    public ClientSideAction setIdentifyAudio(ActionProtos.IdentifyAudio paramIdentifyAudio)
    {
      if (paramIdentifyAudio == null) {
        throw new NullPointerException();
      }
      this.hasIdentifyAudio = true;
      this.identifyAudio_ = paramIdentifyAudio;
      return this;
    }
    
    public ClientSideAction setNavigate(ActionProtos.Navigate paramNavigate)
    {
      if (paramNavigate == null) {
        throw new NullPointerException();
      }
      this.hasNavigate = true;
      this.navigate_ = paramNavigate;
      return this;
    }
    
    public ClientSideAction setPhone(ActionProtos.Phone paramPhone)
    {
      if (paramPhone == null) {
        throw new NullPointerException();
      }
      this.hasPhone = true;
      this.phone_ = paramPhone;
      return this;
    }
    
    public ClientSideAction setSms(ActionProtos.Sms paramSms)
    {
      if (paramSms == null) {
        throw new NullPointerException();
      }
      this.hasSms = true;
      this.sms_ = paramSms;
      return this;
    }
    
    public ClientSideAction setTvControl(ActionProtos.TvControl paramTvControl)
    {
      if (paramTvControl == null) {
        throw new NullPointerException();
      }
      this.hasTvControl = true;
      this.tvControl_ = paramTvControl;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasPhone()) {
        paramCodedOutputStreamMicro.writeMessage(1, getPhone());
      }
      if (hasSms()) {
        paramCodedOutputStreamMicro.writeMessage(2, getSms());
      }
      if (hasNavigate()) {
        paramCodedOutputStreamMicro.writeMessage(3, getNavigate());
      }
      if (hasIdentifyAudio()) {
        paramCodedOutputStreamMicro.writeMessage(4, getIdentifyAudio());
      }
      if (hasEmail()) {
        paramCodedOutputStreamMicro.writeMessage(5, getEmail());
      }
      if (hasTvControl()) {
        paramCodedOutputStreamMicro.writeMessage(6, getTvControl());
      }
    }
  }
  
  public static final class Image
    extends MessageMicro
  {
    private List<AttributionProtos.Attribution> attribution_ = Collections.emptyList();
    private int cachedSize = -1;
    private ByteStringMicro data_ = ByteStringMicro.EMPTY;
    private boolean hasData;
    private boolean hasHeight;
    private boolean hasThumbData;
    private boolean hasThumbHeight;
    private boolean hasThumbUrl;
    private boolean hasThumbWidth;
    private boolean hasUrl;
    private boolean hasWidth;
    private int height_ = 0;
    private ByteStringMicro thumbData_ = ByteStringMicro.EMPTY;
    private int thumbHeight_ = 0;
    private String thumbUrl_ = "";
    private int thumbWidth_ = 0;
    private String url_ = "";
    private int width_ = 0;
    
    public Image addAttribution(AttributionProtos.Attribution paramAttribution)
    {
      if (paramAttribution == null) {
        throw new NullPointerException();
      }
      if (this.attribution_.isEmpty()) {
        this.attribution_ = new ArrayList();
      }
      this.attribution_.add(paramAttribution);
      return this;
    }
    
    public List<AttributionProtos.Attribution> getAttributionList()
    {
      return this.attribution_;
    }
    
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
      if (hasThumbUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getThumbUrl());
      }
      Iterator localIterator = getAttributionList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (AttributionProtos.Attribution)localIterator.next());
      }
      if (hasWidth()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getWidth());
      }
      if (hasHeight()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getHeight());
      }
      if (hasThumbWidth()) {
        i += CodedOutputStreamMicro.computeInt32Size(6, getThumbWidth());
      }
      if (hasThumbHeight()) {
        i += CodedOutputStreamMicro.computeInt32Size(7, getThumbHeight());
      }
      if (hasData()) {
        i += CodedOutputStreamMicro.computeBytesSize(8, getData());
      }
      if (hasThumbData()) {
        i += CodedOutputStreamMicro.computeBytesSize(9, getThumbData());
      }
      this.cachedSize = i;
      return i;
    }
    
    public ByteStringMicro getThumbData()
    {
      return this.thumbData_;
    }
    
    public int getThumbHeight()
    {
      return this.thumbHeight_;
    }
    
    public String getThumbUrl()
    {
      return this.thumbUrl_;
    }
    
    public int getThumbWidth()
    {
      return this.thumbWidth_;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public int getWidth()
    {
      return this.width_;
    }
    
    public boolean hasData()
    {
      return this.hasData;
    }
    
    public boolean hasHeight()
    {
      return this.hasHeight;
    }
    
    public boolean hasThumbData()
    {
      return this.hasThumbData;
    }
    
    public boolean hasThumbHeight()
    {
      return this.hasThumbHeight;
    }
    
    public boolean hasThumbUrl()
    {
      return this.hasThumbUrl;
    }
    
    public boolean hasThumbWidth()
    {
      return this.hasThumbWidth;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public boolean hasWidth()
    {
      return this.hasWidth;
    }
    
    public Image mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setThumbUrl(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          AttributionProtos.Attribution localAttribution = new AttributionProtos.Attribution();
          paramCodedInputStreamMicro.readMessage(localAttribution);
          addAttribution(localAttribution);
          break;
        case 32: 
          setWidth(paramCodedInputStreamMicro.readInt32());
          break;
        case 40: 
          setHeight(paramCodedInputStreamMicro.readInt32());
          break;
        case 48: 
          setThumbWidth(paramCodedInputStreamMicro.readInt32());
          break;
        case 56: 
          setThumbHeight(paramCodedInputStreamMicro.readInt32());
          break;
        case 66: 
          setData(paramCodedInputStreamMicro.readBytes());
          break;
        }
        setThumbData(paramCodedInputStreamMicro.readBytes());
      }
    }
    
    public Image setData(ByteStringMicro paramByteStringMicro)
    {
      this.hasData = true;
      this.data_ = paramByteStringMicro;
      return this;
    }
    
    public Image setHeight(int paramInt)
    {
      this.hasHeight = true;
      this.height_ = paramInt;
      return this;
    }
    
    public Image setThumbData(ByteStringMicro paramByteStringMicro)
    {
      this.hasThumbData = true;
      this.thumbData_ = paramByteStringMicro;
      return this;
    }
    
    public Image setThumbHeight(int paramInt)
    {
      this.hasThumbHeight = true;
      this.thumbHeight_ = paramInt;
      return this;
    }
    
    public Image setThumbUrl(String paramString)
    {
      this.hasThumbUrl = true;
      this.thumbUrl_ = paramString;
      return this;
    }
    
    public Image setThumbWidth(int paramInt)
    {
      this.hasThumbWidth = true;
      this.thumbWidth_ = paramInt;
      return this;
    }
    
    public Image setUrl(String paramString)
    {
      this.hasUrl = true;
      this.url_ = paramString;
      return this;
    }
    
    public Image setWidth(int paramInt)
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
      if (hasThumbUrl()) {
        paramCodedOutputStreamMicro.writeString(2, getThumbUrl());
      }
      Iterator localIterator = getAttributionList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (AttributionProtos.Attribution)localIterator.next());
      }
      if (hasWidth()) {
        paramCodedOutputStreamMicro.writeInt32(4, getWidth());
      }
      if (hasHeight()) {
        paramCodedOutputStreamMicro.writeInt32(5, getHeight());
      }
      if (hasThumbWidth()) {
        paramCodedOutputStreamMicro.writeInt32(6, getThumbWidth());
      }
      if (hasThumbHeight()) {
        paramCodedOutputStreamMicro.writeInt32(7, getThumbHeight());
      }
      if (hasData()) {
        paramCodedOutputStreamMicro.writeBytes(8, getData());
      }
      if (hasThumbData()) {
        paramCodedOutputStreamMicro.writeBytes(9, getThumbData());
      }
    }
  }
  
  public static final class Peanut
    extends MessageMicro
  {
    private List<PeanutProtos.ClientSideAction> actionResponse_ = Collections.emptyList();
    private List<ActionV2Protos.ActionV2> actionV2_ = Collections.emptyList();
    private int cachedSize = -1;
    private float confidence_ = 0.0F;
    private String debug_ = "";
    private float finalScore_ = 0.0F;
    private boolean hasConfidence;
    private boolean hasDebug;
    private boolean hasFinalScore;
    private boolean hasHighConfidenceResponse;
    private boolean hasImageResponseHeader;
    private boolean hasIsLoggable;
    private boolean hasIsQuestion;
    private boolean hasOnlyShowPeanutImageResponse;
    private boolean hasPrimaryType;
    private boolean hasSearchResultsUnnecessary;
    private boolean hasServerName;
    private boolean hasStructuredResponse;
    private boolean hasTextResponse;
    private boolean hasWebSearchType;
    private boolean highConfidenceResponse_ = false;
    private String imageResponseHeader_ = "";
    private List<PeanutProtos.Image> imageResponse_ = Collections.emptyList();
    private boolean isLoggable_ = false;
    private boolean isQuestion_ = false;
    private boolean onlyShowPeanutImageResponse_ = false;
    private List<LatLngProtos.LatLng> placeResponse_ = Collections.emptyList();
    private int primaryType_ = 0;
    private boolean searchResultsUnnecessary_ = true;
    private String serverName_ = "";
    private CommonStructuredResponse.StructuredResponse structuredResponse_ = null;
    private PeanutProtos.Text textResponse_ = null;
    private List<PeanutProtos.Url> urlResponse_ = Collections.emptyList();
    private List<PeanutProtos.Video> videoResponse_ = Collections.emptyList();
    private String webSearchType_ = "";
    
    public static Peanut parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferMicroException
    {
      return (Peanut)new Peanut().mergeFrom(paramArrayOfByte);
    }
    
    public Peanut addActionResponse(PeanutProtos.ClientSideAction paramClientSideAction)
    {
      if (paramClientSideAction == null) {
        throw new NullPointerException();
      }
      if (this.actionResponse_.isEmpty()) {
        this.actionResponse_ = new ArrayList();
      }
      this.actionResponse_.add(paramClientSideAction);
      return this;
    }
    
    public Peanut addActionV2(ActionV2Protos.ActionV2 paramActionV2)
    {
      if (paramActionV2 == null) {
        throw new NullPointerException();
      }
      if (this.actionV2_.isEmpty()) {
        this.actionV2_ = new ArrayList();
      }
      this.actionV2_.add(paramActionV2);
      return this;
    }
    
    public Peanut addImageResponse(PeanutProtos.Image paramImage)
    {
      if (paramImage == null) {
        throw new NullPointerException();
      }
      if (this.imageResponse_.isEmpty()) {
        this.imageResponse_ = new ArrayList();
      }
      this.imageResponse_.add(paramImage);
      return this;
    }
    
    public Peanut addPlaceResponse(LatLngProtos.LatLng paramLatLng)
    {
      if (paramLatLng == null) {
        throw new NullPointerException();
      }
      if (this.placeResponse_.isEmpty()) {
        this.placeResponse_ = new ArrayList();
      }
      this.placeResponse_.add(paramLatLng);
      return this;
    }
    
    public Peanut addUrlResponse(PeanutProtos.Url paramUrl)
    {
      if (paramUrl == null) {
        throw new NullPointerException();
      }
      if (this.urlResponse_.isEmpty()) {
        this.urlResponse_ = new ArrayList();
      }
      this.urlResponse_.add(paramUrl);
      return this;
    }
    
    public Peanut addVideoResponse(PeanutProtos.Video paramVideo)
    {
      if (paramVideo == null) {
        throw new NullPointerException();
      }
      if (this.videoResponse_.isEmpty()) {
        this.videoResponse_ = new ArrayList();
      }
      this.videoResponse_.add(paramVideo);
      return this;
    }
    
    public List<PeanutProtos.ClientSideAction> getActionResponseList()
    {
      return this.actionResponse_;
    }
    
    public ActionV2Protos.ActionV2 getActionV2(int paramInt)
    {
      return (ActionV2Protos.ActionV2)this.actionV2_.get(paramInt);
    }
    
    public int getActionV2Count()
    {
      return this.actionV2_.size();
    }
    
    public List<ActionV2Protos.ActionV2> getActionV2List()
    {
      return this.actionV2_;
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
    
    public String getDebug()
    {
      return this.debug_;
    }
    
    public float getFinalScore()
    {
      return this.finalScore_;
    }
    
    public boolean getHighConfidenceResponse()
    {
      return this.highConfidenceResponse_;
    }
    
    public String getImageResponseHeader()
    {
      return this.imageResponseHeader_;
    }
    
    public List<PeanutProtos.Image> getImageResponseList()
    {
      return this.imageResponse_;
    }
    
    public boolean getIsLoggable()
    {
      return this.isLoggable_;
    }
    
    public boolean getIsQuestion()
    {
      return this.isQuestion_;
    }
    
    public boolean getOnlyShowPeanutImageResponse()
    {
      return this.onlyShowPeanutImageResponse_;
    }
    
    public List<LatLngProtos.LatLng> getPlaceResponseList()
    {
      return this.placeResponse_;
    }
    
    public int getPrimaryType()
    {
      return this.primaryType_;
    }
    
    public boolean getSearchResultsUnnecessary()
    {
      return this.searchResultsUnnecessary_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasServerName();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getServerName());
      }
      if (hasConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getConfidence());
      }
      if (hasTextResponse()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getTextResponse());
      }
      Iterator localIterator1 = getImageResponseList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, (PeanutProtos.Image)localIterator1.next());
      }
      Iterator localIterator2 = getUrlResponseList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, (PeanutProtos.Url)localIterator2.next());
      }
      Iterator localIterator3 = getPlaceResponseList().iterator();
      while (localIterator3.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, (LatLngProtos.LatLng)localIterator3.next());
      }
      if (hasIsQuestion()) {
        i += CodedOutputStreamMicro.computeBoolSize(8, getIsQuestion());
      }
      if (hasPrimaryType()) {
        i += CodedOutputStreamMicro.computeInt32Size(9, getPrimaryType());
      }
      if (hasDebug()) {
        i += CodedOutputStreamMicro.computeStringSize(10, getDebug());
      }
      Iterator localIterator4 = getActionResponseList().iterator();
      while (localIterator4.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(11, (PeanutProtos.ClientSideAction)localIterator4.next());
      }
      if (hasFinalScore()) {
        i += CodedOutputStreamMicro.computeFloatSize(12, getFinalScore());
      }
      if (hasStructuredResponse()) {
        i += CodedOutputStreamMicro.computeMessageSize(13, getStructuredResponse());
      }
      Iterator localIterator5 = getVideoResponseList().iterator();
      while (localIterator5.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(14, (PeanutProtos.Video)localIterator5.next());
      }
      Iterator localIterator6 = getActionV2List().iterator();
      while (localIterator6.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(15, (ActionV2Protos.ActionV2)localIterator6.next());
      }
      if (hasWebSearchType()) {
        i += CodedOutputStreamMicro.computeStringSize(16, getWebSearchType());
      }
      if (hasSearchResultsUnnecessary()) {
        i += CodedOutputStreamMicro.computeBoolSize(17, getSearchResultsUnnecessary());
      }
      if (hasHighConfidenceResponse()) {
        i += CodedOutputStreamMicro.computeBoolSize(18, getHighConfidenceResponse());
      }
      if (hasIsLoggable()) {
        i += CodedOutputStreamMicro.computeBoolSize(19, getIsLoggable());
      }
      if (hasOnlyShowPeanutImageResponse()) {
        i += CodedOutputStreamMicro.computeBoolSize(20, getOnlyShowPeanutImageResponse());
      }
      if (hasImageResponseHeader()) {
        i += CodedOutputStreamMicro.computeStringSize(21, getImageResponseHeader());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getServerName()
    {
      return this.serverName_;
    }
    
    public CommonStructuredResponse.StructuredResponse getStructuredResponse()
    {
      return this.structuredResponse_;
    }
    
    public PeanutProtos.Text getTextResponse()
    {
      return this.textResponse_;
    }
    
    public PeanutProtos.Url getUrlResponse(int paramInt)
    {
      return (PeanutProtos.Url)this.urlResponse_.get(paramInt);
    }
    
    public int getUrlResponseCount()
    {
      return this.urlResponse_.size();
    }
    
    public List<PeanutProtos.Url> getUrlResponseList()
    {
      return this.urlResponse_;
    }
    
    public List<PeanutProtos.Video> getVideoResponseList()
    {
      return this.videoResponse_;
    }
    
    public String getWebSearchType()
    {
      return this.webSearchType_;
    }
    
    public boolean hasConfidence()
    {
      return this.hasConfidence;
    }
    
    public boolean hasDebug()
    {
      return this.hasDebug;
    }
    
    public boolean hasFinalScore()
    {
      return this.hasFinalScore;
    }
    
    public boolean hasHighConfidenceResponse()
    {
      return this.hasHighConfidenceResponse;
    }
    
    public boolean hasImageResponseHeader()
    {
      return this.hasImageResponseHeader;
    }
    
    public boolean hasIsLoggable()
    {
      return this.hasIsLoggable;
    }
    
    public boolean hasIsQuestion()
    {
      return this.hasIsQuestion;
    }
    
    public boolean hasOnlyShowPeanutImageResponse()
    {
      return this.hasOnlyShowPeanutImageResponse;
    }
    
    public boolean hasPrimaryType()
    {
      return this.hasPrimaryType;
    }
    
    public boolean hasSearchResultsUnnecessary()
    {
      return this.hasSearchResultsUnnecessary;
    }
    
    public boolean hasServerName()
    {
      return this.hasServerName;
    }
    
    public boolean hasStructuredResponse()
    {
      return this.hasStructuredResponse;
    }
    
    public boolean hasTextResponse()
    {
      return this.hasTextResponse;
    }
    
    public boolean hasWebSearchType()
    {
      return this.hasWebSearchType;
    }
    
    public Peanut mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setServerName(paramCodedInputStreamMicro.readString());
          break;
        case 21: 
          setConfidence(paramCodedInputStreamMicro.readFloat());
          break;
        case 26: 
          PeanutProtos.Text localText = new PeanutProtos.Text();
          paramCodedInputStreamMicro.readMessage(localText);
          setTextResponse(localText);
          break;
        case 42: 
          PeanutProtos.Image localImage = new PeanutProtos.Image();
          paramCodedInputStreamMicro.readMessage(localImage);
          addImageResponse(localImage);
          break;
        case 50: 
          PeanutProtos.Url localUrl = new PeanutProtos.Url();
          paramCodedInputStreamMicro.readMessage(localUrl);
          addUrlResponse(localUrl);
          break;
        case 58: 
          LatLngProtos.LatLng localLatLng = new LatLngProtos.LatLng();
          paramCodedInputStreamMicro.readMessage(localLatLng);
          addPlaceResponse(localLatLng);
          break;
        case 64: 
          setIsQuestion(paramCodedInputStreamMicro.readBool());
          break;
        case 72: 
          setPrimaryType(paramCodedInputStreamMicro.readInt32());
          break;
        case 82: 
          setDebug(paramCodedInputStreamMicro.readString());
          break;
        case 90: 
          PeanutProtos.ClientSideAction localClientSideAction = new PeanutProtos.ClientSideAction();
          paramCodedInputStreamMicro.readMessage(localClientSideAction);
          addActionResponse(localClientSideAction);
          break;
        case 101: 
          setFinalScore(paramCodedInputStreamMicro.readFloat());
          break;
        case 106: 
          CommonStructuredResponse.StructuredResponse localStructuredResponse = new CommonStructuredResponse.StructuredResponse();
          paramCodedInputStreamMicro.readMessage(localStructuredResponse);
          setStructuredResponse(localStructuredResponse);
          break;
        case 114: 
          PeanutProtos.Video localVideo = new PeanutProtos.Video();
          paramCodedInputStreamMicro.readMessage(localVideo);
          addVideoResponse(localVideo);
          break;
        case 122: 
          ActionV2Protos.ActionV2 localActionV2 = new ActionV2Protos.ActionV2();
          paramCodedInputStreamMicro.readMessage(localActionV2);
          addActionV2(localActionV2);
          break;
        case 130: 
          setWebSearchType(paramCodedInputStreamMicro.readString());
          break;
        case 136: 
          setSearchResultsUnnecessary(paramCodedInputStreamMicro.readBool());
          break;
        case 144: 
          setHighConfidenceResponse(paramCodedInputStreamMicro.readBool());
          break;
        case 152: 
          setIsLoggable(paramCodedInputStreamMicro.readBool());
          break;
        case 160: 
          setOnlyShowPeanutImageResponse(paramCodedInputStreamMicro.readBool());
          break;
        }
        setImageResponseHeader(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Peanut setConfidence(float paramFloat)
    {
      this.hasConfidence = true;
      this.confidence_ = paramFloat;
      return this;
    }
    
    public Peanut setDebug(String paramString)
    {
      this.hasDebug = true;
      this.debug_ = paramString;
      return this;
    }
    
    public Peanut setFinalScore(float paramFloat)
    {
      this.hasFinalScore = true;
      this.finalScore_ = paramFloat;
      return this;
    }
    
    public Peanut setHighConfidenceResponse(boolean paramBoolean)
    {
      this.hasHighConfidenceResponse = true;
      this.highConfidenceResponse_ = paramBoolean;
      return this;
    }
    
    public Peanut setImageResponseHeader(String paramString)
    {
      this.hasImageResponseHeader = true;
      this.imageResponseHeader_ = paramString;
      return this;
    }
    
    public Peanut setIsLoggable(boolean paramBoolean)
    {
      this.hasIsLoggable = true;
      this.isLoggable_ = paramBoolean;
      return this;
    }
    
    public Peanut setIsQuestion(boolean paramBoolean)
    {
      this.hasIsQuestion = true;
      this.isQuestion_ = paramBoolean;
      return this;
    }
    
    public Peanut setOnlyShowPeanutImageResponse(boolean paramBoolean)
    {
      this.hasOnlyShowPeanutImageResponse = true;
      this.onlyShowPeanutImageResponse_ = paramBoolean;
      return this;
    }
    
    public Peanut setPrimaryType(int paramInt)
    {
      this.hasPrimaryType = true;
      this.primaryType_ = paramInt;
      return this;
    }
    
    public Peanut setSearchResultsUnnecessary(boolean paramBoolean)
    {
      this.hasSearchResultsUnnecessary = true;
      this.searchResultsUnnecessary_ = paramBoolean;
      return this;
    }
    
    public Peanut setServerName(String paramString)
    {
      this.hasServerName = true;
      this.serverName_ = paramString;
      return this;
    }
    
    public Peanut setStructuredResponse(CommonStructuredResponse.StructuredResponse paramStructuredResponse)
    {
      if (paramStructuredResponse == null) {
        throw new NullPointerException();
      }
      this.hasStructuredResponse = true;
      this.structuredResponse_ = paramStructuredResponse;
      return this;
    }
    
    public Peanut setTextResponse(PeanutProtos.Text paramText)
    {
      if (paramText == null) {
        throw new NullPointerException();
      }
      this.hasTextResponse = true;
      this.textResponse_ = paramText;
      return this;
    }
    
    public Peanut setWebSearchType(String paramString)
    {
      this.hasWebSearchType = true;
      this.webSearchType_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasServerName()) {
        paramCodedOutputStreamMicro.writeString(1, getServerName());
      }
      if (hasConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(2, getConfidence());
      }
      if (hasTextResponse()) {
        paramCodedOutputStreamMicro.writeMessage(3, getTextResponse());
      }
      Iterator localIterator1 = getImageResponseList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(5, (PeanutProtos.Image)localIterator1.next());
      }
      Iterator localIterator2 = getUrlResponseList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(6, (PeanutProtos.Url)localIterator2.next());
      }
      Iterator localIterator3 = getPlaceResponseList().iterator();
      while (localIterator3.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(7, (LatLngProtos.LatLng)localIterator3.next());
      }
      if (hasIsQuestion()) {
        paramCodedOutputStreamMicro.writeBool(8, getIsQuestion());
      }
      if (hasPrimaryType()) {
        paramCodedOutputStreamMicro.writeInt32(9, getPrimaryType());
      }
      if (hasDebug()) {
        paramCodedOutputStreamMicro.writeString(10, getDebug());
      }
      Iterator localIterator4 = getActionResponseList().iterator();
      while (localIterator4.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(11, (PeanutProtos.ClientSideAction)localIterator4.next());
      }
      if (hasFinalScore()) {
        paramCodedOutputStreamMicro.writeFloat(12, getFinalScore());
      }
      if (hasStructuredResponse()) {
        paramCodedOutputStreamMicro.writeMessage(13, getStructuredResponse());
      }
      Iterator localIterator5 = getVideoResponseList().iterator();
      while (localIterator5.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(14, (PeanutProtos.Video)localIterator5.next());
      }
      Iterator localIterator6 = getActionV2List().iterator();
      while (localIterator6.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(15, (ActionV2Protos.ActionV2)localIterator6.next());
      }
      if (hasWebSearchType()) {
        paramCodedOutputStreamMicro.writeString(16, getWebSearchType());
      }
      if (hasSearchResultsUnnecessary()) {
        paramCodedOutputStreamMicro.writeBool(17, getSearchResultsUnnecessary());
      }
      if (hasHighConfidenceResponse()) {
        paramCodedOutputStreamMicro.writeBool(18, getHighConfidenceResponse());
      }
      if (hasIsLoggable()) {
        paramCodedOutputStreamMicro.writeBool(19, getIsLoggable());
      }
      if (hasOnlyShowPeanutImageResponse()) {
        paramCodedOutputStreamMicro.writeBool(20, getOnlyShowPeanutImageResponse());
      }
      if (hasImageResponseHeader()) {
        paramCodedOutputStreamMicro.writeString(21, getImageResponseHeader());
      }
    }
  }
  
  public static final class SearchResultsInfo
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasQuery;
    private String query_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public String getQuery()
    {
      return this.query_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasQuery();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getQuery());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasQuery()
    {
      return this.hasQuery;
    }
    
    public SearchResultsInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setQuery(paramCodedInputStreamMicro.readString());
      }
    }
    
    public SearchResultsInfo setQuery(String paramString)
    {
      this.hasQuery = true;
      this.query_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasQuery()) {
        paramCodedOutputStreamMicro.writeString(1, getQuery());
      }
    }
  }
  
  public static final class Text
    extends MessageMicro
  {
    private List<AttributionProtos.Attribution> attribution_ = Collections.emptyList();
    private int cachedSize = -1;
    private String disclaimerText_ = "";
    private String disclaimerUrl_ = "";
    private String displayDescription_ = "";
    private String display_ = "";
    private boolean hasDisclaimerText;
    private boolean hasDisclaimerUrl;
    private boolean hasDisplay;
    private boolean hasDisplayDescription;
    private boolean hasLanguage;
    private boolean hasVocalized;
    private boolean hasVocalizedAudio;
    private String language_ = "";
    private ByteStringMicro vocalizedAudio_ = ByteStringMicro.EMPTY;
    private String vocalized_ = "";
    
    public Text addAttribution(AttributionProtos.Attribution paramAttribution)
    {
      if (paramAttribution == null) {
        throw new NullPointerException();
      }
      if (this.attribution_.isEmpty()) {
        this.attribution_ = new ArrayList();
      }
      this.attribution_.add(paramAttribution);
      return this;
    }
    
    public List<AttributionProtos.Attribution> getAttributionList()
    {
      return this.attribution_;
    }
    
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
    
    public String getDisplay()
    {
      return this.display_;
    }
    
    public String getDisplayDescription()
    {
      return this.displayDescription_;
    }
    
    public String getLanguage()
    {
      return this.language_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasDisplay();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getDisplay());
      }
      if (hasVocalized()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getVocalized());
      }
      Iterator localIterator = getAttributionList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (AttributionProtos.Attribution)localIterator.next());
      }
      if (hasLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getLanguage());
      }
      if (hasVocalizedAudio()) {
        i += CodedOutputStreamMicro.computeBytesSize(5, getVocalizedAudio());
      }
      if (hasDisplayDescription()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getDisplayDescription());
      }
      if (hasDisclaimerText()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getDisclaimerText());
      }
      if (hasDisclaimerUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(8, getDisclaimerUrl());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getVocalized()
    {
      return this.vocalized_;
    }
    
    public ByteStringMicro getVocalizedAudio()
    {
      return this.vocalizedAudio_;
    }
    
    public boolean hasDisclaimerText()
    {
      return this.hasDisclaimerText;
    }
    
    public boolean hasDisclaimerUrl()
    {
      return this.hasDisclaimerUrl;
    }
    
    public boolean hasDisplay()
    {
      return this.hasDisplay;
    }
    
    public boolean hasDisplayDescription()
    {
      return this.hasDisplayDescription;
    }
    
    public boolean hasLanguage()
    {
      return this.hasLanguage;
    }
    
    public boolean hasVocalized()
    {
      return this.hasVocalized;
    }
    
    public boolean hasVocalizedAudio()
    {
      return this.hasVocalizedAudio;
    }
    
    public Text mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setDisplay(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setVocalized(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          AttributionProtos.Attribution localAttribution = new AttributionProtos.Attribution();
          paramCodedInputStreamMicro.readMessage(localAttribution);
          addAttribution(localAttribution);
          break;
        case 34: 
          setLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setVocalizedAudio(paramCodedInputStreamMicro.readBytes());
          break;
        case 50: 
          setDisplayDescription(paramCodedInputStreamMicro.readString());
          break;
        case 58: 
          setDisclaimerText(paramCodedInputStreamMicro.readString());
          break;
        }
        setDisclaimerUrl(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Text setDisclaimerText(String paramString)
    {
      this.hasDisclaimerText = true;
      this.disclaimerText_ = paramString;
      return this;
    }
    
    public Text setDisclaimerUrl(String paramString)
    {
      this.hasDisclaimerUrl = true;
      this.disclaimerUrl_ = paramString;
      return this;
    }
    
    public Text setDisplay(String paramString)
    {
      this.hasDisplay = true;
      this.display_ = paramString;
      return this;
    }
    
    public Text setDisplayDescription(String paramString)
    {
      this.hasDisplayDescription = true;
      this.displayDescription_ = paramString;
      return this;
    }
    
    public Text setLanguage(String paramString)
    {
      this.hasLanguage = true;
      this.language_ = paramString;
      return this;
    }
    
    public Text setVocalized(String paramString)
    {
      this.hasVocalized = true;
      this.vocalized_ = paramString;
      return this;
    }
    
    public Text setVocalizedAudio(ByteStringMicro paramByteStringMicro)
    {
      this.hasVocalizedAudio = true;
      this.vocalizedAudio_ = paramByteStringMicro;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasDisplay()) {
        paramCodedOutputStreamMicro.writeString(1, getDisplay());
      }
      if (hasVocalized()) {
        paramCodedOutputStreamMicro.writeString(2, getVocalized());
      }
      Iterator localIterator = getAttributionList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (AttributionProtos.Attribution)localIterator.next());
      }
      if (hasLanguage()) {
        paramCodedOutputStreamMicro.writeString(4, getLanguage());
      }
      if (hasVocalizedAudio()) {
        paramCodedOutputStreamMicro.writeBytes(5, getVocalizedAudio());
      }
      if (hasDisplayDescription()) {
        paramCodedOutputStreamMicro.writeString(6, getDisplayDescription());
      }
      if (hasDisclaimerText()) {
        paramCodedOutputStreamMicro.writeString(7, getDisclaimerText());
      }
      if (hasDisclaimerUrl()) {
        paramCodedOutputStreamMicro.writeString(8, getDisclaimerUrl());
      }
    }
  }
  
  public static final class Url
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<CookieProtos.MajelCookie> cookie_ = Collections.emptyList();
    private String displayLink_ = "";
    private boolean hasDisplayLink;
    private boolean hasHtml;
    private boolean hasLink;
    private boolean hasRenderedLink;
    private boolean hasRenderedPage;
    private boolean hasSearchResultsInfo;
    private boolean hasTitle;
    private String html_ = "";
    private String link_ = "";
    private String renderedLink_ = "";
    private ByteStringMicro renderedPage_ = ByteStringMicro.EMPTY;
    private PeanutProtos.SearchResultsInfo searchResultsInfo_ = null;
    private String title_ = "";
    
    public Url addCookie(CookieProtos.MajelCookie paramMajelCookie)
    {
      if (paramMajelCookie == null) {
        throw new NullPointerException();
      }
      if (this.cookie_.isEmpty()) {
        this.cookie_ = new ArrayList();
      }
      this.cookie_.add(paramMajelCookie);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public List<CookieProtos.MajelCookie> getCookieList()
    {
      return this.cookie_;
    }
    
    public String getDisplayLink()
    {
      return this.displayLink_;
    }
    
    public String getHtml()
    {
      return this.html_;
    }
    
    public String getLink()
    {
      return this.link_;
    }
    
    public String getRenderedLink()
    {
      return this.renderedLink_;
    }
    
    public ByteStringMicro getRenderedPage()
    {
      return this.renderedPage_;
    }
    
    public PeanutProtos.SearchResultsInfo getSearchResultsInfo()
    {
      return this.searchResultsInfo_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLink();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getLink());
      }
      if (hasRenderedLink()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getRenderedLink());
      }
      if (hasRenderedPage()) {
        i += CodedOutputStreamMicro.computeBytesSize(3, getRenderedPage());
      }
      if (hasTitle()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getTitle());
      }
      if (hasHtml()) {
        i += CodedOutputStreamMicro.computeStringSize(5, getHtml());
      }
      if (hasSearchResultsInfo()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getSearchResultsInfo());
      }
      if (hasDisplayLink()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getDisplayLink());
      }
      Iterator localIterator = getCookieList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(8, (CookieProtos.MajelCookie)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public boolean hasDisplayLink()
    {
      return this.hasDisplayLink;
    }
    
    public boolean hasHtml()
    {
      return this.hasHtml;
    }
    
    public boolean hasLink()
    {
      return this.hasLink;
    }
    
    public boolean hasRenderedLink()
    {
      return this.hasRenderedLink;
    }
    
    public boolean hasRenderedPage()
    {
      return this.hasRenderedPage;
    }
    
    public boolean hasSearchResultsInfo()
    {
      return this.hasSearchResultsInfo;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public Url mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setLink(paramCodedInputStreamMicro.readString());
          break;
        case 18: 
          setRenderedLink(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setRenderedPage(paramCodedInputStreamMicro.readBytes());
          break;
        case 34: 
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          setHtml(paramCodedInputStreamMicro.readString());
          break;
        case 50: 
          PeanutProtos.SearchResultsInfo localSearchResultsInfo = new PeanutProtos.SearchResultsInfo();
          paramCodedInputStreamMicro.readMessage(localSearchResultsInfo);
          setSearchResultsInfo(localSearchResultsInfo);
          break;
        case 58: 
          setDisplayLink(paramCodedInputStreamMicro.readString());
          break;
        }
        CookieProtos.MajelCookie localMajelCookie = new CookieProtos.MajelCookie();
        paramCodedInputStreamMicro.readMessage(localMajelCookie);
        addCookie(localMajelCookie);
      }
    }
    
    public Url setDisplayLink(String paramString)
    {
      this.hasDisplayLink = true;
      this.displayLink_ = paramString;
      return this;
    }
    
    public Url setHtml(String paramString)
    {
      this.hasHtml = true;
      this.html_ = paramString;
      return this;
    }
    
    public Url setLink(String paramString)
    {
      this.hasLink = true;
      this.link_ = paramString;
      return this;
    }
    
    public Url setRenderedLink(String paramString)
    {
      this.hasRenderedLink = true;
      this.renderedLink_ = paramString;
      return this;
    }
    
    public Url setRenderedPage(ByteStringMicro paramByteStringMicro)
    {
      this.hasRenderedPage = true;
      this.renderedPage_ = paramByteStringMicro;
      return this;
    }
    
    public Url setSearchResultsInfo(PeanutProtos.SearchResultsInfo paramSearchResultsInfo)
    {
      if (paramSearchResultsInfo == null) {
        throw new NullPointerException();
      }
      this.hasSearchResultsInfo = true;
      this.searchResultsInfo_ = paramSearchResultsInfo;
      return this;
    }
    
    public Url setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLink()) {
        paramCodedOutputStreamMicro.writeString(1, getLink());
      }
      if (hasRenderedLink()) {
        paramCodedOutputStreamMicro.writeString(2, getRenderedLink());
      }
      if (hasRenderedPage()) {
        paramCodedOutputStreamMicro.writeBytes(3, getRenderedPage());
      }
      if (hasTitle()) {
        paramCodedOutputStreamMicro.writeString(4, getTitle());
      }
      if (hasHtml()) {
        paramCodedOutputStreamMicro.writeString(5, getHtml());
      }
      if (hasSearchResultsInfo()) {
        paramCodedOutputStreamMicro.writeMessage(6, getSearchResultsInfo());
      }
      if (hasDisplayLink()) {
        paramCodedOutputStreamMicro.writeString(7, getDisplayLink());
      }
      Iterator localIterator = getCookieList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(8, (CookieProtos.MajelCookie)localIterator.next());
      }
    }
  }
  
  public static final class Video
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int durationMillis_ = 0;
    private boolean hasDurationMillis;
    private boolean hasRenderedThumbUrl;
    private boolean hasThumbUrl;
    private boolean hasTitle;
    private boolean hasUrl;
    private ByteStringMicro renderedThumbUrl_ = ByteStringMicro.EMPTY;
    private String thumbUrl_ = "";
    private String title_ = "";
    private String url_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getDurationMillis()
    {
      return this.durationMillis_;
    }
    
    public ByteStringMicro getRenderedThumbUrl()
    {
      return this.renderedThumbUrl_;
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
      if (hasThumbUrl()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getThumbUrl());
      }
      if (hasRenderedThumbUrl()) {
        i += CodedOutputStreamMicro.computeBytesSize(4, getRenderedThumbUrl());
      }
      if (hasDurationMillis()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getDurationMillis());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getThumbUrl()
    {
      return this.thumbUrl_;
    }
    
    public String getTitle()
    {
      return this.title_;
    }
    
    public String getUrl()
    {
      return this.url_;
    }
    
    public boolean hasDurationMillis()
    {
      return this.hasDurationMillis;
    }
    
    public boolean hasRenderedThumbUrl()
    {
      return this.hasRenderedThumbUrl;
    }
    
    public boolean hasThumbUrl()
    {
      return this.hasThumbUrl;
    }
    
    public boolean hasTitle()
    {
      return this.hasTitle;
    }
    
    public boolean hasUrl()
    {
      return this.hasUrl;
    }
    
    public Video mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setTitle(paramCodedInputStreamMicro.readString());
          break;
        case 26: 
          setThumbUrl(paramCodedInputStreamMicro.readString());
          break;
        case 34: 
          setRenderedThumbUrl(paramCodedInputStreamMicro.readBytes());
          break;
        }
        setDurationMillis(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public Video setDurationMillis(int paramInt)
    {
      this.hasDurationMillis = true;
      this.durationMillis_ = paramInt;
      return this;
    }
    
    public Video setRenderedThumbUrl(ByteStringMicro paramByteStringMicro)
    {
      this.hasRenderedThumbUrl = true;
      this.renderedThumbUrl_ = paramByteStringMicro;
      return this;
    }
    
    public Video setThumbUrl(String paramString)
    {
      this.hasThumbUrl = true;
      this.thumbUrl_ = paramString;
      return this;
    }
    
    public Video setTitle(String paramString)
    {
      this.hasTitle = true;
      this.title_ = paramString;
      return this;
    }
    
    public Video setUrl(String paramString)
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
      if (hasThumbUrl()) {
        paramCodedOutputStreamMicro.writeString(3, getThumbUrl());
      }
      if (hasRenderedThumbUrl()) {
        paramCodedOutputStreamMicro.writeBytes(4, getRenderedThumbUrl());
      }
      if (hasDurationMillis()) {
        paramCodedOutputStreamMicro.writeInt32(5, getDurationMillis());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.PeanutProtos
 * JD-Core Version:    0.7.0.1
 */