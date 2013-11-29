package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

public final class Maps
{
  static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
  
  static int capacity(int paramInt)
  {
    if (paramInt < 3)
    {
      if (paramInt >= 0) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkArgument(bool);
        return paramInt + 1;
      }
    }
    if (paramInt < 1073741824) {
      return paramInt + paramInt / 3;
    }
    return 2147483647;
  }
  
  public static <K, V> Map<K, V> filterEntries(Map<K, V> paramMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
  {
    if ((paramMap instanceof SortedMap)) {
      return filterEntries((SortedMap)paramMap, paramPredicate);
    }
    Preconditions.checkNotNull(paramPredicate);
    if ((paramMap instanceof AbstractFilteredMap)) {
      return filterFiltered((AbstractFilteredMap)paramMap, paramPredicate);
    }
    return new FilteredEntryMap((Map)Preconditions.checkNotNull(paramMap), paramPredicate);
  }
  
  public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> paramSortedMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    if ((paramSortedMap instanceof FilteredEntrySortedMap)) {
      return filterFiltered((FilteredEntrySortedMap)paramSortedMap, paramPredicate);
    }
    return new FilteredEntrySortedMap((SortedMap)Preconditions.checkNotNull(paramSortedMap), paramPredicate);
  }
  
  private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> paramAbstractFilteredMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
  {
    Predicate localPredicate = Predicates.and(paramAbstractFilteredMap.predicate, paramPredicate);
    return new FilteredEntryMap(paramAbstractFilteredMap.unfiltered, localPredicate);
  }
  
  private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> paramFilteredEntrySortedMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
  {
    Predicate localPredicate = Predicates.and(paramFilteredEntrySortedMap.predicate, paramPredicate);
    return new FilteredEntrySortedMap(paramFilteredEntrySortedMap.sortedMap(), localPredicate);
  }
  
  public static <K, V> Map<K, V> filterValues(Map<K, V> paramMap, Predicate<? super V> paramPredicate)
  {
    if ((paramMap instanceof SortedMap)) {
      return filterValues((SortedMap)paramMap, paramPredicate);
    }
    Preconditions.checkNotNull(paramPredicate);
    filterEntries(paramMap, new Predicate()
    {
      public boolean apply(Map.Entry<K, V> paramAnonymousEntry)
      {
        return this.val$valuePredicate.apply(paramAnonymousEntry.getValue());
      }
    });
  }
  
  public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> paramSortedMap, Predicate<? super V> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    filterEntries(paramSortedMap, new Predicate()
    {
      public boolean apply(Map.Entry<K, V> paramAnonymousEntry)
      {
        return this.val$valuePredicate.apply(paramAnonymousEntry.getValue());
      }
    });
  }
  
  public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable K paramK, @Nullable V paramV)
  {
    return new ImmutableEntry(paramK, paramV);
  }
  
  public static <K, V> ConcurrentMap<K, V> newConcurrentMap()
  {
    return new MapMaker().makeMap();
  }
  
  public static <K, V> HashMap<K, V> newHashMap()
  {
    return new HashMap();
  }
  
  public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> paramMap)
  {
    return new HashMap(paramMap);
  }
  
  public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int paramInt)
  {
    return new HashMap(capacity(paramInt));
  }
  
  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap()
  {
    return new LinkedHashMap();
  }
  
  public static <K extends Comparable, V> TreeMap<K, V> newTreeMap()
  {
    return new TreeMap();
  }
  
  public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> paramSortedMap)
  {
    return new TreeMap(paramSortedMap);
  }
  
  static boolean safeContainsKey(Map<?, ?> paramMap, Object paramObject)
  {
    try
    {
      boolean bool = paramMap.containsKey(paramObject);
      return bool;
    }
    catch (ClassCastException localClassCastException) {}
    return false;
  }
  
  static <V> V safeGet(Map<?, V> paramMap, Object paramObject)
  {
    try
    {
      Object localObject = paramMap.get(paramObject);
      return localObject;
    }
    catch (ClassCastException localClassCastException) {}
    return null;
  }
  
  static String toStringImpl(Map<?, ?> paramMap)
  {
    StringBuilder localStringBuilder = Collections2.newStringBuilderForCollection(paramMap.size()).append('{');
    STANDARD_JOINER.appendTo(localStringBuilder, paramMap);
    return '}';
  }
  
  private static abstract class AbstractFilteredMap<K, V>
    extends AbstractMap<K, V>
  {
    final Predicate<? super Map.Entry<K, V>> predicate;
    final Map<K, V> unfiltered;
    Collection<V> values;
    
    AbstractFilteredMap(Map<K, V> paramMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
    {
      this.unfiltered = paramMap;
      this.predicate = paramPredicate;
    }
    
    boolean apply(Object paramObject, V paramV)
    {
      return this.predicate.apply(Maps.immutableEntry(paramObject, paramV));
    }
    
    public boolean containsKey(Object paramObject)
    {
      return (this.unfiltered.containsKey(paramObject)) && (apply(paramObject, this.unfiltered.get(paramObject)));
    }
    
    public V get(Object paramObject)
    {
      Object localObject = this.unfiltered.get(paramObject);
      if ((localObject != null) && (apply(paramObject, localObject))) {
        return localObject;
      }
      return null;
    }
    
    public boolean isEmpty()
    {
      return entrySet().isEmpty();
    }
    
    public V put(K paramK, V paramV)
    {
      Preconditions.checkArgument(apply(paramK, paramV));
      return this.unfiltered.put(paramK, paramV);
    }
    
    public void putAll(Map<? extends K, ? extends V> paramMap)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        Preconditions.checkArgument(apply(localEntry.getKey(), localEntry.getValue()));
      }
      this.unfiltered.putAll(paramMap);
    }
    
    public V remove(Object paramObject)
    {
      if (containsKey(paramObject)) {
        return this.unfiltered.remove(paramObject);
      }
      return null;
    }
    
    public Collection<V> values()
    {
      Object localObject = this.values;
      if (localObject == null)
      {
        localObject = new Values();
        this.values = ((Collection)localObject);
      }
      return localObject;
    }
    
    class Values
      extends AbstractCollection<V>
    {
      Values() {}
      
      public void clear()
      {
        Maps.AbstractFilteredMap.this.entrySet().clear();
      }
      
      public boolean isEmpty()
      {
        return Maps.AbstractFilteredMap.this.entrySet().isEmpty();
      }
      
      public Iterator<V> iterator()
      {
        new UnmodifiableIterator()
        {
          public boolean hasNext()
          {
            return this.val$entryIterator.hasNext();
          }
          
          public V next()
          {
            return ((Map.Entry)this.val$entryIterator.next()).getValue();
          }
        };
      }
      
      public boolean remove(Object paramObject)
      {
        Iterator localIterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if ((Objects.equal(paramObject, localEntry.getValue())) && (Maps.AbstractFilteredMap.this.predicate.apply(localEntry)))
          {
            localIterator.remove();
            return true;
          }
        }
        return false;
      }
      
      public boolean removeAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        boolean bool = false;
        Iterator localIterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if ((paramCollection.contains(localEntry.getValue())) && (Maps.AbstractFilteredMap.this.predicate.apply(localEntry)))
          {
            localIterator.remove();
            bool = true;
          }
        }
        return bool;
      }
      
      public boolean retainAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        boolean bool = false;
        Iterator localIterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if ((!paramCollection.contains(localEntry.getValue())) && (Maps.AbstractFilteredMap.this.predicate.apply(localEntry)))
          {
            localIterator.remove();
            bool = true;
          }
        }
        return bool;
      }
      
      public int size()
      {
        return Maps.AbstractFilteredMap.this.entrySet().size();
      }
      
      public Object[] toArray()
      {
        return Lists.newArrayList(iterator()).toArray();
      }
      
      public <T> T[] toArray(T[] paramArrayOfT)
      {
        return Lists.newArrayList(iterator()).toArray(paramArrayOfT);
      }
    }
  }
  
  static abstract class EntrySet<K, V>
    extends AbstractSet<Map.Entry<K, V>>
  {
    public void clear()
    {
      map().clear();
    }
    
    public boolean contains(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Map.Entry;
      boolean bool2 = false;
      if (bool1)
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        Object localObject1 = localEntry.getKey();
        Object localObject2 = map().get(localObject1);
        boolean bool3 = Objects.equal(localObject2, localEntry.getValue());
        bool2 = false;
        if (bool3) {
          if (localObject2 == null)
          {
            boolean bool4 = map().containsKey(localObject1);
            bool2 = false;
            if (!bool4) {}
          }
          else
          {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    
    public boolean isEmpty()
    {
      return map().isEmpty();
    }
    
    abstract Map<K, V> map();
    
    public boolean remove(Object paramObject)
    {
      if (contains(paramObject))
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        return map().keySet().remove(localEntry.getKey());
      }
      return false;
    }
    
    public boolean removeAll(Collection<?> paramCollection)
    {
      boolean bool1;
      Iterator localIterator;
      try
      {
        boolean bool2 = super.removeAll((Collection)Preconditions.checkNotNull(paramCollection));
        bool1 = bool2;
        return bool1;
      }
      catch (UnsupportedOperationException localUnsupportedOperationException)
      {
        bool1 = true;
        localIterator = paramCollection.iterator();
      }
      while (localIterator.hasNext()) {
        bool1 |= remove(localIterator.next());
      }
    }
    
    public boolean retainAll(Collection<?> paramCollection)
    {
      try
      {
        boolean bool = super.retainAll((Collection)Preconditions.checkNotNull(paramCollection));
        return bool;
      }
      catch (UnsupportedOperationException localUnsupportedOperationException)
      {
        HashSet localHashSet = Sets.newHashSetWithExpectedSize(paramCollection.size());
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
        {
          Object localObject = localIterator.next();
          if (contains(localObject)) {
            localHashSet.add(((Map.Entry)localObject).getKey());
          }
        }
        return map().keySet().retainAll(localHashSet);
      }
    }
    
    public int size()
    {
      return map().size();
    }
  }
  
  static class FilteredEntryMap<K, V>
    extends Maps.AbstractFilteredMap<K, V>
  {
    Set<Map.Entry<K, V>> entrySet;
    final Set<Map.Entry<K, V>> filteredEntrySet = Sets.filter(paramMap.entrySet(), this.predicate);
    Set<K> keySet;
    
    FilteredEntryMap(Map<K, V> paramMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
    {
      super(paramPredicate);
    }
    
    public Set<Map.Entry<K, V>> entrySet()
    {
      Object localObject = this.entrySet;
      if (localObject == null)
      {
        localObject = new EntrySet(null);
        this.entrySet = ((Set)localObject);
      }
      return localObject;
    }
    
    public Set<K> keySet()
    {
      Object localObject = this.keySet;
      if (localObject == null)
      {
        localObject = new KeySet(null);
        this.keySet = ((Set)localObject);
      }
      return localObject;
    }
    
    private class EntrySet
      extends ForwardingSet<Map.Entry<K, V>>
    {
      private EntrySet() {}
      
      protected Set<Map.Entry<K, V>> delegate()
      {
        return Maps.FilteredEntryMap.this.filteredEntrySet;
      }
      
      public Iterator<Map.Entry<K, V>> iterator()
      {
        new UnmodifiableIterator()
        {
          public boolean hasNext()
          {
            return this.val$iterator.hasNext();
          }
          
          public Map.Entry<K, V> next()
          {
            new ForwardingMapEntry()
            {
              protected Map.Entry<K, V> delegate()
              {
                return this.val$entry;
              }
              
              public V setValue(V paramAnonymous2V)
              {
                Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(this.val$entry.getKey(), paramAnonymous2V));
                return super.setValue(paramAnonymous2V);
              }
            };
          }
        };
      }
    }
    
    private class KeySet
      extends AbstractSet<K>
    {
      private KeySet() {}
      
      public void clear()
      {
        Maps.FilteredEntryMap.this.filteredEntrySet.clear();
      }
      
      public boolean contains(Object paramObject)
      {
        return Maps.FilteredEntryMap.this.containsKey(paramObject);
      }
      
      public Iterator<K> iterator()
      {
        new UnmodifiableIterator()
        {
          public boolean hasNext()
          {
            return this.val$iterator.hasNext();
          }
          
          public K next()
          {
            return ((Map.Entry)this.val$iterator.next()).getKey();
          }
        };
      }
      
      public boolean remove(Object paramObject)
      {
        if (Maps.FilteredEntryMap.this.containsKey(paramObject))
        {
          Maps.FilteredEntryMap.this.unfiltered.remove(paramObject);
          return true;
        }
        return false;
      }
      
      public boolean removeAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        boolean bool = false;
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext()) {
          bool |= remove(localIterator.next());
        }
        return bool;
      }
      
      public boolean retainAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        boolean bool = false;
        Iterator localIterator = Maps.FilteredEntryMap.this.unfiltered.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if ((!paramCollection.contains(localEntry.getKey())) && (Maps.FilteredEntryMap.this.predicate.apply(localEntry)))
          {
            localIterator.remove();
            bool = true;
          }
        }
        return bool;
      }
      
      public int size()
      {
        return Maps.FilteredEntryMap.this.filteredEntrySet.size();
      }
      
      public Object[] toArray()
      {
        return Lists.newArrayList(iterator()).toArray();
      }
      
      public <T> T[] toArray(T[] paramArrayOfT)
      {
        return Lists.newArrayList(iterator()).toArray(paramArrayOfT);
      }
    }
  }
  
  private static class FilteredEntrySortedMap<K, V>
    extends Maps.FilteredEntryMap<K, V>
    implements SortedMap<K, V>
  {
    FilteredEntrySortedMap(SortedMap<K, V> paramSortedMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
    {
      super(paramPredicate);
    }
    
    public Comparator<? super K> comparator()
    {
      return sortedMap().comparator();
    }
    
    public K firstKey()
    {
      return keySet().iterator().next();
    }
    
    public SortedMap<K, V> headMap(K paramK)
    {
      return new FilteredEntrySortedMap(sortedMap().headMap(paramK), this.predicate);
    }
    
    public K lastKey()
    {
      Object localObject;
      for (SortedMap localSortedMap = sortedMap();; localSortedMap = sortedMap().headMap(localObject))
      {
        localObject = localSortedMap.lastKey();
        if (apply(localObject, this.unfiltered.get(localObject))) {
          return localObject;
        }
      }
    }
    
    SortedMap<K, V> sortedMap()
    {
      return (SortedMap)this.unfiltered;
    }
    
    public SortedMap<K, V> subMap(K paramK1, K paramK2)
    {
      return new FilteredEntrySortedMap(sortedMap().subMap(paramK1, paramK2), this.predicate);
    }
    
    public SortedMap<K, V> tailMap(K paramK)
    {
      return new FilteredEntrySortedMap(sortedMap().tailMap(paramK), this.predicate);
    }
  }
  
  static abstract class KeySet<K, V>
    extends AbstractSet<K>
  {
    public void clear()
    {
      map().clear();
    }
    
    public boolean contains(Object paramObject)
    {
      return map().containsKey(paramObject);
    }
    
    public boolean isEmpty()
    {
      return map().isEmpty();
    }
    
    public Iterator<K> iterator()
    {
      Iterators.transform(map().entrySet().iterator(), new Function()
      {
        public K apply(Map.Entry<K, V> paramAnonymousEntry)
        {
          return paramAnonymousEntry.getKey();
        }
      });
    }
    
    abstract Map<K, V> map();
    
    public boolean remove(Object paramObject)
    {
      if (contains(paramObject))
      {
        map().remove(paramObject);
        return true;
      }
      return false;
    }
    
    public boolean removeAll(Collection<?> paramCollection)
    {
      return super.removeAll((Collection)Preconditions.checkNotNull(paramCollection));
    }
    
    public int size()
    {
      return map().size();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Maps
 * JD-Core Version:    0.7.0.1
 */