package com.google.android.search.core.util;

import android.database.DataSetObservable;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.common.base.Preconditions;

public class ForceableLock
  extends DataSetObservable
{
  private Owner mCurrentOwner;
  private final ScheduledSingleThreadedExecutor mExecutor;
  private final Object mLock;
  
  public ForceableLock(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mExecutor = paramScheduledSingleThreadedExecutor;
    this.mLock = new Object();
  }
  
  private void forceObtainInternal(Owner paramOwner)
  {
    Preconditions.checkState(this.mExecutor.isThisThread());
    synchronized (this.mLock)
    {
      if ((this.mCurrentOwner != null) && (this.mCurrentOwner != paramOwner)) {
        this.mCurrentOwner.forceReleaseLock();
      }
      this.mCurrentOwner = paramOwner;
      this.mLock.notifyAll();
      return;
    }
  }
  
  public void forceObtain(final Owner paramOwner)
    throws InterruptedException
  {
    if (this.mExecutor.isThisThread())
    {
      forceObtainInternal(paramOwner);
      return;
    }
    for (;;)
    {
      synchronized (this.mLock)
      {
        if ((this.mCurrentOwner == null) || (this.mCurrentOwner == paramOwner))
        {
          this.mCurrentOwner = paramOwner;
          return;
        }
      }
      while (this.mCurrentOwner != paramOwner)
      {
        this.mExecutor.execute(new Runnable()
        {
          public void run()
          {
            ForceableLock.this.forceObtainInternal(paramOwner);
          }
        });
        this.mLock.wait();
      }
    }
  }
  
  public void notifyChanged()
  {
    if (this.mExecutor.isThisThread())
    {
      super.notifyChanged();
      return;
    }
    this.mExecutor.execute(new Runnable()
    {
      public void run()
      {
        ForceableLock.this.notifyChanged();
      }
    });
  }
  
  public void release(Owner paramOwner)
  {
    synchronized (this.mLock)
    {
      if (this.mCurrentOwner == paramOwner)
      {
        this.mCurrentOwner = null;
        notifyChanged();
      }
      return;
    }
  }
  
  public boolean tryObtain(Owner paramOwner)
  {
    synchronized (this.mLock)
    {
      if ((this.mCurrentOwner == null) || (this.mCurrentOwner == paramOwner))
      {
        this.mCurrentOwner = paramOwner;
        return true;
      }
      return false;
    }
  }
  
  public static abstract interface Owner
  {
    public abstract void forceReleaseLock();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.ForceableLock
 * JD-Core Version:    0.7.0.1
 */