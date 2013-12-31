package com.embryo.android.speech.listeners;

import com.embryo.android.speech.exception.RecognizeException;
import com.google.audio.ears.proto.EarsService;
import com.google.majel.proto.MajelProtos;
import com.embryo.speech.recognizer.api.RecognizerProtos;
import com.google.speech.s3.PinholeStream;

public abstract interface RecognitionEventListener {
    public abstract void onBeginningOfSpeech(long paramLong);

    public abstract void onDone();

    public abstract void onEndOfSpeech();

    public abstract void onError(RecognizeException paramRecognizeException);

    public abstract void onMajelResult(MajelProtos.MajelResponse paramMajelResponse);

    public abstract void onMediaDataResult(byte[] paramArrayOfByte);

    public abstract void onMusicDetected();

    public abstract void onNoSpeechDetected();

    public abstract void onPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput);

    public abstract void onReadyForSpeech();

    public abstract void onRecognitionCancelled();

    public abstract void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent);

    public abstract void onSoundSearchResult(EarsService.EarsResultsResponse paramEarsResultsResponse);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEventListener

 * JD-Core Version:    0.7.0.1

 */