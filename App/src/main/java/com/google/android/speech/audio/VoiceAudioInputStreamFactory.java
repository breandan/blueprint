package com.google.android.speech.audio;

import android.content.Context;

import com.google.android.speech.SpeechSettings;
import com.google.android.speech.debug.DebugAudioLogger;

import java.io.IOException;
import java.io.InputStream;

public final class VoiceAudioInputStreamFactory
        implements AudioInputStreamFactory {
    private final Context mContext;
    private final AudioInputStreamFactory mDelegate;
    private final SpeechSettings mSettings;

    public VoiceAudioInputStreamFactory(AudioInputStreamFactory paramAudioInputStreamFactory, SpeechSettings paramSpeechSettings, Context paramContext) {
        this.mDelegate = paramAudioInputStreamFactory;
        this.mSettings = paramSpeechSettings;
        this.mContext = paramContext;
    }

    public InputStream createInputStream()
            throws IOException {
        return DebugAudioLogger.maybeWrapInLogStream(this.mDelegate.createInputStream(), this.mContext, this.mSettings);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.audio.VoiceAudioInputStreamFactory

 * JD-Core Version:    0.7.0.1

 */