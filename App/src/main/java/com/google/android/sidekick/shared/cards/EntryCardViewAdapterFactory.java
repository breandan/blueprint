package com.google.android.sidekick.shared.cards;

import android.net.wifi.WifiManager;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.IntentUtils;
import com.google.android.shared.util.IntentUtilsImpl;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.android.sidekick.shared.util.PlaceDataHelper;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.NewsEntry;
import javax.annotation.Nullable;

public class EntryCardViewAdapterFactory
  extends BaseEntryAdapterFactory<EntryCardViewAdapter>
{
  private final ActivityHelper mActivityHelper;
  private final Clock mClock;
  private final DirectionsLauncher mDirectionsLauncher;
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  private final IntentUtils mIntentUtils = new IntentUtilsImpl();
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final PlaceDataHelper mPlaceDataHelper;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  private final WifiManager mWifiManager;
  
  public EntryCardViewAdapterFactory(Clock paramClock, DirectionsLauncher paramDirectionsLauncher, WifiManager paramWifiManager, ActivityHelper paramActivityHelper, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mClock = paramClock;
    this.mDirectionsLauncher = paramDirectionsLauncher;
    this.mWifiManager = paramWifiManager;
    this.mActivityHelper = paramActivityHelper;
    this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
    this.mFifeImageUrlUtil = new FifeImageUrlUtil();
    this.mPlaceDataHelper = new PlaceDataHelper(this.mFifeImageUrlUtil);
    this.mPhotoWithAttributionDecorator = new PhotoWithAttributionDecorator(this.mFifeImageUrlUtil);
  }
  
  @Nullable
  protected EntryCardViewAdapter createAlbumEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new AlbumEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBarcodeEntry(Sidekick.Entry paramEntry)
  {
    return new BarcodeEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBirthdayCardEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new BirthdayCardEntryAdapter(paramEntry, this.mIntentUtils, this.mFifeImageUrlUtil, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBookEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new BookEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBrowseModeEntityListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new ResearchTopicEntryAdapter(paramEntryTreeNode, paramEntryTreeNode.getGroupEntry().getBrowseModeEntityListEntry(), this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBrowseModeLureAuthorEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new ResearchTopicEntryAdapter(paramEntryTreeNode, paramEntryTreeNode.getGroupEntry().getBrowseModeLureAuthorEntry(), this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBrowseModeLureInterestUpdateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new BrowseModeLureInterestUpdateEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBrowseModeLureTravelEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new BrowseModeLureTravelEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBrowseModeVideoListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new ResearchTopicEntryAdapter(paramEntryTreeNode, paramEntryTreeNode.getGroupEntry().getBrowseModeVideoListEntry(), this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBrowseModeWebLinkEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new BrowseModeWebLinkEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createBusinessEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new BusinessEntryAdapter(paramEntry, paramFrequentPlaceEntry, this.mActivityHelper, this.mIntentUtils, this.mPlaceDataHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createCalendarEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new CalendarEntryAdapter(paramEntry, this.mDirectionsLauncher, this.mActivityHelper, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createCarRentalEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new CarRentalEntryAdapter(paramEntry, this.mActivityHelper, this.mDirectionsLauncher, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createClockEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new ClockEntryAdapter(paramEntry, this.mActivityHelper, this.mClock);
  }
  
  protected EntryCardViewAdapter createConcertTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new ConcertTicketEntryAdapter(paramEntry, this.mDirectionsLauncher, this.mActivityHelper, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createContactEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new ContactEntryAdapter(paramEntry, paramFrequentPlaceEntry, this.mDirectionsLauncher, this.mActivityHelper, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createCurrencyExchangeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new CurrencyExchangeEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createEventEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new EventEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createFlightStatusEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new FlightStatusEntryAdapter(paramEntry, this.mDirectionsLauncher, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createGenericCardEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new GenericCardEntryAdapter(paramEntry, this.mWifiManager, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createGenericPlaceEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new GenericPlaceEntryAdapter(paramEntry, paramFrequentPlaceEntry, this.mDirectionsLauncher, this.mActivityHelper, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createGenericTvProgramEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new GenericTvProgramEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createLastTrainHomeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LastTrainHomeEntryAdapter(paramEntry, this.mActivityHelper, this.mClock, this.mDirectionsLauncher);
  }
  
  @Nullable
  protected EntryCardViewAdapter createLocalAttractionsListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LocalAttractionsListEntryAdapter(paramEntry, this.mIntentUtils, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createLocationHistoryReminderEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new LocationHistoryReminderEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createMovieEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new MovieEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createMovieListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new MovieListEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createMovieTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new MovieTicketEntryAdapter(paramEntry, this.mDirectionsLauncher, this.mActivityHelper, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createNearbyEventsEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new NearbyEventsEntryAdapter(paramEntryTreeNode, this.mFifeImageUrlUtil, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createNearbyPlacesListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new NearbyPlacesListEntryAdapter(paramEntryTreeNode, this.mIntentUtils, this.mPlaceDataHelper, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createNewsEntryAdapter(Sidekick.Entry paramEntry, Sidekick.NewsEntry paramNewsEntry)
  {
    return new NewsEntryAdapter(paramEntry, paramNewsEntry, this.mClock, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createPackageTrackingEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new PackageTrackingEntryAdapter(paramEntry, this.mIntentUtils, this.mFifeImageUrlUtil, this.mDirectionsLauncher, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createPhotoSpotEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new PhotoSpotEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createPublicAlertEntry(Sidekick.Entry paramEntry)
  {
    return new PublicAlertEntryAdapter(paramEntry, this.mActivityHelper, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createRealEstateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new RealEstateEntryAdapter(paramEntry, this.mClock, this.mFifeImageUrlUtil, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createRealEstateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new RealEstateEntryAdapter(paramEntryTreeNode, this.mClock, this.mFifeImageUrlUtil, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createRelevantWebsiteEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new RelevantWebsiteEntryAdapter(paramEntryTreeNode, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createReminderEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new ReminderEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createResearchTopicEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new ResearchTopicEntryAdapter(paramEntryTreeNode, paramEntryTreeNode.getGroupEntry().getResearchTopicEntry(), this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createReservationEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    return new ReservationEntryAdapter(paramEntry, paramFrequentPlaceEntry, this.mIntentUtils, this.mDirectionsLauncher, this.mActivityHelper, this.mClock, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createSharedTrafficCardEntry(Sidekick.Entry paramEntry)
  {
    return new SharedTrafficEntryAdapter(paramEntry, this.mClock, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createSportEventTicketEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new SportEventTicketEntryAdapter(paramEntry, this.mDirectionsLauncher, this.mActivityHelper, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createSportsEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new SportsEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createStockListEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new StockListEntryAdapter(paramEntry, this.mClock, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createThingsToWatchEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new ThingsToWatchEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createThingsToWatchLureEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new ThingsToWatchLureEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createTransitEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new TransitEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createTranslateEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new TranslateEntryAdapter(paramEntry, this.mActivityHelper, this.mUiThreadExecutor);
  }
  
  @Nullable
  protected EntryCardViewAdapter createTvEpisodeEntry(Sidekick.Entry paramEntry)
  {
    return new TvEpisodeEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createTvKnowledgeEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new TvKnowledgeEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createTvKnowledgeListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new TvKnowledgeListEntryAdapter(paramEntryTreeNode, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createTvMusicEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new TvMusicEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createTvNewsEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new TvNewsEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator, this.mClock);
  }
  
  @Nullable
  protected EntryCardViewAdapter createTvRecognitionEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new TvRecognitionEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createVideoGameEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new VideoGameEntryAdapter(paramEntry, this.mActivityHelper, this.mPhotoWithAttributionDecorator);
  }
  
  @Nullable
  protected EntryCardViewAdapter createWalletLoyaltyEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new WalletLoyaltyEntryAdapter(paramEntry, this.mFifeImageUrlUtil, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createWalletOfferEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new WalletOfferEntryAdapter(paramEntry, this.mActivityHelper, this.mClock, this.mFifeImageUrlUtil);
  }
  
  @Nullable
  protected EntryCardViewAdapter createWeatherEntryAdapter(Sidekick.Entry paramEntry)
  {
    return new WeatherEntryAdapter(paramEntry, this.mActivityHelper);
  }
  
  @Nullable
  protected EntryCardViewAdapter createWebsiteUpdateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return new WebsiteUpdateEntryAdapter(paramEntryTreeNode, this.mActivityHelper, this.mClock);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.EntryCardViewAdapterFactory
 * JD-Core Version:    0.7.0.1
 */