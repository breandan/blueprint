package com.google.android.search.core.service;

import android.text.TextUtils;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.suggest.CorrectionPromoter;
import com.google.android.search.core.suggest.MutableSuggestionList;
import com.google.android.search.core.suggest.MutableSuggestionListImpl;
import com.google.android.search.core.suggest.Promoter;
import com.google.android.search.core.suggest.Suggestions;
import com.google.android.search.shared.api.Query;

public class AggregatePromoter
  implements Promoter
{
  private final Promoter mCorrectionPromoter;
  private final GsaConfigFlags mGsaConfigFlags;
  private final int mMaxWeb;
  private final int mMinWeb;
  private final Promoter mNowPromoPromoter;
  private final String[] mQueryPrefixes;
  private final Promoter mSummonsPromoter;
  private final Promoter mWebPromoter;
  
  public AggregatePromoter(int paramInt1, int paramInt2, int paramInt3, Promoter paramPromoter1, Promoter paramPromoter2, CorrectionPromoter paramCorrectionPromoter, Promoter paramPromoter3, GsaConfigFlags paramGsaConfigFlags)
  {
    this.mMinWeb = paramInt1;
    this.mMaxWeb = paramInt2;
    this.mWebPromoter = paramPromoter1;
    this.mSummonsPromoter = paramPromoter2;
    this.mCorrectionPromoter = paramCorrectionPromoter;
    this.mNowPromoPromoter = paramPromoter3;
    this.mGsaConfigFlags = paramGsaConfigFlags;
    this.mQueryPrefixes = new String[paramInt3 - paramInt1];
  }
  
  private int getExpectedMaxSummons(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      if (this.mGsaConfigFlags.getZeroPrefixAppSuggestEnabled())
      {
        i = this.mQueryPrefixes.length;
        return i;
      }
      return 0;
    }
    for (int i = 0;; i++)
    {
      if (i >= this.mQueryPrefixes.length) {
        break label63;
      }
      String str = this.mQueryPrefixes[i];
      if ((str != null) && (TextUtils.indexOf(paramString, str) == 0)) {
        break;
      }
    }
    label63:
    return this.mQueryPrefixes.length;
  }
  
  public void pickPromoted(Suggestions paramSuggestions, int paramInt, MutableSuggestionList paramMutableSuggestionList)
  {
    Query localQuery = paramSuggestions.getQuery();
    String str1 = localQuery.getQueryString();
    MutableSuggestionListImpl localMutableSuggestionListImpl1 = new MutableSuggestionListImpl("corrections", localQuery);
    this.mCorrectionPromoter.pickPromoted(paramSuggestions, 1, localMutableSuggestionListImpl1);
    paramMutableSuggestionList.addAll(localMutableSuggestionListImpl1);
    int i = getExpectedMaxSummons(str1);
    MutableSuggestionListImpl localMutableSuggestionListImpl2 = new MutableSuggestionListImpl("web", localQuery);
    MutableSuggestionListImpl localMutableSuggestionListImpl3 = new MutableSuggestionListImpl("summons", localQuery);
    int j = Math.min(this.mQueryPrefixes.length, paramInt - this.mMinWeb);
    this.mSummonsPromoter.pickPromoted(paramSuggestions, j, localMutableSuggestionListImpl3);
    int k = localMutableSuggestionListImpl3.getCount();
    if ((localMutableSuggestionListImpl3.isFinal()) && (k < this.mQueryPrefixes.length) && (str1.length() > 0))
    {
      String str2 = this.mQueryPrefixes[k];
      if ((str2 == null) || (TextUtils.indexOf(str1, str2) == -1)) {
        this.mQueryPrefixes[k] = str1;
      }
      i = k;
    }
    this.mNowPromoPromoter.pickPromoted(paramSuggestions, 1, localMutableSuggestionListImpl3);
    if (localMutableSuggestionListImpl3.getCount() > k) {
      i++;
    }
    if (paramSuggestions.areWebResultsDone())
    {
      int m = Math.max(this.mMinWeb, this.mMaxWeb - i);
      this.mWebPromoter.pickPromoted(paramSuggestions, m, localMutableSuggestionListImpl2);
    }
    paramMutableSuggestionList.addAll(localMutableSuggestionListImpl2);
    paramMutableSuggestionList.addAll(localMutableSuggestionListImpl3);
    if ((localMutableSuggestionListImpl2.isFinal()) && (localMutableSuggestionListImpl3.isFinal())) {
      paramMutableSuggestionList.setFinal();
    }
    paramMutableSuggestionList.setFromCache(localMutableSuggestionListImpl2.isFromCache());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.service.AggregatePromoter
 * JD-Core Version:    0.7.0.1
 */