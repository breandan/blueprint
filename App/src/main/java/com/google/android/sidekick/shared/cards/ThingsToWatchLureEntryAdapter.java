package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
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

public class ThingsToWatchLureEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.ImageLureCardEntry mLureCardEntry;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  public ThingsToWatchLureEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mLureCardEntry = paramEntry.getThingsToWatchLureEntry();
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
  }
  
  private void launchSecondScreen(Context paramContext)
  {
    Sidekick.Action localAction = ProtoUtils.findAction(getEntry(), 162, new int[0]);
    Intent localIntent = SecondScreenUtil.createIntent(paramContext, new SecondScreenUtil.Options().setInterest(localAction.getInterest()).setTitle(this.mLureCardEntry.getTitle()).setFlags(4));
    getActivityHelper().safeStartActivity(paramContext, localIntent);
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView1 = ImageLureCardUtil.createView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, this.mLureCardEntry, this.mPhotoWithAttributionDecorator, 4);
    if (this.mLureCardEntry.getPhotoCount() > 0) {}
    for (int i = 2131296703;; i = 2131296702)
    {
      View localView2 = localView1.findViewById(i);
      localView2.setVisibility(8);
      localView2.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          Toast.makeText(paramContext, "not implemented", 0).show();
        }
      });
      return localView1;
    }
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    launchSecondScreen(paramContext);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.ThingsToWatchLureEntryAdapter
 * JD-Core Version:    0.7.0.1
 */