package com.embryo.speech.grammar.pumpkin;

public abstract class Validator {
    public String canonicalizeArgument(String paramString) {
        return paramString;
    }

    public abstract float getPosterior(String paramString);

    public boolean init() {
        return true;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Validator

 * JD-Core Version:    0.7.0.1

 */