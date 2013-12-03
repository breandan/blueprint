package com.google.android.voicesearch.ime.formatter;

import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputConnection;

import com.google.android.speech.alternates.Hypothesis;

public abstract interface TextFormatter {
    public abstract Hypothesis format(Hypothesis paramHypothesis);

    public abstract String format(String paramString);

    public abstract void handleCommit(InputConnection paramInputConnection, ExtractedText paramExtractedText);

    public abstract void reset();

    public abstract void startDictation(ExtractedText paramExtractedText);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ime.formatter.TextFormatter

 * JD-Core Version:    0.7.0.1

 */