package com.google.android.sidekick.shared.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.remoteapi.LoggingRequest;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.sidekick.shared.util.WebSearchUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GeoLocatedPhoto;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import com.google.geo.sidekick.Sidekick.QuestionNode;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

public class ClientCardContainer
  implements PredictiveCardContainer
{
  private final Context mAppContext;
  private NowCardsViewWrapper mCardsView;
  private final IntentStarter mIntentStarter;
  private final NowRemoteClient mNowRemoteClient;
  private CardRenderingContext mRenderingContext;
  private final TvRecognitionManager mTvRecognitionManager;
  private final UndoDismissManager mUndoDismissManager;
  private Rect mWindowInsets;
  
  public ClientCardContainer(Context paramContext, IntentStarter paramIntentStarter, NowRemoteClient paramNowRemoteClient, TvRecognitionManager paramTvRecognitionManager, UndoDismissManager paramUndoDismissManager)
  {
    this.mAppContext = paramContext;
    this.mIntentStarter = paramIntentStarter;
    this.mNowRemoteClient = paramNowRemoteClient;
    this.mTvRecognitionManager = paramTvRecognitionManager;
    this.mUndoDismissManager = paramUndoDismissManager;
  }
  
  public void cancelDismissEntryAction(Sidekick.Entry paramEntry)
  {
    this.mNowRemoteClient.queueDismissEntryAction(paramEntry, true);
  }
  
  public void deleteNotificationsForEntry(Sidekick.Entry paramEntry)
  {
    this.mNowRemoteClient.deleteNotificationsForEntry(paramEntry);
  }
  
  public void dismissEntry(Sidekick.Entry paramEntry)
  {
    this.mNowRemoteClient.dismissEntry(paramEntry, true);
  }
  
  public void dismissEntry(Sidekick.Entry paramEntry, boolean paramBoolean)
  {
    this.mNowRemoteClient.dismissEntry(paramEntry, paramBoolean);
  }
  
  public void dismissGroupChildEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
  {
    this.mNowRemoteClient.removeGroupChildEntry(paramEntry1, paramEntry2);
  }
  
  public void enableSearchHistoryForActiveAccount(Consumer<Boolean> paramConsumer)
  {
    this.mNowRemoteClient.enableSearchHistoryForActiveAccount(paramConsumer);
  }
  
  public CardRenderingContext getCardRenderingContext()
  {
    return this.mRenderingContext;
  }
  
  public UriLoader<Drawable> getImageLoader()
  {
    return this.mNowRemoteClient.getImageLoader();
  }
  
  @Nullable
  public IntentStarter getIntentStarter()
  {
    return this.mIntentStarter;
  }
  
  public UriLoader<Drawable> getNonCachingImageLoader()
  {
    return this.mNowRemoteClient.getNonCachingImageLoader();
  }
  
  public StaticMapLoader getStaticMapLoader()
  {
    return this.mNowRemoteClient.getStaticMapLoader();
  }
  
  @Nullable
  public TvRecognitionManager getTvRecognitionManager()
  {
    return this.mTvRecognitionManager;
  }
  
  public UndoDismissManager getUndoDismissManager()
  {
    return this.mUndoDismissManager;
  }
  
  public void invalidateEntries()
  {
    this.mNowRemoteClient.invalidateEntries();
  }
  
  public boolean isReminderSmartActionSupported(int paramInt)
  {
    return this.mNowRemoteClient.isReminderSmartActionSupported(paramInt);
  }
  
  public void logAction(Sidekick.Entry paramEntry, int paramInt, @Nullable Sidekick.ClickAction paramClickAction)
  {
    LoggingRequest localLoggingRequest = LoggingRequest.forMetricsAction(paramEntry, paramInt, paramClickAction);
    this.mNowRemoteClient.logAction(localLoggingRequest);
  }
  
  public void logAnalyticsAction(String paramString1, String paramString2)
  {
    LoggingRequest localLoggingRequest = LoggingRequest.forAnalyticsAction(paramString1, paramString2);
    this.mNowRemoteClient.logAction(localLoggingRequest);
  }
  
  public void markCalendarEntryDismissed(long paramLong)
  {
    this.mNowRemoteClient.markCalendarEntryDismissed(paramLong);
  }
  
  public void optIntoLocationReportingAsync()
  {
    this.mNowRemoteClient.optIntoLocationReportingAsync();
  }
  
  public Intent preparePhotoGalleryIntent(List<Sidekick.GeoLocatedPhoto> paramList, int paramInt)
  {
    return this.mNowRemoteClient.preparePhotoGalleryIntent(paramList, paramInt);
  }
  
  public void pulseTrainingIcon()
  {
    this.mCardsView.pulseTrainIcon();
  }
  
  public void queueDismissEntryAction(Sidekick.Entry paramEntry)
  {
    this.mNowRemoteClient.queueDismissEntryAction(paramEntry, false);
  }
  
  public void recordCardSwipedForDismiss()
  {
    this.mNowRemoteClient.recordCardSwipedForDismiss();
  }
  
  public void recordFeedbackPromptAction(Sidekick.Entry paramEntry, int paramInt)
  {
    this.mNowRemoteClient.recordFeedbackPromptAction(paramEntry, paramInt);
  }
  
  public void recordFirstUseCardDismiss(int paramInt)
  {
    this.mNowRemoteClient.recordFirstUseCardDismiss(paramInt);
  }
  
  public void recordFirstUseCardView(int paramInt)
  {
    this.mNowRemoteClient.recordFirstUseCardView(paramInt);
  }
  
  public void refreshEntries()
  {
    this.mNowRemoteClient.refreshEntries();
  }
  
  public ListenableFuture<Collection<TrainingQuestionNode>> resolveTrainingQuestionsAsync(Collection<Sidekick.QuestionNode> paramCollection)
  {
    return this.mNowRemoteClient.resolveTrainingQuestionsAsync(paramCollection);
  }
  
  public void savePreferences(Bundle paramBundle)
  {
    this.mNowRemoteClient.savePreferences(paramBundle);
  }
  
  public void sendPendingTrainingAnswers()
  {
    this.mNowRemoteClient.sendPendingTrainingAnswers();
  }
  
  public void sendTrainingAction(Sidekick.Entry paramEntry, Sidekick.Question paramQuestion, Sidekick.Action paramAction)
  {
    this.mNowRemoteClient.sendTrainingAction(paramEntry, paramQuestion, paramAction);
  }
  
  public void setCardRenderingContext(CardRenderingContext paramCardRenderingContext)
  {
    this.mRenderingContext = paramCardRenderingContext;
  }
  
  void setCardsView(NowCardsViewWrapper paramNowCardsViewWrapper)
  {
    this.mCardsView = paramNowCardsViewWrapper;
  }
  
  public void setTrafficSharerHiddenState(long paramLong, boolean paramBoolean)
  {
    this.mNowRemoteClient.setTrafficSharerHiddenState(paramLong, paramBoolean);
  }
  
  public void setTrainingAnswer(Sidekick.Question paramQuestion, Sidekick.Question.Answer paramAnswer, @Nullable Sidekick.Entry paramEntry)
  {
    this.mNowRemoteClient.setTrainingAnswer(paramQuestion, paramAnswer, paramEntry);
  }
  
  public void setWindowInsets(Rect paramRect)
  {
    this.mWindowInsets = paramRect;
  }
  
  public void snoozeReminder(Sidekick.Entry paramEntry)
  {
    this.mNowRemoteClient.snoozeReminder(paramEntry);
  }
  
  public boolean startWebSearch(String paramString, @Nullable Location paramLocation)
  {
    WebSearchUtils.startWebSearch(this.mAppContext, paramString, paramLocation);
    return true;
  }
  
  public void toggleBackOfCard(EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    this.mCardsView.toggleBackOfCard(paramEntryCardViewAdapter, true, null);
    this.mNowRemoteClient.recordBackOfCardShown();
  }
  
  @Nullable
  public String translateInPlace(String paramString1, String paramString2, String paramString3)
  {
    return this.mNowRemoteClient.translateInPlace(paramString1, paramString2, paramString3);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.ClientCardContainer
 * JD-Core Version:    0.7.0.1
 */