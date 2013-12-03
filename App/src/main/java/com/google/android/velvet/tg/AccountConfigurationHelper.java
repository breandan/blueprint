package com.google.android.velvet.tg;

import android.accounts.Account;
import android.util.Log;

import com.google.android.gms.location.reporting.ReportingState;
import com.google.android.search.core.GmsClientWrapper.GmsFuture;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.GmsLocationReportingHelper;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.Configuration;
import com.google.geo.sidekick.Sidekick.FetchConfigurationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountConfigurationHelper {
    private static final String TAG = Tag.getTag(AccountConfigurationHelper.class);
    private final GmsLocationReportingHelper mGmsLocationReportingHelper;
    private final NowOptInSettings mNowOptInSettings;

    public AccountConfigurationHelper(GmsLocationReportingHelper paramGmsLocationReportingHelper, NowOptInSettings paramNowOptInSettings) {
        this.mGmsLocationReportingHelper = paramGmsLocationReportingHelper;
        this.mNowOptInSettings = paramNowOptInSettings;
    }

    public List<AnnotatedAccount> updateAccountConfigurations(Account[] paramArrayOfAccount) {
        ExtraPreconditions.checkNotMainThread();
        ArrayList localArrayList = Lists.newArrayListWithExpectedSize(paramArrayOfAccount.length);
        HashMap localHashMap = Maps.newHashMap();
        int i = paramArrayOfAccount.length;
        for (int j = 0; j < i; j++) {
            Account localAccount2 = paramArrayOfAccount[j];
            localHashMap.put(localAccount2, this.mGmsLocationReportingHelper.getReportingState(localAccount2));
        }
        int k = paramArrayOfAccount.length;
        int m = 0;
        if (m < k) {
            Account localAccount1 = paramArrayOfAccount[m];
            Sidekick.FetchConfigurationResponse localFetchConfigurationResponse = null;
            for (int n = 0; (n < 3) && (localFetchConfigurationResponse == null); n++) {
                localFetchConfigurationResponse = this.mNowOptInSettings.fetchAccountConfiguration(localAccount1);
            }
            if ((localFetchConfigurationResponse == null) || (!localFetchConfigurationResponse.hasConfiguration())) {
                Log.w(TAG, "config was null for: " + localAccount1.name);
            }
            for (; ; ) {
                m++;
                break;
                if (localFetchConfigurationResponse.getConfiguration().hasSidekickConfiguration()) {
                    break label211;
                }
                Log.w(TAG, "sidekick config was null for: " + localAccount1.name);
                BugLogger.record(11222718);
            }
            label211:
            Sidekick.Configuration localConfiguration = localFetchConfigurationResponse.getConfiguration();
            ReportingState localReportingState = (ReportingState) ((GmsClientWrapper.GmsFuture) localHashMap.get(localAccount1)).safeGet();
            if (this.mNowOptInSettings.userCanRunNow(localConfiguration, localAccount1, localReportingState) == 1) {
            }
            for (boolean bool = true; ; bool = false) {
                localArrayList.add(new AnnotatedAccount(localAccount1, bool));
                this.mNowOptInSettings.saveConfiguration(localConfiguration, localAccount1, localReportingState);
                break;
            }
        }
        return localArrayList;
    }

    public static class AnnotatedAccount {
        private final Account mAccount;
        private final boolean mNowEnabled;

        public AnnotatedAccount(Account paramAccount, boolean paramBoolean) {
            this.mAccount = paramAccount;
            this.mNowEnabled = paramBoolean;
        }

        public Account getAccount() {
            return this.mAccount;
        }

        public boolean isNowEnabled() {
            return this.mNowEnabled;
        }

        public String toString() {
            return this.mAccount.name;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.tg.AccountConfigurationHelper

 * JD-Core Version:    0.7.0.1

 */