package com.google.android.sidekick.main.contextprovider;

import android.os.Bundle;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.AccountContext;

public class AccountContextProvider
{
  private final LoginHelper mLoginHelper;
  
  public AccountContextProvider(LoginHelper paramLoginHelper)
  {
    this.mLoginHelper = paramLoginHelper;
  }
  
  public void addAccountContext(CardRenderingContext paramCardRenderingContext)
  {
    Bundle localBundle = AccountContext.create(this.mLoginHelper.getAccount());
    paramCardRenderingContext.putSpecificRenderingContextIfAbsent(AccountContext.BUNDLE_KEY, localBundle);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.AccountContextProvider
 * JD-Core Version:    0.7.0.1
 */