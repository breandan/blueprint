package com.google.android.sidekick.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import com.google.android.search.core.google.UserInteractionLogger;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.history.SearchHistoryHelper;
import com.google.android.search.core.preferences.NowConfigurationPreferences;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.shared.util.UriLoader;
import com.google.android.sidekick.main.actions.ReminderSmartActionUtil;
import com.google.android.sidekick.main.actions.RemoveFeedbackPromptEntryUpdater;
import com.google.android.sidekick.main.actions.SnoozeReminderTask;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.main.inject.VelvetImageGalleryHelper;
import com.google.android.sidekick.main.location.LocationReportingOptInHelper;
import com.google.android.sidekick.main.tv.VelvetTvRecognitionManager;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.client.TvRecognitionManager;
import com.google.android.sidekick.shared.client.UndoDismissManager;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.StaticMapLoader;
import com.google.android.velvet.presenter.FirstUseCardHandler;
import com.google.android.velvet.presenter.TgPresenter;
import com.google.android.velvet.presenter.VelvetPresenter;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import javax.annotation.Nullable;

public class TgPredictiveCardContainer
  extends InProcessPredictiveCardContainer
{
  private CardRenderingContext mCardRenderingContext;
  private final Clock mClock;
  private final Object mCreateLock = new Object();
  private final DataBackendVersionStore mDataBackendVersionStore;
  private final EntryProvider mEntryProvider;
  private final SpeechLevelSource mSpeechLevelSource;
  private TgPresenter mTgPresenter;
  private VelvetTvRecognitionManager mTvRecognitionManager;
  
  public TgPredictiveCardContainer(Context paramContext, UserInteractionLogger paramUserInteractionLogger, NetworkClient paramNetworkClient, EntryProvider paramEntryProvider, Clock paramClock, DataBackendVersionStore paramDataBackendVersionStore, Supplier<NowConfigurationPreferences> paramSupplier, LocationReportingOptInHelper paramLocationReportingOptInHelper, CalendarDataProvider paramCalendarDataProvider, LoginHelper paramLoginHelper, SearchHistoryHelper paramSearchHistoryHelper, SpeechLevelSource paramSpeechLevelSource, StaticMapLoader paramStaticMapLoader, TrainingQuestionManager paramTrainingQuestionManager, CardRenderingContext paramCardRenderingContext, UriLoader<Drawable> paramUriLoader1, UriLoader<Drawable> paramUriLoader2, VelvetImageGalleryHelper paramVelvetImageGalleryHelper, FirstUseCardHandler paramFirstUseCardHandler, UndoDismissManager paramUndoDismissManager, ReminderSmartActionUtil paramReminderSmartActionUtil)
  {
    super(paramContext, paramUserInteractionLogger, paramNetworkClient, paramSupplier, paramLocationReportingOptInHelper, paramCalendarDataProvider, paramLoginHelper, paramSearchHistoryHelper, paramStaticMapLoader, paramTrainingQuestionManager, paramUriLoader1, paramUriLoader2, paramVelvetImageGalleryHelper, paramFirstUseCardHandler, paramUndoDismissManager, paramReminderSmartActionUtil);
    this.mCardRenderingContext = paramCardRenderingContext;
    this.mEntryProvider = paramEntryProvider;
    this.mClock = paramClock;
    this.mDataBackendVersionStore = paramDataBackendVersionStore;
    this.mSpeechLevelSource = paramSpeechLevelSource;
  }
  
  protected void dismissEntryImpl(Sidekick.Entry paramEntry)
  {
    this.mEntryProvider.handleDismissedEntries(ImmutableSet.of(paramEntry));
  }
  
  protected void dismissGroupChildEntryImpl(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2)
  {
    this.mEntryProvider.removeGroupChildEntries(paramEntry1, ImmutableSet.of(paramEntry2));
  }
  
  public CardRenderingContext getCardRenderingContext()
  {
    ExtraPreconditions.checkMainThread();
    return this.mCardRenderingContext;
  }
  
  @Nullable
  public IntentStarter getIntentStarter()
  {
    if (this.mTgPresenter.isAttached()) {
      return this.mTgPresenter.getVelvetPresenter().getIntentStarter();
    }
    return null;
  }
  
  @Nullable
  public TvRecognitionManager getTvRecognitionManager()
  {
    synchronized (this.mCreateLock)
    {
      if (this.mTvRecognitionManager == null) {
        this.mTvRecognitionManager = new VelvetTvRecognitionManager(this.mTgPresenter.getEventBus(), this.mSpeechLevelSource);
      }
      return this.mTvRecognitionManager;
    }
  }
  
  public void invalidateEntries()
  {
    this.mEntryProvider.invalidate();
  }
  
  public void pulseTrainingIcon()
  {
    this.mTgPresenter.pulseTrainingIcon();
  }
  
  public void recordFeedbackPromptAction(Sidekick.Entry paramEntry, int paramInt)
  {
    logAction(paramEntry, paramInt, null);
    this.mEntryProvider.updateEntries(new RemoveFeedbackPromptEntryUpdater(paramEntry));
  }
  
  public void refreshEntries()
  {
    this.mEntryProvider.refreshEntriesPreserveMoreState();
  }
  
  public void setCardRenderingContext(CardRenderingContext paramCardRenderingContext)
  {
    ExtraPreconditions.checkMainThread();
    this.mCardRenderingContext = paramCardRenderingContext;
  }
  
  public void setTgPresenter(TgPresenter paramTgPresenter)
  {
    this.mTgPresenter = paramTgPresenter;
  }
  
  public void snoozeReminder(Sidekick.Entry paramEntry)
  {
    Sidekick.Action localAction = ProtoUtils.findAction(paramEntry, 34, new int[0]);
    if (localAction != null) {
      new SnoozeReminderTask(this.mNetworkClient, this.mAppContext, paramEntry, localAction, this.mClock, this.mDataBackendVersionStore).execute(new Void[0]);
    }
  }
  
  public boolean startWebSearch(String paramString, @Nullable Location paramLocation)
  {
    return this.mTgPresenter.startWebSearch(paramString, paramLocation);
  }
  
  public void toggleBackOfCard(EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    this.mTgPresenter.toggleBackOfCard(paramEntryCardViewAdapter);
    this.mFirstUseCardHandler.recordBackOfCardShown();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.TgPredictiveCardContainer
 * JD-Core Version:    0.7.0.1
 */