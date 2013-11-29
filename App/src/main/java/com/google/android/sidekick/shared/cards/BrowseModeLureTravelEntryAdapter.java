package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.ImageLureCardUtil;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.SecondScreenUtil;
import com.google.android.sidekick.shared.util.SecondScreenUtil.Options;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ImageLureCardEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.SecondaryPageHeaderDescriptor;

public class BrowseModeLureTravelEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.ImageLureCardEntry mLureCardEntry;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  public BrowseModeLureTravelEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mLureCardEntry = paramEntry.getBrowseModeLureTravelEntry();
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    return ImageLureCardUtil.createView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, this.mLureCardEntry, this.mPhotoWithAttributionDecorator, 3);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    String str;
    if (this.mLureCardEntry.hasSecondaryPageHeader())
    {
      str = this.mLureCardEntry.getSecondaryPageHeader().getTitle();
      if (!this.mLureCardEntry.hasSecondaryPageHeader()) {
        break label118;
      }
    }
    label118:
    for (Sidekick.Photo localPhoto = this.mLureCardEntry.getSecondaryPageHeader().getContextHeaderImage();; localPhoto = null)
    {
      Sidekick.Action localAction = ProtoUtils.findAction(getEntry(), 161, new int[0]);
      SecondScreenUtil.launchSecondScreen(paramContext, new SecondScreenUtil.Options().setInterest(localAction.getInterest()).setTitle(str).setContextHeaderImage(localPhoto).setLure(paramView, getEntry(), paramContext).setClickPoint(getLastTouchPoint(paramView)));
      return;
      str = this.mLureCardEntry.getTitle();
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BrowseModeLureTravelEntryAdapter
 * JD-Core Version:    0.7.0.1
 */