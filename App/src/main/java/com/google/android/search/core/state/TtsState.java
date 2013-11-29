package com.google.android.search.core.state;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.search.shared.api.Query;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class TtsState
  extends VelvetState
{
  private Query mCommittedQuery = Query.EMPTY;
  private final VelvetEventBus mEventBus;
  @Nullable
  private String mLocalTts;
  @Nullable
  private byte[] mNetworkTts;
  private int mState = 0;
  private boolean mStopTtsPlaybackPending;
  private final Settings mVoiceSettings;
  
  public TtsState(VelvetEventBus paramVelvetEventBus, Settings paramSettings)
  {
    super(paramVelvetEventBus, 8);
    this.mEventBus = paramVelvetEventBus;
    this.mVoiceSettings = paramSettings;
  }
  
  private boolean checkActionData(VoiceAction paramVoiceAction)
  {
    ActionState localActionState = this.mEventBus.getActionState();
    return (paramVoiceAction == localActionState.getTopMostVoiceAction()) && (localActionState.hasDataForQuery(this.mCommittedQuery));
  }
  
  private boolean isPending()
  {
    return (0x30 & this.mState) == 0;
  }
  
  private boolean isSet(int paramInt)
  {
    return (paramInt & this.mState) == paramInt;
  }
  
  private void setDone()
  {
    if (updateFlags(32, 16)) {
      notifyChanged();
    }
  }
  
  private boolean setFlags(int paramInt)
  {
    return setState(paramInt | this.mState);
  }
  
  private boolean setState(int paramInt)
  {
    if (paramInt != this.mState)
    {
      this.mState = paramInt;
      boolean bool1 = isSet(48);
      boolean bool2 = false;
      if (!bool1) {
        if (isSet(1))
        {
          byte[] arrayOfByte = this.mNetworkTts;
          bool2 = false;
          if (arrayOfByte != null) {}
        }
        else
        {
          boolean bool3 = isSet(6);
          bool2 = false;
          if (!bool3) {
            bool2 = true;
          }
        }
      }
      Preconditions.checkState(bool2, "Illegal state: " + toString());
      return true;
    }
    return false;
  }
  
  private boolean updateCanPlay()
  {
    ActionState localActionState = this.mEventBus.getActionState();
    QueryState localQueryState = this.mEventBus.getQueryState();
    UiState localUiState = this.mEventBus.getUiState();
    if (localQueryState.getCommittedQuery().isNotificationAnnouncement()) {
      return setFlags(8);
    }
    if ((localActionState.hasDataForQuery(this.mCommittedQuery)) && (isPending()))
    {
      ActionData localActionData = localActionState.getActionData();
      if (isSet(2)) {
        return setFlags(8);
      }
      if (this.mNetworkTts != null)
      {
        if ((localActionData.isNetworkAction()) && (isSet(4))) {
          return setFlags(8);
        }
        if ((localUiState.shouldShowWebView()) && ((localActionData.isEmpty()) || (localQueryState.isNetworkActionEmpty()))) {
          return setFlags(8);
        }
      }
    }
    return false;
  }
  
  private boolean updateFlags(int paramInt1, int paramInt2)
  {
    return setState(paramInt1 | this.mState & (paramInt2 ^ 0xFFFFFFFF));
  }
  
  public void discard(VoiceAction paramVoiceAction)
  {
    if (checkActionData(paramVoiceAction))
    {
      this.mStopTtsPlaybackPending = isSet(16);
      setDone();
    }
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.println(this);
  }
  
  public String getLocalTts()
  {
    return (String)Preconditions.checkNotNull(this.mLocalTts);
  }
  
  public byte[] getNetworkTts()
  {
    return (byte[])Preconditions.checkNotNull(this.mNetworkTts);
  }
  
  public boolean isDone()
  {
    return isSet(32);
  }
  
  public boolean isPlaying()
  {
    return isSet(16);
  }
  
  public boolean noMoreTtsToPlay()
  {
    return (isSet(32)) || ((!isSet(2)) && (!isSet(4)) && (!isSet(1)) && (!isSet(8)) && (!isSet(16)));
  }
  
  public void onNetworkTtsAvailable(Query paramQuery, byte[] paramArrayOfByte)
  {
    if (this.mEventBus.getQueryState().isCurrentCommit(paramQuery))
    {
      if (!isSet(1)) {
        break label30;
      }
      VelvetStrictMode.logW("TtsState", "Received TTS after decided not available.");
    }
    label30:
    while (this.mNetworkTts != null) {
      return;
    }
    this.mNetworkTts = paramArrayOfByte;
    updateCanPlay();
    notifyChanged();
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("velvet:tts_state:current_query", this.mCommittedQuery);
  }
  
  protected void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    QueryState localQueryState = this.mEventBus.getQueryState();
    boolean bool1 = paramEvent.hasQueryChanged();
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = localQueryState.isCurrentCommit(this.mCommittedQuery);
      bool2 = false;
      if (!bool3)
      {
        this.mCommittedQuery = localQueryState.getCommittedQuery();
        this.mStopTtsPlaybackPending = isSet(16);
        this.mLocalTts = null;
        this.mNetworkTts = null;
        if (!this.mCommittedQuery.shouldPlayTts()) {
          break label128;
        }
        bool2 = false | setState(0);
      }
    }
    if (!isDone()) {
      if ((paramEvent.hasQueryChanged()) || (paramEvent.hasActionChanged())) {
        if (localQueryState.isEditingQuery())
        {
          this.mStopTtsPlaybackPending = isSet(16);
          setDone();
        }
      }
    }
    label128:
    while (!bool2)
    {
      return;
      if (!isDone()) {}
      for (bool2 = true;; bool2 = false)
      {
        setState(32);
        break;
      }
      ActionState localActionState = this.mEventBus.getActionState();
      if ((localQueryState.areVoiceSearchResultsDone(this.mCommittedQuery)) && (this.mNetworkTts == null))
      {
        if (((localActionState.isReady()) && (!localActionState.haveCards())) || (isSet(4)))
        {
          setDone();
          return;
        }
        bool2 |= setFlags(1);
      }
      ActionData localActionData = localActionState.getActionData();
      if ((localActionState.hasDataForQuery(this.mCommittedQuery)) && (((localActionData.isEmpty()) && (isSet(1))) || ((localActionData.isNetworkAction()) && (localActionState.isReady()) && (!localActionState.haveCards()))))
      {
        setDone();
        return;
      }
      if ((paramEvent.hasActionChanged()) || (paramEvent.hasUiChanged()) || (paramEvent.hasQueryChanged())) {
        bool2 |= updateCanPlay();
      }
    }
    notifyChanged();
  }
  
  @Deprecated
  public void onUserInteraction()
  {
    if (!isDone()) {
      requestStop();
    }
  }
  
  public void requestPlay(@Nullable VoiceAction paramVoiceAction, @Nullable String paramString)
  {
    if (((paramVoiceAction != null) && (!checkActionData(paramVoiceAction))) || ((0x6 & this.mState) != 0)) {
      return;
    }
    if (!TextUtils.isEmpty(paramString))
    {
      this.mLocalTts = paramString;
      setFlags(2);
      updateCanPlay();
      notifyChanged();
      return;
    }
    if (isSet(1))
    {
      setDone();
      return;
    }
    setFlags(4);
    updateCanPlay();
    notifyChanged();
  }
  
  public void requestStop()
  {
    if (!this.mStopTtsPlaybackPending)
    {
      this.mStopTtsPlaybackPending = true;
      notifyChanged();
    }
  }
  
  public void restoreInstanceState(Bundle paramBundle)
  {
    this.mCommittedQuery = ((Query)paramBundle.getParcelable("velvet:tts_state:current_query"));
    this.mState = 32;
  }
  
  public void setDone(Query paramQuery)
  {
    if (this.mEventBus.getQueryState().isCurrentCommit(paramQuery)) {
      setDone();
    }
  }
  
  public int takePlay()
  {
    int i;
    if ((isSet(8)) && (isPending())) {
      if (isSet(2))
      {
        i = 1;
        if ((!this.mVoiceSettings.shouldTtsNeverPlay()) && ((!this.mVoiceSettings.isTtsOnlyForHandsFree()) || ((this.mCommittedQuery != null) && (this.mCommittedQuery.isEyesFree())))) {
          break label78;
        }
        i = 0;
        setDone();
      }
    }
    label78:
    while (!setFlags(16))
    {
      return i;
      i = 2;
      break;
      return 0;
    }
    notifyChanged();
    return i;
  }
  
  public boolean takeStop()
  {
    boolean bool = this.mStopTtsPlaybackPending;
    this.mStopTtsPlaybackPending = false;
    return bool;
  }
  
  public String toString()
  {
    ArrayList localArrayList = Lists.newArrayList();
    if (isSet(1)) {
      localArrayList.add("not-available-network");
    }
    if (this.mNetworkTts != null) {
      localArrayList.add("available-network");
    }
    if (isSet(2)) {
      localArrayList.add("play-local-requested");
    }
    if (isSet(4)) {
      localArrayList.add("play-network-requested");
    }
    if (isSet(8)) {
      localArrayList.add("can-play");
    }
    if (isSet(16)) {
      localArrayList.add("playing");
    }
    if (isSet(32)) {
      localArrayList.add("done");
    }
    return "TtsState(state=" + localArrayList + ", stopPending=" + this.mStopTtsPlaybackPending + ")";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.state.TtsState
 * JD-Core Version:    0.7.0.1
 */