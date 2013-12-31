package com.embryo.android.voicesearch;

import com.embryo.android.speech.EngineSelector;
import com.embryo.android.speech.RecognitionEngineStore;
import com.embryo.android.speech.dispatcher.HotwordResultsDispatcher;
import com.google.android.speech.message.GsaS3ResponseProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class SpeechLibFactoryImpl
        implements com.embryo.android.speech.SpeechLibFactory {
    private final ExecutorService mLocalExecutorService;
    private final ExecutorService mNetworkExecutorService;
    private final com.embryo.android.speech.params.RecognitionEngineParams mRecognitionEngineParams;
    private final ScheduledExecutorService mScheduledExecutorService;
    private final com.embryo.android.speech.SpeechSettings mSpeechSettings;

    public SpeechLibFactoryImpl(com.embryo.android.speech.params.RecognitionEngineParams paramRecognitionEngineParams, com.embryo.android.speech.SpeechSettings paramSpeechSettings, ScheduledExecutorService paramScheduledExecutorService) {
        this.mRecognitionEngineParams = paramRecognitionEngineParams;
        this.mSpeechSettings = paramSpeechSettings;
        this.mScheduledExecutorService = paramScheduledExecutorService;
        this.mLocalExecutorService = com.embryo.android.shared.util.ConcurrentUtils.createSafeScheduledExecutorService(1, "LocalEngine");
        this.mNetworkExecutorService = com.embryo.android.shared.util.ConcurrentUtils.createSafeScheduledExecutorService(1, "NetworkEngine");
    }

    private boolean shouldStopMusicDetectorOnStartOfSpeech() {
        com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration localConfiguration = this.mSpeechSettings.getConfiguration();
        if ((!localConfiguration.hasSoundSearch()) || (!localConfiguration.getSoundSearch().hasStopMusicDetectionOnStartOfSpeech())) {
            return true;
        }
        return localConfiguration.getSoundSearch().getStopMusicDetectionOnStartOfSpeech();
    }

    public EngineSelector buildEngineSelector(com.embryo.android.speech.params.SessionParams paramSessionParams) {
        return new com.embryo.android.speech.EngineSelectorImpl(paramSessionParams, this.mSpeechSettings);
    }

    public RecognitionEngineStore buildRecognitionEngineStore() {
        return new com.embryo.android.speech.RecognitionEngineStoreImpl(this.mRecognitionEngineParams, buildSpeechLibLogger(), this.mLocalExecutorService, this.mNetworkExecutorService);
    }

    public com.embryo.android.speech.ResponseProcessor buildResponseProcessor(com.embryo.android.speech.ResponseProcessor.AudioCallback paramAudioCallback, com.embryo.android.speech.listeners.RecognitionEventListener paramRecognitionEventListener, com.embryo.android.speech.params.SessionParams paramSessionParams, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger) {
        return new com.embryo.android.speech.ResponseProcessor(paramAudioCallback, paramRecognitionEventListener, paramSessionParams.stopOnEndOfSpeech(), paramSessionParams.getEndpointerParams(this.mSpeechSettings), new GsaS3ResponseProcessor(), paramSpeechLibLogger);
    }

    public com.embryo.android.speech.dispatcher.RecognitionDispatcher.ResultsMerger buildResultsMerger(com.embryo.android.speech.params.SessionParams paramSessionParams, com.embryo.android.speech.dispatcher.RecognitionDispatcher paramRecognitionDispatcher, EngineSelector paramEngineSelector, com.embryo.android.speech.callback.RecognitionEngineCallback paramRecognitionEngineCallback, ExecutorService paramExecutorService) {
        if (paramSessionParams.getMode() == 6) {
            return new HotwordResultsDispatcher(paramRecognitionDispatcher, paramRecognitionEngineCallback);
        }
        return new com.embryo.android.voicesearch.greco3.ResultsMergerImpl(paramRecognitionDispatcher, paramEngineSelector, paramRecognitionEngineCallback, paramExecutorService, this.mScheduledExecutorService, paramSessionParams.getEmbeddedFallbackTimeout(this.mSpeechSettings), shouldStopMusicDetectorOnStartOfSpeech(), this.mSpeechSettings, buildSpeechLibLogger());
    }

    public com.embryo.android.speech.logger.SpeechLibLogger buildSpeechLibLogger() {
        return com.embryo.android.speech.logger.SpeechLibLoggerImpl.INSTANCE;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SpeechLibFactoryImpl

 * JD-Core Version:    0.7.0.1

 */