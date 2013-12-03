package com.google.android.voicesearch.fragments;

import com.google.android.search.core.SearchError;
import com.google.android.voicesearch.CardController;

public class ErrorController
        extends AbstractCardController<SearchError, Ui> {
    public ErrorController(CardController paramCardController) {
        super(paramCardController);
    }

    protected void initUi() {
        SearchError localSearchError = (SearchError) getVoiceAction();
        ((Ui) getUi()).showError(getCardController().getQuery().getQueryString(), localSearchError);
    }

    protected void retry() {
        SearchError localSearchError = (SearchError) getVoiceAction();
        if (isAttached()) {
            getCardController().retryError(localSearchError);
        }
    }

    public static abstract interface Ui
            extends BaseCardUi {
        public abstract void showError(String paramString, SearchError paramSearchError);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.ErrorController

 * JD-Core Version:    0.7.0.1

 */