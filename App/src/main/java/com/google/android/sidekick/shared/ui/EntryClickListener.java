package com.google.android.sidekick.shared.ui;

import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import javax.annotation.Nullable;

public abstract class EntryClickListener
  implements View.OnClickListener
{
  protected final int mActionType;
  private final PredictiveCardContainer mCardContainer;
  @Nullable
  private final Sidekick.ClickAction mClickAction;
  protected final Sidekick.Entry mEntry;
  
  public EntryClickListener(PredictiveCardContainer paramPredictiveCardContainer, Sidekick.Entry paramEntry, int paramInt)
  {
    this(paramPredictiveCardContainer, paramEntry, paramInt, null);
  }
  
  public EntryClickListener(PredictiveCardContainer paramPredictiveCardContainer, Sidekick.Entry paramEntry, int paramInt, Sidekick.ClickAction paramClickAction)
  {
    this.mCardContainer = paramPredictiveCardContainer;
    this.mEntry = paramEntry;
    this.mActionType = paramInt;
    this.mClickAction = paramClickAction;
  }
  
  public final void onClick(View paramView)
  {
    if (this.mEntry != null) {
      this.mCardContainer.logAction(this.mEntry, this.mActionType, this.mClickAction);
    }
    onEntryClick(paramView);
  }
  
  protected abstract void onEntryClick(View paramView);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.EntryClickListener
 * JD-Core Version:    0.7.0.1
 */