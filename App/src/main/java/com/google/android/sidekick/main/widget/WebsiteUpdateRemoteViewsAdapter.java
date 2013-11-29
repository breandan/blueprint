package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.WebsiteUpdateEntryAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.WebsiteUpdateEntry;
import java.util.List;

public class WebsiteUpdateRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<WebsiteUpdateEntryAdapter>
{
  public WebsiteUpdateRemoteViewsAdapter(WebsiteUpdateEntryAdapter paramWebsiteUpdateEntryAdapter)
  {
    super(paramWebsiteUpdateEntryAdapter);
  }
  
  public boolean canCreateRemoteViews()
  {
    return !((WebsiteUpdateEntryAdapter)getEntryCardViewAdapter()).getWebsiteUpdateEntries().isEmpty();
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    Sidekick.WebsiteUpdateEntry localWebsiteUpdateEntry = ((Sidekick.Entry)((WebsiteUpdateEntryAdapter)getEntryCardViewAdapter()).getWebsiteUpdateEntries().get(0)).getWebsiteUpdateEntry();
    localRemoteViews.setTextViewText(2131297260, localWebsiteUpdateEntry.getUpdateTitle());
    localRemoteViews.setTextViewText(2131297261, ((WebsiteUpdateEntryAdapter)getEntryCardViewAdapter()).createWebsiteInfoSpan(localWebsiteUpdateEntry, paramContext));
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.WebsiteUpdateRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */