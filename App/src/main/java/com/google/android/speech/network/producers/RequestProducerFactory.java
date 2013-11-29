package com.google.android.speech.network.producers;

import com.google.android.speech.params.SessionParams;
import java.io.InputStream;

public abstract interface RequestProducerFactory
{
  public abstract void init(SessionParams paramSessionParams);
  
  public abstract S3RequestProducer newRequestProducer(InputStream paramInputStream);
  
  public abstract void refresh();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.producers.RequestProducerFactory
 * JD-Core Version:    0.7.0.1
 */