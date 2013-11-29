package com.google.android.gms.internal;

import android.accounts.Account;
import android.util.Log;

public class fu
{
  public static String a(Account paramAccount)
  {
    if (paramAccount == null) {
      return "null";
    }
    if (Log.isLoggable("GCoreUlr", 2)) {
      return paramAccount.name;
    }
    return "account#" + paramAccount.name.hashCode() % 20 + "#";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.fu
 * JD-Core Version:    0.7.0.1
 */