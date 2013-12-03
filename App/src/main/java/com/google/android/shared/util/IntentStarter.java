package com.google.android.shared.util;

import android.content.Context;
import android.content.Intent;

public abstract interface IntentStarter
        extends SimpleIntentStarter {
    public abstract boolean startActivityForResult(Intent paramIntent, ResultCallback paramResultCallback);

    public static abstract interface ResultCallback {
        public abstract void onResult(int paramInt, Intent paramIntent, Context paramContext);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.IntentStarter

 * JD-Core Version:    0.7.0.1

 */