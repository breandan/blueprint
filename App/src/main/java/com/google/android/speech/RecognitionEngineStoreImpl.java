package com.google.android.speech;

import android.util.Pair;

import com.google.android.search.core.ears.MusicDetectorRecognitionEngine;
import com.google.android.shared.util.ThreadChanger;
import com.google.android.speech.embedded.Greco3RecognitionEngine;
import com.google.android.speech.embedded.GrecoEventLoggerFactory;
import com.google.android.speech.engine.RecognitionEngine;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.speech.params.RecognitionEngineParams;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RecognitionEngineStoreImpl
        implements RecognitionEngineStore {
    private final RecognitionEngineParams mEngineParams;
    private final ExecutorService mLocalExecutorService;
    private final ExecutorService mMusicExecutorService;
    private final ExecutorService mNetworkExecutorService;
    private final SpeechLibLogger mSpeechLibLogger;
    private RecognitionEngine mEmbeddedRecognitionEngine;
    private RecognitionEngine mMusicDetectorRecognitionEngine;

    public RecognitionEngineStoreImpl(RecognitionEngineParams paramRecognitionEngineParams, SpeechLibLogger paramSpeechLibLogger, ExecutorService paramExecutorService1, ExecutorService paramExecutorService2, ExecutorService paramExecutorService3) {
        this.mEngineParams = paramRecognitionEngineParams;
        this.mSpeechLibLogger = paramSpeechLibLogger;
        this.mLocalExecutorService = paramExecutorService1;
        this.mNetworkExecutorService = paramExecutorService2;
        this.mMusicExecutorService = paramExecutorService3;
    }

    public static final <T> T log(T paramT) {
        return paramT;
    }

    private RecognitionEngine getEmbeddedEngine() {
        if (this.mEmbeddedRecognitionEngine == null) {
            RecognitionEngineParams.EmbeddedParams localEmbeddedParams = this.mEngineParams.getEmbeddedParams();
            this.mEmbeddedRecognitionEngine = ThreadChanger.createNonBlockingThreadChangeProxy(this.mLocalExecutorService, RecognitionEngine.class, log(new Greco3RecognitionEngine(localEmbeddedParams.getGreco3EngineManager(), localEmbeddedParams.getSamplingRate(), localEmbeddedParams.getBytesPerSample(), localEmbeddedParams.getSpeechSettings(), localEmbeddedParams.getCallbackFactory(), localEmbeddedParams.getModeSelector(), new GrecoEventLoggerFactory(), this.mSpeechLibLogger)));
        }
        return this.mEmbeddedRecognitionEngine;
    }

    private RecognitionEngine getMusicDetectorEngine() {
        if (this.mMusicDetectorRecognitionEngine == null) {
            RecognitionEngineParams.MusicDetectorParams localMusicDetectorParams = this.mEngineParams.getMusicDetectorParams();
            this.mMusicDetectorRecognitionEngine = ThreadChanger.createNonBlockingThreadChangeProxy(this.mMusicExecutorService, RecognitionEngine.class, log(new MusicDetectorRecognitionEngine(localMusicDetectorParams.getSettings())));
        }
        return this.mMusicDetectorRecognitionEngine;
    }

    private RecognitionEngine getNetworkEngine() {
        return getEmbeddedEngine();
    }

    public List<Pair<Integer, RecognitionEngine>> getEngines(List<Integer> paramList) {
        ArrayList localArrayList = Lists.newArrayListWithExpectedSize(paramList.size());
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            int i = ((Integer) localIterator.next()).intValue();
            switch (i) {
                default:
                    break;
                case 1:
                    localArrayList.add(Pair.create(Integer.valueOf(i), getEmbeddedEngine()));
                    break;
                case 2:
                    localArrayList.add(Pair.create(Integer.valueOf(i), getNetworkEngine()));
                    break;
                case 3:
                    localArrayList.add(Pair.create(Integer.valueOf(i), getMusicDetectorEngine()));
            }
        }
        return localArrayList;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.RecognitionEngineStoreImpl

 * JD-Core Version:    0.7.0.1

 */