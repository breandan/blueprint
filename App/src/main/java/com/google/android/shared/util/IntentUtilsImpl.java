package com.google.android.shared.util;

import android.content.Context;
import android.content.Intent;

public class IntentUtilsImpl
        implements IntentUtils {
    public boolean isIntentHandled(Context paramContext, Intent paramIntent) {
        boolean bool1 = paramContext.getPackageManager().queryIntentActivities(paramIntent, 0).isEmpty();
        boolean bool2 = false;
        if (!bool1) {
            bool2 = true;
        }
        return bool2;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.IntentUtilsImpl

 * JD-Core Version:    0.7.0.1

 */