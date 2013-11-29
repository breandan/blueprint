package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.ListCardView;
import com.google.android.sidekick.shared.ui.SimpleGroupNodeListAdapter;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.WebsiteUpdateEntry;
import java.util.List;
import javax.annotation.Nullable;

public class WebsiteUpdateEntryAdapter
  extends GroupNodeMultiViewEntryAdapter
{
  private final Clock mClock;
  private final List<Sidekick.Entry> mEntries;
  private final String mTitle;
  
  WebsiteUpdateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntryTreeNode, paramActivityHelper);
    this.mEntries = paramEntryTreeNode.getEntryList();
    this.mTitle = paramEntryTreeNode.getTitle();
    this.mClock = paramClock;
  }
  
  private View createMultipleUpdatesCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    WebsiteUpdateListAdapter localWebsiteUpdateListAdapter = new WebsiteUpdateListAdapter(paramContext, paramPredictiveCardContainer, getGroupEntryTreeNode());
    localWebsiteUpdateListAdapter.setMaxEntries(6);
    ListCardView localListCardView = createListCardView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, localWebsiteUpdateListAdapter, 1);
    localListCardView.setTitle(this.mTitle);
    localListCardView.setSmallSummary(paramContext.getString(2131362877));
    return localListCardView;
  }
  
  private View createSingleWebsiteUpdateCard(PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Sidekick.Entry paramEntry)
  {
    Sidekick.WebsiteUpdateEntry localWebsiteUpdateEntry = paramEntry.getWebsiteUpdateEntry();
    View localView = paramLayoutInflater.inflate(2130968930, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(localWebsiteUpdateEntry.getUpdateTitle());
    populateWebsiteUpdateView(paramPredictiveCardContainer, localWebsiteUpdateEntry, localView);
    return localView;
  }
  
  private void populateWebsiteUpdateView(PredictiveCardContainer paramPredictiveCardContainer, Sidekick.WebsiteUpdateEntry paramWebsiteUpdateEntry, View paramView)
  {
    if (paramWebsiteUpdateEntry.hasImage())
    {
      WebImageView localWebImageView = (WebImageView)paramView.findViewById(2131296383);
      localWebImageView.setVisibility(0);
      localWebImageView.setImageUrl(paramWebsiteUpdateEntry.getImage().getUrl(), paramPredictiveCardContainer.getImageLoader());
    }
    ((TextView)paramView.findViewById(2131297251)).setText(createWebsiteInfoSpan(paramWebsiteUpdateEntry, paramView.getContext()));
  }
  
  public CharSequence createWebsiteInfoSpan(Sidekick.WebsiteUpdateEntry paramWebsiteUpdateEntry, Context paramContext)
  {
    CharSequence localCharSequence = DateUtils.getRelativeTimeSpanString(1000L * paramWebsiteUpdateEntry.getUpdateTimestampSeconds(), this.mClock.currentTimeMillis(), 60000L, 524288);
    return CardTextUtil.hyphenate(new CharSequence[] { CardTextUtil.color(paramWebsiteUpdateEntry.getWebsiteTitle(), paramContext.getResources().getColor(2131230842)), localCharSequence });
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (this.mEntries.size() == 1) {
      return createSingleWebsiteUpdateCard(paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, (Sidekick.Entry)this.mEntries.get(0));
    }
    return createMultipleUpdatesCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
  }
  
  @Nullable
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296450);
  }
  
  public List<Sidekick.Entry> getWebsiteUpdateEntries()
  {
    return this.mEntries;
  }
  
  public int singleEntryClickDetails(Context paramContext, Sidekick.Entry paramEntry)
  {
    Sidekick.WebsiteUpdateEntry localWebsiteUpdateEntry = paramEntry.getWebsiteUpdateEntry();
    if (localWebsiteUpdateEntry.hasClickUrl())
    {
      openUrl(paramContext, localWebsiteUpdateEntry.getClickUrl());
      return 109;
    }
    return -1;
  }
  
  private class WebsiteUpdateListAdapter
    extends SimpleGroupNodeListAdapter
  {
    public WebsiteUpdateListAdapter(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.EntryTreeNode paramEntryTreeNode)
    {
      super(paramPredictiveCardContainer, paramEntryTreeNode, 2130968931);
    }
    
    public void populateRow(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.Entry paramEntry)
    {
      final Sidekick.WebsiteUpdateEntry localWebsiteUpdateEntry = paramEntry.getWebsiteUpdateEntry();
      ((TextView)paramView.findViewById(2131297252)).setText(localWebsiteUpdateEntry.getUpdateTitle());
      WebsiteUpdateEntryAdapter.this.populateWebsiteUpdateView(paramPredictiveCardContainer, localWebsiteUpdateEntry, paramView);
      paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 109)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          WebsiteUpdateEntryAdapter.this.openUrl(paramContext, localWebsiteUpdateEntry.getClickUrl());
        }
      });
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.WebsiteUpdateEntryAdapter
 * JD-Core Version:    0.7.0.1
 */