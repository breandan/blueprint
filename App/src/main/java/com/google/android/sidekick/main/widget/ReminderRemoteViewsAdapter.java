package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.widget.RemoteViews;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.cards.ReminderEntryAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ReminderEntry;

public class ReminderRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<ReminderEntryAdapter>
{
  private final Clock mClock;
  
  public ReminderRemoteViewsAdapter(ReminderEntryAdapter paramReminderEntryAdapter, Clock paramClock)
  {
    super(paramReminderEntryAdapter);
    this.mClock = paramClock;
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    Sidekick.ReminderEntry localReminderEntry = ((ReminderEntryAdapter)getEntryCardViewAdapter()).getEntry().getReminderEntry();
    localRemoteViews.setTextViewText(2131297260, localReminderEntry.getReminderMessage());
    CharSequence localCharSequence = ReminderEntryAdapter.getSubtitleMessage(paramContext, localReminderEntry, this.mClock.currentTimeMillis());
    localRemoteViews.setViewVisibility(2131297261, 4);
    if (localCharSequence != null)
    {
      localRemoteViews.setTextViewText(2131297262, localCharSequence);
      localRemoteViews.setTextViewCompoundDrawables(2131297262, 2130837841, 0, 0, 0);
      localRemoteViews.setViewVisibility(2131297262, 0);
      return localRemoteViews;
    }
    localRemoteViews.setViewVisibility(2131297262, 8);
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.ReminderRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */