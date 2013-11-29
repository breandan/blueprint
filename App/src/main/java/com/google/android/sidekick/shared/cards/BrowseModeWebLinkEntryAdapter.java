package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ResearchPageEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class BrowseModeWebLinkEntryAdapter
  extends BaseEntryAdapter
{
  private final Clock mClock;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final Sidekick.ResearchPageEntry mResearchPageEntry;
  @Nullable
  private final Sidekick.Action mWebLinkAction;
  
  public BrowseModeWebLinkEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mWebLinkAction = ProtoUtils.findAction(paramEntry, 158, new int[0]);
    this.mClock = paramClock;
    this.mResearchPageEntry = paramEntry.getBrowseModeWebLinkEntry();
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView1 = paramLayoutInflater.inflate(2130968611, paramViewGroup, false);
    ((TextView)localView1.findViewById(2131296382)).setText(this.mResearchPageEntry.getTitle());
    if (this.mResearchPageEntry.hasCategory()) {
      CardTextUtil.setTextView(localView1, 2131296394, this.mResearchPageEntry.getCategory());
    }
    if (this.mResearchPageEntry.hasDescription()) {
      CardTextUtil.setTextView(localView1, 2131296396, this.mResearchPageEntry.getDescription());
    }
    ArrayList localArrayList = Lists.newArrayList();
    if (this.mResearchPageEntry.hasAuthor())
    {
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = this.mResearchPageEntry.getAuthor();
      localArrayList.add(paramContext.getString(2131362868, arrayOfObject2));
    }
    if (this.mResearchPageEntry.hasLandingSiteDisplayName()) {
      localArrayList.add(CardTextUtil.color(this.mResearchPageEntry.getLandingSiteDisplayName(), paramContext.getResources().getColor(2131230842)));
    }
    if (this.mResearchPageEntry.hasPublishTimestampSeconds()) {
      localArrayList.add(TimeUtilities.getRelativeElapsedString(paramContext, this.mClock.currentTimeMillis() - TimeUnit.SECONDS.toMillis(this.mResearchPageEntry.getPublishTimestampSeconds()), true));
    }
    CardTextUtil.setHyphenatedTextView(localView1, 2131296395, localArrayList);
    if (this.mResearchPageEntry.hasViewTimestampSeconds())
    {
      long l = this.mClock.currentTimeMillis() - TimeUnit.SECONDS.toMillis(this.mResearchPageEntry.getViewTimestampSeconds());
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = TimeUtilities.getElapsedString(paramContext, l, true);
      CardTextUtil.setTextView(localView1, 2131296397, paramContext.getString(2131362869, arrayOfObject1));
    }
    if ((this.mWebLinkAction != null) && (this.mWebLinkAction.hasDisplayMessage())) {}
    for (int i = 1;; i = 0)
    {
      if (this.mResearchPageEntry.hasImage())
      {
        View localView3 = this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView1.findViewById(2131296398), this.mResearchPageEntry.getImage(), 2131689872, 2131689871, 20);
        int j = 2;
        if (i == 0) {
          j |= 0x4;
        }
        this.mPhotoWithAttributionDecorator.setRoundedCorners(localView3, j);
      }
      if (i == 0) {
        break;
      }
      localView1.findViewById(2131296386).setVisibility(0);
      Button localButton = (Button)localView1.findViewById(2131296400);
      localButton.setText(this.mWebLinkAction.getDisplayMessage());
      localButton.setVisibility(0);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), this.mWebLinkAction.getType())
      {
        public void onEntryClick(View paramAnonymousView)
        {
          BrowseModeWebLinkEntryAdapter.this.openUrl(paramAnonymousView.getContext(), BrowseModeWebLinkEntryAdapter.this.mResearchPageEntry.getUrl());
        }
      });
      return localView1;
    }
    View localView2 = localView1.findViewById(2131296385);
    localView2.setPadding(localView2.getPaddingLeft(), localView2.getPaddingTop(), localView2.getPaddingRight(), paramContext.getResources().getDimensionPixelSize(2131689718));
    return localView1;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    openUrl(paramContext, this.mResearchPageEntry.getUrl());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BrowseModeWebLinkEntryAdapter
 * JD-Core Version:    0.7.0.1
 */