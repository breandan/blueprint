package com.google.android.sidekick.main.entry;

import android.content.Context;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.shared.cards.BaseEntryAdapterFactory;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.BusinessData;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.GenericCardEntry;
import com.google.geo.sidekick.Sidekick.NearbyPlacesListEntry;
import com.google.geo.sidekick.Sidekick.NewsEntry;
import com.google.geo.sidekick.Sidekick.PlaceData;
import com.google.geo.sidekick.Sidekick.SharedTrafficCardEntry;
import com.google.geo.sidekick.Sidekick.SportScoreEntry;
import com.google.geo.sidekick.Sidekick.TransitStationEntry;
import com.google.geo.sidekick.Sidekick.TvNewsEntry;
import com.google.geo.sidekick.Sidekick.WeatherEntry;
import java.util.Set;

public class EntryValidator
  extends BaseEntryAdapterFactory<Boolean>
{
  static final Set<Integer> SUPPORTED_SPORTS;
  private final CalendarDataProvider mCalendarDataProvider;
  private final Context mContext;
  
  static
  {
    Integer[] arrayOfInteger = new Integer[6];
    arrayOfInteger[0] = Integer.valueOf(2);
    arrayOfInteger[1] = Integer.valueOf(0);
    arrayOfInteger[2] = Integer.valueOf(1);
    arrayOfInteger[3] = Integer.valueOf(3);
    arrayOfInteger[4] = Integer.valueOf(4);
    arrayOfInteger[5] = Integer.valueOf(5);
    SUPPORTED_SPORTS = Sets.newHashSet(arrayOfInteger);
  }
  
  public EntryValidator(CalendarDataProvider paramCalendarDataProvider, Context paramContext)
  {
    this.mCalendarDataProvider = paramCalendarDataProvider;
    this.mContext = paramContext;
  }
  
  protected Boolean createAlbumEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createBarcodeEntry(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createBirthdayCardEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createBookEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createBrowseModeEntityListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if ((paramEntryTreeNode.hasGroupEntry()) && (paramEntryTreeNode.getGroupEntry().hasBrowseModeEntityListEntry())) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createBrowseModeLureAuthorEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if ((paramEntryTreeNode.hasGroupEntry()) && (paramEntryTreeNode.getGroupEntry().hasBrowseModeLureAuthorEntry())) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createBrowseModeLureInterestUpdateEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.Action localAction = ProtoUtils.findAction(paramEntry, 160, new int[0]);
    boolean bool1 = false;
    if (localAction != null)
    {
      boolean bool2 = localAction.hasInterest();
      bool1 = false;
      if (bool2) {
        bool1 = true;
      }
    }
    return Boolean.valueOf(bool1);
  }
  
  protected Boolean createBrowseModeLureTravelEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.Action localAction = ProtoUtils.findAction(paramEntry, 161, new int[0]);
    boolean bool1 = false;
    if (localAction != null)
    {
      boolean bool2 = localAction.hasInterest();
      bool1 = false;
      if (bool2) {
        bool1 = true;
      }
    }
    return Boolean.valueOf(bool1);
  }
  
  protected Boolean createBrowseModeVideoListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if ((paramEntryTreeNode.hasGroupEntry()) && (paramEntryTreeNode.getGroupEntry().hasBrowseModeVideoListEntry())) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createBrowseModeWebLinkEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createBusinessEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    Sidekick.FrequentPlace localFrequentPlace = paramFrequentPlaceEntry.getFrequentPlace();
    if ((localFrequentPlace.hasPlaceData()) && (localFrequentPlace.getPlaceData().hasBusinessData()) && (localFrequentPlace.getPlaceData().getBusinessData().hasCid())) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createCalendarEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.CalendarEntry localCalendarEntry = paramEntry.getCalendarEntry();
    if ((this.mCalendarDataProvider.getCalendarDataByServerHash(localCalendarEntry.getHash()) != null) || (paramEntry.getIsExample())) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createCarRentalEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createClockEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createConcertTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createContactEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createCurrencyExchangeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createEventEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createFlightStatusEntryAdapter(Sidekick.Entry paramEntry)
  {
    if (paramEntry.getFlightStatusEntry().getFlightCount() > 0) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createGenericCardEntryAdapter(Sidekick.Entry paramEntry)
  {
    boolean bool = true;
    if ("Wifi".equals(paramEntry.getGenericCardEntry().getCardType()))
    {
      if (Build.VERSION.SDK_INT > 16)
      {
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 0) {}
        for (;;)
        {
          return Boolean.valueOf(bool);
          bool = false;
        }
      }
      if (Settings.System.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 0) {}
      for (;;)
      {
        return Boolean.valueOf(bool);
        bool = false;
      }
    }
    return Boolean.valueOf(bool);
  }
  
  protected Boolean createGenericPlaceEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createGenericTvProgramEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createLastTrainHomeEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = paramEntry.getLastTrainHomeEntry();
    if (localFrequentPlaceEntry.getRouteCount() == 0) {
      return Boolean.valueOf(false);
    }
    return Boolean.valueOf(localFrequentPlaceEntry.getRoute(0).hasTransitDetails());
  }
  
  protected Boolean createLocalAttractionsListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createLocationHistoryReminderEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createMovieEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createMovieListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createMovieTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createNearbyEventsEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if (paramEntryTreeNode.getEntryCount() > 0) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createNearbyPlacesListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    Sidekick.Entry localEntry = paramEntryTreeNode.getGroupEntry();
    if ((localEntry == null) || (!localEntry.hasNearbyPlacesListEntry())) {
      return Boolean.valueOf(false);
    }
    if (!localEntry.getNearbyPlacesListEntry().hasTitle()) {
      return Boolean.valueOf(false);
    }
    int i = paramEntryTreeNode.getEntryCount();
    boolean bool = false;
    if (i > 0) {
      bool = true;
    }
    return Boolean.valueOf(bool);
  }
  
  protected Boolean createNewsEntryAdapter(Sidekick.Entry paramEntry, Sidekick.NewsEntry paramNewsEntry)
  {
    return Boolean.valueOf(paramNewsEntry.hasTitle());
  }
  
  protected Boolean createPackageTrackingEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createPhotoSpotEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createPublicAlertEntry(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createRealEstateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createRealEstateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if (paramEntryTreeNode.getEntryCount() > 0) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createRelevantWebsiteEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createReminderEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createResearchTopicEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if ((paramEntryTreeNode.hasGroupEntry()) && (paramEntryTreeNode.getGroupEntry().hasResearchTopicEntry())) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createReservationEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createSharedTrafficCardEntry(Sidekick.Entry paramEntry)
  {
    if (!Strings.isNullOrEmpty(paramEntry.getSharedTrafficCardEntry().getSharerName())) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createSportEventTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createSportsEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.SportScoreEntry localSportScoreEntry = paramEntry.getSportScoreEntry();
    if (localSportScoreEntry.getSportEntityCount() != 2) {
      return Boolean.valueOf(false);
    }
    if (!SUPPORTED_SPORTS.contains(Integer.valueOf(localSportScoreEntry.getSport()))) {
      return Boolean.valueOf(false);
    }
    int i = localSportScoreEntry.getStatusCode();
    boolean bool;
    if ((i != 2) && (i != 1))
    {
      bool = false;
      if (i != 0) {}
    }
    else
    {
      bool = true;
    }
    return Boolean.valueOf(bool);
  }
  
  protected Boolean createStockListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createThingsToWatchEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(paramEntry.hasThingsToWatchEntry());
  }
  
  protected Boolean createThingsToWatchLureEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.Action localAction = ProtoUtils.findAction(paramEntry, 162, new int[0]);
    boolean bool1 = false;
    if (localAction != null)
    {
      boolean bool2 = localAction.hasInterest();
      bool1 = false;
      if (bool2) {
        bool1 = true;
      }
    }
    return Boolean.valueOf(bool1);
  }
  
  protected Boolean createTransitEntryAdapter(Sidekick.Entry paramEntry)
  {
    if (paramEntry.getTransitStationEntry().getLineCount() > 0) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createTranslateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createTvEpisodeEntry(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createTvKnowledgeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createTvKnowledgeListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if (paramEntryTreeNode.getEntryCount() > 0) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createTvMusicEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createTvNewsEntryAdapter(Sidekick.Entry paramEntry)
  {
    if ((paramEntry.hasTvNewsEntry()) && (paramEntry.getTvNewsEntry().hasNewsEntry())) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createTvRecognitionEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createVideoGameEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createWalletLoyaltyEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createWalletOfferEntryAdapter(Sidekick.Entry paramEntry)
  {
    return Boolean.valueOf(true);
  }
  
  protected Boolean createWeatherEntryAdapter(Sidekick.Entry paramEntry)
  {
    if (paramEntry.getWeatherEntry().getWeatherPointCount() > 0) {}
    for (boolean bool = true;; bool = false) {
      return Boolean.valueOf(bool);
    }
  }
  
  protected Boolean createWebsiteUpdateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return Boolean.valueOf(true);
  }
  
  public boolean validate(Sidekick.Entry paramEntry)
  {
    Boolean localBoolean = (Boolean)super.create(paramEntry);
    if (localBoolean == null) {
      return false;
    }
    return localBoolean.booleanValue();
  }
  
  public boolean validateGroup(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    Boolean localBoolean = (Boolean)super.createForGroup(paramEntryTreeNode);
    if (localBoolean == null) {
      return false;
    }
    return localBoolean.booleanValue();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryValidator
 * JD-Core Version:    0.7.0.1
 */