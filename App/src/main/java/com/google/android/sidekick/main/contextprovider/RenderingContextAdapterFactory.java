package com.google.android.sidekick.main.contextprovider;

import android.content.SharedPreferences;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.entry.EntryValidator;
import com.google.android.sidekick.main.entry.ValidatingBaseEntryAdapterFactory;
import com.google.android.sidekick.shared.util.CarRentalEntryUtil;
import com.google.common.base.Supplier;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.EventEntry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.MoonshineEventTicketEntry;
import com.google.geo.sidekick.Sidekick.MovieTicketEntry;
import com.google.geo.sidekick.Sidekick.NewsEntry;
import javax.annotation.Nullable;

public class RenderingContextAdapterFactory
  extends ValidatingBaseEntryAdapterFactory<EntryRenderingContextAdapter>
{
  private final CalendarDataProvider mCalendarDataProvider;
  private final Supplier<SharedPreferences> mMainPreferences;
  
  public RenderingContextAdapterFactory(CalendarDataProvider paramCalendarDataProvider, Supplier<SharedPreferences> paramSupplier, EntryValidator paramEntryValidator)
  {
    super(paramEntryValidator);
    this.mCalendarDataProvider = paramCalendarDataProvider;
    this.mMainPreferences = paramSupplier;
  }
  
  private Sidekick.CommuteSummary getRoute(Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    if (paramFrequentPlaceEntry.getRouteCount() > 0) {
      return paramFrequentPlaceEntry.getRoute(0);
    }
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createAlbumEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBarcodeEntry(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBirthdayCardEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBookEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBrowseModeEntityListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBrowseModeLureAuthorEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBrowseModeLureInterestUpdateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBrowseModeLureTravelEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBrowseModeVideoListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBrowseModeWebLinkEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createBusinessEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new LocationRenderingContextAdapter(paramFrequentPlaceEntry.getFrequentPlace().getLocation(), getRoute(paramFrequentPlaceEntry));
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createCalendarEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new CalendarEntryRenderingContextAdapter(paramEntry, this.mCalendarDataProvider);
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createCarRentalEntryAdapter(Sidekick.Entry paramEntry)
  {
    Sidekick.Location localLocation = CarRentalEntryUtil.getCarRentalLocation(paramEntry.getCarRentalEntry());
    if (localLocation != null) {
      return new LocationRenderingContextAdapter(localLocation, null);
    }
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createClockEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createConcertTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LocationRenderingContextAdapter(paramEntry.getMoonshineEventTicketEntry().getVenue(), null);
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createContactEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new LocationRenderingContextAdapter(paramFrequentPlaceEntry.getFrequentPlace().getLocation(), getRoute(paramFrequentPlaceEntry));
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createCurrencyExchangeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createEventEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LocationRenderingContextAdapter(paramEntry.getEventEntry().getLocation(), null);
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createFlightStatusEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new FlightStatusEntryRenderingContextAdapter(paramEntry.getFlightStatusEntry());
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createGenericCardEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createGenericPlaceEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new LocationRenderingContextAdapter(paramFrequentPlaceEntry.getFrequentPlace().getLocation(), getRoute(paramFrequentPlaceEntry));
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createGenericTvProgramEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createLastTrainHomeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LocationRenderingContextAdapter(paramEntry.getLastTrainHomeEntry().getFrequentPlace().getLocation(), null);
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createLocalAttractionsListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createLocationHistoryReminderEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LocationHistoryReminderContextAdapter();
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createMovieEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createMovieListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createMovieTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LocationRenderingContextAdapter(paramEntry.getMovieTicketEntry().getTheater(), null);
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createNearbyEventsEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createNearbyPlacesListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createNewsEntryAdapter(Sidekick.Entry paramEntry, Sidekick.NewsEntry paramNewsEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createPackageTrackingEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new PackageTrackingEntryRenderingContextAdapter(paramEntry.getPackageTrackingEntry());
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createPhotoSpotEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createPublicAlertEntry(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createRealEstateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createRealEstateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createRelevantWebsiteEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createReminderEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new ReminderEntryRenderingContextAdapter(this.mMainPreferences);
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createResearchTopicEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createReservationEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new LocationRenderingContextAdapter(paramFrequentPlaceEntry.getFrequentPlace().getLocation(), getRoute(paramFrequentPlaceEntry));
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createSharedTrafficCardEntry(Sidekick.Entry paramEntry)
  {
    return new SharedTrafficContextAdapter(paramEntry);
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createSportEventTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createSportsEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createStockListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createThingsToWatchEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createThingsToWatchLureEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createTransitEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createTranslateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createTvEpisodeEntry(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createTvKnowledgeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createTvKnowledgeListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createTvMusicEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createTvNewsEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createTvRecognitionEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createVideoGameEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createWalletLoyaltyEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createWalletOfferEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createWeatherEntryAdapter(Sidekick.Entry paramEntry)
  {
    return null;
  }
  
  @Nullable
  protected EntryRenderingContextAdapter createWebsiteUpdateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.RenderingContextAdapterFactory
 * JD-Core Version:    0.7.0.1
 */