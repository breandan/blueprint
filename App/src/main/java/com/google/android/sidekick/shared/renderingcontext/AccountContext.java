package com.google.android.sidekick.shared.renderingcontext;

import android.accounts.Account;
import android.os.Bundle;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import javax.annotation.Nullable;

public class AccountContext
{
  public static final String BUNDLE_KEY = AccountContext.class.getName();
  private final Bundle mBundle;
  
  private AccountContext(Bundle paramBundle)
  {
    this.mBundle = paramBundle;
  }
  
  public static Bundle create(@Nullable Account paramAccount)
  {
    Bundle localBundle = new Bundle();
    if (paramAccount != null) {
      localBundle.putParcelable("active_account", paramAccount);
    }
    return localBundle;
  }
  
  public static AccountContext fromCardContainer(PredictiveCardContainer paramPredictiveCardContainer)
  {
    Bundle localBundle = (Bundle)paramPredictiveCardContainer.getCardRenderingContext().getSpecificRenderingContext(BUNDLE_KEY);
    if (localBundle != null) {
      return new AccountContext(localBundle);
    }
    return null;
  }
  
  @Nullable
  public Account getActiveUserAccount()
  {
    return (Account)this.mBundle.getParcelable("active_account");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.renderingcontext.AccountContext
 * JD-Core Version:    0.7.0.1
 */