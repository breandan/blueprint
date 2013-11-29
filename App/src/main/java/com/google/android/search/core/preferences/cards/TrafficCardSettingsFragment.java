package com.google.android.search.core.preferences.cards;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import com.google.android.search.core.AsyncServices;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.WrappingPreference;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.util.GooglePlusIntents;
import com.google.android.sidekick.shared.util.GooglePlusIntents.ToastGooglePlusError;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.ui.settings.SettingsActivity;

public class TrafficCardSettingsFragment
  extends CardSettingsFragment
  implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
{
  IntentStarter mIntentStarter;
  
  private void listenTo(int paramInt)
  {
    String str = getString(paramInt);
    Preference localPreference = getPreferenceScreen().findPreference(str);
    if (localPreference != null)
    {
      if ((localPreference instanceof WrappingPreference)) {
        localPreference.setOnPreferenceClickListener(this);
      }
    }
    else {
      return;
    }
    localPreference.setOnPreferenceChangeListener(this);
  }
  
  protected int getPreferenceResourceId()
  {
    return 2131099676;
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    Activity localActivity = getActivity();
    if ((localActivity instanceof SettingsActivity))
    {
      this.mIntentStarter = ((SettingsActivity)localActivity).getIntentStarter();
      listenTo(2131362051);
      listenTo(2131362050);
      listenTo(2131362163);
    }
  }
  
  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    Context localContext = getActivity().getApplicationContext();
    String str = paramPreference.getKey();
    if (getString(2131362163).equals(str))
    {
      if (((Boolean)paramObject).booleanValue()) {}
      for (int i = 2131362208;; i = 2131362209)
      {
        Toast.makeText(localContext, i, 1).show();
        return true;
      }
    }
    return false;
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    Activity localActivity = getActivity();
    Context localContext = localActivity.getApplicationContext();
    String str1 = paramPreference.getKey();
    if (getString(2131362051).equals(str1))
    {
      String str2 = localContext.getString(2131362565);
      Intent localIntent = new Intent("android.intent.action.VIEW");
      localIntent.setData(Uri.parse(str2));
      this.mIntentStarter.startActivity(new Intent[] { localIntent });
      return true;
    }
    if (getString(2131362050).equals(str1))
    {
      if (GooglePlusIntents.canSendManageLocationSharingIntent(localActivity))
      {
        VelvetServices localVelvetServices = VelvetServices.get();
        LoginHelper localLoginHelper = localVelvetServices.getCoreServices().getLoginHelper();
        ScheduledSingleThreadedExecutor localScheduledSingleThreadedExecutor = localVelvetServices.getAsyncServices().getUiThreadExecutor();
        Account localAccount = localLoginHelper.getAccount();
        GooglePlusIntents.ToastGooglePlusError localToastGooglePlusError = new GooglePlusIntents.ToastGooglePlusError(localContext, localScheduledSingleThreadedExecutor);
        this.mIntentStarter.startActivityForResult(GooglePlusIntents.getManageLocationSharingIntent(localAccount), localToastGooglePlusError);
        return true;
      }
      Toast.makeText(localActivity, 2131362207, 1).show();
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.TrafficCardSettingsFragment
 * JD-Core Version:    0.7.0.1
 */