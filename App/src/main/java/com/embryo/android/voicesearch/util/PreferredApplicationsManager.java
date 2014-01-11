package com.embryo.android.voicesearch.util;

import android.content.SharedPreferences;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class PreferredApplicationsManager {
    private static final Map<String, String> MIME_TYPE_PREFERENCE_MAP = ImmutableMap.of("audio/music", "action_play_music_sticky_app", "video/movie", "action_play_movie_sticky_app", "text/book", "action_open_book_sticky_app");
    private SharedPreferences mSharedPreferences;

    public PreferredApplicationsManager(SharedPreferences paramSharedPreferences) {
        this.mSharedPreferences = paramSharedPreferences;
    }

    public String getPreferredApplication(String paramString) {
        String str = MIME_TYPE_PREFERENCE_MAP.get(paramString);
        if (str != null) {
            return this.mSharedPreferences.getString(str, null);
        }
        return "";
    }

    public void setPreferredApplication(String paramString1, String paramString2) {
        String str = MIME_TYPE_PREFERENCE_MAP.get(paramString1);
        if (str != null) {
            this.mSharedPreferences.edit().putString(str, paramString2).apply();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     PreferredApplicationsManager

 * JD-Core Version:    0.7.0.1

 */