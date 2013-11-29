package com.google.android.speech.listeners;

import com.google.android.speech.exception.RecognizeException;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.common.base.Preconditions;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import com.google.speech.s3.PinholeStream.PinholeOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeRecognitionEventListener
  implements RecognitionEventListener
{
  private final List<RecognitionEventListener> mListeners = new ArrayList();
  
  public void add(RecognitionEventListener paramRecognitionEventListener)
  {
    Preconditions.checkNotNull(paramRecognitionEventListener);
    if (!this.mListeners.contains(paramRecognitionEventListener)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mListeners.add(paramRecognitionEventListener);
      return;
    }
  }
  
  public void onBeginningOfSpeech(long paramLong)
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onBeginningOfSpeech(paramLong);
    }
  }
  
  public void onDone()
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onDone();
    }
  }
  
  public void onEndOfSpeech()
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onEndOfSpeech();
    }
  }
  
  public void onError(RecognizeException paramRecognizeException)
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onError(paramRecognizeException);
    }
  }
  
  public void onMajelResult(MajelProtos.MajelResponse paramMajelResponse)
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onMajelResult(paramMajelResponse);
    }
  }
  
  public void onMediaDataResult(byte[] paramArrayOfByte)
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onMediaDataResult(paramArrayOfByte);
    }
  }
  
  public void onMusicDetected()
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onMusicDetected();
    }
  }
  
  public void onNoSpeechDetected()
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onNoSpeechDetected();
    }
  }
  
  public void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput)
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onPinholeResult(paramPinholeOutput);
    }
  }
  
  public void onReadyForSpeech()
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onReadyForSpeech();
    }
  }
  
  public void onRecognitionCancelled()
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onRecognitionCancelled();
    }
  }
  
  public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onRecognitionResult(paramRecognitionEvent);
    }
  }
  
  public void onSoundSearchResult(EarsService.EarsResultsResponse paramEarsResultsResponse)
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((RecognitionEventListener)localIterator.next()).onSoundSearchResult(paramEarsResultsResponse);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.listeners.CompositeRecognitionEventListener
 * JD-Core Version:    0.7.0.1
 */