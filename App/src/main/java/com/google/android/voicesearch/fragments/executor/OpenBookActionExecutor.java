package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.helper.AccountHelper;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.util.AppSelectionHelper;

public class OpenBookActionExecutor
        extends PlayMediaActionExecutor {
    private final AccountHelper mAccountHelper;

    public OpenBookActionExecutor(IntentStarter paramIntentStarter, AppSelectionHelper paramAppSelectionHelper, AccountHelper paramAccountHelper) {
        super(paramIntentStarter, paramAppSelectionHelper);
        this.mAccountHelper = paramAccountHelper;
    }

    protected int getActionTypeLog() {
        return 32;
    }

    protected Intent getGoogleContentAppIntent(PlayMediaAction paramPlayMediaAction) {
        String str1 = paramPlayMediaAction.getActionV2().getBookItem().getBookAppUrl();
        Intent localIntent;
        if (TextUtils.isEmpty(str1)) {
            localIntent = null;
        }
        String str2;
        do {
            return localIntent;
            localIntent = new Intent("android.intent.action.VIEW").setPackage("com.google.android.apps.books").setData(Uri.parse(str1));
            str2 = this.mAccountHelper.getAccountName();
        } while (str2 == null);
        localIntent.putExtra("authAccount", str2);
        return localIntent;
    }

    protected Intent getOpenFromSearchIntent(PlayMediaAction paramPlayMediaAction) {
        return new Intent("android.media.action.TEXT_OPEN_FROM_SEARCH").putExtra("query", paramPlayMediaAction.getActionV2().getSuggestedQuery());
    }

    protected Intent getPreviewIntent(PlayMediaAction paramPlayMediaAction) {
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramPlayMediaAction.getActionV2().getItemPreviewUrl())).setPackage("com.google.android.apps.books").putExtra("books:addToMyEBooks", false);
        String str = this.mAccountHelper.getAccountName();
        if (str != null) {
            localIntent.putExtra("authAccount", str);
        }
        return localIntent;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.executor.OpenBookActionExecutor

 * JD-Core Version:    0.7.0.1

 */