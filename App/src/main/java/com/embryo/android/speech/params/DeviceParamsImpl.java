package com.embryo.android.speech.params;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;

public class DeviceParamsImpl
        implements DeviceParams {
    private final String mApplicationVersion;
    private final Context mContext;
    private final SearchConfig mSearchConfig;
    private final SearchSettings mSearchSettings;
    private final Supplier<String> mUserAgentSupplier;

    public DeviceParamsImpl(String paramString, Context paramContext, Supplier<String> paramSupplier, SearchConfig paramSearchConfig, SearchSettings paramSearchSettings) {
        if (paramContext.getApplicationContext() == paramContext) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkArgument(bool);
            this.mApplicationVersion = paramString;
            this.mContext = paramContext;
            this.mUserAgentSupplier = paramSupplier;
            this.mSearchConfig = paramSearchConfig;
            this.mSearchSettings = paramSearchSettings;
            return;
        }
    }

    public String getApplicationVersion() {
        return this.mApplicationVersion;
    }

    @Nullable
    public DisplayMetrics getDisplayMetrics() {
        WindowManager localWindowManager = (WindowManager) this.mContext.getSystemService("window");
        if (localWindowManager == null) {
            return null;
        }
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        localWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics;
    }

    public String getSearchDomainCountryCode() {
        return this.mSearchSettings.getSearchDomainCountryCode();
    }

    public String getUserAgent() {
        return (String) this.mUserAgentSupplier.get();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     DeviceParamsImpl

 * JD-Core Version:    0.7.0.1

 */