package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.GenericPlaceEntryAdapter;

public class TrafficRemoteViewsAdapter
  extends BaseEntryRemoteViewsAdapter<GenericPlaceEntryAdapter>
{
  public TrafficRemoteViewsAdapter(GenericPlaceEntryAdapter paramGenericPlaceEntryAdapter)
  {
    super(paramGenericPlaceEntryAdapter);
  }
  
  public boolean canCreateRemoteViews()
  {
    return !((GenericPlaceEntryAdapter)getEntryCardViewAdapter()).placeConfirmationRequested();
  }
  
  public RemoteViews createNarrowRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = ((GenericPlaceEntryAdapter)getEntryCardViewAdapter()).getTitle(paramContext);
    localRemoteViews.setTextViewText(2131297260, paramContext.getString(2131362215, arrayOfObject));
    String str = ((GenericPlaceEntryAdapter)getEntryCardViewAdapter()).getShortEtaString(paramContext);
    if (str != null)
    {
      localRemoteViews.setTextViewText(2131297261, str);
      int i = ((GenericPlaceEntryAdapter)getEntryCardViewAdapter()).getTrafficColor(paramContext, 2131230834);
      if (i != -1) {
        localRemoteViews.setTextColor(2131297261, i);
      }
      localRemoteViews.setTextViewTextSize(2131297260, 0, paramContext.getResources().getDimension(2131689800));
      localRemoteViews.setTextViewTextSize(2131297261, 0, paramContext.getResources().getDimension(2131689803));
    }
    return localRemoteViews;
  }
  
  public RemoteViews createRemoteViewInternal(Context paramContext)
  {
    RemoteViews localRemoteViews = new RemoteViews(paramContext.getPackageName(), 2130968933);
    localRemoteViews.setTextViewText(2131297260, ((GenericPlaceEntryAdapter)getEntryCardViewAdapter()).getFormattedFullTitle(paramContext));
    CharSequence localCharSequence = ((GenericPlaceEntryAdapter)getEntryCardViewAdapter()).getRouteDescription(paramContext);
    if (!TextUtils.isEmpty(localCharSequence)) {
      localRemoteViews.setTextViewText(2131297261, localCharSequence);
    }
    return localRemoteViews;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.TrafficRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */