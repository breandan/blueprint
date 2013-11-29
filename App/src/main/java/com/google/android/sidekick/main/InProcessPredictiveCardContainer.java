package com.google.android.sidekick.main;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.history.SearchHistoryHelper;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.main.actions.ReminderSmartActionUtil;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.inject.VelvetImageGalleryHelper;
import com.google.android.sidekick.main.location.LocationReportingOptInHelper;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.client.UndoDismissManager;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.renderingcontext.SharedPreferencesContext;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.presenter.FirstUseCardHandler;
import com.google.android.velvet.presenter.FirstUseCardHandler.FirstUseCardType;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GeoLocatedPhoto;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import com.google.geo.sidekick.Sidekick.QuestionNode;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.TranslateInPlaceQuery;
import com.google.geo.sidekick.Sidekick.TranslateInPlaceResponse;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

public abstract class InProcessPredictiveCardContainer
  implements PredictiveCardContainer
{
  private static final String TAG = Tag.getTag(InProcessPredictiveCardContainer.class);
  protected final Context mAppContext;
  private final CalendarDataProvider mCalendarDataProvider;
  private final Supplier<NowConfigurationPreferences> mConfiguration;
  protected final FirstUseCardHandler mFirstUseCardHandler;
  private final UriLoader<Drawable> mImageLoader;
  private final LocationReportingOptInHelper mLocationReportingOptInHelper;
  private final LoginHelper mLoginHelper;
  protected final NetworkClient mNetworkClient;
  private final UriLoader<Drawable> mNonCachingImageLoader;
  private final ReminderSmartActionUtil mReminderSmartActionUtil;
  private final SearchHistoryHelper mSearchHistoryHelper;
  private final StaticMapLoader mStaticMapLoader;
  private final TrainingQuestionManager mTrainingQuestionManager;
  private final UndoDismissManager mUndoDismissManager;
  private final UserInteractionLogger mUserInteractionLogger;
  private final VelvetImageGalleryHelper mVelvetImageGalleryHelper;
  private Rect mWindowInsets;
  
  public InProcessPredictiveCardContainer(Context paramContext, UserInteractionLogger paramUserInteractionLogger, NetworkClient paramNetworkClient, Supplier<NowConfigurationPreferences> paramSupplier, LocationReportingOptInHelper paramLocationReportingOptInHelper, CalendarDataProvider paramCalendarDataProvider, LoginHelper paramLoginHelper, SearchHistoryHelper paramSearchHistoryHelper, StaticMapLoader paramStaticMapLoader, TrainingQuestionManager paramTrainingQuestionManager, UriLoader<Drawable> paramUriLoader1, UriLoader<Drawable> paramUriLoader2, VelvetImageGalleryHelper paramVelvetImageGalleryHelper, FirstUseCardHandler paramFirstUseCardHandler, UndoDismissManager paramUndoDismissManager, ReminderSmartActionUtil paramReminderSmartActionUtil)
  {
    this.mAppContext = paramContext;
    this.mUserInteractionLogger = paramUserInteractionLogger;
    this.mNetworkClient = paramNetworkClient;
    this.mConfiguration = paramSupplier;
    this.mLocationReportingOptInHelper = paramLocationReportingOptInHelper;
    this.mCalendarDataProvider = paramCalendarDataProvider;
    this.mLoginHelper = paramLoginHelper;
    this.mSearchHistoryHelper = paramSearchHistoryHelper;
    this.mStaticMapLoader = paramStaticMapLoader;
    this.mTrainingQuestionManager = paramTrainingQuestionManager;
    this.mImageLoader = paramUriLoader1;
    this.mNonCachingImageLoader = paramUriLoader2;
    this.mVelvetImageGalleryHelper = paramVelvetImageGalleryHelper;
    this.mFirstUseCardHandler = paramFirstUseCardHandler;
    this.mUndoDismissManager = paramUndoDismissManager;
    this.mReminderSmartActionUtil = paramReminderSmartActionUtil;
  }
  
  public void cancelDismissEntryAction(Sidekick.Entry paramEntry)
  {
    this.mUserInteractionLogger.cancelMetricsAction(paramEntry, 1);
  }
  
  public void deleteNotificationsForEntry(Sidekick.Entry paramEntry)
  {
    this.mAppContext.startService(NotificationRefreshService.getDeleteNotificationIntent(this.mAppContext, ImmutableSet.of(paramEntry)));
  }
  
  public void dismissEntry(Sidekick.Entry paramEntry)
  {
    dismissEntry(paramEntry, true);
  }
  
  public void dismissEntry(Sidekick.Entry paramEntry, boolean paramBoolean)
  {
    dismissEntryImpl(paramEntry);
    if (paramBoolean) {
      logAction(paramEntry, 1, null);
    }
  }
  
  protected abstract void dismissEntryImpl(Sidekick.Entry paramEntry);
  
  public void dismissGroupChildEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
  {
    logAction(paramEntry2, 1, null);
    dismissGroupChildEntryImpl(paramEntry1, paramEntry2);
  }
  
  protected abstract void dismissGroupChildEntryImpl(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2);
  
  public void enableSearchHistoryForActiveAccount(Consumer<Boolean> paramConsumer)
  {
    Account localAccount = this.mLoginHelper.getAccount();
    if (localAccount == null)
    {
      Log.w(TAG, "No active account");
      paramConsumer.consume(Boolean.valueOf(false));
      return;
    }
    this.mSearchHistoryHelper.setHistoryEnabledAsync(localAccount, true, paramConsumer);
  }
  
  public UriLoader<Drawable> getImageLoader()
  {
    return this.mImageLoader;
  }
  
  public UriLoader<Drawable> getNonCachingImageLoader()
  {
    return this.mNonCachingImageLoader;
  }
  
  public StaticMapLoader getStaticMapLoader()
  {
    return this.mStaticMapLoader;
  }
  
  public UndoDismissManager getUndoDismissManager()
  {
    return this.mUndoDismissManager;
  }
  
  public boolean isReminderSmartActionSupported(int paramInt)
  {
    return this.mReminderSmartActionUtil.isSmartActionSupportedByDevice(paramInt);
  }
  
  public void logAction(Sidekick.Entry paramEntry, int paramInt, @Nullable Sidekick.ClickAction paramClickAction)
  {
    this.mUserInteractionLogger.logMetricsAction(paramEntry, paramInt, paramClickAction);
  }
  
  public void logAnalyticsAction(String paramString1, String paramString2)
  {
    this.mUserInteractionLogger.logAnalyticsAction(paramString1, paramString2);
  }
  
  public void markCalendarEntryDismissed(long paramLong)
  {
    this.mCalendarDataProvider.markEventAsDismissed(paramLong);
  }
  
  public void optIntoLocationReportingAsync()
  {
    this.mLocationReportingOptInHelper.optIntoLocationReportingAsync();
  }
  
  public Intent preparePhotoGalleryIntent(List<Sidekick.GeoLocatedPhoto> paramList, int paramInt)
  {
    this.mVelvetImageGalleryHelper.setupImagesForGallery(paramList);
    return this.mVelvetImageGalleryHelper.createImageGalleryIntent(paramInt);
  }
  
  public void queueDismissEntryAction(Sidekick.Entry paramEntry)
  {
    this.mUserInteractionLogger.queueMetricsAction(paramEntry, 1);
  }
  
  public void recordCardSwipedForDismiss()
  {
    this.mFirstUseCardHandler.recordCardSwipedForDismiss();
  }
  
  public void recordFirstUseCardDismiss(int paramInt)
  {
    FirstUseCardHandler.FirstUseCardType localFirstUseCardType = FirstUseCardHandler.FirstUseCardType.getByOrdinal(paramInt);
    if (localFirstUseCardType != null) {
      this.mFirstUseCardHandler.recordDismiss(localFirstUseCardType);
    }
  }
  
  public void recordFirstUseCardView(int paramInt)
  {
    FirstUseCardHandler.FirstUseCardType localFirstUseCardType = FirstUseCardHandler.FirstUseCardType.getByOrdinal(paramInt);
    if (localFirstUseCardType != null) {
      this.mFirstUseCardHandler.recordView(localFirstUseCardType);
    }
  }
  
  public ListenableFuture<Collection<TrainingQuestionNode>> resolveTrainingQuestionsAsync(Collection<Sidekick.QuestionNode> paramCollection)
  {
    return this.mTrainingQuestionManager.resolveQuestionsAsync(paramCollection);
  }
  
  public void savePreferences(Bundle paramBundle)
  {
    SharedPreferencesContext.updatePreferences(paramBundle, ((NowConfigurationPreferences)this.mConfiguration.get()).edit()).apply();
  }
  
  public void sendPendingTrainingAnswers()
  {
    this.mTrainingQuestionManager.sendAnswers();
  }
  
  public void sendTrainingAction(Sidekick.Entry paramEntry, Sidekick.Question paramQuestion, Sidekick.Action paramAction)
  {
    this.mTrainingQuestionManager.sendAction(paramEntry, paramQuestion, paramAction);
  }
  
  public void setTrafficSharerHiddenState(long paramLong, boolean paramBoolean)
  {
    ((NowConfigurationPreferences)this.mConfiguration.get()).setTrafficSharerHiddenState(this.mAppContext, paramLong, paramBoolean);
  }
  
  public void setTrainingAnswer(Sidekick.Question paramQuestion, Sidekick.Question.Answer paramAnswer, @Nullable Sidekick.Entry paramEntry)
  {
    this.mTrainingQuestionManager.setAnswer(paramQuestion, paramAnswer, paramEntry);
  }
  
  public void setWindowInsets(Rect paramRect)
  {
    this.mWindowInsets = paramRect;
  }
  
  @Nullable
  public String translateInPlace(String paramString1, String paramString2, String paramString3)
  {
    ExtraPreconditions.checkNotMainThread();
    Sidekick.TranslateInPlaceQuery localTranslateInPlaceQuery = new Sidekick.TranslateInPlaceQuery().setQueryText(paramString1).setSourceLanguageCode(paramString2).setTargetLanguageCode(paramString3);
    Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setTranslateInPlaceQuery(localTranslateInPlaceQuery);
    Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload);
    if ((localResponsePayload == null) || (!localResponsePayload.hasTranslateInPlaceResponse())) {
      return null;
    }
    return localResponsePayload.getTranslateInPlaceResponse().getTranslatedText();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.InProcessPredictiveCardContainer
 * JD-Core Version:    0.7.0.1
 */