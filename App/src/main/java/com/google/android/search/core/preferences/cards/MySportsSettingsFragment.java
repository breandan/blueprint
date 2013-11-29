package com.google.android.search.core.preferences.cards;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.Sports.SportTeamPlayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MySportsSettingsFragment
  extends AbstractRepeatedStuffSettingsFragment<Sidekick.SidekickConfiguration.Sports.SportTeamPlayer>
{
  private static final Map<Integer, Integer> SPORT_ENUM_TO_ICON_ID = ImmutableMap.builder().put(Integer.valueOf(2), Integer.valueOf(2130837909)).put(Integer.valueOf(0), Integer.valueOf(2130837912)).put(Integer.valueOf(1), Integer.valueOf(2130837915)).put(Integer.valueOf(3), Integer.valueOf(2130837918)).put(Integer.valueOf(4), Integer.valueOf(2130837925)).put(Integer.valueOf(5), Integer.valueOf(2130837921)).build();
  private static final Predicate<Sidekick.SidekickConfiguration.Sports.SportTeamPlayer> sSettingsNotHidden = new Predicate()
  {
    public boolean apply(Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramAnonymousSportTeamPlayer)
    {
      return !paramAnonymousSportTeamPlayer.getHide();
    }
  };
  public static final Comparator<Sidekick.SidekickConfiguration.Sports.SportTeamPlayer> sSportComparator = new Comparator()
  {
    public int compare(Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramAnonymousSportTeamPlayer1, Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramAnonymousSportTeamPlayer2)
    {
      return ComparisonChain.start().compare(paramAnonymousSportTeamPlayer1.getSport(), paramAnonymousSportTeamPlayer2.getSport()).compare(paramAnonymousSportTeamPlayer1.getTeam().toLowerCase(), paramAnonymousSportTeamPlayer2.getTeam().toLowerCase()).result();
    }
  };
  private static final Function<Sidekick.SidekickConfiguration.Sports.SportTeamPlayer, String> sSportToSumary = new Function()
  {
    public String apply(Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramAnonymousSportTeamPlayer)
    {
      return paramAnonymousSportTeamPlayer.getTeam();
    }
  };
  private Preference mAddPreference;
  private SportsEntitiesProvider mSportsDataUpdater;
  
  public static Iterable<String> getSummary(Context paramContext, NowConfigurationPreferences paramNowConfigurationPreferences)
  {
    ArrayList localArrayList = Lists.newArrayList(Iterables.filter(paramNowConfigurationPreferences.getMessages(paramContext.getString(2131362089)), sSettingsNotHidden));
    Collections.sort(localArrayList, sSportComparator);
    return Iterables.transform(localArrayList, sSportToSumary);
  }
  
  private void updateSportsEntities()
  {
    this.mAddPreference.setEnabled(true);
    this.mAddPreference.setSummary("");
  }
  
  protected boolean decorateAddPreference(Preference paramPreference)
  {
    paramPreference.setTitle(2131362568);
    paramPreference.setEnabled(false);
    paramPreference.setSummary(2131363187);
    this.mAddPreference = paramPreference;
    return true;
  }
  
  protected void decorateSettingPreference(Preference paramPreference, Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramSportTeamPlayer)
  {
    paramPreference.setTitle(AddTeamDialogFragment.sportTeamToLabel(paramSportTeamPlayer));
    paramPreference.setLayoutResource(2130968834);
    Integer localInteger = (Integer)SPORT_ENUM_TO_ICON_ID.get(Integer.valueOf(paramSportTeamPlayer.getSport()));
    if (localInteger != null) {
      paramPreference.setIcon(localInteger.intValue());
    }
  }
  
  protected int getPreferenceResourceId()
  {
    return 2131099665;
  }
  
  protected String getPreferencesKey()
  {
    return getString(2131362089);
  }
  
  protected Comparator<Sidekick.SidekickConfiguration.Sports.SportTeamPlayer> getSettingComparator()
  {
    return sSportComparator;
  }
  
  protected Predicate<Sidekick.SidekickConfiguration.Sports.SportTeamPlayer> isSettingShown()
  {
    return sSettingsNotHidden;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mSportsDataUpdater = new SportsEntitiesProvider(this)
    {
      public void updateSportsEntities()
      {
        jdField_this.updateSportsEntities();
        super.updateSportsEntities();
      }
    };
    this.mSportsDataUpdater.loadSportsData();
  }
  
  protected void setSettingHidden(Sidekick.SidekickConfiguration.Sports.SportTeamPlayer paramSportTeamPlayer, boolean paramBoolean)
  {
    paramSportTeamPlayer.setHide(paramBoolean);
  }
  
  /* Error */
  protected boolean showAddDialog(Preference paramPreference)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 195	com/google/android/search/core/preferences/cards/MySportsSettingsFragment:mSportsDataUpdater	Lcom/google/android/search/core/preferences/cards/SportsEntitiesProvider;
    //   6: invokevirtual 224	com/google/android/search/core/preferences/cards/SportsEntitiesProvider:isReady	()Z
    //   9: istore_3
    //   10: iconst_0
    //   11: istore 4
    //   13: iload_3
    //   14: ifne +8 -> 22
    //   17: aload_0
    //   18: monitorexit
    //   19: iload 4
    //   21: ireturn
    //   22: new 226	com/google/android/search/core/preferences/cards/MySportsSettingsFragment$5
    //   25: dup
    //   26: aload_0
    //   27: invokespecial 228	com/google/android/search/core/preferences/cards/MySportsSettingsFragment$5:<init>	(Lcom/google/android/search/core/preferences/cards/MySportsSettingsFragment;)V
    //   30: astore 5
    //   32: new 230	android/os/Bundle
    //   35: dup
    //   36: invokespecial 231	android/os/Bundle:<init>	()V
    //   39: astore 6
    //   41: aload 6
    //   43: ldc 233
    //   45: aload_0
    //   46: getfield 195	com/google/android/search/core/preferences/cards/MySportsSettingsFragment:mSportsDataUpdater	Lcom/google/android/search/core/preferences/cards/SportsEntitiesProvider;
    //   49: invokevirtual 237	com/google/android/search/core/preferences/cards/SportsEntitiesProvider:getSportsEntities	()Lcom/google/geo/sidekick/Sidekick$SportsTeams;
    //   52: invokevirtual 243	com/google/geo/sidekick/Sidekick$SportsTeams:toByteArray	()[B
    //   55: invokevirtual 247	android/os/Bundle:putByteArray	(Ljava/lang/String;[B)V
    //   58: aload 5
    //   60: aload 6
    //   62: invokevirtual 252	android/app/DialogFragment:setArguments	(Landroid/os/Bundle;)V
    //   65: aload 5
    //   67: aload_0
    //   68: iconst_0
    //   69: invokevirtual 256	android/app/DialogFragment:setTargetFragment	(Landroid/app/Fragment;I)V
    //   72: aload 5
    //   74: aload_0
    //   75: invokevirtual 260	com/google/android/search/core/preferences/cards/MySportsSettingsFragment:getActivity	()Landroid/app/Activity;
    //   78: invokevirtual 266	android/app/Activity:getFragmentManager	()Landroid/app/FragmentManager;
    //   81: ldc_w 268
    //   84: invokevirtual 272	android/app/DialogFragment:show	(Landroid/app/FragmentManager;Ljava/lang/String;)V
    //   87: iconst_1
    //   88: istore 4
    //   90: goto -73 -> 17
    //   93: astore_2
    //   94: aload_0
    //   95: monitorexit
    //   96: aload_2
    //   97: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	98	0	this	MySportsSettingsFragment
    //   0	98	1	paramPreference	Preference
    //   93	4	2	localObject	Object
    //   9	5	3	bool1	boolean
    //   11	78	4	bool2	boolean
    //   30	43	5	local5	5
    //   39	22	6	localBundle	Bundle
    // Exception table:
    //   from	to	target	type
    //   2	10	93	finally
    //   22	87	93	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.MySportsSettingsFragment
 * JD-Core Version:    0.7.0.1
 */