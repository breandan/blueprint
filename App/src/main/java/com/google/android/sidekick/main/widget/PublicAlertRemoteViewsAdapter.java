package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.PublicAlertEntryAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.PublicAlertEntry;

public class PublicAlertRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<PublicAlertEntryAdapter>
{
  public PublicAlertRemoteViewsAdapter(PublicAlertEntryAdapter paramPublicAlertEntryAdapter)
  {
    super(paramPublicAlertEntryAdapter);
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    Sidekick.PublicAlertEntry localPublicAlertEntry = ((PublicAlertEntryAdapter)getEntryCardViewAdapter()).getEntry().getPublicAlertEntry();
    localRemoteViews.setTextViewText(2131297260, Html.fromHtml(localPublicAlertEntry.getTitle()));
    localRemoteViews.setTextColor(2131297260, paramContext.getResources().getColor(2131230843));
    localRemoteViews.setTextViewText(2131297261, Html.fromHtml(localPublicAlertEntry.getText()));
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.PublicAlertRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */