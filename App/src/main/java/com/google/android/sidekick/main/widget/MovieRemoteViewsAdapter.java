package com.google.android.sidekick.main.widget;

import android.content.Context;
import com.google.android.sidekick.shared.cards.MovieEntryAdapter;

public class MovieRemoteViewsAdapter
  extends AbstractReminderCapableEntryAdapter<MovieEntryAdapter>
{
  public MovieRemoteViewsAdapter(MovieEntryAdapter paramMovieEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramMovieEntryAdapter, paramWidgetImageLoader);
  }
  
  protected CharSequence getFirstLine(Context paramContext)
  {
    return ((MovieEntryAdapter)getEntryCardViewAdapter()).getTitle();
  }
  
  protected String getImageUrl(Context paramContext)
  {
    return ((MovieEntryAdapter)getEntryCardViewAdapter()).getImageUrl();
  }
  
  protected CharSequence getSecondLine(Context paramContext)
  {
    return ((MovieEntryAdapter)getEntryCardViewAdapter()).getDescription(paramContext);
  }
  
  protected CharSequence getThirdLine(Context paramContext)
  {
    return ((MovieEntryAdapter)getEntryCardViewAdapter()).getOnCardJustification();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.MovieRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */