package com.google.android.search.core.ears;

import android.text.TextUtils;
import com.google.audio.ears.proto.EarsService.EarsResult;
import com.google.audio.ears.proto.EarsService.MusicResult;
import com.google.audio.ears.proto.EarsService.ProductOffer;
import com.google.audio.ears.proto.EarsService.ProductOffer.PricingInfo;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction.MusicItem;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction.PriceTag;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class EarsResultParser
{
  public static ActionV2Protos.PlayMediaAction convertEarsToPlayMediaAction(EarsService.EarsResult paramEarsResult, boolean paramBoolean, String paramString)
  {
    EarsService.MusicResult localMusicResult = paramEarsResult.getMusicResult();
    ActionV2Protos.PlayMediaAction.MusicItem localMusicItem = new ActionV2Protos.PlayMediaAction.MusicItem().setAlbum(localMusicResult.getAlbum()).setArtist(localMusicResult.getArtist()).setSong(localMusicResult.getTrack()).setIsExplicit(localMusicResult.getIsExplicit());
    ActionV2Protos.PlayMediaAction localPlayMediaAction = new ActionV2Protos.PlayMediaAction().setMediaSource(3).setName(localMusicResult.getTrack()).setIsFromSoundSearch(true).setUrl(getPlayStoreLink(localMusicResult)).setMusicItem(localMusicItem);
    String str1 = getAlbumArtUrl(localMusicResult, paramBoolean);
    if (str1 != null) {
      localPlayMediaAction.setItemImageUrl(str1);
    }
    EarsService.ProductOffer localProductOffer = getGoogleMusicProductOffer(localMusicResult);
    if (localProductOffer != null)
    {
      String str2 = getPrice(localProductOffer, paramString);
      if (str2 != null) {
        localPlayMediaAction.addItemPriceTag(new ActionV2Protos.PlayMediaAction.PriceTag().setPrice(str2));
      }
      if (localProductOffer.hasPreviewUrl()) {
        localPlayMediaAction.setItemPreviewUrl(localProductOffer.getPreviewUrl());
      }
    }
    return localPlayMediaAction;
  }
  
  public static String getAlbumArtUrl(EarsService.MusicResult paramMusicResult, boolean paramBoolean)
  {
    if ((paramMusicResult.hasAlbumArtUrl()) && (!TextUtils.isEmpty(paramMusicResult.getAlbumArtUrl()))) {
      return paramMusicResult.getAlbumArtUrl();
    }
    if ((paramBoolean) && (paramMusicResult.hasSignedInAlbumArtUrl()) && (!TextUtils.isEmpty(paramMusicResult.getSignedInAlbumArtUrl()))) {
      return paramMusicResult.getSignedInAlbumArtUrl();
    }
    return null;
  }
  
  public static EarsService.EarsResult getFirstEarsResultWithMusic(List<EarsService.EarsResult> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      EarsService.EarsResult localEarsResult = (EarsService.EarsResult)localIterator.next();
      if (localEarsResult.hasMusicResult())
      {
        normalizeMusicResult(localEarsResult.getMusicResult());
        return localEarsResult;
      }
    }
    return null;
  }
  
  @Nullable
  public static EarsService.EarsResult getFirstEarsResultWithTv(List<EarsService.EarsResult> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      EarsService.EarsResult localEarsResult = (EarsService.EarsResult)localIterator.next();
      if (localEarsResult.hasTvResult()) {
        return localEarsResult;
      }
    }
    return null;
  }
  
  public static String getGoogleMusicArtistUrl(EarsService.MusicResult paramMusicResult)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramMusicResult.hasArtistId()) && (!TextUtils.isEmpty(paramMusicResult.getArtistId()))) {
      localStringBuilder.append("https://play.google.com/store/music/artist").append("?id=").append(paramMusicResult.getArtistId()).append("&utm_source=sound_search_gsa");
    }
    for (;;)
    {
      return localStringBuilder.toString();
      localStringBuilder.append("https://play.google.com/store/search").append("?q=").append(paramMusicResult.getArtist()).append("&c=music").append("&utm_source=sound_search_gsa");
    }
  }
  
  public static EarsService.ProductOffer getGoogleMusicProductOffer(EarsService.MusicResult paramMusicResult)
  {
    Iterator localIterator = paramMusicResult.getOfferList().iterator();
    while (localIterator.hasNext())
    {
      EarsService.ProductOffer localProductOffer = (EarsService.ProductOffer)localIterator.next();
      if (localProductOffer.getVendor() == 2) {
        return localProductOffer;
      }
    }
    return null;
  }
  
  public static String getGoogleMusicUrl(EarsService.ProductOffer paramProductOffer)
  {
    return "https://play.google.com/store/music/album" + "?id=" + paramProductOffer.getParentIdentifier() + "&tid=song-" + paramProductOffer.getIdentifier() + "&utm_source=sound_search_gsa";
  }
  
  public static String getPlayStoreLink(EarsService.MusicResult paramMusicResult)
  {
    EarsService.ProductOffer localProductOffer = getGoogleMusicProductOffer(paramMusicResult);
    if (localProductOffer != null) {
      return getGoogleMusicUrl(localProductOffer);
    }
    return getGoogleMusicArtistUrl(paramMusicResult);
  }
  
  public static String getPrice(EarsService.ProductOffer paramProductOffer, @Nullable String paramString)
  {
    EarsService.ProductOffer.PricingInfo localPricingInfo;
    String str;
    do
    {
      Iterator localIterator1 = paramProductOffer.getPricingInfoList().iterator();
      Iterator localIterator2;
      while (!localIterator2.hasNext())
      {
        if (!localIterator1.hasNext()) {
          break;
        }
        localPricingInfo = (EarsService.ProductOffer.PricingInfo)localIterator1.next();
        localIterator2 = localPricingInfo.getCountryList().iterator();
      }
      str = (String)localIterator2.next();
    } while ((paramString == null) || (!paramString.equalsIgnoreCase(str)));
    Currency localCurrency = Currency.getInstance(localPricingInfo.getCurrencyCode());
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = localCurrency.getSymbol();
    arrayOfObject[1] = Double.valueOf(localPricingInfo.getPriceMicros() / 1000000.0D);
    return String.format("%s%.2f", arrayOfObject);
    return null;
  }
  
  @Nullable
  public static String getQueryForSearch(List<EarsService.EarsResult> paramList)
  {
    EarsService.EarsResult localEarsResult = getFirstEarsResultWithMusic(paramList);
    if (localEarsResult != null)
    {
      EarsService.MusicResult localMusicResult = localEarsResult.getMusicResult();
      return localMusicResult.getArtist() + " " + localMusicResult.getTrack();
    }
    return null;
  }
  
  public static int getResultTypeInt(EarsService.EarsResult paramEarsResult)
  {
    if (paramEarsResult.hasMusicResult()) {
      return 0;
    }
    if (paramEarsResult.hasTvResult()) {
      return 1;
    }
    return 2;
  }
  
  public static void normalizeMusicResult(EarsService.MusicResult paramMusicResult)
  {
    if (paramMusicResult.hasTrack()) {
      paramMusicResult.setTrack(normalizeString(paramMusicResult.getTrack()));
    }
    if (paramMusicResult.hasArtist()) {
      paramMusicResult.setArtist(normalizeString(paramMusicResult.getArtist()));
    }
  }
  
  private static String normalizeString(String paramString)
  {
    return removeBracketedText(removeBracketedText(paramString, '(', ')'), '[', ']').trim();
  }
  
  private static String removeBracketedText(String paramString, char paramChar1, char paramChar2)
  {
    int i = paramString.indexOf(paramChar1);
    int j;
    if (i > -1) {
      j = 1;
    }
    for (int k = i + 1;; k++)
    {
      int m = paramString.length();
      int n = 0;
      if (k < m)
      {
        if (paramString.charAt(k) == paramChar1) {
          j++;
        }
        if (paramString.charAt(k) == paramChar2) {
          j--;
        }
        if (j == 0)
        {
          paramString = paramString.substring(0, i) + paramString.substring(k + 1);
          n = 1;
        }
      }
      else
      {
        if (n != 0) {
          break;
        }
        paramString = paramString.substring(0, i);
        return paramString;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ears.EarsResultParser
 * JD-Core Version:    0.7.0.1
 */