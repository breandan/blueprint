package com.google.android.voicesearch.settings;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.GserviceWrapper;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Nullable;

class GStaticConfiguration {
    static final String BUNDLE_TIMESTAMP = "2013_10_04_22_22_03";
    private final ExecutorService mExecutorService;
    private final GserviceWrapper mGserviceWrapper;
    private final ArrayList<Settings.ConfigurationChangeListener> mListeners;
    private final Runnable mLoadRunnable;
    private final Object mLoadingLock = new Object();
    private final GsaPreferenceController mPrefController;
    private final Resources mResources;
    private final Object mScheduleLoadLock = new Object();
    private GstaticConfiguration.Configuration mConfiguration;
    private SharedPrefsData mCurrentPrefsData;
    private volatile Future<?> mLoadFuture;

    public GStaticConfiguration(GsaPreferenceController paramGsaPreferenceController, Resources paramResources, ExecutorService paramExecutorService, GserviceWrapper paramGserviceWrapper) {
        this.mPrefController = paramGsaPreferenceController;
        this.mResources = paramResources;
        this.mExecutorService = paramExecutorService;
        this.mGserviceWrapper = paramGserviceWrapper;
        this.mListeners = new ArrayList();
        this.mLoadRunnable = new Runnable() {
            public void run() {
                synchronized (GStaticConfiguration.this.mLoadingLock) {
                    if (GStaticConfiguration.this.mConfiguration != null) {
                        return;
                    }
                    GStaticConfiguration.SharedPrefsData localSharedPrefsData = GStaticConfiguration.this.maybeLoadFromSharedPrefs();
                    if (GStaticConfiguration.maybeOverrideFromResources(localSharedPrefsData, GStaticConfiguration.this.mResources)) {
                        GStaticConfiguration.this.writeToSharedPrefs(localSharedPrefsData);
                    }
                    GStaticConfiguration.this.setCurrentPrefs(localSharedPrefsData);
                    GStaticConfiguration.this.notifyListener();
                    return;
                }
            }
        };
    }

    static String getTimestampFromUrl(String paramString) {
        int i = paramString.lastIndexOf('/');
        if (i == -1) {
        }
        int j;
        do {
            return null;
            j = paramString.indexOf('_', i);
        } while (j == -1);
        int k = j + 1;
        try {
            String str = paramString.substring(k, k + "2013_10_04_22_22_03".length());
            return str;
        } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
        }
        return null;
    }

    private static boolean isPreferenceObsolete(String prefTimestamp) {
        if(prefTimestamp == null) {
            return true;
        }
        if(prefTimestamp.length() == "2013_10_04_22_22_03".length()) {
            boolean localboolean1 = preferenceObsolete;
            Log.i("GStaticConfiguration", "Bundled: 2013_10_04_22_22_03, pref: " + prefTimestamp + " pref obsolete" + "2013_10_04_22_22_03".compareTo(prefTimestamp) > 0 ? localboolean1 : 0x0);
            preferenceObsolete;
            return preferenceObsolete;
        }
        return true;
    }

    private static GstaticConfiguration.Configuration loadBundledConfig(Resources paramResources) {
        InputStream localInputStream = null;
        try {
            localInputStream = paramResources.openRawResource(2131165184);
            GstaticConfiguration.Configuration localConfiguration = GstaticConfiguration.Configuration.parseFrom(ByteStreams.toByteArray(localInputStream));
            return localConfiguration;
        } catch (IOException localIOException) {
            throw new RuntimeException("Unable to load from asset", localIOException);
        } finally {
            Closeables.closeQuietly(localInputStream);
        }
    }

    private static GstaticConfiguration.Configuration maybeMergeDebugOverride(SharedPrefsData paramSharedPrefsData) {
        GstaticConfiguration.Configuration base = paramSharedPrefsData.experimentData == null ? paramSharedPrefsData.configData : paramSharedPrefsData.experimentData;
        if(paramSharedPrefsData.overridenData == null) {
            return base;
        }
        try {
            return (GstaticConfiguration.Configuration)(GstaticConfiguration.Configuration)ProtoUtils.copyOf(base).mergeFrom(paramSharedPrefsData.overridenData.toByteArray());
        } catch(InvalidProtocolBufferMicroException e) {
            return base;
        }
    }

    private static boolean maybeOverrideFromResources(SharedPrefsData paramSharedPrefsData, Resources paramResources) {
        if ((!isPreferenceObsolete(paramSharedPrefsData.configTimestamp)) && (paramSharedPrefsData.configData != null)) {
            return false;
        }
        paramSharedPrefsData.configData = ((GstaticConfiguration.Configuration) Preconditions.checkNotNull(loadBundledConfig(paramResources)));
        paramSharedPrefsData.configTimestamp = "2013_10_04_22_22_03";
        return true;
    }

    @Nullable
    private static GstaticConfiguration.Configuration parseConfig(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        try {
            GstaticConfiguration.Configuration localConfiguration = GstaticConfiguration.Configuration.parseFrom(paramArrayOfByte);
            return localConfiguration;
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
        }
        return null;
    }

    private static void setOrClearConfigBytes(String paramString, GstaticConfiguration.Configuration paramConfiguration, SharedPreferencesExt.Editor paramEditor) {
        if (paramConfiguration != null) {
            paramEditor.putBytes(paramString, paramConfiguration.toByteArray());
            return;
        }
        paramEditor.remove(paramString);
    }

    private static void setOrClearString(String paramString1, String paramString2, SharedPreferencesExt.Editor paramEditor) {
        if (paramString2 != null) {
            paramEditor.putString(paramString1, paramString2);
            return;
        }
        paramEditor.remove(paramString1);
    }

    private byte[] download(HttpHelper paramHttpHelper, String paramString) {
        try {
            byte[] arrayOfByte = paramHttpHelper.rawGet(new HttpHelper.GetRequest(paramString), 0);
            return arrayOfByte;
        } catch (HttpHelper.HttpException localHttpException) {
            Log.w("GStaticConfiguration", "HTTPException error in updating the configuration");
            return null;
        } catch (IOException localIOException) {
            Log.w("GStaticConfiguration", "IOException error in updating the configuration");
        }
        return null;
    }

    private boolean downloadFromNetwork(SharedPrefsData paramSharedPrefsData, HttpHelper paramHttpHelper) {
        String str1 = this.mGserviceWrapper.getString("voice_search:gstatic_experiment_url");
        String str2 = paramSharedPrefsData.experimentUrl;
        int i = 0;
        if (str2 != null) {
            i = 0;
            if (str1 == null) {
                paramSharedPrefsData.experimentUrl = null;
                paramSharedPrefsData.experimentData = null;
                i = 1;
            }
        }
        String str3;
        if (str1 != null) {
            if (str1.equals(paramSharedPrefsData.experimentUrl)) {
                str3 = null;
            }
        }
        while (str3 == null) {
            if (i != 0) {
                maybeOverrideFromResources(paramSharedPrefsData, this.mResources);
                return true;
                str3 = str1;
                continue;
                str3 = getNewConfigurationUrl(paramSharedPrefsData);
            } else {
                return false;
            }
        }
        Preconditions.checkNotNull(str3);
        byte[] arrayOfByte = download(paramHttpHelper, str3);
        if (arrayOfByte == null) {
            Log.i("GStaticConfiguration", "Configuration not updated - error");
            return false;
        }
        GstaticConfiguration.Configuration localConfiguration;
        try {
            localConfiguration = GstaticConfiguration.Configuration.parseFrom(arrayOfByte);
            if (str1 != null) {
                paramSharedPrefsData.experimentUrl = str1;
                paramSharedPrefsData.experimentData = localConfiguration;
                return true;
            }
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
            Log.i("GStaticConfiguration", "Downloaded Configuration cannot be parsed", localInvalidProtocolBufferMicroException);
            return false;
        }
        paramSharedPrefsData.configTimestamp = getTimestampFromUrl(str3);
        paramSharedPrefsData.configData = localConfiguration;
        return true;
    }

    private String getNewConfigurationUrl(SharedPrefsData paramSharedPrefsData) {
        String str1 = this.mGserviceWrapper.getString("voice_search:gstatic_url");
        if ((str1 == null) || (str1.indexOf('/') == -1)) {
            Log.w("GStaticConfiguration", "No valid gstatic url found.");
            return null;
        }
        String str2 = getTimestampFromUrl(str1);
        if (str2 == null) {
            Log.w("GStaticConfiguration", "No valid timestamp in gstatic url.");
            return null;
        }
        String str3 = paramSharedPrefsData.configTimestamp;
        if (str3 == null) {
            Log.i("GStaticConfiguration", "Ignore gservice update: no configuration");
            return null;
        }
        if (str3.compareTo(str2) >= 0) {
            return null;
        }
        Log.i("GStaticConfiguration", "#getNewConfigurationUrl [pref=" + str3 + ", gservice=" + str2);
        return str1;
    }

    private GstaticConfiguration.Configuration maybeWaitForConfiguration() {
        synchronized (this.mLoadingLock) {
            if (this.mConfiguration != null) {
                GstaticConfiguration.Configuration localConfiguration4 = this.mConfiguration;
                return localConfiguration4;
            }
            EventLogger.recordLatencyStart(1);
            for (; ; ) {
                GstaticConfiguration.Configuration localConfiguration1 = this.mConfiguration;
                if (localConfiguration1 == null) {
                    try {
                        this.mLoadingLock.wait();
                    } catch (InterruptedException localInterruptedException) {
                        Log.e("GStaticConfiguration", "Interrupted waiting for configuration");
                        Thread.currentThread().interrupt();
                        GstaticConfiguration.Configuration localConfiguration3 = new GstaticConfiguration.Configuration();
                        return localConfiguration3;
                    }
                }
            }
        }
        EventLogger.recordClientEvent(3);
        GstaticConfiguration.Configuration localConfiguration2 = this.mConfiguration;
        return localConfiguration2;
    }

    private void notifyListener() {

        if (!Thread.holdsLock(this.mLoadingLock)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            Iterator localIterator = this.mListeners.iterator();
            while (localIterator.hasNext()) {
                ((Settings.ConfigurationChangeListener) localIterator.next()).onChange(this.mConfiguration);
            }
        }
    }

    private void scheduleNotifyListener() {
        this.mExecutorService.submit(new Runnable() {
            public void run() {
                GStaticConfiguration.this.notifyListener();
            }
        });
    }

    private void setCurrentPrefs(SharedPrefsData paramSharedPrefsData) {
        GstaticConfiguration.Configuration localConfiguration = maybeMergeDebugOverride(paramSharedPrefsData);
        synchronized (this.mLoadingLock) {
            this.mCurrentPrefsData = paramSharedPrefsData;
            this.mConfiguration = localConfiguration;
            this.mLoadingLock.notifyAll();
            return;
        }
    }

    public void addListener(Settings.ConfigurationChangeListener paramConfigurationChangeListener) {
        for (; ; ) {
            synchronized (this.mScheduleLoadLock) {
                if (this.mLoadFuture == null) {
                    bool = true;
                    Preconditions.checkState(bool);
                    this.mListeners.add(paramConfigurationChangeListener);
                    return;
                }
            }
            boolean bool = false;
        }
    }

    void asyncLoad() {
        for (; ; ) {
            synchronized (this.mScheduleLoadLock) {
                if (this.mListeners.size() > 0) {
                    bool = true;
                    Preconditions.checkState(bool);
                    if (this.mLoadFuture == null) {
                        this.mLoadFuture = this.mExecutorService.submit(this.mLoadRunnable);
                    }
                    return;
                }
            }
            boolean bool = false;
        }
    }

    GstaticConfiguration.Configuration getConfiguration() {
        return maybeWaitForConfiguration();
    }

    @Nullable
    GstaticConfiguration.Configuration getConfigurationIfReady() {
        synchronized (this.mLoadingLock) {
            GstaticConfiguration.Configuration localConfiguration = this.mConfiguration;
            return localConfiguration;
        }
    }

    public GstaticConfiguration.Configuration getOverrideConfiguration() {
        SharedPrefsData localSharedPrefsData = maybeLoadFromSharedPrefs();
        if (localSharedPrefsData.overridenData != null) {
            return localSharedPrefsData.overridenData;
        }
        return new GstaticConfiguration.Configuration().setId("override");
    }

    public void setOverrideConfiguration(@Nullable GstaticConfiguration.Configuration paramConfiguration) {
        SharedPrefsData localSharedPrefsData = maybeLoadFromSharedPrefs();
        localSharedPrefsData.overridenData = paramConfiguration;
        if (!maybeOverrideFromResources(localSharedPrefsData, this.mResources)) {
            writeToSharedPrefs(localSharedPrefsData);
        }
        setCurrentPrefs(localSharedPrefsData);
        scheduleNotifyListener();
    }

    public String getTimestamp() {
        return maybeLoadFromSharedPrefs().configTimestamp;
    }

    protected SharedPrefsData maybeLoadFromSharedPrefs() {
        synchronized (this.mLoadingLock) {
            if (this.mCurrentPrefsData != null) {
                SharedPrefsData localSharedPrefsData1 = new SharedPrefsData();
                localSharedPrefsData1.copyFrom(this.mCurrentPrefsData);
                return localSharedPrefsData1;
            }
            SharedPreferencesExt localSharedPreferencesExt = this.mPrefController.getMainPreferences();
            SharedPrefsData localSharedPrefsData2 = new SharedPrefsData();
            localSharedPrefsData2.configTimestamp = localSharedPreferencesExt.getString("gstatic_configuration_timestamp", null);
            localSharedPrefsData2.configData = parseConfig(localSharedPreferencesExt.getBytes("gstatic_configuration_data", null));
            localSharedPrefsData2.experimentUrl = localSharedPreferencesExt.getString("gstatic_configuration_expriment_url", null);
            localSharedPrefsData2.experimentData = parseConfig(localSharedPreferencesExt.getBytes("gstatic_configuration_experiment_data", null));
            localSharedPrefsData2.overridenData = parseConfig(localSharedPreferencesExt.getBytes("gstatic_configuration_override_1", null));
            return localSharedPrefsData2;
        }
    }

    public void update(HttpHelper paramHttpHelper) {
        ExtraPreconditions.checkNotMainThread();
        SharedPrefsData localSharedPrefsData = maybeLoadFromSharedPrefs();
        boolean bool = downloadFromNetwork(localSharedPrefsData, paramHttpHelper);
        setCurrentPrefs(localSharedPrefsData);
        if (bool) {
            writeToSharedPrefs(localSharedPrefsData);
            notifyListener();
        }
    }

    protected void writeToSharedPrefs(SharedPrefsData paramSharedPrefsData) {
        synchronized (this.mLoadingLock) {
            SharedPreferencesExt.Editor localEditor = this.mPrefController.getMainPreferences().edit();
            setOrClearString("gstatic_configuration_timestamp", paramSharedPrefsData.configTimestamp, localEditor);
            setOrClearConfigBytes("gstatic_configuration_data", paramSharedPrefsData.configData, localEditor);
            setOrClearString("gstatic_configuration_expriment_url", paramSharedPrefsData.experimentUrl, localEditor);
            setOrClearConfigBytes("gstatic_configuration_experiment_data", paramSharedPrefsData.experimentData, localEditor);
            setOrClearConfigBytes("gstatic_configuration_override_1", paramSharedPrefsData.overridenData, localEditor);
            localEditor.apply();
            return;
        }
    }

    static final class SharedPrefsData {
        @Nullable
        GstaticConfiguration.Configuration configData;
        @Nullable
        String configTimestamp;
        @Nullable
        GstaticConfiguration.Configuration experimentData;
        @Nullable
        String experimentUrl;
        @Nullable
        GstaticConfiguration.Configuration overridenData;

        public void copyFrom(SharedPrefsData paramSharedPrefsData) {
            this.configTimestamp = paramSharedPrefsData.configTimestamp;
            this.configData = paramSharedPrefsData.configData;
            this.experimentUrl = paramSharedPrefsData.experimentUrl;
            this.experimentData = paramSharedPrefsData.experimentData;
            this.overridenData = paramSharedPrefsData.overridenData;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.settings.GStaticConfiguration

 * JD-Core Version:    0.7.0.1

 */