package com.google.common.collect;

import java.util.Map;
import java.util.Set;

public abstract interface BiMap<K, V>
  extends Map<K, V>
{
  public abstract Set<V> values();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.BiMap
 * JD-Core Version:    0.7.0.1
 */