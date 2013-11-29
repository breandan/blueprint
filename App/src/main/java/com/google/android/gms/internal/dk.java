package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.util.ArrayList;

public abstract class dk<T extends IInterface>
  implements GooglePlayServicesClient
{
  public static final String[] GOOGLE_PLUS_REQUIRED_FEATURES = { "service_esmobile", "service_googleme" };
  private final String[] jZ;
  private final Context mContext;
  final Handler mHandler;
  private ArrayList<GooglePlayServicesClient.ConnectionCallbacks> oA;
  final ArrayList<GooglePlayServicesClient.ConnectionCallbacks> oB = new ArrayList();
  private boolean oC = false;
  private ArrayList<GooglePlayServicesClient.OnConnectionFailedListener> oD;
  private boolean oE = false;
  private final ArrayList<dk<T>.b<?>> oF = new ArrayList();
  private dk<T>.e oG;
  boolean oH = false;
  boolean oI = false;
  private final Object oJ = new Object();
  private T oz;
  
  protected dk(Context paramContext, GooglePlayServicesClient.ConnectionCallbacks paramConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener paramOnConnectionFailedListener, String... paramVarArgs)
  {
    this.mContext = ((Context)ds.e(paramContext));
    this.oA = new ArrayList();
    this.oA.add(ds.e(paramConnectionCallbacks));
    this.oD = new ArrayList();
    this.oD.add(ds.e(paramOnConnectionFailedListener));
    this.mHandler = new a(paramContext.getMainLooper());
    a(paramVarArgs);
    this.jZ = paramVarArgs;
  }
  
  protected final void D(IBinder paramIBinder)
  {
    try
    {
      a(dp.a.F(paramIBinder), new d(this));
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("GmsClient", "service died");
    }
  }
  
  protected void a(int paramInt, IBinder paramIBinder, Bundle paramBundle)
  {
    this.mHandler.sendMessage(this.mHandler.obtainMessage(1, new f(paramInt, paramIBinder, paramBundle)));
  }
  
  protected void a(ConnectionResult paramConnectionResult)
  {
    this.mHandler.removeMessages(4);
    for (;;)
    {
      int j;
      synchronized (this.oD)
      {
        this.oE = true;
        ArrayList localArrayList2 = this.oD;
        int i = localArrayList2.size();
        j = 0;
        if (j < i)
        {
          if (!this.oH) {
            return;
          }
          if (this.oD.contains(localArrayList2.get(j))) {
            ((GooglePlayServicesClient.OnConnectionFailedListener)localArrayList2.get(j)).onConnectionFailed(paramConnectionResult);
          }
        }
        else
        {
          this.oE = false;
          return;
        }
      }
      j++;
    }
  }
  
  protected abstract void a(dp paramdp, d paramd)
    throws RemoteException;
  
  protected void a(String... paramVarArgs) {}
  
  protected abstract String ag();
  
  protected abstract String ah();
  
  protected void bc()
  {
    boolean bool1 = true;
    for (;;)
    {
      int j;
      synchronized (this.oA)
      {
        if (!this.oC)
        {
          bool2 = bool1;
          ds.k(bool2);
          this.mHandler.removeMessages(4);
          this.oC = true;
          if (this.oB.size() != 0) {
            break label165;
          }
          ds.k(bool1);
          Bundle localBundle = bd();
          ArrayList localArrayList2 = this.oA;
          int i = localArrayList2.size();
          j = 0;
          if ((j >= i) || (!this.oH) || (!isConnected()))
          {
            this.oB.clear();
            this.oC = false;
            return;
          }
          this.oB.size();
          if (this.oB.contains(localArrayList2.get(j))) {
            break label170;
          }
          ((GooglePlayServicesClient.ConnectionCallbacks)localArrayList2.get(j)).onConnected(localBundle);
        }
      }
      boolean bool2 = false;
      continue;
      label165:
      bool1 = false;
      continue;
      label170:
      j++;
    }
  }
  
  protected Bundle bd()
  {
    return null;
  }
  
  protected final void be()
  {
    this.mHandler.removeMessages(4);
    for (;;)
    {
      int j;
      synchronized (this.oA)
      {
        this.oC = true;
        ArrayList localArrayList2 = this.oA;
        int i = localArrayList2.size();
        j = 0;
        if ((j >= i) || (!this.oH))
        {
          this.oC = false;
          return;
        }
        if (this.oA.contains(localArrayList2.get(j))) {
          ((GooglePlayServicesClient.ConnectionCallbacks)localArrayList2.get(j)).onDisconnected();
        }
      }
      j++;
    }
  }
  
  protected final void bf()
  {
    if (!isConnected()) {
      throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
    }
  }
  
  protected final T bg()
  {
    bf();
    return this.oz;
  }
  
  public void connect()
  {
    this.oH = true;
    do
    {
      synchronized (this.oJ)
      {
        this.oI = true;
        int i = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext);
        if (i != 0)
        {
          this.mHandler.sendMessage(this.mHandler.obtainMessage(3, Integer.valueOf(i)));
          return;
        }
      }
      if (this.oG != null)
      {
        Log.e("GmsClient", "Calling connect() while still connected, missing disconnect().");
        this.oz = null;
        dl.t(this.mContext).b(ag(), this.oG);
      }
      this.oG = new e();
    } while (dl.t(this.mContext).a(ag(), this.oG));
    Log.e("GmsClient", "unable to connect to service: " + ag());
    this.mHandler.sendMessage(this.mHandler.obtainMessage(3, Integer.valueOf(9)));
  }
  
  public void disconnect()
  {
    this.oH = false;
    synchronized (this.oJ)
    {
      this.oI = false;
    }
    synchronized (this.oF)
    {
      int i = this.oF.size();
      int j = 0;
      while (j < i)
      {
        ((b)this.oF.get(j)).bi();
        j++;
        continue;
        localObject2 = finally;
        throw localObject2;
      }
      this.oF.clear();
      this.oz = null;
      if (this.oG != null)
      {
        dl.t(this.mContext).b(ag(), this.oG);
        this.oG = null;
      }
      return;
    }
  }
  
  public final Context getContext()
  {
    return this.mContext;
  }
  
  public boolean isConnected()
  {
    return this.oz != null;
  }
  
  public boolean isConnecting()
  {
    synchronized (this.oJ)
    {
      boolean bool = this.oI;
      return bool;
    }
  }
  
  protected abstract T q(IBinder paramIBinder);
  
  final class a
    extends Handler
  {
    public a(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == 1) && (!dk.this.isConnecting()))
      {
        dk.b localb2 = (dk.b)paramMessage.obj;
        localb2.aI();
        localb2.unregister();
        return;
      }
      synchronized (dk.a(dk.this))
      {
        dk.this.oI = false;
        if (paramMessage.what == 3)
        {
          dk.this.a(new ConnectionResult(((Integer)paramMessage.obj).intValue(), null));
          return;
        }
      }
      if (paramMessage.what == 4) {
        synchronized (dk.b(dk.this))
        {
          if ((dk.this.oH) && (dk.this.isConnected()) && (dk.b(dk.this).contains(paramMessage.obj))) {
            ((GooglePlayServicesClient.ConnectionCallbacks)paramMessage.obj).onConnected(dk.this.bd());
          }
          return;
        }
      }
      if ((paramMessage.what == 2) && (!dk.this.isConnected()))
      {
        dk.b localb1 = (dk.b)paramMessage.obj;
        localb1.aI();
        localb1.unregister();
        return;
      }
      if ((paramMessage.what == 2) || (paramMessage.what == 1))
      {
        ((dk.b)paramMessage.obj).bh();
        return;
      }
      Log.wtf("GmsClient", "Don't know how to handle this message.");
    }
  }
  
  protected abstract class b<TListener>
  {
    private TListener mListener;
    private boolean oL;
    
    public b()
    {
      Object localObject;
      this.mListener = localObject;
      this.oL = false;
    }
    
    protected abstract void a(TListener paramTListener);
    
    protected abstract void aI();
    
    /* Error */
    public void bh()
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 21	com/google/android/gms/internal/dk$b:mListener	Ljava/lang/Object;
      //   6: astore_2
      //   7: aload_0
      //   8: getfield 23	com/google/android/gms/internal/dk$b:oL	Z
      //   11: ifeq +33 -> 44
      //   14: ldc 31
      //   16: new 33	java/lang/StringBuilder
      //   19: dup
      //   20: invokespecial 34	java/lang/StringBuilder:<init>	()V
      //   23: ldc 36
      //   25: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   28: aload_0
      //   29: invokevirtual 43	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   32: ldc 45
      //   34: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   37: invokevirtual 49	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   40: invokestatic 55	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   43: pop
      //   44: aload_0
      //   45: monitorexit
      //   46: aload_2
      //   47: ifnull +36 -> 83
      //   50: aload_0
      //   51: aload_2
      //   52: invokevirtual 57	com/google/android/gms/internal/dk$b:a	(Ljava/lang/Object;)V
      //   55: aload_0
      //   56: monitorenter
      //   57: aload_0
      //   58: iconst_1
      //   59: putfield 23	com/google/android/gms/internal/dk$b:oL	Z
      //   62: aload_0
      //   63: monitorexit
      //   64: aload_0
      //   65: invokevirtual 60	com/google/android/gms/internal/dk$b:unregister	()V
      //   68: return
      //   69: astore_1
      //   70: aload_0
      //   71: monitorexit
      //   72: aload_1
      //   73: athrow
      //   74: astore 4
      //   76: aload_0
      //   77: invokevirtual 62	com/google/android/gms/internal/dk$b:aI	()V
      //   80: aload 4
      //   82: athrow
      //   83: aload_0
      //   84: invokevirtual 62	com/google/android/gms/internal/dk$b:aI	()V
      //   87: goto -32 -> 55
      //   90: astore_3
      //   91: aload_0
      //   92: monitorexit
      //   93: aload_3
      //   94: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	95	0	this	b
      //   69	4	1	localObject1	Object
      //   6	46	2	localObject2	Object
      //   90	4	3	localObject3	Object
      //   74	7	4	localRuntimeException	java.lang.RuntimeException
      // Exception table:
      //   from	to	target	type
      //   2	44	69	finally
      //   44	46	69	finally
      //   70	72	69	finally
      //   50	55	74	java/lang/RuntimeException
      //   57	64	90	finally
      //   91	93	90	finally
    }
    
    public void bi()
    {
      try
      {
        this.mListener = null;
        return;
      }
      finally {}
    }
    
    public void unregister()
    {
      bi();
      synchronized (dk.c(dk.this))
      {
        dk.c(dk.this).remove(this);
        return;
      }
    }
  }
  
  public static final class d
    extends do.a
  {
    private dk oM;
    
    public d(dk paramdk)
    {
      this.oM = paramdk;
    }
    
    public void b(int paramInt, IBinder paramIBinder, Bundle paramBundle)
    {
      ds.a("onPostInitComplete can be called only once per call to getServiceFromBroker", this.oM);
      this.oM.a(paramInt, paramIBinder, paramBundle);
      this.oM = null;
    }
  }
  
  final class e
    implements ServiceConnection
  {
    e() {}
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      dk.this.D(paramIBinder);
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      dk.a(dk.this, null);
      dk.this.be();
    }
  }
  
  protected final class f
    extends dk<T>.b<Boolean>
  {
    public final Bundle oN;
    public final IBinder oO;
    public final int statusCode;
    
    public f(int paramInt, IBinder paramIBinder, Bundle paramBundle)
    {
      super(Boolean.valueOf(true));
      this.statusCode = paramInt;
      this.oO = paramIBinder;
      this.oN = paramBundle;
    }
    
    protected void a(Boolean paramBoolean)
    {
      if (paramBoolean == null) {
        return;
      }
      switch (this.statusCode)
      {
      default: 
        if (this.oN == null) {
          break;
        }
      }
      for (PendingIntent localPendingIntent = (PendingIntent)this.oN.getParcelable("pendingIntent");; localPendingIntent = null)
      {
        if (dk.e(dk.this) != null)
        {
          dl.t(dk.f(dk.this)).b(dk.this.ag(), dk.e(dk.this));
          dk.a(dk.this, null);
        }
        dk.a(dk.this, null);
        dk.this.a(new ConnectionResult(this.statusCode, localPendingIntent));
        return;
        try
        {
          String str = this.oO.getInterfaceDescriptor();
          if (dk.this.ah().equals(str))
          {
            dk.a(dk.this, dk.this.q(this.oO));
            if (dk.d(dk.this) != null)
            {
              dk.this.bc();
              return;
            }
          }
        }
        catch (RemoteException localRemoteException)
        {
          dl.t(dk.f(dk.this)).b(dk.this.ag(), dk.e(dk.this));
          dk.a(dk.this, null);
          dk.a(dk.this, null);
          dk.this.a(new ConnectionResult(8, null));
          return;
        }
        throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
      }
    }
    
    protected void aI() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.dk
 * JD-Core Version:    0.7.0.1
 */