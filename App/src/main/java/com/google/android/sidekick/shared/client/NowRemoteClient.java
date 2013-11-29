package com.google.android.sidekick.shared.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.ExecutorAsyncTask;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.PriorityThreadFactory;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.shared.remoteapi.CardsResponse;
import com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService;
import com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService.Stub;
import com.google.android.sidekick.shared.remoteapi.LoggingRequest;
import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.GeoLocatedPhoto;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import com.google.geo.sidekick.Sidekick.QuestionNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class NowRemoteClient
{
  private static final String TAG = Tag.getTag(NowRemoteClient.class);
  private final Context mAppContext;
  private final Executor mBgExecutor;
  private final Set<NowRemoteClientLock> mClientConnectionLocks = Sets.newHashSet();
  private NowConnection mConnection = null;
  private final List<Runnable> mConnectionListeners = Lists.newArrayList();
  private final Object mConnectionLock = new Object();
  private final RemoteImageLoader mImageLoader;
  private final RemoteImageLoader mNonCachingImageLoader;
  private final Object mPendingRequestLock = new Object();
  private final List<PendingRequest> mPendingRequests = Lists.newArrayList();
  private IGoogleNowRemoteService mService = null;
  private final StaticMapLoader mStaticMapLoader;
  private final Executor mUiThread;
  
  public NowRemoteClient(Context paramContext, Executor paramExecutor1, Executor paramExecutor2)
  {
    this.mAppContext = paramContext;
    this.mBgExecutor = paramExecutor1;
    this.mUiThread = paramExecutor2;
    PriorityThreadFactory localPriorityThreadFactory = new PriorityThreadFactory(10);
    ThreadPoolExecutor localThreadPoolExecutor1 = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), localPriorityThreadFactory);
    ThreadPoolExecutor localThreadPoolExecutor2 = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), localPriorityThreadFactory);
    this.mImageLoader = new RemoteImageLoader(this.mUiThread, localThreadPoolExecutor1, paramContext.getResources(), this, true);
    this.mNonCachingImageLoader = new RemoteImageLoader(this.mUiThread, localThreadPoolExecutor2, paramContext.getResources(), this, false);
    this.mStaticMapLoader = new RemoteStaticMapLoader(paramContext.getResources(), this.mUiThread, localThreadPoolExecutor2, this);
  }
  
  private void addPendingRequest(int paramInt, @Nullable Bundle paramBundle)
  {
    synchronized (this.mPendingRequestLock)
    {
      this.mPendingRequests.add(new PendingRequest(paramInt, paramBundle));
      return;
    }
  }
  
  private boolean connect()
  {
    synchronized (this.mConnectionLock)
    {
      if (this.mConnection == null)
      {
        Intent localIntent = new Intent(IGoogleNowRemoteService.class.getName());
        localIntent.setPackage(this.mAppContext.getPackageName());
        this.mConnection = new NowConnection(null);
        if (!this.mAppContext.bindService(localIntent, this.mConnection, 1))
        {
          Log.e(TAG, "Error binding to predictive cards service");
          this.mConnection = null;
          return false;
        }
      }
      return true;
    }
  }
  
  private void disconnect()
  {
    synchronized (this.mConnectionLock)
    {
      if (this.mConnection != null)
      {
        this.mAppContext.unbindService(this.mConnection);
        this.mService = null;
        this.mConnection = null;
      }
      return;
    }
  }
  
  private void dispatch(IGoogleNowRemoteService paramIGoogleNowRemoteService, int paramInt, @Nullable Bundle paramBundle)
    throws RemoteException
  {
    switch (paramInt)
    {
    default: 
      return;
    }
    paramIGoogleNowRemoteService.prefetchImage((Uri)paramBundle.getParcelable("load-image-uri"));
  }
  
  @Nullable
  private IGoogleNowRemoteService getServiceLocked()
  {
    synchronized (this.mConnectionLock)
    {
      IGoogleNowRemoteService localIGoogleNowRemoteService = this.mService;
      return localIGoogleNowRemoteService;
    }
  }
  
  private void sendRequest(int paramInt, @Nullable Bundle paramBundle)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        dispatch(localIGoogleNowRemoteService, paramInt, paramBundle);
        return;
      }
      catch (DeadObjectException localDeadObjectException)
      {
        addPendingRequest(paramInt, paramBundle);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(TAG, "Caught exception making request type: " + paramInt, localRemoteException);
        return;
      }
    }
    addPendingRequest(paramInt, paramBundle);
  }
  
  public Bitmap blockingGetImage(Uri paramUri, boolean paramBoolean)
  {
    ExtraPreconditions.checkNotMainThread();
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        Bitmap localBitmap = localIGoogleNowRemoteService.blockingGetImage(paramUri, paramBoolean);
        return localBitmap;
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error making blocking bitmap request", localException);
      }
    }
    return null;
  }
  
  public void deleteNotificationsForEntry(Sidekick.Entry paramEntry)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.deleteNotificationsForEntry(ProtoParcelable.create(paramEntry));
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making delete notifications request", localException);
    }
  }
  
  public void dismissEntry(Sidekick.Entry paramEntry, boolean paramBoolean)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.dismissEntry(ProtoParcelable.create(paramEntry), paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making dismiss entry request", localException);
    }
  }
  
  public void enableSearchHistoryForActiveAccount(final Consumer<Boolean> paramConsumer)
  {
    final IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      new ExecutorAsyncTask(this.mUiThread, this.mBgExecutor)
      {
        protected Boolean doInBackground(Void... paramAnonymousVarArgs)
        {
          try
          {
            Boolean localBoolean = Boolean.valueOf(localIGoogleNowRemoteService.enableSearchHistoryForActiveAccount());
            return localBoolean;
          }
          catch (Exception localException)
          {
            Log.e(NowRemoteClient.TAG, "Error enabling search history", localException);
          }
          return Boolean.valueOf(false);
        }
        
        protected void onPostExecute(Boolean paramAnonymousBoolean)
        {
          paramConsumer.consume(paramAnonymousBoolean);
        }
      }.execute(new Void[0]);
    }
  }
  
  public Bundle getConfiguration()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        Bundle localBundle = localIGoogleNowRemoteService.getConfiguration();
        return localBundle;
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error getting configuration", localException);
      }
    }
    return new Bundle();
  }
  
  @Nullable
  public CardsResponse getEntries()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        CardsResponse localCardsResponse = localIGoogleNowRemoteService.getCards();
        return localCardsResponse;
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error retrieving entries from service", localException);
      }
    }
    return null;
  }
  
  @Nullable
  public Intent getHelpIntent(String paramString)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        Intent localIntent = localIGoogleNowRemoteService.getHelpIntent(paramString);
        return localIntent;
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error requesting help intent", localException);
      }
    }
    return null;
  }
  
  public UriLoader<Drawable> getImageLoader()
  {
    return this.mImageLoader;
  }
  
  public UriLoader<Drawable> getNonCachingImageLoader()
  {
    return this.mNonCachingImageLoader;
  }
  
  public Bitmap getSampleMap()
  {
    ExtraPreconditions.checkNotMainThread();
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        Bitmap localBitmap = localIGoogleNowRemoteService.getSampleMap();
        return localBitmap;
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error making sample map request", localException);
      }
    }
    return null;
  }
  
  public Bitmap getStaticMap(Sidekick.Location paramLocation, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, boolean paramBoolean)
  {
    ExtraPreconditions.checkNotMainThread();
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        Bitmap localBitmap = localIGoogleNowRemoteService.getStaticMap(LocationUtilities.sidekickLocationToAndroidLocation(paramLocation), ProtoParcelable.create(paramFrequentPlaceEntry), paramBoolean);
        return localBitmap;
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error making static map request", localException);
      }
    }
    return null;
  }
  
  public StaticMapLoader getStaticMapLoader()
  {
    return this.mStaticMapLoader;
  }
  
  public void invalidateEntries()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.invalidateEntries();
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making invalidateEntries request", localException);
    }
  }
  
  public boolean isConnected()
  {
    for (;;)
    {
      synchronized (this.mConnectionLock)
      {
        if (this.mService != null)
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public boolean isReminderSmartActionSupported(int paramInt)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        boolean bool = localIGoogleNowRemoteService.isReminderSmartActionSupported(paramInt);
        return bool;
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error making isReminderSmartActionSupported request", localException);
      }
    }
    return true;
  }
  
  public void logAction(LoggingRequest paramLoggingRequest)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.logAction(paramLoggingRequest);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making logging request", localException);
    }
  }
  
  public void markCalendarEntryDismissed(long paramLong)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.markCalendarEntryDismissed(paramLong);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making dismiss calendar entry request", localException);
    }
  }
  
  public NowRemoteClientLock newConnectionLock(String paramString)
  {
    return new NowRemoteClientLock(paramString, null);
  }
  
  public void optIntoLocationReportingAsync()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.optIntoLocationReporting();
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making location reporting opt in request", localException);
    }
  }
  
  public void pauseImageLoading()
  {
    this.mImageLoader.pause();
    this.mNonCachingImageLoader.pause();
    this.mStaticMapLoader.pause();
  }
  
  public void prefetchImage(Uri paramUri)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("load-image-uri", paramUri);
    sendRequest(1, localBundle);
  }
  
  @Nullable
  public Intent preparePhotoGalleryIntent(List<Sidekick.GeoLocatedPhoto> paramList, int paramInt)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    ArrayList localArrayList;
    if (localIGoogleNowRemoteService != null) {
      try
      {
        localArrayList = Lists.newArrayListWithCapacity(paramList.size());
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          localArrayList.add(ProtoParcelable.create((Sidekick.GeoLocatedPhoto)localIterator.next()));
          continue;
          return null;
        }
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error making setup images request", localException);
      }
    }
    Intent localIntent = localIGoogleNowRemoteService.preparePhotoGalleryIntent(localArrayList, paramInt);
    return localIntent;
  }
  
  public void queueDismissEntryAction(Sidekick.Entry paramEntry, boolean paramBoolean)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.queueDismissEntryAction(ProtoParcelable.create(paramEntry), paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making queueDismissEntryAction request", localException);
    }
  }
  
  public void recordAction(Sidekick.Entry paramEntry, int paramInt)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.recordUserAction(ProtoParcelable.create(paramEntry), paramInt);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making record action request", localException);
    }
  }
  
  public void recordBackOfCardShown()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.recordBackOfCardShown();
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error recording back-of-card shown", localException);
    }
  }
  
  public void recordCardSwipedForDismiss()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.recordCardSwipedForDismiss();
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error recording card swiped for dismiss", localException);
    }
  }
  
  public void recordExecutedUserActions(List<Sidekick.ExecutedUserAction> paramList)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    ArrayList localArrayList;
    if (localIGoogleNowRemoteService != null) {
      try
      {
        localArrayList = Lists.newArrayListWithCapacity(paramList.size());
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          localArrayList.add(ProtoParcelable.create((Sidekick.ExecutedUserAction)localIterator.next()));
          continue;
          return;
        }
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error making record executed-user-actions request", localException);
      }
    }
    localIGoogleNowRemoteService.recordExecutedUserActions(localArrayList);
  }
  
  public void recordFeedbackPromptAction(Sidekick.Entry paramEntry, int paramInt)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.recordFeedbackPromptAction(ProtoParcelable.create(paramEntry), paramInt);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making record feedback prompt request", localException);
    }
  }
  
  public void recordFirstUseCardDismiss(int paramInt)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.recordFirstUseCardDismiss(paramInt);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error recording first-use card dismiss", localException);
    }
  }
  
  public void recordFirstUseCardView(int paramInt)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.recordFirstUseCardView(paramInt);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error recording first-use card view", localException);
    }
  }
  
  public void recordGoogleNowPromoDismissed()
    throws RemoteException
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService == null) {
      throw new RemoteException("Not connected");
    }
    localIGoogleNowRemoteService.recordGoogleNowPromoDismissed();
  }
  
  public void recordPredictiveInteraction()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.recordPredictiveInteraction();
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making request to record interaction", localException);
    }
  }
  
  public void recordViewAction(Sidekick.Entry paramEntry, long paramLong, int paramInt, boolean paramBoolean)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.recordViewAction(ProtoParcelable.create(paramEntry), paramLong, paramInt, paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making record view-action request", localException);
    }
  }
  
  public void refreshEntries()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.refreshEntries();
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making invalidateEntries request", localException);
    }
  }
  
  public void registerConnectionListener(Runnable paramRunnable)
  {
    synchronized (this.mConnectionLock)
    {
      if (!this.mConnectionListeners.contains(paramRunnable)) {
        this.mConnectionListeners.add(paramRunnable);
      }
      return;
    }
  }
  
  public void removeGroupChildEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.removeGroupChildEntry(ProtoParcelable.create(paramEntry1), ProtoParcelable.create(paramEntry2));
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making dismiss child entry request", localException);
    }
  }
  
  public ListenableFuture<Collection<TrainingQuestionNode>> resolveTrainingQuestionsAsync(final Collection<Sidekick.QuestionNode> paramCollection)
  {
    ListenableFutureTask localListenableFutureTask = ListenableFutureTask.create(new Callable()
    {
      public Collection<TrainingQuestionNode> call()
        throws Exception
      {
        IGoogleNowRemoteService localIGoogleNowRemoteService = NowRemoteClient.this.getServiceLocked();
        if (localIGoogleNowRemoteService != null)
        {
          ArrayList localArrayList = Lists.newArrayListWithCapacity(paramCollection.size());
          Iterator localIterator = paramCollection.iterator();
          while (localIterator.hasNext()) {
            localArrayList.add(ProtoParcelable.create((Sidekick.QuestionNode)localIterator.next()));
          }
          return localIGoogleNowRemoteService.resolveTrainingQuestions(localArrayList);
        }
        return ImmutableList.of();
      }
    });
    this.mBgExecutor.execute(localListenableFutureTask);
    return localListenableFutureTask;
  }
  
  public void resumeImageLoading()
  {
    this.mImageLoader.resume();
    this.mNonCachingImageLoader.resume();
    this.mStaticMapLoader.resume();
  }
  
  public void savePreferences(Bundle paramBundle)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.savePreferences(paramBundle);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making save preferences request", localException);
    }
  }
  
  public void sendPendingTrainingAnswers()
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.sendPendingTrainingAnswers();
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making sendPendingTrainingAnswers request", localException);
    }
  }
  
  public void sendTrainingAction(Sidekick.Entry paramEntry, Sidekick.Question paramQuestion, Sidekick.Action paramAction)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.sendTrainingAction(ProtoParcelable.create(paramEntry), ProtoParcelable.create(paramQuestion), ProtoParcelable.create(paramAction));
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making sendTrainingActionAsync request", localException);
    }
  }
  
  public void setTrafficSharerHiddenState(long paramLong, boolean paramBoolean)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.setTrafficSharerHiddenState(paramLong, paramBoolean);
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making save preferences request", localException);
    }
  }
  
  public void setTrainingAnswer(Sidekick.Question paramQuestion, Sidekick.Question.Answer paramAnswer, @Nullable Sidekick.Entry paramEntry)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.setTrainingAnswer(ProtoParcelable.create(paramQuestion), ProtoParcelable.create(paramAnswer), ProtoParcelable.create(paramEntry));
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making setTrainingAnswer request", localException);
    }
  }
  
  public void snoozeReminder(Sidekick.Entry paramEntry)
  {
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {}
    try
    {
      localIGoogleNowRemoteService.snoozeReminder(ProtoParcelable.create(paramEntry));
      return;
    }
    catch (Exception localException)
    {
      Log.e(TAG, "Error making snoozeReminder request", localException);
    }
  }
  
  @Nullable
  public String translateInPlace(String paramString1, String paramString2, String paramString3)
  {
    ExtraPreconditions.checkNotMainThread();
    IGoogleNowRemoteService localIGoogleNowRemoteService = getServiceLocked();
    if (localIGoogleNowRemoteService != null) {
      try
      {
        String str = localIGoogleNowRemoteService.translateInPlace(paramString1, paramString2, paramString3);
        return str;
      }
      catch (Exception localException)
      {
        Log.e(TAG, "Error making translateInPlace request", localException);
      }
    }
    return null;
  }
  
  public void unregisterConnectionListener(Runnable paramRunnable)
  {
    synchronized (this.mConnectionLock)
    {
      this.mConnectionListeners.remove(paramRunnable);
      return;
    }
  }
  
  private class NowConnection
    implements ServiceConnection
  {
    private NowConnection() {}
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      synchronized (NowRemoteClient.this.mConnectionLock)
      {
        NowRemoteClient.access$802(NowRemoteClient.this, IGoogleNowRemoteService.Stub.asInterface(paramIBinder));
        NowRemoteClient.this.mImageLoader.retry();
        NowRemoteClient.this.mNonCachingImageLoader.retry();
        NowRemoteClient.this.mStaticMapLoader.retry();
        synchronized (NowRemoteClient.this.mPendingRequests)
        {
          if (!NowRemoteClient.this.mPendingRequests.isEmpty())
          {
            ImmutableList localImmutableList = ImmutableList.copyOf(NowRemoteClient.this.mPendingRequests);
            NowRemoteClient.this.mPendingRequests.clear();
            Iterator localIterator2 = localImmutableList.iterator();
            if (localIterator2.hasNext())
            {
              NowRemoteClient.PendingRequest localPendingRequest = (NowRemoteClient.PendingRequest)localIterator2.next();
              NowRemoteClient.this.sendRequest(localPendingRequest.mRequestType, localPendingRequest.mArgs);
            }
          }
        }
      }
      if (NowRemoteClient.this.mConnection != null)
      {
        Iterator localIterator1 = NowRemoteClient.this.mConnectionListeners.iterator();
        while (localIterator1.hasNext())
        {
          Runnable localRunnable = (Runnable)localIterator1.next();
          NowRemoteClient.this.mUiThread.execute(localRunnable);
        }
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      synchronized (NowRemoteClient.this.mConnectionLock)
      {
        NowRemoteClient.access$802(NowRemoteClient.this, null);
        return;
      }
    }
  }
  
  public class NowRemoteClientLock
  {
    private final String mTag;
    
    private NowRemoteClientLock(String paramString)
    {
      this.mTag = paramString;
    }
    
    public boolean acquire()
    {
      synchronized (NowRemoteClient.this.mConnectionLock)
      {
        if (NowRemoteClient.this.connect())
        {
          NowRemoteClient.this.mClientConnectionLocks.add(this);
          return true;
        }
        return false;
      }
    }
    
    public void release()
    {
      synchronized (NowRemoteClient.this.mConnectionLock)
      {
        NowRemoteClient.this.mClientConnectionLocks.remove(this);
        if (NowRemoteClient.this.mClientConnectionLocks.isEmpty()) {
          NowRemoteClient.this.disconnect();
        }
        return;
      }
    }
    
    public String toString()
    {
      return this.mTag;
    }
  }
  
  static class PendingRequest
  {
    final Bundle mArgs;
    final int mRequestType;
    
    PendingRequest(int paramInt, @Nullable Bundle paramBundle)
    {
      this.mRequestType = paramInt;
      this.mArgs = paramBundle;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.NowRemoteClient
 * JD-Core Version:    0.7.0.1
 */