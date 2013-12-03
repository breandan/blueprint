package com.google.android.speech.engine;

import com.google.android.speech.RecognitionResponse;
import com.google.android.speech.callback.Callback;
import com.google.android.speech.exception.RecognizeException;
import com.google.speech.s3.S3.S3Response;

public class RetryCallback
        implements Callback<S3.S3Response, RecognizeException> {
    private final Callback<RecognitionResponse, RecognizeException> mCallback;
    private boolean mInvalid;
    private final Retrier mResender;
    private final RetryPolicy mRetryPolicy;

    RetryCallback(Callback<RecognitionResponse, RecognizeException> paramCallback, RetryPolicy paramRetryPolicy, Retrier paramRetrier) {
        this.mCallback = paramCallback;
        this.mRetryPolicy = paramRetryPolicy;
        this.mResender = paramRetrier;
        this.mInvalid = false;
    }

    void invalidate() {
        this.mInvalid = true;
    }

    public void onError(RecognizeException paramRecognizeException) {
        for (; ; ) {
            try {
                boolean bool = this.mInvalid;
                if (bool) {
                    return;
                }
                paramRecognizeException.setEngine(2);
                if (this.mRetryPolicy.canRetry(paramRecognizeException)) {
                    invalidate();
                    this.mResender.scheduleRetry(paramRecognizeException);
                } else {
                    this.mCallback.onError(paramRecognizeException);
                }
            } finally {
            }
        }
    }

    public void onResult(S3.S3Response paramS3Response) {
        for (; ; ) {
            try {
                boolean bool = this.mInvalid;
                if (bool) {
                    return;
                }
                RecognizeException localRecognizeException = this.mRetryPolicy.equivalentToError(paramS3Response);
                if (localRecognizeException != null) {
                    onError(localRecognizeException);
                } else {
                    this.mCallback.onResult(RecognitionResponse.fromS3Response(2, paramS3Response));
                }
            } finally {
            }
        }
    }

    public static abstract interface Retrier {
        public abstract void scheduleRetry(RecognizeException paramRecognizeException);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.engine.RetryCallback

 * JD-Core Version:    0.7.0.1

 */