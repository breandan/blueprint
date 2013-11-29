package com.google.android.speech.message;

import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.speech.s3.S3.S3Response;

public abstract interface S3ResponseProcessor
{
  public abstract void process(S3.S3Response paramS3Response, RecognitionEventListener paramRecognitionEventListener);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.message.S3ResponseProcessor
 * JD-Core Version:    0.7.0.1
 */