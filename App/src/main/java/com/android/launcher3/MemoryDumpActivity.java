package com.android.launcher3;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MemoryDumpActivity
  extends Activity
{
  private static final String TAG = "MemoryDumpActivity";
  
  public static void dumpHprofAndShare(Context paramContext, MemoryTracker paramMemoryTracker)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    ArrayList localArrayList = new ArrayList();
    int i = Process.myPid();
    int[] arrayOfInt1 = paramMemoryTracker.getTrackedProcesses();
    int[] arrayOfInt2 = Arrays.copyOf(arrayOfInt1, arrayOfInt1.length);
    int j = arrayOfInt2.length;
    int k = 0;
    for (;;)
    {
      if (k < j)
      {
        int m = arrayOfInt2[k];
        MemoryTracker.ProcessMemInfo localProcessMemInfo = paramMemoryTracker.getMemInfo(m);
        if (localProcessMemInfo != null) {
          localStringBuilder.append("pid ").append(m).append(":").append(" up=").append(localProcessMemInfo.getUptime()).append(" pss=").append(localProcessMemInfo.currentPss).append(" uss=").append(localProcessMemInfo.currentUss).append("\n");
        }
        String str3;
        if (m == i)
        {
          Object[] arrayOfObject2 = new Object[2];
          arrayOfObject2[0] = Environment.getExternalStorageDirectory();
          arrayOfObject2[1] = Integer.valueOf(m);
          str3 = String.format("%s/launcher-memory-%d.ahprof", arrayOfObject2);
          Log.v("MemoryDumpActivity", "Dumping memory info for process " + m + " to " + str3);
        }
        try
        {
          Debug.dumpHprofData(str3);
          localArrayList.add(str3);
          k++;
        }
        catch (IOException localIOException)
        {
          for (;;)
          {
            Log.e("MemoryDumpActivity", "error dumping memory:", localIOException);
          }
        }
      }
    }
    String str1 = zipUp(localArrayList);
    if (str1 == null) {
      return;
    }
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("application/zip");
    PackageManager localPackageManager = paramContext.getPackageManager();
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = Integer.valueOf(i);
    localIntent.putExtra("android.intent.extra.SUBJECT", String.format("Launcher memory dump (%d)", arrayOfObject1));
    try
    {
      str2 = localPackageManager.getPackageInfo(paramContext.getPackageName(), 0).versionName;
      localStringBuilder.append("\nApp version: ").append(str2).append("\nBuild: ").append(Build.DISPLAY).append("\n");
      localIntent.putExtra("android.intent.extra.TEXT", localStringBuilder.toString());
      localIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(str1)));
      paramContext.startActivity(localIntent);
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        String str2 = "?";
      }
    }
  }
  
  public static void startDump(Context paramContext)
  {
    startDump(paramContext, null);
  }
  
  public static void startDump(Context paramContext, final Runnable paramRunnable)
  {
    ServiceConnection local2 = new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        Log.v("MemoryDumpActivity", "service connected, dumping...");
        MemoryDumpActivity.dumpHprofAndShare(this.val$context, ((MemoryTracker.MemoryTrackerInterface)paramAnonymousIBinder).getService());
        this.val$context.unbindService(this);
        if (paramRunnable != null) {
          paramRunnable.run();
        }
      }
      
      public void onServiceDisconnected(ComponentName paramAnonymousComponentName) {}
    };
    Log.v("MemoryDumpActivity", "attempting to bind to memory tracker");
    paramContext.bindService(new Intent(paramContext, MemoryTracker.class), local2, 1);
  }
  
  /* Error */
  public static String zipUp(ArrayList<String> paramArrayList)
  {
    // Byte code:
    //   0: ldc 227
    //   2: newarray byte
    //   4: astore_1
    //   5: iconst_2
    //   6: anewarray 83	java/lang/Object
    //   9: astore_2
    //   10: aload_2
    //   11: iconst_0
    //   12: invokestatic 89	android/os/Environment:getExternalStorageDirectory	()Ljava/io/File;
    //   15: aastore
    //   16: aload_2
    //   17: iconst_1
    //   18: invokestatic 232	java/lang/System:currentTimeMillis	()J
    //   21: invokestatic 237	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   24: aastore
    //   25: ldc 239
    //   27: aload_2
    //   28: invokestatic 103	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   31: astore_3
    //   32: aconst_null
    //   33: astore 4
    //   35: new 241	java/util/zip/ZipOutputStream
    //   38: dup
    //   39: new 243	java/io/BufferedOutputStream
    //   42: dup
    //   43: new 245	java/io/FileOutputStream
    //   46: dup
    //   47: aload_3
    //   48: invokespecial 246	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   51: invokespecial 249	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   54: invokespecial 250	java/util/zip/ZipOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   57: astore 5
    //   59: aload_0
    //   60: invokevirtual 254	java/util/ArrayList:iterator	()Ljava/util/Iterator;
    //   63: astore 11
    //   65: aload 11
    //   67: invokeinterface 260 1 0
    //   72: ifeq +150 -> 222
    //   75: aload 11
    //   77: invokeinterface 264 1 0
    //   82: checkcast 99	java/lang/String
    //   85: astore 13
    //   87: new 266	java/io/BufferedInputStream
    //   90: dup
    //   91: new 268	java/io/FileInputStream
    //   94: dup
    //   95: aload 13
    //   97: invokespecial 269	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   100: invokespecial 272	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   103: astore 14
    //   105: aload 5
    //   107: new 274	java/util/zip/ZipEntry
    //   110: dup
    //   111: aload 13
    //   113: invokespecial 275	java/util/zip/ZipEntry:<init>	(Ljava/lang/String;)V
    //   116: invokevirtual 279	java/util/zip/ZipOutputStream:putNextEntry	(Ljava/util/zip/ZipEntry;)V
    //   119: aload 14
    //   121: aload_1
    //   122: iconst_0
    //   123: ldc 227
    //   125: invokevirtual 285	java/io/InputStream:read	([BII)I
    //   128: istore 17
    //   130: iload 17
    //   132: ifle +58 -> 190
    //   135: aload 5
    //   137: aload_1
    //   138: iconst_0
    //   139: iload 17
    //   141: invokevirtual 289	java/util/zip/ZipOutputStream:write	([BII)V
    //   144: goto -25 -> 119
    //   147: astore 15
    //   149: aload 14
    //   151: astore 16
    //   153: aload 16
    //   155: invokevirtual 292	java/io/InputStream:close	()V
    //   158: aload 15
    //   160: athrow
    //   161: astore 8
    //   163: aload 5
    //   165: astore 4
    //   167: ldc 8
    //   169: ldc_w 294
    //   172: aload 8
    //   174: invokestatic 133	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   177: pop
    //   178: aload 4
    //   180: ifnull +8 -> 188
    //   183: aload 4
    //   185: invokevirtual 295	java/util/zip/ZipOutputStream:close	()V
    //   188: aconst_null
    //   189: areturn
    //   190: aload 5
    //   192: invokevirtual 298	java/util/zip/ZipOutputStream:closeEntry	()V
    //   195: aload 14
    //   197: invokevirtual 292	java/io/InputStream:close	()V
    //   200: goto -135 -> 65
    //   203: astore 6
    //   205: aload 5
    //   207: astore 4
    //   209: aload 4
    //   211: ifnull +8 -> 219
    //   214: aload 4
    //   216: invokevirtual 295	java/util/zip/ZipOutputStream:close	()V
    //   219: aload 6
    //   221: athrow
    //   222: aload 5
    //   224: ifnull +8 -> 232
    //   227: aload 5
    //   229: invokevirtual 295	java/util/zip/ZipOutputStream:close	()V
    //   232: aload_3
    //   233: areturn
    //   234: astore 12
    //   236: goto -4 -> 232
    //   239: astore 10
    //   241: aconst_null
    //   242: areturn
    //   243: astore 7
    //   245: goto -26 -> 219
    //   248: astore 6
    //   250: goto -41 -> 209
    //   253: astore 8
    //   255: aconst_null
    //   256: astore 4
    //   258: goto -91 -> 167
    //   261: astore 15
    //   263: aconst_null
    //   264: astore 16
    //   266: goto -113 -> 153
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	269	0	paramArrayList	ArrayList<String>
    //   4	134	1	arrayOfByte	byte[]
    //   9	19	2	arrayOfObject	Object[]
    //   31	202	3	str1	String
    //   33	224	4	localObject1	Object
    //   57	171	5	localZipOutputStream	java.util.zip.ZipOutputStream
    //   203	17	6	localObject2	Object
    //   248	1	6	localObject3	Object
    //   243	1	7	localIOException1	IOException
    //   161	12	8	localIOException2	IOException
    //   253	1	8	localIOException3	IOException
    //   239	1	10	localIOException4	IOException
    //   63	13	11	localIterator	java.util.Iterator
    //   234	1	12	localIOException5	IOException
    //   85	27	13	str2	String
    //   103	93	14	localBufferedInputStream1	java.io.BufferedInputStream
    //   147	12	15	localObject4	Object
    //   261	1	15	localObject5	Object
    //   151	114	16	localBufferedInputStream2	java.io.BufferedInputStream
    //   128	12	17	i	int
    // Exception table:
    //   from	to	target	type
    //   105	119	147	finally
    //   119	130	147	finally
    //   135	144	147	finally
    //   190	195	147	finally
    //   59	65	161	java/io/IOException
    //   65	87	161	java/io/IOException
    //   153	161	161	java/io/IOException
    //   195	200	161	java/io/IOException
    //   59	65	203	finally
    //   65	87	203	finally
    //   153	161	203	finally
    //   195	200	203	finally
    //   227	232	234	java/io/IOException
    //   183	188	239	java/io/IOException
    //   214	219	243	java/io/IOException
    //   35	59	248	finally
    //   167	178	248	finally
    //   35	59	253	java/io/IOException
    //   87	105	261	finally
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }
  
  public void onStart()
  {
    super.onStart();
    startDump(this, new Runnable()
    {
      public void run()
      {
        MemoryDumpActivity.this.finish();
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.MemoryDumpActivity
 * JD-Core Version:    0.7.0.1
 */