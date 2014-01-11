package com.embryo.android.voicesearch.serviceapi;

import com.embryo.android.shared.util.ScheduledSingleThreadedExecutor;
import com.embryo.android.shared.util.SpeechLevelSource;
import com.google.common.base.Preconditions;

public class LevelsGenerator {
    private final ScheduledSingleThreadedExecutor mExecutor;
    private ListenerAdapter mListener;
    private final Runnable mRunnable;
    private final SpeechLevelSource mSpeechLevelSource;
    private final com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck mThreadCheck;

    public LevelsGenerator(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, SpeechLevelSource paramSpeechLevelSource) {
        this.mSpeechLevelSource = paramSpeechLevelSource;
        this.mExecutor = paramScheduledSingleThreadedExecutor;
        this.mRunnable = new Runnable() {
            public void run() {
                LevelsGenerator.this.notifyRms();
            }
        };
        this.mThreadCheck = com.embryo.android.shared.util.ExtraPreconditions.createSameThreadCheck();
    }

    private void notifyRms() {
        this.mThreadCheck.check();
        if (this.mListener == null) {
            return;
        }
        int i = this.mSpeechLevelSource.getSpeechLevel();
        this.mListener.sendRmsValue(com.embryo.android.speech.audio.SpeechLevelGenerator.convertVolumeToRmsDb(i));
        scheduleNotifyRms();
    }

    private void scheduleNotifyRms() {
        this.mExecutor.executeDelayed(this.mRunnable, 50L);
    }

    public void start(ListenerAdapter paramListenerAdapter) {
        this.mThreadCheck.check();
        this.mListener = Preconditions.checkNotNull(paramListenerAdapter);
        scheduleNotifyRms();
    }

    public void stop() {
        this.mThreadCheck.check();
        this.mExecutor.cancelExecute(this.mRunnable);
        this.mListener = null;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     LevelsGenerator

 * JD-Core Version:    0.7.0.1

 */