package com.google.android.speech.grammar;

import android.util.Base64;

import com.google.android.shared.util.SplitIterator;
import com.google.common.collect.Iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

public abstract class GrammarBuilder {
    private static final Pattern ABNF_RESERVED_TOKENS = Pattern.compile("[\\Q/|*+?=;[]()<>${}\"\\\\E]");

    public static String decodeName(String paramString) {
        return new String(Base64.decode(paramString.substring(1 + paramString.indexOf("_", "XX_".length())), 11));
    }

    public static double decodeWeight(String paramString) {
        int i = paramString.indexOf("_", "XX_".length());
        return Double.parseDouble(paramString.substring("XX_".length(), i).replace('P', '.'));
    }

    static String[] getWords(String paramString) {
        return (String[]) Iterators.toArray(SplitIterator.splitOnWhitespaceOmitEmptyStrings(stripAbnfTokens(paramString)), String.class);
    }

    static String stripAbnfTokens(String paramString) {
        return ABNF_RESERVED_TOKENS.matcher(paramString).replaceAll("");
    }

    public void append(StringBuilder paramStringBuilder) {
        Collection localCollection = getGrammarTokens();
        if (localCollection.isEmpty()) {
            appendEmptyTokensRules(paramStringBuilder);
            return;
        }
        appendBeforeDisjunctionRules(paramStringBuilder);
        appendDisjunctionAssignment(paramStringBuilder);
        int i = 1;
        Iterator localIterator = localCollection.iterator();
        while (localIterator.hasNext()) {
            GrammarToken localGrammarToken = (GrammarToken) localIterator.next();
            if (i == 0) {
                paramStringBuilder.append(" | ");
            }
            localGrammarToken.append(paramStringBuilder);
            i = 0;
        }
        paramStringBuilder.append(";\n");
    }

    protected abstract void appendBeforeDisjunctionRules(StringBuilder paramStringBuilder);

    protected abstract void appendDisjunctionAssignment(StringBuilder paramStringBuilder);

    protected abstract void appendEmptyTokensRules(StringBuilder paramStringBuilder);

    protected abstract Collection<GrammarToken> getGrammarTokens();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.grammar.GrammarBuilder

 * JD-Core Version:    0.7.0.1

 */