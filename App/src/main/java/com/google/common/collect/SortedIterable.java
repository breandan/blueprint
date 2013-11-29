package com.google.common.collect;

import java.util.Comparator;

abstract interface SortedIterable<T>
  extends Iterable<T>
{
  public abstract Comparator<? super T> comparator();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.SortedIterable
 * JD-Core Version:    0.7.0.1
 */