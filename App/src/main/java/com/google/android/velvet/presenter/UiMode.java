package com.google.android.velvet.presenter;

import com.google.android.search.shared.api.Query;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

public enum UiMode {
    static {
        SUGGEST = new UiMode("SUGGEST", 3);
        RESULTS = new UiMode("RESULTS", 4);
        RESULTS_SUGGEST = new UiMode("RESULTS_SUGGEST", 5);
        SUMMONS = new UiMode("SUMMONS", 6);
        CONNECTION_ERROR = new UiMode("CONNECTION_ERROR", 7);
        SUMMONS_SUGGEST = new UiMode("SUMMONS_SUGGEST", 8);
        SOUND_SEARCH = new UiMode("SOUND_SEARCH", 9);
        EXTERNAL = new UiMode("EXTERNAL", 10);
        VOICE_CORRECTION = new UiMode("VOICE_CORRECTION", 11);
        UiMode[] arrayOfUiMode = new UiMode[12];
        arrayOfUiMode[0] = NONE;
        arrayOfUiMode[1] = PREDICTIVE;
        arrayOfUiMode[2] = VOICESEARCH;
        arrayOfUiMode[3] = SUGGEST;
        arrayOfUiMode[4] = RESULTS;
        arrayOfUiMode[5] = RESULTS_SUGGEST;
        arrayOfUiMode[6] = SUMMONS;
        arrayOfUiMode[7] = CONNECTION_ERROR;
        arrayOfUiMode[8] = SUMMONS_SUGGEST;
        arrayOfUiMode[9] = SOUND_SEARCH;
        arrayOfUiMode[10] = EXTERNAL;
        arrayOfUiMode[11] = VOICE_CORRECTION;
        $VALUES = arrayOfUiMode;
    }

    private UiMode() {
    }

    public static UiMode fromSentinelQuery(Query paramQuery) {
        Preconditions.checkArgument(paramQuery.isSentinel());
        return (UiMode) paramQuery.getSentinelData();
    }

    public boolean canShowLocationOptIn() {
        return (isSuggestMode()) || (this == RESULTS_SUGGEST);
    }

    @Nullable
    public String getBackFragmentTag() {
        switch (1. $SwitchMap$com$google$android$velvet$presenter$UiMode[ordinal()])
        {
            default:
                return null;
            case 1:
            case 2:
            case 3:
                return "suggest";
            case 4:
            case 5:
            case 6:
                return "results";
            case 7:
                return "summons";
            case 8:
                return "error";
        }
        return "actiondiscovery";
    }

    @Nullable
    public String getFrontFragmentTag() {
        switch (1. $SwitchMap$com$google$android$velvet$presenter$UiMode[ordinal()])
        {
            case 5:
            case 7:
            case 8:
            default:
                return null;
            case 4:
                return "suggest";
            case 9:
                return "voicesearchlang";
        }
        return "voicecorrection";
    }

    public boolean isPredictiveMode() {
        return this == PREDICTIVE;
    }

    public boolean isSuggestMode() {
        return (this == SUGGEST) || (this == SUMMONS_SUGGEST);
    }

    public boolean isViewAndEditMode() {
        return (this == SUGGEST) || (this == SUMMONS_SUGGEST) || (this == SUMMONS);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.UiMode

 * JD-Core Version:    0.7.0.1

 */