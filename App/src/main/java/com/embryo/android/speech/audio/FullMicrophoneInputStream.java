package com.embryo.android.speech.audio;

import android.media.MediaSyncEvent;
import android.media.audiofx.NoiseSuppressor;

import javax.annotation.Nullable;

public class FullMicrophoneInputStream
        extends MicrophoneInputStream {
    private NoiseSuppressor mNoiseSuppressor;

    public FullMicrophoneInputStream(int paramInt1, int paramInt2, boolean paramBoolean1, @Nullable SpeakNowSoundPlayer paramSpeakNowSoundPlayer, @Nullable com.embryo.android.voicesearch.audio.AudioRouter paramAudioRouter, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger, boolean paramBoolean2) {
        super(paramInt1, paramInt2, paramBoolean1, paramSpeakNowSoundPlayer, paramAudioRouter, paramSpeechLibLogger, paramBoolean2);
    }

    protected void createNoiseSuppressor() {
        if (this.mNoiseSuppressionEnabled) {
            try {
                this.mNoiseSuppressor = NoiseSuppressor.create(this.mAudioRecord.getAudioSessionId());
                if (this.mNoiseSuppressor.setEnabled(true) != 0) {
                    this.mNoiseSuppressor = null;
                    return;
                }
                this.mSpeechLibLogger.recordBreakdownEvent(51);
                return;
            } catch (Exception localException) {
                this.mNoiseSuppressor = null;
            }
        }
    }

    protected void releaseNoiseSuppressor() {
        if (this.mNoiseSuppressor != null) {
            this.mNoiseSuppressor.release();
            this.mNoiseSuppressor = null;
        }
    }

    protected void startRecording() {
        SpeakNowSoundPlayer localSpeakNowSoundPlayer = this.mBeepPlayer;
        MediaSyncEvent localMediaSyncEvent = null;
        if (localSpeakNowSoundPlayer != null) {
            int i = this.mBeepPlayer.playSpeakNowSound();
            localMediaSyncEvent = null;
            if (i > 0) {
                localMediaSyncEvent = MediaSyncEvent.createEvent(1);
                localMediaSyncEvent.setAudioSessionId(i);
            }
        }
        if (localMediaSyncEvent != null) {
            this.mAudioRecord.startRecording(localMediaSyncEvent);
            this.mSpeechLibLogger.recordBreakdownEvent(52);
            return;
        }
        this.mAudioRecord.startRecording();
        this.mSpeechLibLogger.recordBreakdownEvent(53);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     FullMicrophoneInputStream

 * JD-Core Version:    0.7.0.1

 */