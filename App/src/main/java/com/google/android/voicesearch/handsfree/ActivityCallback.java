package com.google.android.voicesearch.handsfree;

import android.app.Activity;
import android.content.Intent;

class ActivityCallback {
    private final Activity mActivity;

    ActivityCallback(Activity paramActivity) {
        this.mActivity = paramActivity;
    }

    public void finish() {
        this.mActivity.finish();
    }

    public void startActivity(Intent paramIntent) {
        this.mActivity.startActivity(paramIntent);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.handsfree.ActivityCallback

 * JD-Core Version:    0.7.0.1

 */