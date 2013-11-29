package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.Html;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.BarcodeEntry;
import com.google.geo.sidekick.Sidekick.BarcodeEntry.FlightBoardingPass;
import com.google.geo.sidekick.Sidekick.Entry;

public class BarcodeNotification
  extends AbstractSingleEntryNotification
{
  private final Sidekick.BarcodeEntry.FlightBoardingPass mPass;
  
  public BarcodeNotification(Sidekick.Entry paramEntry)
  {
    super(paramEntry);
    this.mPass = paramEntry.getBarcodeEntry().getFlightBoardingPass();
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = BidiUtils.unicodeWrap(this.mPass.getPassengerName());
    return Html.fromHtml(paramContext.getString(2131362322, arrayOfObject));
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = BidiUtils.unicodeWrap(this.mPass.getAirlineName());
    arrayOfObject[1] = BidiUtils.unicodeWrap(this.mPass.getFlightNumber());
    return paramContext.getString(2131362327, arrayOfObject);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    return NowNotificationManager.NotificationType.BARCODE_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130837733;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getNotificationContentTitle(paramContext, paramCardRenderingContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.BarcodeNotification
 * JD-Core Version:    0.7.0.1
 */