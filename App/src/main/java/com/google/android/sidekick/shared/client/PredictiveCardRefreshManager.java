package com.google.android.sidekick.shared.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.cards.BackOfCardTutorialCardAdapter;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.cards.FirstUseIntroCardAdapter;
import com.google.android.sidekick.shared.cards.FirstUseOutroCardAdapter;
import com.google.android.sidekick.shared.cards.GooglePlayServicesCardAdapter;
import com.google.android.sidekick.shared.cards.LoadMoreCardsAdapter;
import com.google.android.sidekick.shared.cards.NoCardsCardAdapter;
import com.google.android.sidekick.shared.cards.SwipeTutorialCardAdapter;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.remoteapi.CardsResponse;
import com.google.android.sidekick.shared.remoteapi.LoggingRequest;
import com.google.android.sidekick.shared.ui.NowProgressBar;
import com.google.android.sidekick.shared.ui.PullToRefreshHandler;
import com.google.android.sidekick.shared.ui.PullToRefreshHandler.Listener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.EntryRefreshUtil;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class PredictiveCardRefreshManager
{
  static final long REFRESH_TIMEOUT_MILLIS = 20000L;
  private final ActivityHelper mActivityHelper;
  private final Context mAppContext;
  private final EntryAdapterFactory<EntryCardViewAdapter> mCardViewAdapterFactory;
  private final CardsUpdateReceiver mCardsUpdateReceiver = new CardsUpdateReceiver(null);
  private long mChangeTimeOfLastPopulate = 0L;
  private Uri mContextImageUri = null;
  private Sidekick.EntryTree mCurrentEntryTree = null;
  private final Object mCurrentStateLock = new Object();
  private FetchEntriesTask mFetchEntriesTask;
  private final Object mFetchEntriesTaskLock = new Object();
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  private final UriLoader<Drawable> mImageLoader;
  private int mLastResponseCode = -1;
  private LoadMoreCardsAdapter mLoadMoreCardsAdapter;
  private final Object mLoadMoreCardsLock = new Object();
  private View mLoadMoreCardsView;
  private final NowRemoteClient mNowRemoteClient;
  private boolean mPredictiveCardsListenersRegistered = false;
  private PredictiveCardsPresenter mPresenter;
  private PullToRefreshHandler mPullToRefreshHandler;
  private RefreshTimeoutHandler mRefreshTimeoutHandler;
  private boolean mReminderSaved = false;
  private final ServiceConnectionListener mServiceListener = new ServiceConnectionListener(null);
  private boolean mShowingMoreCards = false;
  private final ScheduledSingleThreadedExecutor mUiThread;
  private final ViewActionRecorder mViewActionRecorder;
  
  public PredictiveCardRefreshManager(Context paramContext, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, EntryAdapterFactory<EntryCardViewAdapter> paramEntryAdapterFactory, ActivityHelper paramActivityHelper, NowRemoteClient paramNowRemoteClient, ViewActionRecorder paramViewActionRecorder, FifeImageUrlUtil paramFifeImageUrlUtil, UriLoader<Drawable> paramUriLoader)
  {
    this.mAppContext = paramContext;
    this.mUiThread = paramScheduledSingleThreadedExecutor;
    this.mCardViewAdapterFactory = paramEntryAdapterFactory;
    this.mActivityHelper = paramActivityHelper;
    this.mNowRemoteClient = paramNowRemoteClient;
    this.mViewActionRecorder = paramViewActionRecorder;
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
    this.mImageLoader = paramUriLoader;
  }
  
  private void cancelRefreshTimeoutHandler()
  {
    if (this.mRefreshTimeoutHandler != null)
    {
      this.mUiThread.cancelExecute(this.mRefreshTimeoutHandler);
      this.mRefreshTimeoutHandler = null;
    }
  }
  
  private void handleRefreshTimeout()
  {
    this.mPresenter.showError(2131362182);
    this.mPresenter.stopProgressBar();
  }
  
  private boolean hasEntries(Sidekick.EntryResponse paramEntryResponse)
  {
    int i = paramEntryResponse.getEntryTreeCount();
    boolean bool1 = false;
    if (i > 0)
    {
      boolean bool2 = paramEntryResponse.getEntryTree(0).hasRoot();
      bool1 = false;
      if (bool2)
      {
        int j = paramEntryResponse.getEntryTree(0).getRoot().getChildCount();
        bool1 = false;
        if (j > 0) {
          bool1 = true;
        }
      }
    }
    return bool1;
  }
  
  private void showGooglePlayServicesCard(String paramString1, String paramString2, Intent paramIntent)
  {
    GooglePlayServicesCardAdapter localGooglePlayServicesCardAdapter = new GooglePlayServicesCardAdapter(paramString1, paramString2, paramIntent, this.mActivityHelper, this.mPresenter.getIntentStarter());
    this.mPresenter.showSinglePromoCard(localGooglePlayServicesCardAdapter);
  }
  
  private void showLoadMoreCardsError()
  {
    showLoadMoreCardsMessage(true);
  }
  
  private void showLoadMoreCardsMessage(final boolean paramBoolean)
  {
    synchronized (this.mLoadMoreCardsLock)
    {
      if ((this.mLoadMoreCardsAdapter != null) && (this.mLoadMoreCardsView != null))
      {
        final LoadMoreCardsAdapter localLoadMoreCardsAdapter = this.mLoadMoreCardsAdapter;
        final View localView = this.mLoadMoreCardsView;
        this.mLoadMoreCardsAdapter = null;
        this.mLoadMoreCardsView = null;
        this.mUiThread.execute(new Runnable()
        {
          public void run()
          {
            if (paramBoolean) {
              localLoadMoreCardsAdapter.showLoadFailedText(localView);
            }
            for (;;)
            {
              PredictiveCardRefreshManager.this.mUiThread.executeDelayed(new Runnable()
              {
                public void run()
                {
                  PredictiveCardRefreshManager.this.mPresenter.removeCard(PredictiveCardRefreshManager.1.this.val$loadMoreCardsAdapter);
                }
              }, 1000L);
              return;
              localLoadMoreCardsAdapter.showNoMoreCardsText(localView);
            }
          }
        });
      }
      return;
    }
  }
  
  private void showNoMoreCardsMessage()
  {
    showLoadMoreCardsMessage(false);
  }
  
  private void startRefreshTimeoutHandler()
  {
    if (this.mRefreshTimeoutHandler == null)
    {
      this.mRefreshTimeoutHandler = new RefreshTimeoutHandler(null);
      this.mUiThread.executeDelayed(this.mRefreshTimeoutHandler, 20000L);
    }
  }
  
  private void updateContextImage(@Nullable Sidekick.Photo paramPhoto, @Nullable String paramString)
  {
    int i = 1;
    Point localPoint = LayoutUtils.getContextHeaderSize(this.mAppContext);
    if ((paramPhoto != null) && (paramPhoto.getUrlType() == i))
    {
      final Uri localUri = this.mFifeImageUrlUtil.setImageUrlCenterCrop(localPoint.x, localPoint.y, paramPhoto.getUrl());
      if (!localUri.equals(this.mContextImageUri)) {
        if (Strings.isNullOrEmpty(paramString)) {
          break label138;
        }
      }
      final DoodleClickListener localDoodleClickListener;
      CancellableNowOrLater localCancellableNowOrLater;
      label138:
      final boolean bool;
      for (;;)
      {
        localDoodleClickListener = null;
        if (i != 0) {
          localDoodleClickListener = new DoodleClickListener(this.mPresenter, paramString);
        }
        localCancellableNowOrLater = this.mImageLoader.load(localUri);
        if (!localCancellableNowOrLater.haveNow()) {
          break;
        }
        this.mContextImageUri = localUri;
        this.mPresenter.setContextHeader((Drawable)localCancellableNowOrLater.getNow(), i, localDoodleClickListener);
        return;
        bool = false;
      }
      localCancellableNowOrLater.getLater(new Consumer()
      {
        public boolean consume(Drawable paramAnonymousDrawable)
        {
          PredictiveCardRefreshManager.access$602(PredictiveCardRefreshManager.this, localUri);
          PredictiveCardRefreshManager.this.mPresenter.setContextHeader(paramAnonymousDrawable, bool, localDoodleClickListener);
          return true;
        }
      });
      return;
    }
    this.mContextImageUri = null;
    new SetDefaultContextImageTask(localPoint.x, localPoint.y).execute(new Void[0]);
  }
  
  public void addViewActionListeners()
  {
    this.mViewActionRecorder.addViewActionListeners();
  }
  
  public void buildView()
  {
    if (!this.mPresenter.isAttached()) {}
    while (!this.mNowRemoteClient.isConnected()) {
      return;
    }
    if (this.mPresenter.isPredictiveOnlyMode()) {
      this.mNowRemoteClient.recordPredictiveInteraction();
    }
    synchronized (this.mFetchEntriesTaskLock)
    {
      if ((this.mFetchEntriesTask == null) || (this.mFetchEntriesTask.getStatus() == AsyncTask.Status.FINISHED))
      {
        this.mFetchEntriesTask = new FetchEntriesTask();
        this.mFetchEntriesTask.execute(new Void[0]);
      }
      return;
    }
  }
  
  long getChangeTimeOfLastPopulate()
  {
    return this.mChangeTimeOfLastPopulate;
  }
  
  Sidekick.EntryTree getCurrentEntryTreeForTest()
  {
    return this.mCurrentEntryTree;
  }
  
  boolean isShowingMoreCards()
  {
    return this.mShowingMoreCards;
  }
  
  void logStackRender(List<EntryItemStack> paramList)
  {
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator1 = paramList.iterator();
    while (localIterator1.hasNext())
    {
      Iterator localIterator2 = ((EntryItemStack)localIterator1.next()).getEntriesToShow().iterator();
      while (localIterator2.hasNext())
      {
        String str = ((EntryCardViewAdapter)localIterator2.next()).getLoggingName();
        if (localHashMap.containsKey(str)) {
          localHashMap.put(str, Integer.valueOf(1 + ((Integer)localHashMap.get(str)).intValue()));
        } else {
          localHashMap.put(str, Integer.valueOf(1));
        }
      }
    }
    LoggingRequest localLoggingRequest = LoggingRequest.forStackRender("CARD_RENDER", localHashMap);
    this.mNowRemoteClient.logAction(localLoggingRequest);
  }
  
  EntriesResult processCardsResponse(CardsResponse paramCardsResponse)
  {
    int i = paramCardsResponse.getResponseCode();
    boolean bool = true;
    Object localObject1 = null;
    if (i == 1) {}
    for (;;)
    {
      synchronized (this.mCurrentStateLock)
      {
        if (paramCardsResponse.getChangeTimeMillis() == this.mChangeTimeOfLastPopulate) {
          return null;
        }
        this.mChangeTimeOfLastPopulate = paramCardsResponse.getChangeTimeMillis();
        this.mShowingMoreCards = paramCardsResponse.entriesIncludeMore();
        Sidekick.EntryResponse localEntryResponse = paramCardsResponse.getEntryResponse();
        if (hasEntries(localEntryResponse))
        {
          Sidekick.EntryTree localEntryTree1 = localEntryResponse.getEntryTree(0);
          Sidekick.EntryTree localEntryTree2 = localEntryTree1;
          int j;
          int k;
          if ((paramCardsResponse.entriesIncludeMore()) && (this.mCurrentEntryTree != null))
          {
            j = this.mCurrentEntryTree.getRoot().getChildCount();
            k = localEntryTree1.getRoot().getChildCount();
            if (k < j) {
              bool = true;
            }
          }
          else
          {
            this.mCurrentEntryTree = ((Sidekick.EntryTree)com.google.android.shared.util.ProtoUtils.copyOf(localEntryTree1));
            if (localEntryTree2 == null) {
              continue;
            }
            localObject1 = new EntryTreeConverter(this.mCardViewAdapterFactory).apply(localEntryTree2);
            if (bool)
            {
              if (paramCardsResponse.showSwipeTutorial())
              {
                EntryCardViewAdapter[] arrayOfEntryCardViewAdapter6 = new EntryCardViewAdapter[1];
                arrayOfEntryCardViewAdapter6[0] = new SwipeTutorialCardAdapter(this.mActivityHelper);
                ((List)localObject1).add(2, new EntryItemStack(arrayOfEntryCardViewAdapter6));
              }
              if (paramCardsResponse.showBackOfCardTutorial())
              {
                EntryCardViewAdapter[] arrayOfEntryCardViewAdapter5 = new EntryCardViewAdapter[1];
                arrayOfEntryCardViewAdapter5[0] = new BackOfCardTutorialCardAdapter(this.mActivityHelper);
                ((List)localObject1).add(0, new EntryItemStack(arrayOfEntryCardViewAdapter5));
              }
              if (paramCardsResponse.showFirstUseIntro())
              {
                EntryCardViewAdapter[] arrayOfEntryCardViewAdapter4 = new EntryCardViewAdapter[1];
                arrayOfEntryCardViewAdapter4[0] = new FirstUseIntroCardAdapter(this.mActivityHelper);
                ((List)localObject1).add(0, new EntryItemStack(arrayOfEntryCardViewAdapter4));
              }
              if (paramCardsResponse.showFirstUseOutro())
              {
                EntryCardViewAdapter[] arrayOfEntryCardViewAdapter3 = new EntryCardViewAdapter[1];
                arrayOfEntryCardViewAdapter3[0] = new FirstUseOutroCardAdapter(this.mActivityHelper);
                ((List)localObject1).add(new EntryItemStack(arrayOfEntryCardViewAdapter3));
              }
            }
            if (!paramCardsResponse.entriesIncludeMore())
            {
              EntryCardViewAdapter[] arrayOfEntryCardViewAdapter2 = new EntryCardViewAdapter[1];
              arrayOfEntryCardViewAdapter2[0] = new LoadMoreCardsAdapter(this.mActivityHelper, this);
              ((List)localObject1).add(new EntryItemStack(arrayOfEntryCardViewAdapter2));
            }
            return new EntriesResult(paramCardsResponse, (List)localObject1, bool);
          }
          if (j == k)
          {
            showNoMoreCardsMessage();
            localEntryTree2 = null;
            continue;
          }
          localEntryTree2 = new Sidekick.EntryTree();
          localEntryTree2.setRoot(new Sidekick.EntryTreeNode());
          int m = j;
          if (m >= k) {
            break label556;
          }
          localEntryTree2.getRoot().addChild(localEntryTree1.getRoot().getChild(m));
          m++;
          continue;
          stopProgressBar();
          return null;
        }
      }
      EntryItemStack[] arrayOfEntryItemStack = new EntryItemStack[1];
      EntryCardViewAdapter[] arrayOfEntryCardViewAdapter1 = new EntryCardViewAdapter[1];
      arrayOfEntryCardViewAdapter1[0] = new NoCardsCardAdapter(this.mActivityHelper);
      arrayOfEntryItemStack[0] = new EntryItemStack(arrayOfEntryCardViewAdapter1);
      localObject1 = Lists.newArrayList(arrayOfEntryItemStack);
      this.mCurrentEntryTree = null;
      continue;
      label556:
      bool = false;
    }
  }
  
  public void recordViewEndTimes()
  {
    this.mViewActionRecorder.recordViewEndTimes();
  }
  
  public void recordViewStartTimes()
  {
    this.mViewActionRecorder.recordViewStartTimes();
  }
  
  public void refreshCards(int paramInt, boolean paramBoolean)
  {
    Intent localIntent = EntryRefreshUtil.createRefreshIntent();
    localIntent.setAction("com.google.android.apps.sidekick.REFRESH");
    if ((paramInt == 2) && (this.mShowingMoreCards)) {
      paramInt = 3;
    }
    localIntent.putExtra("com.google.android.apps.sidekick.TYPE", paramInt);
    localIntent.putExtra("com.google.android.apps.sidekick.SAVE_CALL_LOG", paramBoolean);
    this.mAppContext.startService(localIntent);
    if ((paramInt == 0) || (EntriesRefreshRequestType.isUserInitiated(paramInt))) {
      startRefreshTimeoutHandler();
    }
  }
  
  public void registerPredictiveCardsListeners()
  {
    if (!this.mPredictiveCardsListenersRegistered)
    {
      this.mAppContext.registerReceiver(this.mCardsUpdateReceiver, new IntentFilter("com.google.android.apps.now.ENTRIES_UPDATED"));
      this.mNowRemoteClient.registerConnectionListener(this.mServiceListener);
      this.mPredictiveCardsListenersRegistered = true;
    }
  }
  
  public void registerPullToRefreshHandler(NowProgressBar paramNowProgressBar)
  {
    if (this.mPullToRefreshHandler == null)
    {
      this.mPullToRefreshHandler = new PullToRefreshHandler(this.mAppContext, this.mPresenter.getScrollViewControl(), this.mPresenter.getSuggestionGridLayout(), paramNowProgressBar, new PullToRefreshListener(null));
      this.mPullToRefreshHandler.register();
    }
  }
  
  public void removeViewActionListeners()
  {
    this.mViewActionRecorder.removeViewActionListeners();
  }
  
  public void requestMoreCards(LoadMoreCardsAdapter paramLoadMoreCardsAdapter, View paramView)
  {
    synchronized (this.mLoadMoreCardsLock)
    {
      this.mLoadMoreCardsAdapter = paramLoadMoreCardsAdapter;
      this.mLoadMoreCardsView = paramView;
      refreshCards(4, false);
      return;
    }
  }
  
  public void reset()
  {
    cancelRefreshTimeoutHandler();
    synchronized (this.mCurrentStateLock)
    {
      this.mChangeTimeOfLastPopulate = 0L;
      this.mShowingMoreCards = false;
      this.mCurrentEntryTree = null;
      return;
    }
  }
  
  void setCurrentStateForTest(long paramLong, boolean paramBoolean, Sidekick.EntryTree paramEntryTree)
  {
    synchronized (this.mCurrentStateLock)
    {
      this.mChangeTimeOfLastPopulate = paramLong;
      this.mShowingMoreCards = paramBoolean;
      this.mCurrentEntryTree = paramEntryTree;
      return;
    }
  }
  
  public void setPresenter(PredictiveCardsPresenter paramPredictiveCardsPresenter)
  {
    this.mPresenter = ((PredictiveCardsPresenter)Preconditions.checkNotNull(paramPredictiveCardsPresenter));
    this.mViewActionRecorder.setScrollableCardView(paramPredictiveCardsPresenter);
  }
  
  public void stopProgressBar()
  {
    this.mPresenter.stopProgressBar();
  }
  
  public void unregisterPredictiveCardsListeners()
  {
    if (this.mPredictiveCardsListenersRegistered)
    {
      this.mAppContext.unregisterReceiver(this.mCardsUpdateReceiver);
      this.mNowRemoteClient.unregisterConnectionListener(this.mServiceListener);
      this.mPredictiveCardsListenersRegistered = false;
    }
    cancelRefreshTimeoutHandler();
  }
  
  public void unregisterPullToRefreshHandler()
  {
    if (this.mPullToRefreshHandler != null)
    {
      this.mPullToRefreshHandler.unregister();
      this.mPullToRefreshHandler = null;
    }
  }
  
  private class CardsUpdateReceiver
    extends BroadcastReceiver
  {
    private CardsUpdateReceiver() {}
    
    private void launchActivity(String paramString, int paramInt)
    {
      Intent localIntent = new Intent();
      localIntent.setClassName("com.google.android.googlequicksearchbox", paramString);
      localIntent.addFlags(paramInt);
      PredictiveCardRefreshManager.this.mAppContext.startActivity(localIntent);
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent == null) {
        break label4;
      }
      for (;;)
      {
        label4:
        return;
        if (PredictiveCardRefreshManager.this.mPresenter.isAttached())
        {
          int i = paramIntent.getIntExtra("type", -1);
          int j = paramIntent.getIntExtra("refresh_type", 0);
          if (((i == 0) || (i == 6)) && (j == 1) && (PredictiveCardRefreshManager.this.mPresenter.isVisible()) && (PredictiveCardRefreshManager.this.mCurrentEntryTree != null)) {
            break;
          }
          PredictiveCardRefreshManager.access$1002(PredictiveCardRefreshManager.this, paramIntent.getBooleanExtra("reminder_updated", false));
          switch (i)
          {
          default: 
            return;
          case 0: 
          case 1: 
          case 2: 
            if (j == 5) {}
            synchronized (PredictiveCardRefreshManager.this.mCurrentStateLock)
            {
              PredictiveCardRefreshManager.access$902(PredictiveCardRefreshManager.this, null);
              PredictiveCardRefreshManager.this.buildView();
              return;
            }
          case 4: 
            Sidekick.Entry localEntry2 = com.google.android.sidekick.shared.util.ProtoUtils.getEntryFromIntent(paramIntent, "entry");
            Sidekick.Entry localEntry3 = com.google.android.sidekick.shared.util.ProtoUtils.getEntryFromIntent(paramIntent, "updated_entry");
            Sidekick.Entry localEntry4 = com.google.android.sidekick.shared.util.ProtoUtils.getEntryFromIntent(paramIntent, "entry_change");
            PredictiveCardRefreshManager.this.mPresenter.updateEntry(localEntry2, localEntry3, localEntry4);
            return;
          case 5: 
            Sidekick.Entry localEntry1 = com.google.android.sidekick.shared.util.ProtoUtils.getEntryFromIntent(paramIntent, "entry");
            Collection localCollection = com.google.android.sidekick.shared.util.ProtoUtils.getEntriesFromIntent(paramIntent, "child_entries");
            PredictiveCardRefreshManager.this.mPresenter.dismissEntry(localEntry1, localCollection);
            return;
          case 3: 
            if (paramIntent.getBooleanExtra("refresh_error_auth", false)) {
              PredictiveCardRefreshManager.this.mPresenter.showError(2131362183);
            }
            for (;;)
            {
              if (PredictiveCardRefreshManager.this.mRefreshTimeoutHandler != null) {
                PredictiveCardRefreshManager.this.cancelRefreshTimeoutHandler();
              }
              PredictiveCardRefreshManager.this.mPresenter.stopProgressBar();
              return;
              if (j == 4) {
                PredictiveCardRefreshManager.this.showLoadMoreCardsError();
              } else {
                PredictiveCardRefreshManager.this.mPresenter.showError(2131362182);
              }
            }
          case 6: 
            int k = paramIntent.getIntExtra("refresh_type", -1);
            if (k != 4) {
              synchronized (PredictiveCardRefreshManager.this.mCurrentStateLock)
              {
                PredictiveCardRefreshManager.access$902(PredictiveCardRefreshManager.this, null);
                if ((k == 0) || (EntriesRefreshRequestType.isUserInitiated(k)))
                {
                  PredictiveCardRefreshManager.this.mPresenter.startProgressBar();
                  return;
                }
              }
            }
            break;
          }
        }
      }
      PredictiveCardRefreshManager.this.cancelRefreshTimeoutHandler();
      if (paramIntent.getIntExtra("disabled_reason", -1) == 2)
      {
        launchActivity("com.google.android.velvet.ui.settings.SettingsActivity", 268435456);
        return;
      }
      launchActivity("com.google.android.googlequicksearchbox.SearchActivity", 268435456);
    }
  }
  
  static class DoodleClickListener
    implements View.OnClickListener
  {
    private final String mDoodleQuery;
    private final PredictiveCardRefreshManager.PredictiveCardsPresenter mPresenter;
    
    DoodleClickListener(PredictiveCardRefreshManager.PredictiveCardsPresenter paramPredictiveCardsPresenter, String paramString)
    {
      this.mPresenter = paramPredictiveCardsPresenter;
      this.mDoodleQuery = paramString;
    }
    
    public void onClick(View paramView)
    {
      this.mPresenter.startWebSearch(this.mDoodleQuery, null);
    }
  }
  
  static class EntriesResult
  {
    final CardsResponse mCardsResponse;
    final List<EntryItemStack> mEntryItemStacks;
    final boolean mFullPopulate;
    
    EntriesResult(CardsResponse paramCardsResponse, List<EntryItemStack> paramList, boolean paramBoolean)
    {
      this.mCardsResponse = paramCardsResponse;
      this.mEntryItemStacks = paramList;
      this.mFullPopulate = paramBoolean;
    }
  }
  
  class FetchEntriesTask
    extends AsyncTask<Void, Void, PredictiveCardRefreshManager.EntriesResult>
  {
    FetchEntriesTask() {}
    
    protected PredictiveCardRefreshManager.EntriesResult doInBackground(Void... paramVarArgs)
    {
      CardsResponse localCardsResponse = PredictiveCardRefreshManager.this.mNowRemoteClient.getEntries();
      if (localCardsResponse != null) {
        return PredictiveCardRefreshManager.this.processCardsResponse(localCardsResponse);
      }
      return null;
    }
    
    protected void onPostExecute(@Nullable PredictiveCardRefreshManager.EntriesResult paramEntriesResult)
    {
      PredictiveCardRefreshManager.this.cancelRefreshTimeoutHandler();
      if (paramEntriesResult == null)
      {
        PredictiveCardRefreshManager.access$1002(PredictiveCardRefreshManager.this, false);
        return;
      }
      int i = paramEntriesResult.mCardsResponse.getResponseCode();
      if (i == 5) {
        PredictiveCardRefreshManager.this.mPresenter.showOptIn();
      }
      label183:
      do
      {
        for (;;)
        {
          PredictiveCardRefreshManager.access$1702(PredictiveCardRefreshManager.this, i);
          if (PredictiveCardRefreshManager.this.mPresenter.isContextHeaderVisible()) {
            PredictiveCardRefreshManager.this.updateContextImage(paramEntriesResult.mCardsResponse.getContextImage(), paramEntriesResult.mCardsResponse.getContextImageQuery());
          }
          if (!PredictiveCardRefreshManager.this.mReminderSaved) {
            break;
          }
          PredictiveCardRefreshManager.this.mPresenter.showRemindersPeekAnimation();
          return;
          if (i != 4) {
            break label183;
          }
          if (PredictiveCardRefreshManager.this.mLastResponseCode != i)
          {
            PredictiveCardRefreshManager.this.mPresenter.resetView();
            PredictiveCardRefreshManager.this.showGooglePlayServicesCard(paramEntriesResult.mCardsResponse.getGooglePlayServicesErrorString(), paramEntriesResult.mCardsResponse.getGooglePlayServicesActionString(), paramEntriesResult.mCardsResponse.getGooglePlayServicesRecoveryIntent());
          }
          else
          {
            PredictiveCardRefreshManager.this.stopProgressBar();
          }
        }
      } while (i != 1);
      PredictiveCardRefreshManager.this.logStackRender(paramEntriesResult.mEntryItemStacks);
      if (paramEntriesResult.mFullPopulate) {
        PredictiveCardRefreshManager.this.mPresenter.populateView(paramEntriesResult.mEntryItemStacks, paramEntriesResult.mCardsResponse.getCardRenderingContext());
      }
      for (;;)
      {
        PredictiveCardRefreshManager.this.mViewActionRecorder.recordViewStartTimes();
        break;
        PredictiveCardRefreshManager.this.mPresenter.addEntries(paramEntriesResult.mEntryItemStacks, paramEntriesResult.mCardsResponse.getCardRenderingContext());
      }
    }
    
    protected void onPreExecute()
    {
      PredictiveCardRefreshManager.this.mViewActionRecorder.recordViewEndTimes();
    }
  }
  
  public static abstract interface PredictiveCardsPresenter
    extends ScrollableCardView
  {
    public abstract void addEntries(List<EntryItemStack> paramList, CardRenderingContext paramCardRenderingContext);
    
    public abstract void dismissEntry(Sidekick.Entry paramEntry, @Nullable Collection<Sidekick.Entry> paramCollection);
    
    public abstract IntentStarter getIntentStarter();
    
    public abstract boolean isAttached();
    
    public abstract boolean isContextHeaderVisible();
    
    public abstract void populateView(@Nullable List<EntryItemStack> paramList, CardRenderingContext paramCardRenderingContext);
    
    public abstract void removeCard(EntryCardViewAdapter paramEntryCardViewAdapter);
    
    public abstract void resetView();
    
    public abstract void setContextHeader(Drawable paramDrawable, boolean paramBoolean, @Nullable View.OnClickListener paramOnClickListener);
    
    public abstract void showError(int paramInt);
    
    public abstract void showOptIn();
    
    public abstract void showRemindersPeekAnimation();
    
    public abstract void showSinglePromoCard(EntryCardViewAdapter paramEntryCardViewAdapter);
    
    public abstract void startProgressBar();
    
    public abstract boolean startWebSearch(String paramString, @Nullable Location paramLocation);
    
    public abstract void stopProgressBar();
    
    public abstract void updateEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2, Sidekick.Entry paramEntry3);
  }
  
  private class PullToRefreshListener
    implements PullToRefreshHandler.Listener
  {
    private PullToRefreshListener() {}
    
    public void onRefreshRequested()
    {
      PredictiveCardRefreshManager.this.refreshCards(2, false);
    }
  }
  
  private class RefreshTimeoutHandler
    implements Runnable
  {
    private RefreshTimeoutHandler() {}
    
    public void run()
    {
      PredictiveCardRefreshManager.access$702(PredictiveCardRefreshManager.this, null);
      PredictiveCardRefreshManager.this.handleRefreshTimeout();
    }
  }
  
  private class ServiceConnectionListener
    implements Runnable
  {
    private ServiceConnectionListener() {}
    
    public void run()
    {
      PredictiveCardRefreshManager.this.buildView();
    }
  }
  
  class SetDefaultContextImageTask
    extends AsyncTask<Void, Void, Drawable>
  {
    private final int mHeight;
    private final int mWidth;
    
    SetDefaultContextImageTask(int paramInt1, int paramInt2)
    {
      this.mWidth = paramInt1;
      this.mHeight = paramInt2;
    }
    
    private Bitmap scaleAndCropBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2)
    {
      double d = Math.max(paramInt1 / paramBitmap.getWidth(), paramInt2 / paramBitmap.getHeight());
      int i = (int)(d * paramBitmap.getWidth());
      int j = (int)(d * paramBitmap.getHeight());
      if ((i == paramInt1) && (j == paramInt2)) {
        return paramBitmap;
      }
      Bitmap localBitmap = Bitmap.createScaledBitmap(paramBitmap, i, j, true);
      int k = (i - paramInt1) / 2;
      int m = (j - paramInt2) / 2;
      return Bitmap.createBitmap(localBitmap, Math.max(0, k), Math.max(0, m), Math.min(i, paramInt1), Math.min(j, paramInt2));
    }
    
    protected Drawable doInBackground(Void... paramVarArgs)
    {
      int i = Calendar.getInstance().get(11);
      int j = 2130837582;
      if ((i >= 6) && (i < 8)) {
        j = 2130837579;
      }
      for (;;)
      {
        Bitmap localBitmap = ((BitmapDrawable)PredictiveCardRefreshManager.this.mAppContext.getResources().getDrawable(j)).getBitmap();
        return new BitmapDrawable(PredictiveCardRefreshManager.this.mAppContext.getResources(), scaleAndCropBitmap(localBitmap, this.mWidth, this.mHeight));
        if ((i >= 8) && (i < 20)) {
          j = 2130837580;
        } else if ((i >= 20) && (i < 22)) {
          j = 2130837581;
        }
      }
    }
    
    protected void onPostExecute(@Nullable Drawable paramDrawable)
    {
      PredictiveCardRefreshManager.this.mPresenter.setContextHeader(paramDrawable, false, null);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.PredictiveCardRefreshManager
 * JD-Core Version:    0.7.0.1
 */