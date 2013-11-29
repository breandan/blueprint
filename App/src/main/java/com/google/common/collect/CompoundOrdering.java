package com.google.common.collect;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;

final class CompoundOrdering<T>
  extends Ordering<T>
  implements Serializable
{
  private static final long serialVersionUID;
  final ImmutableList<Comparator<? super T>> comparators;
  
  CompoundOrdering(Iterable<? extends Comparator<? super T>> paramIterable)
  {
    this.comparators = ImmutableList.copyOf(paramIterable);
  }
  
  CompoundOrdering(Comparator<? super T> paramComparator1, Comparator<? super T> paramComparator2)
  {
    this.comparators = ImmutableList.of(paramComparator1, paramComparator2);
  }
  
  public int compare(T paramT1, T paramT2)
  {
    Iterator localIterator = this.comparators.iterator();
    while (localIterator.hasNext())
    {
      int i = ((Comparator)localIterator.next()).compare(paramT1, paramT2);
      if (i != 0) {
        return i;
      }
    }
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if ((paramObject instanceof CompoundOrdering))
    {
      CompoundOrdering localCompoundOrdering = (CompoundOrdering)paramObject;
      return this.comparators.equals(localCompoundOrdering.comparators);
    }
    return false;
  }
  
  public int hashCode()
  {
    return this.comparators.hashCode();
  }
  
  public String toString()
  {
    return "Ordering.compound(" + this.comparators + ")";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.CompoundOrdering
 * JD-Core Version:    0.7.0.1
 */