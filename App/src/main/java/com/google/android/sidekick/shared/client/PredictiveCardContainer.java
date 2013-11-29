package com.google.android.sidekick.shared.client;

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
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.util.StaticMapLoader;
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

public abstract interface PredictiveCardContainer
{
  public abstract void cancelDismissEntryAction(Sidekick.Entry paramEntry);
  
  public abstract void deleteNotificationsForEntry(Sidekick.Entry paramEntry);
  
  public abstract void dismissEntry(Sidekick.Entry paramEntry);
  
  public abstract void dismissEntry(Sidekick.Entry paramEntry, boolean paramBoolean);
  
  public abstract void dismissGroupChildEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2);
  
  public abstract void enableSearchHistoryForActiveAccount(Consumer<Boolean> paramConsumer);
  
  public abstract CardRenderingContext getCardRenderingContext();
  
  public abstract UriLoader<Drawable> getImageLoader();
  
  @Nullable
  public abstract IntentStarter getIntentStarter();
  
  public abstract UriLoader<Drawable> getNonCachingImageLoader();
  
  public abstract StaticMapLoader getStaticMapLoader();
  
  @Nullable
  public abstract TvRecognitionManager getTvRecognitionManager();
  
  public abstract UndoDismissManager getUndoDismissManager();
  
  public abstract void invalidateEntries();
  
  public abstract boolean isReminderSmartActionSupported(int paramInt);
  
  public abstract void logAction(Sidekick.Entry paramEntry, int paramInt, @Nullable Sidekick.ClickAction paramClickAction);
  
  public abstract void logAnalyticsAction(String paramString1, String paramString2);
  
  public abstract void markCalendarEntryDismissed(long paramLong);
  
  public abstract void optIntoLocationReportingAsync();
  
  public abstract Intent preparePhotoGalleryIntent(List<Sidekick.GeoLocatedPhoto> paramList, int paramInt);
  
  public abstract void pulseTrainingIcon();
  
  public abstract void queueDismissEntryAction(Sidekick.Entry paramEntry);
  
  public abstract void recordCardSwipedForDismiss();
  
  public abstract void recordFeedbackPromptAction(Sidekick.Entry paramEntry, int paramInt);
  
  public abstract void recordFirstUseCardDismiss(int paramInt);
  
  public abstract void recordFirstUseCardView(int paramInt);
  
  public abstract void refreshEntries();
  
  public abstract ListenableFuture<Collection<TrainingQuestionNode>> resolveTrainingQuestionsAsync(Collection<Sidekick.QuestionNode> paramCollection);
  
  public abstract void savePreferences(Bundle paramBundle);
  
  public abstract void sendPendingTrainingAnswers();
  
  public abstract void sendTrainingAction(Sidekick.Entry paramEntry, Sidekick.Question paramQuestion, Sidekick.Action paramAction);
  
  public abstract void setCardRenderingContext(CardRenderingContext paramCardRenderingContext);
  
  public abstract void setTrafficSharerHiddenState(long paramLong, boolean paramBoolean);
  
  public abstract void setTrainingAnswer(Sidekick.Question paramQuestion, Sidekick.Question.Answer paramAnswer, @Nullable Sidekick.Entry paramEntry);
  
  public abstract void setWindowInsets(Rect paramRect);
  
  public abstract void snoozeReminder(Sidekick.Entry paramEntry);
  
  public abstract boolean startWebSearch(String paramString, @Nullable Location paramLocation);
  
  public abstract void toggleBackOfCard(EntryCardViewAdapter paramEntryCardViewAdapter);
  
  @Nullable
  public abstract String translateInPlace(String paramString1, String paramString2, String paramString3);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.PredictiveCardContainer
 * JD-Core Version:    0.7.0.1
 */