package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class Serialization
{
  static <K, V> void populateMultimap(Multimap<K, V> paramMultimap, ObjectInputStream paramObjectInputStream, int paramInt)
    throws IOException, ClassNotFoundException
  {
    for (int i = 0; i < paramInt; i++)
    {
      Collection localCollection = paramMultimap.get(paramObjectInputStream.readObject());
      int j = paramObjectInputStream.readInt();
      for (int k = 0; k < j; k++) {
        localCollection.add(paramObjectInputStream.readObject());
      }
    }
  }
  
  static <E> void populateMultiset(Multiset<E> paramMultiset, ObjectInputStream paramObjectInputStream, int paramInt)
    throws IOException, ClassNotFoundException
  {
    for (int i = 0; i < paramInt; i++) {
      paramMultiset.add(paramObjectInputStream.readObject(), paramObjectInputStream.readInt());
    }
  }
  
  static int readCount(ObjectInputStream paramObjectInputStream)
    throws IOException
  {
    return paramObjectInputStream.readInt();
  }
  
  static <K, V> void writeMultimap(Multimap<K, V> paramMultimap, ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeInt(paramMultimap.asMap().size());
    Iterator localIterator1 = paramMultimap.asMap().entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      paramObjectOutputStream.writeObject(localEntry.getKey());
      paramObjectOutputStream.writeInt(((Collection)localEntry.getValue()).size());
      Iterator localIterator2 = ((Collection)localEntry.getValue()).iterator();
      while (localIterator2.hasNext()) {
        paramObjectOutputStream.writeObject(localIterator2.next());
      }
    }
  }
  
  static <E> void writeMultiset(Multiset<E> paramMultiset, ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeInt(paramMultiset.entrySet().size());
    Iterator localIterator = paramMultiset.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
      paramObjectOutputStream.writeObject(localEntry.getElement());
      paramObjectOutputStream.writeInt(localEntry.getCount());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.Serialization
 * JD-Core Version:    0.7.0.1
 */