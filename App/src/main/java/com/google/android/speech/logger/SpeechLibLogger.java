package com.google.android.speech.logger;

public abstract interface SpeechLibLogger
{
  public abstract void logAudioPathEstablished(LogData paramLogData);
  
  public abstract void logBug(int paramInt);
  
  public abstract void logEndOfSpeech();
  
  public abstract void logNoSpeechDetected();
  
  public abstract void logS3ConnectionDone();
  
  public abstract void logS3ConnectionError();
  
  public abstract void logS3ConnectionOpen();
  
  public abstract void logS3ConnectionOpenLatency();
  
  public abstract void logS3MajelResultReceived();
  
  public abstract void logS3RecognitionCompleted();
  
  public abstract void logS3SendEndOfData();
  
  public abstract void logS3SoundSearchResultReceived();
  
  public abstract void logS3TtsReceived();
  
  public abstract void logUsingResultsFromEmbedded();
  
  public abstract void logUsingResultsFromNetwork();
  
  public abstract void recordBreakdownEvent(int paramInt);
  
  public abstract void recordOpenMicrophoneLatencyStart();
  
  public abstract void recordSpeechEvent(int paramInt);
  
  public abstract void recordSpeechEvent(int paramInt, Object paramObject);
  
  public static class LogData
  {
    public final int audioPath;
    public final int networkType;
    
    public LogData(int paramInt1, int paramInt2)
    {
      this.audioPath = paramInt1;
      this.networkType = paramInt2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.logger.SpeechLibLogger
 * JD-Core Version:    0.7.0.1
 */