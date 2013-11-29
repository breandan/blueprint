package com.google.android.search.core.preferences;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider;
import com.google.android.velvet.util.IntentUtils;

public class AppHistorySettingsController
  extends SettingsControllerBase
  implements Preference.OnPreferenceClickListener
{
  private final Activity mActivity;
  private Preference mPref;
  
  public AppHistorySettingsController(SearchSettings paramSearchSettings, Activity paramActivity)
  {
    super(paramSearchSettings);
    this.mActivity = paramActivity;
  }
  
  private void updateSummary()
  {
    Preference localPreference;
    if (this.mPref != null)
    {
      boolean bool = getSettings().isAppHistoryReportingEnabled();
      localPreference = this.mPref;
      if (!bool) {
        break label35;
      }
    }
    label35:
    for (int i = 2131363198;; i = 2131363199)
    {
      localPreference.setSummary(i);
      return;
    }
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    return !IntentUtils.isGelDefaultLauncher(this.mActivity);
  }
  
  public void handlePreference(Preference paramPreference)
  {
    if ("app_history_key".equals(paramPreference.getKey())) {
      this.mPref = paramPreference;
    }
    paramPreference.setOnPreferenceClickListener(this);
    updateSummary();
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    if ("app_history_reset_app_history_key".equals(paramPreference.getKey()))
    {
      ResetAppHistoryDialog.newInstance().show(this.mActivity.getFragmentManager(), null);
      return true;
    }
    return false;
  }
  
  public void onResume()
  {
    updateSummary();
  }
  
  private static final class ClearApplicationLaunchLogTask
    extends AsyncTask<Void, Void, Void>
  {
    private final ContentResolver mContentResolver;
    
    public ClearApplicationLaunchLogTask(ContentResolver paramContentResolver)
    {
      this.mContentResolver = paramContentResolver;
    }
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      this.mContentResolver.delete(InternalIcingCorporaProvider.CLEAR_APP_LAUNCH_LOG_URI, null, null);
      return null;
    }
  }
  
  public static final class ResetAppHistoryDialog
    extends DialogFragment
  {
    public static ResetAppHistoryDialog newInstance()
    {
      ResetAppHistoryDialog localResetAppHistoryDialog = new ResetAppHistoryDialog();
      localResetAppHistoryDialog.setStyle(1, 0);
      return localResetAppHistoryDialog;
    }
    
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      new AlertDialog.Builder(getActivity()).setTitle(2131363195).setMessage(2131363196).setPositiveButton(2131363197, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          new AppHistorySettingsController.ClearApplicationLaunchLogTask(AppHistorySettingsController.ResetAppHistoryDialog.this.getActivity().getContentResolver()).execute(new Void[0]);
        }
      }).setNegativeButton(17039360, null).create();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.AppHistorySettingsController
 * JD-Core Version:    0.7.0.1
 */