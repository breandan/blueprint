package com.google.android.sidekick.main.remoteservice;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.search.core.GooglePlayServicesHelper;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.shared.api.Query;
import com.google.android.sidekick.main.contextprovider.RenderingContextPopulator;
import com.google.android.sidekick.main.entry.EntriesRefreshIntentService;
import com.google.android.sidekick.main.entry.EntryInvalidator;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.entry.EntryTreePruner;
import com.google.android.sidekick.main.inject.BackgroundImage;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.remoteapi.CardsResponse;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.presenter.FirstUseCardHandler;
import com.google.android.velvet.presenter.FirstUseCardHandler.FirstUseCardType;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.List;
import javax.annotation.Nullable;

public class RemoteServiceHelper
{
  private static final String TAG = Tag.getTag(RemoteServiceHelper.class);
  private final FirstUseCardHandler mCardDismissalHandler;
  private final EntryInvalidator mEntryInvalidator;
  private final EntryProvider mEntryProvider;
  private final EntryTreePruner mEntryTreePruner;
  private final GooglePlayServicesHelper mGooglePlayServicesHelper;
  private final LocationOracle mLocationOracle;
  private final NowOptInSettings mOptInSettings;
  private final RenderingContextPopulator mRenderingContextPopulator;
  private final SearchUrlHelper mSearchUrlHelper;
  
  public RemoteServiceHelper(EntryProvider paramEntryProvider, EntryInvalidator paramEntryInvalidator, LocationOracle paramLocationOracle, NowOptInSettings paramNowOptInSettings, GooglePlayServicesHelper paramGooglePlayServicesHelper, RenderingContextPopulator paramRenderingContextPopulator, SearchUrlHelper paramSearchUrlHelper, EntryTreePruner paramEntryTreePruner, FirstUseCardHandler paramFirstUseCardHandler)
  {
    this.mEntryProvider = paramEntryProvider;
    this.mEntryInvalidator = paramEntryInvalidator;
    this.mLocationOracle = paramLocationOracle;
    this.mOptInSettings = paramNowOptInSettings;
    this.mGooglePlayServicesHelper = paramGooglePlayServicesHelper;
    this.mRenderingContextPopulator = paramRenderingContextPopulator;
    this.mSearchUrlHelper = paramSearchUrlHelper;
    this.mEntryTreePruner = paramEntryTreePruner;
    this.mCardDismissalHandler = paramFirstUseCardHandler;
  }
  
  @Nullable
  private String getContextImageQuery(BackgroundImage paramBackgroundImage)
  {
    if (!paramBackgroundImage.isDoodle()) {}
    Query localQuery;
    do
    {
      Sidekick.Photo localPhoto;
      do
      {
        return null;
        localPhoto = paramBackgroundImage.getPhoto();
      } while ((localPhoto == null) || (!localPhoto.hasInfoUrl()));
      Uri localUri = Uri.parse(localPhoto.getInfoUrl()).buildUpon().scheme(null).authority("").build();
      localQuery = this.mSearchUrlHelper.getQueryFromUrl(Query.EMPTY, localUri);
    } while (localQuery == null);
    return localQuery.getQueryString();
  }
  
  @Nullable
  private String getGooglePlayServicesActionString(Context paramContext, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 1: 
      return paramContext.getString(2131362799);
    case 3: 
      return paramContext.getString(2131362800);
    }
    return paramContext.getString(2131362801);
  }
  
  private String getGooglePlayServicesErrorString(Context paramContext, int paramInt)
  {
    int i = 2131362802;
    switch (paramInt)
    {
    }
    for (;;)
    {
      return paramContext.getString(i);
      i = 2131362795;
      continue;
      i = 2131362797;
      continue;
      i = 2131362796;
      continue;
      i = 2131362798;
    }
  }
  
  private int getNumStacksFromEntryTree(Sidekick.EntryTree paramEntryTree)
  {
    if (paramEntryTree.hasRoot()) {
      return paramEntryTree.getRoot().getChildCount();
    }
    return 0;
  }
  
  private void startEntriesRefresh(Context paramContext)
  {
    Intent localIntent = new Intent(paramContext, EntriesRefreshIntentService.class);
    localIntent.setAction("com.google.android.apps.sidekick.REFRESH");
    localIntent.putExtra("com.google.android.apps.sidekick.TYPE", 0);
    paramContext.startService(localIntent);
  }
  
  public CardsResponse getCards(Context paramContext)
  {
    CardsResponse localCardsResponse = new CardsResponse();
    Sidekick.EntryResponse localEntryResponse = new Sidekick.EntryResponse();
    long l = this.mEntryProvider.getLastRefreshTimeMillis();
    int i = this.mGooglePlayServicesHelper.getGooglePlayServicesAvailability();
    if (!this.mOptInSettings.isUserOptedIn()) {
      localCardsResponse.mResponseCode = 5;
    }
    for (;;)
    {
      List localList = this.mEntryProvider.getBackgroundImagePhotos();
      if ((localList != null) && (!localList.isEmpty()))
      {
        BackgroundImage localBackgroundImage = (BackgroundImage)localList.get(0);
        localCardsResponse.mContextImage = localBackgroundImage.getPhoto();
        localCardsResponse.mContextImageQuery = getContextImageQuery(localBackgroundImage);
      }
      localCardsResponse.mEntryResponse = localEntryResponse;
      return localCardsResponse;
      if (i != 0)
      {
        Log.w(TAG, "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(i));
        localCardsResponse.mResponseCode = 4;
        localCardsResponse.mGooglePlayServicesErrorString = getGooglePlayServicesErrorString(paramContext, i);
        localCardsResponse.mGooglePlayServicesActionString = getGooglePlayServicesActionString(paramContext, i);
        localCardsResponse.mGooglePlayServicesRecoveryIntent = this.mGooglePlayServicesHelper.getGooglePlayServicesAvailabilityRecoveryIntent(i);
        if (l == 0L) {
          startEntriesRefresh(paramContext);
        }
      }
      else if (!this.mEntryProvider.isInitializedFromStorage())
      {
        localCardsResponse.mResponseCode = 2;
      }
      else if (l == 0L)
      {
        localCardsResponse.mResponseCode = 2;
        startEntriesRefresh(paramContext);
      }
      else if (this.mEntryInvalidator.invalidateIfNecessary())
      {
        localCardsResponse.mResponseCode = 3;
        startEntriesRefresh(paramContext);
      }
      else
      {
        localCardsResponse.mResponseCode = 1;
        Sidekick.EntryTree localEntryTree1 = this.mEntryProvider.getEntryTree();
        if (localEntryTree1 != null)
        {
          Sidekick.EntryTree localEntryTree2 = this.mEntryTreePruner.copyAndPrune(localEntryTree1);
          localEntryResponse.addEntryTree(localEntryTree2);
          Location localLocation = this.mLocationOracle.getBestLocation();
          localCardsResponse.mRefreshLocation = LocationUtilities.sidekickLocationToAndroidLocation(this.mEntryProvider.getLastRefreshLocation());
          localCardsResponse.mRefreshTimeMillis = l;
          localCardsResponse.mChangeTimeMillis = this.mEntryProvider.getLastChangeTimeMillis();
          localCardsResponse.mIncludesMore = this.mEntryProvider.entriesIncludeMore();
          localCardsResponse.mShowFirstUseIntro = this.mCardDismissalHandler.shouldShowFirstUseCard(FirstUseCardHandler.FirstUseCardType.INTRO_CARD);
          localCardsResponse.mShowFirstUseOutro = this.mCardDismissalHandler.shouldShowFirstUseCard(FirstUseCardHandler.FirstUseCardType.OUTRO_CARD);
          localCardsResponse.mShowBackOfCardTutorial = this.mCardDismissalHandler.shouldShowBackOfCardTutorialCard();
          localCardsResponse.mShowSwipeTutorial = this.mCardDismissalHandler.shouldShowSwipeTutorialCard(getNumStacksFromEntryTree(localEntryTree2));
          CardRenderingContext localCardRenderingContext = new CardRenderingContext(localLocation, localCardsResponse.mRefreshLocation);
          this.mRenderingContextPopulator.populate(localCardRenderingContext, localEntryTree2);
          localCardsResponse.mCardRenderingContext = localCardRenderingContext;
        }
        else
        {
          Log.e(TAG, "Expected to have entries, but entry tree was null");
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.remoteservice.RemoteServiceHelper
 * JD-Core Version:    0.7.0.1
 */