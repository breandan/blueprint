package com.google.android.speech.embedded;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class Greco3DataManager
{
  private static final FileFilter DIRECTORY_FILTER = new FileFilter()
  {
    public boolean accept(File paramAnonymousFile)
    {
      return paramAnonymousFile.isDirectory();
    }
  };
  static final File SYSTEM_DATA_DIR = new File("/system/usr/srec");
  private Map<String, LocaleResourcesImpl> mAvailableLanguages;
  @Nullable
  private final File mCompiledGrammarRoot;
  private final Context mContext;
  @Nullable
  private final Greco3Preferences mGreco3Prefs;
  final List<Runnable> mInitializationCallbacks;
  private int mNumUpdatesInProgress;
  private PathDeleter mPathDeleter;
  private final ImmutableList<File> mSearchPaths;
  private final int[] mSupportedFormatVersions;
  private final Executor mUiThread;
  private final Executor mUpdateExecutor;
  
  public Greco3DataManager(Context paramContext, @Nullable Greco3Preferences paramGreco3Preferences, int[] paramArrayOfInt, ImmutableList<File> paramImmutableList, @Nullable File paramFile, Executor paramExecutor1, Executor paramExecutor2)
  {
    this.mContext = paramContext;
    this.mGreco3Prefs = paramGreco3Preferences;
    this.mSupportedFormatVersions = paramArrayOfInt;
    this.mSearchPaths = paramImmutableList;
    Iterator localIterator = this.mSearchPaths.iterator();
    while (localIterator.hasNext()) {
      Preconditions.checkState(((File)localIterator.next()).isAbsolute());
    }
    this.mCompiledGrammarRoot = paramFile;
    this.mUpdateExecutor = paramExecutor1;
    this.mUiThread = paramExecutor2;
    this.mNumUpdatesInProgress = 0;
    this.mAvailableLanguages = null;
    this.mInitializationCallbacks = Lists.newArrayList();
  }
  
  public Greco3DataManager(Context paramContext, Greco3Preferences paramGreco3Preferences, int[] paramArrayOfInt, Executor paramExecutor1, Executor paramExecutor2)
  {
    this(paramContext, paramGreco3Preferences, paramArrayOfInt, getSearchPathList(arrayOfFile), new File(paramContext.getCacheDir(), "g3_grammars"), paramExecutor1, paramExecutor2);
  }
  
  private void doLanguageDelete(GstaticConfiguration.LanguagePack paramLanguagePack, final Runnable paramRunnable)
  {
    File localFile = getOutputDirForLocale(paramLanguagePack.getBcp47Locale());
    this.mPathDeleter.delete(localFile, true, new Runnable()
    {
      public void run()
      {
        Greco3DataManager.this.blockingUpdateResources(true);
        if (paramRunnable != null) {
          Greco3DataManager.this.mUiThread.execute(paramRunnable);
        }
      }
    });
  }
  
  private File getOutputDirForLocale(String paramString)
  {
    return new File(this.mContext.getDir("g3_models", 0), paramString);
  }
  
  private static ImmutableList<File> getSearchPathList(File... paramVarArgs)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++)
    {
      File localFile = paramVarArgs[j];
      if (localFile != null) {
        localArrayList.add(localFile);
      }
    }
    return ImmutableList.copyOf(localArrayList);
  }
  
  private void handleLocale(File paramFile, Map<String, LocaleResourcesImpl> paramMap)
  {
    String str = paramFile.getName();
    if (!isValidLocale(str)) {}
    LocaleResourcesImpl localLocaleResourcesImpl;
    File[] arrayOfFile;
    do
    {
      return;
      localLocaleResourcesImpl = (LocaleResourcesImpl)paramMap.get(str);
      if (localLocaleResourcesImpl == null)
      {
        localLocaleResourcesImpl = new LocaleResourcesImpl(this.mSupportedFormatVersions);
        paramMap.put(str, localLocaleResourcesImpl);
      }
      arrayOfFile = paramFile.listFiles();
    } while (arrayOfFile == null);
    int i = arrayOfFile.length;
    int j = 0;
    label73:
    File localFile;
    if (j < i)
    {
      localFile = arrayOfFile[j];
      Greco3Mode localGreco3Mode = Greco3Mode.valueOf(localFile);
      if (localGreco3Mode == null) {
        break label114;
      }
      localLocaleResourcesImpl.addConfig(localGreco3Mode, localFile);
    }
    for (;;)
    {
      j++;
      break label73;
      break;
      label114:
      if ("metadata".equals(localFile.getName())) {
        localLocaleResourcesImpl.addMetadata(localFile);
      } else if ("hotword_prompt.txt".equals(localFile.getName())) {
        localLocaleResourcesImpl.addHotwordPrompt(localFile);
      }
    }
  }
  
  private boolean hasDictationOrGrammarResources(LocaleResources paramLocaleResources)
  {
    if ((paramLocaleResources == null) || (paramLocaleResources.getResourcePaths() == null)) {}
    while ((paramLocaleResources.getConfigFile(Greco3Mode.DICTATION) == null) && (paramLocaleResources.getConfigFile(Greco3Mode.GRAMMAR) == null)) {
      return false;
    }
    return true;
  }
  
  private boolean isValidLocale(String paramString)
  {
    return (paramString.indexOf('-') == 2) || (paramString.indexOf('-') == 3);
  }
  
  private void processGrammar(File paramFile, Greco3Grammar paramGreco3Grammar, LocaleResourcesImpl paramLocaleResourcesImpl)
  {
    String str = paramFile.getName();
    File[] arrayOfFile = paramFile.listFiles();
    if ((arrayOfFile == null) || (arrayOfFile.length < Greco3GrammarCompiler.NUM_GENERATED_FILES)) {}
    Object localObject;
    do
    {
      return;
      localObject = null;
      int i = arrayOfFile.length;
      for (int j = 0; j < i; j++)
      {
        File localFile = arrayOfFile[j];
        if ("metadata".equals(localFile.getName())) {
          localObject = localFile;
        }
      }
    } while (localObject == null);
    paramLocaleResourcesImpl.addGrammar(paramGreco3Grammar, str, paramFile, localObject);
  }
  
  private void processLocaleData(Map<String, LocaleResourcesImpl> paramMap)
  {
    Iterator localIterator = paramMap.values().iterator();
    while (localIterator.hasNext()) {
      if (!((LocaleResourcesImpl)localIterator.next()).processLocaleData()) {
        localIterator.remove();
      }
    }
  }
  
  private void updateGrammars(Map<String, LocaleResourcesImpl> paramMap)
  {
    File[] arrayOfFile1 = this.mCompiledGrammarRoot.listFiles(DIRECTORY_FILTER);
    if ((arrayOfFile1 == null) || (arrayOfFile1.length == 0)) {}
    LocaleResourcesImpl localLocaleResourcesImpl;
    File[] arrayOfFile2;
    label98:
    for (;;)
    {
      return;
      int i = arrayOfFile1.length;
      for (int j = 0;; j++)
      {
        if (j >= i) {
          break label98;
        }
        File localFile1 = arrayOfFile1[j];
        String str1 = localFile1.getName();
        if (!isValidLocale(str1)) {
          break;
        }
        localLocaleResourcesImpl = (LocaleResourcesImpl)paramMap.get(str1);
        if (localLocaleResourcesImpl == null) {
          break;
        }
        arrayOfFile2 = localFile1.listFiles(DIRECTORY_FILTER);
        if ((arrayOfFile2 != null) && (arrayOfFile2.length != 0)) {
          break label100;
        }
      }
    }
    label100:
    int k = arrayOfFile2.length;
    int m = 0;
    label108:
    File localFile2;
    Greco3Grammar localGreco3Grammar;
    if (m < k)
    {
      localFile2 = arrayOfFile2[m];
      localGreco3Grammar = Greco3Grammar.valueOf(localFile2);
      if (localGreco3Grammar != null) {
        break label140;
      }
    }
    label140:
    File[] arrayOfFile3;
    do
    {
      m++;
      break label108;
      break;
      arrayOfFile3 = localFile2.listFiles(DIRECTORY_FILTER);
    } while ((arrayOfFile3 == null) || (arrayOfFile3.length == 0));
    String str2 = this.mGreco3Prefs.getCompiledGrammarRevisionId(localGreco3Grammar);
    int n = arrayOfFile3.length;
    int i1 = 0;
    label180:
    File localFile3;
    if (i1 < n)
    {
      localFile3 = arrayOfFile3[i1];
      if (localFile3.getName().equals(str2)) {
        break label226;
      }
      this.mPathDeleter.delete(localFile3, false, null);
    }
    for (;;)
    {
      i1++;
      break label180;
      break;
      label226:
      processGrammar(localFile3, localGreco3Grammar, localLocaleResourcesImpl);
    }
  }
  
  private void updateResourceListAndNotifyCallback()
  {
    Map localMap = doUpdateResourceList();
    ArrayList localArrayList = Lists.newArrayList(this.mInitializationCallbacks);
    try
    {
      this.mAvailableLanguages = localMap;
      this.mInitializationCallbacks.clear();
      this.mNumUpdatesInProgress = (-1 + this.mNumUpdatesInProgress);
      if (this.mNumUpdatesInProgress >= 0) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool);
        notifyAll();
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          Runnable localRunnable = (Runnable)localIterator.next();
          this.mUiThread.execute(localRunnable);
        }
      }
      return;
    }
    finally {}
  }
  
  private void updateResources(Map<String, LocaleResourcesImpl> paramMap)
  {
    Iterator localIterator = this.mSearchPaths.iterator();
    while (localIterator.hasNext())
    {
      File[] arrayOfFile = ((File)localIterator.next()).listFiles(DIRECTORY_FILTER);
      if (arrayOfFile != null)
      {
        int i = arrayOfFile.length;
        for (int j = 0; j < i; j++) {
          handleLocale(arrayOfFile[j], paramMap);
        }
      }
    }
  }
  
  private void updateResourcesLocked(boolean paramBoolean)
  {
    if (this.mPathDeleter != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      if ((this.mNumUpdatesInProgress <= 0) || (paramBoolean)) {
        break;
      }
      return;
    }
    this.mNumUpdatesInProgress = (1 + this.mNumUpdatesInProgress);
    this.mUpdateExecutor.execute(new Runnable()
    {
      public void run()
      {
        Greco3DataManager.this.updateResourceListAndNotifyCallback();
      }
    });
  }
  
  public void addInitializationCallback(Runnable paramRunnable)
  {
    if (!isInitialized())
    {
      this.mInitializationCallbacks.add(paramRunnable);
      return;
    }
    this.mUiThread.execute(paramRunnable);
  }
  
  public void blockingUpdateResources(boolean paramBoolean)
  {
    try
    {
      ExtraPreconditions.checkNotMainThread();
      updateResourcesLocked(paramBoolean);
      waitForPendingUpdates();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public File createOuputPathForGrammarCache(Greco3Grammar paramGreco3Grammar, String paramString)
  {
    File localFile = new File(new File(this.mCompiledGrammarRoot, paramString), paramGreco3Grammar.getDirectoryName());
    if ((!localFile.exists()) && (!localFile.mkdirs())) {
      localFile = null;
    }
    return localFile;
  }
  
  public File createOutputPathForGrammar(Greco3Grammar paramGreco3Grammar, String paramString1, String paramString2)
  {
    File localFile = new File(new File(new File(this.mCompiledGrammarRoot, paramString1), paramGreco3Grammar.getDirectoryName()), paramString2);
    if ((!localFile.exists()) && (!localFile.mkdirs())) {
      localFile = null;
    }
    return localFile;
  }
  
  public void deleteLanguage(final GstaticConfiguration.LanguagePack paramLanguagePack, Executor paramExecutor, final Runnable paramRunnable)
  {
    paramExecutor.execute(new Runnable()
    {
      public void run()
      {
        Greco3DataManager.this.doLanguageDelete(paramLanguagePack, paramRunnable);
      }
    });
  }
  
  protected Map<String, LocaleResourcesImpl> doUpdateResourceList()
  {
    HashMap localHashMap = Maps.newHashMap();
    updateResources(localHashMap);
    if (this.mCompiledGrammarRoot != null) {
      updateGrammars(localHashMap);
    }
    processLocaleData(localHashMap);
    return localHashMap;
  }
  
  public String getHotwordPrompt(String paramString)
  {
    Preconditions.checkState(isInitialized());
    LocaleResourcesImpl localLocaleResourcesImpl = (LocaleResourcesImpl)this.mAvailableLanguages.get(paramString);
    if (localLocaleResourcesImpl != null)
    {
      String str = localLocaleResourcesImpl.getHotwordPrompt();
      if (!TextUtils.isEmpty(str)) {
        return str;
      }
    }
    return "Google";
  }
  
  public int getHotwordQuality(String paramString)
  {
    Preconditions.checkState(isInitialized());
    LocaleResourcesImpl localLocaleResourcesImpl = (LocaleResourcesImpl)this.mAvailableLanguages.get(paramString);
    if ((localLocaleResourcesImpl != null) && (localLocaleResourcesImpl.getLanguageMetadata().hasHotwordQuality())) {
      return localLocaleResourcesImpl.getLanguageMetadata().getHotwordQuality();
    }
    return -1;
  }
  
  List<Runnable> getInitializationCallbacksForTesting()
  {
    return this.mInitializationCallbacks;
  }
  
  public Map<String, GstaticConfiguration.LanguagePack> getInstalledLanguages()
  {
    HashMap localHashMap = Maps.newHashMap();
    try
    {
      Preconditions.checkState(isInitialized());
      Iterator localIterator = this.mAvailableLanguages.values().iterator();
      while (localIterator.hasNext())
      {
        LocaleResourcesImpl localLocaleResourcesImpl = (LocaleResourcesImpl)localIterator.next();
        GstaticConfiguration.LanguagePack localLanguagePack = localLocaleResourcesImpl.getLanguageMetadata();
        if ((localLanguagePack != null) && (hasDictationOrGrammarResources(localLocaleResourcesImpl))) {
          localHashMap.put(localLanguagePack.getBcp47Locale(), localLanguagePack);
        }
      }
    }
    finally {}
    return localHashMap;
  }
  
  public Supplier<File> getModelsDirSupplier()
  {
    Suppliers.memoize(new Supplier()
    {
      public File get()
      {
        return Greco3DataManager.this.mContext.getDir("g3_models", 0);
      }
    });
  }
  
  public LocaleResources getResources(String paramString)
  {
    try
    {
      Preconditions.checkState(isInitialized());
      LocaleResources localLocaleResources = (LocaleResources)this.mAvailableLanguages.get(paramString);
      return localLocaleResources;
    }
    finally {}
  }
  
  public String getRevisionForGrammar(String paramString, Greco3Grammar paramGreco3Grammar)
  {
    LocaleResources localLocaleResources = getResources(paramString);
    String str;
    if (localLocaleResources == null) {
      str = null;
    }
    do
    {
      return str;
      str = this.mGreco3Prefs.getCompiledGrammarRevisionId(paramGreco3Grammar);
    } while ((str != null) && (localLocaleResources.getGrammarPath(paramGreco3Grammar, str) != null));
    return null;
  }
  
  public boolean hasCompiledGrammar(String paramString, Greco3Grammar paramGreco3Grammar)
  {
    return getRevisionForGrammar(paramString, paramGreco3Grammar) != null;
  }
  
  public boolean hasHotwordPrompt(String paramString)
  {
    Preconditions.checkState(isInitialized());
    LocaleResourcesImpl localLocaleResourcesImpl = (LocaleResourcesImpl)this.mAvailableLanguages.get(paramString);
    return (localLocaleResourcesImpl != null) && (!TextUtils.isEmpty(localLocaleResourcesImpl.getHotwordPrompt()));
  }
  
  public boolean hasResources(String paramString, Greco3Mode paramGreco3Mode)
  {
    LocaleResources localLocaleResources = getResources(paramString);
    if (localLocaleResources == null) {}
    while (localLocaleResources.getConfigFile(paramGreco3Mode) == null) {
      return false;
    }
    return true;
  }
  
  public boolean hasResourcesForCompilation(String paramString)
  {
    LocaleResources localLocaleResources = getResources(paramString);
    if (localLocaleResources == null) {}
    while ((localLocaleResources.getConfigFile(Greco3Mode.COMPILER) == null) || (localLocaleResources.getConfigFile(Greco3Mode.GRAMMAR) == null)) {
      return false;
    }
    return true;
  }
  
  public void initialize()
  {
    try
    {
      if (!isInitialized()) {
        updateResourcesLocked(false);
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void initialize(Runnable paramRunnable)
  {
    try
    {
      addInitializationCallback(paramRunnable);
      initialize();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  public boolean isInitialized()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 95	com/google/android/speech/embedded/Greco3DataManager:mAvailableLanguages	Ljava/util/Map;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull +9 -> 17
    //   11: iconst_1
    //   12: istore_3
    //   13: aload_0
    //   14: monitorexit
    //   15: iload_3
    //   16: ireturn
    //   17: iconst_0
    //   18: istore_3
    //   19: goto -6 -> 13
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	27	0	this	Greco3DataManager
    //   22	4	1	localObject	Object
    //   6	2	2	localMap	Map
    //   12	7	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	7	22	finally
  }
  
  public boolean isInstalledInSystemPartition(String paramString)
  {
    LocaleResources localLocaleResources = getResources(paramString);
    if ((localLocaleResources == null) || (localLocaleResources.getResourcePaths() == null)) {
      return false;
    }
    return localLocaleResources.isInstalledInSystemPartition();
  }
  
  public boolean isUsingDownloadedData(String paramString)
  {
    LocaleResources localLocaleResources = getResources(paramString);
    if ((localLocaleResources == null) || (localLocaleResources.getResourcePaths() == null)) {
      return false;
    }
    return localLocaleResources.isUsingDownloadedData();
  }
  
  public void setPathDeleter(PathDeleter paramPathDeleter)
  {
    this.mPathDeleter = paramPathDeleter;
  }
  
  @Deprecated
  public void waitForInitialization()
  {
    try
    {
      for (;;)
      {
        Map localMap = this.mAvailableLanguages;
        if (localMap == null) {
          try
          {
            wait();
          }
          catch (InterruptedException localInterruptedException) {}
        }
      }
      return;
    }
    finally {}
  }
  
  protected void waitForPendingUpdates()
  {
    try
    {
      for (;;)
      {
        int i = this.mNumUpdatesInProgress;
        if (i <= 0) {
          break;
        }
        try
        {
          wait();
        }
        catch (InterruptedException localInterruptedException)
        {
          Thread.currentThread().interrupt();
          Log.e("VS.G3DataManager", "Interrupted waiting for resource update.");
        }
      }
    }
    finally {}
  }
  
  public static abstract interface LocaleResources
  {
    public abstract String getConfigFile(Greco3Mode paramGreco3Mode);
    
    public abstract String getGrammarPath(Greco3Grammar paramGreco3Grammar, String paramString);
    
    public abstract GstaticConfiguration.LanguagePack getLanguageMetadata();
    
    public abstract List<String> getResourcePaths();
    
    public abstract boolean isInstalledInSystemPartition();
    
    public abstract boolean isUsingDownloadedData();
  }
  
  public static abstract interface PathDeleter
  {
    public abstract void delete(File paramFile, boolean paramBoolean, @Nullable Runnable paramRunnable);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.embedded.Greco3DataManager
 * JD-Core Version:    0.7.0.1
 */