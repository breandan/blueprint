package com.google.android.sidekick.main.contextprovider;

import android.content.Context;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.velvet.Help;
import com.google.common.base.Supplier;

public class CardRenderingContextProviders
{
  private final AccountContextProvider mAccountContextProvider;
  private final CalendarDataContextProvider mCalendarDataContextProvider;
  private final HelpContextProvider mHelpContextProvider;
  private final NavigationContextProvider mNavigationContextProvider;
  private final SharedPreferencesContextProvider mSharedPreferencesContextProvider;
  private final SharedTrafficContextProvider mSharedTrafficContextProvider;
  
  public CardRenderingContextProviders(Context paramContext, PredictiveCardsPreferences paramPredictiveCardsPreferences, Supplier<NowConfigurationPreferences> paramSupplier, CalendarDataProvider paramCalendarDataProvider, LoginHelper paramLoginHelper, Help paramHelp)
  {
    this.mNavigationContextProvider = new NavigationContextProvider(paramContext, paramPredictiveCardsPreferences);
    this.mSharedPreferencesContextProvider = new SharedPreferencesContextProvider(paramContext, paramSupplier);
    this.mCalendarDataContextProvider = new CalendarDataContextProvider(paramCalendarDataProvider);
    this.mAccountContextProvider = new AccountContextProvider(paramLoginHelper);
    this.mSharedTrafficContextProvider = new SharedTrafficContextProvider(paramContext, paramPredictiveCardsPreferences);
    this.mHelpContextProvider = new HelpContextProvider(paramHelp);
  }
  
  public AccountContextProvider getAccountContextProvider()
  {
    return this.mAccountContextProvider;
  }
  
  public CalendarDataContextProvider getCalendarDataContextProvider()
  {
    return this.mCalendarDataContextProvider;
  }
  
  public HelpContextProvider getHelpContextProvider()
  {
    return this.mHelpContextProvider;
  }
  
  public NavigationContextProvider getNavigationContextProvider()
  {
    return this.mNavigationContextProvider;
  }
  
  public SharedPreferencesContextProvider getSharedPreferencesContextProvider()
  {
    return this.mSharedPreferencesContextProvider;
  }
  
  public SharedTrafficContextProvider getSharedTrafficContextProvider()
  {
    return this.mSharedTrafficContextProvider;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.CardRenderingContextProviders
 * JD-Core Version:    0.7.0.1
 */