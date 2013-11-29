package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.CardTableLayout;
import com.google.android.sidekick.shared.ui.ListCardView;
import com.google.android.sidekick.shared.ui.SimpleEntryClickListener;
import com.google.android.sidekick.shared.ui.SimpleGroupNodeListAdapter;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.TvKnowledgeEntry;

public class TvKnowledgeListEntryAdapter
  extends GroupNodeEntryAdapter
{
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  public TvKnowledgeListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntryTreeNode, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ListCardView localListCardView = createListCardView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, new TvKnowledgeListAdapter(paramContext, paramPredictiveCardContainer, getGroupEntryTreeNode(), null), 7);
    ((CardTableLayout)localListCardView.findViewById(2131296762)).setDividerColumn(1);
    return localListCardView;
  }
  
  private class TvKnowledgeListAdapter
    extends SimpleGroupNodeListAdapter
  {
    private TvKnowledgeListAdapter(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.EntryTreeNode paramEntryTreeNode)
    {
      super(paramPredictiveCardContainer, paramEntryTreeNode, 2130968899);
    }
    
    public void populateRow(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.Entry paramEntry)
    {
      Sidekick.TvKnowledgeEntry localTvKnowledgeEntry = paramEntry.getTvKnowledgeEntry();
      if ((localTvKnowledgeEntry.hasPhoto()) && (localTvKnowledgeEntry.getPhoto().hasUrl())) {
        ((WebImageView)paramView.findViewById(2131296383)).setImageUri(TvKnowledgeListEntryAdapter.this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, localTvKnowledgeEntry.getPhoto(), 2131689767, 2131689768), paramPredictiveCardContainer.getImageLoader());
      }
      if (localTvKnowledgeEntry.hasTitle()) {
        CardTextUtil.setTextView(paramView, 2131296382, localTvKnowledgeEntry.getTitle());
      }
      if (localTvKnowledgeEntry.hasSubTitle()) {
        CardTextUtil.setTextView(paramView, 2131296309, localTvKnowledgeEntry.getSubTitle());
      }
      if (localTvKnowledgeEntry.hasLastMentionedTimeDescription()) {
        CardTextUtil.setTextView(paramView, 2131297178, localTvKnowledgeEntry.getLastMentionedTimeDescription());
      }
      if (localTvKnowledgeEntry.hasClickAction()) {
        paramView.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, TvKnowledgeListEntryAdapter.this, paramEntry, 141, localTvKnowledgeEntry.getClickAction()));
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.TvKnowledgeListEntryAdapter
 * JD-Core Version:    0.7.0.1
 */