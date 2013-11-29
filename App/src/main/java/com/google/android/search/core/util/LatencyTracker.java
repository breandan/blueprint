package com.google.android.search.core.util;

import android.util.Log;
import com.google.android.shared.util.Clock;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class LatencyTracker
  extends Latency
{
  private int mError;
  private List<Event> mEvents = Lists.newArrayList();
  private boolean mIsLogged;
  
  public LatencyTracker(Clock paramClock)
  {
    super(paramClock);
  }
  
  public String getLatencyEvents()
  {
    StringBuffer localStringBuffer = new StringBuffer(100);
    Iterator localIterator = this.mEvents.iterator();
    while (localIterator.hasNext())
    {
      Event localEvent = (Event)localIterator.next();
      localStringBuffer.append(localEvent.type);
      localStringBuffer.append('-');
      localStringBuffer.append(localEvent.latency);
      localStringBuffer.append(',');
    }
    return localStringBuffer.toString();
  }
  
  public boolean isLogged()
  {
    return this.mIsLogged;
  }
  
  public void logLatencyEvents(int paramInt1, int paramInt2)
  {
    Log.i("LATENCY", getLatencyEvents());
    EventLogger.recordClientEvent(paramInt1, new EventList(getStartTime(), getCurrentTime(), this.mError, this.mEvents, paramInt2));
    this.mEvents = Lists.newArrayList();
    this.mError = -1;
    this.mIsLogged = true;
  }
  
  public void reportError(int paramInt)
  {
    reportLatencyEvent(41);
    this.mError = paramInt;
  }
  
  public void reportLatencyEvent(int paramInt)
  {
    Event localEvent = new Event(paramInt, getLatency());
    this.mEvents.add(localEvent);
  }
  
  public void reset()
  {
    super.reset();
    this.mEvents.clear();
    this.mIsLogged = false;
  }
  
  public static class Event
  {
    public final int latency;
    public final int type;
    
    public Event(int paramInt1, int paramInt2)
    {
      this.type = paramInt1;
      this.latency = paramInt2;
    }
    
    public String toString()
    {
      return super.toString();
    }
  }
  
  public static class EventList
  {
    public final long endTimestamp;
    public final int error;
    public final List<LatencyTracker.Event> events;
    public final int networkType;
    public final long startTimestamp;
    
    public EventList(long paramLong1, long paramLong2, int paramInt1, List<LatencyTracker.Event> paramList, int paramInt2)
    {
      this.startTimestamp = paramLong1;
      this.endTimestamp = paramLong2;
      this.error = paramInt1;
      this.events = paramList;
      this.networkType = paramInt2;
    }
    
    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer("EventList ");
      Iterator localIterator = this.events.iterator();
      while (localIterator.hasNext())
      {
        localStringBuffer.append((LatencyTracker.Event)localIterator.next());
        localStringBuffer.append(",");
      }
      if (this.error > 0)
      {
        localStringBuffer.append(" error=");
        localStringBuffer.append(this.error);
      }
      return localStringBuffer.toString();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.LatencyTracker
 * JD-Core Version:    0.7.0.1
 */