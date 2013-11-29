package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.text.Html;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.PackageTrackingEntryAdapter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.PackageItem;
import com.google.geo.sidekick.Sidekick.PackageTrackingEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PackageTrackingRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<PackageTrackingEntryAdapter>
{
  public PackageTrackingRemoteViewsAdapter(PackageTrackingEntryAdapter paramPackageTrackingEntryAdapter)
  {
    super(paramPackageTrackingEntryAdapter);
  }
  
  private RemoteViews createRemoteViewInternal(Context paramContext, boolean paramBoolean)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    if (paramBoolean) {}
    ArrayList localArrayList;
    for (CharSequence localCharSequence = getShortTitle(paramContext);; localCharSequence = ((PackageTrackingEntryAdapter)getEntryCardViewAdapter()).getTitle(paramContext))
    {
      localRemoteViews.setTextViewText(2131297260, localCharSequence);
      Integer localInteger = ((PackageTrackingEntryAdapter)getEntryCardViewAdapter()).getTitleColor(paramContext);
      if (localInteger != null) {
        localRemoteViews.setTextColor(2131297260, localInteger.intValue());
      }
      Sidekick.PackageTrackingEntry localPackageTrackingEntry = ((PackageTrackingEntryAdapter)getEntryCardViewAdapter()).getEntry().getPackageTrackingEntry();
      if (localPackageTrackingEntry.getItemsCount() <= 0) {
        return localRemoteViews;
      }
      localArrayList = Lists.newArrayListWithExpectedSize(localPackageTrackingEntry.getItemsCount());
      Iterator localIterator = localPackageTrackingEntry.getItemsList().iterator();
      while (localIterator.hasNext()) {
        localArrayList.add(((Sidekick.PackageItem)localIterator.next()).getName());
      }
    }
    localRemoteViews.setTextViewText(2131297261, Html.fromHtml(Joiner.on(", ").join(localArrayList)));
    return localRemoteViews;
  }
  
  private CharSequence getShortTitle(Context paramContext)
  {
    return paramContext.getString(2131362615);
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext, true);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext, false);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.PackageTrackingRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */