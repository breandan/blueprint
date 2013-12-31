package com.embryo.android.speech.engine;

import com.google.speech.s3.S3;

import javax.annotation.Nullable;

public abstract interface RetryPolicy {
    public abstract boolean canRetry(com.embryo.android.speech.exception.RecognizeException paramRecognizeException);

    @Nullable
    public abstract com.embryo.android.speech.exception.RecognizeException equivalentToError(S3.S3Response paramS3Response);

    public abstract boolean isAuthException(com.embryo.android.speech.exception.RecognizeException paramRecognizeException);

    public abstract void reset();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     RetryPolicy

 * JD-Core Version:    0.7.0.1

 */