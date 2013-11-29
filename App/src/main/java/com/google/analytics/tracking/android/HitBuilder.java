package com.google.analytics.tracking.android;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class HitBuilder
{
  static String encode(String paramString)
  {
    try
    {
      String str = URLEncoder.encode(paramString, "UTF-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError("URL encoding failed for: " + paramString);
    }
  }
  
  static Map<String, String> generateHitParams(MetaModel paramMetaModel, Map<String, String> paramMap)
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      MetaModel.MetaInfo localMetaInfo = paramMetaModel.getMetaInfo((String)localEntry.getKey());
      if (localMetaInfo != null)
      {
        String str1 = localMetaInfo.getUrlParam((String)localEntry.getKey());
        if (str1 != null)
        {
          String str2 = (String)localEntry.getValue();
          if (localMetaInfo.getFormatter() != null) {
            str2 = localMetaInfo.getFormatter().format(str2);
          }
          if ((str2 != null) && (!str2.equals(localMetaInfo.getDefaultValue()))) {
            localHashMap.put(str1, str2);
          }
        }
      }
    }
    return localHashMap;
  }
  
  static String postProcessHit(Hit paramHit, long paramLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramHit.getHitParams());
    if (paramHit.getHitTime() > 0L)
    {
      long l = paramLong - paramHit.getHitTime();
      if (l >= 0L) {
        localStringBuilder.append("&").append("qt").append("=").append(l);
      }
    }
    localStringBuilder.append("&").append("z").append("=").append(paramHit.getHitId());
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.HitBuilder
 * JD-Core Version:    0.7.0.1
 */