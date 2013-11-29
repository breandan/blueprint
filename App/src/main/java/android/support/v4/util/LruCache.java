package android.support.v4.util;

import java.util.LinkedHashMap;

public class LruCache<K, V>
{
  private int evictionCount;
  private int hitCount;
  private final LinkedHashMap<K, V> map;
  private int maxSize;
  private int missCount;
  private int putCount;
  private int size;
  
  public LruCache(int paramInt)
  {
    if (paramInt <= 0) {
      throw new IllegalArgumentException("maxSize <= 0");
    }
    this.maxSize = paramInt;
    this.map = new LinkedHashMap(0, 0.75F, true);
  }
  
  private int safeSizeOf(K paramK, V paramV)
  {
    int i = sizeOf(paramK, paramV);
    if (i < 0) {
      throw new IllegalStateException("Negative size: " + paramK + "=" + paramV);
    }
    return i;
  }
  
  protected void entryRemoved(boolean paramBoolean, K paramK, V paramV1, V paramV2) {}
  
  public final V put(K paramK, V paramV)
  {
    if ((paramK == null) || (paramV == null)) {
      throw new NullPointerException("key == null || value == null");
    }
    try
    {
      this.putCount = (1 + this.putCount);
      this.size += safeSizeOf(paramK, paramV);
      Object localObject2 = this.map.put(paramK, paramV);
      if (localObject2 != null) {
        this.size -= safeSizeOf(paramK, localObject2);
      }
      if (localObject2 != null) {
        entryRemoved(false, paramK, localObject2, paramV);
      }
      trimToSize(this.maxSize);
      return localObject2;
    }
    finally {}
  }
  
  public final V remove(K paramK)
  {
    if (paramK == null) {
      throw new NullPointerException("key == null");
    }
    try
    {
      Object localObject2 = this.map.remove(paramK);
      if (localObject2 != null) {
        this.size -= safeSizeOf(paramK, localObject2);
      }
      if (localObject2 != null) {
        entryRemoved(false, paramK, localObject2, null);
      }
      return localObject2;
    }
    finally {}
  }
  
  protected int sizeOf(K paramK, V paramV)
  {
    return 1;
  }
  
  public final String toString()
  {
    try
    {
      int i = this.hitCount + this.missCount;
      int j = 0;
      if (i != 0) {
        j = 100 * this.hitCount / i;
      }
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = Integer.valueOf(this.maxSize);
      arrayOfObject[1] = Integer.valueOf(this.hitCount);
      arrayOfObject[2] = Integer.valueOf(this.missCount);
      arrayOfObject[3] = Integer.valueOf(j);
      String str = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", arrayOfObject);
      return str;
    }
    finally {}
  }
  
  /* Error */
  public void trimToSize(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 76	android/support/v4/util/LruCache:size	I
    //   6: iflt +20 -> 26
    //   9: aload_0
    //   10: getfield 37	android/support/v4/util/LruCache:map	Ljava/util/LinkedHashMap;
    //   13: invokevirtual 113	java/util/LinkedHashMap:isEmpty	()Z
    //   16: ifeq +48 -> 64
    //   19: aload_0
    //   20: getfield 76	android/support/v4/util/LruCache:size	I
    //   23: ifeq +41 -> 64
    //   26: new 44	java/lang/IllegalStateException
    //   29: dup
    //   30: new 46	java/lang/StringBuilder
    //   33: dup
    //   34: invokespecial 47	java/lang/StringBuilder:<init>	()V
    //   37: aload_0
    //   38: invokevirtual 117	java/lang/Object:getClass	()Ljava/lang/Class;
    //   41: invokevirtual 122	java/lang/Class:getName	()Ljava/lang/String;
    //   44: invokevirtual 53	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: ldc 124
    //   49: invokevirtual 53	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: invokevirtual 62	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   55: invokespecial 63	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   58: athrow
    //   59: astore_2
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_2
    //   63: athrow
    //   64: aload_0
    //   65: getfield 76	android/support/v4/util/LruCache:size	I
    //   68: iload_1
    //   69: if_icmple +13 -> 82
    //   72: aload_0
    //   73: getfield 37	android/support/v4/util/LruCache:map	Ljava/util/LinkedHashMap;
    //   76: invokevirtual 113	java/util/LinkedHashMap:isEmpty	()Z
    //   79: ifeq +6 -> 85
    //   82: aload_0
    //   83: monitorexit
    //   84: return
    //   85: aload_0
    //   86: getfield 37	android/support/v4/util/LruCache:map	Ljava/util/LinkedHashMap;
    //   89: invokevirtual 128	java/util/LinkedHashMap:entrySet	()Ljava/util/Set;
    //   92: invokeinterface 134 1 0
    //   97: invokeinterface 140 1 0
    //   102: checkcast 142	java/util/Map$Entry
    //   105: astore_3
    //   106: aload_3
    //   107: invokeinterface 145 1 0
    //   112: astore 4
    //   114: aload_3
    //   115: invokeinterface 148 1 0
    //   120: astore 5
    //   122: aload_0
    //   123: getfield 37	android/support/v4/util/LruCache:map	Ljava/util/LinkedHashMap;
    //   126: aload 4
    //   128: invokevirtual 91	java/util/LinkedHashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   131: pop
    //   132: aload_0
    //   133: aload_0
    //   134: getfield 76	android/support/v4/util/LruCache:size	I
    //   137: aload_0
    //   138: aload 4
    //   140: aload 5
    //   142: invokespecial 78	android/support/v4/util/LruCache:safeSizeOf	(Ljava/lang/Object;Ljava/lang/Object;)I
    //   145: isub
    //   146: putfield 76	android/support/v4/util/LruCache:size	I
    //   149: aload_0
    //   150: iconst_1
    //   151: aload_0
    //   152: getfield 150	android/support/v4/util/LruCache:evictionCount	I
    //   155: iadd
    //   156: putfield 150	android/support/v4/util/LruCache:evictionCount	I
    //   159: aload_0
    //   160: monitorexit
    //   161: aload_0
    //   162: iconst_1
    //   163: aload 4
    //   165: aload 5
    //   167: aconst_null
    //   168: invokevirtual 82	android/support/v4/util/LruCache:entryRemoved	(ZLjava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
    //   171: goto -171 -> 0
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	174	0	this	LruCache
    //   0	174	1	paramInt	int
    //   59	4	2	localObject1	Object
    //   105	10	3	localEntry	java.util.Map.Entry
    //   112	52	4	localObject2	Object
    //   120	46	5	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   2	26	59	finally
    //   26	59	59	finally
    //   60	62	59	finally
    //   64	82	59	finally
    //   82	84	59	finally
    //   85	161	59	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.util.LruCache
 * JD-Core Version:    0.7.0.1
 */