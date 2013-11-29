package com.google.android.sidekick.shared.cards;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.training.BackOfCardAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import javax.annotation.Nullable;

public abstract interface EntryCardViewAdapter
{
  public abstract BackOfCardAdapter createBackOfCardAdapter(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor);
  
  @Nullable
  public abstract View findViewForChildEntry(View paramView, Sidekick.Entry paramEntry);
  
  public abstract Sidekick.Entry getDismissEntry();
  
  public abstract Sidekick.Entry getEntry();
  
  @Nullable
  public abstract Sidekick.EntryTreeNode getGroupEntryTreeNode();
  
  public abstract String getLoggingName();
  
  public abstract View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup);
  
  public abstract void maybeShowFeedbackPrompt(PredictiveCardContainer paramPredictiveCardContainer, ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater);
  
  public abstract void onDismiss(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer);
  
  public abstract void onViewVisibleOnScreen(PredictiveCardContainer paramPredictiveCardContainer);
  
  public abstract void registerActions(Activity paramActivity, PredictiveCardContainer paramPredictiveCardContainer, View paramView);
  
  public abstract void registerBackOfCardMenuListener(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView);
  
  public abstract void registerDetailsClickListener(PredictiveCardContainer paramPredictiveCardContainer, View paramView);
  
  public abstract void registerTouchListener(View paramView);
  
  public abstract void replaceEntry(Sidekick.Entry paramEntry);
  
  public abstract View updateView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, View paramView, Sidekick.Entry paramEntry);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.EntryCardViewAdapter
 * JD-Core Version:    0.7.0.1
 */