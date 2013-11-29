package com.android.launcher3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class WeightWatcher
  extends LinearLayout
{
  Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (paramAnonymousMessage.what)
      {
      default: 
        return;
      case 1: 
        WeightWatcher.this.mHandler.sendEmptyMessage(3);
        return;
      case 2: 
        WeightWatcher.this.mHandler.removeMessages(3);
        return;
      }
      int[] arrayOfInt = WeightWatcher.this.mMemoryService.getTrackedProcesses();
      int i = WeightWatcher.this.getChildCount();
      if (arrayOfInt.length != i) {
        WeightWatcher.this.initViews();
      }
      label160:
      for (;;)
      {
        WeightWatcher.this.mHandler.sendEmptyMessageDelayed(3, 5000L);
        return;
        for (int j = 0;; j++)
        {
          if (j >= i) {
            break label160;
          }
          WeightWatcher.ProcessWatcher localProcessWatcher = (WeightWatcher.ProcessWatcher)WeightWatcher.this.getChildAt(j);
          if (WeightWatcher.indexOf(arrayOfInt, localProcessWatcher.getPid()) < 0)
          {
            WeightWatcher.this.initViews();
            break;
          }
          localProcessWatcher.update();
        }
      }
    }
  };
  private MemoryTracker mMemoryService;
  
  public WeightWatcher(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public WeightWatcher(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    ServiceConnection local2 = new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        WeightWatcher.access$002(WeightWatcher.this, ((MemoryTracker.MemoryTrackerInterface)paramAnonymousIBinder).getService());
        WeightWatcher.this.initViews();
      }
      
      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        WeightWatcher.access$002(WeightWatcher.this, null);
      }
    };
    paramContext.bindService(new Intent(paramContext, MemoryTracker.class), local2, 1);
    setOrientation(1);
    setBackgroundColor(-1073741824);
  }
  
  static int indexOf(int[] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      if (paramArrayOfInt[i] == paramInt) {
        return i;
      }
    }
    return -1;
  }
  
  public void initViews()
  {
    removeAllViews();
    int[] arrayOfInt = this.mMemoryService.getTrackedProcesses();
    for (int i = 0; i < arrayOfInt.length; i++)
    {
      ProcessWatcher localProcessWatcher = new ProcessWatcher(getContext());
      localProcessWatcher.setPid(arrayOfInt[i]);
      addView(localProcessWatcher);
    }
  }
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mHandler.sendEmptyMessage(1);
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mHandler.sendEmptyMessage(2);
  }
  
  public class ProcessWatcher
    extends LinearLayout
  {
    private MemoryTracker.ProcessMemInfo mMemInfo;
    int mPid;
    GraphView mRamGraph;
    TextView mText;
    
    public ProcessWatcher(Context paramContext)
    {
      this(paramContext, null);
    }
    
    public ProcessWatcher(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      float f = getResources().getDisplayMetrics().density;
      this.mText = new TextView(getContext());
      this.mText.setTextColor(-1);
      this.mText.setTextSize(0, 10.0F * f);
      this.mText.setGravity(19);
      int i = (int)(2.0F * f);
      setPadding(i, 0, i, 0);
      this.mRamGraph = new GraphView(getContext());
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, (int)(14.0F * f), 1.0F);
      addView(this.mText, localLayoutParams);
      localLayoutParams.leftMargin = ((int)(4.0F * f));
      localLayoutParams.weight = 0.0F;
      localLayoutParams.width = ((int)(200.0F * f));
      addView(this.mRamGraph, localLayoutParams);
    }
    
    public int getPid()
    {
      return this.mPid;
    }
    
    public String getUptimeString()
    {
      long l1 = this.mMemInfo.getUptime() / 1000L;
      StringBuilder localStringBuilder = new StringBuilder();
      long l2 = l1 / 86400L;
      if (l2 > 0L)
      {
        l1 -= 86400L * l2;
        localStringBuilder.append(l2);
        localStringBuilder.append("d");
      }
      long l3 = l1 / 3600L;
      if (l3 > 0L)
      {
        l1 -= 3600L * l3;
        localStringBuilder.append(l3);
        localStringBuilder.append("h");
      }
      long l4 = l1 / 60L;
      if (l4 > 0L)
      {
        l1 -= 60L * l4;
        localStringBuilder.append(l4);
        localStringBuilder.append("m");
      }
      localStringBuilder.append(l1);
      localStringBuilder.append("s");
      return localStringBuilder.toString();
    }
    
    public void setPid(int paramInt)
    {
      this.mPid = paramInt;
      this.mMemInfo = WeightWatcher.this.mMemoryService.getMemInfo(this.mPid);
      if (this.mMemInfo == null)
      {
        Log.v("WeightWatcher", "Missing info for pid " + this.mPid + ", removing view: " + this);
        WeightWatcher.this.initViews();
      }
    }
    
    public void update()
    {
      TextView localTextView = this.mText;
      StringBuilder localStringBuilder = new StringBuilder().append("(").append(this.mPid);
      if (this.mPid == Process.myPid()) {}
      for (String str = "/A";; str = "/S")
      {
        localTextView.setText(str + ") up " + getUptimeString() + " P=" + this.mMemInfo.currentPss + " U=" + this.mMemInfo.currentUss);
        this.mRamGraph.invalidate();
        return;
      }
    }
    
    public class GraphView
      extends View
    {
      Paint headPaint;
      Paint pssPaint = new Paint();
      Paint ussPaint;
      
      public GraphView(Context paramContext)
      {
        this(paramContext, null);
      }
      
      public GraphView(Context paramContext, AttributeSet paramAttributeSet)
      {
        super(paramAttributeSet);
        this.pssPaint.setColor(-6697984);
        this.ussPaint = new Paint();
        this.ussPaint.setColor(-6750208);
        this.headPaint = new Paint();
        this.headPaint.setColor(-1);
      }
      
      public void onDraw(Canvas paramCanvas)
      {
        int i = paramCanvas.getWidth();
        int j = paramCanvas.getHeight();
        if (WeightWatcher.ProcessWatcher.this.mMemInfo == null) {
          return;
        }
        int k = WeightWatcher.ProcessWatcher.this.mMemInfo.pss.length;
        float f1 = i / k;
        float f2 = Math.max(1.0F, f1);
        float f3 = j / (float)WeightWatcher.ProcessWatcher.this.mMemInfo.max;
        for (int m = 0; m < k; m++)
        {
          float f5 = f1 * m;
          paramCanvas.drawRect(f5, j - f3 * (float)WeightWatcher.ProcessWatcher.this.mMemInfo.pss[m], f5 + f2, j, this.pssPaint);
          paramCanvas.drawRect(f5, j - f3 * (float)WeightWatcher.ProcessWatcher.this.mMemInfo.uss[m], f5 + f2, j, this.ussPaint);
        }
        float f4 = f1 * WeightWatcher.ProcessWatcher.this.mMemInfo.head;
        paramCanvas.drawRect(f4, 0.0F, f4 + f2, j, this.headPaint);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.WeightWatcher
 * JD-Core Version:    0.7.0.1
 */