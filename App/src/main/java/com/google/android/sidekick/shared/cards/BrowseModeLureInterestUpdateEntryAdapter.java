package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
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
import com.google.android.sidekick.shared.util.SecondScreenUtil;
import com.google.android.sidekick.shared.util.SecondScreenUtil.Options;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.ResearchPageEntry;
import com.google.geo.sidekick.Sidekick.SecondaryPageHeaderDescriptor;
import java.util.ArrayList;
import java.util.List;

public class BrowseModeLureInterestUpdateEntryAdapter
  extends BaseEntryAdapter
{
  private static final String TAG = Tag.getTag(BrowseModeLureInterestUpdateEntryAdapter.class);
  private final Point mButtonTouchPoint = new Point();
  private final Clock mClock;
  private final Sidekick.Action mLureAction;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final Sidekick.ResearchPageEntry mResearchPageEntry;
  
  public BrowseModeLureInterestUpdateEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator, Clock paramClock)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mResearchPageEntry = paramEntry.getBrowseModeLureInterestUpdateEntry();
    this.mLureAction = ProtoUtils.findAction(paramEntry, 160, new int[0]);
    this.mClock = paramClock;
  }
  
  private void launchSecondScreen(Context paramContext, View paramView)
  {
    String str;
    if (this.mResearchPageEntry.hasSecondaryPageHeader())
    {
      str = this.mResearchPageEntry.getSecondaryPageHeader().getTitle();
      if (!this.mResearchPageEntry.hasSecondaryPageHeader()) {
        break label101;
      }
    }
    label101:
    for (Sidekick.Photo localPhoto = this.mResearchPageEntry.getSecondaryPageHeader().getContextHeaderImage();; localPhoto = null)
    {
      SecondScreenUtil.launchSecondScreen(paramContext, new SecondScreenUtil.Options().setInterest(this.mLureAction.getInterest()).setTitle(str).setContextHeaderImage(localPhoto).setLure(paramView, getEntry(), paramContext).setClickPoint(this.mButtonTouchPoint));
      return;
      str = this.mResearchPageEntry.getTitle();
      break;
    }
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    final View localView1 = paramLayoutInflater.inflate(2130968609, paramViewGroup, false);
    ((TextView)localView1.findViewById(2131296451)).setText(Html.fromHtml(this.mResearchPageEntry.getTitle()));
    ArrayList localArrayList = Lists.newArrayList();
    if (this.mResearchPageEntry.hasLandingSiteDisplayName()) {
      localArrayList.add(CardTextUtil.color(this.mResearchPageEntry.getLandingSiteDisplayName(), paramContext.getResources().getColor(2131230842)));
    }
    if (this.mResearchPageEntry.hasPublishTimestampSeconds()) {
      localArrayList.add(TimeUtilities.getRelativeElapsedString(paramContext, this.mClock.currentTimeMillis() - 1000L * this.mResearchPageEntry.getPublishTimestampSeconds(), true));
    }
    CardTextUtil.setHyphenatedTextView(localView1, 2131296308, localArrayList);
    if (this.mResearchPageEntry.hasJustification()) {
      CardTextUtil.setTextView(localView1, 2131296353, this.mResearchPageEntry.getJustification());
    }
    if (this.mResearchPageEntry.hasImage())
    {
      View localView2 = this.mPhotoWithAttributionDecorator.decorate(paramContext, paramPredictiveCardContainer, this, (ViewStub)localView1.findViewById(2131296387), this.mResearchPageEntry.getImage(), 2131689866, 2131689865, 20);
      this.mPhotoWithAttributionDecorator.setRoundedCorners(localView2, 2);
    }
    for (;;)
    {
      final Button localButton = (Button)localView1.findViewById(2131296389);
      localButton.setText(this.mLureAction.getDisplayMessage());
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 160)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          BrowseModeLureInterestUpdateEntryAdapter.this.launchSecondScreen(paramAnonymousView.getContext(), localView1);
        }
      });
      localButton.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
        {
          float f = paramAnonymousMotionEvent.getY() + localView1.getHeight() - localButton.getHeight();
          BrowseModeLureInterestUpdateEntryAdapter.this.mButtonTouchPoint.x = ((int)paramAnonymousMotionEvent.getX());
          BrowseModeLureInterestUpdateEntryAdapter.this.mButtonTouchPoint.y = ((int)f);
          return false;
        }
      });
      return localView1;
      ((ViewGroup.MarginLayoutParams)localView1.findViewById(2131296386).getLayoutParams()).topMargin = paramContext.getResources().getDimensionPixelSize(2131689869);
      localView1.findViewById(2131296385).setMinimumHeight(0);
    }
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mResearchPageEntry.hasUrl()) {
      openUrl(paramContext, this.mResearchPageEntry.getUrl());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BrowseModeLureInterestUpdateEntryAdapter
 * JD-Core Version:    0.7.0.1
 */