package com.google.android.speech.listeners;

import com.google.android.speech.exception.RecognizeException;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import com.google.speech.s3.PinholeStream.PinholeOutput;

public class CancellableRecognitionEventListener
  implements RecognitionEventListener
{
  private boolean mActive = true;
  private final RecognitionEventListener mListener;
  
  public CancellableRecognitionEventListener(RecognitionEventListener paramRecognitionEventListener)
  {
    this.mListener = paramRecognitionEventListener;
  }
  
  public void invalidate()
  {
    this.mActive = false;
  }
  
  public void onBeginningOfSpeech(long paramLong)
  {
    if (this.mActive) {
      this.mListener.onBeginningOfSpeech(paramLong);
    }
  }
  
  public void onDone()
  {
    if (this.mActive) {
      this.mListener.onDone();
    }
  }
  
  public void onEndOfSpeech()
  {
    if (this.mActive) {
      this.mListener.onEndOfSpeech();
    }
  }
  
  public void onError(RecognizeException paramRecognizeException)
  {
    if (this.mActive) {
      this.mListener.onError(paramRecognizeException);
    }
  }
  
  public void onMajelResult(MajelProtos.MajelResponse paramMajelResponse)
  {
    if (this.mActive) {
      this.mListener.onMajelResult(paramMajelResponse);
    }
  }
  
  public void onMediaDataResult(byte[] paramArrayOfByte)
  {
    if (this.mActive) {
      this.mListener.onMediaDataResult(paramArrayOfByte);
    }
  }
  
  public void onMusicDetected()
  {
    if (this.mActive) {
      this.mListener.onMusicDetected();
    }
  }
  
  public void onNoSpeechDetected()
  {
    if (this.mActive) {
      this.mListener.onNoSpeechDetected();
    }
  }
  
  public void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput)
  {
    if (this.mActive) {
      this.mListener.onPinholeResult(paramPinholeOutput);
    }
  }
  
  public void onReadyForSpeech()
  {
    if (this.mActive) {
      this.mListener.onReadyForSpeech();
    }
  }
  
  public void onRecognitionCancelled()
  {
    if (this.mActive) {
      this.mListener.onRecognitionCancelled();
    }
  }
  
  public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
  {
    if (this.mActive) {
      this.mListener.onRecognitionResult(paramRecognitionEvent);
    }
  }
  
  public void onSoundSearchResult(EarsService.EarsResultsResponse paramEarsResultsResponse)
  {
    if (this.mActive) {
      this.mListener.onSoundSearchResult(paramEarsResultsResponse);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.listeners.CancellableRecognitionEventListener
 * JD-Core Version:    0.7.0.1
 */