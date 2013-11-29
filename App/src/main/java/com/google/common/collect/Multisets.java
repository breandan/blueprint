package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

public final class Multisets
{
  private static final Ordering<Multiset.Entry<?>> DECREASING_COUNT_ORDERING = new Ordering()
  {
    public int compare(Multiset.Entry<?> paramAnonymousEntry1, Multiset.Entry<?> paramAnonymousEntry2)
    {
      return Ints.compare(paramAnonymousEntry2.getCount(), paramAnonymousEntry1.getCount());
    }
  };
  
  static <E> boolean addAllImpl(Multiset<E> paramMultiset, Collection<? extends E> paramCollection)
  {
    if (paramCollection.isEmpty()) {
      return false;
    }
    if ((paramCollection instanceof Multiset))
    {
      Iterator localIterator = cast(paramCollection).entrySet().iterator();
      while (localIterator.hasNext())
      {
        Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
        paramMultiset.add(localEntry.getElement(), localEntry.getCount());
      }
    }
    Iterators.addAll(paramMultiset, paramCollection.iterator());
    return true;
  }
  
  static <T> Multiset<T> cast(Iterable<T> paramIterable)
  {
    return (Multiset)paramIterable;
  }
  
  static boolean equalsImpl(Multiset<?> paramMultiset, @Nullable Object paramObject)
  {
    if (paramObject == paramMultiset) {}
    Multiset.Entry localEntry;
    do
    {
      Iterator localIterator;
      while (!localIterator.hasNext())
      {
        return true;
        if (!(paramObject instanceof Multiset)) {
          break;
        }
        Multiset localMultiset = (Multiset)paramObject;
        if ((paramMultiset.size() != localMultiset.size()) || (paramMultiset.entrySet().size() != localMultiset.entrySet().size())) {
          return false;
        }
        localIterator = localMultiset.entrySet().iterator();
      }
      localEntry = (Multiset.Entry)localIterator.next();
    } while (paramMultiset.count(localEntry.getElement()) == localEntry.getCount());
    return false;
    return false;
  }
  
  static <E> Iterator<E> iteratorImpl(Multiset<E> paramMultiset)
  {
    return new MultisetIteratorImpl(paramMultiset, paramMultiset.entrySet().iterator());
  }
  
  static boolean removeAllImpl(Multiset<?> paramMultiset, Collection<?> paramCollection)
  {
    if ((paramCollection instanceof Multiset)) {}
    for (Object localObject = ((Multiset)paramCollection).elementSet();; localObject = paramCollection) {
      return paramMultiset.elementSet().removeAll((Collection)localObject);
    }
  }
  
  static boolean retainAllImpl(Multiset<?> paramMultiset, Collection<?> paramCollection)
  {
    if ((paramCollection instanceof Multiset)) {}
    for (Object localObject = ((Multiset)paramCollection).elementSet();; localObject = paramCollection) {
      return paramMultiset.elementSet().retainAll((Collection)localObject);
    }
  }
  
  static int sizeImpl(Multiset<?> paramMultiset)
  {
    long l = 0L;
    Iterator localIterator = paramMultiset.entrySet().iterator();
    while (localIterator.hasNext()) {
      l += ((Multiset.Entry)localIterator.next()).getCount();
    }
    return Ints.saturatedCast(l);
  }
  
  static abstract class AbstractEntry<E>
    implements Multiset.Entry<E>
  {
    public boolean equals(@Nullable Object paramObject)
    {
      boolean bool1 = paramObject instanceof Multiset.Entry;
      boolean bool2 = false;
      if (bool1)
      {
        Multiset.Entry localEntry = (Multiset.Entry)paramObject;
        int i = getCount();
        int j = localEntry.getCount();
        bool2 = false;
        if (i == j)
        {
          boolean bool3 = Objects.equal(getElement(), localEntry.getElement());
          bool2 = false;
          if (bool3) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    
    public int hashCode()
    {
      Object localObject = getElement();
      if (localObject == null) {}
      for (int i = 0;; i = localObject.hashCode()) {
        return i ^ getCount();
      }
    }
    
    public String toString()
    {
      String str = String.valueOf(getElement());
      int i = getCount();
      if (i == 1) {
        return str;
      }
      return str + " x " + i;
    }
  }
  
  static abstract class ElementSet<E>
    extends AbstractSet<E>
  {
    public void clear()
    {
      multiset().clear();
    }
    
    public boolean contains(Object paramObject)
    {
      return multiset().contains(paramObject);
    }
    
    public boolean containsAll(Collection<?> paramCollection)
    {
      return multiset().containsAll(paramCollection);
    }
    
    public boolean isEmpty()
    {
      return multiset().isEmpty();
    }
    
    public Iterator<E> iterator()
    {
      Iterators.transform(multiset().entrySet().iterator(), new Function()
      {
        public E apply(Multiset.Entry<E> paramAnonymousEntry)
        {
          return paramAnonymousEntry.getElement();
        }
      });
    }
    
    abstract Multiset<E> multiset();
    
    public boolean remove(Object paramObject)
    {
      int i = multiset().count(paramObject);
      if (i > 0)
      {
        multiset().remove(paramObject, i);
        return true;
      }
      return false;
    }
    
    public int size()
    {
      return multiset().entrySet().size();
    }
  }
  
  static abstract class EntrySet<E>
    extends AbstractSet<Multiset.Entry<E>>
  {
    public void clear()
    {
      multiset().clear();
    }
    
    public boolean contains(@Nullable Object paramObject)
    {
      Multiset.Entry localEntry;
      if ((paramObject instanceof Multiset.Entry))
      {
        localEntry = (Multiset.Entry)paramObject;
        if (localEntry.getCount() > 0) {
          break label23;
        }
      }
      label23:
      while (multiset().count(localEntry.getElement()) != localEntry.getCount()) {
        return false;
      }
      return true;
    }
    
    abstract Multiset<E> multiset();
    
    public boolean remove(Object paramObject)
    {
      return (contains(paramObject)) && (multiset().elementSet().remove(((Multiset.Entry)paramObject).getElement()));
    }
  }
  
  static final class MultisetIteratorImpl<E>
    implements Iterator<E>
  {
    private boolean canRemove;
    private Multiset.Entry<E> currentEntry;
    private final Iterator<Multiset.Entry<E>> entryIterator;
    private int laterCount;
    private final Multiset<E> multiset;
    private int totalCount;
    
    MultisetIteratorImpl(Multiset<E> paramMultiset, Iterator<Multiset.Entry<E>> paramIterator)
    {
      this.multiset = paramMultiset;
      this.entryIterator = paramIterator;
    }
    
    public boolean hasNext()
    {
      return (this.laterCount > 0) || (this.entryIterator.hasNext());
    }
    
    public E next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (this.laterCount == 0)
      {
        this.currentEntry = ((Multiset.Entry)this.entryIterator.next());
        int i = this.currentEntry.getCount();
        this.laterCount = i;
        this.totalCount = i;
      }
      this.laterCount = (-1 + this.laterCount);
      this.canRemove = true;
      return this.currentEntry.getElement();
    }
    
    public void remove()
    {
      Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");
      if (this.totalCount == 1) {
        this.entryIterator.remove();
      }
      for (;;)
      {
        this.totalCount = (-1 + this.totalCount);
        this.canRemove = false;
        return;
        this.multiset.remove(this.currentEntry.getElement());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Multisets
 * JD-Core Version:    0.7.0.1
 */