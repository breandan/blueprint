package com.google.android.shared.util;

public abstract interface CancellableNowOrLater<C>
  extends NowOrLater<C>
{
  public abstract void cancelGetLater(Consumer<? super C> paramConsumer);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.CancellableNowOrLater
 * JD-Core Version:    0.7.0.1
 */