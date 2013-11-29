package com.google.android.sidekick.shared.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.MapsLauncher;
import com.google.android.sidekick.shared.util.MapsLauncher.TravelMode;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.android.sidekick.shared.util.TransitPlaceEntryViewUtil;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.android.sidekick.shared.util.ViewInMapsAction;
import com.google.common.base.Strings;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.BusinessData;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails.Alert;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.PlaceData;
import com.google.geo.sidekick.Sidekick.TrafficIncident;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public abstract class AbstractPlaceEntryAdapter
  extends BaseEntryAdapter
{
  private TravelReport mAlternateTravelReport;
  private final Clock mClock;
  protected final DirectionsLauncher mDirectionsLauncher;
  private Sidekick.FrequentPlaceEntry mFrequentPlaceEntry;
  private final Object mLock = new Object();
  private Sidekick.FrequentPlace mPlace;
  private TransitPlaceEntryViewUtil mTransitViewUtil;
  private TravelReport mTravelReport;
  
  protected AbstractPlaceEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mClock = paramClock;
    this.mDirectionsLauncher = paramDirectionsLauncher;
    replaceEntry(paramEntry);
  }
  
  private void addTransitAlertIcons(View paramView, Sidekick.CommuteSummary.TransitDetails.Alert paramAlert)
  {
    View localView = paramView.findViewById(2131297136);
    if (paramAlert.hasTransitLineBackgroundColor())
    {
      localView.setBackgroundColor(paramAlert.getTransitLineBackgroundColor());
      localView.setVisibility(0);
      return;
    }
    localView.setVisibility(8);
  }
  
  @Nullable
  private static String getEtaStringFromTravelReport(Context paramContext, TravelReport paramTravelReport, boolean paramBoolean)
  {
    if (paramTravelReport != null)
    {
      Integer localInteger = paramTravelReport.getTotalEtaMinutes();
      if (localInteger != null) {
        return TimeUtilities.getEtaString(paramContext, localInteger.intValue(), paramBoolean);
      }
    }
    return null;
  }
  
  private int getIncidentsIconId(Sidekick.TrafficIncident paramTrafficIncident)
  {
    if (paramTrafficIncident.hasType()) {}
    switch (paramTrafficIncident.getType())
    {
    default: 
      return 2130837934;
    case 1: 
      return 2130837935;
    case 2: 
      return 2130837932;
    }
    return 2130837933;
  }
  
  private CharSequence getNavigationButtonText(Context paramContext, TravelReport paramTravelReport, int paramInt1, int paramInt2)
  {
    Sidekick.CommuteSummary localCommuteSummary = paramTravelReport.getRoute();
    if ((localCommuteSummary.hasRouteSummary()) && (!TextUtils.isEmpty(localCommuteSummary.getRouteSummary())))
    {
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = getEtaStringFromTravelReport(paramContext, paramTravelReport, true);
      arrayOfObject2[1] = localCommuteSummary.getRouteSummary();
      return paramContext.getString(paramInt1, arrayOfObject2);
    }
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = getEtaStringFromTravelReport(paramContext, paramTravelReport, true);
    return paramContext.getString(paramInt2, arrayOfObject1);
  }
  
  public static void populateSampleCard(View paramView)
  {
    Context localContext = paramView.getContext();
    TextView localTextView1 = (TextView)paramView.findViewById(2131296451);
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = Integer.valueOf(localContext.getResources().getColor(2131230841));
    arrayOfObject1[1] = localContext.getString(2131362497);
    arrayOfObject1[2] = localContext.getString(2131362214);
    localTextView1.setText(Html.fromHtml(localContext.getString(2131362225, arrayOfObject1)));
    TextView localTextView2 = (TextView)paramView.findViewById(2131296330);
    localTextView2.setVisibility(0);
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = localContext.getString(2131362283);
    arrayOfObject2[1] = localContext.getString(2131362498);
    localTextView2.setText(Html.fromHtml(localContext.getString(2131362259, arrayOfObject2)));
    ((ImageView)paramView.findViewById(2131296333)).setVisibility(0);
  }
  
  private View populateTransitView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, LayoutInflater paramLayoutInflater)
  {
    synchronized (this.mLock)
    {
      updateTitle(paramContext, paramView);
      updateNearbyBusiness(paramContext, paramView);
      updateContextMessage(paramContext, paramView);
      if (shouldShowTransitAlertInTitle())
      {
        updateTransitAlerts(paramContext, paramView, paramLayoutInflater);
        updateTimeSummary(paramContext, paramView);
      }
      TransitPlaceEntryViewUtil localTransitPlaceEntryViewUtil = this.mTransitViewUtil;
      boolean bool1 = shouldShowTransitAlertInTitle();
      boolean bool2 = false;
      if (!bool1) {
        bool2 = true;
      }
      localTransitPlaceEntryViewUtil.updateTransitRouteList(paramContext, paramPredictiveCardContainer, paramView, paramLayoutInflater, bool2, false);
      if (this.mFrequentPlaceEntry.getRouteCount() == 1)
      {
        updateTravelTime(paramContext, paramPredictiveCardContainer, paramView);
        if (((this.mFrequentPlaceEntry.getRoute(0).hasTransitDetails()) && (this.mFrequentPlaceEntry.getRoute(0).getTransitDetails().getWalkingPathCount() > 0)) || (placeConfirmationRequested())) {
          updateMapOrImage(paramContext, paramPredictiveCardContainer, paramView);
        }
      }
      return paramView;
    }
  }
  
  private View populateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, LayoutInflater paramLayoutInflater)
  {
    synchronized (this.mLock)
    {
      updateTitle(paramContext, paramView);
      updateNearbyBusiness(paramContext, paramView);
      updateContextMessage(paramContext, paramView);
      updateTransitAlerts(paramContext, paramView, paramLayoutInflater);
      updateTimeSummary(paramContext, paramView);
      updateTravelTime(paramContext, paramPredictiveCardContainer, paramView);
      updateMapOrImage(paramContext, paramPredictiveCardContainer, paramView);
      updateIncidents(paramView);
      return paramView;
    }
  }
  
  private void setAndMakeVisible(View paramView, int paramInt, String paramString, boolean paramBoolean)
  {
    if (paramBoolean) {
      CardTextUtil.setTextView(paramView, paramInt, paramString);
    }
  }
  
  protected Button addActionButton(Context paramContext, View paramView, int paramInt1, CharSequence paramCharSequence, int paramInt2)
  {
    Button localButton = (Button)LayoutInflater.from(paramContext).inflate(2130968624, (ViewGroup)paramView, false);
    localButton.setText(paramCharSequence);
    if (paramInt2 != 0) {
      localButton.setId(paramInt2);
    }
    if (paramInt1 != 0) {
      LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton, paramInt1, 0, 0, 0);
    }
    int i = ((ViewGroup)paramView).getChildCount();
    ((ViewGroup)paramView).addView(localButton, i - 1);
    return localButton;
  }
  
  public TravelReport getAlternateTravelReport()
  {
    synchronized (this.mLock)
    {
      TravelReport localTravelReport = this.mAlternateTravelReport;
      return localTravelReport;
    }
  }
  
  public CharSequence getFormattedFullTitle(Context paramContext)
  {
    if (this.mTravelReport != null)
    {
      if (shouldShowTransitAlertInTitle()) {
        return Html.fromHtml(this.mTravelReport.getTransitAlertTitle(paramContext));
      }
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = this.mTravelReport.getTrafficColorForHtml(paramContext, 2131230835);
      arrayOfObject[1] = getShortEtaString(paramContext);
      arrayOfObject[2] = getTitle(paramContext);
      return Html.fromHtml(paramContext.getString(2131362225, arrayOfObject));
    }
    return Html.fromHtml(getTitle(paramContext));
  }
  
  public final Sidekick.FrequentPlace getFrequentPlace()
  {
    synchronized (this.mLock)
    {
      Sidekick.FrequentPlace localFrequentPlace = this.mPlace;
      return localFrequentPlace;
    }
  }
  
  public final Sidekick.FrequentPlaceEntry getFrequentPlaceEntry()
  {
    synchronized (this.mLock)
    {
      Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = this.mFrequentPlaceEntry;
      return localFrequentPlaceEntry;
    }
  }
  
  protected final Sidekick.Location getLocation()
  {
    synchronized (this.mLock)
    {
      if ((this.mPlace != null) && (this.mPlace.hasLocation()))
      {
        Sidekick.Location localLocation = this.mPlace.getLocation();
        return localLocation;
      }
      return null;
    }
  }
  
  public String getLongEtaString(Context paramContext)
  {
    return getEtaStringFromTravelReport(paramContext, this.mTravelReport, false);
  }
  
  public CharSequence getRouteDescription(Context paramContext)
  {
    if (this.mTravelReport == null) {
      return null;
    }
    return this.mTravelReport.getRouteDescriptionWithTraffic(paramContext);
  }
  
  @Nullable
  public String getShortEtaString(Context paramContext)
  {
    return getEtaStringFromTravelReport(paramContext, this.mTravelReport, true);
  }
  
  protected String getTitle(Context paramContext)
  {
    synchronized (this.mLock)
    {
      String str = BidiUtils.unicodeWrap(PlaceUtils.getPlaceName(paramContext, this.mPlace));
      return str;
    }
  }
  
  @Nullable
  public int getTrafficColor(Context paramContext, int paramInt)
  {
    if (this.mTravelReport != null) {
      return this.mTravelReport.getTrafficColor(paramContext, paramInt);
    }
    return -1;
  }
  
  public TravelReport getTravelReport()
  {
    synchronized (this.mLock)
    {
      TravelReport localTravelReport = this.mTravelReport;
      return localTravelReport;
    }
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968594, paramViewGroup, false);
    if (shouldShowTransitView()) {
      return populateTransitView(paramContext, paramPredictiveCardContainer, localView, paramLayoutInflater);
    }
    return populateView(paramContext, paramPredictiveCardContainer, localView, paramLayoutInflater);
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296312);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = getFrequentPlaceEntry();
    if ((localFrequentPlaceEntry.getRouteCount() > 0) && (localFrequentPlaceEntry.getRoute(0).hasTravelMode()))
    {
      new ViewInMapsAction(paramContext, getActivityHelper(), getLocation(), localFrequentPlaceEntry.getRoute(0)).run();
      return;
    }
    new ViewInMapsAction(paramContext, getActivityHelper(), getLocation()).run();
  }
  
  protected boolean mightShowNavigateButtonFor(TravelReport paramTravelReport, PredictiveCardContainer paramPredictiveCardContainer)
  {
    NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
    return (paramTravelReport != null) && (paramTravelReport.getRoute().hasShowNavigation()) && (paramTravelReport.getRoute().getShowNavigation()) && (localNavigationContext != null) && (localNavigationContext.shouldShowNavigation(this.mPlace.getLocation())) && (!shouldShowTransitView());
  }
  
  public boolean placeConfirmationRequested()
  {
    Iterator localIterator = getEntry().getEntryActionList().iterator();
    while (localIterator.hasNext()) {
      if (((Sidekick.Action)localIterator.next()).getType() == 16) {
        return true;
      }
    }
    return false;
  }
  
  public void registerActions(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    synchronized (this.mLock)
    {
      updateActionButtons(paramActivity, paramPredictiveCardContainer, paramView);
      return;
    }
  }
  
  public void replaceEntry(Sidekick.Entry paramEntry)
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        super.replaceEntry(paramEntry);
        this.mFrequentPlaceEntry = PlaceUtils.getFrequentPlaceEntry(paramEntry);
        this.mPlace = this.mFrequentPlaceEntry.getFrequentPlace();
        this.mTransitViewUtil = new TransitPlaceEntryViewUtil(this.mFrequentPlaceEntry, paramEntry, this.mDirectionsLauncher);
        if (this.mFrequentPlaceEntry.getRouteCount() > 0)
        {
          this.mTravelReport = new TravelReport(this.mFrequentPlaceEntry.getRoute(0));
          if (this.mFrequentPlaceEntry.getRouteCount() > 1)
          {
            this.mAlternateTravelReport = new TravelReport(this.mFrequentPlaceEntry.getRoute(1));
            return;
          }
          this.mAlternateTravelReport = null;
        }
      }
      this.mTravelReport = null;
      this.mAlternateTravelReport = null;
    }
  }
  
  protected boolean shouldShowNavigation(Context paramContext)
  {
    return !shouldShowTransitView();
  }
  
  protected boolean shouldShowRoute()
  {
    return true;
  }
  
  protected final boolean shouldShowTransitAlertInTitle()
  {
    int j;
    for (int i = 1;; j = 0) {
      synchronized (this.mLock)
      {
        if ((this.mTravelReport != null) && (this.mTravelReport.hasTransitAlerts()) && (this.mFrequentPlaceEntry.getRouteCount() == i)) {
          return i;
        }
      }
    }
  }
  
  protected final boolean shouldShowTransitView()
  {
    synchronized (this.mLock)
    {
      boolean bool = this.mTransitViewUtil.shouldShowTransitView();
      return bool;
    }
  }
  
  protected void showMap(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    ImageView localImageView = (ImageView)paramView.findViewById(2131296333);
    if ((this.mTravelReport == null) || (this.mTravelReport.getTotalEtaMinutes() == null))
    {
      localImageView.setVisibility(8);
      return;
    }
    boolean bool = shouldShowRoute();
    localImageView.setVisibility(0);
    Sidekick.Location localLocation = LocationUtilities.androidLocationToSidekickLocation(paramPredictiveCardContainer.getCardRenderingContext().getRefreshLocation());
    paramPredictiveCardContainer.getStaticMapLoader().loadMap(localLocation, this.mFrequentPlaceEntry, bool, localImageView);
  }
  
  protected void updateActionButtons(final Activity paramActivity, final PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (mightShowNavigateButtonFor(this.mTravelReport, paramPredictiveCardContainer)) {
      configureRouteButtons(paramView, this.mTravelReport.getRoute(), paramActivity, this.mDirectionsLauncher, paramPredictiveCardContainer, getLocation(), shouldShowNavigation(paramActivity));
    }
    if (mightShowNavigateButtonFor(this.mAlternateTravelReport, paramPredictiveCardContainer))
    {
      Button localButton2 = addActionButton(paramActivity, paramView, 2130837639, getNavigationButtonText(paramActivity, this.mAlternateTravelReport, 2131362288, 2131362287), 0);
      localButton2.setId(2131296285);
      localButton2.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 58)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
          Sidekick.CommuteSummary localCommuteSummary = AbstractPlaceEntryAdapter.this.mAlternateTravelReport.getRoute();
          MapsLauncher.TravelMode localTravelMode = AbstractPlaceEntryAdapter.this.mDirectionsLauncher.getTravelMode(localNavigationContext, localCommuteSummary);
          if (localCommuteSummary.getPathfinderWaypointCount() > 0) {}
          for (List localList = localCommuteSummary.getPathfinderWaypointList();; localList = null)
          {
            AbstractPlaceEntryAdapter.this.mDirectionsLauncher.start(AbstractPlaceEntryAdapter.this.getLocation(), localList, localTravelMode, MapsLauncher.getPersonalizedRouteToken(localCommuteSummary));
            return;
          }
        }
      });
      if (shouldShowNavigation(paramActivity)) {
        localButton2.setVisibility(0);
      }
    }
    if ((this.mFrequentPlaceEntry.getFrequentPlace().hasPlaceData()) && (this.mFrequentPlaceEntry.getFrequentPlace().getPlaceData().hasBusinessData()) && (this.mFrequentPlaceEntry.getFrequentPlace().getPlaceData().getBusinessData().hasPhoneNumber()))
    {
      Button localButton1 = addActionButton(paramActivity, paramView, 2130837636, paramActivity.getString(2131362280), 0);
      localButton1.setVisibility(0);
      localButton1.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 59)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          Intent localIntent = new Intent("android.intent.action.CALL");
          String str = AbstractPlaceEntryAdapter.this.mFrequentPlaceEntry.getFrequentPlace().getPlaceData().getBusinessData().getPhoneNumber();
          localIntent.setData(Uri.parse("tel:" + str));
          paramActivity.startActivity(localIntent);
        }
      });
    }
  }
  
  protected void updateContextMessage(Context paramContext, View paramView)
  {
    Sidekick.CommuteSummary.TransitDetails localTransitDetails;
    if (this.mTravelReport != null)
    {
      localTransitDetails = this.mTravelReport.getTransitDetails();
      if (localTransitDetails != null) {
        break label20;
      }
    }
    label20:
    String str;
    do
    {
      List localList;
      do
      {
        return;
        localList = localTransitDetails.getAlertList();
      } while ((!shouldShowTransitAlertInTitle()) || (localList.size() != 1));
      long l = this.mClock.currentTimeMillis() / 1000L;
      str = TravelReport.singleAlertTimeContextString(paramContext, (Sidekick.CommuteSummary.TransitDetails.Alert)localList.get(0), l);
    } while (Strings.isNullOrEmpty(str));
    TextView localTextView = (TextView)paramView.findViewById(2131296325);
    localTextView.setText(Html.fromHtml(str));
    localTextView.setVisibility(0);
  }
  
  protected void updateIncidents(View paramView)
  {
    Sidekick.CommuteSummary localCommuteSummary;
    CharSequence localCharSequence;
    SpannableStringBuilder localSpannableStringBuilder;
    Object localObject;
    if (this.mFrequentPlaceEntry.getRouteCount() > 0)
    {
      localCommuteSummary = this.mFrequentPlaceEntry.getRoute(0);
      if (localCommuteSummary.getTrafficIncidentCount() > 0)
      {
        ((ViewStub)paramView.findViewById(2131296331)).inflate();
        TextView localTextView = (TextView)paramView.findViewById(2131297120);
        Sidekick.TrafficIncident localTrafficIncident = localCommuteSummary.getTrafficIncident(0);
        LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localTextView, getIncidentsIconId(localTrafficIncident), 0, 0, 0);
        if (!localTrafficIncident.hasDescription()) {
          break label252;
        }
        localCharSequence = CardTextUtil.color(localTrafficIncident.getDescription(), paramView.getResources().getColor(2131230834));
        boolean bool = localTrafficIncident.hasAttribution();
        localSpannableStringBuilder = null;
        if (bool)
        {
          String str = localTrafficIncident.getAttribution();
          localSpannableStringBuilder = new SpannableStringBuilder(str);
          localSpannableStringBuilder.setSpan(new ForegroundColorSpan(paramView.getResources().getColor(2131230834)), 0, str.length(), 17);
          localSpannableStringBuilder.setSpan(new StyleSpan(2), 0, str.length(), 17);
        }
        if ((localCharSequence == null) || (localSpannableStringBuilder == null)) {
          break label258;
        }
        localObject = TextUtils.concat(new CharSequence[] { localCharSequence, " ", localSpannableStringBuilder });
      }
    }
    for (;;)
    {
      if (localObject != null) {
        CardTextUtil.setTextView(paramView, 2131297120, (CharSequence)localObject);
      }
      setAndMakeVisible(paramView, 2131297121, localCommuteSummary.getMultipleTrafficIncidents(), localCommuteSummary.hasMultipleTrafficIncidents());
      return;
      label252:
      localCharSequence = null;
      break;
      label258:
      if (localCharSequence == null) {
        localObject = localSpannableStringBuilder;
      } else {
        localObject = localCharSequence;
      }
    }
  }
  
  protected void updateMapOrImage(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    showMap(paramContext, paramPredictiveCardContainer, paramView);
  }
  
  protected void updateNearbyBusiness(Context paramContext, View paramView)
  {
    String str;
    if ((this.mPlace != null) && (this.mPlace.hasPlaceData()) && (this.mPlace.getPlaceData().hasBusinessData()) && (this.mPlace.getPlaceData().getBusinessData().hasName()))
    {
      str = this.mPlace.getPlaceData().getBusinessData().getName();
      if (!TextUtils.isEmpty(str)) {
        break label68;
      }
    }
    label68:
    while (getTitle(paramContext).equalsIgnoreCase(str)) {
      return;
    }
    TextView localTextView = (TextView)paramView.findViewById(2131296324);
    localTextView.setText(Html.fromHtml(str));
    localTextView.setVisibility(0);
  }
  
  protected void updateTimeSummary(Context paramContext, View paramView)
  {
    TextView localTextView;
    String str;
    if ((this.mTravelReport != null) && (this.mTravelReport.hasTransitAlerts()))
    {
      localTextView = (TextView)paramView.findViewById(2131296326);
      str = getLongEtaString(paramContext);
      if (str != null) {
        break label74;
      }
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = getTitle(paramContext);
      localTextView.setText(paramContext.getString(2131362249, arrayOfObject2));
    }
    for (;;)
    {
      localTextView.setVisibility(0);
      return;
      label74:
      Object[] arrayOfObject1 = new Object[3];
      arrayOfObject1[0] = TravelReport.getColorForHtml(paramContext.getResources().getColor(2131230840));
      arrayOfObject1[1] = str;
      arrayOfObject1[2] = getTitle(paramContext);
      localTextView.setText(Html.fromHtml(paramContext.getString(2131362225, arrayOfObject1)));
    }
  }
  
  protected void updateTitle(Context paramContext, View paramView)
  {
    TextView localTextView = (TextView)paramView.findViewById(2131296451);
    localTextView.setText(getFormattedFullTitle(paramContext));
    localTextView.setMaxLines(4);
  }
  
  protected void updateTransitAlerts(Context paramContext, View paramView, LayoutInflater paramLayoutInflater)
  {
    if (this.mTravelReport != null)
    {
      Sidekick.CommuteSummary.TransitDetails localTransitDetails = this.mTravelReport.getTransitDetails();
      if ((localTransitDetails != null) && (localTransitDetails.getAlertCount() > 1))
      {
        LinearLayout localLinearLayout = (LinearLayout)((ViewStub)paramView.findViewById(2131296327)).inflate();
        Iterator localIterator = localTransitDetails.getAlertList().iterator();
        while (localIterator.hasNext())
        {
          Sidekick.CommuteSummary.TransitDetails.Alert localAlert = (Sidekick.CommuteSummary.TransitDetails.Alert)localIterator.next();
          View localView = paramLayoutInflater.inflate(2130968881, localLinearLayout, false);
          addTransitAlertIcons(localView, localAlert);
          ((TextView)localView.findViewById(2131297137)).setText(Html.fromHtml(this.mTravelReport.getAlertString(paramContext, this.mClock, localAlert)));
          localLinearLayout.addView(localView);
        }
      }
    }
  }
  
  protected void updateTravelTime(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mTravelReport != null)
    {
      TextView localTextView = (TextView)paramView.findViewById(2131296330);
      this.mTravelReport.updateTravelSummary(paramContext, paramPredictiveCardContainer, localTextView);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.AbstractPlaceEntryAdapter
 * JD-Core Version:    0.7.0.1
 */