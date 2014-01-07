package com.embryo.android.voicesearch.settings;

import android.content.res.Resources;
import android.util.Log;

import com.embryo.android.shared.util.ExtraPreconditions;
import com.embryo.android.shared.util.ProtoUtils;
import com.embryo.android.voicesearch.logger.EventLogger;
import com.embryo.protobuf.micro.InvalidProtocolBufferMicroException;
import com.embryo.wireless.voicesearch.proto.GstaticConfiguration;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.GserviceWrapper;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Nullable;

class GStaticConfiguration {
    private final ExecutorService mExecutorService;
    private final ArrayList<com.embryo.android.voicesearch.settings.Settings.ConfigurationChangeListener> mListeners;
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
                }
            }
        };
    }

    private static boolean isPreferenceObsolete(String prefTimestamp) {
        return false;
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
        if (paramSharedPrefsData.overridenData == null) {
            return base;
        }
        try {
            return (GstaticConfiguration.Configuration) ProtoUtils.copyOf(base).mergeFrom(paramSharedPrefsData.overridenData.toByteArray());
        } catch (InvalidProtocolBufferMicroException e) {
            return base;
        }
    }

    private static boolean maybeOverrideFromResources(SharedPrefsData paramSharedPrefsData, Resources paramResources) {
        if ((!isPreferenceObsolete(paramSharedPrefsData.configTimestamp)) && (paramSharedPrefsData.configData != null)) {
            return false;
        }
        paramSharedPrefsData.configData = Preconditions.checkNotNull(loadBundledConfig(paramResources));
        paramSharedPrefsData.configTimestamp = "2013_10_04_22_22_03";
        return true;
    }

    @Nullable
    private static GstaticConfiguration.Configuration parseConfig(byte[] configData) {
        if (configData == null) {
            return null;
        }
        try {
            return GstaticConfiguration.Configuration.parseFrom(configData);
        } catch (InvalidProtocolBufferMicroException e) {
            return null;
        }
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

    private GstaticConfiguration.Configuration maybeWaitForConfiguration() {
        synchronized (mLoadingLock) {
            if (mConfiguration != null) {
                return mConfiguration;
            } else {
                EventLogger.recordLatencyStart(0x1);
                try {
                    mLoadingLock.wait();
                } catch (InterruptedException e) {
                    Log.e("GStaticConfiguration", "Interrupted waiting for configuration");
                    Thread.currentThread().interrupt();
                    return new GstaticConfiguration.Configuration();
                }
                EventLogger.recordClientEvent(0x3);
                return mConfiguration;
            }
        }
    }

    private void notifyListener() {
        ExtraPreconditions.checkNotMainThread();
        Preconditions.checkState((!Thread.holdsLock(mLoadingLock)));
        for (Settings.ConfigurationChangeListener listener : mListeners) {
            listener.onChange(mConfiguration);
        }
    }

    private void setCurrentPrefs(SharedPrefsData paramSharedPrefsData) {
        GstaticConfiguration.Configuration localConfiguration = maybeMergeDebugOverride(paramSharedPrefsData);
        synchronized (this.mLoadingLock) {
            this.mCurrentPrefsData = paramSharedPrefsData;
            this.mConfiguration = localConfiguration;
            this.mLoadingLock.notifyAll();
        }
    }

    public void addListener(com.embryo.android.voicesearch.settings.Settings.ConfigurationChangeListener listener) {
        synchronized (mScheduleLoadLock) {
            Preconditions.checkState((mLoadFuture == null));
            mListeners.add(listener);
        }
    }

    void asyncLoad() {
        synchronized (mScheduleLoadLock) {
            Preconditions.checkState((mListeners.size() > 0));
            if (mLoadFuture == null) {
                mLoadFuture = mExecutorService.submit(mLoadRunnable);
            }
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

    protected SharedPrefsData maybeLoadFromSharedPrefs() {
        synchronized (this.mLoadingLock) {
            if (this.mCurrentPrefsData != null) {
                SharedPrefsData localSharedPrefsData1 = new SharedPrefsData();
                localSharedPrefsData1.copyFrom(this.mCurrentPrefsData);
                return localSharedPrefsData1;
            }
            return new SharedPrefsData();
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

 * Qualified Name:     GStaticConfiguration

 * JD-Core Version:    0.7.0.1

 */