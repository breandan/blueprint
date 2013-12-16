package com.google.android.voicesearch.greco3.languagepack;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.search.core.Feature;
import com.google.android.speech.embedded.Greco3Container;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.Greco3Preferences;
import com.google.android.speech.embedded.LanguagePackUtils;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.velvet.VelvetApplication;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.io.Closeables;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LanguagePackUpdateController
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Map<String, Long> mActiveDownloads;
    private final Context mContext;
    private final Runnable mDataManagerInitializationCallback = new Runnable() {
        public void run() {
            LanguagePackUpdateController.this.finishInit();
            LanguagePackUpdateController.this.dispatchLanguageListChanged();
        }
    };
    private final int mDeviceClass;
    private final DownloadManager mDownloadManager;
    private final Greco3DataManager mGreco3DataManager;
    private volatile boolean mInitFinished;
    private volatile boolean mInitStarted;
    private BroadcastReceiver mLanguagePackDownloadReceiver;
    private long mLastManifestDownloadId = -9223372036854775808L;
    private final CopyOnWriteArrayList<Listener> mListeners;
    private final Runnable mOnDeleteRunnable = new Runnable() {
        public void run() {
            LanguagePackUpdateController.this.dispatchLanguageListChanged();
        }
    };
    private final Greco3Preferences mPreferenceStore;
    private final Settings mSettings;
    private BroadcastReceiver mStorageBroadcastReceiver;
    private boolean mStorageLow = false;
    private final String mVersionCode;

    LanguagePackUpdateController(Greco3DataManager paramGreco3DataManager, int paramInt, Settings paramSettings, DownloadManager paramDownloadManager, Context paramContext, Greco3Preferences paramGreco3Preferences, String paramString) {
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mDeviceClass = paramInt;
        this.mSettings = paramSettings;
        this.mDownloadManager = paramDownloadManager;
        this.mContext = paramContext;
        this.mPreferenceStore = paramGreco3Preferences;
        this.mVersionCode = paramString;
        this.mListeners = new CopyOnWriteArrayList();
        this.mActiveDownloads = this.mPreferenceStore.getActiveDownloads();
    }

    private static void checkCompatible(GstaticConfiguration.LanguagePack paramLanguagePack) {
        List localList = paramLanguagePack.getLanguagePackFormatVersionList();
        if (localList.isEmpty()) {
            throw new IllegalStateException("Language pack declares no format: " + paramLanguagePack.getLanguagePackId());
        }
        int i = ((Integer) localList.get(-1 + localList.size())).intValue();
        for (int j = 0; j < Greco3Container.SUPPORTED_FORMAT_VERSIONS.length; j++) {
            if (i == Greco3Container.SUPPORTED_FORMAT_VERSIONS[j]) {
                return;
            }
        }
        throw new IllegalStateException("Incompatible language pack: " + paramLanguagePack.getLanguagePackId());
    }

    private void checkStorage() {
        if (this.mStorageBroadcastReceiver == null) {
            this.mStorageBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
                    LanguagePackUpdateController.this.processStorageIntent(paramAnonymousIntent);
                }
            };
            IntentFilter localIntentFilter = new IntentFilter();
            localIntentFilter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
            localIntentFilter.addAction("android.intent.action.DEVICE_STORAGE_OK");
            Intent localIntent = this.mContext.registerReceiver(this.mStorageBroadcastReceiver, localIntentFilter);
            if (localIntent != null) {
                processStorageIntent(localIntent);
            }
        }
    }

    private void cleanUpDanglingDownloads() {
        int i = 0;
        int k;
        String str1;
        DownloadManager.Query localQuery;
        Cursor localCursor;
        synchronized (this.mActiveDownloads) {
            String[] arrayOfString = (String[]) this.mActiveDownloads.keySet().toArray(new String[this.mActiveDownloads.size()]);
            int j = arrayOfString.length;
            k = 0;
            if (k < j) {
                str1 = arrayOfString[k];
                if (TextUtils.isEmpty(str1)) {
                    VelvetStrictMode.logW("LanguagePackUpdateController", "Cannot find download for empty ID.");
                    break label483;
                }
                localQuery = new DownloadManager.Query();
                long[] arrayOfLong = new long[1];
                arrayOfLong[0] = ((Long) this.mActiveDownloads.get(str1)).longValue();
                localQuery.setFilterById(arrayOfLong);
                localCursor = null;
            }
        }
        for (; ; ) {
            String str3;
            GstaticConfiguration.LanguagePack localLanguagePack;
            try {
                localCursor = this.mDownloadManager.query(localQuery);
                localCursor.moveToFirst();
                if (localCursor.getCount() != 1) {
                    Log.w("LanguagePackUpdateController", "DownloadManager query failed for " + str1);
                    i = 1;
                    removeActiveDownload(str1);
                    Closeables.closeQuietly(localCursor);
                    break label483;
                    localObject1 =finally;
                    throw localObject1;
                } else {
                    Closeables.closeQuietly(localCursor);
                    int m = str1.lastIndexOf("-v");
                    if (m < 1) {
                        break label489;
                    }
                    int n = -2 + str1.length();
                    str2 = null;
                    if (m == n) {
                        break label489;
                    }
                    str3 = null;
                    i1 = 0;
                    if (str2 == null) {
                        str3 = str1.substring(0, m);
                        str4 = str1.substring(m + 2, str1.length());
                    }
                }
            } finally {
                String str4;
                int i2;
                Closeables.closeQuietly(localCursor);
            }
            try {
                i2 = Integer.parseInt(str4);
                i1 = i2;
            } catch (NumberFormatException localNumberFormatException) {
                File localFile;
                str2 = "Malformed language pack ID version.";
                i1 = 0;
                continue;
            }
            if (str2 == null) {
                localLanguagePack = (GstaticConfiguration.LanguagePack) getCompatibleLanguages().get(str3);
                if (localLanguagePack == null) {
                    str2 = "No compatible language pack.";
                }
            } else {
                if (str2 == null) {
                    break label483;
                }
                Log.w("LanguagePackUpdateController", "Init'd with bad active download " + str1 + " " + str2);
                i = 1;
                removeActiveDownload(str1);
                break label483;
            }
            if (localLanguagePack.getVersion() > i1) {
                str2 = "More recent version available.";
            } else if ((localLanguagePack.getVersion() == i1) && (isInstalled(str3))) {
                str2 = "Already installed.";
                continue;
                if (this.mActiveDownloads.isEmpty()) {
                    localFile = this.mContext.getExternalFilesDir("download_cache");
                    if (localFile != null) {
                        LanguagePackDownloadUtils.deleteExistingFilesOrCreateDirectory(localFile);
                    }
                }
                if (i != 0) {
                    BugLogger.record(10141154);
                }
                return;
                label483:
                k++;
                break;
                label489:
                str2 = "Malformed language pack ID.";
            }
        }
    }

    public static LanguagePackUpdateController create(Greco3Container paramGreco3Container, Settings paramSettings, DownloadManager paramDownloadManager, Context paramContext, Greco3Preferences paramGreco3Preferences) {
        LanguagePackUpdateController localLanguagePackUpdateController = new LanguagePackUpdateController(paramGreco3Container.getGreco3DataManager(), ((Integer) paramGreco3Container.getDeviceClassSupplier().get()).intValue(), paramSettings, paramDownloadManager, paramContext, paramGreco3Preferences, VelvetApplication.getVersionName());
        paramGreco3Preferences.registerOnSharedPreferenceChangeListener(localLanguagePackUpdateController);
        return localLanguagePackUpdateController;
    }

    private void dispatchDownloadFailed(String paramString) {
        Iterator localIterator1 = getCompatibleLanguages().values().iterator();
        GstaticConfiguration.LanguagePack localLanguagePack;
        do {
            boolean bool = localIterator1.hasNext();
            localObject = null;
            if (!bool) {
                break;
            }
            localLanguagePack = (GstaticConfiguration.LanguagePack) localIterator1.next();
        } while (!localLanguagePack.getLanguagePackId().equals(paramString));
        Object localObject = localLanguagePack;
        if (localObject != null) {
            Iterator localIterator2 = this.mListeners.iterator();
            while (localIterator2.hasNext()) {
                ((Listener) localIterator2.next()).onDownloadFailed(localObject);
            }
        }
        Log.w("LanguagePackUpdateController", "Failed download for unknown language pack " + paramString);
    }

    private void dispatchLanguageListChanged() {
        if (this.mInitFinished) {
            Iterator localIterator = this.mListeners.iterator();
            while (localIterator.hasNext()) {
                ((Listener) localIterator.next()).onLanguageListChanged();
            }
        }
        Log.w("LanguagePackUpdateController", "dispatchLanguageListChanged(): Not initialized.", new Throwable());
    }

    private void finishInit() {
        cleanUpDanglingDownloads();
        this.mInitFinished = true;
    }

    private GstaticConfiguration.LanguagePack getLanguagePackForActiveDownloadLocked(long paramLong) {
        Object localObject = null;
        Iterator localIterator = this.mActiveDownloads.keySet().iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            if (((Long) this.mActiveDownloads.get(str)).equals(Long.valueOf(paramLong))) {
                localObject = str;
            }
        }
        if (localObject != null) {
            return LanguagePackUtils.findById(localObject, this.mSettings.getConfiguration().getEmbeddedRecognitionResourcesList());
        }
        Log.e("LanguagePackUpdateController", "Download ID not found in active set :" + paramLong);
        return null;
    }

    private void processStorageIntent(@Nonnull Intent paramIntent) {
        String str = paramIntent.getAction();
        if ("android.intent.action.DEVICE_STORAGE_LOW".equals(str)) {
            this.mStorageLow = true;
        }
        while (!"android.intent.action.DEVICE_STORAGE_OK".equals(str)) {
            return;
        }
        this.mStorageLow = false;
    }

    private void registerLanguagePackDownloadReceiver() {
        this.mLanguagePackDownloadReceiver = new BroadcastReceiver() {
            public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
                String str = paramAnonymousIntent.getAction();
                ParcelableDownloadRequest localParcelableDownloadRequest = (ParcelableDownloadRequest) paramAnonymousIntent.getParcelableExtra("com.google.android.speech.embedded.download_manifest_request");
                if (localParcelableDownloadRequest != null) {
                    if (!"com.google.android.speech.embedded.download_manifest_complete".equals(str)) {
                        break label43;
                    }
                    LanguagePackUpdateController.this.removeActiveDownload(localParcelableDownloadRequest.getLanguagePackId());
                }
                label43:
                while (!"com.google.android.speech.embedded.download_manifest_failed".equals(str)) {
                    return;
                }
                LanguagePackUpdateController.this.removeActiveDownload(localParcelableDownloadRequest.getLanguagePackId());
                LanguagePackUpdateController.this.dispatchDownloadFailed(localParcelableDownloadRequest.getLanguagePackId());
            }
        };
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.google.android.speech.embedded.download_manifest_complete");
        localIntentFilter.addAction("com.google.android.speech.embedded.download_manifest_failed");
        this.mContext.registerReceiver(this.mLanguagePackDownloadReceiver, localIntentFilter);
    }

    protected ParcelableDownloadRequest buildDownloadRequest(GstaticConfiguration.LanguagePack paramLanguagePack, String paramString, boolean paramBoolean1, boolean paramBoolean2) {
        int i = this.mSettings.getLanguagePacksAutoUpdate();
        boolean bool;
        GstaticConfiguration.LanguagePack localLanguagePack;
        if ((paramBoolean1) || (i == 1)) {
            bool = true;
            localLanguagePack = (GstaticConfiguration.LanguagePack) getInstalledLanguages().get(paramLanguagePack.getBcp47Locale());
            if (localLanguagePack != null) {
                break label113;
            }
        }
        label113:
        for (int j = -1; ; j = localLanguagePack.getVersion()) {
            Uri localUri = buildDownloadUri(paramString, paramLanguagePack.getVersion(), j, i, paramBoolean1);
            return new ParcelableDownloadRequest(paramLanguagePack.getLanguagePackId(), localUri, paramBoolean2, bool, SpokenLanguageUtils.getDisplayName(this.mSettings.getConfiguration(), paramLanguagePack.getBcp47Locale()), "download_cache", LanguagePackUtils.buildDownloadFilename(paramLanguagePack), paramLanguagePack.getSizeKb());
            bool = false;
            break;
        }
    }

    protected Uri buildDownloadUri(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
        StringBuilder localStringBuilder = new StringBuilder().append("tv").append(':').append(paramInt1).append(';').append("pv").append(':').append(paramInt2).append(';').append("av").append(':').append(this.mVersionCode).append(';').append("f").append(':');
        if (paramBoolean) {
        }
        for (int i = 1; ; i = 0) {
            String str = i + ';' + "s" + ':' + paramInt3;
            return Uri.parse(paramString).buildUpon().appendQueryParameter("extraforlog", str).build();
        }
    }

    public void cancelDownload(GstaticConfiguration.LanguagePack paramLanguagePack) {
        for (; ; ) {
            long l;
            int j;
            try {
                boolean bool = this.mActiveDownloads.containsKey(paramLanguagePack.getLanguagePackId());
                if (!bool) {
                    return;
                }
                l = ((Long) this.mActiveDownloads.get(paramLanguagePack.getLanguagePackId())).longValue();
            } finally {
            }
            try {
                j = this.mDownloadManager.remove(new long[]{l});
                i = j;
            } catch (IllegalArgumentException localIllegalArgumentException) {
                Log.e("LanguagePackUpdateController", "Exception from DownloadManager ", localIllegalArgumentException);
                i = 0;
                continue;
            }
            if (i != 1) {
                Log.w("LanguagePackUpdateController", "(DownloadManager) Unexpected number of removals: " + i);
            }
            this.mActiveDownloads.remove(paramLanguagePack.getLanguagePackId());
            this.mPreferenceStore.removeActiveDownload(paramLanguagePack.getLanguagePackId());
        }
    }

    public void doDelete(GstaticConfiguration.LanguagePack paramLanguagePack) {
        this.mGreco3DataManager.deleteLanguage(paramLanguagePack, AsyncTask.THREAD_POOL_EXECUTOR, this.mOnDeleteRunnable);
    }

    public void doDownload(GstaticConfiguration.LanguagePack paramLanguagePack, boolean paramBoolean) {
        if (enqueueDownload(paramLanguagePack, paramBoolean) == -1) {
            dispatchDownloadFailed(paramLanguagePack.getLanguagePackId());
        }
    }

    public void dumpState(String paramString, PrintWriter paramPrintWriter) {
        paramPrintWriter.print(paramString);
        paramPrintWriter.println("LanguagePackUpdateController state:");
        if (!this.mGreco3DataManager.isInitialized()) {
            paramPrintWriter.print(paramString);
            paramPrintWriter.println("  Data manager not initialized.");
        }
        for (; ; ) {
            return;
            paramPrintWriter.print(paramString);
            paramPrintWriter.println("  Installed languages:");
            Map localMap = getInstalledLanguages();
            Iterator localIterator1 = localMap.values().iterator();
            while (localIterator1.hasNext()) {
                GstaticConfiguration.LanguagePack localLanguagePack2 = (GstaticConfiguration.LanguagePack) localIterator1.next();
                paramPrintWriter.print(paramString);
                paramPrintWriter.print("    " + localLanguagePack2.getLanguagePackId());
                if (isUsingDownloadedData(localLanguagePack2.getBcp47Locale())) {
                    paramPrintWriter.println(" downloaded.");
                } else {
                    paramPrintWriter.println(" pre-installed.");
                }
            }
            GstaticConfiguration.Configuration localConfiguration = this.mSettings.getConfigurationIfReady();
            if (localConfiguration == null) {
                paramPrintWriter.print(paramString);
                paramPrintWriter.println("  All compatible languages: Config not ready");
                return;
            }
            paramPrintWriter.print(paramString);
            paramPrintWriter.println("  All compatible languages:");
            Iterator localIterator2 = LanguagePackUtils.getCompatibleLanguagePacks(localMap, localConfiguration.getEmbeddedRecognitionResourcesList(), Greco3Container.SUPPORTED_FORMAT_VERSIONS, this.mDeviceClass).values().iterator();
            while (localIterator2.hasNext()) {
                GstaticConfiguration.LanguagePack localLanguagePack1 = (GstaticConfiguration.LanguagePack) localIterator2.next();
                paramPrintWriter.print(paramString);
                paramPrintWriter.println("    " + localLanguagePack1.getLanguagePackId());
            }
        }
    }

    public int enqueueDownload(GstaticConfiguration.LanguagePack paramLanguagePack, boolean paramBoolean) {
        int i = -1;
        for (; ; ) {
            try {
                checkCompatible(paramLanguagePack);
                checkStorage();
                if (this.mStorageLow) {
                    Log.w("LanguagePackUpdateController", "Skipping download (storage low): " + paramLanguagePack);
                    return i;
                }
                if (this.mActiveDownloads.containsKey(paramLanguagePack.getLanguagePackId())) {
                    Log.w("LanguagePackUpdateController", "Skipping download (already active): " + paramLanguagePack.getLanguagePackId());
                    i = 0;
                    continue;
                }
                long l1 = 0L;
                if ((Feature.INCREMENTAL_LANGUAGE_PACK_UPDATES.isEnabled()) && (paramLanguagePack.hasManifestUrl())) {
                    if (this.mLanguagePackDownloadReceiver == null) {
                        registerLanguagePackDownloadReceiver();
                    }
                    ParcelableDownloadRequest localParcelableDownloadRequest2 = buildDownloadRequest(paramLanguagePack, paramLanguagePack.getManifestUrl(), paramBoolean, false);
                    LanguagePackDownloadService.startFrom(this.mContext, localParcelableDownloadRequest2, paramLanguagePack.getBcp47Locale());
                    l1 = this.mLastManifestDownloadId;
                    this.mLastManifestDownloadId = (1L + l1);
                    j = 1;
                    if (j != 0) {
                        this.mActiveDownloads.put(paramLanguagePack.getLanguagePackId(), Long.valueOf(l1));
                        this.mPreferenceStore.addActiveDownload(paramLanguagePack.getLanguagePackId(), l1);
                        i = 1;
                    }
                } else {
                    boolean bool = paramLanguagePack.hasDownloadUrl();
                    if (!bool) {
                        break label295;
                    }
                    try {
                        ParcelableDownloadRequest localParcelableDownloadRequest1 = buildDownloadRequest(paramLanguagePack, paramLanguagePack.getDownloadUrl(), paramBoolean, true);
                        localParcelableDownloadRequest1.configure(this.mContext);
                        long l2 = this.mDownloadManager.enqueue(localParcelableDownloadRequest1);
                        l1 = l2;
                        j = 1;
                    } catch (IllegalArgumentException localIllegalArgumentException) {
                        Log.e("LanguagePackUpdateController", "Exception from DownloadManager", localIllegalArgumentException);
                        j = 0;
                    }
                    continue;
                }
                continue;
                Log.e("LanguagePackUpdateController", "Language pack has no download and/or no manifest URL, abort.");
            } finally {
            }
            label295:
            int j = 0;
        }
    }

    public Map<String, GstaticConfiguration.LanguagePack> getCompatibleLanguages() {
        return LanguagePackUtils.getCompatibleLanguagePacks(getInstalledLanguages(), this.mSettings.getConfiguration().getEmbeddedRecognitionResourcesList(), Greco3Container.SUPPORTED_FORMAT_VERSIONS, this.mDeviceClass);
    }

    public DownloadInfo getDownloadInfo(long paramLong) {
        DownloadManager.Query localQuery = new DownloadManager.Query();
        localQuery.setFilterById(new long[]{paramLong});
        Cursor localCursor = null;
        try {
            localCursor = this.mDownloadManager.query(localQuery);
            localCursor.moveToFirst();
            if (localCursor.getCount() != 1) {
                Log.w("LanguagePackUpdateController", "Querying download manager failed for ID :" + paramLong);
                return null;
            }
            GstaticConfiguration.LanguagePack localLanguagePack = getLanguagePackForActiveDownloadLocked(paramLong);
            DownloadInfo localDownloadInfo = null;
            if (localLanguagePack != null) {
                localDownloadInfo = new DownloadInfo(localCursor.getString(localCursor.getColumnIndex("local_filename")), localLanguagePack, localCursor.getInt(localCursor.getColumnIndex("status")), paramLong);
            }
            return localDownloadInfo;
        } finally {
            Closeables.closeQuietly(localCursor);
        }
    }

    Map<String, GstaticConfiguration.LanguagePack> getInstalledLanguages() {
        return this.mGreco3DataManager.getInstalledLanguages();
    }

    @Nullable
    public GstaticConfiguration.LanguagePack getUpgrade(GstaticConfiguration.LanguagePack paramLanguagePack) {
        GstaticConfiguration.LanguagePack localLanguagePack = (GstaticConfiguration.LanguagePack) getCompatibleLanguages().get(paramLanguagePack.getBcp47Locale());
        if (localLanguagePack == null) {
            Log.w("LanguagePackUpdateController", "Trying to upgrade " + paramLanguagePack.getLanguagePackId() + " but no " + "compatible language packs found.");
            if (!LanguagePackUtils.isCompatible(paramLanguagePack, Greco3Container.SUPPORTED_FORMAT_VERSIONS, this.mDeviceClass)) {
                Log.w("LanguagePackUpdateController", paramLanguagePack.getLanguagePackId() + " is not itself compatible.");
            }
            BugLogger.record(11028060);
            return null;
        }
        if (localLanguagePack.getVersion() > paramLanguagePack.getVersion()) {
        }
        for (; ; ) {
            return localLanguagePack;
            localLanguagePack = null;
        }
    }

    public void initialize() {
        if ((this.mInitFinished) || (this.mInitStarted)) {
            return;
        }
        this.mInitStarted = true;
        if (this.mGreco3DataManager.isInitialized()) {
            finishInit();
            return;
        }
        this.mGreco3DataManager.initialize(this.mDataManagerInitializationCallback);
    }

    public synchronized boolean isActiveDownload(GstaticConfiguration.LanguagePack paramLanguagePack) {
           return isActiveDownload(paramLanguagePack.getLanguagePackId());
    }

    boolean isActiveDownload(String paramString) {
        return this.mActiveDownloads.containsKey(paramString);
    }

    public boolean isInitialized() {
        return this.mInitFinished;
    }

    public boolean isInstalled(String paramString) {
        return getInstalledLanguages().containsKey(paramString);
    }

    public boolean isInstalledInSystemPartition(GstaticConfiguration.LanguagePack paramLanguagePack) {
        return this.mGreco3DataManager.isInstalledInSystemPartition(paramLanguagePack.getBcp47Locale());
    }

    public boolean isUsingDownloadedData(String paramString) {
        return this.mGreco3DataManager.isUsingDownloadedData(paramString);
    }

    public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString) {
        if ("g3_active_downloads".equals(paramString)) {
            dispatchLanguageListChanged();
        }
    }

    public void registerListener(Listener paramListener) {
        this.mListeners.add(paramListener);
    }

    public synchronized void removeActiveDownload(String paramString) {
            this.mActiveDownloads.remove(paramString);
            this.mPreferenceStore.removeActiveDownload(paramString);
    }

    public void unregisterListener(Listener paramListener) {
        this.mListeners.remove(paramListener);
    }

    public static final class DownloadInfo {
        final long downloadId;
        final String fileName;
        @Nullable
        final GstaticConfiguration.LanguagePack languagePack;
        final int status;

        DownloadInfo(String paramString, GstaticConfiguration.LanguagePack paramLanguagePack, int paramInt, long paramLong) {
            this.fileName = paramString;
            this.languagePack = paramLanguagePack;
            this.status = paramInt;
            this.downloadId = paramLong;
        }
    }

    public static abstract interface Listener {
        public abstract void onDownloadFailed(GstaticConfiguration.LanguagePack paramLanguagePack);

        public abstract void onLanguageListChanged();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.LanguagePackUpdateController

 * JD-Core Version:    0.7.0.1

 */
