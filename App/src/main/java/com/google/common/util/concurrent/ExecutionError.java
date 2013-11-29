package com.google.common.util.concurrent;

public class ExecutionError
  extends Error
{
  private static final long serialVersionUID;
  
  protected ExecutionError() {}
  
  public ExecutionError(Error paramError)
  {
    super(paramError);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ExecutionError
 * JD-Core Version:    0.7.0.1
 */