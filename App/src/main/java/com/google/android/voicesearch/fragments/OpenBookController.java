package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;

public class OpenBookController
        extends PlayMediaController<Ui> {
    public OpenBookController(CardController paramCardController) {
        super(paramCardController);
    }

    protected int getActionTypeLog() {
        return 32;
    }

    public void initUi() {
        super.initUi();
        Ui localUi = (Ui) getUi();
        ActionV2Protos.PlayMediaAction localPlayMediaAction = ((PlayMediaAction) getVoiceAction()).getActionV2();
        localUi.setTitle(localPlayMediaAction.getBookItem().getTitle());
        localUi.setAuthor(localPlayMediaAction.getBookItem().getAuthor());
        uiReady();
    }

    public static abstract interface Ui
            extends PlayMediaController.Ui {
        public abstract void setAuthor(String paramString);

        public abstract void setTitle(String paramString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.OpenBookController

 * JD-Core Version:    0.7.0.1

 */