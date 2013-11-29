package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;

public abstract class SimpleGroupNodeListAdapter
  extends GroupNodeListAdapter
{
  private final PredictiveCardContainer mCardContainer;
  private final int mRowLayout;
  
  public SimpleGroupNodeListAdapter(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.EntryTreeNode paramEntryTreeNode, int paramInt)
  {
    super(paramContext, paramEntryTreeNode);
    this.mCardContainer = paramPredictiveCardContainer;
    this.mRowLayout = paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Sidekick.Entry localEntry = (Sidekick.Entry)getItem(paramInt);
    Context localContext = getContext();
    View localView = LayoutInflater.from(localContext).inflate(this.mRowLayout, paramViewGroup, false);
    populateRow(localContext, this.mCardContainer, localView, localEntry);
    return localView;
  }
  
  protected abstract void populateRow(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.Entry paramEntry);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.SimpleGroupNodeListAdapter
 * JD-Core Version:    0.7.0.1
 */