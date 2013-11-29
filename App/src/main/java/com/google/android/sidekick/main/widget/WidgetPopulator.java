package com.google.android.sidekick.main.widget;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.widget.RemoteViews;
import com.google.android.search.core.GooglePlayServicesHelper;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.entry.EntryTreePruner;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.cards.AlbumEntryAdapter;
import com.google.android.sidekick.shared.cards.BirthdayCardEntryAdapter;
import com.google.android.sidekick.shared.cards.BookEntryAdapter;
import com.google.android.sidekick.shared.cards.CalendarEntryAdapter;
import com.google.android.sidekick.shared.cards.CurrencyExchangeEntryAdapter;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.cards.EventEntryAdapter;
import com.google.android.sidekick.shared.cards.FlightStatusEntryAdapter;
import com.google.android.sidekick.shared.cards.GenericPlaceEntryAdapter;
import com.google.android.sidekick.shared.cards.LastTrainHomeEntryAdapter;
import com.google.android.sidekick.shared.cards.MovieEntryAdapter;
import com.google.android.sidekick.shared.cards.NewsEntryAdapter;
import com.google.android.sidekick.shared.cards.PackageTrackingEntryAdapter;
import com.google.android.sidekick.shared.cards.PublicAlertEntryAdapter;
import com.google.android.sidekick.shared.cards.RelevantWebsiteEntryAdapter;
import com.google.android.sidekick.shared.cards.ReminderEntryAdapter;
import com.google.android.sidekick.shared.cards.ReservationEntryAdapter;
import com.google.android.sidekick.shared.cards.SportsEntryAdapter;
import com.google.android.sidekick.shared.cards.StockListEntryAdapter;
import com.google.android.sidekick.shared.cards.TranslateEntryAdapter;
import com.google.android.sidekick.shared.cards.TvEpisodeEntryAdapter;
import com.google.android.sidekick.shared.cards.VideoGameEntryAdapter;
import com.google.android.sidekick.shared.cards.WeatherEntryAdapter;
import com.google.android.sidekick.shared.cards.WebsiteUpdateEntryAdapter;
import com.google.android.sidekick.shared.client.EntryItemStack;
import com.google.android.sidekick.shared.client.EntryTreeConverter;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.ui.settings.PublicSettingsActivity;
import com.google.android.velvet.util.IntentUtils;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;

public class WidgetPopulator
{
  private static final String TAG = Tag.getTag(WidgetPopulator.class);
  private final CalendarDataProvider mCalendarDataProvider;
  private final EntryAdapterFactory<EntryCardViewAdapter> mCardViewAdapterFactory;
  private final Clock mClock;
  private final EntryProvider mEntryProvider;
  private final EntryTreePruner mEntryTreePruner;
  private final GooglePlayServicesHelper mGooglePlayServicesHelper;
  private final WidgetImageLoader mImageLoader;
  private final LoginHelper mLoginHelper;
  private final NowOptInSettings mNowOptInSettings;
  
  public WidgetPopulator(EntryProvider paramEntryProvider, WidgetImageLoader paramWidgetImageLoader, LoginHelper paramLoginHelper, NowOptInSettings paramNowOptInSettings, Clock paramClock, GooglePlayServicesHelper paramGooglePlayServicesHelper, CalendarDataProvider paramCalendarDataProvider, EntryAdapterFactory<EntryCardViewAdapter> paramEntryAdapterFactory, EntryTreePruner paramEntryTreePruner)
  {
    this.mEntryProvider = paramEntryProvider;
    this.mImageLoader = paramWidgetImageLoader;
    this.mLoginHelper = paramLoginHelper;
    this.mNowOptInSettings = paramNowOptInSettings;
    this.mClock = paramClock;
    this.mGooglePlayServicesHelper = paramGooglePlayServicesHelper;
    this.mCalendarDataProvider = paramCalendarDataProvider;
    this.mCardViewAdapterFactory = paramEntryAdapterFactory;
    this.mEntryTreePruner = paramEntryTreePruner;
  }
  
  private void addCard(Context paramContext, RemoteViews paramRemoteViews, int paramInt, EntryRemoteViewsAdapter<?> paramEntryRemoteViewsAdapter, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (RemoteViews localRemoteViews = paramEntryRemoteViewsAdapter.createNarrowRemoteView(paramContext);; localRemoteViews = paramEntryRemoteViewsAdapter.createRemoteView(paramContext))
    {
      paramRemoteViews.addView(paramInt, localRemoteViews);
      paramRemoteViews.setViewVisibility(paramInt, 0);
      ProtoKey localProtoKey = new ProtoKey(paramEntryRemoteViewsAdapter.getEntryCardViewAdapter().getEntry());
      Intent localIntent = IntentUtils.createAssistIntent(paramContext, 1);
      localIntent.putExtra("target_entry", localProtoKey.getBytes());
      if (paramEntryRemoteViewsAdapter.getEntryCardViewAdapter().getGroupEntryTreeNode() != null) {
        localIntent.putExtra("target_group_entry_tree", paramEntryRemoteViewsAdapter.getEntryCardViewAdapter().getGroupEntryTreeNode().toByteArray());
      }
      paramRemoteViews.setOnClickPendingIntent(paramInt, PendingIntent.getActivity(paramContext, localProtoKey.hashCode(), localIntent, 134217728));
      return;
    }
  }
  
  private void addClickIntent(Intent paramIntent, Context paramContext, int paramInt, RemoteViews paramRemoteViews)
  {
    paramRemoteViews.setOnClickPendingIntent(2131296890, PendingIntent.getActivity(paramContext, paramInt, paramIntent, 134217728));
  }
  
  private void addLaunchIntent(Context paramContext, int paramInt, RemoteViews paramRemoteViews)
  {
    addClickIntent(IntentUtils.createAssistIntent(paramContext, 1), paramContext, paramInt, paramRemoteViews);
  }
  
  @Nullable
  private EntryRemoteViewsAdapter<?> createEntryRemoteViewsAdapter(Context paramContext, EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    WeatherRemoteViewsAdapter localWeatherRemoteViewsAdapter;
    if ((paramEntryCardViewAdapter instanceof WeatherEntryAdapter)) {
      localWeatherRemoteViewsAdapter = new WeatherRemoteViewsAdapter((WeatherEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
    }
    boolean bool;
    do
    {
      return localWeatherRemoteViewsAdapter;
      if ((paramEntryCardViewAdapter instanceof SportsEntryAdapter)) {
        return new SportsRemoteViewsAdapter((SportsEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
      }
      if ((paramEntryCardViewAdapter instanceof GenericPlaceEntryAdapter)) {
        return new TrafficRemoteViewsAdapter((GenericPlaceEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof StockListEntryAdapter)) {
        return new StockQuoteRemoteViewsAdapter((StockListEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof PublicAlertEntryAdapter)) {
        return new PublicAlertRemoteViewsAdapter((PublicAlertEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof CalendarEntryAdapter)) {
        return new CalendarRemoteViewsAdapter((CalendarEntryAdapter)paramEntryCardViewAdapter, this.mCalendarDataProvider);
      }
      if ((paramEntryCardViewAdapter instanceof NewsEntryAdapter)) {
        return new NewsRemoteViewsAdapter((NewsEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof PackageTrackingEntryAdapter)) {
        return new PackageTrackingRemoteViewsAdapter((PackageTrackingEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof TranslateEntryAdapter)) {
        return new TranslateRemoteViewsAdapter((TranslateEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof BirthdayCardEntryAdapter)) {
        return new BirthdayRemoteViewsAdapter((BirthdayCardEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
      }
      if ((paramEntryCardViewAdapter instanceof FlightStatusEntryAdapter)) {
        return new FlightRemoteViewsAdapter((FlightStatusEntryAdapter)paramEntryCardViewAdapter, this.mClock);
      }
      if ((paramEntryCardViewAdapter instanceof CurrencyExchangeEntryAdapter)) {
        return new CurrencyExchangeRemoteViewsAdapter((CurrencyExchangeEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof WebsiteUpdateEntryAdapter)) {
        return new WebsiteUpdateRemoteViewsAdapter((WebsiteUpdateEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof ReminderEntryAdapter)) {
        return new ReminderRemoteViewsAdapter((ReminderEntryAdapter)paramEntryCardViewAdapter, this.mClock);
      }
      if ((paramEntryCardViewAdapter instanceof ReservationEntryAdapter)) {
        return new ReservationRemoteViewsAdapter((ReservationEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
      }
      if ((paramEntryCardViewAdapter instanceof RelevantWebsiteEntryAdapter)) {
        return new RelevantWebsiteRemoteViewsAdapter((RelevantWebsiteEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof LastTrainHomeEntryAdapter)) {
        return new LastTrainHomeRemoteViewsAdapter((LastTrainHomeEntryAdapter)paramEntryCardViewAdapter);
      }
      if ((paramEntryCardViewAdapter instanceof AlbumEntryAdapter)) {
        return new AlbumRemoteViewsAdapter((AlbumEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
      }
      if ((paramEntryCardViewAdapter instanceof BookEntryAdapter)) {
        return new BookRemoteViewsAdapter((BookEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
      }
      if ((paramEntryCardViewAdapter instanceof MovieEntryAdapter)) {
        return new MovieRemoteViewsAdapter((MovieEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
      }
      if ((paramEntryCardViewAdapter instanceof EventEntryAdapter)) {
        return new EventRemoteViewsAdapter((EventEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
      }
      if ((paramEntryCardViewAdapter instanceof TvEpisodeEntryAdapter)) {
        return new TvEpisodeRemoteViewsAdapter((TvEpisodeEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
      }
      bool = paramEntryCardViewAdapter instanceof VideoGameEntryAdapter;
      localWeatherRemoteViewsAdapter = null;
    } while (!bool);
    return new VideoGameRemoteViewsAdapter((VideoGameEntryAdapter)paramEntryCardViewAdapter, this.mImageLoader);
  }
  
  private List<EntryRemoteViewsAdapter<?>> getEntryRemoteViewsAdapters(Context paramContext, Sidekick.EntryTree paramEntryTree)
  {
    Sidekick.EntryTree localEntryTree = this.mEntryTreePruner.copyAndPrune(paramEntryTree);
    List localList = new EntryTreeConverter(this.mCardViewAdapterFactory).apply(localEntryTree);
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      EntryItemStack localEntryItemStack = (EntryItemStack)localIterator.next();
      if (!localEntryItemStack.getEntriesToShow().isEmpty())
      {
        EntryRemoteViewsAdapter localEntryRemoteViewsAdapter = createEntryRemoteViewsAdapter(paramContext, (EntryCardViewAdapter)localEntryItemStack.getEntriesToShow().get(0));
        if ((localEntryRemoteViewsAdapter != null) && (localEntryRemoteViewsAdapter.canCreateRemoteViews())) {
          localArrayList.add(localEntryRemoteViewsAdapter);
        }
      }
    }
    return localArrayList;
  }
  
  private RemoteViews populateViewInternal(Context paramContext, WidgetLayoutInfo paramWidgetLayoutInfo, List<EntryRemoteViewsAdapter<?>> paramList, int paramInt)
  {
    if ((paramWidgetLayoutInfo.mRows == 1) && (!paramWidgetLayoutInfo.mIncludePadding))
    {
      RemoteViews localRemoteViews3 = new RemoteViews(paramContext.getPackageName(), 2130968789);
      addCard(paramContext, localRemoteViews3, 2131296893, (EntryRemoteViewsAdapter)paramList.get(0), false);
      return localRemoteViews3;
    }
    RemoteViews localRemoteViews1 = new RemoteViews(paramContext.getPackageName(), 2130968788);
    localRemoteViews1.setViewVisibility(2131296892, 8);
    localRemoteViews1.setViewVisibility(2131296891, 0);
    localRemoteViews1.removeAllViews(2131296891);
    int i = -1 + 2 * paramWidgetLayoutInfo.mRows;
    int j = 0;
    int k = 0;
    int m = paramList.size();
    ListIterator localListIterator = paramList.listIterator();
    if ((localListIterator.hasNext()) && (localListIterator.nextIndex() < i))
    {
      int n = paramWidgetLayoutInfo.mRows;
      if (k < n)
      {
        EntryRemoteViewsAdapter localEntryRemoteViewsAdapter = (EntryRemoteViewsAdapter)localListIterator.next();
        RemoteViews localRemoteViews2 = new RemoteViews(paramContext.getPackageName(), 2130968789);
        if (j == 0)
        {
          addCard(paramContext, localRemoteViews2, 2131296893, localEntryRemoteViewsAdapter, false);
          label211:
          localRemoteViews1.addView(2131296891, localRemoteViews2);
          k++;
          if (m - localListIterator.nextIndex() < 2 * (paramWidgetLayoutInfo.mRows - k)) {
            break label305;
          }
        }
        label305:
        for (j = 1;; j = 0)
        {
          break;
          addCard(paramContext, localRemoteViews2, 2131296894, localEntryRemoteViewsAdapter, paramWidgetLayoutInfo.mShowHalfWidthCards);
          if (!localListIterator.hasNext()) {
            break label211;
          }
          addCard(paramContext, localRemoteViews2, 2131296895, (EntryRemoteViewsAdapter)localListIterator.next(), paramWidgetLayoutInfo.mShowHalfWidthCards);
          break label211;
        }
      }
    }
    return localRemoteViews1;
  }
  
  private RemoteViews showNoCards(Context paramContext, int paramInt)
  {
    int i = 0;
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968788);
    localRemoteViews.setViewVisibility(2131296892, 0);
    localRemoteViews.setViewVisibility(2131296891, 8);
    Account localAccount = this.mLoginHelper.getAccount();
    Object localObject = paramContext.getString(2131362699);
    int j = this.mNowOptInSettings.canAccountRunNow(localAccount);
    if (localAccount == null)
    {
      String[] arrayOfString = this.mLoginHelper.getAllAccountNames();
      if ((arrayOfString == null) || (arrayOfString.length == 0))
      {
        addClickIntent(new Intent("android.settings.ADD_ACCOUNT_SETTINGS"), paramContext, paramInt, localRemoteViews);
        localObject = paramContext.getString(2131362702);
      }
    }
    for (;;)
    {
      localRemoteViews.setTextViewText(2131296892, (CharSequence)localObject);
      if (i != 0) {
        addLaunchIntent(paramContext, paramInt, localRemoteViews);
      }
      return localRemoteViews;
      Intent localIntent = new Intent("com.google.android.googlequicksearchbox.action.PRIVACY_SETTINGS");
      localIntent.setClass(paramContext, PublicSettingsActivity.class);
      addClickIntent(localIntent, paramContext, paramInt, localRemoteViews);
      localObject = paramContext.getString(2131362703);
      i = 0;
      continue;
      if (j != 1)
      {
        if (j == 0)
        {
          localObject = paramContext.getString(2131362698);
          i = 1;
        }
        else
        {
          i = 0;
          if (localAccount != null)
          {
            Sidekick.Configuration localConfiguration = this.mNowOptInSettings.getSavedConfiguration(localAccount);
            if (localConfiguration != null)
            {
              if (this.mNowOptInSettings.localeIsBlockedFromNow(localConfiguration))
              {
                localObject = paramContext.getString(2131362700);
                i = 0;
              }
              else if (this.mNowOptInSettings.domainIsBlockedFromNow(localConfiguration, localAccount))
              {
                Uri localUri = Uri.parse("http://support.google.com/websearch/answer/2938260");
                localObject = Html.fromHtml(paramContext.getString(2131362694, new Object[] { localUri }));
                addClickIntent(new Intent("android.intent.action.VIEW", localUri), paramContext, paramInt, localRemoteViews);
                i = 0;
              }
              else
              {
                localObject = paramContext.getString(2131362701);
                i = 0;
              }
            }
            else
            {
              localObject = paramContext.getString(2131362698);
              i = 1;
            }
          }
        }
      }
      else if ((!this.mNowOptInSettings.isAccountOptedIn(localAccount)) || (this.mNowOptInSettings.getSavedConfiguration(localAccount) == null))
      {
        localObject = paramContext.getString(2131362698);
        i = 1;
      }
      else
      {
        boolean bool = this.mGooglePlayServicesHelper.isGooglePlayServicesAvailable();
        i = 0;
        if (!bool)
        {
          localObject = paramContext.getString(2131362799);
          i = 1;
        }
      }
    }
  }
  
  RemoteViews populateView(Context paramContext, WidgetLayoutInfo paramWidgetLayoutInfo1, WidgetLayoutInfo paramWidgetLayoutInfo2, int paramInt)
  {
    if (!this.mGooglePlayServicesHelper.isGooglePlayServicesAvailable()) {
      return showNoCards(paramContext, paramInt);
    }
    Sidekick.EntryTree localEntryTree = this.mEntryProvider.getEntryTree();
    if (localEntryTree == null) {
      return showNoCards(paramContext, paramInt);
    }
    List localList = getEntryRemoteViewsAdapters(paramContext, localEntryTree);
    if (localList.isEmpty()) {
      return showNoCards(paramContext, paramInt);
    }
    return new RemoteViews(populateViewInternal(paramContext, paramWidgetLayoutInfo1, localList, paramInt), populateViewInternal(paramContext, paramWidgetLayoutInfo2, localList, paramInt));
  }
  
  static class WidgetLayoutInfo
  {
    public final boolean mIncludePadding;
    public final int mRows;
    public final boolean mShowHalfWidthCards;
    
    public WidgetLayoutInfo(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    {
      this.mRows = paramInt;
      this.mShowHalfWidthCards = paramBoolean1;
      this.mIncludePadding = paramBoolean2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.WidgetPopulator
 * JD-Core Version:    0.7.0.1
 */