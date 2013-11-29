package com.google.android.search.core.preferences;

import android.util.Pair;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryQuery;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.Interest;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class MyPlacesUtil
{
  private static final String TAG = Tag.getTag(MyPlacesUtil.class);
  
  public static Sidekick.EntryQuery buildQuery()
  {
    Sidekick.Interest localInterest = new Sidekick.Interest();
    localInterest.setTargetDisplay(5);
    localInterest.addEntryTypeRestrict(1);
    Sidekick.EntryQuery localEntryQuery = new Sidekick.EntryQuery();
    localEntryQuery.addInterest(localInterest);
    return localEntryQuery;
  }
  
  @Nullable
  public static Pair<Sidekick.Entry, Sidekick.Entry> getHomeWorkEntries(@Nullable Sidekick.EntryResponse paramEntryResponse)
  {
    if ((paramEntryResponse == null) || (paramEntryResponse.getEntryTreeCount() == 0)) {}
    Sidekick.EntryTree localEntryTree;
    do
    {
      return null;
      localEntryTree = paramEntryResponse.getEntryTree(0);
    } while (!localEntryTree.hasRoot());
    Object localObject1 = null;
    Object localObject2 = null;
    Iterator localIterator = localEntryTree.getRoot().getEntryList().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      if (localEntry.hasFrequentPlaceEntry())
      {
        Sidekick.Action localAction = getRenameOrEditAction(localEntry);
        if (localAction != null) {
          if (localAction.getType() == 17) {
            localObject1 = localEntry;
          } else if (localAction.getType() == 18) {
            localObject2 = localEntry;
          }
        }
      }
    }
    return Pair.create(localObject1, localObject2);
  }
  
  private static Sidekick.Action getRenameOrEditAction(Sidekick.Entry paramEntry)
  {
    return ProtoUtils.findAction(paramEntry, 17, new int[] { 18 });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.MyPlacesUtil
 * JD-Core Version:    0.7.0.1
 */