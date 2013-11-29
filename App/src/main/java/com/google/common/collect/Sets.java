package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nullable;

public final class Sets
{
  public static <E> SetView<E> difference(Set<E> paramSet, final Set<?> paramSet1)
  {
    Preconditions.checkNotNull(paramSet, "set1");
    Preconditions.checkNotNull(paramSet1, "set2");
    new SetView(paramSet)
    {
      public boolean contains(Object paramAnonymousObject)
      {
        return (this.val$set1.contains(paramAnonymousObject)) && (!paramSet1.contains(paramAnonymousObject));
      }
      
      public boolean isEmpty()
      {
        return paramSet1.containsAll(this.val$set1);
      }
      
      public Iterator<E> iterator()
      {
        return Iterators.filter(this.val$set1.iterator(), this.val$notInSet2);
      }
      
      public int size()
      {
        return Iterators.size(iterator());
      }
    };
  }
  
  static boolean equalsImpl(Set<?> paramSet, @Nullable Object paramObject)
  {
    boolean bool1 = true;
    boolean bool3;
    if (paramSet == paramObject) {
      bool3 = bool1;
    }
    boolean bool2;
    do
    {
      return bool3;
      bool2 = paramObject instanceof Set;
      bool3 = false;
    } while (!bool2);
    Set localSet = (Set)paramObject;
    try
    {
      if (paramSet.size() == localSet.size())
      {
        boolean bool4 = paramSet.containsAll(localSet);
        if (!bool4) {}
      }
      for (;;)
      {
        return bool1;
        bool1 = false;
      }
      return false;
    }
    catch (NullPointerException localNullPointerException)
    {
      return false;
    }
    catch (ClassCastException localClassCastException) {}
  }
  
  public static <E> Set<E> filter(Set<E> paramSet, Predicate<? super E> paramPredicate)
  {
    if ((paramSet instanceof SortedSet)) {
      return filter((SortedSet)paramSet, paramPredicate);
    }
    if ((paramSet instanceof FilteredSet))
    {
      FilteredSet localFilteredSet = (FilteredSet)paramSet;
      Predicate localPredicate = Predicates.and(localFilteredSet.predicate, paramPredicate);
      return new FilteredSet((Set)localFilteredSet.unfiltered, localPredicate);
    }
    return new FilteredSet((Set)Preconditions.checkNotNull(paramSet), (Predicate)Preconditions.checkNotNull(paramPredicate));
  }
  
  public static <E> SortedSet<E> filter(SortedSet<E> paramSortedSet, Predicate<? super E> paramPredicate)
  {
    if ((paramSortedSet instanceof FilteredSet))
    {
      FilteredSet localFilteredSet = (FilteredSet)paramSortedSet;
      Predicate localPredicate = Predicates.and(localFilteredSet.predicate, paramPredicate);
      return new FilteredSortedSet((SortedSet)localFilteredSet.unfiltered, localPredicate);
    }
    return new FilteredSortedSet((SortedSet)Preconditions.checkNotNull(paramSortedSet), (Predicate)Preconditions.checkNotNull(paramPredicate));
  }
  
  static int hashCodeImpl(Set<?> paramSet)
  {
    int i = 0;
    Iterator localIterator = paramSet.iterator();
    if (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if (localObject != null) {}
      for (int j = localObject.hashCode();; j = 0)
      {
        i += j;
        break;
      }
    }
    return i;
  }
  
  public static <E> SetView<E> intersection(Set<E> paramSet, final Set<?> paramSet1)
  {
    Preconditions.checkNotNull(paramSet, "set1");
    Preconditions.checkNotNull(paramSet1, "set2");
    new SetView(paramSet)
    {
      public boolean contains(Object paramAnonymousObject)
      {
        return (this.val$set1.contains(paramAnonymousObject)) && (paramSet1.contains(paramAnonymousObject));
      }
      
      public boolean containsAll(Collection<?> paramAnonymousCollection)
      {
        return (this.val$set1.containsAll(paramAnonymousCollection)) && (paramSet1.containsAll(paramAnonymousCollection));
      }
      
      public boolean isEmpty()
      {
        return !iterator().hasNext();
      }
      
      public Iterator<E> iterator()
      {
        return Iterators.filter(this.val$set1.iterator(), this.val$inSet2);
      }
      
      public int size()
      {
        return Iterators.size(iterator());
      }
    };
  }
  
  public static <E> HashSet<E> newHashSet()
  {
    return new HashSet();
  }
  
  public static <E> HashSet<E> newHashSet(Iterable<? extends E> paramIterable)
  {
    if ((paramIterable instanceof Collection)) {
      return new HashSet(Collections2.cast(paramIterable));
    }
    return newHashSet(paramIterable.iterator());
  }
  
  public static <E> HashSet<E> newHashSet(Iterator<? extends E> paramIterator)
  {
    HashSet localHashSet = newHashSet();
    while (paramIterator.hasNext()) {
      localHashSet.add(paramIterator.next());
    }
    return localHashSet;
  }
  
  public static <E> HashSet<E> newHashSet(E... paramVarArgs)
  {
    HashSet localHashSet = newHashSetWithExpectedSize(paramVarArgs.length);
    Collections.addAll(localHashSet, paramVarArgs);
    return localHashSet;
  }
  
  public static <E> HashSet<E> newHashSetWithExpectedSize(int paramInt)
  {
    return new HashSet(Maps.capacity(paramInt));
  }
  
  public static <E> LinkedHashSet<E> newLinkedHashSet()
  {
    return new LinkedHashSet();
  }
  
  public static <E> TreeSet<E> newTreeSet(Comparator<? super E> paramComparator)
  {
    return new TreeSet((Comparator)Preconditions.checkNotNull(paramComparator));
  }
  
  public static <E> SetView<E> union(Set<? extends E> paramSet1, final Set<? extends E> paramSet2)
  {
    Preconditions.checkNotNull(paramSet1, "set1");
    Preconditions.checkNotNull(paramSet2, "set2");
    new SetView(paramSet1)
    {
      public boolean contains(Object paramAnonymousObject)
      {
        return (this.val$set1.contains(paramAnonymousObject)) || (paramSet2.contains(paramAnonymousObject));
      }
      
      public boolean isEmpty()
      {
        return (this.val$set1.isEmpty()) && (paramSet2.isEmpty());
      }
      
      public Iterator<E> iterator()
      {
        return Iterators.unmodifiableIterator(Iterators.concat(this.val$set1.iterator(), this.val$set2minus1.iterator()));
      }
      
      public int size()
      {
        return this.val$set1.size() + this.val$set2minus1.size();
      }
    };
  }
  
  private static class FilteredSet<E>
    extends Collections2.FilteredCollection<E>
    implements Set<E>
  {
    FilteredSet(Set<E> paramSet, Predicate<? super E> paramPredicate)
    {
      super(paramPredicate);
    }
    
    public boolean equals(@Nullable Object paramObject)
    {
      return Sets.equalsImpl(this, paramObject);
    }
    
    public int hashCode()
    {
      return Sets.hashCodeImpl(this);
    }
  }
  
  private static class FilteredSortedSet<E>
    extends Collections2.FilteredCollection<E>
    implements SortedSet<E>
  {
    FilteredSortedSet(SortedSet<E> paramSortedSet, Predicate<? super E> paramPredicate)
    {
      super(paramPredicate);
    }
    
    public Comparator<? super E> comparator()
    {
      return ((SortedSet)this.unfiltered).comparator();
    }
    
    public boolean equals(@Nullable Object paramObject)
    {
      return Sets.equalsImpl(this, paramObject);
    }
    
    public E first()
    {
      return iterator().next();
    }
    
    public int hashCode()
    {
      return Sets.hashCodeImpl(this);
    }
    
    public SortedSet<E> headSet(E paramE)
    {
      return new FilteredSortedSet(((SortedSet)this.unfiltered).headSet(paramE), this.predicate);
    }
    
    public E last()
    {
      Object localObject;
      for (SortedSet localSortedSet = (SortedSet)this.unfiltered;; localSortedSet = localSortedSet.headSet(localObject))
      {
        localObject = localSortedSet.last();
        if (this.predicate.apply(localObject)) {
          return localObject;
        }
      }
    }
    
    public SortedSet<E> subSet(E paramE1, E paramE2)
    {
      return new FilteredSortedSet(((SortedSet)this.unfiltered).subSet(paramE1, paramE2), this.predicate);
    }
    
    public SortedSet<E> tailSet(E paramE)
    {
      return new FilteredSortedSet(((SortedSet)this.unfiltered).tailSet(paramE), this.predicate);
    }
  }
  
  public static abstract class SetView<E>
    extends AbstractSet<E>
  {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Sets
 * JD-Core Version:    0.7.0.1
 */