package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.net.Uri;

import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.util.AppSelectionHelper;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;

public class PlayMusicActionExecutor
        extends PlayMediaActionExecutor {
    public PlayMusicActionExecutor(IntentStarter paramIntentStarter, AppSelectionHelper paramAppSelectionHelper) {
        super(paramIntentStarter, paramAppSelectionHelper);
    }

    protected int getActionTypeLog() {
        return 30;
    }

    protected Intent getGoogleContentAppIntent(PlayMediaAction paramPlayMediaAction) {
        ActionV2Protos.PlayMediaAction localPlayMediaAction = paramPlayMediaAction.getActionV2();
        if (localPlayMediaAction.hasSuggestedQueryForPlayMusic()) {
        }
        for (String str = localPlayMediaAction.getSuggestedQueryForPlayMusic(); ; str = localPlayMediaAction.getSuggestedQuery()) {
            return new Intent("android.media.action.MEDIA_PLAY_FROM_SEARCH").setPackage("com.google.android.music").putExtra("query", str);
        }
    }

    protected Intent getOpenFromSearchIntent(PlayMediaAction paramPlayMediaAction) {
        return new Intent("android.media.action.MEDIA_PLAY_FROM_SEARCH").putExtra("query", paramPlayMediaAction.getActionV2().getSuggestedQuery());
    }

    protected Intent getPreviewIntent(PlayMediaAction paramPlayMediaAction) {
        return new Intent("android.intent.action.VIEW").setPackage("com.android.vending").setData(Uri.parse(paramPlayMediaAction.getActionV2().getItemPreviewUrl()));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.executor.PlayMusicActionExecutor

 * JD-Core Version:    0.7.0.1

 */