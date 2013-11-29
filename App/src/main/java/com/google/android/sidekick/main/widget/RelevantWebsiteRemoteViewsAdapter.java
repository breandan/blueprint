package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.content.res.Resources;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.RelevantWebsiteEntryAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.RelevantWebsiteEntry;
import java.util.List;

public class RelevantWebsiteRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<RelevantWebsiteEntryAdapter>
{
  public RelevantWebsiteRemoteViewsAdapter(RelevantWebsiteEntryAdapter paramRelevantWebsiteEntryAdapter)
  {
    super(paramRelevantWebsiteEntryAdapter);
  }
  
  public boolean canCreateRemoteViews()
  {
    return !((RelevantWebsiteEntryAdapter)getEntryCardViewAdapter()).getRelevantWebsiteEntries().isEmpty();
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    Sidekick.RelevantWebsiteEntry localRelevantWebsiteEntry = ((Sidekick.Entry)((RelevantWebsiteEntryAdapter)getEntryCardViewAdapter()).getRelevantWebsiteEntries().get(0)).getRelevantWebsiteEntry();
    localRemoteViews.setTextViewText(2131297260, localRelevantWebsiteEntry.getTitle());
    localRemoteViews.setTextViewText(2131297261, RelevantWebsiteEntryAdapter.formatUrlForDisplay(localRelevantWebsiteEntry.getUrl()));
    localRemoteViews.setTextColor(2131297261, paramContext.getResources().getColor(2131230842));
    localRemoteViews.setBoolean(2131297261, "setSingleLine", true);
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.RelevantWebsiteRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */