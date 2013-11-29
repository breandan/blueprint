package com.google.android.search.core.summons;

import com.google.android.search.core.suggest.SuggestionFilter;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Suggestion;
import javax.annotation.Nullable;

public class ContactsSourceFilter
  implements SuggestionFilter
{
  public static final ContactsSourceFilter INSTANCE = new ContactsSourceFilter();
  
  public boolean accept(@Nullable SuggestionList paramSuggestionList, Suggestion paramSuggestion)
  {
    return "android.provider.Contacts.SEARCH_SUGGESTION_CLICKED".equals(paramSuggestion.getSuggestionIntentAction());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.ContactsSourceFilter
 * JD-Core Version:    0.7.0.1
 */