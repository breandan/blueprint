package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import com.google.android.sidekick.shared.cards.EventEntryAdapter;
import com.google.geo.sidekick.Sidekick.Entry;

public class EventRemoteViewsAdapter
  extends AbstractReminderCapableEntryAdapter<EventEntryAdapter>
{
  public EventRemoteViewsAdapter(EventEntryAdapter paramEventEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramEventEntryAdapter, paramWidgetImageLoader);
  }
  
  protected CharSequence getFirstLine(Context paramContext)
  {
    return ((EventEntryAdapter)getEntryCardViewAdapter()).getTitle();
  }
  
  protected String getImageUrl(Context paramContext)
  {
    Uri localUri = ((EventEntryAdapter)getEntryCardViewAdapter()).getImageUri(paramContext);
    if (localUri != null) {
      return localUri.toString();
    }
    return null;
  }
  
  protected CharSequence getSecondLine(Context paramContext)
  {
    if (!((EventEntryAdapter)getEntryCardViewAdapter()).getEntry().hasReminderData()) {
      return ((EventEntryAdapter)getEntryCardViewAdapter()).getStartDate(paramContext);
    }
    return null;
  }
  
  protected CharSequence getThirdLine(Context paramContext)
  {
    return ((EventEntryAdapter)getEntryCardViewAdapter()).getOnCardJustification();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.EventRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */