package com.google.android.sidekick.main.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.widget.RemoteViews;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GooglePlayServicesHelper;
import com.google.android.search.core.GooglePlayServicesHelper.Listener;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.WidgetManager;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;

public class PredictiveCardsWidgetProvider
  extends AppWidgetProvider
{
  private static final String TAG = Tag.getTag(PredictiveCardsWidgetProvider.class);
  private GooglePlayServicesHelper.Listener mGooglePlayServicesListener;
  
  private Pair<WidgetPopulator.WidgetLayoutInfo, WidgetPopulator.WidgetLayoutInfo> getLayoutInfo(Context paramContext, Bundle paramBundle)
  {
    float f1 = paramContext.getResources().getDimension(2131689794);
    float f2 = paramContext.getResources().getDimension(2131689795);
    float f3 = paramContext.getResources().getDimension(2131689796);
    float f4 = f1 + 2.0F * f2;
    DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
    float f5 = TypedValue.applyDimension(1, paramBundle.getInt("appWidgetMinHeight"), localDisplayMetrics);
    float f6 = TypedValue.applyDimension(1, paramBundle.getInt("appWidgetMaxHeight"), localDisplayMetrics);
    int i = (int)Math.floor(Math.max(f5, f4) / f4);
    int j = (int)Math.floor(Math.max(f6, f4) / f4);
    int k = paramContext.getResources().getDimensionPixelSize(2131689811);
    float f7 = TypedValue.applyDimension(1, paramBundle.getInt("appWidgetMinWidth"), localDisplayMetrics);
    boolean bool1;
    boolean bool2;
    label174:
    boolean bool3;
    if (TypedValue.applyDimension(1, paramBundle.getInt("appWidgetMaxWidth"), localDisplayMetrics) < k)
    {
      bool1 = true;
      if (f7 >= k) {
        break label276;
      }
      bool2 = true;
      int m = (int)(f3 + 2.0F * f2);
      if ((i <= 1) && (f5 - f1 * i <= m)) {
        break label282;
      }
      bool3 = true;
      label208:
      if ((j <= 1) && (f6 - f1 * j <= m)) {
        break label288;
      }
    }
    label276:
    label282:
    label288:
    for (boolean bool4 = true;; bool4 = false)
    {
      WidgetPopulator.WidgetLayoutInfo localWidgetLayoutInfo1 = new WidgetPopulator.WidgetLayoutInfo(i, bool1, bool3);
      WidgetPopulator.WidgetLayoutInfo localWidgetLayoutInfo2 = new WidgetPopulator.WidgetLayoutInfo(j, bool2, bool4);
      return Pair.create(localWidgetLayoutInfo1, localWidgetLayoutInfo2);
      bool1 = false;
      break;
      bool2 = false;
      break label174;
      bool3 = false;
      break label208;
    }
  }
  
  public void onAppWidgetOptionsChanged(Context paramContext, AppWidgetManager paramAppWidgetManager, int paramInt, Bundle paramBundle)
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    WidgetImageLoader localWidgetImageLoader = new WidgetImageLoader(localVelvetServices.getImageLoader(), localVelvetServices.getSidekickInjector().getWidgetManager());
    WidgetPopulator localWidgetPopulator = new WidgetPopulator(localVelvetServices.getSidekickInjector().getEntryProvider(), localWidgetImageLoader, localVelvetServices.getCoreServices().getLoginHelper(), localVelvetServices.getCoreServices().getNowOptInSettings(), localVelvetServices.getCoreServices().getClock(), localVelvetServices.getCoreServices().getGooglePlayServicesHelper(), localVelvetServices.getSidekickInjector().getCalendarDataProvider(), localVelvetServices.getSidekickInjector().getEntryCardViewFactory(), localVelvetServices.getSidekickInjector().getEntryTreePruner());
    Pair localPair = getLayoutInfo(paramContext, paramBundle);
    paramAppWidgetManager.updateAppWidget(paramInt, localWidgetPopulator.populateView(paramContext, (WidgetPopulator.WidgetLayoutInfo)localPair.first, (WidgetPopulator.WidgetLayoutInfo)localPair.second, paramInt));
  }
  
  public void onDisabled(Context paramContext)
  {
    CoreSearchServices localCoreSearchServices = VelvetServices.get().getCoreServices();
    localCoreSearchServices.getUserInteractionLogger().logAnalyticsAction("WIDGET_REMOVED", null);
    unregisterGooglePlayServicesListener(localCoreSearchServices.getGooglePlayServicesHelper());
    super.onDisabled(paramContext);
  }
  
  public void onEnabled(Context paramContext)
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    CoreSearchServices localCoreSearchServices = localVelvetServices.getCoreServices();
    localCoreSearchServices.getUserInteractionLogger().logAnalyticsAction("WIDGET_INSTALLED", null);
    registerGooglePlayServicesListener(localCoreSearchServices.getGooglePlayServicesHelper(), localVelvetServices.getSidekickInjector().getWidgetManager());
    super.onEnabled(paramContext);
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramIntent.getAction();
    if ((!paramIntent.getBooleanExtra("internal_request", false)) && ("android.appwidget.action.APPWIDGET_UPDATE".equals(str)))
    {
      VelvetServices.get().getSidekickInjector().getWidgetManager().updateWidget();
      return;
    }
    super.onReceive(paramContext, paramIntent);
  }
  
  public void onUpdate(Context paramContext, AppWidgetManager paramAppWidgetManager, int[] paramArrayOfInt)
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    WidgetImageLoader localWidgetImageLoader = new WidgetImageLoader(localVelvetServices.getImageLoader(), localVelvetServices.getSidekickInjector().getWidgetManager());
    WidgetPopulator localWidgetPopulator = new WidgetPopulator(localVelvetServices.getSidekickInjector().getEntryProvider(), localWidgetImageLoader, localVelvetServices.getCoreServices().getLoginHelper(), localVelvetServices.getCoreServices().getNowOptInSettings(), localVelvetServices.getCoreServices().getClock(), localVelvetServices.getCoreServices().getGooglePlayServicesHelper(), localVelvetServices.getSidekickInjector().getCalendarDataProvider(), localVelvetServices.getSidekickInjector().getEntryCardViewFactory(), localVelvetServices.getSidekickInjector().getEntryTreePruner());
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      Pair localPair = getLayoutInfo(paramContext, paramAppWidgetManager.getAppWidgetOptions(paramArrayOfInt[i]));
      RemoteViews localRemoteViews = localWidgetPopulator.populateView(paramContext, (WidgetPopulator.WidgetLayoutInfo)localPair.first, (WidgetPopulator.WidgetLayoutInfo)localPair.second, paramArrayOfInt[i]);
      paramAppWidgetManager.updateAppWidget(paramArrayOfInt[i], localRemoteViews);
    }
    if (paramArrayOfInt.length > 0) {
      localVelvetServices.getCoreServices().getUserInteractionLogger().logInternalActionOncePerDay("WIDGET_UPDATE", null);
    }
    super.onUpdate(paramContext, paramAppWidgetManager, paramArrayOfInt);
  }
  
  void registerGooglePlayServicesListener(GooglePlayServicesHelper paramGooglePlayServicesHelper, final WidgetManager paramWidgetManager)
  {
    if (this.mGooglePlayServicesListener != null) {
      return;
    }
    this.mGooglePlayServicesListener = new GooglePlayServicesHelper.Listener()
    {
      public void onAvailabilityChanged(int paramAnonymousInt)
      {
        paramWidgetManager.updateWidget();
      }
    };
    paramGooglePlayServicesHelper.addListener(this.mGooglePlayServicesListener);
  }
  
  void unregisterGooglePlayServicesListener(GooglePlayServicesHelper paramGooglePlayServicesHelper)
  {
    if (this.mGooglePlayServicesListener == null) {
      return;
    }
    paramGooglePlayServicesHelper.removeListener(this.mGooglePlayServicesListener);
    this.mGooglePlayServicesListener = null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.PredictiveCardsWidgetProvider
 * JD-Core Version:    0.7.0.1
 */