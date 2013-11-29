package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.NewsEntry;

public class NewsEntryAdapter
  extends BaseEntryAdapter
{
  static final int DATE_FLAGS = 524308;
  private final Clock mClock;
  private final Sidekick.NewsEntry mNewsEntry;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  public NewsEntryAdapter(Sidekick.Entry paramEntry, Sidekick.NewsEntry paramNewsEntry, Clock paramClock, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    int i = paramEntry.getType();
    if ((i == 24) || (i == 57) || (i == 58) || (i == 74) || (i == 88)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mNewsEntry = paramNewsEntry;
      this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
      this.mClock = paramClock;
      return;
    }
  }
  
  public static CharSequence getSource(Resources paramResources, Clock paramClock, Sidekick.NewsEntry paramNewsEntry)
  {
    Preconditions.checkArgument(paramNewsEntry.hasSource());
    CharSequence localCharSequence1 = CardTextUtil.color(paramNewsEntry.getSource(), paramResources.getColor(2131230842));
    boolean bool = paramNewsEntry.hasTimestampSeconds();
    CharSequence localCharSequence2 = null;
    if (bool) {
      localCharSequence2 = DateUtils.getRelativeTimeSpanString(1000L * paramNewsEntry.getTimestampSeconds(), paramClock.currentTimeMillis(), 60000L, 524308);
    }
    return CardTextUtil.hyphenate(new CharSequence[] { localCharSequence1, localCharSequence2 });
  }
  
  public Sidekick.NewsEntry getNewsEntry()
  {
    return this.mNewsEntry;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView1 = paramLayoutInflater.inflate(2130968756, paramViewGroup, false);
    ((TextView)localView1.findViewById(2131296451)).setText(Html.fromHtml(this.mNewsEntry.getTitle()));
    if (this.mNewsEntry.hasSource())
    {
      ((TextView)localView1.findViewById(2131296810)).setText(getSource(paramContext.getResources(), this.mClock, this.mNewsEntry));
      if (!getEntry().hasReason()) {
        break label178;
      }
      ((TextView)localView1.findViewById(2131296554)).setText(Html.fromHtml(getEntry().getReason()));
    }
    for (;;)
    {
      if (this.mNewsEntry.hasImage())
      {
        View localView2 = this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView1.findViewById(2131296811), this.mNewsEntry.getImage(), 2131689789, 2131689790, 76);
        this.mPhotoWithAttributionDecorator.setRoundedCorners(localView2, 2);
      }
      return localView1;
      localView1.findViewById(2131296810).setVisibility(8);
      break;
      label178:
      localView1.findViewById(2131296554).setVisibility(8);
    }
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mNewsEntry.hasUrl()) {
      openUrl(paramContext, this.mNewsEntry.getUrl());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.NewsEntryAdapter
 * JD-Core Version:    0.7.0.1
 */