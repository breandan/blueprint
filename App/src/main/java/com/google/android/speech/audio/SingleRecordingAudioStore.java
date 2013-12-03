package com.google.android.speech.audio;

import android.text.TextUtils;

import javax.annotation.Nullable;

public class SingleRecordingAudioStore
        implements AudioStore {
    private AudioStore.AudioRecording mLastAudioRecording;
    private String mLastRequestId;

    @Nullable
    public AudioStore.AudioRecording getAudio(String requestId) {
        try {
            if (TextUtils.equals(requestId, mLastRequestId)) {

            }
        } finally {
        }

    }

    @Nullable
    public AudioStore.AudioRecording getLastAudio() {
        try {
            AudioStore.AudioRecording localAudioRecording = this.mLastAudioRecording;
            return localAudioRecording;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public boolean hasAudio(String requestId) {
        return TextUtils.equals(requestId, mLastRequestId);
    }

    public void put(String paramString, AudioStore.AudioRecording paramAudioRecording) {
        try {
            this.mLastRequestId = paramString;
            this.mLastAudioRecording = paramAudioRecording;
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.SingleRecordingAudioStore

 * JD-Core Version:    0.7.0.1

 */