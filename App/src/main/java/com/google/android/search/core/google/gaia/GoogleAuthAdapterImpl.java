package com.google.android.search.core.google.gaia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import java.io.IOException;

public class GoogleAuthAdapterImpl
  implements GoogleAuthAdapter
{
  public String getTokenWithNotification(Context paramContext, String paramString1, String paramString2, Bundle paramBundle)
    throws UserRecoverableNotifiedException, IOException, GoogleAuthException
  {
    return GoogleAuthUtil.getTokenWithNotification(paramContext, paramString1, paramString2, paramBundle, new Intent("com.google.android.googlequicksearchbox.ACCOUNT_CREDENTIAL_UPDATE"));
  }
  
  public void invalidateToken(Context paramContext, String paramString)
  {
    GoogleAuthUtil.invalidateToken(paramContext, paramString);
  }
  
  public void phoneCredentialsUpdated() {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.gaia.GoogleAuthAdapterImpl
 * JD-Core Version:    0.7.0.1
 */