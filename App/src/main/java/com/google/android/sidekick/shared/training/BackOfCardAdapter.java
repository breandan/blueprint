package com.google.android.sidekick.shared.training;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import javax.annotation.Nullable;

public abstract interface BackOfCardAdapter
{
  public abstract void commitFeedback(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer);
  
  public abstract void onPause();
  
  public abstract void onResume();
  
  public abstract void populateBackOfCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater);
  
  public abstract void restoreViewState(Bundle paramBundle);
  
  @Nullable
  public abstract Bundle saveViewState();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.BackOfCardAdapter
 * JD-Core Version:    0.7.0.1
 */