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
            if (!TextUtils.isEmpty(str)) {
                for (; ; ) {
                    j++;
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

    public synchronized void removeActiveDownload(String bcp47Locale) {
        HashMap<String, Long> active = getActiveDownloads();
        if(!active.containsKey(bcp47Locale)) {
            Log.w("VS.G3Preferences", "Attempt to remove non-existent download" + bcp47Locale);
            return;
        }
        active.remove(bcp47Locale);
        writeToSharedPrefs("g3_active_downloads", serializeHashMapToDelimitedKeyValuePairs(active));
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
