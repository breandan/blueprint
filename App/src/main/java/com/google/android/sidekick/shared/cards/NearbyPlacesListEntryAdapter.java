package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.shared.util.IntentUtils;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.ListCardView;
import com.google.android.sidekick.shared.ui.SimpleGroupNodeListAdapter;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.PlaceDataHelper;
import com.google.android.sidekick.shared.util.ViewPlacePageAction;
import com.google.geo.sidekick.Sidekick.BusinessData;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.NearbyPlacesListEntry;
import com.google.geo.sidekick.Sidekick.PlaceData;
import javax.annotation.Nullable;

public class NearbyPlacesListEntryAdapter
  extends GroupNodeEntryAdapter
{
  private final IntentUtils mIntentUtils;
  private final PlaceDataHelper mPlaceDataHelper;
  
  public NearbyPlacesListEntryAdapter(Sidekick.EntryTreeNode paramEntryTreeNode, IntentUtils paramIntentUtils, PlaceDataHelper paramPlaceDataHelper, ActivityHelper paramActivityHelper)
  {
    super(paramEntryTreeNode, paramActivityHelper);
    this.mIntentUtils = paramIntentUtils;
    this.mPlaceDataHelper = paramPlaceDataHelper;
  }
  
  @Nullable
  private Sidekick.FrequentPlace getFrequentPlace(Sidekick.Entry paramEntry)
  {
    if (!paramEntry.hasNearbyPlaceEntry())
    {
      Log.w("NearbyPlacesListEntryAdapter", "Unexpected Entry without nearby place entry.");
      return null;
    }
    Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = paramEntry.getNearbyPlaceEntry();
    if (!localFrequentPlaceEntry.hasFrequentPlace())
    {
      Log.w("NearbyPlacesListEntryAdapter", "Unexpected nearby place entry without frequent place.");
      return null;
    }
    return localFrequentPlaceEntry.getFrequentPlace();
  }
  
  @Nullable
  private Sidekick.PlaceData getPlaceData(Sidekick.Entry paramEntry)
  {
    Sidekick.FrequentPlace localFrequentPlace = getFrequentPlace(paramEntry);
    Sidekick.PlaceData localPlaceData;
    if (localFrequentPlace == null) {
      localPlaceData = null;
    }
    do
    {
      return localPlaceData;
      if (!localFrequentPlace.hasPlaceData())
      {
        Log.w("NearbyPlacesListEntryAdapter", "Unexpected frequent place without place data.");
        return null;
      }
      localPlaceData = localFrequentPlace.getPlaceData();
    } while (localPlaceData.hasBusinessData());
    Log.w("NearbyPlacesListEntryAdapter", "Unexpected place data without business data.");
    return null;
  }
  
  @Nullable
  private Sidekick.ClickAction getSearchForMoreAction(Sidekick.Entry paramEntry)
  {
    if (paramEntry.hasNearbyPlacesListEntry())
    {
      Sidekick.NearbyPlacesListEntry localNearbyPlacesListEntry = paramEntry.getNearbyPlacesListEntry();
      if (localNearbyPlacesListEntry.hasSearchForMore()) {
        return localNearbyPlacesListEntry.getSearchForMore();
      }
    }
    return null;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ListCardView localListCardView = createListCardView(paramContext, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup, new PlaceListAdapter(paramContext, paramPredictiveCardContainer, getGroupEntryTreeNode()), 1);
    localListCardView.setTitle(getEntry().getNearbyPlacesListEntry().getTitle());
    return localListCardView;
  }
  
  protected void onListExpanded(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, ListCardView paramListCardView)
  {
    Sidekick.ClickAction localClickAction = getSearchForMoreAction(getEntry());
    if ((localClickAction != null) && (localClickAction.hasLabel()) && (localClickAction.hasUri()))
    {
      final Uri localUri = Uri.parse(localClickAction.getUri());
      if (localUri != null) {
        paramListCardView.showActionButton(localClickAction.getLabel(), 2130837689, new EntryClickListener(paramPredictiveCardContainer, getEntry(), 139)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            NearbyPlacesListEntryAdapter.this.openUrl(paramContext, localUri);
          }
        });
      }
    }
  }
  
  private class PlaceListAdapter
    extends SimpleGroupNodeListAdapter
  {
    static
    {
      if (!NearbyPlacesListEntryAdapter.class.desiredAssertionStatus()) {}
      for (boolean bool = true;; bool = false)
      {
        $assertionsDisabled = bool;
        return;
      }
    }
    
    public PlaceListAdapter(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, Sidekick.EntryTreeNode paramEntryTreeNode)
    {
      super(paramPredictiveCardContainer, paramEntryTreeNode, 2130968755);
    }
    
    public void populateRow(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.Entry paramEntry)
    {
      Sidekick.PlaceData localPlaceData = NearbyPlacesListEntryAdapter.this.getPlaceData(paramEntry);
      final Sidekick.BusinessData localBusinessData = localPlaceData.getBusinessData();
      if (localPlaceData.hasDisplayName()) {
        ((TextView)paramView.findViewById(2131296299)).setText(localPlaceData.getDisplayName());
      }
      if (localBusinessData.hasCid()) {
        paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntry, 126)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            new ViewPlacePageAction(paramContext, localBusinessData.getCid(), NearbyPlacesListEntryAdapter.this.mIntentUtils, NearbyPlacesListEntryAdapter.this.getActivityHelper()).run();
          }
        });
      }
      Sidekick.FrequentPlace localFrequentPlace = NearbyPlacesListEntryAdapter.this.getFrequentPlace(paramEntry);
      assert (localFrequentPlace != null);
      NearbyPlacesListEntryAdapter.this.mPlaceDataHelper.populateBusinessDataWithJustification(paramContext, paramPredictiveCardContainer, paramView, localFrequentPlace);
      View localView = paramView.findViewById(2131296858);
      NearbyPlacesListEntryAdapter.this.mPlaceDataHelper.populatePlaceReview(paramContext, localView, localBusinessData);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.NearbyPlacesListEntryAdapter
 * JD-Core Version:    0.7.0.1
 */