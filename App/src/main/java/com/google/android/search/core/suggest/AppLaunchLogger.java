package com.google.android.search.core.suggest;

import android.content.ComponentName;
import android.content.Context;
import com.google.common.base.Preconditions;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;

public class AppLaunchLogger
{
  private static final Object FILE_LOCK = new Object();
  
  /* Error */
  public static java.util.List<AppLaunch> getLaunches(FileStreamProvider paramFileStreamProvider)
  {
    // Byte code:
    //   0: new 23	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 24	java/util/ArrayList:<init>	()V
    //   7: astore_1
    //   8: getstatic 13	com/google/android/search/core/suggest/AppLaunchLogger:FILE_LOCK	Ljava/lang/Object;
    //   11: astore_2
    //   12: aload_2
    //   13: monitorenter
    //   14: aload_0
    //   15: ldc 26
    //   17: invokeinterface 32 2 0
    //   22: astore 6
    //   24: aload 6
    //   26: invokevirtual 38	java/io/File:exists	()Z
    //   29: istore 7
    //   31: iload 7
    //   33: ifne +7 -> 40
    //   36: aload_2
    //   37: monitorexit
    //   38: aload_1
    //   39: areturn
    //   40: new 40	java/io/RandomAccessFile
    //   43: dup
    //   44: aload 6
    //   46: ldc 42
    //   48: invokespecial 45	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   51: astore 8
    //   53: aload 8
    //   55: invokevirtual 49	java/io/RandomAccessFile:length	()J
    //   58: lstore 18
    //   60: aload 8
    //   62: invokevirtual 52	java/io/RandomAccessFile:getFilePointer	()J
    //   65: lstore 20
    //   67: lload 20
    //   69: lload 18
    //   71: lcmp
    //   72: ifge +57 -> 129
    //   75: aload_1
    //   76: aload 8
    //   78: invokestatic 58	com/google/android/search/core/suggest/AppLaunchLogger$AppLaunch:read	(Ljava/io/RandomAccessFile;)Lcom/google/android/search/core/suggest/AppLaunchLogger$AppLaunch;
    //   81: invokeinterface 64 2 0
    //   86: pop
    //   87: goto -27 -> 60
    //   90: astore 27
    //   92: ldc 66
    //   94: new 68	java/lang/StringBuilder
    //   97: dup
    //   98: invokespecial 69	java/lang/StringBuilder:<init>	()V
    //   101: ldc 71
    //   103: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: aload_1
    //   107: invokeinterface 79 1 0
    //   112: invokevirtual 82	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   115: ldc 84
    //   117: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   123: aload 27
    //   125: invokestatic 94	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   128: pop
    //   129: aload 8
    //   131: invokevirtual 97	java/io/RandomAccessFile:close	()V
    //   134: aload_0
    //   135: ldc 26
    //   137: invokeinterface 101 2 0
    //   142: ifne +11 -> 153
    //   145: ldc 66
    //   147: ldc 103
    //   149: invokestatic 106	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   152: pop
    //   153: aload_2
    //   154: monitorexit
    //   155: aload_1
    //   156: areturn
    //   157: astore 5
    //   159: aload_2
    //   160: monitorexit
    //   161: aload 5
    //   163: athrow
    //   164: astore_3
    //   165: ldc 66
    //   167: ldc 108
    //   169: aload_3
    //   170: invokestatic 94	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   173: pop
    //   174: aload_2
    //   175: monitorexit
    //   176: aload_1
    //   177: areturn
    //   178: astore 25
    //   180: ldc 66
    //   182: new 68	java/lang/StringBuilder
    //   185: dup
    //   186: invokespecial 69	java/lang/StringBuilder:<init>	()V
    //   189: ldc 110
    //   191: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   194: aload_1
    //   195: invokeinterface 79 1 0
    //   200: invokevirtual 82	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   203: ldc 84
    //   205: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   208: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   211: aload 25
    //   213: invokestatic 94	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   216: pop
    //   217: goto -88 -> 129
    //   220: astore 13
    //   222: ldc 66
    //   224: new 68	java/lang/StringBuilder
    //   227: dup
    //   228: invokespecial 69	java/lang/StringBuilder:<init>	()V
    //   231: ldc 112
    //   233: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   236: aload_1
    //   237: invokeinterface 79 1 0
    //   242: invokevirtual 82	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   245: ldc 114
    //   247: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   250: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   253: aload 13
    //   255: invokestatic 94	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   258: pop
    //   259: aload 8
    //   261: invokevirtual 97	java/io/RandomAccessFile:close	()V
    //   264: aload_0
    //   265: ldc 26
    //   267: invokeinterface 101 2 0
    //   272: ifne -119 -> 153
    //   275: ldc 66
    //   277: ldc 103
    //   279: invokestatic 106	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   282: pop
    //   283: goto -130 -> 153
    //   286: astore 22
    //   288: ldc 66
    //   290: ldc 116
    //   292: aload 22
    //   294: invokestatic 94	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   297: pop
    //   298: goto -164 -> 134
    //   301: astore 15
    //   303: ldc 66
    //   305: ldc 116
    //   307: aload 15
    //   309: invokestatic 94	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   312: pop
    //   313: goto -49 -> 264
    //   316: astore 9
    //   318: aload 8
    //   320: invokevirtual 97	java/io/RandomAccessFile:close	()V
    //   323: aload_0
    //   324: ldc 26
    //   326: invokeinterface 101 2 0
    //   331: ifne +11 -> 342
    //   334: ldc 66
    //   336: ldc 103
    //   338: invokestatic 106	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   341: pop
    //   342: aload 9
    //   344: athrow
    //   345: astore 10
    //   347: ldc 66
    //   349: ldc 116
    //   351: aload 10
    //   353: invokestatic 94	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   356: pop
    //   357: goto -34 -> 323
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	360	0	paramFileStreamProvider	FileStreamProvider
    //   7	230	1	localArrayList	java.util.ArrayList
    //   11	164	2	localObject1	Object
    //   164	6	3	localFileNotFoundException	FileNotFoundException
    //   157	5	5	localObject2	Object
    //   22	23	6	localFile	File
    //   29	3	7	bool	boolean
    //   51	268	8	localRandomAccessFile	RandomAccessFile
    //   316	27	9	localObject3	Object
    //   345	7	10	localIOException1	IOException
    //   220	34	13	localIOException2	IOException
    //   301	7	15	localIOException3	IOException
    //   58	12	18	l1	long
    //   65	3	20	l2	long
    //   286	7	22	localIOException4	IOException
    //   178	34	25	localIOException5	IOException
    //   90	34	27	localParseException	ParseException
    // Exception table:
    //   from	to	target	type
    //   75	87	90	java/text/ParseException
    //   14	31	157	finally
    //   36	38	157	finally
    //   40	53	157	finally
    //   129	134	157	finally
    //   134	153	157	finally
    //   153	155	157	finally
    //   159	161	157	finally
    //   165	176	157	finally
    //   259	264	157	finally
    //   264	283	157	finally
    //   288	298	157	finally
    //   303	313	157	finally
    //   318	323	157	finally
    //   323	342	157	finally
    //   342	345	157	finally
    //   347	357	157	finally
    //   14	31	164	java/io/FileNotFoundException
    //   40	53	164	java/io/FileNotFoundException
    //   75	87	178	java/io/IOException
    //   53	60	220	java/io/IOException
    //   60	67	220	java/io/IOException
    //   92	129	220	java/io/IOException
    //   180	217	220	java/io/IOException
    //   129	134	286	java/io/IOException
    //   259	264	301	java/io/IOException
    //   53	60	316	finally
    //   60	67	316	finally
    //   75	87	316	finally
    //   92	129	316	finally
    //   180	217	316	finally
    //   222	259	316	finally
    //   318	323	345	java/io/IOException
  }
  
  /* Error */
  public static void recordAppLaunch(FileStreamProvider paramFileStreamProvider, AppLaunch paramAppLaunch)
  {
    // Byte code:
    //   0: getstatic 13	com/google/android/search/core/suggest/AppLaunchLogger:FILE_LOCK	Ljava/lang/Object;
    //   3: astore_2
    //   4: aload_2
    //   5: monitorenter
    //   6: aconst_null
    //   7: astore_3
    //   8: new 122	java/io/DataOutputStream
    //   11: dup
    //   12: aload_0
    //   13: ldc 26
    //   15: ldc 123
    //   17: invokeinterface 127 3 0
    //   22: invokespecial 130	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   25: astore 4
    //   27: aload_1
    //   28: aload 4
    //   30: invokevirtual 134	com/google/android/search/core/suggest/AppLaunchLogger$AppLaunch:writeTo	(Ljava/io/DataOutputStream;)V
    //   33: aload 4
    //   35: ifnull +8 -> 43
    //   38: aload 4
    //   40: invokevirtual 135	java/io/DataOutputStream:close	()V
    //   43: aload_2
    //   44: monitorexit
    //   45: return
    //   46: astore 13
    //   48: ldc 66
    //   50: ldc 137
    //   52: invokestatic 140	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   55: pop
    //   56: goto -13 -> 43
    //   59: astore 7
    //   61: aload_2
    //   62: monitorexit
    //   63: aload 7
    //   65: athrow
    //   66: astore 15
    //   68: ldc 66
    //   70: new 68	java/lang/StringBuilder
    //   73: dup
    //   74: invokespecial 69	java/lang/StringBuilder:<init>	()V
    //   77: ldc 142
    //   79: invokevirtual 75	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   82: aload_1
    //   83: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   86: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   89: invokestatic 140	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   92: pop
    //   93: aload_3
    //   94: ifnull +7 -> 101
    //   97: aload_3
    //   98: invokevirtual 135	java/io/DataOutputStream:close	()V
    //   101: aload_2
    //   102: monitorexit
    //   103: return
    //   104: astore 11
    //   106: ldc 66
    //   108: ldc 137
    //   110: invokestatic 140	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   113: pop
    //   114: goto -13 -> 101
    //   117: astore 6
    //   119: aload_3
    //   120: ifnull +7 -> 127
    //   123: aload_3
    //   124: invokevirtual 135	java/io/DataOutputStream:close	()V
    //   127: aload 6
    //   129: athrow
    //   130: astore 8
    //   132: ldc 66
    //   134: ldc 137
    //   136: invokestatic 140	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   139: pop
    //   140: goto -13 -> 127
    //   143: astore 6
    //   145: aload 4
    //   147: astore_3
    //   148: goto -29 -> 119
    //   151: astore 5
    //   153: aload 4
    //   155: astore_3
    //   156: goto -88 -> 68
    //   159: astore 7
    //   161: goto -100 -> 61
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	164	0	paramFileStreamProvider	FileStreamProvider
    //   0	164	1	paramAppLaunch	AppLaunch
    //   3	99	2	localObject1	Object
    //   7	149	3	localObject2	Object
    //   25	129	4	localDataOutputStream	DataOutputStream
    //   151	1	5	localException1	java.lang.Exception
    //   117	11	6	localObject3	Object
    //   143	1	6	localObject4	Object
    //   59	5	7	localObject5	Object
    //   159	1	7	localObject6	Object
    //   130	1	8	localIOException1	IOException
    //   104	1	11	localIOException2	IOException
    //   46	1	13	localIOException3	IOException
    //   66	1	15	localException2	java.lang.Exception
    // Exception table:
    //   from	to	target	type
    //   38	43	46	java/io/IOException
    //   38	43	59	finally
    //   43	45	59	finally
    //   48	56	59	finally
    //   8	27	66	java/lang/Exception
    //   97	101	104	java/io/IOException
    //   8	27	117	finally
    //   68	93	117	finally
    //   123	127	130	java/io/IOException
    //   27	33	143	finally
    //   27	33	151	java/lang/Exception
    //   61	63	159	finally
    //   97	101	159	finally
    //   101	103	159	finally
    //   106	114	159	finally
    //   123	127	159	finally
    //   127	130	159	finally
    //   132	140	159	finally
  }
  
  public static final class AppLaunch
  {
    public final ComponentName componentName;
    public final int source;
    public final long time;
    
    public AppLaunch(int paramInt, long paramLong, ComponentName paramComponentName)
    {
      this.source = paramInt;
      this.time = paramLong;
      this.componentName = ((ComponentName)Preconditions.checkNotNull(paramComponentName));
    }
    
    public static AppLaunch read(RandomAccessFile paramRandomAccessFile)
      throws IOException, ParseException
    {
      int i = paramRandomAccessFile.readInt();
      long l = paramRandomAccessFile.readLong();
      String str = paramRandomAccessFile.readUTF();
      ComponentName localComponentName = ComponentName.unflattenFromString(str);
      if (localComponentName == null) {
        throw new ParseException("Parsing ComponentName failed: " + str, 0);
      }
      return new AppLaunch(i, l, localComponentName);
    }
    
    public String toString()
    {
      return "AppLaunch[source=" + this.source + ",time=" + this.time + ", componentName=" + this.componentName + "]";
    }
    
    public void writeTo(DataOutputStream paramDataOutputStream)
      throws IOException
    {
      paramDataOutputStream.writeInt(this.source);
      paramDataOutputStream.writeLong(this.time);
      paramDataOutputStream.writeUTF(this.componentName.flattenToString());
    }
  }
  
  public static class ContextFileStreamProvider
    implements AppLaunchLogger.FileStreamProvider
  {
    private final Context mContext;
    
    public ContextFileStreamProvider(Context paramContext)
    {
      this.mContext = paramContext;
    }
    
    public boolean deleteFile(String paramString)
    {
      return this.mContext.deleteFile(paramString);
    }
    
    public File getFileStreamPath(String paramString)
    {
      return this.mContext.getFileStreamPath(paramString);
    }
    
    public FileOutputStream openFileOutput(String paramString, int paramInt)
      throws FileNotFoundException
    {
      return this.mContext.openFileOutput(paramString, paramInt);
    }
  }
  
  public static abstract interface FileStreamProvider
  {
    public abstract boolean deleteFile(String paramString);
    
    public abstract File getFileStreamPath(String paramString);
    
    public abstract FileOutputStream openFileOutput(String paramString, int paramInt)
      throws FileNotFoundException;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.AppLaunchLogger
 * JD-Core Version:    0.7.0.1
 */