package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

public abstract class ImmutableList<E>
  extends ImmutableCollection<E>
  implements List<E>, RandomAccess
{
  public static <E> Builder<E> builder()
  {
    return new Builder();
  }
  
  private static Object checkElementNotNull(Object paramObject, int paramInt)
  {
    if (paramObject == null) {
      throw new NullPointerException("at index " + paramInt);
    }
    return paramObject;
  }
  
  private static <E> ImmutableList<E> construct(Object... paramVarArgs)
  {
    for (int i = 0; i < paramVarArgs.length; i++) {
      checkElementNotNull(paramVarArgs[i], i);
    }
    return new RegularImmutableList(paramVarArgs);
  }
  
  private static <E> ImmutableList<E> copyFromCollection(Collection<? extends E> paramCollection)
  {
    Object[] arrayOfObject = paramCollection.toArray();
    switch (arrayOfObject.length)
    {
    default: 
      return construct(arrayOfObject);
    case 0: 
      return of();
    }
    return new SingletonImmutableList(arrayOfObject[0]);
  }
  
  public static <E> ImmutableList<E> copyOf(Iterable<? extends E> paramIterable)
  {
    Preconditions.checkNotNull(paramIterable);
    if ((paramIterable instanceof Collection)) {
      return copyOf(Collections2.cast(paramIterable));
    }
    return copyOf(paramIterable.iterator());
  }
  
  public static <E> ImmutableList<E> copyOf(Collection<? extends E> paramCollection)
  {
    if ((paramCollection instanceof ImmutableCollection))
    {
      ImmutableList localImmutableList = ((ImmutableCollection)paramCollection).asList();
      if (localImmutableList.isPartialView()) {
        localImmutableList = copyFromCollection(localImmutableList);
      }
      return localImmutableList;
    }
    return copyFromCollection(paramCollection);
  }
  
  public static <E> ImmutableList<E> copyOf(Iterator<? extends E> paramIterator)
  {
    return copyFromCollection(Lists.newArrayList(paramIterator));
  }
  
  public static <E> ImmutableList<E> copyOf(E[] paramArrayOfE)
  {
    switch (paramArrayOfE.length)
    {
    default: 
      return construct((Object[])paramArrayOfE.clone());
    case 0: 
      return of();
    }
    return new SingletonImmutableList(paramArrayOfE[0]);
  }
  
  public static <E> ImmutableList<E> of()
  {
    return EmptyImmutableList.INSTANCE;
  }
  
  public static <E> ImmutableList<E> of(E paramE)
  {
    return new SingletonImmutableList(paramE);
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2)
  {
    return construct(new Object[] { paramE1, paramE2 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3, paramE4 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5, paramE6 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6, E paramE7)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5, paramE6, paramE7 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6, E paramE7, E paramE8)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5, paramE6, paramE7, paramE8 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6, E paramE7, E paramE8, E paramE9)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5, paramE6, paramE7, paramE8, paramE9 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6, E paramE7, E paramE8, E paramE9, E paramE10)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5, paramE6, paramE7, paramE8, paramE9, paramE10 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6, E paramE7, E paramE8, E paramE9, E paramE10, E paramE11)
  {
    return construct(new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5, paramE6, paramE7, paramE8, paramE9, paramE10, paramE11 });
  }
  
  public static <E> ImmutableList<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6, E paramE7, E paramE8, E paramE9, E paramE10, E paramE11, E paramE12, E... paramVarArgs)
  {
    Object[] arrayOfObject = new Object[12 + paramVarArgs.length];
    arrayOfObject[0] = paramE1;
    arrayOfObject[1] = paramE2;
    arrayOfObject[2] = paramE3;
    arrayOfObject[3] = paramE4;
    arrayOfObject[4] = paramE5;
    arrayOfObject[5] = paramE6;
    arrayOfObject[6] = paramE7;
    arrayOfObject[7] = paramE8;
    arrayOfObject[8] = paramE9;
    arrayOfObject[9] = paramE10;
    arrayOfObject[10] = paramE11;
    arrayOfObject[11] = paramE12;
    System.arraycopy(paramVarArgs, 0, arrayOfObject, 12, paramVarArgs.length);
    return construct(arrayOfObject);
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Use SerializedForm");
  }
  
  public final void add(int paramInt, E paramE)
  {
    throw new UnsupportedOperationException();
  }
  
  public final boolean addAll(int paramInt, Collection<? extends E> paramCollection)
  {
    throw new UnsupportedOperationException();
  }
  
  public ImmutableList<E> asList()
  {
    return this;
  }
  
  public boolean equals(Object paramObject)
  {
    return Lists.equalsImpl(this, paramObject);
  }
  
  public int hashCode()
  {
    return Lists.hashCodeImpl(this);
  }
  
  public UnmodifiableIterator<E> iterator()
  {
    return listIterator();
  }
  
  public UnmodifiableListIterator<E> listIterator()
  {
    return listIterator(0);
  }
  
  public abstract UnmodifiableListIterator<E> listIterator(int paramInt);
  
  public final E remove(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public final E set(int paramInt, E paramE)
  {
    throw new UnsupportedOperationException();
  }
  
  public abstract ImmutableList<E> subList(int paramInt1, int paramInt2);
  
  Object writeReplace()
  {
    return new SerializedForm(toArray());
  }
  
  public static final class Builder<E>
    extends ImmutableCollection.Builder<E>
  {
    private final ArrayList<E> contents = Lists.newArrayList();
    
    public Builder<E> add(E paramE)
    {
      this.contents.add(Preconditions.checkNotNull(paramE));
      return this;
    }
    
    public Builder<E> add(E... paramVarArgs)
    {
      this.contents.ensureCapacity(this.contents.size() + paramVarArgs.length);
      super.add(paramVarArgs);
      return this;
    }
    
    public Builder<E> addAll(Iterable<? extends E> paramIterable)
    {
      if ((paramIterable instanceof Collection))
      {
        Collection localCollection = (Collection)paramIterable;
        this.contents.ensureCapacity(this.contents.size() + localCollection.size());
      }
      super.addAll(paramIterable);
      return this;
    }
    
    public ImmutableList<E> build()
    {
      return ImmutableList.copyOf(this.contents);
    }
  }
  
  private static class SerializedForm
    implements Serializable
  {
    private static final long serialVersionUID;
    final Object[] elements;
    
    SerializedForm(Object[] paramArrayOfObject)
    {
      this.elements = paramArrayOfObject;
    }
    
    Object readResolve()
    {
      return ImmutableList.copyOf(this.elements);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableList
 * JD-Core Version:    0.7.0.1
 */