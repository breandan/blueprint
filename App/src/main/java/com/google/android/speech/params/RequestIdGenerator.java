package com.google.android.speech.params;

import java.util.UUID;

public class RequestIdGenerator
{
  public static final RequestIdGenerator INSTANCE = new RequestIdGenerator();
  
  public String newRequestId()
  {
    return UUID.randomUUID().toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.params.RequestIdGenerator
 * JD-Core Version:    0.7.0.1
 */