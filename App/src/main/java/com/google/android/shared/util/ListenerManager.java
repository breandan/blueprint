package com.google.android.shared.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;

public class ListenerManager<T>
{
  private final String mAction;
  private final Context mContext;
  private final Dispatcher<T> mDispatcher;
  private Set<T> mListeners;
  private BroadcastReceiver mReceiver;
  
  public ListenerManager(Context paramContext, String paramString, Dispatcher<T> paramDispatcher)
  {
    this.mAction = paramString;
    this.mContext = paramContext;
    this.mDispatcher = paramDispatcher;
    this.mListeners = Sets.newHashSet();
    this.mReceiver = new Receiver(null);
  }
  
  public BroadcastReceiver getReceiver()
  {
    return this.mReceiver;
  }
  
  public boolean isEmpty()
  {
    return this.mListeners.isEmpty();
  }
  
  public void registerListener(T paramT)
  {
    synchronized (this.mListeners)
    {
      if (this.mListeners.isEmpty())
      {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(this.mAction);
        this.mContext.registerReceiver(this.mReceiver, localIntentFilter);
      }
      this.mListeners.add(paramT);
      return;
    }
  }
  
  public void unRegisterListener(T paramT)
  {
    synchronized (this.mListeners)
    {
      if ((this.mListeners.remove(paramT)) && (this.mListeners.isEmpty())) {
        this.mContext.unregisterReceiver(this.mReceiver);
      }
      return;
    }
  }
  
  public static abstract interface Dispatcher<T>
  {
    public abstract void dispatch(T paramT);
  }
  
  private class Receiver
    extends BroadcastReceiver
  {
    private Receiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      synchronized (ListenerManager.this.mListeners)
      {
        ImmutableSet localImmutableSet = ImmutableSet.copyOf(ListenerManager.this.mListeners);
        Iterator localIterator = localImmutableSet.iterator();
        if (localIterator.hasNext())
        {
          Object localObject2 = localIterator.next();
          ListenerManager.this.mDispatcher.dispatch(localObject2);
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.shared.util.ListenerManager
 * JD-Core Version:    0.7.0.1
 */