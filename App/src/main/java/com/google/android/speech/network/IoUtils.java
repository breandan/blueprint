package com.google.android.speech.network;

import com.google.wireless.voicesearch.proto.GstaticConfiguration.HttpServerInfo;
import java.net.HttpURLConnection;

public class IoUtils
{
  public static void addHttpHeaders(HttpURLConnection paramHttpURLConnection, GstaticConfiguration.HttpServerInfo paramHttpServerInfo)
  {
    for (int i = 0; i < paramHttpServerInfo.getHttpHeaderKeyCount(); i++) {
      paramHttpURLConnection.addRequestProperty(paramHttpServerInfo.getHttpHeaderKey(i), paramHttpServerInfo.getHttpHeaderValue(i));
    }
  }
  
  /* Error */
  public static boolean uncompress(java.io.File paramFile1, java.io.File paramFile2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 31	java/io/FileOutputStream
    //   7: dup
    //   8: aload_1
    //   9: invokespecial 35	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   12: astore 4
    //   14: new 37	java/util/zip/GZIPInputStream
    //   17: dup
    //   18: new 39	java/io/FileInputStream
    //   21: dup
    //   22: aload_0
    //   23: invokespecial 40	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   26: invokespecial 43	java/util/zip/GZIPInputStream:<init>	(Ljava/io/InputStream;)V
    //   29: astore 5
    //   31: sipush 1024
    //   34: newarray byte
    //   36: astore 9
    //   38: aload 5
    //   40: aload 9
    //   42: invokevirtual 49	java/io/InputStream:read	([B)I
    //   45: istore 10
    //   47: iload 10
    //   49: iconst_m1
    //   50: if_icmpeq +70 -> 120
    //   53: aload 4
    //   55: aload 9
    //   57: iconst_0
    //   58: iload 10
    //   60: invokevirtual 55	java/io/OutputStream:write	([BII)V
    //   63: goto -25 -> 38
    //   66: astore 7
    //   68: aload 5
    //   70: astore_3
    //   71: aload 4
    //   73: astore_2
    //   74: ldc 57
    //   76: new 59	java/lang/StringBuilder
    //   79: dup
    //   80: invokespecial 62	java/lang/StringBuilder:<init>	()V
    //   83: ldc 64
    //   85: invokevirtual 68	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: aload_0
    //   89: invokevirtual 71	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   92: ldc 73
    //   94: invokevirtual 68	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: aload_1
    //   98: invokevirtual 71	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   101: invokevirtual 77	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   104: aload 7
    //   106: invokestatic 83	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   109: pop
    //   110: aload_2
    //   111: invokestatic 89	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   114: aload_3
    //   115: invokestatic 89	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   118: iconst_0
    //   119: ireturn
    //   120: aload 4
    //   122: invokestatic 89	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   125: aload 5
    //   127: invokestatic 89	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   130: iconst_1
    //   131: ireturn
    //   132: astore 6
    //   134: aload_2
    //   135: invokestatic 89	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   138: aload_3
    //   139: invokestatic 89	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   142: aload 6
    //   144: athrow
    //   145: astore 6
    //   147: aload 4
    //   149: astore_2
    //   150: aconst_null
    //   151: astore_3
    //   152: goto -18 -> 134
    //   155: astore 6
    //   157: aload 5
    //   159: astore_3
    //   160: aload 4
    //   162: astore_2
    //   163: goto -29 -> 134
    //   166: astore 7
    //   168: aconst_null
    //   169: astore_3
    //   170: aconst_null
    //   171: astore_2
    //   172: goto -98 -> 74
    //   175: astore 7
    //   177: aload 4
    //   179: astore_2
    //   180: aconst_null
    //   181: astore_3
    //   182: goto -108 -> 74
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	185	0	paramFile1	java.io.File
    //   0	185	1	paramFile2	java.io.File
    //   1	179	2	localObject1	Object
    //   3	179	3	localObject2	Object
    //   12	166	4	localFileOutputStream	java.io.FileOutputStream
    //   29	129	5	localGZIPInputStream	java.util.zip.GZIPInputStream
    //   132	11	6	localObject3	Object
    //   145	1	6	localObject4	Object
    //   155	1	6	localObject5	Object
    //   66	39	7	localIOException1	java.io.IOException
    //   166	1	7	localIOException2	java.io.IOException
    //   175	1	7	localIOException3	java.io.IOException
    //   36	20	9	arrayOfByte	byte[]
    //   45	14	10	i	int
    // Exception table:
    //   from	to	target	type
    //   31	38	66	java/io/IOException
    //   38	47	66	java/io/IOException
    //   53	63	66	java/io/IOException
    //   4	14	132	finally
    //   74	110	132	finally
    //   14	31	145	finally
    //   31	38	155	finally
    //   38	47	155	finally
    //   53	63	155	finally
    //   4	14	166	java/io/IOException
    //   14	31	175	java/io/IOException
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.IoUtils
 * JD-Core Version:    0.7.0.1
 */