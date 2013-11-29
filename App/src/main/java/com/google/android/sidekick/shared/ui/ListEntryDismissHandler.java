package com.google.android.sidekick.shared.ui;

import android.view.View;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.PendingViewDismiss.Observer;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.client.UndoDismissManager;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.Entry;
import javax.annotation.Nullable;

public class ListEntryDismissHandler
  implements PendingViewDismiss.Observer, DismissableLinearLayout.OnDismissListener
{
  private final PredictiveCardContainer mCardContainer;
  private final Sidekick.Entry mGroupEntry;
  
  public ListEntryDismissHandler(Sidekick.Entry paramEntry, PredictiveCardContainer paramPredictiveCardContainer)
  {
    this.mGroupEntry = paramEntry;
    this.mCardContainer = paramPredictiveCardContainer;
  }
  
  @Nullable
  private Sidekick.Entry getEntry(PendingViewDismiss paramPendingViewDismiss)
  {
    return (Sidekick.Entry)((View)paramPendingViewDismiss.getDismissedViews().get(0)).getTag(2131296280);
  }
  
  public void onCommit(PendingViewDismiss paramPendingViewDismiss)
  {
    Sidekick.Entry localEntry = getEntry(paramPendingViewDismiss);
    if (localEntry != null) {
      this.mCardContainer.dismissGroupChildEntry(this.mGroupEntry, localEntry);
    }
  }
  
  public void onRestore(PendingViewDismiss paramPendingViewDismiss)
  {
    Sidekick.Entry localEntry = getEntry(paramPendingViewDismiss);
    if (localEntry != null) {
      this.mCardContainer.cancelDismissEntryAction(localEntry);
    }
  }
  
  public void onViewDismissed(PendingViewDismiss paramPendingViewDismiss)
  {
    Sidekick.Entry localEntry = getEntry(paramPendingViewDismiss);
    if (localEntry != null)
    {
      paramPendingViewDismiss.addObserver(this);
      this.mCardContainer.queueDismissEntryAction(localEntry);
      this.mCardContainer.getUndoDismissManager().showUndoToast(paramPendingViewDismiss, localEntry);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.ListEntryDismissHandler
 * JD-Core Version:    0.7.0.1
 */