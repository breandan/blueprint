package com.google.android.voicesearch;

import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ConcurrentUtils;
import com.google.android.speech.EngineSelector;
import com.google.android.speech.EngineSelectorImpl;
import com.google.android.speech.RecognitionEngineStore;
import com.google.android.speech.RecognitionEngineStoreImpl;
import com.google.android.speech.ResponseProcessor;
import com.google.android.speech.SpeechLibFactory;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.dispatcher.HotwordResultsDispatcher;
import com.google.android.speech.dispatcher.RecognitionDispatcher;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.speech.logger.SpeechLibLoggerImpl;
import com.google.android.speech.message.GsaS3ResponseProcessor;
import com.google.android.speech.params.RecognitionEngineParams;
import com.google.android.speech.params.SessionParams;
import com.google.android.voicesearch.greco3.ResultsMergerImpl;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class SpeechLibFactoryImpl
        implements SpeechLibFactory {
    private final ExecutorService mLocalExecutorService;
    private final ExecutorService mNetworkExecutorService;
    private final RecognitionEngineParams mRecognitionEngineParams;
    private final ScheduledExecutorService mScheduledExecutorService;
    private final SpeechSettings mSpeechSettings;

    public SpeechLibFactoryImpl(RecognitionEngineParams paramRecognitionEngineParams, SpeechSettings paramSpeechSettings, ScheduledExecutorService paramScheduledExecutorService) {
        this.mRecognitionEngineParams = paramRecognitionEngineParams;
        this.mSpeechSettings = paramSpeechSettings;
        this.mScheduledExecutorService = paramScheduledExecutorService;
        this.mLocalExecutorService = ConcurrentUtils.createSafeScheduledExecutorService(1, "LocalEngine");
        this.mNetworkExecutorService = ConcurrentUtils.createSafeScheduledExecutorService(1, "NetworkEngine");
    }

    private boolean shouldStopMusicDetectorOnStartOfSpeech() {
        GstaticConfiguration.Configuration localConfiguration = this.mSpeechSettings.getConfiguration();
        if ((!localConfiguration.hasSoundSearch()) || (!localConfiguration.getSoundSearch().hasStopMusicDetectionOnStartOfSpeech())) {
            return true;
        }
        return localConfiguration.getSoundSearch().getStopMusicDetectionOnStartOfSpeech();
    }

    public EngineSelector buildEngineSelector(SessionParams paramSessionParams) {
        return new EngineSelectorImpl(paramSessionParams, this.mSpeechSettings);
    }

    public RecognitionEngineStore buildRecognitionEngineStore() {
        return new RecognitionEngineStoreImpl(this.mRecognitionEngineParams, buildSpeechLibLogger(), this.mLocalExecutorService, this.mNetworkExecutorService);
    }

    public ResponseProcessor buildResponseProcessor(ResponseProcessor.AudioCallback paramAudioCallback, RecognitionEventListener paramRecognitionEventListener, SessionParams paramSessionParams, SpeechLibLogger paramSpeechLibLogger) {
        return new ResponseProcessor(paramAudioCallback, paramRecognitionEventListener, paramSessionParams.stopOnEndOfSpeech(), paramSessionParams.getEndpointerParams(this.mSpeechSettings), new GsaS3ResponseProcessor(), paramSpeechLibLogger);
    }

    public RecognitionDispatcher.ResultsMerger buildResultsMerger(SessionParams paramSessionParams, RecognitionDispatcher paramRecognitionDispatcher, EngineSelector paramEngineSelector, RecognitionEngineCallback paramRecognitionEngineCallback, ExecutorService paramExecutorService) {
        if (paramSessionParams.getMode() == 6) {
            return new HotwordResultsDispatcher(paramRecognitionDispatcher, paramRecognitionEngineCallback);
        }
        return new ResultsMergerImpl(paramRecognitionDispatcher, paramEngineSelector, paramRecognitionEngineCallback, paramExecutorService, this.mScheduledExecutorService, paramSessionParams.getEmbeddedFallbackTimeout(this.mSpeechSettings), shouldStopMusicDetectorOnStartOfSpeech(), this.mSpeechSettings, buildSpeechLibLogger());
    }

    public SpeechLibLogger buildSpeechLibLogger() {
        return SpeechLibLoggerImpl.INSTANCE;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.SpeechLibFactoryImpl

 * JD-Core Version:    0.7.0.1

 */