package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.renderingcontext.NavigationContext;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GoogleServiceWebviewClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.android.sidekick.shared.util.MoonshineEventTicketUtils;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EventPerformer;
import com.google.geo.sidekick.Sidekick.EventTicketSeatInfo;
import com.google.geo.sidekick.Sidekick.GmailReference;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.MoonshineEventTicketEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class SportEventTicketEntryAdapter
  extends BaseEntryAdapter
{
  static final int TIME_DATE_FLAGS = 524307;
  private final Clock mClock;
  private final DirectionsLauncher mDirectionsLauncher;
  private final Sidekick.MoonshineEventTicketEntry mSportEventEntry;
  
  SportEventTicketEntryAdapter(Sidekick.Entry paramEntry, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mSportEventEntry = paramEntry.getMoonshineEventTicketEntry();
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mClock = paramClock;
  }
  
  @Nullable
  private String getTitle(Context paramContext)
  {
    if (this.mSportEventEntry.getPerformersCount() != 2) {
      return this.mSportEventEntry.getTitle();
    }
    String str1 = this.mSportEventEntry.getPerformers(0).getImageUrl();
    String str2 = this.mSportEventEntry.getPerformers(1).getImageUrl();
    String str3 = this.mSportEventEntry.getPerformers(0).getName();
    String str4 = this.mSportEventEntry.getPerformers(1).getName();
    if ((TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)))
    {
      if ((TextUtils.isEmpty(str3)) || (TextUtils.isEmpty(str4))) {
        return this.mSportEventEntry.getTitle();
      }
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = BidiUtils.unicodeWrap(str3);
      arrayOfObject[1] = paramContext.getString(2131362589);
      arrayOfObject[2] = BidiUtils.unicodeWrap(str4);
      return String.format("%s %s %s", arrayOfObject);
    }
    return null;
  }
  
  private void maybeAddBookingAgentInfo(View paramView, Context paramContext)
  {
    if (this.mSportEventEntry.hasBookingAgent())
    {
      TextView localTextView = (TextView)paramView.findViewById(2131297032);
      localTextView.setVisibility(0);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = BidiUtils.unicodeWrap(this.mSportEventEntry.getBookingAgent());
      localTextView.setText(paramContext.getString(2131362588, arrayOfObject));
    }
  }
  
  private void maybeAddDoorOpenTimeInfo(View paramView, Context paramContext)
  {
    String str = MoonshineEventTicketUtils.getDoorOpenTimeString(paramContext, this.mSportEventEntry);
    if (str != null)
    {
      TextView localTextView = (TextView)paramView.findViewById(2131296584);
      localTextView.setVisibility(0);
      localTextView.setText(str);
    }
  }
  
  private void maybeAddGmailButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mSportEventEntry.hasGmailReference()) && (this.mSportEventEntry.getGmailReference().hasEmailUrl()))
    {
      String str = this.mSportEventEntry.getGmailReference().getEmailUrl();
      Button localButton = (Button)paramView.findViewById(2131296376);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new GoogleServiceWebviewClickListener(paramContext, str, this.mSportEventEntry.getTitle(), false, this, 94, "mail", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES, null, paramPredictiveCardContainer));
    }
  }
  
  private void maybeAddModifyReservationButton(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mSportEventEntry.hasManageReservationUrl()) && (!this.mSportEventEntry.getManageReservationUrl().isEmpty()))
    {
      final String str = this.mSportEventEntry.getManageReservationUrl();
      Button localButton = (Button)paramView.findViewById(2131296448);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 93)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          SportEventTicketEntryAdapter.this.openUrl(paramContext, str);
        }
      });
    }
  }
  
  private void maybeAddNavigateButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (showNavigation(paramPredictiveCardContainer)) {
      configureRouteButtons(paramView, this.mSportEventEntry.getRoute(), paramContext, this.mDirectionsLauncher, paramPredictiveCardContainer, this.mSportEventEntry.getVenue(), true);
    }
  }
  
  private void maybeAddTicketTypeInfo(PredictiveCardContainer paramPredictiveCardContainer, View paramView, Context paramContext)
  {
    if (!this.mSportEventEntry.hasTicketType()) {}
    do
    {
      return;
      String str = this.mSportEventEntry.getTicketType();
      ((TextView)paramView.findViewById(2131297031)).setText(paramContext.getString(2131362587, new Object[] { str }));
      paramView.findViewById(2131297030).setVisibility(0);
    } while (!this.mSportEventEntry.hasBookingAgentLogoUrl());
    ((WebImageView)paramView.findViewById(2131296574)).setImageUrl(this.mSportEventEntry.getBookingAgentLogoUrl(), paramPredictiveCardContainer.getImageLoader());
  }
  
  private void maybeAddTimeToLeaveBanner(Context paramContext, View paramView, PredictiveCardContainer paramPredictiveCardContainer)
  {
    long l = this.mClock.currentTimeMillis();
    if (this.mSportEventEntry.hasStartTimeMs()) {}
    for (Long localLong1 = Long.valueOf(this.mSportEventEntry.getStartTimeMs());; localLong1 = null)
    {
      boolean bool = this.mSportEventEntry.hasDepartureTimeMs();
      Long localLong2 = null;
      if (bool) {
        localLong2 = Long.valueOf(this.mSportEventEntry.getDepartureTimeMs());
      }
      if (MoonshineEventTicketUtils.shouldShowTtlBanner(l, localLong1, localLong2, showNavigation(paramPredictiveCardContainer))) {
        break;
      }
      return;
    }
    MoonshineEventTicketUtils.addTtlBanner(paramView, l - this.mSportEventEntry.getDepartureTimeMs(), paramContext, this.mSportEventEntry.getDepartureTimeMs());
  }
  
  private void maybeAddVenueAndEventDateTimeInfo(View paramView, Context paramContext)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(2);
    if ((this.mSportEventEntry.hasVenue()) && (this.mSportEventEntry.getVenue().hasName())) {
      localArrayList.add(this.mSportEventEntry.getVenue().getName());
    }
    if (this.mSportEventEntry.hasStartTimeMs()) {
      localArrayList.add(DateUtils.formatDateTime(paramContext, this.mSportEventEntry.getStartTimeMs(), 524307));
    }
    CardTextUtil.setHyphenatedTextView(paramView, 2131297033, localArrayList);
  }
  
  private void maybeAddVenueInfo(View paramView, Context paramContext)
  {
    if ((this.mSportEventEntry.hasVenue()) && (this.mSportEventEntry.getVenue().hasName()))
    {
      TextView localTextView = (TextView)paramView.findViewById(2131297029);
      localTextView.setVisibility(0);
      localTextView.setText(this.mSportEventEntry.getVenue().getName());
    }
  }
  
  private void populateCardTitle(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    String str = getTitle(paramContext);
    TextView localTextView = (TextView)paramView.findViewById(2131296451);
    ViewGroup localViewGroup = (ViewGroup)paramView.findViewById(2131296450);
    if (TextUtils.isEmpty(str))
    {
      localViewGroup.setLayoutParams(new TableRow.LayoutParams(-2, -2));
      setContestantNameAndImage(paramContext, paramPredictiveCardContainer, paramView);
      return;
    }
    TableRow.LayoutParams localLayoutParams = new TableRow.LayoutParams(-1, -2, 1.0F);
    localTextView.setText(Html.fromHtml(str));
    localViewGroup.setLayoutParams(localLayoutParams);
  }
  
  private void populateReservedTicketInfoTable(Sidekick.EventTicketSeatInfo paramEventTicketSeatInfo, View paramView, Context paramContext, LayoutInflater paramLayoutInflater)
  {
    TableLayout localTableLayout = (TableLayout)paramView.findViewById(2131297016);
    localTableLayout.setVisibility(0);
    if (paramEventTicketSeatInfo.hasSeat()) {
      ((TextView)localTableLayout.findViewById(2131297021)).setText(paramEventTicketSeatInfo.getSeat());
    }
    if (paramEventTicketSeatInfo.hasSeatRow()) {
      ((TextView)localTableLayout.findViewById(2131297022)).setText(paramEventTicketSeatInfo.getSeatRow());
    }
    if (paramEventTicketSeatInfo.hasSeatSection()) {
      ((TextView)localTableLayout.findViewById(2131297023)).setText(paramEventTicketSeatInfo.getSeatSection());
    }
    if ((this.mSportEventEntry.hasNumberOfTickets()) && (this.mSportEventEntry.getNumberOfTickets() > 0))
    {
      TextView localTextView = (TextView)localTableLayout.findViewById(2131297024);
      localTextView.setText(Integer.toString(this.mSportEventEntry.getNumberOfTickets()));
      localTextView.setVisibility(0);
      ((TextView)localTableLayout.findViewById(2131297020)).setVisibility(0);
    }
  }
  
  private void populateShowtimeText(View paramView, Context paramContext)
  {
    if (this.mSportEventEntry.hasStartTimeMs())
    {
      String str = paramContext.getString(2131362599, new Object[] { DateUtils.formatDateTime(paramContext, this.mSportEventEntry.getStartTimeMs(), 1) });
      ((TextView)paramView.findViewById(2131296562)).setText(str);
    }
  }
  
  private void populateTicketInfo(View paramView, Context paramContext, LayoutInflater paramLayoutInflater)
  {
    if (this.mSportEventEntry.getSeatInformationCount() > 0)
    {
      Sidekick.EventTicketSeatInfo localEventTicketSeatInfo = this.mSportEventEntry.getSeatInformation(0);
      if (localEventTicketSeatInfo.hasUnderName())
      {
        TextView localTextView = (TextView)paramView.findViewById(2131296569);
        localTextView.setText(localEventTicketSeatInfo.getUnderName());
        localTextView.setVisibility(0);
        ((TextView)paramView.findViewById(2131297026)).setVisibility(0);
      }
      if ((localEventTicketSeatInfo.hasSeat()) || (localEventTicketSeatInfo.hasSeatRow())) {
        populateReservedTicketInfoTable(localEventTicketSeatInfo, paramView, paramContext, paramLayoutInflater);
      }
    }
  }
  
  private void setContestantNameAndImage(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mSportEventEntry.getPerformersCount() != 2) {}
    String str4;
    do
    {
      return;
      ((LinearLayout)paramView.findViewById(2131297034)).setVisibility(0);
      ((LinearLayout)paramView.findViewById(2131297038)).setVisibility(0);
      String str1 = this.mSportEventEntry.getPerformers(0).getName();
      TextView localTextView1 = (TextView)paramView.findViewById(2131297035);
      if ((localTextView1 != null) && (!str1.isEmpty())) {
        localTextView1.setText(str1);
      }
      String str2 = this.mSportEventEntry.getPerformers(0).getImageUrl();
      if (!str2.isEmpty()) {
        ((WebImageView)paramView.findViewById(2131297036)).setImageUrl(str2, paramPredictiveCardContainer.getImageLoader());
      }
      ((TextView)paramView.findViewById(2131297037)).setVisibility(0);
      String str3 = this.mSportEventEntry.getPerformers(1).getName();
      TextView localTextView2 = (TextView)paramView.findViewById(2131297039);
      if ((localTextView2 != null) && (!str3.isEmpty())) {
        localTextView2.setText(str3);
      }
      str4 = this.mSportEventEntry.getPerformers(1).getImageUrl();
    } while (str4.isEmpty());
    ((WebImageView)paramView.findViewById(2131297040)).setImageUrl(str4, paramPredictiveCardContainer.getImageLoader());
  }
  
  private View showBarcodeCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968837, paramViewGroup, false);
    populateCardTitle(paramContext, paramPredictiveCardContainer, localView);
    populateShowtimeText(localView, paramContext);
    if ((this.mSportEventEntry.hasBarcode()) && (this.mSportEventEntry.getBarcode().hasUrl())) {
      MoonshineEventTicketUtils.addBarcode(paramPredictiveCardContainer, localView, this.mSportEventEntry.getBarcode().getUrl());
    }
    populateTicketInfo(localView, paramContext, paramLayoutInflater);
    if (this.mSportEventEntry.hasConfirmationNumber())
    {
      TextView localTextView = (TextView)localView.findViewById(2131297028);
      localTextView.setText(this.mSportEventEntry.getConfirmationNumber());
      localTextView.setVisibility(0);
      ((TextView)localView.findViewById(2131297027)).setVisibility(0);
    }
    maybeAddVenueInfo(localView, paramContext);
    maybeAddDoorOpenTimeInfo(localView, paramContext);
    maybeAddTicketTypeInfo(paramPredictiveCardContainer, localView, paramContext);
    return localView;
  }
  
  private View showConfirmationCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968838, paramViewGroup, false);
    populateCardTitle(paramContext, paramPredictiveCardContainer, localView);
    maybeAddBookingAgentInfo(localView, paramContext);
    maybeAddVenueAndEventDateTimeInfo(localView, paramContext);
    return localView;
  }
  
  private boolean showNavigation(PredictiveCardContainer paramPredictiveCardContainer)
  {
    NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
    return (this.mSportEventEntry.hasVenue()) && (this.mSportEventEntry.hasRoute()) && (localNavigationContext != null) && (localNavigationContext.shouldShowNavigation(this.mSportEventEntry.getVenue()));
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (this.mSportEventEntry.hasConfirmationNumber()) {}
    for (View localView = showBarcodeCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);; localView = showConfirmationCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup))
    {
      maybeAddTimeToLeaveBanner(paramContext, localView, paramPredictiveCardContainer);
      maybeAddNavigateButton(paramContext, paramPredictiveCardContainer, localView);
      maybeAddGmailButton(paramContext, paramPredictiveCardContainer, localView);
      maybeAddModifyReservationButton(paramContext, paramPredictiveCardContainer, localView);
      return localView;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.SportEventTicketEntryAdapter
 * JD-Core Version:    0.7.0.1
 */