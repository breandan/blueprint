package com.google.android.search.core.state;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.Feature;
import com.google.android.search.core.SearchError;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Util;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.actions.CardDecision;
import com.google.android.voicesearch.fragments.action.CancelAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActionState
  extends VelvetState
{
  private ActionData mActionData;
  @Nullable
  private VoiceAction mActionToExecute;
  @Nullable
  private CardDecision mCardDecision;
  private Query mCurrentQuery = Query.EMPTY;
  private SearchError mError;
  private final VelvetEventBus mEventBus;
  private int mFlags;
  @Nullable
  private Query mModifiedCommit;
  private final Set<VoiceAction> mReadyVoiceActions = Sets.newHashSet();
  private List<VoiceAction> mVoiceActions;
  private List<VoiceAction> mVoiceActionsIncludingError = Lists.newArrayList();
  
  public ActionState(VelvetEventBus paramVelvetEventBus)
  {
    super(paramVelvetEventBus, 2);
    this.mEventBus = paramVelvetEventBus;
  }
  
  private boolean cancelCardCountDownByUserInternal()
  {
    return (isReady()) && (setFlags(8)) && (setFlags(32));
  }
  
  private boolean clearFlags(int paramInt)
  {
    int i = this.mFlags & (paramInt ^ 0xFFFFFFFF);
    if (i != this.mFlags)
    {
      this.mFlags = i;
      return true;
    }
    return false;
  }
  
  public static final String debugFlagsToString(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramInt & 0x1) != 0) {
      localArrayList.add("handling");
    }
    if ((paramInt & 0x2) != 0) {
      localArrayList.add("ready");
    }
    if ((paramInt & 0x4) != 0) {
      localArrayList.add("countdown");
    }
    if ((paramInt & 0x100) != 0) {
      localArrayList.add("follow-on started");
    }
    if ((paramInt & 0x80) != 0) {
      localArrayList.add("new follow-on data");
    }
    if ((paramInt & 0x8) != 0) {
      localArrayList.add("countdown requested");
    }
    if ((paramInt & 0x10) != 0) {
      localArrayList.add("countdown cancelled");
    }
    if ((paramInt & 0x20) != 0) {
      localArrayList.add("countdown cancelled by user");
    }
    if ((paramInt & 0x40) != 0) {
      localArrayList.add("action complete");
    }
    if ((paramInt & 0x400) != 0) {
      localArrayList.add("clear discourse requested");
    }
    return localArrayList.toString();
  }
  
  private void dumpVoiceActions(String paramString1, PrintWriter paramPrintWriter, String paramString2, Iterable<VoiceAction> paramIterable)
  {
    paramPrintWriter.print(paramString1);
    paramPrintWriter.print(paramString2);
    if (paramIterable == null) {
      paramPrintWriter.println(": null");
    }
    for (;;)
    {
      return;
      paramPrintWriter.println(":");
      String str = paramString1 + "  ";
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext())
      {
        VoiceAction localVoiceAction = (VoiceAction)localIterator.next();
        paramPrintWriter.print(str);
        paramPrintWriter.println(localVoiceAction);
      }
    }
  }
  
  private boolean setFlags(int paramInt)
  {
    int i = paramInt | this.mFlags;
    if (i != this.mFlags)
    {
      this.mFlags = i;
      return true;
    }
    return false;
  }
  
  private boolean updateReadiness()
  {
    if ((this.mActionData != null) && ((this.mActionData.isEmpty()) || ((this.mVoiceActions != null) && (this.mReadyVoiceActions.containsAll(this.mVoiceActions))))) {
      return setFlags(2);
    }
    return clearFlags(2);
  }
  
  private void updateVoiceActions()
  {
    this.mVoiceActionsIncludingError = Lists.newArrayList();
    if (this.mError != null) {
      this.mVoiceActionsIncludingError.add(this.mError);
    }
    if (this.mVoiceActions != null) {
      this.mVoiceActionsIncludingError.addAll(this.mVoiceActions);
    }
    if ((isCancelable()) && (this.mVoiceActions != null) && (!this.mVoiceActions.isEmpty()) && (!(getTopMostVoiceAction() instanceof CancelAction))) {
      this.mVoiceActionsIncludingError.add(CancelAction.CANCEL_BUTTON);
    }
  }
  
  public void cancelCardCountDown()
  {
    if ((isReady()) && (setFlags(8)) && (setFlags(16))) {
      notifyChanged();
    }
  }
  
  public void cancelCardCountDownByUser()
  {
    if (cancelCardCountDownByUserInternal()) {
      notifyChanged();
    }
  }
  
  public void cancelCurrentAction()
  {
    VoiceAction localVoiceAction = getTopMostVoiceAction();
    if ((localVoiceAction != null) && (!(localVoiceAction instanceof CancelAction)))
    {
      setFlags(1024);
      CancelAction localCancelAction = CancelAction.fromVoiceAction(localVoiceAction);
      setVoiceActions(this.mActionData, Lists.newArrayList(new VoiceAction[] { localCancelAction }), CardDecision.SUPPRESS_NETWORK_DECISION);
    }
  }
  
  public void clearReadiness(ActionData paramActionData)
  {
    if ((Objects.equal(paramActionData, this.mActionData)) && (!this.mReadyVoiceActions.isEmpty()))
    {
      this.mReadyVoiceActions.clear();
      if (updateReadiness()) {
        notifyChanged();
      }
    }
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("ActionState:");
    String str = paramString + "  ";
    paramPrintWriter.print(str);
    paramPrintWriter.print("Flags: ");
    paramPrintWriter.println(debugFlagsToString(this.mFlags));
    paramPrintWriter.print(str);
    paramPrintWriter.print("CurrentQuery: ");
    paramPrintWriter.println(this.mCurrentQuery);
    paramPrintWriter.print(str);
    paramPrintWriter.print("ActionData: ");
    paramPrintWriter.println(this.mActionData);
    paramPrintWriter.print(str);
    paramPrintWriter.print("ModifiedCommit: ");
    paramPrintWriter.println(this.mModifiedCommit);
    dumpVoiceActions(str, paramPrintWriter, "VoiceActions", this.mVoiceActions);
    dumpVoiceActions(str, paramPrintWriter, "ReadyVoiceActions", this.mReadyVoiceActions);
  }
  
  @Nullable
  public ActionData getActionData()
  {
    return this.mActionData;
  }
  
  @Nonnull
  public CardDecision getCardDecision(VoiceAction paramVoiceAction)
  {
    if ((paramVoiceAction != null) && (paramVoiceAction == getTopMostVoiceAction()) && (this.mCardDecision != null)) {
      return this.mCardDecision;
    }
    return CardDecision.SUPPRESS_NETWORK_DECISION;
  }
  
  public VoiceAction getTopMostVoiceAction()
  {
    if ((this.mVoiceActions != null) && (!this.mVoiceActions.isEmpty())) {
      return (VoiceAction)this.mVoiceActions.get(0);
    }
    return null;
  }
  
  public List<VoiceAction> getVoiceActions()
  {
    if ((this.mVoiceActions == null) && (this.mError == null)) {
      return null;
    }
    return this.mVoiceActionsIncludingError;
  }
  
  public boolean hasDataForQuery(Query paramQuery)
  {
    return (this.mActionData != null) && (paramQuery.getCommitId() == this.mCurrentQuery.getCommitId());
  }
  
  public boolean haveCards()
  {
    return (isReady()) && (!this.mVoiceActionsIncludingError.isEmpty());
  }
  
  public boolean isCancelable()
  {
    return (0x200 & this.mFlags) != 0;
  }
  
  public boolean isCardActionComplete()
  {
    return (0x40 & this.mFlags) != 0;
  }
  
  public boolean isReady()
  {
    return (0x2 & this.mFlags) != 0;
  }
  
  public void maybeLogGoBack()
  {
    if ((this.mActionData != null) && (!isCardActionComplete())) {
      this.mEventBus.getLoggingState().setGwsLoggableEvent(this.mActionData, 8192);
    }
  }
  
  public void onCardActionComplete(ActionData paramActionData)
  {
    if ((isReady()) && (this.mActionData.equals(paramActionData)))
    {
      this.mFlags = (0x40 | 0xFFFFFDFF & this.mFlags);
      updateVoiceActions();
      notifyChanged();
      return;
    }
    Log.w("Velvet.ActionState", "onCardActionComplete for unrecognized data or wasn't ready");
  }
  
  @Deprecated
  public void onReady(VoiceAction paramVoiceAction)
  {
    if ((this.mVoiceActions != null) && (this.mVoiceActions.contains(paramVoiceAction)) && (!this.mReadyVoiceActions.contains(paramVoiceAction)))
    {
      this.mReadyVoiceActions.add(paramVoiceAction);
      updateReadiness();
      if (isReady()) {
        notifyChanged();
      }
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("velvet:action:action_data", this.mActionData);
    paramBundle.putParcelable("velvet:action:current_query", this.mCurrentQuery);
    paramBundle.putParcelableArrayList("velvet:action:voice_action", Util.asArrayList(this.mVoiceActions));
    paramBundle.putInt("velvet:action:flags", this.mFlags);
  }
  
  protected void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    QueryState localQueryState;
    ActionData localActionData;
    Query localQuery;
    boolean bool2;
    int i;
    int j;
    label250:
    List localList;
    label258:
    int n;
    label320:
    int i1;
    label345:
    int i2;
    label363:
    int i3;
    if (paramEvent.hasQueryChanged())
    {
      localQueryState = this.mEventBus.getQueryState();
      UiState localUiState = this.mEventBus.getUiState();
      localActionData = localQueryState.getActionData();
      localQuery = localQueryState.getCommittedQuery();
      if ((localQueryState.isEditingQuery()) && (TextUtils.isEmpty(localQueryState.get().getQueryChars()))) {
        cancelCurrentAction();
      }
      boolean bool1 = localQuery.getCommitId() < this.mCurrentQuery.getCommitId();
      bool2 = false;
      if (bool1)
      {
        int i4 = 0x4 & this.mFlags;
        bool2 = false;
        if (i4 != 0) {
          bool2 = false | cancelCardCountDownByUserInternal();
        }
      }
      SearchError localSearchError = localQueryState.getError();
      if (localSearchError != this.mError)
      {
        if (this.mError != null) {
          this.mReadyVoiceActions.remove(this.mError);
        }
        this.mError = localSearchError;
        bool2 = true;
      }
      if ((!Objects.equal(localActionData, this.mActionData)) && ((localSearchError == null) || (this.mActionData == null) || (localActionData != ActionData.NONE)))
      {
        if ((!Feature.HOTWORD_ON_SRP_UI.isEnabled()) || (!localQuery.isVoiceSearch()) || ((!localUiState.shouldShowCards()) && (!localUiState.shouldShowWebView()))) {
          break label461;
        }
        i = 1;
        if ((!Feature.FOLLOW_ON.isEnabled()) || (!localQuery.isFollowOn()) || ((localActionData != null) && (!localActionData.isFollowOnForPreviousAction()))) {
          break label467;
        }
        j = 1;
        if (localActionData != null) {
          break label473;
        }
        localList = null;
        if (localList == null) {
          break label523;
        }
        this.mActionData = localActionData;
        this.mCurrentQuery = localQuery;
        this.mModifiedCommit = null;
        boolean bool3 = localList.isEmpty();
        int m = localList.size();
        if ((bool3) || (!(localList.get(0) instanceof SearchError))) {
          break label490;
        }
        n = 1;
        if ((bool3) || (!(localList.get(m - 1) instanceof CancelAction))) {
          break label496;
        }
        i1 = 1;
        if ((n == 0) && (i1 == 0)) {
          break label514;
        }
        if (n == 0) {
          break label502;
        }
        i2 = 1;
        if (i1 == 0) {
          break label508;
        }
        i3 = 1;
        label371:
        this.mVoiceActions = localList.subList(i2, m - i3);
        label389:
        this.mActionToExecute = null;
        this.mCardDecision = null;
        this.mReadyVoiceActions.clear();
        if (j != 0) {
          this.mReadyVoiceActions.addAll(this.mVoiceActions);
        }
        this.mFlags = 261;
        updateReadiness();
        bool2 = true;
      }
    }
    for (;;)
    {
      if (bool2)
      {
        updateVoiceActions();
        updateReadiness();
        notifyChanged();
      }
      return;
      label461:
      i = 0;
      break;
      label467:
      j = 0;
      break label250;
      label473:
      localList = this.mEventBus.getQueryState().getVoiceActionsFromBackStack(localActionData);
      break label258;
      label490:
      n = 0;
      break label320;
      label496:
      i1 = 0;
      break label345;
      label502:
      i2 = 0;
      break label363;
      label508:
      i3 = 0;
      break label371;
      label514:
      this.mVoiceActions = localList;
      break label389;
      label523:
      if (hasDataForQuery(localQuery))
      {
        if (!localQuery.isRestoredState()) {
          Log.w("Velvet.ActionState", "New ActionData for non-restored query.");
        }
        this.mCurrentQuery = localQuery;
      }
      else if (((i == 0) && (j == 0)) || (localActionData != null))
      {
        this.mActionData = localActionData;
        this.mCurrentQuery = localQuery;
        this.mModifiedCommit = null;
        if (j == 0) {
          break label622;
        }
        this.mFlags = (0x200 | 0x80 | 0xFFFFFEFF & this.mFlags);
        bool2 = true;
      }
    }
    label622:
    if ((this.mActionData != null) && (this.mActionData.isFollowOnForPreviousAction())) {}
    for (int k = 512;; k = 0)
    {
      this.mFlags = k;
      this.mVoiceActions = null;
      this.mCardDecision = null;
      this.mActionToExecute = null;
      this.mReadyVoiceActions.clear();
      if (localActionData == null) {
        break;
      }
      localQueryState.reportLatencyEvent(37);
      break;
    }
  }
  
  public void onUserInteraction()
  {
    takeStartFollowOn();
  }
  
  public void removeVoiceAction(VoiceAction paramVoiceAction)
  {
    boolean bool;
    if ((paramVoiceAction == this.mError) && (this.mError != null))
    {
      this.mError = null;
      bool = true;
    }
    for (;;)
    {
      if (bool)
      {
        this.mReadyVoiceActions.remove(paramVoiceAction);
        updateVoiceActions();
        updateReadiness();
        this.mActionToExecute = null;
        notifyChanged();
      }
      return;
      if (this.mVoiceActions != null) {
        bool = this.mVoiceActions.remove(paramVoiceAction);
      } else {
        bool = false;
      }
    }
  }
  
  public void restoreInstanceState(Bundle paramBundle)
  {
    this.mActionData = ((ActionData)paramBundle.getParcelable("velvet:action:action_data"));
    this.mCurrentQuery = ((Query)paramBundle.getParcelable("velvet:action:current_query"));
    this.mVoiceActions = paramBundle.getParcelableArrayList("velvet:action:voice_action");
    this.mActionToExecute = null;
    this.mFlags = paramBundle.getInt("velvet:action:flags");
    updateVoiceActions();
    updateReadiness();
  }
  
  public void setCardDecision(VoiceAction paramVoiceAction, CardDecision paramCardDecision)
  {
    if ((paramVoiceAction != null) && (paramVoiceAction == getTopMostVoiceAction()) && (this.mCardDecision != paramCardDecision))
    {
      this.mCardDecision = ((CardDecision)Preconditions.checkNotNull(paramCardDecision));
      notifyChanged();
    }
  }
  
  public void setModifiedCommit(ActionData paramActionData, Query paramQuery)
  {
    if (paramActionData.equals(this.mActionData))
    {
      this.mModifiedCommit = ((Query)Preconditions.checkNotNull(paramQuery));
      if (isReady()) {
        notifyChanged();
      }
    }
  }
  
  public void setVoiceActions(ActionData paramActionData, List<VoiceAction> paramList, CardDecision paramCardDecision)
  {
    if (paramActionData.equals(this.mActionData))
    {
      this.mVoiceActions = ((List)Preconditions.checkNotNull(paramList));
      this.mActionToExecute = null;
      this.mCardDecision = ((CardDecision)Preconditions.checkNotNull(paramCardDecision));
      updateVoiceActions();
      this.mReadyVoiceActions.clear();
      if (this.mActionData.isFollowOnForPreviousAction()) {
        this.mReadyVoiceActions.addAll(paramList);
      }
      updateReadiness();
      notifyChanged();
    }
  }
  
  public void setVoiceActionsEmpty(ActionData paramActionData)
  {
    if ((paramActionData.equals(this.mActionData)) && ((this.mVoiceActions == null) || (!this.mVoiceActions.isEmpty())))
    {
      clearFlags(512);
      this.mVoiceActions = Collections.emptyList();
      this.mActionToExecute = null;
      updateVoiceActions();
      this.mReadyVoiceActions.clear();
      updateReadiness();
      notifyChanged();
    }
  }
  
  @Nullable
  public ActionData takeActionDataToHandle()
  {
    if ((this.mActionData != null) && (!this.mActionData.isEmpty()))
    {
      if (setFlags(1)) {
        return this.mActionData;
      }
      if (clearFlags(128)) {
        return this.mActionData;
      }
    }
    return null;
  }
  
  public VoiceAction takeActionToExecute()
  {
    VoiceAction localVoiceAction = this.mActionToExecute;
    this.mActionToExecute = null;
    return localVoiceAction;
  }
  
  public boolean takeClearDiscourseContext()
  {
    return clearFlags(1024);
  }
  
  public boolean takeCountDownCancelled()
  {
    return clearFlags(16);
  }
  
  public boolean takeCountDownCancelledByUser()
  {
    return clearFlags(32);
  }
  
  @Nullable
  public Query takeModifiedCommit()
  {
    Query localQuery = this.mModifiedCommit;
    this.mModifiedCommit = null;
    return localQuery;
  }
  
  public boolean takeStartCountdown()
  {
    return setFlags(4);
  }
  
  public boolean takeStartFollowOn()
  {
    return setFlags(256);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("Action{");
    localStringBuilder1.append("Identity=" + System.identityHashCode(this) + " ");
    localStringBuilder1.append(" Flags").append(debugFlagsToString(this.mFlags));
    StringBuilder localStringBuilder2 = localStringBuilder1.append(" Error=");
    Object localObject;
    StringBuilder localStringBuilder3;
    if (this.mError == null)
    {
      localObject = null;
      localStringBuilder2.append(localObject);
      localStringBuilder3 = localStringBuilder1.append(" ");
      if (this.mActionData != null) {
        break label144;
      }
    }
    label144:
    for (String str = "null data";; str = this.mActionData.toString())
    {
      localStringBuilder3.append(str);
      localStringBuilder1.append("}");
      return localStringBuilder1.toString();
      localObject = this.mError;
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.state.ActionState
 * JD-Core Version:    0.7.0.1
 */