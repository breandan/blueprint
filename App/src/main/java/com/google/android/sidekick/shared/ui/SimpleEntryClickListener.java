package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.sidekick.shared.cards.BaseEntryAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;

public class SimpleEntryClickListener
  implements View.OnClickListener
{
  private final int mActionType;
  private final BaseEntryAdapter mAdapter;
  private final PredictiveCardContainer mCardContainer;
  private final Sidekick.ClickAction mClickAction;
  private final Context mContext;
  private final Sidekick.Entry mEntry;
  
  public SimpleEntryClickListener(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, BaseEntryAdapter paramBaseEntryAdapter, Sidekick.Entry paramEntry, int paramInt, Sidekick.ClickAction paramClickAction)
  {
    this.mContext = paramContext;
    this.mCardContainer = paramPredictiveCardContainer;
    this.mAdapter = paramBaseEntryAdapter;
    this.mEntry = paramEntry;
    this.mActionType = paramInt;
    this.mClickAction = paramClickAction;
  }
  
  public void onClick(View paramView)
  {
    this.mCardContainer.logAction(this.mEntry, this.mActionType, this.mClickAction);
    this.mAdapter.handleClickAction(this.mContext, this.mCardContainer, this.mClickAction);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.SimpleEntryClickListener
 * JD-Core Version:    0.7.0.1
 */