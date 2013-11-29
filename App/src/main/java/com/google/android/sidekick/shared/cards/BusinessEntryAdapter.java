package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.shared.util.IntentUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.PlaceDataHelper;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.ViewPlacePageAction;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.BusinessData;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.PlaceData;
import javax.annotation.Nonnull;

public class BusinessEntryAdapter
  extends BaseEntryAdapter
{
  @Nonnull
  private final Sidekick.BusinessData mBusinessData;
  private final IntentUtils mIntentUtils;
  private final Sidekick.FrequentPlace mPlace;
  private final PlaceDataHelper mPlaceDataHelper;
  
  BusinessEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, ActivityHelper paramActivityHelper, IntentUtils paramIntentUtils, PlaceDataHelper paramPlaceDataHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mPlace = paramFrequentPlaceEntry.getFrequentPlace();
    this.mBusinessData = ((Sidekick.BusinessData)Preconditions.checkNotNull(this.mPlace.getPlaceData().getBusinessData()));
    this.mIntentUtils = paramIntentUtils;
    this.mPlaceDataHelper = paramPlaceDataHelper;
  }
  
  private boolean isGmailReservation()
  {
    boolean bool1 = getEntry().hasFrequentPlaceEntry();
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = getEntry().getFrequentPlaceEntry().hasFrequentPlace();
      bool2 = false;
      if (bool3)
      {
        int i = getEntry().getFrequentPlaceEntry().getFrequentPlace().getSourceType();
        bool2 = false;
        if (i == 6) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  public String getLoggingName()
  {
    if (isGmailReservation()) {
      return "GmailRestaurantReservation";
    }
    return "NearbyPlace";
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView1 = paramLayoutInflater.inflate(2130968602, paramViewGroup, false);
    ((TextView)localView1.findViewById(2131296451)).setText(PlaceUtils.getPlaceName(paramContext, this.mPlace));
    this.mPlaceDataHelper.populateBusinessDataWithJustification(paramContext, paramPredictiveCardContainer, localView1, this.mPlace);
    View localView2 = localView1.findViewById(2131296858);
    this.mPlaceDataHelper.populatePlaceReview(paramContext, localView2, this.mBusinessData);
    return localView1;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mBusinessData.hasCid()) {
      new ViewPlacePageAction(paramContext, this.mBusinessData.getCid(), this.mIntentUtils, getActivityHelper()).run();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.BusinessEntryAdapter
 * JD-Core Version:    0.7.0.1
 */