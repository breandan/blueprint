package com.embryo.android.speech.dispatcher;

public class HotwordResultsDispatcher
        implements RecognitionDispatcher.ResultsMerger {
    private final RecognitionDispatcher mRecognitionDispatcher;
    private final com.embryo.android.speech.callback.RecognitionEngineCallback mRecognitionEngineCallback;
    private final com.embryo.android.shared.util.ExtraPreconditions.ThreadCheck mThreadCheck = com.embryo.android.shared.util.ExtraPreconditions.createSameThreadCheck();
    private boolean mInvalid = false;

    public HotwordResultsDispatcher(RecognitionDispatcher paramRecognitionDispatcher, com.embryo.android.speech.callback.RecognitionEngineCallback paramRecognitionEngineCallback) {
        this.mRecognitionDispatcher = paramRecognitionDispatcher;
        this.mRecognitionEngineCallback = paramRecognitionEngineCallback;
    }

    public void invalidate() {
        this.mThreadCheck.check();
        this.mInvalid = true;
    }

    public void onError(com.embryo.android.speech.exception.RecognizeException exception) {
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

    public void onResult(com.embryo.android.speech.RecognitionResponse response) {
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

 * Qualified Name:     HotwordResultsDispatcher

 * JD-Core Version:    0.7.0.1

 */