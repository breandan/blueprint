package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.GroupNodeListAdapter;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.android.sidekick.shared.util.PlaceDataHelper;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.BrowseModeEntityEntry;
import com.google.geo.sidekick.Sidekick.BrowseModeVideoEntry;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Rating;
import com.google.geo.sidekick.Sidekick.ResearchPageEntry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

class ResearchTopicListAdapter
  extends GroupNodeListAdapter
{
  private static final String TAG = Tag.getTag(ResearchTopicListAdapter.class);
  private final PredictiveCardContainer mCardContainer;
  private final BaseEntryAdapter mEntryAdapter;
  private final PhotoWithAttributionDecorator mPhotoDecorator;
  
  ResearchTopicListAdapter(Context paramContext, Sidekick.EntryTreeNode paramEntryTreeNode, PredictiveCardContainer paramPredictiveCardContainer, BaseEntryAdapter paramBaseEntryAdapter, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramContext, paramEntryTreeNode);
    this.mCardContainer = paramPredictiveCardContainer;
    this.mEntryAdapter = paramBaseEntryAdapter;
    this.mPhotoDecorator = paramPhotoWithAttributionDecorator;
  }
  
  private void addClickAction(View paramView, Sidekick.Entry paramEntry, final Sidekick.ClickAction paramClickAction, int paramInt)
  {
    paramView.setOnClickListener(new EntryClickListener(this.mCardContainer, paramEntry, paramInt)
    {
      protected void onEntryClick(View paramAnonymousView)
      {
        ResearchTopicListAdapter.this.mEntryAdapter.handleClickAction(ResearchTopicListAdapter.this.getContext(), ResearchTopicListAdapter.this.mCardContainer, paramClickAction);
      }
    });
  }
  
  private void addUrlAction(View paramView, Sidekick.Entry paramEntry, final Uri paramUri, int paramInt)
  {
    paramView.setOnClickListener(new EntryClickListener(this.mCardContainer, paramEntry, paramInt)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        ResearchTopicListAdapter.this.mEntryAdapter.getActivityHelper().safeViewUriWithMessage(ResearchTopicListAdapter.this.getContext(), paramUri, false, 2131363215);
      }
    });
  }
  
  private View createBrowseModeAuthorStoryRow(ViewGroup paramViewGroup, Sidekick.Entry paramEntry)
  {
    View localView = LayoutInflater.from(getContext()).inflate(2130968607, paramViewGroup, false);
    if (!paramEntry.hasBrowseModeAuthorStoryEntry()) {}
    Sidekick.ResearchPageEntry localResearchPageEntry;
    do
    {
      return localView;
      localResearchPageEntry = paramEntry.getBrowseModeAuthorStoryEntry();
      if (localResearchPageEntry.hasTitle()) {
        CardTextUtil.setTextView(localView, 2131296382, localResearchPageEntry.getTitle());
      }
      ArrayList localArrayList = Lists.newArrayList();
      if (localResearchPageEntry.hasAuthor())
      {
        Context localContext = getContext();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = localResearchPageEntry.getAuthor();
        localArrayList.add(localContext.getString(2131362868, arrayOfObject));
      }
      if (localResearchPageEntry.hasLandingSiteDisplayName()) {
        localArrayList.add(localResearchPageEntry.getLandingSiteDisplayName());
      }
      if (localResearchPageEntry.hasPublishTimestampSeconds()) {
        localArrayList.add(formatTime(localResearchPageEntry.getPublishTimestampSeconds()));
      }
      CardTextUtil.setHyphenatedTextView(localView, 2131296352, localArrayList);
      if (localResearchPageEntry.hasImage())
      {
        WebImageView localWebImageView = (WebImageView)localView.findViewById(2131296383);
        localWebImageView.setImageUri(this.mPhotoDecorator.getPhotoUri(getContext(), localResearchPageEntry.getImage(), 2131689873, 2131689873), this.mCardContainer.getImageLoader());
        localWebImageView.setVisibility(0);
        setConditionalHeight(localView, localWebImageView, 2131689873, 2131689874);
      }
    } while ((!localResearchPageEntry.hasUrl()) || (TextUtils.isEmpty(localResearchPageEntry.getUrl())));
    addUrlAction(localView, paramEntry, Uri.parse(localResearchPageEntry.getUrl()), 156);
    return localView;
  }
  
  private View createBrowseModeEntityRow(ViewGroup paramViewGroup, Sidekick.Entry paramEntry)
  {
    View localView = LayoutInflater.from(getContext()).inflate(2130968608, paramViewGroup, false);
    if (!paramEntry.hasBrowseModeEntityEntry()) {}
    Sidekick.BrowseModeEntityEntry localBrowseModeEntityEntry;
    do
    {
      return localView;
      localBrowseModeEntityEntry = paramEntry.getBrowseModeEntityEntry();
      if (localBrowseModeEntityEntry.hasTitle()) {
        ((TextView)localView.findViewById(2131296382)).setText(localBrowseModeEntityEntry.getTitle());
      }
      if (localBrowseModeEntityEntry.hasRating()) {
        createRatingView(localBrowseModeEntityEntry.getRating(), ((ViewStub)localView.findViewById(2131296384)).inflate());
      }
      CardTextUtil.setHyphenatedTextView(localView, 2131296352, localBrowseModeEntityEntry.getKnownForTermList());
      if (localBrowseModeEntityEntry.hasImage())
      {
        WebImageView localWebImageView = (WebImageView)localView.findViewById(2131296383);
        localWebImageView.setImageUri(this.mPhotoDecorator.getPhotoUri(getContext(), localBrowseModeEntityEntry.getImage(), 2131689873, 2131689873), this.mCardContainer.getImageLoader());
        localWebImageView.setVisibility(0);
        setConditionalHeight(localView, localWebImageView, 2131689873, 2131689874);
      }
    } while (!localBrowseModeEntityEntry.hasClickAction());
    addClickAction(localView, paramEntry, localBrowseModeEntityEntry.getClickAction(), 155);
    return localView;
  }
  
  private View createBrowseModeVideoRow(ViewGroup paramViewGroup, Sidekick.Entry paramEntry)
  {
    View localView1 = LayoutInflater.from(getContext()).inflate(2130968610, paramViewGroup, false);
    if (!paramEntry.hasBrowseModeVideoEntry()) {}
    Sidekick.BrowseModeVideoEntry localBrowseModeVideoEntry;
    do
    {
      return localView1;
      localBrowseModeVideoEntry = paramEntry.getBrowseModeVideoEntry();
      if (localBrowseModeVideoEntry.hasTitle()) {
        CardTextUtil.setTextView(localView1, 2131296382, localBrowseModeVideoEntry.getTitle());
      }
      if (localBrowseModeVideoEntry.hasPublisher()) {
        CardTextUtil.setTextView(localView1, 2131296390, localBrowseModeVideoEntry.getPublisher());
      }
      Resources localResources = getContext().getResources();
      ArrayList localArrayList = Lists.newArrayList();
      if (localBrowseModeVideoEntry.hasPublishTimestampSeconds()) {
        localArrayList.add(formatTime(localBrowseModeVideoEntry.getPublishTimestampSeconds()));
      }
      if (localBrowseModeVideoEntry.hasViewCount())
      {
        int i = localBrowseModeVideoEntry.getViewCount();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(localBrowseModeVideoEntry.getViewCount());
        localArrayList.add(localResources.getQuantityString(2131558442, i, arrayOfObject));
      }
      CardTextUtil.setHyphenatedTextView(localView1, 2131296352, localArrayList);
      if (localBrowseModeVideoEntry.hasImage())
      {
        View localView2 = localView1.findViewById(2131296391);
        localView2.setVisibility(0);
        ((WebImageView)localView1.findViewById(2131296383)).setImageUri(this.mPhotoDecorator.getPhotoUri(getContext(), localBrowseModeVideoEntry.getImage(), 2131689873, 2131689873), this.mCardContainer.getImageLoader());
        if (localBrowseModeVideoEntry.hasDurationSeconds()) {
          CardTextUtil.setTextView(localView1, 2131296392, DateUtils.formatElapsedTime(localBrowseModeVideoEntry.getDurationSeconds()));
        }
        localView1.findViewById(2131296393).setVisibility(0);
        setConditionalHeight(localView1, localView2, 2131689873, 2131689874);
      }
    } while (!localBrowseModeVideoEntry.hasWatchAction());
    addClickAction(localView1, paramEntry, localBrowseModeVideoEntry.getWatchAction(), 157);
    return localView1;
  }
  
  private void createRatingView(Sidekick.Rating paramRating, View paramView)
  {
    int i = paramRating.getSource();
    boolean bool1 = false;
    if (i == 2) {
      bool1 = true;
    }
    if (paramRating.hasNumRatingStarsE3()) {
      PlaceDataHelper.populateStarRating((TextView)paramView.findViewById(2131296861), (ImageView)paramView.findViewById(2131296862), paramRating.getNumRatingStarsE3(), bool1);
    }
    String str;
    switch (paramRating.getSource())
    {
    case 2: 
    default: 
      boolean bool2 = paramRating.hasNumReviews();
      str = null;
      if (bool2)
      {
        Resources localResources = getContext().getResources();
        int j = paramRating.getNumReviews();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(paramRating.getNumReviews());
        str = localResources.getQuantityString(2131558433, j, arrayOfObject);
      }
      break;
    }
    for (;;)
    {
      CharSequence[] arrayOfCharSequence = new CharSequence[2];
      arrayOfCharSequence[0] = paramRating.getRating();
      arrayOfCharSequence[1] = str;
      CardTextUtil.setHyphenatedTextView(paramView, 2131296863, arrayOfCharSequence);
      return;
      str = getContext().getResources().getString(2131362872);
      continue;
      str = getContext().getResources().getString(2131362873);
    }
  }
  
  private View createResearchPageRow(ViewGroup paramViewGroup, Sidekick.Entry paramEntry)
  {
    View localView = LayoutInflater.from(getContext()).inflate(2130968813, paramViewGroup, false);
    if (!paramEntry.hasResearchPageEntry()) {}
    Sidekick.ResearchPageEntry localResearchPageEntry;
    Uri localUri;
    do
    {
      return localView;
      localResearchPageEntry = paramEntry.getResearchPageEntry();
      if (localResearchPageEntry.hasTitle()) {
        ((TextView)localView.findViewById(2131296952)).setText(localResearchPageEntry.getTitle());
      }
      if (localResearchPageEntry.hasDescription()) {
        ((TextView)localView.findViewById(2131296953)).setText(localResearchPageEntry.getDescription());
      }
      localUri = getLinkUri(localResearchPageEntry);
    } while (localUri == null);
    TextView localTextView = (TextView)localView.findViewById(2131296954);
    if (localResearchPageEntry.hasLandingPageDomain()) {
      localTextView.setText(localResearchPageEntry.getLandingPageDomain());
    }
    for (;;)
    {
      addUrlAction(localView, paramEntry, localUri, 144);
      return localView;
      localTextView.setText(localUri.getHost());
    }
  }
  
  private String formatTime(long paramLong)
  {
    return TimeUtilities.getRelativeElapsedString(getContext(), System.currentTimeMillis() - 1000L * paramLong, true);
  }
  
  @Nullable
  private static Uri getLinkUri(Sidekick.ResearchPageEntry paramResearchPageEntry)
  {
    if (paramResearchPageEntry.hasUrl()) {
      try
      {
        Uri localUri = Uri.parse(paramResearchPageEntry.getUrl());
        return localUri;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.i(TAG, "URL failed to parse");
      }
    }
    return null;
  }
  
  private void setConditionalHeight(View paramView1, final View paramView2, int paramInt1, int paramInt2)
  {
    Resources localResources = getContext().getResources();
    paramView1.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
    {
      public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
      {
        if (paramAnonymousInt4 - paramAnonymousInt2 > this.val$normalHeight) {}
        for (int i = this.val$tallHeight;; i = this.val$normalHeight)
        {
          ViewGroup.LayoutParams localLayoutParams = paramView2.getLayoutParams();
          if ((localLayoutParams != null) && (localLayoutParams.height != i))
          {
            localLayoutParams.height = i;
            paramView2.requestLayout();
          }
          return;
        }
      }
    });
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Sidekick.Entry localEntry = getEntry(paramInt);
    switch (localEntry.getType())
    {
    default: 
      Log.w(TAG, "Skip unsupported entry type: " + localEntry.getType());
      return new View(getContext());
    case 33: 
      return createResearchPageRow(paramViewGroup, localEntry);
    case 81: 
      return createBrowseModeEntityRow(paramViewGroup, localEntry);
    case 83: 
      return createBrowseModeVideoRow(paramViewGroup, localEntry);
    }
    return createBrowseModeAuthorStoryRow(paramViewGroup, localEntry);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.ResearchTopicListAdapter
 * JD-Core Version:    0.7.0.1
 */