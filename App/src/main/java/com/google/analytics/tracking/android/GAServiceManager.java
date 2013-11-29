package com.google.analytics.tracking.android;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class GAServiceManager
  implements ServiceManager
{
  private static final Object MSG_OBJECT = new Object();
  private static GAServiceManager instance;
  private boolean connected = true;
  private Context ctx;
  private int dispatchPeriodInSeconds = 1800;
  private Handler handler;
  private boolean listenForNetwork = true;
  private AnalyticsStoreStateListener listener = new AnalyticsStoreStateListener()
  {
    public void reportStoreIsEmpty(boolean paramAnonymousBoolean)
    {
      GAServiceManager.this.updatePowerSaveMode(paramAnonymousBoolean, GAServiceManager.this.connected);
    }
  };
  private GANetworkReceiver networkReceiver;
  private boolean pendingDispatch = true;
  private AnalyticsStore store;
  private boolean storeIsEmpty = false;
  private volatile AnalyticsThread thread;
  
  public static GAServiceManager getInstance()
  {
    if (instance == null) {
      instance = new GAServiceManager();
    }
    return instance;
  }
  
  private void initializeHandler()
  {
    this.handler = new Handler(this.ctx.getMainLooper(), new Handler.Callback()
    {
      public boolean handleMessage(Message paramAnonymousMessage)
      {
        if ((1 == paramAnonymousMessage.what) && (GAServiceManager.MSG_OBJECT.equals(paramAnonymousMessage.obj)))
        {
          GAUsage.getInstance().setDisableUsage(true);
          GAServiceManager.this.dispatch();
          GAUsage.getInstance().setDisableUsage(false);
          if ((GAServiceManager.this.dispatchPeriodInSeconds > 0) && (!GAServiceManager.this.storeIsEmpty)) {
            GAServiceManager.this.handler.sendMessageDelayed(GAServiceManager.this.handler.obtainMessage(1, GAServiceManager.MSG_OBJECT), 1000 * GAServiceManager.this.dispatchPeriodInSeconds);
          }
        }
        return true;
      }
    });
    if (this.dispatchPeriodInSeconds > 0) {
      this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), 1000 * this.dispatchPeriodInSeconds);
    }
  }
  
  private void initializeNetworkReceiver()
  {
    this.networkReceiver = new GANetworkReceiver(this);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    this.ctx.registerReceiver(this.networkReceiver, localIntentFilter);
  }
  
  /* Error */
  public void dispatch()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 118	com/google/analytics/tracking/android/GAServiceManager:thread	Lcom/google/analytics/tracking/android/AnalyticsThread;
    //   6: ifnonnull +17 -> 23
    //   9: ldc 120
    //   11: invokestatic 126	com/google/analytics/tracking/android/Log:w	(Ljava/lang/String;)I
    //   14: pop
    //   15: aload_0
    //   16: iconst_1
    //   17: putfield 40	com/google/analytics/tracking/android/GAServiceManager:pendingDispatch	Z
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: invokestatic 131	com/google/analytics/tracking/android/GAUsage:getInstance	()Lcom/google/analytics/tracking/android/GAUsage;
    //   26: getstatic 137	com/google/analytics/tracking/android/GAUsage$Field:DISPATCH	Lcom/google/analytics/tracking/android/GAUsage$Field;
    //   29: invokevirtual 141	com/google/analytics/tracking/android/GAUsage:setUsage	(Lcom/google/analytics/tracking/android/GAUsage$Field;)V
    //   32: aload_0
    //   33: getfield 118	com/google/analytics/tracking/android/GAServiceManager:thread	Lcom/google/analytics/tracking/android/AnalyticsThread;
    //   36: invokeinterface 145 1 0
    //   41: goto -21 -> 20
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	49	0	this	GAServiceManager
    //   44	4	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	20	44	finally
    //   23	41	44	finally
  }
  
  AnalyticsStore getStore()
  {
    try
    {
      if (this.store != null) {
        break label50;
      }
      if (this.ctx == null) {
        throw new IllegalStateException("Cant get a store unless we have a context");
      }
    }
    finally {}
    this.store = new PersistentAnalyticsStore(this.listener, this.ctx);
    label50:
    if (this.handler == null) {
      initializeHandler();
    }
    if ((this.networkReceiver == null) && (this.listenForNetwork)) {
      initializeNetworkReceiver();
    }
    AnalyticsStore localAnalyticsStore = this.store;
    return localAnalyticsStore;
  }
  
  /* Error */
  void initialize(Context paramContext, AnalyticsThread paramAnalyticsThread)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 74	com/google/analytics/tracking/android/GAServiceManager:ctx	Landroid/content/Context;
    //   6: astore 4
    //   8: aload 4
    //   10: ifnull +6 -> 16
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: aload_0
    //   17: aload_1
    //   18: invokevirtual 170	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   21: putfield 74	com/google/analytics/tracking/android/GAServiceManager:ctx	Landroid/content/Context;
    //   24: aload_0
    //   25: getfield 118	com/google/analytics/tracking/android/GAServiceManager:thread	Lcom/google/analytics/tracking/android/AnalyticsThread;
    //   28: ifnonnull -15 -> 13
    //   31: aload_0
    //   32: aload_2
    //   33: putfield 118	com/google/analytics/tracking/android/GAServiceManager:thread	Lcom/google/analytics/tracking/android/AnalyticsThread;
    //   36: aload_0
    //   37: getfield 40	com/google/analytics/tracking/android/GAServiceManager:pendingDispatch	Z
    //   40: ifeq -27 -> 13
    //   43: aload_2
    //   44: invokeinterface 145 1 0
    //   49: goto -36 -> 13
    //   52: astore_3
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_3
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	GAServiceManager
    //   0	57	1	paramContext	Context
    //   0	57	2	paramAnalyticsThread	AnalyticsThread
    //   52	4	3	localObject	Object
    //   6	3	4	localContext	Context
    // Exception table:
    //   from	to	target	type
    //   2	8	52	finally
    //   16	49	52	finally
  }
  
  /* Error */
  public void setDispatchPeriod(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 64	com/google/analytics/tracking/android/GAServiceManager:handler	Landroid/os/Handler;
    //   6: ifnonnull +17 -> 23
    //   9: ldc 174
    //   11: invokestatic 126	com/google/analytics/tracking/android/Log:w	(Ljava/lang/String;)I
    //   14: pop
    //   15: aload_0
    //   16: iload_1
    //   17: putfield 38	com/google/analytics/tracking/android/GAServiceManager:dispatchPeriodInSeconds	I
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: invokestatic 131	com/google/analytics/tracking/android/GAUsage:getInstance	()Lcom/google/analytics/tracking/android/GAUsage;
    //   26: getstatic 177	com/google/analytics/tracking/android/GAUsage$Field:SET_DISPATCH_PERIOD	Lcom/google/analytics/tracking/android/GAUsage$Field;
    //   29: invokevirtual 141	com/google/analytics/tracking/android/GAUsage:setUsage	(Lcom/google/analytics/tracking/android/GAUsage$Field;)V
    //   32: aload_0
    //   33: getfield 53	com/google/analytics/tracking/android/GAServiceManager:storeIsEmpty	Z
    //   36: ifne +28 -> 64
    //   39: aload_0
    //   40: getfield 42	com/google/analytics/tracking/android/GAServiceManager:connected	Z
    //   43: ifeq +21 -> 64
    //   46: aload_0
    //   47: getfield 38	com/google/analytics/tracking/android/GAServiceManager:dispatchPeriodInSeconds	I
    //   50: ifle +14 -> 64
    //   53: aload_0
    //   54: getfield 64	com/google/analytics/tracking/android/GAServiceManager:handler	Landroid/os/Handler;
    //   57: iconst_1
    //   58: getstatic 36	com/google/analytics/tracking/android/GAServiceManager:MSG_OBJECT	Ljava/lang/Object;
    //   61: invokevirtual 181	android/os/Handler:removeMessages	(ILjava/lang/Object;)V
    //   64: aload_0
    //   65: iload_1
    //   66: putfield 38	com/google/analytics/tracking/android/GAServiceManager:dispatchPeriodInSeconds	I
    //   69: iload_1
    //   70: ifle -50 -> 20
    //   73: aload_0
    //   74: getfield 53	com/google/analytics/tracking/android/GAServiceManager:storeIsEmpty	Z
    //   77: ifne -57 -> 20
    //   80: aload_0
    //   81: getfield 42	com/google/analytics/tracking/android/GAServiceManager:connected	Z
    //   84: ifeq -64 -> 20
    //   87: aload_0
    //   88: getfield 64	com/google/analytics/tracking/android/GAServiceManager:handler	Landroid/os/Handler;
    //   91: aload_0
    //   92: getfield 64	com/google/analytics/tracking/android/GAServiceManager:handler	Landroid/os/Handler;
    //   95: iconst_1
    //   96: getstatic 36	com/google/analytics/tracking/android/GAServiceManager:MSG_OBJECT	Ljava/lang/Object;
    //   99: invokevirtual 90	android/os/Handler:obtainMessage	(ILjava/lang/Object;)Landroid/os/Message;
    //   102: iload_1
    //   103: sipush 1000
    //   106: imul
    //   107: i2l
    //   108: invokevirtual 94	android/os/Handler:sendMessageDelayed	(Landroid/os/Message;J)Z
    //   111: pop
    //   112: goto -92 -> 20
    //   115: astore_2
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_2
    //   119: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	120	0	this	GAServiceManager
    //   0	120	1	paramInt	int
    //   115	4	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	20	115	finally
    //   23	64	115	finally
    //   64	69	115	finally
    //   73	112	115	finally
  }
  
  public void updateConnectivityStatus(boolean paramBoolean)
  {
    try
    {
      updatePowerSaveMode(this.storeIsEmpty, paramBoolean);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  void updatePowerSaveMode(boolean paramBoolean1, boolean paramBoolean2)
  {
    for (;;)
    {
      StringBuilder localStringBuilder;
      try
      {
        if (this.storeIsEmpty == paramBoolean1)
        {
          boolean bool = this.connected;
          if (bool == paramBoolean2) {
            return;
          }
        }
        if (((paramBoolean1) || (!paramBoolean2)) && (this.dispatchPeriodInSeconds > 0)) {
          this.handler.removeMessages(1, MSG_OBJECT);
        }
        if ((!paramBoolean1) && (paramBoolean2) && (this.dispatchPeriodInSeconds > 0)) {
          this.handler.sendMessageDelayed(this.handler.obtainMessage(1, MSG_OBJECT), 1000 * this.dispatchPeriodInSeconds);
        }
        localStringBuilder = new StringBuilder().append("PowerSaveMode ");
        if (paramBoolean1) {
          break label158;
        }
        if (paramBoolean2) {
          break label151;
        }
      }
      finally {}
      Log.iDebug(str);
      this.storeIsEmpty = paramBoolean1;
      this.connected = paramBoolean2;
      continue;
      label151:
      String str = "terminated.";
      continue;
      label158:
      str = "initiated.";
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.analytics.tracking.android.GAServiceManager
 * JD-Core Version:    0.7.0.1
 */