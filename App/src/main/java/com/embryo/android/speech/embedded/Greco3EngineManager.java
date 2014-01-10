package com.embryo.android.speech.embedded;

import android.util.Log;

import com.embryo.android.shared.util.StopWatch;
import com.embryo.speech.logs.RecognizerOuterClass;
import com.embryo.speech.recognizer.api.NativeRecognizer;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Nullable;

public class Greco3EngineManager
        implements Greco3DataManager.PathDeleter {
    @Nullable
    private final com.embryo.android.speech.embedded.EndpointerModelCopier mEndpointerModelCopier;
    private final Greco3DataManager mGreco3DataManager;
    @Nullable
    private final Greco3Preferences mGreco3Preferences;
    private final ExecutorService mRecognitionExecutor;
    private final HashMap<Greco3Mode, Resources> mResourcesByMode;
    private Future<Greco3Recognizer> mCurrentRecognition;
    private Greco3Recognizer mCurrentRecognizer;
    private boolean mInitialized;

    public Greco3EngineManager(Greco3DataManager paramGreco3DataManager, @Nullable Greco3Preferences paramGreco3Preferences, @Nullable com.embryo.android.speech.embedded.EndpointerModelCopier paramEndpointerModelCopier) {
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mGreco3Preferences = paramGreco3Preferences;
        this.mEndpointerModelCopier = paramEndpointerModelCopier;
        this.mRecognitionExecutor = com.embryo.android.shared.util.ConcurrentUtils.newSingleThreadExecutor("Greco3Thread");
        this.mResourcesByMode = new HashMap();
    }

    private static RecognizerOuterClass.LanguagePackLog buildLanguagePackLog(com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack paramLanguagePack) {
        return new RecognizerOuterClass.LanguagePackLog().setLocale(paramLanguagePack.getBcp47Locale()).setVersion(String.valueOf(paramLanguagePack.getVersion()));
    }

    private static void deleteSingleLevelTree(File resourceDir) {
        if (resourceDir.exists()) {
            File[] files = resourceDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        Log.e("VS.G3EngineManager", "Error deleting resource file: " + file.getAbsolutePath());
                    }
                }
            }
            if (!resourceDir.delete()) {
                Log.e("VS.G3EngineManager", "Error deleting directory: " + resourceDir.getAbsolutePath());
            }
        }
    }

    private synchronized void doResourceDelete(File path, boolean force) {
        if (isUsedLocked(path)) {
            if (force) {
                releaseAllResourcesLocked();
            }
        }
        deleteSingleLevelTree(path);
    }

    private String getCompiledGrammarPath(com.embryo.android.speech.embedded.Greco3Grammar paramGreco3Grammar, Greco3DataManager.LocaleResources paramLocaleResources) {
        if ((paramGreco3Grammar != null) && (this.mGreco3Preferences != null)) {
            return paramLocaleResources.getGrammarPath(paramGreco3Grammar, this.mGreco3Preferences.getCompiledGrammarRevisionId(paramGreco3Grammar));
        }
        return null;
    }

    private synchronized Resources getResourcesInternal(String locale, Greco3Mode mode, @Nullable com.embryo.android.speech.embedded.Greco3Grammar grammarType) {
        Preconditions.checkArgument(mode != Greco3Mode.GRAMMAR || grammarType != null);
        Preconditions.checkState(mCurrentRecognition == null);

        Resources instance = mResourcesByMode.get(mode);
        if (instance == null) {
            instance = loadResourcesFor(locale, mode, grammarType);
            if (instance != null || mode.isEndpointerMode()) {
                if (instance != null) {
                    mResourcesByMode.put(mode, instance);
                }
            } else {
                instance = loadResourcesFor("en-US", mode, null);
            }
        }

        return instance;
    }

    private boolean isUsedLocked(File paramFile) {
        String fileUsedLocked = paramFile.getAbsolutePath();

        for (Resources resource : mResourcesByMode.values()) {
            for (String path : resource.paths) {
                if (path.equals(fileUsedLocked)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Greco3EngineManager.Resources loadResourcesFor(String bcp47Locale, Greco3Mode mode, com.embryo.android.speech.embedded.Greco3Grammar grammarType) {
        Greco3DataManager.LocaleResources resources = mGreco3DataManager.getResources(bcp47Locale);
        if (resources == null) {
            return null;
        }
        String configFile = resources.getConfigFile(mode);
        if (configFile == null) {
            return null;
        }
        List<String> dataPaths = resources.getResourcePaths();
        if ((dataPaths == null) || (dataPaths.isEmpty())) {
            Log.e("VS.G3EngineManager", "Incomplete / partial data for locale: " + bcp47Locale);
            return null;
        }
        if (mode == Greco3Mode.GRAMMAR) {
            String grammarPath = getCompiledGrammarPath(grammarType, resources);
            if ((grammarPath == null) && (mode == Greco3Mode.GRAMMAR)) {
                return null;
            }
            StopWatch initStopWatch = new StopWatch();
            initStopWatch.start();

            int arraySize = dataPaths.size();

            if (grammarPath != null) {
                arraySize += 1;
            }

            String[] pathsArray = new String[arraySize];
            dataPaths.toArray(pathsArray);

            if (grammarPath != null) {
                pathsArray[(pathsArray.length - 0x1)] = grammarPath;
            }

            dataPaths.toArray(pathsArray);
            Log.i("VS.G3EngineManager", "create_rm: m=" + mode + ",l=" + bcp47Locale);
            com.embryo.android.speech.embedded.Greco3ResourceManager rm = com.embryo.android.speech.embedded.Greco3ResourceManager.create(configFile, pathsArray);
            if (rm == null) {
                Log.i("VS.G3EngineManager", "Error loading resources.");
                return null;
            }
            Log.i("VS.G3EngineManager", "Brought up new g3 instance :" + configFile + " for: " + bcp47Locale + "in: " + initStopWatch.getElapsedTime() + " ms");
            return new Greco3EngineManager.Resources(rm, resources.getConfigFile(mode), bcp47Locale, grammarType, mode, pathsArray, resources.getLanguageMetadata());
        }

        return null;
    }

    private void releaseAllResourcesLocked() {
        if (this.mCurrentRecognizer != null) {
            Log.w("VS.G3EngineManager", "Terminating active recognition for shutdown.");
            release(this.mCurrentRecognizer);
        }
        Iterator localIterator = this.mResourcesByMode.values().iterator();
        while (localIterator.hasNext()) {
            ((Resources) localIterator.next()).resources.delete();
        }
        this.mResourcesByMode.clear();
    }

    public void delete(final File paramFile, final boolean paramBoolean, final Runnable paramRunnable) {
        try {
            if ((this.mInitialized) && (!paramBoolean)) {
                return;
            }
            this.mRecognitionExecutor.execute(new Runnable() {
                public void run() {
                    Greco3EngineManager.this.doResourceDelete(paramFile, paramBoolean);
                    if (paramRunnable != null) {
                        paramRunnable.run();
                    }
                }
            });
            return;
        } finally {
        }
    }

    public Resources getResources(String paramString, Greco3Mode paramGreco3Mode, @Nullable com.embryo.android.speech.embedded.Greco3Grammar paramGreco3Grammar) {
        return getResourcesInternal(paramString, paramGreco3Mode, paramGreco3Grammar);
    }

    public void maybeInitialize() {
        synchronized (this) {
            if (mInitialized) {
                return;
            }
            mGreco3DataManager.blockingUpdateResources(false);
            if (mEndpointerModelCopier != null) {
                if (mEndpointerModelCopier.copyEndpointerModels(mGreco3DataManager.getModelsDirSupplier(), mGreco3DataManager)) {
                    mGreco3DataManager.blockingUpdateResources(true);
                }
            }
            synchronized (this) {
                mInitialized = true;
            }
        }
    }

    public void release(Greco3Recognizer recognizer) {
        Preconditions.checkState((mCurrentRecognition != null));
        Preconditions.checkState((recognizer == mCurrentRecognizer));
        recognizer.cancel();
        try {
            mCurrentRecognition.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.e("VS.G3EngineManager", "Interrupted waiting for recognition to complete.");
            return;
        } catch (ExecutionException e) {
            Log.e("VS.G3EngineManager", "Exception while running recognition: " + e);
        }
        mCurrentRecognizer.delete();
        mCurrentRecognition = null;
        mCurrentRecognizer = null;
    }

    public void startRecognition(final Greco3Recognizer paramGreco3Recognizer, InputStream paramInputStream, Greco3Callback paramGreco3Callback, final com.embryo.speech.recognizer.api.RecognizerSessionParamsProto.RecognizerSessionParams paramRecognizerSessionParams, @Nullable final com.embryo.android.speech.embedded.GrecoEventLogger paramGrecoEventLogger, final com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack paramLanguagePack) {
        if (this.mCurrentRecognition == null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            paramGreco3Recognizer.setAudioReader(paramInputStream);
            paramGreco3Recognizer.setSamplingRate((int) paramRecognizerSessionParams.getSampleRate());
            paramGreco3Recognizer.setCallback(paramGreco3Callback);
            this.mCurrentRecognition = this.mRecognitionExecutor.submit(new Callable() {
                public Greco3Recognizer call() {
                    if (paramGrecoEventLogger != null) {
                        paramGrecoEventLogger.recognitionStarted();
                    }
                    NativeRecognizer.NativeRecognitionResult localNativeRecognitionResult = paramGreco3Recognizer.run(paramRecognizerSessionParams);
                    int i = localNativeRecognitionResult.getStatus();
                    if ((i != 0) && (i != 4)) {
                        Log.e("VS.G3EngineManager", "Error running recognition: " + i);
                    }
                    if (paramGrecoEventLogger != null) {
                        RecognizerOuterClass.RecognizerLog localRecognizerLog = localNativeRecognitionResult.getRecognizerInfo();
                        localRecognizerLog.setLangPack(Greco3EngineManager.buildLanguagePackLog(paramLanguagePack));
                        localRecognizerLog.setRecognizerLanguage(paramLanguagePack.getBcp47Locale());
                        paramGrecoEventLogger.recognitionCompleted(localRecognizerLog);
                    }
                    return paramGreco3Recognizer;
                }
            });
            this.mCurrentRecognizer = paramGreco3Recognizer;
            return;
        }
    }

    public static class Resources {
        final String configFile;
        @Nullable
        final com.embryo.android.speech.embedded.Greco3Grammar grammarType;
        final com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack languagePack;
        final String locale;
        final Greco3Mode mode;
        final String[] paths;
        final com.embryo.android.speech.embedded.Greco3ResourceManager resources;

        Resources(com.embryo.android.speech.embedded.Greco3ResourceManager paramGreco3ResourceManager, String paramString1, String paramString2, com.embryo.android.speech.embedded.Greco3Grammar paramGreco3Grammar, Greco3Mode paramGreco3Mode, String[] paramArrayOfString, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack paramLanguagePack) {
            this.resources = paramGreco3ResourceManager;
            this.configFile = paramString1;
            this.locale = paramString2;
            this.grammarType = paramGreco3Grammar;
            this.mode = paramGreco3Mode;
            this.paths = paramArrayOfString;
            this.languagePack = paramLanguagePack;
        }

        public boolean equals(Object paramObject) {
            if (!(paramObject instanceof Resources)) {
                return false;
            }

            Resources localResources = (Resources) paramObject;
            if (!this.locale.equals(localResources.locale) || (this.mode != localResources.mode) || (this.mode != Greco3Mode.GRAMMAR) || (this.grammarType != localResources.grammarType))
                return false;
            return true;
        }

        boolean isEquivalentTo(String paramString, com.embryo.android.speech.embedded.Greco3Grammar paramGreco3Grammar, Greco3Mode paramGreco3Mode) {
            return (paramString.equals(this.locale)) && (paramGreco3Mode == this.mode) && ((paramGreco3Mode != Greco3Mode.GRAMMAR) || (paramGreco3Grammar == this.grammarType));
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3EngineManager

 * JD-Core Version:    0.7.0.1

 */