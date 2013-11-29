package com.google.android.velvet;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.shared.util.Util;
import com.google.common.collect.Maps;
import com.google.wireless.voicesearch.proto.CorporaConfig.CorporaConfiguration2.Corpus;
import com.google.wireless.voicesearch.proto.CorporaConfig.CorporaConfiguration2.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

public class WebCorpus
  extends Corpus
{
  private final boolean mNeedBrowserDimensions;
  private final boolean mNeedLocation;
  private final String mPrefetchPattern;
  private final Map<String, String> mQueryParams;
  private final boolean mShowCards;
  @Nullable
  private final List<String> mSupportedLocales;
  @Nullable
  private final String mUrlAuthority;
  @Nullable
  private final Map<String, String> mUrlParams;
  private final String mUrlPath;
  private final String mWebSearchPattern;
  
  private WebCorpus(String paramString1, Uri paramUri1, Uri paramUri2, String paramString2, String paramString3, String paramString4, @Nullable String paramString5, @Nullable Map<String, String> paramMap1, Map<String, String> paramMap2, @Nullable List<String> paramList, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, @Nullable Corpus paramCorpus, Corpora paramCorpora)
  {
    super(paramString1, paramUri1, paramUri2, 2130968644, paramCorpus, paramCorpora, paramMap2);
    this.mWebSearchPattern = paramString2;
    this.mPrefetchPattern = paramString3;
    this.mUrlPath = paramString4;
    this.mUrlAuthority = paramString5;
    this.mUrlParams = paramMap1;
    this.mQueryParams = paramMap2;
    this.mSupportedLocales = paramList;
    this.mNeedLocation = paramBoolean1;
    this.mShowCards = paramBoolean2;
    this.mNeedBrowserDimensions = paramBoolean3;
  }
  
  public static WebCorpus create(String paramString1, String paramString2, String paramString3, Map<String, String> paramMap, boolean paramBoolean)
  {
    return new WebCorpus(createIdForWebCorpus(paramString1), null, null, paramString2, paramString3, null, null, null, paramMap, null, false, paramBoolean, false, null, null);
  }
  
  private static String createIdForWebCorpus(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return "web";
    }
    return "web." + paramString;
  }
  
  public static WebCorpus createWebCorpus(CorporaConfig.CorporaConfiguration2.Corpus paramCorpus, @Nullable WebCorpus paramWebCorpus, Corpora paramCorpora)
  {
    String str = paramCorpus.getPrefetchPattern();
    if ((TextUtils.isEmpty(str)) && (paramWebCorpus != null)) {
      str = paramWebCorpus.getPrefetchPattern();
    }
    return new WebCorpus(createIdForWebCorpus(paramCorpus.getCorpusIdentifier()), Uri.parse(paramCorpus.getIcon()), Uri.parse(paramCorpus.getName()), paramCorpus.getWebSearchPattern(), str, paramCorpus.getUrlPath(), paramCorpus.getUrlAuthority(), parameterMapFromList(paramCorpus.getUrlParamsList()), parameterMapFromList(paramCorpus.getQueryParamsList()), paramCorpus.getSupportedLocaleList(), paramCorpus.getRequiresLocation(), paramCorpus.getShowCards(), paramCorpus.getRequiresSizeParams(), paramWebCorpus, paramCorpora);
  }
  
  private static Map<String, String> parameterMapFromList(List<CorporaConfig.CorporaConfiguration2.Parameter> paramList)
  {
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      CorporaConfig.CorporaConfiguration2.Parameter localParameter = (CorporaConfig.CorporaConfiguration2.Parameter)localIterator.next();
      localHashMap.put(localParameter.getName(), localParameter.getValue());
    }
    return localHashMap;
  }
  
  public String getPrefetchPattern()
  {
    return this.mPrefetchPattern;
  }
  
  public String getWebSearchPattern()
  {
    return this.mWebSearchPattern;
  }
  
  public boolean isEnabled()
  {
    if ((this.mSupportedLocales != null) && (!this.mSupportedLocales.isEmpty())) {
      return this.mSupportedLocales.contains(Util.getLocaleString());
    }
    return true;
  }
  
  public boolean matchesUrl(Uri paramUri, boolean paramBoolean)
  {
    if (TextUtils.equals(paramUri.getPath(), this.mUrlPath))
    {
      if (paramUri.getAuthority().equals(this.mUrlAuthority)) {
        return true;
      }
      if (paramBoolean)
      {
        if (this.mUrlParams != null)
        {
          Iterator localIterator = this.mUrlParams.entrySet().iterator();
          while (localIterator.hasNext())
          {
            Map.Entry localEntry = (Map.Entry)localIterator.next();
            if (!TextUtils.equals(paramUri.getQueryParameter((String)localEntry.getKey()), (CharSequence)localEntry.getValue())) {
              return false;
            }
          }
        }
        return true;
      }
    }
    return false;
  }
  
  public String toString()
  {
    return "WebCorpus[" + getIdentifier() + ", " + getWebSearchPattern() + ", PATH:" + this.mUrlPath + ", AUTH:" + this.mUrlAuthority + ", PARAMS:" + this.mUrlParams + "]";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.WebCorpus
 * JD-Core Version:    0.7.0.1
 */