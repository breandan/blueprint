package com.google.android.search.core;

import android.content.ContentResolver;
import android.content.Context;
import android.text.TextUtils;
import android.util.JsonWriter;
import android.util.Log;
import com.google.android.gsf.Gservices;
import com.google.android.search.core.debug.DebugFeatures;
import com.google.android.velvet.ActionDiscoveryData;
import com.google.android.velvet.VelvetApplication;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.voicesearch.settings.Settings;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

public class GservicesUpdateTask
  implements Callable<Void>
{
  static final String APP_PREFIX = "qsb:";
  static final String DEBUG_FEATURES_LEVEL_KEY = "debug_level";
  private final int mAppVersion;
  private final GservicesClient mClient;
  private final SearchConfig mConfig;
  private final CoreSearchServices mCoreSearchServices;
  private final SearchSettings mSettings;
  private final Settings mVsSettings;
  
  public GservicesUpdateTask(Context paramContext, CoreSearchServices paramCoreSearchServices, Settings paramSettings, int paramInt)
  {
    this(new GservicesClientImpl(paramContext), paramCoreSearchServices, paramSettings, paramInt);
  }
  
  GservicesUpdateTask(GservicesClient paramGservicesClient, CoreSearchServices paramCoreSearchServices, Settings paramSettings, int paramInt)
  {
    this.mClient = paramGservicesClient;
    this.mConfig = paramCoreSearchServices.getConfig();
    this.mSettings = paramCoreSearchServices.getSearchSettings();
    this.mVsSettings = paramSettings;
    this.mCoreSearchServices = paramCoreSearchServices;
    this.mAppVersion = paramInt;
  }
  
  private static int getDebugFeaturesLevel(String paramString)
  {
    if (paramString == null) {
      return 0;
    }
    try
    {
      int i = Integer.parseInt(paramString);
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Log.w("Search.GservicesUpdateTask", "Malformed integer value.");
    }
    return 0;
  }
  
  public static String getGservicesOverridesJson(Context paramContext)
  {
    return getOverridesJsonAndSetDebugLevel(null, new GservicesClientImpl(paramContext), VelvetApplication.getVersionCode());
  }
  
  private static String getOverridesJsonAndSetDebugLevel(SearchSettings paramSearchSettings, GservicesClient paramGservicesClient, int paramInt)
  {
    int i = 0;
    for (;;)
    {
      try
      {
        StringWriter localStringWriter = new StringWriter();
        JsonWriter localJsonWriter = new JsonWriter(localStringWriter);
        localJsonWriter.beginObject();
        Iterator localIterator = paramGservicesClient.getStringsByPrefix("qsb:").entrySet().iterator();
        if (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          String str4 = ((String)localEntry.getKey()).substring("qsb:".length());
          if ("debug_level".equals(str4))
          {
            i = getDebugFeaturesLevel((String)localEntry.getValue());
          }
          else
          {
            int m = str4.indexOf(':');
            if (m >= 0)
            {
              if (matchesVersion(str4.substring(0, m), paramInt)) {
                str4 = str4.substring(m + 1);
              }
            }
            else {
              localJsonWriter.name(str4).value((String)localEntry.getValue());
            }
          }
        }
        else
        {
          Object localObject2;
          String[] arrayOfString;
          int j;
          int k;
          String str1;
          String str2;
          String str3;
          k++;
        }
      }
      catch (IOException localIOException)
      {
        localObject2 = "";
        return localObject2;
        arrayOfString = SearchConfig.getGservicesExtraOverrideKeys();
        j = arrayOfString.length;
        k = 0;
        if (k < j)
        {
          str1 = arrayOfString[k];
          str2 = paramGservicesClient.getString(str1, null);
          if (str2 != null) {
            localJsonWriter.name(str1).value(str2);
          }
        }
        else
        {
          localJsonWriter.endObject();
          localJsonWriter.close();
          str3 = localStringWriter.toString();
          localObject2 = str3;
          return localObject2;
        }
      }
      finally
      {
        if (paramSearchSettings != null) {
          paramSearchSettings.setDebugFeaturesLevel(i);
        }
      }
    }
  }
  
  private static boolean matchesVersion(String paramString, int paramInt)
  {
    int i = paramString.indexOf(',');
    if (i >= 0) {}
    try
    {
      k = Integer.parseInt(paramString.substring(0, i));
      m = Integer.parseInt(paramString.substring(i + 1));
    }
    catch (NumberFormatException localNumberFormatException)
    {
      int j;
      Log.w("Search.GservicesUpdateTask", "Error parsing gservices version spec " + localNumberFormatException);
      return false;
    }
    j = Integer.parseInt(paramString);
    int k = j;
    int m = k;
    boolean bool = false;
    if (paramInt >= k)
    {
      bool = false;
      if (paramInt <= m) {
        bool = true;
      }
    }
    return bool;
  }
  
  public Void call()
  {
    Log.i("Search.GservicesUpdateTask", "Updating Gservices keys");
    this.mSettings.setGservicesOverridesJson(getOverridesJsonAndSetDebugLevel(this.mSettings, this.mClient, this.mAppVersion));
    DebugFeatures.setSearchSettings(this.mSettings);
    this.mVsSettings.updateStaticConfiguration();
    this.mCoreSearchServices.getActionDiscoveryData().maybeUpdateCache();
    this.mConfig.clearCachedValues();
    if ((!TextUtils.equals(this.mConfig.getTextSearchTokenType(), this.mSettings.getTextSearchTokenTypeRefreshed())) || (!TextUtils.equals(this.mConfig.getVoiceSearchTokenType(), this.mVsSettings.getVoiceSearchTokenTypeRefreshed())))
    {
      this.mCoreSearchServices.getBackgroundTasks().forceRunInterruptingOngoing("refresh_auth_tokens");
      if (TextUtils.isEmpty(this.mConfig.getTextSearchTokenType())) {
        this.mCoreSearchServices.getBackgroundTasks().forceRunInterruptingOngoing("refresh_search_domain_and_cookies");
      }
    }
    return null;
  }
  
  protected static abstract interface GservicesClient
  {
    public abstract String getString(String paramString1, String paramString2);
    
    public abstract Map<String, String> getStringsByPrefix(String paramString);
  }
  
  private static class GservicesClientImpl
    implements GservicesUpdateTask.GservicesClient
  {
    private final ContentResolver mContentResolver;
    
    public GservicesClientImpl(Context paramContext)
    {
      this.mContentResolver = paramContext.getContentResolver();
    }
    
    public String getString(String paramString1, String paramString2)
    {
      return Gservices.getString(this.mContentResolver, paramString1, paramString2);
    }
    
    public Map<String, String> getStringsByPrefix(String paramString)
    {
      return Gservices.getStringsByPrefix(this.mContentResolver, new String[] { paramString });
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GservicesUpdateTask
 * JD-Core Version:    0.7.0.1
 */