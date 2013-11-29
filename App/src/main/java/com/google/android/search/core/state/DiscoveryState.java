package com.google.android.search.core.state;

import android.os.Bundle;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import java.io.PrintWriter;

public class DiscoveryState
  extends VelvetState
{
  private final SearchConfig mConfig;
  private Query mCurrentCommittedQuery = Query.EMPTY;
  private boolean mPeekCounted = false;
  private final QueryState mQueryState;
  private final SearchSettings mSettings;
  private boolean mShouldPeek = false;
  private boolean mSpeechDetected = false;
  private int mState = 0;
  private final ScheduledSingleThreadedExecutor mUiExecutor;
  
  public DiscoveryState(VelvetEventBus paramVelvetEventBus, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, SearchConfig paramSearchConfig, SearchSettings paramSearchSettings)
  {
    super(paramVelvetEventBus, 16);
    this.mQueryState = paramVelvetEventBus.getQueryState();
    this.mUiExecutor = paramScheduledSingleThreadedExecutor;
    this.mConfig = paramSearchConfig;
    this.mSettings = paramSearchSettings;
  }
  
  private void postPeekDelayed(final Query paramQuery)
  {
    this.mUiExecutor.executeDelayed(new Runnable()
    {
      public void run()
      {
        if ((DiscoveryState.this.mQueryState.isCurrentCommit(paramQuery)) && (!DiscoveryState.this.mSpeechDetected))
        {
          DiscoveryState.access$202(DiscoveryState.this, true);
          DiscoveryState.this.setState(2);
        }
      }
    }, this.mConfig.getActionDiscoveryPeekDelayMillis());
  }
  
  private void setState(int paramInt)
  {
    setState(paramInt, false);
  }
  
  private void setState(int paramInt, boolean paramBoolean)
  {
    if ((this.mState != paramInt) || (paramBoolean))
    {
      this.mState = paramInt;
      notifyChanged();
    }
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("DiscoveryState:");
    String str = paramString + "  ";
    paramPrintWriter.print(str);
    paramPrintWriter.print("CurrentCommittedQuery:");
    paramPrintWriter.println(this.mCurrentCommittedQuery);
    paramPrintWriter.print(str);
    paramPrintWriter.print("State: ");
    paramPrintWriter.println(this.mState);
    paramPrintWriter.print(str);
    paramPrintWriter.print("ShouldPeek: ");
    paramPrintWriter.println(this.mShouldPeek);
    paramPrintWriter.print(str);
    paramPrintWriter.print("SpeechDetected: ");
    paramPrintWriter.println(this.mSpeechDetected);
    paramPrintWriter.print(str);
    paramPrintWriter.print("PeekCounted: ");
    paramPrintWriter.println(this.mPeekCounted);
  }
  
  public void onDiscoveryCardsPeeked()
  {
    if (!this.mPeekCounted)
    {
      this.mPeekCounted = true;
      this.mQueryState.reportLatencyEvent(43);
      if (this.mSettings.getVoiceActionDiscoveryInstantPeekCount() < this.mConfig.getActionDiscoveryMaxInstantPeekCount()) {
        this.mSettings.incrementVoiceActionDiscoveryInstantPeekCount();
      }
    }
  }
  
  public void onPeekCardDragged()
  {
    if (this.mShouldPeek)
    {
      this.mShouldPeek = false;
      setState(2);
      this.mQueryState.reportLatencyEvent(44);
    }
  }
  
  public void onReadyForSpeech(Query paramQuery)
  {
    if ((this.mQueryState.isCurrentCommit(paramQuery)) && (this.mCurrentCommittedQuery.isVoiceSearch()) && (this.mState == 0)) {
      postPeekDelayed(paramQuery);
    }
  }
  
  public void onSpeechDetected(Query paramQuery)
  {
    if (this.mQueryState.isCurrentCommit(paramQuery)) {
      this.mSpeechDetected = true;
    }
  }
  
  protected void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    if ((paramEvent.hasQueryChanged()) && (!this.mQueryState.isCurrentCommit(this.mCurrentCommittedQuery)))
    {
      this.mCurrentCommittedQuery = this.mQueryState.getCommittedQuery();
      this.mPeekCounted = false;
      this.mSpeechDetected = false;
      this.mShouldPeek = false;
      setState(0);
      if ((this.mCurrentCommittedQuery.isVoiceSearch()) && (this.mSettings.getVoiceActionDiscoveryInstantPeekCount() < this.mConfig.getActionDiscoveryMaxInstantPeekCount()))
      {
        this.mShouldPeek = true;
        setState(1);
      }
    }
  }
  
  public void restoreInstanceState(Bundle paramBundle)
  {
    this.mState = paramBundle.getInt("velvet:discovery_state:state");
    if (this.mState != 0)
    {
      this.mShouldPeek = paramBundle.getBoolean("velvet:discovery_state:should_peek");
      this.mSpeechDetected = paramBundle.getBoolean("velvet:discovery_state:speech_detected");
      this.mPeekCounted = paramBundle.getBoolean("velvet:discovery_state:peek_counted");
      this.mCurrentCommittedQuery = ((Query)paramBundle.getParcelable("velvet:discovery_state:commited_query"));
    }
  }
  
  public void saveInstanceState(Bundle paramBundle)
  {
    paramBundle.putInt("velvet:discovery_state:state", this.mState);
    if (this.mState != 0)
    {
      paramBundle.putBoolean("velvet:discovery_state:should_peek", this.mShouldPeek);
      paramBundle.putBoolean("velvet:discovery_state:speech_detected", this.mSpeechDetected);
      paramBundle.putBoolean("velvet:discovery_state:peek_counted", this.mPeekCounted);
      paramBundle.putParcelable("velvet:discovery_state:commited_query", this.mCurrentCommittedQuery);
    }
  }
  
  public boolean shouldHideAll()
  {
    return this.mState == 0;
  }
  
  public boolean shouldPeek()
  {
    return this.mShouldPeek;
  }
  
  public boolean shouldShowAll()
  {
    return this.mState == 2;
  }
  
  public boolean shouldShowIntroCard()
  {
    return this.mState == 1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.state.DiscoveryState
 * JD-Core Version:    0.7.0.1
 */