package com.google.android.speech.embedded;

import android.util.Log;
import com.google.common.collect.Maps;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

class LocaleResourcesImpl
  implements Greco3DataManager.LocaleResources
{
  private HashMap<Greco3Mode, String> mConfigPaths;
  private final HashMap<Greco3Mode, ArrayList<File>> mConfigToPathMap = Maps.newHashMap();
  private final HashMap<Greco3Grammar, HashMap<String, String>> mGrammarsToPathsMap = Maps.newHashMap();
  private GstaticConfiguration.LanguagePack mMostRecentLanguagePack;
  private final HashMap<File, String> mPathToHotwordPromptMap = Maps.newHashMap();
  private final HashMap<File, GstaticConfiguration.LanguagePack> mPathToMetadataMap = Maps.newHashMap();
  private ArrayList<String> mResourcePaths;
  private final int[] mSupportedFormatVersions;
  private boolean mSystemPartition;
  private final ArrayList<GrammarInfo> mUnprocessedGrammars;
  
  LocaleResourcesImpl(int[] paramArrayOfInt)
  {
    this.mSupportedFormatVersions = paramArrayOfInt;
    this.mUnprocessedGrammars = new ArrayList();
  }
  
  private ArrayList<File> getCompatiblePaths()
  {
    Object localObject;
    if ((this.mMostRecentLanguagePack == null) || (!this.mMostRecentLanguagePack.hasLanguagePackId())) {
      localObject = null;
    }
    for (;;)
    {
      return localObject;
      localObject = new ArrayList(4);
      Iterator localIterator = this.mPathToMetadataMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        File localFile = (File)localIterator.next();
        if (this.mMostRecentLanguagePack.getLanguagePackId().equals(((GstaticConfiguration.LanguagePack)this.mPathToMetadataMap.get(localFile)).getLanguagePackId())) {
          ((ArrayList)localObject).add(localFile);
        }
      }
    }
  }
  
  /* Error */
  private static GstaticConfiguration.LanguagePack parseMetadata(File paramFile)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: new 113	java/io/FileInputStream
    //   5: dup
    //   6: aload_0
    //   7: invokespecial 116	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   10: astore_2
    //   11: aload_2
    //   12: invokestatic 122	com/google/protobuf/micro/CodedInputStreamMicro:newInstance	(Ljava/io/InputStream;)Lcom/google/protobuf/micro/CodedInputStreamMicro;
    //   15: invokestatic 126	com/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack:parseFrom	(Lcom/google/protobuf/micro/CodedInputStreamMicro;)Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$LanguagePack;
    //   18: astore 5
    //   20: aload_2
    //   21: invokestatic 132	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   24: aload 5
    //   26: areturn
    //   27: astore 6
    //   29: aload_1
    //   30: invokestatic 132	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   33: aconst_null
    //   34: areturn
    //   35: astore 4
    //   37: aload_1
    //   38: invokestatic 132	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   41: aload 4
    //   43: athrow
    //   44: astore 4
    //   46: aload_2
    //   47: astore_1
    //   48: goto -11 -> 37
    //   51: astore_3
    //   52: aload_2
    //   53: astore_1
    //   54: goto -25 -> 29
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	paramFile	File
    //   1	53	1	localObject1	Object
    //   10	43	2	localFileInputStream	java.io.FileInputStream
    //   51	1	3	localIOException1	java.io.IOException
    //   35	7	4	localObject2	Object
    //   44	1	4	localObject3	Object
    //   18	7	5	localLanguagePack	GstaticConfiguration.LanguagePack
    //   27	1	6	localIOException2	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   2	11	27	java/io/IOException
    //   2	11	35	finally
    //   11	20	44	finally
    //   11	20	51	java/io/IOException
  }
  
  private void processLocaleSource()
  {
    String str = Greco3DataManager.SYSTEM_DATA_DIR.getAbsolutePath();
    Iterator localIterator = this.mPathToMetadataMap.keySet().iterator();
    while (localIterator.hasNext()) {
      if (((File)localIterator.next()).getAbsolutePath().startsWith(str))
      {
        this.mSystemPartition = true;
        return;
      }
    }
    this.mSystemPartition = false;
  }
  
  void addConfig(Greco3Mode paramGreco3Mode, File paramFile)
  {
    if (this.mConfigToPathMap.get(paramGreco3Mode) == null)
    {
      ArrayList localArrayList = new ArrayList(2);
      this.mConfigToPathMap.put(paramGreco3Mode, localArrayList);
    }
    ((ArrayList)this.mConfigToPathMap.get(paramGreco3Mode)).add(paramFile);
  }
  
  void addGrammar(Greco3Grammar paramGreco3Grammar, String paramString, File paramFile1, File paramFile2)
  {
    GstaticConfiguration.LanguagePack localLanguagePack = parseMetadata(paramFile2);
    if (localLanguagePack != null) {
      this.mUnprocessedGrammars.add(new GrammarInfo(paramGreco3Grammar, paramString, paramFile1, localLanguagePack));
    }
  }
  
  /* Error */
  void addHotwordPrompt(File paramFile)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: new 168	java/io/BufferedReader
    //   5: dup
    //   6: new 170	java/io/FileReader
    //   9: dup
    //   10: aload_1
    //   11: invokespecial 171	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   14: bipush 100
    //   16: invokespecial 174	java/io/BufferedReader:<init>	(Ljava/io/Reader;I)V
    //   19: astore_3
    //   20: aload_3
    //   21: invokevirtual 177	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   24: astore 9
    //   26: aload 9
    //   28: invokestatic 183	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   31: ifne +17 -> 48
    //   34: aload_0
    //   35: getfield 47	com/google/android/speech/embedded/LocaleResourcesImpl:mPathToHotwordPromptMap	Ljava/util/HashMap;
    //   38: aload_1
    //   39: invokevirtual 187	java/io/File:getParentFile	()Ljava/io/File;
    //   42: aload 9
    //   44: invokevirtual 154	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   47: pop
    //   48: aload_3
    //   49: invokestatic 132	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   52: return
    //   53: astore 4
    //   55: ldc 189
    //   57: ldc 191
    //   59: aload 4
    //   61: invokestatic 197	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   64: pop
    //   65: aload_2
    //   66: invokestatic 132	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   69: return
    //   70: astore 7
    //   72: ldc 189
    //   74: ldc 199
    //   76: aload 7
    //   78: invokestatic 197	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   81: pop
    //   82: aload_2
    //   83: invokestatic 132	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   86: return
    //   87: astore 5
    //   89: aload_2
    //   90: invokestatic 132	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   93: aload 5
    //   95: athrow
    //   96: astore 5
    //   98: aload_3
    //   99: astore_2
    //   100: goto -11 -> 89
    //   103: astore 7
    //   105: aload_3
    //   106: astore_2
    //   107: goto -35 -> 72
    //   110: astore 4
    //   112: aload_3
    //   113: astore_2
    //   114: goto -59 -> 55
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	117	0	this	LocaleResourcesImpl
    //   0	117	1	paramFile	File
    //   1	113	2	localObject1	Object
    //   19	94	3	localBufferedReader	java.io.BufferedReader
    //   53	7	4	localFileNotFoundException1	java.io.FileNotFoundException
    //   110	1	4	localFileNotFoundException2	java.io.FileNotFoundException
    //   87	7	5	localObject2	Object
    //   96	1	5	localObject3	Object
    //   70	7	7	localIOException1	java.io.IOException
    //   103	1	7	localIOException2	java.io.IOException
    //   24	19	9	str	String
    // Exception table:
    //   from	to	target	type
    //   2	20	53	java/io/FileNotFoundException
    //   2	20	70	java/io/IOException
    //   2	20	87	finally
    //   55	65	87	finally
    //   72	82	87	finally
    //   20	48	96	finally
    //   20	48	103	java/io/IOException
    //   20	48	110	java/io/FileNotFoundException
  }
  
  void addMetadata(File paramFile)
  {
    GstaticConfiguration.LanguagePack localLanguagePack = parseMetadata(paramFile);
    if (localLanguagePack == null) {
      Log.e("VS.LocaleResourcesImpl", "Unparsable metadata : " + paramFile);
    }
    do
    {
      return;
      this.mPathToMetadataMap.put(paramFile.getParentFile(), localLanguagePack);
    } while ((!LanguagePackUtils.isCompatible(localLanguagePack, this.mSupportedFormatVersions, 2147483647)) || ((this.mMostRecentLanguagePack != null) && (localLanguagePack.getVersion() <= this.mMostRecentLanguagePack.getVersion())));
    this.mMostRecentLanguagePack = localLanguagePack;
  }
  
  public String getConfigFile(Greco3Mode paramGreco3Mode)
  {
    return (String)this.mConfigPaths.get(paramGreco3Mode);
  }
  
  public String getGrammarPath(Greco3Grammar paramGreco3Grammar, String paramString)
  {
    HashMap localHashMap = (HashMap)this.mGrammarsToPathsMap.get(paramGreco3Grammar);
    if (localHashMap != null) {
      return (String)localHashMap.get(paramString);
    }
    return null;
  }
  
  @Nullable
  public String getHotwordPrompt()
  {
    if (getCompatiblePaths() != null)
    {
      Iterator localIterator = getCompatiblePaths().iterator();
      while (localIterator.hasNext())
      {
        File localFile = (File)localIterator.next();
        String str = (String)this.mPathToHotwordPromptMap.get(localFile);
        if (str != null) {
          return str;
        }
      }
    }
    return null;
  }
  
  public GstaticConfiguration.LanguagePack getLanguageMetadata()
  {
    return this.mMostRecentLanguagePack;
  }
  
  public List<String> getResourcePaths()
  {
    return this.mResourcePaths;
  }
  
  public boolean isInstalledInSystemPartition()
  {
    return this.mSystemPartition;
  }
  
  public boolean isUsingDownloadedData()
  {
    Iterator localIterator = this.mResourcePaths.iterator();
    while (localIterator.hasNext()) {
      if (((String)localIterator.next()).indexOf("g3_models") > 0) {
        return true;
      }
    }
    return false;
  }
  
  void processGrammar(GrammarInfo paramGrammarInfo)
  {
    HashMap localHashMap = (HashMap)this.mGrammarsToPathsMap.get(paramGrammarInfo.grammar);
    if (localHashMap == null)
    {
      localHashMap = Maps.newHashMap();
      this.mGrammarsToPathsMap.put(paramGrammarInfo.grammar, localHashMap);
    }
    localHashMap.put(paramGrammarInfo.revisionName, paramGrammarInfo.directory.getAbsolutePath());
  }
  
  boolean processLocaleData()
  {
    processLocaleSource();
    ArrayList localArrayList = getCompatiblePaths();
    if (localArrayList != null)
    {
      this.mResourcePaths = new ArrayList(localArrayList.size());
      Iterator localIterator1 = localArrayList.iterator();
      while (localIterator1.hasNext())
      {
        File localFile2 = (File)localIterator1.next();
        this.mResourcePaths.add(localFile2.getAbsolutePath());
      }
      this.mConfigPaths = Maps.newHashMap();
      Iterator localIterator2 = this.mConfigToPathMap.keySet().iterator();
      while (localIterator2.hasNext())
      {
        Greco3Mode localGreco3Mode = (Greco3Mode)localIterator2.next();
        Iterator localIterator4 = ((ArrayList)this.mConfigToPathMap.get(localGreco3Mode)).iterator();
        while (localIterator4.hasNext())
        {
          File localFile1 = (File)localIterator4.next();
          if (this.mResourcePaths.contains(localFile1.getParentFile().getAbsolutePath()))
          {
            if (this.mConfigPaths.containsKey(localGreco3Mode)) {
              Log.w("VS.LocaleResourcesImpl", "Duplicate config file, found at: " + localFile1 + ", overwriting: " + (String)this.mConfigPaths.get(localGreco3Mode));
            }
            this.mConfigPaths.put(localGreco3Mode, localFile1.getAbsolutePath());
          }
        }
      }
      if ((this.mMostRecentLanguagePack != null) && (this.mConfigPaths.size() > 0))
      {
        Iterator localIterator3 = this.mUnprocessedGrammars.iterator();
        while (localIterator3.hasNext())
        {
          GrammarInfo localGrammarInfo = (GrammarInfo)localIterator3.next();
          if (localGrammarInfo.metadata.getLanguagePackId().equals(this.mMostRecentLanguagePack.getLanguagePackId())) {
            processGrammar(localGrammarInfo);
          }
        }
        this.mUnprocessedGrammars.clear();
        return true;
      }
    }
    return false;
  }
  
  static class GrammarInfo
  {
    final File directory;
    final Greco3Grammar grammar;
    final GstaticConfiguration.LanguagePack metadata;
    final String revisionName;
    
    GrammarInfo(Greco3Grammar paramGreco3Grammar, String paramString, File paramFile, GstaticConfiguration.LanguagePack paramLanguagePack)
    {
      this.grammar = paramGreco3Grammar;
      this.revisionName = paramString;
      this.directory = paramFile;
      this.metadata = paramLanguagePack;
    }
  }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.LocaleResourcesImpl

 * JD-Core Version:    0.7.0.1

 */