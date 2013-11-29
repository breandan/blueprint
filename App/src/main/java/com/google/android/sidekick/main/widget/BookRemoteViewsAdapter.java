package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import com.google.android.sidekick.shared.cards.BookEntryAdapter;

public class BookRemoteViewsAdapter
  extends AbstractReminderCapableEntryAdapter<BookEntryAdapter>
{
  public BookRemoteViewsAdapter(BookEntryAdapter paramBookEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramBookEntryAdapter, paramWidgetImageLoader);
  }
  
  protected CharSequence getFirstLine(Context paramContext)
  {
    return ((BookEntryAdapter)getEntryCardViewAdapter()).getTitle();
  }
  
  protected String getImageUrl(Context paramContext)
  {
    Uri localUri = ((BookEntryAdapter)getEntryCardViewAdapter()).getPhotoUri(paramContext);
    if (localUri != null) {
      return localUri.toString();
    }
    return null;
  }
  
  protected CharSequence getSecondLine(Context paramContext)
  {
    return ((BookEntryAdapter)getEntryCardViewAdapter()).getAuthorName();
  }
  
  protected CharSequence getThirdLine(Context paramContext)
  {
    return ((BookEntryAdapter)getEntryCardViewAdapter()).getAvailability();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.BookRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */