package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

final class SortedIterables
{
  public static boolean hasSameComparator(Comparator<?> paramComparator, Iterable<?> paramIterable)
  {
    Preconditions.checkNotNull(paramComparator);
    Preconditions.checkNotNull(paramIterable);
    Object localObject;
    if ((paramIterable instanceof SortedSet))
    {
      localObject = ((SortedSet)paramIterable).comparator();
      if (localObject == null) {
        localObject = Ordering.natural();
      }
    }
    for (;;)
    {
      return paramComparator.equals(localObject);
      if ((paramIterable instanceof SortedIterable)) {
        localObject = ((SortedIterable)paramIterable).comparator();
      } else {
        localObject = null;
      }
    }
  }
  
  public static <E> Collection<E> sortedUnique(Comparator<? super E> paramComparator, Iterator<E> paramIterator)
  {
    TreeSet localTreeSet = Sets.newTreeSet(paramComparator);
    Iterators.addAll(localTreeSet, paramIterator);
    return localTreeSet;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.SortedIterables
 * JD-Core Version:    0.7.0.1
 */