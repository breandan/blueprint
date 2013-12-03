package com.google.android.speech.grammar;

import android.util.Base64;

import java.util.Formatter;

class GrammarToken {
    private static final Formatter sFormatter = new Formatter(sSb);
    private static final StringBuilder sSb = new StringBuilder();
    private final String token;
    private double weight;

    public GrammarToken(String paramString, double paramDouble) {
        this.token = paramString;
        this.weight = paramDouble;
    }

    private static String encode(String paramString) {
        return Base64.encodeToString(paramString.getBytes(), 11);
    }

    public static String getEncodeWeight(double paramDouble) {
        sSb.setLength(0);
        Formatter localFormatter = sFormatter;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Double.valueOf(paramDouble);
        localFormatter.format("%f", arrayOfObject);
        return sSb.toString();
    }

    public void add(double paramDouble) {
        this.weight = (paramDouble + this.weight);
    }

    public void append(StringBuilder paramStringBuilder) {
        String str = getEncodeWeight(this.weight);
        paramStringBuilder.append("(");
        paramStringBuilder.append("/");
        paramStringBuilder.append(str);
        paramStringBuilder.append("/ ");
        paramStringBuilder.append(this.token);
        paramStringBuilder.append(" {");
        paramStringBuilder.append("XX_");
        paramStringBuilder.append(str.replace('.', 'P'));
        paramStringBuilder.append("_");
        paramStringBuilder.append(encode(this.token));
        paramStringBuilder.append("})");
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.grammar.GrammarToken

 * JD-Core Version:    0.7.0.1

 */