package com.google.common.util.concurrent;

public class UncheckedExecutionException
  extends RuntimeException
{
  private static final long serialVersionUID;
  
  protected UncheckedExecutionException() {}
  
  public UncheckedExecutionException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.UncheckedExecutionException
 * JD-Core Version:    0.7.0.1
 */