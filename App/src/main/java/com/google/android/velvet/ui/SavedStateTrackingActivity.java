package com.google.android.velvet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SavedStateTrackingActivity
        extends Activity {
    private SavedStateTracker mSavedState = new SavedStateTracker();

    public boolean haveSavedState() {
        return this.mSavedState.haveSavedState();
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        this.mSavedState.onActivityResult();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        this.mSavedState.onCreate();
    }

    protected void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        this.mSavedState.onNewIntent();
    }

    protected void onRestart() {
        super.onRestart();
        this.mSavedState.onRestart();
    }

    protected void onResume() {
        super.onResume();
        this.mSavedState.onResume();
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        this.mSavedState.onSaveInstanceState();
    }

    protected void onStart() {
        super.onStart();
        this.mSavedState.onStart();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.SavedStateTrackingActivity

 * JD-Core Version:    0.7.0.1

 */