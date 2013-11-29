package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Clock.TimeTickListener;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.ClockEntry;
import com.google.geo.sidekick.Sidekick.ClockEntry.TimeZone;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Formatter;

public class ClockEntryAdapter
  extends BaseEntryAdapter
{
  private final Clock mClock;
  private final Sidekick.ClockEntry mClockEntry;
  
  public ClockEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mClock = paramClock;
    this.mClockEntry = paramEntry.getClockEntry();
  }
  
  private String getFormattedText(Context paramContext, Clock paramClock)
  {
    long l = paramClock.currentTimeMillis();
    return DateUtils.formatDateRange(paramContext, new Formatter(new StringBuilder()), l, l, 3, this.mClockEntry.getTimeZone(0).getZoneName()).toString();
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    final View localView = paramLayoutInflater.inflate(2130968716, paramViewGroup, false);
    if (this.mClockEntry.getTimeZoneCount() > 0) {
      ((TextView)localView.findViewById(2131296696)).setText(getFormattedText(paramContext, this.mClock));
    }
    ((TextView)localView.findViewById(2131296697)).setText(this.mClockEntry.getTitle());
    localView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
    {
      public void onTimeTick()
      {
        if (ClockEntryAdapter.this.mClockEntry.getTimeZoneCount() > 0) {
          ((TextView)localView.findViewById(2131296696)).setText(ClockEntryAdapter.this.getFormattedText(this.val$appContext, ClockEntryAdapter.this.mClock));
        }
      }
    }
    {
      public void onViewAttachedToWindow(View paramAnonymousView)
      {
        ClockEntryAdapter.this.mClock.registerTimeTickListener(this.val$timeTickListener);
      }
      
      public void onViewDetachedFromWindow(View paramAnonymousView)
      {
        ClockEntryAdapter.this.mClock.unregisterTimeTickListener(this.val$timeTickListener);
      }
    });
    return localView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.ClockEntryAdapter
 * JD-Core Version:    0.7.0.1
 */