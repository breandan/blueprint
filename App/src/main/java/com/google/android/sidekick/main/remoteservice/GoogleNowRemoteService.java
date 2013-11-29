package com.google.android.sidekick.main.remoteservice;

import android.accounts.Account;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GlobalSearchServices;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.history.SearchHistoryHelper;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.search.core.util.SimpleCallbackFuture;
import com.google.android.shared.util.CancellableNowOrLater;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.NowOrLater;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.sidekick.main.NowOptInHelper;
import com.google.android.sidekick.main.actions.ReminderSmartActionUtil;
import com.google.android.sidekick.main.actions.RemoveFeedbackPromptEntryUpdater;
import com.google.android.sidekick.main.actions.SnoozeReminderTask;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.ExecutedUserActionStore;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.inject.SidekickInteractionManager;
import com.google.android.sidekick.main.inject.StaticMapCache;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.inject.VelvetImageGalleryHelper;
import com.google.android.sidekick.main.location.LocationReportingOptInHelper;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.android.sidekick.shared.remoteapi.CardsResponse;
import com.google.android.sidekick.shared.remoteapi.IGoogleNowRemoteService.Stub;
import com.google.android.sidekick.shared.remoteapi.LoggingRequest;
import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.Help;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.presenter.FirstUseCardHandler;
import com.google.android.velvet.presenter.FirstUseCardHandler.FirstUseCardType;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.android.voicesearch.fragments.reminders.ReminderSaver;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import com.google.geo.sidekick.Sidekick.QuestionNode;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.TranslateInPlaceQuery;
import com.google.geo.sidekick.Sidekick.TranslateInPlaceResponse;
import com.google.majel.proto.ActionV2Protos.AddReminderAction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class GoogleNowRemoteService
  extends Service
{
  public static final String TAG = Tag.getTag(GoogleNowRemoteService.class);
  private final IGoogleNowRemoteService.Stub mBinder = new IGoogleNowRemoteService.Stub()
  {
    private Sidekick.Action getActionFromEntry(Sidekick.Entry paramAnonymousEntry, int paramAnonymousInt)
    {
      Iterator localIterator = paramAnonymousEntry.getEntryActionList().iterator();
      while (localIterator.hasNext())
      {
        Sidekick.Action localAction = (Sidekick.Action)localIterator.next();
        if (localAction.getType() == paramAnonymousInt) {
          return localAction;
        }
      }
      return null;
    }
    
    public Bitmap blockingGetImage(Uri paramAnonymousUri, boolean paramAnonymousBoolean)
      throws RemoteException
    {
      UriLoader localUriLoader;
      CancellableNowOrLater localCancellableNowOrLater;
      Drawable localDrawable;
      if (paramAnonymousBoolean)
      {
        localUriLoader = GoogleNowRemoteService.this.mImageLoader;
        localCancellableNowOrLater = localUriLoader.load(paramAnonymousUri);
        if (!localCancellableNowOrLater.haveNow()) {
          break label71;
        }
        localDrawable = (Drawable)localCancellableNowOrLater.getNow();
      }
      for (;;)
      {
        if (!(localDrawable instanceof BitmapDrawable)) {
          break label144;
        }
        return ((BitmapDrawable)localDrawable).getBitmap();
        localUriLoader = GoogleNowRemoteService.this.mNonCachingImageLoader;
        break;
        label71:
        final SettableFuture localSettableFuture = SettableFuture.create();
        localCancellableNowOrLater.getLater(new Consumer()
        {
          public boolean consume(Drawable paramAnonymous2Drawable)
          {
            localSettableFuture.set(paramAnonymous2Drawable);
            return true;
          }
        });
        try
        {
          localDrawable = (Drawable)localSettableFuture.get();
        }
        catch (ExecutionException localExecutionException)
        {
          Log.e(GoogleNowRemoteService.TAG, "Exeption getting drawable", localExecutionException);
          localDrawable = null;
        }
        catch (InterruptedException localInterruptedException)
        {
          Log.e(GoogleNowRemoteService.TAG, "Exeption getting drawable", localInterruptedException);
          localDrawable = null;
        }
      }
      label144:
      return null;
    }
    
    public boolean canUserOptIn(String paramAnonymousString)
    {
      int i = 1;
      Account localAccount = GoogleNowRemoteService.this.mLoginHelper.findAccountByName(paramAnonymousString);
      if (localAccount == null)
      {
        Log.e(GoogleNowRemoteService.TAG, "Invalid account: " + paramAnonymousString);
        return false;
      }
      GoogleNowRemoteService.this.mOptInSettings.updateWhetherAccountCanRunNow(localAccount);
      if (GoogleNowRemoteService.this.mOptInSettings.canAccountRunNow(localAccount) == i) {}
      for (;;)
      {
        return i;
        int j = 0;
      }
    }
    
    public boolean createReminder(ProtoParcelable paramAnonymousProtoParcelable)
      throws RemoteException
    {
      SetReminderAction localSetReminderAction = SetReminderAction.setUpFromAction((ActionV2Protos.AddReminderAction)paramAnonymousProtoParcelable.getProto(ActionV2Protos.AddReminderAction.class));
      SimpleCallbackFuture localSimpleCallbackFuture = new SimpleCallbackFuture();
      GoogleNowRemoteService.this.mReminderSaver.saveReminder(localSetReminderAction, localSimpleCallbackFuture);
      return ((Boolean)Futures.getUnchecked(localSimpleCallbackFuture)).booleanValue();
    }
    
    public void deleteNotificationsForEntry(ProtoParcelable paramAnonymousProtoParcelable)
      throws RemoteException
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable.getProto(Sidekick.Entry.class);
      Context localContext = GoogleNowRemoteService.this.getApplicationContext();
      localContext.startService(NotificationRefreshService.getDeleteNotificationIntent(localContext, ImmutableSet.of(localEntry)));
    }
    
    public void dismissEntry(ProtoParcelable paramAnonymousProtoParcelable, boolean paramAnonymousBoolean)
      throws RemoteException
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable.getProto(Sidekick.Entry.class);
      GoogleNowRemoteService.this.mEntryProvider.handleDismissedEntries(ImmutableSet.of(localEntry));
      if (paramAnonymousBoolean) {
        GoogleNowRemoteService.this.mUserInteractionLogger.logMetricsAction(localEntry, 1, null);
      }
    }
    
    public boolean enableSearchHistoryForActiveAccount()
      throws RemoteException
    {
      Account localAccount = GoogleNowRemoteService.this.mLoginHelper.getAccount();
      if (localAccount == null)
      {
        Log.w(GoogleNowRemoteService.TAG, "No active account");
        return false;
      }
      try
      {
        boolean bool = GoogleNowRemoteService.this.mSearchHistoryHelper.setHistoryEnabled(localAccount, true);
        return bool;
      }
      catch (IOException localIOException)
      {
        Log.e(GoogleNowRemoteService.TAG, "Error enabling search history", localIOException);
      }
      return false;
    }
    
    public CardsResponse getCards()
      throws RemoteException
    {
      return GoogleNowRemoteService.this.mRemoteServiceHelper.getCards(GoogleNowRemoteService.this.getApplicationContext());
    }
    
    public Bundle getConfiguration()
      throws RemoteException
    {
      Bundle localBundle = new Bundle();
      localBundle.putBoolean("CONFIGURATION_REMINDERS_ENABLED", GoogleNowRemoteService.this.mSidekickInjector.areRemindersEnabled());
      return localBundle;
    }
    
    public Intent getHelpIntent(String paramAnonymousString)
      throws RemoteException
    {
      return new Help(GoogleNowRemoteService.this.getApplicationContext()).getHelpIntent(paramAnonymousString);
    }
    
    public Bitmap getSampleMap()
      throws RemoteException
    {
      return GoogleNowRemoteService.this.mStaticMapCache.getSampleMap();
    }
    
    public Bitmap getStaticMap(Location paramAnonymousLocation, ProtoParcelable paramAnonymousProtoParcelable, boolean paramAnonymousBoolean)
      throws RemoteException
    {
      Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = (Sidekick.FrequentPlaceEntry)paramAnonymousProtoParcelable.getProto(Sidekick.FrequentPlaceEntry.class);
      Sidekick.Location localLocation = LocationUtilities.androidLocationToSidekickLocation(paramAnonymousLocation);
      return GoogleNowRemoteService.this.mStaticMapCache.get(localLocation, localFrequentPlaceEntry, paramAnonymousBoolean);
    }
    
    public String getVersion()
      throws RemoteException
    {
      return "0.1";
    }
    
    public void invalidateEntries()
      throws RemoteException
    {
      GoogleNowRemoteService.this.mEntryProvider.invalidate();
    }
    
    public boolean isReminderSmartActionSupported(int paramAnonymousInt)
      throws RemoteException
    {
      return GoogleNowRemoteService.this.mReminderSmartActionUtil.isSmartActionSupportedByDevice(paramAnonymousInt);
    }
    
    public boolean isUserOptedIn()
      throws RemoteException
    {
      return GoogleNowRemoteService.this.mOptInSettings.isUserOptedIn();
    }
    
    public void logAction(LoggingRequest paramAnonymousLoggingRequest)
      throws RemoteException
    {
      switch (paramAnonymousLoggingRequest.mType)
      {
      default: 
        Log.e(GoogleNowRemoteService.TAG, "Unknown logging request type: " + paramAnonymousLoggingRequest.mType);
      }
      for (;;)
      {
        return;
        GoogleNowRemoteService.this.mUserInteractionLogger.logAnalyticsAction(paramAnonymousLoggingRequest.mAnalyticsActionType, paramAnonymousLoggingRequest.mLabel);
        return;
        GoogleNowRemoteService.this.mUserInteractionLogger.logMetricsAction(paramAnonymousLoggingRequest.mEntry, paramAnonymousLoggingRequest.mEntryActionType, paramAnonymousLoggingRequest.mClickAction);
        return;
        GoogleNowRemoteService.this.mUserInteractionLogger.logInternalAction(paramAnonymousLoggingRequest.mAnalyticsActionType, paramAnonymousLoggingRequest.mLabel);
        return;
        Iterator localIterator = paramAnonymousLoggingRequest.mLabelCountMap.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          GoogleNowRemoteService.this.mUserInteractionLogger.logAnalyticsAction(paramAnonymousLoggingRequest.mAnalyticsActionType, str, ((Integer)paramAnonymousLoggingRequest.mLabelCountMap.get(str)).intValue());
        }
      }
    }
    
    public void markCalendarEntryDismissed(long paramAnonymousLong)
    {
      GoogleNowRemoteService.this.mCalendarDataProvider.markEventAsDismissed(paramAnonymousLong);
    }
    
    public boolean optIn(String paramAnonymousString)
      throws RemoteException
    {
      Account localAccount = GoogleNowRemoteService.this.mLoginHelper.findAccountByName(paramAnonymousString);
      if (localAccount == null) {
        Log.e(GoogleNowRemoteService.TAG, "Invalid account: " + paramAnonymousString);
      }
      while (GoogleNowRemoteService.this.mOptInSettings.canAccountRunNow(localAccount) != 1) {
        return false;
      }
      if (GoogleNowRemoteService.this.mOptInSettings.isAccountOptedIn(localAccount)) {
        return true;
      }
      return GoogleNowRemoteService.this.mNowOptInHelper.optIn(localAccount);
    }
    
    public void optIntoLocationReporting()
      throws RemoteException
    {
      GoogleNowRemoteService.this.mLocationReportingOptInHelper.optIntoLocationReportingAsync();
    }
    
    public boolean placeholder1(boolean paramAnonymousBoolean)
      throws RemoteException
    {
      return true;
    }
    
    public void prefetchImage(Uri paramAnonymousUri)
      throws RemoteException
    {
      GoogleNowRemoteService.this.mImageLoader.load(paramAnonymousUri);
    }
    
    public Intent preparePhotoGalleryIntent(List<ProtoParcelable> paramAnonymousList, int paramAnonymousInt)
      throws RemoteException
    {
      GoogleNowRemoteService.this.mVelvetImageGalleryHelper.setupImagesForGalleryFromProtos(paramAnonymousList);
      return GoogleNowRemoteService.this.mVelvetImageGalleryHelper.createImageGalleryIntent(paramAnonymousInt);
    }
    
    public void queueDismissEntryAction(ProtoParcelable paramAnonymousProtoParcelable, boolean paramAnonymousBoolean)
      throws RemoteException
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable.getProto(Sidekick.Entry.class);
      if (paramAnonymousBoolean)
      {
        GoogleNowRemoteService.this.mUserInteractionLogger.cancelMetricsAction(localEntry, 1);
        return;
      }
      GoogleNowRemoteService.this.mUserInteractionLogger.queueMetricsAction(localEntry, 1);
    }
    
    public void recordBackOfCardShown()
      throws RemoteException
    {
      GoogleNowRemoteService.this.mFirstUseCardHandler.recordBackOfCardShown();
    }
    
    public void recordCardSwipedForDismiss()
    {
      GoogleNowRemoteService.this.mFirstUseCardHandler.recordCardSwipedForDismiss();
    }
    
    public void recordExecutedUserActions(List<ProtoParcelable> paramAnonymousList)
    {
      ArrayList localArrayList = Lists.newArrayListWithCapacity(paramAnonymousList.size());
      Iterator localIterator = paramAnonymousList.iterator();
      while (localIterator.hasNext()) {
        localArrayList.add(((ProtoParcelable)localIterator.next()).getProto(Sidekick.ExecutedUserAction.class));
      }
      GoogleNowRemoteService.this.mExecutedUserActionStore.saveExecutedUserActions(localArrayList);
    }
    
    public void recordFeedbackPromptAction(ProtoParcelable paramAnonymousProtoParcelable, int paramAnonymousInt)
      throws RemoteException
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable.getProto(Sidekick.Entry.class);
      logAction(LoggingRequest.forMetricsAction(localEntry, paramAnonymousInt, null));
      GoogleNowRemoteService.this.mEntryProvider.updateEntries(new RemoveFeedbackPromptEntryUpdater(localEntry));
    }
    
    public void recordFirstUseCardDismiss(int paramAnonymousInt)
      throws RemoteException
    {
      FirstUseCardHandler.FirstUseCardType localFirstUseCardType = FirstUseCardHandler.FirstUseCardType.getByOrdinal(paramAnonymousInt);
      if (localFirstUseCardType != null) {
        GoogleNowRemoteService.this.mFirstUseCardHandler.recordDismiss(localFirstUseCardType);
      }
    }
    
    public void recordFirstUseCardView(int paramAnonymousInt)
      throws RemoteException
    {
      FirstUseCardHandler.FirstUseCardType localFirstUseCardType = FirstUseCardHandler.FirstUseCardType.getByOrdinal(paramAnonymousInt);
      if (localFirstUseCardType != null) {
        GoogleNowRemoteService.this.mFirstUseCardHandler.recordView(localFirstUseCardType);
      }
    }
    
    public void recordGoogleNowPromoDismissed()
    {
      GoogleNowRemoteService.this.mOptInSettings.setGetGoogleNowButtonDismissed();
    }
    
    public void recordPredictiveInteraction()
      throws RemoteException
    {
      GoogleNowRemoteService.this.mSidekickInteractionManager.recordPredictiveInteraction();
    }
    
    public void recordUserAction(ProtoParcelable paramAnonymousProtoParcelable, int paramAnonymousInt)
      throws RemoteException
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable.getProto(Sidekick.Entry.class);
      Sidekick.Action localAction = getActionFromEntry(localEntry, paramAnonymousInt);
      if (localAction != null) {
        GoogleNowRemoteService.this.mExecutedUserActionStore.saveAction(localEntry, localAction);
      }
    }
    
    public void recordViewAction(ProtoParcelable paramAnonymousProtoParcelable, long paramAnonymousLong, int paramAnonymousInt, boolean paramAnonymousBoolean)
      throws RemoteException
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable.getProto(Sidekick.Entry.class);
      Sidekick.Action localAction = getActionFromEntry(localEntry, 21);
      if (localAction != null) {
        GoogleNowRemoteService.this.mExecutedUserActionStore.saveViewAction(localEntry, localAction, paramAnonymousLong, paramAnonymousInt, paramAnonymousBoolean);
      }
    }
    
    public void refreshEntries()
      throws RemoteException
    {
      GoogleNowRemoteService.this.mEntryProvider.refreshEntriesPreserveMoreState();
    }
    
    public void removeGroupChildEntry(ProtoParcelable paramAnonymousProtoParcelable1, ProtoParcelable paramAnonymousProtoParcelable2)
      throws RemoteException
    {
      Sidekick.Entry localEntry1 = (Sidekick.Entry)paramAnonymousProtoParcelable1.getProto(Sidekick.Entry.class);
      Sidekick.Entry localEntry2 = (Sidekick.Entry)paramAnonymousProtoParcelable2.getProto(Sidekick.Entry.class);
      GoogleNowRemoteService.this.mUserInteractionLogger.logMetricsAction(localEntry2, 1, null);
      GoogleNowRemoteService.this.mEntryProvider.removeGroupChildEntries(localEntry1, ImmutableSet.of(localEntry2));
    }
    
    public List<TrainingQuestionNode> resolveTrainingQuestions(List<ProtoParcelable> paramAnonymousList)
      throws RemoteException
    {
      ArrayList localArrayList = Lists.newArrayListWithCapacity(paramAnonymousList.size());
      Iterator localIterator = paramAnonymousList.iterator();
      while (localIterator.hasNext()) {
        localArrayList.add(((ProtoParcelable)localIterator.next()).getProto(Sidekick.QuestionNode.class));
      }
      return GoogleNowRemoteService.this.mTrainingQuestionManager.resolveQuestions(localArrayList);
    }
    
    public void savePreferences(Bundle paramAnonymousBundle)
      throws RemoteException
    {
      SharedPreferencesContext.updatePreferences(paramAnonymousBundle, ((NowConfigurationPreferences)GoogleNowRemoteService.this.mNowPreferences.get()).edit()).apply();
    }
    
    public void sendPendingTrainingAnswers()
      throws RemoteException
    {
      GoogleNowRemoteService.this.mTrainingQuestionManager.sendAnswers();
    }
    
    public void sendTrainingAction(ProtoParcelable paramAnonymousProtoParcelable1, ProtoParcelable paramAnonymousProtoParcelable2, ProtoParcelable paramAnonymousProtoParcelable3)
      throws RemoteException
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable1.getProto(Sidekick.Entry.class);
      Sidekick.Question localQuestion = (Sidekick.Question)paramAnonymousProtoParcelable2.getProto(Sidekick.Question.class);
      Sidekick.Action localAction = (Sidekick.Action)paramAnonymousProtoParcelable3.getProto(Sidekick.Action.class);
      GoogleNowRemoteService.this.mTrainingQuestionManager.sendAction(localEntry, localQuestion, localAction);
    }
    
    public void setTrafficSharerHiddenState(long paramAnonymousLong, boolean paramAnonymousBoolean)
      throws RemoteException
    {
      ((NowConfigurationPreferences)GoogleNowRemoteService.this.mNowPreferences.get()).setTrafficSharerHiddenState(GoogleNowRemoteService.this, paramAnonymousLong, paramAnonymousBoolean);
    }
    
    public void setTrainingAnswer(ProtoParcelable paramAnonymousProtoParcelable1, ProtoParcelable paramAnonymousProtoParcelable2, ProtoParcelable paramAnonymousProtoParcelable3)
      throws RemoteException
    {
      Sidekick.Question localQuestion = (Sidekick.Question)paramAnonymousProtoParcelable1.getProto(Sidekick.Question.class);
      Sidekick.Question.Answer localAnswer = (Sidekick.Question.Answer)paramAnonymousProtoParcelable2.getProto(Sidekick.Question.Answer.class);
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable3.getProto(Sidekick.Entry.class);
      GoogleNowRemoteService.this.mTrainingQuestionManager.setAnswer(localQuestion, localAnswer, localEntry);
    }
    
    public void snoozeReminder(ProtoParcelable paramAnonymousProtoParcelable)
      throws RemoteException
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)paramAnonymousProtoParcelable.getProto(Sidekick.Entry.class);
      Sidekick.Action localAction = ProtoUtils.findAction(localEntry, 34, new int[0]);
      if (localAction != null) {
        new SnoozeReminderTask(GoogleNowRemoteService.this.mNetworkClient, GoogleNowRemoteService.this.getApplicationContext(), localEntry, localAction, GoogleNowRemoteService.this.mClock, GoogleNowRemoteService.this.mDataBackendVersionStore).execute(new Void[0]);
      }
    }
    
    public String translateInPlace(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3)
      throws RemoteException
    {
      Sidekick.TranslateInPlaceQuery localTranslateInPlaceQuery = new Sidekick.TranslateInPlaceQuery().setQueryText(paramAnonymousString1).setSourceLanguageCode(paramAnonymousString2).setTargetLanguageCode(paramAnonymousString3);
      Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setTranslateInPlaceQuery(localTranslateInPlaceQuery);
      Sidekick.ResponsePayload localResponsePayload = GoogleNowRemoteService.this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload);
      if ((localResponsePayload == null) || (!localResponsePayload.hasTranslateInPlaceResponse())) {
        return null;
      }
      return localResponsePayload.getTranslateInPlaceResponse().getTranslatedText();
    }
  };
  private CalendarDataProvider mCalendarDataProvider;
  private Clock mClock;
  private DataBackendVersionStore mDataBackendVersionStore;
  private EntryProvider mEntryProvider;
  private ExecutedUserActionStore mExecutedUserActionStore;
  private FirstUseCardHandler mFirstUseCardHandler;
  private UriLoader<Drawable> mImageLoader;
  private LocationReportingOptInHelper mLocationReportingOptInHelper;
  private LoginHelper mLoginHelper;
  private NetworkClient mNetworkClient;
  private UriLoader<Drawable> mNonCachingImageLoader;
  private NowOptInHelper mNowOptInHelper;
  private Supplier<NowConfigurationPreferences> mNowPreferences;
  private NowOptInSettings mOptInSettings;
  private ReminderSaver mReminderSaver;
  private ReminderSmartActionUtil mReminderSmartActionUtil;
  private RemoteServiceHelper mRemoteServiceHelper;
  private SearchHistoryHelper mSearchHistoryHelper;
  private SidekickInjector mSidekickInjector;
  private SidekickInteractionManager mSidekickInteractionManager;
  private StaticMapCache mStaticMapCache;
  private TrainingQuestionManager mTrainingQuestionManager;
  private UserInteractionLogger mUserInteractionLogger;
  private VelvetImageGalleryHelper mVelvetImageGalleryHelper;
  
  public IBinder onBind(Intent paramIntent)
  {
    return this.mBinder;
  }
  
  public void onCreate()
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    this.mSidekickInjector = localVelvetServices.getSidekickInjector();
    this.mEntryProvider = this.mSidekickInjector.getEntryProvider();
    this.mExecutedUserActionStore = this.mSidekickInjector.getExecutedUserActionStore();
    this.mUserInteractionLogger = localVelvetServices.getCoreServices().getUserInteractionLogger();
    this.mOptInSettings = localVelvetServices.getCoreServices().getNowOptInSettings();
    this.mCalendarDataProvider = this.mSidekickInjector.getCalendarDataProvider();
    this.mStaticMapCache = this.mSidekickInjector.getStaticMapCache();
    this.mNowPreferences = this.mSidekickInjector.getNowConfigurationPreferencesSupplier();
    this.mTrainingQuestionManager = this.mSidekickInjector.getTrainingQuestionManager();
    this.mLocationReportingOptInHelper = this.mSidekickInjector.getLocationReportingOptInHelper();
    this.mImageLoader = localVelvetServices.getImageLoader();
    this.mNonCachingImageLoader = localVelvetServices.getNonCachingImageLoader();
    this.mVelvetImageGalleryHelper = this.mSidekickInjector.getVelvetImageGalleryHelper();
    this.mReminderSaver = localVelvetServices.getFactory().createReminderSaver();
    this.mClock = localVelvetServices.getCoreServices().getClock();
    this.mNetworkClient = this.mSidekickInjector.getNetworkClient();
    this.mDataBackendVersionStore = this.mSidekickInjector.getDataBackendVersionStore();
    this.mLoginHelper = localVelvetServices.getCoreServices().getLoginHelper();
    this.mNowOptInHelper = this.mSidekickInjector.getNowOptInHelper();
    this.mFirstUseCardHandler = this.mSidekickInjector.getFirstUseCardHandler();
    this.mSearchHistoryHelper = localVelvetServices.getGlobalSearchServices().getSearchHistoryHelper();
    this.mSidekickInteractionManager = this.mSidekickInjector.getInteractionManager();
    this.mReminderSmartActionUtil = this.mSidekickInjector.getReminderSmartActionUtil();
    this.mRemoteServiceHelper = new RemoteServiceHelper(this.mEntryProvider, this.mSidekickInjector.getEntryInvalidator(), localVelvetServices.getLocationOracle(), this.mOptInSettings, localVelvetServices.getCoreServices().getGooglePlayServicesHelper(), this.mSidekickInjector.getRenderingContextPopulator(), localVelvetServices.getCoreServices().getSearchUrlHelper(), this.mSidekickInjector.getEntryTreePruner(), this.mFirstUseCardHandler);
  }
  
  public void onDestroy()
  {
    this.mExecutedUserActionStore.persist();
    super.onDestroy();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.remoteservice.GoogleNowRemoteService
 * JD-Core Version:    0.7.0.1
 */