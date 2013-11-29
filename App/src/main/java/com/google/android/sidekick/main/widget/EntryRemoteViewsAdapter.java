package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;

public abstract interface EntryRemoteViewsAdapter<T extends EntryCardViewAdapter>
{
  public abstract boolean canCreateRemoteViews();
  
  public abstract RemoteViews createNarrowRemoteView(Context paramContext);
  
  public abstract RemoteViews createRemoteView(Context paramContext);
  
  public abstract T getEntryCardViewAdapter();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.EntryRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */