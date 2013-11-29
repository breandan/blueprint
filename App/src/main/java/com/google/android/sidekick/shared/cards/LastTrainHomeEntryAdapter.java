package com.google.android.sidekick.shared.cards;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Clock.TimeTickListener;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.sidekick.shared.util.TransitPlaceEntryViewUtil;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.android.sidekick.shared.util.ViewInMapsAction;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Locale;
import javax.annotation.Nullable;

public class LastTrainHomeEntryAdapter
  extends BaseEntryAdapter
{
  private final Clock mClock;
  private final DirectionsLauncher mDirectionsLauncher;
  @Nullable
  private final Sidekick.FrequentPlaceEntry mLastTrainHomeEntry;
  @Nullable
  private final TransitPlaceEntryViewUtil mTransitViewUtil;
  @Nullable
  private final TravelReport mTravelReport;
  
  protected LastTrainHomeEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, Clock paramClock, DirectionsLauncher paramDirectionsLauncher)
  {
    super(paramEntry, paramActivityHelper);
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mClock = paramClock;
    if (paramEntry.hasLastTrainHomeEntry())
    {
      this.mLastTrainHomeEntry = paramEntry.getLastTrainHomeEntry();
      if (this.mLastTrainHomeEntry.getRouteCount() > 0) {}
      for (this.mTravelReport = new TravelReport(this.mLastTrainHomeEntry.getRoute(0));; this.mTravelReport = null)
      {
        this.mTransitViewUtil = new TransitPlaceEntryViewUtil(this.mLastTrainHomeEntry, paramEntry, this.mDirectionsLauncher);
        return;
      }
    }
    this.mTravelReport = null;
    this.mLastTrainHomeEntry = null;
    this.mTransitViewUtil = null;
  }
  
  private boolean afterTimeToLeave()
  {
    long l = getDepartureTimeSeconds().longValue() - 60 * getMinutesBeforeDepartureToLeave();
    return this.mClock.currentTimeMillis() >= 1000L * l;
  }
  
  private int getMinutesBeforeDepartureToLeave()
  {
    if (this.mTravelReport != null)
    {
      Sidekick.CommuteSummary.TransitDetails localTransitDetails = this.mTravelReport.getTransitDetails();
      if (localTransitDetails != null) {
        return 15 + localTransitDetails.getWalkingTimeMinutes();
      }
    }
    return 15;
  }
  
  private void populateDirectionsButton(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mTravelReport != null) && (this.mTravelReport.getRoute() != null))
    {
      Sidekick.Location localLocation = this.mLastTrainHomeEntry.getFrequentPlace().getLocation();
      NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
      if ((this.mTravelReport.getRoute().hasShowNavigation()) && (this.mTravelReport.getRoute().getShowNavigation()) && (localNavigationContext != null) && (localNavigationContext.shouldShowNavigation(localLocation))) {
        configureRouteButtons(paramView, this.mTravelReport.getRoute(), paramActivity, this.mDirectionsLauncher, paramPredictiveCardContainer, localLocation, true);
      }
    }
  }
  
  private void populateScheduleNotificationButton(final PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    Sidekick.Action localAction = findAction(getEntry(), 31);
    Button localButton = (Button)paramView.findViewById(2131296743);
    if ((localAction != null) && (localButton != null) && (!afterTimeToLeave()))
    {
      localButton.setText(Html.fromHtml(localAction.getDisplayMessage()));
      localButton.setVisibility(0);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 67)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          paramPredictiveCardContainer.logAction(LastTrainHomeEntryAdapter.this.getEntry(), 31, null);
          paramPredictiveCardContainer.invalidateEntries();
          paramAnonymousView.setVisibility(8);
        }
      });
    }
  }
  
  private void populateView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, final View paramView, LayoutInflater paramLayoutInflater)
  {
    ((TextView)paramView.findViewById(2131296451)).setText(getFullTitle(paramContext));
    updateHeaderMessages(paramView);
    if ((this.mTransitViewUtil != null) && (this.mTransitViewUtil.shouldShowTransitView())) {
      this.mTransitViewUtil.updateTransitRouteList(paramContext, paramPredictiveCardContainer, paramView, paramLayoutInflater, true, false);
    }
    updateTravelSummary(paramContext, paramPredictiveCardContainer, paramView);
    ImageView localImageView = (ImageView)paramView.findViewById(2131296742);
    localImageView.setVisibility(0);
    Sidekick.Location localLocation = LocationUtilities.androidLocationToSidekickLocation(paramPredictiveCardContainer.getCardRenderingContext().getRefreshLocation());
    paramPredictiveCardContainer.getStaticMapLoader().loadMap(localLocation, this.mLastTrainHomeEntry, true, localImageView);
    paramView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
    {
      public void onTimeTick()
      {
        ((TextView)paramView.findViewById(2131296451)).setText(LastTrainHomeEntryAdapter.this.getFullTitle(paramContext));
        LastTrainHomeEntryAdapter.this.updateHeaderMessages(paramView);
      }
    }
    {
      public void onViewAttachedToWindow(View paramAnonymousView)
      {
        LastTrainHomeEntryAdapter.this.mClock.registerTimeTickListener(this.val$timeTickListener);
      }
      
      public void onViewDetachedFromWindow(View paramAnonymousView)
      {
        LastTrainHomeEntryAdapter.this.mClock.unregisterTimeTickListener(this.val$timeTickListener);
      }
    });
  }
  
  public static int selectMessageByDestination(Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, int paramInt1, int paramInt2)
  {
    if ((paramFrequentPlaceEntry != null) && (paramFrequentPlaceEntry.getFrequentPlace().getSourceType() == 8)) {
      return paramInt2;
    }
    return paramInt1;
  }
  
  private boolean shouldRevealLeaveNowMessage()
  {
    long l = getDepartureTimeSeconds().longValue();
    return (afterTimeToLeave()) && (this.mClock.currentTimeMillis() <= 1000L * l);
  }
  
  private boolean shouldRevealNotificationMessage()
  {
    Sidekick.Action localAction = ProtoUtils.findAction(getEntry(), 31, new int[0]);
    boolean bool = false;
    if (localAction == null) {
      bool = true;
    }
    return bool;
  }
  
  private void updateHeaderMessages(View paramView)
  {
    View localView1 = paramView.findViewById(2131296740);
    View localView2 = paramView.findViewById(2131296741);
    if ((shouldRevealLeaveNowMessage()) && (localView1 != null))
    {
      localView1.setVisibility(0);
      localView2.setVisibility(8);
    }
    while ((!shouldRevealNotificationMessage()) || (localView2 == null)) {
      return;
    }
    localView2.setVisibility(0);
    localView1.setVisibility(8);
  }
  
  private void updateTravelSummary(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mTravelReport != null)
    {
      TextView localTextView = (TextView)paramView.findViewById(2131296330);
      this.mTravelReport.updateTravelSummary(paramContext, paramPredictiveCardContainer, localTextView);
    }
  }
  
  public String getColorForHtml(int paramInt)
  {
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(0xFFFFFF & paramInt);
    return String.format(localLocale, "#%1$h", arrayOfObject);
  }
  
  public int getDepartureColor(Context paramContext)
  {
    int i = 2131230842;
    Long localLong = getDepartureTimeSeconds();
    int j;
    int k;
    if (localLong != null)
    {
      long l = this.mClock.currentTimeMillis() / 1000L;
      j = (int)Math.floor((localLong.longValue() - l) / 60.0D);
      k = getMinutesBeforeDepartureToLeave();
      if (j >= k) {
        break label72;
      }
      i = 2131230840;
    }
    for (;;)
    {
      return paramContext.getResources().getColor(i);
      label72:
      if (j < k + 10) {
        i = 2131230841;
      }
    }
  }
  
  @Nullable
  public Long getDepartureTimeSeconds()
  {
    if ((this.mTravelReport != null) && (this.mTravelReport.getTransitDetails() != null)) {
      return Long.valueOf(this.mTravelReport.getTransitDetails().getDepartureTimeSeconds());
    }
    return null;
  }
  
  public CharSequence getFullTitle(Context paramContext)
  {
    if (this.mTravelReport != null)
    {
      Sidekick.CommuteSummary.TransitDetails localTransitDetails = this.mTravelReport.getTransitDetails();
      if (localTransitDetails != null)
      {
        long l = this.mClock.currentTimeMillis() / 1000L;
        int i = (int)Math.floor((localTransitDetails.getDepartureTimeSeconds() - l) / 60.0D);
        String str1 = getColorForHtml(getDepartureColor(paramContext));
        String str2 = "<font color='" + str1 + "'><b>";
        if (i < 0) {
          return Html.fromHtml(paramContext.getString(selectMessageByDestination(2131362250, 2131362251), new Object[] { str2, "</b></font>" }));
        }
        if (i == 0) {
          return Html.fromHtml(paramContext.getString(selectMessageByDestination(2131362252, 2131362253), new Object[] { str2, "</b></font>" }));
        }
        Resources localResources = paramContext.getResources();
        int j = selectMessageByDestination(2131558420, 2131558421);
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = Integer.valueOf(i);
        arrayOfObject[1] = str2;
        arrayOfObject[2] = "</b></font>";
        return Html.fromHtml(localResources.getQuantityString(j, i, arrayOfObject));
      }
    }
    return "";
  }
  
  public final Sidekick.Location getLocation()
  {
    if ((this.mTravelReport != null) && (this.mTravelReport.getTransitDetails() != null)) {
      return this.mTravelReport.getTransitDetails().getStationLocation();
    }
    return null;
  }
  
  public String getLoggingName()
  {
    return "LastTrainHome";
  }
  
  @Nullable
  public Sidekick.CommuteSummary.TransitDetails getTransitDetails()
  {
    if (this.mTravelReport == null) {
      return null;
    }
    return this.mTravelReport.getTransitDetails();
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968733, paramViewGroup, false);
    populateView(paramContext, paramPredictiveCardContainer, localView, paramLayoutInflater);
    return localView;
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296312);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mTravelReport != null) && (this.mTravelReport.getTravelMode() != -1))
    {
      new ViewInMapsAction(paramContext, getActivityHelper(), getLocation(), this.mTravelReport.getRoute()).run();
      return;
    }
    new ViewInMapsAction(paramContext, getActivityHelper(), getLocation()).run();
  }
  
  public void registerActions(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    populateDirectionsButton(paramActivity, paramPredictiveCardContainer, paramView);
    populateScheduleNotificationButton(paramPredictiveCardContainer, paramView);
  }
  
  public final int selectMessageByDestination(int paramInt1, int paramInt2)
  {
    return selectMessageByDestination(this.mLastTrainHomeEntry, paramInt1, paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.LastTrainHomeEntryAdapter
 * JD-Core Version:    0.7.0.1
 */