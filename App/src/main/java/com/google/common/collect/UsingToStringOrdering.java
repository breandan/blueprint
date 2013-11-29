package com.google.common.collect;

import java.io.Serializable;

final class UsingToStringOrdering
  extends Ordering<Object>
  implements Serializable
{
  static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();
  private static final long serialVersionUID;
  
  private Object readResolve()
  {
    return INSTANCE;
  }
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    return paramObject1.toString().compareTo(paramObject2.toString());
  }
  
  public String toString()
  {
    return "Ordering.usingToString()";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.UsingToStringOrdering
 * JD-Core Version:    0.7.0.1
 */