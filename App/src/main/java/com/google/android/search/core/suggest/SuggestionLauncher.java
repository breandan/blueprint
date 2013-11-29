package com.google.android.search.core.suggest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.clicklog.ClickLog;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.suggest.presenter.SuggestionsPresenter;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchBoxStats;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import com.google.android.shared.util.SimpleIntentStarter;
import com.google.android.voicesearch.logger.EventLogger;
import java.util.List;

public class SuggestionLauncher
{
  private final SearchConfig mConfig;
  private final Context mContext;
  private final GlobalSearchServices mGlobalSearchServices;
  private final SimpleIntentStarter mIntentStarter;
  private final String mPackageName;
  private final SuggestionsPresenter mPresenter;
  private final QueryState mQueryState;
  
  public SuggestionLauncher(Context paramContext, SearchConfig paramSearchConfig, String paramString, SuggestionsPresenter paramSuggestionsPresenter, GlobalSearchServices paramGlobalSearchServices, SimpleIntentStarter paramSimpleIntentStarter, QueryState paramQueryState)
  {
    this.mContext = paramContext;
    this.mConfig = paramSearchConfig;
    this.mPackageName = paramString;
    this.mPresenter = paramSuggestionsPresenter;
    this.mGlobalSearchServices = paramGlobalSearchServices;
    this.mIntentStarter = paramSimpleIntentStarter;
    this.mQueryState = paramQueryState;
  }
  
  private Intent createIntent(Suggestion paramSuggestion, String paramString)
  {
    String str1 = paramSuggestion.getSuggestionIntentAction();
    String str2 = paramSuggestion.getSuggestionIntentDataString();
    String str3 = paramSuggestion.getSuggestionQuery();
    String str4 = paramSuggestion.getSuggestionIntentExtraData();
    Intent localIntent = new Intent(str1);
    localIntent.addFlags(268435456);
    localIntent.addFlags(67108864);
    if (str2 != null) {
      localIntent.setData(Uri.parse(str2));
    }
    localIntent.putExtra("user_query", paramString);
    if (str3 != null) {
      localIntent.putExtra("query", str3);
    }
    if (str4 != null) {
      localIntent.putExtra("intent_extra_data_key", str4);
    }
    localIntent.setComponent(paramSuggestion.getSuggestionIntentComponent());
    if (!this.mPackageName.equals(paramSuggestion.getSourcePackageName())) {
      localIntent.setPackage(paramSuggestion.getSourcePackageName());
    }
    return localIntent;
  }
  
  private void launchIntent(Intent paramIntent)
  {
    if (paramIntent == null) {
      return;
    }
    try
    {
      this.mIntentStarter.startActivity(new Intent[] { paramIntent });
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("QSB.SuggestionLauncher", "Failed to start " + paramIntent.toUri(0), localRuntimeException);
    }
  }
  
  public Intent getAppIntent(Suggestion paramSuggestion)
  {
    if (!"android.intent.action.MAIN".equals(paramSuggestion.getSuggestionIntentAction())) {}
    ComponentName localComponentName;
    do
    {
      String str;
      do
      {
        do
        {
          return null;
        } while (!paramSuggestion.isApplicationSuggestion());
        str = paramSuggestion.getSuggestionIntentDataString();
      } while (TextUtils.isEmpty(str));
      localComponentName = Applications.uriToComponentName(Uri.parse(str));
    } while (localComponentName == null);
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.setFlags(270532608);
    localIntent.setComponent(localComponentName);
    return localIntent;
  }
  
  public void onSuggestionClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
  {
    this.mGlobalSearchServices.getClickLog().reportClick(paramSuggestion);
    if (paramSuggestion.isWebSearchSuggestion()) {
      this.mQueryState.commit(this.mQueryState.get().withQueryChars(paramSuggestion.getSuggestionQuery()).withSearchBoxStats(paramSearchBoxStats));
    }
    for (;;)
    {
      String str = paramSearchBoxStats.getLastSuggestionsStats().getSummonEncoding();
      if (str != null)
      {
        String[] arrayOfString = str.split(",");
        int i = 0;
        label73:
        if (i < arrayOfString.length)
        {
          if (arrayOfString[i] != null) {}
          try
          {
            EventLogger.recordClientEvent(120, Integer.valueOf(arrayOfString[i]));
            i++;
            break label73;
            this.mGlobalSearchServices.getClickLog().reportClick(paramSuggestion);
            if (paramSuggestion.isNavSuggestion()) {}
            for (SummonsLogData localSummonsLogData = new SummonsLogData("navsuggestion", null, false);; localSummonsLogData = new SummonsLogData(paramSuggestion.getSourcePackageName(), paramSuggestion.getSourceCanonicalName(), paramSuggestion.isFromIcing()))
            {
              EventLogger.recordClientEvent(105, localSummonsLogData);
              Intent localIntent = getAppIntent(paramSuggestion);
              if (localIntent == null) {
                break label219;
              }
              InternalIcingCorporaProvider.onApplicationLaunched(this.mContext, this.mConfig, new AppLaunchLogger.AppLaunch(0, System.currentTimeMillis(), localIntent.getComponent()));
              launchIntent(localIntent);
              break;
            }
            label219:
            launchIntent(createIntent(paramSuggestion, paramSuggestion.getSuggestionQuery()));
          }
          catch (NumberFormatException localNumberFormatException)
          {
            for (;;)
            {
              Log.e("QSB.SuggestionLauncher", "Invalid package name:" + localNumberFormatException);
            }
          }
        }
      }
    }
  }
  
  public void onSuggestionQuickContactClicked(Suggestion paramSuggestion, SearchBoxStats paramSearchBoxStats)
  {
    if (paramSuggestion != null) {
      this.mGlobalSearchServices.getClickLog().reportClick(paramSuggestion);
    }
  }
  
  public void onSuggestionRemoveFromHistoryClicked(Suggestion paramSuggestion)
  {
    this.mPresenter.removeSuggestionFromHistory(paramSuggestion);
  }
  
  private static class Applications
  {
    public static ComponentName uriToComponentName(Uri paramUri)
    {
      if (paramUri == null) {}
      List localList;
      do
      {
        do
        {
          return null;
        } while ((!"content".equals(paramUri.getScheme())) || (!"applications".equals(paramUri.getAuthority())));
        localList = paramUri.getPathSegments();
      } while ((localList.size() != 3) || (!"applications".equals(localList.get(0))));
      return new ComponentName((String)localList.get(1), (String)localList.get(2));
    }
  }
  
  public static final class SummonsLogData
  {
    public final String canonicalName;
    public final boolean isFromIcing;
    public final String packageName;
    
    public SummonsLogData(String paramString1, String paramString2, boolean paramBoolean)
    {
      this.packageName = paramString1;
      this.canonicalName = paramString2;
      this.isFromIcing = paramBoolean;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionLauncher
 * JD-Core Version:    0.7.0.1
 */