package com.google.analytics.tracking.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.Command;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

class GAThread
  extends Thread
  implements AnalyticsThread
{
  private static GAThread sInstance;
  private volatile boolean mAppOptOut;
  private volatile String mClientId;
  private volatile boolean mClosed = false;
  private volatile List<Command> mCommands;
  private final Context mContext;
  private volatile boolean mDisabled = false;
  private volatile String mInstallCampaign;
  private volatile MetaModel mMetaModel;
  private volatile ServiceProxy mServiceProxy;
  private final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue();
  
  private GAThread(Context paramContext)
  {
    super("GAThread");
    if (paramContext != null) {}
    for (this.mContext = paramContext.getApplicationContext();; this.mContext = paramContext)
    {
      start();
      return;
    }
  }
  
  private void fillAppParameters(Map<String, String> paramMap)
  {
    PackageManager localPackageManager = this.mContext.getPackageManager();
    String str1 = this.mContext.getPackageName();
    String str2 = localPackageManager.getInstallerPackageName(str1);
    str3 = str1;
    try
    {
      PackageInfo localPackageInfo = localPackageManager.getPackageInfo(this.mContext.getPackageName(), 0);
      str4 = null;
      if (localPackageInfo != null)
      {
        str3 = localPackageManager.getApplicationLabel(localPackageInfo.applicationInfo).toString();
        str4 = localPackageInfo.versionName;
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        Log.e("Error retrieving package info: appName set to " + str3);
        String str4 = null;
      }
    }
    putIfAbsent(paramMap, "appName", str3);
    putIfAbsent(paramMap, "appVersion", str4);
    putIfAbsent(paramMap, "appId", str1);
    putIfAbsent(paramMap, "appInstallerId", str2);
    paramMap.put("apiVersion", "1");
  }
  
  private void fillCampaignParameters(Map<String, String> paramMap)
  {
    String str = Utils.filterCampaign((String)paramMap.get("campaign"));
    if (TextUtils.isEmpty(str)) {
      return;
    }
    Map localMap = Utils.parseURLParameters(str);
    paramMap.put("campaignContent", localMap.get("utm_content"));
    paramMap.put("campaignMedium", localMap.get("utm_medium"));
    paramMap.put("campaignName", localMap.get("utm_campaign"));
    paramMap.put("campaignSource", localMap.get("utm_source"));
    paramMap.put("campaignKeyword", localMap.get("utm_term"));
    paramMap.put("campaignId", localMap.get("utm_id"));
    paramMap.put("gclid", localMap.get("gclid"));
    paramMap.put("dclid", localMap.get("dclid"));
    paramMap.put("gmob_t", localMap.get("gmob_t"));
  }
  
  private void fillExceptionParameters(Map<String, String> paramMap)
  {
    String str = (String)paramMap.get("rawException");
    if (str == null) {}
    for (;;)
    {
      return;
      paramMap.remove("rawException");
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(Utils.hexDecode(str));
      try
      {
        ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
        Object localObject = localObjectInputStream.readObject();
        localObjectInputStream.close();
        if ((localObject instanceof Throwable))
        {
          Throwable localThrowable = (Throwable)localObject;
          ArrayList localArrayList = new ArrayList();
          paramMap.put("exDescription", new StandardExceptionParser(this.mContext, localArrayList).getDescription((String)paramMap.get("exceptionThreadName"), localThrowable));
          return;
        }
      }
      catch (IOException localIOException)
      {
        Log.w("IOException reading exception");
        return;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        Log.w("ClassNotFoundException reading exception");
      }
    }
  }
  
  private String generateClientId()
  {
    String str = UUID.randomUUID().toString().toLowerCase();
    if (!storeClientId(str)) {
      str = "0";
    }
    return str;
  }
  
  static String getAndClearCampaign(Context paramContext)
  {
    try
    {
      FileInputStream localFileInputStream = paramContext.openFileInput("gaInstallData");
      byte[] arrayOfByte = new byte[8192];
      int i = localFileInputStream.read(arrayOfByte, 0, 8192);
      if (localFileInputStream.available() > 0)
      {
        Log.e("Too much campaign data, ignoring it.");
        localFileInputStream.close();
        paramContext.deleteFile("gaInstallData");
        return null;
      }
      localFileInputStream.close();
      paramContext.deleteFile("gaInstallData");
      if (i <= 0)
      {
        Log.w("Campaign file is empty.");
        return null;
      }
      String str = new String(arrayOfByte, 0, i);
      Log.i("Campaign found: " + str);
      return str;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      Log.i("No campaign data found.");
      return null;
    }
    catch (IOException localIOException)
    {
      Log.e("Error reading campaign data.");
      paramContext.deleteFile("gaInstallData");
    }
    return null;
  }
  
  private String getHostUrl(Map<String, String> paramMap)
  {
    String str = (String)paramMap.get("internalHitUrl");
    if (str == null)
    {
      if (!paramMap.containsKey("useSecure")) {
        break label57;
      }
      if (Utils.safeParseBoolean((String)paramMap.get("useSecure"))) {
        str = "https://ssl.google-analytics.com/collect";
      }
    }
    else
    {
      return str;
    }
    return "http://www.google-analytics.com/collect";
    label57:
    return "https://ssl.google-analytics.com/collect";
  }
  
  static GAThread getInstance(Context paramContext)
  {
    if (sInstance == null) {
      sInstance = new GAThread(paramContext);
    }
    return sInstance;
  }
  
  private boolean isSampledOut(Map<String, String> paramMap)
  {
    if (paramMap.get("sampleRate") != null)
    {
      double d = Utils.safeParseDouble((String)paramMap.get("sampleRate"));
      if (d <= 0.0D) {
        return true;
      }
      if (d < 100.0D)
      {
        String str = (String)paramMap.get("clientId");
        if ((str != null) && (Math.abs(str.hashCode()) % 10000 >= 100.0D * d)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean loadAppOptOut()
  {
    return this.mContext.getFileStreamPath("gaOptOut").exists();
  }
  
  private String printStackTrace(Throwable paramThrowable)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream localPrintStream = new PrintStream(localByteArrayOutputStream);
    paramThrowable.printStackTrace(localPrintStream);
    localPrintStream.flush();
    return new String(localByteArrayOutputStream.toByteArray());
  }
  
  private void putIfAbsent(Map<String, String> paramMap, String paramString1, String paramString2)
  {
    if (!paramMap.containsKey(paramString1)) {
      paramMap.put(paramString1, paramString2);
    }
  }
  
  private void queueToThread(Runnable paramRunnable)
  {
    this.queue.add(paramRunnable);
  }
  
  private boolean storeClientId(String paramString)
  {
    try
    {
      FileOutputStream localFileOutputStream = this.mContext.openFileOutput("gaClientId", 0);
      localFileOutputStream.write(paramString.getBytes());
      localFileOutputStream.close();
      return true;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      Log.e("Error creating clientId file.");
      return false;
    }
    catch (IOException localIOException)
    {
      Log.e("Error writing to clientId file.");
    }
    return false;
  }
  
  public void dispatch()
  {
    queueToThread(new Runnable()
    {
      public void run()
      {
        GAThread.this.mServiceProxy.dispatch();
      }
    });
  }
  
  public LinkedBlockingQueue<Runnable> getQueue()
  {
    return this.queue;
  }
  
  public Thread getThread()
  {
    return this;
  }
  
  protected void init()
  {
    this.mServiceProxy.createService();
    this.mCommands = new ArrayList();
    this.mCommands.add(new Command("appendVersion", "_v", "ma1b6"));
    this.mCommands.add(new Command("appendQueueTime", "qt", null));
    this.mCommands.add(new Command("appendCacheBuster", "z", null));
    this.mMetaModel = new MetaModel();
    MetaModelInitializer.set(this.mMetaModel);
  }
  
  /* Error */
  String initializeClientId()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: getfield 53	com/google/analytics/tracking/android/GAThread:mContext	Landroid/content/Context;
    //   6: ldc_w 431
    //   9: invokevirtual 320	android/content/Context:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   12: astore 9
    //   14: sipush 128
    //   17: newarray byte
    //   19: astore 10
    //   21: aload 9
    //   23: aload 10
    //   25: iconst_0
    //   26: sipush 128
    //   29: invokevirtual 326	java/io/FileInputStream:read	([BII)I
    //   32: istore 11
    //   34: aload 9
    //   36: invokevirtual 330	java/io/FileInputStream:available	()I
    //   39: ifle +26 -> 65
    //   42: ldc_w 502
    //   45: invokestatic 183	com/google/analytics/tracking/android/Log:e	(Ljava/lang/String;)I
    //   48: pop
    //   49: aload 9
    //   51: invokevirtual 333	java/io/FileInputStream:close	()V
    //   54: aload_0
    //   55: getfield 53	com/google/analytics/tracking/android/GAThread:mContext	Landroid/content/Context;
    //   58: ldc_w 316
    //   61: invokevirtual 336	android/content/Context:deleteFile	(Ljava/lang/String;)Z
    //   64: pop
    //   65: iload 11
    //   67: ifgt +37 -> 104
    //   70: ldc_w 504
    //   73: invokestatic 183	com/google/analytics/tracking/android/Log:e	(Ljava/lang/String;)I
    //   76: pop
    //   77: aload 9
    //   79: invokevirtual 333	java/io/FileInputStream:close	()V
    //   82: aload_0
    //   83: getfield 53	com/google/analytics/tracking/android/GAThread:mContext	Landroid/content/Context;
    //   86: ldc_w 316
    //   89: invokevirtual 336	android/content/Context:deleteFile	(Ljava/lang/String;)Z
    //   92: pop
    //   93: aload_1
    //   94: ifnonnull +8 -> 102
    //   97: aload_0
    //   98: invokespecial 506	com/google/analytics/tracking/android/GAThread:generateClientId	()Ljava/lang/String;
    //   101: astore_1
    //   102: aload_1
    //   103: areturn
    //   104: new 191	java/lang/String
    //   107: dup
    //   108: aload 10
    //   110: iconst_0
    //   111: iload 11
    //   113: invokespecial 341	java/lang/String:<init>	([BII)V
    //   116: astore 14
    //   118: aload 9
    //   120: invokevirtual 333	java/io/FileInputStream:close	()V
    //   123: aload 14
    //   125: astore_1
    //   126: goto -33 -> 93
    //   129: astore 6
    //   131: ldc_w 508
    //   134: invokestatic 183	com/google/analytics/tracking/android/Log:e	(Ljava/lang/String;)I
    //   137: pop
    //   138: aload_0
    //   139: getfield 53	com/google/analytics/tracking/android/GAThread:mContext	Landroid/content/Context;
    //   142: ldc_w 316
    //   145: invokevirtual 336	android/content/Context:deleteFile	(Ljava/lang/String;)Z
    //   148: pop
    //   149: goto -56 -> 93
    //   152: astore_3
    //   153: ldc_w 510
    //   156: invokestatic 183	com/google/analytics/tracking/android/Log:e	(Ljava/lang/String;)I
    //   159: pop
    //   160: aload_0
    //   161: getfield 53	com/google/analytics/tracking/android/GAThread:mContext	Landroid/content/Context;
    //   164: ldc_w 316
    //   167: invokevirtual 336	android/content/Context:deleteFile	(Ljava/lang/String;)Z
    //   170: pop
    //   171: goto -78 -> 93
    //   174: astore 17
    //   176: aload 14
    //   178: astore_1
    //   179: goto -26 -> 153
    //   182: astore 16
    //   184: aload 14
    //   186: astore_1
    //   187: goto -56 -> 131
    //   190: astore_2
    //   191: aconst_null
    //   192: astore_1
    //   193: goto -100 -> 93
    //   196: astore 15
    //   198: aload 14
    //   200: astore_1
    //   201: goto -108 -> 93
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	204	0	this	GAThread
    //   1	200	1	localObject	Object
    //   190	1	2	localFileNotFoundException1	FileNotFoundException
    //   152	1	3	localNumberFormatException1	java.lang.NumberFormatException
    //   129	1	6	localIOException1	IOException
    //   12	107	9	localFileInputStream	FileInputStream
    //   19	90	10	arrayOfByte	byte[]
    //   32	80	11	i	int
    //   116	83	14	str	String
    //   196	1	15	localFileNotFoundException2	FileNotFoundException
    //   182	1	16	localIOException2	IOException
    //   174	1	17	localNumberFormatException2	java.lang.NumberFormatException
    // Exception table:
    //   from	to	target	type
    //   2	65	129	java/io/IOException
    //   70	93	129	java/io/IOException
    //   104	118	129	java/io/IOException
    //   2	65	152	java/lang/NumberFormatException
    //   70	93	152	java/lang/NumberFormatException
    //   104	118	152	java/lang/NumberFormatException
    //   118	123	174	java/lang/NumberFormatException
    //   118	123	182	java/io/IOException
    //   2	65	190	java/io/FileNotFoundException
    //   70	93	190	java/io/FileNotFoundException
    //   104	118	190	java/io/FileNotFoundException
    //   118	123	196	java/io/FileNotFoundException
  }
  
  public void requestAppOptOut(final GoogleAnalytics.AppOptOutCallback paramAppOptOutCallback)
  {
    queueToThread(new Runnable()
    {
      public void run()
      {
        paramAppOptOutCallback.reportAppOptOut(GAThread.this.mAppOptOut);
      }
    });
  }
  
  public void requestClientId(final AnalyticsThread.ClientIdCallback paramClientIdCallback)
  {
    queueToThread(new Runnable()
    {
      public void run()
      {
        paramClientIdCallback.reportClientId(GAThread.this.mClientId);
      }
    });
  }
  
  public void run()
  {
    try
    {
      Thread.sleep(5000L);
      for (;;)
      {
        try
        {
          if (this.mServiceProxy == null) {
            this.mServiceProxy = new GAServiceProxy(this.mContext, this);
          }
          init();
          this.mAppOptOut = loadAppOptOut();
          this.mClientId = initializeClientId();
          this.mInstallCampaign = getAndClearCampaign(this.mContext);
        }
        catch (Throwable localThrowable1)
        {
          Log.e("Error initializing the GAThread: " + printStackTrace(localThrowable1));
          Log.e("Google Analytics will not start up.");
          this.mDisabled = true;
          continue;
        }
        if (this.mClosed) {
          return;
        }
        try
        {
          Runnable localRunnable = (Runnable)this.queue.take();
          if (!this.mDisabled) {
            localRunnable.run();
          }
        }
        catch (InterruptedException localInterruptedException2)
        {
          Log.i(localInterruptedException2.toString());
        }
        catch (Throwable localThrowable2)
        {
          Log.e("Error on GAThread: " + printStackTrace(localThrowable2));
          Log.e("Google Analytics is shutting down.");
          this.mDisabled = true;
        }
      }
    }
    catch (InterruptedException localInterruptedException1)
    {
      for (;;)
      {
        Log.w("sleep interrupted in GAThread initialize");
      }
    }
  }
  
  public void sendHit(Map<String, String> paramMap)
  {
    final HashMap localHashMap = new HashMap(paramMap);
    final long l = System.currentTimeMillis();
    localHashMap.put("hitTime", Long.toString(l));
    queueToThread(new Runnable()
    {
      public void run()
      {
        localHashMap.put("clientId", GAThread.this.mClientId);
        if ((GAThread.this.mAppOptOut) || (GAThread.this.isSampledOut(localHashMap))) {
          return;
        }
        if (!TextUtils.isEmpty(GAThread.this.mInstallCampaign))
        {
          localHashMap.put("campaign", GAThread.this.mInstallCampaign);
          GAThread.access$302(GAThread.this, null);
        }
        GAThread.this.fillAppParameters(localHashMap);
        GAThread.this.fillCampaignParameters(localHashMap);
        GAThread.this.fillExceptionParameters(localHashMap);
        Map localMap = HitBuilder.generateHitParams(GAThread.this.mMetaModel, localHashMap);
        GAThread.this.mServiceProxy.putHit(localMap, l, GAThread.this.getHostUrl(localHashMap), GAThread.this.mCommands);
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.GAThread
 * JD-Core Version:    0.7.0.1
 */