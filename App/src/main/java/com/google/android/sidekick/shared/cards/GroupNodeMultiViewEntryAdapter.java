package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.View;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.List;
import javax.annotation.Nullable;

public abstract class GroupNodeMultiViewEntryAdapter
  extends GroupNodeEntryAdapter
{
  private final boolean mIsSingleEntry;
  private Sidekick.Entry mSingleEntry;
  
  public GroupNodeMultiViewEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mIsSingleEntry = true;
    this.mSingleEntry = paramEntry;
  }
  
  public GroupNodeMultiViewEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, ActivityHelper paramActivityHelper)
  {
    super(paramEntryTreeNode, paramActivityHelper);
    if (paramEntryTreeNode.getEntryCount() == i) {}
    for (;;)
    {
      this.mIsSingleEntry = i;
      if (!this.mIsSingleEntry) {
        break;
      }
      this.mSingleEntry = ((Sidekick.Entry)paramEntryTreeNode.getEntryList().get(0));
      return;
      i = 0;
    }
    this.mSingleEntry = null;
  }
  
  @Nullable
  Sidekick.Entry getSingleEntry()
  {
    return this.mSingleEntry;
  }
  
  public final void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mIsSingleEntry)
    {
      Sidekick.Entry localEntry = getSingleEntry();
      if (localEntry != null)
      {
        int i = singleEntryClickDetails(paramContext, localEntry);
        if (i != -1) {
          paramPredictiveCardContainer.logAction(localEntry, i, null);
        }
      }
    }
  }
  
  public abstract int singleEntryClickDetails(Context paramContext, Sidekick.Entry paramEntry);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.GroupNodeMultiViewEntryAdapter
 * JD-Core Version:    0.7.0.1
 */