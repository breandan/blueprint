package com.google.android.libraries.tvdetect;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProductTypes
{
  public static final Set<ProductType> ALL = ;
  public static final Set<ProductType> TV_ONLY = newSetWithTvOnly();
  
  private static Set<ProductType> newSetWithAll()
  {
    HashSet localHashSet = new HashSet();
    ProductType[] arrayOfProductType = ProductType.values();
    int i = arrayOfProductType.length;
    for (int j = 0; j < i; j++) {
      localHashSet.add(arrayOfProductType[j]);
    }
    return Collections.unmodifiableSet(localHashSet);
  }
  
  private static Set<ProductType> newSetWithTvOnly()
  {
    HashSet localHashSet = new HashSet();
    localHashSet.add(ProductType.TV);
    return Collections.unmodifiableSet(localHashSet);
  }
  
  public static Set<ProductType> setOfTvOnly()
  {
    return TV_ONLY;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.ProductTypes
 * JD-Core Version:    0.7.0.1
 */