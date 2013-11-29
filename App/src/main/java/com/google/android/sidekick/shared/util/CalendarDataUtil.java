package com.google.android.sidekick.shared.util;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarInfo;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.android.apps.sidekick.calendar.Calendar.ServerData;
import com.google.android.apps.sidekick.calendar.Calendar.ServerData.LatLng;
import com.google.common.base.Charsets;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

public class CalendarDataUtil
{
  private static final String TAG = Tag.getTag(CalendarDataUtil.class);
  
  public static Intent createEmailAttendeesIntent(long paramLong)
  {
    Intent localIntent = new Intent("com.android.calendar.MAIL");
    localIntent.setComponent(ComponentName.unflattenFromString("com.google.android.calendar/com.android.calendar.alerts.AlertReceiver"));
    localIntent.putExtra("eventid", paramLong);
    return localIntent;
  }
  
  public static Calendar.CalendarInfo getCalendarInfoForDbId(long paramLong, Collection<Calendar.CalendarInfo> paramCollection)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Calendar.CalendarInfo localCalendarInfo = (Calendar.CalendarInfo)localIterator.next();
      if (localCalendarInfo.getDbId() == paramLong) {
        return localCalendarInfo;
      }
    }
    return null;
  }
  
  @Nullable
  public static Sidekick.Location getCalendarLocation(Sidekick.CalendarEntry paramCalendarEntry, Calendar.CalendarData paramCalendarData)
  {
    Sidekick.Location localLocation;
    if (paramCalendarData == null) {
      localLocation = null;
    }
    Calendar.ServerData localServerData;
    do
    {
      return localLocation;
      localLocation = new Sidekick.Location();
      if ((paramCalendarData.hasEventData()) && (!TextUtils.isEmpty(paramCalendarData.getEventData().getWhereField()))) {
        localLocation.setName(paramCalendarData.getEventData().getWhereField());
      }
      localServerData = paramCalendarData.getServerData();
      if ((paramCalendarEntry.hasLocation()) && (paramCalendarEntry.getLocation().hasLat()) && (paramCalendarEntry.getLocation().hasLng()))
      {
        localLocation.setLat(paramCalendarEntry.getLocation().getLat());
        localLocation.setLng(paramCalendarEntry.getLocation().getLng());
        return localLocation;
      }
    } while ((localServerData == null) || (!localServerData.getIsGeocodable()));
    localLocation.setLat(localServerData.getGeocodedLatLng().getLat());
    localLocation.setLng(localServerData.getGeocodedLatLng().getLng());
    return localLocation;
  }
  
  @Nullable
  public static String getHashString(String paramString)
  {
    try
    {
      String str = Base64.encodeToString(MessageDigest.getInstance("MD5").digest(paramString.getBytes(Charsets.UTF_8)), 2);
      return str;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      Log.w(TAG, "MD5 not available for calendar id hash " + localNoSuchAlgorithmException);
    }
    return null;
  }
  
  public static Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount toCalendarAccount(Calendar.CalendarInfo paramCalendarInfo)
  {
    return new Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount().setHashedId(getHashString(paramCalendarInfo.getId()));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.CalendarDataUtil
 * JD-Core Version:    0.7.0.1
 */