package com.embryo.android.speech.audio;

import android.text.TextUtils;

import javax.annotation.Nullable;

public class SingleRecordingAudioStore
        implements AudioStore {
    private AudioRecording mLastAudioRecording;
    private String mLastRequestId;

    @Nullable
    public synchronized AudioRecording getAudio(String requestId) {
        if (TextUtils.equals(requestId, mLastRequestId)) {
            return mLastAudioRecording;
        }
        return null;
    }

    @Nullable
    public synchronized AudioRecording getLastAudio() {
        return this.mLastAudioRecording;
    }

    public boolean hasAudio(String requestId) {
        return TextUtils.equals(requestId, mLastRequestId);
    }

    public void put(String paramString, AudioRecording paramAudioRecording) {
        this.mLastRequestId = paramString;
        this.mLastAudioRecording = paramAudioRecording;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SingleRecordingAudioStore

 * JD-Core Version:    0.7.0.1

 */