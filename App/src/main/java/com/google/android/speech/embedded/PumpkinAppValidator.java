package com.google.android.speech.embedded;

import com.google.android.voicesearch.util.AppSelectionHelper;
import com.google.speech.grammar.pumpkin.Validator;

public class PumpkinAppValidator
        extends Validator {
    private final AppSelectionHelper mAppSelectionHelper;

    public PumpkinAppValidator(AppSelectionHelper paramAppSelectionHelper) {
        this.mAppSelectionHelper = paramAppSelectionHelper;
    }

    public float getPosterior(String paramString) {
        if (this.mAppSelectionHelper.findActivities(paramString).size() > 0) {
            return 1.0F;
        }
        return 0.0F;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.PumpkinAppValidator

 * JD-Core Version:    0.7.0.1

 */