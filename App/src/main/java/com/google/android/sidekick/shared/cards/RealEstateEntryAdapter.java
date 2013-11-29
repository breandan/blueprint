package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.BidiUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.ListCardView;
import com.google.android.sidekick.shared.ui.SimpleGroupNodeListAdapter;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.RealEstateEntry;
import java.util.List;

public class RealEstateEntryAdapter
  extends GroupNodeMultiViewEntryAdapter
{
  private final Clock mClock;
  private final List<Sidekick.Entry> mEntries;
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  
  RealEstateEntryAdapter(Sidekick.Entry paramEntry, Clock paramClock, FifeImageUrlUtil paramFifeImageUrlUtil, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mEntries = null;
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
    this.mClock = paramClock;
  }
  
  RealEstateEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, Clock paramClock, FifeImageUrlUtil paramFifeImageUrlUtil, ActivityHelper paramActivityHelper)
  {
    super(paramEntryTreeNode, paramActivityHelper);
    this.mEntries = paramEntryTreeNode.getEntryList();
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
    this.mClock = paramClock;
  }
  
  private View buildMultipleListingCard(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (getGroupEntryTreeNode().getIsExpanded()) {}
    for (int i = 1;; i = 0)
    {
      ListCardView localListCardView = createListCardView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, new RealEstateListAdapter(paramContext, paramPredictiveCardContainer, getGroupEntryTreeNode()), i);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = BidiUtils.unicodeWrap(getGroupEntryTreeNode().getTitle());
      localListCardView.setTitle(paramContext.getString(2131362720, arrayOfObject));
      localListCardView.setSmallSummary(paramContext.getString(2131362707));
      return localListCardView;
    }
  }
  
  private View buildSingleListingCard(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Sidekick.Entry paramEntry)
  {
    View localView = paramLayoutInflater.inflate(2130968797, paramViewGroup, false);
    final Sidekick.RealEstateEntry localRealEstateEntry = paramEntry.getRealEstateEntry();
    if (paramEntry.getType() == 39) {
      localView.findViewById(2131296905).setVisibility(0);
    }
    if (localRealEstateEntry.getAddressCount() > 0)
    {
      TextView localTextView2 = (TextView)localView.findViewById(2131296906);
      localTextView2.setText(localRealEstateEntry.getAddress(0));
      localTextView2.setVisibility(0);
    }
    if (localRealEstateEntry.getAddressCount() > 1)
    {
      TextView localTextView1 = (TextView)localView.findViewById(2131296907);
      localTextView1.setText(localRealEstateEntry.getAddress(1));
      localTextView1.setVisibility(0);
    }
    showListingPhoto(paramContext, paramPredictiveCardContainer, localView, localRealEstateEntry);
    TableLayout localTableLayout = (TableLayout)localView.findViewById(2131296909);
    TablePopulator localTablePopulator = new TablePopulator(paramContext, paramLayoutInflater, localTableLayout, null);
    if (localRealEstateEntry.hasPrice()) {
      localTablePopulator.addEntry(paramContext.getString(2131362708), localRealEstateEntry.getPrice());
    }
    if (localRealEstateEntry.hasLivingArea()) {
      localTablePopulator.addEntry(paramContext.getString(2131362709), BidiUtils.unicodeWrap(localRealEstateEntry.getLivingArea()));
    }
    if (localRealEstateEntry.hasNumBedroomsAndBathrooms()) {
      localTablePopulator.addEntry(paramContext.getString(2131362710), localRealEstateEntry.getNumBedroomsAndBathrooms());
    }
    if (localRealEstateEntry.hasListingTimestamp())
    {
      long l2 = 1000L * localRealEstateEntry.getListingTimestamp();
      long l3 = (this.mClock.currentTimeMillis() - l2) / 86400000L;
      if (l3 > 0L)
      {
        String str = paramContext.getString(2131362711);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Long.valueOf(l3);
        localTablePopulator.addEntry(str, paramContext.getString(2131362712, arrayOfObject));
      }
    }
    if (localRealEstateEntry.hasSubtype()) {
      localTablePopulator.addEntry(paramContext.getString(2131362713), BidiUtils.unicodeWrap(localRealEstateEntry.getSubtype()));
    }
    if (localRealEstateEntry.hasYearBuilt()) {
      localTablePopulator.addEntry(paramContext.getString(2131362714), Integer.toString(localRealEstateEntry.getYearBuilt()));
    }
    if (localRealEstateEntry.hasNextOpenHouseTimestamp())
    {
      long l1 = 1000L * localRealEstateEntry.getNextOpenHouseTimestamp();
      localView.findViewById(2131296910).setVisibility(0);
      ((TextView)localView.findViewById(2131296911)).setText(DateUtils.formatDateTime(paramContext, l1, 524307));
    }
    if (localRealEstateEntry.hasForSaleSimilarListingsUrl())
    {
      Button localButton2 = (Button)localView.findViewById(2131296912);
      localButton2.setVisibility(0);
      localButton2.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 84)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          RealEstateEntryAdapter.this.openUrl(paramContext, localRealEstateEntry.getForSaleSimilarListingsUrl());
        }
      });
    }
    if (localRealEstateEntry.hasRecentlySoldSimilarListingsUrl())
    {
      Button localButton1 = (Button)localView.findViewById(2131296913);
      localButton1.setVisibility(0);
      localButton1.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 83)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          RealEstateEntryAdapter.this.openUrl(paramContext, localRealEstateEntry.getRecentlySoldSimilarListingsUrl());
        }
      });
    }
    return localView;
  }
  
  private void showListingPhoto(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.RealEstateEntry paramRealEstateEntry)
  {
    if (paramRealEstateEntry.getPhotoCount() > 0)
    {
      Sidekick.Photo localPhoto = paramRealEstateEntry.getPhoto(0);
      String str = localPhoto.getUrl();
      WebImageView localWebImageView = (WebImageView)paramView.findViewById(2131296908);
      if (localPhoto.getUrlType() == 2)
      {
        Resources localResources = paramContext.getResources();
        str = this.mFifeImageUrlUtil.setImageUrlSmartCrop(localResources.getDimensionPixelSize(2131689733), localResources.getDimensionPixelSize(2131689734), localPhoto.getUrl()).toString();
      }
      localWebImageView.setImageUrl(str, paramPredictiveCardContainer.getImageLoader());
      localWebImageView.setVisibility(0);
    }
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    if (this.mEntries == null) {
      return buildSingleListingCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, getEntry());
    }
    if (this.mEntries.size() == 1) {
      return buildSingleListingCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, (Sidekick.Entry)this.mEntries.get(0));
    }
    return buildMultipleListingCard(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
  }
  
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296450);
  }
  
  public int singleEntryClickDetails(Context paramContext, Sidekick.Entry paramEntry)
  {
    Sidekick.RealEstateEntry localRealEstateEntry = paramEntry.getRealEstateEntry();
    if (localRealEstateEntry.hasDetailsUrl())
    {
      openUrl(paramContext, localRealEstateEntry.getDetailsUrl());
      return 84;
    }
    return -1;
  }
  
  private class RealEstateListAdapter
    extends SimpleGroupNodeListAdapter
  {
    public RealEstateListAdapter(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.EntryTreeNode paramEntryTreeNode)
    {
      super(paramPredictiveCardContainer, paramEntryTreeNode, 2130968798);
    }
    
    public void populateRow(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.Entry paramEntry)
    {
      Sidekick.RealEstateEntry localRealEstateEntry = paramEntry.getRealEstateEntry();
      ((TextView)paramView.findViewById(2131296914)).setText(localRealEstateEntry.getAddress(0));
      CharSequence[] arrayOfCharSequence1 = new CharSequence[2];
      arrayOfCharSequence1[0] = localRealEstateEntry.getPrice();
      arrayOfCharSequence1[1] = localRealEstateEntry.getType();
      CardTextUtil.setHyphenatedTextView(paramView, 2131296915, arrayOfCharSequence1);
      CharSequence[] arrayOfCharSequence2 = new CharSequence[2];
      arrayOfCharSequence2[0] = localRealEstateEntry.getNumBedroomsAndBathrooms();
      arrayOfCharSequence2[1] = localRealEstateEntry.getLivingArea();
      CardTextUtil.setHyphenatedTextView(paramView, 2131296916, arrayOfCharSequence2);
      if (localRealEstateEntry.hasNextOpenHouseTimestamp())
      {
        long l = 1000L * localRealEstateEntry.getNextOpenHouseTimestamp();
        paramView.findViewById(2131296910).setVisibility(0);
        ((TextView)paramView.findViewById(2131296911)).setText(DateUtils.formatDateTime(paramContext, l, 524307));
      }
      RealEstateEntryAdapter.this.showListingPhoto(paramContext, paramPredictiveCardContainer, paramView, localRealEstateEntry);
      if (localRealEstateEntry.hasDetailsUrl()) {
        paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 82)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            RealEstateEntryAdapter.this.openUrl(paramContext, this.val$uri);
          }
        });
      }
    }
  }
  
  private static class TablePopulator
  {
    private final Context mContext;
    private TableRow mDataRow;
    private TableRow mHeaderRow;
    private int mIndex = 0;
    private final LayoutInflater mLayoutInflater;
    private final TableLayout mTableLayout;
    
    private TablePopulator(Context paramContext, LayoutInflater paramLayoutInflater, TableLayout paramTableLayout)
    {
      this.mContext = paramContext;
      this.mLayoutInflater = paramLayoutInflater;
      this.mTableLayout = paramTableLayout;
    }
    
    private void addEntry(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
    {
      if ((this.mHeaderRow == null) || (this.mDataRow == null))
      {
        this.mHeaderRow = new TableRow(this.mContext);
        this.mDataRow = new TableRow(this.mContext);
        this.mTableLayout.addView(this.mHeaderRow);
        this.mTableLayout.addView(this.mDataRow);
      }
      TextView localTextView1 = (TextView)this.mLayoutInflater.inflate(2130968854, this.mHeaderRow, false);
      localTextView1.setText(paramCharSequence1);
      this.mHeaderRow.addView(localTextView1);
      TextView localTextView2 = (TextView)this.mLayoutInflater.inflate(2130968853, this.mDataRow, false);
      localTextView2.setText(paramCharSequence2);
      this.mDataRow.addView(localTextView2);
      int i = 1 + this.mIndex;
      this.mIndex = i;
      if (i >= 2)
      {
        this.mIndex = 0;
        this.mHeaderRow = null;
        this.mDataRow = null;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.RealEstateEntryAdapter
 * JD-Core Version:    0.7.0.1
 */