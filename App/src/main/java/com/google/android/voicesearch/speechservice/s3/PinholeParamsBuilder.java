package com.google.android.voicesearch.speechservice.s3;

import com.google.speech.s3.PinholeStream.PinholeParams;
import java.util.concurrent.Callable;

public abstract interface PinholeParamsBuilder
{
  public abstract Callable<PinholeStream.PinholeParams> getPinholeParamsCallable(String paramString);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.speechservice.s3.PinholeParamsBuilder
 * JD-Core Version:    0.7.0.1
 */