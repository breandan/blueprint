package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.ListCardView;
import com.google.android.sidekick.shared.ui.SimpleGroupNodeListAdapter;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.RelevantWebsiteEntry;
import java.util.List;
import javax.annotation.Nullable;

public class RelevantWebsiteEntryAdapter
  extends GroupNodeMultiViewEntryAdapter
{
  private final List<Sidekick.Entry> mEntries;
  private final String mTitle;
  
  RelevantWebsiteEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, ActivityHelper paramActivityHelper)
  {
    super(paramEntryTreeNode, paramActivityHelper);
    this.mEntries = paramEntryTreeNode.getEntryList();
    this.mTitle = paramEntryTreeNode.getTitle();
  }
  
  private View createMultipleWebsitesCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    RelevantWebsiteListAdapter localRelevantWebsiteListAdapter = new RelevantWebsiteListAdapter(paramContext, paramPredictiveCardContainer, getGroupEntryTreeNode());
    localRelevantWebsiteListAdapter.setMaxEntries(3);
    ListCardView localListCardView = createListCardView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, localRelevantWebsiteListAdapter, 1);
    localListCardView.setTitle(this.mTitle);
    if (this.mEntries.size() == 1) {}
    for (int i = 2131362759;; i = 2131362760)
    {
      localListCardView.setSmallSummary(paramContext.getString(i));
      return localListCardView;
    }
  }
  
  private View createSingleRelevantWebsiteCard(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Sidekick.Entry paramEntry)
  {
    final Sidekick.RelevantWebsiteEntry localRelevantWebsiteEntry = paramEntry.getRelevantWebsiteEntry();
    View localView = paramLayoutInflater.inflate(2130968802, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(localRelevantWebsiteEntry.getTitle());
    ((TextView)localView.findViewById(2131296936)).setText(paramContext.getString(2131362759));
    populateRelevantWebsiteView(paramPredictiveCardContainer, localRelevantWebsiteEntry, localView);
    localView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 85)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        RelevantWebsiteEntryAdapter.this.openUrl(paramContext, localRelevantWebsiteEntry.getUrl());
      }
    });
    return localView;
  }
  
  public static SpannableStringBuilder formatUrlForDisplay(String paramString)
  {
    Uri localUri = Uri.parse(paramString);
    String str1 = localUri.getScheme();
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramString);
    String str2 = localUri.getHost();
    if ((str2 == null) && (str1 == null))
    {
      int j = paramString.indexOf('/');
      if (j > 0) {
        str2 = paramString.substring(0, j);
      }
    }
    if (str2 != null)
    {
      int i = paramString.indexOf(str2);
      localSpannableStringBuilder.setSpan(new StyleSpan(1), i, i + str2.length(), 17);
    }
    if (str1 != null) {
      localSpannableStringBuilder.delete(0, str1.length());
    }
    while ((localSpannableStringBuilder.length() > 0) && ((localSpannableStringBuilder.charAt(0) == ':') || (localSpannableStringBuilder.charAt(0) == '/'))) {
      localSpannableStringBuilder.delete(0, 1);
    }
    return localSpannableStringBuilder;
  }
  
  private void populateRelevantWebsiteView(PredictiveCardContainer paramPredictiveCardContainer, Sidekick.RelevantWebsiteEntry paramRelevantWebsiteEntry, View paramView)
  {
    if (paramRelevantWebsiteEntry.hasImage())
    {
      WebImageView localWebImageView = (WebImageView)paramView.findViewById(2131296383);
      localWebImageView.setVisibility(0);
      localWebImageView.setImageUrl(paramRelevantWebsiteEntry.getImage().getUrl(), paramPredictiveCardContainer.getImageLoader());
    }
    ((TextView)paramView.findViewById(2131296935)).setText(formatUrlForDisplay(paramRelevantWebsiteEntry.getUrl()));
  }
  
  public List<Sidekick.Entry> getRelevantWebsiteEntries()
  {
    return this.mEntries;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (this.mEntries.size() == 1) {
      return createSingleRelevantWebsiteCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, (Sidekick.Entry)this.mEntries.get(0));
    }
    return createMultipleWebsitesCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
  }
  
  @Nullable
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296450);
  }
  
  public int singleEntryClickDetails(Context paramContext, Sidekick.Entry paramEntry)
  {
    Sidekick.RelevantWebsiteEntry localRelevantWebsiteEntry = paramEntry.getRelevantWebsiteEntry();
    if (localRelevantWebsiteEntry.hasUrl())
    {
      openUrl(paramContext, localRelevantWebsiteEntry.getUrl());
      return 86;
    }
    return -1;
  }
  
  private class RelevantWebsiteListAdapter
    extends SimpleGroupNodeListAdapter
  {
    public RelevantWebsiteListAdapter(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.EntryTreeNode paramEntryTreeNode)
    {
      super(paramPredictiveCardContainer, paramEntryTreeNode, 2130968803);
    }
    
    public void populateRow(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.Entry paramEntry)
    {
      final Sidekick.RelevantWebsiteEntry localRelevantWebsiteEntry = paramEntry.getRelevantWebsiteEntry();
      ((TextView)paramView.findViewById(2131296937)).setText(localRelevantWebsiteEntry.getTitle());
      RelevantWebsiteEntryAdapter.this.populateRelevantWebsiteView(paramPredictiveCardContainer, localRelevantWebsiteEntry, paramView);
      paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 86)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          RelevantWebsiteEntryAdapter.this.openUrl(paramContext, localRelevantWebsiteEntry.getUrl());
        }
      });
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.RelevantWebsiteEntryAdapter
 * JD-Core Version:    0.7.0.1
 */