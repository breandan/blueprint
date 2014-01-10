package com.embryo.android.speech.internal;


import com.embryo.android.speech.embedded.Greco3Grammar;
import com.embryo.android.speech.embedded.Greco3Mode;
import com.embryo.android.speech.embedded.Greco3ModeSelector;

public class DefaultModeSelector
        implements Greco3ModeSelector {
    public Greco3Mode getFallbackMode(Greco3Mode paramGreco3Mode, Greco3Grammar paramGreco3Grammar) {
        if (paramGreco3Mode == Greco3Mode.DICTATION) {
            return Greco3Mode.ENDPOINTER_DICTATION;
        }
        if (paramGreco3Mode == Greco3Mode.GRAMMAR) {
            return Greco3Mode.ENDPOINTER_VOICESEARCH;
        }
        return null;
    }

    public Greco3Mode getMode(Greco3Mode paramGreco3Mode, Greco3Grammar paramGreco3Grammar) {
        Greco3Mode localGreco3Mode = paramGreco3Mode;
        if (localGreco3Mode == Greco3Mode.GRAMMAR) {
            localGreco3Mode = Greco3Mode.ENDPOINTER_VOICESEARCH;
        }
        return localGreco3Mode;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     DefaultModeSelector

 * JD-Core Version:    0.7.0.1

 */