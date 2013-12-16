package com.google.android.speech.audio;

import android.text.TextUtils;

import javax.annotation.Nullable;

public class SingleRecordingAudioStore
        implements AudioStore {
    private AudioStore.AudioRecording mLastAudioRecording;
    private String mLastRequestId;

    @Nullable
    public synchronized AudioStore.AudioRecording getAudio(String requestId) {
        if (TextUtils.equals(requestId, mLastRequestId)) {
            return mLastAudioRecording;
        }
        return null;
    }

    @Nullable
    public synchronized AudioStore.AudioRecording getLastAudio() {
        return this.mLastAudioRecording;
    }

    public boolean hasAudio(String requestId) {
        return TextUtils.equals(requestId, mLastRequestId);
    }

    public void put(String paramString, AudioStore.AudioRecording paramAudioRecording) {
        this.mLastRequestId = paramString;
        this.mLastAudioRecording = paramAudioRecording;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.SingleRecordingAudioStore

 * JD-Core Version:    0.7.0.1

 */