package com.google.android.voicesearch.ime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ExtraPreconditions.ThreadCheck;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public class ScreenStateMonitor
{
  private final BroadcastReceiver mBroadcastReceiver;
  private final Context mContext;
  private final IntentFilter mIntentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
  @Nullable
  private Listener mListener;
  private boolean mRegistered = false;
  private final ExtraPreconditions.ThreadCheck mSameThread = ExtraPreconditions.createSameThreadCheck();
  
  public ScreenStateMonitor(Context paramContext)
  {
    this.mContext = ((Context)Preconditions.checkNotNull(paramContext));
    this.mBroadcastReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        ScreenStateMonitor.this.handleBroadcast(paramAnonymousIntent);
      }
    };
  }
  
  private void handleBroadcast(Intent paramIntent)
  {
    Preconditions.checkNotNull(this.mListener);
    this.mSameThread.check();
    if (paramIntent.getAction().equals("android.intent.action.SCREEN_OFF"))
    {
      Log.i("ScreenStateMonitor", "#onReceive - screen off");
      this.mListener.onScreenOff();
    }
  }
  
  public void register(Listener paramListener)
  {
    if (this.mListener == null) {}
    for (boolean bool1 = true;; bool1 = false)
    {
      Preconditions.checkState(bool1);
      boolean bool2 = this.mRegistered;
      boolean bool3 = false;
      if (!bool2) {
        bool3 = true;
      }
      Preconditions.checkState(bool3);
      this.mSameThread.check();
      this.mListener = paramListener;
      this.mRegistered = true;
      this.mContext.registerReceiver(this.mBroadcastReceiver, this.mIntentFilter);
      return;
    }
  }
  
  public void unregister()
  {
    this.mSameThread.check();
    if (this.mRegistered)
    {
      this.mRegistered = false;
      this.mListener = null;
      this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onScreenOff();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ime.ScreenStateMonitor
 * JD-Core Version:    0.7.0.1
 */