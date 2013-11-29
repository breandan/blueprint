package com.google.android.sidekick.main.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ReminderData;

public abstract class AbstractReminderCapableEntryAdapter<T extends EntryCardViewAdapter>
  extends AbstractThreeLineRemoteViewsAdapter<T>
{
  protected AbstractReminderCapableEntryAdapter(T paramT, WidgetImageLoader paramWidgetImageLoader)
  {
    super(paramT, paramWidgetImageLoader);
  }
  
  protected void setLineThree(RemoteViews paramRemoteViews, Context paramContext)
  {
    if (getEntryCardViewAdapter().getEntry().hasReminderData())
    {
      String str = getEntryCardViewAdapter().getEntry().getReminderData().getFormattedEventDate();
      if (!TextUtils.isEmpty(str))
      {
        paramRemoteViews.setTextViewText(2131297262, str);
        paramRemoteViews.setViewVisibility(2131297262, 0);
        paramRemoteViews.setTextViewCompoundDrawables(2131297262, 2130837841, 0, 0, 0);
      }
      return;
    }
    super.setLineThree(paramRemoteViews, paramContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.widget.AbstractReminderCapableEntryAdapter
 * JD-Core Version:    0.7.0.1
 */