package com.google.android.velvet.presenter;

import android.os.Bundle;
import android.view.View;

import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.suggest.CachingPromoter;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.Suggestions;
import com.google.android.search.core.suggest.SuggestionsController;
import com.google.android.search.core.suggest.SuggestionsController.Listener;
import com.google.android.search.core.suggest.SuggestionsUi;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.SuggestionListView;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.main.TgPredictiveCardContainer;
import com.google.android.sidekick.shared.client.NowRemoteClient;
import com.google.android.sidekick.shared.client.PredictiveCardRefreshManager;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.ui.MainContentView;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;

public class SuggestFragmentPresenter
        extends TgPresenter {
    private final SearchConfig mConfig;
    private int mMaxSummonsDisplayed;
    private View mNoResultsView;
    private final SearchBoxLogging mSearchBoxLogging;
    private boolean mShowPredictive;
    private boolean mShowSuggest;
    private boolean mShowSummons;
    private boolean mSuggestionsInitPosted;
    private CachingPromoter mSummonsPromoter;
    private SuggestionListView mSummonsView;
    private UpdateSuggestionsTransaction mUpdateSuggestionsTransaction;
    private CachingPromoter mWebSuggestPromoter;
    private SuggestionListView mWebSuggestionsView;

    public SuggestFragmentPresenter(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, SearchConfig paramSearchConfig, SearchBoxLogging paramSearchBoxLogging, MainContentView paramMainContentView, PredictiveCardRefreshManager paramPredictiveCardRefreshManager, NowOptInSettings paramNowOptInSettings, TgPredictiveCardContainer paramTgPredictiveCardContainer, NowRemoteClient paramNowRemoteClient) {
        super("suggest", paramScheduledSingleThreadedExecutor, paramMainContentView, paramPredictiveCardRefreshManager, paramNowOptInSettings, paramTgPredictiveCardContainer, paramNowRemoteClient);
        this.mSearchBoxLogging = paramSearchBoxLogging;
        this.mConfig = paramSearchConfig;
    }

    private int getMaxSummons(UiMode paramUiMode) {
        switch (1. $SwitchMap$com$google$android$velvet$presenter$UiMode[paramUiMode.ordinal()])
        {
            default:
                return 2147483647;
        }
        return this.mConfig.getMaxDisplayedSummonsInResultsSuggest();
    }

    private void prepareSuggestionsUi() {
        if (this.mSuggestionsInitPosted) {
            if (this.mWebSuggestionsView != null) {
                resetChildDismissState(this.mWebSuggestionsView);
            }
            return;
        }
        this.mSuggestionsInitPosted = true;
        post(new PrepareSuggestionsUiTransaction(null));
    }

    private void updateSuggestionTypesToShow() {
        if (isAttached()) {
            if ((this.mShowSuggest) || (this.mShowSummons)) {
                prepareSuggestionsUi();
            }
            if (!this.mShowPredictive) {
                setPredictiveMode(1);
            }
            SuggestionsController localSuggestionsController = getVelvetPresenter().getSuggestionsController();
            localSuggestionsController.setSuggestionsViewEnabled(SuggestionsController.WEB_SUGGESTIONS, this.mShowSuggest);
            localSuggestionsController.setSuggestionsViewEnabled(SuggestionsController.SUMMONS, this.mShowSummons);
            localSuggestionsController.setMaxDisplayed(SuggestionsController.SUMMONS, this.mMaxSummonsDisplayed);
            this.mUpdateSuggestionsTransaction.maybePost();
            if (this.mShowPredictive) {
                setPredictiveMode(2);
            }
        }
    }

    protected boolean isNonPredictiveCardView(View paramView) {
        return (super.isNonPredictiveCardView(paramView)) || (paramView == this.mWebSuggestionsView) || (paramView == this.mSummonsView) || (paramView == this.mNoResultsView);
    }

    protected void onPostAttach(Bundle paramBundle) {
        super.onPostAttach(paramBundle);
        this.mUpdateSuggestionsTransaction = new UpdateSuggestionsTransaction(null);
        getVelvetPresenter().getSuggestionsController().addListener(this.mUpdateSuggestionsTransaction);
        updateSuggestionTypesToShow();
    }

    protected void onPreDetach() {
        super.onPreDetach();
        postRemoveAllViews();
        SuggestionsController localSuggestionsController = getVelvetPresenter().getSuggestionsController();
        localSuggestionsController.removeListener(this.mUpdateSuggestionsTransaction);
        localSuggestionsController.removeSuggestionsView(SuggestionsController.WEB_SUGGESTIONS);
        localSuggestionsController.removeSuggestionsView(SuggestionsController.SUMMONS);
        this.mUpdateSuggestionsTransaction = null;
        this.mWebSuggestionsView = null;
        this.mSummonsView = null;
        this.mNoResultsView = null;
        this.mSuggestionsInitPosted = false;
        this.mMaxSummonsDisplayed = 2147483647;
    }

    public void onViewsDismissed(PendingViewDismiss paramPendingViewDismiss) {
        super.onViewsDismissed(paramPendingViewDismiss);
        Iterator localIterator = paramPendingViewDismiss.getDismissedViews().iterator();
        while (localIterator.hasNext()) {
            if ((View) localIterator.next() == this.mWebSuggestionsView) {
                getVelvetPresenter().onWebSuggestionsDismissed();
            }
        }
    }

    public void update(UiModeManager paramUiModeManager, UiMode paramUiMode) {
        boolean bool1 = getEventBus().getQueryState().isZeroQuery();
        boolean bool2 = paramUiModeManager.shouldUsePredictiveInMode(paramUiMode, bool1);
        boolean bool3 = paramUiModeManager.shouldSuggestFragmentShowSuggestInMode(paramUiMode);
        boolean bool4 = paramUiModeManager.shouldSuggestFragmentShowSummonsInMode(paramUiMode, bool1);
        if ((bool2 != this.mShowPredictive) || (bool3 != this.mShowSuggest) || (bool4 != this.mShowSummons)) {
            this.mShowPredictive = bool2;
            this.mShowSuggest = bool3;
            this.mShowSummons = bool4;
            this.mMaxSummonsDisplayed = getMaxSummons(paramUiMode);
            this.mUpdateSuggestionsTransaction.updateHaveNoResults();
            updateSuggestionTypesToShow();
        }
    }

    private class PrepareSuggestionsUiTransaction
            extends MainContentPresenter.Transaction {
        private boolean mRegisteredWithController;

        private PrepareSuggestionsUiTransaction() {
        }

        public void commit(MainContentUi paramMainContentUi) {
            if (SuggestFragmentPresenter.this.isAttached()) {
                paramMainContentUi.getCardsView().addView(SuggestFragmentPresenter.this.mNoResultsView, 0);
                paramMainContentUi.getCardsView().addView(SuggestFragmentPresenter.this.mWebSuggestionsView, 1);
                paramMainContentUi.getCardsView().addView(SuggestFragmentPresenter.this.mSummonsView, 2);
            }
        }

        public boolean prepare() {
            if (!SuggestFragmentPresenter.this.isAttached()) {
            }
            do {
                return true;
                if (SuggestFragmentPresenter.this.mWebSuggestionsView == null) {
                    SuggestFragmentPresenter.access$402(SuggestFragmentPresenter.this, SuggestFragmentPresenter.this.getFactory().createWebSuggestionListView(SuggestFragmentPresenter.this, SuggestFragmentPresenter.this.getCardContainer()));
                    SuggestFragmentPresenter.access$502(SuggestFragmentPresenter.this, SuggestFragmentPresenter.this.getFactory().createWebSuggestionsCachingPromoter());
                    return false;
                }
                if (SuggestFragmentPresenter.this.mSummonsView == null) {
                    SuggestFragmentPresenter.access$602(SuggestFragmentPresenter.this, SuggestFragmentPresenter.this.getFactory().createSummonsListViewForSuggest(SuggestFragmentPresenter.this, SuggestFragmentPresenter.this.getCardContainer()));
                    SuggestFragmentPresenter.this.mSummonsView.setFooterClickListener(new View.OnClickListener() {
                        public void onClick(View paramAnonymousView) {
                            if (SuggestFragmentPresenter.this.isAttached()) {
                                SuggestFragmentPresenter.this.getVelvetPresenter().onSearchPhoneClickedInSuggest();
                            }
                        }
                    });
                    SuggestFragmentPresenter.access$702(SuggestFragmentPresenter.this, SuggestFragmentPresenter.this.getFactory().createSummonsCachingPromoter());
                    return false;
                }
                if (SuggestFragmentPresenter.this.mNoResultsView == null) {
                    SuggestFragmentPresenter.access$802(SuggestFragmentPresenter.this, VelvetFactory.createNoSummonsMessageView(SuggestFragmentPresenter.this, SuggestFragmentPresenter.this.getCardContainer()));
                    SuggestFragmentPresenter.this.mNoResultsView.setVisibility(8);
                    return false;
                }
            } while (this.mRegisteredWithController);
            SuggestionsController localSuggestionsController = SuggestFragmentPresenter.this.getVelvetPresenter().getSuggestionsController();
            if (SuggestFragmentPresenter.this.mUpdateSuggestionsTransaction != null) {
                localSuggestionsController.addSuggestionsView(SuggestionsController.WEB_SUGGESTIONS, SuggestFragmentPresenter.this.mWebSuggestPromoter, SuggestFragmentPresenter.this.mSearchBoxLogging.captureShownWebSuggestions(SuggestFragmentPresenter.this.mUpdateSuggestionsTransaction.getWebSuggestionUi()));
                localSuggestionsController.addSuggestionsView(SuggestionsController.SUMMONS, SuggestFragmentPresenter.this.mSummonsPromoter, SuggestFragmentPresenter.this.mUpdateSuggestionsTransaction.getSummonsSuggestionsUi());
            }
            this.mRegisteredWithController = true;
            return true;
        }
    }

    private class UpdateSuggestionsTransaction
            extends MainContentPresenter.Transaction
            implements SuggestionsController.Listener {
        private int mNumSummons;
        private int mNumWebSuggestions;
        private boolean mPosted;
        private boolean mReportNoSummons;
        private SuggestionList mSummons;
        private boolean mSummonsEnabled;
        private boolean mWebEnabled;
        private SuggestionList mWebSuggestions;

        private UpdateSuggestionsTransaction() {
        }

        private void maybePost() {
            if ((!this.mPosted) && (SuggestFragmentPresenter.this.mSuggestionsInitPosted)) {
                this.mPosted = true;
                SuggestFragmentPresenter.this.post(this);
            }
        }

        private boolean shouldShowNoResults() {
            return (!SuggestFragmentPresenter.this.mShowPredictive) && (!SuggestFragmentPresenter.this.mShowSuggest) && ((!SuggestFragmentPresenter.this.mShowSummons) || (this.mReportNoSummons));
        }

        private void updateHaveNoResults() {
            this.mReportNoSummons &= SuggestFragmentPresenter.this.mShowSummons;
        }

        private boolean updateHaveNoResults(SuggestionsController paramSuggestionsController, Object paramObject, boolean paramBoolean) {
            switch (paramSuggestionsController.getFetchState(paramObject)) {
                default:
                    return paramBoolean;
                case 2:
                    return true;
            }
            return false;
        }

        public void commit(MainContentUi paramMainContentUi) {
            this.mPosted = false;
            if ((SuggestFragmentPresenter.this.mNoResultsView != null) && (!shouldShowNoResults()) && (SuggestFragmentPresenter.this.mNoResultsView.getVisibility() != 8)) {
                SuggestFragmentPresenter.this.mNoResultsView.setVisibility(8);
                maybePost();
            }
            do {
                return;
                ImmutableList.Builder localBuilder = ImmutableList.builder();
                if ((this.mWebSuggestions != null) && (SuggestFragmentPresenter.this.mWebSuggestionsView != null)) {
                    SuggestFragmentPresenter.this.mWebSuggestionsView.showSuggestions(this.mWebSuggestions.getUserQuery().getQueryStringForSuggest(), this.mWebSuggestions.getSuggestions(), this.mNumWebSuggestions, this.mWebEnabled);
                    if (this.mWebEnabled) {
                        localBuilder.addAll(this.mWebSuggestions.getSuggestions().subList(0, this.mNumWebSuggestions));
                    }
                    this.mWebSuggestions = null;
                }
                if ((this.mSummons != null) && (SuggestFragmentPresenter.this.mSummonsView != null)) {
                    SuggestFragmentPresenter.this.mSummonsView.showSuggestions(this.mSummons.getUserQuery().getQueryStringForSuggest(), this.mSummons.getSuggestions(), this.mNumSummons, this.mSummonsEnabled);
                    if (this.mSummonsEnabled) {
                        localBuilder.addAll(this.mSummons.getSuggestions().subList(0, this.mNumSummons));
                    }
                    this.mSummons = null;
                }
                ImmutableList localImmutableList = localBuilder.build();
                if (SuggestFragmentPresenter.this.isAttached()) {
                    SuggestFragmentPresenter.this.getVelvetPresenter().getFormulationLogging().registerSuggestionsShown(localImmutableList, SearchBoxLogging.createSuggestionsLogInfo(localImmutableList), this.mWebEnabled);
                }
            }
            while ((SuggestFragmentPresenter.this.mNoResultsView == null) || (!shouldShowNoResults()));
            SuggestFragmentPresenter.this.mNoResultsView.setVisibility(0);
        }

        public SuggestionsUi getSummonsSuggestionsUi() {
            new SuggestionsUi() {
                public void showSuggestions(SuggestionList paramAnonymousSuggestionList, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                    SuggestFragmentPresenter.UpdateSuggestionsTransaction.access$1502(SuggestFragmentPresenter.UpdateSuggestionsTransaction.this, paramAnonymousSuggestionList);
                    SuggestFragmentPresenter.UpdateSuggestionsTransaction.access$1602(SuggestFragmentPresenter.UpdateSuggestionsTransaction.this, paramAnonymousInt);
                    SuggestFragmentPresenter.UpdateSuggestionsTransaction.access$1702(SuggestFragmentPresenter.UpdateSuggestionsTransaction.this, paramAnonymousBoolean);
                }
            };
        }

        public SuggestionsUi getWebSuggestionUi() {
            new SuggestionsUi() {
                public void showSuggestions(SuggestionList paramAnonymousSuggestionList, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                    SuggestFragmentPresenter.UpdateSuggestionsTransaction.access$1202(SuggestFragmentPresenter.UpdateSuggestionsTransaction.this, paramAnonymousSuggestionList);
                    SuggestFragmentPresenter.UpdateSuggestionsTransaction.access$1302(SuggestFragmentPresenter.UpdateSuggestionsTransaction.this, paramAnonymousInt);
                    SuggestFragmentPresenter.UpdateSuggestionsTransaction.access$1402(SuggestFragmentPresenter.UpdateSuggestionsTransaction.this, paramAnonymousBoolean);
                }
            };
        }

        public void onSuggestionsUpdated(SuggestionsController paramSuggestionsController, Suggestions paramSuggestions) {
            if ((SuggestFragmentPresenter.this.mShowSummons) && (updateHaveNoResults(paramSuggestionsController, SuggestionsController.SUMMONS, this.mReportNoSummons))) {
            }
            for (boolean bool = true; ; bool = false) {
                this.mReportNoSummons = bool;
                maybePost();
                return;
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.SuggestFragmentPresenter

 * JD-Core Version:    0.7.0.1

 */