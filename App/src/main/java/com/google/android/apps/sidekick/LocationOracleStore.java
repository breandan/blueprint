package com.google.android.apps.sidekick;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class LocationOracleStore
{
  public static final class AndroidLocationProto
    extends MessageMicro
  {
    private float accuracyMeters_ = 0.0F;
    private int cachedSize = -1;
    private boolean hasAccuracyMeters;
    private boolean hasLat;
    private boolean hasLng;
    private boolean hasNetworkLocationType;
    private boolean hasProvider;
    private boolean hasTimestampMillis;
    private boolean hasTravelState;
    private double lat_ = 0.0D;
    private double lng_ = 0.0D;
    private String networkLocationType_ = "";
    private String provider_ = "";
    private long timestampMillis_ = 0L;
    private String travelState_ = "";
    
    public float getAccuracyMeters()
    {
      return this.accuracyMeters_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public double getLat()
    {
      return this.lat_;
    }
    
    public double getLng()
    {
      return this.lng_;
    }
    
    public String getNetworkLocationType()
    {
      return this.networkLocationType_;
    }
    
    public String getProvider()
    {
      return this.provider_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasProvider();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getProvider());
      }
      if (hasLat()) {
        i += CodedOutputStreamMicro.computeDoubleSize(2, getLat());
      }
      if (hasLng()) {
        i += CodedOutputStreamMicro.computeDoubleSize(3, getLng());
      }
      if (hasAccuracyMeters()) {
        i += CodedOutputStreamMicro.computeFloatSize(4, getAccuracyMeters());
      }
      if (hasTimestampMillis()) {
        i += CodedOutputStreamMicro.computeInt64Size(5, getTimestampMillis());
      }
      if (hasTravelState()) {
        i += CodedOutputStreamMicro.computeStringSize(6, getTravelState());
      }
      if (hasNetworkLocationType()) {
        i += CodedOutputStreamMicro.computeStringSize(7, getNetworkLocationType());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getTimestampMillis()
    {
      return this.timestampMillis_;
    }
    
    public String getTravelState()
    {
      return this.travelState_;
    }
    
    public boolean hasAccuracyMeters()
    {
      return this.hasAccuracyMeters;
    }
    
    public boolean hasLat()
    {
      return this.hasLat;
    }
    
    public boolean hasLng()
    {
      return this.hasLng;
    }
    
    public boolean hasNetworkLocationType()
    {
      return this.hasNetworkLocationType;
    }
    
    public boolean hasProvider()
    {
      return this.hasProvider;
    }
    
    public boolean hasTimestampMillis()
    {
      return this.hasTimestampMillis;
    }
    
    public boolean hasTravelState()
    {
      return this.hasTravelState;
    }
    
    public AndroidLocationProto mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setProvider(paramCodedInputStreamMicro.readString());
          break;
        case 17: 
          setLat(paramCodedInputStreamMicro.readDouble());
          break;
        case 25: 
          setLng(paramCodedInputStreamMicro.readDouble());
          break;
        case 37: 
          setAccuracyMeters(paramCodedInputStreamMicro.readFloat());
          break;
        case 40: 
          setTimestampMillis(paramCodedInputStreamMicro.readInt64());
          break;
        case 50: 
          setTravelState(paramCodedInputStreamMicro.readString());
          break;
        }
        setNetworkLocationType(paramCodedInputStreamMicro.readString());
      }
    }
    
    public AndroidLocationProto setAccuracyMeters(float paramFloat)
    {
      this.hasAccuracyMeters = true;
      this.accuracyMeters_ = paramFloat;
      return this;
    }
    
    public AndroidLocationProto setLat(double paramDouble)
    {
      this.hasLat = true;
      this.lat_ = paramDouble;
      return this;
    }
    
    public AndroidLocationProto setLng(double paramDouble)
    {
      this.hasLng = true;
      this.lng_ = paramDouble;
      return this;
    }
    
    public AndroidLocationProto setNetworkLocationType(String paramString)
    {
      this.hasNetworkLocationType = true;
      this.networkLocationType_ = paramString;
      return this;
    }
    
    public AndroidLocationProto setProvider(String paramString)
    {
      this.hasProvider = true;
      this.provider_ = paramString;
      return this;
    }
    
    public AndroidLocationProto setTimestampMillis(long paramLong)
    {
      this.hasTimestampMillis = true;
      this.timestampMillis_ = paramLong;
      return this;
    }
    
    public AndroidLocationProto setTravelState(String paramString)
    {
      this.hasTravelState = true;
      this.travelState_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasProvider()) {
        paramCodedOutputStreamMicro.writeString(1, getProvider());
      }
      if (hasLat()) {
        paramCodedOutputStreamMicro.writeDouble(2, getLat());
      }
      if (hasLng()) {
        paramCodedOutputStreamMicro.writeDouble(3, getLng());
      }
      if (hasAccuracyMeters()) {
        paramCodedOutputStreamMicro.writeFloat(4, getAccuracyMeters());
      }
      if (hasTimestampMillis()) {
        paramCodedOutputStreamMicro.writeInt64(5, getTimestampMillis());
      }
      if (hasTravelState()) {
        paramCodedOutputStreamMicro.writeString(6, getTravelState());
      }
      if (hasNetworkLocationType()) {
        paramCodedOutputStreamMicro.writeString(7, getNetworkLocationType());
      }
    }
  }
  
  public static final class LocationOracleData
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<LocationOracleStore.AndroidLocationProto> location_ = Collections.emptyList();
    
    public LocationOracleData addLocation(LocationOracleStore.AndroidLocationProto paramAndroidLocationProto)
    {
      if (paramAndroidLocationProto == null) {
        throw new NullPointerException();
      }
      if (this.location_.isEmpty()) {
        this.location_ = new ArrayList();
      }
      this.location_.add(paramAndroidLocationProto);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getLocationCount()
    {
      return this.location_.size();
    }
    
    public List<LocationOracleStore.AndroidLocationProto> getLocationList()
    {
      return this.location_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getLocationList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (LocationOracleStore.AndroidLocationProto)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public LocationOracleData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        LocationOracleStore.AndroidLocationProto localAndroidLocationProto = new LocationOracleStore.AndroidLocationProto();
        paramCodedInputStreamMicro.readMessage(localAndroidLocationProto);
        addLocation(localAndroidLocationProto);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getLocationList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (LocationOracleStore.AndroidLocationProto)localIterator.next());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.apps.sidekick.LocationOracleStore
 * JD-Core Version:    0.7.0.1
 */