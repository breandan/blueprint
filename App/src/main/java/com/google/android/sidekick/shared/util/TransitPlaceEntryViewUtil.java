package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.common.base.Strings;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails.Line;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails.Step;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Date;
import javax.annotation.Nullable;

public class TransitPlaceEntryViewUtil
{
  private final DirectionsLauncher mDirectionsLauncher;
  private final Sidekick.Entry mEntry;
  private final Sidekick.FrequentPlaceEntry mFrequentPlaceEntry;
  
  public TransitPlaceEntryViewUtil(Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, @Nullable Sidekick.Entry paramEntry, @Nullable DirectionsLauncher paramDirectionsLauncher)
  {
    this.mFrequentPlaceEntry = paramFrequentPlaceEntry;
    this.mEntry = paramEntry;
    this.mDirectionsLauncher = paramDirectionsLauncher;
  }
  
  private boolean addTransitStepViews(Sidekick.CommuteSummary.TransitDetails paramTransitDetails, LinearLayout paramLinearLayout, Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater)
  {
    WindowManager localWindowManager = (WindowManager)paramContext.getSystemService("window");
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
    int i = LayoutUtils.getCardWidth(paramContext) - 4 * LayoutUtils.getContentPadding(paramContext, localDisplayMetrics.widthPixels) - (int)(30.0F * localDisplayMetrics.density);
    int j = 0;
    int k = paramTransitDetails.getStepCount();
    boolean bool = false;
    if (j < k)
    {
      Sidekick.CommuteSummary.TransitDetails.Step localStep = paramTransitDetails.getStep(j);
      Sidekick.CommuteSummary.TransitDetails.Line localLine;
      View localView1;
      WebImageView localWebImageView1;
      if (localStep.getLineCount() > 0)
      {
        localLine = localStep.getLine(0);
        localView1 = paramLayoutInflater.inflate(2130968887, paramLinearLayout, false);
        localWebImageView1 = (WebImageView)localView1.findViewById(2131297151);
        if (!localLine.hasLineIconUrl()) {
          break label255;
        }
        localWebImageView1.setImageUrl(localLine.getLineIconUrl(), paramPredictiveCardContainer.getImageLoader());
        localWebImageView1.setVisibility(0);
      }
      for (;;)
      {
        if (!localLine.hasLineIconUrl()) {
          addLinesView(paramContext, localStep, paramLayoutInflater, localView1);
        }
        if ((j < -1 + paramTransitDetails.getStepCount()) || (paramTransitDetails.getWalkingTimeToDestMinutes() > 0))
        {
          WebImageView localWebImageView2 = (WebImageView)localView1.findViewById(2131297153);
          localWebImageView2.setImageUrl("http://maps.gstatic.com/tactile/directions/cards/arrow-right-2x.png", paramPredictiveCardContainer.getImageLoader());
          localWebImageView2.setVisibility(0);
        }
        if (measureWidth(paramLinearLayout) + measureWidth(localView1) > i) {
          break label289;
        }
        paramLinearLayout.addView(localView1);
        j++;
        break;
        label255:
        if (localLine.hasMethodIconUrl())
        {
          localWebImageView1.setImageUrl(localLine.getMethodIconUrl(), paramPredictiveCardContainer.getImageLoader());
          localWebImageView1.setVisibility(0);
        }
      }
      label289:
      View localView2 = paramLayoutInflater.inflate(2130968629, paramLinearLayout, false);
      ((TextView)localView2.findViewById(2131296460)).setText("…");
      paramLinearLayout.addView(localView2);
      bool = true;
    }
    return bool;
  }
  
  private String getFormattedTime(Context paramContext, long paramLong)
  {
    return android.text.format.DateFormat.getTimeFormat(paramContext).format(new Date(1000L * paramLong));
  }
  
  private int measureWidth(View paramView)
  {
    paramView.measure(-2, -2);
    return paramView.getMeasuredWidth();
  }
  
  private void setDepartureInfo(Context paramContext, Sidekick.CommuteSummary.TransitDetails paramTransitDetails, View paramView)
  {
    if ((paramTransitDetails.hasStationLocation()) && (paramTransitDetails.getStationLocation().hasName()) && (paramTransitDetails.hasDepartureTimeSeconds()))
    {
      TextView localTextView2 = (TextView)paramView.findViewById(2131297147);
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramTransitDetails.getStationLocation().getName();
      arrayOfObject[1] = getFormattedTime(paramContext, paramTransitDetails.getDepartureTimeSeconds());
      localTextView2.setText(paramContext.getString(2131362268, arrayOfObject));
      localTextView2.setVisibility(0);
    }
    if (paramTransitDetails.getFareCount() > 0)
    {
      TextView localTextView1 = (TextView)paramView.findViewById(2131297148);
      localTextView1.setText(paramTransitDetails.getFare(0));
      localTextView1.setVisibility(0);
    }
  }
  
  private void setTransitAlerts(Context paramContext, Sidekick.CommuteSummary paramCommuteSummary, View paramView, LayoutInflater paramLayoutInflater)
  {
    TravelReport localTravelReport = new TravelReport(paramCommuteSummary);
    Sidekick.CommuteSummary.TransitDetails localTransitDetails = localTravelReport.getTransitDetails();
    if ((localTransitDetails != null) && (localTransitDetails.getAlertCount() > 0)) {
      ((TextView)((LinearLayout)((ViewStub)paramView.findViewById(2131297149)).inflate()).findViewById(2131297137)).setText(Html.fromHtml(localTravelReport.getTransitAlertTitle(paramContext)));
    }
  }
  
  void addLinesView(Context paramContext, Sidekick.CommuteSummary.TransitDetails.Step paramStep, LayoutInflater paramLayoutInflater, View paramView)
  {
    LinearLayout localLinearLayout = (LinearLayout)paramView.findViewById(2131297152);
    double d = 0.33D * LayoutUtils.getCardWidth(paramContext);
    String str = "";
    int i = 0;
    if (i < paramStep.getLineCount())
    {
      Sidekick.CommuteSummary.TransitDetails.Line localLine = paramStep.getLine(i);
      if ((localLine.hasBackgroundColor()) && (localLine.getBackgroundColor() != -1))
      {
        if (!str.isEmpty())
        {
          View localView3 = paramLayoutInflater.inflate(2130968884, localLinearLayout, false);
          TextView localTextView3 = (TextView)localView3.findViewById(2131297143);
          localTextView3.setText(str);
          localTextView3.setVisibility(0);
          if (measureWidth(localTextView3) <= d) {
            localLinearLayout.addView(localView3);
          }
          str = "";
        }
        View localView2 = paramLayoutInflater.inflate(2130968884, localLinearLayout, false);
        TextView localTextView2 = (TextView)localView2.findViewById(2131297143);
        localTextView2.setBackgroundColor(localLine.getBackgroundColor());
        localTextView2.setVisibility(0);
        if ((localLine.getShowName()) && (localLine.hasForegroundColor()))
        {
          localTextView2.setTextColor(localLine.getForegroundColor());
          localTextView2.setText(localLine.getName());
          if (measureWidth(localTextView2) > d) {
            localTextView2.setText("");
          }
        }
        localLinearLayout.addView(localView2);
      }
      for (;;)
      {
        i++;
        break;
        if (!Strings.isNullOrEmpty(localLine.getName()))
        {
          if (!str.isEmpty()) {
            str = str + " / ";
          }
          str = str + localLine.getName();
        }
      }
    }
    if (!str.isEmpty())
    {
      View localView1 = paramLayoutInflater.inflate(2130968884, localLinearLayout, false);
      TextView localTextView1 = (TextView)localView1.findViewById(2131297143);
      localTextView1.setText(str);
      localTextView1.setVisibility(0);
      localLinearLayout.addView(localView1);
    }
  }
  
  void addWalkingManStepView(PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, boolean paramBoolean, LinearLayout paramLinearLayout)
  {
    View localView = paramLayoutInflater.inflate(2130968887, paramLinearLayout, false);
    WebImageView localWebImageView1 = (WebImageView)localView.findViewById(2131297151);
    localWebImageView1.setImageUrl("http://maps.gstatic.com/mapfiles/transit/iw2/8/walk.png", paramPredictiveCardContainer.getImageLoader());
    localWebImageView1.setVisibility(0);
    if (paramBoolean)
    {
      WebImageView localWebImageView2 = (WebImageView)localView.findViewById(2131297153);
      localWebImageView2.setImageUrl("http://maps.gstatic.com/tactile/directions/cards/arrow-right-2x.png", paramPredictiveCardContainer.getImageLoader());
      localWebImageView2.setVisibility(0);
    }
    paramLinearLayout.addView(localView);
  }
  
  String getDurationString(Context paramContext, Sidekick.CommuteSummary.TransitDetails paramTransitDetails)
  {
    return TimeUtilities.getEtaString(paramContext, (int)Math.ceil((paramTransitDetails.getArrivalTimeSeconds() - paramTransitDetails.getDepartureTimeSeconds()) / 60.0D) + paramTransitDetails.getWalkingTimeMinutes(), true);
  }
  
  String getIntervalString(Context paramContext, int paramInt)
  {
    if (paramInt >= 1440) {
      return "";
    }
    if (paramInt >= 60)
    {
      int i = paramInt / 60;
      int j = paramInt % 60;
      if (j > 0)
      {
        Object[] arrayOfObject3 = new Object[2];
        arrayOfObject3[0] = Integer.valueOf(i);
        arrayOfObject3[1] = Integer.valueOf(j);
        return paramContext.getString(2131362267, arrayOfObject3);
      }
      Resources localResources2 = paramContext.getResources();
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(i);
      return localResources2.getQuantityString(2131558429, i, arrayOfObject2);
    }
    Resources localResources1 = paramContext.getResources();
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(paramInt);
    return localResources1.getQuantityString(2131558428, paramInt, arrayOfObject1);
  }
  
  void setRowClickAction(View paramView, PredictiveCardContainer paramPredictiveCardContainer, final Sidekick.CommuteSummary paramCommuteSummary)
  {
    if ((this.mDirectionsLauncher != null) && (this.mFrequentPlaceEntry.hasFrequentPlace()) && (this.mFrequentPlaceEntry.getFrequentPlace().hasLocation())) {
      paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, this.mEntry, 58)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          TransitPlaceEntryViewUtil.this.mDirectionsLauncher.start(TransitPlaceEntryViewUtil.this.mFrequentPlaceEntry.getFrequentPlace().getLocation(), null, MapsLauncher.TravelMode.TRANSIT, MapsLauncher.getPersonalizedRouteToken(paramCommuteSummary));
        }
      });
    }
  }
  
  public boolean shouldShowTransitView()
  {
    return (this.mFrequentPlaceEntry != null) && (this.mFrequentPlaceEntry.getRouteCount() > 0) && (this.mFrequentPlaceEntry.getRoute(0).hasTransitDetails()) && (this.mFrequentPlaceEntry.getRoute(0).getTransitDetails().getStepCount() > 0) && (this.mFrequentPlaceEntry.getRoute(0).getTravelMode() == 1);
  }
  
  public View updateTransitRouteList(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, LayoutInflater paramLayoutInflater, boolean paramBoolean1, boolean paramBoolean2)
  {
    LinearLayout localLinearLayout1 = (LinearLayout)((ViewStub)paramView.findViewById(2131296328)).inflate();
    for (int i = 0; i < this.mFrequentPlaceEntry.getRouteCount(); i++)
    {
      Sidekick.CommuteSummary.TransitDetails localTransitDetails = this.mFrequentPlaceEntry.getRoute(i).getTransitDetails();
      View localView = paramLayoutInflater.inflate(2130968886, null);
      if ((localTransitDetails.hasWalkingTimeMinutes()) && (localTransitDetails.hasDepartureTimeSeconds()) && (localTransitDetails.hasArrivalTimeSeconds()))
      {
        String str1 = getFormattedTime(paramContext, localTransitDetails.getDepartureTimeSeconds() - 60 * localTransitDetails.getWalkingTimeMinutes());
        String str2 = getFormattedTime(paramContext, localTransitDetails.getArrivalTimeSeconds());
        TextView localTextView = (TextView)localView.findViewById(2131297144);
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = str1;
        arrayOfObject[1] = str2;
        arrayOfObject[2] = getDurationString(paramContext, localTransitDetails);
        localTextView.setText(paramContext.getString(2131362266, arrayOfObject));
      }
      if (localTransitDetails.hasPeriodicitySeconds())
      {
        int j = localTransitDetails.getPeriodicitySeconds() / 60;
        ((TextView)localView.findViewById(2131297145)).setText(getIntervalString(paramContext, j));
      }
      LinearLayout localLinearLayout2 = (LinearLayout)localView.findViewById(2131297146);
      if (localTransitDetails.getWalkingTimeMinutes() > 0) {
        addWalkingManStepView(paramPredictiveCardContainer, paramLayoutInflater, true, localLinearLayout2);
      }
      boolean bool = addTransitStepViews(localTransitDetails, localLinearLayout2, paramContext, paramPredictiveCardContainer, paramLayoutInflater);
      if ((localTransitDetails.getWalkingTimeToDestMinutes() > 0) && (!bool)) {
        addWalkingManStepView(paramPredictiveCardContainer, paramLayoutInflater, false, localLinearLayout2);
      }
      if (((paramBoolean2) && (i == 0)) || (this.mFrequentPlaceEntry.getRouteCount() > 1)) {
        setDepartureInfo(paramContext, localTransitDetails, localView);
      }
      if (paramBoolean1) {
        setTransitAlerts(paramContext, this.mFrequentPlaceEntry.getRoute(i), localView, paramLayoutInflater);
      }
      if (i == -1 + this.mFrequentPlaceEntry.getRouteCount()) {
        localView.findViewById(2131297150).setVisibility(8);
      }
      setRowClickAction(localView, paramPredictiveCardContainer, this.mFrequentPlaceEntry.getRoute(i));
      localLinearLayout1.addView(localView);
    }
    if (this.mFrequentPlaceEntry.getRouteCount() == 1) {
      localLinearLayout1.setPadding(localLinearLayout1.getPaddingLeft(), localLinearLayout1.getPaddingTop(), localLinearLayout1.getPaddingRight(), 0);
    }
    return localLinearLayout1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.TransitPlaceEntryViewUtil
 * JD-Core Version:    0.7.0.1
 */