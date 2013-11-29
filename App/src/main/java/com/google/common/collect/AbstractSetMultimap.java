package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

abstract class AbstractSetMultimap<K, V>
  extends AbstractMultimap<K, V>
  implements SetMultimap<K, V>
{
  private static final long serialVersionUID = 7431625294878419160L;
  
  protected AbstractSetMultimap(Map<K, Collection<V>> paramMap)
  {
    super(paramMap);
  }
  
  public Map<K, Collection<V>> asMap()
  {
    return super.asMap();
  }
  
  abstract Set<V> createCollection();
  
  public Set<Map.Entry<K, V>> entries()
  {
    return (Set)super.entries();
  }
  
  public boolean equals(@Nullable Object paramObject)
  {
    return super.equals(paramObject);
  }
  
  public Set<V> get(@Nullable K paramK)
  {
    return (Set)super.get(paramK);
  }
  
  public boolean put(K paramK, V paramV)
  {
    return super.put(paramK, paramV);
  }
  
  public Set<V> removeAll(@Nullable Object paramObject)
  {
    return (Set)super.removeAll(paramObject);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractSetMultimap
 * JD-Core Version:    0.7.0.1
 */