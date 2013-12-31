package com.embryo.android.speech.params;

import com.embryo.android.speech.embedded.Greco3CallbackFactory;
import com.embryo.android.speech.embedded.Greco3ModeSelector;

public class RecognitionEngineParams {
    private final EmbeddedParams mEmbeddedParams;
    private final MusicDetectorParams mMusicDetectorParams;

    public RecognitionEngineParams(EmbeddedParams paramEmbeddedParams, MusicDetectorParams paramMusicDetectorParams) {
        this.mEmbeddedParams = paramEmbeddedParams;
        this.mMusicDetectorParams = paramMusicDetectorParams;
    }

    public EmbeddedParams getEmbeddedParams() {
        return this.mEmbeddedParams;
    }

    public MusicDetectorParams getMusicDetectorParams() {
        return this.mMusicDetectorParams;
    }

    public static class EmbeddedParams {
        private final int mBytesPerSample;
        private final Greco3CallbackFactory mCallbackFactory;
        private final com.embryo.android.speech.embedded.Greco3EngineManager mGreco3EngineManager;
        private final Greco3ModeSelector mModeSelector;
        private final int mSamplingRate;
        private final com.embryo.android.shared.util.SpeechLevelSource mSpeechLevelSource;
        private final com.embryo.android.speech.SpeechSettings mSpeechSettings;

        public EmbeddedParams(Greco3CallbackFactory paramGreco3CallbackFactory, com.embryo.android.speech.embedded.Greco3EngineManager paramGreco3EngineManager, Greco3ModeSelector paramGreco3ModeSelector, com.embryo.android.shared.util.SpeechLevelSource paramSpeechLevelSource, com.embryo.android.speech.SpeechSettings paramSpeechSettings, int paramInt1, int paramInt2) {
            this.mCallbackFactory = paramGreco3CallbackFactory;
            this.mGreco3EngineManager = paramGreco3EngineManager;
            this.mModeSelector = paramGreco3ModeSelector;
            this.mSpeechLevelSource = paramSpeechLevelSource;
            this.mSpeechSettings = paramSpeechSettings;
            this.mBytesPerSample = paramInt1;
            this.mSamplingRate = paramInt2;
        }

        public int getBytesPerSample() {
            return this.mBytesPerSample;
        }

        public Greco3CallbackFactory getCallbackFactory() {
            return this.mCallbackFactory;
        }

        public com.embryo.android.speech.embedded.Greco3EngineManager getGreco3EngineManager() {
            return this.mGreco3EngineManager;
        }

        public Greco3ModeSelector getModeSelector() {
            return this.mModeSelector;
        }

        public int getSamplingRate() {
            return this.mSamplingRate;
        }

        public com.embryo.android.speech.SpeechSettings getSpeechSettings() {
            return this.mSpeechSettings;
        }
    }

    public static class MusicDetectorParams {
        private final com.embryo.android.speech.SpeechSettings mSettings;

        public MusicDetectorParams(com.embryo.android.speech.SpeechSettings paramSpeechSettings) {
            this.mSettings = paramSpeechSettings;
        }

        public com.embryo.android.speech.SpeechSettings getSettings() {
            return this.mSettings;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEngineParams

 * JD-Core Version:    0.7.0.1

 */