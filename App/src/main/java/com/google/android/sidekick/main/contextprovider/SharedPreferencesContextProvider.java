package com.google.android.sidekick.main.contextprovider;

import android.content.Context;
import android.os.Bundle;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext;
import com.google.common.base.Supplier;

public class SharedPreferencesContextProvider
{
  private final Context mAppContext;
  private final Supplier<NowConfigurationPreferences> mPreferences;
  
  public SharedPreferencesContextProvider(Context paramContext, Supplier<NowConfigurationPreferences> paramSupplier)
  {
    this.mAppContext = paramContext;
    this.mPreferences = paramSupplier;
  }
  
  public boolean addBoolean(CardRenderingContext paramCardRenderingContext, int paramInt, boolean paramBoolean)
  {
    return addBoolean(paramCardRenderingContext, this.mAppContext.getString(paramInt), paramBoolean);
  }
  
  public boolean addBoolean(CardRenderingContext paramCardRenderingContext, String paramString, boolean paramBoolean)
  {
    ExtraPreconditions.checkNotMainThread();
    Bundle localBundle = (Bundle)paramCardRenderingContext.putSpecificRenderingContextIfAbsent(SharedPreferencesContext.BUNDLE_KEY, new Bundle());
    boolean bool = ((NowConfigurationPreferences)this.mPreferences.get()).getBoolean(paramString, paramBoolean);
    localBundle.putBoolean(paramString, bool);
    return bool;
  }
  
  public int addInt(CardRenderingContext paramCardRenderingContext, String paramString, int paramInt)
  {
    ExtraPreconditions.checkNotMainThread();
    Bundle localBundle = (Bundle)paramCardRenderingContext.putSpecificRenderingContextIfAbsent(SharedPreferencesContext.BUNDLE_KEY, new Bundle());
    int i = ((NowConfigurationPreferences)this.mPreferences.get()).getInt(paramString, paramInt);
    localBundle.putInt(paramString, i);
    return i;
  }
  
  String keyIsBad(int paramInt)
  {
    String str = this.mAppContext.getString(paramInt);
    if (!((NowConfigurationPreferences)this.mPreferences.get()).contains(str)) {
      return str;
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.SharedPreferencesContextProvider
 * JD-Core Version:    0.7.0.1
 */