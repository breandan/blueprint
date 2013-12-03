package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.ui.FlightCard;
import com.google.android.sidekick.shared.ui.FlightCard.Builder;
import com.google.android.sidekick.shared.ui.FlightCard.SegmentBuilder;
import com.google.android.sidekick.shared.util.CalendarDataUtil;
import com.google.android.sidekick.shared.util.GoogleServiceWebviewUtil;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.util.AgendaTimeUtil;
import com.google.android.voicesearch.util.CalendarHelper;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.majel.proto.AgendaProtos.AgendaItemLocation;
import com.google.majel.proto.AgendaProtos.GmailReference;
import com.google.majel.proto.CalendarProtos.AgendaItem;
import com.google.majel.proto.CalendarProtos.DefaultExpansion;
import com.google.majel.proto.CarRentalProtos.CarRentalEntry;
import com.google.majel.proto.FlightProtos.FlightStatusEntry;
import com.google.majel.proto.FlightProtos.FlightStatusEntry.Airport;
import com.google.majel.proto.FlightProtos.FlightStatusEntry.Flight;
import com.google.majel.proto.FlightProtos.FlightStatusEntry.Time;
import com.google.majel.proto.ReservationProtos.Reservation;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Nullable;

public class AgendaCard
        extends AbstractCardView<AgendaController>
        implements AgendaController.Ui {
    private static final Map<Integer, Integer> ICON_MAP = ImmutableMap.builder().put(Integer.valueOf(1), Integer.valueOf(2130837716)).put(Integer.valueOf(2), Integer.valueOf(2130837713)).put(Integer.valueOf(3), Integer.valueOf(2130837719)).put(Integer.valueOf(4), Integer.valueOf(2130837723)).put(Integer.valueOf(7), Integer.valueOf(2130837726)).put(Integer.valueOf(8), Integer.valueOf(2130837710)).put(Integer.valueOf(9), Integer.valueOf(2130837722)).build();
    private static final Map<Integer, Integer> ICON_MAP_ALERT = ImmutableMap.builder().put(Integer.valueOf(1), Integer.valueOf(2130837718)).build();
    private static final Map<Integer, Integer> ICON_MAP_NORMAL = ImmutableMap.builder().put(Integer.valueOf(1), Integer.valueOf(2130837717)).build();
    private AgendaCardContainerView mAgendaCardList;
    private LinearLayout mContainer;
    private final View.OnClickListener mExpandClickListener = new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            AgendaCard.this.expandAgendaItem((AgendaExpandableItemView) paramAnonymousView);
        }
    };
    private final View.OnClickListener mShowMoreButtonClickListener = new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            if (!AgendaCard.this.mAgendaCardList.showMoreItems()) {
                AgendaCard.this.mContainer.removeView((View) paramAnonymousView.getParent());
            }
        }
    };

    public AgendaCard(Context paramContext) {
        super(paramContext);
        SuggestionGridLayout.LayoutParams localLayoutParams = new SuggestionGridLayout.LayoutParams(-1, -2, 0);
        localLayoutParams.canDismiss = false;
        localLayoutParams.canDrag = false;
        setLayoutParams(localLayoutParams);
    }

    private void addFlightSegment(FlightProtos.FlightStatusEntry.Flight paramFlight, FlightCard.SegmentBuilder paramSegmentBuilder, FlightCard.Builder paramBuilder) {
        if (paramFlight.hasStatusCode()) {
            paramSegmentBuilder.setStatus(paramFlight.getStatusCode());
            if ((!paramFlight.hasDepartureAirport()) || (!paramFlight.hasDepartureTime())) {
                break label200;
            }
            FlightProtos.FlightStatusEntry.Airport localAirport2 = paramFlight.getDepartureAirport();
            FlightProtos.FlightStatusEntry.Time localTime2 = paramFlight.getDepartureTime();
            paramSegmentBuilder.departure().setAirportName("").setAirportCode(localAirport2.getCode()).setScheduled(getTime(localTime2, 0)).setActual(getTime(localTime2, 1)).setTerminal(paramFlight.getDepartureTerminal()).setGate(paramFlight.getDepartureGate());
            label92:
            if ((!paramFlight.hasArrivalAirport()) || (!paramFlight.hasArrivalTime())) {
                break label211;
            }
            FlightProtos.FlightStatusEntry.Airport localAirport1 = paramFlight.getArrivalAirport();
            FlightProtos.FlightStatusEntry.Time localTime1 = paramFlight.getArrivalTime();
            paramSegmentBuilder.arrival().setAirportName("").setAirportCode(localAirport1.getCode()).setScheduled(getTime(localTime1, 0)).setActual(getTime(localTime1, 1)).setTerminal(paramFlight.getArrivalTerminal()).setGate(paramFlight.getArrivalGate());
        }
        for (; ; ) {
            paramBuilder.setGmailReferenceList(createSidekickGmailReferenceList(paramFlight.getGmailReferenceList()));
            paramBuilder.setDetailsUrl(paramFlight.getDetailsUrl());
            return;
            paramSegmentBuilder.setUseDepartureForStatus(true);
            break;
            label200:
            Log.w("AgendaCard", "Missing departure info");
            break label92;
            label211:
            Log.w("AgendaCard", "Missing arrival info");
        }
    }

    private AgendaExpandableItemView createItemView(CalendarProtos.AgendaItem paramAgendaItem) {
        AgendaExpandableItemView localAgendaExpandableItemView = new AgendaExpandableItemView(getContext());
        localAgendaExpandableItemView.setOnClickListener(this.mExpandClickListener);
        localAgendaExpandableItemView.setCollapsedView(getCollapsedView(paramAgendaItem));
        if (!paramAgendaItem.hasDefaultExpansion()) {
            CalendarProtos.DefaultExpansion localDefaultExpansion = new CalendarProtos.DefaultExpansion();
            localDefaultExpansion.setLabel(getContext().getString(2131363345));
            paramAgendaItem.setDefaultExpansion(localDefaultExpansion);
        }
        localAgendaExpandableItemView.setExpandedView(getExpandedView(paramAgendaItem));
        return localAgendaExpandableItemView;
    }

    private List<Sidekick.GmailReference> createSidekickGmailReferenceList(List<AgendaProtos.GmailReference> paramList) {
        Lists.transform(paramList, new Function() {
            public Sidekick.GmailReference apply(AgendaProtos.GmailReference paramAnonymousGmailReference) {
                return new Sidekick.GmailReference().setSenderEmailAddress(paramAnonymousGmailReference.getSenderEmailAddress()).setEmailUrl(paramAnonymousGmailReference.getEmailUrl());
            }
        });
    }

    private void expandAgendaItem(AgendaExpandableItemView paramAgendaExpandableItemView) {
        this.mAgendaCardList.expand(paramAgendaExpandableItemView);
    }

    private void fillCollapsedViewIcon(CalendarProtos.AgendaItem paramAgendaItem, View paramView) {
        ImageView localImageView = (ImageView) paramView.findViewById(2131296310);
        if ((paramAgendaItem.getStatus() == 1) && (ICON_MAP_NORMAL.containsKey(Integer.valueOf(paramAgendaItem.getIcon())))) {
            localImageView.setImageResource(((Integer) ICON_MAP_NORMAL.get(Integer.valueOf(paramAgendaItem.getIcon()))).intValue());
        }
        do {
            return;
            if ((paramAgendaItem.getStatus() == 2) && (ICON_MAP_ALERT.containsKey(Integer.valueOf(paramAgendaItem.getIcon())))) {
                localImageView.setImageResource(((Integer) ICON_MAP_ALERT.get(Integer.valueOf(paramAgendaItem.getIcon()))).intValue());
                return;
            }
        } while (!ICON_MAP.containsKey(Integer.valueOf(paramAgendaItem.getIcon())));
        localImageView.setImageResource(((Integer) ICON_MAP.get(Integer.valueOf(paramAgendaItem.getIcon()))).intValue());
    }

    private void fillCollapsedViewSubtitle(CalendarProtos.AgendaItem paramAgendaItem, View paramView) {
        TextView localTextView1 = (TextView) paramView.findViewById(2131296309);
        switch (paramAgendaItem.getEventType()) {
            default:
                if (paramAgendaItem.hasSubtitle()) {
                    String str2 = "";
                    if (!TextUtils.isEmpty(paramAgendaItem.getSubtitle().getStatus())) {
                        TextView localTextView2 = (TextView) paramView.findViewById(2131296308);
                        localTextView2.setText(paramAgendaItem.getSubtitle().getStatus());
                        localTextView2.setTextColor(getColorForStatus(paramAgendaItem.getStatus()));
                        localTextView2.setVisibility(0);
                        str2 = " – ";
                    }
                    if (paramAgendaItem.getSubtitle().getTextSegmentCount() > 0) {
                        localTextView1.setText(str2 + paramAgendaItem.getSubtitle().getTextSegment(0));
                    }
                }
                return;
            case 1:
            case 2:
                localTextView1.setText(getContext().getString(2131363356));
                return;
        }
        Time localTime = AgendaTimeUtil.toTime(paramAgendaItem.getEndTime());
        if ((paramAgendaItem.getAllDay()) || (AgendaTimeUtil.isStartOfDay(localTime))) {
            localTextView1.setText(getContext().getString(2131363356));
            return;
        }
        String str1 = DateUtils.formatDateTime(getContext(), localTime.toMillis(false), 1);
        localTextView1.setText(getContext().getString(2131363357, new Object[]{str1}));
    }

    private void fillCollapsedViewTime(CalendarProtos.AgendaItem paramAgendaItem, View paramView) {
        if ((paramAgendaItem.getAllDay()) || (paramAgendaItem.getStartTime().getTimeUnspecified())) {
        }
        while ((paramAgendaItem.getEventType() == 2) || (paramAgendaItem.getEventType() == 3)) {
            return;
        }
        TextView localTextView = (TextView) paramView.findViewById(2131296305);
        if (DateFormat.is24HourFormat(getContext())) {
            localTextView.setText(AgendaTimeUtil.format(getContext(), paramAgendaItem.getStartTime(), 1));
            return;
        }
        Calendar localCalendar = AgendaTimeUtil.toCalendar(paramAgendaItem.getStartTime());
        localTextView.setText(DateFormat.format("h:mm", localCalendar));
        ((TextView) paramView.findViewById(2131296306)).setText(DateFormat.format("a", localCalendar));
    }

    private View.OnClickListener getCalendarEventClickHandler(final long paramLong) {
        new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent localIntent = CalendarHelper.createViewEventIntent(paramLong);
                localIntent.setFlags(268435456);
                AgendaCard.this.getContext().startActivity(localIntent);
            }
        };
    }

    private View getCarRentalExpandedView(CalendarProtos.AgendaItem paramAgendaItem) {
        CarRentalProtos.CarRentalEntry localCarRentalEntry = paramAgendaItem.getCarRental();
        Context localContext = getContext();
        View localView;
        if (localCarRentalEntry.getType() == 1) {
            localView = View.inflate(getContext(), 2130968621, null);
            if (paramAgendaItem.hasStartTime()) {
                setVisibleWithText(localView, 2131296443, AgendaTimeUtil.format(localContext, paramAgendaItem.getStartTime(), 18));
                setVisibleWithText(localView, 2131296446, AgendaTimeUtil.format(localContext, paramAgendaItem.getStartTime(), 1));
            }
            if (localCarRentalEntry.hasPickupLocation()) {
                setLocationWithAddress(localCarRentalEntry.getPickupLocation(), 2131296444, 2131296447, localView, getContext());
            }
            if (localCarRentalEntry.hasConfirmationNumber()) {
                localView.findViewById(2131296439).setVisibility(0);
                setVisibleWithText(localView, 2131296441, localCarRentalEntry.getConfirmationNumber());
            }
        }
        for (; ; ) {
            if (paramAgendaItem.hasTitle()) {
                setVisibleWithText(localView, 2131296451, paramAgendaItem.getTitle());
            }
            if (paramAgendaItem.hasSubtitle()) {
                setVisibleWithText(localView, 2131296425, paramAgendaItem.getSubtitle().getTextSegment(0));
            }
            if (localCarRentalEntry.hasRenterName()) {
                setVisibleWithText(localView, 2131296427, localCarRentalEntry.getRenterName());
            }
            if (localCarRentalEntry.hasGmailReference()) {
                String str1 = localCarRentalEntry.getGmailReference().getEmailUrl();
                String str2 = paramAgendaItem.getTitle();
                Button localButton = (Button) localView.findViewById(2131296376);
                localButton.setVisibility(0);
                localButton.setOnClickListener(getGmailViewClickHandler(str1, str2));
            }
            return localView;
            localView = View.inflate(getContext(), 2130968620, null);
            if (paramAgendaItem.hasEndTime()) {
                setVisibleWithText(localView, 2131296430, AgendaTimeUtil.format(localContext, paramAgendaItem.getEndTime(), 18));
                setVisibleWithText(localView, 2131296433, AgendaTimeUtil.format(localContext, paramAgendaItem.getEndTime(), 1));
            }
            if (localCarRentalEntry.hasReturnLocation()) {
                setLocationWithAddress(localCarRentalEntry.getReturnLocation(), 2131296431, 2131296434, localView, getContext());
            }
        }
    }

    private View getCollapsedView(CalendarProtos.AgendaItem paramAgendaItem) {
        View localView = View.inflate(getContext(), 2130968587, null);
        fillCollapsedViewTime(paramAgendaItem, localView);
        ((TextView) localView.findViewById(2131296307)).setText(paramAgendaItem.getTitle());
        fillCollapsedViewSubtitle(paramAgendaItem, localView);
        fillCollapsedViewIcon(paramAgendaItem, localView);
        return localView;
    }

    private int getColorForStatus(int paramInt) {
        switch (paramInt) {
            default:
                return getResources().getColor(2131230920);
            case 1:
                return getResources().getColor(2131230923);
        }
        return getResources().getColor(2131230922);
    }

    private View getDefaultExpandedView(CalendarProtos.AgendaItem paramAgendaItem) {
        View localView = View.inflate(getContext(), 2130968589, null);
        CalendarProtos.DefaultExpansion localDefaultExpansion = paramAgendaItem.getDefaultExpansion();
        ((TextView) localView.findViewById(2131296451)).setText(paramAgendaItem.getTitle());
        TextView localTextView1 = (TextView) localView.findViewById(2131296313);
        if ((!paramAgendaItem.getAllDay()) && (!paramAgendaItem.getStartTime().getTimeUnspecified())) {
            localTextView1.setVisibility(0);
            localTextView1.setText(AgendaTimeUtil.formatPretty(getContext(), paramAgendaItem.getStartTime(), 0));
        }
        TextView localTextView2 = (TextView) localView.findViewById(2131296314);
        if ((paramAgendaItem.hasSubtitle()) && (paramAgendaItem.getSubtitle().getTextSegmentCount() > 0)) {
            localTextView2.setVisibility(0);
            localTextView2.setText(paramAgendaItem.getSubtitle().getTextSegment(0));
        }
        Button localButton = (Button) localView.findViewById(2131296315);
        localButton.setText(localDefaultExpansion.getLabel());
        String str1 = localDefaultExpansion.getUrl();
        if (localDefaultExpansion.getUrlType() == 1) {
            String str2 = paramAgendaItem.getTitle();
            localButton.setCompoundDrawablesWithIntrinsicBounds(2130837645, 0, 0, 0);
            localButton.setOnClickListener(getGmailViewClickHandler(str1, str2));
            return localView;
        }
        localButton.setOnClickListener(getWebUrlClickHandler(str1));
        return localView;
    }

    private View.OnClickListener getDirectionsClickHandler(AgendaProtos.AgendaItemLocation paramAgendaItemLocation) {
        Sidekick.Location localLocation = new Sidekick.Location();
        localLocation.setName(paramAgendaItemLocation.getName());
        localLocation.setAddress(paramAgendaItemLocation.getAddress());
        localLocation.setLat(paramAgendaItemLocation.getLat());
        localLocation.setLng(paramAgendaItemLocation.getLng());
        new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent localIntent = new Intent("android.intent.action.VIEW", this.val$mapsUri);
                localIntent.setFlags(268435456);
                AgendaCard.this.getContext().startActivity(localIntent);
            }
        };
    }

    private View.OnClickListener getEmailAttendeesClickHandler(final long paramLong) {
        new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent localIntent = CalendarDataUtil.createEmailAttendeesIntent(paramLong);
                localIntent.setFlags(268435456);
                AgendaCard.this.getContext().sendBroadcast(localIntent);
            }
        };
    }

    private View getEventExpandedView(CalendarProtos.AgendaItem paramAgendaItem) {
        View localView = View.inflate(getContext(), 2130968612, null);
        ((TextView) localView.findViewById(2131296451)).setText(paramAgendaItem.getTitle());
        if (paramAgendaItem.getEvent().getIsGplusEvent()) {
            ((TextView) localView.findViewById(2131296401)).setText(getContext().getString(2131363354));
        }
        Time localTime1 = AgendaTimeUtil.toTime(paramAgendaItem.getStartTime());
        Time localTime2;
        if (paramAgendaItem.hasEndTime()) {
            localTime2 = AgendaTimeUtil.toTime(paramAgendaItem.getEndTime());
            if (!paramAgendaItem.getAllDay()) {
                break label399;
            }
        }
        ViewGroup localViewGroup;
        int j;
        label399:
        for (int i = 524306; ; i = 527123) {
            String str = DateUtils.formatDateRange(getContext(), localTime1.toMillis(false), localTime2.toMillis(false), i);
            if ((paramAgendaItem.getAllDay()) && (AgendaTimeUtil.relativeDays(localTime1, localTime2) <= 1)) {
                str = getContext().getString(2131363355, new Object[]{str});
            }
            ((TextView) localView.findViewById(2131296402)).setText(str);
            if (!TextUtils.isEmpty(paramAgendaItem.getEvent().getDescription())) {
                ((TextView) localView.findViewById(2131296408)).setText(paramAgendaItem.getEvent().getDescription());
                localView.findViewById(2131296407).setVisibility(0);
            }
            if (!TextUtils.isEmpty(paramAgendaItem.getEvent().getLocation())) {
                ((TextView) localView.findViewById(2131296404)).setText(paramAgendaItem.getEvent().getLocation());
                localView.findViewById(2131296403).setVisibility(0);
            }
            if (!TextUtils.isEmpty(paramAgendaItem.getEvent().getCalendarName())) {
                ((TextView) localView.findViewById(2131296406)).setText(paramAgendaItem.getEvent().getCalendarName());
                localView.findViewById(2131296405).setVisibility(0);
            }
            if (paramAgendaItem.getEvent().getAttendeeCount() <= 0) {
                break label494;
            }
            localViewGroup = (ViewGroup) localView.findViewById(2131296410);
            j = Math.min(3, paramAgendaItem.getEvent().getAttendeeCount());
            for (int k = 0; k < j; k++) {
                TextView localTextView2 = (TextView) View.inflate(getContext(), 2130968632, null);
                localTextView2.setText(paramAgendaItem.getEvent().getAttendee(k));
                localViewGroup.addView(localTextView2);
            }
            localTime2 = localTime1;
            break;
        }
        int m = paramAgendaItem.getEvent().getAttendeeCount() - j;
        if (m > 0) {
            TextView localTextView1 = (TextView) View.inflate(getContext(), 2130968632, null);
            Context localContext = getContext();
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Integer.valueOf(m);
            localTextView1.setText(localContext.getString(2131363352, arrayOfObject));
            localViewGroup.addView(localTextView1);
        }
        localView.findViewById(2131296409).setVisibility(0);
        label494:
        Button localButton1 = (Button) localView.findViewById(2131296412);
        if (paramAgendaItem.getEvent().hasEventId()) {
            long l = paramAgendaItem.getEvent().getEventId();
            localButton1.setOnClickListener(getCalendarEventClickHandler(l));
            if (paramAgendaItem.getEvent().getTotalAttendeeCount() > 1) {
                Button localButton2 = (Button) localView.findViewById(2131296411);
                localButton2.setVisibility(0);
                localButton2.setOnClickListener(getEmailAttendeesClickHandler(l));
            }
            return localView;
        }
        localButton1.setOnClickListener(getWebUrlClickHandler(paramAgendaItem.getEvent().getUrl()));
        return localView;
    }

    private int getEventTypeForDay(int paramInt1, int paramInt2, int paramInt3) {
        if (paramInt1 == paramInt2) {
            return 0;
        }
        if (paramInt3 == paramInt1) {
            return 1;
        }
        if (paramInt3 == paramInt2) {
            return 3;
        }
        return 2;
    }

    @Nullable
    private View getExpandedView(CalendarProtos.AgendaItem paramAgendaItem) {
        View localView;
        if (paramAgendaItem.hasFlight()) {
            localView = getFlightExpandedView(paramAgendaItem);
        }
        for (; ; ) {
            if ((localView == null) && (paramAgendaItem.hasDefaultExpansion())) {
                localView = getDefaultExpandedView(paramAgendaItem);
            }
            return localView;
            if (paramAgendaItem.hasEvent()) {
                localView = getEventExpandedView(paramAgendaItem);
            } else if (paramAgendaItem.hasReservation()) {
                localView = getReservationExpandedView(paramAgendaItem);
            } else {
                boolean bool = paramAgendaItem.hasCarRental();
                localView = null;
                if (bool) {
                    localView = getCarRentalExpandedView(paramAgendaItem);
                }
            }
        }
    }

    private View getFlightExpandedView(CalendarProtos.AgendaItem paramAgendaItem) {
        FlightCard localFlightCard = (FlightCard) View.inflate(getContext(), 2130968690, null);
        FlightProtos.FlightStatusEntry localFlightStatusEntry = paramAgendaItem.getFlight();
        FlightProtos.FlightStatusEntry.Flight localFlight = localFlightStatusEntry.getFlight(0);
        FlightCard.Builder localBuilder = new FlightCard.Builder(null, localFlight.getAirlineName(), localFlight.getFlightNumber());
        Iterator localIterator = localFlightStatusEntry.getFlightList().iterator();
        while (localIterator.hasNext()) {
            addFlightSegment((FlightProtos.FlightStatusEntry.Flight) localIterator.next(), localBuilder.addSegment(), localBuilder);
        }
        localBuilder.update(localFlightCard);
        return localFlightCard;
    }

    private View.OnClickListener getGmailViewClickHandler(final String paramString1, final String paramString2) {
        new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent localIntent = GoogleServiceWebviewUtil.createIntent(Uri.parse(paramString1));
                localIntent.putExtra("webview_service", "mail").putExtra("webview_title", paramString2).putExtra("enable_javascript", false).putExtra("webview_url_prefixes", GoogleServiceWebviewUtil.GMAIL_URL_PREFIXES);
                AgendaCard.this.getContext().startActivity(localIntent);
            }
        };
    }

    private CharSequence getReservationContextMessage(CalendarProtos.AgendaItem paramAgendaItem) {
        boolean bool = AgendaTimeUtil.isToday(paramAgendaItem);
        int i;
        if (bool) {
            i = 1;
            switch (paramAgendaItem.getReservation().getType()) {
            }
        }
        do {
            Log.w("AgendaCard", "Unrecognized reservation type");
            return null;
            i = 19;
            break;
            return getReservationString(paramAgendaItem, 2131362690, i);
            return getReservationString(paramAgendaItem, 2131362689, i);
            if (bool) {
                return getContext().getString(2131362686);
            }
            return getReservationString(paramAgendaItem, 2131362685, 18);
            if (bool) {
                return getContext().getString(2131362687);
            }
        } while (!AgendaTimeUtil.isTomorrow(paramAgendaItem));
        return getContext().getString(2131362688);
    }

    private View getReservationExpandedView(CalendarProtos.AgendaItem paramAgendaItem) {
        View localView = View.inflate(getContext(), 2130968672, null);
        ReservationProtos.Reservation localReservation = paramAgendaItem.getReservation();
        ((TextView) localView.findViewById(2131296451)).setText(paramAgendaItem.getTitle());
        String str1 = localReservation.getLocation().getAddress();
        if (!TextUtils.isEmpty(str1)) {
            ((TextView) localView.findViewById(2131296556)).setText(str1);
        }
        CharSequence localCharSequence = getReservationContextMessage(paramAgendaItem);
        if (!TextUtils.isEmpty(localCharSequence)) {
            ((TextView) localView.findViewById(2131296325)).setText(localCharSequence);
        }
        String str2 = localReservation.getLocation().getStaticMapUrl();
        if (!TextUtils.isEmpty(str2)) {
            Uri localUri = Uri.parse("http://www.google.com" + str2);
            VelvetServices localVelvetServices = VelvetServices.get();
            WebImageView localWebImageView = (WebImageView) localView.findViewById(2131296557);
            localWebImageView.setImageUri(localUri, localVelvetServices.getImageLoader());
            localWebImageView.setVisibility(0);
        }
        if ((localReservation.getLocation().hasLat()) && (localReservation.getLocation().hasLng())) {
            Button localButton3 = (Button) localView.findViewById(2131296335);
            localButton3.setVisibility(0);
            localButton3.setOnClickListener(getDirectionsClickHandler(localReservation.getLocation()));
        }
        if ((localReservation.hasPartySize()) && (localReservation.hasReservationUrl())) {
            Button localButton2 = (Button) localView.findViewById(2131296448);
            localButton2.setVisibility(0);
            localButton2.setOnClickListener(getWebUrlClickHandler(localReservation.getReservationUrl()));
        }
        if (localReservation.getGmailReferenceCount() > 0) {
            String str3 = localReservation.getGmailReference(0).getEmailUrl();
            String str4 = paramAgendaItem.getTitle();
            Button localButton1 = (Button) localView.findViewById(2131296376);
            localButton1.setVisibility(0);
            localButton1.setOnClickListener(getGmailViewClickHandler(str3, str4));
        }
        return localView;
    }

    private CharSequence getReservationString(CalendarProtos.AgendaItem paramAgendaItem, int paramInt1, int paramInt2) {
        Context localContext = getContext();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = AgendaTimeUtil.format(getContext(), paramAgendaItem.getStartTime(), paramInt2);
        return localContext.getString(paramInt1, arrayOfObject);
    }

    private Pair<Integer, Integer> getStartAndEndDay(CalendarProtos.AgendaItem paramAgendaItem) {
        int i = AgendaTimeUtil.getJulianDay(paramAgendaItem.getStartTime());
        if ((!paramAgendaItem.hasEvent()) || (!paramAgendaItem.hasEndTime())) {
            return new Pair(Integer.valueOf(i), Integer.valueOf(i));
        }
        Time localTime = AgendaTimeUtil.toTime(paramAgendaItem.getEndTime());
        int j = AgendaTimeUtil.getJulianDay(localTime);
        if (AgendaTimeUtil.isStartOfDay(localTime)) {
            j--;
        }
        return new Pair(Integer.valueOf(i), Integer.valueOf(j));
    }

    private static Calendar getTime(FlightProtos.FlightStatusEntry.Time paramTime, int paramInt) {
        Preconditions.checkNotNull(Integer.valueOf(paramInt));
        if (paramTime == null) {
            return null;
        }
        String str;
        label29:
        TimeZone localTimeZone;
        long l;
        if (paramTime.hasTimeZoneId()) {
            str = FlightCard.fixTimeZone(paramTime.getTimeZoneId());
            localTimeZone = TimeZone.getTimeZone(str);
            l = 0L;
            switch (paramInt) {
                default:
                    if (paramTime.hasActualTimeSecondsSinceEpoch()) {
                        l = paramTime.getActualTimeSecondsSinceEpoch();
                    }
                    break;
            }
        }
        while (l > 0L) {
            GregorianCalendar localGregorianCalendar = new GregorianCalendar(localTimeZone);
            localGregorianCalendar.setTimeInMillis(1000L * l);
            return localGregorianCalendar;
            str = "UTC";
            break label29;
            if (paramTime.hasScheduledTimeSecondsSinceEpoch()) {
                l = paramTime.getScheduledTimeSecondsSinceEpoch();
            }
        }
    }

    private View.OnClickListener getWebUrlClickHandler(final String paramString) {
        new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
                AgendaCard.this.getContext().startActivity(localIntent);
            }
        };
    }

    private void setLocationWithAddress(@Nullable AgendaProtos.AgendaItemLocation paramAgendaItemLocation, int paramInt1, int paramInt2, View paramView, Context paramContext) {
        if (paramAgendaItemLocation != null) {
            if (paramAgendaItemLocation.hasName()) {
                setVisibleWithText(paramView, paramInt1, paramAgendaItemLocation.getName());
            }
            setVisibleWithText(paramView, paramInt2, paramAgendaItemLocation.getAddress());
        }
    }

    private void setVisibleWithText(View paramView, int paramInt, String paramString) {
        TextView localTextView = (TextView) paramView.findViewById(paramInt);
        localTextView.setText(paramString);
        localTextView.setVisibility(0);
    }

    public boolean onBackPressed() {
        return (this.mAgendaCardList != null) && (this.mAgendaCardList.collapseAll());
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        this.mContainer = new LinearLayout(getContext());
        this.mContainer.setOrientation(1);
        return this.mContainer;
    }

    public void setAgenda(List<CalendarProtos.AgendaItem> paramList, boolean paramBoolean1, int paramInt, long paramLong1, long paramLong2, boolean paramBoolean2) {
        if (paramList.isEmpty()) {
            this.mContainer.addView(View.inflate(getContext(), 2130968590, null));
        }
        Object localObject1;
        Object localObject2;
        label167:
        label194:
        do {
            return;
            this.mAgendaCardList = new AgendaCardContainerView(getContext());
            this.mAgendaCardList.setAbstractCardView(this);
            this.mContainer.addView(this.mAgendaCardList);
            this.mAgendaCardList.setUseReverseOrder(paramBoolean2);
            this.mAgendaCardList.setBatchSize(paramInt);
            localObject1 = null;
            localObject2 = null;
            int i = AgendaTimeUtil.getJulianDay(paramLong1);
            int j = AgendaTimeUtil.getJulianDay(paramLong2);
            Iterator localIterator = paramList.iterator();
            if (localIterator.hasNext()) {
                CalendarProtos.AgendaItem localAgendaItem = (CalendarProtos.AgendaItem) localIterator.next();
                Pair localPair = getStartAndEndDay(localAgendaItem);
                int k = ((Integer) localPair.first).intValue();
                int m = ((Integer) localPair.second).intValue();
                int n = k;
                if (n <= m) {
                    if ((n >= i) && (n <= j)) {
                        break label194;
                    }
                }
                for (; ; ) {
                    n++;
                    break label167;
                    break;
                    localAgendaItem.setEventType(getEventTypeForDay(k, m, n));
                    AgendaExpandableItemView localAgendaExpandableItemView = createItemView(localAgendaItem);
                    this.mAgendaCardList.addItem(localAgendaExpandableItemView, n);
                    if (localObject1 == null) {
                        localObject1 = localAgendaExpandableItemView;
                    }
                    localObject2 = localAgendaExpandableItemView;
                }
            }
            if (this.mAgendaCardList.showMoreItems()) {
                View localView = View.inflate(getContext(), 2130968591, null);
                localView.findViewById(2131296316).setOnClickListener(this.mShowMoreButtonClickListener);
                this.mContainer.addView(localView);
            }
        } while (!paramBoolean1);
        AgendaCardContainerView localAgendaCardContainerView = this.mAgendaCardList;
        if (paramBoolean2) {
        }
        for (; ; ) {
            localAgendaCardContainerView.expand(localObject2);
            return;
            localObject2 = localObject1;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.AgendaCard

 * JD-Core Version:    0.7.0.1

 */