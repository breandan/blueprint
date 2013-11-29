package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import javax.annotation.Nullable;

public final class Lists
{
  public static <E> List<E> asList(@Nullable E paramE, E[] paramArrayOfE)
  {
    return new OnePlusArrayList(paramE, paramArrayOfE);
  }
  
  static int computeArrayListCapacity(int paramInt)
  {
    if (paramInt >= 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      return Ints.saturatedCast(5L + paramInt + paramInt / 10);
    }
  }
  
  static boolean equalsImpl(List<?> paramList, @Nullable Object paramObject)
  {
    if (paramObject == Preconditions.checkNotNull(paramList)) {}
    List localList;
    do
    {
      return true;
      if (!(paramObject instanceof List)) {
        return false;
      }
      localList = (List)paramObject;
    } while ((paramList.size() == localList.size()) && (Iterators.elementsEqual(paramList.iterator(), localList.iterator())));
    return false;
  }
  
  static int hashCodeImpl(List<?> paramList)
  {
    int i = 1;
    Iterator localIterator = paramList.iterator();
    if (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      int j = i * 31;
      if (localObject == null) {}
      for (int k = 0;; k = localObject.hashCode())
      {
        i = j + k;
        break;
      }
    }
    return i;
  }
  
  public static <E> ArrayList<E> newArrayList()
  {
    return new ArrayList();
  }
  
  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> paramIterable)
  {
    Preconditions.checkNotNull(paramIterable);
    if ((paramIterable instanceof Collection)) {
      return new ArrayList(Collections2.cast(paramIterable));
    }
    return newArrayList(paramIterable.iterator());
  }
  
  public static <E> ArrayList<E> newArrayList(Iterator<? extends E> paramIterator)
  {
    Preconditions.checkNotNull(paramIterator);
    ArrayList localArrayList = newArrayList();
    while (paramIterator.hasNext()) {
      localArrayList.add(paramIterator.next());
    }
    return localArrayList;
  }
  
  public static <E> ArrayList<E> newArrayList(E... paramVarArgs)
  {
    Preconditions.checkNotNull(paramVarArgs);
    ArrayList localArrayList = new ArrayList(computeArrayListCapacity(paramVarArgs.length));
    Collections.addAll(localArrayList, paramVarArgs);
    return localArrayList;
  }
  
  public static <E> ArrayList<E> newArrayListWithCapacity(int paramInt)
  {
    if (paramInt >= 0) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      return new ArrayList(paramInt);
    }
  }
  
  public static <E> ArrayList<E> newArrayListWithExpectedSize(int paramInt)
  {
    return new ArrayList(computeArrayListCapacity(paramInt));
  }
  
  public static <E> LinkedList<E> newLinkedList()
  {
    return new LinkedList();
  }
  
  public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> paramIterable)
  {
    LinkedList localLinkedList = newLinkedList();
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext()) {
      localLinkedList.add(localIterator.next());
    }
    return localLinkedList;
  }
  
  public static <T> List<T> reverse(List<T> paramList)
  {
    if ((paramList instanceof ReverseList)) {
      return ((ReverseList)paramList).getForwardList();
    }
    if ((paramList instanceof RandomAccess)) {
      return new RandomAccessReverseList(paramList);
    }
    return new ReverseList(paramList);
  }
  
  public static <F, T> List<T> transform(List<F> paramList, Function<? super F, ? extends T> paramFunction)
  {
    if ((paramList instanceof RandomAccess)) {
      return new TransformingRandomAccessList(paramList, paramFunction);
    }
    return new TransformingSequentialList(paramList, paramFunction);
  }
  
  private static class OnePlusArrayList<E>
    extends AbstractList<E>
    implements Serializable, RandomAccess
  {
    private static final long serialVersionUID;
    final E first;
    final E[] rest;
    
    OnePlusArrayList(@Nullable E paramE, E[] paramArrayOfE)
    {
      this.first = paramE;
      this.rest = ((Object[])Preconditions.checkNotNull(paramArrayOfE));
    }
    
    public E get(int paramInt)
    {
      Preconditions.checkElementIndex(paramInt, size());
      if (paramInt == 0) {
        return this.first;
      }
      return this.rest[(paramInt - 1)];
    }
    
    public int size()
    {
      return 1 + this.rest.length;
    }
  }
  
  private static class RandomAccessReverseList<T>
    extends Lists.ReverseList<T>
    implements RandomAccess
  {
    RandomAccessReverseList(List<T> paramList)
    {
      super();
    }
  }
  
  private static class ReverseList<T>
    extends AbstractList<T>
  {
    private final List<T> forwardList;
    
    ReverseList(List<T> paramList)
    {
      this.forwardList = ((List)Preconditions.checkNotNull(paramList));
    }
    
    private int reverseIndex(int paramInt)
    {
      int i = size();
      Preconditions.checkElementIndex(paramInt, i);
      return i - 1 - paramInt;
    }
    
    private int reversePosition(int paramInt)
    {
      int i = size();
      Preconditions.checkPositionIndex(paramInt, i);
      return i - paramInt;
    }
    
    public void add(int paramInt, @Nullable T paramT)
    {
      this.forwardList.add(reversePosition(paramInt), paramT);
    }
    
    public void clear()
    {
      this.forwardList.clear();
    }
    
    public boolean contains(@Nullable Object paramObject)
    {
      return this.forwardList.contains(paramObject);
    }
    
    public boolean containsAll(Collection<?> paramCollection)
    {
      return this.forwardList.containsAll(paramCollection);
    }
    
    public T get(int paramInt)
    {
      return this.forwardList.get(reverseIndex(paramInt));
    }
    
    List<T> getForwardList()
    {
      return this.forwardList;
    }
    
    public int indexOf(@Nullable Object paramObject)
    {
      int i = this.forwardList.lastIndexOf(paramObject);
      if (i >= 0) {
        return reverseIndex(i);
      }
      return -1;
    }
    
    public boolean isEmpty()
    {
      return this.forwardList.isEmpty();
    }
    
    public Iterator<T> iterator()
    {
      return listIterator();
    }
    
    public int lastIndexOf(@Nullable Object paramObject)
    {
      int i = this.forwardList.indexOf(paramObject);
      if (i >= 0) {
        return reverseIndex(i);
      }
      return -1;
    }
    
    public ListIterator<T> listIterator(int paramInt)
    {
      int i = reversePosition(paramInt);
      new ListIterator()
      {
        boolean canRemove;
        boolean canSet;
        
        public void add(T paramAnonymousT)
        {
          this.val$forwardIterator.add(paramAnonymousT);
          this.val$forwardIterator.previous();
          this.canRemove = false;
          this.canSet = false;
        }
        
        public boolean hasNext()
        {
          return this.val$forwardIterator.hasPrevious();
        }
        
        public boolean hasPrevious()
        {
          return this.val$forwardIterator.hasNext();
        }
        
        public T next()
        {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          this.canRemove = true;
          this.canSet = true;
          return this.val$forwardIterator.previous();
        }
        
        public int nextIndex()
        {
          return Lists.ReverseList.this.reversePosition(this.val$forwardIterator.nextIndex());
        }
        
        public T previous()
        {
          if (!hasPrevious()) {
            throw new NoSuchElementException();
          }
          this.canRemove = true;
          this.canSet = true;
          return this.val$forwardIterator.next();
        }
        
        public int previousIndex()
        {
          return -1 + nextIndex();
        }
        
        public void remove()
        {
          Preconditions.checkState(this.canRemove);
          this.val$forwardIterator.remove();
          this.canSet = false;
          this.canRemove = false;
        }
        
        public void set(T paramAnonymousT)
        {
          Preconditions.checkState(this.canSet);
          this.val$forwardIterator.set(paramAnonymousT);
        }
      };
    }
    
    public T remove(int paramInt)
    {
      return this.forwardList.remove(reverseIndex(paramInt));
    }
    
    protected void removeRange(int paramInt1, int paramInt2)
    {
      subList(paramInt1, paramInt2).clear();
    }
    
    public T set(int paramInt, @Nullable T paramT)
    {
      return this.forwardList.set(reverseIndex(paramInt), paramT);
    }
    
    public int size()
    {
      return this.forwardList.size();
    }
    
    public List<T> subList(int paramInt1, int paramInt2)
    {
      Preconditions.checkPositionIndexes(paramInt1, paramInt2, size());
      return Lists.reverse(this.forwardList.subList(reversePosition(paramInt2), reversePosition(paramInt1)));
    }
  }
  
  private static class TransformingRandomAccessList<F, T>
    extends AbstractList<T>
    implements Serializable, RandomAccess
  {
    private static final long serialVersionUID;
    final List<F> fromList;
    final Function<? super F, ? extends T> function;
    
    TransformingRandomAccessList(List<F> paramList, Function<? super F, ? extends T> paramFunction)
    {
      this.fromList = ((List)Preconditions.checkNotNull(paramList));
      this.function = ((Function)Preconditions.checkNotNull(paramFunction));
    }
    
    public void clear()
    {
      this.fromList.clear();
    }
    
    public T get(int paramInt)
    {
      return this.function.apply(this.fromList.get(paramInt));
    }
    
    public boolean isEmpty()
    {
      return this.fromList.isEmpty();
    }
    
    public T remove(int paramInt)
    {
      return this.function.apply(this.fromList.remove(paramInt));
    }
    
    public int size()
    {
      return this.fromList.size();
    }
  }
  
  private static class TransformingSequentialList<F, T>
    extends AbstractSequentialList<T>
    implements Serializable
  {
    private static final long serialVersionUID;
    final List<F> fromList;
    final Function<? super F, ? extends T> function;
    
    TransformingSequentialList(List<F> paramList, Function<? super F, ? extends T> paramFunction)
    {
      this.fromList = ((List)Preconditions.checkNotNull(paramList));
      this.function = ((Function)Preconditions.checkNotNull(paramFunction));
    }
    
    public void clear()
    {
      this.fromList.clear();
    }
    
    public ListIterator<T> listIterator(int paramInt)
    {
      new ListIterator()
      {
        public void add(T paramAnonymousT)
        {
          throw new UnsupportedOperationException();
        }
        
        public boolean hasNext()
        {
          return this.val$delegate.hasNext();
        }
        
        public boolean hasPrevious()
        {
          return this.val$delegate.hasPrevious();
        }
        
        public T next()
        {
          return Lists.TransformingSequentialList.this.function.apply(this.val$delegate.next());
        }
        
        public int nextIndex()
        {
          return this.val$delegate.nextIndex();
        }
        
        public T previous()
        {
          return Lists.TransformingSequentialList.this.function.apply(this.val$delegate.previous());
        }
        
        public int previousIndex()
        {
          return this.val$delegate.previousIndex();
        }
        
        public void remove()
        {
          this.val$delegate.remove();
        }
        
        public void set(T paramAnonymousT)
        {
          throw new UnsupportedOperationException("not supported");
        }
      };
    }
    
    public int size()
    {
      return this.fromList.size();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Lists
 * JD-Core Version:    0.7.0.1
 */