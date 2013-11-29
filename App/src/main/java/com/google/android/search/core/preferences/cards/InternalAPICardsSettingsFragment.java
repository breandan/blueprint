package com.google.android.search.core.preferences.cards;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.View;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.velvet.VelvetServices;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.DisplayConfiguration;
import com.google.geo.sidekick.Sidekick.DisplayConfiguration.InternalApiClientsConfig;
import com.google.geo.sidekick.Sidekick.DisplayConfiguration.InternalApiClientsConfig.ClientName;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InternalAPICardsSettingsFragment
  extends AbstractRepeatedStuffSettingsFragment<Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting>
{
  private static final Comparator<Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting> sClientSettingComparator = new Comparator()
  {
    public int compare(Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting paramAnonymousClientSetting1, Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting paramAnonymousClientSetting2)
    {
      return Integer.valueOf(paramAnonymousClientSetting1.getClientId()).compareTo(Integer.valueOf(paramAnonymousClientSetting2.getClientId()));
    }
  };
  private final Map<Integer, String> clientNames = Maps.newHashMap();
  
  private String getClientName(int paramInt)
  {
    if (this.clientNames.containsKey(Integer.valueOf(paramInt))) {
      return (String)this.clientNames.get(Integer.valueOf(paramInt));
    }
    return "Client " + paramInt;
  }
  
  protected Preference createSettingPreference(Context paramContext, Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting paramClientSetting)
  {
    return new NonStoredSwitchPreference(paramContext, paramClientSetting.getOn());
  }
  
  protected boolean decorateAddPreference(Preference paramPreference)
  {
    return false;
  }
  
  protected void decorateSettingPreference(Preference paramPreference, Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting paramClientSetting)
  {
    ((NonStoredSwitchPreference)paramPreference).setTitle(getClientName(paramClientSetting.getClientId()));
  }
  
  protected int getPreferenceResourceId()
  {
    return 2131099661;
  }
  
  protected String getPreferencesKey()
  {
    return getString(2131362162);
  }
  
  protected Comparator<Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting> getSettingComparator()
  {
    return sClientSettingComparator;
  }
  
  protected Predicate<Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting> isSettingShown()
  {
    return Predicates.alwaysTrue();
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    CoreSearchServices localCoreSearchServices = VelvetServices.get().getCoreServices();
    Account localAccount = localCoreSearchServices.getLoginHelper().getAccount();
    if (localAccount != null)
    {
      Sidekick.DisplayConfiguration localDisplayConfiguration = localCoreSearchServices.getPredictiveCardsPreferences().getDisplayConfigurationFor(localAccount);
      if ((localDisplayConfiguration != null) && (localDisplayConfiguration.hasInternalApiClientsConfig()) && (localDisplayConfiguration.getInternalApiClientsConfig().getClientNameCount() > 0))
      {
        Iterator localIterator = localDisplayConfiguration.getInternalApiClientsConfig().getClientNameList().iterator();
        while (localIterator.hasNext())
        {
          Sidekick.DisplayConfiguration.InternalApiClientsConfig.ClientName localClientName = (Sidekick.DisplayConfiguration.InternalApiClientsConfig.ClientName)localIterator.next();
          this.clientNames.put(Integer.valueOf(localClientName.getClientId()), localClientName.getName());
        }
      }
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setHasOptionsMenu(false);
    findPreference(getString(2131362157)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
    {
      public boolean onPreferenceClick(Preference paramAnonymousPreference)
      {
        paramAnonymousPreference.setSummary(InternalAPICardsSettingsFragment.this.getString(2131362202));
        return true;
      }
    });
  }
  
  public boolean onPreferenceClick(Preference paramPreference)
  {
    return false;
  }
  
  protected void setSettingHidden(Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting paramClientSetting, boolean paramBoolean)
  {
    paramClientSetting.setOn(paramBoolean);
  }
  
  private class NonStoredSwitchPreference
    extends PartialSwitchPreference
    implements Preference.OnPreferenceChangeListener
  {
    private boolean mOnValue;
    
    public NonStoredSwitchPreference(Context paramContext, boolean paramBoolean)
    {
      super();
      setPersistent(false);
      setOnPreferenceChangeListener(this);
      this.mOnValue = paramBoolean;
    }
    
    protected void onBindView(View paramView)
    {
      setChecked(this.mOnValue);
      super.onBindView(paramView);
    }
    
    public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
    {
      boolean bool = ((Boolean)paramObject).booleanValue();
      InternalAPICardsSettingsFragment.this.setPreferenceHidden(getKey(), bool);
      this.mOnValue = bool;
      return true;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.InternalAPICardsSettingsFragment
 * JD-Core Version:    0.7.0.1
 */