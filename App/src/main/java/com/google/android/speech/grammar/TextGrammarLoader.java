package com.google.android.speech.grammar;

import android.content.res.Resources;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.annotation.Nullable;

public class TextGrammarLoader {
    private final String mPackageName;
    private final Resources mResources;

    public TextGrammarLoader(Resources paramResources, String paramString) {
        this.mResources = paramResources;
        this.mPackageName = paramString;
    }

    public StringBuilder get(String name, int capacity) throws IOException {
        int contactGrammarId = mResources.getIdentifier(name, "raw", mPackageName);
        StringBuilder stringBuilder = new StringBuilder(capacity);
        if(contactGrammarId != 0) {
            InputStreamReader reader = new InputStreamReader(mResources.openRawResource(contactGrammarId));
            CharStreams.copy(reader, stringBuilder);
            Closeables.closeQuietly(reader);
            return stringBuilder;
        }
        return null;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.grammar.TextGrammarLoader

 * JD-Core Version:    0.7.0.1

 */