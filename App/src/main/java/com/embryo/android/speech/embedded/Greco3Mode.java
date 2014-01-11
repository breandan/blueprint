package com.embryo.android.speech.embedded;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public enum Greco3Mode {
    DICTATION,
    ENDPOINTER_VOICESEARCH,
    ENDPOINTER_DICTATION,
    HOTWORD,
    COMPILER,
    GRAMMAR;
    private static final Map<String, Greco3Mode> MODE_MAP;

    static {
        HashMap localHashMap = Maps.newHashMap();
        localHashMap.put("dictation", DICTATION);
        localHashMap.put("endpointer_voicesearch", ENDPOINTER_VOICESEARCH);
        localHashMap.put("endpointer_dictation", ENDPOINTER_DICTATION);
        localHashMap.put("google_hotword", HOTWORD);
        localHashMap.put("hotword", HOTWORD);
        localHashMap.put("compile_grammar", COMPILER);
        localHashMap.put("grammar", GRAMMAR);
        MODE_MAP = localHashMap;
    }

    private Greco3Mode() {
    }

    private static String getFileName(File paramFile) {
        String str = paramFile.getName();
        int i = str.indexOf(".");
        if (i > 0) {
            str = str.substring(0, i);
        }
        return str;
    }

    public static boolean isAsciiConfiguration(File paramFile) {
        return paramFile.getName().endsWith(".ascii_proto");
    }

    public static Greco3Mode valueOf(File paramFile) {
        String str = getFileName(paramFile);
        return MODE_MAP.get(str);
    }

    public boolean isEndpointerMode() {
        return (this == ENDPOINTER_DICTATION) || (this == ENDPOINTER_VOICESEARCH);
    }
}
