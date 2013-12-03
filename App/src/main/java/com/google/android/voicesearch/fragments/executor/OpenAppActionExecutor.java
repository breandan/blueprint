package com.google.android.voicesearch.fragments.executor;

import android.text.TextUtils;

import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.android.voicesearch.util.AppSelectionHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class OpenAppActionExecutor
        extends PlayMediaActionExecutor {
    public OpenAppActionExecutor(IntentStarter paramIntentStarter, AppSelectionHelper paramAppSelectionHelper) {
        super(paramIntentStarter, paramAppSelectionHelper);
    }

    protected int getActionTypeLog() {
        return 3;
    }

    protected AppSelectionHelper.App getDefaultApp(PlayMediaAction paramPlayMediaAction, Collection<AppSelectionHelper.App> paramCollection) {
        List localList = paramPlayMediaAction.getLocalResults();
        AppSelectionHelper.App localApp = paramPlayMediaAction.getPlayStoreLink();
        if ((!localList.isEmpty()) || (localApp != null)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            if (localList.size() <= 0) {
                break;
            }
            return (AppSelectionHelper.App) localList.get(0);
        }
        return localApp;
    }

    protected ImmutableList<AppSelectionHelper.App> getLocalApps(PlayMediaAction paramPlayMediaAction) {
        LinkedHashSet localLinkedHashSet = Sets.newLinkedHashSet();
        ActionV2Protos.PlayMediaAction localPlayMediaAction = paramPlayMediaAction.getActionV2();
        String str = paramPlayMediaAction.getActionV2().getSuggestedQuery();
        if (!TextUtils.isEmpty(str)) {
            localLinkedHashSet.addAll(getAppSelectionHelper().findActivities(str));
        }
        if (!str.equals(localPlayMediaAction.getAppItem().getName())) {
            localLinkedHashSet.addAll(getAppSelectionHelper().findActivities(localPlayMediaAction.getAppItem().getName()));
        }
        return ImmutableList.copyOf(localLinkedHashSet);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.executor.OpenAppActionExecutor

 * JD-Core Version:    0.7.0.1

 */