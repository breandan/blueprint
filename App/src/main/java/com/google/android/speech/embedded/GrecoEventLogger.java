package com.google.android.speech.embedded;

import com.google.speech.logs.RecognizerOuterClass;

import javax.annotation.Nullable;

public abstract interface GrecoEventLogger {
    public abstract void recognitionCompleted(@Nullable RecognizerOuterClass.RecognizerLog paramRecognizerLog);

    public abstract void recognitionStarted();

    public static abstract interface Factory {
        public abstract GrecoEventLogger getEventLoggerForMode(Greco3Mode paramGreco3Mode);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.GrecoEventLogger

 * JD-Core Version:    0.7.0.1

 */