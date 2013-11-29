package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import com.google.android.sidekick.shared.cards.VideoGameEntryAdapter;
import javax.annotation.Nullable;

public class VideoGameRemoteViewsAdapter
  extends AbstractReminderCapableEntryAdapter<VideoGameEntryAdapter>
{
  public VideoGameRemoteViewsAdapter(VideoGameEntryAdapter paramVideoGameEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramVideoGameEntryAdapter, paramWidgetImageLoader);
  }
  
  protected CharSequence getFirstLine(Context paramContext)
  {
    return ((VideoGameEntryAdapter)getEntryCardViewAdapter()).getTitle();
  }
  
  protected String getImageUrl(Context paramContext)
  {
    Uri localUri = ((VideoGameEntryAdapter)getEntryCardViewAdapter()).getPhotoUri(paramContext);
    if (localUri != null) {
      return localUri.toString();
    }
    return null;
  }
  
  @Nullable
  protected CharSequence getSecondLine(Context paramContext)
  {
    return null;
  }
  
  protected CharSequence getThirdLine(Context paramContext)
  {
    return ((VideoGameEntryAdapter)getEntryCardViewAdapter()).getAvailability();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.VideoGameRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */