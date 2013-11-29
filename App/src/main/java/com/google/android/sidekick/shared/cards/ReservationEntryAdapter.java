package com.google.android.sidekick.shared.cards;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.IntentUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GoogleServiceWebviewClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.android.sidekick.shared.util.MoonshineUtilities;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.android.sidekick.shared.util.TravelReport;
import com.google.android.sidekick.shared.util.ViewPlacePageAction;
import com.google.geo.sidekick.Sidekick.BusinessData;
import com.google.geo.sidekick.Sidekick.CommuteSummary.TransitDetails;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.GmailReference;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.PlaceData;
import javax.annotation.Nullable;

public class ReservationEntryAdapter
  extends AbstractPlaceEntryAdapter
{
  private final IntentUtils mIntentUtils;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final int mRelativeDays;
  
  protected ReservationEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, IntentUtils paramIntentUtils, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper, Clock paramClock, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramFrequentPlaceEntry, paramDirectionsLauncher, paramActivityHelper, paramClock);
    this.mIntentUtils = paramIntentUtils;
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    Time localTime1 = new Time();
    localTime1.setToNow();
    Time localTime2 = new Time();
    localTime2.set(1000L * getFrequentPlaceEntry().getEventTimeSeconds());
    this.mRelativeDays = relativeDays(localTime1, localTime2);
  }
  
  private View createEventView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968672, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(getLocationName());
    CharSequence localCharSequence1 = getLocationAddress();
    if (!TextUtils.isEmpty(localCharSequence1)) {
      ((TextView)localView.findViewById(2131296556)).setText(localCharSequence1);
    }
    CharSequence localCharSequence2 = getContextMessage(paramContext);
    if (!TextUtils.isEmpty(localCharSequence2)) {
      ((TextView)localView.findViewById(2131296325)).setText(localCharSequence2);
    }
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = getFrequentPlaceEntry();
    if ((localFrequentPlaceEntry != null) && (localFrequentPlaceEntry.hasEventImage())) {
      this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView.findViewById(2131296551), localFrequentPlaceEntry.getEventImage());
    }
    return localView;
  }
  
  private CharSequence getReservationString(Context paramContext, int paramInt1, int paramInt2)
  {
    long l = 1000L * getFrequentPlaceEntry().getEventTimeSeconds();
    return paramContext.getString(paramInt1, new Object[] { DateUtils.formatDateRange(paramContext, l, l, paramInt2) });
  }
  
  private boolean isNavigationShowing(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    return (mightShowNavigateButtonFor(getTravelReport(), paramPredictiveCardContainer)) && (shouldShowNavigation(paramContext));
  }
  
  private boolean isSourcedFromGmail()
  {
    return getFrequentPlaceEntry().getFrequentPlace().getSourceType() == 7;
  }
  
  static int relativeDays(Time paramTime1, Time paramTime2)
  {
    int i = Time.getJulianDay(paramTime1.toMillis(false), paramTime1.gmtoff);
    return Time.getJulianDay(paramTime2.toMillis(false), paramTime2.gmtoff) - i;
  }
  
  private boolean reservationIsToday()
  {
    return this.mRelativeDays == 0;
  }
  
  private boolean reservationIsTomorrow()
  {
    return this.mRelativeDays == 1;
  }
  
  @Nullable
  public CharSequence getContextMessage(Context paramContext)
  {
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = getFrequentPlaceEntry();
    switch (getFrequentPlace().getSourceType())
    {
    }
    do
    {
      for (;;)
      {
        return null;
        if (reservationIsToday()) {}
        for (int i = 1;; i = 19) {
          switch (localFrequentPlaceEntry.getEventType())
          {
          default: 
            break;
          case 5: 
            return getReservationString(paramContext, 2131362689, i);
          }
        }
        return getReservationString(paramContext, 2131362690, i);
        switch (localFrequentPlaceEntry.getEventType())
        {
        }
      }
      if (reservationIsToday()) {
        return paramContext.getString(2131362686);
      }
      return getReservationString(paramContext, 2131362685, 18);
      if (reservationIsToday()) {
        return paramContext.getString(2131362687);
      }
    } while (!reservationIsTomorrow());
    return paramContext.getString(2131362688);
  }
  
  @Nullable
  public CharSequence getLocationAddress()
  {
    Sidekick.FrequentPlace localFrequentPlace = getFrequentPlace();
    if ((localFrequentPlace.hasLocation()) && (localFrequentPlace.getLocation().hasAddress())) {
      return localFrequentPlace.getLocation().getAddress();
    }
    return null;
  }
  
  @Nullable
  public CharSequence getLocationName()
  {
    Sidekick.FrequentPlace localFrequentPlace = getFrequentPlace();
    if ((localFrequentPlace.hasPlaceData()) && (localFrequentPlace.getPlaceData().hasDisplayName())) {
      return Html.fromHtml(localFrequentPlace.getPlaceData().getDisplayName());
    }
    if ((localFrequentPlace.hasPlaceData()) && (localFrequentPlace.getPlaceData().hasBusinessData())) {
      return Html.fromHtml(localFrequentPlace.getPlaceData().getBusinessData().getName());
    }
    if ((localFrequentPlace.hasLocation()) && (localFrequentPlace.getLocation().hasName())) {
      return Html.fromHtml(localFrequentPlace.getLocation().getName());
    }
    return null;
  }
  
  public String getLoggingName()
  {
    switch (getFrequentPlaceEntry().getFrequentPlace().getSourceType())
    {
    default: 
      return super.getLoggingName();
    case 7: 
      return "GmailEventReservation";
    case 6: 
      return "GmailRestaurantReservation";
    }
    return "GmailHotelReservation";
  }
  
  @Nullable
  public String getPhotoUrl()
  {
    Sidekick.FrequentPlace localFrequentPlace = getFrequentPlace();
    if ((localFrequentPlace.hasPlaceData()) && (localFrequentPlace.getPlaceData().hasBusinessData()))
    {
      Sidekick.BusinessData localBusinessData = localFrequentPlace.getPlaceData().getBusinessData();
      if (localBusinessData.hasCoverPhoto()) {
        return localBusinessData.getCoverPhoto().getUrl();
      }
    }
    return null;
  }
  
  @Nullable
  public CharSequence getTravelTime(Context paramContext)
  {
    TravelReport localTravelReport = getTravelReport();
    if (localTravelReport != null)
    {
      Integer localInteger = localTravelReport.getTotalEtaMinutes();
      if (localInteger != null)
      {
        if ((localTravelReport.getTravelMode() == 1) && (localTravelReport.getTransitDetails() != null)) {}
        for (String str = localTravelReport.getTransitDetails().getTransitLineName(); str != null; str = localTravelReport.getRouteSummary())
        {
          Object[] arrayOfObject = new Object[3];
          arrayOfObject[0] = localTravelReport.getTrafficColorForHtml(paramContext, 2131230834);
          arrayOfObject[1] = localInteger;
          arrayOfObject[2] = str;
          return Html.fromHtml(paramContext.getString(2131362691, arrayOfObject));
        }
      }
    }
    return null;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (isSourcedFromGmail()) {
      return createEventView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
    }
    return super.getView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    Sidekick.FrequentPlace localFrequentPlace = getFrequentPlace();
    if (isNavigationShowing(paramContext, paramPredictiveCardContainer)) {
      super.launchDetails(paramContext, paramPredictiveCardContainer, paramView);
    }
    while ((!localFrequentPlace.hasPlaceData()) || (!localFrequentPlace.getPlaceData().hasBusinessData()) || (!localFrequentPlace.getPlaceData().getBusinessData().hasCid())) {
      return;
    }
    new ViewPlacePageAction(paramContext, localFrequentPlace.getPlaceData().getBusinessData().getCid(), this.mIntentUtils, getActivityHelper()).run();
  }
  
  protected void updateActionButtons(final Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    super.updateActionButtons(paramActivity, paramPredictiveCardContainer, paramView);
    Sidekick.FrequentPlace localFrequentPlace = getFrequentPlace();
    Button localButton2;
    final long l;
    if ((!isNavigationShowing(paramActivity, paramPredictiveCardContainer)) && (localFrequentPlace.hasPlaceData()) && (localFrequentPlace.getPlaceData().hasBusinessData()) && (localFrequentPlace.getPlaceData().getBusinessData().hasCid()))
    {
      localButton2 = addActionButton(paramActivity, paramView, 2130837676, paramActivity.getString(2131362281), 0);
      l = localFrequentPlace.getPlaceData().getBusinessData().getCid();
      switch (getFrequentPlaceEntry().getEventType())
      {
      }
    }
    for (;;)
    {
      localButton2.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 113)
      {
        protected void onEntryClick(View paramAnonymousView)
        {
          new ViewPlacePageAction(paramActivity, l, ReservationEntryAdapter.this.mIntentUtils, ReservationEntryAdapter.this.getActivityHelper()).run();
        }
      });
      Sidekick.GmailReference localGmailReference = MoonshineUtilities.getEffectiveGmailReference(localFrequentPlace.getGmailReferenceList());
      if (localGmailReference != null)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = BidiUtils.unicodeWrapLtr(localGmailReference.getSenderEmailAddress());
        Button localButton1 = addActionButton(paramActivity, paramView, 2130837645, paramActivity.getString(2131362630, arrayOfObject), 0);
        String str2 = ((TextView)paramView.findViewById(2131296451)).getText().toString();
        localButton1.setOnClickListener(new GoogleServiceWebviewClickListener(paramActivity, localGmailReference.getEmailUrl(), str2, false, this, 112, "mail", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES, null, paramPredictiveCardContainer));
      }
      if (localFrequentPlace.hasModifyReservationUrl())
      {
        final String str1 = localFrequentPlace.getModifyReservationUrl();
        addActionButton(paramActivity, paramView, 2130837706, paramActivity.getString(2131362597), 0).setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 111)
        {
          protected void onEntryClick(View paramAnonymousView)
          {
            ReservationEntryAdapter.this.openUrl(paramActivity, str1);
          }
        });
      }
      return;
      localButton2.setText(2131362676);
      continue;
      localButton2.setText(2131362673);
    }
  }
  
  protected void updateContextMessage(Context paramContext, View paramView)
  {
    CharSequence localCharSequence = getContextMessage(paramContext);
    if (!TextUtils.isEmpty(localCharSequence))
    {
      TextView localTextView = (TextView)paramView.findViewById(2131296325);
      localTextView.setText(localCharSequence);
      localTextView.setVisibility(0);
    }
  }
  
  protected void updateMapOrImage(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (getTravelReport() != null) {
      showMap(paramContext, paramPredictiveCardContainer, paramView);
    }
    String str;
    do
    {
      return;
      str = getPhotoUrl();
    } while (TextUtils.isEmpty(str));
    WebImageView localWebImageView = (WebImageView)paramView.findViewById(2131296333);
    localWebImageView.setImageUrl(str, paramPredictiveCardContainer.getImageLoader());
    localWebImageView.setVisibility(0);
  }
  
  protected void updateTitle(Context paramContext, View paramView)
  {
    CharSequence localCharSequence = getLocationName();
    if (!TextUtils.isEmpty(localCharSequence)) {
      ((TextView)paramView.findViewById(2131296451)).setText(localCharSequence);
    }
  }
  
  protected void updateTravelTime(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    CharSequence localCharSequence = getTravelTime(paramContext);
    if (!TextUtils.isEmpty(localCharSequence))
    {
      TextView localTextView = (TextView)paramView.findViewById(2131296330);
      localTextView.setText(localCharSequence);
      localTextView.setVisibility(0);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.ReservationEntryAdapter
 * JD-Core Version:    0.7.0.1
 */