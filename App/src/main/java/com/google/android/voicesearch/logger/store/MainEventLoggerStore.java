package com.google.android.voicesearch.logger.store;

import android.os.SystemClock;
import com.google.android.search.core.EventLoggerStore;
import java.util.ArrayList;

public class MainEventLoggerStore
  implements EventLoggerStore
{
  private final ArrayList<Object> mDatas = new ArrayList();
  private final ArrayList<Integer> mEvents = new ArrayList();
  private final ArrayList<Long> mTime = new ArrayList();
  
  public void clearResults()
  {
    try
    {
      this.mEvents.clear();
      this.mTime.clear();
      this.mDatas.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Results getAndClearResults()
  {
    try
    {
      Results localResults = getResults();
      clearResults();
      return localResults;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Results getResults()
  {
    try
    {
      Results local1 = new Results()
      {
        public Object getData(int paramAnonymousInt)
        {
          return this.val$datas.get(paramAnonymousInt);
        }
        
        public int getEvent(int paramAnonymousInt)
        {
          return ((Integer)this.val$events.get(paramAnonymousInt)).intValue();
        }
        
        public long getTime(int paramAnonymousInt)
        {
          return ((Long)this.val$times.get(paramAnonymousInt)).longValue();
        }
        
        public int size()
        {
          return this.val$events.size();
        }
      };
      return local1;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void recordEvent(int paramInt, Object paramObject)
  {
    try
    {
      long l = SystemClock.elapsedRealtime();
      this.mEvents.add(Integer.valueOf(paramInt));
      this.mTime.add(Long.valueOf(l));
      this.mDatas.add(paramObject);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public static abstract interface Results
  {
    public abstract Object getData(int paramInt);
    
    public abstract int getEvent(int paramInt);
    
    public abstract long getTime(int paramInt);
    
    public abstract int size();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.logger.store.MainEventLoggerStore
 * JD-Core Version:    0.7.0.1
 */