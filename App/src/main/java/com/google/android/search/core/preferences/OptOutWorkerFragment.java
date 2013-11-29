package com.google.android.search.core.preferences;

import android.accounts.Account;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.PlacevaultConfiguration;
import com.google.geo.sidekick.Sidekick.RequestPayload;

public class OptOutWorkerFragment
  extends Fragment
{
  private static final String TAG = Tag.getTag(OptOutWorkerFragment.class);
  private Account mAccount;
  private OptOutTask mOptOutTask;
  private boolean mTurnOffHistory;
  
  static OptOutWorkerFragment newInstance(Account paramAccount, boolean paramBoolean)
  {
    OptOutWorkerFragment localOptOutWorkerFragment = new OptOutWorkerFragment();
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("turn_off_history_key", paramBoolean);
    localBundle.putParcelable("account_key", paramAccount);
    localOptOutWorkerFragment.setArguments(localBundle);
    return localOptOutWorkerFragment;
  }
  
  public void finishOptOut()
  {
    OptOutSwitchHandler.findOptOutHandler(this).updatePredictiveCardsEnabledSwitch(null);
    DialogFragment localDialogFragment = (DialogFragment)getFragmentManager().findFragmentByTag("opt_out_progress");
    if (localDialogFragment != null) {
      localDialogFragment.dismissAllowingStateLoss();
    }
    this.mOptOutTask = null;
    getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Bundle localBundle = getArguments();
    this.mAccount = ((Account)localBundle.getParcelable("account_key"));
    this.mTurnOffHistory = localBundle.getBoolean("turn_off_history_key");
    setRetainInstance(true);
    NetworkClient localNetworkClient = VelvetServices.get().getSidekickInjector().getNetworkClient();
    this.mOptOutTask = new OptOutTask(getActivity().getApplicationContext(), this.mTurnOffHistory, localNetworkClient);
    this.mOptOutTask.execute(new Void[0]);
  }
  
  public void onDestroy()
  {
    if ((this.mOptOutTask != null) && (!this.mOptOutTask.isCancelled())) {
      this.mOptOutTask.cancel(true);
    }
    this.mOptOutTask = null;
    super.onDestroy();
  }
  
  public class OptOutTask
    extends AsyncTask<Void, Void, Integer>
  {
    private final Context mAppContext;
    private final boolean mDisableLocationHistory;
    private final NetworkClient mNetworkClient;
    
    public OptOutTask(Context paramContext, boolean paramBoolean, NetworkClient paramNetworkClient)
    {
      this.mAppContext = paramContext;
      this.mDisableLocationHistory = paramBoolean;
      this.mNetworkClient = paramNetworkClient;
    }
    
    protected Integer doInBackground(Void... paramVarArgs)
    {
      try
      {
        Sidekick.Configuration localConfiguration = new Sidekick.Configuration().setCompletelyOptOutOfSidekick(true);
        if (this.mDisableLocationHistory)
        {
          Sidekick.PlacevaultConfiguration localPlacevaultConfiguration = new Sidekick.PlacevaultConfiguration();
          localPlacevaultConfiguration.setLocationHistoryRecording(false);
          localConfiguration.setPlacevaultConfiguration(localPlacevaultConfiguration);
        }
        Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setConfigurationQuery(localConfiguration);
        if (this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload) == null) {
          return Integer.valueOf(1);
        }
        Integer localInteger = Integer.valueOf(0);
        return localInteger;
      }
      catch (Exception localException)
      {
        Log.e(OptOutWorkerFragment.TAG, "Exception while attempting to opt out", localException);
      }
      return Integer.valueOf(2);
    }
    
    protected void onPostExecute(Integer paramInteger)
    {
      switch (paramInteger.intValue())
      {
      }
      for (;;)
      {
        OptOutWorkerFragment.this.finishOptOut();
        return;
        Toast.makeText(this.mAppContext, 2131362493, 0).show();
        continue;
        Toast.makeText(this.mAppContext, 2131362494, 0).show();
        continue;
        optTheUserOut();
      }
    }
    
    void optTheUserOut()
    {
      CoreSearchServices localCoreSearchServices = VelvetServices.get().getCoreServices();
      localCoreSearchServices.getUserInteractionLogger().logAnalyticsAction("OPT_OUT", null);
      localCoreSearchServices.getNowOptInSettings().disableForAccount(OptOutWorkerFragment.this.mAccount);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.OptOutWorkerFragment
 * JD-Core Version:    0.7.0.1
 */