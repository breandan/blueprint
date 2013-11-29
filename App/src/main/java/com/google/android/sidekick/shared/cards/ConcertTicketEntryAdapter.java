package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TableLayout;
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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EventPerformer;
import com.google.geo.sidekick.Sidekick.EventTicketSeatInfo;
import com.google.geo.sidekick.Sidekick.GmailReference;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.MoonshineEventTicketEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ConcertTicketEntryAdapter
  extends BaseEntryAdapter
{
  static final int DATE_FLAGS = 524306;
  static final int TIME_FLAGS = 1;
  private final Clock mClock;
  private final Sidekick.MoonshineEventTicketEntry mConcertEntry;
  private final DirectionsLauncher mDirectionsLauncher;
  
  ConcertTicketEntryAdapter(Sidekick.Entry paramEntry, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mConcertEntry = paramEntry.getMoonshineEventTicketEntry();
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mClock = paramClock;
  }
  
  private void inflateReservedTicketInfoTable(Sidekick.EventTicketSeatInfo paramEventTicketSeatInfo, View paramView, Context paramContext)
  {
    TableLayout localTableLayout = (TableLayout)((ViewStub)paramView.findViewById(2131296570)).inflate();
    if (paramEventTicketSeatInfo.hasSeat())
    {
      ((TextView)localTableLayout.findViewById(2131296580)).setText(paramEventTicketSeatInfo.getSeat());
      if (!paramEventTicketSeatInfo.hasSeatRow()) {
        break label207;
      }
      ((TextView)localTableLayout.findViewById(2131296581)).setText(paramEventTicketSeatInfo.getSeatRow());
      label65:
      if (!paramEventTicketSeatInfo.hasSeatSection()) {
        break label240;
      }
      ((TextView)localTableLayout.findViewById(2131296582)).setText(paramEventTicketSeatInfo.getSeatSection());
    }
    for (;;)
    {
      String str = getDoorOpenTimeString(paramContext);
      if (str != null)
      {
        ((TextView)localTableLayout.findViewById(2131296584)).setText(str);
        localTableLayout.findViewById(2131296583).setVisibility(0);
      }
      if ((this.mConcertEntry.hasVenue()) && (this.mConcertEntry.getVenue().hasName())) {
        ((TextView)localTableLayout.findViewById(2131296575)).setText(this.mConcertEntry.getVenue().getName());
      }
      return;
      ((TextView)localTableLayout.findViewById(2131296361)).setVisibility(8);
      ((TextView)localTableLayout.findViewById(2131296580)).setVisibility(8);
      break;
      label207:
      ((TextView)localTableLayout.findViewById(2131296578)).setVisibility(8);
      ((TextView)localTableLayout.findViewById(2131296581)).setVisibility(8);
      break label65;
      label240:
      ((TextView)localTableLayout.findViewById(2131296579)).setVisibility(8);
      ((TextView)localTableLayout.findViewById(2131296582)).setVisibility(8);
    }
  }
  
  private void inflateUnreservedTicketInfoTable(View paramView, Context paramContext)
  {
    TableLayout localTableLayout = (TableLayout)((ViewStub)paramView.findViewById(2131296571)).inflate();
    if (this.mConcertEntry.hasNumberOfTickets()) {
      ((TextView)localTableLayout.findViewById(2131296588)).setText(Integer.toString(this.mConcertEntry.getNumberOfTickets()));
    }
    if ((this.mConcertEntry.hasVenue()) && (this.mConcertEntry.getVenue().hasName())) {
      ((TextView)localTableLayout.findViewById(2131296575)).setText(this.mConcertEntry.getVenue().getName());
    }
    String str = getDoorOpenTimeString(paramContext);
    if (str != null) {
      ((TextView)localTableLayout.findViewById(2131296586)).setText(str);
    }
  }
  
  private void maybeAddGmailButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mConcertEntry.hasGmailReference()) && (this.mConcertEntry.getGmailReference().hasEmailUrl()))
    {
      String str = this.mConcertEntry.getGmailReference().getEmailUrl();
      Button localButton = (Button)paramView.findViewById(2131296376);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new GoogleServiceWebviewClickListener(paramContext, str, this.mConcertEntry.getTitle(), false, this, 52, "mail", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES, null, paramPredictiveCardContainer));
    }
  }
  
  private void maybeAddModifyReservationButton(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if ((this.mConcertEntry.hasManageReservationUrl()) && (!this.mConcertEntry.getManageReservationUrl().isEmpty()))
    {
      final String str = this.mConcertEntry.getManageReservationUrl();
      Button localButton = (Button)paramView.findViewById(2131296448);
      localButton.setVisibility(0);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 51)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          ConcertTicketEntryAdapter.this.openUrl(paramContext, str);
        }
      });
    }
  }
  
  private void maybeAddNavigateButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (showNavigation(paramPredictiveCardContainer)) {
      configureRouteButtons(paramView, this.mConcertEntry.getRoute(), paramContext, this.mDirectionsLauncher, paramPredictiveCardContainer, this.mConcertEntry.getVenue(), true);
    }
  }
  
  private void maybeAddTicketAdditionalInfo(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (!this.mConcertEntry.hasTicketType()) {}
    do
    {
      return;
      String str = this.mConcertEntry.getTicketType();
      TextView localTextView = (TextView)paramView.findViewById(2131296573);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = BidiUtils.unicodeWrap(str);
      localTextView.setText(paramContext.getString(2131362587, arrayOfObject));
      paramView.findViewById(2131296572).setVisibility(0);
    } while (!this.mConcertEntry.hasBookingAgentLogoUrl());
    ((WebImageView)paramView.findViewById(2131296574)).setImageUrl(this.mConcertEntry.getBookingAgentLogoUrl(), paramPredictiveCardContainer.getImageLoader());
  }
  
  private void maybeAddTimeToLeaveBanner(Context paramContext, View paramView, PredictiveCardContainer paramPredictiveCardContainer)
  {
    long l = this.mClock.currentTimeMillis();
    if (this.mConcertEntry.hasStartTimeMs()) {}
    for (Long localLong1 = Long.valueOf(this.mConcertEntry.getStartTimeMs());; localLong1 = null)
    {
      boolean bool = this.mConcertEntry.hasDepartureTimeMs();
      Long localLong2 = null;
      if (bool) {
        localLong2 = Long.valueOf(this.mConcertEntry.getDepartureTimeMs());
      }
      if (MoonshineEventTicketUtils.shouldShowTtlBanner(l, localLong1, localLong2, showNavigation(paramPredictiveCardContainer))) {
        break;
      }
      return;
    }
    MoonshineEventTicketUtils.addTtlBanner(paramView, l - this.mConcertEntry.getDepartureTimeMs(), paramContext, this.mConcertEntry.getDepartureTimeMs());
  }
  
  private void populateConcertInfo(PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    ((TextView)paramView.findViewById(2131296451)).setText(Html.fromHtml(this.mConcertEntry.getTitle()));
    if (this.mConcertEntry.getPerformersCount() > 0)
    {
      String str = null;
      ArrayList localArrayList = Lists.newArrayListWithCapacity(2);
      Iterator localIterator = Iterables.limit(this.mConcertEntry.getPerformersList(), 2).iterator();
      while (localIterator.hasNext())
      {
        Sidekick.EventPerformer localEventPerformer = (Sidekick.EventPerformer)localIterator.next();
        localArrayList.add(localEventPerformer.getName());
        if ((str == null) || (str.isEmpty())) {
          str = localEventPerformer.getImageUrl();
        }
      }
      CardTextUtil.setHyphenatedTextView(paramView, 2131296563, localArrayList);
      if (str != null)
      {
        paramView.findViewById(2131296333).setVisibility(0);
        ((WebImageView)paramView.findViewById(2131296552)).setImageUrl(str, paramPredictiveCardContainer.getImageLoader());
      }
    }
  }
  
  private void populateShowtimeText(View paramView, Context paramContext)
  {
    if (this.mConcertEntry.hasStartTimeMs())
    {
      String str = paramContext.getString(2131362585, new Object[] { DateUtils.formatDateTime(paramContext, this.mConcertEntry.getStartTimeMs(), 1) });
      ((TextView)paramView.findViewById(2131296562)).setText(str);
    }
  }
  
  private void populateTicketInfo(View paramView, Context paramContext)
  {
    if (this.mConcertEntry.getSeatInformationCount() > 0)
    {
      Sidekick.EventTicketSeatInfo localEventTicketSeatInfo = this.mConcertEntry.getSeatInformation(0);
      if (localEventTicketSeatInfo.hasUnderName())
      {
        TextView localTextView = (TextView)paramView.findViewById(2131296569);
        localTextView.setText(localEventTicketSeatInfo.getUnderName());
        localTextView.setVisibility(0);
        ((TextView)paramView.findViewById(2131296566)).setVisibility(0);
      }
      if ((localEventTicketSeatInfo.hasSeat()) || (localEventTicketSeatInfo.hasSeatRow()))
      {
        inflateReservedTicketInfoTable(localEventTicketSeatInfo, paramView, paramContext);
        return;
      }
    }
    inflateUnreservedTicketInfoTable(paramView, paramContext);
  }
  
  private View showBarcodeCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968673, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(this.mConcertEntry.getTitle());
    populateShowtimeText(localView, paramContext);
    if ((this.mConcertEntry.hasBarcode()) && (this.mConcertEntry.getBarcode().hasUrl())) {
      MoonshineEventTicketUtils.addBarcode(paramPredictiveCardContainer, localView, this.mConcertEntry.getBarcode().getUrl());
    }
    if (this.mConcertEntry.hasConfirmationNumber()) {
      MoonshineEventTicketUtils.addTicketNumber(localView, this.mConcertEntry.getConfirmationNumber());
    }
    populateTicketInfo(localView, paramContext);
    populateConcertInfo(paramPredictiveCardContainer, localView);
    maybeAddTicketAdditionalInfo(paramContext, paramPredictiveCardContainer, localView);
    return localView;
  }
  
  private View showConcertCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968674, paramViewGroup, false);
    populateConcertInfo(paramPredictiveCardContainer, localView);
    populateShowtimeText(localView, paramContext);
    if ((this.mConcertEntry.hasVenue()) && (this.mConcertEntry.getVenue().hasName()))
    {
      TextView localTextView2 = (TextView)localView.findViewById(2131296575);
      localTextView2.setText(this.mConcertEntry.getVenue().getName());
      localTextView2.setVisibility(0);
    }
    if (this.mConcertEntry.hasStartTimeMs())
    {
      String str = DateUtils.formatDateTime(paramContext, this.mConcertEntry.getStartTimeMs(), 524306);
      TextView localTextView1 = (TextView)localView.findViewById(2131296576);
      localTextView1.setText(str);
      localTextView1.setVisibility(0);
    }
    return localView;
  }
  
  private boolean showNavigation(PredictiveCardContainer paramPredictiveCardContainer)
  {
    NavigationContext localNavigationContext = NavigationContext.fromRenderingContext(paramPredictiveCardContainer.getCardRenderingContext());
    return (this.mConcertEntry.hasVenue()) && (this.mConcertEntry.hasRoute()) && (localNavigationContext.shouldShowNavigation(this.mConcertEntry.getVenue()));
  }
  
  @Nullable
  String getDoorOpenTimeString(Context paramContext)
  {
    if (this.mConcertEntry.hasDoorOpenTimeMs()) {
      return paramContext.getString(2131362586, new Object[] { DateUtils.formatDateTime(paramContext, this.mConcertEntry.getDoorOpenTimeMs(), 1), DateUtils.formatDateTime(paramContext, this.mConcertEntry.getDoorOpenTimeMs(), 524306) });
    }
    return DateUtils.formatDateTime(paramContext, this.mConcertEntry.getStartTimeMs(), 524306);
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (this.mConcertEntry.hasConfirmationNumber()) {}
    for (View localView = showBarcodeCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);; localView = showConcertCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup))
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
 * Qualified Name:     com.google.android.sidekick.shared.cards.ConcertTicketEntryAdapter
 * JD-Core Version:    0.7.0.1
 */