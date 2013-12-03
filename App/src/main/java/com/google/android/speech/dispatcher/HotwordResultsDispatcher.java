package com.google.android.speech.dispatcher;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.RecognitionResponse;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.exception.RecognizeException;

public class HotwordResultsDispatcher
        implements RecognitionDispatcher.ResultsMerger {
    private boolean mInvalid = false;
    private final RecognitionDispatcher mRecognitionDispatcher;
    private final RecognitionEngineCallback mRecognitionEngineCallback;
    private final ExtraPreconditions.ThreadCheck mThreadCheck = ExtraPreconditions.createSameThreadCheck();

    public HotwordResultsDispatcher(RecognitionDispatcher paramRecognitionDispatcher, RecognitionEngineCallback paramRecognitionEngineCallback) {
        this.mRecognitionDispatcher = paramRecognitionDispatcher;
        this.mRecognitionEngineCallback = paramRecognitionEngineCallback;
    }

    public void invalidate() {
        this.mThreadCheck.check();
        this.mInvalid = true;
    }

    public void onError(RecognizeException paramRecognizeException) {
        this.mThreadCheck.check();
        if (this.mInvalid) {
        }
        while (paramRecognizeException.getEngine() != 1) {
            return;
        }
        this.mRecognitionDispatcher.cancel();
        this.mRecognitionEngineCallback.onError(paramRecognizeException);
    }

    public void onProgressUpdate(int paramInt, long paramLong) {
    }

    public void onResult(RecognitionResponse paramRecognitionResponse) {
        this.mThreadCheck.check();
        if (this.mInvalid) {
        }
        while (((paramRecognitionResponse.getEngine() != 1) || (paramRecognitionResponse.getType() != 1)) && (paramRecognitionResponse.getEngine() != 3)) {
            return;
        }
        this.mRecognitionEngineCallback.onResult(paramRecognitionResponse);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.dispatcher.HotwordResultsDispatcher

 * JD-Core Version:    0.7.0.1

 */