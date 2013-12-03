package com.google.android.voicesearch.fragments;

import android.text.TextUtils;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.PuntAction;

public class PuntController
        extends AbstractCardController<PuntAction, Ui> {
    public PuntController(CardController paramCardController) {
        super(paramCardController);
    }

    @Deprecated
    protected int getActionTypeLog() {
        return 25;
    }

    protected void initUi() {
        Ui localUi = (Ui) getUi();
        PuntAction localPuntAction = (PuntAction) getVoiceAction();
        if (localPuntAction.getMessage() != null) {
            localUi.setMessageText(localPuntAction.getMessage());
            if (TextUtils.isEmpty(localPuntAction.getQuery())) {
                break label88;
            }
            localUi.setQuery(localPuntAction.getQuery());
        }
        for (; ; ) {
            if (localPuntAction.getIntent() != null) {
                localUi.showActionButton(localPuntAction.getActionIcon(), localPuntAction.getActionLabel());
            }
            return;
            localUi.setMessageId(localPuntAction.getMessageId());
            break;
            label88:
            localUi.setNoQuery();
        }
    }

    public static abstract interface Ui
            extends BaseCardUi {
        public abstract void setMessageId(int paramInt);

        public abstract void setMessageText(CharSequence paramCharSequence);

        public abstract void setNoQuery();

        public abstract void setQuery(CharSequence paramCharSequence);

        public abstract void showActionButton(int paramInt1, int paramInt2);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.PuntController

 * JD-Core Version:    0.7.0.1

 */