package com.google.android.search.core.google.gaia;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import java.io.IOException;

public abstract interface GoogleAuthAdapter
{
  public abstract String getTokenWithNotification(Context paramContext, String paramString1, String paramString2, Bundle paramBundle)
    throws UserRecoverableNotifiedException, IOException, GoogleAuthException;
  
  public abstract void invalidateToken(Context paramContext, String paramString);
  
  public abstract void phoneCredentialsUpdated();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.gaia.GoogleAuthAdapter
 * JD-Core Version:    0.7.0.1
 */