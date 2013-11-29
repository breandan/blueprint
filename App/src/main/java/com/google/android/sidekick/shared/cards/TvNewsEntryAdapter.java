package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.ClickActionHelper;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.NewsEntry;
import com.google.geo.sidekick.Sidekick.TvNewsEntry;

public class TvNewsEntryAdapter
  extends BaseEntryAdapter
{
  private final Clock mClock;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final Sidekick.TvNewsEntry mTvNewsEntry;
  
  public TvNewsEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    Preconditions.checkArgument(paramEntry.hasTvNewsEntry());
    Preconditions.checkArgument(paramEntry.getTvNewsEntry().hasNewsEntry());
    this.mTvNewsEntry = paramEntry.getTvNewsEntry();
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mClock = paramClock;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ViewGroup localViewGroup = (ViewGroup)paramLayoutInflater.inflate(2130968901, paramViewGroup, false);
    Sidekick.NewsEntry localNewsEntry = this.mTvNewsEntry.getNewsEntry();
    if (localNewsEntry.hasTitle()) {
      ((TextView)localViewGroup.findViewById(2131296813)).setText(localNewsEntry.getTitle());
    }
    if (localNewsEntry.hasImage()) {
      this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localViewGroup.findViewById(2131296811), localNewsEntry.getImage(), 2131689789, 2131689790, 76);
    }
    if (localNewsEntry.hasSnippet()) {
      CardTextUtil.setTextView(localViewGroup, 2131296396, localNewsEntry.getSnippet());
    }
    if (localNewsEntry.hasSource()) {
      CardTextUtil.setTextView(localViewGroup, 2131296810, NewsEntryAdapter.getSource(paramContext.getResources(), this.mClock, localNewsEntry));
    }
    if (this.mTvNewsEntry.hasLastMentionedTimeDescription()) {
      CardTextUtil.setTextView(localViewGroup, 2131297178, this.mTvNewsEntry.getLastMentionedTimeDescription());
    }
    if (this.mTvNewsEntry.hasNewsClusterAction()) {
      ClickActionHelper.addClickActionButton(paramContext, paramPredictiveCardContainer, paramLayoutInflater, this, localViewGroup, this.mTvNewsEntry.getNewsClusterAction(), 2130837689, paramContext.getString(2131362815));
    }
    return localViewGroup;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mTvNewsEntry.getNewsEntry().hasUrl()) {
      openUrl(paramContext, this.mTvNewsEntry.getNewsEntry().getUrl());
    }
    while (!this.mTvNewsEntry.hasNewsClusterAction()) {
      return;
    }
    handleClickAction(paramContext, paramPredictiveCardContainer, this.mTvNewsEntry.getNewsClusterAction());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.TvNewsEntryAdapter
 * JD-Core Version:    0.7.0.1
 */