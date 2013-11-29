package com.google.android.speech.embedded;

import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.common.collect.Maps;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LanguagePackUtils
{
  public static String buildDownloadFilename(GstaticConfiguration.LanguagePack paramLanguagePack)
  {
    return paramLanguagePack.getLanguagePackId() + ".zip";
  }
  
  public static GstaticConfiguration.LanguagePack findById(String paramString, List<GstaticConfiguration.LanguagePack> paramList)
  {
    int i = paramList.size();
    for (int j = 0; j < i; j++)
    {
      GstaticConfiguration.LanguagePack localLanguagePack = (GstaticConfiguration.LanguagePack)paramList.get(j);
      if (localLanguagePack.getLanguagePackId().equals(paramString)) {
        return localLanguagePack;
      }
    }
    return null;
  }
  
  public static Map<String, GstaticConfiguration.LanguagePack> getCompatibleLanguagePacks(Map<String, GstaticConfiguration.LanguagePack> paramMap, List<GstaticConfiguration.LanguagePack> paramList, int[] paramArrayOfInt, int paramInt)
  {
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator1 = paramMap.values().iterator();
    while (localIterator1.hasNext()) {
      maybeAddCompatible(localHashMap, (GstaticConfiguration.LanguagePack)localIterator1.next(), paramArrayOfInt, paramInt);
    }
    Iterator localIterator2 = paramList.iterator();
    while (localIterator2.hasNext()) {
      maybeAddCompatible(localHashMap, (GstaticConfiguration.LanguagePack)localIterator2.next(), paramArrayOfInt, paramInt);
    }
    return localHashMap;
  }
  
  public static boolean isCompatible(GstaticConfiguration.LanguagePack paramLanguagePack, int[] paramArrayOfInt, int paramInt)
  {
    int i = paramLanguagePack.getLanguagePackFormatVersionCount();
    if (i == 0) {
      return false;
    }
    int j = paramLanguagePack.getLanguagePackFormatVersion(i - 1);
    for (int k = 0;; k++)
    {
      int m = paramArrayOfInt.length;
      int n = 0;
      if (k < m)
      {
        if (paramArrayOfInt[k] == j) {
          n = 1;
        }
      }
      else
      {
        if ((n == 0) || ((paramInt < paramLanguagePack.getMinimumDeviceClass()) && (paramLanguagePack.hasMinimumDeviceClass()))) {
          break;
        }
        return true;
      }
    }
  }
  
  private static void maybeAddCompatible(HashMap<String, GstaticConfiguration.LanguagePack> paramHashMap, GstaticConfiguration.LanguagePack paramLanguagePack, int[] paramArrayOfInt, int paramInt)
  {
    String str = paramLanguagePack.getBcp47Locale();
    if ((isCompatible(paramLanguagePack, paramArrayOfInt, paramInt)) && ((!paramHashMap.containsKey(str)) || (((GstaticConfiguration.LanguagePack)paramHashMap.get(str)).getVersion() < paramLanguagePack.getVersion()))) {
      paramHashMap.put(str, paramLanguagePack);
    }
  }
  
  public static Comparator<GstaticConfiguration.LanguagePack> newLanguagePackComparator(GstaticConfiguration.Configuration paramConfiguration)
  {
    new Comparator()
    {
      public int compare(GstaticConfiguration.LanguagePack paramAnonymousLanguagePack1, GstaticConfiguration.LanguagePack paramAnonymousLanguagePack2)
      {
        int i = 1;
        String str1 = SpokenLanguageUtils.getDisplayName(this.val$config, paramAnonymousLanguagePack1.getBcp47Locale());
        String str2 = SpokenLanguageUtils.getDisplayName(this.val$config, paramAnonymousLanguagePack2.getBcp47Locale());
        int j;
        if (str1 == null)
        {
          j = i;
          if (str2 != null) {
            break label67;
          }
        }
        label67:
        for (int k = i;; k = 0)
        {
          if ((k ^ j) == 0) {
            break label73;
          }
          if (str1 == null) {
            i = -1;
          }
          return i;
          j = 0;
          break;
        }
        label73:
        if ((str1 != null) && (str2 != null)) {
          return str1.compareTo(str2);
        }
        return 0;
      }
    };
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.embedded.LanguagePackUtils
 * JD-Core Version:    0.7.0.1
 */