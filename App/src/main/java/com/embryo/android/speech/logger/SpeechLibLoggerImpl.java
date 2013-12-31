package com.embryo.android.speech.logger;

public class SpeechLibLoggerImpl
        implements SpeechLibLogger {
    public static final SpeechLibLoggerImpl INSTANCE = new SpeechLibLoggerImpl();

    public void logAudioPathEstablished(SpeechLibLogger.LogData paramLogData) {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(76, paramLogData);
    }

    public void logBug(int paramInt) {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(29, Integer.valueOf(paramInt));
    }

    public void logEndOfSpeech() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(6);
    }

    public void logNoSpeechDetected() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(90);
    }

    public void logS3ConnectionDone() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(10);
    }

    public void logS3ConnectionError() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(9);
    }

    public void logS3ConnectionOpen() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(7);
    }

    public void logS3ConnectionOpenLatency() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(8);
    }

    public void logS3MajelResultReceived() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(12);
    }

    public void logS3RecognitionCompleted() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(11);
    }

    public void logS3SendEndOfData() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(23);
    }

    public void logS3SoundSearchResultReceived() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(35);
    }

    public void logS3TtsReceived() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(30);
    }

    public void logUsingResultsFromEmbedded() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(70);
    }

    public void logUsingResultsFromNetwork() {
        com.embryo.android.voicesearch.logger.EventLogger.recordClientEvent(71);
    }

    public void recordBreakdownEvent(int paramInt) {
        com.embryo.android.voicesearch.logger.EventLogger.recordBreakdownEvent(paramInt);
    }

    public void recordOpenMicrophoneLatencyStart() {
        com.embryo.android.voicesearch.logger.EventLogger.recordLatencyStart(11);
    }

    public void recordSpeechEvent(int paramInt) {
        com.embryo.android.voicesearch.logger.EventLogger.recordSpeechEvent(paramInt);
    }

    public void recordSpeechEvent(int paramInt, Object paramObject) {
        com.embryo.android.voicesearch.logger.EventLogger.recordSpeechEvent(paramInt, paramObject);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SpeechLibLoggerImpl

 * JD-Core Version:    0.7.0.1

 */