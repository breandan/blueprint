package location.unified;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class LocationDescriptorProto
{
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
  
  public static final class FieldOfView
    extends MessageMicro
  {
    private int cachedSize = -1;
    private float fieldOfViewXDegrees_ = 0.0F;
    private float fieldOfViewYDegrees_ = 0.0F;
    private boolean hasFieldOfViewXDegrees;
    private boolean hasFieldOfViewYDegrees;
    private boolean hasScreenWidthPixels;
    private int screenWidthPixels_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getFieldOfViewXDegrees()
    {
      return this.fieldOfViewXDegrees_;
    }
    
    public float getFieldOfViewYDegrees()
    {
      return this.fieldOfViewYDegrees_;
    }
    
    public int getScreenWidthPixels()
    {
      return this.screenWidthPixels_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasFieldOfViewXDegrees();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeFloatSize(1, getFieldOfViewXDegrees());
      }
      if (hasFieldOfViewYDegrees()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getFieldOfViewYDegrees());
      }
      if (hasScreenWidthPixels()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getScreenWidthPixels());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasFieldOfViewXDegrees()
    {
      return this.hasFieldOfViewXDegrees;
    }
    
    public boolean hasFieldOfViewYDegrees()
    {
      return this.hasFieldOfViewYDegrees;
    }
    
    public boolean hasScreenWidthPixels()
    {
      return this.hasScreenWidthPixels;
    }
    
    public FieldOfView mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setFieldOfViewXDegrees(paramCodedInputStreamMicro.readFloat());
          break;
        case 21: 
          setFieldOfViewYDegrees(paramCodedInputStreamMicro.readFloat());
          break;
        }
        setScreenWidthPixels(paramCodedInputStreamMicro.readInt32());
      }
    }
    
    public FieldOfView setFieldOfViewXDegrees(float paramFloat)
    {
      this.hasFieldOfViewXDegrees = true;
      this.fieldOfViewXDegrees_ = paramFloat;
      return this;
    }
    
    public FieldOfView setFieldOfViewYDegrees(float paramFloat)
    {
      this.hasFieldOfViewYDegrees = true;
      this.fieldOfViewYDegrees_ = paramFloat;
      return this;
    }
    
    public FieldOfView setScreenWidthPixels(int paramInt)
    {
      this.hasScreenWidthPixels = true;
      this.screenWidthPixels_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasFieldOfViewXDegrees()) {
        paramCodedOutputStreamMicro.writeFloat(1, getFieldOfViewXDegrees());
      }
      if (hasFieldOfViewYDegrees()) {
        paramCodedOutputStreamMicro.writeFloat(2, getFieldOfViewYDegrees());
      }
      if (hasScreenWidthPixels()) {
        paramCodedOutputStreamMicro.writeInt32(3, getScreenWidthPixels());
      }
    }
  }
  
  public static final class LatLng
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasLatitudeE7;
    private boolean hasLongitudeE7;
    private int latitudeE7_ = 0;
    private int longitudeE7_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getLatitudeE7()
    {
      return this.latitudeE7_;
    }
    
    public int getLongitudeE7()
    {
      return this.longitudeE7_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLatitudeE7();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeSFixed32Size(1, getLatitudeE7());
      }
      if (hasLongitudeE7()) {
        i += CodedOutputStreamMicro.computeSFixed32Size(2, getLongitudeE7());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasLatitudeE7()
    {
      return this.hasLatitudeE7;
    }
    
    public boolean hasLongitudeE7()
    {
      return this.hasLongitudeE7;
    }
    
    public LatLng mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setLatitudeE7(paramCodedInputStreamMicro.readSFixed32());
          break;
        }
        setLongitudeE7(paramCodedInputStreamMicro.readSFixed32());
      }
    }
    
    public LatLng setLatitudeE7(int paramInt)
    {
      this.hasLatitudeE7 = true;
      this.latitudeE7_ = paramInt;
      return this;
    }
    
    public LatLng setLongitudeE7(int paramInt)
    {
      this.hasLongitudeE7 = true;
      this.longitudeE7_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLatitudeE7()) {
        paramCodedOutputStreamMicro.writeSFixed32(1, getLatitudeE7());
      }
      if (hasLongitudeE7()) {
        paramCodedOutputStreamMicro.writeSFixed32(2, getLongitudeE7());
      }
    }
  }
  
  public static final class LatLngRect
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasHi;
    private boolean hasLo;
    private LocationDescriptorProto.LatLng hi_ = null;
    private LocationDescriptorProto.LatLng lo_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public LocationDescriptorProto.LatLng getHi()
    {
      return this.hi_;
    }
    
    public LocationDescriptorProto.LatLng getLo()
    {
      return this.lo_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLo();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getLo());
      }
      if (hasHi()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getHi());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasHi()
    {
      return this.hasHi;
    }
    
    public boolean hasLo()
    {
      return this.hasLo;
    }
    
    public LatLngRect mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          LocationDescriptorProto.LatLng localLatLng2 = new LocationDescriptorProto.LatLng();
          paramCodedInputStreamMicro.readMessage(localLatLng2);
          setLo(localLatLng2);
          break;
        }
        LocationDescriptorProto.LatLng localLatLng1 = new LocationDescriptorProto.LatLng();
        paramCodedInputStreamMicro.readMessage(localLatLng1);
        setHi(localLatLng1);
      }
    }
    
    public LatLngRect setHi(LocationDescriptorProto.LatLng paramLatLng)
    {
      if (paramLatLng == null) {
        throw new NullPointerException();
      }
      this.hasHi = true;
      this.hi_ = paramLatLng;
      return this;
    }
    
    public LatLngRect setLo(LocationDescriptorProto.LatLng paramLatLng)
    {
      if (paramLatLng == null) {
        throw new NullPointerException();
      }
      this.hasLo = true;
      this.lo_ = paramLatLng;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLo()) {
        paramCodedOutputStreamMicro.writeMessage(1, getLo());
      }
      if (hasHi()) {
        paramCodedOutputStreamMicro.writeMessage(2, getHi());
      }
    }
  }
  
  public static final class LocationAttributesProto
    extends MessageMicro
  {
    private double altitudeMetersFromGround_ = 0.0D;
    private int bearingDegrees_ = 0;
    private int cachedSize = -1;
    private int detectedActivity_ = 0;
    private LocationDescriptorProto.FieldOfView fieldOfView_ = null;
    private boolean hasAltitudeMetersFromGround;
    private boolean hasBearingDegrees;
    private boolean hasDetectedActivity;
    private boolean hasFieldOfView;
    private boolean hasHeadingDegrees;
    private boolean hasRollDegrees;
    private boolean hasSpeedKph;
    private boolean hasTiltDegrees;
    private int headingDegrees_ = 0;
    private int rollDegrees_ = 0;
    private int speedKph_ = 0;
    private int tiltDegrees_ = 0;
    
    public double getAltitudeMetersFromGround()
    {
      return this.altitudeMetersFromGround_;
    }
    
    public int getBearingDegrees()
    {
      return this.bearingDegrees_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getDetectedActivity()
    {
      return this.detectedActivity_;
    }
    
    public LocationDescriptorProto.FieldOfView getFieldOfView()
    {
      return this.fieldOfView_;
    }
    
    public int getHeadingDegrees()
    {
      return this.headingDegrees_;
    }
    
    public int getRollDegrees()
    {
      return this.rollDegrees_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasDetectedActivity();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getDetectedActivity());
      }
      if (hasHeadingDegrees()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getHeadingDegrees());
      }
      if (hasBearingDegrees()) {
        i += CodedOutputStreamMicro.computeInt32Size(3, getBearingDegrees());
      }
      if (hasSpeedKph()) {
        i += CodedOutputStreamMicro.computeInt32Size(4, getSpeedKph());
      }
      if (hasTiltDegrees()) {
        i += CodedOutputStreamMicro.computeInt32Size(5, getTiltDegrees());
      }
      if (hasRollDegrees()) {
        i += CodedOutputStreamMicro.computeInt32Size(6, getRollDegrees());
      }
      if (hasAltitudeMetersFromGround()) {
        i += CodedOutputStreamMicro.computeDoubleSize(7, getAltitudeMetersFromGround());
      }
      if (hasFieldOfView()) {
        i += CodedOutputStreamMicro.computeMessageSize(8, getFieldOfView());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getSpeedKph()
    {
      return this.speedKph_;
    }
    
    public int getTiltDegrees()
    {
      return this.tiltDegrees_;
    }
    
    public boolean hasAltitudeMetersFromGround()
    {
      return this.hasAltitudeMetersFromGround;
    }
    
    public boolean hasBearingDegrees()
    {
      return this.hasBearingDegrees;
    }
    
    public boolean hasDetectedActivity()
    {
      return this.hasDetectedActivity;
    }
    
    public boolean hasFieldOfView()
    {
      return this.hasFieldOfView;
    }
    
    public boolean hasHeadingDegrees()
    {
      return this.hasHeadingDegrees;
    }
    
    public boolean hasRollDegrees()
    {
      return this.hasRollDegrees;
    }
    
    public boolean hasSpeedKph()
    {
      return this.hasSpeedKph;
    }
    
    public boolean hasTiltDegrees()
    {
      return this.hasTiltDegrees;
    }
    
    public LocationAttributesProto mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setDetectedActivity(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setHeadingDegrees(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setBearingDegrees(paramCodedInputStreamMicro.readInt32());
          break;
        case 32: 
          setSpeedKph(paramCodedInputStreamMicro.readInt32());
          break;
        case 40: 
          setTiltDegrees(paramCodedInputStreamMicro.readInt32());
          break;
        case 48: 
          setRollDegrees(paramCodedInputStreamMicro.readInt32());
          break;
        case 57: 
          setAltitudeMetersFromGround(paramCodedInputStreamMicro.readDouble());
          break;
        }
        LocationDescriptorProto.FieldOfView localFieldOfView = new LocationDescriptorProto.FieldOfView();
        paramCodedInputStreamMicro.readMessage(localFieldOfView);
        setFieldOfView(localFieldOfView);
      }
    }
    
    public LocationAttributesProto setAltitudeMetersFromGround(double paramDouble)
    {
      this.hasAltitudeMetersFromGround = true;
      this.altitudeMetersFromGround_ = paramDouble;
      return this;
    }
    
    public LocationAttributesProto setBearingDegrees(int paramInt)
    {
      this.hasBearingDegrees = true;
      this.bearingDegrees_ = paramInt;
      return this;
    }
    
    public LocationAttributesProto setDetectedActivity(int paramInt)
    {
      this.hasDetectedActivity = true;
      this.detectedActivity_ = paramInt;
      return this;
    }
    
    public LocationAttributesProto setFieldOfView(LocationDescriptorProto.FieldOfView paramFieldOfView)
    {
      if (paramFieldOfView == null) {
        throw new NullPointerException();
      }
      this.hasFieldOfView = true;
      this.fieldOfView_ = paramFieldOfView;
      return this;
    }
    
    public LocationAttributesProto setHeadingDegrees(int paramInt)
    {
      this.hasHeadingDegrees = true;
      this.headingDegrees_ = paramInt;
      return this;
    }
    
    public LocationAttributesProto setRollDegrees(int paramInt)
    {
      this.hasRollDegrees = true;
      this.rollDegrees_ = paramInt;
      return this;
    }
    
    public LocationAttributesProto setSpeedKph(int paramInt)
    {
      this.hasSpeedKph = true;
      this.speedKph_ = paramInt;
      return this;
    }
    
    public LocationAttributesProto setTiltDegrees(int paramInt)
    {
      this.hasTiltDegrees = true;
      this.tiltDegrees_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasDetectedActivity()) {
        paramCodedOutputStreamMicro.writeInt32(1, getDetectedActivity());
      }
      if (hasHeadingDegrees()) {
        paramCodedOutputStreamMicro.writeInt32(2, getHeadingDegrees());
      }
      if (hasBearingDegrees()) {
        paramCodedOutputStreamMicro.writeInt32(3, getBearingDegrees());
      }
      if (hasSpeedKph()) {
        paramCodedOutputStreamMicro.writeInt32(4, getSpeedKph());
      }
      if (hasTiltDegrees()) {
        paramCodedOutputStreamMicro.writeInt32(5, getTiltDegrees());
      }
      if (hasRollDegrees()) {
        paramCodedOutputStreamMicro.writeInt32(6, getRollDegrees());
      }
      if (hasAltitudeMetersFromGround()) {
        paramCodedOutputStreamMicro.writeDouble(7, getAltitudeMetersFromGround());
      }
      if (hasFieldOfView()) {
        paramCodedOutputStreamMicro.writeMessage(8, getFieldOfView());
      }
    }
  }
  
  public static final class LocationDescriptor
    extends MessageMicro
  {
    private LocationDescriptorProto.LocationAttributesProto attributes_ = null;
    private int cachedSize = -1;
    private int confidence_ = 100;
    private String diagnosticInfo_ = "";
    private LocationDescriptorProto.FeatureIdProto featureId_ = null;
    private boolean hasAttributes;
    private boolean hasConfidence;
    private boolean hasDiagnosticInfo;
    private boolean hasFeatureId;
    private boolean hasHistoricalProducer;
    private boolean hasHistoricalProminence;
    private boolean hasHistoricalRole;
    private boolean hasLanguage;
    private boolean hasLatlng;
    private boolean hasLatlngSpan;
    private boolean hasLevelFeatureId;
    private boolean hasLevelNumber;
    private boolean hasLoc;
    private boolean hasMid;
    private boolean hasProducer;
    private boolean hasProvenance;
    private boolean hasRadius;
    private boolean hasRect;
    private boolean hasRole;
    private boolean hasTimestamp;
    private int historicalProducer_ = 0;
    private int historicalProminence_ = 0;
    private int historicalRole_ = 0;
    private String language_ = "";
    private LocationDescriptorProto.LatLng latlngSpan_ = null;
    private LocationDescriptorProto.LatLng latlng_ = null;
    private LocationDescriptorProto.FeatureIdProto levelFeatureId_ = null;
    private float levelNumber_ = 0.0F;
    private String loc_ = "";
    private long mid_ = 0L;
    private int producer_ = 0;
    private int provenance_ = 0;
    private float radius_ = 0.0F;
    private LocationDescriptorProto.LatLngRect rect_ = null;
    private int role_ = 0;
    private long timestamp_ = 0L;
    
    public LocationDescriptorProto.LocationAttributesProto getAttributes()
    {
      return this.attributes_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getConfidence()
    {
      return this.confidence_;
    }
    
    public String getDiagnosticInfo()
    {
      return this.diagnosticInfo_;
    }
    
    public LocationDescriptorProto.FeatureIdProto getFeatureId()
    {
      return this.featureId_;
    }
    
    public int getHistoricalProducer()
    {
      return this.historicalProducer_;
    }
    
    public int getHistoricalProminence()
    {
      return this.historicalProminence_;
    }
    
    public int getHistoricalRole()
    {
      return this.historicalRole_;
    }
    
    public String getLanguage()
    {
      return this.language_;
    }
    
    public LocationDescriptorProto.LatLng getLatlng()
    {
      return this.latlng_;
    }
    
    public LocationDescriptorProto.LatLng getLatlngSpan()
    {
      return this.latlngSpan_;
    }
    
    public LocationDescriptorProto.FeatureIdProto getLevelFeatureId()
    {
      return this.levelFeatureId_;
    }
    
    public float getLevelNumber()
    {
      return this.levelNumber_;
    }
    
    public String getLoc()
    {
      return this.loc_;
    }
    
    public long getMid()
    {
      return this.mid_;
    }
    
    public int getProducer()
    {
      return this.producer_;
    }
    
    public int getProvenance()
    {
      return this.provenance_;
    }
    
    public float getRadius()
    {
      return this.radius_;
    }
    
    public LocationDescriptorProto.LatLngRect getRect()
    {
      return this.rect_;
    }
    
    public int getRole()
    {
      return this.role_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasRole();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getRole());
      }
      if (hasProducer()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getProducer());
      }
      if (hasTimestamp()) {
        i += CodedOutputStreamMicro.computeInt64Size(3, getTimestamp());
      }
      if (hasLoc()) {
        i += CodedOutputStreamMicro.computeStringSize(4, getLoc());
      }
      if (hasLatlng()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getLatlng());
      }
      if (hasLatlngSpan()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getLatlngSpan());
      }
      if (hasRadius()) {
        i += CodedOutputStreamMicro.computeFloatSize(7, getRadius());
      }
      if (hasConfidence()) {
        i += CodedOutputStreamMicro.computeInt32Size(8, getConfidence());
      }
      if (hasProvenance()) {
        i += CodedOutputStreamMicro.computeInt32Size(9, getProvenance());
      }
      if (hasFeatureId()) {
        i += CodedOutputStreamMicro.computeMessageSize(10, getFeatureId());
      }
      if (hasLanguage()) {
        i += CodedOutputStreamMicro.computeStringSize(11, getLanguage());
      }
      if (hasHistoricalRole()) {
        i += CodedOutputStreamMicro.computeInt32Size(12, getHistoricalRole());
      }
      if (hasHistoricalProducer()) {
        i += CodedOutputStreamMicro.computeInt32Size(13, getHistoricalProducer());
      }
      if (hasRect()) {
        i += CodedOutputStreamMicro.computeMessageSize(14, getRect());
      }
      if (hasHistoricalProminence()) {
        i += CodedOutputStreamMicro.computeInt32Size(15, getHistoricalProminence());
      }
      if (hasMid()) {
        i += CodedOutputStreamMicro.computeUInt64Size(16, getMid());
      }
      if (hasLevelFeatureId()) {
        i += CodedOutputStreamMicro.computeMessageSize(17, getLevelFeatureId());
      }
      if (hasLevelNumber()) {
        i += CodedOutputStreamMicro.computeFloatSize(18, getLevelNumber());
      }
      if (hasAttributes()) {
        i += CodedOutputStreamMicro.computeMessageSize(19, getAttributes());
      }
      if (hasDiagnosticInfo()) {
        i += CodedOutputStreamMicro.computeStringSize(20, getDiagnosticInfo());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getTimestamp()
    {
      return this.timestamp_;
    }
    
    public boolean hasAttributes()
    {
      return this.hasAttributes;
    }
    
    public boolean hasConfidence()
    {
      return this.hasConfidence;
    }
    
    public boolean hasDiagnosticInfo()
    {
      return this.hasDiagnosticInfo;
    }
    
    public boolean hasFeatureId()
    {
      return this.hasFeatureId;
    }
    
    public boolean hasHistoricalProducer()
    {
      return this.hasHistoricalProducer;
    }
    
    public boolean hasHistoricalProminence()
    {
      return this.hasHistoricalProminence;
    }
    
    public boolean hasHistoricalRole()
    {
      return this.hasHistoricalRole;
    }
    
    public boolean hasLanguage()
    {
      return this.hasLanguage;
    }
    
    public boolean hasLatlng()
    {
      return this.hasLatlng;
    }
    
    public boolean hasLatlngSpan()
    {
      return this.hasLatlngSpan;
    }
    
    public boolean hasLevelFeatureId()
    {
      return this.hasLevelFeatureId;
    }
    
    public boolean hasLevelNumber()
    {
      return this.hasLevelNumber;
    }
    
    public boolean hasLoc()
    {
      return this.hasLoc;
    }
    
    public boolean hasMid()
    {
      return this.hasMid;
    }
    
    public boolean hasProducer()
    {
      return this.hasProducer;
    }
    
    public boolean hasProvenance()
    {
      return this.hasProvenance;
    }
    
    public boolean hasRadius()
    {
      return this.hasRadius;
    }
    
    public boolean hasRect()
    {
      return this.hasRect;
    }
    
    public boolean hasRole()
    {
      return this.hasRole;
    }
    
    public boolean hasTimestamp()
    {
      return this.hasTimestamp;
    }
    
    public LocationDescriptor mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setRole(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setProducer(paramCodedInputStreamMicro.readInt32());
          break;
        case 24: 
          setTimestamp(paramCodedInputStreamMicro.readInt64());
          break;
        case 34: 
          setLoc(paramCodedInputStreamMicro.readString());
          break;
        case 42: 
          LocationDescriptorProto.LatLng localLatLng2 = new LocationDescriptorProto.LatLng();
          paramCodedInputStreamMicro.readMessage(localLatLng2);
          setLatlng(localLatLng2);
          break;
        case 50: 
          LocationDescriptorProto.LatLng localLatLng1 = new LocationDescriptorProto.LatLng();
          paramCodedInputStreamMicro.readMessage(localLatLng1);
          setLatlngSpan(localLatLng1);
          break;
        case 61: 
          setRadius(paramCodedInputStreamMicro.readFloat());
          break;
        case 64: 
          setConfidence(paramCodedInputStreamMicro.readInt32());
          break;
        case 72: 
          setProvenance(paramCodedInputStreamMicro.readInt32());
          break;
        case 82: 
          LocationDescriptorProto.FeatureIdProto localFeatureIdProto2 = new LocationDescriptorProto.FeatureIdProto();
          paramCodedInputStreamMicro.readMessage(localFeatureIdProto2);
          setFeatureId(localFeatureIdProto2);
          break;
        case 90: 
          setLanguage(paramCodedInputStreamMicro.readString());
          break;
        case 96: 
          setHistoricalRole(paramCodedInputStreamMicro.readInt32());
          break;
        case 104: 
          setHistoricalProducer(paramCodedInputStreamMicro.readInt32());
          break;
        case 114: 
          LocationDescriptorProto.LatLngRect localLatLngRect = new LocationDescriptorProto.LatLngRect();
          paramCodedInputStreamMicro.readMessage(localLatLngRect);
          setRect(localLatLngRect);
          break;
        case 120: 
          setHistoricalProminence(paramCodedInputStreamMicro.readInt32());
          break;
        case 128: 
          setMid(paramCodedInputStreamMicro.readUInt64());
          break;
        case 138: 
          LocationDescriptorProto.FeatureIdProto localFeatureIdProto1 = new LocationDescriptorProto.FeatureIdProto();
          paramCodedInputStreamMicro.readMessage(localFeatureIdProto1);
          setLevelFeatureId(localFeatureIdProto1);
          break;
        case 149: 
          setLevelNumber(paramCodedInputStreamMicro.readFloat());
          break;
        case 154: 
          LocationDescriptorProto.LocationAttributesProto localLocationAttributesProto = new LocationDescriptorProto.LocationAttributesProto();
          paramCodedInputStreamMicro.readMessage(localLocationAttributesProto);
          setAttributes(localLocationAttributesProto);
          break;
        }
        setDiagnosticInfo(paramCodedInputStreamMicro.readString());
      }
    }
    
    public LocationDescriptor setAttributes(LocationDescriptorProto.LocationAttributesProto paramLocationAttributesProto)
    {
      if (paramLocationAttributesProto == null) {
        throw new NullPointerException();
      }
      this.hasAttributes = true;
      this.attributes_ = paramLocationAttributesProto;
      return this;
    }
    
    public LocationDescriptor setConfidence(int paramInt)
    {
      this.hasConfidence = true;
      this.confidence_ = paramInt;
      return this;
    }
    
    public LocationDescriptor setDiagnosticInfo(String paramString)
    {
      this.hasDiagnosticInfo = true;
      this.diagnosticInfo_ = paramString;
      return this;
    }
    
    public LocationDescriptor setFeatureId(LocationDescriptorProto.FeatureIdProto paramFeatureIdProto)
    {
      if (paramFeatureIdProto == null) {
        throw new NullPointerException();
      }
      this.hasFeatureId = true;
      this.featureId_ = paramFeatureIdProto;
      return this;
    }
    
    public LocationDescriptor setHistoricalProducer(int paramInt)
    {
      this.hasHistoricalProducer = true;
      this.historicalProducer_ = paramInt;
      return this;
    }
    
    public LocationDescriptor setHistoricalProminence(int paramInt)
    {
      this.hasHistoricalProminence = true;
      this.historicalProminence_ = paramInt;
      return this;
    }
    
    public LocationDescriptor setHistoricalRole(int paramInt)
    {
      this.hasHistoricalRole = true;
      this.historicalRole_ = paramInt;
      return this;
    }
    
    public LocationDescriptor setLanguage(String paramString)
    {
      this.hasLanguage = true;
      this.language_ = paramString;
      return this;
    }
    
    public LocationDescriptor setLatlng(LocationDescriptorProto.LatLng paramLatLng)
    {
      if (paramLatLng == null) {
        throw new NullPointerException();
      }
      this.hasLatlng = true;
      this.latlng_ = paramLatLng;
      return this;
    }
    
    public LocationDescriptor setLatlngSpan(LocationDescriptorProto.LatLng paramLatLng)
    {
      if (paramLatLng == null) {
        throw new NullPointerException();
      }
      this.hasLatlngSpan = true;
      this.latlngSpan_ = paramLatLng;
      return this;
    }
    
    public LocationDescriptor setLevelFeatureId(LocationDescriptorProto.FeatureIdProto paramFeatureIdProto)
    {
      if (paramFeatureIdProto == null) {
        throw new NullPointerException();
      }
      this.hasLevelFeatureId = true;
      this.levelFeatureId_ = paramFeatureIdProto;
      return this;
    }
    
    public LocationDescriptor setLevelNumber(float paramFloat)
    {
      this.hasLevelNumber = true;
      this.levelNumber_ = paramFloat;
      return this;
    }
    
    public LocationDescriptor setLoc(String paramString)
    {
      this.hasLoc = true;
      this.loc_ = paramString;
      return this;
    }
    
    public LocationDescriptor setMid(long paramLong)
    {
      this.hasMid = true;
      this.mid_ = paramLong;
      return this;
    }
    
    public LocationDescriptor setProducer(int paramInt)
    {
      this.hasProducer = true;
      this.producer_ = paramInt;
      return this;
    }
    
    public LocationDescriptor setProvenance(int paramInt)
    {
      this.hasProvenance = true;
      this.provenance_ = paramInt;
      return this;
    }
    
    public LocationDescriptor setRadius(float paramFloat)
    {
      this.hasRadius = true;
      this.radius_ = paramFloat;
      return this;
    }
    
    public LocationDescriptor setRect(LocationDescriptorProto.LatLngRect paramLatLngRect)
    {
      if (paramLatLngRect == null) {
        throw new NullPointerException();
      }
      this.hasRect = true;
      this.rect_ = paramLatLngRect;
      return this;
    }
    
    public LocationDescriptor setRole(int paramInt)
    {
      this.hasRole = true;
      this.role_ = paramInt;
      return this;
    }
    
    public LocationDescriptor setTimestamp(long paramLong)
    {
      this.hasTimestamp = true;
      this.timestamp_ = paramLong;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasRole()) {
        paramCodedOutputStreamMicro.writeInt32(1, getRole());
      }
      if (hasProducer()) {
        paramCodedOutputStreamMicro.writeInt32(2, getProducer());
      }
      if (hasTimestamp()) {
        paramCodedOutputStreamMicro.writeInt64(3, getTimestamp());
      }
      if (hasLoc()) {
        paramCodedOutputStreamMicro.writeString(4, getLoc());
      }
      if (hasLatlng()) {
        paramCodedOutputStreamMicro.writeMessage(5, getLatlng());
      }
      if (hasLatlngSpan()) {
        paramCodedOutputStreamMicro.writeMessage(6, getLatlngSpan());
      }
      if (hasRadius()) {
        paramCodedOutputStreamMicro.writeFloat(7, getRadius());
      }
      if (hasConfidence()) {
        paramCodedOutputStreamMicro.writeInt32(8, getConfidence());
      }
      if (hasProvenance()) {
        paramCodedOutputStreamMicro.writeInt32(9, getProvenance());
      }
      if (hasFeatureId()) {
        paramCodedOutputStreamMicro.writeMessage(10, getFeatureId());
      }
      if (hasLanguage()) {
        paramCodedOutputStreamMicro.writeString(11, getLanguage());
      }
      if (hasHistoricalRole()) {
        paramCodedOutputStreamMicro.writeInt32(12, getHistoricalRole());
      }
      if (hasHistoricalProducer()) {
        paramCodedOutputStreamMicro.writeInt32(13, getHistoricalProducer());
      }
      if (hasRect()) {
        paramCodedOutputStreamMicro.writeMessage(14, getRect());
      }
      if (hasHistoricalProminence()) {
        paramCodedOutputStreamMicro.writeInt32(15, getHistoricalProminence());
      }
      if (hasMid()) {
        paramCodedOutputStreamMicro.writeUInt64(16, getMid());
      }
      if (hasLevelFeatureId()) {
        paramCodedOutputStreamMicro.writeMessage(17, getLevelFeatureId());
      }
      if (hasLevelNumber()) {
        paramCodedOutputStreamMicro.writeFloat(18, getLevelNumber());
      }
      if (hasAttributes()) {
        paramCodedOutputStreamMicro.writeMessage(19, getAttributes());
      }
      if (hasDiagnosticInfo()) {
        paramCodedOutputStreamMicro.writeString(20, getDiagnosticInfo());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     location.unified.LocationDescriptorProto
 * JD-Core Version:    0.7.0.1
 */