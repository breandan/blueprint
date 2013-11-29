package com.google.common.collect;

import java.util.Map.Entry;
import javax.annotation.Nullable;

public abstract class ImmutableBiMap<K, V>
  extends ImmutableMap<K, V>
  implements BiMap<K, V>
{
  private static final ImmutableBiMap<Object, Object> EMPTY_IMMUTABLE_BIMAP = new EmptyBiMap();
  
  public static <K, V> ImmutableBiMap<K, V> of()
  {
    return EMPTY_IMMUTABLE_BIMAP;
  }
  
  public boolean containsKey(@Nullable Object paramObject)
  {
    return delegate().containsKey(paramObject);
  }
  
  public boolean containsValue(@Nullable Object paramObject)
  {
    return inverse().containsKey(paramObject);
  }
  
  abstract ImmutableMap<K, V> delegate();
  
  public ImmutableSet<Map.Entry<K, V>> entrySet()
  {
    return delegate().entrySet();
  }
  
  public boolean equals(@Nullable Object paramObject)
  {
    return (paramObject == this) || (delegate().equals(paramObject));
  }
  
  public V get(@Nullable Object paramObject)
  {
    return delegate().get(paramObject);
  }
  
  public int hashCode()
  {
    return delegate().hashCode();
  }
  
  public abstract ImmutableBiMap<V, K> inverse();
  
  public boolean isEmpty()
  {
    return delegate().isEmpty();
  }
  
  public ImmutableSet<K> keySet()
  {
    return delegate().keySet();
  }
  
  public int size()
  {
    return delegate().size();
  }
  
  public String toString()
  {
    return delegate().toString();
  }
  
  public ImmutableSet<V> values()
  {
    return inverse().keySet();
  }
  
  Object writeReplace()
  {
    return new SerializedForm(this);
  }
  
  public static final class Builder<K, V>
    extends ImmutableMap.Builder<K, V>
  {
    public ImmutableBiMap<K, V> build()
    {
      ImmutableMap localImmutableMap = super.build();
      if (localImmutableMap.isEmpty()) {
        return ImmutableBiMap.of();
      }
      return new RegularImmutableBiMap(localImmutableMap);
    }
    
    public Builder<K, V> put(K paramK, V paramV)
    {
      super.put(paramK, paramV);
      return this;
    }
  }
  
  static class EmptyBiMap
    extends ImmutableBiMap<Object, Object>
  {
    ImmutableMap<Object, Object> delegate()
    {
      return ImmutableMap.of();
    }
    
    public ImmutableBiMap<Object, Object> inverse()
    {
      return this;
    }
    
    boolean isPartialView()
    {
      return false;
    }
    
    Object readResolve()
    {
      return ImmutableBiMap.EMPTY_IMMUTABLE_BIMAP;
    }
  }
  
  private static class SerializedForm
    extends ImmutableMap.SerializedForm
  {
    private static final long serialVersionUID;
    
    SerializedForm(ImmutableBiMap<?, ?> paramImmutableBiMap)
    {
      super();
    }
    
    Object readResolve()
    {
      return createMap(new ImmutableBiMap.Builder());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableBiMap
 * JD-Core Version:    0.7.0.1
 */