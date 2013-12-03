package com.google.android.velvet.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PublicSettingsActivity
        extends Activity {
    private void showAppSettings() {
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.setClass(this, SettingsActivity.class);
        startActivity(localIntent);
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        String str = getIntent().getAction();
        if (("android.search.action.SEARCH_SETTINGS".equals(str)) || ("com.google.android.googlequicksearchbox.action.PRIVACY_SETTINGS".equals(str))) {
            showAppSettings();
        }
        finish();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.settings.PublicSettingsActivity

 * JD-Core Version:    0.7.0.1

 */