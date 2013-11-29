package com.google.android.speech.embedded;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import com.google.common.base.Supplier;

public class DeviceClassSupplier
  implements Supplier<Integer>
{
  private final ActivityManager mActivityManager;
  private Integer mDeviceClass;
  
  public DeviceClassSupplier(Context paramContext)
  {
    this.mActivityManager = ((ActivityManager)paramContext.getSystemService("activity"));
  }
  
  private Integer calculateDeviceClass()
  {
    if (!hasNeon()) {
      return Integer.valueOf(5);
    }
    ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
    this.mActivityManager.getMemoryInfo(localMemoryInfo);
    if (localMemoryInfo.totalMem > 700000000L) {
      return Integer.valueOf(100);
    }
    return Integer.valueOf(10);
  }
  
  /* Error */
  private static boolean hasNeon()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: new 57	java/io/BufferedReader
    //   5: dup
    //   6: new 59	java/io/InputStreamReader
    //   9: dup
    //   10: new 61	java/io/FileInputStream
    //   13: dup
    //   14: ldc 63
    //   16: invokespecial 66	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   19: invokespecial 69	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   22: invokespecial 72	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   25: astore_1
    //   26: aload_1
    //   27: invokevirtual 76	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   30: astore 5
    //   32: iconst_0
    //   33: istore 6
    //   35: aload 5
    //   37: ifnull +86 -> 123
    //   40: aload 5
    //   42: ldc 78
    //   44: iconst_2
    //   45: invokevirtual 84	java/lang/String:split	(Ljava/lang/String;I)[Ljava/lang/String;
    //   48: astore 7
    //   50: aload 7
    //   52: ifnull -26 -> 26
    //   55: aload 7
    //   57: arraylength
    //   58: iconst_2
    //   59: if_icmpne -33 -> 26
    //   62: ldc 86
    //   64: aload 7
    //   66: iconst_0
    //   67: aaload
    //   68: invokevirtual 90	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   71: ifeq -45 -> 26
    //   74: aload 7
    //   76: iconst_1
    //   77: aaload
    //   78: ldc 92
    //   80: invokevirtual 95	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   83: astore 8
    //   85: aload 8
    //   87: arraylength
    //   88: istore 9
    //   90: iconst_0
    //   91: istore 10
    //   93: iconst_0
    //   94: istore 6
    //   96: iload 10
    //   98: iload 9
    //   100: if_icmpge +23 -> 123
    //   103: ldc 97
    //   105: aload 8
    //   107: iload 10
    //   109: aaload
    //   110: invokevirtual 101	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   113: istore 11
    //   115: iload 11
    //   117: ifeq +13 -> 130
    //   120: iconst_1
    //   121: istore 6
    //   123: aload_1
    //   124: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   127: iload 6
    //   129: ireturn
    //   130: iinc 10 1
    //   133: goto -40 -> 93
    //   136: astore 12
    //   138: ldc 109
    //   140: ldc 111
    //   142: invokestatic 117	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   145: pop
    //   146: aload_0
    //   147: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   150: iconst_0
    //   151: ireturn
    //   152: astore_3
    //   153: aload_0
    //   154: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   157: aload_3
    //   158: athrow
    //   159: astore_3
    //   160: aload_1
    //   161: astore_0
    //   162: goto -9 -> 153
    //   165: astore_2
    //   166: aload_1
    //   167: astore_0
    //   168: goto -30 -> 138
    // Local variable table:
    //   start	length	slot	name	signature
    //   1	167	0	localObject1	Object
    //   25	142	1	localBufferedReader	java.io.BufferedReader
    //   165	1	2	localIOException1	java.io.IOException
    //   152	6	3	localObject2	Object
    //   159	1	3	localObject3	Object
    //   30	11	5	str	java.lang.String
    //   33	95	6	bool1	boolean
    //   48	27	7	arrayOfString1	java.lang.String[]
    //   83	23	8	arrayOfString2	java.lang.String[]
    //   88	13	9	i	int
    //   91	40	10	j	int
    //   113	3	11	bool2	boolean
    //   136	1	12	localIOException2	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   2	26	136	java/io/IOException
    //   2	26	152	finally
    //   138	146	152	finally
    //   26	32	159	finally
    //   40	50	159	finally
    //   55	90	159	finally
    //   103	115	159	finally
    //   26	32	165	java/io/IOException
    //   40	50	165	java/io/IOException
    //   55	90	165	java/io/IOException
    //   103	115	165	java/io/IOException
  }
  
  public Integer get()
  {
    try
    {
      if (this.mDeviceClass == null) {
        this.mDeviceClass = calculateDeviceClass();
      }
      Integer localInteger = this.mDeviceClass;
      return localInteger;
    }
    finally {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.embedded.DeviceClassSupplier
 * JD-Core Version:    0.7.0.1
 */