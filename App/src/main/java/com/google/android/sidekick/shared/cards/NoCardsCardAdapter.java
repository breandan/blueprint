package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.IntentDispatcherUtil;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.geo.sidekick.Sidekick.Entry;

public class NoCardsCardAdapter
  extends BaseEntryAdapter
{
  public NoCardsCardAdapter(ActivityHelper paramActivityHelper)
  {
    super(new Sidekick.Entry(), paramActivityHelper);
  }
  
  private int getTitleStringId()
  {
    return ((Integer)TimeUtilities.getTimeOfDayDependentObject(Integer.valueOf(2131362853), Integer.valueOf(2131362854), Integer.valueOf(2131362855), Integer.valueOf(2131362856), Integer.valueOf(2131362857))).intValue();
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968759, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296817)).setText(getTitleStringId());
    ((Button)localView.findViewById(2131296611)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        IntentDispatcherUtil.dispatchIntent(paramContext, "com.google.android.googlequicksearchbox.TRAINING_CLOSET");
      }
    });
    return localView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.NoCardsCardAdapter
 * JD-Core Version:    0.7.0.1
 */