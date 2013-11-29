package com.google.android.sidekick.main.contextprovider;

import com.google.android.sidekick.shared.cards.SharedTrafficEntryAdapter;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.SharedTrafficCardEntry;

public class SharedTrafficContextAdapter
  implements EntryRenderingContextAdapter
{
  private final Sidekick.SharedTrafficCardEntry mSharedTrafficCardEntry;
  
  public SharedTrafficContextAdapter(Sidekick.Entry paramEntry)
  {
    this.mSharedTrafficCardEntry = paramEntry.getSharedTrafficCardEntry();
  }
  
  public void addTypeSpecificRenderingContext(CardRenderingContext paramCardRenderingContext, CardRenderingContextProviders paramCardRenderingContextProviders)
  {
    paramCardRenderingContextProviders.getSharedPreferencesContextProvider().addInt(paramCardRenderingContext, SharedTrafficEntryAdapter.confirmationPresentationCountKey(this.mSharedTrafficCardEntry), 0);
    paramCardRenderingContextProviders.getSharedTrafficContextProvider().addSharedTrafficContext(paramCardRenderingContext, this.mSharedTrafficCardEntry);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.SharedTrafficContextAdapter
 * JD-Core Version:    0.7.0.1
 */