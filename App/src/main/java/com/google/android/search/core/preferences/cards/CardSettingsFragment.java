package com.google.android.search.core.preferences.cards;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PreferencesUtil;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.velvet.VelvetServices;

public abstract class CardSettingsFragment
  extends PreferenceFragment
  implements SharedPreferences.OnSharedPreferenceChangeListener
{
  private Switch mActionBarSwitch;
  private NowConfigurationPreferences mSharedPreferences;
  private Handler mUpdateHandler;
  
  private void removeCardEnablementSwitch()
  {
    if (this.mActionBarSwitch != null)
    {
      getActivity().getActionBar().setDisplayOptions(0, 16);
      this.mActionBarSwitch.setOnCheckedChangeListener(null);
      this.mActionBarSwitch = null;
    }
  }
  
  protected void addCardEnablementSwitch(final String paramString)
  {
    Activity localActivity = getActivity();
    if ((localActivity instanceof PreferenceActivity))
    {
      this.mActionBarSwitch = createAndAddEnablementSwitch(localActivity);
      final SharedPreferences localSharedPreferences = getPreferenceManager().getSharedPreferences();
      boolean bool = isOverallPreferenceEnabled(localSharedPreferences, paramString);
      this.mActionBarSwitch.setChecked(bool);
      this.mActionBarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
      {
        public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
        {
          CardSettingsFragment.this.persistOverallPreference(localSharedPreferences, paramString, paramAnonymousBoolean);
          PreferencesUtil.updatePreferenceSummaries(CardSettingsFragment.this.getPreferenceScreen());
        }
      });
      setHasOptionsMenu(true);
    }
  }
  
  protected Switch createAndAddEnablementSwitch(Activity paramActivity)
  {
    Switch localSwitch = new Switch(paramActivity.getActionBar().getThemedContext());
    localSwitch.setPadding(0, 0, paramActivity.getResources().getDimensionPixelSize(2131689601), 0);
    paramActivity.getActionBar().setDisplayOptions(16, 16);
    paramActivity.getActionBar().setCustomView(localSwitch, new ActionBar.LayoutParams(-2, -2, 21));
    return localSwitch;
  }
  
  protected NowConfigurationPreferences getConfigurationPreferences()
  {
    return this.mSharedPreferences;
  }
  
  protected abstract int getPreferenceResourceId();
  
  protected boolean isOverallPreferenceEnabled(SharedPreferences paramSharedPreferences, String paramString)
  {
    return paramSharedPreferences.getBoolean(paramString, true);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getPreferenceManager().setSharedPreferencesName("sidekick");
    this.mSharedPreferences = ((NowConfigurationPreferences)getPreferenceManager().getSharedPreferences());
    addPreferencesFromResource(getPreferenceResourceId());
    CharSequence localCharSequence = getPreferenceScreen().getTitle();
    if (localCharSequence != null) {
      getActivity().getActionBar().setTitle(localCharSequence);
    }
    this.mUpdateHandler = new Handler();
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    paramMenu.clear();
  }
  
  public void onPause()
  {
    super.onPause();
    removeCardEnablementSwitch();
    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    VelvetServices.get().getSidekickInjector().getEntryProvider().invalidateWithDelayedRefresh();
  }
  
  public void onResume()
  {
    super.onResume();
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    String str = getPreferenceScreen().getKey();
    if (str != null) {
      addCardEnablementSwitch(str);
    }
    updatePreferenceEnablement();
    PreferencesUtil.updatePreferenceSummaries(getPreferenceScreen());
  }
  
  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    this.mUpdateHandler.post(new Runnable()
    {
      public void run()
      {
        PreferencesUtil.updatePreferenceSummaries(CardSettingsFragment.this.getPreferenceScreen());
      }
    });
    updatePreferenceEnablement();
  }
  
  protected void persistOverallPreference(SharedPreferences paramSharedPreferences, String paramString, boolean paramBoolean)
  {
    paramSharedPreferences.edit().putBoolean(paramString, paramBoolean).apply();
  }
  
  protected void updatePreferenceEnablement()
  {
    String str = getPreferenceScreen().getKey();
    if (str == null) {}
    for (;;)
    {
      return;
      boolean bool = isOverallPreferenceEnabled(getPreferenceManager().getSharedPreferences(), str);
      PreferenceScreen localPreferenceScreen = getPreferenceScreen();
      for (int i = 0; i < localPreferenceScreen.getPreferenceCount(); i++)
      {
        Preference localPreference = localPreferenceScreen.getPreference(i);
        if (!str.equals(localPreference.getKey())) {
          localPreference.setEnabled(bool);
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.CardSettingsFragment
 * JD-Core Version:    0.7.0.1
 */