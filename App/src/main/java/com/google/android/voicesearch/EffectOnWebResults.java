package com.google.android.voicesearch;

import com.google.majel.proto.PeanutProtos.Peanut;

public enum EffectOnWebResults {
    static {
        PREVENT = new EffectOnWebResults("PREVENT", 2);
        EffectOnWebResults[] arrayOfEffectOnWebResults = new EffectOnWebResults[3];
        arrayOfEffectOnWebResults[0] = NO_EFFECT;
        arrayOfEffectOnWebResults[1] = SUPPRESS;
        arrayOfEffectOnWebResults[2] = PREVENT;
        $VALUES = arrayOfEffectOnWebResults;
    }

    private EffectOnWebResults() {
    }

    public static EffectOnWebResults getPeanutEffect(PeanutProtos.Peanut paramPeanut) {
        if (paramPeanut.getSearchResultsUnnecessary()) {
            return SUPPRESS;
        }
        return NO_EFFECT;
    }

    public static EffectOnWebResults getPumpkinEffect() {
        return SUPPRESS;
    }

    public boolean shouldPreventWebResults() {
        return this == PREVENT;
    }

    public boolean shouldSuppressWebResults() {
        return this == SUPPRESS;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.EffectOnWebResults

 * JD-Core Version:    0.7.0.1

 */