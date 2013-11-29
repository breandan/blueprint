package com.google.android.search.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.preferences.SharedPreferencesExt.Editor;
import com.google.android.velvet.presenter.FirstUseCardHandler.FirstUseCardType;
import com.google.android.velvet.util.Cursors;
import com.google.android.velvet.util.Cursors.CursorRowHandler;
import com.google.common.base.Preconditions;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GsaPreferenceUpgrader
  implements Cursors.CursorRowHandler
{
  private static final Uri OLD_SETTINGS_PROVIDER_URI = Uri.parse("content://com.google.android.voicesearch/prefs");
  private final Context mContext;
  private SharedPreferencesExt.Editor mMainEditor;
  private final SharedPreferencesExt mMainPrefs;
  private SharedPreferencesExt.Editor mStartupEditor;
  private final SharedPreferencesExt mStartupPrefs;
  private final String mVersionKey;
  
  private GsaPreferenceUpgrader(Context paramContext, SharedPreferencesExt paramSharedPreferencesExt1, SharedPreferencesExt paramSharedPreferencesExt2, String paramString)
  {
    this.mContext = paramContext;
    this.mStartupPrefs = paramSharedPreferencesExt1;
    this.mMainPrefs = paramSharedPreferencesExt2;
    this.mVersionKey = paramString;
  }
  
  private EditorSelector createSettingsEditorSelector(final Map<String, ?> paramMap)
  {
    new EditorSelector()
    {
      public SharedPreferencesExt.Editor selectEditor(String paramAnonymousString)
      {
        SharedPreferencesExt.Editor localEditor;
        if (paramAnonymousString.equals("google_account"))
        {
          GsaPreferenceUpgrader.this.getMainEditor().remove(paramAnonymousString);
          localEditor = GsaPreferenceUpgrader.this.getStartupEditor();
        }
        Object localObject;
        boolean bool1;
        do
        {
          do
          {
            return localEditor;
            if ((paramAnonymousString.equals("web_corpora_json")) || (paramAnonymousString.equals("web_corpora_json_url")))
            {
              GsaPreferenceUpgrader.this.getMainEditor().remove(paramAnonymousString);
              return null;
            }
            if ((!paramAnonymousString.equals("lastloc")) && (!paramAnonymousString.equals("session_key")) && (!paramAnonymousString.equals("web_corpora_config")) && (!paramAnonymousString.equals("gstatic_configuration_data")) && (!paramAnonymousString.equals("gstatic_configuration_override_1")) && (!paramAnonymousString.startsWith("configuration_bytes_key_"))) {
              break;
            }
            localObject = paramMap.get(paramAnonymousString);
            localEditor = null;
          } while (localObject == null);
          bool1 = localObject instanceof String;
          localEditor = null;
        } while (!bool1);
        try
        {
          byte[] arrayOfByte = Base64.decode((String)localObject, 0);
          GsaPreferenceUpgrader.this.getMainEditor().putBytes(paramAnonymousString, arrayOfByte);
          return null;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          return null;
        }
        int i;
        if (!NowOptInSettingsImpl.isStartupSetting(paramAnonymousString))
        {
          boolean bool2 = paramAnonymousString.equals("enableTestPlatformLogging");
          i = 0;
          if (!bool2) {}
        }
        else
        {
          i = 1;
        }
        if (i != 0) {
          return GsaPreferenceUpgrader.this.getStartupEditor();
        }
        return GsaPreferenceUpgrader.this.getMainEditor();
      }
    };
  }
  
  private void doApplySettings(Map<String, ?> paramMap, EditorSelector paramEditorSelector)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str = (String)localEntry.getKey();
      SharedPreferencesExt.Editor localEditor = paramEditorSelector.selectEditor(str);
      if (localEditor != null)
      {
        Object localObject = localEntry.getValue();
        if ((localObject instanceof Boolean)) {
          localEditor.putBoolean(str, ((Boolean)localObject).booleanValue());
        } else if ((localObject instanceof Integer)) {
          localEditor.putInt(str, ((Integer)localObject).intValue());
        } else if ((localObject instanceof Long)) {
          localEditor.putLong(str, ((Long)localObject).longValue());
        } else if ((localObject instanceof String)) {
          localEditor.putString(str, (String)localObject);
        } else if ((localObject instanceof Set)) {
          localEditor.putStringSet(str, (Set)localObject);
        } else if ((localObject instanceof Float)) {
          localEditor.putFloat(str, ((Float)localObject).floatValue());
        }
      }
    }
  }
  
  private void doClearCorporaConfigIfAppropriate()
  {
    if (TextUtils.isEmpty(this.mMainPrefs.getString("web_corpora_config_url", null))) {
      getMainEditor().remove("web_corpora_config");
    }
  }
  
  private void doCopyPersonalizedSearchValue()
  {
    String str = this.mMainPrefs.getString("personalized_search", null);
    SharedPreferencesExt.Editor localEditor = getMainEditor();
    if (!"0".equals(str)) {}
    for (boolean bool = true;; bool = false)
    {
      localEditor.putBoolean("personalized_search_bool", bool);
      return;
    }
  }
  
  private void doFetchGservicesKeysToSettings()
  {
    getMainEditor().putString("gservices_overrides", GservicesUpdateTask.getGservicesOverridesJson(this.mContext));
  }
  
  private void doPutSourceClickUpgradeNeededBoolean()
  {
    getMainEditor().putBoolean("need_source_stats_upgrade", true);
  }
  
  private void doStoreSettings(Map<String, ?> paramMap, final SharedPreferencesExt.Editor paramEditor)
  {
    Preconditions.checkNotNull(paramMap);
    doApplySettings(paramMap, new EditorSelector()
    {
      public SharedPreferencesExt.Editor selectEditor(String paramAnonymousString)
      {
        return paramEditor;
      }
    });
  }
  
  private void doUpgrade(int paramInt1, int paramInt2)
  {
    boolean bool1 = true;
    boolean bool2;
    if (paramInt1 < paramInt2)
    {
      bool2 = bool1;
      Preconditions.checkArgument(bool2);
      if (paramInt2 < 6) {
        break label74;
      }
    }
    for (;;)
    {
      Preconditions.checkArgument(bool1);
      this.mStartupPrefs.delayWrites();
      this.mMainPrefs.delayWrites();
      try
      {
        doUpgradeInternal(paramInt1, paramInt2);
        return;
      }
      finally
      {
        label74:
        this.mStartupPrefs.allowWrites();
        this.mMainPrefs.allowWrites();
      }
      bool2 = false;
      break;
      bool1 = false;
    }
  }
  
  private void doUpgradeAlarmSettings()
  {
    Map localMap = getXmlPrefsAndDelete("AlarmUtils");
    if (localMap == null) {}
    for (;;)
    {
      return;
      Iterator localIterator = localMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str1 = (String)localEntry.getKey();
        Object localObject = localEntry.getValue();
        if ((str1.endsWith("_StartTimeMillis")) && ((localObject instanceof Long)))
        {
          String str2 = "AlarmStartTimeMillis_" + str1.substring(0, str1.length() - "_StartTimeMillis".length());
          getMainEditor().putLong(str2, ((Long)localObject).longValue());
        }
      }
    }
  }
  
  private void doUpgradeDefaultSettings()
  {
    Map localMap = getXmlPrefsAndDelete("com.google.android.googlequicksearchbox_preferences");
    if (localMap == null) {
      return;
    }
    doApplySettings(localMap, createSettingsEditorSelector(localMap));
  }
  
  private void doUpgradeInternal(int paramInt1, int paramInt2)
  {
    this.mStartupEditor = this.mStartupPrefs.edit();
    if (paramInt1 < 1)
    {
      Map localMap = getXmlPrefsAndDelete("StartupSettings");
      if (localMap != null)
      {
        Object localObject = localMap.get(this.mVersionKey);
        if ((localObject != null) && ((localObject instanceof Integer)))
        {
          int i = ((Integer)localObject).intValue();
          if ((i >= 1) && (i <= 2))
          {
            paramInt1 = i;
            doStoreSettings(localMap, this.mStartupEditor);
          }
        }
      }
    }
    if (paramInt1 < 1)
    {
      doUpgradeVoiceSearchSettings();
      doUpgradeAlarmSettings();
      doUpgradePredictiveCardsOptInSettings();
      doUpgradeDefaultSettings();
    }
    if (paramInt1 < 2) {
      doFetchGservicesKeysToSettings();
    }
    if (paramInt1 < 3) {
      doUpgradeSearchSettings();
    }
    this.mStartupEditor.apply();
    if (this.mMainEditor != null) {
      this.mMainEditor.apply();
    }
    if (paramInt1 < 5) {
      doCopyPersonalizedSearchValue();
    }
    if (paramInt1 < 6) {
      doClearCorporaConfigIfAppropriate();
    }
    if (paramInt1 < 8) {
      doPutSourceClickUpgradeNeededBoolean();
    }
    if (paramInt1 < 9) {
      doUpgradeNowFirstUseSettings();
    }
    if (paramInt1 < 10) {
      doUpgradeSafeSearch();
    }
    getStartupEditor().putInt(this.mVersionKey, paramInt2);
    this.mStartupEditor.apply();
    this.mStartupEditor = null;
    if (this.mMainEditor != null)
    {
      this.mMainEditor.apply();
      this.mMainEditor = null;
    }
  }
  
  private void doUpgradeNowFirstUseSettings()
  {
    if (this.mMainPrefs.getBoolean("promo_card_dismissed", false))
    {
      getMainEditor().putBoolean(FirstUseCardHandler.FirstUseCardType.INTRO_CARD.getDismissedPrefKey(), true);
      getMainEditor().putBoolean(FirstUseCardHandler.FirstUseCardType.OUTRO_CARD.getDismissedPrefKey(), true);
    }
    if (this.mMainPrefs.getBoolean("swipe_card_dismissed", false)) {
      getMainEditor().putBoolean("card_swiped_for_dismiss", true);
    }
  }
  
  private void doUpgradePredictiveCardsOptInSettings()
  {
    Map localMap = getXmlPrefsAndDelete("PredictiveCardsOptInSettings");
    if (localMap == null) {
      return;
    }
    doApplySettings(localMap, createSettingsEditorSelector(localMap));
  }
  
  private void doUpgradeSafeSearch()
  {
    boolean bool = this.mMainPrefs.contains("safe_search");
    String str = this.mMainPrefs.getString("safe_search_settings", SearchUrlHelper.SAFE_SEARCH_TRIMODAL_IMAGES);
    if ((SearchUrlHelper.SAFE_SEARCH_TRIMODAL_STRICT.equals(str)) && (!bool)) {
      getMainEditor().putBoolean("safe_search", true);
    }
    getMainEditor().remove("safe_search_settings");
    if ((this.mMainPrefs.getBoolean("safe_search_bimodal", false)) && (!bool)) {
      getMainEditor().putBoolean("safe_search", true);
    }
    getMainEditor().remove("safe_search_bimodal");
  }
  
  private void doUpgradeSearchSettings()
  {
    Map localMap = getXmlPrefsAndDelete("SearchSettings");
    if (localMap == null) {
      return;
    }
    doApplySettings(localMap, createSettingsEditorSelector(localMap));
  }
  
  private void doUpgradeVoiceSearchSettings()
  {
    try
    {
      if (this.mMainPrefs.getBoolean("settings_upgraded", false))
      {
        getMainEditor().remove("settings_upgraded");
        return;
      }
      Cursors.iterateCursor(this, this.mContext.getContentResolver().query(OLD_SETTINGS_PROVIDER_URI, null, null, null, null));
      return;
    }
    catch (Exception localException)
    {
      Log.e("Search.GsaPreferenceUpgrader", "Error during voice search settings upgrade.");
    }
  }
  
  private SharedPreferencesExt.Editor getMainEditor()
  {
    if (this.mMainEditor == null) {
      this.mMainEditor = this.mMainPrefs.edit();
    }
    return this.mMainEditor;
  }
  
  private SharedPreferencesExt.Editor getStartupEditor()
  {
    Preconditions.checkNotNull(this.mStartupEditor);
    return this.mStartupEditor;
  }
  
  private Map<String, ?> getXmlPrefsAndDelete(String paramString)
  {
    Object localObject1 = new File(new File(this.mContext.getApplicationInfo().dataDir, "shared_prefs"), paramString + ".xml");
    File localFile = new File(((File)localObject1).getPath() + ".bak");
    if (localFile.exists())
    {
      ((File)localObject1).delete();
      localObject1 = localFile;
    }
    if (((File)localObject1).exists()) {
      try
      {
        Map localMap = this.mContext.getSharedPreferences(paramString, 0).getAll();
        return localMap;
      }
      finally
      {
        ((File)localObject1).delete();
      }
    }
    return null;
  }
  
  public static void upgrade(Context paramContext, SharedPreferencesExt paramSharedPreferencesExt1, SharedPreferencesExt paramSharedPreferencesExt2, String paramString, int paramInt1, int paramInt2)
  {
    new GsaPreferenceUpgrader(paramContext, paramSharedPreferencesExt1, paramSharedPreferencesExt2, paramString).doUpgrade(paramInt1, paramInt2);
  }
  
  public void handleCurrentRow(Cursor paramCursor)
  {
    String str1 = paramCursor.getString(0);
    String str2 = paramCursor.getString(1);
    if ("profanityFilter".equals(str1)) {
      getMainEditor().putBoolean(str1, Boolean.parseBoolean(str2));
    }
    for (;;)
    {
      return;
      if ("actual_language_setting".equals(str1))
      {
        getMainEditor().putString("spoken-language-bcp-47", str2);
        return;
      }
      if ("pref-voice-personalization-status".equals(str1)) {
        try
        {
          if (Integer.parseInt(str2) == 4)
          {
            getMainEditor().putBoolean("personalizedResults", true);
            return;
          }
        }
        catch (NumberFormatException localNumberFormatException) {}
      }
    }
  }
  
  private static abstract interface EditorSelector
  {
    public abstract SharedPreferencesExt.Editor selectEditor(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GsaPreferenceUpgrader
 * JD-Core Version:    0.7.0.1
 */