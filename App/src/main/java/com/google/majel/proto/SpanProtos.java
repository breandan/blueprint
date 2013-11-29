package com.google.majel.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class SpanProtos
{
  public static final class AlternateSpan
    extends MessageMicro
  {
    private List<SpanProtos.AlternateText> alternateText_ = Collections.emptyList();
    private int cachedSize = -1;
    
    public AlternateSpan addAlternateText(SpanProtos.AlternateText paramAlternateText)
    {
      if (paramAlternateText == null) {
        throw new NullPointerException();
      }
      if (this.alternateText_.isEmpty()) {
        this.alternateText_ = new ArrayList();
      }
      this.alternateText_.add(paramAlternateText);
      return this;
    }
    
    public List<SpanProtos.AlternateText> getAlternateTextList()
    {
      return this.alternateText_;
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
      int i = 0;
      Iterator localIterator = getAlternateTextList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (SpanProtos.AlternateText)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public AlternateSpan mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        SpanProtos.AlternateText localAlternateText = new SpanProtos.AlternateText();
        paramCodedInputStreamMicro.readMessage(localAlternateText);
        addAlternateText(localAlternateText);
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getAlternateTextList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (SpanProtos.AlternateText)localIterator.next());
      }
    }
  }
  
  public static final class AlternateText
    extends MessageMicro
  {
    private int cachedSize = -1;
    private float confidence_ = 0.0F;
    private boolean hasConfidence;
    private boolean hasText;
    private String text_ = "";
    
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
    
    public int getSerializedSize()
    {
      boolean bool = hasText();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getText());
      }
      if (hasConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getConfidence());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getText()
    {
      return this.text_;
    }
    
    public boolean hasConfidence()
    {
      return this.hasConfidence;
    }
    
    public boolean hasText()
    {
      return this.hasText;
    }
    
    public AlternateText mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
        setConfidence(paramCodedInputStreamMicro.readFloat());
      }
    }
    
    public AlternateText setConfidence(float paramFloat)
    {
      this.hasConfidence = true;
      this.confidence_ = paramFloat;
      return this;
    }
    
    public AlternateText setText(String paramString)
    {
      this.hasText = true;
      this.text_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasText()) {
        paramCodedOutputStreamMicro.writeString(1, getText());
      }
      if (hasConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(2, getConfidence());
      }
    }
  }
  
  public static final class Span
    extends MessageMicro
  {
    private SpanProtos.AlternateSpan alternateSpanExtension_ = null;
    private int cachedSize = -1;
    private boolean hasAlternateSpanExtension;
    private boolean hasLength;
    private boolean hasStart;
    private int length_ = 0;
    private int start_ = 0;
    
    public SpanProtos.AlternateSpan getAlternateSpanExtension()
    {
      return this.alternateSpanExtension_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getLength()
    {
      return this.length_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasStart();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getStart());
      }
      if (hasLength()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getLength());
      }
      if (hasAlternateSpanExtension()) {
        i += CodedOutputStreamMicro.computeMessageSize(26944131, getAlternateSpanExtension());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getStart()
    {
      return this.start_;
    }
    
    public boolean hasAlternateSpanExtension()
    {
      return this.hasAlternateSpanExtension;
    }
    
    public boolean hasLength()
    {
      return this.hasLength;
    }
    
    public boolean hasStart()
    {
      return this.hasStart;
    }
    
    public Span mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setStart(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setLength(paramCodedInputStreamMicro.readInt32());
          break;
        }
        SpanProtos.AlternateSpan localAlternateSpan = new SpanProtos.AlternateSpan();
        paramCodedInputStreamMicro.readMessage(localAlternateSpan);
        setAlternateSpanExtension(localAlternateSpan);
      }
    }
    
    public Span setAlternateSpanExtension(SpanProtos.AlternateSpan paramAlternateSpan)
    {
      if (paramAlternateSpan == null) {
        throw new NullPointerException();
      }
      this.hasAlternateSpanExtension = true;
      this.alternateSpanExtension_ = paramAlternateSpan;
      return this;
    }
    
    public Span setLength(int paramInt)
    {
      this.hasLength = true;
      this.length_ = paramInt;
      return this;
    }
    
    public Span setStart(int paramInt)
    {
      this.hasStart = true;
      this.start_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasStart()) {
        paramCodedOutputStreamMicro.writeInt32(1, getStart());
      }
      if (hasLength()) {
        paramCodedOutputStreamMicro.writeInt32(2, getLength());
      }
      if (hasAlternateSpanExtension()) {
        paramCodedOutputStreamMicro.writeMessage(26944131, getAlternateSpanExtension());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.majel.proto.SpanProtos
 * JD-Core Version:    0.7.0.1
 */