package com.google.android.sidekick.main.notifications;

import android.content.Context;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.WeatherEntry;
import com.google.geo.sidekick.Sidekick.WeatherEntry.WeatherPoint;

public class WeatherNotification
  extends AbstractSingleEntryNotification
{
  private final Sidekick.WeatherEntry mWeatherEntry;
  
  public WeatherNotification(Sidekick.Entry paramEntry)
  {
    super(paramEntry);
    this.mWeatherEntry = paramEntry.getWeatherEntry();
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return null;
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Sidekick.WeatherEntry.WeatherPoint localWeatherPoint = this.mWeatherEntry.getCurrentConditions();
    if (localWeatherPoint == null) {}
    do
    {
      return null;
      if ((localWeatherPoint.hasHighTemperature()) && (localWeatherPoint.hasDescription()))
      {
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = Integer.valueOf(localWeatherPoint.getHighTemperature());
        arrayOfObject2[1] = localWeatherPoint.getDescription();
        return paramContext.getString(2131362337, arrayOfObject2);
      }
    } while (!localWeatherPoint.hasHighTemperature());
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(localWeatherPoint.getHighTemperature());
    return paramContext.getString(2131362334, arrayOfObject1);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    return NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130838077;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.WeatherNotification
 * JD-Core Version:    0.7.0.1
 */