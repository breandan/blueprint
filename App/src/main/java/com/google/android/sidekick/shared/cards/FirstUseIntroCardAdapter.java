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

public class FirstUseIntroCardAdapter
  extends BaseEntryAdapter
{
  public FirstUseIntroCardAdapter(ActivityHelper paramActivityHelper)
  {
    super(new Sidekick.Entry(), paramActivityHelper);
  }
  
  private int getWelcomeStringId()
  {
    return ((Integer)TimeUtilities.getTimeOfDayDependentObject(Integer.valueOf(2131362840), Integer.valueOf(2131362841), Integer.valueOf(2131362842), Integer.valueOf(2131362843), Integer.valueOf(2131362844))).intValue();
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    paramPredictiveCardContainer.recordFirstUseCardView(0);
    View localView = paramLayoutInflater.inflate(2130968688, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296610)).setText(getWelcomeStringId());
    ((Button)localView.findViewById(2131296611)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        IntentDispatcherUtil.dispatchIntent(paramContext, "com.google.android.googlequicksearchbox.TRAINING_CLOSET");
      }
    });
    return localView;
  }
  
  public void onDismiss(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    super.onDismiss(paramContext, paramPredictiveCardContainer);
    paramPredictiveCardContainer.recordFirstUseCardDismiss(0);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.FirstUseIntroCardAdapter
 * JD-Core Version:    0.7.0.1
 */