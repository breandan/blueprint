package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
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
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.EventEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NearbyEventsEntryAdapter
  extends GroupNodeEntryAdapter
{
  private final List<Sidekick.Entry> mEntries;
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  
  NearbyEventsEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, FifeImageUrlUtil paramFifeImageUrlUtil, ActivityHelper paramActivityHelper)
  {
    super(paramEntryTreeNode, paramActivityHelper);
    this.mEntries = paramEntryTreeNode.getEntryList();
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
  }
  
  private String getSummaryText()
  {
    ArrayList localArrayList = new ArrayList(this.mEntries.size());
    Iterator localIterator = this.mEntries.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      if ((localEntry.hasEventEntry()) && (localEntry.getEventEntry().hasTitle())) {
        localArrayList.add(localEntry.getEventEntry().getTitle());
      }
    }
    return TextUtils.join(", ", localArrayList);
  }
  
  private boolean shouldShowImages()
  {
    Iterator localIterator = this.mEntries.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      if ((localEntry.hasEventEntry()) && (localEntry.getEventEntry().hasImage()) && (localEntry.getEventEntry().getImage().hasUrl())) {
        return true;
      }
    }
    return false;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ListCardView localListCardView = createListCardView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, new EventsListAdapter(paramContext, paramPredictiveCardContainer, getGroupEntryTreeNode(), shouldShowImages(), null));
    localListCardView.setTitle(paramContext.getString(2131362657));
    localListCardView.setSummary(getSummaryText());
    return localListCardView;
  }
  
  protected void onListExpanded(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, ListCardView paramListCardView)
  {
    paramListCardView.setTitle(paramContext.getString(2131362658));
    paramListCardView.setSummary(null);
  }
  
  private class EventsListAdapter
    extends SimpleGroupNodeListAdapter
  {
    private final boolean mShowImages;
    
    private EventsListAdapter(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.EntryTreeNode paramEntryTreeNode, boolean paramBoolean)
    {
      super(paramPredictiveCardContainer, paramEntryTreeNode, 2130968754);
      this.mShowImages = paramBoolean;
    }
    
    public void populateRow(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.Entry paramEntry)
    {
      Sidekick.EventEntry localEventEntry = paramEntry.getEventEntry();
      ((TextView)paramView.findViewById(2131296807)).setText(localEventEntry.getTitle());
      TextView localTextView1 = (TextView)paramView.findViewById(2131296404);
      TextView localTextView2;
      CharSequence localCharSequence;
      label101:
      label108:
      WebImageView localWebImageView;
      if (localEventEntry.hasLocation())
      {
        localTextView1.setText(localEventEntry.getLocation().getName());
        localTextView2 = (TextView)paramView.findViewById(2131296808);
        if (!localEventEntry.hasStartTime()) {
          break label358;
        }
        if (!localEventEntry.hasEndTime()) {
          break label342;
        }
        localCharSequence = TimeUtilities.formatDateTimeRangeFromRFC3339(paramContext, localEventEntry.getStartTime(), localEventEntry.getEndTime(), 524296);
        localTextView2.setText(localCharSequence);
        localWebImageView = (WebImageView)paramView.findViewById(2131296552);
        if (!this.mShowImages) {
          break label377;
        }
        if ((!localEventEntry.hasImage()) || (!localEventEntry.getImage().hasUrl())) {
          break label368;
        }
        Sidekick.Photo localPhoto = localEventEntry.getImage();
        String str = localPhoto.getUrl();
        if (localPhoto.getUrlType() == 2)
        {
          Resources localResources = paramContext.getResources();
          str = NearbyEventsEntryAdapter.this.mFifeImageUrlUtil.setImageUrlSmartCrop(localResources.getDimensionPixelSize(2131689733), localResources.getDimensionPixelSize(2131689734), localPhoto.getUrl()).toString();
        }
        localWebImageView.setImageUri(Uri.parse(str), paramPredictiveCardContainer.getImageLoader());
        if (localPhoto.hasInfoUrl()) {
          localWebImageView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 54)
          {
            public void onEntryClick(View paramAnonymousView)
            {
              NearbyEventsEntryAdapter.this.openUrl(paramContext, this.val$infoUrl);
            }
          });
        }
      }
      for (;;)
      {
        if (localEventEntry.getCategoryCount() > 0) {
          ((TextView)paramView.findViewById(2131296809)).setText(TextUtils.join(", ", localEventEntry.getCategoryList()));
        }
        if ((localEventEntry.hasViewAction()) && (localEventEntry.getViewAction().hasUri())) {
          paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 55)
          {
            public void onEntryClick(View paramAnonymousView)
            {
              NearbyEventsEntryAdapter.this.openUrl(paramContext, this.val$uri);
            }
          });
        }
        return;
        localTextView1.setVisibility(8);
        break;
        label342:
        localCharSequence = TimeUtilities.formatDateTimeFromRFC3339(paramContext, localEventEntry.getStartTime(), 524296);
        break label101;
        label358:
        localTextView2.setVisibility(8);
        break label108;
        label368:
        localWebImageView.setVisibility(4);
        continue;
        label377:
        localWebImageView.setVisibility(8);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.NearbyEventsEntryAdapter
 * JD-Core Version:    0.7.0.1
 */