package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

public final class Iterables
{
  private static void checkNonnegativeIndex(int paramInt)
  {
    if (paramInt < 0) {
      throw new IndexOutOfBoundsException("position cannot be negative: " + paramInt);
    }
  }
  
  public static boolean elementsEqual(Iterable<?> paramIterable1, Iterable<?> paramIterable2)
  {
    return Iterators.elementsEqual(paramIterable1.iterator(), paramIterable2.iterator());
  }
  
  public static <T> Iterable<T> filter(Iterable<T> paramIterable, final Predicate<? super T> paramPredicate)
  {
    Preconditions.checkNotNull(paramIterable);
    Preconditions.checkNotNull(paramPredicate);
    new IterableWithToString()
    {
      public Iterator<T> iterator()
      {
        return Iterators.filter(this.val$unfiltered.iterator(), paramPredicate);
      }
    };
  }
  
  public static <T> T get(Iterable<T> paramIterable, int paramInt)
  {
    Preconditions.checkNotNull(paramIterable);
    if ((paramIterable instanceof List)) {
      return ((List)paramIterable).get(paramInt);
    }
    if ((paramIterable instanceof Collection)) {
      Preconditions.checkElementIndex(paramInt, ((Collection)paramIterable).size());
    }
    for (;;)
    {
      return Iterators.get(paramIterable.iterator(), paramInt);
      checkNonnegativeIndex(paramInt);
    }
  }
  
  public static <T> T getOnlyElement(Iterable<T> paramIterable)
  {
    return Iterators.getOnlyElement(paramIterable.iterator());
  }
  
  public static boolean isEmpty(Iterable<?> paramIterable)
  {
    if ((paramIterable instanceof Collection)) {
      return ((Collection)paramIterable).isEmpty();
    }
    return !paramIterable.iterator().hasNext();
  }
  
  public static <T> Iterable<T> limit(Iterable<T> paramIterable, final int paramInt)
  {
    Preconditions.checkNotNull(paramIterable);
    if (paramInt >= 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool, "limit is negative");
      new IterableWithToString()
      {
        public Iterator<T> iterator()
        {
          return Iterators.limit(this.val$iterable.iterator(), paramInt);
        }
      };
    }
  }
  
  public static <T> boolean removeIf(Iterable<T> paramIterable, Predicate<? super T> paramPredicate)
  {
    if (((paramIterable instanceof RandomAccess)) && ((paramIterable instanceof List))) {
      return removeIfFromRandomAccessList((List)paramIterable, (Predicate)Preconditions.checkNotNull(paramPredicate));
    }
    return Iterators.removeIf(paramIterable.iterator(), paramPredicate);
  }
  
  private static <T> boolean removeIfFromRandomAccessList(List<T> paramList, Predicate<? super T> paramPredicate)
  {
    int i = 0;
    int j = 0;
    for (;;)
    {
      if (i < paramList.size())
      {
        Object localObject = paramList.get(i);
        if ((paramPredicate.apply(localObject)) || (i > j)) {}
        try
        {
          paramList.set(j, localObject);
          j++;
          i++;
        }
        catch (UnsupportedOperationException localUnsupportedOperationException)
        {
          slowRemoveIfForRemainingElements(paramList, paramPredicate, j, i);
        }
      }
    }
    do
    {
      return true;
      paramList.subList(j, paramList.size()).clear();
    } while (i != j);
    return false;
  }
  
  public static int size(Iterable<?> paramIterable)
  {
    if ((paramIterable instanceof Collection)) {
      return ((Collection)paramIterable).size();
    }
    return Iterators.size(paramIterable.iterator());
  }
  
  private static <T> void slowRemoveIfForRemainingElements(List<T> paramList, Predicate<? super T> paramPredicate, int paramInt1, int paramInt2)
  {
    for (int i = -1 + paramList.size(); i > paramInt2; i--) {
      if (paramPredicate.apply(paramList.get(i))) {
        paramList.remove(i);
      }
    }
    for (int j = paramInt2 - 1; j >= paramInt1; j--) {
      paramList.remove(j);
    }
  }
  
  static Object[] toArray(Iterable<?> paramIterable)
  {
    return toCollection(paramIterable).toArray();
  }
  
  public static <T> T[] toArray(Iterable<? extends T> paramIterable, Class<T> paramClass)
  {
    Collection localCollection = toCollection(paramIterable);
    return localCollection.toArray(ObjectArrays.newArray(paramClass, localCollection.size()));
  }
  
  private static <E> Collection<E> toCollection(Iterable<E> paramIterable)
  {
    if ((paramIterable instanceof Collection)) {
      return (Collection)paramIterable;
    }
    return Lists.newArrayList(paramIterable.iterator());
  }
  
  public static String toString(Iterable<?> paramIterable)
  {
    return Iterators.toString(paramIterable.iterator());
  }
  
  public static <F, T> Iterable<T> transform(Iterable<F> paramIterable, final Function<? super F, ? extends T> paramFunction)
  {
    Preconditions.checkNotNull(paramIterable);
    Preconditions.checkNotNull(paramFunction);
    new IterableWithToString()
    {
      public Iterator<T> iterator()
      {
        return Iterators.transform(this.val$fromIterable.iterator(), paramFunction);
      }
    };
  }
  
  static abstract class IterableWithToString<E>
    implements Iterable<E>
  {
    public String toString()
    {
      return Iterables.toString(this);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Iterables
 * JD-Core Version:    0.7.0.1
 */