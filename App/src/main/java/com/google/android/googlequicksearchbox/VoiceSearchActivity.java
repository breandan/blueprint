package com.google.android.googlequicksearchbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class VoiceSearchActivity
        extends Activity {
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        Intent localIntent = new Intent("android.speech.action.WEB_SEARCH");
        localIntent.fillIn(getIntent(), 0);
        localIntent.setFlags(0xFF7FFFFF & localIntent.getFlags());
        startActivity(SearchActivity.fillInTargetActivityClass(this, localIntent));
        finish();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.googlequicksearchbox.VoiceSearchActivity

 * JD-Core Version:    0.7.0.1

 */