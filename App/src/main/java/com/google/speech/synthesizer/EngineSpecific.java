package com.google.speech.synthesizer;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class EngineSpecific
{
  public static final class SynthesisEngineSpecificRequest
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
    
    public SynthesisEngineSpecificRequest mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.synthesizer.EngineSpecific
 * JD-Core Version:    0.7.0.1
 */