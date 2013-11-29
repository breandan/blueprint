package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.google.android.search.shared.ui.CrossfadingWebImageView;
import com.google.android.search.shared.ui.WebImageView.Listener;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GeoLocatedPhoto;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.List;
import javax.annotation.Nullable;

public class PhotoSpotThumbnailGrid
  extends TableLayout
{
  private static final String TAG = Tag.getTag(PhotoSpotThumbnailGrid.class);
  private List<WebImageView.Listener> mImageListeners;
  private List<Integer> mInvalidIndices;
  private List<Sidekick.GeoLocatedPhoto> mPhotoList = ImmutableList.of();
  @Nullable
  private Sidekick.Location mSourceLocation;
  
  public PhotoSpotThumbnailGrid(Context paramContext)
  {
    super(paramContext);
  }
  
  public PhotoSpotThumbnailGrid(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private int getPhotoCount()
  {
    return Math.min(this.mPhotoList.size(), 12);
  }
  
  public void displayThumbnails(List<Sidekick.GeoLocatedPhoto> paramList, @Nullable Sidekick.Location paramLocation, LayoutInflater paramLayoutInflater, EntryCardViewAdapter paramEntryCardViewAdapter, final PredictiveCardContainer paramPredictiveCardContainer)
  {
    this.mPhotoList = Lists.newArrayList(paramList);
    this.mSourceLocation = paramLocation;
    this.mImageListeners = Lists.newArrayList();
    this.mInvalidIndices = Lists.newArrayList();
    TableRow localTableRow = null;
    int i = 0;
    if (i < getPhotoCount())
    {
      Sidekick.GeoLocatedPhoto localGeoLocatedPhoto = (Sidekick.GeoLocatedPhoto)this.mPhotoList.get(i);
      if (i % 4 == 0)
      {
        Context localContext = getContext();
        localTableRow = new TableRow(localContext);
        addView(localTableRow);
      }
      CrossfadingWebImageView localCrossfadingWebImageView2 = (CrossfadingWebImageView)paramLayoutInflater.inflate(2130968776, localTableRow, false);
      localCrossfadingWebImageView2.setImageUri(Uri.parse(localGeoLocatedPhoto.getThumbnail().getUrl()), paramPredictiveCardContainer.getImageLoader());
      if (localCrossfadingWebImageView2.isLoadedFromCache()) {
        if (localCrossfadingWebImageView2.getDrawable() == null) {
          this.mInvalidIndices.add(Integer.valueOf(i));
        }
      }
      for (;;)
      {
        final int j = i;
        localCrossfadingWebImageView2.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, paramEntryCardViewAdapter.getEntry(), 79)
        {
          public void onEntryClick(View paramAnonymousView)
          {
            Intent localIntent = paramPredictiveCardContainer.preparePhotoGalleryIntent(PhotoSpotThumbnailGrid.this.mPhotoList, j);
            if (localIntent != null)
            {
              PhotoSpotThumbnailGrid.this.getContext().startActivity(localIntent);
              return;
            }
            Log.e(PhotoSpotThumbnailGrid.TAG, "Got back a null intent to launch gallery");
          }
        });
        localTableRow.addView(localCrossfadingWebImageView2);
        i++;
        break;
        ImageLoadingListener localImageLoadingListener = new ImageLoadingListener(paramEntryCardViewAdapter, paramPredictiveCardContainer, i, null);
        localCrossfadingWebImageView2.setOnDownloadListener(localImageLoadingListener);
        this.mImageListeners.add(localImageLoadingListener);
      }
    }
    while (i % 4 != 0)
    {
      CrossfadingWebImageView localCrossfadingWebImageView1 = (CrossfadingWebImageView)paramLayoutInflater.inflate(2130968776, localTableRow, false);
      localTableRow.addView(localCrossfadingWebImageView1);
      i++;
    }
    maybeClearInvalidImages(paramEntryCardViewAdapter, paramPredictiveCardContainer);
  }
  
  List<Sidekick.GeoLocatedPhoto> getPhotoList()
  {
    return this.mPhotoList;
  }
  
  void maybeClearInvalidImages(EntryCardViewAdapter paramEntryCardViewAdapter, PredictiveCardContainer paramPredictiveCardContainer)
  {
    if ((!this.mImageListeners.isEmpty()) || (this.mInvalidIndices.isEmpty())) {
      return;
    }
    for (int i = -1 + this.mPhotoList.size(); i >= 0; i--) {
      if (this.mInvalidIndices.contains(Integer.valueOf(i))) {
        this.mPhotoList.remove(i);
      }
    }
    removeAllViews();
    displayThumbnails(this.mPhotoList, this.mSourceLocation, (LayoutInflater)getContext().getSystemService("layout_inflater"), paramEntryCardViewAdapter, paramPredictiveCardContainer);
  }
  
  void setImageListeners(List<WebImageView.Listener> paramList)
  {
    this.mImageListeners = paramList;
  }
  
  void setInvalidIndices(List<Integer> paramList)
  {
    this.mInvalidIndices = paramList;
  }
  
  private class ImageLoadingListener
    implements WebImageView.Listener
  {
    private final PredictiveCardContainer mCardContainer;
    private final EntryCardViewAdapter mEntryCardViewAdapter;
    private final int mIndex;
    
    private ImageLoadingListener(EntryCardViewAdapter paramEntryCardViewAdapter, PredictiveCardContainer paramPredictiveCardContainer, int paramInt)
    {
      this.mEntryCardViewAdapter = paramEntryCardViewAdapter;
      this.mCardContainer = paramPredictiveCardContainer;
      this.mIndex = paramInt;
    }
    
    public void onImageDownloaded(Drawable paramDrawable)
    {
      if (PhotoSpotThumbnailGrid.this.mImageListeners.contains(this))
      {
        PhotoSpotThumbnailGrid.this.mImageListeners.remove(this);
        if (paramDrawable == null) {
          PhotoSpotThumbnailGrid.this.mInvalidIndices.add(Integer.valueOf(this.mIndex));
        }
        PhotoSpotThumbnailGrid.this.maybeClearInvalidImages(this.mEntryCardViewAdapter, this.mCardContainer);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.PhotoSpotThumbnailGrid
 * JD-Core Version:    0.7.0.1
 */