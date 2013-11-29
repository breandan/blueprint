package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

public final class HashMultimap<K, V>
  extends AbstractSetMultimap<K, V>
{
  private static final long serialVersionUID;
  transient int expectedValuesPerKey = 8;
  
  private HashMultimap()
  {
    super(new HashMap());
  }
  
  public static <K, V> HashMultimap<K, V> create()
  {
    return new HashMultimap();
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    this.expectedValuesPerKey = paramObjectInputStream.readInt();
    int i = Serialization.readCount(paramObjectInputStream);
    setMap(Maps.newHashMapWithExpectedSize(i));
    Serialization.populateMultimap(this, paramObjectInputStream, i);
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeInt(this.expectedValuesPerKey);
    Serialization.writeMultimap(this, paramObjectOutputStream);
  }
  
  Set<V> createCollection()
  {
    return Sets.newHashSetWithExpectedSize(this.expectedValuesPerKey);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.HashMultimap
 * JD-Core Version:    0.7.0.1
 */