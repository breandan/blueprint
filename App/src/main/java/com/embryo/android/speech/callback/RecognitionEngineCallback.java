package com.embryo.android.speech.callback;

import com.embryo.android.speech.RecognitionResponse;
import com.embryo.android.speech.exception.RecognizeException;

public abstract interface RecognitionEngineCallback
        extends Callback<RecognitionResponse, RecognizeException> {
    public abstract void onProgressUpdate(int paramInt, long paramLong);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionEngineCallback

 * JD-Core Version:    0.7.0.1

 */