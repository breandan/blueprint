package com.google.android.search.core.ears;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.audio.ears.proto.EarsService.EarsResult;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.audio.ears.proto.EarsService.MusicResult;
import com.google.audio.ears.proto.EarsService.ProductOffer;
import java.util.Calendar;

public class EarsContentProviderHelper
{
  private static final Uri CONTENT_URI = new Uri.Builder().path("heard").authority("com.google.android.ears.heard.EarsContentProvider").scheme("content").build();
  private final ContentResolver mContentResolver;
  private final PackageManager mPackageManager;
  
  public EarsContentProviderHelper(ContentResolver paramContentResolver, PackageManager paramPackageManager)
  {
    this.mContentResolver = paramContentResolver;
    this.mPackageManager = paramPackageManager;
  }
  
  private ContentValues createContentValues(long paramLong, EarsService.EarsResult paramEarsResult, boolean paramBoolean1, boolean paramBoolean2, String paramString)
  {
    ContentValues localContentValues = new ContentValues();
    EarsService.MusicResult localMusicResult = paramEarsResult.getMusicResult();
    localContentValues.put("_id", Long.valueOf(paramLong));
    localContentValues.put("resultType", Integer.valueOf(EarsResultParser.getResultTypeInt(paramEarsResult)));
    localContentValues.put("deleted", Boolean.valueOf(paramBoolean1));
    localContentValues.put("synced", Boolean.valueOf(paramBoolean2));
    localContentValues.put("refId", Long.valueOf(paramEarsResult.getReferenceId()));
    localContentValues.put("album", localMusicResult.getAlbum());
    localContentValues.put("albumArtUrl", localMusicResult.getAlbumArtUrl());
    localContentValues.put("signedInAlbumArtUrl", localMusicResult.getSignedInAlbumArtUrl());
    localContentValues.put("artist", localMusicResult.getArtist());
    if (localMusicResult.hasArtistId()) {
      localContentValues.put("artistId", localMusicResult.getArtistId());
    }
    localContentValues.put("track", localMusicResult.getTrack());
    EarsService.ProductOffer localProductOffer = EarsResultParser.getGoogleMusicProductOffer(localMusicResult);
    if (localProductOffer != null)
    {
      localContentValues.put("productId", localProductOffer.getIdentifier());
      localContentValues.put("productParentId", localProductOffer.getParentIdentifier());
    }
    if (shouldIncludeCountry()) {
      localContentValues.put("countryCode", paramString);
    }
    return localContentValues;
  }
  
  public long insertHeardMatch(EarsService.EarsResultsResponse paramEarsResultsResponse)
  {
    return insertHeardMatch(paramEarsResultsResponse, 1000L * Calendar.getInstance().getTimeInMillis(), false, false);
  }
  
  public long insertHeardMatch(EarsService.EarsResultsResponse paramEarsResultsResponse, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    EarsService.EarsResult localEarsResult = EarsResultParser.getFirstEarsResultWithMusic(paramEarsResultsResponse.getResultList());
    if (!localEarsResult.hasMusicResult()) {
      paramLong = 0L;
    }
    do
    {
      return paramLong;
      String str = paramEarsResultsResponse.getDetectedCountryCode();
      ContentValues localContentValues = createContentValues(paramLong, localEarsResult, paramBoolean1, paramBoolean2, str);
      try
      {
        Uri localUri2 = this.mContentResolver.insert(CONTENT_URI, localContentValues);
        localUri1 = localUri2;
      }
      catch (SecurityException localSecurityException)
      {
        for (;;)
        {
          localUri1 = null;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        for (;;)
        {
          Uri localUri1 = null;
        }
      }
    } while (localUri1 != null);
    return 0L;
  }
  
  boolean shouldIncludeCountry()
  {
    try
    {
      PackageInfo localPackageInfo = this.mPackageManager.getPackageInfo("com.google.android.ears", 0);
      int i = localPackageInfo.versionCode;
      boolean bool = false;
      if (i >= 10) {
        bool = true;
      }
      return bool;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ears.EarsContentProviderHelper
 * JD-Core Version:    0.7.0.1
 */