package com.google.android.velvet.actions;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.shared.util.Clock;
import com.google.android.voicesearch.util.AgendaTimeUtil;
import com.google.majel.proto.CalendarProtos.AgendaItem;
import java.util.Iterator;
import java.util.List;

public class AgendaTtsUtil
{
  public static void populateTtsValues(List<CalendarProtos.AgendaItem> paramList, int paramInt, Context paramContext, Clock paramClock)
  {
    Iterator localIterator = paramList.iterator();
    if (localIterator.hasNext())
    {
      CalendarProtos.AgendaItem localAgendaItem = (CalendarProtos.AgendaItem)localIterator.next();
      String str2;
      label49:
      String str3;
      if (AgendaTimeUtil.isToday(localAgendaItem))
      {
        str2 = paramContext.getResources().getString(2131362742);
        str3 = AgendaTimeUtil.format(paramContext, localAgendaItem.getStartTime(), 65);
        switch (paramInt)
        {
        default: 
          if ((!localAgendaItem.getAllDay()) && (!AgendaTimeUtil.alreadyEnded(localAgendaItem, paramClock))) {
            break;
          }
        }
      }
      for (String str4 = paramContext.getResources().getString(2131363689);; str4 = String.format(paramContext.getResources().getString(2131363688), new Object[] { str2, str3 }))
      {
        localAgendaItem.setTtsSingleItemDescription(str4);
        localAgendaItem.setTtsMultipleItemDescription(str4);
        break;
        if (AgendaTimeUtil.isTomorrow(localAgendaItem))
        {
          str2 = paramContext.getResources().getString(2131362765);
          break label49;
        }
        String str1 = paramContext.getResources().getString(2131363683);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = AgendaTimeUtil.format(paramContext, localAgendaItem.getStartTime(), 16);
        str2 = String.format(str1, arrayOfObject);
        break label49;
        if ((localAgendaItem.getAllDay()) || (AgendaTimeUtil.alreadyEnded(localAgendaItem, paramClock)))
        {
          localAgendaItem.setTtsMultipleItemDescription(paramContext.getResources().getString(2131363685));
          localAgendaItem.setTtsSingleItemDescription(paramContext.getResources().getString(2131363687));
          break;
        }
        localAgendaItem.setTtsMultipleItemDescription(String.format(paramContext.getResources().getString(2131363684), new Object[] { str2, str3 }));
        localAgendaItem.setTtsSingleItemDescription(String.format(paramContext.getResources().getString(2131363686), new Object[] { str2, str3 }));
        break;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.actions.AgendaTtsUtil
 * JD-Core Version:    0.7.0.1
 */