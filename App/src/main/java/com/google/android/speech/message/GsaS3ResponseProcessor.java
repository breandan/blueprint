package com.google.android.speech.message;

import android.util.Log;
import com.google.android.speech.exception.ServerRecognizeException;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.protobuf.micro.ByteStringMicro;
import com.google.speech.s3.PinholeStream.PinholeOutput;
import com.google.speech.s3.S3.S3Response;
import com.google.speech.speech.s3.Majel.MajelServiceEvent;
import com.google.speech.speech.s3.Recognizer.RecognizerEvent;
import com.google.speech.speech.s3.SoundSearch.SoundSearchServiceEvent;
import com.google.speech.speech.s3.Synthesizer.TtsServiceEvent;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class GsaS3ResponseProcessor
  implements S3ResponseProcessor
{
  private final ByteArrayOutputStream mAudioBytes = new ByteArrayOutputStream();
  
  private void processDone(RecognitionEventListener paramRecognitionEventListener, S3.S3Response paramS3Response)
  {
    Iterator localIterator = paramS3Response.getDebugLineList().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Log.d("GsaS3ResponseProcessor", "DBG: " + str);
    }
    paramRecognitionEventListener.onDone();
  }
  
  @Nullable
  public static MajelProtos.MajelResponse processMajelServiceEvent(Majel.MajelServiceEvent paramMajelServiceEvent)
  {
    new MajelProtos.MajelResponse();
    if (paramMajelServiceEvent.hasCompressedMajelResponse())
    {
      Log.e("GsaS3ResponseProcessor", "Received compressed majel response");
      return null;
    }
    return paramMajelServiceEvent.getMajel();
  }
  
  private void processMajelServiceEvent(RecognitionEventListener paramRecognitionEventListener, Majel.MajelServiceEvent paramMajelServiceEvent)
  {
    MajelProtos.MajelResponse localMajelResponse = processMajelServiceEvent(paramMajelServiceEvent);
    if (localMajelResponse != null) {
      paramRecognitionEventListener.onMajelResult(localMajelResponse);
    }
  }
  
  private void processPinholeOutputEvent(RecognitionEventListener paramRecognitionEventListener, PinholeStream.PinholeOutput paramPinholeOutput)
  {
    paramRecognitionEventListener.onPinholeResult(paramPinholeOutput);
  }
  
  private void processRecognizerEvent(RecognitionEventListener paramRecognitionEventListener, Recognizer.RecognizerEvent paramRecognizerEvent)
  {
    paramRecognitionEventListener.onRecognitionResult(paramRecognizerEvent.getRecognitionEvent());
  }
  
  private void processSoundSearchEvent(RecognitionEventListener paramRecognitionEventListener, SoundSearch.SoundSearchServiceEvent paramSoundSearchServiceEvent)
  {
    if (paramSoundSearchServiceEvent.getResultsResponse() != null) {
      paramRecognitionEventListener.onSoundSearchResult(paramSoundSearchServiceEvent.getResultsResponse());
    }
  }
  
  private void processTtsServiceEvent(RecognitionEventListener paramRecognitionEventListener, Synthesizer.TtsServiceEvent paramTtsServiceEvent)
  {
    if ((paramTtsServiceEvent.getEndOfData()) && (this.mAudioBytes.size() > 0))
    {
      paramRecognitionEventListener.onMediaDataResult(this.mAudioBytes.toByteArray());
      return;
    }
    byte[] arrayOfByte = paramTtsServiceEvent.getAudio().toByteArray();
    this.mAudioBytes.write(arrayOfByte, 0, arrayOfByte.length);
  }
  
  public void process(S3.S3Response paramS3Response, RecognitionEventListener paramRecognitionEventListener)
  {
    switch (paramS3Response.getStatus())
    {
    default: 
    case 0: 
      do
      {
        return;
        if (paramS3Response.hasTtsServiceEventExtension()) {
          processTtsServiceEvent(paramRecognitionEventListener, paramS3Response.getTtsServiceEventExtension());
        }
        if (paramS3Response.hasRecognizerEventExtension()) {
          processRecognizerEvent(paramRecognitionEventListener, paramS3Response.getRecognizerEventExtension());
        }
        if (paramS3Response.hasMajelServiceEventExtension()) {
          processMajelServiceEvent(paramRecognitionEventListener, paramS3Response.getMajelServiceEventExtension());
        }
        if (paramS3Response.hasSoundSearchServiceEventExtension()) {
          processSoundSearchEvent(paramRecognitionEventListener, paramS3Response.getSoundSearchServiceEventExtension());
        }
      } while (!paramS3Response.hasPinholeOutputExtension());
      processPinholeOutputEvent(paramRecognitionEventListener, paramS3Response.getPinholeOutputExtension());
      return;
    case 1: 
      processDone(paramRecognitionEventListener, paramS3Response);
      return;
    case 2: 
      throw new IllegalStateException("Error S3Response received via onResult");
    }
    Log.w("GsaS3ResponseProcessor", "NOT_STARTED received");
    paramRecognitionEventListener.onError(new ServerRecognizeException(0));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.message.GsaS3ResponseProcessor
 * JD-Core Version:    0.7.0.1
 */