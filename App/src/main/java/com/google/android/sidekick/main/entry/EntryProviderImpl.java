package com.google.android.sidekick.main.entry;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.apps.sidekick.EntryProviderData;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.preferences.PredictiveCardsPreferences;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.LocaleUtils;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.sidekick.main.file.AsyncFileStorage;
import com.google.android.sidekick.main.inject.BackgroundImage;
import com.google.android.sidekick.main.inject.ExecutedUserActionStore;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.WidgetManager;
import com.google.android.sidekick.main.location.LocationOracle;
import com.google.android.sidekick.main.notifications.EntryNotification;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.android.sidekick.main.notifications.NotificationStore;
import com.google.android.sidekick.main.notifications.NowNotificationManager;
import com.google.android.sidekick.main.notifications.NowNotificationManager.NotificationType;
import com.google.android.sidekick.main.sync.RepeatedMessageInfo;
import com.google.android.sidekick.main.sync.StateMerge;
import com.google.android.sidekick.main.trigger.TriggerConditionEvaluator;
import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.client.EntriesRefreshRequestType;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.BackgroundPhotoDescriptor;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryChanges;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.PrototypeEntry;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration;
import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class EntryProviderImpl
  implements EntryProvider
{
  private static final Sidekick.Interest INTEREST_FOR_STANDARD_NOTIFICATIONS = new Sidekick.Interest().setTargetDisplay(1);
  private static final String TAG = Tag.getTag(EntryProviderImpl.class);
  private final Context mAppContext;
  private final AsyncFileStorage mAsyncFileStorage;
  private final Executor mBackgroundExecutor;
  private List<BackgroundImage> mBackgroundImagePhotos = null;
  private final Clock mClock;
  private final SearchConfig mConfiguration;
  private ScheduledFuture<?> mDelayedRefresher;
  private final EntryAdapterFactory<EntryNotification> mEntryNotificationFactory;
  private final EntryTreePruner mEntryTreePruner;
  @Nullable
  private ByteStringMicro mEventId;
  private final ScheduledExecutorService mExecutor;
  private final AtomicBoolean mFileStoreReadComplete = new AtomicBoolean();
  private boolean mIncludesMoreEntries = false;
  private Locale mLastRefreshLocale;
  private Sidekick.Location mLastRefreshLocation;
  private final LocationOracle mLocationOracle;
  private long mMainEntriesLastChangeTimeMillis;
  private long mMainEntriesLastRefreshTimeMillis;
  private final Object mMainEntriesLock = new Object();
  private Sidekick.EntryTree mMainEntryTree = null;
  protected BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("com.google.android.apps.sidekick.DATA_BACKEND_VERSION_STORE".equals(paramAnonymousIntent.getAction()))
      {
        EntryProviderImpl.this.cancelDelayedRefresh();
        EntryProviderImpl.this.refreshEntries(paramAnonymousIntent.getExtras());
      }
    }
  };
  private final NotificationStore mNotificationStore;
  private final EntryProviderObservable mObservable;
  private final PredictiveCardsPreferences mPredictiveCardsPreferences;
  private final Object mRefresherLock = new Object();
  private final SidekickInjector mSidekickInjector;
  
  public EntryProviderImpl(Clock paramClock, Context paramContext, AsyncFileStorage paramAsyncFileStorage, NotificationStore paramNotificationStore, LocationOracle paramLocationOracle, SearchConfig paramSearchConfig, Executor paramExecutor, SidekickInjector paramSidekickInjector, ScheduledExecutorService paramScheduledExecutorService, PredictiveCardsPreferences paramPredictiveCardsPreferences, EntryAdapterFactory<EntryNotification> paramEntryAdapterFactory, EntryTreePruner paramEntryTreePruner)
  {
    this.mClock = paramClock;
    this.mAppContext = paramContext;
    this.mAsyncFileStorage = paramAsyncFileStorage;
    this.mNotificationStore = paramNotificationStore;
    this.mLocationOracle = paramLocationOracle;
    this.mConfiguration = paramSearchConfig;
    this.mBackgroundExecutor = paramExecutor;
    this.mSidekickInjector = paramSidekickInjector;
    this.mExecutor = paramScheduledExecutorService;
    this.mPredictiveCardsPreferences = paramPredictiveCardsPreferences;
    this.mEntryNotificationFactory = paramEntryAdapterFactory;
    this.mObservable = new EntryProviderObservable(this.mAppContext);
    this.mEntryTreePruner = paramEntryTreePruner;
    LocalBroadcastManager.getInstance(paramContext).registerReceiver(this.mMessageReceiver, new IntentFilter("com.google.android.apps.sidekick.DATA_BACKEND_VERSION_STORE"));
  }
  
  private void doInitializeFromStorage()
  {
    if (!this.mPredictiveCardsPreferences.isSavedConfigurationVersionCurrent())
    {
      this.mFileStoreReadComplete.set(true);
      invalidate();
      return;
    }
    this.mAsyncFileStorage.readFromEncryptedFile("entry_provider", new Function()
    {
      public Void apply(byte[] paramAnonymousArrayOfByte)
      {
        i = 1;
        if (paramAnonymousArrayOfByte != null) {}
        try
        {
          localEntryProviderData = new EntryProviderData();
          localEntryProviderData.mergeFrom(paramAnonymousArrayOfByte);
          long l1 = EntryProviderImpl.this.mClock.currentTimeMillis();
          long l2 = localEntryProviderData.getLastRefreshMillis();
          long l3 = l1 - l2;
          if (l3 >= 0L) {
            if (l3 <= 3600000L) {
              break label87;
            }
          }
        }
        catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
        {
          for (;;)
          {
            EntryProviderData localEntryProviderData;
            boolean bool1;
            Locale localLocale;
            boolean bool2;
            EntryProviderImpl localEntryProviderImpl;
            Sidekick.EntryResponse localEntryResponse;
            Sidekick.Location localLocation;
            long l4;
            int j;
            Log.e(EntryProviderImpl.TAG, "File storage contained invalid data");
            EntryProviderImpl.this.mFileStoreReadComplete.set(true);
            if (i != 0) {
              EntryProviderImpl.this.invalidate();
            }
          }
        }
        finally
        {
          EntryProviderImpl.this.mFileStoreReadComplete.set(true);
          if (i == 0) {
            break label248;
          }
          EntryProviderImpl.this.invalidate();
        }
        return null;
        label87:
        bool1 = localEntryProviderData.hasLocale();
        localLocale = null;
        if (bool1) {
          localLocale = LocaleUtils.parseJavaLocale(localEntryProviderData.getLocale(), null);
        }
        EntryProviderImpl.this.mFileStoreReadComplete.set(true);
        bool2 = localEntryProviderData.getIncludesMoreCards();
        localEntryProviderImpl = EntryProviderImpl.this;
        localEntryResponse = localEntryProviderData.getEntryResponse();
        localLocation = localEntryProviderData.getLocation();
        l4 = localEntryProviderData.getLastRefreshMillis();
        if (bool2) {}
        for (j = 3;; j = 2)
        {
          localEntryProviderImpl.updateFromEntryResponseInternal(localEntryResponse, localLocation, l4, j, localLocale, false, null);
          i = 0;
          break;
        }
      }
    });
  }
  
  private int getEntryCount(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    int i;
    if (paramEntryTreeNode.hasGroupEntry()) {
      i = 1;
    }
    for (;;)
    {
      return i;
      if (paramEntryTreeNode.getEntryCount() > 0) {
        return paramEntryTreeNode.getEntryCount();
      }
      if (paramEntryTreeNode.getChildCount() <= 0) {
        break;
      }
      i = 0;
      Iterator localIterator = paramEntryTreeNode.getChildList().iterator();
      while (localIterator.hasNext()) {
        i += getEntryCount((Sidekick.EntryTreeNode)localIterator.next());
      }
    }
    return 0;
  }
  
  private void insertNewEntries(Iterable<Sidekick.Entry> paramIterable, Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      paramEntryTreeNode.getChildList().add(0, new Sidekick.EntryTreeNode().addEntry(localEntry));
    }
  }
  
  private void mutateEntries(EntryTreeVisitor paramEntryTreeVisitor)
  {
    synchronized (this.mMainEntriesLock)
    {
      Sidekick.EntryTree localEntryTree1 = this.mMainEntryTree;
      int i = 0;
      if (localEntryTree1 != null)
      {
        Sidekick.EntryTree localEntryTree2 = (Sidekick.EntryTree)ProtoUtils.copyOf(this.mMainEntryTree);
        paramEntryTreeVisitor.visitWithNotifying(localEntryTree2.getRoot());
        this.mMainEntryTree = localEntryTree2;
        this.mMainEntriesLastChangeTimeMillis = this.mClock.currentTimeMillis();
        i = 1;
      }
      if (i != 0)
      {
        this.mSidekickInjector.getWidgetManager().updateWidget();
        updateStoredEntries(paramEntryTreeVisitor);
      }
      return;
    }
  }
  
  private boolean notifyForEntryTreeNode(NowNotificationManager paramNowNotificationManager, Sidekick.EntryTreeNode paramEntryTreeNode, boolean paramBoolean)
  {
    if (paramEntryTreeNode.hasGroupEntry()) {
      paramBoolean = maybeShowNotification(paramNowNotificationManager, paramBoolean, (EntryNotification)this.mEntryNotificationFactory.createForGroup(paramEntryTreeNode));
    }
    for (;;)
    {
      return paramBoolean;
      Iterator localIterator1 = paramEntryTreeNode.getEntryList().iterator();
      while (localIterator1.hasNext())
      {
        Sidekick.Entry localEntry = (Sidekick.Entry)localIterator1.next();
        paramBoolean = maybeShowNotification(paramNowNotificationManager, paramBoolean, (EntryNotification)this.mEntryNotificationFactory.create(localEntry));
      }
      Iterator localIterator2 = paramEntryTreeNode.getChildList().iterator();
      while (localIterator2.hasNext()) {
        paramBoolean = notifyForEntryTreeNode(paramNowNotificationManager, (Sidekick.EntryTreeNode)localIterator2.next(), paramBoolean);
      }
    }
  }
  
  private boolean showNotification(NowNotificationManager paramNowNotificationManager, EntryNotification paramEntryNotification, boolean paramBoolean)
  {
    NowNotificationManager.NotificationType localNotificationType = paramEntryNotification.getNotificationId();
    boolean bool = paramEntryNotification.getEntries().isEmpty();
    PendingIntent localPendingIntent = null;
    if (!bool) {
      localPendingIntent = NotificationRefreshService.getNotificationDismissIntent(this.mAppContext, paramEntryNotification.getEntries(), localNotificationType);
    }
    Notification localNotification = paramNowNotificationManager.createNotification(paramEntryNotification, localPendingIntent, paramBoolean);
    if (localNotification != null)
    {
      Iterator localIterator1 = paramEntryNotification.getEntries().iterator();
      while (localIterator1.hasNext())
      {
        Sidekick.Entry localEntry = (Sidekick.Entry)localIterator1.next();
        this.mNotificationStore.setOrAddNotification(localEntry, INTEREST_FOR_STANDARD_NOTIFICATIONS);
      }
      paramNowNotificationManager.showNotification(localNotification, localNotificationType);
      if (paramEntryNotification.isActiveNotification())
      {
        Iterator localIterator2 = paramEntryNotification.getEntries().iterator();
        while (localIterator2.hasNext()) {
          paramNowNotificationManager.sendDeliverActiveNotification((Sidekick.Entry)localIterator2.next());
        }
      }
      return true;
    }
    return false;
  }
  
  private void updateFromEntryResponseInternal(Sidekick.EntryResponse paramEntryResponse, Sidekick.Location paramLocation, long paramLong, int paramInt, @Nullable Locale paramLocale, boolean paramBoolean, @Nullable ByteStringMicro paramByteStringMicro)
  {
    this.mEntryTreePruner.prune(paramEntryResponse);
    int i;
    int j;
    synchronized (this.mMainEntriesLock)
    {
      if (paramEntryResponse.getBackgroundImageCount() > 0)
      {
        this.mBackgroundImagePhotos = Lists.newLinkedList();
        if (paramEntryResponse.getBackgroundImageDescriptorCount() <= 0) {
          break label239;
        }
        i = 1;
        break label233;
        while (j < paramEntryResponse.getBackgroundImageCount())
        {
          Sidekick.Photo localPhoto = paramEntryResponse.getBackgroundImage(j);
          Sidekick.BackgroundPhotoDescriptor localBackgroundPhotoDescriptor;
          if (i != 0)
          {
            localBackgroundPhotoDescriptor = paramEntryResponse.getBackgroundImageDescriptor(j);
            this.mBackgroundImagePhotos.add(new BackgroundImage(localPhoto, localBackgroundPhotoDescriptor));
            j++;
          }
          else
          {
            localBackgroundPhotoDescriptor = new Sidekick.BackgroundPhotoDescriptor();
          }
        }
      }
    }
    if (paramEntryResponse.getEntryTreeCount() > 0) {}
    for (Sidekick.EntryTree localEntryTree = paramEntryResponse.getEntryTree(0);; localEntryTree = null)
    {
      this.mMainEntryTree = localEntryTree;
      this.mEventId = paramByteStringMicro;
      this.mIncludesMoreEntries = EntriesRefreshRequestType.isMore(paramInt);
      this.mMainEntriesLastRefreshTimeMillis = paramLong;
      this.mLastRefreshLocation = paramLocation;
      this.mLastRefreshLocale = paramLocale;
      Bundle localBundle = null;
      if (paramBoolean)
      {
        localBundle = new Bundle();
        localBundle.putBoolean("reminder_updated", true);
      }
      this.mObservable.notifyRefreshed(localBundle, paramInt);
      this.mSidekickInjector.getWidgetManager().updateWidget();
      return;
    }
    for (;;)
    {
      label233:
      j = 0;
      break;
      label239:
      i = 0;
    }
  }
  
  private boolean updateFromPartialEntriesLocked(Sidekick.EntryChanges paramEntryChanges)
  {
    UpdatePartialEntriesVisitor localUpdatePartialEntriesVisitor1 = new UpdatePartialEntriesVisitor(this.mObservable, paramEntryChanges.getUpdatesList(), false, false);
    UpdatePartialEntriesVisitor localUpdatePartialEntriesVisitor2 = new UpdatePartialEntriesVisitor(this.mObservable, paramEntryChanges.getUpdateOnlyList(), false, true);
    if (paramEntryChanges.getUpdatesCount() > 0) {
      mutateEntries(localUpdatePartialEntriesVisitor1);
    }
    if (paramEntryChanges.getUpdateOnlyCount() > 0) {
      mutateEntries(localUpdatePartialEntriesVisitor2);
    }
    if (localUpdatePartialEntriesVisitor1.getLeftovers().size() > 0)
    {
      final ArrayList localArrayList = Lists.newArrayList();
      Iterator localIterator = localUpdatePartialEntriesVisitor1.getLeftovers().iterator();
      while (localIterator.hasNext())
      {
        Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
        if ((!localEntry.hasEncodedEventId()) && (this.mEventId != null)) {
          localEntry.setEncodedEventId(this.mEventId);
        }
        FindPrototype localFindPrototype = new FindPrototype(localEntry);
        if (this.mMainEntryTree != null) {
          localFindPrototype.visitWithNotifying(this.mMainEntryTree.getRoot());
        }
        if (localEntry.hasPublicAlertEntry())
        {
          Optional localOptional = localFindPrototype.getPrototype();
          long l = localEntry.getEntryUpdateId();
          if ((localOptional.isPresent()) && (((Sidekick.PrototypeEntry)localOptional.get()).getDismissedEntriesIdList().contains(Long.valueOf(l)))) {}
          for (boolean bool = true;; bool = false)
          {
            if (!bool) {
              bool = this.mSidekickInjector.getExecutedUserActionStore().hasExecutedUserAction(localEntry, new Sidekick.Action().setType(1));
            }
            if (bool) {
              break;
            }
            TriggerConditionEvaluator localTriggerConditionEvaluator = this.mSidekickInjector.getTriggerConditionEvaluator();
            if ((localEntry.hasTriggerCondition()) && (!localTriggerConditionEvaluator.evaluate(localEntry.getTriggerCondition(), null))) {
              break;
            }
            localArrayList.add(localEntry);
            break;
          }
        }
      }
      if (localArrayList.size() > 0)
      {
        Sidekick.EntryTree localEntryTree = (Sidekick.EntryTree)ProtoUtils.copyOf(this.mMainEntryTree);
        insertNewEntries(localArrayList, localEntryTree.getRoot());
        this.mMainEntriesLastChangeTimeMillis = this.mClock.currentTimeMillis();
        this.mMainEntryTree = localEntryTree;
        EntryTreeVisitor local5 = new EntryTreeVisitor()
        {
          protected void processRoot(Sidekick.EntryTreeNode paramAnonymousEntryTreeNode)
          {
            EntryProviderImpl.this.insertNewEntries(localArrayList, paramAnonymousEntryTreeNode);
          }
        };
        updateStoredEntries(local5);
        return true;
      }
    }
    return false;
  }
  
  private void updateStoredEntries(final EntryTreeVisitor paramEntryTreeVisitor)
  {
    this.mAsyncFileStorage.updateEncryptedFile("entry_provider", new Function()
    {
      public byte[] apply(byte[] paramAnonymousArrayOfByte)
      {
        if ((paramAnonymousArrayOfByte == null) || (paramAnonymousArrayOfByte.length == 0)) {}
        for (;;)
        {
          return null;
          try
          {
            EntryProviderData localEntryProviderData = new EntryProviderData();
            localEntryProviderData.mergeFrom(paramAnonymousArrayOfByte);
            if ((localEntryProviderData.getLastRefreshMillis() == EntryProviderImpl.this.mMainEntriesLastRefreshTimeMillis) && (localEntryProviderData.hasEntryResponse()))
            {
              Sidekick.EntryResponse localEntryResponse = localEntryProviderData.getEntryResponse();
              if (localEntryResponse.getEntryTreeCount() != 0)
              {
                Sidekick.EntryTree localEntryTree = localEntryResponse.getEntryTree(0);
                if (localEntryTree.hasRoot())
                {
                  paramEntryTreeVisitor.visitWithoutNotifying(localEntryTree.getRoot());
                  byte[] arrayOfByte = localEntryProviderData.toByteArray();
                  return arrayOfByte;
                }
              }
            }
          }
          catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
          {
            Log.e(EntryProviderImpl.TAG, "File storage contained invalid data");
          }
        }
        return null;
      }
    });
  }
  
  public void appendMoreCardEntries(Sidekick.EntryResponse paramEntryResponse, Location paramLocation)
  {
    this.mEntryTreePruner.prune(paramEntryResponse);
    Sidekick.EntryTree localEntryTree;
    synchronized (this.mMainEntriesLock)
    {
      if (this.mMainEntryTree == null) {
        return;
      }
      if ((paramEntryResponse == null) || (paramEntryResponse.getEntryTreeCount() <= 0) || (!paramEntryResponse.getEntryTree(0).hasRoot())) {
        break label139;
      }
      localEntryTree = (Sidekick.EntryTree)ProtoUtils.copyOf(this.mMainEntryTree);
      Iterator localIterator = paramEntryResponse.getEntryTree(0).getRoot().getChildList().iterator();
      if (localIterator.hasNext())
      {
        Sidekick.EntryTreeNode localEntryTreeNode = (Sidekick.EntryTreeNode)localIterator.next();
        localEntryTree.getRoot().addChild(localEntryTreeNode);
      }
    }
    this.mMainEntryTree = localEntryTree;
    this.mMainEntriesLastChangeTimeMillis = this.mClock.currentTimeMillis();
    label139:
    this.mIncludesMoreEntries = true;
    if ((paramEntryResponse != null) && (paramEntryResponse.getEntryTreeCount() > 0)) {
      this.mObservable.notifyEntriesAdded(paramEntryResponse.getEntryTree(0));
    }
    this.mSidekickInjector.getWidgetManager().updateWidget();
  }
  
  public void cancelDelayedRefresh()
  {
    synchronized (this.mRefresherLock)
    {
      if (this.mDelayedRefresher != null)
      {
        this.mDelayedRefresher.cancel(false);
        this.mDelayedRefresher = null;
      }
      return;
    }
  }
  
  public boolean entriesIncludeMore()
  {
    synchronized (this.mMainEntriesLock)
    {
      boolean bool = this.mIncludesMoreEntries;
      return bool;
    }
  }
  
  public List<BackgroundImage> getBackgroundImagePhotos()
  {
    synchronized (this.mMainEntriesLock)
    {
      List localList = this.mBackgroundImagePhotos;
      return localList;
    }
  }
  
  public Sidekick.EntryTree getEntryTree()
  {
    synchronized (this.mMainEntriesLock)
    {
      Sidekick.EntryTree localEntryTree = this.mMainEntryTree;
      return localEntryTree;
    }
  }
  
  public long getLastChangeTimeMillis()
  {
    synchronized (this.mMainEntriesLock)
    {
      long l = Math.max(this.mMainEntriesLastRefreshTimeMillis, this.mMainEntriesLastChangeTimeMillis);
      return l;
    }
  }
  
  @Nullable
  public Sidekick.Location getLastRefreshLocation()
  {
    synchronized (this.mMainEntriesLock)
    {
      Sidekick.Location localLocation = this.mLastRefreshLocation;
      return localLocation;
    }
  }
  
  public long getLastRefreshTimeMillis()
  {
    synchronized (this.mMainEntriesLock)
    {
      long l = this.mMainEntriesLastRefreshTimeMillis;
      return l;
    }
  }
  
  public int getTotalEntryCount()
  {
    Sidekick.EntryTree localEntryTree = getEntryTree();
    if ((localEntryTree == null) || (!localEntryTree.hasRoot())) {
      return 0;
    }
    return getEntryCount(localEntryTree.getRoot());
  }
  
  public void handleDismissedEntries(Collection<Sidekick.Entry> paramCollection)
  {
    if ((paramCollection == null) || (paramCollection.isEmpty())) {
      return;
    }
    HashSet localHashSet = Sets.newHashSetWithExpectedSize(paramCollection.size());
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      if (localEntry != null) {
        localHashSet.add(new ProtoKey(localEntry));
      }
    }
    mutateEntries(new EntryRemover(this.mObservable, localHashSet));
  }
  
  public boolean hasLocationChangedSignificantlySinceRefresh()
  {
    Sidekick.Location localLocation = getLastRefreshLocation();
    if (localLocation == null) {}
    Location localLocation1;
    do
    {
      return false;
      localLocation1 = this.mLocationOracle.getBestLocation();
    } while ((localLocation1 == null) || (LocationUtilities.distanceBetween(localLocation1, LocationUtilities.sidekickLocationToAndroidLocation(localLocation)) <= this.mConfiguration.getMarinerMaximumStaleDataRefreshDistanceMeters()));
    return true;
  }
  
  public boolean hasPendingRefresh()
  {
    for (;;)
    {
      synchronized (this.mRefresherLock)
      {
        if (this.mDelayedRefresher != null)
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public void initializeFromStorage()
  {
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        EntryProviderImpl.this.doInitializeFromStorage();
      }
    });
  }
  
  public void invalidate()
  {
    synchronized (this.mMainEntriesLock)
    {
      this.mMainEntriesLastRefreshTimeMillis = 0L;
      this.mMainEntryTree = null;
      this.mEventId = null;
      this.mBackgroundImagePhotos = null;
      this.mLastRefreshLocation = null;
      this.mLastRefreshLocale = null;
      this.mObservable.notifyInvalidated();
      this.mSidekickInjector.getWidgetManager().updateWidget();
      this.mAsyncFileStorage.deleteFile("entry_provider");
      return;
    }
  }
  
  public boolean invalidateIfEntriesAreStale()
  {
    synchronized (this.mMainEntriesLock)
    {
      long l = getLastRefreshTimeMillis();
      if (this.mClock.currentTimeMillis() - l > 3600000L)
      {
        invalidate();
        return true;
      }
      return false;
    }
  }
  
  public void invalidateWithDelayedRefresh()
  {
    invalidate();
    synchronized (this.mRefresherLock)
    {
      DelayedRefresher localDelayedRefresher = new DelayedRefresher(null);
      cancelDelayedRefresh();
      this.mDelayedRefresher = this.mExecutor.schedule(localDelayedRefresher, 10000L, TimeUnit.MILLISECONDS);
      return;
    }
  }
  
  public void invalidateWithImmediateRefresh()
  {
    invalidate();
    cancelDelayedRefresh();
    refreshEntries(null);
  }
  
  public boolean isDataForLocale(@Nullable Locale paramLocale)
  {
    for (;;)
    {
      synchronized (this.mMainEntriesLock)
      {
        if (this.mLastRefreshLocale != null)
        {
          if (!this.mLastRefreshLocale.equals(paramLocale)) {
            break label44;
          }
          break label38;
          return bool;
        }
      }
      label38:
      boolean bool = true;
      continue;
      label44:
      bool = false;
    }
  }
  
  public boolean isInitializedFromStorage()
  {
    return this.mFileStoreReadComplete.get();
  }
  
  boolean maybeShowNotification(NowNotificationManager paramNowNotificationManager, boolean paramBoolean, @Nullable EntryNotification paramEntryNotification)
  {
    if (paramEntryNotification != null)
    {
      if (!paramEntryNotification.isLowPriorityNotification()) {
        break label27;
      }
      if (!paramBoolean) {
        paramBoolean = showNotification(paramNowNotificationManager, paramEntryNotification, true);
      }
    }
    label27:
    label158:
    label180:
    label184:
    for (;;)
    {
      return paramBoolean;
      if (!paramEntryNotification.doNotSuppress()) {}
      boolean bool;
      Collection localCollection;
      int k;
      int m;
      for (int i = 1;; i = 0)
      {
        bool = true;
        j = 0;
        if (i == 0) {
          break label158;
        }
        localCollection = paramEntryNotification.getEntries();
        k = 0;
        m = 0;
        Iterator localIterator = localCollection.iterator();
        while (localIterator.hasNext())
        {
          Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
          int n = this.mNotificationStore.getNotificationState(localEntry);
          if ((n & 0x1) != 0) {
            k++;
          }
          if ((n & 0x2) != 0) {
            m++;
          }
        }
      }
      if (k == 0)
      {
        bool = true;
        if (m != localCollection.size()) {
          break label180;
        }
      }
      for (int j = 1;; j = 0)
      {
        if (j != 0) {
          break label184;
        }
        showNotification(paramNowNotificationManager, paramEntryNotification, bool);
        return paramBoolean;
        bool = false;
        break;
      }
    }
  }
  
  public void notifyAboutGoogleNowDisabled(int paramInt)
  {
    this.mObservable.notifyGoogleNowDisabled(paramInt);
  }
  
  public void notifyAboutRefreshFailure(int paramInt, boolean paramBoolean)
  {
    this.mObservable.notifyRefreshFailed(paramInt, paramBoolean);
  }
  
  public void notifyAboutRefreshStarting(int paramInt)
  {
    this.mObservable.notifyRefreshStarting(paramInt);
  }
  
  void notifyForCards(NowNotificationManager paramNowNotificationManager)
  {
    Sidekick.EntryTree localEntryTree = getEntryTree();
    if ((localEntryTree != null) && (localEntryTree.hasRoot())) {
      notifyForEntryTreeNode(paramNowNotificationManager, localEntryTree.getRoot(), false);
    }
  }
  
  public void refreshEntries(@Nullable Bundle paramBundle)
  {
    Intent localIntent = new Intent(this.mAppContext, EntriesRefreshIntentService.class);
    localIntent.setAction("com.google.android.apps.sidekick.REFRESH");
    localIntent.putExtra("com.google.android.apps.sidekick.TYPE", 2);
    if (paramBundle != null) {
      localIntent.putExtras(paramBundle);
    }
    this.mAppContext.startService(localIntent);
  }
  
  public void refreshEntriesPreserveMoreState()
  {
    cancelDelayedRefresh();
    Bundle localBundle = new Bundle();
    if (entriesIncludeMore()) {
      localBundle.putInt("com.google.android.apps.sidekick.TYPE", 3);
    }
    for (;;)
    {
      refreshEntries(localBundle);
      return;
      localBundle.putInt("com.google.android.apps.sidekick.TYPE", 2);
    }
  }
  
  public void refreshNowIfDelayedRefreshInFlight()
  {
    synchronized (this.mRefresherLock)
    {
      if (this.mDelayedRefresher != null) {
        invalidateWithImmediateRefresh();
      }
      return;
    }
  }
  
  public void registerEntryProviderObserver(EntryProviderObserver paramEntryProviderObserver)
  {
    this.mObservable.registerObserver(paramEntryProviderObserver);
  }
  
  public void removeGroupChildEntries(Sidekick.Entry paramEntry, Collection<Sidekick.Entry> paramCollection)
  {
    HashSet localHashSet = Sets.newHashSetWithExpectedSize(paramCollection.size());
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      if (localEntry != null) {
        localHashSet.add(new ProtoKey(localEntry));
      }
    }
    ProtoKey localProtoKey = new ProtoKey(paramEntry);
    mutateEntries(new ChildEntryRemover(this.mObservable, localProtoKey, localHashSet));
  }
  
  void setLastRefreshLocationForTest(Location paramLocation)
  {
    synchronized (this.mMainEntriesLock)
    {
      this.mLastRefreshLocation = LocationUtilities.androidLocationToSidekickLocation(paramLocation);
      return;
    }
  }
  
  void setMainEntryTree(Sidekick.EntryTree paramEntryTree)
  {
    synchronized (this.mMainEntriesLock)
    {
      this.mMainEntryTree = paramEntryTree;
      return;
    }
  }
  
  public void updateEntries(EntryUpdater.EntryUpdaterFunc paramEntryUpdaterFunc)
  {
    mutateEntries(new EntryUpdater(this.mObservable, paramEntryUpdaterFunc));
  }
  
  public void updateFromEntryResponse(Sidekick.EntryResponse paramEntryResponse, int paramInt, @Nullable Location paramLocation, @Nullable Locale paramLocale, boolean paramBoolean, @Nullable ByteStringMicro paramByteStringMicro)
  {
    if (paramLocation != null) {}
    for (Sidekick.Location localLocation = LocationUtilities.androidLocationToSidekickLocation(paramLocation);; localLocation = null)
    {
      long l = this.mClock.currentTimeMillis();
      updateFromEntryResponseInternal(paramEntryResponse, localLocation, l, paramInt, paramLocale, paramBoolean, paramByteStringMicro);
      NowNotificationManager localNowNotificationManager = this.mSidekickInjector.getNowNotificationManager();
      localNowNotificationManager.cancelNotification(NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION);
      notifyForCards(localNowNotificationManager);
      if (paramLocation != null)
      {
        EntryProviderData localEntryProviderData = new EntryProviderData().setLastRefreshMillis(l).setEntryResponse(paramEntryResponse).setIncludesMoreCards(EntriesRefreshRequestType.isMore(paramInt)).setLocation(LocationUtilities.androidLocationToSidekickLocation(paramLocation));
        if (paramLocale != null) {
          localEntryProviderData.setLocale(paramLocale.toString());
        }
        byte[] arrayOfByte = localEntryProviderData.toByteArray();
        this.mAsyncFileStorage.writeToEncryptedFile("entry_provider", arrayOfByte);
      }
      return;
    }
  }
  
  public void updateFromPartialEntries(Sidekick.EntryChanges paramEntryChanges)
  {
    for (;;)
    {
      synchronized (this.mMainEntriesLock)
      {
        boolean bool = updateFromPartialEntriesLocked(paramEntryChanges);
        if (bool)
        {
          this.mObservable.notifyRefreshed(null, 5);
          this.mSidekickInjector.getWidgetManager().updateWidget();
        }
        Sidekick.SidekickConfiguration localSidekickConfiguration = this.mPredictiveCardsPreferences.getWorkingConfiguration();
        if ((localSidekickConfiguration != null) && (localSidekickConfiguration.getNotificationOverride() == 2))
        {
          i = 1;
          if (i == 0) {
            break;
          }
          return;
        }
      }
      int i = 0;
    }
    notifyForCards(this.mSidekickInjector.getNowNotificationManager());
  }
  
  public void updateFromPartialEntryResponse(Sidekick.EntryResponse paramEntryResponse)
  {
    ArrayList localArrayList = Lists.newArrayList();
    if ((paramEntryResponse != null) && (paramEntryResponse.getEntryTreeCount() > 0))
    {
      Sidekick.EntryTreeNode localEntryTreeNode1 = paramEntryResponse.getEntryTree(0).getRoot();
      if (localEntryTreeNode1 != null)
      {
        Iterator localIterator1 = localEntryTreeNode1.getChildList().iterator();
        while (localIterator1.hasNext())
        {
          Sidekick.EntryTreeNode localEntryTreeNode2 = (Sidekick.EntryTreeNode)localIterator1.next();
          if (localEntryTreeNode2.getEntryCount() > 0)
          {
            Iterator localIterator2 = localEntryTreeNode2.getEntryList().iterator();
            while (localIterator2.hasNext())
            {
              Sidekick.Entry localEntry = (Sidekick.Entry)localIterator2.next();
              localEntry.clearEntryAction();
              localArrayList.add(localEntry);
            }
          }
        }
      }
    }
    if (!localArrayList.isEmpty()) {
      mutateEntries(new UpdatePartialEntriesVisitor(this.mObservable, localArrayList, true, true));
    }
  }
  
  private class DelayedRefresher
    implements Runnable
  {
    private DelayedRefresher() {}
    
    public void run()
    {
      synchronized (EntryProviderImpl.this.mRefresherLock)
      {
        if (EntryProviderImpl.this.mDelayedRefresher == null) {
          return;
        }
        EntryProviderImpl.access$802(EntryProviderImpl.this, null);
        EntryProviderImpl.this.mSidekickInjector.getWidgetManager().updateWidget();
        EntryProviderImpl.this.refreshEntries(null);
        return;
      }
    }
  }
  
  static class FindPrototype
    extends EntryTreeVisitor
  {
    final Sidekick.Entry mNewEntry;
    Optional<Sidekick.PrototypeEntry> mPrototypeEntry = Optional.absent();
    
    FindPrototype(Sidekick.Entry paramEntry)
    {
      this.mNewEntry = paramEntry;
    }
    
    Optional<Sidekick.PrototypeEntry> getPrototype()
    {
      return this.mPrototypeEntry;
    }
    
    protected void process(ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Entry paramEntry)
    {
      if ((paramEntry.hasPrototypeEntry()) && (paramEntry.getPrototypeEntry().getType() == this.mNewEntry.getType())) {
        this.mPrototypeEntry = Optional.of(paramEntry.getPrototypeEntry());
      }
    }
  }
  
  static class UpdatePartialEntriesVisitor
    extends EntryTreeVisitor
  {
    static final RepeatedMessageInfo primaryKeys = new RepeatedMessageInfo();
    final Iterable<Sidekick.Entry> mEntryUpdates;
    final List<Sidekick.Entry> mLeftovers;
    final EntryProviderObservable mObservable;
    final boolean mReplaceEntries;
    final boolean mUpdateOnly;
    
    UpdatePartialEntriesVisitor(EntryProviderObservable paramEntryProviderObservable, Iterable<Sidekick.Entry> paramIterable, boolean paramBoolean1, boolean paramBoolean2)
    {
      this.mObservable = paramEntryProviderObservable;
      this.mEntryUpdates = paramIterable;
      this.mReplaceEntries = paramBoolean1;
      this.mUpdateOnly = paramBoolean2;
      this.mLeftovers = Lists.newArrayList(paramIterable);
    }
    
    private static boolean idMatch(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
    {
      if (!paramEntry2.hasEntryUpdateId()) {}
      while ((paramEntry1.hasEntryUpdateId()) && (paramEntry1.getEntryUpdateId() == paramEntry2.getEntryUpdateId())) {
        return true;
      }
      return false;
    }
    
    private void maybeUpdateEntry(Sidekick.Entry paramEntry)
    {
      Iterator localIterator = this.mEntryUpdates.iterator();
      while (localIterator.hasNext())
      {
        Sidekick.Entry localEntry1 = (Sidekick.Entry)localIterator.next();
        if ((typeMatch(paramEntry, localEntry1)) && (idMatch(paramEntry, localEntry1)) && (updateMoreRecent(paramEntry, localEntry1)))
        {
          this.mLeftovers.remove(localEntry1);
          Sidekick.Entry localEntry2 = (Sidekick.Entry)ProtoUtils.copyOf(paramEntry);
          updateEntry(paramEntry, localEntry1);
          if (shouldNotify()) {
            this.mObservable.notifyEntryUpdate(localEntry2, paramEntry, localEntry1);
          }
        }
      }
    }
    
    private static boolean typeMatch(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
    {
      return paramEntry1.getType() == paramEntry2.getType();
    }
    
    private void updateEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
    {
      if (this.mReplaceEntries)
      {
        paramEntry1.clear();
        ProtoUtils.copyOf(paramEntry2, paramEntry1);
        return;
      }
      try
      {
        StateMerge.newStateMergeFor(paramEntry1, primaryKeys, this.mUpdateOnly).applyUpdates(paramEntry2);
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e(EntryProviderImpl.TAG, "Failure to merge a partial entry", localRuntimeException);
      }
    }
    
    private static boolean updateMoreRecent(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
    {
      if ((!paramEntry1.hasEntryUpdateTimestampMillis()) || (!paramEntry2.hasEntryUpdateTimestampMillis())) {}
      while (paramEntry2.getEntryUpdateTimestampMillis() > paramEntry1.getEntryUpdateTimestampMillis()) {
        return true;
      }
      return false;
    }
    
    List<Sidekick.Entry> getLeftovers()
    {
      return this.mLeftovers;
    }
    
    protected void process(ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Entry paramEntry)
    {
      maybeUpdateEntry(paramEntry);
    }
    
    protected void process(Sidekick.EntryTreeNode paramEntryTreeNode)
    {
      if (paramEntryTreeNode.hasGroupEntry()) {
        maybeUpdateEntry(paramEntryTreeNode.getGroupEntry());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryProviderImpl
 * JD-Core Version:    0.7.0.1
 */