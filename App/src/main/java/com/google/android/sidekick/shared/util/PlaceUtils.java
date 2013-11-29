package com.google.android.sidekick.shared.util;

import android.content.Context;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.PlaceData;
import javax.annotation.Nullable;

public class PlaceUtils
{
  @Nullable
  public static String getAddress(Sidekick.Entry paramEntry)
  {
    Sidekick.Location localLocation = getLocation(paramEntry);
    if ((localLocation != null) && (localLocation.hasAddress())) {
      return localLocation.getAddress();
    }
    return null;
  }
  
  @Nullable
  public static String getConfirmedAddress(@Nullable Sidekick.Entry paramEntry)
  {
    if ((paramEntry == null) || (!isConfirmed(paramEntry))) {
      return null;
    }
    return getAddress(paramEntry);
  }
  
  @Nullable
  public static Sidekick.FrequentPlaceEntry getFrequentPlaceEntry(Sidekick.Entry paramEntry)
  {
    if (paramEntry.hasFrequentPlaceEntry()) {
      return paramEntry.getFrequentPlaceEntry();
    }
    if (paramEntry.hasNearbyPlaceEntry()) {
      return paramEntry.getNearbyPlaceEntry();
    }
    if (paramEntry.hasHotelPlaceEntry()) {
      return paramEntry.getHotelPlaceEntry();
    }
    if (paramEntry.hasRestaurantPlaceEntry()) {
      return paramEntry.getRestaurantPlaceEntry();
    }
    return null;
  }
  
  @Nullable
  private static Sidekick.Location getLocation(Sidekick.Entry paramEntry)
  {
    if (paramEntry.hasFrequentPlaceEntry())
    {
      Sidekick.FrequentPlaceEntry localFrequentPlaceEntry = paramEntry.getFrequentPlaceEntry();
      if (localFrequentPlaceEntry.hasFrequentPlace())
      {
        Sidekick.FrequentPlace localFrequentPlace = localFrequentPlaceEntry.getFrequentPlace();
        if (localFrequentPlace.hasLocation()) {
          return localFrequentPlace.getLocation();
        }
      }
    }
    return null;
  }
  
  @Nullable
  public static String getLocationName(Sidekick.Entry paramEntry)
  {
    Sidekick.Location localLocation = getLocation(paramEntry);
    if ((localLocation != null) && (localLocation.hasName())) {
      return localLocation.getName();
    }
    return null;
  }
  
  public static Sidekick.PlaceData getPlaceDataFromEntry(Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry)
  {
    if (paramFrequentPlaceEntry.hasFrequentPlace()) {}
    for (Sidekick.FrequentPlace localFrequentPlace = paramFrequentPlaceEntry.getFrequentPlace();; localFrequentPlace = null)
    {
      Sidekick.PlaceData localPlaceData = null;
      if (localFrequentPlace != null)
      {
        boolean bool = localFrequentPlace.hasPlaceData();
        localPlaceData = null;
        if (bool) {
          localPlaceData = localFrequentPlace.getPlaceData();
        }
      }
      return localPlaceData;
    }
  }
  
  public static String getPlaceName(Context paramContext, Sidekick.FrequentPlace paramFrequentPlace)
  {
    Object localObject = null;
    Sidekick.Location localLocation;
    String str1;
    label45:
    Sidekick.PlaceData localPlaceData;
    label58:
    String str2;
    if (paramFrequentPlace != null)
    {
      if (!paramFrequentPlace.hasLocation()) {
        break label103;
      }
      localLocation = paramFrequentPlace.getLocation();
      if ((localLocation == null) || (!localLocation.hasName()) || (localLocation.getName().length() <= 0)) {
        break label108;
      }
      str1 = localLocation.getName();
      if (!paramFrequentPlace.hasPlaceData()) {
        break label114;
      }
      localPlaceData = paramFrequentPlace.getPlaceData();
      if ((localPlaceData == null) || (!localPlaceData.hasDisplayName()) || (localPlaceData.getDisplayName().length() <= 0)) {
        break label120;
      }
      str2 = localPlaceData.getDisplayName();
      label89:
      if (str2 == null) {
        break label126;
      }
      localObject = str2;
    }
    for (;;)
    {
      if (localObject == null) {
        break label209;
      }
      return localObject;
      label103:
      localLocation = null;
      break;
      label108:
      str1 = null;
      break label45;
      label114:
      localPlaceData = null;
      break label58;
      label120:
      str2 = null;
      break label89;
      label126:
      if (str1 != null)
      {
        localObject = str1;
      }
      else
      {
        localObject = null;
        if (localLocation != null)
        {
          boolean bool1 = localLocation.hasLat();
          localObject = null;
          if (bool1)
          {
            boolean bool2 = localLocation.hasLng();
            localObject = null;
            if (bool2)
            {
              Object[] arrayOfObject = new Object[2];
              arrayOfObject[0] = Double.valueOf(localLocation.getLat());
              arrayOfObject[1] = Double.valueOf(localLocation.getLng());
              localObject = paramContext.getString(2131362270, arrayOfObject);
            }
          }
        }
      }
    }
    label209:
    return paramContext.getString(2131362213);
  }
  
  public static boolean isConfirmed(Sidekick.Entry paramEntry)
  {
    Sidekick.Action localAction = ProtoUtils.findAction(paramEntry, 16, new int[0]);
    boolean bool = false;
    if (localAction == null) {
      bool = true;
    }
    return bool;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.PlaceUtils
 * JD-Core Version:    0.7.0.1
 */