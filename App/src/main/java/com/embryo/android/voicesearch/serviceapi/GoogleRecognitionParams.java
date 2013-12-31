package com.embryo.android.voicesearch.serviceapi;

import android.content.Intent;
import android.util.Log;

import com.google.common.base.Strings;

public class GoogleRecognitionParams {
    private final boolean mDictationRequested;
    private final boolean mPartialResultsRequested;
    private final boolean mProfanityFilterEnabled;
    private final String mSpokenBcp47Locale;
    private final String mTriggerApplication;

    public GoogleRecognitionParams(Intent paramIntent, com.embryo.android.voicesearch.settings.Settings paramSettings) {
        mDictationRequested = initDictationRequested(paramIntent);
        mPartialResultsRequested = initPartialResultsRequested(paramIntent);
        mProfanityFilterEnabled = initProfanityFilterEnabled(paramSettings, paramIntent);
        mSpokenBcp47Locale = initSpokenBcp47Locale(paramSettings, paramIntent);
        mTriggerApplication = initTriggerApplication(paramIntent);
    }

    private boolean initDictationRequested(Intent paramIntent) {
        return paramIntent.getBooleanExtra("android.speech.extra.DICTATION_MODE", false);
    }

    private boolean initPartialResultsRequested(Intent paramIntent) {
        return paramIntent.getBooleanExtra("android.speech.extra.PARTIAL_RESULTS", false);
    }

    private boolean initProfanityFilterEnabled(com.embryo.android.voicesearch.settings.Settings paramSettings, Intent paramIntent) {
        return paramIntent.getBooleanExtra("android.speech.extra.PROFANITY_FILTER", paramSettings.isProfanityFilterEnabled());
    }

    private String initSpokenBcp47Locale(com.embryo.android.voicesearch.settings.Settings paramSettings, Intent paramIntent) {
        String str = paramIntent.getStringExtra("android.speech.extra.LANGUAGE");
        if (str != null) {
            if (com.embryo.android.speech.utils.SpokenLanguageUtils.getLanguageDialect(paramSettings.getConfiguration(), str) != null) {
                return str;
            }
            com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Dialect localDialect = com.embryo.android.speech.utils.SpokenLanguageUtils.getSpokenLanguageByJavaLocale(paramSettings.getConfiguration(), str);
            if (localDialect != null) {
                Log.w("GoogleRecognitionParams", "The locale should be specified in BCP47");
                return localDialect.getBcp47Locale();
            }
        }
        return paramSettings.getSpokenLocaleBcp47();
    }

    private String initTriggerApplication(Intent paramIntent) {
        return Strings.nullToEmpty(paramIntent.getStringExtra("calling_package"));
    }

    public String getSpokenBcp47Locale() {
        return this.mSpokenBcp47Locale;
    }

    public String getTriggerApplication() {
        return this.mTriggerApplication;
    }

    public boolean isDictationRequested() {
        return this.mDictationRequested;
    }

    public boolean isPartialResultsRequested() {
        return this.mPartialResultsRequested;
    }

    public boolean isProfanityFilterEnabled() {
        return this.mProfanityFilterEnabled;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     GoogleRecognitionParams

 * JD-Core Version:    0.7.0.1

 */