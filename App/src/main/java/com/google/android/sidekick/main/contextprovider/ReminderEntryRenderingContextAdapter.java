package com.google.android.sidekick.main.contextprovider;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.common.base.Supplier;

public class ReminderEntryRenderingContextAdapter
  implements EntryRenderingContextAdapter
{
  private final Supplier<SharedPreferences> mPreferences;
  
  public ReminderEntryRenderingContextAdapter(Supplier<SharedPreferences> paramSupplier)
  {
    this.mPreferences = paramSupplier;
  }
  
  public void addTypeSpecificRenderingContext(CardRenderingContext paramCardRenderingContext, CardRenderingContextProviders paramCardRenderingContextProviders)
  {
    ExtraPreconditions.checkNotMainThread();
    paramCardRenderingContextProviders.getSharedPreferencesContextProvider().addBoolean(paramCardRenderingContext, "com.google.android.apps.sidekick.REMINDER_INTRO_DISPLAYED", false);
    ((SharedPreferences)this.mPreferences.get()).edit().putBoolean("com.google.android.apps.sidekick.REMINDER_INTRO_DISPLAYED", true).apply();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.ReminderEntryRenderingContextAdapter
 * JD-Core Version:    0.7.0.1
 */