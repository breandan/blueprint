package com.google.android.search.core.google.gaia;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import java.io.IOException;

public class AccountManagerGoogleAuthAdapter
  implements GoogleAuthAdapter
{
  private final AccountManager mAccountManager;
  
  public AccountManagerGoogleAuthAdapter(AccountManager paramAccountManager)
  {
    this.mAccountManager = paramAccountManager;
  }
  
  public String getTokenWithNotification(Context paramContext, String paramString1, String paramString2, Bundle paramBundle)
    throws UserRecoverableNotifiedException, IOException, GoogleAuthException
  {
    try
    {
      String str = this.mAccountManager.blockingGetAuthToken(new Account(paramString1, "com.google"), paramString2, true);
      return str;
    }
    catch (OperationCanceledException localOperationCanceledException)
    {
      throw new IOException(localOperationCanceledException);
    }
    catch (AuthenticatorException localAuthenticatorException)
    {
      throw new UserRecoverableNotifiedException(localAuthenticatorException.getMessage());
    }
  }
  
  public void invalidateToken(Context paramContext, String paramString)
  {
    this.mAccountManager.invalidateAuthToken("com.google", paramString);
  }
  
  public void phoneCredentialsUpdated() {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.gaia.AccountManagerGoogleAuthAdapter
 * JD-Core Version:    0.7.0.1
 */