package com.google.android.speech;

import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.dispatcher.RecognitionDispatcher;
import com.google.android.speech.dispatcher.RecognitionDispatcher.ResultsMerger;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.speech.params.SessionParams;
import java.util.concurrent.ExecutorService;

public abstract interface SpeechLibFactory
{
  public abstract EngineSelector buildEngineSelector(SessionParams paramSessionParams);
  
  public abstract RecognitionEngineStore buildRecognitionEngineStore();
  
  public abstract ResponseProcessor buildResponseProcessor(ResponseProcessor.AudioCallback paramAudioCallback, RecognitionEventListener paramRecognitionEventListener, SessionParams paramSessionParams, SpeechLibLogger paramSpeechLibLogger);
  
  public abstract RecognitionDispatcher.ResultsMerger buildResultsMerger(SessionParams paramSessionParams, RecognitionDispatcher paramRecognitionDispatcher, EngineSelector paramEngineSelector, RecognitionEngineCallback paramRecognitionEngineCallback, ExecutorService paramExecutorService);
  
  public abstract SpeechLibLogger buildSpeechLibLogger();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.SpeechLibFactory
 * JD-Core Version:    0.7.0.1
 */