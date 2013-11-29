package speech.s3.goggles;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GogglesS3
{
  public static final class GogglesS3SessionOptions
    extends MessageMicro
  {
    private String annotation_ = "";
    private int cachedSize = -1;
    private boolean canLogImage_ = false;
    private boolean canLogLocation_ = false;
    private List<Integer> disclosedCapabilities_ = Collections.emptyList();
    private boolean hasAnnotation;
    private boolean hasCanLogImage;
    private boolean hasCanLogLocation;
    private boolean hasTextLanguageHint;
    private String textLanguageHint_ = "";
    
    public GogglesS3SessionOptions addDisclosedCapabilities(int paramInt)
    {
      if (this.disclosedCapabilities_.isEmpty()) {
        this.disclosedCapabilities_ = new ArrayList();
      }
      this.disclosedCapabilities_.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public String getAnnotation()
    {
      return this.annotation_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public boolean getCanLogImage()
    {
      return this.canLogImage_;
    }
    
    public boolean getCanLogLocation()
    {
      return this.canLogLocation_;
    }
    
    public List<Integer> getDisclosedCapabilitiesList()
    {
      return this.disclosedCapabilities_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasAnnotation();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getAnnotation());
      }
      if (hasCanLogImage()) {
        i += CodedOutputStreamMicro.computeBoolSize(2, getCanLogImage());
      }
      if (hasCanLogLocation()) {
        i += CodedOutputStreamMicro.computeBoolSize(3, getCanLogLocation());
      }
      int j = 0;
      Iterator localIterator = getDisclosedCapabilitiesList().iterator();
      while (localIterator.hasNext()) {
        j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer)localIterator.next()).intValue());
      }
      int k = i + j + 1 * getDisclosedCapabilitiesList().size();
      if (hasTextLanguageHint()) {
        k += CodedOutputStreamMicro.computeStringSize(5, getTextLanguageHint());
      }
      this.cachedSize = k;
      return k;
    }
    
    public String getTextLanguageHint()
    {
      return this.textLanguageHint_;
    }
    
    public boolean hasAnnotation()
    {
      return this.hasAnnotation;
    }
    
    public boolean hasCanLogImage()
    {
      return this.hasCanLogImage;
    }
    
    public boolean hasCanLogLocation()
    {
      return this.hasCanLogLocation;
    }
    
    public boolean hasTextLanguageHint()
    {
      return this.hasTextLanguageHint;
    }
    
    public GogglesS3SessionOptions mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setAnnotation(paramCodedInputStreamMicro.readString());
          break;
        case 16: 
          setCanLogImage(paramCodedInputStreamMicro.readBool());
          break;
        case 24: 
          setCanLogLocation(paramCodedInputStreamMicro.readBool());
          break;
        case 32: 
          addDisclosedCapabilities(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setTextLanguageHint(paramCodedInputStreamMicro.readString());
      }
    }
    
    public GogglesS3SessionOptions setAnnotation(String paramString)
    {
      this.hasAnnotation = true;
      this.annotation_ = paramString;
      return this;
    }
    
    public GogglesS3SessionOptions setCanLogImage(boolean paramBoolean)
    {
      this.hasCanLogImage = true;
      this.canLogImage_ = paramBoolean;
      return this;
    }
    
    public GogglesS3SessionOptions setCanLogLocation(boolean paramBoolean)
    {
      this.hasCanLogLocation = true;
      this.canLogLocation_ = paramBoolean;
      return this;
    }
    
    public GogglesS3SessionOptions setTextLanguageHint(String paramString)
    {
      this.hasTextLanguageHint = true;
      this.textLanguageHint_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasAnnotation()) {
        paramCodedOutputStreamMicro.writeString(1, getAnnotation());
      }
      if (hasCanLogImage()) {
        paramCodedOutputStreamMicro.writeBool(2, getCanLogImage());
      }
      if (hasCanLogLocation()) {
        paramCodedOutputStreamMicro.writeBool(3, getCanLogLocation());
      }
      Iterator localIterator = getDisclosedCapabilitiesList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeInt32(4, ((Integer)localIterator.next()).intValue());
      }
      if (hasTextLanguageHint()) {
        paramCodedOutputStreamMicro.writeString(5, getTextLanguageHint());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     speech.s3.goggles.GogglesS3
 * JD-Core Version:    0.7.0.1
 */