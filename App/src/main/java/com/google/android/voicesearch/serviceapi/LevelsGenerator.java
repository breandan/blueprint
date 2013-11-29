package com.google.android.voicesearch.serviceapi;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.speech.audio.SpeechLevelGenerator;
import com.google.common.base.Preconditions;

public class LevelsGenerator
{
  private final ScheduledSingleThreadedExecutor mExecutor;
  private ListenerAdapter mListener;
  private final Runnable mRunnable;
  private final SpeechLevelSource mSpeechLevelSource;
  private final ExtraPreconditions.ThreadCheck mThreadCheck;
  
  public LevelsGenerator(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, SpeechLevelSource paramSpeechLevelSource)
  {
    this.mSpeechLevelSource = paramSpeechLevelSource;
    this.mExecutor = paramScheduledSingleThreadedExecutor;
    this.mRunnable = new Runnable()
    {
      public void run()
      {
        LevelsGenerator.this.notifyRms();
      }
    };
    this.mThreadCheck = ExtraPreconditions.createSameThreadCheck();
  }
  
  private void notifyRms()
  {
    this.mThreadCheck.check();
    if (this.mListener == null) {
      return;
    }
    int i = this.mSpeechLevelSource.getSpeechLevel();
    this.mListener.sendRmsValue(SpeechLevelGenerator.convertVolumeToRmsDb(i));
    scheduleNotifyRms();
  }
  
  private void scheduleNotifyRms()
  {
    this.mExecutor.executeDelayed(this.mRunnable, 50L);
  }
  
  public void start(ListenerAdapter paramListenerAdapter)
  {
    this.mThreadCheck.check();
    this.mListener = ((ListenerAdapter)Preconditions.checkNotNull(paramListenerAdapter));
    scheduleNotifyRms();
  }
  
  public void stop()
  {
    this.mThreadCheck.check();
    this.mExecutor.cancelExecute(this.mRunnable);
    this.mListener = null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.serviceapi.LevelsGenerator
 * JD-Core Version:    0.7.0.1
 */