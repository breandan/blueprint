package com.google.common.collect;

import java.util.Map.Entry;
import javax.annotation.Nullable;

public abstract class ForwardingMapEntry<K, V>
  extends ForwardingObject
  implements Map.Entry<K, V>
{
  protected abstract Map.Entry<K, V> delegate();
  
  public boolean equals(@Nullable Object paramObject)
  {
    return delegate().equals(paramObject);
  }
  
  public K getKey()
  {
    return delegate().getKey();
  }
  
  public V getValue()
  {
    return delegate().getValue();
  }
  
  public int hashCode()
  {
    return delegate().hashCode();
  }
  
  public V setValue(V paramV)
  {
    return delegate().setValue(paramV);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingMapEntry
 * JD-Core Version:    0.7.0.1
 */