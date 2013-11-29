package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.SportsEntryAdapter;
import com.google.android.sidekick.shared.cards.SportsEntryAdapter.LogoUriWrapper;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.SportScoreEntry;
import com.google.geo.sidekick.Sidekick.SportScoreEntry.SportEntity;

public class SportsRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<SportsEntryAdapter>
{
  private final WidgetImageLoader mImageLoader;
  
  public SportsRemoteViewsAdapter(SportsEntryAdapter paramSportsEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramSportsEntryAdapter);
    this.mImageLoader = paramWidgetImageLoader;
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968932);
    Sidekick.SportScoreEntry localSportScoreEntry = ((SportsEntryAdapter)getEntryCardViewAdapter()).getEntry().getSportScoreEntry();
    Sidekick.SportScoreEntry.SportEntity localSportEntity1 = localSportScoreEntry.getSportEntity(0);
    Sidekick.SportScoreEntry.SportEntity localSportEntity2 = localSportScoreEntry.getSportEntity(1);
    localRemoteViews.setTextViewText(2131297254, localSportEntity1.getName());
    localRemoteViews.setTextViewText(2131297257, localSportEntity2.getName());
    if (localSportEntity1.hasScore())
    {
      localRemoteViews.setTextViewText(2131297255, localSportEntity1.getScore());
      localRemoteViews.setViewVisibility(2131297255, 0);
    }
    if (localSportEntity2.hasScore())
    {
      localRemoteViews.setTextViewText(2131297258, localSportEntity2.getScore());
      localRemoteViews.setViewVisibility(2131297258, 0);
    }
    SportsEntryAdapter.LogoUriWrapper localLogoUriWrapper1 = SportsEntryAdapter.LogoUriWrapper.fromSportEntry(localSportScoreEntry, localSportEntity1);
    SportsEntryAdapter.LogoUriWrapper localLogoUriWrapper2 = SportsEntryAdapter.LogoUriWrapper.fromSportEntry(localSportScoreEntry, localSportEntity2);
    Uri localUri1 = localLogoUriWrapper1.getUri();
    if (localUri1 != null) {
      this.mImageLoader.loadImageUri(paramContext, localRemoteViews, 2131297253, localUri1, localLogoUriWrapper1.getClipRect());
    }
    Uri localUri2 = localLogoUriWrapper2.getUri();
    if (localUri2 != null) {
      this.mImageLoader.loadImageUri(paramContext, localRemoteViews, 2131297256, localUri2, localLogoUriWrapper2.getClipRect());
    }
    String str = ((SportsEntryAdapter)getEntryCardViewAdapter()).getStatus(paramContext, localSportScoreEntry.getStatusCode());
    if (!TextUtils.isEmpty(str))
    {
      localRemoteViews.setTextViewText(2131296308, str);
      localRemoteViews.setViewVisibility(2131296308, 0);
    }
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.SportsRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */