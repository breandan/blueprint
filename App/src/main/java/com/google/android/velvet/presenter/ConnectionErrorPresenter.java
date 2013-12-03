package com.google.android.velvet.presenter;

import android.os.Bundle;
import android.view.View;

import com.google.android.search.core.SearchError;
import com.google.android.search.core.state.QueryState;
import com.google.android.search.core.ui.ErrorView;
import com.google.android.search.shared.api.Query;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.ui.MainContentView;

public class ConnectionErrorPresenter
        extends MainContentPresenter {
    private SearchError mError;
    private Query mQuery;

    public ConnectionErrorPresenter(MainContentView paramMainContentView) {
        super("error", paramMainContentView);
    }

    private void onTryAgainClicked() {
        if ((isAttached()) && (this.mError != null)) {
            this.mError.retry(getEventBus().getQueryState(), this.mQuery);
        }
    }

    private View prepareErrorCard() {
        ErrorView localErrorView = VelvetFactory.createErrorCard(this, getCardContainer());
        localErrorView.setError(this.mQuery, this.mError);
        localErrorView.setTryAgainClickListener(new Runnable() {
            public void run() {
                ConnectionErrorPresenter.this.onTryAgainClicked();
            }
        });
        return localErrorView;
    }

    protected void onPostAttach(Bundle paramBundle) {
        QueryState localQueryState = getEventBus().getQueryState();
        this.mQuery = localQueryState.getCommittedQuery();
        this.mError = localQueryState.getError();
        View[] arrayOfView = new View[1];
        arrayOfView[0] = prepareErrorCard();
        postAddViews(arrayOfView);
    }

    protected void onPreDetach() {
        postRemoveAllViews();
        postResetScroll();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.ConnectionErrorPresenter

 * JD-Core Version:    0.7.0.1

 */