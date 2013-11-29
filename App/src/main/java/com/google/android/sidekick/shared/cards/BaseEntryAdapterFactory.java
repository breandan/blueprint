package com.google.android.sidekick.shared.cards;

import android.util.Log;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.NewsEntry;
import com.google.geo.sidekick.Sidekick.PlaceData;
import javax.annotation.Nullable;

public abstract class BaseEntryAdapterFactory<T>
  implements EntryAdapterFactory<T>
{
  private static final String TAG = Tag.getTag(BaseEntryAdapterFactory.class);
  
  private T createPlaceEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    if ((paramFrequentPlaceEntry.hasFrequentPlace()) && (paramFrequentPlaceEntry.getFrequentPlace().hasSourceType()))
    {
      int m = paramFrequentPlaceEntry.getFrequentPlace().getSourceType();
      if ((paramFrequentPlaceEntry.getEventType() != 7) && ((m == 6) || (m == 8) || (m == 7))) {
        return createReservationEntryAdapter(paramEntry, paramFrequentPlaceEntry);
      }
    }
    Sidekick.PlaceData localPlaceData = PlaceUtils.getPlaceDataFromEntry(paramFrequentPlaceEntry);
    if (localPlaceData != null)
    {
      if (localPlaceData.hasContactData()) {
        return createContactEntryAdapter(paramEntry, paramFrequentPlaceEntry);
      }
      if (localPlaceData.hasBusinessData())
      {
        boolean bool = paramFrequentPlaceEntry.hasFrequentPlace();
        int i = 0;
        int j;
        if (bool)
        {
          j = paramFrequentPlaceEntry.getFrequentPlace().getSourceType();
          if ((j != 3) && (j != 11)) {
            break label144;
          }
          i = 1;
        }
        while (i != 0)
        {
          return createBusinessEntryAdapter(paramEntry, paramFrequentPlaceEntry);
          label144:
          i = 0;
          if (j == 6)
          {
            int k = paramFrequentPlaceEntry.getEventType();
            i = 0;
            if (k == 7) {
              i = 1;
            }
          }
        }
      }
    }
    return createGenericPlaceEntryAdapter(paramEntry, paramFrequentPlaceEntry);
  }
  
  @Nullable
  public T create(Sidekick.Entry paramEntry)
  {
    if (paramEntry.hasFrequentPlaceEntry()) {
      return createPlaceEntryAdapter(paramEntry, paramEntry.getFrequentPlaceEntry());
    }
    if (paramEntry.hasHotelPlaceEntry()) {
      return createReservationEntryAdapter(paramEntry, paramEntry.getHotelPlaceEntry());
    }
    if (paramEntry.hasRestaurantPlaceEntry()) {
      return createReservationEntryAdapter(paramEntry, paramEntry.getRestaurantPlaceEntry());
    }
    if (paramEntry.hasNearbyPlaceEntry()) {
      return createPlaceEntryAdapter(paramEntry, paramEntry.getNearbyPlaceEntry());
    }
    if (paramEntry.hasSharedTrafficCardEntry()) {
      return createSharedTrafficCardEntry(paramEntry);
    }
    if (paramEntry.hasCalendarEntry()) {
      return createCalendarEntryAdapter(paramEntry);
    }
    if (paramEntry.hasWeatherEntry()) {
      return createWeatherEntryAdapter(paramEntry);
    }
    if (paramEntry.hasTransitStationEntry()) {
      return createTransitEntryAdapter(paramEntry);
    }
    if (paramEntry.hasGenericCardEntry()) {
      return createGenericCardEntryAdapter(paramEntry);
    }
    if (paramEntry.hasFlightStatusEntry()) {
      return createFlightStatusEntryAdapter(paramEntry);
    }
    if (paramEntry.hasSportScoreEntry()) {
      return createSportsEntryAdapter(paramEntry);
    }
    if (paramEntry.hasTranslateEntry()) {
      return createTranslateEntryAdapter(paramEntry);
    }
    if (paramEntry.hasCurrencyExchangeEntry()) {
      return createCurrencyExchangeEntryAdapter(paramEntry);
    }
    if (paramEntry.hasClockEntry()) {
      return createClockEntryAdapter(paramEntry);
    }
    if (paramEntry.hasPublicAlertEntry()) {
      return createPublicAlertEntry(paramEntry);
    }
    if (paramEntry.hasMovieListEntry()) {
      return createMovieListEntryAdapter(paramEntry);
    }
    if (paramEntry.hasStockQuoteListEntry()) {
      return createStockListEntryAdapter(paramEntry);
    }
    if (paramEntry.hasAttractionListEntry()) {
      return createLocalAttractionsListEntryAdapter(paramEntry);
    }
    if (paramEntry.hasPackageTrackingEntry()) {
      return createPackageTrackingEntryAdapter(paramEntry);
    }
    if (paramEntry.hasNewsEntry()) {
      return createNewsEntryAdapter(paramEntry, paramEntry.getNewsEntry());
    }
    if (paramEntry.hasBreakingNewsEntry()) {
      return createNewsEntryAdapter(paramEntry, paramEntry.getBreakingNewsEntry());
    }
    if (paramEntry.hasHyperlocalNewsEntry()) {
      return createNewsEntryAdapter(paramEntry, paramEntry.getHyperlocalNewsEntry());
    }
    if (paramEntry.hasEntityNewsEntry()) {
      return createNewsEntryAdapter(paramEntry, paramEntry.getEntityNewsEntry());
    }
    if (paramEntry.hasPersonalizedNewsEntry()) {
      return createNewsEntryAdapter(paramEntry, paramEntry.getPersonalizedNewsEntry());
    }
    if (paramEntry.hasPhotoSpotEntry()) {
      return createPhotoSpotEntryAdapter(paramEntry);
    }
    if (paramEntry.hasLocationHistoryReminderEntry()) {
      return createLocationHistoryReminderEntryAdapter(paramEntry);
    }
    if (paramEntry.hasBirthdayCardEntry()) {
      return createBirthdayCardEntryAdapter(paramEntry);
    }
    if (paramEntry.hasEventEntry()) {
      return createEventEntryAdapter(paramEntry);
    }
    if (paramEntry.hasMovieEntry()) {
      return createMovieEntryAdapter(paramEntry);
    }
    if (paramEntry.hasBookEntry()) {
      return createBookEntryAdapter(paramEntry);
    }
    if (paramEntry.hasAlbumEntry()) {
      return createAlbumEntryAdapter(paramEntry);
    }
    if (paramEntry.hasTvEpisodeEntry()) {
      return createTvEpisodeEntry(paramEntry);
    }
    if (paramEntry.hasVideoGameEntry()) {
      return createVideoGameEntryAdapter(paramEntry);
    }
    if (paramEntry.hasBarcodeEntry()) {
      return createBarcodeEntry(paramEntry);
    }
    if (paramEntry.hasMovieTicketEntry()) {
      return createMovieTicketEntryAdapter(paramEntry);
    }
    if (paramEntry.hasReminderEntry()) {
      return createReminderEntryAdapter(paramEntry);
    }
    if (paramEntry.getType() == 41) {
      return createRealEstateEntryAdapter(paramEntry);
    }
    if (paramEntry.hasLastTrainHomeEntry()) {
      return createLastTrainHomeEntryAdapter(paramEntry);
    }
    if (paramEntry.hasWalletOfferEntry()) {
      return createWalletOfferEntryAdapter(paramEntry);
    }
    if (paramEntry.hasWalletLoyaltyEntry()) {
      return createWalletLoyaltyEntryAdapter(paramEntry);
    }
    if (paramEntry.hasTvRecognitionEntry()) {
      return createTvRecognitionEntryAdapter(paramEntry);
    }
    if (paramEntry.hasGenericTvProgramEntry()) {
      return createGenericTvProgramEntryAdapter(paramEntry);
    }
    if ((paramEntry.hasMoonshineEventTicketEntry()) && (paramEntry.getType() == 66)) {
      return createConcertTicketEntryAdapter(paramEntry);
    }
    if (paramEntry.hasTvKnowledgeEntry()) {
      return createTvKnowledgeEntryAdapter(paramEntry);
    }
    if (paramEntry.hasTvMusicEntry()) {
      return createTvMusicEntryAdapter(paramEntry);
    }
    if (paramEntry.hasTvNewsEntry()) {
      return createTvNewsEntryAdapter(paramEntry);
    }
    if (paramEntry.hasCarRentalEntry()) {
      return createCarRentalEntryAdapter(paramEntry);
    }
    if ((paramEntry.hasMoonshineEventTicketEntry()) && (paramEntry.getType() == 72)) {
      return createSportEventTicketEntryAdapter(paramEntry);
    }
    if (paramEntry.hasBrowseModeLureInterestUpdateEntry()) {
      return createBrowseModeLureInterestUpdateEntryAdapter(paramEntry);
    }
    if (paramEntry.hasBrowseModeWebLinkEntry()) {
      return createBrowseModeWebLinkEntryAdapter(paramEntry);
    }
    if (paramEntry.hasBrowseModeLureTravelEntry()) {
      return createBrowseModeLureTravelEntryAdapter(paramEntry);
    }
    if (paramEntry.hasThingsToWatchEntry()) {
      return createThingsToWatchEntryAdapter(paramEntry);
    }
    if (paramEntry.hasThingsToWatchLureEntry()) {
      return createThingsToWatchLureEntryAdapter(paramEntry);
    }
    return null;
  }
  
  @Nullable
  protected abstract T createAlbumEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createBarcodeEntry(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createBirthdayCardEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createBookEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createBrowseModeEntityListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createBrowseModeLureAuthorEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createBrowseModeLureInterestUpdateEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createBrowseModeLureTravelEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createBrowseModeVideoListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createBrowseModeWebLinkEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createBusinessEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry);
  
  @Nullable
  protected abstract T createCalendarEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createCarRentalEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createClockEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createConcertTicketEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createContactEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry);
  
  @Nullable
  protected abstract T createCurrencyExchangeEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createEventEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createFlightStatusEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  public T createForGroup(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if (!paramEntryTreeNode.hasGroupEntry()) {
      if (paramEntryTreeNode.getEntryCount() > 0)
      {
        int i = paramEntryTreeNode.getEntry(0).getType();
        Log.w(TAG, "Skipping EntryTreeNode with no group entry, and child of type: " + i);
      }
    }
    do
    {
      return null;
      Log.w(TAG, "Skipping EntryTreeNode with no group entry and no children");
      return null;
      if (paramEntryTreeNode.getGroupEntry().getType() == 32) {
        return createResearchTopicEntryAdapter(paramEntryTreeNode);
      }
      if (paramEntryTreeNode.getGroupEntry().getType() == 31) {
        return createNearbyEventsEntryAdapter(paramEntryTreeNode);
      }
      if (paramEntryTreeNode.getGroupEntry().getType() == 38) {
        return createNearbyPlacesListEntryAdapter(paramEntryTreeNode);
      }
      if (paramEntryTreeNode.getGroupEntry().getType() == 40) {
        return createRealEstateEntryAdapter(paramEntryTreeNode);
      }
      if (paramEntryTreeNode.getGroupEntry().getType() == 46) {
        return createWebsiteUpdateEntryAdapter(paramEntryTreeNode);
      }
      if (paramEntryTreeNode.getGroupEntry().getType() == 55) {
        return createRelevantWebsiteEntryAdapter(paramEntryTreeNode);
      }
      if (paramEntryTreeNode.getGroupEntry().getType() == 71) {
        return createTvKnowledgeListEntryAdapter(paramEntryTreeNode);
      }
      if (paramEntryTreeNode.getGroupEntry().getType() == 77) {
        return createBrowseModeLureAuthorEntryAdapter(paramEntryTreeNode);
      }
      if (paramEntryTreeNode.getGroupEntry().getType() == 80) {
        return createBrowseModeEntityListEntryAdapter(paramEntryTreeNode);
      }
    } while (paramEntryTreeNode.getGroupEntry().getType() != 82);
    return createBrowseModeVideoListEntryAdapter(paramEntryTreeNode);
  }
  
  @Nullable
  protected abstract T createGenericCardEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createGenericPlaceEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry);
  
  @Nullable
  protected abstract T createGenericTvProgramEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createLastTrainHomeEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createLocalAttractionsListEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createLocationHistoryReminderEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createMovieEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createMovieListEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createMovieTicketEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createNearbyEventsEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createNearbyPlacesListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createNewsEntryAdapter(Sidekick.Entry paramEntry, Sidekick.NewsEntry paramNewsEntry);
  
  @Nullable
  protected abstract T createPackageTrackingEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createPhotoSpotEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createPublicAlertEntry(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createRealEstateEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createRealEstateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createRelevantWebsiteEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createReminderEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createResearchTopicEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createReservationEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry);
  
  @Nullable
  protected abstract T createSharedTrafficCardEntry(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createSportEventTicketEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createSportsEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createStockListEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createThingsToWatchEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createThingsToWatchLureEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createTransitEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createTranslateEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createTvEpisodeEntry(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createTvKnowledgeEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createTvKnowledgeListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
  
  @Nullable
  protected abstract T createTvMusicEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createTvNewsEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createTvRecognitionEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createVideoGameEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createWalletLoyaltyEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createWalletOfferEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createWeatherEntryAdapter(Sidekick.Entry paramEntry);
  
  @Nullable
  protected abstract T createWebsiteUpdateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BaseEntryAdapterFactory
 * JD-Core Version:    0.7.0.1
 */