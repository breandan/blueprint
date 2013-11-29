package com.google.android.speech;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.endpointing.DefaultEndpointerEventProcessor;
import com.google.android.speech.endpointing.EndpointerEventProcessor;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.speech.message.S3ResponseProcessor;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.speech.recognizer.api.RecognizerProtos.EndpointerEvent;
import com.google.speech.recognizer.api.RecognizerProtos.RecognitionEvent;
import com.google.speech.s3.PinholeStream.PinholeOutput;
import com.google.speech.s3.S3.S3Response;
import com.google.speech.speech.s3.Recognizer.RecognizerEvent;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.EndpointerParams;

public class ResponseProcessor
  implements RecognitionEngineCallback
{
  private final AudioCallback mAudioCallback;
  private final EndpointerEventProcessor mEndpointerEventProcessor;
  private final RecognitionEventListener mEventListener;
  private boolean mInvalid = false;
  private final SpeechLibLogger mLogger;
  private final S3ResponseProcessor mS3ResponseProcessor;
  private final ExtraPreconditions.ThreadCheck mThreadCheck = ExtraPreconditions.createSameThreadCheck();
  
  public ResponseProcessor(AudioCallback paramAudioCallback, RecognitionEventListener paramRecognitionEventListener, boolean paramBoolean, GstaticConfiguration.EndpointerParams paramEndpointerParams, S3ResponseProcessor paramS3ResponseProcessor, SpeechLibLogger paramSpeechLibLogger)
  {
    this.mAudioCallback = paramAudioCallback;
    this.mEventListener = new InternalRecognitionEventListener(paramRecognitionEventListener, paramSpeechLibLogger, paramAudioCallback, paramBoolean);
    this.mEndpointerEventProcessor = new DefaultEndpointerEventProcessor(this.mEventListener, paramEndpointerParams);
    this.mS3ResponseProcessor = paramS3ResponseProcessor;
    this.mLogger = paramSpeechLibLogger;
  }
  
  private void handleMusicDetectorResponse(Boolean paramBoolean)
  {
    if (paramBoolean == Boolean.TRUE) {
      this.mEventListener.onMusicDetected();
    }
  }
  
  private void handleS3Response(S3.S3Response paramS3Response)
  {
    if (paramS3Response.getStatus() == 1)
    {
      this.mLogger.logS3ConnectionDone();
      this.mAudioCallback.shutdownAudio();
    }
    for (;;)
    {
      this.mS3ResponseProcessor.process(paramS3Response, this.mEventListener);
      return;
      if (paramS3Response.getStatus() == 2)
      {
        this.mLogger.logS3ConnectionError();
        this.mAudioCallback.shutdownAudio();
      }
      else if ((paramS3Response.getStatus() == 0) && (paramS3Response.hasRecognizerEventExtension()) && (paramS3Response.getRecognizerEventExtension().hasRecognitionEvent()) && (paramS3Response.getRecognizerEventExtension().getRecognitionEvent().getEventType() == 1))
      {
        this.mLogger.logS3RecognitionCompleted();
        this.mAudioCallback.stopAudio();
      }
    }
  }
  
  public void invalidate()
  {
    this.mThreadCheck.check();
    this.mInvalid = true;
  }
  
  public void onError(RecognizeException paramRecognizeException)
  {
    this.mThreadCheck.check();
    if (this.mInvalid) {
      return;
    }
    this.mAudioCallback.shutdownAudio();
    this.mEventListener.onError(paramRecognizeException);
  }
  
  public void onProgressUpdate(int paramInt, long paramLong)
  {
    this.mThreadCheck.check();
    if (this.mInvalid) {
      return;
    }
    this.mEndpointerEventProcessor.updateProgress(paramInt, paramLong);
  }
  
  public void onRecognitionCancelled()
  {
    this.mThreadCheck.check();
    if (this.mInvalid) {
      return;
    }
    this.mEventListener.onRecognitionCancelled();
  }
  
  public void onResult(RecognitionResponse paramRecognitionResponse)
  {
    this.mThreadCheck.check();
    if (this.mInvalid) {
      return;
    }
    int i = paramRecognitionResponse.getType();
    if (i == 3)
    {
      handleMusicDetectorResponse((Boolean)paramRecognitionResponse.get(3));
      return;
    }
    if (i == 2)
    {
      RecognizerProtos.EndpointerEvent localEndpointerEvent = (RecognizerProtos.EndpointerEvent)paramRecognitionResponse.get(2);
      this.mEndpointerEventProcessor.process(localEndpointerEvent);
      return;
    }
    if (i == 1)
    {
      handleS3Response((S3.S3Response)paramRecognitionResponse.get(1));
      return;
    }
    throw new IllegalArgumentException("Unknown response type: ");
  }
  
  public static abstract interface AudioCallback
  {
    public abstract void recordingStarted(long paramLong);
    
    public abstract void shutdownAudio();
    
    public abstract void stopAudio();
  }
  
  static final class InternalRecognitionEventListener
    implements RecognitionEventListener
  {
    private final ResponseProcessor.AudioCallback mAudioCallback;
    private final RecognitionEventListener mDelegate;
    private final SpeechLibLogger mLogger;
    private final boolean mStopOnEndOfSpeech;
    
    InternalRecognitionEventListener(RecognitionEventListener paramRecognitionEventListener, SpeechLibLogger paramSpeechLibLogger, ResponseProcessor.AudioCallback paramAudioCallback, boolean paramBoolean)
    {
      this.mDelegate = paramRecognitionEventListener;
      this.mLogger = paramSpeechLibLogger;
      this.mAudioCallback = paramAudioCallback;
      this.mStopOnEndOfSpeech = paramBoolean;
    }
    
    public void onBeginningOfSpeech(long paramLong)
    {
      this.mAudioCallback.recordingStarted(paramLong);
      this.mDelegate.onBeginningOfSpeech(paramLong);
    }
    
    public void onDone()
    {
      this.mDelegate.onDone();
    }
    
    public void onEndOfSpeech()
    {
      if (this.mStopOnEndOfSpeech) {
        this.mAudioCallback.stopAudio();
      }
      this.mLogger.logEndOfSpeech();
      this.mDelegate.onEndOfSpeech();
    }
    
    public void onError(RecognizeException paramRecognizeException)
    {
      this.mDelegate.onError(paramRecognizeException);
    }
    
    public void onMajelResult(MajelProtos.MajelResponse paramMajelResponse)
    {
      this.mLogger.logS3MajelResultReceived();
      this.mDelegate.onMajelResult(paramMajelResponse);
    }
    
    public void onMediaDataResult(byte[] paramArrayOfByte)
    {
      this.mLogger.logS3TtsReceived();
      this.mDelegate.onMediaDataResult(paramArrayOfByte);
    }
    
    public void onMusicDetected()
    {
      this.mDelegate.onMusicDetected();
    }
    
    public void onNoSpeechDetected()
    {
      this.mLogger.logNoSpeechDetected();
      this.mDelegate.onNoSpeechDetected();
    }
    
    public void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput)
    {
      this.mDelegate.onPinholeResult(paramPinholeOutput);
    }
    
    public void onReadyForSpeech()
    {
      this.mDelegate.onReadyForSpeech();
    }
    
    public void onRecognitionCancelled()
    {
      this.mDelegate.onRecognitionCancelled();
    }
    
    public void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent)
    {
      this.mDelegate.onRecognitionResult(paramRecognitionEvent);
    }
    
    public void onSoundSearchResult(EarsService.EarsResultsResponse paramEarsResultsResponse)
    {
      this.mLogger.logS3SoundSearchResultReceived();
      this.mDelegate.onSoundSearchResult(paramEarsResultsResponse);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.ResponseProcessor
 * JD-Core Version:    0.7.0.1
 */