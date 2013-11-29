package com.google.common.collect;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

public final class Multimaps
{
  static abstract class Entries<K, V>
    extends AbstractCollection<Map.Entry<K, V>>
  {
    public void clear()
    {
      multimap().clear();
    }
    
    public boolean contains(@Nullable Object paramObject)
    {
      if ((paramObject instanceof Map.Entry))
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        return multimap().containsEntry(localEntry.getKey(), localEntry.getValue());
      }
      return false;
    }
    
    abstract Multimap<K, V> multimap();
    
    public boolean remove(@Nullable Object paramObject)
    {
      if ((paramObject instanceof Map.Entry))
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        return multimap().remove(localEntry.getKey(), localEntry.getValue());
      }
      return false;
    }
    
    public int size()
    {
      return multimap().size();
    }
  }
  
  static abstract class EntrySet<K, V>
    extends Multimaps.Entries<K, V>
    implements Set<Map.Entry<K, V>>
  {
    public boolean equals(@Nullable Object paramObject)
    {
      return Sets.equalsImpl(this, paramObject);
    }
    
    public int hashCode()
    {
      return Sets.hashCodeImpl(this);
    }
  }
  
  static abstract class Values<K, V>
    extends AbstractCollection<V>
  {
    public void clear()
    {
      multimap().clear();
    }
    
    public boolean contains(@Nullable Object paramObject)
    {
      return multimap().containsValue(paramObject);
    }
    
    public Iterator<V> iterator()
    {
      new Iterator()
      {
        public boolean hasNext()
        {
          return this.val$backingIterator.hasNext();
        }
        
        public V next()
        {
          return ((Map.Entry)this.val$backingIterator.next()).getValue();
        }
        
        public void remove()
        {
          this.val$backingIterator.remove();
        }
      };
    }
    
    abstract Multimap<K, V> multimap();
    
    public int size()
    {
      return multimap().size();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Multimaps
 * JD-Core Version:    0.7.0.1
 */