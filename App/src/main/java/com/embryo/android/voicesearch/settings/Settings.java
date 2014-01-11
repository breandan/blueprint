package com.embryo.android.voicesearch.settings;

import android.content.Context;
import android.os.Build;

import com.embryo.android.search.core.GsaPreferenceController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Settings
        implements com.embryo.android.speech.SpeechSettings {
    private final GStaticConfiguration mGStaticConfiguration;

    public Settings(Context paramContext, GsaPreferenceController paramGsaPreferenceController, ExecutorService paramExecutorService) {
        this(new GStaticConfiguration(paramGsaPreferenceController, paramContext.getResources(), paramExecutorService));
    }

    Settings(GStaticConfiguration paramGStaticConfiguration) {
        this.mGStaticConfiguration = paramGStaticConfiguration;
    }

//    private SharedPreferences getPrefs() {
//        return this.mPrefController.getMainPreferences();
//    }

    private boolean isBlacklistedSoundSearchDevice() {
        com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration config = getConfiguration();
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

//    private String setDefaultSpokenLocaleBcp47(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration paramConfiguration) {
//        String str = com.embryo.android.speech.utils.SpokenLanguageUtils.getDefaultMainSpokenLanguageBcp47(Locale.getDefault().toString(), paramConfiguration);
//        getPrefs().edit().putString("spoken-language-bcp-47", str).putBoolean("spoken-language-default", true).apply();
//        return str;
//    }

//    private void verifySpokenLocaleBcp47(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration paramConfiguration) {
//        SharedPreferences localSharedPreferences = getPrefs();
//        String str1 = localSharedPreferences.getString("spoken-language-bcp-47", null);
//        String str2 = Locale.getDefault().toString();
//        if ((str1 != null) && (com.embryo.android.speech.utils.SpokenLanguageUtils.isSupportedBcp47Locale(paramConfiguration, str1))) {
//            boolean bool = str1.equals(com.embryo.android.speech.utils.SpokenLanguageUtils.getDefaultMainSpokenLanguageBcp47(str2, paramConfiguration));
//            if (bool != localSharedPreferences.getBoolean("spoken-language-default", false)) {
//                localSharedPreferences.edit().putBoolean("spoken-language-default", bool).apply();
//            }
//            return;
//        }
//        setDefaultSpokenLocaleBcp47(paramConfiguration);
//    }

    public void addConfigurationListener(ConfigurationChangeListener paramConfigurationChangeListener) {
        this.mGStaticConfiguration.addListener(paramConfigurationChangeListener);
    }

    public void asyncLoad() {
        this.mGStaticConfiguration.asyncLoad();
    }

    public com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration getConfiguration() {
        return this.mGStaticConfiguration.getConfiguration();
    }

//    public String getDebugRecognitionEngineRestrict() {
//        return getPrefs().getString("debugRecognitionEngineRestrict", null);
//    }

    public List<String> getExperimentIds() {
        return new ArrayList<>();
    }

//    public synchronized String getSpokenLocaleBcp47() {
//        String slb47 = getPrefs().getString("spoken-language-bcp-47", null);
//        if (slb47 == null) {
//            slb47 = setDefaultSpokenLocaleBcp47(getConfiguration());
//        }
//        return slb47;
//    }

    public boolean isBluetoothHeadsetEnabled() {
        return false;
    }

    public boolean isDebugAudioLoggingEnabled() {
        return true;
    }

    public boolean isEmbeddedEndpointingEnabled() {
        return true;
    }

    public static abstract interface ConfigurationChangeListener {
        public abstract void onChange(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration paramConfiguration);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Settings

 * JD-Core Version:    0.7.0.1

 */