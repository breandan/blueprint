package com.google.android.sidekick.main;

import android.accounts.Account;
import android.accounts.AccountsException;
import android.util.Log;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.location.LocationReportingOptInHelper;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class NowOptInHelper
{
  private final String TAG = Tag.getTag(NowOptInHelper.class);
  private final PredictiveCardsPreferences mCardPrefs;
  private final EntryProvider mEntryProvider;
  private final LocationReportingOptInHelper mLocationReportingOptInHelper;
  private final LoginHelper mLoginHelper;
  private final NetworkClient mNetworkClient;
  private final NowOptInSettings mNowOptInSettings;
  private final Executor mUiExecutor;
  private final VelvetServices mVelvetServices;
  
  public NowOptInHelper(Executor paramExecutor, LoginHelper paramLoginHelper, NetworkClient paramNetworkClient, PredictiveCardsPreferences paramPredictiveCardsPreferences, NowOptInSettings paramNowOptInSettings, LocationReportingOptInHelper paramLocationReportingOptInHelper, VelvetServices paramVelvetServices, EntryProvider paramEntryProvider)
  {
    this.mUiExecutor = paramExecutor;
    this.mLoginHelper = paramLoginHelper;
    this.mNetworkClient = paramNetworkClient;
    this.mCardPrefs = paramPredictiveCardsPreferences;
    this.mNowOptInSettings = paramNowOptInSettings;
    this.mLocationReportingOptInHelper = paramLocationReportingOptInHelper;
    this.mVelvetServices = paramVelvetServices;
    this.mEntryProvider = paramEntryProvider;
  }
  
  private void completeOptIn(Account paramAccount)
  {
    Preconditions.checkNotNull(paramAccount);
    this.mNowOptInSettings.optAccountIn(paramAccount);
    this.mNowOptInSettings.setFirstRunScreensShown();
    this.mCardPrefs.copyMasterToWorkingFor(paramAccount);
    this.mLocationReportingOptInHelper.optIntoLocationReportingAsync();
    this.mVelvetServices.startNowServices();
    this.mVelvetServices.maybeRegisterSidekickAlarms();
    this.mEntryProvider.invalidate();
  }
  
  private boolean sendServerOptInRequest()
  {
    try
    {
      Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setConfigurationQuery(new Sidekick.Configuration().setOptInToSidekick(true));
      if (this.mNetworkClient.sendRequestWithLocation(localRequestPayload) == null)
      {
        Log.e(this.TAG, "Network error while attempting to opt-in");
        return false;
      }
    }
    catch (Exception localException)
    {
      Log.e(this.TAG, "Exception while attempting to authenticate", localException);
      return false;
    }
    return true;
  }
  
  public boolean optIn(final Account paramAccount)
  {
    ExtraPreconditions.checkNotMainThread();
    Preconditions.checkNotNull(paramAccount);
    FutureTask localFutureTask = new FutureTask(new Callable()
    {
      public Boolean call()
        throws Exception
      {
        try
        {
          NowOptInHelper.this.mLoginHelper.setAccountToUseByName(paramAccount.name);
          Boolean localBoolean = Boolean.valueOf(true);
          return localBoolean;
        }
        catch (AccountsException localAccountsException)
        {
          Log.w(NowOptInHelper.this.TAG, "Account not available on device.");
        }
        return Boolean.valueOf(false);
      }
    });
    this.mUiExecutor.execute(localFutureTask);
    if (!((Boolean)Futures.getUnchecked(localFutureTask)).booleanValue()) {
      return false;
    }
    this.mCardPrefs.clearWorkingConfiguration();
    if (sendServerOptInRequest())
    {
      completeOptIn(paramAccount);
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.NowOptInHelper
 * JD-Core Version:    0.7.0.1
 */