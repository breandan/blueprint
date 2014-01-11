package com.embryo.android.speech.embedded;

import android.content.SharedPreferences;

import com.embryo.common.base.Joiner;
import com.embryo.common.collect.Maps;

public class Greco3Preferences {
    private static final Joiner.MapJoiner ACTIVE_DOWNLOADS_JOINER = Joiner.on(',').withKeyValueSeparator(":");
    private final SharedPreferences mSharedPreferences;

    public Greco3Preferences(SharedPreferences paramSharedPreferences) {
        this.mSharedPreferences = paramSharedPreferences;
    }

    public String getCompiledGrammarRevisionId(Greco3Grammar paramGreco3Grammar) {
        String str = "g3_apk_grammar_revision_id_v1:" + paramGreco3Grammar.getDirectoryName();
        return this.mSharedPreferences.getString(str, null);
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3Preferences

 * JD-Core Version:    0.7.0.1

 */
