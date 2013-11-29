package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.format.DateUtils;
import android.widget.RemoteViews;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.cards.LastTrainHomeEntryAdapter;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Locale;

public class LastTrainHomeRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<LastTrainHomeEntryAdapter>
{
  public LastTrainHomeRemoteViewsAdapter(LastTrainHomeEntryAdapter paramLastTrainHomeEntryAdapter)
  {
    super(paramLastTrainHomeEntryAdapter);
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    localRemoteViews.setTextViewText(2131297260, paramContext.getString(((LastTrainHomeEntryAdapter)getEntryCardViewAdapter()).selectMessageByDestination(2131362256, 2131362257)));
    localRemoteViews.setTextViewText(2131297261, getNarrowWidgetFooter(paramContext));
    localRemoteViews.setTextViewTextSize(2131297260, 0, paramContext.getResources().getDimension(2131689800));
    localRemoteViews.setTextViewTextSize(2131297261, 0, paramContext.getResources().getDimension(2131689803));
    localRemoteViews.setTextColor(2131297261, ((LastTrainHomeEntryAdapter)getEntryCardViewAdapter()).getDepartureColor(paramContext));
    return localRemoteViews;
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    localRemoteViews.setTextViewText(2131297260, getWidgetHeader(paramContext));
    localRemoteViews.setTextViewText(2131297261, getWidgetFooter(paramContext));
    return localRemoteViews;
  }
  
  public CharSequence getNarrowWidgetFooter(Context paramContext)
  {
    Long localLong = ((LastTrainHomeEntryAdapter)getEntryCardViewAdapter()).getDepartureTimeSeconds();
    if (localLong != null) {
      return DateUtils.formatDateTime(paramContext, 1000L * localLong.longValue(), 1);
    }
    return "";
  }
  
  public CharSequence getWidgetFooter(Context paramContext)
  {
    Sidekick.CommuteSummary.TransitDetails localTransitDetails = ((LastTrainHomeEntryAdapter)getEntryCardViewAdapter()).getTransitDetails();
    if (localTransitDetails != null)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = BidiUtils.unicodeWrap(localTransitDetails.getStationLocation().getName());
      arrayOfObject[1] = TimeUtilities.getEtaString(paramContext, localTransitDetails.getWalkingTimeMinutes(), true);
      return paramContext.getString(2131362262, arrayOfObject);
    }
    return "";
  }
  
  public CharSequence getWidgetHeader(Context paramContext)
  {
    Long localLong = ((LastTrainHomeEntryAdapter)getEntryCardViewAdapter()).getDepartureTimeSeconds();
    if (localLong != null)
    {
      String str = DateUtils.formatDateTime(paramContext, 1000L * localLong.longValue(), 1);
      int i = ((LastTrainHomeEntryAdapter)getEntryCardViewAdapter()).getDepartureColor(paramContext);
      int j = ((LastTrainHomeEntryAdapter)getEntryCardViewAdapter()).selectMessageByDestination(2131362254, 2131362255);
      Object[] arrayOfObject1 = new Object[3];
      arrayOfObject1[0] = str;
      Locale localLocale = Locale.US;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(0xFFFFFF & i);
      arrayOfObject1[1] = String.format(localLocale, "<font color='#%1$h'><b>", arrayOfObject2);
      arrayOfObject1[2] = "</b></font>";
      return Html.fromHtml(paramContext.getString(j, arrayOfObject1));
    }
    return "";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.LastTrainHomeRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */