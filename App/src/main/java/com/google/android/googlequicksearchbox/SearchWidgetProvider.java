package com.google.android.googlequicksearchbox;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.google.android.velvet.util.IntentUtils;

public class SearchWidgetProvider
  extends BroadcastReceiver
{
  private static SearchWidgetState getSearchWidgetState(Context paramContext, int paramInt)
  {
    SearchWidgetState localSearchWidgetState = new SearchWidgetState(paramInt);
    localSearchWidgetState.setQueryTextViewIntent(IntentUtils.createSearchIntent(paramContext, "launcher-widget"));
    localSearchWidgetState.setVoiceSearchIntent(IntentUtils.createVoiceSearchIntent(paramContext, "launcher-widget"));
    return localSearchWidgetState;
  }
  
  private static SearchWidgetState[] getSearchWidgetStates(Context paramContext)
  {
    int[] arrayOfInt = AppWidgetManager.getInstance(paramContext).getAppWidgetIds(myComponentName(paramContext));
    SearchWidgetState[] arrayOfSearchWidgetState = new SearchWidgetState[arrayOfInt.length];
    for (int i = 0; i < arrayOfInt.length; i++) {
      arrayOfSearchWidgetState[i] = getSearchWidgetState(paramContext, arrayOfInt[i]);
    }
    return arrayOfSearchWidgetState;
  }
  
  private static ComponentName myComponentName(Context paramContext)
  {
    return new ComponentName(paramContext.getPackageName(), SearchWidgetProvider.class.getCanonicalName());
  }
  
  public static void updateSearchWidgets(Context paramContext)
  {
    SearchWidgetState[] arrayOfSearchWidgetState = getSearchWidgetStates(paramContext);
    int i = arrayOfSearchWidgetState.length;
    for (int j = 0; j < i; j++) {
      arrayOfSearchWidgetState[j].updateWidget(paramContext, AppWidgetManager.getInstance(paramContext));
    }
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramIntent.getAction();
    if ("android.appwidget.action.APPWIDGET_ENABLED".equals(str)) {}
    while (!"android.appwidget.action.APPWIDGET_UPDATE".equals(str)) {
      return;
    }
    updateSearchWidgets(paramContext);
  }
  
  private static class SearchWidgetState
  {
    private final int mAppWidgetId;
    private Intent mQueryTextViewIntent;
    private Intent mVoiceSearchIntent;
    
    public SearchWidgetState(int paramInt)
    {
      this.mAppWidgetId = paramInt;
    }
    
    private void setOnClickActivityIntent(Context paramContext, RemoteViews paramRemoteViews, int paramInt, Intent paramIntent)
    {
      paramRemoteViews.setOnClickPendingIntent(paramInt, PendingIntent.getActivity(paramContext, 0, paramIntent, 0));
    }
    
    public void setQueryTextViewIntent(Intent paramIntent)
    {
      this.mQueryTextViewIntent = paramIntent;
    }
    
    public void setVoiceSearchIntent(Intent paramIntent)
    {
      this.mVoiceSearchIntent = paramIntent;
    }
    
    public void updateWidget(Context paramContext, AppWidgetManager paramAppWidgetManager)
    {
      RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968819);
      setOnClickActivityIntent(paramContext, localRemoteViews, 2131296977, this.mQueryTextViewIntent);
      if (this.mVoiceSearchIntent != null)
      {
        setOnClickActivityIntent(paramContext, localRemoteViews, 2131296979, this.mVoiceSearchIntent);
        localRemoteViews.setViewVisibility(2131296978, 0);
      }
      for (;;)
      {
        paramAppWidgetManager.updateAppWidget(this.mAppWidgetId, localRemoteViews);
        return;
        localRemoteViews.setViewVisibility(2131296978, 8);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.googlequicksearchbox.SearchWidgetProvider
 * JD-Core Version:    0.7.0.1
 */