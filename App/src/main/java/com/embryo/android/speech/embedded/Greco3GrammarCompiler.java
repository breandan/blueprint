package com.embryo.android.speech.embedded;

import android.os.SystemClock;
import android.util.Log;

import com.embryo.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Greco3GrammarCompiler {
    public static int NUM_GENERATED_FILES = 4;
    private GrammarCompilerImpl mCompiler;
    private final String mConfigFile;
    private final String[] mSearchPaths;

    public Greco3GrammarCompiler(String paramString, List<String> paramList) {
        this.mConfigFile = paramString;
        this.mSearchPaths = new String[paramList.size()];
        paramList.toArray(this.mSearchPaths);
    }

    public boolean compile(String paramString1, String paramString2, String paramString3) {
        long l = SystemClock.elapsedRealtime();
        if (!this.mCompiler.readCache(paramString3)) {
            Log.e("G3GrammarCompiler", "Error reading cache file: " + paramString3);
        }
        if (!this.mCompiler.compileAbnf(paramString1)) {
        }
        while ((!this.mCompiler.writeClgFst(paramString2 + "/grammar_clg", paramString2 + "/grammar_symbols")) || (!this.mCompiler.writeSemanticFst(paramString2 + "/semantic_fst", paramString2 + "/semantic_symbols"))) {
            return false;
        }
        if (!this.mCompiler.writeCache(paramString3, true)) {
            Log.e("G3GrammarCompiler", "Error writing cache to: " + paramString3);
            return true;
        }
        Log.i("G3GrammarCompiler", "Compilation complete, time = " + (float) (SystemClock.elapsedRealtime() - l) / 1000.0F + " s");
        return true;
    }

    public void delete() {
        this.mCompiler.delete();
    }

    public boolean init() {
        this.mCompiler = new GrammarCompilerImpl();
        try {
            if (Greco3Mode.isAsciiConfiguration(new File(this.mConfigFile))) {
                return this.mCompiler.initFromFile(this.mConfigFile, this.mSearchPaths);
            }
            byte[] arrayOfByte = Files.toByteArray(new File(this.mConfigFile));
            boolean bool = this.mCompiler.initFromProto(arrayOfByte, this.mSearchPaths);
            return bool;
        } catch (IOException localIOException) {
            Log.w("G3GrammarCompiler", "I/O Exception reading binary config file: " + localIOException);
        }
        return false;
    }

    private static class GrammarCompilerImpl
            extends com.embryo.speech.grammar.AbstractGrammarCompiler {
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3GrammarCompiler

 * JD-Core Version:    0.7.0.1

 */