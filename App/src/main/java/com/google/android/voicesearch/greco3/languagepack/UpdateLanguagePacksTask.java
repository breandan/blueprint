package com.google.android.voicesearch.greco3.languagepack;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.voicesearch.settings.Settings;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public class UpdateLanguagePacksTask
        implements LanguagePackUpdateController.Listener, Callable<Void> {
    private final LanguagePackUpdateController mController;
    private final GsaConfigFlags mGsaConfig;
    private final NetworkInformation mNetworkInformation;
    private final Settings mSettings;

    public UpdateLanguagePacksTask(LanguagePackUpdateController paramLanguagePackUpdateController, NetworkInformation paramNetworkInformation, Settings paramSettings, GsaConfigFlags paramGsaConfigFlags) {
        this.mController = paramLanguagePackUpdateController;
        this.mNetworkInformation = paramNetworkInformation;
        this.mSettings = paramSettings;
        this.mGsaConfig = paramGsaConfigFlags;
    }

    private void doUpdate(Collection<GstaticConfiguration.LanguagePack> paramCollection, Map<String, GstaticConfiguration.LanguagePack> paramMap) {
        String str = this.mSettings.getSpokenLocaleBcp47();
        if ((this.mSettings.hasEverUsedVoiceSearch()) && (!this.mController.isInstalled(str))) {
            Iterator localIterator2 = paramCollection.iterator();
            while (localIterator2.hasNext()) {
                GstaticConfiguration.LanguagePack localLanguagePack3 = (GstaticConfiguration.LanguagePack) localIterator2.next();
                if (TextUtils.equals(localLanguagePack3.getBcp47Locale(), str)) {
                    this.mController.doDownload(localLanguagePack3, false);
                }
            }
        }
        Iterator localIterator1 = paramMap.values().iterator();
        while (localIterator1.hasNext()) {
            GstaticConfiguration.LanguagePack localLanguagePack1 = (GstaticConfiguration.LanguagePack) localIterator1.next();
            if ((this.mController.isUsingDownloadedData(localLanguagePack1.getBcp47Locale())) || (TextUtils.equals(localLanguagePack1.getBcp47Locale(), str))) {
                GstaticConfiguration.LanguagePack localLanguagePack2 = this.mController.getUpgrade(localLanguagePack1);
                if (localLanguagePack2 != null) {
                    this.mController.doDownload(localLanguagePack2, false);
                }
            }
        }
    }

    private boolean permittedToDownloadNow() {
        switch (this.mSettings.getLanguagePacksAutoUpdate()) {
            default:
                Log.e("UpdateLanguagePacksTask", "Unexpected download strategy.");
            case 0:
                return false;
            case 1:
                return true;
        }
        return this.mNetworkInformation.isConnectedUnmetered();
    }

    private void updateIfPermitted() {
        if (permittedToDownloadNow()) {
            Log.i("UpdateLanguagePacksTask", "Checking for language pack updates.");
            if (this.mController.isInitialized()) {
                doUpdate(this.mController.getCompatibleLanguages().values(), this.mController.getInstalledLanguages());
                return;
            }
            this.mController.registerListener(this);
            this.mController.initialize();
            return;
        }
        Log.i("UpdateLanguagePacksTask", "Language pack update policy prevents downloading now.");
    }

    public Void call() {
        if ((!Feature.LANGUAGE_PACK_AUTO_DOWNLOAD.isEnabled()) || (!this.mGsaConfig.isLangagePackAutoUpdateEnabled())) {
        }
        while (!this.mNetworkInformation.isConnected()) {
            return null;
        }
        updateIfPermitted();
        return null;
    }

    public void onDownloadFailed(GstaticConfiguration.LanguagePack paramLanguagePack) {
        Log.w("UpdateLanguagePacksTask", "Download failed " + paramLanguagePack.getLanguagePackId());
    }

    public void onLanguageListChanged() {
        this.mController.unregisterListener(this);
        doUpdate(this.mController.getCompatibleLanguages().values(), this.mController.getInstalledLanguages());
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.UpdateLanguagePacksTask

 * JD-Core Version:    0.7.0.1

 */