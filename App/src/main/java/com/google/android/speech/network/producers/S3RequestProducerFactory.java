package com.google.android.speech.network.producers;

import com.google.android.speech.params.SessionParams;
import java.io.InputStream;
import javax.annotation.Nonnull;

public class S3RequestProducerFactory
  implements RequestProducerFactory
{
  private int mMode = -1;
  private final SoundSearchRequestProducerFactory mSoundSearchRequestProducerFactory;
  private final VoiceSearchRequestProducerFactory mVoiceSearchRequestProducerFactory;
  
  public S3RequestProducerFactory(SoundSearchRequestProducerFactory paramSoundSearchRequestProducerFactory, VoiceSearchRequestProducerFactory paramVoiceSearchRequestProducerFactory)
  {
    this.mSoundSearchRequestProducerFactory = paramSoundSearchRequestProducerFactory;
    this.mVoiceSearchRequestProducerFactory = paramVoiceSearchRequestProducerFactory;
  }
  
  public void init(@Nonnull SessionParams paramSessionParams)
  {
    this.mMode = paramSessionParams.getMode();
    this.mSoundSearchRequestProducerFactory.init(paramSessionParams);
    this.mVoiceSearchRequestProducerFactory.init(paramSessionParams);
  }
  
  public S3RequestProducer newRequestProducer(InputStream paramInputStream)
  {
    if (SessionParams.isSoundSearch(this.mMode)) {
      return this.mSoundSearchRequestProducerFactory.newRequestProducer(paramInputStream);
    }
    return this.mVoiceSearchRequestProducerFactory.newRequestProducer(paramInputStream);
  }
  
  public void refresh()
  {
    if (this.mMode == -1) {
      return;
    }
    if (SessionParams.isSoundSearch(this.mMode))
    {
      this.mSoundSearchRequestProducerFactory.refresh();
      return;
    }
    this.mVoiceSearchRequestProducerFactory.refresh();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.producers.S3RequestProducerFactory
 * JD-Core Version:    0.7.0.1
 */