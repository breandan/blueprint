package com.google.android.speech.grammar;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.util.Map;

import javax.annotation.Nonnull;

public class GrammarContact {
    private static final Joiner SPACE_JOINER = Joiner.on(" ");
    @Nonnull
    final String displayName;
    final long lastTimeContactedElapsed;
    final int timesContacted;
    double weight;

    public GrammarContact(@Nonnull String paramString, int paramInt, long paramLong) {
        this.displayName = Preconditions.checkNotNull(paramString);
        this.timesContacted = paramInt;
        this.lastTimeContactedElapsed = paramLong;
    }

    private void addToken(String paramString, double paramDouble, Map<String, GrammarToken> paramMap) {
        if (paramMap.containsKey(paramString)) {
            paramMap.get(paramString).add(paramDouble);
            return;
        }
        paramMap.put(paramString, new GrammarToken(paramString, paramDouble));
    }

    public void addTokens(Map<String, GrammarToken> tokens) {
        String escapedNamed = GrammarBuilder.stripAbnfTokens(displayName);
        String[] words = GrammarBuilder.getWords(escapedNamed);
        if ((words == null) || (words.length == 0)) {
            return;
        }
        addToken(words[0x0], weight, tokens);
        if (words.length > 0x1) {
            addToken(words[(words.length - 0x1)], weight, tokens);
            addToken(SPACE_JOINER.join(words), weight, tokens);
        }
    }

    public String toString() {
        return "GrammarContact[" + this.displayName + ",#" + this.timesContacted + ",last-time=" + this.lastTimeContactedElapsed + ",weigth=" + this.weight + "]";
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.grammar.GrammarContact

 * JD-Core Version:    0.7.0.1

 */