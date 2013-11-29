package com.google.android.voicesearch.serviceapi;

import android.content.Intent;
import android.util.Log;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Strings;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Dialect;

public class GoogleRecognitionParams
{
  private final boolean mDictationRequested = initDictationRequested(paramIntent);
  private final boolean mPartialResultsRequested = initPartialResultsRequested(paramIntent);
  private final boolean mProfanityFilterEnabled = initProfanityFilterEnabled(paramSettings, paramIntent);
  private final String mSpokenBcp47Locale = initSpokenBcp47Locale(paramSettings, paramIntent);
  private final String mTriggerApplication = initTriggerApplication(paramIntent);
  
  public GoogleRecognitionParams(Intent paramIntent, Settings paramSettings) {}
  
  private boolean initDictationRequested(Intent paramIntent)
  {
    return paramIntent.getBooleanExtra("android.speech.extra.DICTATION_MODE", false);
  }
  
  private boolean initPartialResultsRequested(Intent paramIntent)
  {
    return paramIntent.getBooleanExtra("android.speech.extra.PARTIAL_RESULTS", false);
  }
  
  private boolean initProfanityFilterEnabled(Settings paramSettings, Intent paramIntent)
  {
    return paramIntent.getBooleanExtra("android.speech.extra.PROFANITY_FILTER", paramSettings.isProfanityFilterEnabled());
  }
  
  private String initSpokenBcp47Locale(Settings paramSettings, Intent paramIntent)
  {
    String str = paramIntent.getStringExtra("android.speech.extra.LANGUAGE");
    if (str != null)
    {
      if (SpokenLanguageUtils.getLanguageDialect(paramSettings.getConfiguration(), str) != null) {
        return str;
      }
      GstaticConfiguration.Dialect localDialect = SpokenLanguageUtils.getSpokenLanguageByJavaLocale(paramSettings.getConfiguration(), str);
      if (localDialect != null)
      {
        Log.w("GoogleRecognitionParams", "The locale should be specified in BCP47");
        return localDialect.getBcp47Locale();
      }
    }
    return paramSettings.getSpokenLocaleBcp47();
  }
  
  private String initTriggerApplication(Intent paramIntent)
  {
    return Strings.nullToEmpty(paramIntent.getStringExtra("calling_package"));
  }
  
  public String getSpokenBcp47Locale()
  {
    return this.mSpokenBcp47Locale;
  }
  
  public String getTriggerApplication()
  {
    return this.mTriggerApplication;
  }
  
  public boolean isDictationRequested()
  {
    return this.mDictationRequested;
  }
  
  public boolean isPartialResultsRequested()
  {
    return this.mPartialResultsRequested;
  }
  
  public boolean isProfanityFilterEnabled()
  {
    return this.mProfanityFilterEnabled;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.serviceapi.GoogleRecognitionParams
 * JD-Core Version:    0.7.0.1
 */