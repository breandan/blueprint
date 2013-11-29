package com.google.common.collect;

import java.util.Map;
import javax.annotation.Nullable;

public abstract class ImmutableTable<R, C, V>
  implements Table<R, C, V>
{
  public abstract ImmutableSet<Table.Cell<R, C, V>> cellSet();
  
  public boolean equals(@Nullable Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if ((paramObject instanceof Table))
    {
      Table localTable = (Table)paramObject;
      return cellSet().equals(localTable.cellSet());
    }
    return false;
  }
  
  public int hashCode()
  {
    return cellSet().hashCode();
  }
  
  public abstract ImmutableMap<R, Map<C, V>> rowMap();
  
  public String toString()
  {
    return rowMap().toString();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableTable
 * JD-Core Version:    0.7.0.1
 */