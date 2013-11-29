package com.google.common.util.concurrent;

public abstract interface FutureCallback<V>
{
  public abstract void onFailure(Throwable paramThrowable);
  
  public abstract void onSuccess(V paramV);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.FutureCallback
 * JD-Core Version:    0.7.0.1
 */