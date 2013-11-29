package com.google.android.sidekick.main.remoteservice;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;

public class CurrentAccountActivity
  extends Activity
{
  private static final String TAG = Tag.getTag(CurrentAccountActivity.class);
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    CoreSearchServices localCoreSearchServices = VelvetServices.get().getCoreServices();
    LoginHelper localLoginHelper = localCoreSearchServices.getLoginHelper();
    NowOptInSettings localNowOptInSettings = localCoreSearchServices.getNowOptInSettings();
    Account localAccount = localLoginHelper.getAccount();
    if (!localNowOptInSettings.isAccountOptedIn(localAccount)) {
      localAccount = null;
    }
    Intent localIntent = new Intent();
    localIntent.putExtra("account", localAccount);
    setResult(-1, localIntent);
    finish();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.remoteservice.CurrentAccountActivity
 * JD-Core Version:    0.7.0.1
 */