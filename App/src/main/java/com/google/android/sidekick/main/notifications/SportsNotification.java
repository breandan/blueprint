package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.format.DateUtils;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.SportScoreEntry;
import com.google.geo.sidekick.Sidekick.SportScoreEntry.SportEntity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SportsNotification
  extends AbstractSingleEntryNotification
{
  private final Sidekick.SportScoreEntry mSportEntry;
  
  public SportsNotification(Sidekick.Entry paramEntry)
  {
    super(paramEntry);
    this.mSportEntry = paramEntry.getSportScoreEntry();
  }
  
  String getFormattedStartTime(Context paramContext)
  {
    long l = 1000L * this.mSportEntry.getStartTimeSeconds();
    if (DateUtils.isToday(l))
    {
      Date localDate = new Date(l);
      return paramContext.getString(2131362347, new Object[] { android.text.format.DateFormat.getTimeFormat(paramContext).format(localDate), new SimpleDateFormat("z").format(localDate) });
    }
    return DateUtils.formatDateTime(paramContext, l, 0);
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    switch (this.mSportEntry.getStatusCode())
    {
    default: 
      return null;
    case 0: 
      return paramContext.getString(2131362352);
    case 1: 
      return paramContext.getString(2131362376);
    }
    if (DateUtils.isToday(1000L * this.mSportEntry.getStartTimeSeconds()))
    {
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = getFormattedStartTime(paramContext);
      return paramContext.getString(2131362374, arrayOfObject2);
    }
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = getFormattedStartTime(paramContext);
    return paramContext.getString(2131362375, arrayOfObject1);
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Sidekick.SportScoreEntry.SportEntity localSportEntity1 = this.mSportEntry.getSportEntity(0);
    Sidekick.SportScoreEntry.SportEntity localSportEntity2 = this.mSportEntry.getSportEntity(1);
    if ((localSportEntity1.hasScore()) && (localSportEntity2.hasScore()))
    {
      if (this.mSportEntry.getSport() == 4) {}
      for (int i = 2131362373;; i = 2131362372)
      {
        Object[] arrayOfObject2 = new Object[4];
        arrayOfObject2[0] = BidiUtils.unicodeWrap(localSportEntity1.getName());
        arrayOfObject2[1] = localSportEntity1.getScore();
        arrayOfObject2[2] = BidiUtils.unicodeWrap(localSportEntity2.getName());
        arrayOfObject2[3] = localSportEntity2.getScore();
        return paramContext.getString(i, arrayOfObject2);
      }
    }
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = BidiUtils.unicodeWrap(localSportEntity1.getName());
    arrayOfObject1[1] = BidiUtils.unicodeWrap(localSportEntity2.getName());
    return paramContext.getString(2131362371, arrayOfObject1);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    return NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    switch (this.mSportEntry.getSport())
    {
    default: 
      return 2130838071;
    case 2: 
      return 2130838059;
    case 0: 
      return 2130838060;
    case 1: 
      return 2130838064;
    case 3: 
      return 2130838065;
    }
    return 2130838070;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.SportsNotification
 * JD-Core Version:    0.7.0.1
 */