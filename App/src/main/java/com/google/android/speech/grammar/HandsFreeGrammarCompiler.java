package com.google.android.speech.grammar;

import android.util.Log;

import com.google.android.shared.util.StopWatch;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.embedded.Greco3GrammarCompiler;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HandsFreeGrammarCompiler {
    private final Greco3DataManager mGreco3DataManager;
    private final Greco3Grammar mGreco3Grammar;
    private final TextGrammarLoader mTextGrammarLoader;

    public HandsFreeGrammarCompiler(Greco3DataManager paramGreco3DataManager, TextGrammarLoader paramTextGrammarLoader, Greco3Grammar paramGreco3Grammar) {
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mTextGrammarLoader = paramTextGrammarLoader;
        this.mGreco3Grammar = paramGreco3Grammar;
    }

    private String buildAbnfGrammar(Greco3DataManager.LocaleResources paramLocaleResources) {
        String str = paramLocaleResources.getLanguageMetadata().getBcp47Locale();
        if (!"en-US".equals(str)) {
            return null;
        }
        StringBuilder localStringBuilder;
        try {
            localStringBuilder = this.mTextGrammarLoader.get(this.mGreco3Grammar.getApkFullName(str), 524288);
            if (localStringBuilder != null) {
                if (this.mGreco3Grammar.isAddContacts()) {
                    List localList = new LinkedList<GrammarContact>();
                    EventLogger.recordSpeechEvent(14, Integer.valueOf(localList.size()));
                    ContactsGrammarBuilder localContactsGrammarBuilder = new ContactsGrammarBuilder();
                    Iterator localIterator = localList.iterator();
                    while (localIterator.hasNext()) {
                        localContactsGrammarBuilder.addContact((GrammarContact) localIterator.next());
                    }
                    localContactsGrammarBuilder.append(localStringBuilder);
                }
            }
            return localStringBuilder.toString();
        } catch (IOException localIOException) {
            Log.e("VS.G3ContactsCompiler", "I/O Exception reading ABNF grammar: ", localIOException);
            return null;
        }
    }

    private boolean compileGrammar(Greco3DataManager.LocaleResources paramLocaleResources, String paramString, File paramFile1, File paramFile2) {
        Greco3GrammarCompiler localGreco3GrammarCompiler = new Greco3GrammarCompiler(paramLocaleResources.getConfigFile(Greco3Mode.COMPILER), paramLocaleResources.getResourcePaths());
        if (!localGreco3GrammarCompiler.init()) {
            return false;
        }
        boolean bool = localGreco3GrammarCompiler.compile(paramString, paramFile1.getAbsolutePath(), paramFile2.getAbsolutePath());
        localGreco3GrammarCompiler.delete();
        return bool;
    }

    public String buildGrammar(String paramString) {
        Preconditions.checkState(this.mGreco3DataManager.isInitialized());
        Greco3DataManager.LocaleResources localLocaleResources = this.mGreco3DataManager.getResources(paramString);
        if (localLocaleResources == null) {
            Log.e("VS.G3ContactsCompiler", "Grammar compilation failed, no resources for locale :" + paramString);
            return null;
        }
        return buildAbnfGrammar(localLocaleResources);
    }

    public synchronized boolean compileGrammar(String grammar, String bcp47Locale, File outputPath, File cachePath) {
        Preconditions.checkState(mGreco3DataManager.isInitialized());
        StopWatch sw = new StopWatch();
        sw.start();
        Greco3DataManager.LocaleResources resources = mGreco3DataManager.getResources(bcp47Locale);
        if (resources == null) {
            Log.e("VS.G3ContactsCompiler", "Grammar compilation failed, no resources for locale :" + bcp47Locale);
            return false;
        }
        if (grammar != null) {
            sw.start();
            boolean compiled = compileGrammar(resources, grammar, outputPath, cachePath);
            if (compiled) {
                Log.i("VS.G3ContactsCompiler", "Compiled grammar : " + sw.getElapsedTime() + " ms");
            }
            return true;
        }

        return false;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.grammar.HandsFreeGrammarCompiler

 * JD-Core Version:    0.7.0.1

 */