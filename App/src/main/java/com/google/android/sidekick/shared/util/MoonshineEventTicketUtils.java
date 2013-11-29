package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import com.google.android.search.shared.ui.CrossfadingWebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.geo.sidekick.Sidekick.MoonshineEventTicketEntry;
import javax.annotation.Nullable;

public class MoonshineEventTicketUtils
{
  static final int DATE_FLAGS = 524310;
  static final int TIME_FLAGS = 1;
  
  public static void addBarcode(PredictiveCardContainer paramPredictiveCardContainer, View paramView, String paramString)
  {
    CrossfadingWebImageView localCrossfadingWebImageView = (CrossfadingWebImageView)paramView.findViewById(2131296372);
    localCrossfadingWebImageView.setImageUrl(paramString, paramPredictiveCardContainer.getImageLoader());
    localCrossfadingWebImageView.setVisibility(0);
  }
  
  public static void addTicketNumber(View paramView, String paramString)
  {
    TextView localTextView = (TextView)paramView.findViewById(2131296373);
    localTextView.setText(paramString);
    localTextView.setVisibility(0);
  }
  
  public static void addTtlBanner(View paramView, long paramLong1, Context paramContext, long paramLong2)
  {
    TextView localTextView = (TextView)paramView.findViewById(2131296424);
    if (paramLong1 >= 0L) {
      localTextView.setText(paramContext.getString(2131362584));
    }
    for (;;)
    {
      localTextView.setVisibility(0);
      return;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = DateUtils.formatDateTime(paramContext, paramLong2, 1);
      localTextView.setText(paramContext.getString(2131362583, arrayOfObject));
    }
  }
  
  @Nullable
  public static String getDoorOpenTimeString(Context paramContext, Sidekick.MoonshineEventTicketEntry paramMoonshineEventTicketEntry)
  {
    if (paramMoonshineEventTicketEntry.hasDoorOpenTimeMs()) {
      return paramContext.getString(2131362586, new Object[] { DateUtils.formatDateTime(paramContext, paramMoonshineEventTicketEntry.getDoorOpenTimeMs(), 1), DateUtils.formatDateTime(paramContext, paramMoonshineEventTicketEntry.getDoorOpenTimeMs(), 524310) });
    }
    if (paramMoonshineEventTicketEntry.hasStartTimeMs()) {
      return DateUtils.formatDateTime(paramContext, paramMoonshineEventTicketEntry.getStartTimeMs(), 524310);
    }
    return null;
  }
  
  public static boolean shouldShowTtlBanner(long paramLong, Long paramLong1, Long paramLong2, boolean paramBoolean)
  {
    if ((paramLong1 == null) || (paramLong2 == null) || (!paramBoolean)) {}
    long l1;
    long l2;
    do
    {
      return false;
      l1 = paramLong - paramLong2.longValue();
      l2 = paramLong - paramLong1.longValue();
    } while ((l1 < -900000L) || (l2 > 0L));
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.MoonshineEventTicketUtils
 * JD-Core Version:    0.7.0.1
 */