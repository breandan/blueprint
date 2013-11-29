package com.google.android.search.core.util;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class ByteArrayWithHeadersResponse
{
  private final byte[] mByteResponse;
  private final Map<String, List<String>> mResponseHeaders;
  
  public ByteArrayWithHeadersResponse(byte[] paramArrayOfByte, Map<String, List<String>> paramMap)
  {
    this.mByteResponse = paramArrayOfByte;
    this.mResponseHeaders = Maps.newHashMap(paramMap);
  }
  
  public byte[] getResponse()
  {
    return this.mByteResponse;
  }
  
  public Map<String, List<String>> getResponseHeaders()
  {
    return this.mResponseHeaders;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.ByteArrayWithHeadersResponse
 * JD-Core Version:    0.7.0.1
 */