package com.google.android.search.gel;

import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.api.SuggestionLogInfo;
import com.google.android.search.shared.ui.SuggestionClickListener;
import com.google.android.search.shared.ui.SuggestionFormatter;
import com.google.android.search.shared.ui.SuggestionViewFactory;
import com.google.android.search.shared.ui.ViewRecycler;
import com.google.android.search.shared.ui.util.SearchFormulationLogging;
import com.google.android.shared.util.UriLoader;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import java.util.List;

public class GelSuggestionsController
{
  private final SuggestionLogInfo mCachedZeroPrefixSuggestionLogInfo = SuggestionLogInfo.EMPTY;
  private final List<Suggestion> mCachedZeroPrefixSuggestions = Lists.newArrayList();
  private SearchFormulationLogging mFormulationLogging;
  private final GoogleNowPromoController mGoogleNowPromoController;
  private final int mMaxSummonsSuggestions;
  private final int mMinWebSuggestions;
  private int mMode;
  private Suggestion mNowPromoSuggestion;
  private final SuggestionLogInfo mNowPromoSuggestionLogInfo = SuggestionLogInfo.EMPTY;
  private int mSummonSuggestionsShown;
  private final List<Suggestion> mSummonsSuggestions = Lists.newArrayList();
  private final Ui mUi;
  private boolean mUpdateInProgress;
  private final List<Suggestion> mWebSuggestions = Lists.newArrayList();
  private int mWebSuggestionsShown;
  
  public GelSuggestionsController(int paramInt1, int paramInt2, Ui paramUi, SuggestionViewFactory paramSuggestionViewFactory, SuggestionFormatter paramSuggestionFormatter, UriLoader<Drawable> paramUriLoader, ViewRecycler paramViewRecycler, GoogleNowPromoController paramGoogleNowPromoController)
  {
    this.mMaxSummonsSuggestions = paramInt2;
    this.mMinWebSuggestions = paramInt1;
    this.mUi = paramUi;
    this.mGoogleNowPromoController = paramGoogleNowPromoController;
    this.mUi.initialize(paramSuggestionViewFactory, paramSuggestionFormatter, paramUriLoader, paramViewRecycler, paramGoogleNowPromoController);
  }
  
  public int getZeroPrefixSuggestionsCount()
  {
    return this.mCachedZeroPrefixSuggestions.size();
  }
  
  public void greyOutSuggestions()
  {
    if (!this.mUpdateInProgress) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mUi.greyOutWebSuggestions();
      this.mUi.greyOutSummonsSuggestions();
      this.mUi.greyOutGoogleNowPromo();
      return;
    }
  }
  
  public void onUpdateAdd(Query paramQuery, Suggestion paramSuggestion)
  {
    Preconditions.checkState(this.mUpdateInProgress);
    if ((paramSuggestion.isWebSearchSuggestion()) || (paramSuggestion.isNavSuggestion()))
    {
      this.mWebSuggestions.add(paramSuggestion);
      return;
    }
    if (paramSuggestion.isNowPromo())
    {
      this.mNowPromoSuggestion = paramSuggestion;
      return;
    }
    this.mSummonsSuggestions.add(paramSuggestion);
  }
  
  public void onUpdateEnd(Query paramQuery, boolean paramBoolean, SuggestionLogInfo paramSuggestionLogInfo)
  {
    boolean bool = true;
    Preconditions.checkState(this.mUpdateInProgress);
    String str = paramQuery.getQueryStringForSuggest();
    if (str.isEmpty())
    {
      this.mCachedZeroPrefixSuggestions.clear();
      this.mCachedZeroPrefixSuggestions.addAll(this.mWebSuggestions);
    }
    if (this.mMode == 2)
    {
      if ((paramBoolean) || (this.mWebSuggestions.size() != 0)) {
        break label215;
      }
      this.mUi.greyOutWebSuggestions();
      this.mWebSuggestionsShown = Math.max(this.mMinWebSuggestions, this.mWebSuggestionsShown - (this.mSummonsSuggestions.size() - this.mSummonSuggestionsShown));
      this.mUi.retainWebSuggestions(this.mWebSuggestionsShown);
      if ((paramBoolean) || (this.mSummonsSuggestions.size() != 0)) {
        break label250;
      }
      this.mUi.greyOutSummonsSuggestions();
    }
    for (;;)
    {
      if ((paramBoolean) || (this.mNowPromoSuggestion != null)) {
        break label341;
      }
      this.mUi.greyOutGoogleNowPromo();
      if (this.mFormulationLogging != null) {
        this.mFormulationLogging.registerSuggestionsShown(new ImmutableList.Builder().addAll(this.mWebSuggestions).addAll(this.mSummonsSuggestions).build(), paramSuggestionLogInfo, paramBoolean);
      }
      this.mUpdateInProgress = false;
      return;
      label215:
      this.mWebSuggestionsShown = this.mWebSuggestions.size();
      this.mUi.updateWebSuggestions(str, this.mWebSuggestions, this.mWebSuggestionsShown);
      break;
      label250:
      this.mSummonSuggestionsShown = this.mSummonsSuggestions.size();
      if (this.mSummonSuggestionsShown >= this.mMaxSummonsSuggestions)
      {
        this.mUi.showSummonsFooter(bool);
        this.mUi.updateSummonsSuggestions(str, this.mSummonsSuggestions, -1 + this.mSummonSuggestionsShown);
      }
      else
      {
        this.mUi.showSummonsFooter(false);
        this.mUi.updateSummonsSuggestions(str, this.mSummonsSuggestions, this.mSummonSuggestionsShown);
      }
    }
    label341:
    if ((this.mGoogleNowPromoController.shouldShowPromo()) && (this.mNowPromoSuggestion != null)) {}
    for (;;)
    {
      this.mUi.showGoogleNowPromo(bool);
      break;
      bool = false;
    }
  }
  
  public void onUpdateStart()
  {
    if (!this.mUpdateInProgress) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mUpdateInProgress = true;
      this.mWebSuggestions.clear();
      this.mSummonsSuggestions.clear();
      this.mNowPromoSuggestion = null;
      return;
    }
  }
  
  public void setMode(int paramInt, boolean paramBoolean, SearchFormulationLogging paramSearchFormulationLogging)
  {
    boolean bool;
    if (!this.mUpdateInProgress)
    {
      bool = true;
      Preconditions.checkState(bool);
      this.mFormulationLogging = paramSearchFormulationLogging;
      if (paramBoolean) {
        this.mUi.disableLayoutTransitionsUntilNextLayout();
      }
      if (paramInt != 2) {
        break label182;
      }
      if (this.mCachedZeroPrefixSuggestions.size() > 0)
      {
        this.mUi.updateWebSuggestions("", this.mCachedZeroPrefixSuggestions, this.mCachedZeroPrefixSuggestions.size());
        if (this.mFormulationLogging != null) {
          this.mFormulationLogging.registerSuggestionsShown(this.mCachedZeroPrefixSuggestions, this.mCachedZeroPrefixSuggestionLogInfo, true);
        }
      }
      if ((this.mNowPromoSuggestion == null) || (!this.mGoogleNowPromoController.shouldShowPromo())) {
        break label164;
      }
      this.mUi.showGoogleNowPromo(true);
      if (this.mFormulationLogging != null) {
        this.mFormulationLogging.registerSuggestionsShown(ImmutableList.of(this.mNowPromoSuggestion), this.mNowPromoSuggestionLogInfo, true);
      }
    }
    for (;;)
    {
      this.mMode = paramInt;
      return;
      bool = false;
      break;
      label164:
      this.mUi.showGoogleNowPromo(false);
      this.mNowPromoSuggestion = null;
      continue;
      label182:
      this.mUi.hideSuggestions();
    }
  }
  
  public void setSuggestionClickListener(SuggestionClickListener paramSuggestionClickListener)
  {
    this.mUi.setSuggestionClickListener(paramSuggestionClickListener);
  }
  
  public void setSummonsFooterClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mUi.setSummonsFooterClickListener(paramOnClickListener);
  }
  
  public static abstract interface Ui
  {
    public abstract void disableLayoutTransitionsUntilNextLayout();
    
    public abstract void greyOutGoogleNowPromo();
    
    public abstract void greyOutSummonsSuggestions();
    
    public abstract void greyOutWebSuggestions();
    
    public abstract void hideSuggestions();
    
    public abstract void initialize(SuggestionViewFactory paramSuggestionViewFactory, SuggestionFormatter paramSuggestionFormatter, UriLoader<Drawable> paramUriLoader, ViewRecycler paramViewRecycler, GoogleNowPromoController paramGoogleNowPromoController);
    
    public abstract void retainWebSuggestions(int paramInt);
    
    public abstract void setSuggestionClickListener(SuggestionClickListener paramSuggestionClickListener);
    
    public abstract void setSummonsFooterClickListener(View.OnClickListener paramOnClickListener);
    
    public abstract void showGoogleNowPromo(boolean paramBoolean);
    
    public abstract void showSummonsFooter(boolean paramBoolean);
    
    public abstract void updateSummonsSuggestions(String paramString, List<Suggestion> paramList, int paramInt);
    
    public abstract void updateWebSuggestions(String paramString, List<Suggestion> paramList, int paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.GelSuggestionsController
 * JD-Core Version:    0.7.0.1
 */