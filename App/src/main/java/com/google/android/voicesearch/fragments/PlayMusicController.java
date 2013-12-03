package com.google.android.voicesearch.fragments;

import android.view.View;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;

public class PlayMusicController
        extends PlayMediaController<Ui> {
    private boolean mImagePopulated;

    public PlayMusicController(CardController paramCardController) {
        super(paramCardController);
    }

    public void initUi() {
        Ui localUi = (Ui) getUi();
        ActionV2Protos.PlayMediaAction localPlayMediaAction = ((PlayMediaAction) getVoiceAction()).getActionV2();
        if (localPlayMediaAction.getMusicItem().hasSong()) {
            localUi.showSong(localPlayMediaAction.getMusicItem().getSong(), localPlayMediaAction.getMusicItem().getAlbum(), localPlayMediaAction.getMusicItem().getArtist());
        }
        for (; ; ) {
            localUi.showIsExplicit(localPlayMediaAction.getMusicItem().getIsExplicit());
            super.initUi();
            uiReady();
            return;
            if (localPlayMediaAction.getMusicItem().hasAlbum()) {
                localUi.showAlbum(localPlayMediaAction.getMusicItem().getAlbum(), localPlayMediaAction.getMusicItem().getArtist());
            } else if (localPlayMediaAction.getMusicItem().hasArtist()) {
                localUi.showArtist(localPlayMediaAction.getMusicItem().getArtist());
            } else {
                localUi.showMisc(localPlayMediaAction.getName());
            }
        }
    }

    protected void setImage(ActionV2Protos.PlayMediaAction paramPlayMediaAction) {
        Ui localUi = (Ui) getUi();
        ViewGroup.LayoutParams localLayoutParams = localUi.getImageDimensions();
        if (localLayoutParams == null) {
            return;
        }
        if (localLayoutParams.width <= 0) {
            localUi.setImageOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8) {
                    int i = paramAnonymousInt3 - paramAnonymousInt1;
                    int j = paramAnonymousInt4 - paramAnonymousInt2;
                    if ((i != 0) && (!PlayMusicController.this.mImagePopulated)) {
                        PlayMusicController.this.setImage(i, j);
                    }
                }
            });
            return;
        }
        setImage(localLayoutParams.width, localLayoutParams.height);
    }

    public static abstract interface Ui
            extends PlayMediaController.Ui {
        public abstract void setImageOnLayoutChangeListener(View.OnLayoutChangeListener paramOnLayoutChangeListener);

        public abstract void showAlbum(String paramString1, String paramString2);

        public abstract void showArtist(String paramString);

        public abstract void showIsExplicit(boolean paramBoolean);

        public abstract void showMisc(String paramString);

        public abstract void showSong(String paramString1, String paramString2, String paramString3);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.PlayMusicController

 * JD-Core Version:    0.7.0.1

 */