package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public final class dl
  implements Handler.Callback
{
  private static final Object oP = new Object();
  private static dl oQ;
  private final Handler mHandler = new Handler(paramContext.getMainLooper(), this);
  private final Context oR;
  private final HashMap<String, a> oS = new HashMap();
  
  private dl(Context paramContext)
  {
    this.oR = paramContext.getApplicationContext();
  }
  
  public static dl t(Context paramContext)
  {
    synchronized (oP)
    {
      if (oQ == null) {
        oQ = new dl(paramContext.getApplicationContext());
      }
      return oQ;
    }
  }
  
  public boolean a(String paramString, dk<?>.e paramdk)
  {
    for (;;)
    {
      a locala;
      synchronized (this.oS)
      {
        locala = (a)this.oS.get(paramString);
        if (locala == null)
        {
          locala = new a(paramString);
          locala.a(paramdk);
          Intent localIntent1 = new Intent(paramString).setPackage("com.google.android.gms");
          locala.l(this.oR.bindService(localIntent1, locala.bj(), 129));
          this.oS.put(paramString, locala);
          boolean bool = locala.isBound();
          return bool;
        }
        this.mHandler.removeMessages(0, locala);
        if (locala.c(paramdk)) {
          throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  startServiceAction=" + paramString);
        }
      }
      locala.a(paramdk);
      switch (locala.getState())
      {
      case 1: 
        paramdk.onServiceConnected(locala.getComponentName(), locala.getBinder());
        break;
      case 2: 
        Intent localIntent2 = new Intent(paramString).setPackage("com.google.android.gms");
        locala.l(this.oR.bindService(localIntent2, locala.bj(), 129));
      }
    }
  }
  
  public void b(String paramString, dk<?>.e paramdk)
  {
    a locala;
    synchronized (this.oS)
    {
      locala = (a)this.oS.get(paramString);
      if (locala == null) {
        throw new IllegalStateException("Nonexistent connection status for service action: " + paramString);
      }
    }
    if (!locala.c(paramdk)) {
      throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  startServiceAction=" + paramString);
    }
    locala.b(paramdk);
    if (locala.bl())
    {
      Message localMessage = this.mHandler.obtainMessage(0, locala);
      this.mHandler.sendMessageDelayed(localMessage, 5000L);
    }
  }
  
  public boolean handleMessage(Message paramMessage)
  {
    switch (paramMessage.what)
    {
    default: 
      return false;
    }
    a locala = (a)paramMessage.obj;
    synchronized (this.oS)
    {
      if (locala.bl())
      {
        this.oR.unbindService(locala.bj());
        this.oS.remove(locala.bk());
      }
      return true;
    }
  }
  
  final class a
  {
    private int mState;
    private final String oT;
    private final a oU;
    private final HashSet<dk<?>.e> oV;
    private boolean oW;
    private IBinder oX;
    private ComponentName oY;
    
    public a(String paramString)
    {
      this.oT = paramString;
      this.oU = new a();
      this.oV = new HashSet();
      this.mState = 0;
    }
    
    public void a(dk<?>.e paramdk)
    {
      this.oV.add(paramdk);
    }
    
    public void b(dk<?>.e paramdk)
    {
      this.oV.remove(paramdk);
    }
    
    public a bj()
    {
      return this.oU;
    }
    
    public String bk()
    {
      return this.oT;
    }
    
    public boolean bl()
    {
      return this.oV.isEmpty();
    }
    
    public boolean c(dk<?>.e paramdk)
    {
      return this.oV.contains(paramdk);
    }
    
    public IBinder getBinder()
    {
      return this.oX;
    }
    
    public ComponentName getComponentName()
    {
      return this.oY;
    }
    
    public int getState()
    {
      return this.mState;
    }
    
    public boolean isBound()
    {
      return this.oW;
    }
    
    public void l(boolean paramBoolean)
    {
      this.oW = paramBoolean;
    }
    
    public class a
      implements ServiceConnection
    {
      public a() {}
      
      public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
      {
        synchronized (dl.a(dl.this))
        {
          dl.a.a(dl.a.this, paramIBinder);
          dl.a.a(dl.a.this, paramComponentName);
          Iterator localIterator = dl.a.a(dl.a.this).iterator();
          if (localIterator.hasNext()) {
            ((dk.e)localIterator.next()).onServiceConnected(paramComponentName, paramIBinder);
          }
        }
        dl.a.a(dl.a.this, 1);
      }
      
      public void onServiceDisconnected(ComponentName paramComponentName)
      {
        synchronized (dl.a(dl.this))
        {
          dl.a.a(dl.a.this, null);
          dl.a.a(dl.a.this, paramComponentName);
          Iterator localIterator = dl.a.a(dl.a.this).iterator();
          if (localIterator.hasNext()) {
            ((dk.e)localIterator.next()).onServiceDisconnected(paramComponentName);
          }
        }
        dl.a.a(dl.a.this, 2);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.gms.internal.dl
 * JD-Core Version:    0.7.0.1
 */