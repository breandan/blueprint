package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.ClickActionHelper;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.geo.sidekick.Sidekick.TvMusicEntry;
import java.util.Iterator;
import java.util.List;

public class TvMusicEntryAdapter
  extends BaseEntryAdapter
{
  private final Sidekick.TvMusicEntry mMusic;
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  
  public TvMusicEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mMusic = paramEntry.getTvMusicEntry();
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ViewGroup localViewGroup = (ViewGroup)paramLayoutInflater.inflate(2130968900, paramViewGroup, false);
    if ((this.mMusic.hasPhoto()) && (this.mMusic.getPhoto().hasUrl()))
    {
      WebImageView localWebImageView = (WebImageView)localViewGroup.findViewById(2131297179);
      localWebImageView.setImageUri(this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, this.mMusic.getPhoto(), 2131689771, 2131689771), paramPredictiveCardContainer.getImageLoader());
      localWebImageView.setVisibility(0);
    }
    if (this.mMusic.hasSongName()) {
      CardTextUtil.setTextView(localViewGroup, 2131297180, this.mMusic.getSongName());
    }
    if (this.mMusic.hasArtistName()) {
      CardTextUtil.setTextView(localViewGroup, 2131297181, this.mMusic.getArtistName());
    }
    if (this.mMusic.hasAlbumName()) {
      CardTextUtil.setTextView(localViewGroup, 2131297182, this.mMusic.getAlbumName());
    }
    if (this.mMusic.hasLastMentionedTimeDescription()) {
      CardTextUtil.setTextView(localViewGroup, 2131297178, this.mMusic.getLastMentionedTimeDescription());
    }
    Iterator localIterator = this.mMusic.getClickActionList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.ClickAction localClickAction = (Sidekick.ClickAction)localIterator.next();
      if (localClickAction.hasLabel()) {
        ClickActionHelper.addClickActionButton(paramContext, paramPredictiveCardContainer, paramLayoutInflater, this, localViewGroup, localClickAction);
      }
    }
    return localViewGroup;
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mMusic.getClickActionCount() > 0) {
      handleClickAction(paramContext, paramPredictiveCardContainer, this.mMusic.getClickAction(0));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.TvMusicEntryAdapter
 * JD-Core Version:    0.7.0.1
 */