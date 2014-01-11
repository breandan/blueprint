package com.embryo.android.speech.embedded;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

public class Greco3DataManager {
    static final File SYSTEM_DATA_DIR = new File("/system/usr/srec");
    private static final FileFilter DIRECTORY_FILTER = new FileFilter() {
        public boolean accept(File paramAnonymousFile) {
            return paramAnonymousFile.isDirectory();
        }
    };
    final List<Runnable> mInitializationCallbacks;
    @Nullable
    private final File mCompiledGrammarRoot;
    private final Context mContext;
    @Nullable
    private final Greco3Preferences mGreco3Prefs;
    private final ImmutableList<File> mSearchPaths;
    private final int[] mSupportedFormatVersions;
    private final Executor mUiThread;
    private final Executor mUpdateExecutor;
    private Map<String, com.embryo.android.speech.embedded.LocaleResourcesImpl> mAvailableLanguages;
    private int mNumUpdatesInProgress;
    private PathDeleter mPathDeleter;

    public Greco3DataManager(Context paramContext, @Nullable Greco3Preferences paramGreco3Preferences, int[] paramArrayOfInt, ImmutableList<File> paramImmutableList, @Nullable File paramFile, Executor paramExecutor1, Executor paramExecutor2) {
        this.mContext = paramContext;
        this.mGreco3Prefs = paramGreco3Preferences;
        this.mSupportedFormatVersions = paramArrayOfInt;
        this.mSearchPaths = paramImmutableList;
        Iterator localIterator = this.mSearchPaths.iterator();
        while (localIterator.hasNext()) {
            Preconditions.checkState(((File) localIterator.next()).isAbsolute());
        }
        this.mCompiledGrammarRoot = paramFile;
        this.mUpdateExecutor = paramExecutor1;
        this.mUiThread = paramExecutor2;
        this.mNumUpdatesInProgress = 0;
        this.mAvailableLanguages = null;
        this.mInitializationCallbacks = Lists.newArrayList();
    }

    public Greco3DataManager(Context paramContext, Greco3Preferences paramGreco3Preferences, int[] paramArrayOfInt, Executor paramExecutor1, Executor paramExecutor2) {
        this(paramContext, paramGreco3Preferences, paramArrayOfInt, getSearchPathList(new File[2]), new File(paramContext.getCacheDir(), "g3_grammars"), paramExecutor1, paramExecutor2);
    }

    private static ImmutableList<File> getSearchPathList(File... paramVarArgs) {
        ArrayList localArrayList = new ArrayList();
        int i = paramVarArgs.length;
        for (int j = 0; j < i; j++) {
            File localFile = paramVarArgs[j];
            if (localFile != null) {
                localArrayList.add(localFile);
            }
        }
        return ImmutableList.copyOf(localArrayList);
    }

    private void doLanguageDelete(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack paramLanguagePack, final Runnable paramRunnable) {
        File localFile = getOutputDirForLocale(paramLanguagePack.getBcp47Locale());
        this.mPathDeleter.delete(localFile, true, new Runnable() {
            public void run() {
                Greco3DataManager.this.blockingUpdateResources(true);
                if (paramRunnable != null) {
                    Greco3DataManager.this.mUiThread.execute(paramRunnable);
                }
            }
        });
    }

    private File getOutputDirForLocale(String paramString) {
        return new File(this.mContext.getDir("g3_models", 0), paramString);
    }

    private void handleLocale(File paramFile, Map<String, com.embryo.android.speech.embedded.LocaleResourcesImpl> langPacks) {
        String str = paramFile.getName();
        if (!isValidLocale(str)) {
            return;
        }

        com.embryo.android.speech.embedded.LocaleResourcesImpl localLocaleResourcesImpl = langPacks.get(str);
        if (localLocaleResourcesImpl == null) {
            localLocaleResourcesImpl = new com.embryo.android.speech.embedded.LocaleResourcesImpl(mSupportedFormatVersions);
            langPacks.put(str, localLocaleResourcesImpl);
        }


        File[] fileList = paramFile.listFiles();

        if (fileList == null) {
            return;
        }

        for (int i = 0; i < fileList.length; i++) {
            File file = fileList[i];
            Greco3Mode type = Greco3Mode.valueOf(file);
            if (type == null) {
                if ("metadata".equals(file.getName())) {
                    localLocaleResourcesImpl.addMetadata(file);
                } else if ("hotword_prompt.txt".equals(file.getName())) {
                    localLocaleResourcesImpl.addHotwordPrompt(file);
                }
            } else {
                localLocaleResourcesImpl.addConfig(type, file);
            }
        }
    }

    private boolean hasDictationOrGrammarResources(LocaleResources paramLocaleResources) {
        if ((paramLocaleResources == null) || (paramLocaleResources.getResourcePaths() == null)) {
        }
        while ((paramLocaleResources.getConfigFile(Greco3Mode.DICTATION) == null) && (paramLocaleResources.getConfigFile(Greco3Mode.GRAMMAR) == null)) {
            return false;
        }
        return true;
    }

    private boolean isValidLocale(String paramString) {
        return (paramString.indexOf('-') == 2) || (paramString.indexOf('-') == 3);
    }

    private void processGrammar(File directory, com.embryo.android.speech.embedded.Greco3Grammar grammar, com.embryo.android.speech.embedded.LocaleResourcesImpl localeResources) {
        String revisionName = directory.getName();
        File[] grammarResources = directory.listFiles();
        if ((grammarResources == null) || (grammarResources.length < com.embryo.android.speech.embedded.Greco3GrammarCompiler.NUM_GENERATED_FILES)) {
            return;
        }
        File metadata = null;
        for (File resource : grammarResources) {
            if ("metadata".equals(resource.getName())) {
                metadata = resource;
            }
        }
        if (metadata != null) {
            localeResources.addGrammar(grammar, revisionName, directory, metadata);
        }
    }

    private void processLocaleData(Map<String, com.embryo.android.speech.embedded.LocaleResourcesImpl> paramMap) {
        Iterator localIterator = paramMap.values().iterator();
        while (localIterator.hasNext()) {
            if (!((com.embryo.android.speech.embedded.LocaleResourcesImpl) localIterator.next()).processLocaleData()) {
                localIterator.remove();
            }
        }
    }

    private void updateGrammars(Map<String, com.embryo.android.speech.embedded.LocaleResourcesImpl> available) {
        File[] locales = this.mCompiledGrammarRoot.listFiles(DIRECTORY_FILTER);
        if ((locales == null) || (locales.length == 0)) {
            return;
        }

        for (int j = 0; j < locales.length; j++) {
            File locale = locales[j];
            String localName = locale.getName();

            if (!isValidLocale(localName)) {
                return;
            }
            com.embryo.android.speech.embedded.LocaleResourcesImpl lr = available.get(localName);
            if (lr == null) {
                return;
            }

            File[] grammars = locale.listFiles(DIRECTORY_FILTER);
            if ((grammars != null) && (grammars.length != 0)) {
                for (int m = 0; m < grammars.length; m++) {
                    File grammar = grammars[m];
                    com.embryo.android.speech.embedded.Greco3Grammar grammarType = com.embryo.android.speech.embedded.Greco3Grammar.valueOf(grammar);
                    if (grammarType != null) {
                        File[] revisions = grammar.listFiles(DIRECTORY_FILTER);
                        if ((revisions != null) && (revisions.length != 0)) {
                            String currentRevision = this.mGreco3Prefs.getCompiledGrammarRevisionId(grammarType);
                            for (int i1 = 0; i1 < revisions.length; i1++) {
                                File revision = revisions[i1];
                                if (revision.getName().equals(currentRevision)) {
                                    mPathDeleter.delete(revision, false, null);
                                }
                                processGrammar(revision, grammarType, lr);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateResourceListAndNotifyCallback() {
        Map<String, com.embryo.android.speech.embedded.LocaleResourcesImpl> availableLanguages = doUpdateResourceList();
        List<Runnable> callbacks = Lists.newArrayList(mInitializationCallbacks);
        synchronized (this) {
            mAvailableLanguages = availableLanguages;
            mInitializationCallbacks.clear();
            mNumUpdatesInProgress = (mNumUpdatesInProgress - 0x1);
            Preconditions.checkState((mNumUpdatesInProgress >= 0));
            notifyAll();
        }
        for (Runnable callback : callbacks) {
            mUiThread.execute(callback);
        }
    }

    private void updateResources(Map<String, com.embryo.android.speech.embedded.LocaleResourcesImpl> paramMap) {
        Iterator localIterator = this.mSearchPaths.iterator();
        while (localIterator.hasNext()) {
            File[] arrayOfFile = ((File) localIterator.next()).listFiles(DIRECTORY_FILTER);
            if (arrayOfFile != null) {
                int i = arrayOfFile.length;
                for (int j = 0; j < i; j++) {
                    handleLocale(arrayOfFile[j], paramMap);
                }
            }
        }
    }

    private void updateResourcesLocked(boolean paramBoolean) {
        if (this.mPathDeleter != null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            if ((this.mNumUpdatesInProgress <= 0) || (paramBoolean)) {
                break;
            }
            return;
        }
        this.mNumUpdatesInProgress = (1 + this.mNumUpdatesInProgress);
        this.mUpdateExecutor.execute(new Runnable() {
            public void run() {
                Greco3DataManager.this.updateResourceListAndNotifyCallback();
            }
        });
    }

    public void addInitializationCallback(Runnable paramRunnable) {
        if (!isInitialized()) {
            this.mInitializationCallbacks.add(paramRunnable);
            return;
        }
        this.mUiThread.execute(paramRunnable);
    }

    public synchronized void blockingUpdateResources(boolean paramBoolean) {
        com.embryo.android.shared.util.ExtraPreconditions.checkNotMainThread();
        updateResourcesLocked(paramBoolean);
        waitForPendingUpdates();
    }

    public File createOuputPathForGrammarCache(com.embryo.android.speech.embedded.Greco3Grammar paramGreco3Grammar, String paramString) {
        File localFile = new File(new File(this.mCompiledGrammarRoot, paramString), paramGreco3Grammar.getDirectoryName());
        if ((!localFile.exists()) && (!localFile.mkdirs())) {
            localFile = null;
        }
        return localFile;
    }

    public File createOutputPathForGrammar(com.embryo.android.speech.embedded.Greco3Grammar paramGreco3Grammar, String paramString1, String paramString2) {
        File localFile = new File(new File(new File(this.mCompiledGrammarRoot, paramString1), paramGreco3Grammar.getDirectoryName()), paramString2);
        if ((!localFile.exists()) && (!localFile.mkdirs())) {
            localFile = null;
        }
        return localFile;
    }

    public void deleteLanguage(final com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack paramLanguagePack, Executor paramExecutor, final Runnable paramRunnable) {
        paramExecutor.execute(new Runnable() {
            public void run() {
                Greco3DataManager.this.doLanguageDelete(paramLanguagePack, paramRunnable);
            }
        });
    }

    protected Map<String, com.embryo.android.speech.embedded.LocaleResourcesImpl> doUpdateResourceList() {
        HashMap localHashMap = Maps.newHashMap();
        updateResources(localHashMap);
        if (this.mCompiledGrammarRoot != null) {
            updateGrammars(localHashMap);
        }
        processLocaleData(localHashMap);
        return localHashMap;
    }

    public String getHotwordPrompt(String paramString) {
        Preconditions.checkState(isInitialized());
        com.embryo.android.speech.embedded.LocaleResourcesImpl localLocaleResourcesImpl = this.mAvailableLanguages.get(paramString);
        if (localLocaleResourcesImpl != null) {
            String str = localLocaleResourcesImpl.getHotwordPrompt();
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return "Google";
    }

    public int getHotwordQuality(String paramString) {
        Preconditions.checkState(isInitialized());
        com.embryo.android.speech.embedded.LocaleResourcesImpl localLocaleResourcesImpl = this.mAvailableLanguages.get(paramString);
        if ((localLocaleResourcesImpl != null) && (localLocaleResourcesImpl.getLanguageMetadata().hasHotwordQuality())) {
            return localLocaleResourcesImpl.getLanguageMetadata().getHotwordQuality();
        }
        return -1;
    }

    List<Runnable> getInitializationCallbacksForTesting() {
        return this.mInitializationCallbacks;
    }

    public Map getInstalledLanguages() {
        Map<String, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack> languages = Maps.newHashMap();
        synchronized (this) {
            Preconditions.checkState(isInitialized());
            for (com.embryo.android.speech.embedded.LocaleResourcesImpl resource : mAvailableLanguages.values()) {
                com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack metadata = resource.getLanguageMetadata();
                if ((metadata != null) && (hasDictationOrGrammarResources(resource))) {
                    languages.put(metadata.getBcp47Locale(), metadata);
                }
            }
        }
        return languages;
    }

    public Supplier<File> getModelsDirSupplier() {
        return Suppliers.memoize(new Supplier() {
            public File get() {
                return Greco3DataManager.this.mContext.getDir("g3_models", 0);
            }
        });
    }

    public Greco3DataManager.LocaleResources getResources(String bcp47Locale) {
        synchronized (this) {
            Preconditions.checkState(isInitialized());
            return mAvailableLanguages.get(bcp47Locale);
        }
    }

    public String getRevisionForGrammar(String bcp47Locale, com.embryo.android.speech.embedded.Greco3Grammar grammar) {
        Greco3DataManager.LocaleResources resources = getResources(bcp47Locale);
        if (resources == null) {
            return null;
        }
        String grammarRevision = mGreco3Prefs.getCompiledGrammarRevisionId(grammar);
        if ((grammarRevision == null) || (resources.getGrammarPath(grammar, grammarRevision) == null)) {
            return null;
        }
        return grammarRevision;
    }

    public boolean hasCompiledGrammar(String paramString, com.embryo.android.speech.embedded.Greco3Grammar paramGreco3Grammar) {
        return getRevisionForGrammar(paramString, paramGreco3Grammar) != null;
    }

    public boolean hasHotwordPrompt(String paramString) {
        Preconditions.checkState(isInitialized());
        com.embryo.android.speech.embedded.LocaleResourcesImpl localLocaleResourcesImpl = this.mAvailableLanguages.get(paramString);
        return (localLocaleResourcesImpl != null) && (!TextUtils.isEmpty(localLocaleResourcesImpl.getHotwordPrompt()));
    }

    public boolean hasResources(String bcp47Locale, Greco3Mode mode) {
        Greco3DataManager.LocaleResources resources = getResources(bcp47Locale);
        boolean localboolean1 = resources != null;
        if (resources.getConfigFile(mode) != null) {
            return true;
        }
        return localboolean1;
    }

    public boolean hasResourcesForCompilation(String bcp47Locale) {
        Greco3DataManager.LocaleResources resources = getResources(bcp47Locale);
        if ((resources == null) || (resources.getConfigFile(Greco3Mode.COMPILER) == null) || (resources.getConfigFile(Greco3Mode.GRAMMAR) == null)) {
            return false;
        }
        return true;
    }

    public synchronized void initialize() {
        if (!isInitialized()) {
            updateResourcesLocked(false);
        }
    }

    public synchronized void initialize(Runnable paramRunnable) {
        addInitializationCallback(paramRunnable);
        initialize();
    }

    public synchronized boolean isInitialized() {
        if (mAvailableLanguages != null) {
            return true;
        }
        return false;
    }

    public boolean isInstalledInSystemPartition(String paramString) {
        LocaleResources localLocaleResources = getResources(paramString);
        if ((localLocaleResources == null) || (localLocaleResources.getResourcePaths() == null)) {
            return false;
        }
        return localLocaleResources.isInstalledInSystemPartition();
    }

    public boolean isUsingDownloadedData(String paramString) {
        LocaleResources localLocaleResources = getResources(paramString);
        if ((localLocaleResources == null) || (localLocaleResources.getResourcePaths() == null)) {
            return false;
        }
        return localLocaleResources.isUsingDownloadedData();
    }

    public void setPathDeleter(PathDeleter paramPathDeleter) {
        this.mPathDeleter = paramPathDeleter;
    }

    @Deprecated
    public synchronized void waitForInitialization() {
        while (null == this.mAvailableLanguages) {
            try {
                wait();
            } catch (InterruptedException localInterruptedException) {
            }
        }
    }

    protected synchronized void waitForPendingUpdates() {
        while (mNumUpdatesInProgress > 0) {
            try {
                wait();
                continue;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                Log.e("VS.G3DataManager", "Interrupted waiting for resource update.");
            }
        }
    }

    public static abstract interface LocaleResources {
        public abstract String getConfigFile(Greco3Mode paramGreco3Mode);

        public abstract String getGrammarPath(com.embryo.android.speech.embedded.Greco3Grammar paramGreco3Grammar, String paramString);

        public abstract com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack getLanguageMetadata();

        public abstract List<String> getResourcePaths();

        public abstract boolean isInstalledInSystemPartition();

        public abstract boolean isUsingDownloadedData();
    }

    public static abstract interface PathDeleter {
        public abstract void delete(File paramFile, boolean paramBoolean, @Nullable Runnable paramRunnable);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3DataManager

 * JD-Core Version:    0.7.0.1

 */
