package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public abstract interface Multimap<K, V>
{
  public abstract Map<K, Collection<V>> asMap();
  
  public abstract void clear();
  
  public abstract boolean containsEntry(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  public abstract boolean containsValue(@Nullable Object paramObject);
  
  public abstract Collection<Map.Entry<K, V>> entries();
  
  public abstract Collection<V> get(@Nullable K paramK);
  
  public abstract boolean put(@Nullable K paramK, @Nullable V paramV);
  
  public abstract boolean remove(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  public abstract Collection<V> removeAll(@Nullable Object paramObject);
  
  public abstract int size();
  
  public abstract Collection<V> values();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Multimap
 * JD-Core Version:    0.7.0.1
 */