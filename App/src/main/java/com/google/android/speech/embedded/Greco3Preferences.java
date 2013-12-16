package com.google.android.speech.embedded;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class Greco3Preferences {
    private static final Joiner.MapJoiner ACTIVE_DOWNLOADS_JOINER = Joiner.on(',').withKeyValueSeparator(":");
    private final SharedPreferences mSharedPreferences;

    public Greco3Preferences(SharedPreferences paramSharedPreferences) {
        this.mSharedPreferences = paramSharedPreferences;
    }

    public static HashMap<String, Long> buildHashMapFromDelimitedKeyValuePairs(String paramString) {
        HashMap localHashMap = Maps.newHashMap();
        String[] arrayOfString1 = paramString.split(",");
        int i = arrayOfString1.length;
        int j = 0;
        if (j < i) {
            String str = arrayOfString1[j].trim();
            if (TextUtils.isEmpty(str)) {
            }
            for (; ; ) {
                j++;
                break;
                String[] arrayOfString2 = str.split(":");
                if ((arrayOfString2.length != 2) || (TextUtils.isEmpty(arrayOfString2[0]))) {
                    Log.e("VS.G3Preferences", "Skipping bad value in active downloads setting pref :" + str);
                } else {
                    try {
                        Long localLong = Long.valueOf(Long.parseLong(arrayOfString2[1]));
                        localHashMap.put(arrayOfString2[0], localLong);
                    } catch (NumberFormatException localNumberFormatException) {
                        Log.e("VS.G3Preferences", "Skipping bad value in active downloads pref: " + str);
                    }
                }
            }
        }
        return localHashMap;
    }

    static String serializeHashMapToDelimitedKeyValuePairs(Map<String, Long> paramMap) {
        return ACTIVE_DOWNLOADS_JOINER.join(paramMap);
    }

    private void writeToSharedPrefs(String paramString1, String paramString2) {
        this.mSharedPreferences.edit().putString(paramString1, paramString2).apply();
    }

    public void addActiveDownload(String paramString, long paramLong) {
        HashMap localHashMap;
        try {
            localHashMap = getActiveDownloads();
            if (localHashMap.containsKey(paramString)) {
                throw new IllegalStateException("Attempt to add download :" + paramString + ", was already active.");
            }
        } finally {
        }
        localHashMap.put(paramString, Long.valueOf(paramLong));
        writeToSharedPrefs("g3_active_downloads", serializeHashMapToDelimitedKeyValuePairs(localHashMap));
    }

    public synchronized HashMap<String, Long> getActiveDownloads() {
            HashMap localHashMap = buildHashMapFromDelimitedKeyValuePairs(this.mSharedPreferences.getString("g3_active_downloads", ""));
            return localHashMap;
    }

    public String getCompiledGrammarRevisionId(Greco3Grammar paramGreco3Grammar) {
        String str = "g3_apk_grammar_revision_id_v1:" + paramGreco3Grammar.getDirectoryName();
        return this.mSharedPreferences.getString(str, null);
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener) {
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
    }

    /* Error */
    public void removeActiveDownload(String paramString) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: invokevirtual 125	com/google/android/speech/embedded/Greco3Preferences:getActiveDownloads	()Ljava/util/HashMap;
        //   6: astore_3
        //   7: aload_3
        //   8: aload_1
        //   9: invokevirtual 129	java/util/HashMap:containsKey	(Ljava/lang/Object;)Z
        //   12: ifne +31 -> 43
        //   15: ldc 60
        //   17: new 62	java/lang/StringBuilder
        //   20: dup
        //   21: invokespecial 63	java/lang/StringBuilder:<init>	()V
        //   24: ldc 168
        //   26: invokevirtual 69	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   29: aload_1
        //   30: invokevirtual 69	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   33: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   36: invokestatic 171	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
        //   39: pop
        //   40: aload_0
        //   41: monitorexit
        //   42: return
        //   43: aload_3
        //   44: aload_1
        //   45: invokevirtual 175	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
        //   48: pop
        //   49: aload_0
        //   50: ldc 140
        //   52: aload_3
        //   53: invokestatic 142	com/google/android/speech/embedded/Greco3Preferences:serializeHashMapToDelimitedKeyValuePairs	(Ljava/util/Map;)Ljava/lang/String;
        //   56: invokespecial 144	com/google/android/speech/embedded/Greco3Preferences:writeToSharedPrefs	(Ljava/lang/String;Ljava/lang/String;)V
        //   59: goto -19 -> 40
        //   62: astore_2
        //   63: aload_0
        //   64: monitorexit
        //   65: aload_2
        //   66: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	67	0	this	Greco3Preferences
        //   0	67	1	paramString	String
        //   62	4	2	localObject	Object
        //   6	47	3	localHashMap	HashMap
        // Exception table:
        //   from	to	target	type
        //   2	40	62	finally
        //   43	59	62	finally
    }

    public void setCompiledGrammarRevisionId(Greco3Grammar paramGreco3Grammar, String paramString) {
        String str = "g3_apk_grammar_revision_id_v1:" + paramGreco3Grammar.getDirectoryName();
        this.mSharedPreferences.edit().putString(str, paramString).apply();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.Greco3Preferences

 * JD-Core Version:    0.7.0.1

 */
