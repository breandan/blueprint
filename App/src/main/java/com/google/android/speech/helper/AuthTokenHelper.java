package com.google.android.speech.helper;

import java.util.Collection;

import javax.annotation.Nullable;

public abstract interface AuthTokenHelper {
    @Nullable
    public abstract Collection<String> blockingGetAllTokens(String paramString, long paramLong);

    public abstract void invalidateToken(String paramString);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.helper.AuthTokenHelper

 * JD-Core Version:    0.7.0.1

 */