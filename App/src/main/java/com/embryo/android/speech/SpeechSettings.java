package com.embryo.android.speech;

public abstract interface SpeechSettings {
    public abstract com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration getConfiguration();

//    public abstract List<String> getExperimentIds();

//    public abstract String getSpokenLocaleBcp47();

    public abstract boolean isDebugAudioLoggingEnabled();

    public abstract boolean isEmbeddedEndpointingEnabled();

//    public abstract boolean isEmbeddedRecognitionOnlyForDebug();
//
//    public abstract boolean isS3DebugLoggingEnabled();
//
//    public abstract boolean isSoundSearchEnabled();
//
//    public abstract void setSpokenLanguageBcp47(String paramString, boolean paramBoolean);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SpeechSettings

 * JD-Core Version:    0.7.0.1

 */