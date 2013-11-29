package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.SimpleEntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.KnowledgeFact;
import com.google.geo.sidekick.Sidekick.TvKnowledgeEntry;
import java.util.Iterator;
import java.util.List;

public class TvKnowledgeEntryAdapter
  extends BaseEntryAdapter
{
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final Sidekick.TvKnowledgeEntry mTvKnowledge;
  
  public TvKnowledgeEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mTvKnowledge = paramEntry.getTvKnowledgeEntry();
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ViewGroup localViewGroup1 = (ViewGroup)paramLayoutInflater.inflate(2130968897, paramViewGroup, false);
    if (this.mTvKnowledge.hasTitle()) {
      CardTextUtil.setTextView(localViewGroup1, 2131297169, this.mTvKnowledge.getTitle());
    }
    if (this.mTvKnowledge.hasSubTitle()) {
      CardTextUtil.setTextView(localViewGroup1, 2131297170, this.mTvKnowledge.getSubTitle());
    }
    if (this.mTvKnowledge.hasSnippet()) {
      CardTextUtil.setTextView(localViewGroup1, 2131297171, this.mTvKnowledge.getSnippet());
    }
    if (this.mTvKnowledge.hasPhoto())
    {
      WebImageView localWebImageView = (WebImageView)localViewGroup1.findViewById(2131297172);
      localWebImageView.setImageUri(this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, this.mTvKnowledge.getPhoto(), 2131689765, 2131689766), paramPredictiveCardContainer.getImageLoader());
      localWebImageView.setVisibility(0);
    }
    if (this.mTvKnowledge.hasSnippetAttribution())
    {
      TextView localTextView2 = CardTextUtil.setTextView(localViewGroup1, 2131297173, this.mTvKnowledge.getSnippetAttribution());
      if (this.mTvKnowledge.hasSnippetAttributionAction()) {
        localTextView2.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, this, getEntry(), 140, this.mTvKnowledge.getSnippetAttributionAction()));
      }
    }
    ViewGroup localViewGroup2 = (ViewGroup)localViewGroup1.findViewById(2131297174);
    Iterator localIterator = this.mTvKnowledge.getFactList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.KnowledgeFact localKnowledgeFact = (Sidekick.KnowledgeFact)localIterator.next();
      TextView localTextView1 = (TextView)paramLayoutInflater.inflate(2130968898, localViewGroup2, false);
      CharSequence[] arrayOfCharSequence = new CharSequence[2];
      arrayOfCharSequence[0] = CardTextUtil.color(localKnowledgeFact.getName(), paramContext.getResources().getColor(2131230834));
      arrayOfCharSequence[1] = localKnowledgeFact.getValue();
      localTextView1.setText(CardTextUtil.hyphenate(arrayOfCharSequence));
      localViewGroup2.addView(localTextView1);
    }
    if (this.mTvKnowledge.hasLastMentionedTimeDescription()) {
      CardTextUtil.setTextView(localViewGroup1, 2131297175, this.mTvKnowledge.getLastMentionedTimeDescription());
    }
    if (this.mTvKnowledge.hasClickAction())
    {
      View localView = localViewGroup1.findViewById(2131297176);
      localView.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, this, getEntry(), 20, this.mTvKnowledge.getClickAction()));
      localView.setVisibility(0);
    }
    return localViewGroup1;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mTvKnowledge.hasClickAction()) {
      handleClickAction(paramContext, paramPredictiveCardContainer, this.mTvKnowledge.getClickAction());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.TvKnowledgeEntryAdapter
 * JD-Core Version:    0.7.0.1
 */