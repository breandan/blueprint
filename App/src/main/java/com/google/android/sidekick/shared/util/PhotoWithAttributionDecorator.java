package com.google.android.sidekick.shared.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import com.google.android.search.shared.ui.RoundedCornerWebImageView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.cards.BaseEntryAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.ui.SimpleEntryClickListener;
import com.google.geo.sidekick.Sidekick.Attribution;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Photo;
import javax.annotation.Nullable;

public class PhotoWithAttributionDecorator
{
  private final FifeImageUrlUtil mFifeImageUrlUtil;
  
  public PhotoWithAttributionDecorator(FifeImageUrlUtil paramFifeImageUrlUtil)
  {
    this.mFifeImageUrlUtil = paramFifeImageUrlUtil;
  }
  
  public View decorate(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, BaseEntryAdapter paramBaseEntryAdapter, ViewStub paramViewStub, Sidekick.Photo paramPhoto, int paramInt1, int paramInt2, int paramInt3)
  {
    View localView = paramViewStub.inflate();
    decorate(paramContext, paramPredictiveCardContainer, paramBaseEntryAdapter, localView, paramPhoto, paramInt1, paramInt2, paramInt3, true);
    return localView;
  }
  
  public void decorate(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, @Nullable final BaseEntryAdapter paramBaseEntryAdapter, View paramView, final Sidekick.Photo paramPhoto, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    if ((paramPhoto == null) || (!paramPhoto.hasUrl())) {
      return;
    }
    WebImageView localWebImageView = (WebImageView)paramView.findViewById(2131296383);
    localWebImageView.setImageUri(getPhotoUri(paramContext, paramPhoto, paramInt1, paramInt2), paramPredictiveCardContainer.getImageLoader());
    Sidekick.Attribution localAttribution;
    if (paramBoolean)
    {
      if (paramPhoto.hasInfoUrl()) {
        localWebImageView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramBaseEntryAdapter.getEntry(), paramInt3)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            paramBaseEntryAdapter.openUrl(paramContext, paramPhoto.getInfoUrl());
          }
        });
      }
    }
    else if (paramPhoto.hasPhotoAttribution())
    {
      localAttribution = paramPhoto.getPhotoAttribution();
      if (!localAttribution.hasTitle()) {
        break label221;
      }
    }
    label221:
    for (String str = localAttribution.getTitle();; str = localAttribution.getUrl())
    {
      TextView localTextView = (TextView)paramView.findViewById(2131296704);
      localTextView.setText(str);
      localTextView.setVisibility(0);
      if ((paramBoolean) && (localAttribution.hasUrl())) {
        localTextView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramBaseEntryAdapter.getEntry(), 121)
        {
          protected void onEntryClick(View paramAnonymousView)
          {
            paramBaseEntryAdapter.openUrl(paramContext, paramPhoto.getPhotoAttribution().getUrl());
          }
        });
      }
      paramView.setVisibility(0);
      return;
      if (!paramPhoto.hasClickAction()) {
        break;
      }
      localWebImageView.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, paramBaseEntryAdapter, paramBaseEntryAdapter.getEntry(), paramInt3, paramPhoto.getClickAction()));
      break;
    }
  }
  
  public void decorate(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, BaseEntryAdapter paramBaseEntryAdapter, ViewStub paramViewStub, Sidekick.Photo paramPhoto)
  {
    decorate(paramContext, paramPredictiveCardContainer, paramBaseEntryAdapter, paramViewStub, paramPhoto, 0, 0, 20);
  }
  
  public void decorate(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, BaseEntryAdapter paramBaseEntryAdapter, ViewStub paramViewStub, Sidekick.Photo paramPhoto, int paramInt1, int paramInt2)
  {
    decorate(paramContext, paramPredictiveCardContainer, paramBaseEntryAdapter, paramViewStub, paramPhoto, paramInt1, paramInt2, 20);
  }
  
  public void decorateWithoutClickHandlers(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, Sidekick.Photo paramPhoto, int paramInt1, int paramInt2)
  {
    decorate(paramContext, paramPredictiveCardContainer, null, paramView, paramPhoto, paramInt1, paramInt2, 0, false);
  }
  
  public Uri getPhotoUri(Context paramContext, Sidekick.Photo paramPhoto, int paramInt1, int paramInt2)
  {
    String str = ProtoUtils.getPhotoUrl(paramPhoto);
    if ((paramInt1 != 0) && (paramInt2 != 0) && (paramPhoto.getUrlType() == 2))
    {
      Resources localResources = paramContext.getResources();
      return this.mFifeImageUrlUtil.setImageUrlSmartCrop(localResources.getDimensionPixelSize(paramInt1), localResources.getDimensionPixelSize(paramInt2), str);
    }
    return Uri.parse(str);
  }
  
  public void setRoundedCorners(View paramView, int paramInt)
  {
    ((RoundedCornerWebImageView)paramView.findViewById(2131296383)).setRoundedCorners(paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator
 * JD-Core Version:    0.7.0.1
 */