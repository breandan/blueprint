package com.google.android.search.core.sdch;

import android.content.Context;
import android.util.Log;
import com.google.android.shared.util.Clock;
import com.google.android.speech.callback.SimpleCallback;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class SdchDictionaryCache
{
  private List<String> mCacheEntries;
  private final Clock mClock;
  private final Context mContext;
  private final Executor mExecutor;
  
  public SdchDictionaryCache(Context paramContext, Executor paramExecutor, Clock paramClock)
  {
    this.mContext = paramContext;
    this.mExecutor = paramExecutor;
    this.mClock = paramClock;
    this.mCacheEntries = null;
  }
  
  private void deleteCacheEntry(String paramString)
  {
    File localFile = new File(new File(this.mContext.getCacheDir(), "sdch_cache"), paramString);
    if (!localFile.delete()) {
      Log.e("Velvet.SdchDictionaryCache", "Unable to delete cache entry: " + localFile.getAbsolutePath());
    }
    this.mCacheEntries.remove(paramString);
  }
  
  private File generateCacheFileName()
  {
    File localFile = new File(this.mContext.getCacheDir(), "sdch_cache");
    if (!localFile.exists()) {
      localFile.mkdirs();
    }
    return new File(localFile, String.valueOf(this.mClock.currentTimeMillis()));
  }
  
  public void addCacheEntryForTesting(String paramString)
  {
    if (this.mCacheEntries == null) {
      this.mCacheEntries = new ArrayList();
    }
    this.mCacheEntries.add(paramString);
  }
  
  protected List<String> getEvictionList()
  {
    ArrayList localArrayList = Lists.newArrayListWithExpectedSize(1);
    if (this.mCacheEntries.size() < 5) {
      return localArrayList;
    }
    long l1 = 9223372036854775807L;
    Object localObject = null;
    Iterator localIterator = this.mCacheEntries.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      try
      {
        long l2 = Long.parseLong(str);
        if (l2 < l1)
        {
          l1 = l2;
          localObject = str;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        Log.e("Velvet.SdchDictionaryCache", "Bad cache file name: " + localNumberFormatException);
        localArrayList.add(str);
      }
    }
    localArrayList.add(localObject);
    return localArrayList;
  }
  
  void loadAll(final SimpleCallback<List<SdchDictionary>> paramSimpleCallback)
  {
    this.mExecutor.execute(new Runnable()
    {
      public void run()
      {
        SdchDictionaryCache.this.loadAllInternal(paramSimpleCallback);
      }
    });
  }
  
  /* Error */
  void loadAllInternal(SimpleCallback<List<SdchDictionary>> paramSimpleCallback)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new 30	java/io/File
    //   5: dup
    //   6: aload_0
    //   7: getfield 20	com/google/android/search/core/sdch/SdchDictionaryCache:mContext	Landroid/content/Context;
    //   10: invokevirtual 36	android/content/Context:getCacheDir	()Ljava/io/File;
    //   13: ldc 38
    //   15: invokespecial 41	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   18: astore_2
    //   19: aload_2
    //   20: invokevirtual 79	java/io/File:exists	()Z
    //   23: ifne +23 -> 46
    //   26: aload_0
    //   27: iconst_5
    //   28: invokestatic 160	com/google/common/collect/Lists:newArrayListWithCapacity	(I)Ljava/util/ArrayList;
    //   31: putfield 26	com/google/android/search/core/sdch/SdchDictionaryCache:mCacheEntries	Ljava/util/List;
    //   34: aload_1
    //   35: invokestatic 165	java/util/Collections:emptyList	()Ljava/util/List;
    //   38: invokeinterface 171 2 0
    //   43: aload_0
    //   44: monitorexit
    //   45: return
    //   46: aload_2
    //   47: invokevirtual 175	java/io/File:listFiles	()[Ljava/io/File;
    //   50: astore 4
    //   52: aload 4
    //   54: arraylength
    //   55: invokestatic 160	com/google/common/collect/Lists:newArrayListWithCapacity	(I)Ljava/util/ArrayList;
    //   58: astore 5
    //   60: aload 4
    //   62: arraylength
    //   63: invokestatic 160	com/google/common/collect/Lists:newArrayListWithCapacity	(I)Ljava/util/ArrayList;
    //   66: astore 6
    //   68: iconst_0
    //   69: istore 7
    //   71: iload 7
    //   73: aload 4
    //   75: arraylength
    //   76: if_icmpge +103 -> 179
    //   79: aload 4
    //   81: iload 7
    //   83: aaload
    //   84: astore 8
    //   86: aconst_null
    //   87: astore 9
    //   89: new 177	java/io/BufferedInputStream
    //   92: dup
    //   93: new 179	java/io/FileInputStream
    //   96: dup
    //   97: aload 8
    //   99: invokespecial 182	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   102: invokespecial 185	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   105: astore 10
    //   107: aload 5
    //   109: aload 10
    //   111: invokestatic 191	com/google/android/search/core/sdch/SdchDictionary:parseFrom	(Ljava/io/InputStream;)Lcom/google/android/search/core/sdch/SdchDictionary;
    //   114: invokeinterface 101 2 0
    //   119: pop
    //   120: aload 6
    //   122: aload 8
    //   124: invokevirtual 194	java/io/File:getName	()Ljava/lang/String;
    //   127: invokeinterface 101 2 0
    //   132: pop
    //   133: aload 10
    //   135: invokestatic 200	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   138: iinc 7 1
    //   141: goto -70 -> 71
    //   144: astore 11
    //   146: ldc 47
    //   148: ldc 202
    //   150: aload 11
    //   152: invokestatic 205	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   155: pop
    //   156: aload 9
    //   158: invokestatic 200	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   161: goto -23 -> 138
    //   164: astore_3
    //   165: aload_0
    //   166: monitorexit
    //   167: aload_3
    //   168: athrow
    //   169: astore 12
    //   171: aload 9
    //   173: invokestatic 200	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   176: aload 12
    //   178: athrow
    //   179: aload_0
    //   180: aload 6
    //   182: putfield 26	com/google/android/search/core/sdch/SdchDictionaryCache:mCacheEntries	Ljava/util/List;
    //   185: aload_1
    //   186: aload 5
    //   188: invokeinterface 171 2 0
    //   193: goto -150 -> 43
    //   196: astore 12
    //   198: aload 10
    //   200: astore 9
    //   202: goto -31 -> 171
    //   205: astore 11
    //   207: aload 10
    //   209: astore 9
    //   211: goto -65 -> 146
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	214	0	this	SdchDictionaryCache
    //   0	214	1	paramSimpleCallback	SimpleCallback<List<SdchDictionary>>
    //   18	29	2	localFile1	File
    //   164	4	3	localObject1	Object
    //   50	30	4	arrayOfFile	File[]
    //   58	129	5	localArrayList1	ArrayList
    //   66	115	6	localArrayList2	ArrayList
    //   69	70	7	i	int
    //   84	39	8	localFile2	File
    //   87	123	9	localObject2	Object
    //   105	103	10	localBufferedInputStream	java.io.BufferedInputStream
    //   144	7	11	localIOException1	java.io.IOException
    //   205	1	11	localIOException2	java.io.IOException
    //   169	8	12	localObject3	Object
    //   196	1	12	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   89	107	144	java/io/IOException
    //   2	43	164	finally
    //   46	68	164	finally
    //   71	86	164	finally
    //   133	138	164	finally
    //   156	161	164	finally
    //   171	179	164	finally
    //   179	193	164	finally
    //   89	107	169	finally
    //   146	156	169	finally
    //   107	133	196	finally
    //   107	133	205	java/io/IOException
  }
  
  /* Error */
  void writeDictionaryToCache(SdchDictionary paramSdchDictionary)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 26	com/google/android/search/core/sdch/SdchDictionaryCache:mCacheEntries	Ljava/util/List;
    //   6: ifnull +52 -> 58
    //   9: iconst_1
    //   10: istore_3
    //   11: iload_3
    //   12: invokestatic 215	com/google/common/base/Preconditions:checkState	(Z)V
    //   15: aload_0
    //   16: invokevirtual 217	com/google/android/search/core/sdch/SdchDictionaryCache:getEvictionList	()Ljava/util/List;
    //   19: invokeinterface 121 1 0
    //   24: astore 4
    //   26: aload 4
    //   28: invokeinterface 126 1 0
    //   33: ifeq +30 -> 63
    //   36: aload_0
    //   37: aload 4
    //   39: invokeinterface 130 1 0
    //   44: checkcast 90	java/lang/String
    //   47: invokespecial 219	com/google/android/search/core/sdch/SdchDictionaryCache:deleteCacheEntry	(Ljava/lang/String;)V
    //   50: goto -24 -> 26
    //   53: astore_2
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_2
    //   57: athrow
    //   58: iconst_0
    //   59: istore_3
    //   60: goto -49 -> 11
    //   63: aload_0
    //   64: invokespecial 221	com/google/android/search/core/sdch/SdchDictionaryCache:generateCacheFileName	()Ljava/io/File;
    //   67: astore 5
    //   69: iconst_0
    //   70: istore 6
    //   72: aconst_null
    //   73: astore 7
    //   75: new 223	java/io/BufferedOutputStream
    //   78: dup
    //   79: new 225	java/io/FileOutputStream
    //   82: dup
    //   83: aload 5
    //   85: invokespecial 226	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   88: invokespecial 229	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   91: astore 8
    //   93: aload_1
    //   94: aload 8
    //   96: invokevirtual 232	com/google/android/search/core/sdch/SdchDictionary:serializeTo	(Ljava/io/OutputStream;)V
    //   99: aload_0
    //   100: getfield 26	com/google/android/search/core/sdch/SdchDictionaryCache:mCacheEntries	Ljava/util/List;
    //   103: aload 5
    //   105: invokevirtual 194	java/io/File:getName	()Ljava/lang/String;
    //   108: invokeinterface 101 2 0
    //   113: pop
    //   114: aload 8
    //   116: invokestatic 200	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   119: iload 6
    //   121: ifeq +37 -> 158
    //   124: aload 5
    //   126: invokevirtual 45	java/io/File:delete	()Z
    //   129: ifne +29 -> 158
    //   132: ldc 47
    //   134: new 49	java/lang/StringBuilder
    //   137: dup
    //   138: invokespecial 50	java/lang/StringBuilder:<init>	()V
    //   141: ldc 234
    //   143: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: aload 5
    //   148: invokevirtual 141	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   151: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   154: invokestatic 69	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   157: pop
    //   158: aload_0
    //   159: monitorexit
    //   160: return
    //   161: astore 9
    //   163: ldc 47
    //   165: ldc 236
    //   167: aload 9
    //   169: invokestatic 205	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   172: pop
    //   173: aload 7
    //   175: invokestatic 200	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   178: iconst_0
    //   179: istore 6
    //   181: goto -62 -> 119
    //   184: astore 13
    //   186: ldc 47
    //   188: ldc 238
    //   190: aload 13
    //   192: invokestatic 205	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   195: pop
    //   196: iconst_1
    //   197: istore 6
    //   199: aload 7
    //   201: invokestatic 200	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   204: goto -85 -> 119
    //   207: aload 7
    //   209: invokestatic 200	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   212: aload 10
    //   214: athrow
    //   215: astore 10
    //   217: aload 8
    //   219: astore 7
    //   221: goto -14 -> 207
    //   224: astore 13
    //   226: aload 8
    //   228: astore 7
    //   230: goto -44 -> 186
    //   233: astore 9
    //   235: aload 8
    //   237: astore 7
    //   239: goto -76 -> 163
    //   242: astore 10
    //   244: goto -37 -> 207
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	247	0	this	SdchDictionaryCache
    //   0	247	1	paramSdchDictionary	SdchDictionary
    //   53	4	2	localObject1	Object
    //   10	50	3	bool	boolean
    //   24	14	4	localIterator	Iterator
    //   67	80	5	localFile	File
    //   70	128	6	i	int
    //   73	165	7	localObject2	Object
    //   91	145	8	localBufferedOutputStream	java.io.BufferedOutputStream
    //   161	7	9	localFileNotFoundException1	java.io.FileNotFoundException
    //   233	1	9	localFileNotFoundException2	java.io.FileNotFoundException
    //   212	1	10	localObject3	Object
    //   215	1	10	localObject4	Object
    //   242	1	10	localObject5	Object
    //   184	7	13	localIOException1	java.io.IOException
    //   224	1	13	localIOException2	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   2	9	53	finally
    //   11	26	53	finally
    //   26	50	53	finally
    //   63	69	53	finally
    //   114	119	53	finally
    //   124	158	53	finally
    //   173	178	53	finally
    //   199	204	53	finally
    //   207	215	53	finally
    //   75	93	161	java/io/FileNotFoundException
    //   75	93	184	java/io/IOException
    //   93	114	215	finally
    //   93	114	224	java/io/IOException
    //   93	114	233	java/io/FileNotFoundException
    //   75	93	242	finally
    //   163	173	242	finally
    //   186	196	242	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.sdch.SdchDictionaryCache
 * JD-Core Version:    0.7.0.1
 */