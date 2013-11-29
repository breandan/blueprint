package com.google.android.gms.appdatasearch;

import java.util.HashMap;
import java.util.Map;

public class GlobalSearchSections
{
  private static final String[] iK;
  private static final Map<String, Integer> iL;
  
  static
  {
    int i = 0;
    iK = new String[] { "text1", "text2", "icon", "intent_action", "intent_data", "intent_data_id", "intent_extra_data", "suggest_large_icon", "intent_activity" };
    iL = new HashMap(iK.length);
    while (i < iK.length)
    {
      iL.put(iK[i], Integer.valueOf(i));
      i++;
    }
  }
  
  public static int getSectionId(String paramString)
  {
    Integer localInteger = (Integer)iL.get(paramString);
    if (localInteger == null) {
      throw new IllegalArgumentException("[" + paramString + "] is not a valid global search section name");
    }
    return localInteger.intValue();
  }
  
  public static int getSectionsCount()
  {
    return iK.length;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.appdatasearch.GlobalSearchSections
 * JD-Core Version:    0.7.0.1
 */