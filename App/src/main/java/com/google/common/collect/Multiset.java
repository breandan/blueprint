package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

public abstract interface Multiset<E>
  extends Collection<E>
{
  public abstract int add(@Nullable E paramE, int paramInt);
  
  public abstract boolean add(E paramE);
  
  public abstract boolean contains(@Nullable Object paramObject);
  
  public abstract boolean containsAll(Collection<?> paramCollection);
  
  public abstract int count(@Nullable Object paramObject);
  
  public abstract Set<E> elementSet();
  
  public abstract Set<Entry<E>> entrySet();
  
  public abstract int remove(@Nullable Object paramObject, int paramInt);
  
  public abstract boolean remove(@Nullable Object paramObject);
  
  public static abstract interface Entry<E>
  {
    public abstract int getCount();
    
    public abstract E getElement();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Multiset
 * JD-Core Version:    0.7.0.1
 */