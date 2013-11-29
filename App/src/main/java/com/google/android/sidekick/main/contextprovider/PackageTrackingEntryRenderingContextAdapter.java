package com.google.android.sidekick.main.contextprovider;

import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.PackageTrackingEntry;

public class PackageTrackingEntryRenderingContextAdapter
  implements EntryRenderingContextAdapter
{
  private final Sidekick.PackageTrackingEntry mPackageTrackingEntry;
  
  public PackageTrackingEntryRenderingContextAdapter(Sidekick.PackageTrackingEntry paramPackageTrackingEntry)
  {
    this.mPackageTrackingEntry = paramPackageTrackingEntry;
  }
  
  public void addTypeSpecificRenderingContext(CardRenderingContext paramCardRenderingContext, CardRenderingContextProviders paramCardRenderingContextProviders)
  {
    paramCardRenderingContextProviders.getAccountContextProvider().addAccountContext(paramCardRenderingContext);
    if (this.mPackageTrackingEntry.hasPickupLocation()) {
      paramCardRenderingContextProviders.getNavigationContextProvider().addNavigationContext(paramCardRenderingContext, this.mPackageTrackingEntry.getPickupLocation(), null);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.PackageTrackingEntryRenderingContextAdapter
 * JD-Core Version:    0.7.0.1
 */