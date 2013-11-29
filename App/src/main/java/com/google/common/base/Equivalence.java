package com.google.common.base;

import javax.annotation.Nullable;

public abstract class Equivalence<T>
{
  protected abstract boolean doEquivalent(T paramT1, T paramT2);
  
  protected abstract int doHash(T paramT);
  
  public final boolean equivalent(@Nullable T paramT1, @Nullable T paramT2)
  {
    if (paramT1 == paramT2) {
      return true;
    }
    if ((paramT1 == null) || (paramT2 == null)) {
      return false;
    }
    return doEquivalent(paramT1, paramT2);
  }
  
  public final int hash(@Nullable T paramT)
  {
    if (paramT == null) {
      return 0;
    }
    return doHash(paramT);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.base.Equivalence
 * JD-Core Version:    0.7.0.1
 */