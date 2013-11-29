package com.google.android.sidekick.main.notifications;

import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.actions.ReminderSmartActionUtil;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.entry.EntryValidator;
import com.google.android.sidekick.main.entry.ValidatingBaseEntryAdapterFactory;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.shared.util.CalendarDataUtil;
import com.google.android.sidekick.shared.util.CarRentalEntryUtil;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.RelevantFlight;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.NewsEntry;
import com.google.geo.sidekick.Sidekick.Notification;
import javax.annotation.Nullable;

public class EntryNotificationFactory
  extends ValidatingBaseEntryAdapterFactory<EntryNotification>
{
  private final CalendarDataProvider mCalendarDataProvider;
  private final Clock mClock;
  private final DirectionsLauncher mDirectionsLauncher;
  private final LocationOracle mLocationOracle;
  private final ReminderSmartActionUtil mReminderSmartActionUtil;
  
  public EntryNotificationFactory(LocationOracle paramLocationOracle, CalendarDataProvider paramCalendarDataProvider, DirectionsLauncher paramDirectionsLauncher, Clock paramClock, EntryValidator paramEntryValidator, ReminderSmartActionUtil paramReminderSmartActionUtil)
  {
    super(paramEntryValidator);
    this.mLocationOracle = paramLocationOracle;
    this.mCalendarDataProvider = paramCalendarDataProvider;
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mClock = paramClock;
    this.mReminderSmartActionUtil = paramReminderSmartActionUtil;
  }
  
  private Sidekick.Location getCurrentSidekickLocation()
  {
    return LocationUtilities.androidLocationToSidekickLocation(this.mLocationOracle.getBestLocation());
  }
  
  @Nullable
  public EntryNotification create(Sidekick.Entry paramEntry)
  {
    EntryNotification localEntryNotification;
    if (!isValid(paramEntry)) {
      localEntryNotification = null;
    }
    do
    {
      return localEntryNotification;
      if ((!paramEntry.hasNotification()) || (!paramEntry.getNotification().hasType())) {
        return null;
      }
      localEntryNotification = (EntryNotification)super.create(paramEntry);
    } while (localEntryNotification != null);
    if (paramEntry.getNotification().getType() == 4) {
      return new GenericLowPriorityNotification(paramEntry, 2130903043);
    }
    return null;
  }
  
  @Nullable
  protected EntryNotification createAlbumEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  protected EntryNotification createBarcodeEntry(Sidekick.Entry paramEntry)
  {
    return new BarcodeNotification(paramEntry);
  }
  
  @Nullable
  protected EntryNotification createBirthdayCardEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createBookEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createBrowseModeEntityListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createBrowseModeLureAuthorEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createBrowseModeLureInterestUpdateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createBrowseModeLureTravelEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createBrowseModeVideoListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createBrowseModeWebLinkEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createBusinessEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return null;
  }
  
  protected EntryNotification createCalendarEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.CalendarEntry localCalendarEntry = paramEntry.getCalendarEntry();
    Sidekick.Location localLocation = CalendarDataUtil.getCalendarLocation(localCalendarEntry, this.mCalendarDataProvider.getCalendarDataByServerHash(localCalendarEntry.getHash()));
    return new CalendarNotification(paramEntry, this.mCalendarDataProvider, getCurrentSidekickLocation(), localLocation, this.mDirectionsLauncher);
  }
  
  protected EntryNotification createCarRentalEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new CarRentalNotification(paramEntry, getCurrentSidekickLocation(), CarRentalEntryUtil.getCarRentalLocation(paramEntry.getCarRentalEntry()), this.mDirectionsLauncher);
  }
  
  @Nullable
  protected EntryNotification createClockEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  protected EntryNotification createConcertTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new MoonshineEventTicketNotification(paramEntry);
  }
  
  protected EntryNotification createContactEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new TrafficNotification(paramEntry, getCurrentSidekickLocation(), this.mDirectionsLauncher);
  }
  
  @Nullable
  protected EntryNotification createCurrencyExchangeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createEventEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createFlightStatusEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.Notification localNotification = paramEntry.getNotification();
    if (localNotification.hasType())
    {
      if (localNotification.getType() == 3) {
        return new FlightTimeToLeaveForNotification(paramEntry, getCurrentSidekickLocation(), this.mDirectionsLauncher);
      }
      return new FlightStatusNotification(paramEntry, RelevantFlight.fromFlightStatusEntry(paramEntry.getFlightStatusEntry(), this.mClock.currentTimeMillis()));
    }
    return null;
  }
  
  @Nullable
  protected EntryNotification createGenericCardEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  protected EntryNotification createGenericPlaceEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new TrafficNotification(paramEntry, getCurrentSidekickLocation(), this.mDirectionsLauncher);
  }
  
  @Nullable
  protected EntryNotification createGenericTvProgramEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  protected EntryNotification createLastTrainHomeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LastTrainHomeNotification(paramEntry, this.mClock, getCurrentSidekickLocation(), this.mDirectionsLauncher);
  }
  
  @Nullable
  protected EntryNotification createLocalAttractionsListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createLocationHistoryReminderEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createMovieEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createMovieListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  protected EntryNotification createMovieTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new MovieTicketNotification(paramEntry);
  }
  
  @Nullable
  protected EntryNotification createNearbyEventsEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createNearbyPlacesListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createNewsEntryAdapter(Sidekick.Entry paramEntry, Sidekick.NewsEntry paramNewsEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createPackageTrackingEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createPhotoSpotEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  protected EntryNotification createPublicAlertEntry(Sidekick.Entry paramEntry)
  {
    return new PublicAlertNotification(paramEntry);
  }
  
  @Nullable
  protected EntryNotification createRealEstateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createRealEstateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createRelevantWebsiteEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  protected EntryNotification createReminderEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new ReminderNotification(paramEntry, this.mClock, this.mReminderSmartActionUtil);
  }
  
  @Nullable
  protected EntryNotification createResearchTopicEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  protected EntryNotification createReservationEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new TimeToLeaveNotification(paramEntry, getCurrentSidekickLocation(), this.mDirectionsLauncher);
  }
  
  @Nullable
  protected EntryNotification createSharedTrafficCardEntry(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  protected EntryNotification createSportEventTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new MoonshineEventTicketNotification(paramEntry);
  }
  
  protected EntryNotification createSportsEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new SportsNotification(paramEntry);
  }
  
  @Nullable
  protected EntryNotification createStockListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createThingsToWatchEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createThingsToWatchLureEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createTransitEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createTranslateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createTvEpisodeEntry(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createTvKnowledgeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createTvKnowledgeListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createTvMusicEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createTvNewsEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createTvRecognitionEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createVideoGameEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createWalletLoyaltyEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createWalletOfferEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryNotification createWeatherEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new WeatherNotification(paramEntry);
  }
  
  @Nullable
  protected EntryNotification createWebsiteUpdateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.EntryNotificationFactory
 * JD-Core Version:    0.7.0.1
 */