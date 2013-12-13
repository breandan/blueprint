package com.google.android.speech.engine;

import com.google.android.speech.exception.RecognizeException;
import com.google.speech.s3.S3;

import javax.annotation.Nullable;

public abstract interface RetryPolicy {
    public abstract boolean canRetry(RecognizeException paramRecognizeException);

    @Nullable
    public abstract RecognizeException equivalentToError(S3.S3Response paramS3Response);

    public abstract boolean isAuthException(RecognizeException paramRecognizeException);

    public abstract void reset();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.engine.RetryPolicy

 * JD-Core Version:    0.7.0.1

 */