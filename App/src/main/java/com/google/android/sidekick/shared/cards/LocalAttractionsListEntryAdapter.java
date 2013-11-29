package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.CrossfadingWebImageView;
import com.google.android.shared.util.IntentUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.android.sidekick.shared.util.LocationUtilities.DistanceUnit;
import com.google.android.sidekick.shared.util.ViewPlacePageAction;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Attraction;
import com.google.geo.sidekick.Sidekick.AttractionListEntry;
import com.google.geo.sidekick.Sidekick.BusinessData;
import com.google.geo.sidekick.Sidekick.CommuteSummary;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.ArrayList;
import java.util.List;

public class LocalAttractionsListEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.AttractionListEntry mAttractionList;
  private final IntentUtils mIntentUtils;
  
  LocalAttractionsListEntryAdapter(Sidekick.Entry paramEntry, IntentUtils paramIntentUtils, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mAttractionList = paramEntry.getAttractionListEntry();
    this.mIntentUtils = paramIntentUtils;
  }
  
  private void addAttractions(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, View paramView, int paramInt1, int paramInt2)
  {
    LinearLayout localLinearLayout = (LinearLayout)paramView.findViewById(2131296766);
    for (int i = paramInt1; (i < paramInt2) && (i < this.mAttractionList.getAttractionCount()); i++)
    {
      Sidekick.Attraction localAttraction = this.mAttractionList.getAttraction(i);
      View localView = paramLayoutInflater.inflate(2130968737, localLinearLayout, false);
      final Sidekick.BusinessData localBusinessData = localAttraction.getBusinessData();
      ((TextView)localView.findViewById(2131296765)).setText(localBusinessData.getName());
      ArrayList localArrayList = Lists.newArrayListWithCapacity(2);
      if ((localAttraction.hasRoute()) && (localAttraction.getRoute().hasDistanceInMeters())) {
        localArrayList.add(formatDistance(paramContext, LocationUtilities.toLocalDistanceUnits(localAttraction.getRoute().getDistanceInMeters())));
      }
      if (!TextUtils.isEmpty(localBusinessData.getOpenUntil())) {
        localArrayList.add(CardTextUtil.color(localBusinessData.getOpenUntil(), paramContext.getResources().getColor(2131230855)));
      }
      CardTextUtil.setHyphenatedTextView(localView, 2131296395, localArrayList);
      if (localBusinessData.getKnownForTermCount() > 0) {
        CardTextUtil.setTextView(localView, 2131296352, Html.fromHtml(CardTextUtil.hyphenate(localBusinessData.getKnownForTermList()).toString()));
      }
      if (localBusinessData.hasCoverPhoto()) {
        ((CrossfadingWebImageView)localView.findViewById(2131296383)).setImageUrl(localBusinessData.getCoverPhoto().getUrl(), paramPredictiveCardContainer.getImageLoader());
      }
      if (localBusinessData.hasCid()) {
        localView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 40)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            new ViewPlacePageAction(paramContext, localBusinessData.getCid(), LocalAttractionsListEntryAdapter.this.mIntentUtils, LocalAttractionsListEntryAdapter.this.getActivityHelper()).run();
          }
        });
      }
      localLinearLayout.addView(localView);
    }
  }
  
  private static String formatDistance(Context paramContext, double paramDouble)
  {
    if (LocationUtilities.getLocalDistanceUnits() == LocationUtilities.DistanceUnit.KILOMETERS) {}
    for (int i = 2131362654;; i = 2131362653)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Double.valueOf(paramDouble);
      return paramContext.getString(i, arrayOfObject);
    }
  }
  
  private void hideBottomDivider(View paramView)
  {
    ((LinearLayout)paramView.findViewById(2131296766)).setShowDividers(3);
  }
  
  public View getView(final Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, final LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    final View localView = paramLayoutInflater.inflate(2130968738, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(paramContext.getString(2131362614));
    addAttractions(paramContext, paramPredictiveCardContainer, paramLayoutInflater, localView, 0, 3);
    if (this.mAttractionList.getAttractionCount() > 3)
    {
      final Button localButton = (Button)localView.findViewById(2131296316);
      localButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 39)
      {
        public void onEntryClick(View paramAnonymousView)
        {
          localButton.setVisibility(8);
          LocalAttractionsListEntryAdapter.this.addAttractions(paramContext, paramPredictiveCardContainer, paramLayoutInflater, localView, 3, LocalAttractionsListEntryAdapter.this.mAttractionList.getAttractionCount());
          LocalAttractionsListEntryAdapter.this.hideBottomDivider(localView);
        }
      });
      localButton.setVisibility(0);
      return localView;
    }
    hideBottomDivider(localView);
    return localView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.LocalAttractionsListEntryAdapter
 * JD-Core Version:    0.7.0.1
 */