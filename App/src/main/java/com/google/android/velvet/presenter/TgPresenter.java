package com.google.android.velvet.presenter;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.main.TgPredictiveCardContainer;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.client.EntryItemStack;
import com.google.android.sidekick.shared.client.NowCardsViewWrapper;
import com.google.android.sidekick.shared.client.NowCardsViewWrapper.CardsObserver;
import com.google.android.sidekick.shared.client.NowCardsViewWrapper.SearchPlateSticker;
import com.google.android.sidekick.shared.client.NowRemoteClient;
import com.google.android.sidekick.shared.client.NowRemoteClient.NowRemoteClientLock;
import com.google.android.sidekick.shared.client.PredictiveCardRefreshManager;
import com.google.android.sidekick.shared.client.PredictiveCardRefreshManager.PredictiveCardsPresenter;
import com.google.android.sidekick.shared.client.ScrollableCardView;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.ui.NowProgressBar;
import com.google.android.sidekick.shared.ui.PredictiveCardWrapper;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.ui.MainContentView;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.Entry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public abstract class TgPresenter
        extends MainContentPresenter
        implements PredictiveCardRefreshManager.PredictiveCardsPresenter, ScrollableCardView {
    static final long STALE_THRESHOLD_MILLIS = 900000L;
    private final TgPredictiveCardContainer mCardContainer;
    private boolean mDisableAnimationsOnNextPopulate = false;
    private View mGetGoogleNowCard;
    private int mMode = 0;
    private NowCardsViewWrapper mNowCardsView;
    private final NowOptInSettings mNowOptInSettings;
    private final NowRemoteClient mNowRemoteClient;
    private NowRemoteClient.NowRemoteClientLock mNowRemoteClientLock;
    private boolean mPaused = true;
    private Map<Long, Bundle> mPendingCardsState;
    private final PredictiveCardRefreshManager mRefreshManager;
    private int mScrollPosAfterNextPopulate = -1;
    private final ScheduledSingleThreadedExecutor mUiThread;

    public TgPresenter(String paramString, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, MainContentView paramMainContentView, PredictiveCardRefreshManager paramPredictiveCardRefreshManager, NowOptInSettings paramNowOptInSettings, TgPredictiveCardContainer paramTgPredictiveCardContainer, NowRemoteClient paramNowRemoteClient) {
        super(paramString, paramMainContentView);
        this.mUiThread = paramScheduledSingleThreadedExecutor;
        this.mRefreshManager = paramPredictiveCardRefreshManager;
        this.mNowOptInSettings = paramNowOptInSettings;
        this.mCardContainer = paramTgPredictiveCardContainer;
        this.mNowRemoteClient = paramNowRemoteClient;
    }

    private void buildPredictiveView() {
        if (this.mMode == 2) {
            this.mRefreshManager.registerPredictiveCardsListeners();
        }
        for (; ; ) {
            if (this.mMode == 2) {
                this.mRefreshManager.buildView();
            }
            return;
            this.mRefreshManager.unregisterPredictiveCardsListeners();
        }
    }

    private Sidekick.Entry getTargetEntry() {
        Bundle localBundle = getVelvetPresenter().getCommittedQuery().getExtras();
        if (localBundle != null) {
            byte[] arrayOfByte = localBundle.getByteArray("target_entry");
            if (arrayOfByte != null) {
                localBundle.remove("target_entry");
                return ProtoUtils.getEntryFromByteArray(arrayOfByte);
            }
        }
        return null;
    }

    public void addEntries(List<EntryItemStack> paramList, CardRenderingContext paramCardRenderingContext) {
        if (!isAttached()) {
            return;
        }
        this.mNowCardsView.addCards(getVelvetPresenter().getActivity(), paramList, paramCardRenderingContext, true, false, true, null, null, null, -1);
    }

    public void dismissEntry(Sidekick.Entry paramEntry, @Nullable Collection<Sidekick.Entry> paramCollection) {
        if (!isAttached()) {
            return;
        }
        this.mNowCardsView.dismissEntry(paramEntry, paramCollection);
    }

    public void doUserRefresh(boolean paramBoolean) {
        this.mNowCardsView.commitAllFeedback(false);
        postSmoothScrollTo(0);
        postRemovePredictiveCards();
        this.mRefreshManager.refreshCards(2, paramBoolean);
    }

    public IntentStarter getIntentStarter() {
        if (!isAttached()) {
            return null;
        }
        return getVelvetPresenter().getIntentStarter();
    }

    public SuggestionGridLayout getSuggestionGridLayout() {
        return (SuggestionGridLayout) getCardContainer();
    }

    public boolean isContextHeaderVisible() {
        return (isAttached()) && (getVelvetPresenter().isContextHeaderPresenterEnabled());
    }

    protected boolean isNonPredictiveCardView(View paramView) {
        return !(paramView instanceof PredictiveCardWrapper);
    }

    public boolean isPredictiveOnlyMode() {
        if (!isAttached()) {
            return false;
        }
        return getVelvetPresenter().isPredictiveOnlyMode();
    }

    public boolean isVisible() {
        return isAttached();
    }

    public void notifyCardVisible(PredictiveCardWrapper paramPredictiveCardWrapper) {
        paramPredictiveCardWrapper.getEntryCardViewAdapter().onViewVisibleOnScreen(this.mCardContainer);
    }

    public boolean onBackPressed() {
        boolean bool;
        if ((!isAttached()) || (this.mMode != 2)) {
            bool = false;
        }
        do {
            return bool;
            bool = this.mNowCardsView.isTrainingModeShowing();
        } while (!bool);
        post(new MainContentPresenter.Transaction() {
            public void commit(MainContentUi paramAnonymousMainContentUi) {
                if ((TgPresenter.this.isAttached()) && (TgPresenter.this.mNowCardsView != null)) {
                    TgPresenter.this.mNowCardsView.commitAllFeedback(true);
                }
            }
        });
        postRestoreSearchPlateStickiness();
        VelvetServices.get().getCoreServices().getUserInteractionLogger().logAnalyticsAction("BACK_BUTTON_CLOSE_FEEDBACK", null);
        return bool;
    }

    public void onPause() {
        this.mPaused = true;
        super.onPause();
        this.mRefreshManager.recordViewEndTimes();
        this.mRefreshManager.removeViewActionListeners();
        this.mRefreshManager.unregisterPullToRefreshHandler();
        if (this.mNowRemoteClientLock != null) {
            this.mNowRemoteClientLock.release();
            this.mNowRemoteClientLock = null;
        }
    }

    protected void onPostAttach(Bundle paramBundle) {
        NowCardsViewWrapper.CardsObserver local1 = new NowCardsViewWrapper.CardsObserver() {
            public void onCardsAdded() {
                TgPresenter.this.mRefreshManager.recordViewStartTimes();
                TgPresenter.this.mRefreshManager.stopProgressBar();
            }
        };
        this.mNowCardsView = new NowCardsViewWrapper(this.mUiThread, local1, (SuggestionGridLayout) getCardContainer(), getScrollViewControl(), getTrainingPeekView(), getTrainingPeekIcon(), getReminderPeekView(), getTrainingFooterIcon(), getRemindersFooterIcon(), this.mCardContainer);
        this.mNowCardsView.registerListeners();
        if (paramBundle != null) {
            if (paramBundle.getInt("predictive_mode", 0) != 2) {
                break label122;
            }
        }
        label122:
        for (boolean bool = true; ; bool = false) {
            this.mDisableAnimationsOnNextPopulate = bool;
            this.mScrollPosAfterNextPopulate = paramBundle.getInt("scroll_pos", -1);
            this.mPendingCardsState = this.mNowCardsView.getPredictiveCardsState(paramBundle);
            this.mRefreshManager.reset();
            return;
        }
    }

    protected void onPreDetach() {
        if (this.mGetGoogleNowCard != null) {
            View[] arrayOfView = new View[1];
            arrayOfView[0] = this.mGetGoogleNowCard;
            postRemoveViews(arrayOfView);
            this.mGetGoogleNowCard = null;
        }
        if (this.mNowCardsView != null) {
            this.mNowCardsView.unregisterListeners();
            this.mNowCardsView = null;
        }
        stopProgressBar();
    }

    public void onResume() {
        super.onResume();
        if (this.mNowRemoteClientLock == null) {
            this.mNowRemoteClientLock = this.mNowRemoteClient.newConnectionLock("Velvet.TgPresenter");
            this.mNowRemoteClientLock.acquire();
        }
        buildPredictiveView();
        if (isPredictiveOnlyMode()) {
            this.mRefreshManager.recordViewStartTimes();
        }
        this.mRefreshManager.addViewActionListeners();
        if (this.mMode == 2) {
            NowProgressBar localNowProgressBar = getProgressBar();
            if (localNowProgressBar != null) {
                this.mRefreshManager.registerPullToRefreshHandler(localNowProgressBar);
            }
        }
        this.mPaused = false;
    }

    public void onStop() {
        super.onStop();
        this.mRefreshManager.unregisterPredictiveCardsListeners();
        if (!getVelvetPresenter().isChangingConfigurations()) {
            this.mNowCardsView.commitAllFeedback(false);
        }
    }

    public void onViewsDismissed(PendingViewDismiss paramPendingViewDismiss) {
        this.mNowCardsView.onViewsDismissed(paramPendingViewDismiss);
    }

    public void populateView(@Nullable List<EntryItemStack> paramList, CardRenderingContext paramCardRenderingContext) {
        if (this.mMode != 2) {
        }
        while ((!isAttached()) || (!getVelvetPresenter().isNowEnabled())) {
            return;
        }
        if (this.mDisableAnimationsOnNextPopulate) {
            postSetLayoutAnimationsEnabled(false);
        }
        if ((paramList != null) && (!paramList.isEmpty())) {
            postAddPredictiveCards(paramList, paramCardRenderingContext, false, true, getTargetEntry(), this.mScrollPosAfterNextPopulate);
        }
        if (this.mDisableAnimationsOnNextPopulate) {
            postSetLayoutAnimationsEnabled(true);
        }
        this.mDisableAnimationsOnNextPopulate = false;
        this.mScrollPosAfterNextPopulate = -1;
    }

    void postAddPredictiveCards(final List<EntryItemStack> paramList, final CardRenderingContext paramCardRenderingContext, final boolean paramBoolean1, final boolean paramBoolean2, @Nullable final Sidekick.Entry paramEntry, final int paramInt) {
        final Map localMap = this.mPendingCardsState;
        this.mPendingCardsState = null;
        if (this.mGetGoogleNowCard == null) {
        }
        for (Object localObject = null; ; localObject = ImmutableList.of(this.mGetGoogleNowCard)) {
            this.mGetGoogleNowCard = null;
            post(new MainContentPresenter.Transaction() {
                public void commit(MainContentUi paramAnonymousMainContentUi) {
                    if ((TgPresenter.this.isAttached()) && (TgPresenter.this.mNowCardsView != null)) {
                        TgPresenter.this.mNowCardsView.addCards(TgPresenter.this.getVelvetPresenter().getActivity(), paramList, paramCardRenderingContext, paramBoolean1, paramBoolean2, true, localMap, this.val$viewsToRemove, paramEntry, paramInt);
                    }
                }
            });
            return;
        }
    }

    void postRemovePredictiveCards() {
        post(new MainContentPresenter.Transaction("removePredictiveCards") {
            public void commit(MainContentUi paramAnonymousMainContentUi) {
                SuggestionGridLayout localSuggestionGridLayout = paramAnonymousMainContentUi.getCardsView();
                for (int i = -1 + localSuggestionGridLayout.getChildCount(); i >= 0; i--) {
                    View localView = localSuggestionGridLayout.getChildAt(i);
                    if (!TgPresenter.this.isNonPredictiveCardView(localView)) {
                        localSuggestionGridLayout.removeGridItem(localView);
                    }
                }
            }
        });
    }

    public boolean preStackViewOrderChange(Iterable<View> paramIterable) {
        return this.mNowCardsView.commitFeedbackFromViews(paramIterable);
    }

    public void pulseTrainingIcon() {
        this.mNowCardsView.pulseTrainIcon();
    }

    public void removeCard(EntryCardViewAdapter paramEntryCardViewAdapter) {
        if (!isAttached()) {
            return;
        }
        this.mNowCardsView.removeCard(paramEntryCardViewAdapter);
    }

    public void resetView() {
        if (isAttached()) {
            postRemovePredictiveCards();
        }
        this.mDisableAnimationsOnNextPopulate = false;
        this.mScrollPosAfterNextPopulate = -1;
    }

    public void saveInstanceState(Bundle paramBundle, boolean paramBoolean) {
        paramBundle.putInt("predictive_mode", this.mMode);
        paramBundle.putInt("scroll_pos", getVelvetPresenter().getScrollViewControl().getScrollY());
        if (paramBoolean) {
            this.mNowCardsView.savePredictiveCardsState(paramBundle);
        }
    }

    void setCardStateForTest(int paramInt) {
        if ((paramInt >= 0) && (paramInt <= 2)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mMode = paramInt;
            return;
        }
    }

    public void setContextHeader(Drawable paramDrawable, boolean paramBoolean, @Nullable View.OnClickListener paramOnClickListener) {
        if (isAttached()) {
            getVelvetPresenter().setContextHeader(paramDrawable, paramBoolean, paramOnClickListener);
        }
    }

    protected void setPredictiveMode(int paramInt) {
        if (!getVelvetPresenter().isNowEnabled()) {
            this.mMode = 1;
            this.mRefreshManager.unregisterPredictiveCardsListeners();
            if (isAttached()) {
                postRemovePredictiveCards();
                if ((this.mGetGoogleNowCard == null) && (getVelvetPresenter().canRunTheGoogle()) && (!this.mNowOptInSettings.userHasDismissedGetGoogleNowButton()) && (getVelvetPresenter().isCurrentBackFragment(this))) {
                    this.mGetGoogleNowCard = getFactory().createGetGoogleNowView(this, getCardContainer());
                    View[] arrayOfView = new View[1];
                    arrayOfView[0] = this.mGetGoogleNowCard;
                    postAddViews(arrayOfView);
                }
            }
        }
        do {
            int i;
            do {
                do {
                    return;
                } while (paramInt == this.mMode);
                i = this.mMode;
                this.mMode = paramInt;
                if ((paramInt == 2) && (isAttached())) {
                    NowProgressBar localNowProgressBar = getProgressBar();
                    if (localNowProgressBar != null) {
                        this.mRefreshManager.registerPullToRefreshHandler(localNowProgressBar);
                    }
                }
            } while (!isAttached());
            if (i == 2) {
                this.mNowCardsView.commitAllFeedback(false);
                this.mRefreshManager.reset();
                this.mRefreshManager.unregisterPullToRefreshHandler();
            }
            postRemovePredictiveCards();
        } while (this.mPaused);
        buildPredictiveView();
    }

    public void showError(int paramInt) {
        if (isAttached()) {
            stopProgressBar();
            showToast(paramInt);
        }
    }

    public void showOptIn() {
    }

    public void showRemindersPeekAnimation() {
        this.mNowCardsView.flashReminderIcon();
    }

    public void showSinglePromoCard(final EntryCardViewAdapter paramEntryCardViewAdapter) {
        post(new MainContentPresenter.Transaction() {
            public void commit(MainContentUi paramAnonymousMainContentUi) {
                if ((TgPresenter.this.isAttached()) && (TgPresenter.this.mNowCardsView != null)) {
                    TgPresenter.this.mNowCardsView.showSinglePromoCard(TgPresenter.this.getVelvetPresenter().getActivity(), paramEntryCardViewAdapter);
                }
            }
        });
    }

    public void startProgressBar() {
        if (isAttached()) {
            NowProgressBar localNowProgressBar = getProgressBar();
            if (localNowProgressBar != null) {
                localNowProgressBar.start();
            }
        }
    }

    public boolean startWebSearch(String paramString, @Nullable Location paramLocation) {
        if (isAttached()) {
            VelvetEventBus localVelvetEventBus = getEventBus();
            if (localVelvetEventBus != null) {
                QueryState localQueryState = localVelvetEventBus.getQueryState();
                localQueryState.commit(localQueryState.get().fromPredictiveToWeb(paramString, paramLocation));
                return true;
            }
            Log.w("Velvet.TgPresenter", "Tried to start predictive web search but event bus was null");
        }
        for (; ; ) {
            return false;
            Log.w("Velvet.TgPresenter", "Tried to start predictive web search but TgPresenter was not attached");
        }
    }

    public void stopProgressBar() {
        if (isAttached()) {
            NowProgressBar localNowProgressBar = getProgressBar();
            if (localNowProgressBar != null) {
                localNowProgressBar.stop();
            }
        }
    }

    public void toggleBackOfCard(final EntryCardViewAdapter paramEntryCardViewAdapter) {
        post(new MainContentPresenter.Transaction() {
            public void commit(MainContentUi paramAnonymousMainContentUi) {
                if ((TgPresenter.this.isAttached()) && (TgPresenter.this.mNowCardsView != null)) {
                    TgPresenter.this.mNowCardsView.toggleBackOfCard(paramEntryCardViewAdapter, true, new TgPresenter.MainContentSearchPlateSticker(paramAnonymousMainContentUi, TgPresenter.this.getVelvetPresenter()));
                }
            }
        });
        postRestoreSearchPlateStickiness();
    }

    public void updateEntry(final Sidekick.Entry paramEntry1, final Sidekick.Entry paramEntry2, final Sidekick.Entry paramEntry3) {
        if (!isAttached()) {
            return;
        }
        post(new MainContentPresenter.Transaction() {
            public void commit(MainContentUi paramAnonymousMainContentUi) {
                if (TgPresenter.this.mNowCardsView != null) {
                    TgPresenter.this.mNowCardsView.updateEntry(paramEntry1, paramEntry2, paramEntry3);
                }
            }
        });
    }

    static class MainContentSearchPlateSticker
            implements NowCardsViewWrapper.SearchPlateSticker {
        private final VelvetPresenter mPresenter;
        private final MainContentUi mUi;

        MainContentSearchPlateSticker(MainContentUi paramMainContentUi, VelvetPresenter paramVelvetPresenter) {
            this.mUi = paramMainContentUi;
            this.mPresenter = paramVelvetPresenter;
        }

        public int getSearchPlateHeight() {
            return this.mPresenter.getSearchPlateHeight();
        }

        public void setSearchPlateStuckToScrollingView(boolean paramBoolean) {
            this.mUi.setSearchPlateStuckToScrollingView(paramBoolean);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.TgPresenter

 * JD-Core Version:    0.7.0.1

 */