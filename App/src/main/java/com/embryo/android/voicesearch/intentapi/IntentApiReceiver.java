package com.embryo.android.voicesearch.intentapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.embryo.android.speech.utils.SpokenLanguageUtils;
import com.google.android.velvet.VelvetServices;
import com.embryo.android.voicesearch.settings.Settings;

import javax.annotation.Nullable;

public class IntentApiReceiver
        extends BroadcastReceiver {
    @Nullable
    Bundle getExtras(Context paramContext, Intent paramIntent) {
        Bundle localBundle;
        if (!"android.speech.action.GET_LANGUAGE_DETAILS".equals(paramIntent.getAction())) {
            localBundle = null;
        }
        Settings localSettings;
        do {
            return localBundle;
            localBundle = new Bundle();
            localSettings = VelvetServices.get().getVoiceSearchServices().getSettings();
            localBundle.putString("android.speech.extra.LANGUAGE_PREFERENCE", localSettings.getSpokenLocaleBcp47());
        }
        while (paramIntent.getBooleanExtra("android.speech.extra.ONLY_RETURN_LANGUAGE_PREFERENCE", false));
        localBundle.putStringArrayList("android.speech.extra.SUPPORTED_LANGUAGES", SpokenLanguageUtils.getSupportedBcp47Locales(localSettings.getConfiguration()));
        localBundle.putStringArrayList("android.speech.extra.SUPPORTED_LANGUAGE_NAMES", SpokenLanguageUtils.getSupportedDisplayNames(localSettings.getConfiguration()));
        return localBundle;
    }

    public void onReceive(Context paramContext, Intent paramIntent) {
        if (!isOrderedBroadcast()) {
        }
        Bundle localBundle;
        do {
            return;
            localBundle = getExtras(paramContext, paramIntent);
        } while (localBundle == null);
        setResultExtras(localBundle);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     IntentApiReceiver

 * JD-Core Version:    0.7.0.1

 */