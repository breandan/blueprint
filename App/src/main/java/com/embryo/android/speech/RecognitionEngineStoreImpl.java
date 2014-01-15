package com.embryo.android.speech;

import android.util.Pair;

import com.embryo.android.shared.util.ThreadChanger;
import com.google.speech.embedded.Greco3RecognitionEngine;
import com.embryo.android.speech.embedded.GrecoEventLoggerFactory;
import com.embryo.android.speech.engine.RecognitionEngine;
import com.embryo.android.speech.params.RecognitionEngineParams;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RecognitionEngineStoreImpl
        implements RecognitionEngineStore {
    private final RecognitionEngineParams mEngineParams;
    private final ExecutorService mLocalExecutorService;
    private final com.embryo.android.speech.logger.SpeechLibLogger mSpeechLibLogger;
    private RecognitionEngine mEmbeddedRecognitionEngine;

    public RecognitionEngineStoreImpl(RecognitionEngineParams paramRecognitionEngineParams, com.embryo.android.speech.logger.SpeechLibLogger paramSpeechLibLogger, ExecutorService paramExecutorService1) {
        this.mEngineParams = paramRecognitionEngineParams;
        this.mSpeechLibLogger = paramSpeechLibLogger;
        this.mLocalExecutorService = paramExecutorService1;
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
                    localArrayList.add(Pair.create(Integer.valueOf(i), getEmbeddedEngine()));
                    break;
            }
        }
        return localArrayList;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEngineStoreImpl

 * JD-Core Version:    0.7.0.1

 */