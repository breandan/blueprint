package com.google.android.sidekick.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider.UpdateCorporaService;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;

public class AccountsChangedReceiver
  extends BroadcastReceiver
{
  private static final String TAG = Tag.getTag(AccountsChangedReceiver.class);
  
  private void handleAccountsChanged(Context paramContext)
  {
    CoreSearchServices localCoreSearchServices = VelvetServices.get().getCoreServices();
    SearchConfig localSearchConfig = localCoreSearchServices.getConfig();
    LoginHelper localLoginHelper = localCoreSearchServices.getLoginHelper();
    NowOptInSettings localNowOptInSettings = localCoreSearchServices.getNowOptInSettings();
    InternalIcingCorporaProvider.UpdateCorporaService.maybeScheduleContactsSync(paramContext, localSearchConfig);
    if (!localNowOptInSettings.isAccountOptedIn(localLoginHelper.getAccount())) {
      localNowOptInSettings.cleanUpAccountData();
    }
  }
  
  private void handleCredentialsUpdate()
  {
    VelvetServices.get().getCoreServices().getLoginHelper().credentialsUpdated();
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (paramIntent == null) {
      return;
    }
    String str = paramIntent.getAction();
    if ("com.google.android.googlequicksearchbox.ACCOUNT_CREDENTIAL_UPDATE".equals(str))
    {
      handleCredentialsUpdate();
      return;
    }
    if ("android.accounts.LOGIN_ACCOUNTS_CHANGED".equals(str))
    {
      handleCredentialsUpdate();
      handleAccountsChanged(paramContext);
      return;
    }
    Log.w(TAG, "Received unexpected action: " + str);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.AccountsChangedReceiver
 * JD-Core Version:    0.7.0.1
 */