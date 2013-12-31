package com.google.android.voicesearch.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.GserviceWrapper;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

public class Settings
        implements SpeechSettings {
    private final GStaticConfiguration mGStaticConfiguration;
    private final GsaPreferenceController mPrefController;

    public Settings(Context paramContext, GsaPreferenceController paramGsaPreferenceController, ExecutorService paramExecutorService) {
        this(paramGsaPreferenceController, new GStaticConfiguration(paramGsaPreferenceController, paramContext.getResources(), paramExecutorService, new GserviceWrapper(paramContext.getContentResolver())));
    }

    Settings(GsaPreferenceController paramGsaPreferenceController, GStaticConfiguration paramGStaticConfiguration) {
        this.mPrefController = paramGsaPreferenceController;
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

    public String getDebugRecognitionEngineRestrict() {
        return getPrefs().getString("debugRecognitionEngineRestrict", null);
    }

    public List<String> getExperimentIds() {
        return new ArrayList<>();
    }

    public synchronized String getSpokenLocaleBcp47() {
        String slb47 = getPrefs().getString("spoken-language-bcp-47", null);
        if (slb47 == null) {
            slb47 = setDefaultSpokenLocaleBcp47(getConfiguration());
        }
        return slb47;
    }

    public boolean isBluetoothHeadsetEnabled() {
        return false;
    }

    public boolean isDebugAudioLoggingEnabled() {
        return true;
    }

    public boolean isEmbeddedEndpointingEnabled() {
        return true;
    }

    public boolean isEmbeddedRecognitionOnlyForDebug() {
        return "embeddedOnly".equals(getDebugRecognitionEngineRestrict());
    }

    public boolean isProfanityFilterEnabled() {
        return false;
    }

    public boolean isS3DebugLoggingEnabled() {
        return getPrefs().getBoolean("debugS3Logging", false);
    }

    public boolean isServerEndpointingEnabled() {
        return false;
    }

    public boolean isSoundSearchEnabled() {
        return false;
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