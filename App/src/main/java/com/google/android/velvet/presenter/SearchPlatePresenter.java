package com.google.android.velvet.presenter;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import com.google.android.search.core.Feature;
import com.google.android.search.core.SearchController;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.TtsState;
import com.google.android.search.core.state.UiState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.core.suggest.CachingPromoter;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.SuggestionsController;
import com.google.android.search.core.suggest.SuggestionsUi;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.SearchPlateUi;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.ui.SearchPlate.Callback;
import com.google.android.velvet.VelvetFactory;

public class SearchPlatePresenter
  extends VelvetFragmentPresenter
  implements SuggestionsUi
{
  private int mHintFlags = -1;
  public final SearchPlate.Callback mSearchPlateCallback = new SearchPlate.Callback()
  {
    public void onCancelRecordingClicked()
    {
      if (SearchPlatePresenter.this.isAttached()) {
        SearchPlatePresenter.this.getVelvetPresenter().goBack();
      }
    }
    
    public void onClearButtonClick()
    {
      if (SearchPlatePresenter.this.isAttached()) {
        SearchPlatePresenter.this.getVelvetPresenter().clearQuery();
      }
    }
    
    public void onCorrectionEnd()
    {
      if (SearchPlatePresenter.this.isAttached())
      {
        QueryState localQueryState = SearchPlatePresenter.this.getEventBus().getQueryState();
        localQueryState.set(localQueryState.get().withoutVoiceCorrection());
      }
    }
    
    public void onLauncherSearchButtonClick()
    {
      Log.w("SearchPlatePresenter", "Unexpected call to onLauncherModeButtonClick.");
    }
    
    public void onPromotedSoundSearchClick()
    {
      QueryState localQueryState = SearchPlatePresenter.this.getEventBus().getQueryState();
      localQueryState.switchQuery(localQueryState.getCommittedQuery(), localQueryState.getCommittedQuery().musicSearchFromPromotedQuery());
    }
    
    public void onQueryTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt, boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean) {
        SearchPlatePresenter.this.getEventBus().getQueryState().startQueryEdit();
      }
      if (SearchPlatePresenter.this.isAttached()) {
        SearchPlatePresenter.this.getVelvetPresenter().onQueryTextChanged(paramAnonymousCharSequence, paramAnonymousInt);
      }
    }
    
    public boolean onSearchBoxEditorAction(int paramAnonymousInt)
    {
      if ((paramAnonymousInt == 3) && (SearchPlatePresenter.this.isAttached()))
      {
        SearchPlatePresenter.this.getVelvetPresenter().commitTextQuery();
        return true;
      }
      return false;
    }
    
    public void onSearchBoxKeyboardFocused()
    {
      if (SearchPlatePresenter.this.isAttached()) {
        SearchPlatePresenter.this.getVelvetPresenter().onSearchBoxKeyboardFocused();
      }
    }
    
    public void onSearchBoxTouched()
    {
      if (SearchPlatePresenter.this.isAttached()) {
        SearchPlatePresenter.this.getVelvetPresenter().onSearchBoxTouched();
      }
      SearchPlatePresenter.this.mUi.focusQueryAndShowKeyboard();
    }
    
    public void onSearchButtonClick()
    {
      if (SearchPlatePresenter.this.isAttached()) {
        SearchPlatePresenter.this.getVelvetPresenter().commitTextQuery();
      }
    }
    
    public void onStartRecordingClicked()
    {
      QueryState localQueryState = SearchPlatePresenter.this.getEventBus().getQueryState();
      Query localQuery;
      if (SearchPlatePresenter.this.isAttached())
      {
        localQuery = localQueryState.getCommittedQuery();
        if (!localQuery.isVoiceSearch()) {
          break label55;
        }
        if (localQuery.isFollowOn()) {
          localQueryState.commit(localQuery.voiceSearchFromGui());
        }
      }
      else
      {
        return;
      }
      localQueryState.recommit(localQuery);
      return;
      label55:
      Log.i("SearchPlatePresenter", "Cannot start voice search: committed query is not voice search");
    }
    
    public void onStartVoiceSearchClicked()
    {
      if (SearchPlatePresenter.this.isAttached())
      {
        QueryState localQueryState = SearchPlatePresenter.this.getEventBus().getQueryState();
        localQueryState.commit(localQueryState.get().voiceSearchFromGui());
      }
    }
    
    public void onStopRecordingClicked()
    {
      if (SearchPlatePresenter.this.isAttached())
      {
        QueryState localQueryState = SearchPlatePresenter.this.getEventBus().getQueryState();
        localQueryState.stopListening(localQueryState.getCommittedQuery());
      }
    }
    
    public void onTextSelected(CharSequence paramAnonymousCharSequence, boolean paramAnonymousBoolean, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (SearchPlatePresenter.this.isAttached()) {
        SearchPlatePresenter.this.getVelvetPresenter().onQueryTextSelected(paramAnonymousCharSequence, paramAnonymousInt1, paramAnonymousInt2);
      }
    }
  };
  private final VelvetSearchPlateUi mUi;
  
  public SearchPlatePresenter(VelvetSearchPlateUi paramVelvetSearchPlateUi)
  {
    super("searchplate");
    this.mUi = paramVelvetSearchPlateUi;
    this.mUi.setPresenter(this);
  }
  
  public void focusQueryAndShowKeyboard()
  {
    this.mUi.focusQueryAndShowKeyboard();
  }
  
  public SearchPlate.Callback getSearchPlateCallback()
  {
    return this.mSearchPlateCallback;
  }
  
  int getSearchPlateMode()
  {
    VelvetEventBus localVelvetEventBus = getEventBus();
    QueryState localQueryState = localVelvetEventBus.getQueryState();
    ActionState localActionState = localVelvetEventBus.getActionState();
    UiState localUiState = localVelvetEventBus.getUiState();
    Query localQuery = localQueryState.getCommittedQuery();
    int i;
    if ((localQuery.isVoiceSearch()) || (localQuery.isMusicSearch()) || (localQuery.isTvSearch()))
    {
      i = 1;
      if (!localQuery.isSentinel()) {
        break label90;
      }
      if ((!UiMode.fromSentinelQuery(localQuery).isPredictiveMode()) || (localQueryState.isEditingQuery())) {
        break label88;
      }
    }
    label88:
    label90:
    while (localQuery.isPredictiveTvSearch())
    {
      return 1;
      i = 0;
      break;
      return 2;
    }
    if (localQueryState.getError() != null)
    {
      if ((Feature.CONNECTION_ERROR_CARDS.isEnabled()) && (i != 0)) {
        return 8;
      }
      return 7;
    }
    if ((localQuery.isVoiceSearch()) && (localQuery.getQueryString().isEmpty()))
    {
      if ((localUiState.shouldShowWebView()) || (localUiState.shouldShowCards()) || (localQueryState.isLoadingWebSearchResults())) {
        return 9;
      }
      if (localQueryState.isMusicDetected()) {
        return 4;
      }
      return 3;
    }
    if ((localQuery.isMusicSearch()) && (!localActionState.hasDataForQuery(localQuery))) {
      return 5;
    }
    if ((localQuery.isTvSearch()) && (!localActionState.hasDataForQuery(localQuery))) {
      return 6;
    }
    if (i != 0) {
      return 8;
    }
    if (localQueryState.isEditingQuery()) {
      return 2;
    }
    return 7;
  }
  
  public SearchPlateUi getSearchPlateUi()
  {
    return this.mUi;
  }
  
  protected void onPostAttach(Bundle paramBundle)
  {
    CachingPromoter localCachingPromoter = getFactory().createCorrectionCachingPromoter();
    getVelvetPresenter().getSuggestionsController().addSuggestionsView(SuggestionsController.CORRECTION_SUGGESTIONS, localCachingPromoter, this);
    if (paramBundle != null) {
      this.mUi.restoreInstanceState(paramBundle);
    }
  }
  
  protected void onPreDetach()
  {
    getVelvetPresenter().getSuggestionsController().removeSuggestionsView(SuggestionsController.CORRECTION_SUGGESTIONS);
  }
  
  public void setQuery(Query paramQuery)
  {
    this.mUi.setQuery(paramQuery);
  }
  
  public void showSuggestions(SuggestionList paramSuggestionList, int paramInt, boolean paramBoolean)
  {
    if (paramInt > 0)
    {
      Spanned localSpanned = (Spanned)paramSuggestionList.get(0).getSuggestionText1();
      this.mUi.setTextQueryCorrections(localSpanned);
    }
  }
  
  public void unfocusQueryAndHideKeyboard()
  {
    this.mUi.unfocusQueryAndHideKeyboard();
  }
  
  void updateHintText(UiMode paramUiMode, Context paramContext, SearchController paramSearchController, boolean paramBoolean)
  {
    String str;
    int i;
    if (isAttached())
    {
      str = "";
      UiMode localUiMode = UiMode.SUMMONS;
      i = 0;
      if (paramUiMode == localUiMode) {
        i = 0x0 | 0x8;
      }
      if (getEventBus().getQueryState().isHotwordSupported())
      {
        i |= 0x2;
        if (getEventBus().getQueryState().isHotwordActive()) {
          i |= 0x1;
        }
      }
      if (!paramSearchController.maybeInitializeGreco3DataManager()) {
        break label134;
      }
      i &= 0xFFFFFFFE;
    }
    for (;;)
    {
      if (getEventBus().getTtsState().isPlaying()) {
        i |= 0x20;
      }
      if (this.mHintFlags != i)
      {
        this.mUi.setExternalFlags(i, str, paramBoolean);
        this.mHintFlags = i;
      }
      return;
      label134:
      if (paramSearchController.shouldShowHotwordHint()) {
        i |= 0x4;
      }
      str = paramSearchController.getHotwordPrompt();
      if (paramSearchController.hasHotwordPrompt()) {
        i |= 0x10;
      }
    }
  }
  
  public void updateSearchPlate(boolean paramBoolean1, boolean paramBoolean2, UiMode paramUiMode, Context paramContext, SearchController paramSearchController)
  {
    VelvetEventBus localVelvetEventBus = getEventBus();
    QueryState localQueryState = localVelvetEventBus.getQueryState();
    UiState localUiState = localVelvetEventBus.getUiState();
    int i;
    if (localQueryState.getCommittedQuery().isTriggeredFromHotword())
    {
      i = 1;
      this.mUi.setSearchPlateMode(getSearchPlateMode(), i, paramBoolean2);
      if ((paramBoolean1) && (localUiState.takeShouldShowKeyboardChanged()))
      {
        if (!localUiState.shouldShowKeyboard()) {
          break label121;
        }
        if (localQueryState.isEditingQuery()) {
          this.mUi.focusQueryAndShowKeyboard();
        }
      }
    }
    for (;;)
    {
      this.mUi.showProgress(localVelvetEventBus.getUiState().shouldShowSpinner());
      updateHintText(paramUiMode, paramContext, paramSearchController, paramBoolean2);
      return;
      i = 0;
      break;
      label121:
      this.mUi.unfocusQueryAndHideKeyboard();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.SearchPlatePresenter
 * JD-Core Version:    0.7.0.1
 */