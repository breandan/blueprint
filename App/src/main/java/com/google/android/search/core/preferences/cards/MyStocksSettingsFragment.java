package com.google.android.search.core.preferences.cards;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.shared.util.BidiUtils;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.StockQuotes.StockData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.annotation.Nullable;

public class MyStocksSettingsFragment
  extends AbstractRepeatedStuffSettingsFragment<Sidekick.SidekickConfiguration.StockQuotes.StockData>
  implements AddStockDialogFragment.StockFragment
{
  private static final String ADD_STOCK_DIALOG_TAG = "add_stock_dialog_tag";
  private static final String STOCK_PREFERENCE_LABEL = "stock_preference_label";
  private static final String WORK_FRAGMENT_TAG = "work";
  private static final Predicate<Sidekick.SidekickConfiguration.StockQuotes.StockData> sSettingsNotHidden = new Predicate()
  {
    public boolean apply(Sidekick.SidekickConfiguration.StockQuotes.StockData paramAnonymousStockData)
    {
      return !paramAnonymousStockData.getDeleted();
    }
  };
  private static final Comparator<Sidekick.SidekickConfiguration.StockQuotes.StockData> sStockComparator = new Comparator()
  {
    public int compare(Sidekick.SidekickConfiguration.StockQuotes.StockData paramAnonymousStockData1, Sidekick.SidekickConfiguration.StockQuotes.StockData paramAnonymousStockData2)
    {
      return paramAnonymousStockData1.getSymbol().compareTo(paramAnonymousStockData2.getSymbol());
    }
  };
  private static final Function<Sidekick.SidekickConfiguration.StockQuotes.StockData, String> sStockToSumary = new Function()
  {
    public String apply(Sidekick.SidekickConfiguration.StockQuotes.StockData paramAnonymousStockData)
    {
      return paramAnonymousStockData.getDescription();
    }
  };
  private AddStockDialogFragment.TickerFetcherFragment mWorkerFragment;
  
  public static Iterable<String> getSummary(Context paramContext, NowConfigurationPreferences paramNowConfigurationPreferences)
  {
    ArrayList localArrayList = Lists.newArrayList(Iterables.filter(paramNowConfigurationPreferences.getMessages(paramContext.getString(2131362067)), sSettingsNotHidden));
    Collections.sort(localArrayList, sStockComparator);
    return Iterables.transform(localArrayList, sStockToSumary);
  }
  
  private static String stockToLabel(Activity paramActivity, Sidekick.SidekickConfiguration.StockQuotes.StockData paramStockData)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = BidiUtils.unicodeWrap(paramStockData.getDescription());
    arrayOfObject[1] = BidiUtils.unicodeWrap(paramStockData.getSymbol());
    return paramActivity.getString(2131362635, arrayOfObject);
  }
  
  public void addStock(AddStockDialogFragment.StockDataWithName paramStockDataWithName, @Nullable Integer paramInteger)
  {
    addSetting(paramStockDataWithName.getStockData());
  }
  
  protected boolean decorateAddPreference(Preference paramPreference)
  {
    paramPreference.setTitle(2131362609);
    return true;
  }
  
  protected void decorateRemoveDialog(DialogFragment paramDialogFragment, Preference paramPreference)
  {
    String str = paramPreference.getExtras().getString("stock_preference_label");
    Bundle localBundle = paramDialogFragment.getArguments();
    localBundle.putCharSequence("title", str);
    paramDialogFragment.setArguments(localBundle);
  }
  
  protected void decorateSettingPreference(Preference paramPreference, Sidekick.SidekickConfiguration.StockQuotes.StockData paramStockData)
  {
    paramPreference.setTitle(paramStockData.getSymbol());
    paramPreference.setSummary(paramStockData.getDescription());
    paramPreference.setLayoutResource(2130968834);
    paramPreference.getExtras().putString("stock_preference_label", stockToLabel(getActivity(), paramStockData));
  }
  
  protected int getPreferenceResourceId()
  {
    return 2131099666;
  }
  
  protected String getPreferencesKey()
  {
    return getString(2131362067);
  }
  
  protected Comparator<Sidekick.SidekickConfiguration.StockQuotes.StockData> getSettingComparator()
  {
    return sStockComparator;
  }
  
  protected Predicate<Sidekick.SidekickConfiguration.StockQuotes.StockData> isSettingShown()
  {
    return sSettingsNotHidden;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    FragmentManager localFragmentManager = getFragmentManager();
    this.mWorkerFragment = ((AddStockDialogFragment.TickerFetcherFragment)localFragmentManager.findFragmentByTag("work"));
    if (this.mWorkerFragment == null)
    {
      this.mWorkerFragment = new AddStockDialogFragment.TickerFetcherFragment();
      localFragmentManager.beginTransaction().add(this.mWorkerFragment, "work").commit();
    }
  }
  
  protected void setSettingHidden(Sidekick.SidekickConfiguration.StockQuotes.StockData paramStockData, boolean paramBoolean)
  {
    paramStockData.setDeleted(paramBoolean);
  }
  
  protected boolean showAddDialog(Preference paramPreference)
  {
    try
    {
      AddStockDialogFragment.newInstance(this, "work", null).show(getActivity().getFragmentManager(), "add_stock_dialog_tag");
      return true;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.MyStocksSettingsFragment
 * JD-Core Version:    0.7.0.1
 */