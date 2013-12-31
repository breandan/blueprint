package com.embryo.android.speech.embedded;

import com.embryo.android.speech.exception.RecognizeException;
import com.embryo.speech.recognizer.RecognizerCallback;

public abstract interface Greco3Callback
        extends RecognizerCallback {
    public abstract void handleError(RecognizeException paramRecognizeException);

    public abstract void handleProgressUpdate(long paramLong);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3Callback

 * JD-Core Version:    0.7.0.1

 */