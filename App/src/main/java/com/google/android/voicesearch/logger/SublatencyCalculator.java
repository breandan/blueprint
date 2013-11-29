package com.google.android.voicesearch.logger;

import android.util.SparseIntArray;
import com.google.speech.logs.VoicesearchClientLogProto.LatencyBreakdownEvent;
import com.google.speech.logs.VoicesearchClientLogProto.LatencyData;
import java.util.Iterator;
import java.util.List;

public class SublatencyCalculator
{
  private final SparseIntArray mSeenBreakDownEvents = new SparseIntArray();
  private final SparseIntArray mSublatencies = new SparseIntArray();
  
  public SublatencyCalculator()
  {
    this.mSublatencies.put(2, 1);
    this.mSublatencies.put(10, 8);
    this.mSublatencies.put(11, 9);
    this.mSublatencies.put(15, 14);
    this.mSublatencies.put(26, 25);
    this.mSublatencies.put(30, 29);
    this.mSublatencies.put(31, 29);
  }
  
  public void addBreakDownSublatency(VoicesearchClientLogProto.LatencyData paramLatencyData)
  {
    Iterator localIterator = paramLatencyData.getBreakdownList().iterator();
    while (localIterator.hasNext())
    {
      VoicesearchClientLogProto.LatencyBreakdownEvent localLatencyBreakdownEvent = (VoicesearchClientLogProto.LatencyBreakdownEvent)localIterator.next();
      this.mSeenBreakDownEvents.put(localLatencyBreakdownEvent.getEvent(), localLatencyBreakdownEvent.getOffsetMsec());
      if (this.mSublatencies.indexOfKey(localLatencyBreakdownEvent.getEvent()) >= 0)
      {
        int i = this.mSublatencies.get(localLatencyBreakdownEvent.getEvent());
        if (this.mSeenBreakDownEvents.indexOfKey(i) >= 0)
        {
          int j = this.mSeenBreakDownEvents.get(i);
          localLatencyBreakdownEvent.setSublatency(localLatencyBreakdownEvent.getOffsetMsec() - j);
        }
      }
    }
    this.mSeenBreakDownEvents.clear();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.logger.SublatencyCalculator
 * JD-Core Version:    0.7.0.1
 */