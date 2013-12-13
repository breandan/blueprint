package com.google.android.speech;

import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.List;

public abstract interface SpeechSettings {
    public abstract GstaticConfiguration.Configuration getConfiguration();

    public abstract List<String> getExperimentIds();

    public abstract String getInstallId();

    public abstract int getServerEndpointingActivityTimeoutMs();

    public abstract String getSpokenLocaleBcp47();

    public abstract String getVoiceSearchTokenType();

    public abstract boolean isDebugAudioLoggingEnabled();

    public abstract boolean isEmbeddedEndpointingEnabled();

    public abstract boolean isEmbeddedRecognitionOnlyForDebug();

    public abstract boolean isNetworkRecognitionOnlyForDebug();

    public abstract boolean isPersonalizationEnabled();

    public abstract boolean isS3DebugLoggingEnabled();

    public abstract boolean isServerEndpointingEnabled();

    public abstract boolean isSoundSearchEnabled();

    public abstract void setSpokenLanguageBcp47(String paramString, boolean paramBoolean);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.SpeechSettings

 * JD-Core Version:    0.7.0.1

 */