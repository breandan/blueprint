package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.common.base.Preconditions;

public abstract class BaseEntryRemoteViewsAdapter<T extends EntryCardViewAdapter>
  implements EntryRemoteViewsAdapter<T>
{
  private final T mEntryCardViewAdapter;
  
  public BaseEntryRemoteViewsAdapter(T paramT)
  {
    this.mEntryCardViewAdapter = paramT;
  }
  
  public boolean canCreateRemoteViews()
  {
    return true;
  }
  
  public final RemoteViews createNarrowRemoteView(Context paramContext)
  {
    Preconditions.checkState(canCreateRemoteViews());
    RemoteViews localRemoteViews = createNarrowRemoteViewInternal(paramContext);
    if (localRemoteViews != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      return localRemoteViews;
    }
  }
  
  protected abstract RemoteViews createNarrowRemoteViewInternal(Context paramContext);
  
  public final RemoteViews createRemoteView(Context paramContext)
  {
    Preconditions.checkState(canCreateRemoteViews());
    RemoteViews localRemoteViews = createRemoteViewInternal(paramContext);
    if (localRemoteViews != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      return localRemoteViews;
    }
  }
  
  protected abstract RemoteViews createRemoteViewInternal(Context paramContext);
  
  public T getEntryCardViewAdapter()
  {
    return this.mEntryCardViewAdapter;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.BaseEntryRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */