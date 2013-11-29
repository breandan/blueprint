package com.google.common.base;

import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

public abstract class Optional<T>
  implements Serializable
{
  private static final long serialVersionUID;
  
  public static <T> Optional<T> absent()
  {
    return Absent.INSTANCE;
  }
  
  public static <T> Optional<T> of(T paramT)
  {
    return new Present(Preconditions.checkNotNull(paramT));
  }
  
  public abstract T get();
  
  public abstract boolean isPresent();
  
  private static final class Absent
    extends Optional<Object>
  {
    private static final Absent INSTANCE = new Absent();
    private static final long serialVersionUID;
    
    private Absent()
    {
      super();
    }
    
    private Object readResolve()
    {
      return INSTANCE;
    }
    
    public boolean equals(@Nullable Object paramObject)
    {
      return paramObject == this;
    }
    
    public Object get()
    {
      throw new IllegalStateException("value is absent");
    }
    
    public int hashCode()
    {
      return 1502476572;
    }
    
    public boolean isPresent()
    {
      return false;
    }
    
    public String toString()
    {
      return "Optional.absent()";
    }
  }
  
  private static final class Present<T>
    extends Optional<T>
  {
    private static final long serialVersionUID;
    private final T reference;
    
    Present(T paramT)
    {
      super();
      this.reference = paramT;
    }
    
    public boolean equals(@Nullable Object paramObject)
    {
      if ((paramObject instanceof Present))
      {
        Present localPresent = (Present)paramObject;
        return this.reference.equals(localPresent.reference);
      }
      return false;
    }
    
    public T get()
    {
      return this.reference;
    }
    
    public int hashCode()
    {
      return 1502476572 + this.reference.hashCode();
    }
    
    public boolean isPresent()
    {
      return true;
    }
    
    public String toString()
    {
      return "Optional.of(" + this.reference + ")";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.base.Optional
 * JD-Core Version:    0.7.0.1
 */