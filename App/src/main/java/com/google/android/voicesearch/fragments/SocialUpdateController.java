package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.SocialUpdateAction;

public class SocialUpdateController
        extends AbstractCardController<SocialUpdateAction, Ui> {
    public SocialUpdateController(CardController paramCardController) {
        super(paramCardController);
    }

    public void initUi() {
        ((Ui) getUi()).showEditPost(((SocialUpdateAction) getVoiceAction()).getSocialNetwork(), ((SocialUpdateAction) getVoiceAction()).getMessage());
    }

    public static abstract interface Ui
            extends BaseCardUi, CountDownUi {
        public abstract void showEditPost(SocialUpdateAction.SocialNetwork paramSocialNetwork, CharSequence paramCharSequence);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.SocialUpdateController

 * JD-Core Version:    0.7.0.1

 */