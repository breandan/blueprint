package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;

class ComputingConcurrentHashMap<K, V>
  extends MapMakerInternalMap<K, V>
{
  private static final long serialVersionUID = 4L;
  final Function<? super K, ? extends V> computingFunction;
  
  ComputingConcurrentHashMap(MapMaker paramMapMaker, Function<? super K, ? extends V> paramFunction)
  {
    super(paramMapMaker);
    this.computingFunction = ((Function)Preconditions.checkNotNull(paramFunction));
  }
  
  MapMakerInternalMap.Segment<K, V> createSegment(int paramInt1, int paramInt2)
  {
    return new ComputingSegment(this, paramInt1, paramInt2);
  }
  
  V getOrCompute(K paramK)
    throws ExecutionException
  {
    int i = hash(Preconditions.checkNotNull(paramK));
    return segmentFor(i).getOrCompute(paramK, i, this.computingFunction);
  }
  
  ComputingSegment<K, V> segmentFor(int paramInt)
  {
    return (ComputingSegment)super.segmentFor(paramInt);
  }
  
  Object writeReplace()
  {
    return new ComputingSerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this, this.computingFunction);
  }
  
  private static final class ComputationExceptionReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V>
  {
    final Throwable t;
    
    ComputationExceptionReference(Throwable paramThrowable)
    {
      this.t = paramThrowable;
    }
    
    public void clear(MapMakerInternalMap.ValueReference<K, V> paramValueReference) {}
    
    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return this;
    }
    
    public V get()
    {
      return null;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry()
    {
      return null;
    }
    
    public boolean isComputingReference()
    {
      return false;
    }
    
    public V waitForValue()
      throws ExecutionException
    {
      throw new ExecutionException(this.t);
    }
  }
  
  private static final class ComputedReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V>
  {
    final V value;
    
    ComputedReference(@Nullable V paramV)
    {
      this.value = paramV;
    }
    
    public void clear(MapMakerInternalMap.ValueReference<K, V> paramValueReference) {}
    
    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return this;
    }
    
    public V get()
    {
      return this.value;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry()
    {
      return null;
    }
    
    public boolean isComputingReference()
    {
      return false;
    }
    
    public V waitForValue()
    {
      return get();
    }
  }
  
  static final class ComputingMapAdapter<K, V>
    extends ComputingConcurrentHashMap<K, V>
    implements Serializable
  {
    private static final long serialVersionUID;
    
    ComputingMapAdapter(MapMaker paramMapMaker, Function<? super K, ? extends V> paramFunction)
    {
      super(paramFunction);
    }
    
    public V get(Object paramObject)
    {
      Object localObject;
      try
      {
        localObject = getOrCompute(paramObject);
        if (localObject == null) {
          throw new NullPointerException(this.computingFunction + " returned null for key " + paramObject + ".");
        }
      }
      catch (ExecutionException localExecutionException)
      {
        Throwable localThrowable = localExecutionException.getCause();
        Throwables.propagateIfInstanceOf(localThrowable, ComputationException.class);
        throw new ComputationException(localThrowable);
      }
      return localObject;
    }
  }
  
  static final class ComputingSegment<K, V>
    extends MapMakerInternalMap.Segment<K, V>
  {
    ComputingSegment(MapMakerInternalMap<K, V> paramMapMakerInternalMap, int paramInt1, int paramInt2)
    {
      super(paramInt1, paramInt2);
    }
    
    /* Error */
    V compute(K paramK, int paramInt, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry, ComputingConcurrentHashMap.ComputingValueReference<K, V> paramComputingValueReference)
      throws ExecutionException
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore 5
      //   3: invokestatic 19	java/lang/System:nanoTime	()J
      //   6: pop2
      //   7: lconst_0
      //   8: lstore 8
      //   10: aload_3
      //   11: monitorenter
      //   12: aload 4
      //   14: aload_1
      //   15: iload_2
      //   16: invokevirtual 24	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference:compute	(Ljava/lang/Object;I)Ljava/lang/Object;
      //   19: astore 5
      //   21: invokestatic 19	java/lang/System:nanoTime	()J
      //   24: lstore 8
      //   26: aload_3
      //   27: monitorexit
      //   28: aload 5
      //   30: ifnull +26 -> 56
      //   33: aload_0
      //   34: aload_1
      //   35: iload_2
      //   36: aload 5
      //   38: iconst_1
      //   39: invokevirtual 28	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:put	(Ljava/lang/Object;ILjava/lang/Object;Z)Ljava/lang/Object;
      //   42: ifnull +14 -> 56
      //   45: aload_0
      //   46: aload_1
      //   47: iload_2
      //   48: aload 5
      //   50: getstatic 34	com/google/common/collect/MapMaker$RemovalCause:REPLACED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   53: invokevirtual 38	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:enqueueNotification	(Ljava/lang/Object;ILjava/lang/Object;Lcom/google/common/collect/MapMaker$RemovalCause;)V
      //   56: lload 8
      //   58: lconst_0
      //   59: lcmp
      //   60: ifne +7 -> 67
      //   63: invokestatic 19	java/lang/System:nanoTime	()J
      //   66: pop2
      //   67: aload 5
      //   69: ifnonnull +12 -> 81
      //   72: aload_0
      //   73: aload_1
      //   74: iload_2
      //   75: aload 4
      //   77: invokevirtual 42	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:clearValue	(Ljava/lang/Object;ILcom/google/common/collect/MapMakerInternalMap$ValueReference;)Z
      //   80: pop
      //   81: aload 5
      //   83: areturn
      //   84: astore 14
      //   86: aload_3
      //   87: monitorexit
      //   88: aload 14
      //   90: athrow
      //   91: astore 10
      //   93: lload 8
      //   95: lconst_0
      //   96: lcmp
      //   97: ifne +7 -> 104
      //   100: invokestatic 19	java/lang/System:nanoTime	()J
      //   103: pop2
      //   104: aload 5
      //   106: ifnonnull +12 -> 118
      //   109: aload_0
      //   110: aload_1
      //   111: iload_2
      //   112: aload 4
      //   114: invokevirtual 42	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:clearValue	(Ljava/lang/Object;ILcom/google/common/collect/MapMakerInternalMap$ValueReference;)Z
      //   117: pop
      //   118: aload 10
      //   120: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	121	0	this	ComputingSegment
      //   0	121	1	paramK	K
      //   0	121	2	paramInt	int
      //   0	121	3	paramReferenceEntry	MapMakerInternalMap.ReferenceEntry<K, V>
      //   0	121	4	paramComputingValueReference	ComputingConcurrentHashMap.ComputingValueReference<K, V>
      //   1	104	5	localObject1	Object
      //   8	86	8	l	long
      //   91	28	10	localObject2	Object
      //   84	5	14	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   12	28	84	finally
      //   86	88	84	finally
      //   10	12	91	finally
      //   33	56	91	finally
      //   88	91	91	finally
    }
    
    V getOrCompute(K paramK, int paramInt, Function<? super K, ? extends V> paramFunction)
      throws ExecutionException
    {
      Object localObject2;
      for (;;)
      {
        Object localObject6;
        Object localObject7;
        ComputingConcurrentHashMap.ComputingValueReference localComputingValueReference;
        try
        {
          localObject2 = getEntry(paramK, paramInt);
          if (localObject2 != null)
          {
            Object localObject3 = getLiveValue((MapMakerInternalMap.ReferenceEntry)localObject2);
            if (localObject3 != null)
            {
              recordRead((MapMakerInternalMap.ReferenceEntry)localObject2);
              return localObject3;
            }
          }
          if ((localObject2 == null) || (!((MapMakerInternalMap.ReferenceEntry)localObject2).getValueReference().isComputingReference()))
          {
            int i = 1;
            lock();
            int j;
            AtomicReferenceArray localAtomicReferenceArray;
            int k;
            MapMakerInternalMap.ReferenceEntry localReferenceEntry1;
            Object localObject8;
            try
            {
              preWriteCleanup();
              j = -1 + this.count;
              localAtomicReferenceArray = this.table;
              k = paramInt & -1 + localAtomicReferenceArray.length();
              localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(k);
              localObject2 = localReferenceEntry1;
              if (localObject2 != null)
              {
                localObject6 = ((MapMakerInternalMap.ReferenceEntry)localObject2).getKey();
                if ((((MapMakerInternalMap.ReferenceEntry)localObject2).getHash() != paramInt) || (localObject6 == null) || (!this.map.keyEquivalence.equivalent(paramK, localObject6))) {
                  break label406;
                }
                if (((MapMakerInternalMap.ReferenceEntry)localObject2).getValueReference().isComputingReference()) {
                  i = 0;
                }
              }
              else
              {
                localObject7 = null;
                if (i != 0)
                {
                  localComputingValueReference = new ComputingConcurrentHashMap.ComputingValueReference(paramFunction);
                  if (localObject2 != null) {
                    break label422;
                  }
                }
              }
            }
            finally {}
          }
        }
        finally
        {
          postReadCleanup();
        }
        try
        {
          localObject2 = newEntry(paramK, paramInt, localReferenceEntry1);
          ((MapMakerInternalMap.ReferenceEntry)localObject2).setValueReference(localComputingValueReference);
          localAtomicReferenceArray.set(k, localObject2);
          localObject7 = localComputingValueReference;
          unlock();
          postWriteCleanup();
          if (i == 0) {
            break;
          }
          localObject8 = compute(paramK, paramInt, (MapMakerInternalMap.ReferenceEntry)localObject2, localObject7);
          postReadCleanup();
          return localObject8;
        }
        finally
        {
          MapMakerInternalMap.ReferenceEntry localReferenceEntry2;
          boolean bool;
          continue;
        }
        Object localObject10 = ((MapMakerInternalMap.ReferenceEntry)localObject2).getValueReference().get();
        if (localObject10 == null)
        {
          enqueueNotification(localObject6, paramInt, localObject10, MapMaker.RemovalCause.COLLECTED);
          this.evictionQueue.remove(localObject2);
          this.expirationQueue.remove(localObject2);
          this.count = j;
          continue;
          unlock();
          postWriteCleanup();
          throw localObject4;
        }
        else if ((this.map.expires()) && (this.map.isExpired((MapMakerInternalMap.ReferenceEntry)localObject2)))
        {
          enqueueNotification(localObject6, paramInt, localObject10, MapMaker.RemovalCause.EXPIRED);
        }
        else
        {
          recordLockedRead((MapMakerInternalMap.ReferenceEntry)localObject2);
          unlock();
          postWriteCleanup();
          postReadCleanup();
          return localObject10;
          label406:
          localReferenceEntry2 = ((MapMakerInternalMap.ReferenceEntry)localObject2).getNext();
          localObject2 = localReferenceEntry2;
          continue;
          label422:
          ((MapMakerInternalMap.ReferenceEntry)localObject2).setValueReference(localComputingValueReference);
          localObject7 = localComputingValueReference;
        }
      }
      if (!Thread.holdsLock(localObject2)) {}
      for (bool = true;; bool = false)
      {
        Preconditions.checkState(bool, "Recursive computation");
        Object localObject9 = ((MapMakerInternalMap.ReferenceEntry)localObject2).getValueReference().waitForValue();
        if (localObject9 == null) {
          break;
        }
        recordRead((MapMakerInternalMap.ReferenceEntry)localObject2);
        postReadCleanup();
        return localObject9;
      }
    }
  }
  
  static final class ComputingSerializationProxy<K, V>
    extends MapMakerInternalMap.AbstractSerializationProxy<K, V>
  {
    private static final long serialVersionUID = 4L;
    final Function<? super K, ? extends V> computingFunction;
    
    ComputingSerializationProxy(MapMakerInternalMap.Strength paramStrength1, MapMakerInternalMap.Strength paramStrength2, Equivalence<Object> paramEquivalence1, Equivalence<Object> paramEquivalence2, long paramLong1, long paramLong2, int paramInt1, int paramInt2, MapMaker.RemovalListener<? super K, ? super V> paramRemovalListener, ConcurrentMap<K, V> paramConcurrentMap, Function<? super K, ? extends V> paramFunction)
    {
      super(paramStrength2, paramEquivalence1, paramEquivalence2, paramLong1, paramLong2, paramInt1, paramInt2, paramRemovalListener, paramConcurrentMap);
      this.computingFunction = paramFunction;
    }
    
    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      paramObjectInputStream.defaultReadObject();
      this.delegate = readMapMaker(paramObjectInputStream).makeComputingMap(this.computingFunction);
      readEntries(paramObjectInputStream);
    }
    
    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.defaultWriteObject();
      writeMapTo(paramObjectOutputStream);
    }
    
    Object readResolve()
    {
      return this.delegate;
    }
  }
  
  private static final class ComputingValueReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V>
  {
    volatile MapMakerInternalMap.ValueReference<K, V> computedReference = MapMakerInternalMap.unset();
    final Function<? super K, ? extends V> computingFunction;
    
    public ComputingValueReference(Function<? super K, ? extends V> paramFunction)
    {
      this.computingFunction = paramFunction;
    }
    
    public void clear(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
    {
      setValueReference(paramValueReference);
    }
    
    V compute(K paramK, int paramInt)
      throws ExecutionException
    {
      try
      {
        Object localObject = this.computingFunction.apply(paramK);
        setValueReference(new ComputingConcurrentHashMap.ComputedReference(localObject));
        return localObject;
      }
      catch (Throwable localThrowable)
      {
        setValueReference(new ComputingConcurrentHashMap.ComputationExceptionReference(localThrowable));
        throw new ExecutionException(localThrowable);
      }
    }
    
    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return this;
    }
    
    public V get()
    {
      return null;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry()
    {
      return null;
    }
    
    public boolean isComputingReference()
    {
      return true;
    }
    
    void setValueReference(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
    {
      try
      {
        if (this.computedReference == MapMakerInternalMap.UNSET)
        {
          this.computedReference = paramValueReference;
          notifyAll();
        }
        return;
      }
      finally {}
    }
    
    /* Error */
    public V waitForValue()
      throws ExecutionException
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 26	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference:computedReference	Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
      //   4: getstatic 67	com/google/common/collect/MapMakerInternalMap:UNSET	Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
      //   7: if_acmpne +51 -> 58
      //   10: iconst_0
      //   11: istore_1
      //   12: aload_0
      //   13: monitorenter
      //   14: aload_0
      //   15: getfield 26	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference:computedReference	Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
      //   18: astore 4
      //   20: getstatic 67	com/google/common/collect/MapMakerInternalMap:UNSET	Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
      //   23: astore 5
      //   25: aload 4
      //   27: aload 5
      //   29: if_acmpne +17 -> 46
      //   32: aload_0
      //   33: invokevirtual 76	java/lang/Object:wait	()V
      //   36: goto -22 -> 14
      //   39: astore 6
      //   41: iconst_1
      //   42: istore_1
      //   43: goto -29 -> 14
      //   46: aload_0
      //   47: monitorexit
      //   48: iload_1
      //   49: ifeq +9 -> 58
      //   52: invokestatic 82	java/lang/Thread:currentThread	()Ljava/lang/Thread;
      //   55: invokevirtual 85	java/lang/Thread:interrupt	()V
      //   58: aload_0
      //   59: getfield 26	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference:computedReference	Lcom/google/common/collect/MapMakerInternalMap$ValueReference;
      //   62: invokeinterface 87 1 0
      //   67: areturn
      //   68: astore_3
      //   69: aload_0
      //   70: monitorexit
      //   71: aload_3
      //   72: athrow
      //   73: astore_2
      //   74: iload_1
      //   75: ifeq +9 -> 84
      //   78: invokestatic 82	java/lang/Thread:currentThread	()Ljava/lang/Thread;
      //   81: invokevirtual 85	java/lang/Thread:interrupt	()V
      //   84: aload_2
      //   85: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	86	0	this	ComputingValueReference
      //   11	64	1	i	int
      //   73	12	2	localObject1	Object
      //   68	4	3	localObject2	Object
      //   18	8	4	localValueReference1	MapMakerInternalMap.ValueReference
      //   23	5	5	localValueReference2	MapMakerInternalMap.ValueReference
      //   39	1	6	localInterruptedException	java.lang.InterruptedException
      // Exception table:
      //   from	to	target	type
      //   32	36	39	java/lang/InterruptedException
      //   14	25	68	finally
      //   32	36	68	finally
      //   46	48	68	finally
      //   69	71	68	finally
      //   12	14	73	finally
      //   71	73	73	finally
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.collect.ComputingConcurrentHashMap
 * JD-Core Version:    0.7.0.1
 */