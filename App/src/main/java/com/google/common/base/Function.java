package com.google.common.base;

import javax.annotation.Nullable;

public abstract interface Function<F, T>
{
  public abstract T apply(@Nullable F paramF);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.base.Function
 * JD-Core Version:    0.7.0.1
 */