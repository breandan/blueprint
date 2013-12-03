package com.google.android.speech.embedded;

import com.google.android.voicesearch.logger.EventLogger;

public class GrecoEventLoggerFactory
        implements GrecoEventLogger.Factory {
    private static final GrecoEventLogger DEFAULT_EVENT_LOGGER = new RecognizerEventLogger(51, 52);
    private static final GrecoEventLogger HOTWORD_EVENT_LOGGER = new RecognizerEventLogger(88, 89);

    public GrecoEventLogger getEventLoggerForMode(Greco3Mode paramGreco3Mode) {
        if (paramGreco3Mode.isEndpointerMode()) {
            return null;
        }
        if (paramGreco3Mode == Greco3Mode.HOTWORD) {
            return HOTWORD_EVENT_LOGGER;
        }
        return DEFAULT_EVENT_LOGGER;
    }

    private static class RecognizerEventLogger
            implements GrecoEventLogger {
        final int mCompleted;
        final int mStart;

        RecognizerEventLogger(int paramInt1, int paramInt2) {
            this.mStart = paramInt1;
            this.mCompleted = paramInt2;
        }

        public void recognitionCompleted(RecognizerOuterClass.RecognizerLog paramRecognizerLog) {
            EventLogger.recordClientEvent(this.mCompleted, paramRecognizerLog);
        }

        public void recognitionStarted() {
            EventLogger.recordClientEvent(this.mStart);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.GrecoEventLoggerFactory

 * JD-Core Version:    0.7.0.1

 */