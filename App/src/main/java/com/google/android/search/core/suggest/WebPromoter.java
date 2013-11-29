package com.google.android.search.core.suggest;

import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.shared.api.Suggestion;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class WebPromoter
  extends FilteringPromoter
{
  private final GsaConfigFlags mConfigFlags;
  
  public WebPromoter(SuggestionFilter paramSuggestionFilter, GsaConfigFlags paramGsaConfigFlags)
  {
    super(paramSuggestionFilter);
    this.mConfigFlags = paramGsaConfigFlags;
  }
  
  public void pickPromoted(Suggestions paramSuggestions, int paramInt, MutableSuggestionList paramMutableSuggestionList)
  {
    SuggestionList localSuggestionList = paramSuggestions.getWebResult();
    int i;
    ArrayList localArrayList1;
    ArrayList localArrayList2;
    int j;
    label45:
    Suggestion localSuggestion;
    if (localSuggestionList == null)
    {
      i = 0;
      if (localSuggestionList != null) {
        paramMutableSuggestionList.setFromCache(localSuggestionList.isFromCache());
      }
      localArrayList1 = Lists.newArrayList();
      localArrayList2 = Lists.newArrayList();
      j = 0;
      if (j >= i) {
        break label139;
      }
      localSuggestion = localSuggestionList.get(j);
      if (!localSuggestion.isFromDeviceHistory()) {
        break label99;
      }
      localArrayList2.add(localSuggestion);
    }
    for (;;)
    {
      j++;
      break label45;
      i = localSuggestionList.getCount();
      break;
      label99:
      if (((localSuggestion.isWebSearchSuggestion()) || (localSuggestion.isNavSuggestion())) && (accept(localSuggestionList, localSuggestion))) {
        localArrayList1.add(localSuggestion);
      }
    }
    label139:
    if (this.mConfigFlags.getShowLastQueryOnSuggestListBottom())
    {
      paramMutableSuggestionList.addAll(localArrayList1.subList(0, Math.min(localArrayList1.size(), paramInt - localArrayList2.size())));
      paramMutableSuggestionList.addAll(localArrayList2);
    }
    for (;;)
    {
      if (paramSuggestions.areWebResultsDone()) {
        paramMutableSuggestionList.setFinal();
      }
      return;
      paramMutableSuggestionList.addAll(localArrayList2);
      paramMutableSuggestionList.addAll(localArrayList1.subList(0, Math.min(localArrayList1.size(), paramInt - localArrayList2.size())));
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.WebPromoter
 * JD-Core Version:    0.7.0.1
 */