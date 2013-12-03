package com.google.android.speech.callback;

import com.google.android.speech.RecognitionResponse;
import com.google.android.speech.exception.RecognizeException;

public abstract interface RecognitionEngineCallback
        extends Callback<RecognitionResponse, RecognizeException> {
    public abstract void onProgressUpdate(int paramInt, long paramLong);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.callback.RecognitionEngineCallback

 * JD-Core Version:    0.7.0.1

 */