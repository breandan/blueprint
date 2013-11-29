package com.google.android.voicesearch;

import android.content.Context;
import android.widget.Toast;
import com.google.android.search.core.Feature;
import com.google.android.search.core.SearchError;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.search.core.discoursecontext.Mention;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.LoggingState;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.TtsState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.actions.CardDecision;
import com.google.android.velvet.actions.CardDecisionFactory;
import com.google.android.velvet.presenter.MainContentPresenter;
import com.google.android.velvet.presenter.MainContentPresenter.Transaction;
import com.google.android.voicesearch.fragments.action.CancelAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.speech.logs.VoicesearchClientLogProto.EmbeddedParserDetails;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nullable;

public class SearchCardController
  implements CardController
{
  private ActionData mActionData;
  private final AutoExecutionRunnable mAutoExecutionRunnable = new AutoExecutionRunnable();
  private final ExecutorService mBackgroundExecutor;
  private final CardDecisionFactory mCardDecisionFactory;
  private final Clock mClock;
  private final Context mContext;
  private CountDownStatus mCountDownStatus = CountDownStatus.NOT_STARTED;
  private final Supplier<DiscourseContext> mDiscourseContextSupplier;
  private final VelvetEventBus mEventBus;
  private final MainContentPresenter mPresenter;
  private Query mQuery;
  private boolean mReady;
  private Runnable mRunnable;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  
  public SearchCardController(Context paramContext, ExecutorService paramExecutorService, Clock paramClock, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, VelvetEventBus paramVelvetEventBus, Supplier<DiscourseContext> paramSupplier, CardDecisionFactory paramCardDecisionFactory, MainContentPresenter paramMainContentPresenter, Query paramQuery, ActionData paramActionData)
  {
    this.mContext = paramContext;
    this.mBackgroundExecutor = paramExecutorService;
    this.mClock = paramClock;
    this.mUiThreadExecutor = ((ScheduledSingleThreadedExecutor)Preconditions.checkNotNull(paramScheduledSingleThreadedExecutor));
    this.mEventBus = paramVelvetEventBus;
    this.mDiscourseContextSupplier = paramSupplier;
    this.mCardDecisionFactory = paramCardDecisionFactory;
    this.mPresenter = paramMainContentPresenter;
    this.mQuery = paramQuery;
    this.mActionData = paramActionData;
  }
  
  private CardDecision getCardDecision(VoiceAction paramVoiceAction)
  {
    return this.mEventBus.getActionState().getCardDecision(paramVoiceAction);
  }
  
  private void logDismissed()
  {
    EventLogger.recordClientEvent(14, Integer.valueOf(getActionTypeLog()));
    suppressGwsLoggableEvent(16384);
    setGwsLoggableEvent(2048);
  }
  
  private void setGwsLoggableEvent(int paramInt)
  {
    this.mEventBus.getLoggingState().setGwsLoggableEvent(this.mActionData, paramInt);
  }
  
  private void setPumpkinLoggableEvent(int paramInt)
  {
    this.mEventBus.getLoggingState().setPumpkinLoggableEvent(this.mActionData, paramInt);
  }
  
  private void suppressGwsLoggableEvent(int paramInt)
  {
    this.mEventBus.getLoggingState().suppressGwsLoggableEvent(this.mActionData, paramInt);
  }
  
  public void cancelAction()
  {
    this.mEventBus.getActionState().cancelCurrentAction();
  }
  
  public boolean cancelCountDown()
  {
    boolean bool;
    if (this.mCountDownStatus == CountDownStatus.STARTED) {
      bool = true;
    }
    for (;;)
    {
      this.mUiThreadExecutor.cancelExecute(this.mAutoExecutionRunnable);
      try
      {
        this.mEventBus.removeObserver(this.mAutoExecutionRunnable);
        label36:
        this.mCountDownStatus = CountDownStatus.CANCELLED;
        return bool;
        bool = false;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        break label36;
      }
    }
  }
  
  public int getActionTypeLog()
  {
    if (this.mActionData == ActionData.NONE) {
      return 0;
    }
    return this.mActionData.getActionTypeLog((DiscourseContext)this.mDiscourseContextSupplier.get());
  }
  
  public Executor getBackgroundExecutor()
  {
    return this.mBackgroundExecutor;
  }
  
  public CharSequence getDisplayPrompt(VoiceAction paramVoiceAction)
  {
    return getCardDecision(paramVoiceAction).getDisplayPrompt();
  }
  
  @Deprecated
  public Query getQuery()
  {
    return this.mEventBus.getQueryState().get();
  }
  
  public boolean isFollowOnEnabledForRequest()
  {
    return this.mCardDecisionFactory.isFollowOnEnabledForRequest(this.mQuery);
  }
  
  @Deprecated
  public boolean isTtsPlaying()
  {
    return this.mEventBus.getTtsState().isPlaying();
  }
  
  public void logAttach()
  {
    EventLogger.recordClientEvent(4, Integer.valueOf(getActionTypeLog()));
  }
  
  public void logCancelCountDownByUser()
  {
    EventLogger.recordClientEvent(72, Integer.valueOf(getActionTypeLog()));
    suppressGwsLoggableEvent(16384);
    setGwsLoggableEvent(4096);
  }
  
  public void logExecute(boolean paramBoolean)
  {
    EventLogger.recordClientEvent(13, Integer.valueOf(getActionTypeLog()));
    if (paramBoolean)
    {
      setGwsLoggableEvent(512);
      return;
    }
    setGwsLoggableEvent(256);
  }
  
  public void logOpenExternalApp()
  {
    EventLogger.recordClientEvent(50, Integer.valueOf(getActionTypeLog()));
    setGwsLoggableEvent(1024);
  }
  
  public void mentionEntity(@Nullable Object paramObject)
  {
    if ((Feature.DISCOURSE_CONTEXT.isEnabled()) && (paramObject != null)) {
      ((DiscourseContext)this.mDiscourseContextSupplier.get()).mention(paramObject, new Mention(this.mClock.currentTimeMillis()));
    }
  }
  
  public void onCardActionComplete()
  {
    this.mEventBus.getActionState().onCardActionComplete(this.mActionData);
    if (Feature.DISCOURSE_CONTEXT.isEnabled()) {
      ((DiscourseContext)this.mDiscourseContextSupplier.get()).clearCurrentActionAccept();
    }
  }
  
  public void onDismissed(VoiceAction paramVoiceAction)
  {
    logDismissed();
    ActionState localActionState = this.mEventBus.getActionState();
    if ((localActionState.isCancelable()) && (paramVoiceAction == localActionState.getTopMostVoiceAction())) {
      cancelAction();
    }
    while (!Feature.DISCOURSE_CONTEXT.isEnabled()) {
      return;
    }
    ((DiscourseContext)this.mDiscourseContextSupplier.get()).clearCurrentActionCancel();
  }
  
  public void onUserInteraction()
  {
    this.mEventBus.getQueryState().onUserInteraction();
    this.mEventBus.getActionState().onUserInteraction();
    this.mEventBus.getTtsState().onUserInteraction();
  }
  
  public void post(MainContentPresenter.Transaction paramTransaction)
  {
    this.mPresenter.post(paramTransaction);
  }
  
  public void removeVoiceAction(VoiceAction paramVoiceAction)
  {
    this.mEventBus.getActionState().removeVoiceAction(paramVoiceAction);
  }
  
  public void retryError(SearchError paramSearchError)
  {
    paramSearchError.retry(this.mEventBus.getQueryState(), this.mQuery);
  }
  
  public void setQuery(Query paramQuery, ActionData paramActionData)
  {
    this.mQuery = paramQuery;
    this.mActionData = paramActionData;
  }
  
  public boolean showCard(VoiceAction paramVoiceAction)
  {
    CardDecision localCardDecision = getCardDecision(paramVoiceAction);
    ActionState localActionState = this.mEventBus.getActionState();
    if ((Feature.FOLLOW_ON.isEnabled()) && (this.mQuery.isVoiceSearch()) && (localCardDecision.shouldStartFollowOnVoiceSearch()) && (localActionState.takeStartFollowOn())) {
      localActionState.setModifiedCommit(this.mActionData, Query.EMPTY.voiceSearchFollowOn());
    }
    updateActionTts(paramVoiceAction);
    ActionV2Protos.ActionV2 localActionV2;
    int i;
    if (this.mActionData.hasActionV2(0))
    {
      localActionV2 = this.mActionData.getActionV2(0);
      if ((Feature.DISCOURSE_CONTEXT.isEnabled()) && (!(paramVoiceAction instanceof SearchError)) && (!(paramVoiceAction instanceof CancelAction))) {
        ((DiscourseContext)this.mDiscourseContextSupplier.get()).mentionVoiceActionExperimental(localActionV2, paramVoiceAction, localCardDecision, this.mActionData.getEventId());
      }
      if (this.mReady) {
        break label249;
      }
      this.mReady = true;
      i = this.mActionData.getActionParserLog();
      if (i != 93) {
        break label234;
      }
      setPumpkinLoggableEvent(65536);
      EventLogger.recordClientEvent(i, new EmbeddedParserLogData(getActionTypeLog(), this.mActionData.getEmbeddedParserDetails()));
    }
    for (;;)
    {
      if (this.mActionData.shouldLogShownToGws()) {
        this.mEventBus.getLoggingState().setGwsLoggableEvent(this.mActionData, 32768);
      }
      localActionState.onReady(paramVoiceAction);
      return true;
      localActionV2 = null;
      break;
      label234:
      EventLogger.recordClientEvent(i, Integer.valueOf(getActionTypeLog()));
    }
    label249:
    return false;
  }
  
  public void showToast(int paramInt)
  {
    Toast.makeText(this.mContext, paramInt, 0).show();
  }
  
  public long startCountDown(VoiceAction paramVoiceAction, Runnable paramRunnable)
  {
    this.mCountDownStatus = CountDownStatus.STARTED;
    this.mRunnable = paramRunnable;
    CardDecision localCardDecision = getCardDecision(paramVoiceAction);
    if (localCardDecision.getCountDownDurationMs() > 0L) {
      this.mUiThreadExecutor.executeDelayed(this.mAutoExecutionRunnable, localCardDecision.getCountDownDurationMs());
    }
    for (;;)
    {
      this.mEventBus.addObserver(this.mAutoExecutionRunnable);
      return localCardDecision.getCountDownDurationMs();
      this.mAutoExecutionRunnable.run();
    }
  }
  
  public boolean takeStartCountDown(VoiceAction paramVoiceAction)
  {
    return (!this.mQuery.isIntentQuery()) && (!this.mQuery.isTextSearch()) && (getCardDecision(paramVoiceAction).shouldAutoExecute()) && (this.mCountDownStatus == CountDownStatus.NOT_STARTED) && (this.mEventBus.getActionState().takeStartCountdown());
  }
  
  public void updateActionTts(VoiceAction paramVoiceAction)
  {
    TtsState localTtsState = this.mEventBus.getTtsState();
    CardDecision localCardDecision = getCardDecision(paramVoiceAction);
    if ((localCardDecision != null) && (localCardDecision.shouldPlayTts()))
    {
      localTtsState.requestPlay(paramVoiceAction, localCardDecision.getVocalizedPrompt());
      return;
    }
    localTtsState.discard(paramVoiceAction);
  }
  
  public void updateCardDecision(VoiceAction paramVoiceAction)
  {
    this.mEventBus.getActionState().setCardDecision(paramVoiceAction, this.mCardDecisionFactory.makeDecision(paramVoiceAction, this.mActionData, this.mQuery));
  }
  
  public class AutoExecutionRunnable
    implements VelvetEventBus.Observer, Runnable
  {
    private boolean mTtsDone = false;
    
    public AutoExecutionRunnable() {}
    
    private void maybeExecuteAction()
    {
      if ((this.mTtsDone) && (SearchCardController.this.mCountDownStatus == SearchCardController.CountDownStatus.COMPLETED) && (SearchCardController.this.mRunnable != null)) {
        SearchCardController.this.mRunnable.run();
      }
    }
    
    public void onStateChanged(VelvetEventBus.Event paramEvent)
    {
      if ((paramEvent.hasTtsChanged()) && (!this.mTtsDone) && (SearchCardController.this.mEventBus.getTtsState().isDone())) {
        this.mTtsDone = true;
      }
      try
      {
        SearchCardController.this.mEventBus.removeObserver(this);
        label46:
        maybeExecuteAction();
        return;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        break label46;
      }
    }
    
    public void run()
    {
      if (SearchCardController.this.mCountDownStatus == SearchCardController.CountDownStatus.STARTED)
      {
        SearchCardController.access$102(SearchCardController.this, SearchCardController.CountDownStatus.COMPLETED);
        maybeExecuteAction();
      }
    }
  }
  
  private static enum CountDownStatus
  {
    static
    {
      CANCELLED = new CountDownStatus("CANCELLED", 2);
      COMPLETED = new CountDownStatus("COMPLETED", 3);
      CountDownStatus[] arrayOfCountDownStatus = new CountDownStatus[4];
      arrayOfCountDownStatus[0] = NOT_STARTED;
      arrayOfCountDownStatus[1] = STARTED;
      arrayOfCountDownStatus[2] = CANCELLED;
      arrayOfCountDownStatus[3] = COMPLETED;
      $VALUES = arrayOfCountDownStatus;
    }
    
    private CountDownStatus() {}
  }
  
  public static class EmbeddedParserLogData
  {
    public final int actionType;
    public final VoicesearchClientLogProto.EmbeddedParserDetails embeddedParserDetails;
    
    public EmbeddedParserLogData(int paramInt, VoicesearchClientLogProto.EmbeddedParserDetails paramEmbeddedParserDetails)
    {
      this.actionType = paramInt;
      this.embeddedParserDetails = paramEmbeddedParserDetails;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.SearchCardController
 * JD-Core Version:    0.7.0.1
 */