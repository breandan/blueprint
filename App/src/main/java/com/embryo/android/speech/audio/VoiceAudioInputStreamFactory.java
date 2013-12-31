package com.embryo.android.speech.audio;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public final class VoiceAudioInputStreamFactory
        implements AudioInputStreamFactory {
    private final Context mContext;
    private final AudioInputStreamFactory mDelegate;
    private final com.embryo.android.speech.SpeechSettings mSettings;

    public VoiceAudioInputStreamFactory(AudioInputStreamFactory paramAudioInputStreamFactory, com.embryo.android.speech.SpeechSettings paramSpeechSettings, Context paramContext) {
        this.mDelegate = paramAudioInputStreamFactory;
        this.mSettings = paramSpeechSettings;
        this.mContext = paramContext;
    }

    public InputStream createInputStream()
            throws IOException {
        return com.embryo.android.speech.debug.DebugAudioLogger.maybeWrapInLogStream(this.mDelegate.createInputStream(), this.mContext, this.mSettings);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoiceAudioInputStreamFactory

 * JD-Core Version:    0.7.0.1

 */