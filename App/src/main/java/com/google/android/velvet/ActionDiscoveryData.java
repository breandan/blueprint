package com.google.android.velvet;

import android.text.TextUtils;

import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.util.Downloadable;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.NowOrLater;
import com.google.android.shared.util.UriLoader;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.majel.proto.ActionV2Protos.HelpAction;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public class ActionDiscoveryData
        extends Downloadable
        implements NowOrLater<ActionV2Protos.HelpAction> {
    private final SearchConfig mConfig;
    private final Set<Consumer<? super ActionV2Protos.HelpAction>> mConsumers = Sets.newHashSet();
    private ActionV2Protos.HelpAction mHelpData;
    @Nullable
    private final String mLanguage;
    private boolean mLoaded;
    private final String mLocaleBcp47;
    private final SearchSettings mSettings;

    public ActionDiscoveryData(SearchConfig paramSearchConfig, SearchSettings paramSearchSettings, UriLoader<byte[]> paramUriLoader, Executor paramExecutor, @Nullable String paramString) {
        super("Velvet.ActionDiscoveryData", paramUriLoader, paramExecutor);
        this.mConfig = paramSearchConfig;
        this.mSettings = paramSearchSettings;
        this.mLocaleBcp47 = paramString;
        this.mLanguage = getSupportedLanguage(paramString);
    }

    @Nullable
    private String getSupportedLanguage(@Nullable String paramString) {
        String str3;
        if (paramString == null) {
            str3 = null;
            return str3;
        }
        String[] arrayOfString1 = paramString.split("-", 2);
        String str1 = arrayOfString1[0];
        int i = arrayOfString1.length;
        String str2 = null;
        if (i > 1) {
            str2 = str1 + "_" + arrayOfString1[1].toLowerCase(Locale.US);
        }
        String[] arrayOfString2 = this.mConfig.getActionDiscoverySupportedLocales();
        int j = arrayOfString2.length;
        for (int k = 0; ; k++) {
            if (k >= j) {
                break label124;
            }
            str3 = arrayOfString2[k];
            if ((str3.equals(str2)) || (str3.equals(str1))) {
                break;
            }
        }
        label124:
        return null;
    }

    private boolean initialize() {
        if (this.mLanguage != null) {
            if (this.mSettings.isActionDiscoveryDataAvailable(this.mLanguage)) {
                initializeFromCached();
            }
            for (; ; ) {
                return false;
                maybeUpdateCache();
            }
        }
        return true;
    }

    protected byte[] getCachedData() {
        if (this.mLanguage != null) {
            return this.mSettings.getActionDiscoveryData(this.mLanguage);
        }
        return null;
    }

    protected String getCachedDataUrl() {
        if (this.mLanguage != null) {
            return this.mSettings.getActionDiscoveryDataUri(this.mLanguage);
        }
        return null;
    }

    protected byte[] getExternalData() {
        throw new UnsupportedOperationException("Always use cache.");
    }

    /* Error */
    public void getLater(Consumer<? super ActionV2Protos.HelpAction> paramConsumer) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 128	com/google/android/velvet/ActionDiscoveryData:mLoaded	Z
        //   6: ifne +11 -> 17
        //   9: aload_0
        //   10: aload_0
        //   11: invokespecial 130	com/google/android/velvet/ActionDiscoveryData:initialize	()Z
        //   14: putfield 128	com/google/android/velvet/ActionDiscoveryData:mLoaded	Z
        //   17: aload_0
        //   18: getfield 128	com/google/android/velvet/ActionDiscoveryData:mLoaded	Z
        //   21: ifeq +17 -> 38
        //   24: aload_1
        //   25: aload_0
        //   26: getfield 132	com/google/android/velvet/ActionDiscoveryData:mHelpData	Lcom/google/majel/proto/ActionV2Protos$HelpAction;
        //   29: invokeinterface 137 2 0
        //   34: pop
        //   35: aload_0
        //   36: monitorexit
        //   37: return
        //   38: aload_0
        //   39: getfield 37	com/google/android/velvet/ActionDiscoveryData:mConsumers	Ljava/util/Set;
        //   42: aload_1
        //   43: invokeinterface 142 2 0
        //   48: pop
        //   49: goto -14 -> 35
        //   52: astore_2
        //   53: aload_0
        //   54: monitorexit
        //   55: aload_2
        //   56: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	57	0	this	ActionDiscoveryData
        //   0	57	1	paramConsumer	Consumer<? super ActionV2Protos.HelpAction>
        //   52	4	2	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   2	17	52	finally
        //   17	35	52	finally
        //   38	49	52	finally
    }

    @Nullable
    protected String getLatestUrl() {
        String str1 = this.mLanguage;
        String str2 = null;
        if (str1 != null) {
            String str3 = this.mConfig.getActionDiscoveryDataUri();
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = this.mLanguage;
            str2 = String.format(str3, arrayOfObject);
        }
        return str2;
    }

    public synchronized ActionV2Protos.HelpAction getNow() {
            Preconditions.checkState(this.mLoaded);
            ActionV2Protos.HelpAction localHelpAction = this.mHelpData;
            return localHelpAction;
    }

    public synchronized boolean haveNow() {
            boolean bool = this.mLoaded;
            return bool;
    }

    public boolean isSupported(@Nullable String paramString) {
        return (TextUtils.equals(paramString, this.mLocaleBcp47)) || (TextUtils.equals(getSupportedLanguage(paramString), this.mLanguage));
    }

    protected void onDataLoaded(@Nullable byte[] paramArrayOfByte)
            throws Exception {
        if (paramArrayOfByte != null) {
        }
        for (; ; ) {
            try {
                this.mHelpData = ActionV2Protos.HelpAction.parseFrom(paramArrayOfByte);
                this.mLoaded = true;
                Iterator localIterator = this.mConsumers.iterator();
                if (!localIterator.hasNext()) {
                    break;
                }
                ((Consumer) localIterator.next()).consume(this.mHelpData);
                continue;
                this.mHelpData = null;
            } finally {
            }
        }
        this.mConsumers.clear();
    }

    protected void saveCached(byte[] paramArrayOfByte, String paramString) {
        Preconditions.checkNotNull(this.mLanguage);
        this.mSettings.setActionDiscoveryData(this.mLanguage, paramArrayOfByte);
        this.mSettings.setActionDiscoveryDataUri(this.mLanguage, paramString);
    }

    protected boolean useCache() {
        return true;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ActionDiscoveryData

 * JD-Core Version:    0.7.0.1

 */
