package com.android.launcher3;

import android.content.Intent;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Stats
{
  ArrayList<Integer> mHistogram;
  ArrayList<String> mIntents;
  private final Launcher mLauncher;
  DataOutputStream mLog;
  
  public Stats(Launcher paramLauncher)
  {
    this.mLauncher = paramLauncher;
    loadStats();
    try
    {
      this.mLog = new DataOutputStream(this.mLauncher.openFileOutput("launches.log", 32768));
      this.mLog.writeInt(1);
      this.mLog.writeInt(1);
      return;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      Log.e("Launcher3/Stats", "unable to create stats log: " + localFileNotFoundException);
      this.mLog = null;
      return;
    }
    catch (IOException localIOException)
    {
      Log.e("Launcher3/Stats", "unable to write to stats log: " + localIOException);
      this.mLog = null;
    }
  }
  
  /* Error */
  private void loadStats()
  {
    // Byte code:
    //   0: aload_0
    //   1: new 75	java/util/ArrayList
    //   4: dup
    //   5: bipush 100
    //   7: invokespecial 77	java/util/ArrayList:<init>	(I)V
    //   10: putfield 79	com/android/launcher3/Stats:mIntents	Ljava/util/ArrayList;
    //   13: aload_0
    //   14: new 75	java/util/ArrayList
    //   17: dup
    //   18: bipush 100
    //   20: invokespecial 77	java/util/ArrayList:<init>	(I)V
    //   23: putfield 81	com/android/launcher3/Stats:mHistogram	Ljava/util/ArrayList;
    //   26: aconst_null
    //   27: astore_1
    //   28: new 83	java/io/DataInputStream
    //   31: dup
    //   32: aload_0
    //   33: getfield 24	com/android/launcher3/Stats:mLauncher	Lcom/android/launcher3/Launcher;
    //   36: ldc 85
    //   38: invokevirtual 89	com/android/launcher3/Launcher:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   41: invokespecial 92	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   44: astore_2
    //   45: aload_2
    //   46: invokevirtual 96	java/io/DataInputStream:readInt	()I
    //   49: iconst_1
    //   50: if_icmpne +60 -> 110
    //   53: aload_2
    //   54: invokevirtual 96	java/io/DataInputStream:readInt	()I
    //   57: istore 10
    //   59: iconst_0
    //   60: istore 11
    //   62: iload 11
    //   64: iload 10
    //   66: if_icmpge +44 -> 110
    //   69: aload_2
    //   70: invokevirtual 99	java/io/DataInputStream:readUTF	()Ljava/lang/String;
    //   73: astore 12
    //   75: aload_2
    //   76: invokevirtual 96	java/io/DataInputStream:readInt	()I
    //   79: istore 13
    //   81: aload_0
    //   82: getfield 79	com/android/launcher3/Stats:mIntents	Ljava/util/ArrayList;
    //   85: aload 12
    //   87: invokevirtual 103	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   90: pop
    //   91: aload_0
    //   92: getfield 81	com/android/launcher3/Stats:mHistogram	Ljava/util/ArrayList;
    //   95: iload 13
    //   97: invokestatic 109	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   100: invokevirtual 103	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   103: pop
    //   104: iinc 11 1
    //   107: goto -45 -> 62
    //   110: aload_2
    //   111: ifnull +77 -> 188
    //   114: aload_2
    //   115: invokevirtual 112	java/io/DataInputStream:close	()V
    //   118: return
    //   119: astore 9
    //   121: return
    //   122: astore 17
    //   124: aload_1
    //   125: ifnull -7 -> 118
    //   128: aload_1
    //   129: invokevirtual 112	java/io/DataInputStream:close	()V
    //   132: return
    //   133: astore 4
    //   135: return
    //   136: astore 16
    //   138: aload_1
    //   139: ifnull -21 -> 118
    //   142: aload_1
    //   143: invokevirtual 112	java/io/DataInputStream:close	()V
    //   146: return
    //   147: astore 6
    //   149: return
    //   150: astore 7
    //   152: aload_1
    //   153: ifnull +7 -> 160
    //   156: aload_1
    //   157: invokevirtual 112	java/io/DataInputStream:close	()V
    //   160: aload 7
    //   162: athrow
    //   163: astore 8
    //   165: goto -5 -> 160
    //   168: astore 7
    //   170: aload_2
    //   171: astore_1
    //   172: goto -20 -> 152
    //   175: astore 5
    //   177: aload_2
    //   178: astore_1
    //   179: goto -41 -> 138
    //   182: astore_3
    //   183: aload_2
    //   184: astore_1
    //   185: goto -61 -> 124
    //   188: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	189	0	this	Stats
    //   27	158	1	localObject1	Object
    //   44	140	2	localDataInputStream	java.io.DataInputStream
    //   182	1	3	localFileNotFoundException1	FileNotFoundException
    //   133	1	4	localIOException1	IOException
    //   175	1	5	localIOException2	IOException
    //   147	1	6	localIOException3	IOException
    //   150	11	7	localObject2	Object
    //   168	1	7	localObject3	Object
    //   163	1	8	localIOException4	IOException
    //   119	1	9	localIOException5	IOException
    //   57	10	10	i	int
    //   60	45	11	j	int
    //   73	13	12	str	String
    //   79	17	13	k	int
    //   136	1	16	localIOException6	IOException
    //   122	1	17	localFileNotFoundException2	FileNotFoundException
    // Exception table:
    //   from	to	target	type
    //   114	118	119	java/io/IOException
    //   28	45	122	java/io/FileNotFoundException
    //   128	132	133	java/io/IOException
    //   28	45	136	java/io/IOException
    //   142	146	147	java/io/IOException
    //   28	45	150	finally
    //   156	160	163	java/io/IOException
    //   45	59	168	finally
    //   69	104	168	finally
    //   45	59	175	java/io/IOException
    //   69	104	175	java/io/IOException
    //   45	59	182	java/io/FileNotFoundException
    //   69	104	182	java/io/FileNotFoundException
  }
  
  /* Error */
  private void saveStats()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: new 29	java/io/DataOutputStream
    //   5: dup
    //   6: aload_0
    //   7: getfield 24	com/android/launcher3/Stats:mLauncher	Lcom/android/launcher3/Launcher;
    //   10: ldc 115
    //   12: iconst_0
    //   13: invokevirtual 38	com/android/launcher3/Launcher:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
    //   16: invokespecial 41	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   19: astore_2
    //   20: aload_2
    //   21: iconst_1
    //   22: invokevirtual 47	java/io/DataOutputStream:writeInt	(I)V
    //   25: aload_0
    //   26: getfield 81	com/android/launcher3/Stats:mHistogram	Ljava/util/ArrayList;
    //   29: invokevirtual 118	java/util/ArrayList:size	()I
    //   32: istore 11
    //   34: aload_2
    //   35: iload 11
    //   37: invokevirtual 47	java/io/DataOutputStream:writeInt	(I)V
    //   40: iconst_0
    //   41: istore 12
    //   43: iload 12
    //   45: iload 11
    //   47: if_icmpge +44 -> 91
    //   50: aload_2
    //   51: aload_0
    //   52: getfield 79	com/android/launcher3/Stats:mIntents	Ljava/util/ArrayList;
    //   55: iload 12
    //   57: invokevirtual 122	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   60: checkcast 124	java/lang/String
    //   63: invokevirtual 128	java/io/DataOutputStream:writeUTF	(Ljava/lang/String;)V
    //   66: aload_2
    //   67: aload_0
    //   68: getfield 81	com/android/launcher3/Stats:mHistogram	Ljava/util/ArrayList;
    //   71: iload 12
    //   73: invokevirtual 122	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   76: checkcast 105	java/lang/Integer
    //   79: invokevirtual 131	java/lang/Integer:intValue	()I
    //   82: invokevirtual 47	java/io/DataOutputStream:writeInt	(I)V
    //   85: iinc 12 1
    //   88: goto -45 -> 43
    //   91: aload_2
    //   92: invokevirtual 132	java/io/DataOutputStream:close	()V
    //   95: aconst_null
    //   96: astore_1
    //   97: aload_0
    //   98: getfield 24	com/android/launcher3/Stats:mLauncher	Lcom/android/launcher3/Launcher;
    //   101: ldc 115
    //   103: invokevirtual 136	com/android/launcher3/Launcher:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   106: aload_0
    //   107: getfield 24	com/android/launcher3/Stats:mLauncher	Lcom/android/launcher3/Launcher;
    //   110: ldc 85
    //   112: invokevirtual 136	com/android/launcher3/Launcher:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   115: invokevirtual 142	java/io/File:renameTo	(Ljava/io/File;)Z
    //   118: pop
    //   119: iconst_0
    //   120: ifeq +7 -> 127
    //   123: aconst_null
    //   124: invokevirtual 132	java/io/DataOutputStream:close	()V
    //   127: return
    //   128: astore_3
    //   129: ldc 49
    //   131: new 51	java/lang/StringBuilder
    //   134: dup
    //   135: invokespecial 52	java/lang/StringBuilder:<init>	()V
    //   138: ldc 144
    //   140: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: aload_3
    //   144: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   147: invokevirtual 65	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   150: invokestatic 71	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   153: pop
    //   154: aload_1
    //   155: ifnull -28 -> 127
    //   158: aload_1
    //   159: invokevirtual 132	java/io/DataOutputStream:close	()V
    //   162: return
    //   163: astore 7
    //   165: return
    //   166: astore 8
    //   168: ldc 49
    //   170: new 51	java/lang/StringBuilder
    //   173: dup
    //   174: invokespecial 52	java/lang/StringBuilder:<init>	()V
    //   177: ldc 146
    //   179: invokevirtual 58	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: aload 8
    //   184: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   187: invokevirtual 65	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   190: invokestatic 71	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   193: pop
    //   194: aload_1
    //   195: ifnull -68 -> 127
    //   198: aload_1
    //   199: invokevirtual 132	java/io/DataOutputStream:close	()V
    //   202: return
    //   203: astore 10
    //   205: return
    //   206: astore 4
    //   208: aload_1
    //   209: ifnull +7 -> 216
    //   212: aload_1
    //   213: invokevirtual 132	java/io/DataOutputStream:close	()V
    //   216: aload 4
    //   218: athrow
    //   219: astore 14
    //   221: return
    //   222: astore 5
    //   224: goto -8 -> 216
    //   227: astore 4
    //   229: aload_2
    //   230: astore_1
    //   231: goto -23 -> 208
    //   234: astore 8
    //   236: aload_2
    //   237: astore_1
    //   238: goto -70 -> 168
    //   241: astore_3
    //   242: aload_2
    //   243: astore_1
    //   244: goto -115 -> 129
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	247	0	this	Stats
    //   1	243	1	localObject1	Object
    //   19	224	2	localDataOutputStream	DataOutputStream
    //   128	16	3	localFileNotFoundException1	FileNotFoundException
    //   241	1	3	localFileNotFoundException2	FileNotFoundException
    //   206	11	4	localObject2	Object
    //   227	1	4	localObject3	Object
    //   222	1	5	localIOException1	IOException
    //   163	1	7	localIOException2	IOException
    //   166	17	8	localIOException3	IOException
    //   234	1	8	localIOException4	IOException
    //   203	1	10	localIOException5	IOException
    //   32	16	11	i	int
    //   41	45	12	j	int
    //   219	1	14	localIOException6	IOException
    // Exception table:
    //   from	to	target	type
    //   2	20	128	java/io/FileNotFoundException
    //   97	119	128	java/io/FileNotFoundException
    //   158	162	163	java/io/IOException
    //   2	20	166	java/io/IOException
    //   97	119	166	java/io/IOException
    //   198	202	203	java/io/IOException
    //   2	20	206	finally
    //   97	119	206	finally
    //   129	154	206	finally
    //   168	194	206	finally
    //   123	127	219	java/io/IOException
    //   212	216	222	java/io/IOException
    //   20	40	227	finally
    //   50	85	227	finally
    //   91	95	227	finally
    //   20	40	234	java/io/IOException
    //   50	85	234	java/io/IOException
    //   91	95	234	java/io/IOException
    //   20	40	241	java/io/FileNotFoundException
    //   50	85	241	java/io/FileNotFoundException
    //   91	95	241	java/io/FileNotFoundException
  }
  
  public void incrementLaunch(String paramString)
  {
    int i = this.mIntents.indexOf(paramString);
    if (i < 0)
    {
      this.mIntents.add(paramString);
      this.mHistogram.add(Integer.valueOf(1));
      return;
    }
    this.mHistogram.set(i, Integer.valueOf(1 + ((Integer)this.mHistogram.get(i)).intValue()));
  }
  
  public void recordLaunch(Intent paramIntent)
  {
    recordLaunch(paramIntent, null);
  }
  
  public void recordLaunch(Intent paramIntent, ShortcutInfo paramShortcutInfo)
  {
    Intent localIntent1 = new Intent(paramIntent);
    localIntent1.setSourceBounds(null);
    String str = localIntent1.toUri(0);
    Intent localIntent2 = new Intent("com.android.launcher3.action.LAUNCH").putExtra("intent", str);
    if (paramShortcutInfo != null) {
      localIntent2.putExtra("container", paramShortcutInfo.container).putExtra("screen", paramShortcutInfo.screenId).putExtra("cellX", paramShortcutInfo.cellX).putExtra("cellY", paramShortcutInfo.cellY);
    }
    this.mLauncher.sendBroadcast(localIntent2, "com.android.launcher3.permission.RECEIVE_LAUNCH_BROADCASTS");
    incrementLaunch(str);
    saveStats();
    if (this.mLog != null) {
      try
      {
        this.mLog.writeInt(4096);
        this.mLog.writeLong(System.currentTimeMillis());
        if (paramShortcutInfo == null)
        {
          this.mLog.writeShort(0);
          this.mLog.writeShort(0);
          this.mLog.writeShort(0);
          this.mLog.writeShort(0);
        }
        for (;;)
        {
          this.mLog.writeUTF(str);
          this.mLog.flush();
          return;
          this.mLog.writeShort((short)(int)paramShortcutInfo.container);
          this.mLog.writeShort((short)(int)paramShortcutInfo.screenId);
          this.mLog.writeShort((short)paramShortcutInfo.cellX);
          this.mLog.writeShort((short)paramShortcutInfo.cellY);
        }
        return;
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.Stats
 * JD-Core Version:    0.7.0.1
 */