package com.google.android.sidekick.main.tv;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.common.collect.ImmutableSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public class TvConfig
{
  private static final Set<String> SUPPORTED_COUNTRIES = ImmutableSet.of("US");
  
  @Nullable
  public static String getTvSearchQuery(SearchConfig paramSearchConfig, Locale paramLocale)
  {
    Map localMap = paramSearchConfig.getTvSearchQueryByLocale();
    if (localMap.containsKey(paramLocale.toString().toLowerCase(Locale.US))) {
      return (String)localMap.get(paramLocale.toString().toLowerCase(Locale.US));
    }
    if (!TextUtils.isEmpty(paramLocale.getVariant()))
    {
      String str = new Locale(paramLocale.getLanguage(), paramLocale.getCountry()).toString().toLowerCase(Locale.US);
      if (localMap.containsKey(str)) {
        return (String)localMap.get(str);
      }
    }
    return (String)localMap.get(paramLocale.getLanguage().toLowerCase(Locale.US));
  }
  
  public static boolean isCurrentLocaleSupported()
  {
    String str = Locale.getDefault().getCountry().toUpperCase(Locale.US);
    return SUPPORTED_COUNTRIES.contains(str);
  }
  
  public static boolean isFeatureEnabled(Context paramContext, NowConfigurationPreferences paramNowConfigurationPreferences)
  {
    boolean bool1 = isCurrentLocaleSupported();
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = paramNowConfigurationPreferences.getBoolean(paramContext.getString(2131362161), false);
      bool2 = false;
      if (bool3) {
        bool2 = true;
      }
    }
    return bool2;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.tv.TvConfig
 * JD-Core Version:    0.7.0.1
 */