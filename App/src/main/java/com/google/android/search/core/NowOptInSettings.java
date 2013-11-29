package com.google.android.search.core;

import android.accounts.Account;
import com.google.android.gms.location.reporting.ReportingState;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.FetchConfigurationResponse;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration;
import javax.annotation.Nullable;

public abstract interface NowOptInSettings
{
  public abstract int canAccountRunNow(Account paramAccount);
  
  public abstract int canLoggedInAccountRunNow();
  
  public abstract void cleanUpAccountData();
  
  public abstract void disableForAccount(Account paramAccount);
  
  public abstract boolean domainIsBlockedFromNow(Sidekick.Configuration paramConfiguration, Account paramAccount);
  
  public abstract Sidekick.FetchConfigurationResponse fetchAccountConfiguration(Account paramAccount);
  
  public abstract Sidekick.Configuration getSavedConfiguration(Account paramAccount);
  
  public abstract boolean isAccountOptedIn(@Nullable Account paramAccount);
  
  public abstract boolean isPreferenceKeyForAnOptInChange(String paramString);
  
  public abstract boolean isUserOptedIn();
  
  public abstract boolean localeIsBlockedFromNow(Sidekick.Configuration paramConfiguration);
  
  public abstract void onAccountChanged(@Nullable Account paramAccount);
  
  public abstract void optAccountIn(Account paramAccount);
  
  public abstract void optAccountOut(Account paramAccount);
  
  public abstract void saveConfiguration(Sidekick.Configuration paramConfiguration, Account paramAccount, @Nullable ReportingState paramReportingState);
  
  public abstract void setFirstRunScreensShown();
  
  public abstract void setGetGoogleNowButtonDismissed();
  
  public abstract boolean stopServicesIfUserOptedOut();
  
  public abstract boolean updateConfigurationForAccount(Account paramAccount, Sidekick.Configuration paramConfiguration, @Nullable ReportingState paramReportingState);
  
  public abstract void updateSidekickConfigurationForCurrentAccount(Sidekick.SidekickConfiguration paramSidekickConfiguration);
  
  public abstract void updateWhetherAccountCanRunNow(Account paramAccount);
  
  public abstract int userCanRunNow(Sidekick.Configuration paramConfiguration, Account paramAccount, @Nullable ReportingState paramReportingState);
  
  public abstract boolean userHasDismissedGetGoogleNowButton();
  
  public abstract boolean userHasSeenFirstRunScreens();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.NowOptInSettings
 * JD-Core Version:    0.7.0.1
 */