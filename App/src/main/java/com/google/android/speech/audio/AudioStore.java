package com.google.android.speech.audio;

import javax.annotation.Nullable;

public abstract interface AudioStore {
    @Nullable
    public abstract AudioRecording getLastAudio();

    public abstract boolean hasAudio(String paramString);

    public abstract void put(String paramString, AudioRecording paramAudioRecording);

    public static class AudioRecording {
        private final byte[] mAudio;
        private final int mSampleRate;

        public AudioRecording(int paramInt, byte[] paramArrayOfByte) {
            this.mSampleRate = paramInt;
            this.mAudio = paramArrayOfByte;
        }

        public byte[] getAudio() {
            return this.mAudio;
        }

        public int getSampleRate() {
            return this.mSampleRate;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.AudioStore

 * JD-Core Version:    0.7.0.1

 */