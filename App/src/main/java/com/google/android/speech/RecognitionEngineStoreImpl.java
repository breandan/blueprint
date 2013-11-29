package com.google.android.speech;

import android.util.Pair;
import com.google.android.search.core.ears.MusicDetectorRecognitionEngine;
import com.google.android.shared.util.ThreadChanger;
import com.google.android.speech.embedded.Greco3RecognitionEngine;
import com.google.android.speech.embedded.GrecoEventLoggerFactory;
import com.google.android.speech.engine.NetworkRecognitionEngine;
import com.google.android.speech.engine.RecognitionEngine;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.speech.network.producers.S3RequestProducerFactory;
import com.google.android.speech.network.producers.SoundSearchRequestProducerFactory;
import com.google.android.speech.network.producers.VoiceSearchRequestProducerFactory;
import com.google.android.speech.params.RecognitionEngineParams;
import com.google.android.speech.params.RecognitionEngineParams.EmbeddedParams;
import com.google.android.speech.params.RecognitionEngineParams.MusicDetectorParams;
import com.google.android.speech.params.RecognitionEngineParams.NetworkParams;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RecognitionEngineStoreImpl
  implements RecognitionEngineStore
{
  private RecognitionEngine mEmbeddedRecognitionEngine;
  private final RecognitionEngineParams mEngineParams;
  private final ExecutorService mLocalExecutorService;
  private RecognitionEngine mMusicDetectorRecognitionEngine;
  private final ExecutorService mMusicExecutorService;
  private final ExecutorService mNetworkExecutorService;
  private RecognitionEngine mNetworkRecognitionEngine;
  private final SpeechLibLogger mSpeechLibLogger;
  
  public RecognitionEngineStoreImpl(RecognitionEngineParams paramRecognitionEngineParams, SpeechLibLogger paramSpeechLibLogger, ExecutorService paramExecutorService1, ExecutorService paramExecutorService2, ExecutorService paramExecutorService3)
  {
    this.mEngineParams = paramRecognitionEngineParams;
    this.mSpeechLibLogger = paramSpeechLibLogger;
    this.mLocalExecutorService = paramExecutorService1;
    this.mNetworkExecutorService = paramExecutorService2;
    this.mMusicExecutorService = paramExecutorService3;
  }
  
  private RecognitionEngine getEmbeddedEngine()
  {
    if (this.mEmbeddedRecognitionEngine == null)
    {
      RecognitionEngineParams.EmbeddedParams localEmbeddedParams = this.mEngineParams.getEmbeddedParams();
      this.mEmbeddedRecognitionEngine = ((RecognitionEngine)ThreadChanger.createNonBlockingThreadChangeProxy(this.mLocalExecutorService, RecognitionEngine.class, log(new Greco3RecognitionEngine(localEmbeddedParams.getGreco3EngineManager(), localEmbeddedParams.getSamplingRate(), localEmbeddedParams.getBytesPerSample(), localEmbeddedParams.getSpeechSettings(), localEmbeddedParams.getCallbackFactory(), localEmbeddedParams.getModeSelector(), new GrecoEventLoggerFactory(), this.mSpeechLibLogger))));
    }
    return this.mEmbeddedRecognitionEngine;
  }
  
  private RecognitionEngine getMusicDetectorEngine()
  {
    if (this.mMusicDetectorRecognitionEngine == null)
    {
      RecognitionEngineParams.MusicDetectorParams localMusicDetectorParams = this.mEngineParams.getMusicDetectorParams();
      this.mMusicDetectorRecognitionEngine = ((RecognitionEngine)ThreadChanger.createNonBlockingThreadChangeProxy(this.mMusicExecutorService, RecognitionEngine.class, log(new MusicDetectorRecognitionEngine(localMusicDetectorParams.getSettings()))));
    }
    return this.mMusicDetectorRecognitionEngine;
  }
  
  private RecognitionEngine getNetworkEngine()
  {
    if (this.mNetworkRecognitionEngine == null)
    {
      RecognitionEngineParams.NetworkParams localNetworkParams = this.mEngineParams.getNetworkParams();
      S3RequestProducerFactory localS3RequestProducerFactory = new S3RequestProducerFactory(new SoundSearchRequestProducerFactory(this.mNetworkExecutorService, localNetworkParams.getNetworkRequestProducerParams()), new VoiceSearchRequestProducerFactory(this.mNetworkExecutorService, localNetworkParams.getNetworkRequestProducerParams(), this.mSpeechLibLogger));
      this.mNetworkRecognitionEngine = ((RecognitionEngine)ThreadChanger.createNonBlockingThreadChangeProxy(this.mNetworkExecutorService, RecognitionEngine.class, log(new NetworkRecognitionEngine(localNetworkParams.getPrimaryConnectionFactory(), localNetworkParams.getFallbackConnectionFactory(), localNetworkParams.getRetryPolicy(), this.mNetworkExecutorService, localS3RequestProducerFactory, this.mSpeechLibLogger))));
    }
    return this.mNetworkRecognitionEngine;
  }
  
  public static final <T> T log(T paramT)
  {
    return paramT;
  }
  
  public List<Pair<Integer, RecognitionEngine>> getEngines(List<Integer> paramList)
  {
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      switch (i)
      {
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