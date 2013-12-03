package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.helper.AccountHelper;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.util.AppSelectionHelper;

public class PlayMovieActionExecutor
        extends PlayMediaActionExecutor {
    private final AccountHelper mAccountHelper;

    public PlayMovieActionExecutor(IntentStarter paramIntentStarter, AppSelectionHelper paramAppSelectionHelper, AccountHelper paramAccountHelper) {
        super(paramIntentStarter, paramAppSelectionHelper);
        this.mAccountHelper = paramAccountHelper;
    }

    protected int getActionTypeLog() {
        return 31;
    }

    protected Intent getGoogleContentAppIntent(PlayMediaAction paramPlayMediaAction) {
        Intent localIntent;
        if (TextUtils.isEmpty(paramPlayMediaAction.getActionV2().getMovieItem().getMovieAppUrl())) {
            localIntent = null;
        }
        String str;
        do {
            return localIntent;
            localIntent = new Intent("com.google.android.videos.intent.action.VIEW").setPackage("com.google.android.videos").setData(Uri.parse(paramPlayMediaAction.getActionV2().getMovieItem().getMovieAppUrl()));
            str = this.mAccountHelper.getAccountName();
        } while (str == null);
        localIntent.putExtra("authAccount", str);
        return localIntent;
    }

    protected Intent getOpenFromSearchIntent(PlayMediaAction paramPlayMediaAction) {
        return new Intent("android.media.action.VIDEO_PLAY_FROM_SEARCH").putExtra("query", paramPlayMediaAction.getActionV2().getSuggestedQuery());
    }

    protected Intent getPreviewIntent(PlayMediaAction paramPlayMediaAction) {
        return new Intent("android.intent.action.VIEW", Uri.parse(paramPlayMediaAction.getActionV2().getItemPreviewUrl())).putExtra("force_fullscreen", true).setPackage("com.google.android.youtube");
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.executor.PlayMovieActionExecutor

 * JD-Core Version:    0.7.0.1

 */