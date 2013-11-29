package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConnectionPool
{
  private static final ConnectionPool systemDefault;
  private final LinkedList<Connection> connections = new LinkedList();
  private final Callable<Void> connectionsCleanupCallable = new Callable()
  {
    public Void call()
      throws Exception
    {
      ArrayList localArrayList = new ArrayList(2);
      int i = 0;
      synchronized (ConnectionPool.this)
      {
        ListIterator localListIterator1 = ConnectionPool.this.connections.listIterator(ConnectionPool.this.connections.size());
        for (;;)
        {
          Connection localConnection2;
          if (localListIterator1.hasPrevious())
          {
            localConnection2 = (Connection)localListIterator1.previous();
            if ((!localConnection2.isAlive()) || (localConnection2.isExpired(ConnectionPool.this.keepAliveDurationNs)))
            {
              localListIterator1.remove();
              localArrayList.add(localConnection2);
              if (localArrayList.size() != 2) {
                continue;
              }
            }
          }
          else
          {
            ListIterator localListIterator2 = ConnectionPool.this.connections.listIterator(ConnectionPool.this.connections.size());
            while ((localListIterator2.hasPrevious()) && (i > ConnectionPool.this.maxIdleConnections))
            {
              Connection localConnection1 = (Connection)localListIterator2.previous();
              if (localConnection1.isIdle())
              {
                localArrayList.add(localConnection1);
                localListIterator2.remove();
                i--;
              }
            }
          }
          if (localConnection2.isIdle()) {
            i++;
          }
        }
        Iterator localIterator = localArrayList.iterator();
        if (localIterator.hasNext()) {
          Util.closeQuietly((Connection)localIterator.next());
        }
      }
      return null;
    }
  };
  private final ExecutorService executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.daemonThreadFactory("OkHttp ConnectionPool"));
  private final long keepAliveDurationNs;
  private final int maxIdleConnections;
  
  static
  {
    String str1 = System.getProperty("http.keepAlive");
    String str2 = System.getProperty("http.keepAliveDuration");
    String str3 = System.getProperty("http.maxConnections");
    if (str2 != null) {}
    for (long l = Long.parseLong(str2); (str1 != null) && (!Boolean.parseBoolean(str1)); l = 300000L)
    {
      systemDefault = new ConnectionPool(0, l);
      return;
    }
    if (str3 != null)
    {
      systemDefault = new ConnectionPool(Integer.parseInt(str3), l);
      return;
    }
    systemDefault = new ConnectionPool(5, l);
  }
  
  public ConnectionPool(int paramInt, long paramLong)
  {
    this.maxIdleConnections = paramInt;
    this.keepAliveDurationNs = (1000L * (paramLong * 1000L));
  }
  
  public static ConnectionPool getDefault()
  {
    return systemDefault;
  }
  
  /* Error */
  public Connection get(Address paramAddress)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 65	com/squareup/okhttp/ConnectionPool:connections	Ljava/util/LinkedList;
    //   6: aload_0
    //   7: getfield 65	com/squareup/okhttp/ConnectionPool:connections	Ljava/util/LinkedList;
    //   10: invokevirtual 120	java/util/LinkedList:size	()I
    //   13: invokevirtual 124	java/util/LinkedList:listIterator	(I)Ljava/util/ListIterator;
    //   16: astore_3
    //   17: aload_3
    //   18: invokeinterface 130 1 0
    //   23: istore 4
    //   25: aconst_null
    //   26: astore 5
    //   28: iload 4
    //   30: ifeq +87 -> 117
    //   33: aload_3
    //   34: invokeinterface 134 1 0
    //   39: checkcast 136	com/squareup/okhttp/Connection
    //   42: astore 6
    //   44: aload 6
    //   46: invokevirtual 140	com/squareup/okhttp/Connection:getRoute	()Lcom/squareup/okhttp/Route;
    //   49: invokevirtual 146	com/squareup/okhttp/Route:getAddress	()Lcom/squareup/okhttp/Address;
    //   52: aload_1
    //   53: invokevirtual 152	com/squareup/okhttp/Address:equals	(Ljava/lang/Object;)Z
    //   56: ifeq -39 -> 17
    //   59: aload 6
    //   61: invokevirtual 155	com/squareup/okhttp/Connection:isAlive	()Z
    //   64: ifeq -47 -> 17
    //   67: invokestatic 159	java/lang/System:nanoTime	()J
    //   70: aload 6
    //   72: invokevirtual 162	com/squareup/okhttp/Connection:getIdleStartTimeNs	()J
    //   75: lsub
    //   76: aload_0
    //   77: getfield 104	com/squareup/okhttp/ConnectionPool:keepAliveDurationNs	J
    //   80: lcmp
    //   81: ifge -64 -> 17
    //   84: aload_3
    //   85: invokeinterface 165 1 0
    //   90: aload 6
    //   92: invokevirtual 168	com/squareup/okhttp/Connection:isSpdy	()Z
    //   95: istore 7
    //   97: iload 7
    //   99: ifne +14 -> 113
    //   102: invokestatic 173	com/squareup/okhttp/internal/Platform:get	()Lcom/squareup/okhttp/internal/Platform;
    //   105: aload 6
    //   107: invokevirtual 177	com/squareup/okhttp/Connection:getSocket	()Ljava/net/Socket;
    //   110: invokevirtual 181	com/squareup/okhttp/internal/Platform:tagSocket	(Ljava/net/Socket;)V
    //   113: aload 6
    //   115: astore 5
    //   117: aload 5
    //   119: ifnull +20 -> 139
    //   122: aload 5
    //   124: invokevirtual 168	com/squareup/okhttp/Connection:isSpdy	()Z
    //   127: ifeq +12 -> 139
    //   130: aload_0
    //   131: getfield 65	com/squareup/okhttp/ConnectionPool:connections	Ljava/util/LinkedList;
    //   134: aload 5
    //   136: invokevirtual 185	java/util/LinkedList:addFirst	(Ljava/lang/Object;)V
    //   139: aload_0
    //   140: getfield 91	com/squareup/okhttp/ConnectionPool:executorService	Ljava/util/concurrent/ExecutorService;
    //   143: aload_0
    //   144: getfield 98	com/squareup/okhttp/ConnectionPool:connectionsCleanupCallable	Ljava/util/concurrent/Callable;
    //   147: invokeinterface 191 2 0
    //   152: pop
    //   153: aload_0
    //   154: monitorexit
    //   155: aload 5
    //   157: areturn
    //   158: astore 9
    //   160: aload 6
    //   162: invokestatic 195	com/squareup/okhttp/internal/Util:closeQuietly	(Ljava/io/Closeable;)V
    //   165: invokestatic 173	com/squareup/okhttp/internal/Platform:get	()Lcom/squareup/okhttp/internal/Platform;
    //   168: new 197	java/lang/StringBuilder
    //   171: dup
    //   172: invokespecial 198	java/lang/StringBuilder:<init>	()V
    //   175: ldc 200
    //   177: invokevirtual 204	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: aload 9
    //   182: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   185: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   188: invokevirtual 215	com/squareup/okhttp/internal/Platform:logW	(Ljava/lang/String;)V
    //   191: goto -174 -> 17
    //   194: astore_2
    //   195: aload_0
    //   196: monitorexit
    //   197: aload_2
    //   198: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	199	0	this	ConnectionPool
    //   0	199	1	paramAddress	Address
    //   194	4	2	localObject1	Object
    //   16	69	3	localListIterator	ListIterator
    //   23	6	4	bool1	boolean
    //   26	130	5	localObject2	Object
    //   42	119	6	localConnection	Connection
    //   95	3	7	bool2	boolean
    //   158	23	9	localSocketException	java.net.SocketException
    // Exception table:
    //   from	to	target	type
    //   102	113	158	java/net/SocketException
    //   2	17	194	finally
    //   17	25	194	finally
    //   33	97	194	finally
    //   102	113	194	finally
    //   122	139	194	finally
    //   139	153	194	finally
    //   160	191	194	finally
  }
  
  public void maybeShare(Connection paramConnection)
  {
    this.executorService.submit(this.connectionsCleanupCallable);
    if (!paramConnection.isSpdy()) {}
    while (!paramConnection.isAlive()) {
      return;
    }
    try
    {
      this.connections.addFirst(paramConnection);
      return;
    }
    finally {}
  }
  
  /* Error */
  public void recycle(Connection paramConnection)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 168	com/squareup/okhttp/Connection:isSpdy	()Z
    //   4: ifeq +4 -> 8
    //   7: return
    //   8: aload_1
    //   9: invokevirtual 155	com/squareup/okhttp/Connection:isAlive	()Z
    //   12: ifne +8 -> 20
    //   15: aload_1
    //   16: invokestatic 195	com/squareup/okhttp/internal/Util:closeQuietly	(Ljava/io/Closeable;)V
    //   19: return
    //   20: invokestatic 173	com/squareup/okhttp/internal/Platform:get	()Lcom/squareup/okhttp/internal/Platform;
    //   23: aload_1
    //   24: invokevirtual 177	com/squareup/okhttp/Connection:getSocket	()Ljava/net/Socket;
    //   27: invokevirtual 221	com/squareup/okhttp/internal/Platform:untagSocket	(Ljava/net/Socket;)V
    //   30: aload_0
    //   31: monitorenter
    //   32: aload_0
    //   33: getfield 65	com/squareup/okhttp/ConnectionPool:connections	Ljava/util/LinkedList;
    //   36: aload_1
    //   37: invokevirtual 185	java/util/LinkedList:addFirst	(Ljava/lang/Object;)V
    //   40: aload_1
    //   41: invokevirtual 224	com/squareup/okhttp/Connection:resetIdleStartTime	()V
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_0
    //   47: getfield 91	com/squareup/okhttp/ConnectionPool:executorService	Ljava/util/concurrent/ExecutorService;
    //   50: aload_0
    //   51: getfield 98	com/squareup/okhttp/ConnectionPool:connectionsCleanupCallable	Ljava/util/concurrent/Callable;
    //   54: invokeinterface 191 2 0
    //   59: pop
    //   60: return
    //   61: astore_2
    //   62: invokestatic 173	com/squareup/okhttp/internal/Platform:get	()Lcom/squareup/okhttp/internal/Platform;
    //   65: new 197	java/lang/StringBuilder
    //   68: dup
    //   69: invokespecial 198	java/lang/StringBuilder:<init>	()V
    //   72: ldc 226
    //   74: invokevirtual 204	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: aload_2
    //   78: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   81: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   84: invokevirtual 215	com/squareup/okhttp/internal/Platform:logW	(Ljava/lang/String;)V
    //   87: aload_1
    //   88: invokestatic 195	com/squareup/okhttp/internal/Util:closeQuietly	(Ljava/io/Closeable;)V
    //   91: return
    //   92: astore_3
    //   93: aload_0
    //   94: monitorexit
    //   95: aload_3
    //   96: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	97	0	this	ConnectionPool
    //   0	97	1	paramConnection	Connection
    //   61	17	2	localSocketException	java.net.SocketException
    //   92	4	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   20	30	61	java/net/SocketException
    //   32	46	92	finally
    //   93	95	92	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.squareup.okhttp.ConnectionPool
 * JD-Core Version:    0.7.0.1
 */