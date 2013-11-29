package com.google.android.libraries.tvdetect.impl;

import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.Device.BuildException;
import com.google.android.libraries.tvdetect.DeviceCache;
import com.google.android.libraries.tvdetect.DeviceFinder;
import com.google.android.libraries.tvdetect.DeviceFinder.Callback;
import com.google.android.libraries.tvdetect.DeviceFinderOptions;
import com.google.android.libraries.tvdetect.ProductInfo;
import com.google.android.libraries.tvdetect.ProductInfoService;
import com.google.android.libraries.tvdetect.net.NetworkAccessor;
import com.google.android.libraries.tvdetect.net.WifiNetwork;
import com.google.android.libraries.tvdetect.util.Clock;
import com.google.android.libraries.tvdetect.util.DeviceUtil;
import com.google.android.libraries.tvdetect.util.HttpFetcher;
import com.google.android.libraries.tvdetect.util.L;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class SsdpDeviceFinder
  implements DeviceFinder
{
  private static final List<DlnaDeviceClass> REQUIRED_DEVICE_CLASSES = ;
  private final ConcurrentMap<String, Boolean> allFoundUuids;
  private DeviceFinder.Callback callback;
  private final Clock clock;
  private final DeviceCache deviceCache;
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
  private final HttpFetcher httpFetcher;
  private final NetworkAccessor networkAccessor;
  private DeviceFinderOptions options;
  private final ProductInfoService productInfoService;
  private long searchStartMillis = 0L;
  
  public SsdpDeviceFinder(NetworkAccessor paramNetworkAccessor, DeviceCache paramDeviceCache, ProductInfoService paramProductInfoService, HttpFetcher paramHttpFetcher, Clock paramClock)
  {
    this.networkAccessor = paramNetworkAccessor;
    this.deviceCache = paramDeviceCache;
    this.productInfoService = paramProductInfoService;
    this.httpFetcher = paramHttpFetcher;
    this.clock = paramClock;
    this.allFoundUuids = new ConcurrentHashMap();
  }
  
  private static void cancelTasks(List<Future<Void>> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext()) {
      ((Future)localIterator.next()).cancel(true);
    }
  }
  
  private String createDiscoveryMessage(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("M-SEARCH * HTTP/1.1\r\n");
    localStringBuffer.append("MX: ").append(1).append("\r\n");
    localStringBuffer.append("ST: ").append(paramString).append("\r\n");
    localStringBuffer.append("HOST: 239.255.255.250:1900\r\n");
    localStringBuffer.append("MAN: \"ssdp:discover\"\r\n");
    localStringBuffer.append("\r\n");
    return localStringBuffer.toString();
  }
  
  private void deviceFound(Device paramDevice)
  {
    try
    {
      if (this.callback != null) {
        this.callback.onDeviceFound(paramDevice);
      }
      return;
    }
    finally {}
  }
  
  private void executeSearch(List<DlnaDeviceClass> paramList)
  {
    WifiNetwork localWifiNetwork = this.networkAccessor.getActiveWifiNetwork(true);
    if (localWifiNetwork == null)
    {
      L.i("No active network for device discovery");
      searchFinished();
      return;
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = getSearchTargetForDeviceClass((DlnaDeviceClass)localIterator.next());
      if (str != null) {
        localArrayList.add(str);
      }
    }
    Future localFuture = scheduleSendSsdpRequests(localWifiNetwork, localArrayList);
    if (localFuture == null)
    {
      searchFinished();
      return;
    }
    final long l1 = this.clock.getCurrentTimeMillis();
    final long l2 = l1 + TimeUnit.SECONDS.toMillis(11L);
    this.executor.schedule(new Runnable()
    {
      public void run()
      {
        try
        {
          long l = Math.max(0L, l2 - SsdpDeviceFinder.this.clock.getCurrentTimeMillis());
          l1.get(l, TimeUnit.MILLISECONDS);
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
          L.w("SSDP device response read task interrupted", localInterruptedException);
          return;
        }
        catch (ExecutionException localExecutionException)
        {
          L.w("SSDP device response read task had error", localExecutionException);
          return;
        }
        catch (TimeoutException localTimeoutException)
        {
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Long.valueOf(SsdpDeviceFinder.this.clock.getCurrentTimeMillis() - this.val$startTimeMillis);
          L.w(String.format("SSDP device response read task timeout after %d millis", arrayOfObject));
          l1.cancel(true);
          return;
        }
        finally
        {
          SsdpDeviceFinder.this.searchFinished();
        }
      }
    }, 5L, TimeUnit.SECONDS);
  }
  
  private static List<DlnaDeviceClass> getRequiredDeviceClasses()
  {
    ArrayList localArrayList = new ArrayList();
    DlnaDeviceClass[] arrayOfDlnaDeviceClass = DlnaDeviceClass.values();
    int i = arrayOfDlnaDeviceClass.length;
    for (int j = 0; j < i; j++) {
      localArrayList.add(arrayOfDlnaDeviceClass[j]);
    }
    return localArrayList;
  }
  
  private String getSearchTargetForDeviceClass(DlnaDeviceClass paramDlnaDeviceClass)
  {
    switch (6.$SwitchMap$com$google$android$libraries$tvdetect$impl$SsdpDeviceFinder$DlnaDeviceClass[paramDlnaDeviceClass.ordinal()])
    {
    default: 
      return null;
    }
    return "urn:schemas-upnp-org:device:MediaRenderer:1";
  }
  
  private DatagramPacket newMSearchPacket(String paramString)
  {
    String str = createDiscoveryMessage(paramString);
    try
    {
      DatagramPacket localDatagramPacket = new DatagramPacket(str.getBytes(), str.getBytes().length, InetAddress.getByName("239.255.255.250"), 1900);
      return localDatagramPacket;
    }
    catch (UnknownHostException localUnknownHostException) {}
    return null;
  }
  
  private void readDeviceResponses(DatagramSocket paramDatagramSocket, List<String> paramList, String paramString)
  {
    List localList = readSsdpReplies(paramDatagramSocket, paramList, paramString);
    long l = this.clock.getCurrentTimeMillis() + TimeUnit.SECONDS.toMillis(5L);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(localList.size());
    L.ifmt("Waiting for %d tasks", arrayOfObject);
    Iterator localIterator = localList.iterator();
    for (;;)
    {
      Future localFuture;
      if (localIterator.hasNext()) {
        localFuture = (Future)localIterator.next();
      }
      try
      {
        localFuture.get(Math.max(0L, l - this.clock.getCurrentTimeMillis()), TimeUnit.MILLISECONDS);
      }
      catch (InterruptedException localInterruptedException)
      {
        L.w("Task cancelled while reading device details");
        cancelTasks(localList);
        return;
      }
      catch (ExecutionException localExecutionException)
      {
        L.e("Error in task while reading device details");
      }
      catch (TimeoutException localTimeoutException)
      {
        L.w("Task timed out while reading device details");
      }
    }
  }
  
  /* Error */
  private List<Future<Void>> readSsdpReplies(DatagramSocket paramDatagramSocket, List<String> paramList, final String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 62	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:clock	Lcom/google/android/libraries/tvdetect/util/Clock;
    //   4: invokeinterface 206 1 0
    //   9: getstatic 212	java/util/concurrent/TimeUnit:SECONDS	Ljava/util/concurrent/TimeUnit;
    //   12: ldc2_w 224
    //   15: invokevirtual 218	java/util/concurrent/TimeUnit:toMillis	(J)J
    //   18: ladd
    //   19: lstore 4
    //   21: sipush 1024
    //   24: newarray byte
    //   26: astore 6
    //   28: new 185	java/util/ArrayList
    //   31: dup
    //   32: invokespecial 186	java/util/ArrayList:<init>	()V
    //   35: astore 7
    //   37: lload 4
    //   39: aload_0
    //   40: getfield 62	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:clock	Lcom/google/android/libraries/tvdetect/util/Clock;
    //   43: invokeinterface 206 1 0
    //   48: lsub
    //   49: lstore 8
    //   51: lload 8
    //   53: lconst_0
    //   54: lcmp
    //   55: ifgt +12 -> 67
    //   58: ldc_w 334
    //   61: invokestatic 183	com/google/android/libraries/tvdetect/util/L:i	(Ljava/lang/String;)V
    //   64: aload 7
    //   66: areturn
    //   67: new 253	java/net/DatagramPacket
    //   70: dup
    //   71: aload 6
    //   73: aload 6
    //   75: arraylength
    //   76: invokespecial 337	java/net/DatagramPacket:<init>	([BI)V
    //   79: astore 10
    //   81: lload 8
    //   83: l2i
    //   84: istore 11
    //   86: aload_1
    //   87: iload 11
    //   89: invokevirtual 343	java/net/DatagramSocket:setSoTimeout	(I)V
    //   92: aload_1
    //   93: aload 10
    //   95: invokevirtual 347	java/net/DatagramSocket:receive	(Ljava/net/DatagramPacket;)V
    //   98: new 255	java/lang/String
    //   101: dup
    //   102: aload 10
    //   104: invokevirtual 350	java/net/DatagramPacket:getData	()[B
    //   107: iconst_0
    //   108: aload 10
    //   110: invokevirtual 353	java/net/DatagramPacket:getLength	()I
    //   113: ldc_w 355
    //   116: invokespecial 358	java/lang/String:<init>	([BIILjava/lang/String;)V
    //   119: astore 15
    //   121: new 360	com/google/android/libraries/tvdetect/impl/SsdpReply
    //   124: dup
    //   125: aload 15
    //   127: invokespecial 362	com/google/android/libraries/tvdetect/impl/SsdpReply:<init>	(Ljava/lang/String;)V
    //   130: astore 16
    //   132: aload 16
    //   134: invokevirtual 365	com/google/android/libraries/tvdetect/impl/SsdpReply:getSearchTarget	()Ljava/lang/String;
    //   137: astore 17
    //   139: aload 17
    //   141: ifnonnull +60 -> 201
    //   144: ldc_w 367
    //   147: invokestatic 313	com/google/android/libraries/tvdetect/util/L:w	(Ljava/lang/String;)V
    //   150: goto -113 -> 37
    //   153: astore 12
    //   155: ldc_w 369
    //   158: aload 12
    //   160: invokestatic 372	com/google/android/libraries/tvdetect/util/L:w	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   163: aload 7
    //   165: areturn
    //   166: astore 14
    //   168: ldc_w 374
    //   171: invokestatic 183	com/google/android/libraries/tvdetect/util/L:i	(Ljava/lang/String;)V
    //   174: aload 7
    //   176: areturn
    //   177: astore 13
    //   179: ldc_w 376
    //   182: aload 13
    //   184: invokestatic 372	com/google/android/libraries/tvdetect/util/L:w	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   187: goto -150 -> 37
    //   190: astore 28
    //   192: ldc_w 378
    //   195: invokestatic 313	com/google/android/libraries/tvdetect/util/L:w	(Ljava/lang/String;)V
    //   198: goto -161 -> 37
    //   201: aload_2
    //   202: aload 17
    //   204: invokeinterface 381 2 0
    //   209: ifne +12 -> 221
    //   212: ldc_w 383
    //   215: invokestatic 313	com/google/android/libraries/tvdetect/util/L:w	(Ljava/lang/String;)V
    //   218: goto -181 -> 37
    //   221: aload 16
    //   223: invokevirtual 386	com/google/android/libraries/tvdetect/impl/SsdpReply:getDeviceUuid	()Ljava/lang/String;
    //   226: astore 18
    //   228: aload 18
    //   230: ifnull +11 -> 241
    //   233: aload 18
    //   235: invokevirtual 389	java/lang/String:isEmpty	()Z
    //   238: ifeq +12 -> 250
    //   241: ldc_w 391
    //   244: invokestatic 313	com/google/android/libraries/tvdetect/util/L:w	(Ljava/lang/String;)V
    //   247: goto -210 -> 37
    //   250: aload 16
    //   252: invokevirtual 394	com/google/android/libraries/tvdetect/impl/SsdpReply:getLocation	()Ljava/lang/String;
    //   255: astore 19
    //   257: aload 19
    //   259: ifnull +11 -> 270
    //   262: aload 19
    //   264: invokevirtual 389	java/lang/String:isEmpty	()Z
    //   267: ifeq +12 -> 279
    //   270: ldc_w 396
    //   273: invokestatic 313	com/google/android/libraries/tvdetect/util/L:w	(Ljava/lang/String;)V
    //   276: goto -239 -> 37
    //   279: aload_0
    //   280: getfield 67	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:allFoundUuids	Ljava/util/concurrent/ConcurrentMap;
    //   283: aload 18
    //   285: iconst_1
    //   286: invokestatic 401	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   289: invokeinterface 407 3 0
    //   294: ifnonnull -257 -> 37
    //   297: iconst_1
    //   298: anewarray 4	java/lang/Object
    //   301: astore 20
    //   303: aload 20
    //   305: iconst_0
    //   306: aload_0
    //   307: getfield 62	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:clock	Lcom/google/android/libraries/tvdetect/util/Clock;
    //   310: invokeinterface 206 1 0
    //   315: aload_0
    //   316: getfield 44	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:searchStartMillis	J
    //   319: lsub
    //   320: invokestatic 412	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   323: aastore
    //   324: ldc_w 414
    //   327: aload 20
    //   329: invokestatic 295	com/google/android/libraries/tvdetect/util/L:ifmt	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   332: aload_0
    //   333: getfield 56	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:deviceCache	Lcom/google/android/libraries/tvdetect/DeviceCache;
    //   336: aload 18
    //   338: invokeinterface 420 2 0
    //   343: astore 21
    //   345: aload 21
    //   347: ifnull +139 -> 486
    //   350: aload 21
    //   352: aload_0
    //   353: getfield 62	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:clock	Lcom/google/android/libraries/tvdetect/util/Clock;
    //   356: aload_0
    //   357: getfield 105	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:options	Lcom/google/android/libraries/tvdetect/DeviceFinderOptions;
    //   360: getfield 426	com/google/android/libraries/tvdetect/DeviceFinderOptions:forceReloadCachedData	Z
    //   363: invokestatic 432	com/google/android/libraries/tvdetect/util/DeviceUtil:mustFetchProductInfoForDevice	(Lcom/google/android/libraries/tvdetect/Device;Lcom/google/android/libraries/tvdetect/util/Clock;Z)Z
    //   366: ifne +120 -> 486
    //   369: aload 21
    //   371: getfield 438	com/google/android/libraries/tvdetect/Device:deviceDescriptionUrl	Ljava/lang/String;
    //   374: aload 19
    //   376: invokevirtual 441	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   379: ifeq +56 -> 435
    //   382: aload 21
    //   384: getfield 444	com/google/android/libraries/tvdetect/Device:networkBssid	Ljava/lang/String;
    //   387: aload_3
    //   388: invokevirtual 441	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   391: ifeq +44 -> 435
    //   394: aload 21
    //   396: astore 26
    //   398: aload 26
    //   400: ifnull -363 -> 37
    //   403: aload_0
    //   404: getfield 105	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:options	Lcom/google/android/libraries/tvdetect/DeviceFinderOptions;
    //   407: getfield 448	com/google/android/libraries/tvdetect/DeviceFinderOptions:wantedProductTypes	Ljava/util/Set;
    //   410: aload 26
    //   412: getfield 452	com/google/android/libraries/tvdetect/Device:productInfo	Lcom/google/android/libraries/tvdetect/ProductInfo;
    //   415: getfield 458	com/google/android/libraries/tvdetect/ProductInfo:type	Lcom/google/android/libraries/tvdetect/ProductType;
    //   418: invokeinterface 461 2 0
    //   423: ifeq -386 -> 37
    //   426: aload_0
    //   427: aload 26
    //   429: invokespecial 111	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:deviceFound	(Lcom/google/android/libraries/tvdetect/Device;)V
    //   432: goto -395 -> 37
    //   435: aload 21
    //   437: invokestatic 465	com/google/android/libraries/tvdetect/Device:newBuilder	(Lcom/google/android/libraries/tvdetect/Device;)Lcom/google/android/libraries/tvdetect/Device$Builder;
    //   440: aload_3
    //   441: invokevirtual 471	com/google/android/libraries/tvdetect/Device$Builder:setNetworkBssid	(Ljava/lang/String;)Lcom/google/android/libraries/tvdetect/Device$Builder;
    //   444: aload 19
    //   446: invokevirtual 474	com/google/android/libraries/tvdetect/Device$Builder:setDeviceDescriptionUrl	(Ljava/lang/String;)Lcom/google/android/libraries/tvdetect/Device$Builder;
    //   449: invokevirtual 478	com/google/android/libraries/tvdetect/Device$Builder:build	()Lcom/google/android/libraries/tvdetect/Device;
    //   452: astore 27
    //   454: aload_0
    //   455: getfield 56	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:deviceCache	Lcom/google/android/libraries/tvdetect/DeviceCache;
    //   458: aload 27
    //   460: invokeinterface 481 2 0
    //   465: aload 27
    //   467: astore 26
    //   469: goto -71 -> 398
    //   472: astore 25
    //   474: ldc_w 483
    //   477: invokestatic 320	com/google/android/libraries/tvdetect/util/L:e	(Ljava/lang/String;)V
    //   480: aconst_null
    //   481: astore 26
    //   483: goto -85 -> 398
    //   486: aload_0
    //   487: getfield 52	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:executor	Ljava/util/concurrent/ScheduledExecutorService;
    //   490: astore 22
    //   492: new 485	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder$4
    //   495: dup
    //   496: aload_0
    //   497: aload 19
    //   499: aload 18
    //   501: aload_3
    //   502: invokespecial 488	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder$4:<init>	(Lcom/google/android/libraries/tvdetect/impl/SsdpDeviceFinder;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   505: astore 23
    //   507: aload 7
    //   509: aload 22
    //   511: aload 23
    //   513: invokeinterface 492 2 0
    //   518: invokeinterface 196 2 0
    //   523: pop
    //   524: goto -487 -> 37
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	527	0	this	SsdpDeviceFinder
    //   0	527	1	paramDatagramSocket	DatagramSocket
    //   0	527	2	paramList	List<String>
    //   0	527	3	paramString	String
    //   19	19	4	l1	long
    //   26	48	6	arrayOfByte	byte[]
    //   35	473	7	localArrayList	ArrayList
    //   49	33	8	l2	long
    //   79	30	10	localDatagramPacket	DatagramPacket
    //   84	4	11	i	int
    //   153	6	12	localSocketException	java.net.SocketException
    //   177	6	13	localIOException	IOException
    //   166	1	14	localSocketTimeoutException	java.net.SocketTimeoutException
    //   119	7	15	str1	String
    //   130	121	16	localSsdpReply	SsdpReply
    //   137	66	17	str2	String
    //   226	274	18	str3	String
    //   255	243	19	str4	String
    //   301	27	20	arrayOfObject	Object[]
    //   343	93	21	localDevice1	Device
    //   490	20	22	localScheduledExecutorService	ScheduledExecutorService
    //   505	7	23	local4	4
    //   472	1	25	localBuildException	Device.BuildException
    //   396	86	26	localObject	Object
    //   452	14	27	localDevice2	Device
    //   190	1	28	localUnsupportedEncodingException	java.io.UnsupportedEncodingException
    // Exception table:
    //   from	to	target	type
    //   86	92	153	java/net/SocketException
    //   92	98	166	java/net/SocketTimeoutException
    //   92	98	177	java/io/IOException
    //   98	121	190	java/io/UnsupportedEncodingException
    //   435	465	472	com/google/android/libraries/tvdetect/Device$BuildException
  }
  
  private Future<?> scheduleSendSsdpRequests(final WifiNetwork paramWifiNetwork, final List<String> paramList)
  {
    try
    {
      final MulticastSocket localMulticastSocket = paramWifiNetwork.newMulticastSocket();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        final String str = (String)localIterator.next();
        for (int i = 0; i < 2; i++) {
          this.executor.schedule(new Runnable()
          {
            public void run()
            {
              try
              {
                DatagramPacket localDatagramPacket = SsdpDeviceFinder.this.newMSearchPacket(str);
                if (localDatagramPacket != null)
                {
                  L.i("Sending search packet");
                  localMulticastSocket.send(localDatagramPacket);
                }
                return;
              }
              catch (IOException localIOException)
              {
                L.e("Error sending msearch");
              }
            }
          }, i * 500, TimeUnit.MILLISECONDS);
        }
      }
      this.executor.submit(new Runnable()
      {
        public void run()
        {
          SsdpDeviceFinder.this.readDeviceResponses(localMulticastSocket, paramList, paramWifiNetwork.getBssid());
        }
      });
    }
    catch (IOException localIOException)
    {
      L.w("Error creating socket on interface", localIOException);
      return null;
    }
  }
  
  private void searchFinished()
  {
    try
    {
      if (this.callback != null)
      {
        this.callback.onProgressChanged(1, 1);
        this.callback = null;
      }
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Long.valueOf(this.clock.getCurrentTimeMillis() - this.searchStartMillis);
      L.ifmt("SSDP device search finished in %d millis", arrayOfObject);
      this.executor.shutdownNow();
      return;
    }
    finally {}
  }
  
  private boolean setSearchStartMillisOnce(long paramLong)
  {
    try
    {
      if (this.searchStartMillis == 0L)
      {
        this.searchStartMillis = paramLong;
        return true;
      }
      return false;
    }
    finally {}
  }
  
  public boolean search(DeviceFinder.Callback paramCallback, DeviceFinderOptions paramDeviceFinderOptions)
  {
    L.i("Starting SSDP device search");
    if (!setSearchStartMillisOnce(this.clock.getCurrentTimeMillis()))
    {
      L.e("SSDP search started more than once.");
      return false;
    }
    this.allFoundUuids.clear();
    this.callback = paramCallback;
    this.options = paramDeviceFinderOptions;
    this.executor.execute(new Runnable()
    {
      public void run()
      {
        try
        {
          SsdpDeviceFinder.this.executeSearch(SsdpDeviceFinder.REQUIRED_DEVICE_CLASSES);
          return;
        }
        catch (Exception localException)
        {
          L.e("SSDP executeSearch failed");
          SsdpDeviceFinder.this.searchFinished();
        }
      }
    });
    return true;
  }
  
  /* Error */
  public void stopSearch()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc_w 546
    //   5: invokestatic 183	com/google/android/libraries/tvdetect/util/L:i	(Ljava/lang/String;)V
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: aconst_null
    //   12: putfield 164	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:callback	Lcom/google/android/libraries/tvdetect/DeviceFinder$Callback;
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_0
    //   18: getfield 52	com/google/android/libraries/tvdetect/impl/SsdpDeviceFinder:executor	Ljava/util/concurrent/ScheduledExecutorService;
    //   21: invokeinterface 522 1 0
    //   26: pop
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_2
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_2
    //   34: athrow
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	40	0	this	SsdpDeviceFinder
    //   35	4	1	localObject1	Object
    //   30	4	2	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   10	17	30	finally
    //   31	33	30	finally
    //   2	10	35	finally
    //   17	27	35	finally
    //   33	35	35	finally
  }
  
  private static enum DlnaDeviceClass
  {
    static
    {
      DlnaDeviceClass[] arrayOfDlnaDeviceClass = new DlnaDeviceClass[1];
      arrayOfDlnaDeviceClass[0] = MEDIA_RENDERER;
      $VALUES = arrayOfDlnaDeviceClass;
    }
    
    private DlnaDeviceClass() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.impl.SsdpDeviceFinder
 * JD-Core Version:    0.7.0.1
 */