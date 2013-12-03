package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.net.Uri;

import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.StopNavigationAction;

public class StopNavigationActionExecutor
        extends IntentActionExecutor<StopNavigationAction> {
    public StopNavigationActionExecutor(IntentStarter paramIntentStarter) {
        super(paramIntentStarter);
    }

    protected Intent[] getExecuteIntents(StopNavigationAction paramStopNavigationAction) {
        Intent localIntent = getIntent();
        if (localIntent != null) {
            return new Intent[]{localIntent};
        }
        return new Intent[0];
    }

    protected Intent getIntent() {
        return new Intent("android.intent.action.VIEW", Uri.parse("google.navigation:quitquitquit=true"));
    }

    protected Intent[] getOpenExternalAppIntents(StopNavigationAction paramStopNavigationAction) {
        Intent localIntent = getIntent();
        if (localIntent != null) {
            return new Intent[]{localIntent};
        }
        return new Intent[0];
    }

    protected Intent[] getProberIntents(StopNavigationAction paramStopNavigationAction) {
        return getExecuteIntents(paramStopNavigationAction);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.executor.StopNavigationActionExecutor

 * JD-Core Version:    0.7.0.1

 */