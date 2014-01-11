/**
 * Generated by smali2java 1.0.0.558
 * Copyright (C) 2013 Hensence.com
 */

package com.embryo.android.speech.alternates;

import android.app.PendingIntent;
import android.content.Context;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.EasyEditSpan;
import android.text.style.SuggestionSpan;

import com.embryo.common.base.Preconditions;

import java.lang.reflect.Constructor;

public class HypothesisToSuggestionSpansConverter {
    private final Context mApplicationContext;
    private final Constructor<EasyEditSpan> mEasyEditSpanConstructor;
    private int mSuggestionCounter = 0x0;

    public HypothesisToSuggestionSpansConverter(Context applicationContext) {
        mApplicationContext = Preconditions.checkNotNull(applicationContext);
        mEasyEditSpanConstructor = getEasyEditSpanConstructor();
    }

    private static Constructor getEasyEditSpanConstructor() {
        try {
            return EasyEditSpan.class.getConstructor(new Class[]{PendingIntent.class});
        } catch (NoSuchMethodException localNoSuchMethodException1) {
            return null;
        }
    }

    public SpannableString getSuggestionSpannableString(String requestId, int segmentId, com.embryo.android.speech.alternates.Hypothesis text) {
        SpannableString output = getSuggestionSpan(requestId, segmentId, text);
        EasyEditSpan easyEditSpan = createEasyEditSpan(mApplicationContext, requestId, segmentId);
        output.setSpan(easyEditSpan, 0x0, output.length(), 0x21);
        return output;
    }

    public SpannedString getSuggestionSpannedStringForQuery(String requestId, com.embryo.android.speech.alternates.Hypothesis hypothesis) {
        SpannableString spannable = getSuggestionSpan(requestId, 0x0, hypothesis);
        return SpannedString.valueOf(spannable);
    }

    private SpannableString getSuggestionSpan(String requestId, int segmentId, com.embryo.android.speech.alternates.Hypothesis hypothesis) {
        SpannableString suggestionsSpannedText = new SpannableString(hypothesis.getText());
        if (requestId != null) {
            for (Hypothesis.Span span : hypothesis.getSpans()) {
                String[] alternatives = new String[span.mAlternates.size()];
                span.mAlternates.toArray(alternatives);
                SuggestionSpan suggestionSpan = new SuggestionSpan(mApplicationContext, null, alternatives, 0x1, SuggestionSpanBroadcastReceiver.class);
                suggestionsSpannedText.setSpan(suggestionSpan, span.mUtf16Start, span.mUtf16End, 0x21);
            }
        }
        return suggestionsSpannedText;
    }

    private EasyEditSpan createEasyEditSpan(Context paramContext, String paramString, int paramInt) {
        return null;
        //TODO

    }
}
