package com.google.common.util.concurrent;

public final class Uninterruptibles
{
  /* Error */
  public static <V> V getUninterruptibly(java.util.concurrent.Future<V> paramFuture)
    throws java.util.concurrent.ExecutionException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: invokeinterface 16 1 0
    //   8: astore 4
    //   10: iload_1
    //   11: ifeq +9 -> 20
    //   14: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   17: invokevirtual 26	java/lang/Thread:interrupt	()V
    //   20: aload 4
    //   22: areturn
    //   23: astore_3
    //   24: iconst_1
    //   25: istore_1
    //   26: goto -24 -> 2
    //   29: astore_2
    //   30: iload_1
    //   31: ifeq +9 -> 40
    //   34: invokestatic 22	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   37: invokevirtual 26	java/lang/Thread:interrupt	()V
    //   40: aload_2
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	paramFuture	java.util.concurrent.Future<V>
    //   1	30	1	i	int
    //   29	12	2	localObject1	Object
    //   23	1	3	localInterruptedException	java.lang.InterruptedException
    //   8	13	4	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   2	10	23	java/lang/InterruptedException
    //   2	10	29	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.Uninterruptibles
 * JD-Core Version:    0.7.0.1
 */