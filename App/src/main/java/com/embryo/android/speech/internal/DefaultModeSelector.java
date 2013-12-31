package com.embryo.android.speech.internal;

import com.embryo.android.speech.embedded.Greco3Grammar;
import com.embryo.android.speech.embedded.Greco3ModeSelector;

public class DefaultModeSelector
        implements Greco3ModeSelector {
    private final boolean mIsTelephoneCapable;

    public DefaultModeSelector(boolean paramBoolean) {
        this.mIsTelephoneCapable = paramBoolean;
    }

    public com.embryo.android.speech.embedded.Greco3Mode getFallbackMode(com.embryo.android.speech.embedded.Greco3Mode paramGreco3Mode, Greco3Grammar paramGreco3Grammar) {
        if (paramGreco3Mode == com.embryo.android.speech.embedded.Greco3Mode.DICTATION) {
            return com.embryo.android.speech.embedded.Greco3Mode.ENDPOINTER_DICTATION;
        }
        if (paramGreco3Mode == com.embryo.android.speech.embedded.Greco3Mode.GRAMMAR) {
            return com.embryo.android.speech.embedded.Greco3Mode.ENDPOINTER_VOICESEARCH;
        }
        return null;
    }

    public com.embryo.android.speech.embedded.Greco3Mode getMode(com.embryo.android.speech.embedded.Greco3Mode paramGreco3Mode, Greco3Grammar paramGreco3Grammar) {
        com.embryo.android.speech.embedded.Greco3Mode localGreco3Mode = paramGreco3Mode;
        if ((!this.mIsTelephoneCapable) && (localGreco3Mode == com.embryo.android.speech.embedded.Greco3Mode.GRAMMAR)) {
            localGreco3Mode = com.embryo.android.speech.embedded.Greco3Mode.ENDPOINTER_VOICESEARCH;
        }
        return localGreco3Mode;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     DefaultModeSelector

 * JD-Core Version:    0.7.0.1

 */