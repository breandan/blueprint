package com.embryo.android.speech.embedded;

import android.text.TextUtils;
import android.util.Log;

import com.embryo.protobuf.micro.CodedInputStreamMicro;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

class LocaleResourcesImpl
        implements Greco3DataManager.LocaleResources {
    private final HashMap<Greco3Mode, ArrayList<File>> mConfigToPathMap = Maps.newHashMap();
    private final HashMap<Greco3Grammar, HashMap<String, String>> mGrammarsToPathsMap = Maps.newHashMap();
    private final HashMap<File, String> mPathToHotwordPromptMap = Maps.newHashMap();
    private final HashMap<File, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack> mPathToMetadataMap = Maps.newHashMap();
    private final int[] mSupportedFormatVersions;
    private final ArrayList<GrammarInfo> mUnprocessedGrammars;
    private HashMap<Greco3Mode, String> mConfigPaths;
    private com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack mMostRecentLanguagePack;
    private ArrayList<String> mResourcePaths;
    private boolean mSystemPartition;

    LocaleResourcesImpl(int[] paramArrayOfInt) {
        this.mSupportedFormatVersions = paramArrayOfInt;
        this.mUnprocessedGrammars = new ArrayList();
    }

    private static com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack parseMetadata(File metadataFile) {
        com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack metadata = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(metadataFile);
            CodedInputStreamMicro cism = CodedInputStreamMicro.newInstance(fileInputStream);
            metadata = com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack.parseFrom(cism);
            Closeables.closeQuietly(fileInputStream);
        } catch (FileNotFoundException e) {
            Closeables.closeQuietly(fileInputStream);
        } catch (IOException e) {
            Closeables.closeQuietly(fileInputStream);
        }

        return metadata;
    }

    private ArrayList getCompatiblePaths() {
        if ((mMostRecentLanguagePack == null) || (!mMostRecentLanguagePack.hasLanguagePackId())) {
            return null;
        }

        ArrayList<File> compatible = new ArrayList(4);
        for (File path : mPathToMetadataMap.keySet()) {
            if (mMostRecentLanguagePack.getLanguagePackId().equals(mPathToMetadataMap.get(path).getLanguagePackId())) {
                compatible.add(path);
            }
        }

        return compatible;
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
        com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack localLanguagePack = parseMetadata(paramFile2);
        if (localLanguagePack != null) {
            this.mUnprocessedGrammars.add(new GrammarInfo(paramGreco3Grammar, paramString, paramFile1, localLanguagePack));
        }
    }

    void addHotwordPrompt(File file) {
        BufferedReader br = null;
        try {
            String prompt = br.readLine();
            if (!TextUtils.isEmpty(prompt)) {
                mPathToHotwordPromptMap.put(file.getParentFile(), prompt);
            }
        } catch (FileNotFoundException e) {
            Log.e("VS.LocaleResourcesImpl", "Could not open hotword prompt file.", e);
            return;
        } catch (IOException e) {
            Log.e("VS.LocaleResourcesImpl", "Could not read hotword prompt file.", e);
            return;
        } finally {
            Closeables.closeQuietly(br);
        }
    }

    void addMetadata(File file) {
        com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack metadata = parseMetadata(file);
        if (metadata == null) {
            Log.e("VS.LocaleResourcesImpl", "Unparsable metadata : " + file);
            return;
        }
        mPathToMetadataMap.put(file.getParentFile(), metadata);
        if (LanguagePackUtils.isCompatible(metadata, mSupportedFormatVersions, 0x7fffffff)) {
            if ((mMostRecentLanguagePack == null) || (metadata.getVersion() > mMostRecentLanguagePack.getVersion())) {
                mMostRecentLanguagePack = metadata;
            }
        }
    }

    public String getConfigFile(Greco3Mode paramGreco3Mode) {
        return this.mConfigPaths.get(paramGreco3Mode);
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
                String str = this.mPathToHotwordPromptMap.get(localFile);
                if (str != null) {
                    return str;
                }
            }
        }
        return null;
    }

    public com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack getLanguageMetadata() {
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
                            Log.w("VS.LocaleResourcesImpl", "Duplicate config file, found at: " + localFile1 + ", overwriting: " + this.mConfigPaths.get(localGreco3Mode));
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
        final com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack metadata;
        final String revisionName;

        GrammarInfo(Greco3Grammar paramGreco3Grammar, String paramString, File paramFile, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack paramLanguagePack) {
            this.grammar = paramGreco3Grammar;
            this.revisionName = paramString;
            this.directory = paramFile;
            this.metadata = paramLanguagePack;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     LocaleResourcesImpl

 * JD-Core Version:    0.7.0.1

 */