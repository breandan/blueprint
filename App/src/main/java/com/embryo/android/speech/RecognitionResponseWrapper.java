package com.embryo.android.speech;

import com.embryo.android.speech.exception.RecognizeException;
import com.embryo.speech.s3.S3;

public class RecognitionResponseWrapper
        implements com.embryo.android.speech.callback.Callback<S3.S3Response, RecognizeException> {
    final com.embryo.android.speech.callback.Callback<RecognitionResponse, RecognizeException> mCallback;
    final int mEngine;

    public RecognitionResponseWrapper(com.embryo.android.speech.callback.Callback<RecognitionResponse, RecognizeException> paramCallback, int paramInt) {
        this.mCallback = paramCallback;
        this.mEngine = paramInt;
    }

    public void onError(com.embryo.android.speech.exception.RecognizeException paramRecognizeException) {
        paramRecognizeException.setEngine(this.mEngine);
        this.mCallback.onError(paramRecognizeException);
    }

    public void onResult(S3.S3Response paramS3Response) {
        this.mCallback.onResult(RecognitionResponse.fromS3Response(this.mEngine, paramS3Response));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RecognitionResponseWrapper

 * JD-Core Version:    0.7.0.1

 */