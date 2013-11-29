package com.google.android.shared.util;

import java.util.concurrent.Executor;

public class PostToExecutorLater<C>
  extends WrappingNowOrLaterBase<C, C>
{
  private final Executor mExecutor;
  
  public PostToExecutorLater(Executor paramExecutor, NowOrLater<? extends C> paramNowOrLater)
  {
    super(paramNowOrLater);
    this.mExecutor = paramExecutor;
  }
  
  protected Consumer<C> createConsumer(Consumer<? super C> paramConsumer)
  {
    return new ConsumerWrapper(paramConsumer);
  }
  
  public C getNow()
  {
    return this.mWrapped.getNow();
  }
  
  private class ConsumerWrapper
    extends WrappingNowOrLaterBase.WrappingConsumerBase
  {
    public ConsumerWrapper()
    {
      super(localConsumer);
    }
    
    protected boolean doConsume(Consumer<? super C> paramConsumer, C paramC)
    {
      Consumers.consumeAsync(PostToExecutorLater.this.mExecutor, paramConsumer, paramC);
      return true;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.PostToExecutorLater
 * JD-Core Version:    0.7.0.1
 */