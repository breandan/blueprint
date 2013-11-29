package com.google.analytics.tracking.android;

import java.util.HashMap;
import java.util.Map;

class MetaModel
{
  private Map<String, MetaInfo> mMetaInfos = new HashMap();
  
  public void addField(String paramString1, String paramString2, String paramString3, Formatter paramFormatter)
  {
    this.mMetaInfos.put(paramString1, new MetaInfo(paramString2, paramString3, paramFormatter));
  }
  
  MetaInfo getMetaInfo(String paramString)
  {
    if (paramString.startsWith("&")) {
      return new MetaInfo(paramString.substring(1), null, null);
    }
    String str = paramString;
    if (paramString.contains("*")) {
      str = paramString.substring(0, paramString.indexOf("*"));
    }
    return (MetaInfo)this.mMetaInfos.get(str);
  }
  
  public static abstract interface Formatter
  {
    public abstract String format(String paramString);
  }
  
  public static class MetaInfo
  {
    private final String mDefaultValue;
    private final MetaModel.Formatter mFormatter;
    private final String mUrlParam;
    
    public MetaInfo(String paramString1, String paramString2, MetaModel.Formatter paramFormatter)
    {
      this.mUrlParam = paramString1;
      this.mDefaultValue = paramString2;
      this.mFormatter = paramFormatter;
    }
    
    public String getDefaultValue()
    {
      return this.mDefaultValue;
    }
    
    public MetaModel.Formatter getFormatter()
    {
      return this.mFormatter;
    }
    
    public String getUrlParam(String paramString)
    {
      if (paramString.contains("*"))
      {
        String str1 = this.mUrlParam;
        String[] arrayOfString = paramString.split("\\*");
        int i = arrayOfString.length;
        String str2 = null;
        if (i > 1) {}
        try
        {
          int j = Integer.parseInt(arrayOfString[1]);
          str2 = str1 + j;
          return str2;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          Log.w("Unable to parse slot for url parameter " + str1);
          return null;
        }
      }
      return this.mUrlParam;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.MetaModel
 * JD-Core Version:    0.7.0.1
 */