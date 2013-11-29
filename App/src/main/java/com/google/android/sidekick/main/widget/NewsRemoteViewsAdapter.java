package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.text.Html;
import android.widget.RemoteViews;
import com.google.android.shared.util.BidiUtils;
import com.google.android.sidekick.shared.cards.NewsEntryAdapter;
import com.google.geo.sidekick.Sidekick.NewsEntry;

public class NewsRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<NewsEntryAdapter>
{
  public NewsRemoteViewsAdapter(NewsEntryAdapter paramNewsEntryAdapter)
  {
    super(paramNewsEntryAdapter);
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968757);
    Sidekick.NewsEntry localNewsEntry = ((NewsEntryAdapter)getEntryCardViewAdapter()).getNewsEntry();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = BidiUtils.unicodeWrap(localNewsEntry.getTitle());
    localRemoteViews.setTextViewText(2131296813, Html.fromHtml(paramContext.getString(2131362705, arrayOfObject)));
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.NewsRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */