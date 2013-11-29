package com.google.android.sidekick.main.tv;

import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.state.VelvetEventBus.Event;
import com.google.android.search.core.state.VelvetEventBus.Observer;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.sidekick.shared.client.TvRecognitionManager;
import com.google.android.sidekick.shared.util.Tag;
import javax.annotation.Nullable;

public class VelvetTvRecognitionManager
  extends TvRecognitionManager
  implements VelvetEventBus.Observer
{
  private static final String TAG = Tag.getTag(VelvetTvRecognitionManager.class);
  @Nullable
  private Query mCommittedQuery;
  @Nullable
  private final VelvetEventBus mEventBus;
  private final SpeechLevelSource mSpeechLevelSource;
  
  public VelvetTvRecognitionManager(VelvetEventBus paramVelvetEventBus, SpeechLevelSource paramSpeechLevelSource)
  {
    this.mEventBus = paramVelvetEventBus;
    this.mSpeechLevelSource = paramSpeechLevelSource;
  }
  
  @Nullable
  public SpeechLevelSource getSpeechLevelSource()
  {
    return this.mSpeechLevelSource;
  }
  
  public void onStateChanged(VelvetEventBus.Event paramEvent)
  {
    if (this.mCommittedQuery == null) {}
    for (;;)
    {
      return;
      if (paramEvent.hasQueryChanged())
      {
        if (!this.mEventBus.getQueryState().getCommittedQuery().isPredictiveTvSearch()) {
          setRecognitionState(1);
        }
        while (!this.mEventBus.getQueryState().isCurrentCommit(this.mCommittedQuery))
        {
          this.mEventBus.removeObserver(this);
          this.mCommittedQuery = null;
          return;
          if (this.mEventBus.getQueryState().getError() != null) {
            setRecognitionState(3);
          } else if (this.mEventBus.getActionState().hasDataForQuery(this.mCommittedQuery)) {
            setRecognitionState(1);
          } else {
            setRecognitionState(2);
          }
        }
      }
    }
  }
  
  public void startRecognition()
  {
    if (this.mCommittedQuery == null) {
      this.mEventBus.addObserver(this);
    }
    for (;;)
    {
      QueryState localQueryState = this.mEventBus.getQueryState();
      localQueryState.commit(localQueryState.get().predictiveTvSearch());
      this.mCommittedQuery = localQueryState.getCommittedQuery();
      setRecognitionState(2);
      return;
      this.mCommittedQuery = null;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.tv.VelvetTvRecognitionManager
 * JD-Core Version:    0.7.0.1
 */