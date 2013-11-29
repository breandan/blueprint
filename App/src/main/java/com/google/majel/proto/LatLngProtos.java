package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class LatLngProtos
{
  public static final class LatLng
    extends MessageMicro
  {
    private float accuracyMeters_ = 0.0F;
    private int cachedSize = -1;
    private boolean hasAccuracyMeters;
    private boolean hasLabel;
    private boolean hasLatDegrees;
    private boolean hasLngDegrees;
    private String label_ = "";
    private float latDegrees_ = 0.0F;
    private float lngDegrees_ = 0.0F;
    
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
    
    public String getLabel()
    {
      return this.label_;
    }
    
    public float getLatDegrees()
    {
      return this.latDegrees_;
    }
    
    public float getLngDegrees()
    {
      return this.lngDegrees_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLatDegrees();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeFloatSize(1, getLatDegrees());
      }
      if (hasLngDegrees()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getLngDegrees());
      }
      if (hasLabel()) {
        i += CodedOutputStreamMicro.computeStringSize(3, getLabel());
      }
      if (hasAccuracyMeters()) {
        i += CodedOutputStreamMicro.computeFloatSize(4, getAccuracyMeters());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasAccuracyMeters()
    {
      return this.hasAccuracyMeters;
    }
    
    public boolean hasLabel()
    {
      return this.hasLabel;
    }
    
    public boolean hasLatDegrees()
    {
      return this.hasLatDegrees;
    }
    
    public boolean hasLngDegrees()
    {
      return this.hasLngDegrees;
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
          setLatDegrees(paramCodedInputStreamMicro.readFloat());
          break;
        case 21: 
          setLngDegrees(paramCodedInputStreamMicro.readFloat());
          break;
        case 26: 
          setLabel(paramCodedInputStreamMicro.readString());
          break;
        }
        setAccuracyMeters(paramCodedInputStreamMicro.readFloat());
      }
    }
    
    public LatLng setAccuracyMeters(float paramFloat)
    {
      this.hasAccuracyMeters = true;
      this.accuracyMeters_ = paramFloat;
      return this;
    }
    
    public LatLng setLabel(String paramString)
    {
      this.hasLabel = true;
      this.label_ = paramString;
      return this;
    }
    
    public LatLng setLatDegrees(float paramFloat)
    {
      this.hasLatDegrees = true;
      this.latDegrees_ = paramFloat;
      return this;
    }
    
    public LatLng setLngDegrees(float paramFloat)
    {
      this.hasLngDegrees = true;
      this.lngDegrees_ = paramFloat;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLatDegrees()) {
        paramCodedOutputStreamMicro.writeFloat(1, getLatDegrees());
      }
      if (hasLngDegrees()) {
        paramCodedOutputStreamMicro.writeFloat(2, getLngDegrees());
      }
      if (hasLabel()) {
        paramCodedOutputStreamMicro.writeString(3, getLabel());
      }
      if (hasAccuracyMeters()) {
        paramCodedOutputStreamMicro.writeFloat(4, getAccuracyMeters());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.LatLngProtos
 * JD-Core Version:    0.7.0.1
 */