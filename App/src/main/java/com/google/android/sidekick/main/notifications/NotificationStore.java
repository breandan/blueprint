package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.apps.sidekick.notifications.Notifications.ClientData;
import com.google.android.apps.sidekick.notifications.Notifications.PendingNotification;
import com.google.android.apps.sidekick.notifications.Notifications.PendingRefresh;
import com.google.android.search.core.AsyncServices;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.sidekick.main.entry.EntryProviderObserver;
import com.google.android.sidekick.main.entry.EntryTreeVisitor;
import com.google.android.sidekick.main.file.FileBytesReader;
import com.google.android.sidekick.main.file.FileBytesWriter;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Interest;
import com.google.geo.sidekick.Sidekick.Notification;
import com.google.geo.sidekick.Sidekick.ReminderEntry;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class NotificationStore
  implements EntryProviderObserver
{
  public static final String FILE_NAME = "notifications_store";
  private static final Function<Notifications.PendingNotification, Sidekick.Notification> GET_NOTIFICATION = new Function()
  {
    public Sidekick.Notification apply(Notifications.PendingNotification paramAnonymousPendingNotification)
    {
      return paramAnonymousPendingNotification.getEntry().getNotification();
    }
  };
  private static final Function<Sidekick.Entry, Sidekick.Notification> GET_NOTIFICATION_FROM_ENTRY = new Function()
  {
    @Nullable
    public Sidekick.Notification apply(Sidekick.Entry paramAnonymousEntry)
    {
      return paramAnonymousEntry.getNotification();
    }
  };
  private static final Predicate<Sidekick.Notification> SUPPORTED;
  private static final String TAG = Tag.getTag(NotificationStore.class);
  private static final Predicate<Sidekick.Notification> UNSUPPORTED;
  private final Context mAppContext;
  private final AsyncServices mAsyncServices;
  private volatile Notifications.ClientData mClientData;
  private final Clock mClock;
  private final FileBytesReader mFileBytesReader;
  private final FileBytesWriter mFileBytesWriter;
  private final AtomicBoolean mInitialized = new AtomicBoolean();
  private final CountDownLatch mInitializedLatch = new CountDownLatch(1);
  private final Object mWriteLock = new Object();
  
  static
  {
    SUPPORTED = new Predicate()
    {
      public boolean apply(@Nullable Sidekick.Notification paramAnonymousNotification)
      {
        if (paramAnonymousNotification == null) {}
        while (PendingNotificationAdapter.getAdapter(paramAnonymousNotification) == null) {
          return false;
        }
        return true;
      }
    };
    UNSUPPORTED = Predicates.not(SUPPORTED);
  }
  
  public NotificationStore(FileBytesReader paramFileBytesReader, FileBytesWriter paramFileBytesWriter, Clock paramClock, Context paramContext, AsyncServices paramAsyncServices)
  {
    this.mFileBytesReader = paramFileBytesReader;
    this.mFileBytesWriter = paramFileBytesWriter;
    this.mClock = paramClock;
    this.mAppContext = paramContext;
    this.mAsyncServices = paramAsyncServices;
  }
  
  private void flush()
  {
    ExtraPreconditions.checkNotMainThread();
    ExtraPreconditions.checkHoldsLock(this.mWriteLock);
    this.mFileBytesWriter.writeEncryptedFileBytes("notifications_store", this.mClientData.toByteArray(), 524288);
  }
  
  private List<Sidekick.Entry> getEntriesWithNotifications(Sidekick.EntryTree paramEntryTree)
  {
    if (paramEntryTree.hasRoot())
    {
      NotificationFinder localNotificationFinder = new NotificationFinder(null);
      localNotificationFinder.visit(paramEntryTree.getRoot());
      return localNotificationFinder.entriesWithNotifications;
    }
    return ImmutableList.of();
  }
  
  @Nullable
  private Notifications.PendingNotification getPendingNotificationForEntry(Sidekick.Entry paramEntry)
  {
    if (this.mClientData.getPendingNotificationCount() > 0)
    {
      Iterator localIterator = this.mClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        if (isEquivalent(paramEntry, localPendingNotification.getEntry())) {
          return localPendingNotification;
        }
      }
    }
    return null;
  }
  
  private void helpUpdateNotifications(List<Sidekick.Entry> paramList, Sidekick.Interest paramInterest, boolean paramBoolean)
  {
    Notifications.ClientData localClientData;
    for (;;)
    {
      Iterator localIterator1;
      Object localObject3;
      synchronized (this.mWriteLock)
      {
        localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
        Iterators.removeIf(Iterators.transform(paramList.iterator(), GET_NOTIFICATION_FROM_ENTRY), UNSUPPORTED);
        localIterator1 = localClientData.getPendingNotificationList().iterator();
        ProtoKey localProtoKey = new ProtoKey(paramInterest);
        if (!localIterator1.hasNext()) {
          break;
        }
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator1.next();
        if (!localProtoKey.equals(new ProtoKey(localPendingNotification.getInterest()))) {
          continue;
        }
        localObject3 = null;
        Iterator localIterator3 = paramList.iterator();
        if (localIterator3.hasNext())
        {
          Sidekick.Entry localEntry2 = (Sidekick.Entry)localIterator3.next();
          if (!isEquivalent(localEntry2, localPendingNotification.getEntry())) {
            continue;
          }
          localObject3 = localEntry2;
          localPendingNotification.setEntry(localEntry2);
        }
      }
      if (localObject3 == null)
      {
        if (paramBoolean) {
          localIterator1.remove();
        }
      }
      else {
        paramList.remove(localObject3);
      }
    }
    long l = this.mClock.currentTimeMillis() / 1000L;
    Iterator localIterator2 = paramList.iterator();
    while (localIterator2.hasNext())
    {
      Sidekick.Entry localEntry1 = (Sidekick.Entry)localIterator2.next();
      localClientData.addPendingNotification(new Notifications.PendingNotification().setFirstInsertTimeSeconds(l).setEntry(localEntry1).setInterest(paramInterest));
    }
    this.mClientData = localClientData;
    flush();
  }
  
  private boolean isEquivalent(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
  {
    boolean bool = true;
    if (paramEntry1.getType() != paramEntry2.getType()) {
      return false;
    }
    if ((paramEntry1.hasEntryUpdateId()) && (paramEntry2.hasEntryUpdateId()))
    {
      if (paramEntry1.getEntryUpdateId() == paramEntry2.getEntryUpdateId()) {}
      for (;;)
      {
        return bool;
        bool = false;
      }
    }
    switch (paramEntry1.getType())
    {
    }
    do
    {
      Sidekick.FrequentPlace localFrequentPlace1;
      Sidekick.FrequentPlace localFrequentPlace2;
      do
      {
        Sidekick.FrequentPlaceEntry localFrequentPlaceEntry1;
        Sidekick.FrequentPlaceEntry localFrequentPlaceEntry2;
        do
        {
          return bool;
          if (paramEntry1.hasFrequentPlaceEntry() != paramEntry2.hasFrequentPlaceEntry()) {
            break;
          }
          localFrequentPlaceEntry1 = PlaceUtils.getFrequentPlaceEntry(paramEntry1);
          localFrequentPlaceEntry2 = PlaceUtils.getFrequentPlaceEntry(paramEntry2);
          if ((localFrequentPlaceEntry1.hasFrequentPlace() != localFrequentPlaceEntry2.hasFrequentPlace()) || (localFrequentPlaceEntry1.getEventType() != localFrequentPlaceEntry2.getEventType()) || (localFrequentPlaceEntry1.getEventTimeSeconds() != localFrequentPlaceEntry2.getEventTimeSeconds())) {
            break;
          }
        } while (!localFrequentPlaceEntry1.hasFrequentPlace());
        localFrequentPlace1 = localFrequentPlaceEntry1.getFrequentPlace();
        localFrequentPlace2 = localFrequentPlaceEntry2.getFrequentPlace();
        if ((localFrequentPlace1.getSourceType() != localFrequentPlace2.getSourceType()) || (localFrequentPlace1.hasPlaceData() != localFrequentPlace2.hasPlaceData())) {
          break;
        }
      } while (TextUtils.equals(PlaceUtils.getPlaceName(this.mAppContext, localFrequentPlace1), PlaceUtils.getPlaceName(this.mAppContext, localFrequentPlace2)));
      return false;
      if (!paramEntry1.hasReminderEntry())
      {
        if (!paramEntry2.hasReminderEntry()) {}
        for (;;)
        {
          return bool;
          bool = false;
        }
      }
      if (!paramEntry2.hasReminderEntry()) {
        break;
      }
      Sidekick.ReminderEntry localReminderEntry1 = paramEntry1.getReminderEntry();
      Sidekick.ReminderEntry localReminderEntry2 = paramEntry2.getReminderEntry();
      if (!localReminderEntry1.hasTaskId())
      {
        if (!localReminderEntry2.hasTaskId()) {}
        for (;;)
        {
          return bool;
          bool = false;
        }
      }
      if (!localReminderEntry2.hasTaskId()) {
        break;
      }
      return localReminderEntry1.getTaskId().equals(localReminderEntry2.getTaskId());
    } while (paramEntry1.getNotification().getType() == paramEntry2.getNotification().getType());
    return false;
  }
  
  private void readFromDisk()
  {
    ExtraPreconditions.checkNotMainThread();
    Notifications.ClientData localClientData = new Notifications.ClientData();
    byte[] arrayOfByte = this.mFileBytesReader.readEncryptedFileBytes("notifications_store", 524288);
    if (arrayOfByte != null) {}
    try
    {
      localClientData.mergeFrom(arrayOfByte);
      pruneUnsupportedNotifications(localClientData);
      pruneOldNotifications(localClientData);
      pruneOldPendingRefreshes(localClientData);
      this.mClientData = localClientData;
      return;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      for (;;)
      {
        Log.e(TAG, "Error reading notifications from disk", localInvalidProtocolBufferMicroException);
      }
    }
  }
  
  private boolean waitForInitialization()
  {
    
    try
    {
      this.mInitializedLatch.await();
      return true;
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.w(TAG, "Initialization latch wait interrupted");
      Thread.currentThread().interrupt();
    }
    return false;
  }
  
  public void clearAll()
  {
    if (!waitForInitialization()) {
      return;
    }
    synchronized (this.mWriteLock)
    {
      this.mClientData = new Notifications.ClientData();
      this.mFileBytesWriter.deleteFile("notifications_store");
      return;
    }
  }
  
  public void clearNotifiedMarkers()
  {
    if (!waitForInitialization()) {
      return;
    }
    synchronized (this.mWriteLock)
    {
      Notifications.ClientData localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
      int i = 0;
      Iterator localIterator = localClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        if ((localPendingNotification.getNotified()) && (!localPendingNotification.getNotificationDismissed()))
        {
          localPendingNotification.setNotified(false);
          i = 1;
        }
      }
      if (i != 0)
      {
        this.mClientData = localClientData;
        flush();
      }
      return;
    }
  }
  
  public boolean deleteNotification(Sidekick.Entry paramEntry)
  {
    if (!waitForInitialization()) {
      return false;
    }
    synchronized (this.mWriteLock)
    {
      Notifications.ClientData localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
      boolean bool = false;
      Iterator localIterator = localClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext()) {
        if (isEquivalent(paramEntry, ((Notifications.PendingNotification)localIterator.next()).getEntry()))
        {
          localIterator.remove();
          bool = true;
        }
      }
      if (bool)
      {
        this.mClientData = localClientData;
        flush();
      }
      return bool;
    }
  }
  
  Notifications.ClientData getClientData()
  {
    return this.mClientData;
  }
  
  public Collection<Sidekick.Entry> getEntriesToNotify()
  {
    Object localObject;
    if (!waitForInitialization()) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      localObject = Lists.newArrayList();
      long l = this.mClock.currentTimeMillis() / 1000L;
      Iterator localIterator = this.mClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        if (PendingNotificationAdapter.getAdapter(localPendingNotification).getBringUpTimeSeconds(localPendingNotification, l) <= l) {
          ((List)localObject).add(localPendingNotification.getEntry());
        }
      }
    }
  }
  
  public Collection<Sidekick.Entry> getEntriesWithNotificationCurrentlyShownAndValid()
  {
    Object localObject;
    if (!waitForInitialization()) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      localObject = Sets.newHashSet();
      long l = this.mClock.currentTimeMillis() / 1000L;
      Iterator localIterator = this.mClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        if (PendingNotificationAdapter.getAdapter(localPendingNotification).isCurrentlyShownAndValid(localPendingNotification, l)) {
          ((Set)localObject).add(localPendingNotification.getEntry());
        }
      }
    }
  }
  
  public Collection<Sidekick.Entry> getEntriesWithNotificationToBringDown()
  {
    Object localObject;
    if (!waitForInitialization()) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      localObject = Sets.newHashSet();
      long l = this.mClock.currentTimeMillis() / 1000L;
      Iterator localIterator = this.mClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        if (PendingNotificationAdapter.getAdapter(localPendingNotification).getBringDownTimeSeconds(localPendingNotification) <= l) {
          ((Set)localObject).add(localPendingNotification.getEntry());
        }
      }
    }
  }
  
  public Long getNextNotificationBringDownTimeMillis()
  {
    if (!waitForInitialization()) {}
    long l1;
    do
    {
      return null;
      l1 = 9223372036854775807L;
      long l2 = this.mClock.currentTimeMillis() / 1000L;
      Iterator localIterator = this.mClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        long l3 = PendingNotificationAdapter.getAdapter(localPendingNotification).getBringDownTimeSeconds(localPendingNotification);
        if (l3 > l2) {
          l1 = Math.min(l1, l3);
        }
      }
    } while (l1 == 9223372036854775807L);
    return Long.valueOf(l1 * 1000L);
  }
  
  public Long getNextNotificationTimeMillis()
  {
    if (!waitForInitialization()) {}
    long l2;
    do
    {
      return null;
      long l1 = this.mClock.currentTimeMillis() / 1000L;
      l2 = 9223372036854775807L;
      Iterator localIterator = this.mClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        long l3 = PendingNotificationAdapter.getAdapter(localPendingNotification).getBringUpTimeSeconds(localPendingNotification, l1);
        if (l3 > l1) {
          l2 = Math.min(l2, l3);
        }
      }
    } while (l2 == 9223372036854775807L);
    return Long.valueOf(l2 * 1000L);
  }
  
  public Long getNextRefreshTimeMillis()
  {
    if (!waitForInitialization()) {}
    long l1;
    do
    {
      return null;
      l1 = 9223372036854775807L;
      long l2 = this.mClock.currentTimeMillis() / 1000L;
      Iterator localIterator = this.mClientData.getPendingRefreshList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingRefresh localPendingRefresh = (Notifications.PendingRefresh)localIterator.next();
        if (localPendingRefresh.getRefreshTimeSeconds() >= l2) {
          l1 = Math.min(l1, localPendingRefresh.getRefreshTimeSeconds());
        }
      }
    } while (l1 == 9223372036854775807L);
    return Long.valueOf(l1 * 1000L);
  }
  
  public int getNotificationState(Sidekick.Entry paramEntry)
  {
    Notifications.PendingNotification localPendingNotification = getPendingNotificationForEntry(paramEntry);
    int i = 0;
    if (localPendingNotification != null)
    {
      i = 0x0 | 0x1;
      if (localPendingNotification.getNotificationDismissed()) {
        i |= 0x2;
      }
    }
    return i;
  }
  
  @Nullable
  public List<Sidekick.Interest> getPendingRefreshInterests()
  {
    Object localObject;
    if (!waitForInitialization()) {
      localObject = ImmutableList.of();
    }
    for (;;)
    {
      return localObject;
      long l = this.mClock.currentTimeMillis() / 1000L;
      localObject = null;
      Iterator localIterator = this.mClientData.getPendingRefreshList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingRefresh localPendingRefresh = (Notifications.PendingRefresh)localIterator.next();
        if (localPendingRefresh.getRefreshTimeSeconds() <= l)
        {
          if (localObject == null) {
            localObject = Lists.newArrayList();
          }
          ((List)localObject).add(localPendingRefresh.getInterest());
        }
      }
    }
  }
  
  @Nullable
  public Sidekick.Entry getStoredEntry(Sidekick.Entry paramEntry)
  {
    Notifications.PendingNotification localPendingNotification = getPendingNotificationForEntry(paramEntry);
    if (localPendingNotification != null) {
      return localPendingNotification.getEntry();
    }
    return null;
  }
  
  List<Sidekick.Interest> getStoredNotificationInterests()
  {
    if (!waitForInitialization()) {
      return ImmutableList.of();
    }
    List localList = this.mClientData.getPendingNotificationList();
    if (localList.isEmpty()) {
      return ImmutableList.of();
    }
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Interest localInterest = ((Notifications.PendingNotification)localIterator.next()).getInterest();
      localHashMap.put(new ProtoKey(localInterest), localInterest);
    }
    return ImmutableList.copyOf(localHashMap.values());
  }
  
  public void initialize()
  {
    
    if (!this.mInitialized.getAndSet(true))
    {
      readFromDisk();
      this.mInitializedLatch.countDown();
    }
  }
  
  public boolean isInitialized()
  {
    return this.mInitializedLatch.getCount() == 0L;
  }
  
  public void markEntryNotificationDismissed(Sidekick.Entry paramEntry)
  {
    if (!waitForInitialization()) {
      return;
    }
    synchronized (this.mWriteLock)
    {
      Notifications.ClientData localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
      int i = 0;
      Iterator localIterator = localClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        if (isEquivalent(paramEntry, localPendingNotification.getEntry()))
        {
          localPendingNotification.setNotificationDismissed(true);
          i = 1;
        }
      }
      if (i != 0)
      {
        this.mClientData = localClientData;
        flush();
      }
      return;
    }
  }
  
  public void markEntryNotified(Sidekick.Entry paramEntry)
  {
    if (!waitForInitialization()) {
      return;
    }
    synchronized (this.mWriteLock)
    {
      Notifications.ClientData localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
      int i = 0;
      Iterator localIterator = localClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        if (isEquivalent(paramEntry, localPendingNotification.getEntry()))
        {
          localPendingNotification.setNotified(true);
          i = 1;
        }
      }
      if (i != 0)
      {
        this.mClientData = localClientData;
        flush();
      }
      return;
    }
  }
  
  public void onEntriesAdded(Sidekick.EntryTree paramEntryTree) {}
  
  public void onEntryDismissed(final Sidekick.Entry paramEntry, @Nullable Collection<Sidekick.Entry> paramCollection)
  {
    this.mAsyncServices.getPooledBackgroundExecutorService().submit(new Runnable()
    {
      public void run()
      {
        NotificationStore.this.markEntryNotificationDismissed(paramEntry);
      }
    });
  }
  
  public void onEntryUpdate(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2, Sidekick.Entry paramEntry3) {}
  
  public void onInvalidated() {}
  
  public void onRefreshed(Bundle paramBundle) {}
  
  void pruneOldNotifications(Notifications.ClientData paramClientData)
  {
    long l = this.mClock.currentTimeMillis() / 1000L;
    Iterator localIterator = paramClientData.getPendingNotificationList().iterator();
    while (localIterator.hasNext())
    {
      Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
      if (PendingNotificationAdapter.getAdapter(localPendingNotification).shouldPruneDuringInitialization(localPendingNotification, l, paramClientData.getPendingRefreshList())) {
        localIterator.remove();
      }
    }
  }
  
  void pruneOldPendingRefreshes(Notifications.ClientData paramClientData)
  {
    long l = this.mClock.currentTimeMillis();
    Iterator localIterator = paramClientData.getPendingRefreshList().iterator();
    while (localIterator.hasNext()) {
      if (1000L * ((Notifications.PendingRefresh)localIterator.next()).getRefreshTimeSeconds() < l) {
        localIterator.remove();
      }
    }
  }
  
  void pruneUnsupportedNotifications(Notifications.ClientData paramClientData)
  {
    Iterators.removeIf(Iterators.transform(paramClientData.getPendingNotificationList().iterator(), GET_NOTIFICATION), UNSUPPORTED);
  }
  
  public void recordRefresh(long paramLong, Sidekick.Interest paramInterest)
  {
    if (!waitForInitialization()) {
      return;
    }
    synchronized (this.mWriteLock)
    {
      Notifications.ClientData localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
      int i = 0;
      Iterator localIterator = localClientData.getPendingRefreshList().iterator();
      ProtoKey localProtoKey = new ProtoKey(paramInterest);
      while (localIterator.hasNext())
      {
        Notifications.PendingRefresh localPendingRefresh = (Notifications.PendingRefresh)localIterator.next();
        if ((localProtoKey.equals(new ProtoKey(localPendingRefresh.getInterest()))) && (localPendingRefresh.getRefreshTimeSeconds() <= paramLong))
        {
          localIterator.remove();
          i = 1;
        }
      }
      if (i != 0)
      {
        this.mClientData = localClientData;
        flush();
      }
      return;
    }
  }
  
  public void resetNotification(Sidekick.Entry paramEntry)
  {
    if (!waitForInitialization()) {
      return;
    }
    synchronized (this.mWriteLock)
    {
      Notifications.ClientData localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
      int i = 0;
      Iterator localIterator = localClientData.getPendingNotificationList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        if (isEquivalent(paramEntry, localPendingNotification.getEntry()))
        {
          localPendingNotification.setNotified(false);
          i = 1;
        }
      }
      if (i != 0)
      {
        this.mClientData = localClientData;
        flush();
      }
      return;
    }
  }
  
  void setClientDataForTest(Notifications.ClientData paramClientData)
  {
    this.mClientData = paramClientData;
    this.mInitializedLatch.countDown();
  }
  
  public void setOrAddNotification(Sidekick.Entry paramEntry, Sidekick.Interest paramInterest)
  {
    if (!waitForInitialization()) {}
    while (UNSUPPORTED.apply(paramEntry.getNotification())) {
      return;
    }
    helpUpdateNotifications(Lists.newArrayList(new Sidekick.Entry[] { paramEntry }), paramInterest, false);
  }
  
  public void updateLocationTriggerConditions(TriggerConditionsUpdater paramTriggerConditionsUpdater)
  {
    if (!waitForInitialization()) {
      return;
    }
    Notifications.ClientData localClientData;
    synchronized (this.mWriteLock)
    {
      localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
      Iterator localIterator = localClientData.getPendingNotificationList().iterator();
      if (localIterator.hasNext())
      {
        Notifications.PendingNotification localPendingNotification = (Notifications.PendingNotification)localIterator.next();
        PendingNotificationAdapter.getAdapter(localPendingNotification).updateByTriggerConditionsUpdater(localPendingNotification, paramTriggerConditionsUpdater, this.mClock);
      }
    }
    if (paramTriggerConditionsUpdater.hasAffectedNotifications())
    {
      this.mClientData = localClientData;
      flush();
    }
  }
  
  public void updateNextRefreshTime(long paramLong, Sidekick.Interest paramInterest, boolean paramBoolean)
  {
    if (!waitForInitialization()) {
      return;
    }
    int i = 0;
    int j = 0;
    ProtoKey localProtoKey = new ProtoKey(paramInterest);
    long l = this.mClock.currentTimeMillis() / 1000L;
    synchronized (this.mWriteLock)
    {
      Notifications.ClientData localClientData = (Notifications.ClientData)ProtoUtils.copyOf(this.mClientData, new Notifications.ClientData());
      Iterator localIterator = localClientData.getPendingRefreshList().iterator();
      while (localIterator.hasNext())
      {
        Notifications.PendingRefresh localPendingRefresh = (Notifications.PendingRefresh)localIterator.next();
        if (localProtoKey.equals(new ProtoKey(localPendingRefresh.getInterest())))
        {
          j = 1;
          if ((paramBoolean) || (paramLong < localPendingRefresh.getRefreshTimeSeconds()) || (localPendingRefresh.getRefreshTimeSeconds() < l))
          {
            localPendingRefresh.setRefreshTimeSeconds(paramLong);
            i = 1;
          }
        }
      }
      if (j == 0)
      {
        localClientData.addPendingRefresh(new Notifications.PendingRefresh().setInterest(paramInterest).setRefreshTimeSeconds(paramLong));
        i = 1;
      }
      if (i != 0)
      {
        this.mClientData = localClientData;
        flush();
      }
      return;
    }
  }
  
  public void updatePendingNotifications(Sidekick.EntryTree paramEntryTree, Sidekick.Interest paramInterest)
  {
    if (!waitForInitialization()) {
      return;
    }
    helpUpdateNotifications(getEntriesWithNotifications(paramEntryTree), paramInterest, true);
  }
  
  private static final class NotificationFinder
    extends EntryTreeVisitor
  {
    private final List<Sidekick.Entry> entriesWithNotifications = Lists.newArrayList();
    
    protected void process(ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Entry paramEntry)
    {
      if (!paramEntry.hasNotification()) {}
      Sidekick.Notification localNotification;
      do
      {
        return;
        localNotification = paramEntry.getNotification();
      } while (!NotificationStore.SUPPORTED.apply(localNotification));
      this.entriesWithNotifications.add(paramEntry);
    }
  }
  
  public static abstract class TriggerConditionsUpdater
  {
    private final Set<Sidekick.Entry> mConcluded = Sets.newHashSet();
    private final Set<Sidekick.Entry> mTriggered = Sets.newHashSet();
    
    protected abstract boolean areTriggerConditionsSatisfied(Sidekick.Entry paramEntry, boolean paramBoolean);
    
    public final Set<Sidekick.Entry> getConcludedNotifications()
    {
      return this.mConcluded;
    }
    
    public final Set<Sidekick.Entry> getTriggeredNotifications()
    {
      return this.mTriggered;
    }
    
    public final boolean hasAffectedNotifications()
    {
      return (!this.mTriggered.isEmpty()) || (!this.mConcluded.isEmpty());
    }
    
    void update(Notifications.PendingNotification paramPendingNotification, Clock paramClock)
    {
      boolean bool = paramPendingNotification.hasLastTriggerTimeSeconds();
      Sidekick.Entry localEntry = paramPendingNotification.getEntry();
      if (areTriggerConditionsSatisfied(localEntry, bool)) {
        if (!bool)
        {
          paramPendingNotification.setLastTriggerTimeSeconds(paramClock.currentTimeMillis() / 1000L);
          this.mTriggered.add(localEntry);
        }
      }
      while (!bool) {
        return;
      }
      paramPendingNotification.clearLastTriggerTimeSeconds();
      this.mConcluded.add(localEntry);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.NotificationStore
 * JD-Core Version:    0.7.0.1
 */