package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.ui.PhotoSpotThumbnailGrid;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.LocationUtilities;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.PhotoSpotEntry;

public class PhotoSpotEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.PhotoSpotEntry mPhotoEntry;
  
  public PhotoSpotEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoEntry = paramEntry.getPhotoSpotEntry();
  }
  
  private int getWalkingTimeMinutes()
  {
    return 1 + this.mPhotoEntry.getWalkingTimeSeconds() / 60;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968778, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296451)).setText(paramContext.getString(2131362640));
    PhotoSpotThumbnailGrid localPhotoSpotThumbnailGrid = (PhotoSpotThumbnailGrid)localView.findViewById(2131296856);
    Sidekick.Location localLocation = LocationUtilities.androidLocationToSidekickLocation(paramPredictiveCardContainer.getCardRenderingContext().getCurrentLocation());
    localPhotoSpotThumbnailGrid.displayThumbnails(this.mPhotoEntry.getPhotoList(), localLocation, paramLayoutInflater, this, paramPredictiveCardContainer);
    if (this.mPhotoEntry.hasWalkingTimeSeconds())
    {
      int i = getWalkingTimeMinutes();
      if (i < 60)
      {
        TextView localTextView = (TextView)localView.findViewById(2131296857);
        localTextView.setVisibility(0);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(i);
        localTextView.setText(paramContext.getString(2131362642, arrayOfObject));
      }
    }
    return localView;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.PhotoSpotEntryAdapter
 * JD-Core Version:    0.7.0.1
 */