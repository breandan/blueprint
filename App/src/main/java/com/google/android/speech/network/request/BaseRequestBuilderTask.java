package com.google.android.speech.network.request;

import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public abstract class BaseRequestBuilderTask<T>
  implements Callable<T>
{
  private final String mTag;
  
  protected BaseRequestBuilderTask(String paramString)
  {
    this.mTag = paramString;
  }
  
  @Nullable
  protected abstract T build();
  
  @Nullable
  public T call()
  {
    return build();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.request.BaseRequestBuilderTask
 * JD-Core Version:    0.7.0.1
 */