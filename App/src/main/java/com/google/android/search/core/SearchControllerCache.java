package com.google.android.search.core;

import android.util.Log;
import com.google.android.shared.util.TokenAssignmentCache;
import com.google.android.velvet.VelvetFactory;

public class SearchControllerCache
  extends TokenAssignmentCache<SearchController>
{
  private final VelvetFactory mFactory;
  
  public SearchControllerCache(VelvetFactory paramVelvetFactory)
  {
    this.mFactory = paramVelvetFactory;
  }
  
  public SearchController createItem()
  {
    Log.v("SearchControllerCache", "creating SearchController");
    return this.mFactory.createSearchController();
  }
  
  public void destroyItem(SearchController paramSearchController)
  {
    Log.v("SearchControllerCache", "disposing SearchController");
    paramSearchController.dispose();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.SearchControllerCache
 * JD-Core Version:    0.7.0.1
 */