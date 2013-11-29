package com.google.android.sidekick.main.inject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.google.android.search.shared.api.Query;
import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.android.sidekick.shared.util.MapsLauncher;
import com.google.android.velvet.gallery.ImageMetadataController;
import com.google.android.velvet.gallery.NavigatingPhotoViewActivity;
import com.google.android.velvet.gallery.VelvetImage;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Attribution;
import com.google.geo.sidekick.Sidekick.GeoLocatedPhoto;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.ArrayList;
import java.util.List;

public class VelvetImageGalleryHelper
{
  private static final String[] PHOTO_PROJECTION = { "uri", "_display_name", "contentUri", "thumbnailUri", "contentType", "loadingIndicator", "domain", "width", "height", "source", "id", "nav_uri" };
  private final Context mAppContext;
  private final ImageMetadataController mImageMetadataController;
  
  VelvetImageGalleryHelper(Context paramContext, ImageMetadataController paramImageMetadataController)
  {
    this.mAppContext = paramContext;
    this.mImageMetadataController = paramImageMetadataController;
  }
  
  public Intent createImageGalleryIntent(int paramInt)
  {
    Intent localIntent = new Intent();
    String str = NavigatingPhotoViewActivity.class.getName();
    localIntent.setComponent(new ComponentName(this.mAppContext.getPackageName(), str));
    localIntent.setAction("android.intent.action.VIEW");
    localIntent.setFlags(524288);
    localIntent.putExtra("photos_uri", "content://com.google.android.velvet.gallery.ImageProvider/images");
    localIntent.putExtra("projection", PHOTO_PROJECTION);
    localIntent.putExtra("photo_index", paramInt);
    localIntent.putExtra("max_scale", 4.0F);
    return localIntent;
  }
  
  VelvetImage createVelvetImage(Sidekick.GeoLocatedPhoto paramGeoLocatedPhoto, int paramInt)
  {
    VelvetImage localVelvetImage = new VelvetImage();
    localVelvetImage.setId(Integer.toString(paramInt));
    localVelvetImage.setName(paramGeoLocatedPhoto.getName());
    localVelvetImage.setThumbnailUri(paramGeoLocatedPhoto.getThumbnail().getUrl());
    Sidekick.Photo localPhoto = paramGeoLocatedPhoto.getMediumSizedPhoto();
    localVelvetImage.setUri(localPhoto.getUrl());
    localVelvetImage.setWidth(localPhoto.getWidth());
    localVelvetImage.setHeight(localPhoto.getHeight());
    if ((localPhoto.hasPhotoAttribution()) && (localPhoto.getPhotoAttribution().hasTitle())) {
      localVelvetImage.setDomain(localPhoto.getPhotoAttribution().getTitle());
    }
    if (localPhoto.hasInfoUrl()) {
      localVelvetImage.setSourceUri(localPhoto.getInfoUrl());
    }
    if (paramGeoLocatedPhoto.hasLocation()) {
      localVelvetImage.setNavigationUri(MapsLauncher.buildWalkingDirectionsUri(paramGeoLocatedPhoto.getLocation()));
    }
    return localVelvetImage;
  }
  
  public void setupImagesForGallery(List<Sidekick.GeoLocatedPhoto> paramList)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(paramList.size());
    for (int i = 0; i < paramList.size(); i++) {
      localArrayList.add(createVelvetImage((Sidekick.GeoLocatedPhoto)paramList.get(i), i));
    }
    this.mImageMetadataController.setQueryWithImages(Query.EMPTY, null, localArrayList);
  }
  
  public void setupImagesForGalleryFromProtos(List<ProtoParcelable> paramList)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(paramList.size());
    for (int i = 0; i < paramList.size(); i++) {
      localArrayList.add(createVelvetImage((Sidekick.GeoLocatedPhoto)((ProtoParcelable)paramList.get(i)).getProto(Sidekick.GeoLocatedPhoto.class), i));
    }
    this.mImageMetadataController.setQueryWithImages(Query.EMPTY, null, localArrayList);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.VelvetImageGalleryHelper
 * JD-Core Version:    0.7.0.1
 */