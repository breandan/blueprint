package com.google.android.sidekick.main.gcm;

import android.accounts.Account;
import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GcmUtil
{
  public static String accountHashFor(Account paramAccount)
  {
    try
    {
      String str = accountHashForImpl(paramAccount);
      return str;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new AssertionError("SHA1 unsupported");
    }
  }
  
  private static String accountHashForImpl(Account paramAccount)
    throws NoSuchAlgorithmException
  {
    String str = paramAccount.type + '|' + paramAccount.name;
    MessageDigest localMessageDigest = MessageDigest.getInstance("SHA1");
    localMessageDigest.update(str.getBytes());
    return Base64.encodeToString(localMessageDigest.digest(), 3);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.gcm.GcmUtil
 * JD-Core Version:    0.7.0.1
 */