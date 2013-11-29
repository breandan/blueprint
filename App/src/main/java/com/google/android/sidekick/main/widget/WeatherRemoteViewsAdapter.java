package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.WeatherEntryAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.WeatherEntry;
import com.google.geo.sidekick.Sidekick.WeatherEntry.WeatherPoint;

public class WeatherRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<WeatherEntryAdapter>
{
  private final WidgetImageLoader mImageLoader;
  
  public WeatherRemoteViewsAdapter(WeatherEntryAdapter paramWeatherEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramWeatherEntryAdapter);
    this.mImageLoader = paramWidgetImageLoader;
  }
  
  private RemoteViews createRemoteViewInternal(Context paramContext, boolean paramBoolean)
  {
    Sidekick.WeatherEntry localWeatherEntry = ((WeatherEntryAdapter)getEntryCardViewAdapter()).getEntry().getWeatherEntry();
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968924);
    if ((localWeatherEntry.hasLocation()) && (localWeatherEntry.getLocation().hasName())) {
      localRemoteViews.setTextViewText(2131296539, localWeatherEntry.getLocation().getName());
    }
    if (localWeatherEntry.hasCurrentConditions()) {
      showCurrentConditions(localRemoteViews, paramContext, localWeatherEntry);
    }
    if ((!localWeatherEntry.hasCurrentConditions()) || (!paramBoolean)) {
      showForecast(localRemoteViews, paramContext, localWeatherEntry, paramBoolean);
    }
    return localRemoteViews;
  }
  
  private boolean maybeShowForecastPoint(RemoteViews paramRemoteViews, Context paramContext, Sidekick.WeatherEntry.WeatherPoint paramWeatherPoint, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((!paramWeatherPoint.hasLabel()) || (!paramWeatherPoint.hasImageUrl())) {
      return false;
    }
    paramRemoteViews.setTextViewText(paramInt2, paramWeatherPoint.getLabel());
    ((WeatherEntryAdapter)getEntryCardViewAdapter());
    Uri localUri = Uri.parse(WeatherEntryAdapter.getImageUrl(paramWeatherPoint.getImageUrl(), paramContext));
    this.mImageLoader.loadImageUri(paramContext, paramRemoteViews, paramInt3, localUri, null);
    paramRemoteViews.setViewVisibility(paramInt1, 0);
    return true;
  }
  
  private void showCurrentConditions(RemoteViews paramRemoteViews, Context paramContext, Sidekick.WeatherEntry paramWeatherEntry)
  {
    Sidekick.WeatherEntry.WeatherPoint localWeatherPoint = paramWeatherEntry.getCurrentConditions();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(localWeatherPoint.getHighTemperature());
    paramRemoteViews.setTextViewText(2131297238, paramContext.getString(2131362334, arrayOfObject));
    if (localWeatherPoint.hasImageUrl())
    {
      ((WeatherEntryAdapter)getEntryCardViewAdapter());
      Uri localUri = Uri.parse(WeatherEntryAdapter.getImageUrl(localWeatherPoint.getImageUrl(), paramContext));
      this.mImageLoader.loadImageUri(paramContext, paramRemoteViews, 2131297232, localUri, null);
    }
  }
  
  private void showForecast(RemoteViews paramRemoteViews, Context paramContext, Sidekick.WeatherEntry paramWeatherEntry, boolean paramBoolean)
  {
    if (!maybeShowForecastPoint(paramRemoteViews, paramContext, paramWeatherEntry.getWeatherPoint(0), 2131297240, 2131297241, 2131297242)) {}
    do
    {
      return;
      paramRemoteViews.setViewVisibility(2131297239, 0);
    } while ((paramBoolean) || (paramWeatherEntry.getWeatherPointCount() < 2) || (!maybeShowForecastPoint(paramRemoteViews, paramContext, paramWeatherEntry.getWeatherPoint(1), 2131297244, 2131297245, 2131297246)));
    paramRemoteViews.setViewVisibility(2131297243, 0);
  }
  
  public boolean canCreateRemoteViews()
  {
    Sidekick.WeatherEntry localWeatherEntry = ((WeatherEntryAdapter)getEntryCardViewAdapter()).getEntry().getWeatherEntry();
    if (localWeatherEntry.hasCurrentConditions()) {
      return true;
    }
    if (localWeatherEntry.getWeatherPointCount() > 0)
    {
      Sidekick.WeatherEntry.WeatherPoint localWeatherPoint = localWeatherEntry.getWeatherPoint(0);
      if ((localWeatherPoint.hasLabel()) && (localWeatherPoint.hasImageUrl())) {}
      for (boolean bool = true;; bool = false) {
        return bool;
      }
    }
    return false;
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext, true);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext, false);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.WeatherRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */