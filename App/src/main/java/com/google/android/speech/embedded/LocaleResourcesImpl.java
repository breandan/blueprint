package com.google.android.speech.embedded;

import android.text.TextUtils;
import android.util.Log;

import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

class LocaleResourcesImpl
        implements Greco3DataManager.LocaleResources {
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

    LocaleResourcesImpl(int[] paramArrayOfInt) {
        this.mSupportedFormatVersions = paramArrayOfInt;
        this.mUnprocessedGrammars = new ArrayList();
    }

    private ArrayList<File> getCompatiblePaths() {
        Object localObject;
        if ((this.mMostRecentLanguagePack == null) || (!this.mMostRecentLanguagePack.hasLanguagePackId())) {
            localObject = null;
        }
        for (; ; ) {
            return localObject;
            localObject = new ArrayList(4);
            Iterator localIterator = this.mPathToMetadataMap.keySet().iterator();
            while (localIterator.hasNext()) {
                File localFile = (File) localIterator.next();
                if (this.mMostRecentLanguagePack.getLanguagePackId().equals(((GstaticConfiguration.LanguagePack) this.mPathToMetadataMap.get(localFile)).getLanguagePackId())) {
                    ((ArrayList) localObject).add(localFile);
                }
            }
        }
    }

    /* Error */
    private static GstaticConfiguration.LanguagePack parseMetadata(File paramFile) {
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

    private void processLocaleSource() {
        String str = Greco3DataManager.SYSTEM_DATA_DIR.getAbsolutePath();
        Iterator localIterator = this.mPathToMetadataMap.keySet().iterator();
        while (localIterator.hasNext()) {
            if (((File) localIterator.next()).getAbsolutePath().startsWith(str)) {
                this.mSystemPartition = true;
                return;
            }
        }
        this.mSystemPartition = false;
    }

    void addConfig(Greco3Mode paramGreco3Mode, File paramFile) {
        if (this.mConfigToPathMap.get(paramGreco3Mode) == null) {
            ArrayList localArrayList = new ArrayList(2);
            this.mConfigToPathMap.put(paramGreco3Mode, localArrayList);
        }
        ((ArrayList) this.mConfigToPathMap.get(paramGreco3Mode)).add(paramFile);
    }

    void addGrammar(Greco3Grammar paramGreco3Grammar, String paramString, File paramFile1, File paramFile2) {
        GstaticConfiguration.LanguagePack localLanguagePack = parseMetadata(paramFile2);
        if (localLanguagePack != null) {
            this.mUnprocessedGrammars.add(new GrammarInfo(paramGreco3Grammar, paramString, paramFile1, localLanguagePack));
        }
    }
    void addHotwordPrompt(File file) {
        BufferedReader br = 0x0;
        try {
            String prompt = br.readLine();
            if(!TextUtils.isEmpty(prompt)) {
                mPathToHotwordPromptMap.put(file.getParentFile(), prompt);
            }
        } catch(FileNotFoundException e) {
            Log.e("VS.LocaleResourcesImpl", "Could not open hotword prompt file.", e);
            return;
        } catch(IOException e) {
            Log.e("VS.LocaleResourcesImpl", "Could not read hotword prompt file.", e);
            return;
        } finally {
            Closeables.closeQuietly(br);
        }
    }

    void addMetadata(File paramFile) {
        GstaticConfiguration.LanguagePack localLanguagePack = parseMetadata(paramFile);
        if (localLanguagePack == null) {
            Log.e("VS.LocaleResourcesImpl", "Unparsable metadata : " + paramFile);
        }
        do {
            return;
            this.mPathToMetadataMap.put(paramFile.getParentFile(), localLanguagePack);
        }
        while ((!LanguagePackUtils.isCompatible(localLanguagePack, this.mSupportedFormatVersions, 2147483647)) || ((this.mMostRecentLanguagePack != null) && (localLanguagePack.getVersion() <= this.mMostRecentLanguagePack.getVersion())));
        this.mMostRecentLanguagePack = localLanguagePack;
    }

    public String getConfigFile(Greco3Mode paramGreco3Mode) {
        return (String) this.mConfigPaths.get(paramGreco3Mode);
    }

    public String getGrammarPath(Greco3Grammar paramGreco3Grammar, String paramString) {
        HashMap localHashMap = (HashMap) this.mGrammarsToPathsMap.get(paramGreco3Grammar);
        if (localHashMap != null) {
            return (String) localHashMap.get(paramString);
        }
        return null;
    }

    @Nullable
    public String getHotwordPrompt() {
        if (getCompatiblePaths() != null) {
            Iterator localIterator = getCompatiblePaths().iterator();
            while (localIterator.hasNext()) {
                File localFile = (File) localIterator.next();
                String str = (String) this.mPathToHotwordPromptMap.get(localFile);
                if (str != null) {
                    return str;
                }
            }
        }
        return null;
    }

    public GstaticConfiguration.LanguagePack getLanguageMetadata() {
        return this.mMostRecentLanguagePack;
    }

    public List<String> getResourcePaths() {
        return this.mResourcePaths;
    }

    public boolean isInstalledInSystemPartition() {
        return this.mSystemPartition;
    }

    public boolean isUsingDownloadedData() {
        Iterator localIterator = this.mResourcePaths.iterator();
        while (localIterator.hasNext()) {
            if (((String) localIterator.next()).indexOf("g3_models") > 0) {
                return true;
            }
        }
        return false;
    }

    void processGrammar(GrammarInfo paramGrammarInfo) {
        HashMap localHashMap = (HashMap) this.mGrammarsToPathsMap.get(paramGrammarInfo.grammar);
        if (localHashMap == null) {
            localHashMap = Maps.newHashMap();
            this.mGrammarsToPathsMap.put(paramGrammarInfo.grammar, localHashMap);
        }
        localHashMap.put(paramGrammarInfo.revisionName, paramGrammarInfo.directory.getAbsolutePath());
    }

    boolean processLocaleData() {
        processLocaleSource();
        ArrayList localArrayList = getCompatiblePaths();
        if (localArrayList != null) {
            this.mResourcePaths = new ArrayList(localArrayList.size());
            Iterator localIterator1 = localArrayList.iterator();
            while (localIterator1.hasNext()) {
                File localFile2 = (File) localIterator1.next();
                this.mResourcePaths.add(localFile2.getAbsolutePath());
            }
            this.mConfigPaths = Maps.newHashMap();
            Iterator localIterator2 = this.mConfigToPathMap.keySet().iterator();
            while (localIterator2.hasNext()) {
                Greco3Mode localGreco3Mode = (Greco3Mode) localIterator2.next();
                Iterator localIterator4 = ((ArrayList) this.mConfigToPathMap.get(localGreco3Mode)).iterator();
                while (localIterator4.hasNext()) {
                    File localFile1 = (File) localIterator4.next();
                    if (this.mResourcePaths.contains(localFile1.getParentFile().getAbsolutePath())) {
                        if (this.mConfigPaths.containsKey(localGreco3Mode)) {
                            Log.w("VS.LocaleResourcesImpl", "Duplicate config file, found at: " + localFile1 + ", overwriting: " + (String) this.mConfigPaths.get(localGreco3Mode));
                        }
                        this.mConfigPaths.put(localGreco3Mode, localFile1.getAbsolutePath());
                    }
                }
            }
            if ((this.mMostRecentLanguagePack != null) && (this.mConfigPaths.size() > 0)) {
                Iterator localIterator3 = this.mUnprocessedGrammars.iterator();
                while (localIterator3.hasNext()) {
                    GrammarInfo localGrammarInfo = (GrammarInfo) localIterator3.next();
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

    static class GrammarInfo {
        final File directory;
        final Greco3Grammar grammar;
        final GstaticConfiguration.LanguagePack metadata;
        final String revisionName;

        GrammarInfo(Greco3Grammar paramGreco3Grammar, String paramString, File paramFile, GstaticConfiguration.LanguagePack paramLanguagePack) {
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