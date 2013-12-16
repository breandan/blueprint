package com.google.android.velvet;

import android.content.Context;
import android.database.DataSetObservable;
import android.text.TextUtils;

import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.util.Downloadable;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.shared.util.UriLoader;
import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.wireless.voicesearch.proto.CorporaConfig.CorporaConfiguration2.Corpus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public class Corpora
        extends DataSetObservable {
    private final int mAppVersion;
    private final SearchConfig mConfig;
    private final Context mContext;
    private final Map<String, Corpus> mCorpora;
    private final Downloadable mDownloadableWebCorpora;
    private boolean mInitialized;
    private final SearchSettings mSettings;
    private final Map<Corpus, List<Corpus>> mSubCorpora;
    private final ScheduledSingleThreadedExecutor mUiExecutor;
    private CorporaConfig.CorporaConfiguration2 mWebCorporaConfig;

    public Corpora(Context paramContext, SearchConfig paramSearchConfig, SearchSettings paramSearchSettings, UriLoader<byte[]> paramUriLoader, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, Executor paramExecutor, int paramInt) {
        this.mConfig = paramSearchConfig;
        this.mSettings = paramSearchSettings;
        this.mUiExecutor = paramScheduledSingleThreadedExecutor;
        this.mAppVersion = paramInt;
        this.mDownloadableWebCorpora = new DownloadableWebCorpora(paramUriLoader, paramExecutor);
        this.mCorpora = Maps.newHashMap();
        this.mSubCorpora = Maps.newHashMap();
        this.mContext = paramContext;
    }

    private void addCorpora(final Corpus paramCorpus, final List<Corpus> paramList) {
        this.mUiExecutor.execute(new Runnable() {
            public void run() {
                synchronized (Corpora.this) {
                    Corpora.this.mCorpora.put(paramCorpus.getIdentifier(), paramCorpus);
                    Iterator localIterator = paramList.iterator();
                    if (localIterator.hasNext()) {
                        Corpus localCorpus = (Corpus) localIterator.next();
                        Corpora.this.mCorpora.put(localCorpus.getIdentifier(), localCorpus);
                    }
                }
                Corpora.this.mSubCorpora.put(paramCorpus, paramList);
                Corpora.this.notifyChanged();
                Corpora.this.notifyAll();
            }
        });
    }

    private void buildWebCorpora(CorporaConfig.CorporaConfiguration2 paramCorporaConfiguration2) {
        if ((this.mWebCorporaConfig == null) || (!this.mWebCorporaConfig.equals(paramCorporaConfiguration2))) {
            this.mWebCorporaConfig = paramCorporaConfiguration2;
            Object localObject = null;
            ArrayList localArrayList = Lists.newArrayList();
            Iterator localIterator = paramCorporaConfiguration2.getCorpusList().iterator();
            while (localIterator.hasNext()) {
                CorporaConfig.CorporaConfiguration2.Corpus localCorpus = (CorporaConfig.CorporaConfiguration2.Corpus) localIterator.next();
                if (shouldUseCorpus(localCorpus)) {
                    WebCorpus localWebCorpus = WebCorpus.createWebCorpus(localCorpus, (WebCorpus) localObject, this);
                    if (localObject == null) {
                        localObject = localWebCorpus;
                        Preconditions.checkState(((WebCorpus) localObject).isTopLevelWebCorpus());
                    } else {
                        localArrayList.add(localWebCorpus);
                    }
                }
            }
            addCorpora((Corpus) localObject, localArrayList);
        }
    }

    private boolean shouldUseCorpus(CorporaConfig.CorporaConfiguration2.Corpus paramCorpus) {
        if (paramCorpus.hasMinimumAppVersion()) {
            int j = paramCorpus.getMinimumAppVersion();
            if (this.mAppVersion >= j) {
            }
        }
        int i;
        do {
            return false;
            if (!paramCorpus.hasMaximumAppVersion()) {
                break;
            }
            i = paramCorpus.getMaximumAppVersion();
        } while (this.mAppVersion > i);
        return true;
    }

    public boolean areWebCorporaLoaded() {
        return getWebCorpus() != null;
    }

    public Corpus getCorpus(String paramString) {
        Corpus localCorpus = (Corpus) this.mCorpora.get(paramString);
        int i;
        if (localCorpus == null) {
            i = paramString.indexOf('.');
            if (i < 0) {
                break label66;
            }
        }
        label66:
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            localCorpus = (Corpus) this.mCorpora.get(paramString.substring(0, i));
            Preconditions.checkNotNull(localCorpus);
            return localCorpus;
        }
    }

    public Iterable<? extends Corpus> getSubCorpora(Corpus paramCorpus) {
        return (Iterable) this.mSubCorpora.get(paramCorpus);
    }

    public Corpus getSummonsCorpus() {
        return (Corpus) this.mCorpora.get("summons");
    }

    public WebCorpus getWebCorpus() {
        return (WebCorpus) this.mCorpora.get("web");
    }

    public synchronized void init() {
            if (!this.mInitialized) {
                addCorpora(new Corpus("summons", Util.getResourceUri(this.mContext, 2130837883), Util.getResourceUri(this.mContext, 2131363119), 2130968644, null, this, null), ImmutableList.of());
                this.mDownloadableWebCorpora.initializeFromCached();
                this.mInitialized = true;
            }
    }

    public void initializeDelayed() {
        this.mDownloadableWebCorpora.maybeUpdateCache();
    }

    public String resolveWebCorpusId(String paramString) {
        String str = "web." + paramString;
        if (this.mCorpora.containsKey(str)) {
            return str;
        }
        return "web";
    }

    private class DownloadableWebCorpora
            extends Downloadable {
        public DownloadableWebCorpora(Executor paramExecutor) {
            super(paramExecutor, localExecutor);
        }

        private CorporaConfig.CorporaConfiguration2 getDefaultCorpora()
                throws Exception {
            return CorporaConfig.CorporaConfiguration2.parseFrom(getExternalData());
        }

        protected byte[] getCachedData() {
            return Corpora.this.mSettings.getWebCorpora();
        }

        protected String getCachedDataUrl() {
            return Corpora.this.mSettings.getWebCorporaConfigUrl();
        }

        protected byte[] getExternalData() {
            return Util.loadBytesFromRawResource(Corpora.this.mContext.getResources(), 2131165185);
        }

        protected String getLatestUrl() {
            return Corpora.this.mConfig.getCorporaConfigUri();
        }

        protected void onDataLoaded(@Nullable byte[] paramArrayOfByte)
                throws Exception {
            if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
            }
            for (; ; ) {
                try {
                    CorporaConfig.CorporaConfiguration2 localCorporaConfiguration22 = CorporaConfig.CorporaConfiguration2.parseFrom(paramArrayOfByte);
                    localCorporaConfiguration21 = localCorporaConfiguration22;
                } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
                    localCorporaConfiguration21 = getDefaultCorpora();
                    continue;
                }
                Corpora.this.buildWebCorpora(localCorporaConfiguration21);
                return;
                CorporaConfig.CorporaConfiguration2 localCorporaConfiguration21 = getDefaultCorpora();
            }
        }

        protected void onDownloadException(String paramString, IllegalStateException paramIllegalStateException) {
            VelvetStrictMode.logW("Velvet.Corpora", "Invalid corpora data at URL: " + paramString);
        }

        protected void onLoadException(String paramString, Exception paramException) {
            if (paramString != null) {
                VelvetStrictMode.logW("Velvet.Corpora", "Could not parse data from " + paramString, paramException);
                return;
            }
            VelvetStrictMode.logW("Velvet.Corpora", "Could not parse data from settings", paramException);
        }

        protected void saveCached(byte[] paramArrayOfByte, String paramString) {
            Corpora.this.mSettings.setWebCorpora(paramArrayOfByte);
            Corpora.this.mSettings.setWebCorporaConfigUrl(paramString);
        }

        protected boolean useCache() {
            return !TextUtils.isEmpty(getLatestUrl());
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.Corpora

 * JD-Core Version:    0.7.0.1

 */
