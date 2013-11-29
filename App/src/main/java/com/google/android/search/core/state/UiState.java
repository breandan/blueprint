package com.google.android.search.core.state;

import android.os.Bundle;
import com.google.android.search.core.Feature;
import com.google.android.search.shared.api.Query;
import com.google.android.velvet.ActionData;
import com.google.android.voicesearch.EffectOnWebResults;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UiState
  extends VelvetState
{
  private ActionData mCurrentActionData;
  private Query mCurrentQuery = Query.EMPTY;
  private EffectOnWebResults mEffectOnWebResults = EffectOnWebResults.NO_EFFECT;
  private final VelvetEventBus mEventBus;
  private int mFlags;
  
  public UiState(VelvetEventBus paramVelvetEventBus)
  {
    super(paramVelvetEventBus, 4);
    this.mEventBus = paramVelvetEventBus;
    this.mFlags = 512;
  }
  
  private boolean clearAndSetFlags(int paramInt1, int paramInt2)
  {
    int i = paramInt2 | this.mFlags & (paramInt1 ^ 0xFFFFFFFF);
    if (this.mFlags != i)
    {
      this.mFlags = i;
      return true;
    }
    return false;
  }
  
  private boolean clearFlags(int paramInt)
  {
    return clearAndSetFlags(paramInt, 0);
  }
  
  private boolean setFlags(int paramInt)
  {
    return clearAndSetFlags(0, paramInt);
  }
  
  private boolean updateEffectOnWebResults()
  {
    if (this.mCurrentActionData != null)
    {
      if (this.mEventBus.getQueryState().shouldOverrideEffectOnWebResults()) {}
      for (EffectOnWebResults localEffectOnWebResults = ActionData.ANSWER_IN_SRP.getEffectOnWebResults(); localEffectOnWebResults != this.mEffectOnWebResults; localEffectOnWebResults = this.mCurrentActionData.getEffectOnWebResults())
      {
        this.mEffectOnWebResults = localEffectOnWebResults;
        return true;
      }
    }
    return false;
  }
  
  private boolean updateShouldShowCards()
  {
    if (this.mEventBus.getQueryState().adClickInProgress()) {}
    for (boolean bool = false; bool; bool = this.mEventBus.getActionState().haveCards()) {
      return setFlags(2);
    }
    return clearFlags(2);
  }
  
  private boolean updateShouldShowError()
  {
    boolean bool1 = Feature.CONNECTION_ERROR_CARDS.isEnabled();
    boolean bool2 = false;
    if (!bool1) {
      if (this.mEventBus.getQueryState().getError() == null) {
        break label40;
      }
    }
    label40:
    for (int i = 1; i != 0; i = 0)
    {
      bool2 = setFlags(4);
      return bool2;
    }
    return clearFlags(4);
  }
  
  private boolean updateShouldShowKeyboard()
  {
    if (this.mEventBus.getQueryState().isEditingQuery())
    {
      if (!setFlags(256)) {}
    }
    else {
      while (clearFlags(256))
      {
        if (!setFlags(512)) {
          break;
        }
        return true;
      }
    }
    return false;
  }
  
  private boolean updateShouldShowSpinner()
  {
    QueryState localQueryState = this.mEventBus.getQueryState();
    boolean bool1 = localQueryState.haveCommit();
    int i = 0;
    if (bool1)
    {
      Query localQuery = localQueryState.getCommittedQuery();
      boolean bool2 = localQuery.isTextOrVoiceWebSearchWithQueryChars();
      i = 0;
      if (bool2)
      {
        boolean bool3 = localQueryState.hasNetworkActionError();
        i = 0;
        if (!bool3)
        {
          boolean bool4 = localQueryState.areWebSearchResultsFinished();
          i = 0;
          if (!bool4) {
            i = 1;
          }
        }
      }
      if ((localQuery.shouldShowCards()) && (!this.mEventBus.getActionState().isReady())) {
        i = 1;
      }
    }
    if (localQueryState.adClickInProgress()) {
      i = 1;
    }
    if (localQueryState.getError() != null) {
      i = 0;
    }
    if (i != 0) {
      return setFlags(8);
    }
    return clearFlags(8);
  }
  
  private boolean updateShouldShowWebView()
  {
    QueryState localQueryState = this.mEventBus.getQueryState();
    ActionState localActionState = this.mEventBus.getActionState();
    int i;
    if (localQueryState.adClickInProgress()) {
      i = 0;
    }
    while (i != 0)
    {
      return setFlags(1);
      if (!localQueryState.isLoadingWebSearchResults()) {
        i = 0;
      } else if (this.mEffectOnWebResults.shouldPreventWebResults()) {
        i = 0;
      } else if (localQueryState.hasWebviewStateError()) {
        i = 0;
      } else if (!localActionState.isReady()) {
        i = 0;
      } else if (localActionState.isCancelable()) {
        i = 0;
      } else if ((this.mEffectOnWebResults.shouldSuppressWebResults()) && (this.mCurrentActionData != null) && (!this.mCurrentActionData.isNetworkAction()) && (!localQueryState.areNetworkResultsFinished())) {
        i = 0;
      } else if (!localQueryState.isWebViewReadyToShow()) {
        i = 0;
      } else {
        i = 1;
      }
    }
    return clearFlags(1);
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.println(this);
  }
  
  public void forceReportShouldShowKeyboardChanged()
  {
    setFlags(512);
  }
  
  public EffectOnWebResults getEffectOnWebResults()
  {
    return this.mEffectOnWebResults;
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    if (this.mEffectOnWebResults != null) {}
    for (int i = this.mEffectOnWebResults.ordinal();; i = -1)
    {
      paramBundle.putInt("velvet:ui_state:effect_on_web_results", i);
      paramBundle.putInt("velvet:ui_state:flags", this.mFlags);
      paramBundle.putParcelable("velvet:ui_state:action_data", this.mCurrentActionData);
      paramBundle.putParcelable("velvet:ui_state:current_query", this.mCurrentQuery);
      return;
    }
  }
  
  protected void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    QueryState localQueryState = this.mEventBus.getQueryState();
    ActionState localActionState = this.mEventBus.getActionState();
    boolean bool1 = paramEvent.hasQueryChanged();
    boolean bool2 = false;
    int i = 0;
    int j = 0;
    boolean bool3 = false;
    if (bool1)
    {
      Query localQuery = localQueryState.getCommittedQuery();
      bool3 = localQueryState.hasNetworkActionError();
      boolean bool5 = localQuery.getCommitId() < this.mCurrentQuery.getCommitId();
      boolean bool6 = false;
      i = 0;
      j = 0;
      if (bool5)
      {
        if (this.mCurrentQuery.isValidSearch())
        {
          boolean bool7 = localQuery.isTextSearch();
          i = 0;
          j = 0;
          if (bool7) {}
        }
        else
        {
          j = 64;
          i = 128;
        }
        this.mCurrentQuery = localQuery;
        bool6 = false | setFlags(1024);
      }
      bool2 = bool6 | updateShouldShowError() | updateShouldShowKeyboard();
    }
    int k;
    if (paramEvent.hasActionChanged())
    {
      ActionData localActionData = localActionState.getActionData();
      if (localActionData != null)
      {
        if (!localActionData.isEmpty()) {
          break label345;
        }
        j = 128;
        i = 64;
      }
      if ((Objects.equal(localActionData, this.mCurrentActionData)) || ((localActionData != null) && (localActionData.isFollowOnForPreviousAction()))) {
        break label404;
      }
      k = 1;
      label211:
      if (k != 0) {
        i |= 0x20;
      }
      this.mCurrentActionData = localActionData;
      if ((k == 0) || (!setFlags(16))) {
        break label410;
      }
    }
    label404:
    label410:
    for (boolean bool4 = true;; bool4 = false)
    {
      bool2 |= bool4;
      if (bool3)
      {
        j &= 0xFFFFFF7F;
        i |= 0x80;
      }
      if ((j != 0) || (i != 0)) {
        bool2 |= clearAndSetFlags(i, j);
      }
      if ((paramEvent.hasQueryChanged()) || (paramEvent.hasActionChanged())) {
        bool2 = bool2 | updateEffectOnWebResults() | updateShouldShowCards() | updateShouldShowWebView() | updateShouldShowSpinner();
      }
      if (bool2) {
        notifyChanged();
      }
      return;
      label345:
      if (!localActionState.isReady()) {
        break;
      }
      if (takeSuppressCorporaInitially())
      {
        j = 64;
        i = 128;
      }
      if (((0x40 & this.mFlags) == 0) || (!localActionState.isReady()) || (localActionState.haveCards())) {
        break;
      }
      j = 128;
      i = 64;
      break;
      k = 0;
      break label211;
    }
  }
  
  public void restoreInstanceState(Bundle paramBundle)
  {
    this.mFlags = paramBundle.getInt("velvet:ui_state:flags");
    int i = paramBundle.getInt("velvet:ui_state:effect_on_web_results");
    if (i == -1) {}
    for (EffectOnWebResults localEffectOnWebResults = null;; localEffectOnWebResults = EffectOnWebResults.values()[i])
    {
      this.mEffectOnWebResults = localEffectOnWebResults;
      this.mCurrentActionData = ((ActionData)paramBundle.getParcelable("velvet:ui_state:action_data"));
      this.mCurrentQuery = ((Query)paramBundle.getParcelable("velvet:ui_state:current_query"));
      return;
    }
  }
  
  public boolean shouldShowCards()
  {
    return (0x2 & this.mFlags) != 0;
  }
  
  public boolean shouldShowError()
  {
    return (0x4 & this.mFlags) != 0;
  }
  
  public boolean shouldShowKeyboard()
  {
    return (0x100 & this.mFlags) != 0;
  }
  
  public boolean shouldShowSpinner()
  {
    return (0x8 & this.mFlags) != 0;
  }
  
  public boolean shouldShowWebView()
  {
    return (0x1 & this.mFlags) != 0;
  }
  
  public boolean shouldSuppressCorpora()
  {
    return (0x40 & this.mFlags) != 0;
  }
  
  public boolean takeActionUiInvalidated()
  {
    return clearFlags(16);
  }
  
  public boolean takeShouldShowKeyboardChanged()
  {
    return clearFlags(512);
  }
  
  public boolean takeShowCorporaRequest()
  {
    if (clearFlags(128))
    {
      int i = 0x40 & this.mFlags;
      boolean bool = false;
      if (i == 0) {
        bool = true;
      }
      Preconditions.checkState(bool);
      return true;
    }
    return false;
  }
  
  public boolean takeShowSearchPlate()
  {
    return clearFlags(1024);
  }
  
  boolean takeSuppressCorporaInitially()
  {
    if (this.mEffectOnWebResults.shouldSuppressWebResults()) {
      return setFlags(32);
    }
    return false;
  }
  
  public String toString()
  {
    ArrayList localArrayList = Lists.newArrayList();
    if ((0x1 & this.mFlags) != 0) {
      localArrayList.add("should-show-webview");
    }
    if ((0x2 & this.mFlags) != 0) {
      localArrayList.add("should-show-cards");
    }
    if ((0x4 & this.mFlags) != 0) {
      localArrayList.add("should-show-error");
    }
    if ((0x8 & this.mFlags) != 0) {
      localArrayList.add("should-show-spinner");
    }
    if ((0x10 & this.mFlags) != 0) {
      localArrayList.add("action-ui-invalidated");
    }
    if ((0x20 & this.mFlags) != 0) {
      localArrayList.add("corpora-initially-suppressed");
    }
    if ((0x40 & this.mFlags) != 0) {
      localArrayList.add("corpora-suppressed");
    }
    if ((0x80 & this.mFlags) != 0) {
      localArrayList.add("pending-corpora-show");
    }
    return "UiState[query=" + this.mCurrentQuery + "effects=" + this.mEffectOnWebResults + ", actionData=" + this.mCurrentActionData + ", flags=" + localArrayList + "]";
  }
  
  public void unsuppressCorpora()
  {
    if (this.mCurrentActionData != null) {
      this.mEventBus.getLoggingState().setGwsLoggableEvent(this.mCurrentActionData, 16384);
    }
    if (clearFlags(64)) {
      notifyChanged();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.state.UiState
 * JD-Core Version:    0.7.0.1
 */