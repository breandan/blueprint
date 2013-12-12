package com.google.android.speech.embedded;

import android.util.Log;

import com.google.android.shared.util.ConcurrentUtils;
import com.google.android.shared.util.StopWatch;
import com.google.common.base.Preconditions;
import com.google.speech.logs.RecognizerOuterClass;
import com.google.speech.recognizer.api.NativeRecognizer;
import com.google.speech.recognizer.api.RecognizerSessionParamsProto;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

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
    private final EndpointerModelCopier mEndpointerModelCopier;
    private final Greco3DataManager mGreco3DataManager;
    @Nullable
    private final Greco3Preferences mGreco3Preferences;
    private final ExecutorService mRecognitionExecutor;
    private final HashMap<Greco3Mode, Resources> mResourcesByMode;
    private Future<Greco3Recognizer> mCurrentRecognition;
    private Greco3Recognizer mCurrentRecognizer;
    private boolean mInitialized;

    public Greco3EngineManager(Greco3DataManager paramGreco3DataManager, @Nullable Greco3Preferences paramGreco3Preferences, @Nullable EndpointerModelCopier paramEndpointerModelCopier) {
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mGreco3Preferences = paramGreco3Preferences;
        this.mEndpointerModelCopier = paramEndpointerModelCopier;
        this.mRecognitionExecutor = ConcurrentUtils.newSingleThreadExecutor("Greco3Thread");
        this.mResourcesByMode = new HashMap();
    }

    private static RecognizerOuterClass.LanguagePackLog buildLanguagePackLog(GstaticConfiguration.LanguagePack paramLanguagePack) {
        return new RecognizerOuterClass.LanguagePackLog().setLocale(paramLanguagePack.getBcp47Locale()).setVersion(String.valueOf(paramLanguagePack.getVersion()));
    }

    private static void deleteSingleLevelTree(File paramFile) {
        if (paramFile.exists()) {
            File[] arrayOfFile = paramFile.listFiles();
            if (arrayOfFile != null) {
                int i = arrayOfFile.length;
                for (int j = 0; j < i; j++) {
                    File localFile = arrayOfFile[j];
                    if (!localFile.delete()) {
                        Log.e("VS.G3EngineManager", "Error deleting resource file: " + localFile.getAbsolutePath());
                    }
                }
            }
            if (!paramFile.delete()) {
                Log.e("VS.G3EngineManager", "Error deleting directory: " + paramFile.getAbsolutePath());
            }
        }
    }

    private void doResourceDelete(File paramFile, boolean paramBoolean) {
        try {
            if (isUsedLocked(paramFile)) {
                if (paramBoolean) {
                    releaseAllResourcesLocked();
                }
            } else {
                deleteSingleLevelTree(paramFile);
            }
            return;
        } finally {
        }
    }

    private String getCompiledGrammarPath(Greco3Grammar paramGreco3Grammar, Greco3DataManager.LocaleResources paramLocaleResources) {
        if ((paramGreco3Grammar != null) && (this.mGreco3Preferences != null)) {
            return paramLocaleResources.getGrammarPath(paramGreco3Grammar, this.mGreco3Preferences.getCompiledGrammarRevisionId(paramGreco3Grammar));
        }
        return null;
    }

    private Resources getResourcesInternal(String paramString, Greco3Mode paramGreco3Mode, @Nullable Greco3Grammar paramGreco3Grammar) {
        boolean bool1 = true;
        label171:
        for (; ; ) {
            try {
                if (paramGreco3Mode == Greco3Mode.GRAMMAR) {
                    Resources localResources1;
                    if (paramGreco3Grammar != null) {
                        break label171;
                        Preconditions.checkArgument(bool2);
                        if (this.mCurrentRecognition == null) {
                            Preconditions.checkState(bool1);
                            localResources1 = (Resources) this.mResourcesByMode.get(paramGreco3Mode);
                            if (localResources1 == null) {
                                continue;
                            }
                            boolean bool3 = localResources1.isEquivalentTo(paramString, paramGreco3Grammar, paramGreco3Mode);
                            if (!bool3) {
                                continue;
                            }
                            localObject2 = localResources1;
                            return localObject2;
                        }
                    } else {
                        bool2 = false;
                        continue;
                    }
                    bool1 = false;
                    continue;
                    localResources1.resources.delete();
                    this.mResourcesByMode.remove(paramGreco3Mode);
                    Resources localResources2 = loadResourcesFor(paramString, paramGreco3Mode, paramGreco3Grammar);
                    if ((localResources2 == null) && (paramGreco3Mode.isEndpointerMode())) {
                        localResources2 = loadResourcesFor("en-US", paramGreco3Mode, null);
                    }
                    Object localObject2 = null;
                    if (localResources2 == null) {
                        continue;
                    }
                    this.mResourcesByMode.put(paramGreco3Mode, localResources2);
                    localObject2 = localResources2;
                    continue;
                }
                boolean bool2 = bool1;
            } finally {
            }
        }
    }

    private boolean isUsedLocked(File paramFile) {
        String str = paramFile.getAbsolutePath();
        Iterator localIterator = this.mResourcesByMode.values().iterator();
        while (localIterator.hasNext()) {
            String[] arrayOfString = ((Resources) localIterator.next()).paths;
            int i = arrayOfString.length;
            for (int j = 0; j < i; j++) {
                if (str.equals(arrayOfString[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private Resources loadResourcesFor(String paramString, Greco3Mode paramGreco3Mode, Greco3Grammar paramGreco3Grammar) {
        Greco3DataManager.LocaleResources localLocaleResources = this.mGreco3DataManager.getResources(paramString);
        if (localLocaleResources == null) {
            return null;
        }
        String str1 = localLocaleResources.getConfigFile(paramGreco3Mode);
        if (str1 == null) {
            return null;
        }
        List localList = localLocaleResources.getResourcePaths();
        if ((localList == null) || (localList.isEmpty())) {
            Log.e("VS.G3EngineManager", "Incomplete / partial data for locale: " + paramString);
            return null;
        }
        String str2;
        if (paramGreco3Mode == Greco3Mode.GRAMMAR) {
            str2 = getCompiledGrammarPath(paramGreco3Grammar, localLocaleResources);
            if ((str2 == null) && (paramGreco3Mode == Greco3Mode.GRAMMAR)) {
                return null;
            }
        } else {
            str2 = null;
        }
        StopWatch localStopWatch = new StopWatch();
        localStopWatch.start();
        if (str2 == null) {
        }
        String[] arrayOfString;
        Greco3ResourceManager localGreco3ResourceManager;
        for (int i = localList.size(); ; i = 1 + localList.size()) {
            arrayOfString = new String[i];
            localList.toArray(arrayOfString);
            if (str2 != null) {
                arrayOfString[(-1 + arrayOfString.length)] = str2;
            }
            Log.i("VS.G3EngineManager", "create_rm: m=" + paramGreco3Mode + ",l=" + paramString);
            localGreco3ResourceManager = Greco3ResourceManager.create(str1, arrayOfString);
            if (localGreco3ResourceManager != null) {
                break;
            }
            Log.i("VS.G3EngineManager", "Error loading resources.");
            return null;
        }
        Log.i("VS.G3EngineManager", "Brought up new g3 instance :" + str1 + " for: " + paramString + "in: " + localStopWatch.getElapsedTime() + " ms");
        return new Resources(localGreco3ResourceManager, localLocaleResources.getConfigFile(paramGreco3Mode), paramString, paramGreco3Grammar, paramGreco3Mode, arrayOfString, localLocaleResources.getLanguageMetadata());
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

    public Resources getResources(String paramString, Greco3Mode paramGreco3Mode, @Nullable Greco3Grammar paramGreco3Grammar) {
        return getResourcesInternal(paramString, paramGreco3Mode, paramGreco3Grammar);
    }

    /* Error */
    public void maybeInitialize() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 321	com/google/android/speech/embedded/Greco3EngineManager:mInitialized	Z
        //   6: ifeq +6 -> 12
        //   9: aload_0
        //   10: monitorexit
        //   11: return
        //   12: aload_0
        //   13: monitorexit
        //   14: aload_0
        //   15: getfield 32	com/google/android/speech/embedded/Greco3EngineManager:mGreco3DataManager	Lcom/google/android/speech/embedded/Greco3DataManager;
        //   18: iconst_0
        //   19: invokevirtual 338	com/google/android/speech/embedded/Greco3DataManager:blockingUpdateResources	(Z)V
        //   22: aload_0
        //   23: getfield 36	com/google/android/speech/embedded/Greco3EngineManager:mEndpointerModelCopier	Lcom/google/android/speech/embedded/EndpointerModelCopier;
        //   26: ifnull +34 -> 60
        //   29: aload_0
        //   30: getfield 36	com/google/android/speech/embedded/Greco3EngineManager:mEndpointerModelCopier	Lcom/google/android/speech/embedded/EndpointerModelCopier;
        //   33: aload_0
        //   34: getfield 32	com/google/android/speech/embedded/Greco3EngineManager:mGreco3DataManager	Lcom/google/android/speech/embedded/Greco3DataManager;
        //   37: invokevirtual 342	com/google/android/speech/embedded/Greco3DataManager:getModelsDirSupplier	()Lcom/google/common/base/Supplier;
        //   40: aload_0
        //   41: getfield 32	com/google/android/speech/embedded/Greco3EngineManager:mGreco3DataManager	Lcom/google/android/speech/embedded/Greco3DataManager;
        //   44: invokeinterface 348 3 0
        //   49: ifeq +11 -> 60
        //   52: aload_0
        //   53: getfield 32	com/google/android/speech/embedded/Greco3EngineManager:mGreco3DataManager	Lcom/google/android/speech/embedded/Greco3DataManager;
        //   56: iconst_1
        //   57: invokevirtual 338	com/google/android/speech/embedded/Greco3DataManager:blockingUpdateResources	(Z)V
        //   60: aload_0
        //   61: monitorenter
        //   62: aload_0
        //   63: iconst_1
        //   64: putfield 321	com/google/android/speech/embedded/Greco3EngineManager:mInitialized	Z
        //   67: aload_0
        //   68: monitorexit
        //   69: return
        //   70: astore_2
        //   71: aload_0
        //   72: monitorexit
        //   73: aload_2
        //   74: athrow
        //   75: astore_1
        //   76: aload_0
        //   77: monitorexit
        //   78: aload_1
        //   79: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	80	0	this	Greco3EngineManager
        //   75	4	1	localObject1	Object
        //   70	4	2	localObject2	Object
        // Exception table:
        //   from	to	target	type
        //   62	69	70	finally
        //   71	73	70	finally
        //   2	11	75	finally
        //   12	14	75	finally
        //   76	78	75	finally
    }

    public void release(Greco3Recognizer paramGreco3Recognizer) {
        boolean bool1 = true;
        if (this.mCurrentRecognition == null) {
            boolean bool2 = bool1;
        }
        for (; ; ) {
            Preconditions.checkState(bool2);
            if (paramGreco3Recognizer == this.mCurrentRecognizer) {
                Preconditions.checkState(bool1);
                paramGreco3Recognizer.cancel();
            }
            try {
                this.mCurrentRecognition.get();
                this.mCurrentRecognizer.delete();
                this.mCurrentRecognition = null;
                this.mCurrentRecognizer = null;
                return;
                bool2 = false;
                continue;
                bool1 = false;
            } catch (InterruptedException localInterruptedException) {
                Thread.currentThread().interrupt();
                Log.e("VS.G3EngineManager", "Interrupted waiting for recognition to complete.");
            } catch (ExecutionException localExecutionException) {
                Log.e("VS.G3EngineManager", "Exception while running recognition: " + localExecutionException);
            }
        }
    }

    public void startRecognition(final Greco3Recognizer paramGreco3Recognizer, InputStream paramInputStream, Greco3Callback paramGreco3Callback, final RecognizerSessionParamsProto.RecognizerSessionParams paramRecognizerSessionParams, @Nullable final GrecoEventLogger paramGrecoEventLogger, final GstaticConfiguration.LanguagePack paramLanguagePack) {
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
        final Greco3Grammar grammarType;
        final GstaticConfiguration.LanguagePack languagePack;
        final String locale;
        final Greco3Mode mode;
        final String[] paths;
        final Greco3ResourceManager resources;

        Resources(Greco3ResourceManager paramGreco3ResourceManager, String paramString1, String paramString2, Greco3Grammar paramGreco3Grammar, Greco3Mode paramGreco3Mode, String[] paramArrayOfString, GstaticConfiguration.LanguagePack paramLanguagePack) {
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

        boolean isEquivalentTo(String paramString, Greco3Grammar paramGreco3Grammar, Greco3Mode paramGreco3Mode) {
            return (paramString.equals(this.locale)) && (paramGreco3Mode == this.mode) && ((paramGreco3Mode != Greco3Mode.GRAMMAR) || (paramGreco3Grammar == this.grammarType));
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.Greco3EngineManager

 * JD-Core Version:    0.7.0.1

 */