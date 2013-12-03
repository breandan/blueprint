package com.google.android.voicesearch.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.android.sidekick.shared.util.FifeImageUrlUtil;
import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.fragments.executor.PlayMediaActionExecutor;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction.PriceTag;

import java.util.List;
import java.util.Locale;

public abstract class PlayMediaController<T extends Ui>
        extends AbstractCardController<PlayMediaAction, T> {
    private final FifeImageUrlUtil mFifeImageUrlUtil = new FifeImageUrlUtil();

    public PlayMediaController(CardController paramCardController) {
        super(paramCardController);
    }

    private String getFifeParams(int paramInt1, int paramInt2) {
        Locale localLocale = Locale.US;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(paramInt1);
        arrayOfObject[1] = Integer.valueOf(paramInt2);
        return String.format(localLocale, "w%d-h%d", arrayOfObject);
    }

    protected void appSelected(AppSelectionHelper.App paramApp) {
        boolean bool1 = true;
        ((PlayMediaAction) getVoiceAction()).selectApp(paramApp);
        Ui localUi = (Ui) getUi();
        localUi.setAppLabel(paramApp.getLabel());
        boolean bool2 = isPlayStoreLink(paramApp);
        boolean bool3;
        if (!bool2) {
            bool3 = bool1;
            localUi.showOwnedMode(bool3);
            if ((!bool2) || (!((PlayMediaAction) getVoiceAction()).isPreviewEnabled())) {
                break label86;
            }
        }
        for (; ; ) {
            localUi.showPreview(bool1);
            return;
            bool3 = false;
            break;
            label86:
            bool1 = false;
        }
    }

    public void initUi() {
        Ui localUi = (Ui) getUi();
        PlayMediaAction localPlayMediaAction = (PlayMediaAction) getVoiceAction();
        ActionV2Protos.PlayMediaAction localPlayMediaAction1 = localPlayMediaAction.getActionV2();
        List localList = localPlayMediaAction.getApps();
        AppSelectionHelper.App localApp = localPlayMediaAction.getSelectedApp();
        localUi.setSelectorApps(localList, localList.indexOf(localApp), isPlayStoreLink(localApp));
        if ((isPlayStoreLink(localApp)) && (localList.size() > 1)) {
            localUi.showVoiceOfGoogle();
        }
        appSelected(localApp);
        if (localPlayMediaAction1.hasItemRating()) {
        }
        for (double d = localPlayMediaAction1.getItemRating(); ; d = -1.0D) {
            localUi.showPlayStoreRating(d);
            if (localPlayMediaAction1.getItemPriceTagCount() > 0) {
                ActionV2Protos.PlayMediaAction.PriceTag localPriceTag = localPlayMediaAction1.getItemPriceTag(0);
                localUi.setPrice(localPriceTag.getPriceType(), localPriceTag.getPrice());
            }
            if ((localPlayMediaAction1.hasItemImage()) || (localPlayMediaAction1.hasItemImageUrl())) {
                setImage(localPlayMediaAction1);
            }
            return;
        }
    }

    protected boolean isPlayStoreLink(AppSelectionHelper.App paramApp) {
        return paramApp == ((PlayMediaAction) getVoiceAction()).getPlayStoreLink();
    }

    protected void onPostExecute() {
        if (!((PlayMediaAction) getVoiceAction()).getActionV2().getIsFromSoundSearch()) {
            clearCard();
        }
    }

    protected void setImage(int paramInt1, int paramInt2) {
        PlayMediaAction localPlayMediaAction = (PlayMediaAction) getVoiceAction();
        ActionV2Protos.PlayMediaAction localPlayMediaAction1 = localPlayMediaAction.getActionV2();
        Ui localUi = (Ui) getUi();
        if (localPlayMediaAction1.hasItemImage()) {
            Bitmap localBitmap = BitmapFactory.decodeByteArray(localPlayMediaAction1.getItemImage().toByteArray(), 0, 0);
            if ((localBitmap.getWidth() <= paramInt1) || (localBitmap.getHeight() <= paramInt2)) {
                localUi.showImageBitmap(localBitmap);
            }
        }
        while (!localPlayMediaAction1.hasItemImageUrl()) {
            return;
        }
        Uri localUri = localPlayMediaAction.getImageUri();
        String str1;
        String str2;
        if (localUri == null) {
            str1 = localPlayMediaAction1.getItemImageUrl();
            str2 = getFifeParams(paramInt1, paramInt2);
            if (!this.mFifeImageUrlUtil.isFifeHostedUrl(str1)) {
                break label152;
            }
            localUri = this.mFifeImageUrlUtil.setImageUriOptions(str2, Uri.parse(str1));
        }
        for (; ; ) {
            localPlayMediaAction.setImageUri(localUri);
            localUi.showImageUri(localUri);
            return;
            label152:
            if (str1.endsWith("?fife")) {
                localUri = Uri.parse(str1 + "=" + str2);
            } else {
                localUri = Uri.parse(str1);
            }
        }
    }

    protected void setImage(ActionV2Protos.PlayMediaAction paramPlayMediaAction) {
        ViewGroup.LayoutParams localLayoutParams = ((Ui) getUi()).getImageDimensions();
        if (localLayoutParams != null) {
            int i = localLayoutParams.height;
            setImage(localLayoutParams.width, i);
        }
    }

    public void start() {
        if (((PlayMediaActionExecutor) getActionExecutor()).setUpAppsForAction((PlayMediaAction) getVoiceAction())) {
            getCardController().updateCardDecision(getVoiceAction());
            showCard();
            return;
        }
        cancel();
        clearCard();
    }

    public static abstract interface Ui
            extends BaseCardUi, CountDownUi {
        public abstract ViewGroup.LayoutParams getImageDimensions();

        public abstract void setAppLabel(int paramInt);

        public abstract void setAppLabel(String paramString);

        public abstract void setPrice(String paramString1, String paramString2);

        public abstract void setSelectorApps(List<AppSelectionHelper.App> paramList, int paramInt, boolean paramBoolean);

        public abstract void showImageBitmap(Bitmap paramBitmap);

        public abstract void showImageDrawable(Drawable paramDrawable);

        public abstract void showImageUri(Uri paramUri);

        public abstract void showOwnedMode(boolean paramBoolean);

        public abstract void showPlayStoreRating(double paramDouble);

        public abstract void showPreview(boolean paramBoolean);

        public abstract void showVoiceOfGoogle();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.PlayMediaController

 * JD-Core Version:    0.7.0.1

 */