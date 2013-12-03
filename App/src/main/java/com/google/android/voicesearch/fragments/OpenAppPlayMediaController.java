package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;

public class OpenAppPlayMediaController
        extends PlayMediaController<Ui> {
    public OpenAppPlayMediaController(CardController paramCardController) {
        super(paramCardController);
    }

    private void updateUi(AppSelectionHelper.App paramApp, ActionV2Protos.PlayMediaAction paramPlayMediaAction) {
        Ui localUi = (Ui) getUi();
        if (isPlayStoreLink(paramApp)) {
            localUi.setTitle(paramPlayMediaAction.getAppItem().getName());
        }
        for (; ; ) {
            setImage(paramPlayMediaAction);
            if (getCardController().getDisplayPrompt(getVoiceAction()) != null) {
                localUi.hideCountDownView();
            }
            return;
            localUi.setTitle(paramApp.getLabel());
            localUi.setAppLabel(2131363445);
        }
    }

    public void appSelected(AppSelectionHelper.App paramApp) {
        super.appSelected(paramApp);
        updateUi(paramApp, ((PlayMediaAction) getVoiceAction()).getActionV2());
    }

    protected int getActionTypeLog() {
        return 3;
    }

    public void initUi() {
        super.initUi();
        updateUi(((PlayMediaAction) getVoiceAction()).getSelectedApp(), ((PlayMediaAction) getVoiceAction()).getActionV2());
        uiReady();
    }

    protected void setImage(ActionV2Protos.PlayMediaAction paramPlayMediaAction) {
        AppSelectionHelper.App localApp = ((PlayMediaAction) getVoiceAction()).getSelectedApp();
        if ((isPlayStoreLink(localApp)) || (localApp.getPackageName().equals(paramPlayMediaAction.getAppItem().getPackageName()))) {
            super.setImage(paramPlayMediaAction);
            return;
        }
        ((Ui) getUi()).showImageDrawable(localApp.getIcon());
    }

    public static abstract interface Ui
            extends PlayMediaController.Ui {
        public abstract void hideCountDownView();

        public abstract void setTitle(String paramString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.OpenAppPlayMediaController

 * JD-Core Version:    0.7.0.1

 */