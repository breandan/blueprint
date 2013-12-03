package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.StopNavigationAction;

public class StopNavigationController
        extends AbstractCardController<StopNavigationAction, Ui> {
    public StopNavigationController(CardController paramCardController) {
        super(paramCardController);
    }

    protected void initUi() {
        ((Ui) getUi()).setText("Stopping navigation");
    }

    public static abstract interface Ui
            extends BaseCardUi, CountDownUi {
        public abstract void setText(String paramString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.StopNavigationController

 * JD-Core Version:    0.7.0.1

 */