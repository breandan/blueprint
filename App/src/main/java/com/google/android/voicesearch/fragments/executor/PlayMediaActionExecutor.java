package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.net.Uri;
import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.util.AppSelectionHelper;
import com.google.android.voicesearch.util.AppSelectionHelper.App;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class PlayMediaActionExecutor
  implements ActionExecutor<PlayMediaAction>
{
  private final AppSelectionHelper mAppSelectionHelper;
  private final IntentStarter mIntentStarter;
  
  public PlayMediaActionExecutor(IntentStarter paramIntentStarter, AppSelectionHelper paramAppSelectionHelper)
  {
    this.mIntentStarter = paramIntentStarter;
    this.mAppSelectionHelper = paramAppSelectionHelper;
  }
  
  private static void addIfNotExists(AppSelectionHelper.App paramApp, Map<String, AppSelectionHelper.App> paramMap)
  {
    if (!paramMap.containsKey(paramApp.getPackageName())) {
      paramMap.put(paramApp.getPackageName(), paramApp);
    }
  }
  
  private Intent getMediaPreviewIntent(PlayMediaAction paramPlayMediaAction)
  {
    if (!paramPlayMediaAction.getActionV2().hasItemPreviewUrl()) {
      return null;
    }
    return getPreviewIntent(paramPlayMediaAction);
  }
  
  private boolean hasPlayStoreIntent(ActionV2Protos.PlayMediaAction paramPlayMediaAction)
  {
    List localList = paramPlayMediaAction.getIntentFlagList();
    return (localList.contains(Integer.valueOf(1))) || (localList.contains(Integer.valueOf(2)));
  }
  
  public boolean canExecute(PlayMediaAction paramPlayMediaAction)
  {
    if ((paramPlayMediaAction.getApps() == null) && (!setUpAppsForAction(paramPlayMediaAction))) {}
    while (paramPlayMediaAction.getApps().isEmpty()) {
      return false;
    }
    return true;
  }
  
  public boolean execute(PlayMediaAction paramPlayMediaAction)
  {
    AppSelectionHelper.App localApp = paramPlayMediaAction.getSelectedApp();
    this.mAppSelectionHelper.appSelected(paramPlayMediaAction.getMimeType(), localApp.getPackageName());
    if (paramPlayMediaAction.getPlayStoreLink() == localApp) {
      EventLogger.recordClientEvent(91, Integer.valueOf(getActionTypeLog()));
    }
    Intent localIntent = paramPlayMediaAction.getSelectedApp().getLaunchIntent();
    localIntent.addFlags(268435456);
    return this.mIntentStarter.startActivity(new Intent[] { localIntent });
  }
  
  protected abstract int getActionTypeLog();
  
  public AppSelectionHelper getAppSelectionHelper()
  {
    return this.mAppSelectionHelper;
  }
  
  protected AppSelectionHelper.App getDefaultApp(PlayMediaAction paramPlayMediaAction, Collection<AppSelectionHelper.App> paramCollection)
  {
    boolean bool;
    ActionV2Protos.PlayMediaAction localPlayMediaAction;
    AppSelectionHelper.App localApp1;
    AppSelectionHelper.App localApp2;
    AppSelectionHelper.App localApp3;
    if (!paramCollection.isEmpty())
    {
      bool = true;
      Preconditions.checkArgument(bool);
      localPlayMediaAction = paramPlayMediaAction.getActionV2();
      localApp1 = getAppSelectionHelper().getSelectedApp(paramPlayMediaAction.getMimeType(), paramCollection);
      localApp2 = paramPlayMediaAction.getPlayStoreLink();
      localApp3 = paramPlayMediaAction.getGoogleContentApp();
      switch (localPlayMediaAction.getOwnershipStatus())
      {
      }
    }
    do
    {
      do
      {
        do
        {
          return localApp1;
          bool = false;
          break;
        } while ((!hasPlayStoreIntent(localPlayMediaAction)) || (localApp2 == null));
        return localApp2;
      } while ((localApp1 != localApp2) || (localApp3 == null));
      return localApp3;
    } while (((!hasPlayStoreIntent(localPlayMediaAction)) && (localApp1 != localApp3)) || (localApp2 == null));
    return localApp2;
  }
  
  @Nullable
  protected AppSelectionHelper.App getGoogleContentApp(PlayMediaAction paramPlayMediaAction)
  {
    List localList = getAppSelectionHelper().findActivities(getGoogleContentAppIntent(paramPlayMediaAction));
    if (localList.size() >= 1) {
      return (AppSelectionHelper.App)localList.get(0);
    }
    return null;
  }
  
  @Nullable
  protected Intent getGoogleContentAppIntent(PlayMediaAction paramPlayMediaAction)
  {
    return null;
  }
  
  protected ImmutableList<AppSelectionHelper.App> getLocalApps(PlayMediaAction paramPlayMediaAction)
  {
    return ImmutableList.copyOf(getAppSelectionHelper().findActivities(getOpenFromSearchIntent(paramPlayMediaAction)));
  }
  
  @Nullable
  protected Intent getOpenFromSearchIntent(PlayMediaAction paramPlayMediaAction)
  {
    return null;
  }
  
  @Nullable
  protected AppSelectionHelper.App getPlayStoreApp(PlayMediaAction paramPlayMediaAction)
  {
    AppSelectionHelper.App localApp = getAppSelectionHelper().getPlayStoreLink(Uri.parse(paramPlayMediaAction.getActionV2().getUrl()));
    if ((localApp != null) && (getAppSelectionHelper().isSupported(localApp.getLaunchIntent()))) {
      return localApp;
    }
    return null;
  }
  
  @Nullable
  protected Intent getPreviewIntent(PlayMediaAction paramPlayMediaAction)
  {
    return null;
  }
  
  public boolean openExternalApp(PlayMediaAction paramPlayMediaAction)
  {
    Intent localIntent = getPreviewIntent(paramPlayMediaAction);
    boolean bool = false;
    if (localIntent != null) {
      bool = this.mIntentStarter.startActivity(new Intent[] { localIntent });
    }
    return bool;
  }
  
  public boolean setUpAppsForAction(PlayMediaAction paramPlayMediaAction)
  {
    Intent localIntent = getMediaPreviewIntent(paramPlayMediaAction);
    if ((localIntent != null) && (getAppSelectionHelper().isSupported(localIntent))) {}
    LinkedHashMap localLinkedHashMap;
    ImmutableList localImmutableList;
    for (boolean bool = true;; bool = false)
    {
      paramPlayMediaAction.setPreviewEnabled(bool);
      localLinkedHashMap = Maps.newLinkedHashMap();
      AppSelectionHelper.App localApp1 = getPlayStoreApp(paramPlayMediaAction);
      paramPlayMediaAction.setPlayStoreLink(localApp1);
      if (localApp1 != null) {
        addIfNotExists(localApp1, localLinkedHashMap);
      }
      if (paramPlayMediaAction.getActionV2().getIsFromSoundSearch()) {
        break label181;
      }
      AppSelectionHelper.App localApp2 = getGoogleContentApp(paramPlayMediaAction);
      paramPlayMediaAction.setGoogleContentApp(localApp2);
      if ((localApp2 != null) && ((paramPlayMediaAction.getActionV2().getOwnershipStatus() == 3) || (paramPlayMediaAction.getActionV2().getOwnershipStatus() == 4) || (paramPlayMediaAction.getActionV2().getOwnershipStatus() == 1))) {
        addIfNotExists(localApp2, localLinkedHashMap);
      }
      localImmutableList = getLocalApps(paramPlayMediaAction);
      Iterator localIterator = localImmutableList.iterator();
      while (localIterator.hasNext()) {
        addIfNotExists((AppSelectionHelper.App)localIterator.next(), localLinkedHashMap);
      }
    }
    paramPlayMediaAction.setLocalResults(localImmutableList);
    label181:
    Collection localCollection = localLinkedHashMap.values();
    paramPlayMediaAction.setApps(localCollection);
    if (!localCollection.isEmpty())
    {
      paramPlayMediaAction.selectApp(getDefaultApp(paramPlayMediaAction, localCollection));
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.PlayMediaActionExecutor
 * JD-Core Version:    0.7.0.1
 */