package com.google.android.speech.contacts;

import com.google.android.search.core.GsaConfigFlags;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class FavoriteContactNamesSupplier
  implements Supplier<List<String>>
{
  private long cacheTimestamp;
  private List<String> cachedFavorites;
  private final ContactLookup mContactLookup;
  private final GsaConfigFlags mGsaFlags;
  
  public FavoriteContactNamesSupplier(GsaConfigFlags paramGsaConfigFlags, ContactLookup paramContactLookup)
  {
    this.mGsaFlags = paramGsaConfigFlags;
    this.mContactLookup = paramContactLookup;
  }
  
  public List<String> get()
  {
    ArrayList localArrayList = Lists.newArrayList();
    int i = this.mGsaFlags.getS3MaxTopContactNamesToSend();
    if (i == 0) {
      return localArrayList;
    }
    String[] arrayOfString = this.mGsaFlags.getMolinoInjectTopContacts();
    if (arrayOfString.length > 0)
    {
      int k = arrayOfString.length;
      for (int m = 0; m < k; m++) {
        localArrayList.add(arrayOfString[m]);
      }
    }
    if (System.currentTimeMillis() - this.cacheTimestamp > 300000L) {
      this.cachedFavorites = null;
    }
    if (this.cachedFavorites == null)
    {
      int j = i * 4;
      System.currentTimeMillis();
      this.cachedFavorites = this.mContactLookup.findFavoriteContactNames(j, i);
      System.currentTimeMillis();
      this.cacheTimestamp = System.currentTimeMillis();
    }
    localArrayList.addAll(this.cachedFavorites);
    return localArrayList;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.FavoriteContactNamesSupplier
 * JD-Core Version:    0.7.0.1
 */