package com.google.android.voicesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.search.core.preferences.VoiceSettingsFragment;
import com.google.android.velvet.ui.settings.SettingsActivity;

public class VoiceSearchPreferences
        extends Activity {
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.setClass(this, SettingsActivity.class);
        localIntent.putExtra(":android:no_headers", true);
        localIntent.putExtra(":android:show_fragment", VoiceSettingsFragment.class.getName());
        startActivity(localIntent);
        finish();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.VoiceSearchPreferences

 * JD-Core Version:    0.7.0.1

 */