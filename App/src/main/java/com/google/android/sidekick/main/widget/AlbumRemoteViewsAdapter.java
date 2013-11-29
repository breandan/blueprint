package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import com.google.android.sidekick.shared.cards.AlbumEntryAdapter;

public class AlbumRemoteViewsAdapter
  extends AbstractReminderCapableEntryAdapter<AlbumEntryAdapter>
{
  public AlbumRemoteViewsAdapter(AlbumEntryAdapter paramAlbumEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramAlbumEntryAdapter, paramWidgetImageLoader);
  }
  
  protected CharSequence getFirstLine(Context paramContext)
  {
    return ((AlbumEntryAdapter)getEntryCardViewAdapter()).getTitle();
  }
  
  protected String getImageUrl(Context paramContext)
  {
    Uri localUri = ((AlbumEntryAdapter)getEntryCardViewAdapter()).getPhotoUri(paramContext);
    if (localUri != null) {
      return localUri.toString();
    }
    return null;
  }
  
  protected CharSequence getSecondLine(Context paramContext)
  {
    return ((AlbumEntryAdapter)getEntryCardViewAdapter()).getArtistName();
  }
  
  protected CharSequence getThirdLine(Context paramContext)
  {
    return ((AlbumEntryAdapter)getEntryCardViewAdapter()).getAvailability();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.AlbumRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */