package com.google.android.sidekick.shared.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.apps.sidekick.calendar.Calendar.EventData;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.CalendarDataContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CalendarDataUtil;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.MapsLauncher;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.sidekick.shared.util.TimeToLeaveUtil;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.android.sidekick.shared.util.TransitPlaceEntryViewUtil;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.android.sidekick.shared.util.ViewInMapsAction;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import java.util.Locale;
import javax.annotation.Nullable;

public class CalendarEntryAdapter
  extends BaseEntryAdapter
{
  private Calendar.CalendarData mCalendarData;
  private final Sidekick.CalendarEntry mCalendarEntry;
  private final DirectionsLauncher mDirectionsLauncher;
  private Sidekick.FrequentPlaceEntry mFrequentPlaceEntry;
  private Sidekick.Location mLocation;
  private final TimeToLeaveUtil mTimeToLeaveUtil;
  private TransitPlaceEntryViewUtil mTransitViewUtil;
  
  public CalendarEntryAdapter(Sidekick.Entry paramEntry, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mCalendarEntry = paramEntry.getCalendarEntry();
    this.mDirectionsLauncher = paramDirectionsLauncher;
    if (paramEntry.hasTimeToLeaveDetails()) {}
    for (TimeToLeaveUtil localTimeToLeaveUtil = new TimeToLeaveUtil(paramEntry, this.mCalendarEntry.getRoute(), paramDirectionsLauncher, 2131362423, paramClock);; localTimeToLeaveUtil = null)
    {
      this.mTimeToLeaveUtil = localTimeToLeaveUtil;
      return;
    }
  }
  
  private void addEmailButton(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mCalendarData.getEventData().getNumberOfAttendees() < 2)
    {
      if (this.mTimeToLeaveUtil != null) {
        paramView.findViewById(2131297119).setVisibility(0);
      }
      return;
    }
    View localView = paramView.findViewById(2131296334);
    if ((localView != null) && (localView.getVisibility() == 0)) {
      paramView.findViewById(2131296902).setVisibility(0);
    }
    Button localButton = (Button)paramView.findViewById(2131296411);
    localButton.setVisibility(0);
    localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 47)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        Intent localIntent = CalendarDataUtil.createEmailAttendeesIntent(CalendarEntryAdapter.this.mCalendarData.getEventData().getEventId());
        paramContext.sendBroadcast(localIntent);
      }
    });
  }
  
  private Calendar.CalendarData createExampleCalendarData(Context paramContext)
  {
    Calendar.EventData localEventData = new Calendar.EventData().setTitle(paramContext.getString(2131362513)).setWhereField(paramContext.getString(2131362514)).setStartTimeSeconds(1331754600L);
    return new Calendar.CalendarData().setEventData(localEventData);
  }
  
  private View createNextAppointmentCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968758, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(getTitle());
    ((TextView)localView.findViewById(2131296814)).setText(getFormattedStartTime(paramContext));
    CharSequence localCharSequence = getWhereField();
    if (!TextUtils.isEmpty(localCharSequence))
    {
      TextView localTextView = (TextView)localView.findViewById(2131296815);
      localTextView.setText(localCharSequence);
      localTextView.setVisibility(0);
      linkifyTextView(localTextView);
    }
    if (!getEntry().getIsExample()) {
      addEmailButton(paramContext, paramPredictiveCardContainer, localView);
    }
    return localView;
  }
  
  private View createTimeToLeaveCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968860, paramViewGroup, false);
    if (this.mTimeToLeaveUtil != null) {
      this.mTimeToLeaveUtil.setupTimeToLeave(paramContext, localView, paramPredictiveCardContainer, paramLayoutInflater, this.mLocation, this.mTransitViewUtil);
    }
    Calendar.CalendarData localCalendarData = this.mCalendarData;
    if (showSample()) {
      localCalendarData = createExampleCalendarData(paramContext);
    }
    CharSequence localCharSequence = formatTime(paramContext, localCalendarData.getEventData().getStartTimeSeconds());
    TextView localTextView1 = (TextView)localView.findViewById(2131296451);
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = BidiUtils.unicodeWrap(localCalendarData.getEventData().getTitle());
    arrayOfObject[1] = localCharSequence;
    localTextView1.setText(paramContext.getString(2131362402, arrayOfObject));
    TextView localTextView2 = (TextView)localView.findViewById(2131296815);
    localTextView2.setText(localCalendarData.getEventData().getWhereField());
    linkifyTextView(localTextView2);
    if (this.mTimeToLeaveUtil == null) {
      setupLegacyCommute(paramContext, paramPredictiveCardContainer, paramLayoutInflater, localView);
    }
    if (!showSample()) {
      addEmailButton(paramContext, paramPredictiveCardContainer, localView);
    }
    return localView;
  }
  
  private CharSequence formatTime(Context paramContext, long paramLong)
  {
    return TimeUtilities.formatDisplayTime(paramContext, 1000L * paramLong, 0);
  }
  
  private void linkifyTextView(TextView paramTextView)
  {
    Linkify.addLinks(paramTextView, 15);
  }
  
  private void setupLegacyCommute(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, View paramView)
  {
    View localView = paramView.findViewById(2131297118);
    Sidekick.CommuteSummary localCommuteSummary = this.mCalendarEntry.getRoute();
    if (localCommuteSummary != null)
    {
      TravelReport localTravelReport = new TravelReport(localCommuteSummary);
      localTravelReport.updateTravelSummary(paramContext, paramPredictiveCardContainer, (TextView)localView.findViewById(2131296330));
    }
    if ((this.mTransitViewUtil != null) && (this.mTransitViewUtil.shouldShowTransitView())) {
      this.mTransitViewUtil.updateTransitRouteList(paramContext, paramPredictiveCardContainer, localView, paramLayoutInflater, true, false);
    }
    ImageView localImageView = (ImageView)localView.findViewById(2131296742);
    localImageView.setVisibility(0);
    if (showSample())
    {
      paramPredictiveCardContainer.getStaticMapLoader().loadSampleMap(localImageView);
      NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
      if ((localCommuteSummary == null) || (localNavigationContext == null) || (!localNavigationContext.shouldShowNavigation(this.mLocation))) {
        break label208;
      }
      configureRouteButtons(localView, localCommuteSummary, paramContext, this.mDirectionsLauncher, paramPredictiveCardContainer, this.mLocation, true);
    }
    label208:
    while (!showSample())
    {
      return;
      Sidekick.Location localLocation = LocationUtilities.androidLocationToSidekickLocation(paramPredictiveCardContainer.getCardRenderingContext().getRefreshLocation());
      paramPredictiveCardContainer.getStaticMapLoader().loadMap(localLocation, this.mFrequentPlaceEntry, true, localImageView);
      break;
    }
    localView.findViewById(2131296334).setVisibility(0);
  }
  
  private boolean showSample()
  {
    return getEntry().getIsExample();
  }
  
  public CharSequence getFormattedStartTime(Context paramContext)
  {
    return formatTime(paramContext, this.mCalendarData.getEventData().getStartTimeSeconds());
  }
  
  public String getLoggingName()
  {
    if (this.mCalendarEntry.hasRoute()) {
      return "TimeToLeaveCalendar";
    }
    return "NextAppointment";
  }
  
  public CharSequence getTitle()
  {
    return this.mCalendarData.getEventData().getTitle();
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (showSample()) {
      setCalendarProviderData(createExampleCalendarData(paramContext));
    }
    while (showSample())
    {
      return createTimeToLeaveCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
      setCalendarProviderData(CalendarDataContext.fromCardContainer(paramPredictiveCardContainer).getCalendarData(this.mCalendarEntry.getHash()));
    }
    if (this.mCalendarEntry.hasRoute()) {
      return createTimeToLeaveCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
    }
    return createNextAppointmentCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296312);
  }
  
  @Nullable
  public CharSequence getWhereField()
  {
    if (this.mCalendarData.getEventData().hasWhereField()) {
      return this.mCalendarData.getEventData().getWhereField();
    }
    return null;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mCalendarEntry.hasRoute()) {
      new ViewInMapsAction(paramContext, getActivityHelper(), this.mLocation, this.mCalendarEntry.getRoute()).run();
    }
    while (this.mCalendarData == null) {
      return;
    }
    Locale localLocale = Locale.US;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(this.mCalendarData.getEventData().getEventId());
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(String.format(localLocale, "content://com.android.calendar/events/%1$s", arrayOfObject)));
    Calendar.EventData localEventData = this.mCalendarData.getEventData();
    localIntent.putExtra("beginTime", 1000L * localEventData.getStartTimeSeconds());
    localIntent.putExtra("endTime", 1000L * localEventData.getEndTimeSeconds());
    getActivityHelper().safeStartActivityWithMessage(paramContext, localIntent, 2131363305);
  }
  
  public void onDismiss(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    super.onDismiss(paramContext, paramPredictiveCardContainer);
    paramPredictiveCardContainer.markCalendarEntryDismissed(this.mCalendarData.getEventData().getProviderId());
  }
  
  public void registerActions(final Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    super.registerActions(paramActivity, paramPredictiveCardContainer, paramView);
    if ((this.mLocation != null) && (this.mLocation.hasLat()) && (this.mLocation.hasLng()) && (this.mTimeToLeaveUtil == null))
    {
      Button localButton = (Button)paramView.findViewById(2131296816);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 48)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          MapsLauncher.start(paramActivity, CalendarEntryAdapter.this.getActivityHelper(), CalendarEntryAdapter.this.mLocation);
        }
      });
    }
  }
  
  public void setCalendarProviderData(Calendar.CalendarData paramCalendarData)
  {
    this.mCalendarData = ((Calendar.CalendarData)Preconditions.checkNotNull(paramCalendarData));
    this.mLocation = CalendarDataUtil.getCalendarLocation(this.mCalendarEntry, this.mCalendarData);
    Sidekick.FrequentPlace localFrequentPlace = new Sidekick.FrequentPlace();
    if (this.mLocation != null) {
      localFrequentPlace.setLocation(this.mLocation);
    }
    this.mFrequentPlaceEntry = new Sidekick.FrequentPlaceEntry().setFrequentPlace(localFrequentPlace);
    if (this.mCalendarEntry.hasRoute())
    {
      this.mFrequentPlaceEntry.addRoute(this.mCalendarEntry.getRoute());
      this.mTransitViewUtil = new TransitPlaceEntryViewUtil(this.mFrequentPlaceEntry, getEntry(), this.mDirectionsLauncher);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.CalendarEntryAdapter
 * JD-Core Version:    0.7.0.1
 */