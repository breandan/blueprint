package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.Entry;

public class BackOfCardTutorialCardAdapter
  extends BaseEntryAdapter
{
  public BackOfCardTutorialCardAdapter(ActivityHelper paramActivityHelper)
  {
    super(new Sidekick.Entry(), paramActivityHelper);
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return paramLayoutInflater.inflate(2130968603, paramViewGroup, false);
  }
  
  public void onDismiss(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer)
  {
    super.onDismiss(paramContext, paramPredictiveCardContainer);
    paramPredictiveCardContainer.recordFirstUseCardDismiss(3);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BackOfCardTutorialCardAdapter
 * JD-Core Version:    0.7.0.1
 */