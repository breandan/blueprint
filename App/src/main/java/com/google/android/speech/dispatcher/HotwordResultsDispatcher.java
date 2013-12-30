package com.google.android.speech.dispatcher;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.RecognitionResponse;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.exception.RecognizeException;

public class HotwordResultsDispatcher
        implements RecognitionDispatcher.ResultsMerger {
    private final RecognitionDispatcher mRecognitionDispatcher;
    private final RecognitionEngineCallback mRecognitionEngineCallback;
    private final ExtraPreconditions.ThreadCheck mThreadCheck = ExtraPreconditions.createSameThreadCheck();
    private boolean mInvalid = false;

    public HotwordResultsDispatcher(RecognitionDispatcher paramRecognitionDispatcher, RecognitionEngineCallback paramRecognitionEngineCallback) {
        this.mRecognitionDispatcher = paramRecognitionDispatcher;
        this.mRecognitionEngineCallback = paramRecognitionEngineCallback;
    }

    public void invalidate() {
        this.mThreadCheck.check();
        this.mInvalid = true;
    }

    public void onError(RecognizeException exception) {
        mThreadCheck.check();
        if (mInvalid) {
            return;
        }
        if (exception.getEngine() == 0x1) {
            mRecognitionDispatcher.cancel();
            mRecognitionEngineCallback.onError(exception);
        }
    }

    public void onProgressUpdate(int paramInt, long paramLong) {

    }

    public void onResult(RecognitionResponse response) {
        mThreadCheck.check();
        if (mInvalid) {
            return;
        }
        if ((response.getEngine() == 0x1) && (response.getType() == 0x1)
                || response.getEngine() != 0x3) {
            mRecognitionEngineCallback.onResult(response);
            return;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.dispatcher.HotwordResultsDispatcher

 * JD-Core Version:    0.7.0.1

 */