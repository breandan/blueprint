package com.google.android.speech.embedded;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public enum Greco3Mode
{
  private static final Map<String, Greco3Mode> MODE_MAP;
  
  static
  {
    ENDPOINTER_DICTATION = new Greco3Mode("ENDPOINTER_DICTATION", 2);
    HOTWORD = new Greco3Mode("HOTWORD", 3);
    COMPILER = new Greco3Mode("COMPILER", 4);
    GRAMMAR = new Greco3Mode("GRAMMAR", 5);
    Greco3Mode[] arrayOfGreco3Mode = new Greco3Mode[6];
    arrayOfGreco3Mode[0] = DICTATION;
    arrayOfGreco3Mode[1] = ENDPOINTER_VOICESEARCH;
    arrayOfGreco3Mode[2] = ENDPOINTER_DICTATION;
    arrayOfGreco3Mode[3] = HOTWORD;
    arrayOfGreco3Mode[4] = COMPILER;
    arrayOfGreco3Mode[5] = GRAMMAR;
    $VALUES = arrayOfGreco3Mode;
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
  
  private Greco3Mode() {}
  
  private static String getFileName(File paramFile)
  {
    String str = paramFile.getName();
    int i = str.indexOf(".");
    if (i > 0) {
      str = str.substring(0, i);
    }
    return str;
  }
  
  public static boolean isAsciiConfiguration(File paramFile)
  {
    return paramFile.getName().endsWith(".ascii_proto");
  }
  
  public static Greco3Mode valueOf(File paramFile)
  {
    String str = getFileName(paramFile);
    return (Greco3Mode)MODE_MAP.get(str);
  }
  
  public boolean isEndpointerMode()
  {
    return (this == ENDPOINTER_DICTATION) || (this == ENDPOINTER_VOICESEARCH);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.embedded.Greco3Mode
 * JD-Core Version:    0.7.0.1
 */