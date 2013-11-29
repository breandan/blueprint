package com.google.android.voicesearch.intentapi;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.speech.audio.AudioUtils.Encoding;
import com.google.common.base.Strings;
import javax.annotation.Nullable;

public class IntentApiParams
{
  public static final String EXTRA_GET_AUDIO = "android.speech.extra.GET_AUDIO_FORMAT";
  public static final String EXTRA_PROFANITY_FILTER = "android.speech.extra.PROFANITY_FILTER";
  private final boolean mAutoScript;
  private final String mCallingPackage;
  private final String mLanguage;
  private final int mMaxResults;
  private final Bundle mPendingBundleIntent;
  private final PendingIntent mPendingIntent;
  private final Boolean mProfanityFilterEnabled;
  private final String mPrompt;
  private final boolean mReturnAudio;
  
  public IntentApiParams(Intent paramIntent, String paramString)
  {
    this.mAutoScript = paramIntent.getBooleanExtra("EXPERIMENTAL_AUTO_SCRIPT", false);
    this.mPendingIntent = ((PendingIntent)paramIntent.getParcelableExtra("android.speech.extra.RESULTS_PENDINGINTENT"));
    this.mPendingBundleIntent = paramIntent.getBundleExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE");
    this.mReturnAudio = getReturnAudio(paramIntent);
    this.mPrompt = paramIntent.getStringExtra("android.speech.extra.PROMPT");
    this.mMaxResults = paramIntent.getIntExtra("android.speech.extra.MAX_RESULTS", -1);
    this.mLanguage = paramIntent.getStringExtra("android.speech.extra.LANGUAGE");
    if (this.mAutoScript)
    {
      this.mCallingPackage = "auto-script";
      if (!paramIntent.hasExtra("android.speech.extra.PROFANITY_FILTER")) {
        break label134;
      }
    }
    label134:
    for (Boolean localBoolean = Boolean.valueOf(paramIntent.getBooleanExtra("android.speech.extra.PROFANITY_FILTER", false));; localBoolean = null)
    {
      this.mProfanityFilterEnabled = localBoolean;
      return;
      this.mCallingPackage = getCallingPackage(paramIntent, this.mPendingIntent, paramString);
      break;
    }
  }
  
  private String getCallingPackage(Intent paramIntent, PendingIntent paramPendingIntent, String paramString)
  {
    if (paramString == null)
    {
      if (paramPendingIntent != null) {
        paramString = paramPendingIntent.getTargetPackage();
      }
    }
    else
    {
      if (("android".equals(paramString)) && (paramIntent.hasExtra("calling_package"))) {
        paramString = paramIntent.getStringExtra("calling_package");
      }
      return Strings.nullToEmpty(paramString);
    }
    Log.e("IntentApiParams", "ACTION_RECOGNIZE_SPEECH intent called incorrectly. Maybe you called startActivity, but you should have called startActivityForResult (or otherwise included a pending intent).");
    return "";
  }
  
  private boolean getReturnAudio(Intent paramIntent)
  {
    String str = paramIntent.getStringExtra("android.speech.extra.GET_AUDIO_FORMAT");
    if (AudioUtils.Encoding.AMR.getMimeType().equals(str)) {
      return true;
    }
    Log.w("IntentApiParams", "The audio format is not supported [requested=" + str + " supported=" + AudioUtils.Encoding.AMR.getMimeType() + "]");
    return false;
  }
  
  public String getCallingPackage()
  {
    return this.mCallingPackage;
  }
  
  public String getLanguage()
  {
    return this.mLanguage;
  }
  
  public int getMaxResults()
  {
    return this.mMaxResults;
  }
  
  @Nullable
  public Bundle getPendingBundleIntent()
  {
    return this.mPendingBundleIntent;
  }
  
  @Nullable
  public PendingIntent getPendingIntent()
  {
    return this.mPendingIntent;
  }
  
  public boolean getProfanityFilterEnabled(boolean paramBoolean)
  {
    if (this.mProfanityFilterEnabled != null) {
      paramBoolean = this.mProfanityFilterEnabled.booleanValue();
    }
    return paramBoolean;
  }
  
  @Nullable
  public String getPrompt()
  {
    return this.mPrompt;
  }
  
  public boolean isAutoScript()
  {
    return this.mAutoScript;
  }
  
  public boolean isReturnAudio()
  {
    return this.mReturnAudio;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.intentapi.IntentApiParams
 * JD-Core Version:    0.7.0.1
 */