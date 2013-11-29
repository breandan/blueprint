package com.google.android.sidekick.main.contextprovider;

import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;

public class LocationHistoryReminderContextAdapter
  implements EntryRenderingContextAdapter
{
  public void addTypeSpecificRenderingContext(CardRenderingContext paramCardRenderingContext, CardRenderingContextProviders paramCardRenderingContextProviders)
  {
    paramCardRenderingContextProviders.getSharedPreferencesContextProvider().addBoolean(paramCardRenderingContext, 2131362147, false);
    paramCardRenderingContextProviders.getHelpContextProvider().addHelpUri(paramCardRenderingContext, "locationhistory");
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.LocationHistoryReminderContextAdapter
 * JD-Core Version:    0.7.0.1
 */