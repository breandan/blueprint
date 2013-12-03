package com.google.android.velvet;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider.UpdateCorporaService;

public final class VelvetUpgradeTasks {
    public static void maybeExecuteUpgradeTasks(Context paramContext, SearchSettings paramSearchSettings, SearchConfig paramSearchConfig, VelvetBackgroundTasks paramVelvetBackgroundTasks) {
        int i = 1;
        for (; ; ) {
            int j;
            int k;
            String str1;
            String str2;
            int m;
            try {
                j = paramSearchSettings.getLastRunVersion();
                k = VelvetApplication.getVersionCode();
                str1 = paramSearchSettings.getLastRunSystemBuild();
                str2 = Build.ID;
                if (j != -1) {
                    break label180;
                }
                paramVelvetBackgroundTasks.forceRunInterruptingOngoing("update_gservices_config");
            } finally {
            }
            if (!TextUtils.equals(str1, str2)) {
                if (m != 0) {
                    if (paramSearchConfig.isInternalIcingCorporaEnabled()) {
                        paramContext.startService(InternalIcingCorporaProvider.UpdateCorporaService.createForcedUpdateAllIntent(paramContext));
                    }
                    paramVelvetBackgroundTasks.forceRun("delete_local_search_history", 0L);
                    paramVelvetBackgroundTasks.forceRunInterruptingOngoing("refresh_auth_tokens");
                    paramVelvetBackgroundTasks.forceRunInterruptingOngoing("refresh_search_domain_and_cookies");
                    paramVelvetBackgroundTasks.forceRun("sync_gel_prefs", 0L);
                    paramSearchSettings.setLastRunVersion(k);
                }
                if (i != 0) {
                    if ((m == 0) && (paramSearchConfig.isInternalIcingCorporaEnabled())) {
                        paramContext.startService(InternalIcingCorporaProvider.UpdateCorporaService.createForcedUpdateAppsIntent(paramContext));
                    }
                    paramSearchSettings.setLastRunSystemBuild(str2);
                }
                return;
                m = 0;
            } else {
                i = 0;
                continue;
                label180:
                if (j != k) {
                    m = i;
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.VelvetUpgradeTasks

 * JD-Core Version:    0.7.0.1

 */