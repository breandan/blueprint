package com.google.android.search.core.util;

import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InputStreamChunkProducer
  extends ChunkProducer
{
  private final InputSupplier<InputStream> mInputStreamSupplier;
  private final Object mLock = new Object();
  private InputStream mSourceStream = null;
  private boolean mStopWasCalled = false;
  
  protected InputStreamChunkProducer(InputSupplier<InputStream> paramInputSupplier, @Nonnull ExecutorService paramExecutorService, int paramInt)
  {
    super(paramExecutorService, paramInt);
    this.mInputStreamSupplier = paramInputSupplier;
  }
  
  public InputStreamChunkProducer(@Nonnull final InputStream paramInputStream, @Nonnull ExecutorService paramExecutorService, int paramInt)
  {
    super(paramExecutorService, paramInt);
    Preconditions.checkNotNull(paramInputStream);
    this.mInputStreamSupplier = new InputSupplier()
    {
      public InputStream getInput()
      {
        return paramInputStream;
      }
    };
  }
  
  protected void bufferAllData(InputStream paramInputStream)
    throws IOException, InterruptedException, InputStreamChunkProducer.SizeExceededException, InputStreamChunkProducer.ZeroSizeException
  {
    int i = 0;
    int m;
    for (int j = 0;; j = m)
    {
      byte[] arrayOfByte = new byte[65536];
      int k = 0;
      for (;;)
      {
        int i1;
        if (k < 65536)
        {
          throwIOExceptionIfStopped(null);
          i1 = 65536 - k;
        }
        int i2;
        try
        {
          i2 = paramInputStream.read(arrayOfByte, k, i1);
          if (i2 < 0)
          {
            i += k;
            if (k != 0) {
              break;
            }
            if (i != 0) {
              return;
            }
            throw new ZeroSizeException();
          }
        }
        catch (IllegalStateException localIllegalStateException)
        {
          throwIOExceptionIfStopped(localIllegalStateException);
          throw localIllegalStateException;
        }
        k += i2;
      }
      m = j + 1;
      int n = j;
      if (k == 65536) {}
      for (ChunkProducer.DataChunk localDataChunk = new ChunkProducer.DataChunk(arrayOfByte, n);; localDataChunk = new ChunkProducer.DataChunk(arrayOfByte, k, n))
      {
        consumerOnChunk(localDataChunk);
        if (i <= this.mMaxResponseBytes) {
          break;
        }
        throw new SizeExceededException();
      }
      if (Thread.currentThread().isInterrupted()) {
        throw new InterruptedException();
      }
    }
  }
  
  public void close()
  {
    synchronized (this.mLock)
    {
      this.mStopWasCalled = true;
      cancelAndInterruptBufferTask();
      return;
    }
  }
  
  protected void closeSource(@Nullable InputStream paramInputStream, boolean paramBoolean)
  {
    if (paramInputStream != null) {
      Closeables.closeQuietly(paramInputStream);
    }
  }
  
  /* Error */
  protected void runBufferTask()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_0
    //   5: getfield 32	com/google/android/search/core/util/InputStreamChunkProducer:mInputStreamSupplier	Lcom/google/common/io/InputSupplier;
    //   8: invokestatic 39	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   11: pop
    //   12: aload_0
    //   13: getfield 26	com/google/android/search/core/util/InputStreamChunkProducer:mLock	Ljava/lang/Object;
    //   16: astore 35
    //   18: aload 35
    //   20: monitorenter
    //   21: aload_0
    //   22: getfield 30	com/google/android/search/core/util/InputStreamChunkProducer:mStopWasCalled	Z
    //   25: ifeq +82 -> 107
    //   28: new 48	java/io/IOException
    //   31: dup
    //   32: ldc 112
    //   34: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   37: athrow
    //   38: astore 36
    //   40: aload 35
    //   42: monitorexit
    //   43: aload 36
    //   45: athrow
    //   46: astore 26
    //   48: ldc 117
    //   50: ldc 119
    //   52: invokestatic 125	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   55: pop
    //   56: aload_0
    //   57: getfield 26	com/google/android/search/core/util/InputStreamChunkProducer:mLock	Ljava/lang/Object;
    //   60: astore 28
    //   62: aload 28
    //   64: monitorenter
    //   65: aload_0
    //   66: getfield 30	com/google/android/search/core/util/InputStreamChunkProducer:mStopWasCalled	Z
    //   69: istore 30
    //   71: aconst_null
    //   72: astore_2
    //   73: iload 30
    //   75: ifne +190 -> 265
    //   78: new 48	java/io/IOException
    //   81: dup
    //   82: ldc 119
    //   84: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   87: astore_2
    //   88: aload 28
    //   90: monitorexit
    //   91: aload_0
    //   92: aload_1
    //   93: iconst_0
    //   94: invokevirtual 127	com/google/android/search/core/util/InputStreamChunkProducer:closeSource	(Ljava/io/InputStream;Z)V
    //   97: iconst_0
    //   98: ifeq +201 -> 299
    //   101: aload_0
    //   102: invokevirtual 130	com/google/android/search/core/util/InputStreamChunkProducer:setComplete	()Z
    //   105: pop
    //   106: return
    //   107: aload 35
    //   109: monitorexit
    //   110: aload_0
    //   111: getfield 32	com/google/android/search/core/util/InputStreamChunkProducer:mInputStreamSupplier	Lcom/google/common/io/InputSupplier;
    //   114: invokeinterface 136 1 0
    //   119: checkcast 63	java/io/InputStream
    //   122: astore_1
    //   123: iconst_0
    //   124: istore 37
    //   126: aconst_null
    //   127: astore_2
    //   128: aload_1
    //   129: ifnull +92 -> 221
    //   132: aload_0
    //   133: getfield 26	com/google/android/search/core/util/InputStreamChunkProducer:mLock	Ljava/lang/Object;
    //   136: astore 38
    //   138: aload 38
    //   140: monitorenter
    //   141: aload_0
    //   142: getfield 30	com/google/android/search/core/util/InputStreamChunkProducer:mStopWasCalled	Z
    //   145: ifeq +60 -> 205
    //   148: new 48	java/io/IOException
    //   151: dup
    //   152: ldc 112
    //   154: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   157: athrow
    //   158: astore 39
    //   160: aload 38
    //   162: monitorexit
    //   163: aload 39
    //   165: athrow
    //   166: astore 20
    //   168: ldc 117
    //   170: ldc 138
    //   172: invokestatic 141	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   175: pop
    //   176: new 48	java/io/IOException
    //   179: dup
    //   180: ldc 143
    //   182: aload 20
    //   184: invokespecial 146	java/io/IOException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   187: astore 22
    //   189: aload_0
    //   190: aload_1
    //   191: iconst_0
    //   192: invokevirtual 127	com/google/android/search/core/util/InputStreamChunkProducer:closeSource	(Ljava/io/InputStream;Z)V
    //   195: iconst_0
    //   196: ifeq +129 -> 325
    //   199: aload_0
    //   200: invokevirtual 130	com/google/android/search/core/util/InputStreamChunkProducer:setComplete	()Z
    //   203: pop
    //   204: return
    //   205: aload_0
    //   206: aload_1
    //   207: putfield 28	com/google/android/search/core/util/InputStreamChunkProducer:mSourceStream	Ljava/io/InputStream;
    //   210: aload 38
    //   212: monitorexit
    //   213: aload_0
    //   214: aload_1
    //   215: invokevirtual 148	com/google/android/search/core/util/InputStreamChunkProducer:bufferAllData	(Ljava/io/InputStream;)V
    //   218: iconst_1
    //   219: istore 37
    //   221: aload_0
    //   222: aload_1
    //   223: iload 37
    //   225: invokevirtual 127	com/google/android/search/core/util/InputStreamChunkProducer:closeSource	(Ljava/io/InputStream;Z)V
    //   228: iload 37
    //   230: ifeq +9 -> 239
    //   233: aload_0
    //   234: invokevirtual 130	com/google/android/search/core/util/InputStreamChunkProducer:setComplete	()Z
    //   237: pop
    //   238: return
    //   239: iconst_0
    //   240: ifeq +10 -> 250
    //   243: aload_0
    //   244: aconst_null
    //   245: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   248: pop
    //   249: return
    //   250: aload_0
    //   251: new 48	java/io/IOException
    //   254: dup
    //   255: ldc 154
    //   257: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   260: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   263: pop
    //   264: return
    //   265: aload 26
    //   267: astore_2
    //   268: goto -180 -> 88
    //   271: astore 29
    //   273: aload 28
    //   275: monitorexit
    //   276: aload 29
    //   278: athrow
    //   279: astore 6
    //   281: aload_0
    //   282: aload_1
    //   283: iconst_0
    //   284: invokevirtual 127	com/google/android/search/core/util/InputStreamChunkProducer:closeSource	(Ljava/io/InputStream;Z)V
    //   287: iconst_0
    //   288: ifeq +227 -> 515
    //   291: aload_0
    //   292: invokevirtual 130	com/google/android/search/core/util/InputStreamChunkProducer:setComplete	()Z
    //   295: pop
    //   296: aload 6
    //   298: athrow
    //   299: aload_2
    //   300: ifnull +10 -> 310
    //   303: aload_0
    //   304: aload_2
    //   305: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   308: pop
    //   309: return
    //   310: aload_0
    //   311: new 48	java/io/IOException
    //   314: dup
    //   315: ldc 154
    //   317: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   320: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   323: pop
    //   324: return
    //   325: aload 22
    //   327: ifnull +11 -> 338
    //   330: aload_0
    //   331: aload 22
    //   333: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   336: pop
    //   337: return
    //   338: aload_0
    //   339: new 48	java/io/IOException
    //   342: dup
    //   343: ldc 154
    //   345: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   348: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   351: pop
    //   352: return
    //   353: astore 14
    //   355: ldc 117
    //   357: ldc 156
    //   359: invokestatic 141	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   362: pop
    //   363: new 48	java/io/IOException
    //   366: dup
    //   367: ldc 158
    //   369: aload 14
    //   371: invokespecial 146	java/io/IOException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   374: astore 16
    //   376: aload_0
    //   377: aload_1
    //   378: iconst_0
    //   379: invokevirtual 127	com/google/android/search/core/util/InputStreamChunkProducer:closeSource	(Ljava/io/InputStream;Z)V
    //   382: iconst_0
    //   383: ifeq +9 -> 392
    //   386: aload_0
    //   387: invokevirtual 130	com/google/android/search/core/util/InputStreamChunkProducer:setComplete	()Z
    //   390: pop
    //   391: return
    //   392: aload 16
    //   394: ifnull +11 -> 405
    //   397: aload_0
    //   398: aload 16
    //   400: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   403: pop
    //   404: return
    //   405: aload_0
    //   406: new 48	java/io/IOException
    //   409: dup
    //   410: ldc 154
    //   412: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   415: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   418: pop
    //   419: return
    //   420: astore_3
    //   421: aload_0
    //   422: getfield 26	com/google/android/search/core/util/InputStreamChunkProducer:mLock	Ljava/lang/Object;
    //   425: astore 4
    //   427: aload 4
    //   429: monitorenter
    //   430: aload_0
    //   431: getfield 30	com/google/android/search/core/util/InputStreamChunkProducer:mStopWasCalled	Z
    //   434: ifeq +24 -> 458
    //   437: aload_3
    //   438: astore_2
    //   439: aload 4
    //   441: monitorexit
    //   442: aload_0
    //   443: aload_1
    //   444: iconst_0
    //   445: invokevirtual 127	com/google/android/search/core/util/InputStreamChunkProducer:closeSource	(Ljava/io/InputStream;Z)V
    //   448: iconst_0
    //   449: ifeq +40 -> 489
    //   452: aload_0
    //   453: invokevirtual 130	com/google/android/search/core/util/InputStreamChunkProducer:setComplete	()Z
    //   456: pop
    //   457: return
    //   458: ldc 117
    //   460: ldc 160
    //   462: aload_3
    //   463: invokestatic 164	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   466: pop
    //   467: new 48	java/io/IOException
    //   470: dup
    //   471: ldc 166
    //   473: aload_3
    //   474: invokespecial 146	java/io/IOException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   477: astore_2
    //   478: goto -39 -> 439
    //   481: astore 5
    //   483: aload 4
    //   485: monitorexit
    //   486: aload 5
    //   488: athrow
    //   489: aload_2
    //   490: ifnull +10 -> 500
    //   493: aload_0
    //   494: aload_2
    //   495: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   498: pop
    //   499: return
    //   500: aload_0
    //   501: new 48	java/io/IOException
    //   504: dup
    //   505: ldc 154
    //   507: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   510: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   513: pop
    //   514: return
    //   515: aload_2
    //   516: ifnull +12 -> 528
    //   519: aload_0
    //   520: aload_2
    //   521: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   524: pop
    //   525: goto -229 -> 296
    //   528: aload_0
    //   529: new 48	java/io/IOException
    //   532: dup
    //   533: ldc 154
    //   535: invokespecial 115	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   538: invokevirtual 152	com/google/android/search/core/util/InputStreamChunkProducer:setFailed	(Ljava/lang/Exception;)Z
    //   541: pop
    //   542: goto -246 -> 296
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	545	0	this	InputStreamChunkProducer
    //   1	443	1	localInputStream	InputStream
    //   3	518	2	localObject1	Object
    //   420	54	3	localIOException1	IOException
    //   481	6	5	localObject3	Object
    //   279	18	6	localObject4	Object
    //   353	17	14	localZeroSizeException	ZeroSizeException
    //   374	25	16	localIOException2	IOException
    //   166	17	20	localSizeExceededException	SizeExceededException
    //   187	145	22	localIOException3	IOException
    //   46	220	26	localInterruptedException	InterruptedException
    //   271	6	29	localObject6	Object
    //   69	5	30	bool1	boolean
    //   38	6	36	localObject8	Object
    //   124	105	37	bool2	boolean
    //   158	6	39	localObject10	Object
    // Exception table:
    //   from	to	target	type
    //   21	38	38	finally
    //   40	43	38	finally
    //   107	110	38	finally
    //   4	21	46	java/lang/InterruptedException
    //   43	46	46	java/lang/InterruptedException
    //   110	123	46	java/lang/InterruptedException
    //   132	141	46	java/lang/InterruptedException
    //   163	166	46	java/lang/InterruptedException
    //   213	218	46	java/lang/InterruptedException
    //   141	158	158	finally
    //   160	163	158	finally
    //   205	213	158	finally
    //   4	21	166	com/google/android/search/core/util/InputStreamChunkProducer$SizeExceededException
    //   43	46	166	com/google/android/search/core/util/InputStreamChunkProducer$SizeExceededException
    //   110	123	166	com/google/android/search/core/util/InputStreamChunkProducer$SizeExceededException
    //   132	141	166	com/google/android/search/core/util/InputStreamChunkProducer$SizeExceededException
    //   163	166	166	com/google/android/search/core/util/InputStreamChunkProducer$SizeExceededException
    //   213	218	166	com/google/android/search/core/util/InputStreamChunkProducer$SizeExceededException
    //   65	71	271	finally
    //   78	88	271	finally
    //   88	91	271	finally
    //   273	276	271	finally
    //   4	21	279	finally
    //   43	46	279	finally
    //   48	65	279	finally
    //   110	123	279	finally
    //   132	141	279	finally
    //   163	166	279	finally
    //   168	189	279	finally
    //   213	218	279	finally
    //   276	279	279	finally
    //   355	376	279	finally
    //   421	430	279	finally
    //   486	489	279	finally
    //   4	21	353	com/google/android/search/core/util/InputStreamChunkProducer$ZeroSizeException
    //   43	46	353	com/google/android/search/core/util/InputStreamChunkProducer$ZeroSizeException
    //   110	123	353	com/google/android/search/core/util/InputStreamChunkProducer$ZeroSizeException
    //   132	141	353	com/google/android/search/core/util/InputStreamChunkProducer$ZeroSizeException
    //   163	166	353	com/google/android/search/core/util/InputStreamChunkProducer$ZeroSizeException
    //   213	218	353	com/google/android/search/core/util/InputStreamChunkProducer$ZeroSizeException
    //   4	21	420	java/io/IOException
    //   43	46	420	java/io/IOException
    //   110	123	420	java/io/IOException
    //   132	141	420	java/io/IOException
    //   163	166	420	java/io/IOException
    //   213	218	420	java/io/IOException
    //   430	437	481	finally
    //   439	442	481	finally
    //   458	478	481	finally
    //   483	486	481	finally
  }
  
  public void start(@Nonnull ChunkProducer.ChunkConsumer paramChunkConsumer)
  {
    Preconditions.checkNotNull(this.mInputStreamSupplier, "Should have called setInputStreamSupplier by now.");
    synchronized (this.mLock)
    {
      if (this.mStopWasCalled) {
        return;
      }
      super.start(paramChunkConsumer);
      return;
    }
  }
  
  public void throwIOExceptionIfStopped(@Nullable Exception paramException)
    throws IOException
  {
    synchronized (this.mLock)
    {
      if (!this.mStopWasCalled) {
        break label44;
      }
      if (paramException == null) {
        throw new IOException("Source stream was closed");
      }
    }
    throw new IOException("Source stream was closed", paramException);
    label44:
  }
  
  public static class SizeExceededException
    extends Exception
  {}
  
  public static class ZeroSizeException
    extends Exception
  {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.InputStreamChunkProducer
 * JD-Core Version:    0.7.0.1
 */