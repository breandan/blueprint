package com.google.android.velvet.presenter;

import com.google.android.search.core.state.QueryState;

public class UiModeManager {
    private boolean mReadyToShowPredictive;
    private boolean mReadyToShowSuggest;
    private boolean mStartupComplete;

    private boolean zeroQueryFromPredictiveMode(QueryState paramQueryState) {
        return (paramQueryState.isZeroQuery()) && (paramQueryState.getCommittedQuery().isSentinel()) && (UiMode.fromSentinelQuery(paramQueryState.getCommittedQuery()).isPredictiveMode());
    }

    int getFooterStickiness(UiMode paramUiMode, boolean paramBoolean) {
        int i = 3;
        switch (1. $SwitchMap$com$google$android$velvet$presenter$UiMode[paramUiMode.ordinal()])
        {
            default:
                i = 0;
            case 5:
            case 4:
                do {
                    return i;
                } while (paramBoolean);
                return 4;
        }
        return 2;
    }

    int getSearchPlateStickiness(UiMode paramUiMode, boolean paramBoolean) {
        int i = 1;
        switch (1. $SwitchMap$com$google$android$velvet$presenter$UiMode[paramUiMode.ordinal()])
        {
            default:
                i = 0;
            case 1:
            case 5:
                return i;
            case 2:
            case 3:
                int j = 0;
                if (paramBoolean) {
                }
                for (; ; ) {
                    return j;
                    j = i;
                }
            case 4:
                return 4;
        }
        return 2;
    }

    boolean isStartupComplete() {
        return this.mStartupComplete;
    }

    void setReadyToShowPredictive() {
        this.mReadyToShowPredictive = true;
    }

    void setReadyToShowSuggest() {
        this.mReadyToShowSuggest = true;
    }

    void setStartupComplete() {
        this.mStartupComplete = true;
    }

    boolean shouldGoBackOnPreImeBackPress(UiMode paramUiMode, boolean paramBoolean1, boolean paramBoolean2) {
        return (paramUiMode.isSuggestMode()) && (paramBoolean2);
    }

    boolean shouldScrollToTopOnSearchBoxTouch(UiMode paramUiMode, boolean paramBoolean) {
        return (paramUiMode.isPredictiveMode()) || ((paramBoolean) && (paramUiMode.isSuggestMode()));
    }

    boolean shouldShowContextHeader(UiMode paramUiMode) {
        return paramUiMode.isPredictiveMode();
    }

    boolean shouldShowCorpusBarInMode(UiMode paramUiMode1, UiMode paramUiMode2, boolean paramBoolean) {
        return (paramUiMode1 == UiMode.RESULTS) || (paramUiMode1 == UiMode.SUMMONS) || ((paramUiMode1 == UiMode.CONNECTION_ERROR) && ((paramBoolean) || (shouldShowCorpusBarInMode(paramUiMode2, UiMode.NONE, true))));
    }

    boolean shouldShowTgFooterButton(UiMode paramUiMode, boolean paramBoolean1, boolean paramBoolean2) {
        boolean bool1 = false;
        if (paramBoolean2) {
            if (!paramUiMode.isPredictiveMode()) {
                boolean bool2 = paramUiMode.isSuggestMode();
                bool1 = false;
                if (bool2) {
                    bool1 = false;
                    if (!paramBoolean1) {
                    }
                }
            } else {
                bool1 = true;
            }
        }
        return bool1;
    }

    public boolean shouldStopQueryEditOnMainViewClick(UiMode paramUiMode, QueryState paramQueryState) {
        if (paramUiMode == UiMode.SUGGEST) {
            return zeroQueryFromPredictiveMode(paramQueryState);
        }
        return paramUiMode != UiMode.VOICESEARCH;
    }

    boolean shouldSuggestFragmentShowSuggestInMode(UiMode paramUiMode) {
        return ((paramUiMode == UiMode.SUGGEST) || (paramUiMode == UiMode.RESULTS_SUGGEST)) && (this.mReadyToShowSuggest);
    }

    boolean shouldSuggestFragmentShowSummonsInMode(UiMode paramUiMode, boolean paramBoolean) {
        return ((paramUiMode.isSuggestMode()) || (paramUiMode == UiMode.RESULTS_SUGGEST)) && (!paramBoolean) && (this.mStartupComplete);
    }

    public boolean shouldSwitchToSummonsOnWebSuggestDismiss(UiMode paramUiMode, QueryState paramQueryState) {
        UiMode localUiMode = UiMode.SUGGEST;
        boolean bool1 = false;
        if (paramUiMode == localUiMode) {
            boolean bool2 = zeroQueryFromPredictiveMode(paramQueryState);
            bool1 = false;
            if (!bool2) {
                bool1 = true;
            }
        }
        return bool1;
    }

    boolean shouldUsePredictiveInMode(UiMode paramUiMode, boolean paramBoolean) {
        return (paramUiMode == UiMode.PREDICTIVE) || (((paramUiMode == UiMode.SUGGEST) || (paramUiMode == UiMode.SUMMONS_SUGGEST)) && (paramBoolean) && (this.mReadyToShowPredictive));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.UiModeManager

 * JD-Core Version:    0.7.0.1

 */