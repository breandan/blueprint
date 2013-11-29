package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.DismissableLinearLayout;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GroupNodeListAdapter;
import com.google.android.sidekick.shared.ui.ListCardView;
import com.google.android.sidekick.shared.ui.ListCardView.ExpandListener;
import com.google.android.sidekick.shared.ui.ListEntryDismissHandler;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.List;

public abstract class GroupNodeEntryAdapter
  extends BaseEntryAdapter
{
  public GroupNodeEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
  }
  
  public GroupNodeEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, ActivityHelper paramActivityHelper)
  {
    super(paramEntryTreeNode, paramActivityHelper);
  }
  
  protected ListCardView createListCardView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, GroupNodeListAdapter paramGroupNodeListAdapter)
  {
    return createListCardView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, paramGroupNodeListAdapter, 0);
  }
  
  protected ListCardView createListCardView(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, final GroupNodeListAdapter paramGroupNodeListAdapter, int paramInt)
  {
    final ListCardView localListCardView = (ListCardView)paramLayoutInflater.inflate(2130968735, paramViewGroup, false);
    if ((paramInt & 0x1) != 0)
    {
      paramGroupNodeListAdapter.showAllEntries();
      onListExpanded(paramContext, paramPredictiveCardContainer, localListCardView);
      localListCardView.setEntryListAdapter(paramGroupNodeListAdapter);
      if ((paramInt & 0x2) == 0) {
        break label111;
      }
      localListCardView.setDismissEnabled(false);
    }
    for (;;)
    {
      if ((paramInt & 0x4) != 0) {
        localListCardView.hideHeader();
      }
      return localListCardView;
      localListCardView.setExpandListener(new ListCardView.ExpandListener()
      {
        public void onExpand()
        {
          GroupNodeEntryAdapter.this.onListExpanded(paramContext, paramPredictiveCardContainer, localListCardView);
        }
      });
      localListCardView.setHeaderClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 37)
      {
        protected void onEntryClick(View paramAnonymousView)
        {
          paramGroupNodeListAdapter.showAllEntries();
        }
      });
      break;
      label111:
      localListCardView.setOnDismissListener(new ListEntryDismissHandler(getEntry(), paramPredictiveCardContainer));
    }
  }
  
  public Sidekick.Entry getDismissEntry()
  {
    if (getGroupEntryTreeNode() != null)
    {
      List localList = getGroupEntryTreeNode().getEntryList();
      if (localList.size() == 1) {
        return (Sidekick.Entry)localList.get(0);
      }
    }
    return super.getDismissEntry();
  }
  
  protected void onListExpanded(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, ListCardView paramListCardView) {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.GroupNodeEntryAdapter
 * JD-Core Version:    0.7.0.1
 */