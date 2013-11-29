package com.android.launcher3;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.util.LongSparseArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MemoryTracker
  extends Service
{
  public static final String TAG = MemoryTracker.class.getSimpleName();
  ActivityManager mAm;
  private final IBinder mBinder = new MemoryTrackerInterface();
  public final LongSparseArray<ProcessMemInfo> mData = new LongSparseArray();
  Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (paramAnonymousMessage.what)
      {
      default: 
        return;
      case 1: 
        MemoryTracker.this.mHandler.removeMessages(3);
        MemoryTracker.this.mHandler.sendEmptyMessage(3);
        return;
      case 2: 
        MemoryTracker.this.mHandler.removeMessages(3);
        return;
      }
      MemoryTracker.this.update();
      MemoryTracker.this.mHandler.removeMessages(3);
      MemoryTracker.this.mHandler.sendEmptyMessageDelayed(3, 5000L);
    }
  };
  private final Object mLock = new Object();
  public final ArrayList<Long> mPids = new ArrayList();
  private int[] mPidsArray = new int[0];
  
  public static void startTrackingMe(Context paramContext, String paramString)
  {
    paramContext.startService(new Intent(paramContext, MemoryTracker.class).setAction("com.android.launcher3.action.START_TRACKING").putExtra("pid", Process.myPid()).putExtra("name", paramString));
  }
  
  public ProcessMemInfo getMemInfo(int paramInt)
  {
    return (ProcessMemInfo)this.mData.get(paramInt);
  }
  
  public int[] getTrackedProcesses()
  {
    return this.mPidsArray;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    this.mHandler.sendEmptyMessage(1);
    return this.mBinder;
  }
  
  public void onCreate()
  {
    this.mAm = ((ActivityManager)getSystemService("activity"));
    Iterator localIterator1 = this.mAm.getRunningServices(256).iterator();
    while (localIterator1.hasNext())
    {
      ActivityManager.RunningServiceInfo localRunningServiceInfo = (ActivityManager.RunningServiceInfo)localIterator1.next();
      if (localRunningServiceInfo.service.getPackageName().equals(getPackageName()))
      {
        Log.v(TAG, "discovered running service: " + localRunningServiceInfo.process + " (" + localRunningServiceInfo.pid + ")");
        startTrackingProcess(localRunningServiceInfo.pid, localRunningServiceInfo.process, System.currentTimeMillis() - (SystemClock.elapsedRealtime() - localRunningServiceInfo.activeSince));
      }
    }
    Iterator localIterator2 = this.mAm.getRunningAppProcesses().iterator();
    while (localIterator2.hasNext())
    {
      ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)localIterator2.next();
      String str = localRunningAppProcessInfo.processName;
      if (str.startsWith(getPackageName()))
      {
        Log.v(TAG, "discovered other running process: " + str + " (" + localRunningAppProcessInfo.pid + ")");
        startTrackingProcess(localRunningAppProcessInfo.pid, str, System.currentTimeMillis());
      }
    }
  }
  
  public void onDestroy()
  {
    this.mHandler.sendEmptyMessage(2);
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    Log.v(TAG, "Received start id " + paramInt2 + ": " + paramIntent);
    if ((paramIntent != null) && ("com.android.launcher3.action.START_TRACKING".equals(paramIntent.getAction()))) {
      startTrackingProcess(paramIntent.getIntExtra("pid", -1), paramIntent.getStringExtra("name"), paramIntent.getLongExtra("start", System.currentTimeMillis()));
    }
    this.mHandler.sendEmptyMessage(1);
    return 1;
  }
  
  public void startTrackingProcess(int paramInt, String paramString, long paramLong)
  {
    synchronized (this.mLock)
    {
      Long localLong = new Long(paramInt);
      if (this.mPids.contains(localLong)) {
        return;
      }
      this.mPids.add(localLong);
      updatePidsArrayL();
      this.mData.put(paramInt, new ProcessMemInfo(paramInt, paramString, paramLong));
      return;
    }
  }
  
  void update()
  {
    for (;;)
    {
      int i;
      int j;
      synchronized (this.mLock)
      {
        Debug.MemoryInfo[] arrayOfMemoryInfo = this.mAm.getProcessMemoryInfo(this.mPidsArray);
        i = 0;
        Debug.MemoryInfo localMemoryInfo;
        if (i < arrayOfMemoryInfo.length)
        {
          localMemoryInfo = arrayOfMemoryInfo[i];
          if (i > this.mPids.size()) {
            Log.e(TAG, "update: unknown process info received: " + localMemoryInfo);
          }
        }
        else
        {
          j = -1 + this.mPids.size();
          if (j < 0) {
            continue;
          }
          long l1 = ((Long)this.mPids.get(j)).intValue();
          if (this.mData.get(l1) != null) {
            break label373;
          }
          this.mPids.remove(j);
          updatePidsArrayL();
          break label373;
        }
        long l2 = ((Long)this.mPids.get(i)).intValue();
        ProcessMemInfo localProcessMemInfo = (ProcessMemInfo)this.mData.get(l2);
        localProcessMemInfo.head = ((1 + localProcessMemInfo.head) % localProcessMemInfo.pss.length);
        long[] arrayOfLong1 = localProcessMemInfo.pss;
        int k = localProcessMemInfo.head;
        long l3 = localMemoryInfo.getTotalPss();
        localProcessMemInfo.currentPss = l3;
        arrayOfLong1[k] = l3;
        long[] arrayOfLong2 = localProcessMemInfo.uss;
        int m = localProcessMemInfo.head;
        long l4 = localMemoryInfo.getTotalPrivateDirty();
        localProcessMemInfo.currentUss = l4;
        arrayOfLong2[m] = l4;
        if (localProcessMemInfo.currentPss > localProcessMemInfo.max) {
          localProcessMemInfo.max = localProcessMemInfo.currentPss;
        }
        if (localProcessMemInfo.currentUss > localProcessMemInfo.max) {
          localProcessMemInfo.max = localProcessMemInfo.currentUss;
        }
        if (localProcessMemInfo.currentPss != 0L) {
          break label379;
        }
        Log.v(TAG, "update: pid " + l2 + " has pss=0, it probably died");
        this.mData.remove(l2);
        break label379;
        return;
      }
      label373:
      j--;
      continue;
      label379:
      i++;
    }
  }
  
  void updatePidsArrayL()
  {
    int i = this.mPids.size();
    this.mPidsArray = new int[i];
    StringBuffer localStringBuffer = new StringBuffer("Now tracking processes: ");
    for (int j = 0; j < i; j++)
    {
      int k = ((Long)this.mPids.get(j)).intValue();
      this.mPidsArray[j] = k;
      localStringBuffer.append(k);
      localStringBuffer.append(" ");
    }
    Log.v(TAG, localStringBuffer.toString());
  }
  
  public class MemoryTrackerInterface
    extends Binder
  {
    public MemoryTrackerInterface() {}
    
    MemoryTracker getService()
    {
      return MemoryTracker.this;
    }
  }
  
  public static class ProcessMemInfo
  {
    public long currentPss;
    public long currentUss;
    public int head = 0;
    public long max = 1L;
    public String name;
    public int pid;
    public long[] pss = new long[256];
    public long startTime;
    public long[] uss = new long[256];
    
    public ProcessMemInfo(int paramInt, String paramString, long paramLong)
    {
      this.pid = paramInt;
      this.name = paramString;
      this.startTime = paramLong;
    }
    
    public long getUptime()
    {
      return System.currentTimeMillis() - this.startTime;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.MemoryTracker
 * JD-Core Version:    0.7.0.1
 */