package com.google.android.search.core.summons;

import com.google.android.search.core.suggest.Promoter;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Query;

public class FirstNonEmptySummonsPromoter
  implements Promoter
{
  private SuggestionList mChosenResults;
  private Query mChosenResultsQuery;
  
  /* Error */
  public void pickPromoted(com.google.android.search.core.suggest.Suggestions paramSuggestions, int paramInt, com.google.android.search.core.suggest.MutableSuggestionList paramMutableSuggestionList)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 18	com/google/android/search/core/summons/FirstNonEmptySummonsPromoter:mChosenResults	Lcom/google/android/search/core/suggest/SuggestionList;
    //   6: ifnull +17 -> 23
    //   9: aload_1
    //   10: invokevirtual 24	com/google/android/search/core/suggest/Suggestions:getQuery	()Lcom/google/android/search/shared/api/Query;
    //   13: aload_0
    //   14: getfield 26	com/google/android/search/core/summons/FirstNonEmptySummonsPromoter:mChosenResultsQuery	Lcom/google/android/search/shared/api/Query;
    //   17: invokestatic 32	com/google/android/search/shared/api/Query:equivalentForSuggest	(Lcom/google/android/search/shared/api/Query;Lcom/google/android/search/shared/api/Query;)Z
    //   20: ifne +65 -> 85
    //   23: aload_0
    //   24: aconst_null
    //   25: putfield 18	com/google/android/search/core/summons/FirstNonEmptySummonsPromoter:mChosenResults	Lcom/google/android/search/core/suggest/SuggestionList;
    //   28: aload_1
    //   29: invokevirtual 36	com/google/android/search/core/suggest/Suggestions:getSourceResults	()Ljava/lang/Iterable;
    //   32: invokeinterface 42 1 0
    //   37: astore 5
    //   39: aload 5
    //   41: invokeinterface 48 1 0
    //   46: ifeq +39 -> 85
    //   49: aload 5
    //   51: invokeinterface 52 1 0
    //   56: checkcast 54	com/google/android/search/core/suggest/SuggestionList
    //   59: astore 9
    //   61: aload 9
    //   63: invokeinterface 58 1 0
    //   68: ifle -29 -> 39
    //   71: aload_0
    //   72: aload 9
    //   74: putfield 18	com/google/android/search/core/summons/FirstNonEmptySummonsPromoter:mChosenResults	Lcom/google/android/search/core/suggest/SuggestionList;
    //   77: aload_0
    //   78: aload_1
    //   79: invokevirtual 24	com/google/android/search/core/suggest/Suggestions:getQuery	()Lcom/google/android/search/shared/api/Query;
    //   82: putfield 26	com/google/android/search/core/summons/FirstNonEmptySummonsPromoter:mChosenResultsQuery	Lcom/google/android/search/shared/api/Query;
    //   85: aload_0
    //   86: getfield 18	com/google/android/search/core/summons/FirstNonEmptySummonsPromoter:mChosenResults	Lcom/google/android/search/core/suggest/SuggestionList;
    //   89: ifnull +46 -> 135
    //   92: aload_0
    //   93: getfield 18	com/google/android/search/core/summons/FirstNonEmptySummonsPromoter:mChosenResults	Lcom/google/android/search/core/suggest/SuggestionList;
    //   96: invokeinterface 59 1 0
    //   101: astore 6
    //   103: aload 6
    //   105: invokeinterface 48 1 0
    //   110: ifeq +25 -> 135
    //   113: aload 6
    //   115: invokeinterface 52 1 0
    //   120: checkcast 61	com/google/android/search/shared/api/Suggestion
    //   123: astore 7
    //   125: aload_3
    //   126: invokeinterface 64 1 0
    //   131: iload_2
    //   132: if_icmplt +19 -> 151
    //   135: aload_1
    //   136: invokevirtual 67	com/google/android/search/core/suggest/Suggestions:areSummonsDone	()Z
    //   139: ifeq +9 -> 148
    //   142: aload_3
    //   143: invokeinterface 70 1 0
    //   148: aload_0
    //   149: monitorexit
    //   150: return
    //   151: aload_3
    //   152: aload 7
    //   154: invokeinterface 74 2 0
    //   159: pop
    //   160: goto -57 -> 103
    //   163: astore 4
    //   165: aload_0
    //   166: monitorexit
    //   167: aload 4
    //   169: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	170	0	this	FirstNonEmptySummonsPromoter
    //   0	170	1	paramSuggestions	com.google.android.search.core.suggest.Suggestions
    //   0	170	2	paramInt	int
    //   0	170	3	paramMutableSuggestionList	com.google.android.search.core.suggest.MutableSuggestionList
    //   163	5	4	localObject	Object
    //   37	13	5	localIterator1	java.util.Iterator
    //   101	13	6	localIterator2	java.util.Iterator
    //   123	30	7	localSuggestion	com.google.android.search.shared.api.Suggestion
    //   59	14	9	localSuggestionList	SuggestionList
    // Exception table:
    //   from	to	target	type
    //   2	23	163	finally
    //   23	39	163	finally
    //   39	85	163	finally
    //   85	103	163	finally
    //   103	135	163	finally
    //   135	148	163	finally
    //   151	160	163	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.FirstNonEmptySummonsPromoter
 * JD-Core Version:    0.7.0.1
 */