package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

public final class Iterators
{
  static final UnmodifiableIterator<Object> EMPTY_ITERATOR = new UnmodifiableIterator()
  {
    public boolean hasNext()
    {
      return false;
    }
    
    public Object next()
    {
      throw new NoSuchElementException();
    }
  };
  private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator()
  {
    public boolean hasNext()
    {
      return false;
    }
    
    public Object next()
    {
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      throw new IllegalStateException();
    }
  };
  
  public static <T> boolean addAll(Collection<T> paramCollection, Iterator<? extends T> paramIterator)
  {
    Preconditions.checkNotNull(paramCollection);
    boolean bool = false;
    while (paramIterator.hasNext()) {
      bool |= paramCollection.add(paramIterator.next());
    }
    return bool;
  }
  
  public static <T> boolean any(Iterator<T> paramIterator, Predicate<? super T> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    while (paramIterator.hasNext()) {
      if (paramPredicate.apply(paramIterator.next())) {
        return true;
      }
    }
    return false;
  }
  
  private static void checkNonnegative(int paramInt)
  {
    if (paramInt < 0) {
      throw new IndexOutOfBoundsException("position (" + paramInt + ") must not be negative");
    }
  }
  
  static void clear(Iterator<?> paramIterator)
  {
    Preconditions.checkNotNull(paramIterator);
    while (paramIterator.hasNext())
    {
      paramIterator.next();
      paramIterator.remove();
    }
  }
  
  public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> paramIterator)
  {
    Preconditions.checkNotNull(paramIterator);
    new Iterator()
    {
      Iterator<? extends T> current = Iterators.emptyIterator();
      Iterator<? extends T> removeFrom;
      
      public boolean hasNext()
      {
        boolean bool;
        for (;;)
        {
          bool = ((Iterator)Preconditions.checkNotNull(this.current)).hasNext();
          if ((bool) || (!this.val$inputs.hasNext())) {
            break;
          }
          this.current = ((Iterator)this.val$inputs.next());
        }
        return bool;
      }
      
      public T next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        this.removeFrom = this.current;
        return this.current.next();
      }
      
      public void remove()
      {
        if (this.removeFrom != null) {}
        for (boolean bool = true;; bool = false)
        {
          Preconditions.checkState(bool, "no calls to next() since last call to remove()");
          this.removeFrom.remove();
          this.removeFrom = null;
          return;
        }
      }
    };
  }
  
  public static <T> Iterator<T> concat(Iterator<? extends T> paramIterator1, Iterator<? extends T> paramIterator2)
  {
    Preconditions.checkNotNull(paramIterator1);
    Preconditions.checkNotNull(paramIterator2);
    return concat(Arrays.asList(new Iterator[] { paramIterator1, paramIterator2 }).iterator());
  }
  
  public static boolean contains(Iterator<?> paramIterator, @Nullable Object paramObject)
  {
    if (paramObject == null)
    {
      do
      {
        if (!paramIterator.hasNext()) {
          break;
        }
      } while (paramIterator.next() != null);
      return true;
    }
    while (paramIterator.hasNext()) {
      if (paramObject.equals(paramIterator.next())) {
        return true;
      }
    }
    return false;
  }
  
  public static <T> Iterator<T> cycle(Iterable<T> paramIterable)
  {
    Preconditions.checkNotNull(paramIterable);
    new Iterator()
    {
      Iterator<T> iterator = Iterators.emptyIterator();
      Iterator<T> removeFrom;
      
      public boolean hasNext()
      {
        if (!this.iterator.hasNext()) {
          this.iterator = this.val$iterable.iterator();
        }
        return this.iterator.hasNext();
      }
      
      public T next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        this.removeFrom = this.iterator;
        return this.iterator.next();
      }
      
      public void remove()
      {
        if (this.removeFrom != null) {}
        for (boolean bool = true;; bool = false)
        {
          Preconditions.checkState(bool, "no calls to next() since last call to remove()");
          this.removeFrom.remove();
          this.removeFrom = null;
          return;
        }
      }
    };
  }
  
  public static boolean elementsEqual(Iterator<?> paramIterator1, Iterator<?> paramIterator2)
  {
    if (paramIterator1.hasNext()) {
      if (paramIterator2.hasNext()) {}
    }
    while (paramIterator2.hasNext())
    {
      return false;
      if (Objects.equal(paramIterator1.next(), paramIterator2.next())) {
        break;
      }
      return false;
    }
    return true;
  }
  
  public static <T> UnmodifiableIterator<T> emptyIterator()
  {
    return EMPTY_ITERATOR;
  }
  
  static <T> Iterator<T> emptyModifiableIterator()
  {
    return EMPTY_MODIFIABLE_ITERATOR;
  }
  
  public static <T> UnmodifiableIterator<T> filter(Iterator<T> paramIterator, final Predicate<? super T> paramPredicate)
  {
    Preconditions.checkNotNull(paramIterator);
    Preconditions.checkNotNull(paramPredicate);
    new AbstractIterator()
    {
      protected T computeNext()
      {
        while (this.val$unfiltered.hasNext())
        {
          Object localObject = this.val$unfiltered.next();
          if (paramPredicate.apply(localObject)) {
            return localObject;
          }
        }
        return endOfData();
      }
    };
  }
  
  public static <T> UnmodifiableIterator<T> forArray(final T... paramVarArgs)
  {
    Preconditions.checkNotNull(paramVarArgs);
    new AbstractIndexedListIterator(paramVarArgs.length)
    {
      protected T get(int paramAnonymousInt)
      {
        return paramVarArgs[paramAnonymousInt];
      }
    };
  }
  
  static <T> UnmodifiableIterator<T> forArray(final T[] paramArrayOfT, final int paramInt1, int paramInt2)
  {
    if (paramInt2 >= 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      Preconditions.checkPositionIndexes(paramInt1, paramInt1 + paramInt2, paramArrayOfT.length);
      new AbstractIndexedListIterator(paramInt2)
      {
        protected T get(int paramAnonymousInt)
        {
          return paramArrayOfT[(paramAnonymousInt + paramInt1)];
        }
      };
    }
  }
  
  public static <T> T get(Iterator<T> paramIterator, int paramInt)
  {
    checkNonnegative(paramInt);
    int j;
    for (int i = 0; paramIterator.hasNext(); i = j)
    {
      Object localObject = paramIterator.next();
      j = i + 1;
      if (i == paramInt) {
        return localObject;
      }
    }
    throw new IndexOutOfBoundsException("position (" + paramInt + ") must be less than the number of elements that remained (" + i + ")");
  }
  
  public static <T> T getOnlyElement(Iterator<T> paramIterator)
  {
    Object localObject = paramIterator.next();
    if (!paramIterator.hasNext()) {
      return localObject;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("expected one element but was: <" + localObject);
    for (int i = 0; (i < 4) && (paramIterator.hasNext()); i++) {
      localStringBuilder.append(", " + paramIterator.next());
    }
    if (paramIterator.hasNext()) {
      localStringBuilder.append(", ...");
    }
    localStringBuilder.append('>');
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static <T> Iterator<T> limit(final Iterator<T> paramIterator, int paramInt)
  {
    Preconditions.checkNotNull(paramIterator);
    if (paramInt >= 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool, "limit is negative");
      new Iterator()
      {
        private int count;
        
        public boolean hasNext()
        {
          return (this.count < this.val$limitSize) && (paramIterator.hasNext());
        }
        
        public T next()
        {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          this.count = (1 + this.count);
          return paramIterator.next();
        }
        
        public void remove()
        {
          paramIterator.remove();
        }
      };
    }
  }
  
  public static boolean removeAll(Iterator<?> paramIterator, Collection<?> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    boolean bool = false;
    while (paramIterator.hasNext()) {
      if (paramCollection.contains(paramIterator.next()))
      {
        paramIterator.remove();
        bool = true;
      }
    }
    return bool;
  }
  
  public static <T> boolean removeIf(Iterator<T> paramIterator, Predicate<? super T> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    boolean bool = false;
    while (paramIterator.hasNext()) {
      if (paramPredicate.apply(paramIterator.next()))
      {
        paramIterator.remove();
        bool = true;
      }
    }
    return bool;
  }
  
  public static boolean retainAll(Iterator<?> paramIterator, Collection<?> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    boolean bool = false;
    while (paramIterator.hasNext()) {
      if (!paramCollection.contains(paramIterator.next()))
      {
        paramIterator.remove();
        bool = true;
      }
    }
    return bool;
  }
  
  public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable T paramT)
  {
    new UnmodifiableIterator()
    {
      boolean done;
      
      public boolean hasNext()
      {
        return !this.done;
      }
      
      public T next()
      {
        if (this.done) {
          throw new NoSuchElementException();
        }
        this.done = true;
        return this.val$value;
      }
    };
  }
  
  public static int size(Iterator<?> paramIterator)
  {
    for (int i = 0; paramIterator.hasNext(); i++) {
      paramIterator.next();
    }
    return i;
  }
  
  public static <T> T[] toArray(Iterator<? extends T> paramIterator, Class<T> paramClass)
  {
    return Iterables.toArray(Lists.newArrayList(paramIterator), paramClass);
  }
  
  public static String toString(Iterator<?> paramIterator)
  {
    if (!paramIterator.hasNext()) {
      return "[]";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[').append(paramIterator.next());
    while (paramIterator.hasNext()) {
      localStringBuilder.append(", ").append(paramIterator.next());
    }
    return ']';
  }
  
  public static <F, T> Iterator<T> transform(Iterator<F> paramIterator, final Function<? super F, ? extends T> paramFunction)
  {
    Preconditions.checkNotNull(paramIterator);
    Preconditions.checkNotNull(paramFunction);
    new Iterator()
    {
      public boolean hasNext()
      {
        return this.val$fromIterator.hasNext();
      }
      
      public T next()
      {
        Object localObject = this.val$fromIterator.next();
        return paramFunction.apply(localObject);
      }
      
      public void remove()
      {
        this.val$fromIterator.remove();
      }
    };
  }
  
  public static <T> UnmodifiableIterator<T> unmodifiableIterator(Iterator<T> paramIterator)
  {
    Preconditions.checkNotNull(paramIterator);
    if ((paramIterator instanceof UnmodifiableIterator)) {
      return (UnmodifiableIterator)paramIterator;
    }
    new UnmodifiableIterator()
    {
      public boolean hasNext()
      {
        return this.val$iterator.hasNext();
      }
      
      public T next()
      {
        return this.val$iterator.next();
      }
    };
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Iterators
 * JD-Core Version:    0.7.0.1
 */