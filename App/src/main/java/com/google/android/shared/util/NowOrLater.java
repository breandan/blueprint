package com.google.android.shared.util;

public abstract interface NowOrLater<C>
{
  public abstract void getLater(Consumer<? super C> paramConsumer);
  
  public abstract C getNow();
  
  public abstract boolean haveNow();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.NowOrLater
 * JD-Core Version:    0.7.0.1
 */