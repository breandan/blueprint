package com.google.android.libraries.tvdetect.impl;

import com.google.android.libraries.tvdetect.util.DefaultHttpFetcher;
import com.google.android.libraries.tvdetect.util.DeviceIdUtil;
import java.util.Map;

class SsdpReply
{
  private final Map<String, String> headers;
  
  public SsdpReply(String paramString)
  {
    this.headers = DefaultHttpFetcher.parseHttpHeaders(paramString);
  }
  
  public String getDeviceUuid()
  {
    String str = (String)this.headers.get("USN");
    if (str == null) {
      return null;
    }
    return DeviceIdUtil.getUuidFromUsn(str);
  }
  
  public String getLocation()
  {
    return (String)this.headers.get("LOCATION");
  }
  
  public String getSearchTarget()
  {
    return (String)this.headers.get("ST");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.impl.SsdpReply
 * JD-Core Version:    0.7.0.1
 */