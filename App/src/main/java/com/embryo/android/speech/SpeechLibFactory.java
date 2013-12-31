package com.embryo.android.speech;

import com.embryo.android.speech.dispatcher.RecognitionDispatcher;

import java.util.concurrent.ExecutorService;

public abstract interface SpeechLibFactory {
    public abstract com.embryo.android.speech.EngineSelector buildEngineSelector(com.embryo.android.speech.params.SessionParams paramSessionParams);

    public abstract RecognitionEngineStore buildRecognitionEngineStore();

    public abstract com.embryo.android.speech.ResponseProcessor buildResponseProcessor(com.embryo.android.speech.ResponseProcessor.AudioCallback paramAudioCallback, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, com.embryo.android.speech.params.SessionParams paramSessionParams, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger);

    public abstract RecognitionDispatcher.ResultsMerger buildResultsMerger(com.embryo.android.speech.params.SessionParams paramSessionParams, RecognitionDispatcher paramRecognitionDispatcher, com.embryo.android.speech.EngineSelector paramEngineSelector, com.embryo.android.speech.callback.RecognitionEngineCallback paramRecognitionEngineCallback, ExecutorService paramExecutorService);

    public abstract com.embryo.android.speech.logger.SpeechLibLogger buildSpeechLibLogger();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SpeechLibFactory

 * JD-Core Version:    0.7.0.1

 */