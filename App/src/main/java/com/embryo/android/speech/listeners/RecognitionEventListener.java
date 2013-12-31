package com.embryo.android.speech.listeners;

import com.embryo.android.speech.exception.RecognizeException;
import com.embryo.speech.recognizer.api.RecognizerProtos;

public abstract interface RecognitionEventListener {
    public abstract void onBeginningOfSpeech(long paramLong);

    public abstract void onDone();

    public abstract void onEndOfSpeech();

    public abstract void onError(RecognizeException paramRecognizeException);

    public abstract void onMediaDataResult(byte[] paramArrayOfByte);

    public abstract void onNoSpeechDetected();

    public abstract void onReadyForSpeech();

    public abstract void onRecognitionCancelled();

    public abstract void onRecognitionResult(RecognizerProtos.RecognitionEvent paramRecognitionEvent);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEventListener

 * JD-Core Version:    0.7.0.1

 */