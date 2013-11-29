package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.TextView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails.Alert;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class TravelReport
{
  private static final String TAG = Tag.getTag(TravelReport.class);
  private final Sidekick.CommuteSummary mRoute;
  
  public TravelReport(Sidekick.CommuteSummary paramCommuteSummary)
  {
    this.mRoute = ((Sidekick.CommuteSummary)Preconditions.checkNotNull(paramCommuteSummary));
  }
  
  public static String getColorForHtml(int paramInt)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(0xFFFFFF & paramInt);
    return String.format(localLocale, "#%1$h", arrayOfObject);
  }
  
  public static String getTrafficStatusAsString(Context paramContext, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 3: 
      return paramContext.getString(2131362282);
    case 1: 
      return paramContext.getString(2131362284);
    }
    return paramContext.getString(2131362283);
  }
  
  static String multipleAlertTypeTimeString(Context paramContext, Sidekick.CommuteSummary.TransitDetails.Alert paramAlert, long paramLong)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    String str = "";
    switch (paramAlert.getType())
    {
    default: 
      Log.e(TAG, "Unknown Alert Type:" + paramAlert.getType());
    }
    while ((paramLong >= paramAlert.getStartTimeSeconds()) && (paramLong < paramAlert.getEndTimeSeconds()))
    {
      Object[] arrayOfObject3 = new Object[4];
      arrayOfObject3[0] = "<b>";
      arrayOfObject3[1] = BidiUtils.unicodeWrap(str);
      arrayOfObject3[2] = "</b>";
      arrayOfObject3[3] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getEndTimeSeconds(), 0);
      return paramContext.getString(i, arrayOfObject3);
      Object[] arrayOfObject4 = new Object[3];
      arrayOfObject4[0] = "<b>";
      arrayOfObject4[1] = BidiUtils.unicodeWrap(paramAlert.getLineName());
      arrayOfObject4[2] = "</b>";
      return paramContext.getString(2131362238, arrayOfObject4);
      i = 2131362241;
      j = 2131362242;
      k = 2131362235;
      str = paramAlert.getStationLocation().getName();
      continue;
      i = 2131362245;
      j = 2131362246;
      k = 2131362237;
      str = paramAlert.getLineName();
      continue;
      i = 2131362243;
      j = 2131362244;
      k = 2131362236;
      str = paramAlert.getLineName();
    }
    if (paramLong < paramAlert.getStartTimeSeconds())
    {
      Object[] arrayOfObject2 = new Object[5];
      arrayOfObject2[0] = "<b>";
      arrayOfObject2[1] = BidiUtils.unicodeWrap(str);
      arrayOfObject2[2] = "</b>";
      arrayOfObject2[3] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getStartTimeSeconds(), 0);
      arrayOfObject2[4] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getEndTimeSeconds(), 0);
      return paramContext.getString(j, arrayOfObject2);
    }
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = "<b>";
    arrayOfObject1[1] = BidiUtils.unicodeWrap(str);
    arrayOfObject1[2] = "</b>";
    return paramContext.getString(k, arrayOfObject1);
  }
  
  private int numTransitAlerts()
  {
    Sidekick.CommuteSummary.TransitDetails localTransitDetails = getTransitDetails();
    if (localTransitDetails == null) {
      return 0;
    }
    return localTransitDetails.getAlertCount();
  }
  
  @Nullable
  public static String singleAlertTimeContextString(Context paramContext, Sidekick.CommuteSummary.TransitDetails.Alert paramAlert, long paramLong)
  {
    if ((paramLong >= paramAlert.getStartTimeSeconds()) && (paramLong < paramAlert.getEndTimeSeconds()))
    {
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getEndTimeSeconds(), 0);
      return paramContext.getString(2131362239, arrayOfObject2);
    }
    if (paramLong < paramAlert.getStartTimeSeconds())
    {
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getStartTimeSeconds(), 0);
      arrayOfObject1[1] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getEndTimeSeconds(), 0);
      return paramContext.getString(2131362240, arrayOfObject1);
    }
    return null;
  }
  
  static String singleAlertTypeTimeString(Context paramContext, Sidekick.CommuteSummary.TransitDetails.Alert paramAlert, long paramLong)
  {
    if (paramAlert.getType() == 1) {}
    for (String str = paramAlert.getStationLocation().getName(); (paramLong >= paramAlert.getStartTimeSeconds()) && (paramLong < paramAlert.getEndTimeSeconds()); str = paramAlert.getLineName())
    {
      Object[] arrayOfObject3 = new Object[4];
      arrayOfObject3[0] = "<b>";
      arrayOfObject3[1] = BidiUtils.unicodeWrap(str);
      arrayOfObject3[2] = "</b>";
      arrayOfObject3[3] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getEndTimeSeconds(), 0);
      return paramContext.getString(2131362247, arrayOfObject3);
    }
    if (paramLong < paramAlert.getStartTimeSeconds())
    {
      Object[] arrayOfObject2 = new Object[5];
      arrayOfObject2[0] = "<b>";
      arrayOfObject2[1] = BidiUtils.unicodeWrap(str);
      arrayOfObject2[2] = "</b>";
      arrayOfObject2[3] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getStartTimeSeconds(), 0);
      arrayOfObject2[4] = TimeUtilities.formatDisplayTime(paramContext, 1000L * paramAlert.getEndTimeSeconds(), 0);
      return paramContext.getString(2131362248, arrayOfObject2);
    }
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = BidiUtils.unicodeWrap(str);
    return String.format("<b>%s</b>", arrayOfObject1);
  }
  
  private SparseBooleanArray transitAlertTypes()
  {
    SparseBooleanArray localSparseBooleanArray = new SparseBooleanArray();
    Sidekick.CommuteSummary.TransitDetails localTransitDetails = getTransitDetails();
    if (localTransitDetails == null) {}
    for (;;)
    {
      return localSparseBooleanArray;
      Iterator localIterator = localTransitDetails.getAlertList().iterator();
      while (localIterator.hasNext()) {
        localSparseBooleanArray.put(((Sidekick.CommuteSummary.TransitDetails.Alert)localIterator.next()).getType(), true);
      }
    }
  }
  
  @Nullable
  public String buildCommuteString(Context paramContext)
  {
    Integer localInteger = getTotalEtaMinutes();
    if (localInteger == null) {
      return null;
    }
    Object[] arrayOfObject6;
    if (localInteger.intValue() >= 60)
    {
      arrayOfObject6 = new Object[2];
      arrayOfObject6[0] = Integer.valueOf(localInteger.intValue() / 60);
      arrayOfObject6[1] = Integer.valueOf(localInteger.intValue() % 60);
    }
    String str2;
    int j;
    for (String str1 = paramContext.getString(2131362220, arrayOfObject6);; str1 = paramContext.getString(2131362219, new Object[] { localInteger }))
    {
      str2 = "";
      if (this.mRoute.hasHistoricalTrafficDelayInMinutes())
      {
        j = this.mRoute.getTrafficDelayInMinutes() - this.mRoute.getHistoricalTrafficDelayInMinutes();
        if (j != 0) {
          break;
        }
        Resources localResources3 = paramContext.getResources();
        int n = this.mRoute.getTrafficDelayInMinutes();
        Object[] arrayOfObject5 = new Object[1];
        arrayOfObject5[0] = Integer.valueOf(this.mRoute.getTrafficDelayInMinutes());
        str2 = localResources3.getQuantityString(2131558427, n, arrayOfObject5);
      }
      if ((str2.isEmpty()) && (this.mRoute.hasTrafficDelayInMinutes()))
      {
        int i = this.mRoute.getTrafficDelayInMinutes();
        Resources localResources1 = paramContext.getResources();
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = Integer.valueOf(i);
        str2 = localResources1.getQuantityString(2131558424, i, arrayOfObject3);
      }
      if ((!this.mRoute.hasRouteSummary()) || (getTravelMode() == 1)) {
        break label380;
      }
      if (!str2.isEmpty()) {
        break label343;
      }
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = str1;
      arrayOfObject2[1] = this.mRoute.getRouteSummary();
      return paramContext.getString(2131362265, arrayOfObject2);
    }
    if (j > 0) {}
    for (int k = 2131558425;; k = 2131558426)
    {
      int m = Math.abs(j);
      Resources localResources2 = paramContext.getResources();
      Object[] arrayOfObject4 = new Object[1];
      arrayOfObject4[0] = Integer.valueOf(m);
      str2 = localResources2.getQuantityString(k, m, arrayOfObject4);
      break;
    }
    label343:
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = str1;
    arrayOfObject1[1] = str2;
    arrayOfObject1[2] = this.mRoute.getRouteSummary();
    return paramContext.getString(2131362264, arrayOfObject1);
    label380:
    if (str2.isEmpty()) {
      return paramContext.getString(2131362269, new Object[] { str1 });
    }
    return paramContext.getString(2131362263, new Object[] { str1, str2 });
  }
  
  public String getAlertString(Context paramContext, Clock paramClock, Sidekick.CommuteSummary.TransitDetails.Alert paramAlert)
  {
    long l = paramClock.currentTimeMillis() / 1000L;
    if (transitAlertTypes().size() > 1) {
      return multipleAlertTypeTimeString(paramContext, paramAlert, l);
    }
    return singleAlertTypeTimeString(paramContext, paramAlert, l);
  }
  
  public Sidekick.CommuteSummary getRoute()
  {
    return this.mRoute;
  }
  
  public CharSequence getRouteDescriptionWithTraffic(Context paramContext)
  {
    int i = getTravelMode();
    if (i == 0)
    {
      String str4 = getRoute().getRouteSummary();
      Object localObject = getTrafficStatusAsString(paramContext);
      if ((str4 != null) && (localObject != null)) {
        localObject = Html.fromHtml(paramContext.getString(2131362259, new Object[] { localObject, str4 }));
      }
      do
      {
        return localObject;
        if (str4 != null) {
          return Html.fromHtml(str4);
        }
      } while (localObject != null);
      return null;
    }
    if (i == 1)
    {
      Sidekick.CommuteSummary.TransitDetails localTransitDetails = getTransitDetails();
      if (localTransitDetails == null) {
        return null;
      }
      Sidekick.Location localLocation = localTransitDetails.getStationLocation();
      if ((localLocation == null) || (!localTransitDetails.hasWalkingTimeMinutes())) {
        return null;
      }
      long l = System.currentTimeMillis() / 1000L;
      if ((!localTransitDetails.hasDepartureTimeSeconds()) || (l > localTransitDetails.getDepartureTimeSeconds())) {
        return null;
      }
      Date localDate = new Date(1000L * localTransitDetails.getDepartureTimeSeconds());
      String str1 = android.text.format.DateFormat.getTimeFormat(paramContext).format(localDate);
      String str2 = localTransitDetails.getTransitLineName();
      Object[] arrayOfObject2;
      if (localTransitDetails.getWalkingTimeMinutes() > 0)
      {
        arrayOfObject2 = new Object[6];
        arrayOfObject2[0] = "<b>";
        arrayOfObject2[1] = BidiUtils.unicodeWrap(str2);
        arrayOfObject2[2] = "</b>";
        arrayOfObject2[3] = str1;
        arrayOfObject2[4] = Integer.valueOf(localTransitDetails.getWalkingTimeMinutes());
        arrayOfObject2[5] = BidiUtils.unicodeWrap(localLocation.getName());
      }
      Object[] arrayOfObject1;
      for (String str3 = paramContext.getString(2131362260, arrayOfObject2);; str3 = paramContext.getString(2131362261, arrayOfObject1))
      {
        return Html.fromHtml(str3);
        arrayOfObject1 = new Object[5];
        arrayOfObject1[0] = "<b>";
        arrayOfObject1[1] = BidiUtils.unicodeWrap(str2);
        arrayOfObject1[2] = "</b>";
        arrayOfObject1[3] = str1;
        arrayOfObject1[4] = BidiUtils.unicodeWrap(localLocation.getName());
      }
    }
    if (i == 2) {
      return paramContext.getString(2131362457);
    }
    if (i == 3) {
      return paramContext.getString(2131362458);
    }
    return null;
  }
  
  public String getRouteSummary()
  {
    if (!this.mRoute.hasRouteSummary()) {
      return null;
    }
    return this.mRoute.getRouteSummary();
  }
  
  @Nullable
  public Integer getTotalEtaMinutes()
  {
    if (!this.mRoute.hasTravelTimeWithoutDelayInMinutes()) {
      return null;
    }
    int i;
    if (getTravelMode() == 0)
    {
      i = this.mRoute.getTrafficDelayInMinutes();
      return Integer.valueOf(i + this.mRoute.getTravelTimeWithoutDelayInMinutes());
    }
    long l1 = System.currentTimeMillis() / 1000L;
    if (this.mRoute.hasTransitDetails()) {}
    for (long l2 = this.mRoute.getTransitDetails().getDepartureTimeSeconds() - l1 - 60 * this.mRoute.getTransitDetails().getWalkingTimeMinutes();; l2 = 0L)
    {
      boolean bool = l2 < 0L;
      i = 0;
      if (!bool) {
        break;
      }
      i = (int)Math.ceil(l2 / 60.0D);
      break;
    }
  }
  
  public int getTrafficColor(Context paramContext, int paramInt)
  {
    int i = paramInt;
    int j = getTrafficStatus();
    if (j != -1) {
      switch (j)
      {
      }
    }
    for (;;)
    {
      return paramContext.getResources().getColor(i);
      i = 2131230840;
      continue;
      i = 2131230842;
      continue;
      i = 2131230841;
    }
  }
  
  public String getTrafficColorForHtml(Context paramContext, int paramInt)
  {
    return getColorForHtml(getTrafficColor(paramContext, paramInt));
  }
  
  public int getTrafficStatus()
  {
    if (!this.mRoute.hasTrafficStatus())
    {
      Log.e(TAG, "Expected commute to have traffic status" + this.mRoute);
      return -1;
    }
    return this.mRoute.getTrafficStatus();
  }
  
  public String getTrafficStatusAsString(Context paramContext)
  {
    int i = getTrafficStatus();
    if (i != -1) {
      return getTrafficStatusAsString(paramContext, i);
    }
    return null;
  }
  
  @Nullable
  public String getTransitAlertTitle(Context paramContext)
  {
    if (numTransitAlerts() == 0) {
      return null;
    }
    SparseBooleanArray localSparseBooleanArray = transitAlertTypes();
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = getColorForHtml(paramContext.getResources().getColor(2131230840));
    String str = String.format("<b><font color='%s'>", arrayOfObject1);
    if (localSparseBooleanArray.size() > 1) {
      return paramContext.getString(2131362234, new Object[] { str, "</font></b>" });
    }
    List localList = getTransitDetails().getAlertList();
    int i = localSparseBooleanArray.keyAt(0);
    switch (i)
    {
    default: 
      Log.e(TAG, "Unknown Alert Type:" + i);
      return "";
    case 2: 
      if (localList.size() == 1)
      {
        Object[] arrayOfObject5 = new Object[3];
        arrayOfObject5[0] = BidiUtils.unicodeWrap(((Sidekick.CommuteSummary.TransitDetails.Alert)localList.get(0)).getLineName());
        arrayOfObject5[1] = str;
        arrayOfObject5[2] = "</font></b>";
        return paramContext.getString(2131362228, arrayOfObject5);
      }
      return paramContext.getString(2131362229, new Object[] { str, "</font></b>" });
    case 1: 
      if (localList.size() == 1)
      {
        Object[] arrayOfObject4 = new Object[3];
        arrayOfObject4[0] = BidiUtils.unicodeWrap(((Sidekick.CommuteSummary.TransitDetails.Alert)localList.get(0)).getStationLocation().getName());
        arrayOfObject4[1] = str;
        arrayOfObject4[2] = "</font></b>";
        return paramContext.getString(2131362226, arrayOfObject4);
      }
      return paramContext.getString(2131362227, new Object[] { str, "</font></b>" });
    case 4: 
      if (localList.size() == 1)
      {
        Object[] arrayOfObject3 = new Object[3];
        arrayOfObject3[0] = BidiUtils.unicodeWrap(((Sidekick.CommuteSummary.TransitDetails.Alert)localList.get(0)).getLineName());
        arrayOfObject3[1] = str;
        arrayOfObject3[2] = "</font></b>";
        return paramContext.getString(2131362230, arrayOfObject3);
      }
      return paramContext.getString(2131362231, new Object[] { str, "</font></b>" });
    }
    if (localList.size() == 1)
    {
      Object[] arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = str;
      arrayOfObject2[1] = "</font></b>";
      arrayOfObject2[2] = BidiUtils.unicodeWrap(((Sidekick.CommuteSummary.TransitDetails.Alert)localList.get(0)).getLineName());
      return paramContext.getString(2131362232, arrayOfObject2);
    }
    return paramContext.getString(2131362233, new Object[] { str, "</font></b>" });
  }
  
  @Nullable
  public Sidekick.CommuteSummary.TransitDetails getTransitDetails()
  {
    if (getTravelMode() == 1) {
      return this.mRoute.getTransitDetails();
    }
    return null;
  }
  
  public int getTravelMode()
  {
    if (!this.mRoute.hasTravelMode()) {
      return -1;
    }
    return this.mRoute.getTravelMode();
  }
  
  public boolean hasTransitAlerts()
  {
    return numTransitAlerts() > 0;
  }
  
  public void updateTravelSummary(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, TextView paramTextView)
  {
    CharSequence localCharSequence = getRouteDescriptionWithTraffic(paramContext);
    if (!TextUtils.isEmpty(localCharSequence))
    {
      paramTextView.setText(localCharSequence);
      paramTextView.setVisibility(0);
      return;
    }
    paramTextView.setVisibility(8);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.TravelReport
 * JD-Core Version:    0.7.0.1
 */