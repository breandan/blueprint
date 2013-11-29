package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.sidekick.shared.cards.ReservationEntryAdapter;

public class ReservationRemoteViewsAdapter
  extends AbstractThreeLineRemoteViewsAdapter<ReservationEntryAdapter>
{
  public ReservationRemoteViewsAdapter(ReservationEntryAdapter paramReservationEntryAdapter, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramReservationEntryAdapter, paramWidgetImageLoader);
  }
  
  protected CharSequence getFirstLine(Context paramContext)
  {
    return ((ReservationEntryAdapter)getEntryCardViewAdapter()).getLocationName();
  }
  
  protected String getImageUrl(Context paramContext)
  {
    return ((ReservationEntryAdapter)getEntryCardViewAdapter()).getPhotoUrl();
  }
  
  protected CharSequence getSecondLine(Context paramContext)
  {
    return ((ReservationEntryAdapter)getEntryCardViewAdapter()).getContextMessage(paramContext);
  }
  
  protected CharSequence getThirdLine(Context paramContext)
  {
    CharSequence localCharSequence = ((ReservationEntryAdapter)getEntryCardViewAdapter()).getTravelTime(paramContext);
    if (TextUtils.isEmpty(localCharSequence)) {
      localCharSequence = ((ReservationEntryAdapter)getEntryCardViewAdapter()).getLocationAddress();
    }
    return localCharSequence;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.ReservationRemoteViewsAdapter
 * JD-Core Version:    0.7.0.1
 */