package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.client.PredictiveCardRefreshManager;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.Entry;

public class LoadMoreCardsAdapter
  extends BaseEntryAdapter
{
  private final PredictiveCardRefreshManager mRefreshManager;
  
  public LoadMoreCardsAdapter(ActivityHelper paramActivityHelper, PredictiveCardRefreshManager paramPredictiveCardRefreshManager)
  {
    super(new Sidekick.Entry(), paramActivityHelper);
    this.mRefreshManager = paramPredictiveCardRefreshManager;
  }
  
  private void showMessage(View paramView, int paramInt)
  {
    paramView.findViewById(2131296764).setVisibility(4);
    TextView localTextView = (TextView)paramView.findViewById(2131296681);
    localTextView.setText(paramInt);
    localTextView.setVisibility(0);
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    final View localView = paramLayoutInflater.inflate(2130968736, paramViewGroup, false);
    localView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        localView.setOnClickListener(null);
        LoadMoreCardsAdapter.this.mRefreshManager.requestMoreCards(LoadMoreCardsAdapter.this, localView);
        localView.findViewById(2131296681).setVisibility(4);
        localView.findViewById(2131296764).setVisibility(0);
      }
    });
    return localView;
  }
  
  public void registerDetailsClickListener(PredictiveCardContainer paramPredictiveCardContainer, View paramView) {}
  
  public void showLoadFailedText(View paramView)
  {
    showMessage(paramView, 2131362182);
  }
  
  public void showNoMoreCardsText(View paramView)
  {
    showMessage(paramView, 2131362333);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.LoadMoreCardsAdapter
 * JD-Core Version:    0.7.0.1
 */