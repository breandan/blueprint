package com.google.android.shared.util;

import android.content.Intent;

public abstract interface SimpleIntentStarter {
    public abstract boolean resolveIntent(Intent paramIntent);

    public abstract boolean startActivity(Intent... paramVarArgs);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.SimpleIntentStarter

 * JD-Core Version:    0.7.0.1

 */