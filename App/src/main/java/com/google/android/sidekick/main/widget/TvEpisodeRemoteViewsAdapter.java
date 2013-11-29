package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import com.google.android.sidekick.shared.cards.TvEpisodeEntryAdapter;
import javax.annotation.Nullable;

public class TvEpisodeRemoteViewsAdapter
  extends AbstractReminderCapableEntryAdapter<TvEpisodeEntryAdapter>
{
  public TvEpisodeRemoteViewsAdapter(TvEpisodeEntryAdapter paramTvEpisodeEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramTvEpisodeEntryAdapter, paramWidgetImageLoader);
  }
  
  protected CharSequence getFirstLine(Context paramContext)
  {
    return ((TvEpisodeEntryAdapter)getEntryCardViewAdapter()).getTitle();
  }
  
  @Nullable
  protected String getImageUrl(Context paramContext)
  {
    Uri localUri = ((TvEpisodeEntryAdapter)getEntryCardViewAdapter()).getPhotoUri(paramContext);
    if (localUri != null) {
      return localUri.toString();
    }
    return null;
  }
  
  @Nullable
  protected CharSequence getSecondLine(Context paramContext)
  {
    return ((TvEpisodeEntryAdapter)getEntryCardViewAdapter()).getFormattedShowtimeAndStation();
  }
  
  @Nullable
  protected CharSequence getThirdLine(Context paramContext)
  {
    return ((TvEpisodeEntryAdapter)getEntryCardViewAdapter()).getEpisodeInfo();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.TvEpisodeRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */