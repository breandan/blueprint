package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.net.Uri;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.BirthdayCardEntryAdapter;
import com.google.geo.sidekick.Sidekick.BirthdayCardEntry;
import com.google.geo.sidekick.Sidekick.Entry;
import java.text.NumberFormat;

public class BirthdayRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<BirthdayCardEntryAdapter>
{
  private final WidgetImageLoader mImageLoader;
  
  public BirthdayRemoteViewsAdapter(BirthdayCardEntryAdapter paramBirthdayCardEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramBirthdayCardEntryAdapter);
    this.mImageLoader = paramWidgetImageLoader;
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    return createRemoteViewInternal(paramContext);
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    Sidekick.BirthdayCardEntry localBirthdayCardEntry = ((BirthdayCardEntryAdapter)getEntryCardViewAdapter()).getEntry().getBirthdayCardEntry();
    if ((localBirthdayCardEntry.hasOwnBirthday()) && (localBirthdayCardEntry.getOwnBirthday()))
    {
      localRemoteViews.setTextViewText(2131297260, paramContext.getString(2131362661));
      if (localBirthdayCardEntry.hasOwnBirthdaySeconds())
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = NumberFormat.getInstance().format(localBirthdayCardEntry.getOwnBirthdaySeconds());
        localRemoteViews.setTextViewText(2131297261, paramContext.getString(2131362662, arrayOfObject));
      }
    }
    Uri localUri;
    do
    {
      return localRemoteViews;
      localRemoteViews.setTextViewText(2131297260, localBirthdayCardEntry.getName());
      localRemoteViews.setTextViewText(2131297261, paramContext.getString(2131362665));
      localUri = ((BirthdayCardEntryAdapter)getEntryCardViewAdapter()).getPhotoUri(paramContext);
    } while (localUri == null);
    this.mImageLoader.loadImageUri(paramContext, localRemoteViews, 2131297263, localUri, null);
    localRemoteViews.setViewVisibility(2131297263, 0);
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.BirthdayRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */