package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GeostoreFeatureId;
import com.google.geo.sidekick.Sidekick.TransitStationEntry;
import com.google.geo.sidekick.Sidekick.TransitStationEntry.Line;
import com.google.geo.sidekick.Sidekick.TransitStationEntry.Line.DepartureGroup;
import com.google.geo.sidekick.Sidekick.TransitStationEntry.Line.DepartureGroup.Departure;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class TransitEntryAdapter
  extends BaseEntryAdapter
{
  private static final Uri MAPS_PLACE_URI = Uri.parse("http://maps.google.com/maps/place");
  private final Sidekick.TransitStationEntry mTransitStation;
  
  public TransitEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mTransitStation = paramEntry.getTransitStationEntry();
  }
  
  private String getLineName(Sidekick.TransitStationEntry.Line paramLine, Sidekick.TransitStationEntry.Line.DepartureGroup paramDepartureGroup)
  {
    if ((paramLine.getName().isEmpty()) && (paramDepartureGroup.hasLineLongName())) {
      return paramDepartureGroup.getLineLongName();
    }
    return paramLine.getName();
  }
  
  private List<DepartureTime> getSortedDepartureTimes()
  {
    ArrayList localArrayList = Lists.newArrayList();
    long l = this.mTransitStation.getResponseEpochSeconds();
    Iterator localIterator1 = this.mTransitStation.getLineList().iterator();
    if (localIterator1.hasNext())
    {
      Sidekick.TransitStationEntry.Line localLine = (Sidekick.TransitStationEntry.Line)localIterator1.next();
      Integer localInteger1;
      label66:
      Integer localInteger2;
      if (localLine.hasColor())
      {
        localInteger1 = Integer.valueOf(localLine.getColor());
        if (!localLine.hasTextColor()) {
          break label233;
        }
        localInteger2 = Integer.valueOf(localLine.getTextColor());
        label84:
        if (!localLine.hasLineIconUrl()) {
          break label239;
        }
      }
      label233:
      label239:
      for (String str1 = localLine.getLineIconUrl();; str1 = null)
      {
        Iterator localIterator2 = localLine.getDepartureGroupList().iterator();
        while (localIterator2.hasNext())
        {
          Sidekick.TransitStationEntry.Line.DepartureGroup localDepartureGroup = (Sidekick.TransitStationEntry.Line.DepartureGroup)localIterator2.next();
          String str2 = localDepartureGroup.getHeadsign();
          Iterator localIterator3 = localDepartureGroup.getDepartureList().iterator();
          while (localIterator3.hasNext())
          {
            Sidekick.TransitStationEntry.Line.DepartureGroup.Departure localDeparture = (Sidekick.TransitStationEntry.Line.DepartureGroup.Departure)localIterator3.next();
            if (localDeparture.hasRelativeDepartureTimeSeconds()) {
              localArrayList.add(new DepartureTime(1000L * (l + localDeparture.getRelativeDepartureTimeSeconds()), getLineName(localLine, localDepartureGroup), str2, localInteger1, localInteger2, str1));
            }
          }
        }
        localInteger1 = null;
        break label66;
        localInteger2 = null;
        break label84;
      }
    }
    Collections.sort(localArrayList);
    return localArrayList;
  }
  
  private int getVehicleType()
  {
    int i = -1;
    Iterator localIterator = this.mTransitStation.getLineList().iterator();
    while (localIterator.hasNext())
    {
      int j = ((Sidekick.TransitStationEntry.Line)localIterator.next()).getVehicleType();
      if (i == -1) {
        i = j;
      } else if (i != j) {
        i = -1;
      }
    }
    return i;
  }
  
  private CharSequence getVehicleTypeString(Context paramContext)
  {
    switch (getVehicleType())
    {
    default: 
      return null;
    case 1: 
      return paramContext.getString(2131362291);
    case 3: 
      return paramContext.getString(2131362292);
    case 2: 
      return paramContext.getString(2131362293);
    }
    return paramContext.getString(2131362290);
  }
  
  private boolean lineNamesAreShort(List<DepartureTime> paramList)
  {
    for (int i = 0; (i < 5) && (i < paramList.size()); i++) {
      if (((DepartureTime)paramList.get(i)).mLine.length() > 2) {
        return false;
      }
    }
    return true;
  }
  
  public View getView(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    final View localView1 = paramLayoutInflater.inflate(2130968883, paramViewGroup, false);
    ((TextView)localView1.findViewById(2131296451)).setText(this.mTransitStation.getStationName());
    CharSequence localCharSequence = getVehicleTypeString(paramContext);
    if (localCharSequence != null)
    {
      TextView localTextView3 = (TextView)localView1.findViewById(2131297141);
      localTextView3.setText(localCharSequence);
      localTextView3.setVisibility(0);
    }
    List localList = getSortedDepartureTimes();
    boolean bool = lineNamesAreShort(localList);
    int i = 0;
    TableLayout localTableLayout = (TableLayout)localView1.findViewById(2131297142);
    for (int j = 0; (j < 5) && (j < localList.size()); j++)
    {
      DepartureTime localDepartureTime = (DepartureTime)localList.get(j);
      View localView2 = paramLayoutInflater.inflate(2130968882, null);
      if (localDepartureTime.mIconUrl != null)
      {
        WebImageView localWebImageView = (WebImageView)localView2.findViewById(2131297138);
        localWebImageView.setImageUrl(localDepartureTime.mIconUrl, paramPredictiveCardContainer.getImageLoader());
        localWebImageView.setVisibility(0);
        i = 1;
      }
      if ((localDepartureTime.mHeadSignColor != null) && (localDepartureTime.mHeadSignColor.intValue() != -1))
      {
        ((TextView)localView2.findViewById(2131297139)).setBackgroundColor(localDepartureTime.mHeadSignColor.intValue());
        ((TextView)localView2.findViewById(2131297139)).setVisibility(0);
      }
      TextView localTextView1 = (TextView)localView2.findViewById(2131297106);
      localTextView1.setText(localDepartureTime.mLine);
      if (bool) {
        localTextView1.setLayoutParams(new TableRow.LayoutParams(-2, -2, 0.04F));
      }
      ((TextView)localView2.findViewById(2131297140)).setText(localDepartureTime.getDepartureTimeString(paramContext));
      localTableLayout.addView(localView2);
      View localView3 = paramLayoutInflater.inflate(2130968882, null);
      TextView localTextView2 = (TextView)localView3.findViewById(2131297106);
      localTextView2.setText(localDepartureTime.mHeadSign);
      localTextView2.setTextColor(paramContext.getResources().getColor(2131230836));
      localTableLayout.addView(localView3);
    }
    if (i == 0) {
      localTableLayout.setColumnCollapsed(0, true);
    }
    ((Button)localView1.findViewById(2131296618)).setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 104)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        TransitEntryAdapter.this.launchDetails(paramContext, paramPredictiveCardContainer, localView1);
      }
    });
    return localView1;
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296450);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (!this.mTransitStation.hasStationId())
    {
      Log.e("TransitEntryAdapter", "Missing station identifier");
      return;
    }
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Long.valueOf(this.mTransitStation.getStationId().getCellId());
    arrayOfObject[1] = Long.valueOf(this.mTransitStation.getStationId().getFprint());
    String str = String.format(localLocale, "0x%1$x:0x%2$x", arrayOfObject);
    openUrlWithMessage(paramContext, MAPS_PLACE_URI.buildUpon().appendQueryParameter("ftid", str).build(), 2131363303);
  }
  
  private static class DepartureTime
    implements Comparable<DepartureTime>
  {
    public final long mDepartureTimeMillis;
    public final String mHeadSign;
    public final Integer mHeadSignColor;
    public final Integer mHeadSignTextColor;
    public String mIconUrl;
    public final String mLine;
    
    public DepartureTime(long paramLong, String paramString1, String paramString2, @Nullable Integer paramInteger1, @Nullable Integer paramInteger2, String paramString3)
    {
      this.mDepartureTimeMillis = paramLong;
      this.mLine = paramString1;
      this.mHeadSign = paramString2;
      this.mHeadSignColor = paramInteger1;
      this.mHeadSignTextColor = paramInteger2;
      this.mIconUrl = paramString3;
    }
    
    public int compareTo(DepartureTime paramDepartureTime)
    {
      return (int)(this.mDepartureTimeMillis - paramDepartureTime.mDepartureTimeMillis);
    }
    
    public String getDepartureTimeString(Context paramContext)
    {
      return android.text.format.DateFormat.getTimeFormat(paramContext).format(new Date(this.mDepartureTimeMillis));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.TransitEntryAdapter
 * JD-Core Version:    0.7.0.1
 */