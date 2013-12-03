package com.google.android.voicesearch.ime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.inputmethodcommon.InputMethodSettingsFragment;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.preferences.PreferenceController;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.ui.settings.SettingsActivity;
import com.google.android.voicesearch.VoiceSearchServices;

public class ImePreferences
        extends Activity {
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        VoiceSearchServices localVoiceSearchServices = VelvetServices.get().getVoiceSearchServices();
        localVoiceSearchServices.getVoiceImeSubtypeUpdater().maybeScheduleUpdate(localVoiceSearchServices.getSettings().getConfiguration());
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.setClass(this, SettingsActivity.class);
        localIntent.putExtra(":android:no_headers", true);
        localIntent.putExtra(":android:show_fragment", Settings.class.getName());
        localIntent.putExtra(":android:show_fragment_title", 2131363467);
        startActivity(localIntent);
        finish();
    }

    public static class Settings
            extends InputMethodSettingsFragment {
        private PreferenceController mController;

        public void onCreate(Bundle paramBundle) {
            super.onCreate(paramBundle);
            setInputMethodSettingsCategoryTitle(2131363473);
            setSubtypeEnablerTitle(2131363474);
            this.mController = VelvetServices.get().createPreferenceController(getActivity());
            GsaPreferenceController.useMainPreferences(getPreferenceManager());
            addPreferencesFromResource(2131099660);
            this.mController.handlePreference(getPreferenceScreen());
            this.mController.onCreateComplete(paramBundle);
        }

        public void onDestroy() {
            this.mController.onDestroy();
            super.onDestroy();
        }

        public void onPause() {
            this.mController.onPause();
            super.onPause();
        }

        public void onResume() {
            super.onResume();
            this.mController.onResume();
        }

        public void onSaveInstanceState(Bundle paramBundle) {
            super.onSaveInstanceState(paramBundle);
            this.mController.onSaveInstanceState(paramBundle);
        }

        public void onStop() {
            this.mController.onStop();
            super.onStop();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.ImePreferences

 * JD-Core Version:    0.7.0.1

 */