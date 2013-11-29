package com.google.android.search.core.state;

import android.os.Bundle;
import com.google.android.common.base.ObserverList;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.velvet.VelvetFactory;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VelvetEventBus
{
  private static final Event ALL_STATES_CHANGED = new Event(31);
  private final ActionState mActionState;
  private final long mCreationId;
  private final DiscoveryState mDiscoveryState;
  private final LoggingState mLoggingState;
  private final ObserverList<Observer> mObservers;
  private Event mPendingEvent;
  private final QueryState mQueryState;
  private final VelvetState[] mStates;
  private final TtsState mTtsState;
  private final UiState mUiState;
  private boolean mUpdating;
  
  public VelvetEventBus(VelvetFactory paramVelvetFactory, Settings paramSettings, long paramLong)
  {
    this.mQueryState = paramVelvetFactory.createQueryState(this);
    this.mActionState = paramVelvetFactory.createActionState(this);
    this.mTtsState = paramVelvetFactory.createTtsState(this, paramSettings);
    this.mUiState = paramVelvetFactory.createUiState(this);
    this.mDiscoveryState = paramVelvetFactory.createDiscoveryState(this);
    this.mLoggingState = paramVelvetFactory.createLoggingState();
    VelvetState[] arrayOfVelvetState = new VelvetState[5];
    arrayOfVelvetState[0] = this.mQueryState;
    arrayOfVelvetState[1] = this.mActionState;
    arrayOfVelvetState[2] = this.mTtsState;
    arrayOfVelvetState[3] = this.mUiState;
    arrayOfVelvetState[4] = this.mDiscoveryState;
    this.mStates = arrayOfVelvetState;
    this.mObservers = new ObserverList();
    this.mCreationId = paramLong;
  }
  
  private void notifyObservers(Event paramEvent)
  {
    Iterator localIterator = this.mObservers.iterator();
    while (localIterator.hasNext()) {
      ((Observer)localIterator.next()).onStateChanged(paramEvent);
    }
  }
  
  public void addObserver(Observer paramObserver)
  {
    
    if (!this.mObservers.hasObserver(paramObserver))
    {
      this.mObservers.addObserver(paramObserver);
      if (this.mUpdating) {
        paramObserver.onStateChanged(ALL_STATES_CHANGED);
      }
    }
    else
    {
      return;
    }
    if (this.mPendingEvent == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mUpdating = true;
      paramObserver.onStateChanged(ALL_STATES_CHANGED);
      this.mUpdating = false;
      if (this.mPendingEvent == null) {
        break;
      }
      notifyStateChanged(0);
      return;
    }
  }
  
  public void dump()
  {
    PrintWriter localPrintWriter = new PrintWriter(System.out);
    dump("", localPrintWriter);
    localPrintWriter.flush();
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("VelvetEventBus:");
    String str1 = paramString + "  ";
    paramPrintWriter.print(str1);
    paramPrintWriter.println("Observers:");
    String str2 = str1 + "  ";
    Iterator localIterator = this.mObservers.iterator();
    while (localIterator.hasNext())
    {
      Observer localObserver = (Observer)localIterator.next();
      paramPrintWriter.print(str2);
      paramPrintWriter.println(localObserver);
    }
    VelvetState[] arrayOfVelvetState = this.mStates;
    int i = arrayOfVelvetState.length;
    for (int j = 0; j < i; j++) {
      arrayOfVelvetState[j].dump(str1, paramPrintWriter);
    }
  }
  
  public ActionState getActionState()
  {
    return this.mActionState;
  }
  
  public DiscoveryState getDiscoveryState()
  {
    return this.mDiscoveryState;
  }
  
  public LoggingState getLoggingState()
  {
    return this.mLoggingState;
  }
  
  public QueryState getQueryState()
  {
    return this.mQueryState;
  }
  
  public TtsState getTtsState()
  {
    return this.mTtsState;
  }
  
  public UiState getUiState()
  {
    return this.mUiState;
  }
  
  void notifyStateChanged(int paramInt)
  {
    if (this.mPendingEvent == null) {
      this.mPendingEvent = new Event(paramInt);
    }
    while (this.mUpdating)
    {
      return;
      Event.access$076(this.mPendingEvent, paramInt);
    }
    ExtraPreconditions.checkMainThread();
    this.mUpdating = true;
    while (this.mPendingEvent != null)
    {
      Event localEvent = this.mPendingEvent;
      this.mPendingEvent = null;
      for (VelvetState localVelvetState : this.mStates)
      {
        localVelvetState.onStateChanged(localEvent);
        if (localVelvetState.takeNotified()) {
          Event.access$076(localEvent, localVelvetState.getId());
        }
      }
      if (this.mPendingEvent == null) {
        notifyObservers(localEvent);
      } else {
        Event.access$076(this.mPendingEvent, localEvent.mFlags);
      }
    }
    this.mUpdating = false;
  }
  
  public void removeObserver(Observer paramObserver)
  {
    ExtraPreconditions.checkMainThread();
    this.mObservers.removeObserver(paramObserver);
  }
  
  public void restoreInstanceState(Bundle paramBundle)
  {
    if (this.mCreationId != paramBundle.getLong("velvet:event_bus", this.mCreationId))
    {
      this.mActionState.restoreInstanceState(paramBundle);
      this.mQueryState.restoreInstanceState(paramBundle);
      this.mUiState.restoreInstanceState(paramBundle);
      this.mTtsState.restoreInstanceState(paramBundle);
      this.mDiscoveryState.restoreInstanceState(paramBundle);
    }
  }
  
  public void saveInstanceState(Bundle paramBundle)
  {
    paramBundle.putLong("velvet:event_bus", this.mCreationId);
    this.mQueryState.saveInstanceState(paramBundle);
    this.mActionState.onSaveInstanceState(paramBundle);
    this.mUiState.onSaveInstanceState(paramBundle);
    this.mTtsState.onSaveInstanceState(paramBundle);
    this.mDiscoveryState.saveInstanceState(paramBundle);
  }
  
  public static class Event
  {
    private int mFlags;
    
    Event(int paramInt)
    {
      this.mFlags = paramInt;
    }
    
    int getFlags()
    {
      return this.mFlags;
    }
    
    public boolean hasActionChanged()
    {
      return (0x2 & this.mFlags) != 0;
    }
    
    public boolean hasDiscoveryChanged()
    {
      return (0x10 & this.mFlags) != 0;
    }
    
    public boolean hasQueryChanged()
    {
      return (0x1 & this.mFlags) != 0;
    }
    
    public boolean hasTtsChanged()
    {
      return (0x8 & this.mFlags) != 0;
    }
    
    public boolean hasUiChanged()
    {
      return (0x4 & this.mFlags) != 0;
    }
    
    public String toString()
    {
      ArrayList localArrayList = Lists.newArrayList();
      if (hasQueryChanged()) {
        localArrayList.add("query");
      }
      if (hasActionChanged()) {
        localArrayList.add("action");
      }
      if (hasUiChanged()) {
        localArrayList.add("ui");
      }
      if (hasTtsChanged()) {
        localArrayList.add("tts");
      }
      return "Event" + localArrayList;
    }
  }
  
  public static abstract interface Observer
  {
    public abstract void onStateChanged(VelvetEventBus.Event paramEvent);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.state.VelvetEventBus
 * JD-Core Version:    0.7.0.1
 */