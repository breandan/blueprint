package com.google.android.search.core.preferences;

import android.accounts.Account;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.sidekick.shared.util.Tag;

public class OptOutSwitchHandler
  implements CompoundButton.OnCheckedChangeListener
{
  private static final String TAG = Tag.getTag(OptOutSwitchHandler.class);
  private final Activity mActivity;
  private boolean mHasSavedSwitchState;
  private final LoginHelper mLoginHelper;
  private final NowOptInSettings mNowOptInSettings;
  private final Intent mOptInIntent;
  private boolean mSavedSwitchState;
  private Switch mSwitch;
  private boolean mSwitchState;
  private final UserInteractionLogger mUserInteractionLogger;
  
  public OptOutSwitchHandler(NowOptInSettings paramNowOptInSettings, LoginHelper paramLoginHelper, Intent paramIntent, Activity paramActivity, UserInteractionLogger paramUserInteractionLogger)
  {
    this.mNowOptInSettings = paramNowOptInSettings;
    this.mLoginHelper = paramLoginHelper;
    this.mOptInIntent = paramIntent;
    this.mActivity = paramActivity;
    this.mUserInteractionLogger = paramUserInteractionLogger;
  }
  
  static OptOutSwitchHandler findOptOutHandler(Fragment paramFragment)
  {
    Fragment localFragment = paramFragment.getTargetFragment();
    if ((localFragment instanceof HasOptOutSwitchHandler)) {
      return ((HasOptOutSwitchHandler)localFragment).getOptOutSwitchHandler();
    }
    Activity localActivity = paramFragment.getActivity();
    if ((localActivity instanceof HasOptOutSwitchHandler)) {
      return ((HasOptOutSwitchHandler)localActivity).getOptOutSwitchHandler();
    }
    throw new IllegalStateException();
  }
  
  public boolean hasSwitch()
  {
    return this.mSwitch != null;
  }
  
  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    this.mSwitchState = paramBoolean;
    Account localAccount = this.mLoginHelper.getAccount();
    boolean bool = this.mNowOptInSettings.isAccountOptedIn(localAccount);
    if ((!paramBoolean) && (bool)) {
      new OptOutDialogFragment().show(this.mActivity.getFragmentManager(), "optout_dialog");
    }
    while ((!paramBoolean) || (bool)) {
      return;
    }
    this.mUserInteractionLogger.logAnalyticsAction("BUTTON_PRESS", "SETTINGS_OPT_IN");
    this.mActivity.startActivity(this.mOptInIntent);
  }
  
  public void onResume()
  {
    if (this.mSwitch != null)
    {
      this.mSwitch.setOnCheckedChangeListener(null);
      updatePredictiveCardsEnabledSwitch(null);
      this.mSwitch.setOnCheckedChangeListener(this);
    }
  }
  
  public void restoreInstanceState(Bundle paramBundle)
  {
    this.mHasSavedSwitchState = false;
    if ((paramBundle != null) && (paramBundle.containsKey("mariner_enabled_switch_state")))
    {
      this.mSavedSwitchState = paramBundle.getBoolean("mariner_enabled_switch_state");
      this.mHasSavedSwitchState = true;
    }
  }
  
  public void saveInstanceState(Bundle paramBundle)
  {
    paramBundle.putBoolean("mariner_enabled_switch_state", this.mSwitchState);
  }
  
  public void setSwitch(Switch paramSwitch, Boolean paramBoolean)
  {
    if (this.mSwitch == paramSwitch) {}
    do
    {
      return;
      if (this.mSwitch != null) {
        this.mSwitch.setOnCheckedChangeListener(null);
      }
      this.mSwitch = paramSwitch;
    } while (this.mSwitch == null);
    if (this.mHasSavedSwitchState)
    {
      this.mSwitch.setChecked(this.mSavedSwitchState);
      this.mSwitchState = this.mSavedSwitchState;
      this.mHasSavedSwitchState = false;
    }
    for (;;)
    {
      this.mSwitch.setOnCheckedChangeListener(this);
      return;
      updatePredictiveCardsEnabledSwitch(paramBoolean);
    }
  }
  
  void startOptOutTask(boolean paramBoolean)
  {
    this.mUserInteractionLogger.logAnalyticsAction("BUTTON_PRESS", "SETTINGS_OPT_OUT");
    Account localAccount = this.mLoginHelper.getAccount();
    if (localAccount == null) {}
    FragmentManager localFragmentManager;
    do
    {
      do
      {
        return;
        localFragmentManager = this.mActivity.getFragmentManager();
      } while (localFragmentManager == null);
      if ((OptOutSpinnerDialog)localFragmentManager.findFragmentByTag("opt_out_progress") == null) {
        new OptOutSpinnerDialog().show(localFragmentManager, "opt_out_progress");
      }
    } while (localFragmentManager.findFragmentByTag("opt_out_worker_fragment") != null);
    OptOutWorkerFragment localOptOutWorkerFragment = OptOutWorkerFragment.newInstance(localAccount, paramBoolean);
    localFragmentManager.beginTransaction().add(localOptOutWorkerFragment, "opt_out_worker_fragment").commit();
  }
  
  void updatePredictiveCardsEnabledSwitch(Boolean paramBoolean)
  {
    Account localAccount;
    boolean bool1;
    if (this.mSwitch != null)
    {
      localAccount = this.mLoginHelper.getAccount();
      if (localAccount == null) {
        break label104;
      }
      bool1 = this.mNowOptInSettings.isAccountOptedIn(localAccount);
      if (paramBoolean == null) {
        break label98;
      }
    }
    label98:
    for (boolean bool2 = paramBoolean.booleanValue();; bool2 = bool1)
    {
      this.mSwitch.setChecked(bool2);
      this.mSwitchState = bool2;
      int i = this.mNowOptInSettings.canAccountRunNow(localAccount);
      Switch localSwitch = this.mSwitch;
      boolean bool3;
      if (i != 0)
      {
        bool3 = false;
        if (i != 1) {}
      }
      else
      {
        bool3 = true;
      }
      localSwitch.setEnabled(bool3);
      return;
    }
    label104:
    this.mSwitch.setChecked(false);
    this.mSwitch.setEnabled(false);
    this.mSwitchState = false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.OptOutSwitchHandler
 * JD-Core Version:    0.7.0.1
 */