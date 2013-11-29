package com.google.android.sidekick.main.entry;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import com.google.common.collect.Maps;
import java.util.Map;

public abstract class DelayedStopIntentService
  extends Service
{
  private static final Integer BACKGROUND_DONE = Integer.valueOf(2);
  private static final Integer HANDLE_INTENT_DONE = Integer.valueOf(1);
  private final String mName;
  private boolean mRedelivery;
  private ServiceHandler mServiceHandler;
  private Looper mServiceLooper;
  private final Map<Integer, Integer> mStopStatesByStartIds;
  
  public DelayedStopIntentService(String paramString)
  {
    this.mName = paramString;
    this.mStopStatesByStartIds = Maps.newConcurrentMap();
  }
  
  private void doStopSelf(int paramInt)
  {
    this.mStopStatesByStartIds.remove(Integer.valueOf(paramInt));
    stopSelf(paramInt);
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }
  
  public void onCreate()
  {
    super.onCreate();
    HandlerThread localHandlerThread = new HandlerThread("DelayedStopIntentService[" + this.mName + "]");
    localHandlerThread.start();
    this.mServiceLooper = localHandlerThread.getLooper();
    this.mServiceHandler = new ServiceHandler(this.mServiceLooper);
  }
  
  public void onDestroy()
  {
    this.mServiceLooper.quit();
  }
  
  protected abstract int onHandleIntent(Intent paramIntent, StopLatch paramStopLatch);
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    Message localMessage = this.mServiceHandler.obtainMessage();
    localMessage.arg1 = paramInt2;
    localMessage.obj = paramIntent;
    this.mServiceHandler.sendMessage(localMessage);
    if (this.mRedelivery) {
      return 3;
    }
    return 2;
  }
  
  public void setIntentRedelivery(boolean paramBoolean)
  {
    this.mRedelivery = paramBoolean;
  }
  
  private final class ServiceHandler
    extends Handler
  {
    public ServiceHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = paramMessage.arg1;
      final DelayedStopIntentService.StopLatch localStopLatch = new DelayedStopIntentService.StopLatch(DelayedStopIntentService.this, i, null);
      int j = DelayedStopIntentService.this.onHandleIntent((Intent)paramMessage.obj, localStopLatch);
      if ((j == 0) || (DelayedStopIntentService.BACKGROUND_DONE.equals(DelayedStopIntentService.this.mStopStatesByStartIds.put(Integer.valueOf(i), DelayedStopIntentService.HANDLE_INTENT_DONE))))
      {
        DelayedStopIntentService.this.doStopSelf(i);
        return;
      }
      postDelayed(new Runnable()
      {
        public void run()
        {
          localStopLatch.release();
        }
      }, j);
    }
  }
  
  protected class StopLatch
  {
    private final int mStartId;
    
    private StopLatch(int paramInt)
    {
      this.mStartId = paramInt;
    }
    
    public void release()
    {
      if (DelayedStopIntentService.HANDLE_INTENT_DONE.equals(DelayedStopIntentService.this.mStopStatesByStartIds.put(Integer.valueOf(this.mStartId), DelayedStopIntentService.BACKGROUND_DONE))) {
        DelayedStopIntentService.this.doStopSelf(this.mStartId);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.DelayedStopIntentService
 * JD-Core Version:    0.7.0.1
 */