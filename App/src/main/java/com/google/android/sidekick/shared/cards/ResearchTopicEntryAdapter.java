package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GoogleServiceWebviewClickListener;
import com.google.android.sidekick.shared.ui.ListCardView;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.SecondScreenUtil;
import com.google.android.sidekick.shared.util.SecondScreenUtil.Options;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Attribution;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.ResearchTopicEntry;
import com.google.geo.sidekick.Sidekick.SecondaryPageHeaderDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ResearchTopicEntryAdapter
  extends GroupNodeEntryAdapter
{
  private static final String[] URL_PREFIXES_STAY_IN_WEBVIEW = { "www.google.com/now/topics", "www.google.com/plus/history/tasks" };
  private final Sidekick.EntryTreeNode mEntryTreeNode;
  private final PhotoWithAttributionDecorator mPhotoDecorator;
  private final Sidekick.ResearchTopicEntry mResearchTopicEntry;
  
  public ResearchTopicEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, Sidekick.ResearchTopicEntry paramResearchTopicEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntryTreeNode, paramActivityHelper);
    this.mEntryTreeNode = paramEntryTreeNode;
    this.mResearchTopicEntry = paramResearchTopicEntry;
    this.mPhotoDecorator = paramPhotoWithAttributionDecorator;
  }
  
  @Nullable
  private View createImageStrip(Sidekick.ResearchTopicEntry paramResearchTopicEntry, final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, ViewGroup paramViewGroup)
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator1 = paramResearchTopicEntry.getImageList().iterator();
    do
    {
      if (!localIterator1.hasNext()) {
        break;
      }
      Sidekick.Photo localPhoto2 = (Sidekick.Photo)localIterator1.next();
      if ((localPhoto2.getUrlType() == 0) || (!localPhoto2.hasUrlType())) {
        localArrayList.add(localPhoto2);
      }
    } while (localArrayList.size() < 4);
    Object localObject;
    if (localArrayList.size() <= 1) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      localObject = (LinearLayout)LayoutInflater.from(paramContext).inflate(2130968720, paramViewGroup, false);
      Iterator localIterator2 = localArrayList.iterator();
      while (localIterator2.hasNext())
      {
        Sidekick.Photo localPhoto1 = (Sidekick.Photo)localIterator2.next();
        WebImageView localWebImageView = new WebImageView(paramContext);
        localWebImageView.setImageUrl(localPhoto1.getUrl(), paramPredictiveCardContainer.getImageLoader());
        localWebImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        localWebImageView.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 1.0F));
        if (localPhoto1.hasPhotoAttribution())
        {
          final Sidekick.Attribution localAttribution = localPhoto1.getPhotoAttribution();
          if (localAttribution.hasUrl()) {
            localWebImageView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 91)
            {
              public void onEntryClick(View paramAnonymousView)
              {
                ResearchTopicEntryAdapter.this.openUrl(paramContext, localAttribution.getUrl());
              }
            });
          }
        }
        ((LinearLayout)localObject).addView(localWebImageView);
      }
    }
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ResearchTopicListAdapter localResearchTopicListAdapter = new ResearchTopicListAdapter(paramContext, getGroupEntryTreeNode(), paramPredictiveCardContainer, this, this.mPhotoDecorator);
    if (this.mResearchTopicEntry.getCollapsed()) {}
    for (int i = 0;; i = 1)
    {
      if ((!this.mResearchTopicEntry.hasTopic()) && (!this.mResearchTopicEntry.hasActionHeader())) {
        i |= 0x4;
      }
      ListCardView localListCardView = createListCardView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, localResearchTopicListAdapter, i | 0x2);
      if (this.mResearchTopicEntry.hasTopic()) {
        localListCardView.setTitle(this.mResearchTopicEntry.getTopic());
      }
      if (this.mResearchTopicEntry.hasActionHeader()) {
        localListCardView.setSmallSummary(this.mResearchTopicEntry.getActionHeader());
      }
      return localListCardView;
    }
  }
  
  protected void onListExpanded(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, ListCardView paramListCardView)
  {
    View localView = createImageStrip(this.mResearchTopicEntry, paramContext, paramPredictiveCardContainer, paramListCardView);
    if (localView != null) {
      paramListCardView.addCustomSummaryView(localView, true);
    }
    String str1;
    String str2;
    if (this.mResearchTopicEntry.hasExploreMoreUrl()) {
      if (this.mResearchTopicEntry.hasExploreMoreAuthForService())
      {
        str1 = this.mResearchTopicEntry.getExploreMoreAuthForService();
        if (!this.mResearchTopicEntry.hasExploreMoreTitle()) {
          break label127;
        }
        str2 = this.mResearchTopicEntry.getExploreMoreTitle();
        label73:
        paramListCardView.showActionButton(paramContext.getString(2131362632), 2130837706, new GoogleServiceWebviewClickListener(paramContext, this.mResearchTopicEntry.getExploreMoreUrl(), str2, true, this, 143, str1, URL_PREFIXES_STAY_IN_WEBVIEW, this.mEntryTreeNode.getGroupEntry(), paramPredictiveCardContainer));
      }
    }
    label127:
    Sidekick.Action localAction;
    do
    {
      return;
      str1 = null;
      break;
      str2 = this.mResearchTopicEntry.getTopic();
      break label73;
      localAction = ProtoUtils.findAction(getEntry(), 159, new int[0]);
    } while ((localAction == null) || (!localAction.hasDisplayMessage()) || (!localAction.hasInterest()));
    paramListCardView.showActionButton(localAction.getDisplayMessage(), 2130837628, new BrowseClickListener(paramPredictiveCardContainer, localAction, this.mResearchTopicEntry.getSecondaryPageHeader()));
  }
  
  private class BrowseClickListener
    extends EntryClickListener
  {
    private final Sidekick.Action mAction;
    @Nullable
    private final Sidekick.SecondaryPageHeaderDescriptor mSecondaryPageHeader;
    
    public BrowseClickListener(PredictiveCardContainer paramPredictiveCardContainer, Sidekick.Action paramAction, @Nullable Sidekick.SecondaryPageHeaderDescriptor paramSecondaryPageHeaderDescriptor)
    {
      super(ResearchTopicEntryAdapter.this.getEntry(), paramAction.getType());
      this.mAction = paramAction;
      this.mSecondaryPageHeader = paramSecondaryPageHeaderDescriptor;
    }
    
    protected void onEntryClick(View paramView)
    {
      Context localContext = paramView.getContext();
      String str;
      if (this.mSecondaryPageHeader != null)
      {
        str = this.mSecondaryPageHeader.getTitle();
        if (this.mSecondaryPageHeader == null) {
          break label94;
        }
      }
      label94:
      for (Sidekick.Photo localPhoto = this.mSecondaryPageHeader.getContextHeaderImage();; localPhoto = null)
      {
        SecondScreenUtil.Options localOptions = new SecondScreenUtil.Options().setInterest(this.mAction.getInterest()).setTitle(str).setContextHeaderImage(localPhoto);
        ResearchTopicEntryAdapter.this.getActivityHelper().safeStartActivity(localContext, SecondScreenUtil.createIntent(localContext, localOptions));
        return;
        str = this.mAction.getDisplayMessage();
        break;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.ResearchTopicEntryAdapter
 * JD-Core Version:    0.7.0.1
 */