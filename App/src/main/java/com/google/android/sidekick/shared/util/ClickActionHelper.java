package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.cards.BaseEntryAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.SimpleEntryClickListener;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.ClickAction;
import javax.annotation.Nullable;

public class ClickActionHelper
{
  public static void addClickActionButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, BaseEntryAdapter paramBaseEntryAdapter, ViewGroup paramViewGroup, Sidekick.ClickAction paramClickAction)
  {
    Preconditions.checkArgument(paramClickAction.hasLabel());
    addClickActionButton(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramBaseEntryAdapter, paramViewGroup, paramClickAction, -1, null);
  }
  
  public static void addClickActionButton(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, BaseEntryAdapter paramBaseEntryAdapter, ViewGroup paramViewGroup, Sidekick.ClickAction paramClickAction, int paramInt, @Nullable CharSequence paramCharSequence)
  {
    boolean bool;
    Button localButton;
    if ((paramCharSequence != null) || (paramClickAction.hasLabel()))
    {
      bool = true;
      Preconditions.checkArgument(bool);
      if (paramClickAction.hasLabel()) {
        paramCharSequence = paramClickAction.getLabel();
      }
      if (paramClickAction.hasIconType())
      {
        int i = getIconResId(paramClickAction);
        if (i != -1) {
          paramInt = i;
        }
      }
      if (paramInt != -1) {
        break label125;
      }
      localButton = (Button)paramLayoutInflater.inflate(2130968622, paramViewGroup, false);
    }
    for (;;)
    {
      localButton.setText(paramCharSequence);
      localButton.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, paramBaseEntryAdapter, paramBaseEntryAdapter.getEntry(), 20, paramClickAction));
      paramViewGroup.addView(localButton);
      return;
      bool = false;
      break;
      label125:
      localButton = (Button)paramLayoutInflater.inflate(2130968624, paramViewGroup, false);
      LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton, paramInt, 0, 0, 0);
    }
  }
  
  public static int getIconResId(Sidekick.ClickAction paramClickAction)
  {
    if (!paramClickAction.hasIconType()) {
      return -1;
    }
    switch (paramClickAction.getIconType())
    {
    case 6: 
    default: 
      return -1;
    case 1: 
      return 2130837706;
    case 2: 
      return 2130837645;
    case 3: 
      return 2130837665;
    case 4: 
      return 2130837636;
    case 5: 
      return 2130837639;
    case 7: 
      return 2130837985;
    case 8: 
      return 2130837679;
    }
    return 2130837689;
  }
  
  public static boolean performClick(PredictiveCardContainer paramPredictiveCardContainer, Sidekick.ClickAction paramClickAction, boolean paramBoolean)
  {
    if (paramClickAction.hasUri()) {
      return startIntent(paramPredictiveCardContainer, new Intent("android.intent.action.VIEW", Uri.parse(paramClickAction.getUri())), paramBoolean);
    }
    if (paramClickAction.hasAction()) {
      return startIntent(paramPredictiveCardContainer, new Intent(paramClickAction.getAction()), paramBoolean);
    }
    if (paramClickAction.hasSearchQuery()) {
      return paramPredictiveCardContainer.startWebSearch(paramClickAction.getSearchQuery(), null);
    }
    return false;
  }
  
  private static boolean startIntent(PredictiveCardContainer paramPredictiveCardContainer, Intent paramIntent, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramIntent.setFlags(268435456);
    }
    IntentStarter localIntentStarter = paramPredictiveCardContainer.getIntentStarter();
    if (localIntentStarter == null) {
      return false;
    }
    return localIntentStarter.startActivity(new Intent[] { paramIntent });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ClickActionHelper
 * JD-Core Version:    0.7.0.1
 */