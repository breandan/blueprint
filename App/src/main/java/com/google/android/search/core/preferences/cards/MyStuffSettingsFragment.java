package com.google.android.search.core.preferences.cards;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class MyStuffSettingsFragment
  extends AbstractMyStuffSettingsFragment
{
  private void updateSummary(Iterable<String> paramIterable, int paramInt1, int paramInt2)
  {
    if (Iterables.size(paramIterable) > 2) {}
    for (int i = 1;; i = 0)
    {
      Iterable localIterable = Iterables.limit(paramIterable, 2);
      if (Iterables.isEmpty(localIterable)) {
        break;
      }
      String str = Joiner.on(", ").join(localIterable);
      if (i != 0) {
        str = str + "…";
      }
      findPreference(getString(paramInt1)).setSummary(str);
      return;
    }
    findPreference(getString(paramInt1)).setSummary(paramInt2);
  }
  
  protected int getPreferenceResourceId()
  {
    return 2131099667;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!VelvetServices.get().getSidekickInjector().areRemindersEnabled())
    {
      Preference localPreference = findPreference(getString(2131362153));
      getPreferenceScreen().removePreference(localPreference);
    }
  }
  
  public void onResume()
  {
    super.onResume();
    updateSummary(MyStocksSettingsFragment.getSummary(getActivity(), getConfigurationPreferences()), 2131362149, 2131362748);
    updateSummary(MySportsSettingsFragment.getSummary(getActivity(), getConfigurationPreferences()), 2131362151, 2131362749);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.MyStuffSettingsFragment
 * JD-Core Version:    0.7.0.1
 */