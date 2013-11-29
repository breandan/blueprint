package com.google.android.speech.helper;

import com.google.android.speech.callback.SimpleCallback;

public abstract interface AccountHelper
{
  public abstract String getAccountName();
  
  public abstract void getMainGmailAccount(SimpleCallback<String> paramSimpleCallback);
  
  public abstract boolean hasAccount();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.helper.AccountHelper
 * JD-Core Version:    0.7.0.1
 */