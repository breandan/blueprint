package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarData;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.shared.cards.CalendarEntryAdapter;
import com.google.geo.sidekick.Sidekick.CalendarEntry;
import com.google.geo.sidekick.Sidekick.Entry;

public class CalendarRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<CalendarEntryAdapter>
{
  private final Calendar.CalendarData mCalendarData;
  
  public CalendarRemoteViewsAdapter(CalendarEntryAdapter paramCalendarEntryAdapter, CalendarDataProvider paramCalendarDataProvider)
  {
    super(paramCalendarEntryAdapter);
    this.mCalendarData = paramCalendarDataProvider.getCalendarDataByServerHash(paramCalendarEntryAdapter.getEntry().getCalendarEntry().getHash());
    if (this.mCalendarData != null) {
      paramCalendarEntryAdapter.setCalendarProviderData(this.mCalendarData);
    }
  }
  
  public boolean canCreateRemoteViews()
  {
    return this.mCalendarData != null;
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    localRemoteViews.setTextViewText(2131297260, ((CalendarEntryAdapter)getEntryCardViewAdapter()).getTitle());
    localRemoteViews.setTextViewText(2131297261, ((CalendarEntryAdapter)getEntryCardViewAdapter()).getFormattedStartTime(paramContext));
    CharSequence localCharSequence = ((CalendarEntryAdapter)getEntryCardViewAdapter()).getWhereField();
    if (!TextUtils.isEmpty(localCharSequence))
    {
      localRemoteViews.setTextViewText(2131297262, localCharSequence);
      localRemoteViews.setViewVisibility(2131297262, 0);
    }
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.CalendarRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */