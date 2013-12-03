package com.google.android.speech.logger;

import com.google.android.voicesearch.logger.EventLogger;

public class SpeechLibLoggerImpl
        implements SpeechLibLogger {
    public static final SpeechLibLoggerImpl INSTANCE = new SpeechLibLoggerImpl();

    public void logAudioPathEstablished(SpeechLibLogger.LogData paramLogData) {
        EventLogger.recordClientEvent(76, paramLogData);
    }

    public void logBug(int paramInt) {
        EventLogger.recordClientEvent(29, Integer.valueOf(paramInt));
    }

    public void logEndOfSpeech() {
        EventLogger.recordClientEvent(6);
    }

    public void logNoSpeechDetected() {
        EventLogger.recordClientEvent(90);
    }

    public void logS3ConnectionDone() {
        EventLogger.recordClientEvent(10);
    }

    public void logS3ConnectionError() {
        EventLogger.recordClientEvent(9);
    }

    public void logS3ConnectionOpen() {
        EventLogger.recordClientEvent(7);
    }

    public void logS3ConnectionOpenLatency() {
        EventLogger.recordClientEvent(8);
    }

    public void logS3MajelResultReceived() {
        EventLogger.recordClientEvent(12);
    }

    public void logS3RecognitionCompleted() {
        EventLogger.recordClientEvent(11);
    }

    public void logS3SendEndOfData() {
        EventLogger.recordClientEvent(23);
    }

    public void logS3SoundSearchResultReceived() {
        EventLogger.recordClientEvent(35);
    }

    public void logS3TtsReceived() {
        EventLogger.recordClientEvent(30);
    }

    public void logUsingResultsFromEmbedded() {
        EventLogger.recordClientEvent(70);
    }

    public void logUsingResultsFromNetwork() {
        EventLogger.recordClientEvent(71);
    }

    public void recordBreakdownEvent(int paramInt) {
        EventLogger.recordBreakdownEvent(paramInt);
    }

    public void recordOpenMicrophoneLatencyStart() {
        EventLogger.recordLatencyStart(11);
    }

    public void recordSpeechEvent(int paramInt) {
        EventLogger.recordSpeechEvent(paramInt);
    }

    public void recordSpeechEvent(int paramInt, Object paramObject) {
        EventLogger.recordSpeechEvent(paramInt, paramObject);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.logger.SpeechLibLoggerImpl

 * JD-Core Version:    0.7.0.1

 */