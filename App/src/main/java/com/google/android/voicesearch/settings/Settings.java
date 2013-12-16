package com.google.android.voicesearch.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.GserviceWrapper;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nullable;

public class Settings
        implements SpeechSettings {
    private final GsaConfigFlags mConfigFlags;
    private final GStaticConfiguration mGStaticConfiguration;
    private final GsaPreferenceController mPrefController;
    private final SearchConfig mSearchConfig;
    private final SearchSettings mSearchSettings;

    public Settings(Context paramContext, GsaPreferenceController paramGsaPreferenceController, SearchSettings paramSearchSettings, SearchConfig paramSearchConfig, GsaConfigFlags paramGsaConfigFlags, HttpHelper paramHttpHelper, ExecutorService paramExecutorService) {
        this(paramGsaPreferenceController, paramContext, paramSearchSettings, paramSearchConfig, paramGsaConfigFlags, paramHttpHelper, new GStaticConfiguration(paramGsaPreferenceController, paramContext.getResources(), paramExecutorService, new GserviceWrapper(paramContext.getContentResolver())));
    }

    Settings(GsaPreferenceController paramGsaPreferenceController, Context paramContext, SearchSettings paramSearchSettings, SearchConfig paramSearchConfig, GsaConfigFlags paramGsaConfigFlags, HttpHelper paramHttpHelper, GStaticConfiguration paramGStaticConfiguration) {
        this.mPrefController = paramGsaPreferenceController;
        this.mSearchSettings = paramSearchSettings;
        this.mSearchConfig = paramSearchConfig;
        this.mConfigFlags = paramGsaConfigFlags;
        this.mGStaticConfiguration = paramGStaticConfiguration;
        this.mGStaticConfiguration.addListener(new ConfigurationChangeListener() {
            public void onChange(GstaticConfiguration.Configuration paramAnonymousConfiguration) {
                Settings.this.verifySpokenLocaleBcp47(paramAnonymousConfiguration);
            }
        });
    }

    private SharedPreferences getPrefs() {
        return this.mPrefController.getMainPreferences();
    }

    private boolean isBlacklistedSoundSearchDevice() {
        GstaticConfiguration.Configuration config = getConfiguration();
        if (!config.hasSoundSearch()) {
            return false;
        }
        for (String device : config.getSoundSearch().getBlacklistedDevicesList()) {
            if (device.equalsIgnoreCase(Build.MODEL)) {
                return true;
            }
        }
        return false;
    }

    private String setDefaultSpokenLocaleBcp47(GstaticConfiguration.Configuration paramConfiguration) {
        String str = SpokenLanguageUtils.getDefaultMainSpokenLanguageBcp47(Locale.getDefault().toString(), paramConfiguration);
        getPrefs().edit().putString("spoken-language-bcp-47", str).putBoolean("spoken-language-default", true).apply();
        return str;
    }

    private void verifySpokenLocaleBcp47(GstaticConfiguration.Configuration paramConfiguration) {
        SharedPreferences localSharedPreferences = getPrefs();
        String str1 = localSharedPreferences.getString("spoken-language-bcp-47", null);
        String str2 = Locale.getDefault().toString();
        if ((str1 != null) && (SpokenLanguageUtils.isSupportedBcp47Locale(paramConfiguration, str1))) {
            boolean bool = str1.equals(SpokenLanguageUtils.getDefaultMainSpokenLanguageBcp47(str2, paramConfiguration));
            if (bool != localSharedPreferences.getBoolean("spoken-language-default", false)) {
                localSharedPreferences.edit().putBoolean("spoken-language-default", bool).apply();
            }
            return;
        }
        setDefaultSpokenLocaleBcp47(paramConfiguration);
    }

    public void addConfigurationListener(ConfigurationChangeListener paramConfigurationChangeListener) {
        this.mGStaticConfiguration.addListener(paramConfigurationChangeListener);
    }

    public void asyncLoad() {
        this.mGStaticConfiguration.asyncLoad();
    }

    public GstaticConfiguration.Configuration getConfiguration() {
        return this.mGStaticConfiguration.getConfiguration();
    }

    @Nullable
    public GstaticConfiguration.Configuration getConfigurationIfReady() {
        return this.mGStaticConfiguration.getConfigurationIfReady();
    }

    public String getDebugRecognitionEngineRestrict() {
        return getPrefs().getString("debugRecognitionEngineRestrict", null);
    }

    public String getDebugS3SandboxOverride() {
        return getPrefs().getString("s3SandboxOverride", null);
    }

    public long getDefaultActionCountDownMs() {
        return this.mGStaticConfiguration.getConfiguration().getVoiceSearch().getActionCountDownMsec();
    }

    public List<String> getExperimentIds() {
        ArrayList localArrayList = Lists.newArrayListWithExpectedSize(1);
        localArrayList.add(getConfiguration().getId());
        Integer[] arrayOfInteger = this.mConfigFlags.getExperimentIds();
        int i = arrayOfInteger.length;
        for (int j = 0; j < i; j++) {
            localArrayList.add(arrayOfInteger[j].toString());
        }
        for (String str : this.mSearchConfig.getGservicesExperimentIds().split(",")) {
            if (!TextUtils.isEmpty(str)) {
                localArrayList.add(str);
            }
        }
        if (this.mSearchConfig.isSdchEnabledForSerp()) {
            localArrayList.add("sdch");
        }
        if (this.mSearchConfig.isSpdyForSearchResultFetchesEnabled()) {
            localArrayList.add("spdy_search");
        }
        if (this.mSearchConfig.isSpdyForSuggestionsEnabled()) {
            localArrayList.add("spdy_suggest");
        }
        if (this.mSearchConfig.shouldUseChromePrerender()) {
            localArrayList.add("chrome_prerender");
        }
        return localArrayList;
    }

    public String getInstallId() {
        return this.mSearchSettings.getVoiceSearchInstallId();
    }

    public int getLanguagePacksAutoUpdate() {
        return getPrefs().getInt("languagePacksAutoUpdate", 2);
    }

    public void setLanguagePacksAutoUpdate(int strategy) {
        Preconditions.checkArgument((strategy == 0) || (strategy == 1) || (strategy == 2));
        getPrefs().edit().putInt("languagePacksAutoUpdate", strategy).apply();
    }

    public int getPersonalizationValue() {
        return getPrefs().getInt("pref-voice-personalization-status", 0);
    }

    public void setPersonalizationValue(int paramInt) {
        getPrefs().edit().putInt("pref-voice-personalization-status", paramInt).apply();
    }

    public String getS3ServerOverride() {
        return getPrefs().getString("debugS3Server", "");
    }

    public int getServerEndpointingActivityTimeoutMs() {
        return this.mConfigFlags.getServerEndpointingActivityTimeoutMs();
    }

    public Locale getSpokenLocale() {
        return SpokenLanguageUtils.getMainJavaLocaleForBcp47(getConfiguration(), getSpokenLocaleBcp47());
    }

    public synchronized String getSpokenLocaleBcp47() {
        String slb47 = getPrefs().getString("spoken-language-bcp-47", null);
        if (slb47 == null) {
            slb47 = setDefaultSpokenLocaleBcp47(getConfiguration());
        }
        return slb47;
    }

    public String getVoiceSearchTokenType() {
        return this.mSearchConfig.getVoiceSearchTokenType();
    }

    public boolean hasEverUsedVoiceSearch() {
        return getPrefs().getBoolean("hasEverUsedVoiceSearch", false);
    }

    public boolean isBluetoothHeadsetEnabled() {
        boolean bool1 = this.mSearchConfig.isBluetoothEnabled();
        boolean bool2 = false;
        if (bool1) {
            boolean bool3 = getPrefs().getBoolean("bluetoothHeadset", false);
            bool2 = false;
            if (bool3) {
                bool2 = true;
            }
        }
        return bool2;
    }

    public boolean isDebugAudioLoggingEnabled() {
        return this.mSearchConfig.isDebugAudioLoggingEnabled();
    }

    public boolean isDefaultSpokenLanguage() {
        return getPrefs().getBoolean("spoken-language-default", false);
    }

    public boolean isEmbeddedEndpointingEnabled() {
        return this.mConfigFlags.isEmbeddedEndpointingEnabled();
    }

    public boolean isEmbeddedRecognitionOnlyForDebug() {
        return "embeddedOnly".equals(getDebugRecognitionEngineRestrict());
    }

    public boolean isNetworkRecognitionOnlyForDebug() {
        return "networkOnly".equals(getDebugRecognitionEngineRestrict());
    }

    public boolean isPersonalizationEnabled() {
        return getPersonalizationValue() == 4;
    }

    public boolean isProfanityFilterEnabled() {
        return getPrefs().getBoolean("profanityFilter", true);
    }

    public boolean isS3DebugLoggingEnabled() {
        return getPrefs().getBoolean("debugS3Logging", false);
    }

    public boolean isServerEndpointingEnabled() {
        return this.mConfigFlags.isServerEndpointingEnabled();
    }

    public boolean isSoundSearchEnabled() {
        return ((this.mSearchConfig.getSoundSearchEnabled()) && (!isBlacklistedSoundSearchDevice())) || (getConfiguration().hasDebug());
    }

    public void setHasEverUsedVoiceSearch() {
        getPrefs().edit().putBoolean("hasEverUsedVoiceSearch", true).apply();
    }

    public synchronized void setSpokenLanguageBcp47(String paramString, boolean paramBoolean) {
        SharedPreferences localSharedPreferences = getPrefs();
        if (!paramString.equals(localSharedPreferences.getString("spoken-language-bcp-47", null))) {
            localSharedPreferences.edit().putString("spoken-language-bcp-47", paramString).putBoolean("spoken-language-default", paramBoolean).apply();
        }
    }

    public static abstract interface ConfigurationChangeListener {
        public abstract void onChange(GstaticConfiguration.Configuration paramConfiguration);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.settings.Settings

 * JD-Core Version:    0.7.0.1

 */